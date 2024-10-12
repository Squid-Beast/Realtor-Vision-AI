CREATE TABLE listing_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    bathrooms INT NOT NULL,
    bedrooms INT NOT NULL,
    square_feet INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL
);

CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    mobile VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    street_address_1 VARCHAR(255) NOT NULL,
    street_address_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_code VARCHAR(10) NOT NULL
);

CREATE TABLE marketing_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    package_name ENUM('SILVER', 'PLATINUM') NOT NULL,
    agent_id BIGINT NOT NULL,
    coordinator_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    listing_details_id BIGINT NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id),
    FOREIGN KEY (listing_details_id) REFERENCES listing_details(id)
);

CREATE INDEX idx_agent_id ON marketing_order(agent_id);
CREATE INDEX idx_coordinator_id ON marketing_order(coordinator_id);

CREATE TABLE image_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    image_url TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    marketing_order_id BIGINT NOT NULL,
    hashtags VARCHAR(255),
    FOREIGN KEY (marketing_order_id) REFERENCES marketing_order(id)
);
