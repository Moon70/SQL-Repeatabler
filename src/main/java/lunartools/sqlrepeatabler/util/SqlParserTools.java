package lunartools.sqlrepeatabler.util;

public class SqlParserTools {

	public static StringBuilder stripWhitespace(StringBuilder sb) {
		boolean singleQuoteOpen=false;
		boolean doubleQuoteOpen=false;
		boolean space=false;
		boolean whitespace=true;

		StringBuilder buffer=new StringBuilder();
		for(int i=0;i<sb.length();i++) {
			char c=sb.charAt(i);
			if((c=='\n') || (c=='\r') || (c=='\t')) {
				if(whitespace) {
					continue;
				}
				if(!singleQuoteOpen && !doubleQuoteOpen) {
					if(space) {
						continue;
					}else {
						c=' ';
						space=true;
					}
				}
			}else {
				if(c!=' ') {
					space=false;
					whitespace=false;
				}
			}
			if(c==' ') {
				if(!singleQuoteOpen && !doubleQuoteOpen && space || whitespace) {
					continue;
				}else {
					space=true;
				}
			}
			if(c==39){// '
				singleQuoteOpen=!singleQuoteOpen;
			}
			if(c=='"'){
				doubleQuoteOpen=!doubleQuoteOpen;
			}

			buffer.append(c);
		}
		return buffer;
	}

	public static void stripUntil(StringBuilder sb,char c) {
		while(sb.charAt(0)!=c) {
			sb.deleteCharAt(0);
		}
	}

	public static void stripSpace(StringBuilder sb) {
		while(sb.charAt(0)==' ') {
			sb.deleteCharAt(0);
		}
	}

	public static void stripSpaceRight(StringBuilder sb) {
		while(sb.charAt(sb.length()-1)==' ') {
			sb.deleteCharAt(sb.length()-1);
		}
	}

	public static int indexOfNotInLiteral(StringBuilder sb, char c) {
		boolean doubleQuote=false;
		boolean singleQuote=false;

		for(int i=0;i<sb.length();i++) {
			char cIndex=sb.charAt(i);
			if(cIndex=='"') {
				doubleQuote=!doubleQuote;
			}else if(cIndex=='\'') {
				singleQuote=!singleQuote;
			}else if((cIndex==c) && !doubleQuote && !singleQuote) {
				return i;
			}
		}
		return -1;
	}

	public static int lastIndexOfNotInLiteral(StringBuilder sb, char c) {
		boolean doubleQuote=false;
		boolean singleQuote=false;

		for(int i=sb.length()-1;i>=0;i--) {
			char cIndex=sb.charAt(i);
			if(cIndex=='"') {
				doubleQuote=!doubleQuote;
			}else if(cIndex=='\'') {
				singleQuote=!singleQuote;
			}else if((cIndex==c) && !doubleQuote && !singleQuote) {
				return i;
			}
		}
		return -1;
	}

	public static boolean startsWithIgnoreCase(StringBuilder sb, String s) {
		int len=s.length();
		if (sb.length()<len) {
			return false;
		}
		String sLowercase=s.toLowerCase();
		for (int i=0;i<len;i++) {
			char c1=sb.charAt(i);
			if (Character.toLowerCase(c1)!=sLowercase.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean consumePrefixIgnoreCaseAndSpace(StringBuilder sb, String prefix) {
		if(startsWithIgnoreCase(sb,prefix)) {
			sb.delete(0, prefix.length());
			stripSpace(sb);
			return true;
		}else {
			return false;
		}
	}

	public static String consumeTokensInParenthesis(StringBuilder sbCommand) throws Exception{
		int openParenthesis=0;
		stripSpace(sbCommand);
		if(sbCommand.charAt(0)!='(') {
			throw new Exception("Error parsing tokens in parenthesis! Expected: >(<. Found: >"+sbCommand.charAt(0)+"<, Buffer: >"+(sbCommand.length()>10?sbCommand.substring(0,10):sbCommand.toString()));
		}
		int index=1;
		while(true) {
			char c=sbCommand.charAt(index);
			if(c==')'){
				if(openParenthesis==0) {
					break;
				}else{
					openParenthesis--;
				}
			}else if(c=='(') {
				openParenthesis++;
			}
			index++;
		}
		
//		int i=getIndexOfClosingParenthesisIgnoringQuotes(sbCommand);
		String result=sbCommand.substring(0, index+1);
		sbCommand.delete(0, index+1);
		if(sbCommand.charAt(0)==' ') {
			sbCommand.deleteCharAt(0);
		}
		return result;
	}

	private static int getIndexOfClosingParenthesisIgnoringQuotes(StringBuilder sbCommand) throws Exception {
		boolean quotes=false;
		boolean doubleQuotes=false;
		for(int i=0;i<sbCommand.length();i++) {
			if(sbCommand.charAt(i)=='\'') {
				quotes=!quotes;
			}else if(sbCommand.charAt(i)=='"') {
				doubleQuotes=!doubleQuotes;
			}else if(quotes||doubleQuotes) {
				continue;
			}else if(sbCommand.charAt(i)==')') {
				return i;
			}
		}
		throw new Exception("closing parenthesis not found");
	}

}
