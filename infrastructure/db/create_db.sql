-- create database updater_db;
DROP TABLE IF EXISTS app_version;
DROP TYPE IF EXISTS platform_enum;
DROP TYPE IF EXISTS update_type_enum;

CREATE TYPE platform_enum AS ENUM ('IOS', 'ANDROID');
CREATE TYPE update_type_enum AS ENUM ('MANDATORY', 'OPTIONAL', 'DEPRECATED');

CREATE TABLE app_version (
    id SERIAL PRIMARY KEY,
    version VARCHAR(20) NOT NULL,
    platform platform_enum NOT NULL,
    release_date TIMESTAMP NOT NULL,
    change_log TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    update_type update_type_enum NOT NULL,

    UNIQUE (version, platform)
);

INSERT INTO app_version (version, release_date, change_log, is_active, update_type, platform) VALUES
('1.0.0', '2023-01-01 10:00:00', 'Initial release', true, 'MANDATORY', 'IOS'),
('1.0.0', '2023-01-01 10:00:00', 'Initial release', true, 'MANDATORY', 'ANDROID'),
('1.1.0', '2023-02-01 14:30:00', 'New features added', true, 'OPTIONAL', 'IOS'),
('1.1.0', '2023-02-01 14:30:00', 'New features added', false, 'DEPRECATED', 'ANDROID');

CREATE INDEX idx_app_version_active ON app_version(is_active);
CREATE INDEX idx_app_version_platform ON app_version(platform);
CREATE INDEX idx_app_version_release_date ON app_version(release_date);