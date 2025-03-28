-- Table representant les techniques de sudoku
CREATE TABLE possedeTech (
    id_post INTEGER PRIMARY KEY AUTOINCREMENT,
    player VARCHAR REFERENCES profile(pseudo),
    id_tech INTEGER REFERENCES tech(id_tech),
    count INTEGER DEFAULT 0,
    already BOOLEAN DEFAULT false
);