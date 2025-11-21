package lunartools.sqlrepeatabler.statement.actions;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class DropConstraintAction extends AlterTableAction{
	private Token name;

	public DropConstraintAction(Token name) {
		super(new Token("DROP",Category.COMMAND),name);
		this.name=name;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF EXISTS ("										,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    SELECT 1"										,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    FROM sys.objects AS o"						,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    WHERE o.name = N'%s'"                  		,Category.INSERTED,name.cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("        AND o.parent_object_id = OBJECT_ID(N'%s')",Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
        sqlBlock.add(SqlString.createSqlStringFromString("        AND o.type IN ('C', 'F', 'PK', 'UQ')"     ,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString(")"												,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"											,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"										,Category.INSERTED,tokenStatement, tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("    %s CONSTRAINT %s;"							,Category.INSERTED,getAction(),name));
        sqlBlock.add(SqlString.createSqlStringFromString("END;"												,Category.INSERTED));
	}
	
	public String toString() {
		return String.format("DropConstraintAction: %s %s",getAction(),name);
	}
}
