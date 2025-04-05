DELETE
FROM shopping_carts
WHERE user_id = 4;

DELETE
FROM users
WHERE users.id = 4
  AND first_name = 'TestUserName1'
  AND last_name = 'TestLastName1'
  AND email = 'testUser@Gmail.com'
  AND shipping_address = 'TesShippingAddress 1';
