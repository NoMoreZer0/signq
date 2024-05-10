CREATE TABLE file (
    id UUID PRIMARY KEY NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    created_by UUID,
    file_path VARCHAR(255),
    name VARCHAR(255),
    type VARCHAR(255)
)