package timesheet.panels;

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
import javax.swing.JTextField;

import timesheet.connection.DBEngine.DbEngine;

public class AdminPanel extends JPanel {
	private final static Logger LOGGER = Logger.getLogger(AdminPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final JButton btnNewButton = new JButton("Add");
	private JTextField txtResource;

	public AdminPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Add new project");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
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

		JLabel lblAddAResource = new JLabel("Add a resource");
		GridBagConstraints gbc_lblAddAResource = new GridBagConstraints();
		gbc_lblAddAResource.anchor = GridBagConstraints.EAST;
		gbc_lblAddAResource.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddAResource.gridx = 0;
		gbc_lblAddAResource.gridy = 1;
		add(lblAddAResource, gbc_lblAddAResource);

		txtResource = new JTextField();
		GridBagConstraints gbc_txtResource = new GridBagConstraints();
		gbc_txtResource.insets = new Insets(0, 0, 5, 5);
		gbc_txtResource.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtResource.gridx = 1;
		gbc_txtResource.gridy = 1;
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
		gbc_btnNewButton_1.gridy = 1;
		add(btnNewButton_1, gbc_btnNewButton_1);

		JLabel lblNoteCannotBe = new JLabel("Note cannot be removed!");
		GridBagConstraints gbc_lblNoteCannotBe = new GridBagConstraints();
		gbc_lblNoteCannotBe.insets = new Insets(0, 0, 0, 5);
		gbc_lblNoteCannotBe.gridx = 1;
		gbc_lblNoteCannotBe.gridy = 2;
		add(lblNoteCannotBe, gbc_lblNoteCannotBe);
	}

}
