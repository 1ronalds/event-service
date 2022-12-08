use eventdb;

DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
user_id bigint(20) NOT NULL AUTO_INCREMENT,
username varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL UNIQUE,
email varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL UNIQUE,
password varchar(20)CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
name varchar(20)CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
surname varchar(20)CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
role varchar(6)CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE events (
event_id bigint(20) NOT NULL AUTO_INCREMENT,
event_title varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
event_description varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
event_location_country varchar(25)  CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
event_location_city varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
organiser_id bigint(20) NOT NULL,
max_attendance int NOT NULL,
event_datetime datetime NOT NULL,
attendee_count int DEFAULT '0',
PRIMARY KEY (event_id),
FOREIGN KEY (organiser_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE attendance (
id bigint(20) NOT NULL AUTO_INCREMENT,
event_id bigint(20) NOT NULL,
user_id bigint NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (event_id) REFERENCES events(event_id),
FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;