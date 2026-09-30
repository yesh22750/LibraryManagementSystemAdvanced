INSERT IGNORE INTO categories(category_name,description) VALUES ('Programming','Software development'),('Database','Database systems'),('Fiction','Fiction'),('Science','Science and technology'),('Business','Business');
INSERT INTO authors(author_name,country) SELECT 'Robert C. Martin','USA' WHERE NOT EXISTS (SELECT 1 FROM authors WHERE author_name='Robert C. Martin');
INSERT INTO authors(author_name,country) SELECT 'Thomas H. Cormen','USA' WHERE NOT EXISTS (SELECT 1 FROM authors WHERE author_name='Thomas H. Cormen');
INSERT INTO authors(author_name,country) SELECT 'Abraham Silberschatz','USA' WHERE NOT EXISTS (SELECT 1 FROM authors WHERE author_name='Abraham Silberschatz');
INSERT INTO authors(author_name,country) SELECT 'J. K. Rowling','UK' WHERE NOT EXISTS (SELECT 1 FROM authors WHERE author_name='J. K. Rowling');
INSERT INTO users(full_name,email,phone,membership_date) SELECT 'Arun Kumar','arun@example.com','9876543210','2026-01-10' WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='arun@example.com');
INSERT INTO users(full_name,email,phone,membership_date) SELECT 'Priya Sharma','priya@example.com','9876543211','2026-02-15' WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='priya@example.com');
INSERT INTO users(full_name,email,phone,membership_date) SELECT 'Rahul Kumar','rahul@example.com','9876543212','2026-03-01' WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='rahul@example.com');
INSERT INTO books(title,isbn,category_id,author_id,publication_year,total_copies,available_copies,shelf_location)
SELECT 'Clean Code','9780132350884',c.category_id,a.author_id,2008,4,4,'P-01' FROM categories c,authors a WHERE c.category_name='Programming' AND a.author_name='Robert C. Martin' AND NOT EXISTS(SELECT 1 FROM books WHERE isbn='9780132350884');
INSERT INTO books(title,isbn,category_id,author_id,publication_year,total_copies,available_copies,shelf_location)
SELECT 'Introduction to Algorithms','9780262046305',c.category_id,a.author_id,2022,3,3,'P-02' FROM categories c,authors a WHERE c.category_name='Programming' AND a.author_name='Thomas H. Cormen' AND NOT EXISTS(SELECT 1 FROM books WHERE isbn='9780262046305');
INSERT INTO books(title,isbn,category_id,author_id,publication_year,total_copies,available_copies,shelf_location)
SELECT 'Database System Concepts','9780078022159',c.category_id,a.author_id,2019,3,3,'D-01' FROM categories c,authors a WHERE c.category_name='Database' AND a.author_name='Abraham Silberschatz' AND NOT EXISTS(SELECT 1 FROM books WHERE isbn='9780078022159');
