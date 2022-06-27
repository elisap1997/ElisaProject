This is my P1 project for Revature.

#Description
Set up the backend for an online store to buy items. Users can register and log in to add items to their cart. Then they can place orders. Registered users are stored in the Users table. Items that users can buy are stored in the Items table. User's carts are stored in the Cart table. Users orders are stored in the Orders table. Features to be Implemented [ You can add also some other features ] Register - register new users Login/Logout - login and logout users Get Users - get all users and their cart Get Items - gets all items that are available in store Add Item to Cart - adds selected item to the cart

#Project Requirements

Register User
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/register

User Login
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/login/{username}/{password}

Get All Users
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/getusersandcarts

Adding Item
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/additem

Add Product to Cart
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/addproducttocart/{useriD}/{password}

Get All Items
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/getitemsinstock

Delete Item
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/deleteitem/{itemiD}

Delete User
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/deleteuser/{userId}

Logout
http://ec2-52-202-91-226.compute-1.amazonaws.com:8084/logout

