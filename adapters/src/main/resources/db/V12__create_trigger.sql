CREATE TRIGGER update_expense_summary_after_insert
    AFTER INSERT ON expanses
    FOR EACH ROW
BEGIN
    UPDATE user_expenses_summary
    SET total_expenses = total_expenses + NEW.amount
    WHERE user_id = NEW.user_id;
END;
