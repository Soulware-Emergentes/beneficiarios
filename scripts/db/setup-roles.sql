-- Sets up the two roles this project uses instead of one shared
-- credential, so DDL (schema changes) and DML (application queries)
-- are handled by different roles with different privileges:
--
--   beneficiarios_migrator - owns the schema, runs Flyway migrations (DDL rights)
--   beneficiarios_app      - used by the Spring Boot app at runtime (DML rights only)
--
-- Run as the postgres superuser, e.g. on Fedora:
--   sudo -iu postgres psql -f scripts/db/setup-roles.sql
--
-- Passwords are read from environment variables so nothing sensitive
-- ends up in shell history or this file. Set them before running:
--   export BENEFICIARIOS_MIGRATOR_PASSWORD='...'
--   export BENEFICIARIOS_APP_PASSWORD='...'
-- (use the same values you put in your local .env, see .env.example)

\set migrator_pw `echo "${BENEFICIARIOS_MIGRATOR_PASSWORD:?BENEFICIARIOS_MIGRATOR_PASSWORD not set}"`
\set app_pw `echo "${BENEFICIARIOS_APP_PASSWORD:?BENEFICIARIOS_APP_PASSWORD not set}"`

CREATE ROLE beneficiarios_migrator WITH LOGIN PASSWORD :'migrator_pw';
CREATE ROLE beneficiarios_app WITH LOGIN PASSWORD :'app_pw';

-- beneficiarios_migrator owns the database, so it can create/alter/drop objects
-- in it (that's what Flyway needs to run migrations).
CREATE DATABASE beneficiarios OWNER beneficiarios_migrator;

\connect beneficiarios

-- beneficiarios_app only needs to connect and use the public schema - it does not
-- own anything here, so by default it has no privileges on the tables
-- beneficiarios_migrator creates (ownership does not imply privileges in Postgres).
GRANT CONNECT ON DATABASE beneficiarios TO beneficiarios_app;
GRANT USAGE ON SCHEMA public TO beneficiarios_app;

-- Grant DML on every table/sequence beneficiarios_migrator creates from now on,
-- automatically. Without this, each new Flyway migration would require
-- a manual GRANT before beneficiarios_app could use the new table.
ALTER DEFAULT PRIVILEGES FOR ROLE beneficiarios_migrator IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO beneficiarios_app;

ALTER DEFAULT PRIVILEGES FOR ROLE beneficiarios_migrator IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO beneficiarios_app;
