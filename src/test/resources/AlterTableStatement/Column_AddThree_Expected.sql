IF COL_LENGTH ('table_name','column1_name') IS NULL
BEGIN
    ALTER TABLE [table_name]
        ADD [column1_name] INT NULL;
END;

IF COL_LENGTH ('table_name','column2_name') IS NULL
BEGIN
    ALTER TABLE [table_name]
        ADD [column2_name] NVARCHAR(100) NULL;
END;

IF COL_LENGTH ('table_name','column3_name') IS NULL
BEGIN
    ALTER TABLE [table_name]
        ADD [column3_name] DATETIME NULL;
END;
