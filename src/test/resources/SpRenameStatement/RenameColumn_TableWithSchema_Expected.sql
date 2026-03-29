IF EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('abc.table_name')
            AND c.name = 'COLUMN_NAME'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM sys.columns c
        WHERE c.object_id = OBJECT_ID('abc.table_name')
            AND c.name = 'NEW_COLUMN_NAME'
    )
BEGIN
    EXEC sp_rename @objname = 'abc.table_name.COLUMN_NAME',
                   @newname = 'NEW_COLUMN_NAME',
                   @objtype = 'COLUMN';
END;
