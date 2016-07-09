package timesheet.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import timesheet.fun.Achievement;
import timesheet.utils.Props;

public class AboutPanel extends JPanel {
	int easter = 0;
	boolean seen = false;

	public AboutPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 5, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNoteHidingA = new JLabel(
				"FAQ1: Hiding a row will mean the time in the row is not counted in the UI. Reporting will be unaffected.");
		GridBagConstraints gbc_lblNoteHidingA = new GridBagConstraints();
		gbc_lblNoteHidingA.anchor = GridBagConstraints.WEST;
		gbc_lblNoteHidingA.gridwidth = 2;
		gbc_lblNoteHidingA.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoteHidingA.gridx = 1;
		gbc_lblNoteHidingA.gridy = 1;
		add(lblNoteHidingA, gbc_lblNoteHidingA);

		JLabel lblFaqIfYou = new JLabel(
				"FAQ2: If you want to reshow a row, go about adding a new project to your timesheet");
		GridBagConstraints gbc_lblFaqIfYou = new GridBagConstraints();
		gbc_lblFaqIfYou.gridwidth = 2;
		gbc_lblFaqIfYou.insets = new Insets(0, 0, 5, 5);
		gbc_lblFaqIfYou.gridx = 1;
		gbc_lblFaqIfYou.gridy = 2;
		add(lblFaqIfYou, gbc_lblFaqIfYou);

		JLabel lblIfYouEnconter = new JLabel("If you enconter any issues or bugs. Please restart the application");
		GridBagConstraints gbc_lblIfYouEnconter = new GridBagConstraints();
		gbc_lblIfYouEnconter.gridwidth = 2;
		gbc_lblIfYouEnconter.anchor = GridBagConstraints.WEST;
		gbc_lblIfYouEnconter.insets = new Insets(0, 0, 5, 5);
		gbc_lblIfYouEnconter.gridx = 1;
		gbc_lblIfYouEnconter.gridy = 3;
		add(lblIfYouEnconter, gbc_lblIfYouEnconter);

		JLabel lblIfTheyPresist = new JLabel("If they presist, ask Adam!");
		lblIfTheyPresist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				easter++;
				if (easter == 10) {
					JOptionPane.showConfirmDialog(null, "You found an easter egg!");
				}
			}
		});
		GridBagConstraints gbc_lblIfTheyPresist = new GridBagConstraints();
		gbc_lblIfTheyPresist.insets = new Insets(0, 0, 5, 5);
		gbc_lblIfTheyPresist.anchor = GridBagConstraints.WEST;
		gbc_lblIfTheyPresist.gridx = 1;
		gbc_lblIfTheyPresist.gridy = 4;
		add(lblIfTheyPresist, gbc_lblIfTheyPresist);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Props.deleteProperty("username");
				Props.deleteProperty("password");
			}
		});

		JButton btnDidYouWin = new JButton("Did you win?");
		btnDidYouWin.addActionListener(e -> {
			try {
				if (!seen)
					new Achievement().calculateAchievements();
				seen = true;
			} catch (SQLException ee) {
			}
		});
		GridBagConstraints gbc_btnDidYouWin = new GridBagConstraints();
		gbc_btnDidYouWin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDidYouWin.insets = new Insets(0, 0, 5, 5);
		gbc_btnDidYouWin.gridx = 2;
		gbc_btnDidYouWin.gridy = 7;
		add(btnDidYouWin, gbc_btnDidYouWin);
		GridBagConstraints gbc_btnLogout = new GridBagConstraints();
		gbc_btnLogout.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogout.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogout.gridx = 2;
		gbc_btnLogout.gridy = 8;
		add(btnLogout, gbc_btnLogout);

		JLabel lblNewLabel = new JLabel("Made by Adam Meadows of Qmatic UK");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 9;
		add(lblNewLabel, gbc_lblNewLabel);
	}

	private static final long serialVersionUID = 1L;

}
