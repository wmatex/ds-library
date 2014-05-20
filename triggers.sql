-- 8.3 nema array_agg
CREATE AGGREGATE array_agg(anyelement) (
  SFUNC=array_append,
  STYPE=anyarray,
  INITCOND='{}'
);

CREATE OR REPLACE FUNCTION generate_bar_code() RETURNS TRIGGER AS $_$
BEGIN
  NEW.car_kod := to_char(NEW.id_vytisk, 'FM0000000000000');
  RETURN NEW;
END;
$_$ LANGUAGE plpgsql;

CREATE TRIGGER gen_bar_code BEFORE INSERT ON vytisk
FOR EACH ROW EXECUTE PROCEDURE generate_bar_code();

CREATE OR REPLACE FUNCTION check_vypujcka() RETURNS TRIGGER AS $_$
DECLARE
  _cnt integer;
BEGIN
  SELECT COUNT(*) INTO _cnt FROM vypujcka
  WHERE id_vytisk = NEW.id_vytisk AND
  je_vraceno = FALSE;
  if _cnt > 0 then
    RAISE EXCEPTION 'Tento titul je již půjčený';
  end if;

  RETURN NEW;
END;
$_$ LANGUAGE plpgsql;
CREATE TRIGGER check_vypujcka_trg BEFORE INSERT ON vypujcka
FOR EACH ROW EXECUTE PROCEDURE check_vypujcka();

-- Nahodne vygenerovane heslo
CREATE OR REPLACE FUNCTION nahodne_heslo(length integer) RETURNS text AS $_$
DECLARE
  chars text[] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
  result text := '';
  i integer := 0;
BEGIN
  FOR i in 1..length LOOP
    result := result || chars[1+random()*(array_length(chars, 1)-1)];
  END LOOP;
  RETURN result;
END;
$_$ LANGUAGE plpgsql;

-- Vytvor noveho uzivatele
CREATE OR REPLACE FUNCTION novy_uzivatel(_jmeno text, _prijmeni text, _email text, OUT _heslo text) RETURNS text AS $_$
BEGIN
  INSERT INTO uzivatel (krestni_jmeno, prijmeni, email)
  VALUES (_jmeno, _prijmeni, _email);
  _heslo := nahodne_heslo(5);
  UPDATE uzivatel SET heslo = MD5(_heslo) WHERE email = _email;
END;
$_$ LANGUAGE plpgsql;

-- Vsechny tituly s poctem volnych vytisku
CREATE OR REPLACE VIEW vw_titul AS
SELECT t.id_titul, t.nazev, t.rok_vydani, t.vypujcni_doba_dny, t.cena, 
z.nazev as zanr, COALESCE(v2.cnt, 0) - COALESCE(v.cnt, 0) as volne_vytisky
FROM titul t
INNER JOIN zanr z ON z.id_zanr = t.id_zanr
LEFT OUTER JOIN (
  SELECT vk.id_titul, COUNT(vp.*) as cnt
  FROM vytisk vk
  INNER JOIN vypujcka vp ON vk.id_vytisk = vp.id_vytisk
  WHERE vp.je_vraceno = FALSE
  GROUP BY vk.id_titul
) v ON v.id_titul = t.id_titul
LEFT OUTER JOIN (
  SELECT id_titul, COUNT(*) as cnt
  FROM vytisk
  GROUP BY id_titul
) v2 ON v2.id_titul = t.id_titul
ORDER BY t.id_titul ASC;

CREATE OR REPLACE RULE vw_titul_insert_rule AS ON INSERT TO vw_titul
DO INSTEAD (
  INSERT INTO titul (nazev, rok_vydani, vypujcni_doba_dny, cena, id_zanr)
  VALUES (NEW.nazev, NEW.rok_vydani, NEW.vypujcni_doba_dny, NEW.cena,
      (SELECT id_zanr FROM zanr WHERE zanr.nazev = NEW.zanr LIMIT 1)
    )
  );

CREATE OR REPLACE RULE vw_titul_update_rule AS ON UPDATE TO vw_titul
DO INSTEAD (
  UPDATE titul SET
  nazev = NEW.nazev, rok_vydani = NEW.rok_vydani,
  vypujcni_doba_dny = NEW.vypujcni_doba_dny, cena = NEW.cena,
  id_zanr = (SELECT id_zanr FROM zanr WHERE zanr.nazev = NEW.zanr LIMIT 1)
  WHERE id_titul = NEW.id_titul
);

CREATE OR REPLACE RULE vw_titul_delete_rule AS ON DELETE TO vw_titul
DO INSTEAD (
  DELETE FROM titul WHERE id_titul = OLD.id_titul
);

-- Vsechny vypujcky
CREATE OR REPLACE VIEW vw_vypujcka AS
SELECT u.id_uzivatel, u.krestni_jmeno, u.prijmeni, u.email, t.id_titul,
vt.id_vytisk, t.nazev, vp.id_vypujcka, vp.datum_pujceni, vp.datum_vraceni, vp.je_vraceno
FROM vypujcka vp
INNER JOIN vytisk vt ON vp.id_vytisk = vt.id_vytisk
INNER JOIN titul t ON t.id_titul = vt.id_titul
INNER JOIN uzivatel u ON u.id_uzivatel = vp.id_uzivatel
ORDER BY vp.je_vraceno ASC, vp.datum_vraceni DESC;

CREATE OR REPLACE RULE vw_vypujcka_insert AS ON INSERT TO vw_vypujcka
DO INSTEAD (
  INSERT INTO vypujcka (datum_pujceni, datum_vraceni, id_uzivatel, id_vytisk)
  SELECT DISTINCT current_date, 
  current_date + t.vypujcni_doba_dny,
  NEW.id_uzivatel, NEW.id_vytisk
  FROM titul t
  INNER JOIN vytisk v ON v.id_titul = t.id_titul
  WHERE v.id_vytisk = NEW.id_vytisk
);

CREATE OR REPLACE RULE vw_vypujcka_update AS ON UPDATE TO vw_vypujcka
DO INSTEAD (
  UPDATE vypujcka SET je_vraceno = NEW.je_vraceno, 
  datum_vraceni = NEW.datum_vraceni
  WHERE id_vypujcka = NEW.id_vypujcka
);

CREATE OR REPLACE RULE vw_vypujcka_delete AS ON DELETE TO vw_vypujcka
DO INSTEAD (
  DELETE FROM vypujcka WHERE id_vypujcka = OLD.id_vypujcka
);

-- Rezervace
CREATE OR REPLACE VIEW vw_rezervace_id AS
SELECT r.id_uzivatel, r.id_titul,
r.poradi, r.splnena, r.vyprsi, r.zajem_do
FROM rezervace r
WHERE r.zajem_do > current_date AND (r.splnena != TRUE OR r.vyprsi > current_date);

-- Rezervace s poradim
CREATE OR REPLACE VIEW vw_rezervace_poradi AS
SELECT a.id_uzivatel, a.id_titul, COUNT(a.*) as poradi
FROM vw_rezervace_id a INNER JOIN vw_rezervace_id b ON
b.id_titul = a.id_titul AND b.poradi <= a.poradi 
GROUP BY a.id_uzivatel, a.id_titul;

CREATE OR REPLACE VIEW vw_rezervace AS
SELECT r.id_uzivatel, u.krestni_jmeno, u.prijmeni, u.email, r.id_titul, t.nazev,
p.poradi, r.splnena, r.vyprsi, r.zajem_do, e.poradi as vytvorena
FROM vw_rezervace_id r
INNER JOIN uzivatel u ON r.id_uzivatel = u.id_uzivatel
INNER JOIN titul t    ON r.id_titul    = t.id_titul
INNER JOIN vw_rezervace_poradi p ON p.id_uzivatel = r.id_uzivatel AND p.id_titul = r.id_titul
INNER JOIN rezervace e ON e.id_uzivatel = r.id_uzivatel AND e.id_titul = r.id_titul
ORDER BY r.splnena DESC, vytvorena ASC;

CREATE OR REPLACE RULE vw_rezervace_update AS ON UPDATE TO vw_rezervace
DO INSTEAD (
  UPDATE rezervace SET
  zajem_do = NEW.zajem_do,
  vyprsi = NEW.vyprsi,
  splnena = NEW.splnena
  WHERE id_uzivatel = OLD.id_uzivatel
  AND id_titul = OLD.id_titul
);

CREATE OR REPLACE FUNCTION vytvor_rezervaci(
  _id_uzivatel int, _id_titul int, _interval text) RETURNS VOID AS $_$
DECLARE
  _int text;
  cnt int;
BEGIN
  if _interval is null then
    _int := '1 year';
  else
    _int := _interval;
  end if;
  BEGIN
    INSERT INTO rezervace (id_uzivatel, id_titul, zajem_do)
    VALUES (_id_uzivatel, _id_titul, current_date + _int::interval);
  EXCEPTION WHEN unique_violation THEN
    SELECT COUNT(*) INTO cnt FROM vw_rezervace_id 
    WHERE id_uzivatel = _id_uzivatel AND id_titul = _id_titul;
    if cnt < 1 then
      DELETE FROM rezervace WHERE
      id_uzivatel = _id_uzivatel AND id_titul = _id_titul;
      INSERT INTO rezervace (id_uzivatel, id_titul, zajem_do)
      VALUES (_id_uzivatel, _id_titul, current_date + _int);
    else
      RAISE EXCEPTION 'Rezervace na tento titul již existuje';
    end if;
  END;
END;
$_$ LANGUAGE plpgsql;

-- Spln rezervaci pro prvniho uzivatele v poradi
CREATE OR REPLACE FUNCTION spln_rezervaci(_id_titul int) RETURNS VOID AS $_$
BEGIN
  UPDATE vw_rezervace SET splnena = TRUE, vyprsi = current_date + interval '7 days'
  WHERE id_titul = _id_titul AND poradi = 1;
END;
$_$ LANGUAGE plpgsql;

-- Fulltextové vyhledávání
CREATE INDEX titul_fulltext_idx ON titul USING gin(fulltext);

CREATE OR REPLACE FUNCTION update_fulltext() RETURNS TRIGGER AS $_$
BEGIN
  UPDATE titul set fulltext = 
  (SELECT
    to_tsvector('simple', t.nazev) ||
    to_tsvector('simple', coalesce(array_to_string(array_agg(a.prijmeni), ' '))) ||
    to_tsvector('simple', coalesce(array_to_string(array_agg(a.jmeno), ' '))) 
    FROM titul t
    INNER JOIN titul_autor ta ON ta.id_titul = t.id_titul
    INNER JOIN autor a ON a.id_autor = ta.id_autor
    WHERE t.id_titul = NEW.id_titul
    GROUP BY t.id_titul, t.nazev
  )
  WHERE titul.id_titul = NEW.id_titul;

  RETURN NEW;
END;
$_$ LANGUAGE plpgsql;
CREATE TRIGGER update_fulltext_trg AFTER INSERT OR UPDATE ON titul_autor
FOR EACH ROW EXECUTE PROCEDURE update_fulltext();

CREATE OR REPLACE FUNCTION update_titul_fulltext() RETURNS VOID AS $_$
BEGIN
  UPDATE titul set fulltext = 
  (SELECT
    to_tsvector('simple', t.nazev) ||
    to_tsvector('simple', coalesce(array_to_string(array_agg(a.prijmeni), ' '))) ||
    to_tsvector('simple', coalesce(array_to_string(array_agg(a.jmeno), ' '))) 
    FROM titul t
    INNER JOIN titul_autor ta ON ta.id_titul = t.id_titul
    INNER JOIN autor a ON a.id_autor = ta.id_autor
    WHERE t.id_titul = titul.id_titul
    GROUP BY t.id_titul, t.nazev
  );
END;
$_$ LANGUAGE plpgsql;

-- Vlozi autora do tabulky autor, pokud neexistuje a spoji ho 
-- s titulem pomoci tabulky titul_autor
CREATE OR REPLACE FUNCTION vloz_autora(_id_titul int, _jmeno text,
  _prijmeni text) RETURNS VOID AS $_$
DECLARE
  _id_autor int;
BEGIN
  SELECT id_autor INTO _id_autor
  FROM autor
  WHERE jmeno = _jmeno
  AND prijmeni = _prijmeni;
  IF NOT FOUND THEN
    INSERT INTO autor (jmeno, prijmeni)
    VALUES (_jmeno, _prijmeni)
    RETURNING id_autor
    INTO _id_autor;
  END IF;

  INSERT INTO titul_autor (id_titul, id_autor)
  VALUES (_id_titul, _id_autor);
END;
$_$ LANGUAGE plpgsql;

