-- Table representant les techniques de sudoku
CREATE TABLE tech (
    id_tech INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR NOT NULL UNIQUE,
    short_desc TEXT NOT NULL,
    long_desc TEXT NOT NULL,
    cells VARCHAR(81)
);


-- Insérer des données dans la table tech
INSERT INTO tech (name, short_desc, long_desc, cells) VALUES
(
    'HiddenPairs',
    'Description courte HiddenPairs',
    'Description longue HiddenPairs',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'HiddenSingle',
    'Description courte HiddenSingle',
    'Description longue HiddenSingle',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'HiddenTriple',
    'Description courte HiddenTriple',
    'Description longue HiddenTriple',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'LastCell',
    'Description courte LastCell',
    'Description longue LastCell',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'LastNumber',
    'Description courte LastNumber',
    'Description longue LastNumber, je suis et donc anticonstitutionnellement la te je mais ou et donc or ni car qui que quoi comment quand pourquoi ainsi coucou salut hello bonjour au revoir ici de nous vous ils enzo emma nathan kilian lina alban gabriel louison dylan victor vincent |||| Description longue LastNumber, je suis et donc la te je mais ou et donc or ni car qui que quoi comment quand pourquoi ainsi coucou salut hello bonjour au revoir ici de nous vous ils enzo emma nathan kilian lina alban',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'LastPossible',
    'Description courte LastPossible',
    'Description longue LastPossible',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'NakedPairs',
    'Description courte NakedPairs',
    'Description longue NakedPairs',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'NakedSingleton',
    'Description courte NakedSingleton',
    'Description longue NakedSingleton',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'NakedTriples',
    'Description courte NakedTriples',
    'Description longue NakedTriples',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'PointingPairs',
    'Description courte PointingPairs',
    'Description longue PointingPairs',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'PointingTriples',
    'Description courte PointingTriples',
    'Description longue PointingTriples',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
),

(
    'XWing',
    'Description courte XWing',
    'Description longue XWing',
    '000000000000000000000000000000000000000000000000000000000000000000000000000000000'
);
