package timesheet.panels;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import timesheet.connection.DBEngine.DbEngine;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class AdminPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final JButton btnNewButton = new JButton("Add");

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
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String project = textField.getText();
				try {
					if (!new DbEngine().addProject(project))
						JOptionPane.showConfirmDialog(null, "FAILED");
				} catch (SQLException e1) {
					JOptionPane.showConfirmDialog(null, "FAILED\nPerhaps it already exists?");
					e1.printStackTrace();
				}
			}
		});
		add(btnNewButton, gbc_btnNewButton);

		JLabel lblNoteCannotBe = new JLabel("Note cannot be removed!");
		GridBagConstraints gbc_lblNoteCannotBe = new GridBagConstraints();
		gbc_lblNoteCannotBe.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoteCannotBe.gridx = 1;
		gbc_lblNoteCannotBe.gridy = 1;
		add(lblNoteCannotBe, gbc_lblNoteCannotBe);
	}

}
