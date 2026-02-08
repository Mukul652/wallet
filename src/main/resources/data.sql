INSERT OR IGNORE INTO asset_types (id, name) VALUES (1, 'Gold Coins');

INSERT OR IGNORE INTO users (id, username) VALUES (1, 'alice');
INSERT OR IGNORE INTO users (id, username) VALUES (2, 'bob');

INSERT OR IGNORE INTO wallets (id, user_id, asset_type_id, balance)
VALUES (1, 1, 1, 500);

INSERT OR IGNORE INTO wallets (id, user_id, asset_type_id, balance)
VALUES (2, 2, 1, 300);

INSERT OR IGNORE INTO users (id, username) VALUES (999, 'SYSTEM');

INSERT OR IGNORE INTO wallets (id, user_id, asset_type_id, balance)
VALUES (999, 999, 1, 1000000);


