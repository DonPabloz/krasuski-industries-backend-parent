drop table IF EXISTS company_address;
drop table IF EXISTS locker_address;
drop table IF EXISTS private_address;
drop table IF EXISTS user_refresh_token;
drop table IF EXISTS multi_factor_authentication_token;
drop table IF EXISTS user_details;
drop table IF EXISTS role;

create TABLE user_role(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(100) NOT NULL
);

create TABLE user_details(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    public_id UUID NOT NULL,
    name VARCHAR(50),
    surname VARCHAR(50),
    email VARCHAR(100) NOT NULL,
    password VARCHAR(1000) NOT NULL,
    company_name VARCHAR(1000),
    company_nip VARCHAR(1000),
    phone_extension VARCHAR(10),
    phone_number VARCHAR(15),
    enabled BOOLEAN NOT NULL,
    user_role_id LONG NOT NULL,
    FOREIGN KEY (user_role_id) REFERENCES user_role(id),
    is_newsletter_subscriber BOOLEAN NOT NULL,
    UNIQUE (email)
);

create TABLE user_refresh_token(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_details_id LONG NOT NULL,
    FOREIGN KEY (user_details_id) REFERENCES user_details(id),
    token VARCHAR(100) NOT NULL,
    expiration_time LONG NOT NULL
);

create TABLE multi_factor_authentication_token(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_details_id LONG NOT NULL,
    FOREIGN KEY (user_details_id) REFERENCES user_details(id),
    token VARCHAR(100) NOT NULL,
    expiry_date DATETIME NOT NULL
);

CREATE TABLE company_address (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    user_id LONG NOT NULL,
    public_id UUID NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    company_nip VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_extension VARCHAR(255),
    phone_number VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    building_number INT NOT NULL,
    apartment_number INT,
    FOREIGN KEY (user_id) REFERENCES user_details(id)
);

CREATE TABLE locker_address (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    user_id LONG NOT NULL,
    public_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    building_number INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_details(id)
);

CREATE TABLE private_address (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    user_id LONG NOT NULL,
    public_id UUID NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_extension VARCHAR(255),
    phone_number VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    building_number INT NOT NULL,
    apartment_number INT,
    FOREIGN KEY (user_id) REFERENCES user_details(id)
);
