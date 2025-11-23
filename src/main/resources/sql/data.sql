-- Очистка существующих данных (опционально)
-- DELETE FROM user_devices_applications;
-- DELETE FROM user_devices;
-- DELETE FROM tokens;
-- DELETE FROM app_version;
-- DELETE FROM applications;
-- DELETE FROM users;
-- DELETE FROM role_permission;
-- DELETE FROM permissions;
-- DELETE FROM roles;

-- Вставка ролей
INSERT INTO roles (id, name) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN'),
(3, 'ROLE_DEVELOPER');

-- Вставка разрешений
INSERT INTO permissions (id, resource, operation) VALUES
(1, 'application', 'read'),
(2, 'application', 'write'),
(3, 'application', 'delete'),
(4, 'user', 'read'),
(5, 'user', 'write'),
(6, 'version', 'publish'),
(7, 'version', 'rollback');

-- Связь ролей и разрешений
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 1), (1, 4),                           -- USER: чтение приложений и пользователей
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),  -- ADMIN: все права
(3, 1), (3, 2), (3, 6), (3, 7);          -- DEVELOPER: управление версиями

-- Вставка пользователей (пароль: 'password' закодированный в bcrypt)
INSERT INTO users (id, email, username, password, enabled, role_id) VALUES
(1, 'user1@example.com', 'user1', '$2a$10$7/iqKiYEpv5cni4Gke5BcuJs17e061PTdJUjyD3frRjfWOWeEQTzu', true, 1),
(2, 'user2@example.com', 'user2', '$2a$10$7/iqKiYEpv5cni4Gke5BcuJs17e061PTdJUjyD3frRjfWOWeEQTzu', true, 1),
(3, 'admin@example.com', 'admin', '$2a$10$7/iqKiYEpv5cni4Gke5BcuJs17e061PTdJUjyD3frRjfWOWeEQTzu', true, 2),
(4, 'dev@example.com', 'developer', '$2a$10$7/iqKiYEpv5cni4Gke5BcuJs17e061PTdJUjyD3frRjfWOWeEQTzu', true, 3);

-- Вставка приложений
INSERT INTO applications (id, title, type, developer, publisher, last_update, release_date) VALUES
(1, 'Epic Quest Adventure', 'Game', 'GameDev Studio', 'GamePub Inc', CURRENT_TIMESTAMP, '2023-01-15 10:00:00'),
(2, 'Productivity Pro', 'Application', 'ProdDev LLC', 'ProdCorp', CURRENT_TIMESTAMP, '2023-02-20 09:30:00'),
(3, 'LearnEasy Tutor', 'Application', 'EduTech', 'EduPublish', CURRENT_TIMESTAMP, '2023-03-10 14:15:00'),
(4, 'SocialConnect Plus', 'Application', 'SocialNet Co', 'MediaGroup', CURRENT_TIMESTAMP, '2023-04-05 16:45:00'),
(5, 'Galaxy Warriors', 'Game', 'ToolMakers', 'ToolsRUs', CURRENT_TIMESTAMP, '2023-05-12 11:20:00');

INSERT INTO application_supported_platforms (application_id, supported_platform) VALUES
(1, 'WINDOWS'),
(1, 'MACOS'),
(1, 'STEAMDECK'),
(2, 'WINDOWS'),
(2, 'MACOS'),
(2, 'LINUX'),
(3, 'IOS'),
(3, 'ANDROID'),
(3, 'WINDOWS'),
(4, 'IOS'),
(4, 'ANDROID'),
(5, 'WINDOWS'),
(5, 'LINUX'),
(5, 'MACOS');

-- Вставка версий приложений
INSERT INTO app_versions (id, version, platform, application_id, release_date, change_log, is_active, update_type) VALUES
-- App 1 (Game) versions
(1, '1.0.0', 'WINDOWS', 1, '2023-01-15 10:00:00', 'Initial release', true, 'MANDATORY'),
(2, '1.1.0', 'WINDOWS', 1, '2023-02-01 12:00:00', 'Added new levels', true, 'OPTIONAL'),
(3, '1.1.1', 'WINDOWS', 1, '2023-02-15 14:30:00', 'Bug fixes', true, 'MANDATORY'),
(4, '1.0.0', 'MACOS', 1, '2023-01-20 09:00:00', 'Mac initial release', true, 'MANDATORY'),
(5, '2.0.0', 'WINDOWS', 1, '2023-03-10 16:00:00', 'Major update with new features', true, 'OPTIONAL'),
(6, '1.0.0', 'STEAMDECK', 1, '2023-03-20 11:00:00', 'Steam Deck support', true, 'MANDATORY'),

-- App 2 (Productivity) versions
(7, '1.0.0', 'WINDOWS', 2, '2023-02-20 09:30:00', 'Initial release', true, 'MANDATORY'),
(8, '1.0.0', 'MACOS', 2, '2023-02-25 10:15:00', 'Mac initial release', true, 'MANDATORY'),
(9, '1.1.0', 'WINDOWS', 2, '2023-03-15 11:20:00', 'New templates added', true, 'OPTIONAL'),
(10, '1.0.1', 'LINUX', 2, '2023-03-01 13:45:00', 'Linux support', true, 'MANDATORY'),
(11, '2.0.0', 'WINDOWS', 2, '2023-04-10 14:30:00', 'Complete redesign', false, 'DEPRECATED'),

-- App 3 (Education) versions
(12, '1.0.0', 'WINDOWS', 3, '2023-03-10 14:15:00', 'Initial release', true, 'MANDATORY'),
(13, '1.0.0', 'ANDROID', 3, '2023-03-20 15:30:00', 'Android release', true, 'MANDATORY'),
(14, '1.1.0', 'WINDOWS', 3, '2023-04-05 10:00:00', 'New courses added', true, 'OPTIONAL'),
(15, '1.1.0', 'ANDROID', 3, '2023-04-10 11:30:00', 'New courses added', true, 'OPTIONAL'),
(16, '1.0.1', 'IOS', 3, '2023-04-15 12:00:00', 'iOS initial release', true, 'MANDATORY'),

-- App 4 (Social) versions
(17, '2.0.0', 'IOS', 4, '2023-04-05 16:45:00', 'Major redesign', true, 'MANDATORY'),
(18, '2.1.0', 'IOS', 4, '2023-04-25 17:00:00', 'New chat features', true, 'OPTIONAL'),
(19, '2.0.0', 'ANDROID', 4, '2023-04-10 18:30:00', 'Android release', true, 'MANDATORY'),
(20, '2.1.0', 'ANDROID', 4, '2023-05-05 19:00:00', 'New chat features', true, 'OPTIONAL'),

-- App 5 (Game) versions
(21, '1.0.0', 'WINDOWS', 5, '2023-05-12 11:20:00', 'Initial release', true, 'MANDATORY'),
(22, '1.0.0', 'MACOS', 5, '2023-05-15 12:00:00', 'Mac release', true, 'MANDATORY'),
(23, '1.0.1', 'WINDOWS', 5, '2023-05-20 13:15:00', 'Security update', true, 'MANDATORY'),
(24, '1.1.0', 'WINDOWS', 5, '2023-06-01 14:30:00', 'New tools added', true, 'OPTIONAL'),
(25, '1.0.0', 'LINUX', 5, '2023-06-10 15:45:00', 'Linux support', true, 'MANDATORY');

-- Вставка устройств пользователей
INSERT INTO user_devices (id, name, platform, last_seen, user_id) VALUES
-- User 1 devices
(1, 'My Windows Laptop', 'WINDOWS', CURRENT_TIMESTAMP, 1),
(2, 'My Android Phone', 'ANDROID', CURRENT_TIMESTAMP, 1),
(3, 'Work Mac', 'MACOS', '2023-06-10 09:00:00', 1),

-- User 2 devices
(4, 'Home PC', 'WINDOWS', CURRENT_TIMESTAMP, 2),
(5, 'iPhone 14', 'IOS', '2023-06-08 14:20:00', 2),
(6, 'Android Tablet', 'ANDROID', '2023-06-05 10:30:00', 2),

-- Admin devices
(7, 'Admin Desktop', 'WINDOWS', CURRENT_TIMESTAMP, 3),
(8, 'Admin iPad', 'IOS', CURRENT_TIMESTAMP, 3),
(9, 'Admin Android', 'ANDROID', '2023-06-09 16:45:00', 3),

-- Developer devices
(10, 'Dev Laptop', 'WINDOWS', CURRENT_TIMESTAMP, 4),
(11, 'Test Phone', 'ANDROID', '2023-06-07 11:15:00', 4),
(12, 'Test Steam Deck', 'STEAMDECK', '2023-06-12 13:20:00', 4);

-- Вставка связей устройств и приложений (таблица user_devices_applications)
INSERT INTO user_devices_app_versions (user_device_id, app_versions_id) VALUES
-- User 1 Windows Laptop
(7, 1),  -- Game v1.0.0 Windows
(1, 7),  -- Productivity v1.0.0 Windows
(1, 12), -- Education v1.0.0 Windows

-- User 1 Android Phone
(2, 13), -- Education v1.0.0 Android
(2, 19), -- Social v2.0.0 Android

-- User 1 Work Mac
(3, 4),  -- Game v1.0.0 MacOS
(3, 8),  -- Productivity v1.0.0 MacOS

-- User 2 Home PC
(4, 2),  -- Game v1.1.0 Windows
(4, 21), -- Game 2 v1.0.0 Windows

-- User 2 iPhone
(5, 16), -- Education v1.0.1 iOS
(5, 17), -- Social v2.0.0 iOS

-- User 2 Android Tablet
(6, 15), -- Education v1.1.0 Android
(6, 20), -- Social v2.1.0 Android

-- Admin Windows Desktop
(7, 3),  -- Game v1.1.1 Windows
(7, 9),  -- Productivity v1.1.0 Windows
(7, 14), -- Education v1.1.0 Windows
(7, 23), -- Game 2 v1.0.1 Windows

-- Admin iPad
(8, 16), -- Education v1.0.1 iOS
(8, 18), -- Social v2.1.0 iOS

-- Admin Android Phone
(9, 6),  -- Game v1.0.0 SteamDeck
(9, 13), -- Education v1.0.0 Android
(9, 19), -- Social v2.0.0 Android

-- Developer Laptop
(10, 5), -- Game v2.0.0 Windows
(10, 7), -- Productivity v1.0.0 Windows
(10, 24),-- Game 2 v1.1.0 Windows

-- Developer Test Phone
(11, 15),-- Education v1.1.0 Android
(11, 20),-- Social v2.1.0 Android

-- Developer Steam Deck
(12, 6), -- Game v1.0.0 SteamDeck
(12, 21);-- Game 2 v1.0.0 Windows

-- Сброс последовательностей (для PostgreSQL)
ALTER SEQUENCE roles_id_seq RESTART WITH 100;
ALTER SEQUENCE permissions_id_seq RESTART WITH 100;
ALTER SEQUENCE users_id_seq RESTART WITH 100;
ALTER SEQUENCE applications_id_seq RESTART WITH 100;
ALTER SEQUENCE app_versions_id_seq RESTART WITH 100;
ALTER SEQUENCE user_devices_id_seq RESTART WITH 100;