-- Table representant les profiles
CREATE TABLE profile (
    id_profile INTEGER PRIMARY KEY AUTOINCREMENT,
    pseudo VARCHAR NOT NULL UNIQUE
);

