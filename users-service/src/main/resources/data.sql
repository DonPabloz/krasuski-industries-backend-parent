insert into user_role (role) values
('ADMIN'),
('USER');

insert into user_details (public_id, name, surname, email, password, company_name, company_nip, phone_extension, phone_number, enabled, user_role_id, is_newsletter_subscriber) values
('f75acd01-303d-45f7-b3ad-9be4a69327da', 'Paweł', 'Krasuski', 'pawel.kras77@gmail.com', '$2a$10$3BWantCBlk112HtjzbTUg.vFLLV1iX223224mZU8Sdd9P4KONrMSK', 'Krasuski Industries', '2222', '48', '696551514', true, 1, true);

insert into user_refresh_token (user_details_id, token, expiration_time) values (1, '4cda6b65-8528-466e-83f6-68e26285b9e4', 999999999);

insert into company_address (public_id, user_id, company_name, company_nip, email, phone_extension, phone_number, city, zip_code, street, building_number, apartment_number) values
('b12e5efe-4e0a-45d0-ba29-790fa7607088', 1, 'Pawel Krasuski Software', '123123123', 'pawel.kras77@gmail.com', '48', '696551514', 'Warszawa', '05-082', 'Chabrowa', 6, null);

insert into locker_address (public_id, user_id, name, external_id, city, zip_code, street, building_number) values
('cb988375-4e02-4aeb-b3db-e9c885fb3bd0', 1, 'PaczkomatGdzieśTam', 'JakieśIdPaczkomatu', 'Warszawa', '05-092', 'Ulicza', 4);

insert into private_address (public_id, user_id, first_name, last_name, email, phone_extension, phone_number, city, zip_code, street, building_number, apartment_number) values
('0b58c15e-c539-47ed-b795-9c30b0fd9e86', 1, 'Paweł', 'Krasuski', 'pawel.kras77@gmail.com', '48', '696551514', 'Warszawa', '05-082', 'Chabrowa', 6, null),
('7c6a9e2a-d8fe-427b-b1bf-1f4d138513f9', 1, 'Kasia', 'Tusień', 'pawel.kras77@gmail.com', '48', '696551514', 'Warszawa', '05-082', 'Chabrowa', 6, null);
