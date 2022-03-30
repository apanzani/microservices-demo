DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default",
    firstname character varying COLLATE pg_catalog."default",
    lastname character varying COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE users
    OWNER to keycloak;

DROP TABLE IF EXISTS documents CASCADE;

CREATE TABLE documents
(
    id uuid NOT NULL,
    document_id character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT documents_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE documents
    OWNER to keycloak;

DROP TABLE IF EXISTS user_permissions CASCADE;

CREATE TABLE user_permissions
(
    user_id uuid NOT NULL,
    document_id uuid NOT NULL,
    user_permission_id uuid NOT NULL,
    permission_type character varying COLLATE pg_catalog."default",
    CONSTRAINT document_fk FOREIGN KEY (document_id)
        REFERENCES documents (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE user_permissions
    OWNER to keycloak;

CREATE INDEX "fki_USER_FK"
    ON user_permissions USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX fki_document_fk
    ON user_permissions USING btree
    (document_id ASC NULLS LAST)
    TABLESPACE pg_default;
