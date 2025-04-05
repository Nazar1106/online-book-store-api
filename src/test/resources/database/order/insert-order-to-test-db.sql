INSERT INTO orders
VALUES (1, 1, 'PENDING', 500, TIME(NOW()), 'testShippingAddress', false),
       (2, 1, 'COMPLETED', 200, TIME(NOW()), 'TEST ADDRESS 1', false),
       (3, 2, 'DELIVERED', 350, TIME(NOW()), 'TEST ADDRESS 3', false);
