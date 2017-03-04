DROP TABLE IF EXISTS book;
CREATE TABLE book(
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL UNIQUE,
  rating INT,
  release_date DATE,
  lang VARCHAR(40),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS author;
CREATE TABLE author(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(60) NOT NULL,
  surname VARCHAR(60) NOT NULL,
  birth_date DATE,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS book_author;
CREATE TABLE book_author(
  book_id INT NOT NULL,
  author_id INT NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (author_id) REFERENCES author(id)
);