CREATE TABLE users (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    created_by UUID,
    email VARCHAR(255),
    iin VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    phone_number VARCHAR(255),
    role         SMALLINT NOT NULL
        CONSTRAINT users_role_check
            CHECK ((role >= 0) AND (role <= 1))
);