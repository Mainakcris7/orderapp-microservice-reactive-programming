CREATE DATABASE IF NOT EXISTS order_booking_db;
USE order_booking_db;

CREATE TABLE orders(
	order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    customer_id INT,
    quantity INT,
    total_price INT,
    order_time TIMESTAMP default CURRENT_TIMESTAMP
);

CREATE TABLE products(
	product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(20),
    stock_quantity INT,
    price INT
);

CREATE TABLE customers(
	customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(100),
    gender VARCHAR(10) CHECK(gender IN ('MALE', 'FEMALE'))
);

CREATE TABLE customer_orders(
	id INT AUTO_INCREMENT PRIMARY KEY,
	order_id INT,
    customer_id INT,
    FOREIGN KEY(customer_id) REFERENCES customers(customer_id)
);

SELECT * FROM products;
SELECT * FROM orders;
SELECT * FROM customers;
SELECT * FROM customer_orders;

-- DROP TABLE orders;  
-- DROP TABLE products;  
-- DROP TABLE customers;  
-- DROP TABLE customer_orders;