-- The registry of beneficiaries.
--
-- A beneficiary is identified by a legal document, unique across the registry,
-- whose number follows the rules of its type. The ubigeo is the district they
-- live in: two digits each for department, province and district.
CREATE TABLE beneficiaries (
    id UUID PRIMARY KEY,
    legal_document_type VARCHAR(20) NOT NULL CHECK (legal_document_type IN ('DNI', 'FOREIGNER_ID_CARD', 'PASSPORT')),
    legal_document VARCHAR(12) NOT NULL,
    names VARCHAR(255) NOT NULL,
    paternal_surname VARCHAR(255) NOT NULL,
    maternal_surname VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    ubigeo VARCHAR(6) NOT NULL CHECK (ubigeo ~ '^[0-9]{6}$'),
    CONSTRAINT beneficiaries_legal_document_key UNIQUE (legal_document_type, legal_document),
    CHECK (legal_document_type <> 'DNI' OR legal_document ~ '^[0-9]{8}$'),
    CHECK (legal_document_type <> 'FOREIGNER_ID_CARD' OR legal_document ~ '^[A-Za-z0-9]{9,12}$'),
    CHECK (legal_document_type <> 'PASSPORT' OR legal_document ~ '^[A-Za-z0-9]{6,12}$')
);
