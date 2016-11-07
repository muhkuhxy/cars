-- fuel, price, new

# --- !Ups
insert into cars (title, fuel, price, new, mileage, firstRegistration) values (
    'VW Golf II', 'Diesel', 2500, false, 167000, '1990-06-15'
), (
    'Audi A4 Avant', 'Gasoline', 30000, true, null, null
), (
    'Ford Escort', 'Gasoline', 1000, false, 87413, '2006-10-01'
);


# --- !Downs
delete from cars;