package co.uk.antony.sql_row_duplicator.io;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import co.uk.antony.sql_row_duplicator.trigger.SubmitEvent;
import co.uk.antony.sql_row_duplicator.trigger.SubmitListener;

public class Control extends JPanel implements SwingConstants {
	
	public static final String TXT_INPUT_ACTION_COMMAND = "TXT_IN";
	public static final String TXT_OUTPUT_ACTION_COMMAND = "TXT_OUT";
	public static final String BTN_INPUT_ACTION_COMMAND = "BTN_IN";
	public static final String BTN_OUTPUT_ACTION_COMMAND = "BTN_OUT";
	
	private SubmitListener submitListener;
	
	public Control() {
		initComponents();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnSubmit.setEnabled(enabled);
		txtInput.setEnabled(enabled);
		txtOutput.setEnabled(enabled);
		btnInput.setEnabled(enabled);
		btnOutput.setEnabled(enabled);
		spnTotalRows.setEnabled(enabled);
		chkPrimaryKey.setEnabled(enabled);
		spnPrimaryKey.setEnabled(enabled && chkPrimaryKey.isSelected());
		btnQuickCopyCommand.setEnabled(enabled);
	}
	
	public SubmitListener getSubmitListener() {
		return submitListener;
	}
	
	public void setSubmitListener(SubmitListener submitListener) {
		this.submitListener = submitListener;
	}
	
	public void initComponents() {
		
		/*
		 * INIT COMPONENTS
		 */
		
		lblInput = new JLabel("File input (.sql)");
		lblOutput = new JLabel("File output (.sql)");
		txtInput = new JTextField(25);
		txtOutput = new JTextField(25);
		btnInput = new JButton("Locate");
		btnOutput = new JButton("Locate");
		lblTotalRows = new JLabel("Rows to make");
		spnTotalRows = new JSpinner(new SpinnerNumberModel(100000, 1, Integer.MAX_VALUE, 100000));
		btnSubmit = new JButton("Submit");
		fileChooser = new CustomFileChooser();
		chkPrimaryKey = new JCheckBox("Include primary key (start at value)");
		spnPrimaryKey = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		spnPrimaryKey.setEnabled(false);
		btnQuickCopyCommand = new JButton("Copy Oracle SQL run command");
		
		/*
		 * INIT AND SET LAYOUT
		 */
		
		layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JPanel pnlSeparator = new JPanel(null);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(lblInput)
				.addGroup(layout.createSequentialGroup()
						.addComponent(txtInput)
						.addComponent(btnInput))
				.addComponent(lblOutput)
				.addGroup(layout.createSequentialGroup()
						.addComponent(txtOutput)
						.addComponent(btnOutput))
				.addComponent(lblTotalRows)
				.addComponent(spnTotalRows)
				.addComponent(chkPrimaryKey)
				.addComponent(spnPrimaryKey)
				.addGroup(layout.createSequentialGroup()
						.addComponent(pnlSeparator)
						.addComponent(btnSubmit)
						.addPreferredGap(btnSubmit, btnQuickCopyCommand, ComponentPlacement.RELATED)
						.addComponent(btnQuickCopyCommand)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(lblInput)
				.addGroup(layout.createBaselineGroup(false, true)
						.addComponent(txtInput)
						.addComponent(btnInput))
				.addGap(15)
				.addComponent(lblOutput)
				.addGroup(layout.createBaselineGroup(false, true)
						.addComponent(txtOutput)
						.addComponent(btnOutput))
				.addGap(15)
				.addGroup(layout.createBaselineGroup(false, true)
					.addComponent(lblTotalRows))
				.addGroup(layout.createBaselineGroup(false, true)
					.addComponent(spnTotalRows))
				.addGap(15)
				.addGroup(layout.createBaselineGroup(false, true)
						.addComponent(chkPrimaryKey))
				.addGroup(layout.createBaselineGroup(false, true)
						.addComponent(spnPrimaryKey))
				.addGap(15)
				.addComponent(pnlSeparator)
				.addGroup(layout.createBaselineGroup(false, false)
						.addComponent(btnSubmit)
						.addComponent(btnQuickCopyCommand)));
		
		super.setLayout(layout);
		
		/*
		 * INIT LISTENERS
		 */

		btnInput.addActionListener(new LoadFileListener());
		btnOutput.addActionListener(new SaveFileListener());
		btnSubmit.addActionListener(new SubmitButtonListener());
		chkPrimaryKey.addActionListener(new TogglePrimaryKeySpinner());
		btnQuickCopyCommand.addActionListener(new CopyOracleSQLRunCommand());
	}
	
	public void setSaveFile(File file) {
		String path = file.getAbsolutePath();
		if (!path.endsWith(".sql")) {
			path += ".sql";
		}
		txtOutput.setText(path);
	}
	
	public void setLoadFile(File file) {
		
		String path = file.getAbsolutePath();
		String outputPath = txtOutput.getText();
		
		if (!path.endsWith(".sql")) {
			path += ".sql";
		}
		
		if (outputPath == null || outputPath.isEmpty()) {
			setSaveFile(new File(path));
		}
		
		txtInput.setText(path);
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}

	public void showSuccessMessage() {
		JOptionPane.showMessageDialog(this, "SQL file has been extended!", "Success!", JOptionPane.PLAIN_MESSAGE);
	}

	public void showFailedMessage() {
		showError("Failed to extend SQL!");
	}

	public boolean isAuthorisedToGenerateLargeQuantityOfRows(int number) {
		return JOptionPane.showConfirmDialog(this, "You are about to generate " + number + " rows!", "Warning!",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION;
	}

	private GroupLayout layout;
	private JLabel lblInput;
	private JLabel lblOutput;
	private JTextField txtInput;
	private JTextField txtOutput;
	private JButton btnInput;
	private JButton btnOutput;
	private JLabel lblTotalRows;
	private JSpinner spnTotalRows;
	private JButton btnSubmit;
	private CustomFileChooser fileChooser;
	private JCheckBox chkPrimaryKey;
	private JSpinner spnPrimaryKey;
	private JButton btnQuickCopyCommand;
	
	private class SaveFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int val = fileChooser.showSaveDialog(Control.this);

			if (val == JFileChooser.APPROVE_OPTION) {

				setSaveFile(fileChooser.getSelectedFile());
			}

		}
	}

	private class LoadFileListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int val = fileChooser.showOpenDialog(Control.this);
			
			if (val == JFileChooser.APPROVE_OPTION) {
			
				setLoadFile(fileChooser.getSelectedFile());
			}
		}
	}
	
	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String strIn = null;
			File fileIn = null;
			
			String strOut = null;
			File fileOut = null;
			
			int rowCount = -1;
			
			if (submitListener != null) {
				
				// SET AND CHECK INPUT FILE
				
				strIn = txtInput.getText();
				fileIn = new File(strIn);
				
				if (!strIn.endsWith(".sql") || !fileIn.exists() || !fileIn.isFile()) {
					
					showError("Input file is invalid!");
					
					return;
				}
				
				// SET AND CHECK OUTPUT FILE
				
				strOut = txtOutput.getText();
				fileOut = new File(strOut);
				
				try {
				
					if (!strOut.endsWith(".sql")) {
						
						showError("Output file is invalid!");
						
						return;
						
					} else if (!fileOut.exists() && !fileOut.createNewFile()) {
						
						showError("Failed to create output file!");
	
						return;
					}
				
				} catch (IOException exception) {
					
					showError(exception.getMessage());
				}
				
				// SET ROW COUNT

				rowCount = (Integer) spnTotalRows.getValue();

				if (rowCount <= 1000000 || isAuthorisedToGenerateLargeQuantityOfRows(rowCount)) {
					submitListener.submitRecieved(new SubmitEvent(fileIn, fileOut, rowCount, chkPrimaryKey.isSelected(),
							((Integer) spnPrimaryKey.getValue()).intValue()));
				}
			}
		}
	}
	
	private class TogglePrimaryKeySpinner implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			spnPrimaryKey.setEnabled(chkPrimaryKey.isSelected());
		}
	}
	
	private class CopyOracleSQLRunCommand implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {

			if (!txtOutput.getText().isEmpty()) {
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(new StringSelection('@' + txtOutput.getText() + ";"), null);
			}
		}
	}
}
