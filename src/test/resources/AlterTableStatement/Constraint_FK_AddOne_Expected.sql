IF NOT EXISTS (
    SELECT 1
    FROM sys.foreign_keys
    WHERE name = 'constraint1_name'
        AND parent_object_id = OBJECT_ID('table1_name')
)
BEGIN
    ALTER TABLE [table1_name]
        ADD CONSTRAINT [constraint1_name]
        FOREIGN KEY ([column1_ID])
        REFERENCES [table2_name] ([ID]);
END;
