CREATE TRIGGER update_user_group_id_in_expense_summary
    AFTER UPDATE ON users
    FOR EACH ROW
BEGIN
    IF OLD.group_id IS NULL AND NEW.group_id IS NOT NULL THEN
    UPDATE user_expenses_summary
    SET group_id = NEW.group_id
    WHERE user_id = NEW.id;
END IF;
END;
