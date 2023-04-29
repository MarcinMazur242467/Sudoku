create database sudoku;

use sudoku;

create table sudokuBoard (
	id int NOT NULL AUTO_INCREMENT primary key,
    nazwa varchar(255),
    data datetime default now()
    );
    
create table sudokuFields (
	id_field int NOT NULL auto_increment primary key,
    sudokuBoard_id int not null,
    foreign key (sudokuBoard_id) references sudokuBoard(id),
    wartosc int not null
    );