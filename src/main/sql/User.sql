use mydatabase;

create table User(
    id int primary key auto_increment,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    age int not null,
    email varchar(100) not null unique,
    password varchar(100) not null
);
