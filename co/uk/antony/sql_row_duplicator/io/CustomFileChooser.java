package co.uk.antony.sql_row_duplicator.io;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class CustomFileChooser extends JFileChooser {

	public CustomFileChooser() {
		super.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				return "SQL file type";
			}

			@Override
			public boolean accept(File f) {

				return f.getName().endsWith(".sql");
			}
		});
	}

	// http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
	@Override
	public void approveSelection() {
		File f = getSelectedFile();
		if (f.exists() && getDialogType() == SAVE_DIALOG) {
			int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
					JOptionPane.YES_NO_CANCEL_OPTION);
			switch (result) {
			case JOptionPane.YES_OPTION:
				super.approveSelection();
				return;
			case JOptionPane.NO_OPTION:
				return;
			case JOptionPane.CLOSED_OPTION:
				return;
			case JOptionPane.CANCEL_OPTION:
				cancelSelection();
				return;
			}
		}
		super.approveSelection();
	}

}
