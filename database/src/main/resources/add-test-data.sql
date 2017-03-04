INSERT INTO book (title,rating,release_date,lang) VALUES
  ('1984',89,'2014-01-01','Eng'),
  ('Brave New World',85,'2016-01-02','Eng'),
  ('The Road to Wigan Pier',50,'2015-01-01','Eng'),
  ('The Doors of Perception',54,'2015-02-02','Eng');

INSERT INTO author(name,surname,birth_date) VALUES
  ('George','Orwell','2010-01-01'),
  ('Aldous','Haxley','2009-01-01'),
  ('Vlad','Safronov','1998-08-22');

INSERT INTO book_author (book_id,author_id) VALUES
  (1,1),
  (2,2),
  (3,1),
  (4,2);