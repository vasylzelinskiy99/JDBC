CREATE SCHEMA IF NOT EXISTS vasia_zel_5 DEFAULT CHARACTER SET utf8;
USE vasia_zel_5;

CREATE TABLE Library
(
  Library VARCHAR(25) NOT NULL,
  PRIMARY KEY (Library)
) ENGINE = InnoDB;

CREATE TABLE  Reader
(
  ID_Reader INT NOT NULL AUTO_INCREMENT,
  Surname VARCHAR(25) NOT NULL,
  Name VARCHAR(25) NOT NULL,
  Library VARCHAR(25) NOT NULL,
  Email VARCHAR(45) NULL,
  PRIMARY KEY (ID_Reader),
  CONSTRAINT FOREIGN KEY (Library)
  REFERENCES Library (Library)
) ENGINE = InnoDB;

CREATE TABLE Book
(
  ID_Book INT NOT NULL AUTO_INCREMENT,
  BookName VARCHAR(45) NOT NULL,
  Author VARCHAR(45) NOT NULL,
  Amount INT NOT NULL,
  PRIMARY KEY (ID_Book)
) ENGINE = InnoDB;

CREATE TABLE  ReadersBook (
  ID_Reader INT NOT NULL,
  ID_Book INT NOT NULL,
  PRIMARY KEY (ID_Reader, ID_Book),
  CONSTRAINT  FOREIGN KEY (ID_Reader)
  REFERENCES  Reader (ID_Reader),
  CONSTRAINT   FOREIGN KEY (ID_Book)
  REFERENCES  Book (ID_Book)
) ENGINE = InnoDB;

INSERT INTO book VALUES
  (1,'Bible','St. mans',5),
  (2,'Kobzar','Shevchenko ',4),
  (3,'Harry Potter','J. K. Rowling',1),
  (4,'Zakhar Berkut','I. Franko',2),
  (5,'The Jungle Book','Rudyard Kipling',1);

INSERT INTO library VALUES ('Hersons City Library'),('Kyiv City Library'),('Lviv City Library'),('Poltava City Library'),('Ternopil City Library');

INSERT INTO reader VALUES
  (1,'Koldovskyy','Vyacheslav','Lviv','koldovsky@gmail.com'),
  (2,'Pavelchak','Andrii','Poltava','apavelchak@gmail.com'),
  (3,'Soluk','Andrian','Herson','andriansoluk@gmail.com'),
  (4,'Dubyniak','Bohdan','Ternopil','bohdan.dub@gmail.com'),
  (5,'Faryna','Igor','Kyiv','farynaihor@gmail.com'),
  (6,'Kurylo','Volodymyr','Poltava','kurylo.volodymyr@gmail.com'),
  (7,'Matskiv','Marian','Herson','marian3912788@gmail.com'),
  (8,'Shyika','Tamara','Kyiv','tamara.shyika@gmail.com'),
  (9,'Tkachyk','Volodymyr','Ternopil','vova1234.tkachik@gmail.com');

INSERT INTO readersbook VALUES (4,1),(5,1),(8,1),(2,2),(6,2),(7,2),(1,3),(1,4),(9,4),(3,5);

DELIMITER //
CREATE PROCEDURE InsertPersonBook
  (
    IN SurmanePersonIn varchar(25),
    IN BookNameIN varchar(45)
  )
  BEGIN
    DECLARE msg varchar(40);


    IF NOT EXISTS( SELECT * FROM Reader WHERE Surname=SurmanePersonIn)
    THEN SET msg = 'This Surname is absent';

ELSEIF NOT EXISTS( SELECT * FROM Book WHERE BookName=BookNameIN)
THEN SET msg = 'This Book is absent';
checks if there are this combination already
ELSEIF EXISTS( SELECT * FROM readersbook
WHERE ID_Reader = (SELECT ID_Reader FROM Reader WHERE Surname=SurmanePersonIn)
AND ID_Book = (SELECT ID_Book FROM Book WHERE BookName=BookNameIN)
)
THEN SET msg = 'This Person already has this book';


ELSEIF (SELECT Amount FROM Book WHERE BookName=BookNameIN )
(SELECT COUNT(*) FROM readersbook WHERE ID_Book=(SELECT ID_Book FROM Book WHERE BookName=BookNameIN) )
THEN SET msg = 'There are no this Book already';

— makes insert
ELSE
INSERT readersbook (ID_Reader, ID_Book)
  Value ( (SELECT ID_Reader FROM Reader WHERE Surname=SurmanePersonIn),
          (SELECT ID_Book FROM Book WHERE BookName=BookNameIN) );
SET msg = 'OK';

END IF;

SELECT msg AS msg;

END //
DELIMITER ;