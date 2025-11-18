IF NOT EXISTS (
    SELECT 1
    FROM sys.objects
    WHERE object_id = OBJECT_ID(N'table_name')
        AND type = 'U'
)
BEGIN
    CREATE TABLE [table_name] (
        [ID] int identity not null,
        [column_name_1] nvarchar(255),
        [column_name_2] int,
        [column_name_3] datetime,
        CONSTRAINT PK_table_name_ID primary key ([ID])
    );
END;
