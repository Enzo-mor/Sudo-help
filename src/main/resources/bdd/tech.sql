-- Table representant les techniques de sudoku
CREATE TABLE tech (
    id_tech INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR NOT NULL UNIQUE,
    short_desc TEXT NOT NULL,
    long_desc TEXT NOT NULL,
    cells VARCHAR(81),
    cells_final VARCHAR(81),
    annot_final TEXT
);


-- Insérer des données dans la table tech
INSERT INTO tech (name, short_desc, long_desc, cells, cells_final, annot_final) VALUES
(
    'HiddenPairs',
    'Pensez à utiliser la technique HiddenPairs',
    'La technique des Hidden Pairs (Paires cachées) consiste à repérer deux chiffres qui n''ont que deux emplacements possibles dans une même ligne, colonne ou bloc. Même si ces deux cases contiennent d''autres candidats, le simple fait que ces deux chiffres n''ont aucune autre possibilité permet de conclure que ces deux cases doivent contenir ces deux chiffres (dans un ordre inconnu). On peut alors supprimer tous les autres candidats dans ces deux cases. Exemple : Dans une ligne, si les chiffres 3 et 7 n''apparaissent que dans les cases A et C, mais que ces cases contiennent aussi d''autres candidats comme 2, 5 ou 9, alors 3 et 7 forment une Hidden Pair. On peut donc retirer les 2, 5 et 9 de ces cases, car elles ne contiendront que 3 et 7.',
    '009032000000700000162000000010020560000900000050000107000000403026009000005870000',
    '009032000000700000162000000010020560000900000050000107000000403026009000005870000',
    '4578:478::1456:::678:14578:14568:3458:348:348::145689:14568:23689:1234589:1245689::::45:4589:458:3789:345789:4589:34789::3478:34::3478:::489:26:3478:3478::14568:1345678:238:2348:248:26::348:346:468:3468::23489::789:789:178:1256:156:156::125789::3478:::1345:145::78:1578:158:349:349::::1346:269:129:1269'
),

(
    'HiddenSingle',
    'Pensez à utiliser la technique HiddenSingle',
    'La technique des Hidden Single (Unique caché) consiste à repérer un chiffre qui ne peut apparaître que dans une seule case d''une ligne, colonne ou bloc. Même si cette case contient d''autres candidats, ce chiffre est forcément la solution, car il n''a pas d''autre emplacement possible. Exemple : Si le chiffre 9 n''a qu''une seule case possible dans un bloc, même si cette case contient d''autres candidats comme 2 ou 4, on peut immédiatement placer le 9.',
    '009032000000700000162000000010020560000900000050000107000000403026009000005870000',
    '009032000000700000162000000010020560000900000050000107001000403026009000005870000',
    '4578:478::1456:::678:14578:14568:3458:348:348::145689:14568:23689:1234589:1245689::::45:4589:458:3789:345789:4589:34789::3478:34::3478:::489:234678:3478:3478::14568:1345678:238:2348:248:234689::348:346:468:3468::23489::789:789::256:56:56::25789::3478:::1345:145::78:1578:158:349:349::::1346:269:129:1269'
),

(
    'HiddenTriple',
    'Pensez à utiliser la technique HiddenTriple',
    'La technique des Hidden Triples (Triples cachés) consiste à identifier trois chiffres qui ne peuvent apparaître que dans exactement trois cases d''une même ligne, colonne ou bloc. Même si ces cases contiennent d''autres candidats, ces trois chiffres doivent occuper ces cases. On peut alors supprimer les autres candidats de ces trois cases. Exemple : Dans un bloc, si les chiffres 2, 4 et 6 n''apparaissent que dans les cases A, B et C, on peut enlever les autres candidats présents dans ces trois cases.',
    '008007000042005000000000000003006801000000006900000000080130470000090000010000000',
    '008007000042005000000000000003006801000000006900000000080130470000090000010000000',
    '1356:3569::23469:1246::123569:1234569:23459:1367:::3689:168::13679:13689:3789:13567:35679:15679:234689:12468:123489:1235679:12345689:2345789:2457:257::24579:2457:::2459::124578:257:1457:2345789:124578:123489:23579:23459:::2567:14567:234578:124578:12348:2357:2345:23457:256::569:::2:::259:234567:23567:4567:567::248:12356:123568:2358:234567::45679:567:567:248:23569:235689:23589'
),

(
    'LastCell',
    'Pensez à utiliser la technique LastCell',
    'La technique Last Cell (Dernière case) consiste à remplir la seule case vide restante d''une unité (ligne, colonne ou bloc). Le chiffre manquant est déductible par élimination. Exemple : Si une ligne contient les chiffres de 1 à 8, la neuvième case doit obligatoirement contenir le 9.',
    '123000000456000000780000000000000000000000000000000000000000000000000000000000000',
    '123000000456000000789000000000000000000000000000000000000000000000000000000000000',
    ':::456789:456789:456789:456789:456789:456789::::123789:123789:123789:123789:123789:123789:::9:1234569:1234569:1234569:1234569:1234569:1234569:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789:235689:134679:1245789:123456789:123456789:123456789:123456789:123456789:123456789'
),

(
    'LastNumber',
    'Pensez à utiliser la technique LastNumber',
    'La technique Last Number (Dernier chiffre) consiste à repérer qu''un chiffre est absent d''une seule case dans une ligne, colonne ou bloc. Il doit donc être placé à cet endroit. Exemple : Si le chiffre 5 manque dans une colonne et qu''une seule case est vide, cette case doit contenir le 5.',
    '246000000000306074370000000000000000100000000000000000000000000800000000900000000',
    '246000000500306074370000000000000000100000000000000000000000000800000000900000000',
    ':::15789:15789:15789:13589:13589:13589:5:1589:1589::12589::12589:::::1589:124589:124589:124589:125689:125689:125689:4567:235689:2345789:12456789:123456789:12345789:123456789:12345689:12356789::235689:2345789:2456789:23456789:2345789:23456789:2345689:2356789:4567:235689:2345789:12456789:123456789:12345789:123456789:12345689:12356789:4567:12356:123457:12456789:123456789:12345789:123456789:12345689:12356789::12356:123457:1245679:12345679:1234579:12345679:1234569:1235679::12356:123457:1245678:12345678:1234578:12345678:1234568:1235678'
),

(
    'LastPossible',
    'Pensez à utiliser la technique LastPossible',
    'La technique Last Possible (Dernière possibilité) consiste à identifier qu''un chiffre n''a qu''un seul emplacement possible dans une ligne, colonne ou bloc. Ce chiffre est donc la solution de cette case. Exemple : Si le chiffre 2 n''a plus qu''une seule case possible dans un bloc, même si la case contient d''autres candidats, on peut poser le 2.',
    '000000000000000000000000000008000000000000000000000000000080000060000000910000000',
    '000000000000000000000000000008000000000000000000000000000080000860000000910000000',
    '1234567:2345789:12345679:123456789:12345679:123456789:123456789:123456789:123456789:1234567:2345789:12345679:123456789:12345679:123456789:123456789:123456789:123456789:1234567:2345789:12345679:123456789:12345679:123456789:123456789:123456789:123456789:1234567:234579::12345679:12345679:12345679:12345679:12345679:12345679:1234567:234579:12345679:123456789:12345679:123456789:123456789:123456789:123456789:1234567:234579:12345679:123456789:12345679:123456789:123456789:123456789:123456789:23457:23457:23457:12345679::12345679:12345679:12345679:12345679:::23457:1234579:1234579:1234579:1234579:1234579:1234579:::23457:234567:234567:234567:2345678:2345678:2345678'
),

(
    'NakedPairs',
    'Pensez à utiliser la technique NakedPairs',
    'La technique des Naked Pairs (Paires nues) consiste à repérer deux cases d''une unité qui contiennent exactement les mêmes deux candidats et aucun autre. Ces deux chiffres doivent aller dans ces deux cases. On peut les retirer des autres cases de l''unité. Exemple : Si deux cases contiennent uniquement les candidats 3 et 8, on peut supprimer 3 et 8 des autres cases de la ligne, colonne ou bloc.',
    '002085004000030060004210030000000052000000310900000000800006000250400008000001600',
    '002085004000030060004210030000000052000000310900000000800006000250400008000001600',
    '1367:13679::6:::179:79::157:1789:15789:79::4:125789::1579:567:6789::::79:5789::579:13467:134678:13678:136789:4679:34789:4789:::4567:24678:5678:56789:245679:24789:::679::1234678:135678:135678:24567:23478:478:478:67::13479:1379:3579:2579::124579:2479:13579:::13679::79:379:179:79::347:3479:379:35789:2579:::2479:3579'
),

(
    'NakedSingleton',
    'Pensez à utiliser la technique NakedSingleton',
    'La technique Naked Singleton (Singleton nu) est la plus simple : lorsqu''une case contient un seul candidat, ce chiffre est obligatoirement la solution. Exemple : Une case ne contient que le candidat 4. On inscrit 4 dans cette case.',
    '008007900042005000000600050003006801000000006900070000080130470000090000010000000',
    '008007900042005000000600050003006801000000006900070000080132470000090000010000000',
    '1356:356::234:124:::12346:234:1367:::389:18::1367:1368:378:137:379:179::1248:13489:1237::23478:2457:257::2459:245:::249::124578:257:1457:234589:12458:13489:2357:2349:::256:1456:23458::1348:235:234:2345:56::569::::::59:234567:23567:4567:4578::48:12356:12368:2358:234567::45679:4578:4568:48:2356:23689:23589'
),

(
    'NakedTriples',
    'Pensez à utiliser la technique NakedTriples',
    'La technique des Naked Triples (Triples nus) consiste à repérer trois cases d''une unité qui contiennent exactement les mêmes trois candidats. Ces trois chiffres doivent occuper ces cases. On peut donc les supprimer des autres cases de l''unité. Exemple : Trois cases contiennent les candidats 1, 5 et 9. On enlève ces candidats des autres cases de la ligne, colonne ou bloc.',
    '370000090900070000000420006001084200000000000800600050006002010000000039050000400',
    '370000090900070000000420006001084200000000000800600050006002010000000039050000400',
    '::24:158:156:1568:158::12458::246:24:1358::13568:1358:248:123458:15:18:58:::13589:13578:78::567:369::3579::::67:37:24567:23469:234579:123579:1359:13579:136789:4678:13478::2349:23479::139:1379:1379::1347:47:3489::35789:3459::578::578:1247:1248:2478:1578:1456:15678:5678:::127::23789:13789:1369:136789::2678:278'
),

(
    'PointingPairs',
    'Pensez à utiliser la technique PointingPairs',
    'La technique des Pointing Pairs (Paires pointées) consiste à repérer qu''un chiffre est limité à une seule ligne ou colonne dans un bloc. Ce chiffre ne peut apparaître ailleurs dans cette ligne ou colonne en dehors du bloc. Exemple : Si le chiffre 7 n''a que deux candidats possibles situés sur la même ligne dans un bloc, on peut retirer 7 des autres cases de cette ligne',
    '009070000080400000003000028100000670020013040040007800600030000010000000000000284',
    '009070000080400000003000028100000670020013040040007800600030000010000000000000284',
    '245:56::123568::12568:1345:1356:1356:257::12567::2569:12569:13579:13569:135679:457:567::1569:569:1569:14579::::359:58:2589:24589:24589:::2359:5789::5678:5689:::59::59:359::56:2569:2569:::1359:12359::579:24578:125789::124589:1579:159:1579:235789::24578:256789:245689:245689:3579:3569:35679:3579:3579:57:15679:569:1569:::'
),

(
    'XWing',
    'Pensez à utiliser la technique XWing',
    'La technique X-Wing consiste à repérer un chiffre qui apparaît exactement deux fois dans deux lignes, et aux mêmes colonnes. Ce motif forme un X. Ce chiffre peut alors être supprimé de toutes les autres cases situées sur ces colonnes. Exemple : Si le chiffre 9 apparaît aux positions (Ligne 2, Colonne 3), (Ligne 2, Colonne 7), (Ligne 5, Colonne 3) et (Ligne 5, Colonne 7), alors on peut éliminer le candidat 9 de toutes les autres cases des colonnes 3 et 7.',
    '003800510008700930100305728000200849801906257000500163964127385382659471010400692',
    '003800510008700930100305728000200849801906257000500163964127385382659471010400692',
    '2467:279:::69:24:::46:2456:25:::16:124:::46::49:69::469:::::567:357:567::137:13:::::34:::34:::::247:279:79::458:48::::::::::::::::::::::57::57::38:38:::'
);
