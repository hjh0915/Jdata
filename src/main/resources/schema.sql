create table users (
    user_id int not null,
    gender varchar(50) not null,
    age int not null,
    occupation int not null,
    zip int not null
);

create table movies (
    movie_id int not null,
    title varchar(100) not null,
    genres varchar(100) not null
);

create table rates (
    user_id int not null,
    movie_id int not null,
    rating int not null,
    timestamp varchar(50) not null
);