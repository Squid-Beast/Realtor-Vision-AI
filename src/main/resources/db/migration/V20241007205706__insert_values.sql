-- Inserting records into listing_details
INSERT INTO listing_details (bathrooms, bedrooms, square_feet, price)
VALUES
    (2, 3, 1500, 350000.00),
    (3, 4, 2200, 475000.00),
    (1, 2, 1200, 275000.00),
    (4, 5, 3000, 620000.00),
    (2, 3, 1800, 399000.00);

-- Inserting records into address
INSERT INTO address (full_name, mobile, email, street_address_1, street_address_2, city, state, zip_code)
VALUES
    ('Joe Goldberg', '8549651549', 'joegoldberg@gmail.com', '123 Maple Street', NULL, 'Los Angeles', 'California', '90001'),
    ('Barney Stinson', '2543697458', 'barney125@gmail.com', '456 Oak Avenue', 'Apt 3B', 'Chicago', 'Illinois', '60601'),
    ('Mike Ross', '4589657702', 'mikeross@yahoo.com', '789 Pine Road', NULL, 'Houston', 'Texas', '77001'),
    ('Harvey Spector', '2548963002', 'harveyspectorad@gmail.com', '1010 Cedar Blvd', 'Suite 500', 'Miami', 'Florida', '33101'),
    ('William Davis', '2142368540', 'williamdav@gmail.com', '2020 Birch Lane', NULL, 'Seattle', 'Washington', '98101');

-- Inserting records into marketing_order
INSERT INTO marketing_order (package_name, agent_id, coordinator_id, address_id, listing_details_id)
VALUES
    ('SILVER', 101, 201, 1, 1),  -- Ensure agent_id and coordinator_id exist
    ('PLATINUM', 102, 202, 2, 2),
    ('SILVER', 103, 203, 3, 3),
    ('PLATINUM', 104, 204, 4, 4),
    ('SILVER', 105, 205, 5, 5);

-- Inserting records into image_details
INSERT INTO image_details (image_url, is_deleted, marketing_order_id, hashtags)
VALUES
    (NULL, FALSE, 1, NULL),  -- Ensure marketing_order_id exists
    (NULL, FALSE, 2, NULL),
    (NULL, FALSE, 3, NULL),
    (NULL, FALSE, 4, NULL),
    (NULL, FALSE, 5, NULL);
