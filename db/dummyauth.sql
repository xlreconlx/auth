DROP DATABASE IF EXISTS dummyauth;

CREATE DATABASE dummyauth
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Conectarse a la base recién creada
\c dummyauth;

-- Habilitar la extensión para generar UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Crear la tabla login_log
CREATE TABLE IF NOT EXISTS public.login_log
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    username character varying(255) NOT NULL,
    login_time timestamp with time zone NOT NULL,
    access_token character varying(2048),
    refresh_token character varying(2048),
    CONSTRAINT login_log_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

-- Asignar propietario de la tabla
ALTER TABLE IF EXISTS public.login_log
    OWNER TO postgres;