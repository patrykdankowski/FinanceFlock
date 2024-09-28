CREATE TRIGGER create_expense_summary_after_user_insert
    AFTER INSERT ON users
    FOR EACH ROW
BEGIN
    INSERT INTO user_expenses_summary (user_id, total_expenses)
    VALUES (NEW.id, 0);
END;
