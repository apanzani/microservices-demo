CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('4191acfa-298a-4bbd-ac0d-55e6fa5b4ab9', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('74c6a7fb-2c02-47ae-8fa7-98889473403a', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('55a05e8b-4404-4df0-89bd-00a69ad5423e', 'app_super_user', 'Super', 'User');


insert into public.documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 1);
insert into public.documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 2);
insert into public.documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 3);

insert into public.user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'4191acfa-298a-4bbd-ac0d-55e6fa5b4ab9', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into public.user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'74c6a7fb-2c02-47ae-8fa7-98889473403a', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into public.user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'74c6a7fb-2c02-47ae-8fa7-98889473403a', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into public.user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '74c6a7fb-2c02-47ae-8fa7-98889473403a', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into public.user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '55a05e8b-4404-4df0-89bd-00a69ad5423e', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');


