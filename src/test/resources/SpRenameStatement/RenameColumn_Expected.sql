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
    EXEC sp_rename @objname = 'dbo.table_name.column_name',
                   @newname = 'new_column_name',
                   @objtype = 'COLUMN';
END;
