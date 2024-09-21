CREATE TABLE user_expenses_summary
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT NOT NULL,
    group_id       BIGINT,
    total_expenses DECIMAL(18, 2) DEFAULT 0,
--     share_data     BOOLEAN        DEFAULT TRUE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES budget_groups (id)
);