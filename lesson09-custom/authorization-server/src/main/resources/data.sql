INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('client', '{noop}secret', 'read,write',
	'password,authorization_code,refresh_token,client_credentials', 'http://localhost:8082/login', null, 36000, 36000, null, true);


delete from USERS;

-- password
INSERT INTO USERS (ID,Name, USERNAME,PASSWORD) VALUES (
   1, 'admin','admin','{noop}pass');

INSERT INTO USERS (ID, Name, USERNAME,PASSWORD) VALUES (
   2, 'user','user','{noop}pass');
