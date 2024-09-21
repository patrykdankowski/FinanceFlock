CREATE TABLE budget_groups (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               description VARCHAR(255),
                               owner_id BIGINT,
                               CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL
);
