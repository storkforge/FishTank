ALTER TABLE appuser
    ADD COLUMN authentication_code VARCHAR(255) NOT NULL UNIQUE;
