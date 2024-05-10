CREATE TABLE petition (
    id UUID PRIMARY KEY NOT NULL,
    file_id UUID NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    created_by UUID,
    agency     VARCHAR(255),
    title VARCHAR(255),
    body oid,
    status VARCHAR(255)
);

ALTER TABLE petition ADD CONSTRAINT fk_petition_file FOREIGN KEY (file_id) REFERENCES file(id);