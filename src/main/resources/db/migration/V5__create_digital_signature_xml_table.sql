CREATE TABLE digital_signature_xml (
   id UUID NOT NULL PRIMARY KEY,
   petition_id UUID,
   created_at TIMESTAMP(6),
   created_by UUID,
   updated_at  TIMESTAMP(6),
   signature_time TIMESTAMP(6),
   signature oid,
   user_iin VARCHAR(255)
);

ALTER TABLE digital_signature_xml ADD CONSTRAINT uq_signature UNIQUE (signature);