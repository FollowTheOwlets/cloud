/* init-db.sql */
CREATE TABLE IF NOT EXISTS public.users
(
    id       bigint NOT NULL,
    login    character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
) TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

INSERT INTO public.users(id, login, password)
VALUES (1, 'lev', 'pas');