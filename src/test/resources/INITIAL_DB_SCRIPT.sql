INSERT INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'JOURNALIST'),
(3, 'SUBSCRIBER');

INSERT INTO users (id, username, password, role_id) VALUES
(1, 'admin', 'admin123', 1),
(2, 'user1', 'password1', 2),
(3, 'user2', 'password2', 3);