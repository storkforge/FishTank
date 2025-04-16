
-- Insert sex
INSERT INTO sex (name)
VALUES
    ('Male'),
    ('Female'),
    ('Hermaphrodite');

-- Insert Access

INSERT INTO access (name)
VALUES
    ('Standard'),
    ('Premium');

--Insert WaterType

INSERT INTO watertype (name)
VALUES
    ('Salt Water'),
    ('Fresh Water');

--Insert AppUsers

INSERT INTO appuser (name, password_hash, email, authentication_code, appuser_access_id_fk)
VALUES
    ('John Doe', 'hashedpassword1', 'johndoe@example.com', 'testUser1', 1),  -- Standard
    ('Jane Smith', 'hashedpassword2', 'janesmith@example.com', 'testUser2',2),  -- Premium
    ('Alice Johnson', 'hashedpassword3', 'alicejohnson@example.com', 'testUser3',1),  -- Standard
    ('Bob Brown', 'hashedpassword4', 'bobbrown@example.com', 'testUser4',2),  -- Premium
    ('Charlie White', 'hashedpassword5', 'charliewhite@example.com', 'testUser5',1);  -- Standard

--Insert Fishes

INSERT INTO fish (name, species, description, fish_watertype_id_fk, fish_sex_id_fk, fish_appuser_id_fk)
VALUES
    ('Clownfish', 'Amphiprioninae', 'Bright orange fish with white bands, popular in aquariums.', 1, 1, 1),  -- Salt Water, Male, John Doe
    ('Goldfish', 'Carassius auratus', 'Small freshwater fish, often kept as a pet.', 2, 2, 2),  -- Fresh Water, Female, Jane Smith
    ('Betta Fish', 'Betta splendens', 'Colorful fish, often known for aggressive behavior.', 2, 1, 3),  -- Fresh Water, Male, Alice Johnson
    ('Shark', 'Carcharhinidae', 'Large predatory fish found in saltwater.', 1, 1, 4),  -- Salt Water, Male, Bob Brown
    ('Angelfish', 'Pterophyllum', 'Small, freshwater fish known for its distinct triangular shape.', 2, 2, 5);  -- Fresh Water, Female, Charlie White

INSERT INTO post (posttext, post_fish_id_fk)
VALUES
    ('Clownfish Habitat Update', 1),  -- Linked to the Clownfish (ID 1)
    ('Betta Fish Care Tips', 3),      -- Linked to the Betta Fish (ID 3)
    ('Shark Feeding Habits', 4);      -- Linked to the Shark (ID 4)

INSERT INTO event (eventtext, cityname, eventdate, event_appuser_id_fk)
VALUES
    ('Clownfish Breeding Workshop', 'New York', '2023-10-15 10:00:00', 1),  -- John Doe
    ('Goldfish Care Seminar', 'Los Angeles', '2023-11-20 14:00:00', 2),  -- Jane Smith
    ('Aquarium Setup Class', 'Chicago', '2023-12-05 09:30:00', 3);  -- Alice Johnson
