INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'NewBookTitle1', 'NewBookAuthor1', 'NewBookIsbn1', 150, 'NewBookDescription1', 'NewCoverImage1', FALSE),
       (2, 'NewBookTitle2', 'NewBookAuthor2', 'NewBookIsbn2', 120, 'NewBookDescription2', 'NewCoverImage2', FALSE),
       (3, 'NewBookTitle3', 'NewBookAuthor3', 'NewBookIsbn3', 200, 'NewBookDescription3', 'NewCoverImage3', FALSE);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 1),
       (3, 2);
