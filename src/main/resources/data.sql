-- ================================================================
-- TUS - Technology University Shannon - Athlone
-- Object-Oriented Programming I - (AL_KCNCM_9_1) 29468
-- Candidate: Weverton de Souza Castanho
-- Email: wevertonsc@gmail.com
-- Data: 29.NOVEMBER.2025
-- ================================================================
-- Initial Database Load Script
-- ================================================================

-- Insert Messages (Error and Success codes)
INSERT INTO messages (id, message, description) VALUES (1, 'ERROR', 'Invalid card number');
INSERT INTO messages (id, message, description) VALUES (2, 'ERROR', 'Invalid cardholder information');
INSERT INTO messages (id, message, description) VALUES (3, 'ERROR', 'Invalid expiration date');
INSERT INTO messages (id, message, description) VALUES (4, 'ERROR', 'Invalid CVV');
INSERT INTO messages (id, message, description) VALUES (5, 'ERROR', 'Insufficient balance');
INSERT INTO messages (id, message, description) VALUES (6, 'SUCCESS', 'Operation completed successfully');

-- Insert Brands
INSERT INTO brand (id, name) VALUES (1, 'Visa');
INSERT INTO brand (id, name) VALUES (2, 'Mastercard');
INSERT INTO brand (id, name) VALUES (3, 'American Express');
INSERT INTO brand (id, name) VALUES (4, 'Discover');

-- Insert Clients
INSERT INTO client (id, name, email) VALUES (1, 'Plato of Athens', 'plato.of.athens@macunaima.com');
INSERT INTO client (id, name, email) VALUES (2, 'Aristotle of Greece', 'aristotle.of.greece@macunaima.com');
INSERT INTO client (id, name, email) VALUES (3, 'Carl Sagan', 'carl.sagan@macunaima.com');
INSERT INTO client (id, name, email) VALUES (4, 'Richard Feynman', 'richard.fyenman@macunaima.com');

-- Insert Cards
-- Card 1: Plato of Athens - Visa (Primary test card)
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (1, '4532015112830366', '12/25', '123', 5000.0, 3000.0, 1, 1);

-- Card 2: Plato of Athena's - Mastercard
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (2, '5425233430109903', '11/24', '456', 3000.0, 2000.0, 1, 2);

-- Card 3: Aristotle of Greece - Visa (High balance)
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (3, '4916338506082832', '09/26', '789', 10000.0, 8500.0, 2, 1);

-- Card 4: Aristotle of Greece - Mastercard
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (4, '5555555555554444', '03/27', '321', 7500.0, 5000.0, 2, 2);

-- Card 5: Plato of Athens - Visa (Low balance for testing insufficient funds)
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (5, '4024007178460741', '06/25', '654', 2000.0, 150.0, 1, 1);

-- Card 6: Carl Sagan - Visa
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (6, '4532261615476013', '08/26', '147', 6000.0, 4200.0, 3, 1);

-- Card 7: Richard Feynman - Mastercard
INSERT INTO card (id, number, expiration, cvv, limits, balance, client_id, brand_id) 
VALUES (7, '5105105105105100', '12/26', '258', 4500.0, 3300.0, 4, 2);

-- ================================================================
-- Summary of Initial Data:
-- - 6 Messages (1-5: Errors, 6: Success)
-- - 4 Brands (Visa, Mastercard, Amex, Discover)
-- - 4 Clients (John, Jane, Bob, Alice)
-- - 7 Cards (Various balances for testing)
-- ================================================================