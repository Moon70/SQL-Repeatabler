IF NOT EXISTS (SELECT 1 FROM table_name WHERE ID=row1_columnValue_1)
BEGIN
	SET IDENTITY_INSERT table_name ON;
		INSERT INTO table_name (ID, columnName_2, columnName_3, columnName_4) VALUES
			(row1_columnValue_1, 'row1_columnValue_2', 'row1_columnValue_3', 'row1_columnValue_4');
	SET IDENTITY_INSERT table_name OFF;
END;

IF NOT EXISTS (SELECT 1 FROM table_name WHERE ID=row2_columnValue_1)
BEGIN
	SET IDENTITY_INSERT table_name ON;
		INSERT INTO table_name (ID, columnName_2, columnName_3, columnName_4) VALUES
			(row2_columnValue_1, 'row2_columnValue_2', 'row2_columnValue_3', 'row2_columnValue_4');
	SET IDENTITY_INSERT table_name OFF;
END;

IF NOT EXISTS (SELECT 1 FROM table_name WHERE ID=row3_columnValue_1)
BEGIN
	SET IDENTITY_INSERT table_name ON;
		INSERT INTO table_name (ID, columnName_2, columnName_3, columnName_4) VALUES
			(row3_columnValue_1, 'row3_columnValue_2', 'row3_columnValue_3', 'row3_columnValue_4');
	SET IDENTITY_INSERT table_name OFF;
END;
