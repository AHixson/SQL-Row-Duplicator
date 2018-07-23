package co.uk.antony.sql_row_duplicator;

import javax.swing.SwingUtilities;

import co.uk.antony.sql_row_duplicator.io.Control;
import co.uk.antony.sql_row_duplicator.io.Debug;
import co.uk.antony.sql_row_duplicator.io.Display;
import co.uk.antony.sql_row_duplicator.trigger.SubmitEvent;
import co.uk.antony.sql_row_duplicator.trigger.SubmitListener;
import co.uk.antony.sql_row_duplicator.util.Methods;
import co.uk.antony.sql_row_duplicator.wrapper.VTable;

public class Main implements SubmitListener {
	
	private final Display display;
	private final Control control;
	private final Debug debug;
	
	public Main() {
		
		this.display = new Display();
		this.control = display.getControl();
		this.debug = display.getDebug();
		
		this.control.setSubmitListener(this);
		this.display.setVisible(true);
	}
	
	@Override
	public void submitRecieved(SubmitEvent event) {
		
		new Thread(() -> {
			
			VTable vt = null;

			try {
				
				display.setEnabled(false);
				
				debug.getStatus().set(Debug.STATE_WORKING);
				debug.getLogger().println("Input: " + event.getInput().getAbsolutePath());
				debug.getLogger().println("Output: " + event.getOutput().getAbsolutePath());
				debug.getLogger().print("Creating virtual table");

				vt = Methods.extractAndCreateVTable(debug, event.getInput());

				debug.getLogger().println(" - " + vt.getTableName());

				debug.getLogger().print("Virtual table columns: { ");
				vt.getColumns().forEach((k, v) -> debug.getLogger().print(k + ":" + v.size() + ", "));
				debug.getLogger().println("}");

				debug.getLogger().print("Generating SQL document with " + event.getRowCount() + " rows");

				Methods.writeNewSQLFile(debug, vt, event);

				debug.getLogger().println(" - Successful!");

			} catch (Exception exception) {

				debug.getStatus().set(Debug.STATE_ERROR);
				debug.getLogger().println(exception);
				for (StackTraceElement next : exception.getStackTrace()) {
					debug.getLogger().println(next);
				}
				
			} finally {
				
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				
				debug.getStatus().set(Debug.STATE_READY);
				debug.getLogger().println("");

				display.setEnabled(true);
			}
			
		}).start();
	}
	
	/*
	 * Main class
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Methods.setSystemLAF();
			new Main();
		});
	}
}
