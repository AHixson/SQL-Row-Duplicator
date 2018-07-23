package co.uk.antony.sql_row_duplicator.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Antony Hixson
 *
 */
public class SQLInsertParser {

	public static final int SQL_INSERT_TABLE_NAME_START = 12;
	
	private final String input;
	private final String tableName;
	private final String headerText;
	private final int[] bracketIndexes;
	private final String dataText;
	private final List<String> headerContent;
	private final List<String> dataContent;
	private final String[] headerContentToArray;
	private final String[] dataContentToArray;
	
	public SQLInsertParser(String input) {
		this.input = input;
		this.tableName = input.substring(SQL_INSERT_TABLE_NAME_START, input.indexOf(" ("));
		
		this.bracketIndexes = new int[4];
		this.bracketIndexes[0] = input.indexOf('(') + 1;
		this.bracketIndexes[1] = input.indexOf(')');
		this.bracketIndexes[2] = input.indexOf('(', bracketIndexes[1]) + 1;
		this.bracketIndexes[3] = input.lastIndexOf(')');
		
		this.headerText = input.substring(bracketIndexes[0], bracketIndexes[1]);
		this.dataText = input.substring(bracketIndexes[2], bracketIndexes[3]);
		
		this.headerContent = safeSplitString(headerText);
		this.dataContent = safeSplitString(dataText);
		
		this.headerContentToArray = headerContent.toArray(new String[headerContent.size()]);
		this.dataContentToArray = dataContent.toArray(new String[dataContent.size()]);
	}

	public String getInput() {
		return input;
	}

	public String getTableName() {
		return tableName;
	}

	public String getHeaderText() {
		return headerText;
	}

	public String getDataText() {
		return dataText;
	}

	public List<String> getHeaderContent() {
		return headerContent;
	}

	public List<String> getDataContent() {
		return dataContent;
	}
	
	public String[] getHeaderContentToArray() {
		return headerContentToArray;
	}
	
	public String[] getDataContentToArray() {
		return dataContentToArray;
	}
	
	/**
	 * 
	 * @param input
	 *            SQL String to parse
	 * @return Contents of the string which escapes SQL string type
	 */
	public static List<String> safeSplitString(String input) {

		List<String> list = new ArrayList<>();

		boolean insideString = false;

		StringBuilder sb = new StringBuilder();

		char[] arr = input.toCharArray();
		char c = '0';
		for (int i = 0; i < arr.length; i++) {
			c = arr[i];
			if (i == arr.length - 1) {
				// Append last character regardless and add string to list
				sb.append(c);
				list.add(sb.toString().trim());
				sb = new StringBuilder();
			} else {
				// Check string
				switch (c) {
				case '\'': // Toggle inside/outside string
					insideString = !insideString;
					sb.append(c);
					break;
				case ',': // Create and add text to list
					if (insideString) {
						sb.append(',');
					} else {
						list.add(sb.toString().trim());
						sb = new StringBuilder();
					}
					break;
				default: // Add text to buffer
					sb.append(c);
					break;
				}
			}
		}

		return list;
	}
}
