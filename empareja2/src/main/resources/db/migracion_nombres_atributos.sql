-- =====================================================================
-- Empareja2 - Migracion: nombres de atributos unicos + cambios de modelo
--
--   1. Renombra columnas para que no se repitan entre tablas
--      (nombre -> nombre_carrera, nombre_ciudad, ...).
--   2. reaccion: se elimina la columna tipo.
--   3. red_social: pasa de ser un catalogo de redes a guardar el perfil de
--      cada estudiante: red_social(id_usuario, tipo_red_social, url_perfil).
--      La tabla estudiante_red_social deja de existir.
--
-- Requiere MySQL 8.0.16 o superior (RENAME COLUMN, DROP CHECK).
-- Ejecutar UNA sola vez, sobre la base empareja2.
--
-- ANTES DE EJECUTAR: haz un respaldo. En MySQL los ALTER TABLE hacen commit
-- implicito, asi que si algo falla a la mitad no hay ROLLBACK.
--   mysqldump -h 127.0.0.1 -P 3307 -u Puerto314 -p --single-transaction empareja2 > respaldo_empareja2.sql
--
-- Orden: 1) corre este script   2) arranca la nueva version del back.
-- =====================================================================

USE empareja2;

-- ---------------------------------------------------------------------
-- 1. Renombres simples
-- ---------------------------------------------------------------------
ALTER TABLE administrador RENAME COLUMN fecha_designacion TO fecha_designacion_admin;

ALTER TABLE carrera       RENAME COLUMN nombre TO nombre_carrera;
ALTER TABLE ciudad        RENAME COLUMN nombre TO nombre_ciudad;
ALTER TABLE universidad   RENAME COLUMN nombre TO nombre_universidad;
ALTER TABLE usuario       RENAME COLUMN nombre TO nombre_usuario;

ALTER TABLE comentario
    RENAME COLUMN numero     TO numero_comentario,
    RENAME COLUMN contenido  TO contenido_comentario,
    RENAME COLUMN fecha_hora TO fecha_hora_comentario;

-- ---------------------------------------------------------------------
-- 2. reaccion: renombres + eliminar la columna tipo
-- ---------------------------------------------------------------------
-- MySQL no deja tocar una columna que usa un CHECK: primero se quita el CHECK.
ALTER TABLE reaccion DROP CHECK chk3_reaccion_tipo;

ALTER TABLE reaccion
    RENAME COLUMN numero     TO numero_reaccion,
    RENAME COLUMN fecha_hora TO fecha_hora_reaccion;

-- Al borrar la columna, MySQL la saca sola de los indices que la usan:
--   uq_reaccion_autor_destinatario_tipo (id_autor, id_destinatario, tipo) -> (id_autor, id_destinatario)
--   ix_reaccion_destinatario            (id_destinatario, tipo)           -> (id_destinatario)
-- Si existieran dos filas del mismo autor al mismo destinatario, el ALTER falla
-- (con el CHECK anterior solo existia el tipo ME_GUSTA, asi que no deberia pasar).
ALTER TABLE reaccion DROP COLUMN tipo;

ALTER TABLE reaccion
    RENAME INDEX uq_reaccion_autor_destinatario_tipo TO uq_reaccion_autor_destinatario;

-- ---------------------------------------------------------------------
-- 3. reporte: renombres (se recrean los CHECK con los nombres nuevos)
-- ---------------------------------------------------------------------
ALTER TABLE reporte DROP CHECK chk3_reporte_estado;
ALTER TABLE reporte DROP CHECK chk3_reporte_tipo;

ALTER TABLE reporte
    RENAME COLUMN tipo        TO tipo_reporte,
    RENAME COLUMN descripcion TO descripcion_reporte,
    RENAME COLUMN estado      TO estado_reporte,
    RENAME COLUMN fecha_hora  TO fecha_hora_reporte;

ALTER TABLE reporte
    ADD CONSTRAINT chk3_reporte_estado
        CHECK (estado_reporte IN ('PENDIENTE', 'EN_REVISION', 'RESUELTO', 'DESCARTADO'));
ALTER TABLE reporte
    ADD CONSTRAINT chk3_reporte_tipo
        CHECK (tipo_reporte IN ('SPAM', 'ACOSO', 'PERFIL_FALSO', 'CONTENIDO_INAPROPIADO', 'OTRO'));

-- ---------------------------------------------------------------------
-- 4. red_social: de catalogo de redes a perfiles de los usuarios
-- ---------------------------------------------------------------------
-- Las tablas viejas no se borran: quedan como *_respaldo hasta que verifiques.
RENAME TABLE estudiante_red_social TO estudiante_red_social_respaldo;
RENAME TABLE red_social            TO red_social_catalogo_respaldo;

CREATE TABLE red_social (
    id_usuario      BIGINT       NOT NULL,
    tipo_red_social VARCHAR(50)  NOT NULL,
    url_perfil      VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_usuario, tipo_red_social),
    CONSTRAINT fk_red_social_estudiante FOREIGN KEY (id_usuario)
        REFERENCES estudiante (id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Se conservan los perfiles que ya existian: url_base + nombre de usuario.
-- (si url_base no trae http:// o https://, se le antepone https://)
INSERT INTO red_social (id_usuario, tipo_red_social, url_perfil)
SELECT er.id_estudiante,
       r.nombre,
       LEFT(CONCAT(
            CASE WHEN r.url_base LIKE 'http://%' OR r.url_base LIKE 'https://%'
                 THEN TRIM(TRAILING '/' FROM r.url_base)
                 ELSE CONCAT('https://', TRIM(TRAILING '/' FROM r.url_base)) END,
            '/',
            TRIM(LEADING '@' FROM er.nombre_usuario)), 255)
FROM estudiante_red_social_respaldo er
JOIN red_social_catalogo_respaldo r ON r.id_red_social = er.id_red_social;

-- ---------------------------------------------------------------------
-- 5. Verificacion (revisa estas salidas antes de borrar los respaldos)
-- ---------------------------------------------------------------------
-- Deben coincidir: perfiles viejos vs. filas nuevas.
SELECT (SELECT COUNT(*) FROM estudiante_red_social_respaldo) AS perfiles_antes,
       (SELECT COUNT(*) FROM red_social)                     AS perfiles_despues;

-- Mira como quedaron las urls migradas.
SELECT * FROM red_social ORDER BY id_usuario, tipo_red_social;

-- Estructura final.
SHOW CREATE TABLE reaccion;
SHOW CREATE TABLE reporte;
SHOW CREATE TABLE red_social;

-- ---------------------------------------------------------------------
-- 6. Limpieza (SOLO cuando hayas verificado que todo esta bien)
-- ---------------------------------------------------------------------
-- DROP TABLE estudiante_red_social_respaldo;
-- DROP TABLE red_social_catalogo_respaldo;
