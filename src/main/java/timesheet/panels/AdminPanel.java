package timesheet.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOResource;
import timesheet.connection.ConnectionManager;
import timesheet.connection.DBEngine.DbEngine;

public class AdminPanel extends JPanel {
	private final static Logger LOGGER = Logger.getLogger(AdminPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final JButton btnAddProject = new JButton("Add");
	private JTextField txtResource;

	JComboBox<DTOResource> cmbUsers = new JComboBox<DTOResource>();
	JComboBox<DTOProject> cmbProject = new JComboBox<DTOProject>();
	JComboBox<Levels> lvlsUsers = new JComboBox<Levels>();

	public AdminPanel() {
		jbInit();
		fillCmbBoxes();
	}

	private void jbInit() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0 };
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
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		btnAddProject.addActionListener(e -> {
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
		add(btnAddProject, gbc_btnNewButton);

		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 4;
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

		JButton btnAddResource = new JButton("Add");
		btnAddResource.addActionListener(e -> {
			try {
				Levels selectedItem = (Levels) lvlsUsers.getSelectedItem();
				if (!new DbEngine().addResource(txtResource.getText(), selectedItem.level))
					JOptionPane.showConfirmDialog(null, "FAILED");
			} catch (SQLException e1) {
				LOGGER.log(Level.SEVERE, "", e1);
			}
		});

		lvlsUsers.addItem(new Levels(1, "User"));
		lvlsUsers.addItem(new Levels(2, "Admin"));
		lvlsUsers.setSelectedIndex(0);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 2;
		add(lvlsUsers, gbc_comboBox);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 2;
		add(btnAddResource, gbc_btnNewButton_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 3;
		add(separator_1, gbc_separator_1);

		JLabel lblNewLabel_1 = new JLabel("Remove User's Project");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		GridBagConstraints gbc_cmbUsers = new GridBagConstraints();
		gbc_cmbUsers.insets = new Insets(0, 0, 5, 5);
		gbc_cmbUsers.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbUsers.gridx = 1;
		gbc_cmbUsers.gridy = 4;
		add(cmbUsers, gbc_cmbUsers);
		GridBagConstraints gbc_cmbProject = new GridBagConstraints();
		gbc_cmbProject.insets = new Insets(0, 0, 5, 5);
		gbc_cmbProject.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbProject.gridx = 2;
		gbc_cmbProject.gridy = 4;
		add(cmbProject, gbc_cmbProject);

		JButton btnRemove = new JButton("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// DTOProject proj = (DTOProject) cmbProject.getSelectedItem();
				// DTOResource res = (DTOResource) cmbUsers.getSelectedItem();
				// DbEngine db = new DbEngine();
				// // db.removeProjectFromResource(proj, res);
				// // TODO IMPLEMENT
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 3;
		gbc_btnRemove.gridy = 4;
		add(btnRemove, gbc_btnRemove);

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 4;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 5;
		add(separator_2, gbc_separator_2);

		JLabel lblNoteTheseCannot = new JLabel("Note these cannot be removed. Add carefully!");
		GridBagConstraints gbc_lblNoteTheseCannot = new GridBagConstraints();
		gbc_lblNoteTheseCannot.gridwidth = 4;
		gbc_lblNoteTheseCannot.gridx = 0;
		gbc_lblNoteTheseCannot.gridy = 6;
		add(lblNoteTheseCannot, gbc_lblNoteTheseCannot);
	}

	private void fillCmbBoxes() {
		DbEngine db = new DbEngine();
		try (Connection connection = ConnectionManager.getConnection();) {
			List<DTOProject> allProject = db.getAllProject(connection);
			List<DTOResource> allResources = db.getAllResources(connection);
			for (DTOResource dtoResource : allResources) {
				cmbUsers.addItem(dtoResource);
			}
			for (DTOProject dtoProject : allProject) {
				cmbProject.addItem(dtoProject);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to pop cmbs", e);
		}
	}

	private class Levels {
		public Levels(int level, String levelS) {
			this.level = level;
			this.levelS = levelS;
		}

		@Override
		public String toString() {
			return levelS;
		}

		public int level;
		private String levelS;
	}
}
