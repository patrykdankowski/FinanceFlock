CREATE TABLE expanses (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_id BIGINT NOT NULL,
                          amount DECIMAL(9,2) NOT NULL,
                          expense_date DATETIME NOT NULL,
                          description VARCHAR(255),
                          location VARCHAR(255),
                          CONSTRAINT fk_user_expanses FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
