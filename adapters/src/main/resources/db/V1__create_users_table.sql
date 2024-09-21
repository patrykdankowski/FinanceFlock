CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       last_logged_in_at DATETIME,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       role VARCHAR(50),
                       group_id BIGINT,
                       share_data BOOLEAN,
                       last_toggled_share_data DATETIME
);
