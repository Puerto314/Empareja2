-- Empareja2 v3: estado de moderacion de los reportes.
-- La aplicacion ya ejecuta esto sola al arrancar (EsquemaMigrador); este script es
-- solo por si el usuario de la base de datos no tiene permiso ALTER.
-- Valores validos: PENDIENTE, EN_REVISION, RESUELTO, DESCARTADO.
ALTER TABLE reporte
    ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE';
