package timesheet.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AboutPanel extends JPanel {
	int easter = 0;

	public AboutPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblIfYouEnconter = new JLabel("If you enconter any issues. Please restart the application");
		GridBagConstraints gbc_lblIfYouEnconter = new GridBagConstraints();
		gbc_lblIfYouEnconter.anchor = GridBagConstraints.WEST;
		gbc_lblIfYouEnconter.insets = new Insets(0, 0, 5, 0);
		gbc_lblIfYouEnconter.gridx = 1;
		gbc_lblIfYouEnconter.gridy = 1;
		add(lblIfYouEnconter, gbc_lblIfYouEnconter);

		JLabel lblIfTheyPresist = new JLabel("If they presist, ask adam");
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
		gbc_lblIfTheyPresist.anchor = GridBagConstraints.WEST;
		gbc_lblIfTheyPresist.gridx = 1;
		gbc_lblIfTheyPresist.gridy = 2;
		add(lblIfTheyPresist, gbc_lblIfTheyPresist);
	}

	private static final long serialVersionUID = 1L;

}
