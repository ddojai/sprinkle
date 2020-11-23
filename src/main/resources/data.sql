insert into user (name, money_amount, created_date, modified_date) values ('user1', 1000000, now(),
now());
insert into user (name, money_amount, created_date, modified_date) values ('user2', 1000000, now(),
now());
insert into user (name, money_amount, created_date, modified_date) values ('user3', 1000000, now(),
now());

insert into room (code, created_date, modified_date) values (UUID(), now(), now());
insert into room (code, created_date, modified_date) values (UUID(), now(), now());

insert into room_user (room_id, user_id, created_date, modified_date) values (1, 1, now(), now());
insert into room_user (room_id, user_id, created_date, modified_date) values (1, 2, now(), now());
insert into room_user (room_id, user_id, created_date, modified_date) values (1, 3, now(), now());
insert into room_user (room_id, user_id, created_date, modified_date) values (2, 1, now(), now());
insert into room_user (room_id, user_id, created_date, modified_date) values (2, 2, now(), now());

insert into message (room_id, user_id, token, money_amount, created_date, modified_date) values (1,
 1, 'asd', 1000, now(), now());

insert into money (message_id, money_amount, created_date, modified_date) values
(1, 500, now(), now());
insert into money (message_id, money_amount, created_date, modified_date) values
(1, 500, now(), now());