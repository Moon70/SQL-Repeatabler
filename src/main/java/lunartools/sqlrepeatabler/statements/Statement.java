package lunartools.sqlrepeatabler.statements;

public interface Statement {

	public abstract void toSql(StringBuilder sb)throws Exception;

}
