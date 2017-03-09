CREATE TABLE user
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARBINARY(256) NOT NULL,
    salt VARBINARY(64) NOT NULL
);
CREATE UNIQUE INDEX user_username_uindex ON user (username);

CREATE TABLE Category
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX Category_name_uindex ON Category (name);

CREATE TABLE author
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX author_name_uindex ON author (name);

CREATE TABLE book
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    language_iso2 VARCHAR(2) NOT NULL,
    pageCount INT(4) NOT NULL,
    retailPrice FLOAT
);
CREATE UNIQUE INDEX book_title_uindex ON book (title);

CREATE TABLE author_book
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    author_id INT(4),
    book_id INT(4),
    CONSTRAINT author_book_author_id_fk FOREIGN KEY (author_id) REFERENCES author (id),
    CONSTRAINT author_book_book_id_fk FOREIGN KEY (book_id) REFERENCES book (id)
);
CREATE INDEX author_book_author_id_fk ON author_book (author_id);
CREATE INDEX author_book_book_id_fk ON author_book (book_id);

CREATE TABLE reading_activity
(
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    user_id INT(4) NOT NULL,
    book_id INT(4) NOT NULL,
    start_reading DATETIME,
    end_reading DATETIME,
    bookmark INT(6),
    CONSTRAINT reading_activity_user_id_fk FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT reading_activity_book_id_fk FOREIGN KEY (book_id) REFERENCES book (id)
);
CREATE INDEX reading_activity_book_id_fk ON reading_activity (book_id);
CREATE INDEX reading_activity_user_id_fk ON reading_activity (user_id);

CREATE TABLE reading_diary_entry
(
    activity_id INT(4) NOT NULL,
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME NOT NULL,
    entry VARCHAR(4096),
    CONSTRAINT reading_diary_reading_activity_id_fk FOREIGN KEY (activity_id) REFERENCES reading_activity (id) ON DELETE CASCADE
);
CREATE INDEX reading_diary_reading_activity_id_fk ON reading_diary_entry (activity_id);

CREATE TABLE reading_interest
(
    user_id INT(4),
    category_id INT(4),
    id INT(4) PRIMARY KEY AUTO_INCREMENT,
    CONSTRAINT reading_interest_user_id_fk FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT reading_interest_Category_id_fk FOREIGN KEY (category_id) REFERENCES Category (id)
);
CREATE INDEX reading_interest_user_id_fk ON reading_interest (user_id);
CREATE INDEX reading_interest_Category_id_fk ON reading_interest (category_id);
