CREATE TABLE game(
    id_game INTEGER PRIMARY KEY AUTOINCREMENT,
    grid  INTEGER REFERENCES grid(id_grid),
    player VARCHAR  REFERENCES profile(pseudo),
    created_date TEXT  UNIQUE not null,
    last_modifed_date Text UNIQUE not null,
    progress_rate REAL not null,
    score INTEGER DEFAULT 0,
    actions TEXT,
    elapsed_time INTEGER DEFAULT 0,
    game_state TEXT not null

);