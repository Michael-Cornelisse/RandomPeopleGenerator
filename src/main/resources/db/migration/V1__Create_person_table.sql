CREATE TABLE IF NOT EXISTS "person"
(
    "id"        Int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "lastname"  VARCHAR(100) NOT NULL,
    "firstname" VARCHAR(100) NOT NULL,
    "sex"       Char(1)      NOT NULL,
    "birthdate" VARCHAR(10)  NOT NULL,
    "zipcode"   Char(6)      NOT NULL

);


