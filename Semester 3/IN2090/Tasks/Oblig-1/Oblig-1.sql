-- Exercise 1:
SELECT count(*)
  FROM materie;



-- Exercise 2:
-- a)
SELECT navn
  FROM planet
 WHERE stjerne = 'Proxima Centauri';

-- b)
SELECT DISTINCT oppdaget
  FROM planet
 WHERE stjerne SIMILAR TO '(TRAPPIST-1|Kepler-154)';

-- c)
SELECT count(*)
  FROM planet
 WHERE masse IS NULL;

-- d) P.S. There probably was a better way...
--    This one is clean though
SELECT navn, planet.masse
  FROM planet,
       (SELECT avg(masse) AS masse
          FROM planet) AS avg
 WHERE oppdaget = '2020' AND
       planet.masse > avg.masse;

-- e)
SELECT max - min AS differanse
  FROM (SELECT min(oppdaget), max(oppdaget)
          FROM planet);



-- Exercise 3:
-- a)
SELECT navn
  FROM planet INNER JOIN materie ON (navn = materie.planet)
 WHERE 10 > masse AND masse > 3
       AND molekyl = 'H2O';

-- b)
SELECT planet.navn
  FROM planet INNER JOIN materie ON (planet.navn = materie.planet)
       INNER JOIN stjerne ON (planet.stjerne = stjerne.navn)
 WHERE avstand < 12 AND
       molekyl = 'H';

-- c)
SELECT p1.navn
  FROM planet AS p1, planet AS p2
       INNER JOIN stjerne ON (p2.stjerne = stjerne.navn)
 WHERE p1.navn != p2.navn AND
       p1.stjerne = p2.stjerne AND
       p1.masse > 10 AND p2.masse > 10 AND
       avstand < 50;



-- Exercise 4:
-- It does not work because NATURAL JOIN is trying to find collumns with the same name to join.
-- In our database this doesn't work because in "planet" table "navn" and "masse" are planet's parameters,
-- whilst in "stjerne" table those are star's parameters, and there are no parameters
-- where a star and a planet share same mass, same name, and where the distance to that star is over 8000.

-- If we were to rename "stjerne" collumn into "stjerne_navn" in "planet" table,
-- and rename "navn" collumn into "stjerne_navn" in "stjerne" table,
-- as well as rename "masse" into "planet_masse" and "stjerne_masse" respectively,
-- then I think this would work just fine.

-- But, I have to fix this don't I? So, here is the more manual process of fixing this problem,
-- and a simplier one at that:
SELECT oppdaget FROM planet INNER JOIN stjerne ON(planet.stjerne = stjerne.navn) WHERE avstand > 8000;



-- Exercise 5:
-- a)
INSERT INTO stjerne
VALUES ('Sola', 0, 1);

-- b)
INSERT INTO planet
VALUES ('Jorda', 0.003146, NULL, 'Sola');



-- Exercise 6:
CREATE TABLE observasjon (
    observasjons_id int,
    timestamp date,
    planet text REFERENCES planet (navn),  -- References planet name
    kommentar text,
    CONSTRAINT obid_pk PRIMARY KEY (observasjons_id)  -- Stands for observasjons_id primary key
    CONSTRAINT ts_n_p_nn NOT NULL (timestamp, planet)  -- Stands for timestamp and planet not null
);