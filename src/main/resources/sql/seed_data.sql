INSERT INTO categories (name) VALUES ('Electronics');
INSERT INTO categories (name) VALUES ('Books');
INSERT INTO categories (name) VALUES ('Clothing');

-- Insert Default User
INSERT INTO users (user_id, email, password_hash, role) VALUES (1, 'admin@example.com', 'hashed_password', 'admin');

INSERT INTO products (category_id, name, price, stock_quantity) VALUES (1, 'Laptop', 1200.00, 10);
INSERT INTO products (category_id, name, price, stock_quantity) VALUES (1, 'Smartphone', 800.00, 20);
INSERT INTO products (category_id, name, price, stock_quantity) VALUES (2, 'Java Programming', 45.00, 50);
INSERT INTO products (category_id, name, price, stock_quantity) VALUES (3, 'T-Shirt', 15.00, 100);
