-- ============================================================
-- SAMPLE DATA — Desarrollo / Demostración
-- ============================================================
-- ACTIVO por defecto (spring.sql.init.mode=always).
-- Usa MERGE INTO (H2 upsert con KEY por unique constraint)
-- para ser seguro ante reinicios dentro del mismo JVM.
-- ============================================================

-- USERS (passwords BCrypt: admin123 / user123 / admin123)
MERGE INTO users (email, password, name, role, created_at, updated_at) KEY(email)
VALUES ('admin@parcial.com', '$2b$10$3S51YwiS.XtwR1Z2cOpoHODh44xGokP8lzAsKHkb9IboesTKFXdwy', 'Admin User', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO users (email, password, name, role, created_at, updated_at) KEY(email)
VALUES ('user@parcial.com', '$2b$10$3S51YwiS.XtwR1Z2cOpoHOOVSgKHqE/s7wuJ3V00vR/CMR.u5ujim', 'Regular User', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO users (email, password, name, role, created_at, updated_at) KEY(email)
VALUES ('jdoe@parcial.com', '$2b$10$3S51YwiS.XtwR1Z2cOpoHODh44xGokP8lzAsKHkb9IboesTKFXdwy', 'John Doe', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- DUMMIES (sin unique constraint, INSERT simple funciona)
INSERT INTO dummy (name, type, created_at, updated_at)
VALUES ('Dummy Alpha', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dummy (name, type, created_at, updated_at)
VALUES ('Dummy Beta', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dummy (name, type, created_at, updated_at)
VALUES ('Dummy Gamma', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dummy (name, type, created_at, updated_at)
VALUES ('Dummy Delta', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
