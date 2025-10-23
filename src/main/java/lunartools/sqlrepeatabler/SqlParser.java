package lunartools.sqlrepeatabler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.AlterTableStatementFactory;
import lunartools.sqlrepeatabler.statements.CreateTableStatementFactory;
import lunartools.sqlrepeatabler.statements.InsertIntoStatementFactory;
import lunartools.sqlrepeatabler.statements.MultiLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.SetIdentityInsertStatementFactory;
import lunartools.sqlrepeatabler.statements.SpRenameStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;
import lunartools.sqlrepeatabler.statements.StatementFactory;
import lunartools.sqlrepeatabler.statements.WhitespaceLineStatementFactory;

public class SqlParser {
	private static Logger logger = LoggerFactory.getLogger(SqlParser.class);
	public static final String CRLF="\r\n";

	public static StringBuilder parse(Path path) throws Exception {
		try(BufferedReader bufferedReader=Files.newBufferedReader(path, StandardCharsets.UTF_8)){
			return parse(bufferedReader);
		}
	}

	public static StringBuilder parse(File file) throws Exception {
		try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
			return parse(bufferedReader);
		}
	}

	public static StringBuilder parse(BufferedReader bufferedReader) throws Exception {
		StringBuilder result=new StringBuilder();
		ArrayList<StatementFactory> sqlSegmentFactories=new ArrayList<>();
		ArrayList<Statement> sqlSegments=new ArrayList<>();
		sqlSegmentFactories.add(new AlterTableStatementFactory());
		sqlSegmentFactories.add(new CreateTableStatementFactory());
		sqlSegmentFactories.add(new InsertIntoStatementFactory());
		sqlSegmentFactories.add(new MultiLineCommentStatementFactory());
		sqlSegmentFactories.add(new OneLineCommentStatementFactory());
		sqlSegmentFactories.add(new SetIdentityInsertStatementFactory());
		sqlSegmentFactories.add(new SpRenameStatementFactory());
		sqlSegmentFactories.add(new WhitespaceLineStatementFactory());

		SqlScript sqlScript=SqlScript.createInstance(bufferedReader);
		while(sqlScript.hasCurrentLine()) {
			String line=sqlScript.peekLine();
			int lineNumber=sqlScript.getLineNumber();
			
			logger.debug("processing line "+lineNumber+"\t:"+line);
			Statement statement=null;
			for(StatementFactory statementFactory:sqlSegmentFactories) {
				if(statementFactory.match(line)) {
					statement=statementFactory.createStatement(sqlScript);
					sqlSegments.add(statement);
					//System.out.println("##################################################");
					StringBuilder sb=new StringBuilder();
					statement.toSql(sb);
					//System.out.print(sb.toString());
					result.append(sb);
					//System.out.println("##################################################");
					break;
				}
			}
			if(statement==null) {
			    throw new Exception("Unsupported content in line "+lineNumber+" :"+line);
				//throw new SqlParserException("Unsupported content: "+(line.length()<20?line:line.substring(0, 20)+"..."),sqlScript.getFirstCharacterOfCurrentLine());
			}
		}
		return result;
	}

}
