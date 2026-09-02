-- Creating an entity Menneske
CREATE TABLE Menneske (
    PersonNummer int PRIMARY KEY,  -- Choosing PersonNummer to be primary-
    Brukernavn text UNIQUE,        -- key, not sure if it is serial though
    Navn text
);

-- Creating an entity Romvesen
CREATE TABLE Romvesen (
    Navn text PRIMARY KEY,
    Gruppe text  -- I assume that groups go by names
);

-- Creating an entity Melding
CREATE TABLE Melding (
    ID SERIAL PRIMARY KEY,
    MenneskePN int NOT NULL REFERENCES Menneske (PersonNummer),  -- Adding relation to Menneske
    Diagram bytea,  -- I am assuming that this is how diagrams are stored, but-
    Dato date,      -- diagram could be a text as path to folder it is stored
    Time time,
);

-- Creating an entity Vedlegg
CREATE TABLE Vedlegg (
    Navn text NOT NULL,  -- Combination of Navn and MeldingID act as a primary key
    MeldingID int NOT NULL REFERENCES Melding (ID)  -- Adding relation to Melding
    Storrelse int,  -- Can be calculated though, but I don't know how ;(
    Innhold text,
);

-- Creating a relation between Romvesen and Melding
CREATE TABLE RV_Mottaker (
    MeldingID int NOT NULL REFERENCES Melding (ID),
    RomvesenNavn text NOT NULL REFERENCES Romvesen (Navn)
);

-- Creating a table for multi-attributes
CREATE TABLE Ansvarsomrade (
    MenneskePN int PRIMARY KEY REFERENCES Menneske (PersonNummer),  -- Adding reference to Menneske
    Ansvarsomrade text NOT NULL  -- What is the point of adding this table if this is empty
);