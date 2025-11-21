IF COL_LENGTH ('table_name','column1_name') IS NULL
BEGIN
    ALTER TABLE [table_name]
        ADD [column1_name] nvarchar(255);
END;
