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

import lunartools.sqlrepeatabler.common.SqlScript;
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
		while(sqlScript.hasCurrentLines()) {
			String line=sqlScript.peekLine();
			int lineNumber=sqlScript.getLineNumber();
			
			logger.debug("processing line "+lineNumber+"\t:"+line);
			Statement sqlSegment=null;
			for(StatementFactory sqlSegmentFactory:sqlSegmentFactories) {
				if(sqlSegmentFactory.match(line)) {
					sqlSegment=sqlSegmentFactory.createSqlSegment(sqlScript);
					sqlSegments.add(sqlSegment);
					//System.out.println("##################################################");
					StringBuilder sb=new StringBuilder();
					sqlSegment.toSql(sb);
					//System.out.print(sb.toString());
					result.append(sb);
					//System.out.println("##################################################");
					break;
				}
			}
			if(sqlSegment==null) {
				throw new Exception("Unsupported content in line "+lineNumber+" :"+line);
			}
		}
		return result;
	}

}
