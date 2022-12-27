CREATE TABLE IF NOT EXISTS party (
    id int auto_increment NOT NULL ,
    nombre VARCHAR(255) UNIQUE NOT NULL,
    cantidadDeAventureros int NOT NULL,
    PRIMARY KEY (id)
);