IF NOT EXISTS (SELECT 1 FROM table_name WHERE ID=columnValue_1)
BEGIN
	SET IDENTITY_INSERT table_name ON;
		INSERT table_name (ID, columnName_2, columnName_3, columnName_4) VALUES
			(columnValue_1, 'columnValue_2', 'columnValue_3', GETDATE());
	SET IDENTITY_INSERT table_name OFF;
END;
