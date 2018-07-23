package co.uk.antony.sql_row_duplicator.wrapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author Antony Hixson
 *
 */
public class VTable {
	
	public static final String TO_STRING_FORMAT = "{name: %s, column: %s}";
	public static final String ABSTRACT_ROW = "insert into %s (%s) values (%s);\n";
	
	private final String tableName;
	private final Map<String, VColumn> columns;
	private final String headersToString;
	
	public VTable(String tableName, String... headers) {
		
		this.tableName = tableName;
		this.columns = new LinkedHashMap<>();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < headers.length; i++) {
			String next = headers[i];
			sb.append(headers[i]);
			if (i < headers.length - 1) {
				sb.append(", ");
			}
			columns.put(next, new VColumn());
		}
		this.headersToString = sb.toString();
	}
	
	private VTable(SQLInsertParser sqlInsertParser) {
		this(sqlInsertParser.getTableName(), sqlInsertParser.getHeaderContentToArray());
	}
	
	public VTable(String input) {
		this(new SQLInsertParser(input));
	}
	
	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, tableName, columns.keySet().size());
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public Map<String, VColumn> getColumns() {
		return columns;
	}
	
	/*
	 * Other methods
	 */
	
	public String generateRandomRowData(boolean includePrimaryKey, int primaryKey) {
		StringBuilder sb = new StringBuilder();
		if (includePrimaryKey) {
			sb.append(primaryKey + ", ");
		}
		sb.append(createRandomRowStringFromVColumns(includePrimaryKey));
		return String.format(ABSTRACT_ROW, tableName, headersToString, sb.toString());
	}
	
	private String createRandomRowStringFromVColumns(boolean includePrimaryKey) {
		final AtomicBoolean togglePk = new AtomicBoolean(includePrimaryKey);
		final StringBuilder sb = new StringBuilder();
		columns.forEach((k, v) -> {
			if (togglePk.get()) {
				togglePk.set(false);
			} else {
				sb.append(v.getRandom() + ", ");
			}
		});
		return sb.toString().substring(0, sb.length() - 2);
	}
	
	public void parseAndProcess(int lineNumber, String nextline) {
		
		SQLInsertParser sip = new SQLInsertParser(nextline);
		
		String[] headerArr = null;
		String[] dataArr = null;
		
		if (nextline == null || nextline.isEmpty()) {
			throw new RuntimeException("Failed to parse inputted line");
		}
		
		headerArr = sip.getHeaderContentToArray();
		dataArr = sip.getDataContentToArray();
		
		if (headerArr == null) {
			throw new RuntimeException("Headers at " + lineNumber + " are null");
		} else if (dataArr == null) {
			throw new RuntimeException("Data at " + lineNumber + " are null");
		} else if (headerArr.length != dataArr.length) {
			throw new RuntimeException("Inconsistent array sizes for header and data at line " + lineNumber);
		}
		
		for (int i = 0; i < headerArr.length; i++) {
			columns.get(headerArr[i]).add(dataArr[i]);
		}
	}
	
}
