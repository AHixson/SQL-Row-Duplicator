package co.uk.antony.sql_row_duplicator.io;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * 
 * @author Antony Hixson
 * 
 *         This component is intended to produce logs and status progress.
 */
public class Debug extends JPanel {
	
	public static final int STATE_READY = 0;
	public static final int STATE_WORKING = 1;
	public static final int STATE_ERROR = -1;
	
	private final JTextArea txtaLogger;
	private final JLabel lblStatus;
	private final JButton btnClearLogger;
	private final JButton btnCopyLogger;
	private final JScrollPane scrTxtaLogger;
	private final JToolBar tlbContainer;
	
	private final Logger logger;
	private final Status status;
	
	public Debug() {
		
		/*
		 * Initialise components
		 */
		
		this.txtaLogger = new JTextArea(10, 25);
		this.txtaLogger.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		this.txtaLogger.setEditable(false);
		//this.txtaLogger.setLineWrap(true);
		this.txtaLogger.setWrapStyleWord(true);
		
		this.lblStatus = new JLabel("Status: Ready.");
		
		this.btnClearLogger = new JButton(new AbstractAction("Clear") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				logger.clear();
			}
		});
		
		this.btnCopyLogger = new JButton(new AbstractAction("Copy") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				logger.copy();
			}
		});
		
		this.scrTxtaLogger = new JScrollPane(txtaLogger);
		
		this.tlbContainer = new JToolBar();
		this.tlbContainer.setFloatable(false);
		this.tlbContainer.add(lblStatus);
		this.tlbContainer.add(Box.createHorizontalGlue());
		this.tlbContainer.add(btnClearLogger);
		this.tlbContainer.add(btnCopyLogger);
		
		
		/*
		 * This
		 */
		
		super.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		super.setLayout(new BorderLayout());
		
		/*
		 * This -> add
		 */
		super.add(scrTxtaLogger, BorderLayout.CENTER);
		super.add(tlbContainer, BorderLayout.SOUTH);
		
		/*
		 * Initialise other variables
		 */
		
		this.logger = new Logger();
		this.status = new Status();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnClearLogger.setEnabled(enabled);
		btnCopyLogger.setEnabled(enabled);
		
	}
	
	/*
	 * Other methods
	 */
	
	public Logger getLogger() {
		return logger;
	}
	
	public Status getStatus() {
		return status;
	}
	
	/*
	 * Other classes
	 */
	
	public class Logger {
		
		public Logger() {
		}
		
		public void clear() {
			txtaLogger.setText("");
		}
		
		public void print(Object obj) {
			if (obj != null) {
				txtaLogger.append(obj.toString());
			} else {
				txtaLogger.append("\n");
			}
		}
		
		public void println(Object obj) {
			print(obj);
			print(null);
		}
		
		public void copy() {
			Toolkit.getDefaultToolkit()
				.getSystemClipboard()
				.setContents(new StringSelection(txtaLogger.getText()), null);
		}
	}
	
	public class Status {
		
		public void clear() {
			set(null);
		}
		
		public void set(Object obj) {
			if (obj != null) {
				lblStatus.setText("Status: " + obj.toString());
			} else {
				lblStatus.setText("Status:");
			}
		}
		
		public void set(int state) {
			
			String status = null;
			
			switch(state) {
			case STATE_ERROR:
				status = "Error - check logger for details";
				break;
			case STATE_READY:
				status = "Ready";
				break;
			case STATE_WORKING:
				status = "Working";
				break;
			default:
				throw new IllegalStateException("No state found for [" + state + "]");
			}
			
			assert (status != null);
			
			set(status);
		}
	}
	
}
