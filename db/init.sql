
CREATE SEQUENCE product_sequence START 1;

CREATE TABLE Products (
    product_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    overall_quantity INT DEFAULT 0
);

CREATE OR REPLACE FUNCTION generate_product_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.product_id := 'PR' || NEXTVAL('product_sequence');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER product_id_trigger
BEFORE INSERT ON Products
FOR EACH ROW
EXECUTE FUNCTION generate_product_id();

-- Create a sequence for the warehouse ID
CREATE SEQUENCE warehouse_seq START 1;

CREATE TABLE Warehouses (
    warehouse_id VARCHAR(10) PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    zipcode VARCHAR(10) NOT NULL
);

CREATE OR REPLACE FUNCTION generate_warehouse_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.warehouse_id := 'WH' || NEXTVAL('warehouse_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER warehouse_id_trigger
BEFORE INSERT ON Warehouses
FOR EACH ROW
EXECUTE FUNCTION generate_warehouse_id();


CREATE TABLE Stock (
    stock_id VARCHAR(10) PRIMARY KEY,
    warehouse_id VARCHAR(10), 
    product_id VARCHAR(10),   
    quantity INT NOT NULL DEFAULT 0,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);


CREATE SEQUENCE stock_seq START 1;


CREATE OR REPLACE FUNCTION generate_stock_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.stock_id := 'S' || NEXTVAL('stock_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER stock_id_trigger
BEFORE INSERT ON Stock
FOR EACH ROW
EXECUTE FUNCTION generate_stock_id();


CREATE SEQUENCE order_seq START 1;

CREATE TABLE Orders (
    order_id VARCHAR(10) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    mobile_no VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    zipcode VARCHAR(10) NOT NULL
);

CREATE OR REPLACE FUNCTION generate_order_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.order_id := 'O' || NEXTVAL('order_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER order_id_trigger
BEFORE INSERT ON Orders
FOR EACH ROW
EXECUTE FUNCTION generate_order_id();


CREATE SEQUENCE order_item_seq START 1;

CREATE TABLE Order_Items (
    order_item_id VARCHAR(10) PRIMARY KEY,
    order_id VARCHAR(10), 
    product_id VARCHAR(10),
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

CREATE OR REPLACE FUNCTION generate_order_item_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.order_item_id := 'OR' || NEXTVAL('order_item_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER order_item_id_trigger
BEFORE INSERT ON Order_Items
FOR EACH ROW
EXECUTE FUNCTION generate_order_item_id();

-- Table: public.users
CREATE TABLE IF NOT EXISTS public.users
(
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_status VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(255) NOT NULL,
    warehouse_id VARCHAR(255),


);

-- Set the owner of the table
ALTER TABLE IF EXISTS public.users
    OWNER TO postgres;

-- Sequence: user_seq
CREATE SEQUENCE IF NOT EXISTS user_seq
    START 1;

-- Function: public.generate_user_id
CREATE OR REPLACE FUNCTION public.generate_user_id()
RETURNS TRIGGER AS $$
BEGIN
    NEW.user_id := 'U' || NEXTVAL('user_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: user_id_trigger
CREATE OR REPLACE TRIGGER user_id_trigger
    BEFORE INSERT
    ON public.users
    FOR EACH ROW
    EXECUTE FUNCTION public.generate_user_id();




CREATE SEQUENCE warehouse_stock_deduction_seq START 1;

CREATE TABLE WarehouseStockDeduction (
    deduction_id VARCHAR(15) PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    warehouse_id VARCHAR(10) NOT NULL,
    quantity_deducted INT NOT NULL,
    deduction_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION generate_deduction_id() RETURNS TRIGGER AS $$
BEGIN
    NEW.deduction_id := 'WSD' || LPAD(NEXTVAL('warehouse_stock_deduction_seq')::TEXT, 10, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER deduction_id_trigger
BEFORE INSERT ON WarehouseStockDeduction
FOR EACH ROW
EXECUTE FUNCTION generate_deduction_id();


CREATE OR REPLACE FUNCTION update_overall_quantity_after_insert() 
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Products
    SET overall_quantity = (SELECT SUM(quantity) FROM Stock WHERE product_id = NEW.product_id)
    WHERE product_id = NEW.product_id;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_overall_quantity_after_insert
AFTER INSERT ON Stock
FOR EACH ROW
EXECUTE FUNCTION update_overall_quantity_after_insert();


CREATE OR REPLACE FUNCTION update_overall_quantity_after_update() 
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Products
    SET overall_quantity = (SELECT SUM(quantity) FROM Stock WHERE product_id = NEW.product_id)
    WHERE product_id = NEW.product_id;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_overall_quantity_after_update
AFTER UPDATE ON Stock
FOR EACH ROW
EXECUTE FUNCTION update_overall_quantity_after_update();


CREATE OR REPLACE FUNCTION update_overall_quantity_after_delete() 
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Products
    SET overall_quantity = (SELECT SUM(quantity) FROM Stock WHERE product_id = OLD.product_id)
    WHERE product_id = OLD.product_id;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_overall_quantity_after_delete
AFTER DELETE ON Stock
FOR EACH ROW
EXECUTE FUNCTION update_overall_quantity_after_delete();




INSERT INTO Products (name, price, overall_quantity)
VALUES 
('Apple', 10.00, 0),
('Banana', 20.00, 0),
('Carrot', 30.00, 0);


INSERT INTO Warehouses (location, capacity, zipcode)
VALUES 
('Edinburgh', 1000, "EH4 4DT"),
('Glasgow', 800, "EH4 4DT"),
('NewCastle', 1200, "EH4 4DT");

 Select * from Products;
INSERT INTO Stock (warehouse_id, product_id, quantity)
VALUES
('WH4', 'PR4', 100), 
('WH5', 'PR4', 150),  
('WH4', 'PR5', 200),  
('WH6', 'PR5', 300),  
('WH6', 'PR6', 500);

INSERT INTO Orders (email, mobile_no, address, zipcode)
VALUES 
('abhi@gmail.com', '0123456789', '123 Main St', '12345'),
('avengers@example.com', '0987654321', '456 Oak St', '67890');

INSERT INTO Order_Items (order_id, product_id, quantity)
VALUES
('O1', 'PR4', 2),  
('O1', 'PR5', 3),  
('O2', 'PR6', 1);

INSERT INTO public.users (name, email, password, role)
VALUES ('John ', 'john@gmail.com', 'securepassword', 'admin');


select * from products;


SELECT warehouse_id, product_id, quantity FROM Stock WHERE product_id = 'PR4';
UPDATE Stock SET quantity = 50 WHERE warehouse_id = 'WH4' AND product_id = 'PR4';


SELECT last_value FROM stock_seq;
ALTER SEQUENCE stock_seq RESTART WITH 1;
