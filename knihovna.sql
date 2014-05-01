CREATE SEQUENCE seq_uzivatel;
CREATE  TABLE uzivatel (
  id_uzivatel INT NOT NULL DEFAULT nextval('seq_uzivatel'),
  krestni_jmeno VARCHAR(30) NOT NULL ,
  prijmeni VARCHAR(45) NOT NULL ,
  email VARCHAR(50) NOT NULL UNIQUE,
  heslo CHAR(32) NOT NULL,
  role SMALLINT NOT NULL DEFAULT 1,
  PRIMARY KEY (id_uzivatel) );

CREATE SEQUENCE seq_zanr;
CREATE  TABLE zanr (
  id_zanr INT NOT NULL UNIQUE DEFAULT nextval('seq_zanr'),
  nazev VARCHAR(30) NOT NULL ,
  PRIMARY KEY (id_zanr) );

CREATE SEQUENCE seq_titul;
CREATE  TABLE titul (
  id_titul INT NOT NULL DEFAULT nextval('seq_titul'),
  nazev VARCHAR(255) NOT NULL UNIQUE,
  rok_vydani SMALLINT NOT NULL ,
  vypujcni_doba_dny SMALLINT NOT NULL DEFAULT 30 ,
  cena NUMERIC(2) NOT NULL CHECK (cena > 0),
  id_zanr INT NOT NULL ,
  PRIMARY KEY (id_titul) ,
  CONSTRAINT fk_titul_zanr1
    FOREIGN KEY (id_zanr )
    REFERENCES zanr (id_zanr )
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE SEQUENCE seq_poradi;
CREATE  TABLE rezervace (
  id_uzivatel INT NOT NULL ,
  id_titul INT NOT NULL ,
  poradi INT NOT NULL UNIQUE DEFAULT nextval('seq_poradi'),
  zajem_do DATE NOT NULL ,
  splnena BOOLEAN NOT NULL DEFAULT FALSE,
  vyprsi DATE DEFAULT NULL,
  PRIMARY KEY (id_uzivatel, id_titul) ,
  CONSTRAINT fk_ctenar_has_titul_ctenar1
    FOREIGN KEY (id_uzivatel )
    REFERENCES uzivatel (id_uzivatel )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_ctenar_has_titul_titul1
    FOREIGN KEY (id_titul )
    REFERENCES titul (id_titul )
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE SEQUENCE seq_autor;
CREATE  TABLE autor (
  id_autor INT NOT NULL DEFAULT nextval('seq_autor'),
  jmeno VARCHAR(30) NOT NULL ,
  prijmeni VARCHAR(50) NOT NULL ,
  PRIMARY KEY (id_autor) );

CREATE  TABLE titul_autor (
  titul_id_titul INT NOT NULL ,
  autor_id_autor INT NOT NULL ,
  PRIMARY KEY (titul_id_titul, autor_id_autor) ,
  CONSTRAINT fk_titul_has_autor_titul1
    FOREIGN KEY (titul_id_titul )
    REFERENCES titul (id_titul )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_titul_has_autor_autor1
    FOREIGN KEY (autor_id_autor )
    REFERENCES autor (id_autor )
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE SEQUENCE seq_vytisk;
CREATE  TABLE vytisk (
  id_vytisk INT NOT NULL DEFAULT nextval('seq_vytisk'),
  car_kod CHAR(13) NOT NULL UNIQUE,
  id_titul INT NOT NULL ,
  PRIMARY KEY (id_vytisk) ,
  CONSTRAINT fk_vytisk_titul1
    FOREIGN KEY (id_titul )
    REFERENCES titul (id_titul )
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE SEQUENCE seq_vypujcka;
CREATE  TABLE vypujcka (
  id_vypujcka INT NOT NULL DEFAULT nextval('seq_vypujcka'),
  datum_pujceni DATE NOT NULL ,
  datum_vraceni DATE NOT NULL,
  je_vraceno BOOLEAN NOT NULL DEFAULT FALSE ,
  id_uzivatel INT NOT NULL ,
  id_vytisk INT NOT NULL ,
  PRIMARY KEY (id_vypujcka) ,
  CONSTRAINT check_datum CHECK (datum_vraceni >= datum_pujceni),
  CONSTRAINT fk_vypujcka_ctenar1
    FOREIGN KEY (id_uzivatel )
    REFERENCES uzivatel (id_uzivatel )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_vypujcka_vytisk1
    FOREIGN KEY (id_vytisk )
    REFERENCES vytisk (id_vytisk )
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
