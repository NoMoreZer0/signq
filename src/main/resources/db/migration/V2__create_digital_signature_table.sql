CREATE TABLE digital_signature (
    id UUID NOT NULL PRIMARY KEY,
    petition_id UUID,
    created_at TIMESTAMP(6),
    created_by UUID,
    updated_at  TIMESTAMP(6),
    signature_time TIMESTAMP(6),
    certificate bytea,
    signature bytea,
    data VARCHAR(255),
    user_iin VARCHAR(255)
);

ALTER TABLE digital_signature ADD CONSTRAINT uq_data UNIQUE (data);