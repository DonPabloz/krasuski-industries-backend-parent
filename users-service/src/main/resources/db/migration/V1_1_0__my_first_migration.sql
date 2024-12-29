CREATE TABLE ddd_user (
    id BIGSERIAL PRIMARY KEY,
    pub_id VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(255),
    hashed_password VARCHAR(255),
    is_account_verified BOOLEAN
);

CREATE TABLE ddd_verification_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255),
    creation_date TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);

CREATE TABLE ddd_refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255),
    creation_date TIMESTAMP,
    is_active BOOLEAN,
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);

CREATE TABLE ddd_company_address (
    id BIGSERIAL PRIMARY KEY,
    pub_id VARCHAR(255),
    company_name VARCHAR(255),
    company_nip VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(255),
    street VARCHAR(255),
    building_number INTEGER,
    apartment_number INTEGER,
    zip_code VARCHAR(255),
    city VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);

CREATE TABLE ddd_private_address (
    id BIGSERIAL PRIMARY KEY,
    pub_id VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(255),
    street VARCHAR(255),
    building_number INTEGER,
    apartment_number INTEGER,
    zip_code VARCHAR(255),
    city VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);

CREATE TABLE ddd_password_reset_token (
    id BIGSERIAL PRIMARY KEY,
    creation_date TIMESTAMP,
    token VARCHAR(255),
    expiration_date TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);

CREATE TABLE ddd_inpost_locker (
    id BIGSERIAL PRIMARY KEY,
    inpost_external_pub_id VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES ddd_user(id)
);