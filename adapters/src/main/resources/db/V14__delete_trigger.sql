CREATE TRIGGER update_expense_summary_after_delete
    AFTER DELETE ON expanses
    FOR EACH ROW
BEGIN
    UPDATE user_expenses_summary
    SET total_expenses = total_expenses - OLD.amount
    WHERE user_id = OLD.user_id;
END;
