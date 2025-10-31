package lunartools.sqlrepeatabler.segments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Token;

public class TableSegment {
	private static Logger logger = LoggerFactory.getLogger(TableSegment.class);
	private Token token;

	public TableSegment(Token token) {
		this.token=token;
	}

	public void toSql(StringBuilder sb,boolean mySql) throws Exception {
		if(mySql) {
			String element=replaceBackTicksWithSquareBrackets(token.toString());
			if(element.contains("auto_increment")) {
				logger.warn("replacing 'auto_increment' (MySQL) with 'identity' (MSSQL):");
				logger.warn("--- "+element);
				element=element.replace("auto_increment","identity");
				logger.warn("+++ "+element);
			}
			if(element.contains("engine")) {
				logger.warn("ignoring database engine parameter 'engine', which is MySQL specific:");
				logger.warn("--- "+element);
				StringBuffer sbLine=new StringBuffer(element);
				int p1=element.indexOf("engine");
				int p2=element.lastIndexOf(';');
				sbLine.delete(p1,p2);
				while(sbLine.charAt(sbLine.lastIndexOf(";")-1)==' ') {
					sbLine.deleteCharAt(sbLine.lastIndexOf(";")-1);
				}
				element=sbLine.toString();
				logger.warn("+++ "+element);
			}
			sb.append(element);
		}else {
			sb.append(token.toString());
		}

	}

	private String replaceBackTicksWithSquareBrackets(String s) throws Exception {
		StringBuffer sb=new StringBuffer(s);
		int p=0;
		while((p=sb.indexOf("`",p))!=-1) {
			sb.setCharAt(p,'[');
			p=sb.indexOf("`",p);
			if(p==-1) {
				throw new Exception("number of backticks must be even: "+s);
			}
			sb.setCharAt(p,']');
		}
		return sb.toString();
	}
	
	public Token getToken() {
	    return token;
	}

}
