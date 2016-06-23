package timesheet.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import timesheet.connection.DBEngine.DbEngine;

public class AdminPanel extends JPanel {
	private final static Logger LOGGER = Logger.getLogger(AdminPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final JButton btnNewButton = new JButton("Add");
	private JTextField txtResource;
	private JTextField textField_1;

	public AdminPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Add new project");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		btnNewButton.addActionListener(e -> {
			String project = textField.getText();
			try {
				if (!new DbEngine().addProject(project))
					MainWindow.sendErrorNotification(
							"Failed to add project. Perhaps it already exists? Please restart the application");
			} catch (SQLException e1) {
				MainWindow.sendErrorNotification(
						"Failed to add project. Perhaps it already exists? Please restart the application");
				LOGGER.log(Level.SEVERE, "failed to add", e1);
			}
		});
		add(btnNewButton, gbc_btnNewButton);

		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 3;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		add(separator, gbc_separator);

		JLabel lblAddAResource = new JLabel("Add a resource");
		GridBagConstraints gbc_lblAddAResource = new GridBagConstraints();
		gbc_lblAddAResource.anchor = GridBagConstraints.WEST;
		gbc_lblAddAResource.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddAResource.gridx = 0;
		gbc_lblAddAResource.gridy = 2;
		add(lblAddAResource, gbc_lblAddAResource);

		txtResource = new JTextField();
		GridBagConstraints gbc_txtResource = new GridBagConstraints();
		gbc_txtResource.insets = new Insets(0, 0, 5, 5);
		gbc_txtResource.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtResource.gridx = 1;
		gbc_txtResource.gridy = 2;
		add(txtResource, gbc_txtResource);
		txtResource.setColumns(10);

		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.addActionListener(e -> {
			try {
				if (!new DbEngine().addResource(txtResource.getText()))
					JOptionPane.showConfirmDialog(null, "FAILED");
			} catch (SQLException e1) {
				LOGGER.log(Level.SEVERE, "", e1);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 2;
		add(btnNewButton_1, gbc_btnNewButton_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 3;
		add(separator_1, gbc_separator_1);

		JLabel lblNewLabel_1 = new JLabel("Add a something");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 4;
		add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JButton button = new JButton("Add");
		button.setEnabled(false);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 2;
		gbc_button.gridy = 4;
		add(button, gbc_button);

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 3;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 5;
		add(separator_2, gbc_separator_2);

		JLabel lblNoteTheseCannot = new JLabel("Note these cannot be removed. Add carefully!");
		GridBagConstraints gbc_lblNoteTheseCannot = new GridBagConstraints();
		gbc_lblNoteTheseCannot.gridwidth = 3;
		gbc_lblNoteTheseCannot.insets = new Insets(0, 0, 0, 5);
		gbc_lblNoteTheseCannot.gridx = 0;
		gbc_lblNoteTheseCannot.gridy = 6;
		add(lblNoteTheseCannot, gbc_lblNoteTheseCannot);
	}

}
