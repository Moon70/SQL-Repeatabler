package lunartools.sqlrepeatabler.core.model;

public class WhitespaceLineStatement implements Statement{
    public static final WhitespaceLineStatement WHITESPACE_LINE=new WhitespaceLineStatement();
    
    public WhitespaceLineStatement() {}

    @Override
    public void toSqlCharacters(SqlBlock sqlBlock){
        sqlBlock.add(SqlString.EMPTY_LINE);
    }

}
