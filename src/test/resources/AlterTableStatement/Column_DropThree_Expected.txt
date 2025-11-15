IF COL_LENGTH ('table_name','column1_name') IS NOT NULL
BEGIN
    ALTER TABLE [table_name]
        DROP COLUMN [column1_name];
END;

IF COL_LENGTH ('table_name','column2_name') IS NOT NULL
BEGIN
    ALTER TABLE [table_name]
        DROP COLUMN [column2_name];
END;

IF COL_LENGTH ('table_name','column3_name') IS NOT NULL
BEGIN
    ALTER TABLE [table_name]
        DROP COLUMN [column3_name];
END;
