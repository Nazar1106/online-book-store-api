delete
from shopping_carts
where user_id = 4;

delete
from users
where users.id = 4
  AND first_name = 'TestUserName1'
  AND last_name = 'TestLastName1'
  AND email = 'testUser@Gmail.com'
  AND shipping_address = 'TesShippingAddress 1';
