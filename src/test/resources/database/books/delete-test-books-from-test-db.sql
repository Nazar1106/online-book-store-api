DELETE FROM books_categories
       WHERE book_id IN (SELECT id FROM books
                                   WHERE title = 'NewBookTitle1'
                                     AND author = 'NewBookAuthor1'
                                     AND isbn = 'NewBookIsbn1'
                                     AND price = 150);
DELETE FROM books
       WHERE title = 'NewBookTitle2'
         AND author = 'NewBookAuthor2'
         AND isbn = 'NewBookIsbn2'
         AND price = 100;
