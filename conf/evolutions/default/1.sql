-- fuel, price, new

# --- !Ups
create table cars (
    id int auto_increment primary key,
    title varchar(255) not null,
    fuel varchar(255) not null,
    price int not null,
    new bool not null
);


# --- !Downs
drop table cars;