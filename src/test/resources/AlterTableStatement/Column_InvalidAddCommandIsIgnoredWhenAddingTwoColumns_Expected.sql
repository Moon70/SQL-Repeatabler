IF COL_LENGTH ('table_foo','COLUMN1_NAME') IS NULL
BEGIN
    ALTER TABLE [table_foo]
        ADD [COLUMN1_NAME] nvarchar(255);
END;

IF COL_LENGTH ('table_foo','COLUMN2_NAME') IS NULL
BEGIN
    ALTER TABLE [table_foo]
        ADD [COLUMN2_NAME] int;
END;
