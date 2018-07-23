package co.uk.antony.sql_row_duplicator.trigger;

import java.io.File;

public class SubmitEvent {

	public static final String TO_STRING_fORMAT = "{file-input: %s, file-output: %s, row-count: %s, primary-key: %s, primary-key-value: %s}";

	private final File input;
	private final File output;
	private final int rowCount;
	private final boolean primaryKeyIncluded;
	private final int primaryKeyValue;

	public SubmitEvent(File input, File output, int rowCount, boolean primaryKeyIncluded, int primaryKeyValue) {
		this.input = input;
		this.output = output;
		this.rowCount = rowCount;
		this.primaryKeyIncluded = primaryKeyIncluded;
		this.primaryKeyValue = primaryKeyValue;
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_fORMAT, input, output, rowCount, primaryKeyIncluded, primaryKeyValue);
	}

	public File getInput() {
		return input;
	}

	public File getOutput() {
		return output;
	}

	public int getRowCount() {
		return rowCount;
	}
	
	public boolean isPrimaryKeyIncluded() {
		return primaryKeyIncluded;
	}
	
	public int getPrimaryKeyValue() {
		return primaryKeyValue;
	}

}
