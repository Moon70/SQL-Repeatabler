IF EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('dbo.table_name')
            AND c.name = 'column_name'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('dbo.table_name')
            AND c.name = 'new_column_name'
    )
BEGIN
    EXEC sp_rename 'dbo.table_name.column_name',
                   'new_column_name',
                   'COLUMN';
END;
