package lunartools.sqlrepeatabler.core.processing;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.service.BackgroundColorProvider;
import lunartools.sqlrepeatabler.core.model.AlterTableStatementFactory;
import lunartools.sqlrepeatabler.core.model.CreateTableStatementFactory;
import lunartools.sqlrepeatabler.core.model.InsertStatementFactory;
import lunartools.sqlrepeatabler.core.model.MultiLineCommentStatementFactory;
import lunartools.sqlrepeatabler.core.model.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.core.model.SetIdentityInsertStatementFactory;
import lunartools.sqlrepeatabler.core.model.SpRenameStatementFactory;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlParserException;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.SqlString;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.model.StatementFactory;
import lunartools.sqlrepeatabler.core.model.UpdateStatementFactory;
import lunartools.sqlrepeatabler.core.model.UseStatementFactory;
import lunartools.sqlrepeatabler.core.model.WhitespaceLineStatement;
import lunartools.sqlrepeatabler.core.model.WhitespaceLineStatementFactory;

public class SqlParser {
	private static Logger logger = LoggerFactory.getLogger(SqlParser.class);
	public static final String CRLF="\r\n";

	public static SqlBlock parse(SqlScript sqlScript) throws Exception {
		BackgroundColorProvider.getInstance().reset();
		SqlBlock sqlBlockResult=new SqlBlock();
		ArrayList<StatementFactory> sqlStatementFactories=new ArrayList<>();
		ArrayList<Statement> statements=new ArrayList<>();
		sqlStatementFactories.add(new AlterTableStatementFactory());
		sqlStatementFactories.add(new CreateTableStatementFactory());
		sqlStatementFactories.add(new InsertStatementFactory());
		sqlStatementFactories.add(new MultiLineCommentStatementFactory());
		sqlStatementFactories.add(new OneLineCommentStatementFactory());
		sqlStatementFactories.add(new SetIdentityInsertStatementFactory());
		sqlStatementFactories.add(new SpRenameStatementFactory());
		sqlStatementFactories.add(new WhitespaceLineStatementFactory());
        sqlStatementFactories.add(new UseStatementFactory());
        sqlStatementFactories.add(new UpdateStatementFactory());

		while(sqlScript.hasCurrentLine()) {
			SqlString sqlString=sqlScript.peekLine();

			logger.debug("Processing line "+(sqlScript.getIndex()+1)+"\t:"+sqlString.toString());
			Statement statement=null;
			for(StatementFactory statementFactory:sqlStatementFactories) {
				if(statementFactory.match(sqlString.toString())) {
					statement=statementFactory.createStatement(sqlScript);
					if(statements.size()==0 && statement instanceof WhitespaceLineStatement) {
						logger.debug("Ignoring leading whitespace lines");
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
				sqlString.markError();
				BackgroundColorProvider.getInstance().getNextPrimaryColor();//consume one color, so that in case the second statement of a two statement script fails, the first statement shows a background color
				throw new SqlParserException("Unsupported content: "+sqlString.toString(),sqlString.getLocation());
			}
		}
		return sqlBlockResult;
	}

}
