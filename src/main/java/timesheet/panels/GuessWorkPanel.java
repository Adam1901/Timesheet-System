package timesheet.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import timesheet.extras.WindowWatcher;
import timesheet.utils.Props;

public class GuessWorkPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	boolean running = false;

	public GuessWorkPanel() {
		WindowWatcher windowWatcher = new WindowWatcher();
		DefaultListModel<String> listModel = new DefaultListModel<>();
		JList<String> list = new JList<String>(listModel);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(arg0 -> {
			Props.setProperty("WatchWindows", "true");
			if (!running) {
				Thread t = new Thread(windowWatcher);
				t.start();
				running = true;
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

		JButton btnNewButton_1 = new JButton("Report");
		btnNewButton_1.addActionListener(e -> {
			listModel.removeAllElements();

			List<String> strs = new ArrayList<>();

			Map<String, Integer> times = windowWatcher.getAdjustedTime();
			int highest = -1;
			for (Entry<String, Integer> time1 : times.entrySet()) {
				Integer value1 = time1.getValue();
				if (value1 > highest)
					highest = value1;
			}
			for (Entry<String, Integer> time2 : times.entrySet()) {
				Integer value2 = time2.getValue();
				if (value2 == highest)
					continue;
				strs.add(secondsToString(value2) + " - " + time2.getKey());
			}

			Collections.sort(strs, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					Integer o11 = Integer.valueOf(o1.substring(0, 2));
					Integer o12 = Integer.valueOf(o1.substring(3, 5));

					Integer o21 = Integer.valueOf(o2.substring(0, 2));
					Integer o22 = Integer.valueOf(o2.substring(3, 5));

					int compareTo = o21.compareTo(o11);
					if (compareTo == 0) {
						return o22.compareTo(o12);
					} else {
						return compareTo;
					}
				}
			});

			for (String string : strs) {
				listModel.addElement(string);
			}

		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 0;
		add(btnNewButton_1, gbc_btnNewButton_1);

		JLabel lblNewLabel = new JLabel(
				"Note: It does try to be clever. However it should not be relied on, and it is not 100% accurate");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JButton btnNewButton_2 = new JButton("Erase History");
		btnNewButton_2.addActionListener(arg0 -> {
			windowWatcher.setTime(new HashMap<>());
			listModel.removeAllElements();
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 3;
		gbc_btnNewButton_2.gridy = 0;
		add(btnNewButton_2, gbc_btnNewButton_2);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		scrollPane.setViewportView(list);
	}

	private String secondsToString(int pTime) {
		return String.format("%02d:%02d", pTime / 60, pTime % 60);
	}
}
