package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AddForeignKeyConstraintSegment extends Segment{
	private Token name;
	private Token foreignKey;
	private Token referencesTable;
	private Token referencesColumn;

	public AddForeignKeyConstraintSegment(Token name,Token foreignKey, Token referencesTable, Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.foreignKey=foreignKey;
		this.referencesTable=referencesTable;
		this.referencesColumn=referencesColumn;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName,boolean mySql) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF NOT EXISTS (", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\tSELECT 1", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\tFROM sys.foreign_keys", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\tWHERE name = '%s' AND parent_object_id = OBJECT_ID('%s')", Category.INSERTED,getName().cloneWithoutDelimiters(),tableName.getFullNameWithoutDelimiter()));
        sqlBlock.add(SqlString.createSqlStringFromString(")", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\t%s %s", Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("\t\t%s CONSTRAINT %s", Category.INSERTED,getAction(),getName()));
        sqlBlock.add(SqlString.createSqlStringFromString("\t\tFOREIGN KEY %s", Category.INSERTED,foreignKey));
        sqlBlock.add(SqlString.createSqlStringFromString("\t\tREFERENCES %s %s;", Category.INSERTED,referencesTable,referencesColumn));
        sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddForeignKeyConstraintSegment: %s CONSTRAINT %s %s %s %s", getAction().toString(),name,foreignKey,referencesTable,referencesColumn);
	}
}
