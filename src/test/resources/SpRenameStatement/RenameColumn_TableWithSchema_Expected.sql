IF EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('abc.table_name')
            AND c.name = 'column_name'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('abc.table_name')
            AND c.name = 'new_column_name'
    )
BEGIN
    EXEC sp_rename @objname = 'abc.table_name.column_name',
                   @newname = 'new_column_name',
                   @objtype = 'COLUMN';
END;
