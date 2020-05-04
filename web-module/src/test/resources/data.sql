INSERT INTO article (title, content, date)
VALUES ('test title', 'test content', '2020-05-01');
INSERT INTO item (id, unique_number, name, price)
VALUES (1, 'testUniqueNumber', 'Watermelon', 13.30);
INSERT INTO item_details (item_id, description)
VALUES ((SELECT id FROM item WHERE name = 'Watermelon'), 'test item description');
INSERT INTO ordering (date, status, amount, price, ordered_item_id, customer_id)
VALUES ('2020-05-03','NEW',5,10,1,2);