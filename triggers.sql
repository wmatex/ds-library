CREATE OR REPLACE FUNCTION generate_bar_code() RETURNS TRIGGER AS $_$
BEGIN
  NEW.car_kod := to_char(NEW.id_vytisk, 'FM0000000000000');
  RETURN NEW;
END;
$_$ LANGUAGE plpgsql;

CREATE TRIGGER gen_bar_code BEFORE INSERT ON vytisk
FOR EACH ROW EXECUTE PROCEDURE generate_bar_code();

-- Vsechny tituly s poctem volnych vytisku
CREATE OR REPLACE VIEW vw_titul AS
SELECT t.id_titul, t.nazev, t.rok_vydani, t.vypujcni_doba_dny, t.cena, 
z.nazev as zanr, v2.cnt - COALESCE(v.cnt, 0) as volne_vytisky
FROM titul t
INNER JOIN zanr z ON z.id_zanr = t.id_zanr
LEFT OUTER JOIN (
  SELECT vk.id_titul, COUNT(vp.*) as cnt
  FROM vytisk vk
  INNER JOIN vypujcka vp ON vk.id_vytisk = vp.id_vytisk
  WHERE vp.je_vraceno = FALSE
  GROUP BY vk.id_titul
) v ON v.id_titul = t.id_titul
INNER JOIN (
  SELECT id_titul, COUNT(*) as cnt
  FROM vytisk
  GROUP BY id_titul
) v2 ON v2.id_titul = t.id_titul
ORDER BY t.id_titul ASC;

CREATE OR REPLACE FUNCTION vytvor_rezervaci(
  _id_uzivatel int, _id_titul int, _interval interval) RETURNS VOID AS $_$
DECLARE
  _int interval;
  count int;
BEGIN
  SELECT volne_vytisky INTO count FROM vw_titul
  WHERE id_titul = _id_titul;
  if count > 0 then
    RAISE EXCEPTION 'Existuje volny vytisk';
  end if;
  if _interval is null then
    _int := '1 year';
  else
    _int := _interval;
  end if;
  INSERT INTO rezervace (id_uzivatel, id_titul, zajem_do)
  VALUES (_id_uzivatel, _id_titul, current_date + _int);
END;
$_$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION spln_rezervaci(
  _id_uzivatel int, _id_titul int) RETURNS VOID AS $_$
BEGIN
  UPDATE rezervace SET splnena = TRUE, vyprsi = current_date + interval '7 days'
  WHERE id_uzivatel = _id_uzivatel AND id_titul = _id_titul;
END;
$_$ LANGUAGE plpgsql;

-- Nevracene vypujcky
CREATE OR REPLACE VIEW vw_vypujcka
SELECT u.id_uzivatel, u.krestni_jmeno, u.prijmeni, u.email, t.id_titul,
t.nazev, vp.id_vypujcka, vp.datum_pujceni, vp.datum_vraceni, vp.je_vraceno
FROM vypujcka vp
INNER JOIN vytisk vt ON vp.id_vytisk = vt.id_vytisk
INNER JOIN titul t ON t.id_titul = vt.id_titul
INNER JOIN uzivatel u ON u.id_uzivatel = vp.id_uzivatel
WHERE vp.je_vraceno = FALSE
ORDER BY vp.datum_vraceni DESC;
