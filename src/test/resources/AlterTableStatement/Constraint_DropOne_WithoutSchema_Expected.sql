IF EXISTS (
    SELECT 1
    FROM sys.objects AS o
    WHERE o.name = N'constraint1_name'
        AND o.parent_object_id = OBJECT_ID(N'table_name')
        AND o.type IN ('C', 'F', 'PK', 'UQ')
)
BEGIN
    ALTER TABLE [table_name]
    DROP CONSTRAINT [constraint1_name];
END;
