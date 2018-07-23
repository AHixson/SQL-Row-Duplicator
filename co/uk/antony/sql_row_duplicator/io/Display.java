package co.uk.antony.sql_row_duplicator.io;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * 
 * @author Antony Hixson
 *
 *         Display for this tool
 */
public class Display extends JFrame {

	private final Control control;
	private final Debug debug;
	
	public Display() {
		
		/*
		 * This
		 */
		super.setTitle("SQL Row Duplicator - By Antony Hixson");
		super.setSize(800, 550);
		super.setLocationRelativeTo(getOwner());
		super.setLayout(new BorderLayout());
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		/*
		 * This -> Add
		 */
		
		this.control = new Control();
		this.debug = new Debug();
		
		super.add(control, BorderLayout.CENTER);
		super.add(debug, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		control.setEnabled(b);
		debug.setEnabled(b);
	}
	
	public Control getControl() {
		return control;
	}
	
	public Debug getDebug() {
		return debug;
	}
}
