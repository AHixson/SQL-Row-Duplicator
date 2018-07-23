package co.uk.antony.sql_row_duplicator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.UIManager;

import co.uk.antony.sql_row_duplicator.io.Debug;
import co.uk.antony.sql_row_duplicator.trigger.SubmitEvent;
import co.uk.antony.sql_row_duplicator.wrapper.VTable;

public class Methods {

	public static boolean setSystemLAF() {
		boolean b = false;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			b = true;
		} catch (Exception e) {
		}
		return b;
	}
	
	public static int getRandom(int a, int b) {
		int min = Math.min(a, b);
		int max = Math.max(a, b);
		int dif = max - min;
		int ran = (dif > 0 ? ran = new Random().nextInt(dif) : 0);
		return min + ran;
	}
	
	public static VTable extractAndCreateVTable(Debug debug, File file) throws IOException {
		
		VTable vTable = null;
		String nextline = null;
		int lineNumber = 1;
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		vTable = new VTable(br.readLine());
		
		while((nextline = br.readLine()) != null) {
			if (vTable != null) {
				vTable.parseAndProcess(lineNumber, nextline);
			} else {
				vTable = new VTable(nextline);
				vTable.parseAndProcess(lineNumber, nextline);
			}
			debug.getStatus().set("Reading from line " + (++lineNumber));
		}
		
		br.close();
		
		return vTable;
	}
	
	public static void writeNewSQLFile(Debug debug, VTable vTable, SubmitEvent submitEvent) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(submitEvent.getOutput()));
		
		int count = submitEvent.getRowCount();
		
		String nextline = null;
		
		for (int i = 0; i < count; i++) {
			
			debug.getStatus().set("Creating row " + (i + 1) + "/" + count);
			
			nextline = vTable.generateRandomRowData(submitEvent.isPrimaryKeyIncluded(), submitEvent.getPrimaryKeyValue() + i);
			
			bw.write(nextline);
		}
		
		bw.flush();
		
		bw.close();
	}
	
}
