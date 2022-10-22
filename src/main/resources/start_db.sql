create table if not exists ProtectedAreas(
                                             uuid varchar(50) primary key,
                                             x_1 integer,
                                             z_1 integer,
                                             x_2 integer,
                                             z_2 integer,
                                             owner varchar(20)
);
create table if not exists AllowedBlocks(
                                            id integer primary key auto_increment,
                                            x integer,
                                            y integer,
                                            z integer,
                                            owner varchar(20)
);
create table if not exists ProtectionPermissions(
                                                    id integer primary key auto_increment,
                                                    player varchar(20),
                                                    protected_area_id varchar(50),
                                                    unique(player, protected_area_id),
                                                    foreign key (protected_area_id) references ProtectedAreas(uuid)
);
create table if not exists SuperProtectedBlocks(
                                                   id integer primary key auto_increment,
                                                   x integer,
                                                   y integer,
                                                   z integer,
                                                   owner varchar(20)
);
create table if not exists Users(
                                    uuid varchar(50) primary key,
                                    player varchar(20),
                                    password varchar(500),
                                    unique (player)
);


create table if not exists Sessions(
                                       id integer primary key auto_increment,
                                       ip varchar(15),
                                       player_id varchar(50),
                                       foreign key (player_id) references Users(uuid)
);


create or replace view registered_users as
select uuid as id, player from Users;

