CREATE TABLE cartesbancaires (
    iduser UUID PRIMARY KEY,
    codecarte NUMERIC(8, 0) NOT NULL,
    codesecret NUMERIC(4, 0) NOT NULL
);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    card_code NUMERIC(8, 0) DEFAULT FLOOR(RANDOM() * 1e8)::NUMERIC NOT NULL,
    card_number NUMERIC(8, 0) GENERATED ALWAYS AS (card_code) STORED,
    wentThrough BOOLEAN DEFAULT FALSE NOT NULL
    amount DOUBLE PRECISION NOT NULL
);
