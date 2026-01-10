package lunartools.sqlrepeatabler.core.model;

public class UpdateStatement implements Statement{
    public static final String COMMAND="UPDATE";
    private Token tokenStatement;
    private TableName tableName;
    private Token tokenSetClause=new Token("SET",Category.COMMAND);
    private Token tokenWhereClause=new Token("WHERE",Category.COMMAND);
    private Token tokenSetData;
    private Token tokenWhereData;

    public UpdateStatement(Token statement,TableName tableName,Token tokenSetData,Token tokenWhereData) {
        this.tokenStatement=statement;
        this.tableName=tableName;
        this.tokenSetData=tokenSetData;
        this.tokenWhereData=tokenWhereData;
    }

    @Override
    public void toSqlCharacters(SqlBlock sqlBlock){
        SqlBlock sqlBlockStatement=new SqlBlock();
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "%s %s ("  ,Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "  %s %s"   ,Category.INSERTED,tokenSetClause,tokenSetData));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "  %s %s;"   ,Category.INSERTED,tokenWhereClause,tokenWhereData));

        sqlBlock.add(sqlBlockStatement);
    }
}
