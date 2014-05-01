CREATE OR REPLACE FUNCTION generate_bar_code() RETURNS TRIGGER AS $_$
BEGIN
  NEW.car_kod := to_char(NEW.id_vytisk, 'FM0000000000000');
  RETURN NEW;
END;
$_$ LANGUAGE plpgsql;

CREATE TRIGGER gen_bar_code BEFORE INSERT ON vytisk
FOR EACH ROW EXECUTE PROCEDURE generate_bar_code();

CREATE OR REPLACE FUNCTION vytvor_rezervaci(
  _id_uzivatel int, _id_titul int, _interval interval) RETURNS VOID AS $_$
DECLARE
  _int interval;
  count int;
BEGIN
  SELECT COUNT(*) INTO count FROM vytisk
  WHERE id_titul = _id_titul;
  if count > 0 then
    RAISE EXCEPTION 'Existuje volny vytisk'
  end if
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
