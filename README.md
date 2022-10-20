This is my P1 project for Revature.

#Description
Set up the backend for an online store to buy items. Users can register and log in to add items to their cart. Then they can place orders. Registered users are stored in the Users table. Items that users can buy are stored in the Items table. User's carts are stored in the Cart table. Users orders are stored in the Orders table. Features to be Implemented [ You can add also some other features ] Register - register new users Login/Logout - login and logout users Get Users - get all users and their cart Get Items - gets all items that are available in store Add Item to Cart - adds selected item to the cart

#Project Requirements

Register User
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/register
-POST
-enter variables in User

User Login
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/login/{username}/{password}
-POST
-enter details you have registered in register user

Get All Users
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/getusersandcarts
-GET
- please login as an admin

Adding Item
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/additem
-POST
-enter variables {"itemId", "itemName", "qoh", "price"}

Add Product to Cart
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/addproducttocart/{useriD}/{password}
-PUT
- enter variables {"userId", "itemId"}

Get All Items
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/getitemsinstock
-GET

Delete Item
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/deleteitem/{itemiD}
-DELETE
-enter variable {"itemId"}

Delete User
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/deleteuser/{userId}
-DELETE
-enter variable {"userId"}

Logout
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/logout
-POST
