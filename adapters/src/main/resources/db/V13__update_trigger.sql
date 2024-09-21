CREATE TRIGGER update_expense_summary_after_update
    AFTER UPDATE ON expanses
    FOR EACH ROW
BEGIN
    UPDATE user_expenses_summary
    SET total_expenses = total_expenses - OLD.amount + NEW.amount
    WHERE user_id = NEW.user_id;
END;

