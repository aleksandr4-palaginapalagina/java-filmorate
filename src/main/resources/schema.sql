create table if not exists USERS
(
    id int auto_increment primary key,
    email varchar(255) not null,
    login varchar(255) not null,
    name varchar(255),
    birthday date,
    deleted bool default true
);

create table if not exists GENRES
(
    genres_id int not null primary key auto_increment,
    genres_name varchar(255)
);

create table if not exists MPA
(
    mpa_id int not null primary key auto_increment,
    mpa_name varchar(255)
);

create table if not exists FILMS
(
    id int not null primary key auto_increment,
    name varchar(255),
    description varchar(255),
    release_date date,
    rate int,
    duration int,
    mpa_id int references MPA(mpa_id),
    deleted bool default false
);

create table if not exists FiLM_GENRES
(
    film_id int references FILMS(id),
    genre_id int references GENRES(genres_id),
    PRIMARY KEY (film_id, genre_id)
);

create table if not exists FRIENDS
(
    user_id int not null references USERS(id),
    friend_id int not null references USERS(id),
    PRIMARY KEY (user_id, friend_id)
);

create table if not exists LIKES
(
    user_id int references USERS (id),
    film_id int references FILMS (id),
    PRIMARY KEY (user_id, film_id)
);