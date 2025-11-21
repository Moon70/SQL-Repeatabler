IF NOT EXISTS (
    SELECT 1
    FROM sys.key_constraints kc
    WHERE kc.name = 'constraint1_name'
        AND kc.type = 'UQ'
)
BEGIN
    ALTER TABLE [table1_name]
        ADD CONSTRAINT constraint1_name UNIQUE ([column1_ID]);
END;
