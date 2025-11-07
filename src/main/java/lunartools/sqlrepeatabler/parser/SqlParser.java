package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.statements.AlterTableStatementFactory;
import lunartools.sqlrepeatabler.statements.CreateTableStatementFactory;
import lunartools.sqlrepeatabler.statements.InsertIntoStatementFactory;
import lunartools.sqlrepeatabler.statements.MultiLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.SetIdentityInsertStatementFactory;
import lunartools.sqlrepeatabler.statements.SpRenameStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;
import lunartools.sqlrepeatabler.statements.StatementFactory;
import lunartools.sqlrepeatabler.statements.UseStatementFactory;
import lunartools.sqlrepeatabler.statements.WhitespaceLineStatement;
import lunartools.sqlrepeatabler.statements.WhitespaceLineStatementFactory;

public class SqlParser {
	private static Logger logger = LoggerFactory.getLogger(SqlParser.class);
	public static final String CRLF="\r\n";

//	public static StringBuilder parse(Path path) throws Exception {
//		try(BufferedReader bufferedReader=Files.newBufferedReader(path, StandardCharsets.UTF_8)){
//			return parse(bufferedReader);
//		}
//	}
//
//	public static StringBuilder parse(File file) throws Exception {
//		try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
//			return parse(bufferedReader);
//		}
//	}

	public static SqlBlock parse(SqlScript sqlScript) throws Exception {
		SqlBlock sqlBlockResult=new SqlBlock();
		ArrayList<StatementFactory> sqlSegmentFactories=new ArrayList<>();
		ArrayList<Statement> statements=new ArrayList<>();
		sqlSegmentFactories.add(new AlterTableStatementFactory());
		sqlSegmentFactories.add(new CreateTableStatementFactory());
		sqlSegmentFactories.add(new InsertIntoStatementFactory());
		sqlSegmentFactories.add(new MultiLineCommentStatementFactory());
		sqlSegmentFactories.add(new OneLineCommentStatementFactory());
		sqlSegmentFactories.add(new SetIdentityInsertStatementFactory());
		sqlSegmentFactories.add(new SpRenameStatementFactory());
        sqlSegmentFactories.add(new WhitespaceLineStatementFactory());
        sqlSegmentFactories.add(new UseStatementFactory());

		while(sqlScript.hasCurrentLine()) {
			SqlString sqlString=sqlScript.peekLine();
			
			logger.debug("processing line "+(sqlScript.getIndex()+1)+"\t:"+sqlString.toString());
			Statement statement=null;
			for(StatementFactory statementFactory:sqlSegmentFactories) {
				if(statementFactory.match(sqlString.toString())) {
					statement=statementFactory.createStatement(sqlScript);
					if(statements.size()==0 && statement instanceof WhitespaceLineStatement) {
					    logger.warn("ignoring leading whitespace lines");
					    break;
					}
					statements.add(statement);
					SqlBlock tempCharacters=new SqlBlock();
					statement.toSqlCharacters(tempCharacters);
					sqlBlockResult.add(tempCharacters);
					break;
				}
			}
			if(statement==null) {
				throw new SqlParserException("Unsupported content: "+sqlString.toString(),sqlString.getFirstCharacter());
			}
		}
		return sqlBlockResult;
	}

}
