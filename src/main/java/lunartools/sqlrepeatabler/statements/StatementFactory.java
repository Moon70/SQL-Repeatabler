package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public abstract class StatementFactory {

	public abstract boolean match(String line);
	
	public abstract Statement createSqlSegment(SqlScript sqlScript) throws Exception;
	
	public String consumeTokenIgnoreSpaceAndComma(StringBuilder sb) throws Exception{
		boolean doubleQuoteOpen=false;
		boolean singleQuoteOpen=false;
		StringBuilder token=new StringBuilder();
		
		while(sb.length()>0) {
			char c=sb.charAt(0);
			if(c=='"') {
				doubleQuoteOpen=!doubleQuoteOpen;
			}else if(c=='\'') {
				singleQuoteOpen=!singleQuoteOpen;
			}else if(singleQuoteOpen||doubleQuoteOpen) {
				//characters inside quotes or parenthesis
			}else if(c==';') {
				return token.toString();
			}else if(c==' ' || c==',') {
				sb.deleteCharAt(0);
				SqlParserTools.stripSpace(sb);
				return token.toString();
			}
			token.append(c);
			sb.deleteCharAt(0);
		}
		throw new Exception("Unexpected end of buffer");
	}
	
	public String consumeTokenIgnoreSpaceCommaParenthesis(StringBuilder sb) throws Exception{
		boolean doubleQuoteOpen=false;
		boolean singleQuoteOpen=false;
		int parenthesisCount=0;
		StringBuilder token=new StringBuilder();
		
		while(sb.length()>0) {
			char c=sb.charAt(0);
			if(c=='"') {
				doubleQuoteOpen=!doubleQuoteOpen;
			}else if(c=='\'') {
				singleQuoteOpen=!singleQuoteOpen;
			}else if(c=='(') {
				parenthesisCount++;
			}else if(singleQuoteOpen||doubleQuoteOpen||parenthesisCount!=0) {
				//characters inside quotes or parenthesis
			}else if(c==')') {
				parenthesisCount--;
			}else if(c==';') {
				return token.toString();
			}else if(c==' ' || c==',') {
				sb.deleteCharAt(0);
				SqlParserTools.stripSpace(sb);
				return token.toString();
			}
			token.append(c);
			sb.deleteCharAt(0);
		}
		throw new Exception("Unexpected end of buffer");
	}
	
	public String consumeAllTokensIgnoreComma(StringBuilder sb) throws Exception{
		boolean doubleQuoteOpen=false;
		boolean singleQuoteOpen=false;
		StringBuilder token=new StringBuilder();
		
		while(sb.length()>0) {
			char c=sb.charAt(0);
			if(c=='"') {
				doubleQuoteOpen=!doubleQuoteOpen;
			}else if(c=='\'') {
				singleQuoteOpen=!singleQuoteOpen;
			}else if(singleQuoteOpen||doubleQuoteOpen) {
				//characters inside quotes
			}else if(c==';') {
				return token.toString();
			}else if(c==',') {
				sb.deleteCharAt(0);
				SqlParserTools.stripSpace(sb);
				return token.toString();
			}
			token.append(c);
			sb.deleteCharAt(0);
		}
		throw new Exception("Unexpected end of buffer");
	}
	
	public String consumeAllTokensIgnoreCommaParenthesis(StringBuilder sb) throws Exception{
		boolean doubleQuoteOpen=false;
		boolean singleQuoteOpen=false;
		int parenthesisCount=0;
		StringBuilder token=new StringBuilder();
		
		while(sb.length()>0) {
			char c=sb.charAt(0);
			if(c=='"') {
				doubleQuoteOpen=!doubleQuoteOpen;
			}else if(c=='\'') {
				singleQuoteOpen=!singleQuoteOpen;
			}else if(c==')') {
				parenthesisCount--;
			}else if(c=='(') {
				parenthesisCount++;
			}else if(singleQuoteOpen||doubleQuoteOpen||parenthesisCount!=0) {
				//characters inside quotes
			}else if(c==';') {
				return token.toString();
			}else if(c==',') {
				sb.deleteCharAt(0);
				SqlParserTools.stripSpace(sb);
				return token.toString();
			}
			token.append(c);
			sb.deleteCharAt(0);
		}
		throw new Exception("Unexpected end of buffer");
	}
}
