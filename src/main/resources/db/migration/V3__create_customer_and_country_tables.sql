CREATE TABLE countries (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dial VARCHAR(5) NOT NULL,
    code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(500) NOT NULL UNIQUE,
    email VARCHAR(500) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    tax_number VARCHAR(20) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    is_active TINYINT(1) NOT NULL,
    country_id VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES countries(id)
);

INSERT INTO countries (id, code, name, dial) VALUES
('COU5Ddf93JF', 'VN', 'Vietnam', '+84'),
('COU8Axk21YT', 'US', 'United States', '+1'),
('COUb97Wq0MZ', 'GB', 'United Kingdom', '+44'),
('COU3LnVd8EP', 'JP', 'Japan', '+81'),
('COUZvKs47QD', 'FR', 'France', '+33'),
('COUsYl8v52A', 'DE', 'Germany', '+49'),
('COUhEtLx13F', 'CN', 'China', '+86'),
('COUt9NcZ3MG', 'KR', 'South Korea', '+82'),
('COUeB72XfDL', 'IN', 'India', '+91'),
('COUgPj8MwXK', 'TH', 'Thailand', '+66'),
('COUa1LN4uVT', 'SG', 'Singapore', '+65'),
('COUyRfZ0cWB', 'MY', 'Malaysia', '+60'),
('COUqTNZl6OE', 'CA', 'Canada', '+1'),
('COUxML93KJW', 'AU', 'Australia', '+61');