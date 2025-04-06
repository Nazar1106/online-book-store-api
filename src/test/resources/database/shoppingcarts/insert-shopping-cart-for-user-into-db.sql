INSERT INTO shopping_carts (id, user_id, is_deleted)
VALUES (1, 1, false),
       (2, 2, false);

INSERT INTO cart_items (shopping_cart_id, book_id, quantity)
VALUES (1, 1, 20),
       (2, 2, 30);

