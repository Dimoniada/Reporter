-- H2 syntax --
drop table if exists "traffic_mon";
drop table if exists "users";

create table "users"
(
    "id"       integer not null auto_increment,
    "username" nvarchar(255),
    "password" nvarchar(255),
    primary key ("id")
);

create table "traffic_mon"
(
    "client_name"      nvarchar(255)     null,
    "date1_amount"     integer           null,
    "date2_amount"     integer           null,
    "abs_diff"         nvarchar(16)      null,
    "rel_diff_percent" nvarchar(45)      null,
    "login"            nvarchar(255)     null,
    "user_id"          integer default 1 null,
    "id"               integer auto_increment,
    primary key ("id"),
    constraint "fk_user_id" foreign key ("user_id") references "users" ("id")
);

insert into "users"
("id",
 "username",
 "password")
values ('1', 'user1', '3c73a928-0306-4767-849d-5cb1824be735'),
       ('2', 'user2', 'c44f65cf-9fb9-46ff-a583-89783ef4d3cc'),
       ('3', 'user3', '361794ef-4344-42a6-99c3-6c2fc0be63a7'),
       ('4', 'user4', '9505e560-9f21-4db4-a0fd-8b4b07274814');

insert into "traffic_mon" ("login",
                           "client_name",
                           "date1_amount",
                           "date2_amount",
                           "abs_diff",
                           "rel_diff_percent",
                           "user_id")
values ('АО ВТБ Лизинг', 'client1', '30', '30', '100%', '79779117745', '1'),
       ('"АО КБ ""Хлынов"""', 'client2', '0', '-4', '-100%', '79532546804', '3'),
       ('ООО Стэй ИН Тач', 'client3', '2', '2', '100%', '79017248626', '1'),
       ('"ООО ""НОВЫЙ ГОРОД"""', 'client4', '7', '7', '100%', '79532531508', '3'),
       ('"ООО ""А + А Эксист-Инфо"""', 'client5', '0', '-2', '-100%', '79960174254', '1'),
       ('"ООО ""Здоровье"""', 'client6', '363', '363', '100%', '79528821480', '1'),
       ('АО ВТБ Лизинг', 'client7', '12', '12', '100%', '79779117695', '1'),
       ('ООО «Комтехцентр»', 'client8', '3', '3', '100%', '79916298051', '1'),
       ('Управление Министерства внутренних дел Россий', 'client9', '1', '1', '100%', '79536686788', '4'),
       ('"ООО МКК ""ДЕНЬГИМИГОМ"""', 'client10', '1', '1', '100%', '79509479008', '1'),
       ('"ООО ""ГАЗПРОМ ГАЗОРАСПРЕДЕЛЕНИЕ ЙОШКАР-ОЛА"""', 'client11', '814', '814', '100%', '79026728288', '1');
