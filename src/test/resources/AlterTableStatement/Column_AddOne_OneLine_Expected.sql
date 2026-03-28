IF COL_LENGTH ('table_name','COLUMN1_NAME') IS NULL
BEGIN
    ALTER TABLE [table_name]
        ADD [COLUMN1_NAME] nvarchar(255);
END;
