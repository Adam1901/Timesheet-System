package timesheet;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserName;

	/**
	 * Create the frame.
	 */
	public LoginFrame(String[] args) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 98);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Username");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		txtUserName = new JTextField();
		GridBagConstraints gbc_txtUserName = new GridBagConstraints();
		gbc_txtUserName.insets = new Insets(0, 0, 5, 0);
		gbc_txtUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUserName.gridx = 1;
		gbc_txtUserName.gridy = 0;
		contentPane.add(txtUserName, gbc_txtUserName);
		txtUserName.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					login(args);
				} catch (ClassNotFoundException | SQLException | UnsupportedLookAndFeelException | RDNE e1) {
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		contentPane.add(btnLogin, gbc_btnNewButton);
	}

	private void login(String[] args)
			throws SQLException, ClassNotFoundException, UnsupportedLookAndFeelException, RDNE {

		Application.name = txtUserName.getText();
		Application.login();
	}

}
