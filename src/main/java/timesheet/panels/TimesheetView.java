package timesheet.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import timesheet.Application;
import timesheet.RDNE;
import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.DTO.DTOTime;
import timesheet.connection.ConnectionManager;
import timesheet.connection.DBEngine.DbEngine;
import timesheet.utils.Utils;

public class TimesheetView extends JPanel {
	private static final long serialVersionUID = 1L;

	private List<Row> rows = new ArrayList<>();

	private DateTime dateTime = new DateTime();

	JLabel lblDay1;
	JLabel lblDay2;
	JLabel lblDay3;
	JLabel lblDay4;
	JLabel lblDay5;
	JLabel lblDay6;
	JLabel lblDay7;
	JLabel lblColumnTotal;
	JTextField txtTot1;
	JTextField txtTot2;
	JTextField txtTot3;
	JTextField txtTot4;
	JTextField txtTot5;
	JTextField txtTot6;
	JTextField txtTot7;
	JTextField txtTotTotal;

	JSeparator sep;

	List<JTextField> txtEndOfRows = new ArrayList<>();
	private JLabel lblRowTotal;

	private JLabel[] getLables() {
		JLabel[] lables = { lblDay1, lblDay2, lblDay3, lblDay4, lblDay5, lblDay6, lblDay7 };
		return lables;
	}

	private JTextField[] getTotalBoxes() {
		JTextField[] lables = { txtTot1, txtTot2, txtTot3, txtTot4, txtTot5, txtTot6, txtTot7 };
		return lables;
	}

	/**
	 * Create the panel.
	 * 
	 * @throws SQLException
	 * @throws RDNE
	 */
	public TimesheetView() throws SQLException, RDNE {

		jbinit();
		labelTextInit(getDateTime());
		try (Connection connection = ConnectionManager.getConnection();) {
			createTextFields(connection);
			populateTextField(connection);
		}
		calculateTotals();
		repaint();
		validate();
	}

	private void calculateTotals() {
		if (!validateInput())
			return;

		List<Row> rows2 = getRows();
		if (rows2.isEmpty())
			return;
		double time[][] = new double[rows2.size()][7];
		for (int i = 0; i < getRows().size(); i++) {
			Row row = getRows().get(i);
			for (int j = 0; j < row.getTxtRowDay().size(); j++) {
				JTextField jTextField = row.getTxtRowDay().get(j);
				double timeLogged = Double.valueOf(jTextField.getText());
				time[i][j] = timeLogged;
			}
		}

		// gets the total for the end of the rows
		int it = 0;
		{
			double totalRowTime[] = new double[getRows().size()];
			for (double[] x : time) {
				double tmp = 0.0;
				for (double y : x) {
					tmp += y;
				}
				totalRowTime[it++] = tmp;
			}
			for (int k = 0; k < txtEndOfRows.size(); k++) {
				JTextField jTextField = txtEndOfRows.get(k);
				// TODO still a bug :( hmmm
				jTextField.setText(String.valueOf(totalRowTime[k]));
			}
		}
		{
			// gets the column totals
			double totalTime[] = columnSum(time);

			// sets totals at bottom
			JTextField[] totalBoxes = getTotalBoxes();
			for (int j = 0; j < totalBoxes.length; j++) {
				double d = totalTime[j];
				if (d < Application.HOURS_IN_DAY) {
					totalBoxes[j].setBackground(Color.RED);
				} else if (d == Application.HOURS_IN_DAY) {
					totalBoxes[j].setBackground(Color.YELLOW);
				} else if (d > Application.HOURS_IN_DAY) {
					totalBoxes[j].setBackground(Color.GREEN);
				}
				totalBoxes[j].setText(String.valueOf(d));
			}

			// Set colour the bottom right total
			double tmp = 0.0;
			for (double d : totalTime) {
				tmp += d;
			}
			txtTotTotal.setText(String.valueOf(tmp));
			if (tmp < Application.HOURS_IN_WEEK) {
				txtTotTotal.setBackground(Color.RED);
			} else if (tmp == Application.HOURS_IN_WEEK) {
				txtTotTotal.setBackground(Color.YELLOW);
			} else if (tmp > Application.HOURS_IN_WEEK) {
				txtTotTotal.setBackground(Color.GREEN);
			}

		}
	}

	private boolean validateInput() {
		for (Row row : getRows()) {
			for (JTextField jTextField : row.getTxtRowDay()) {
				try {
					Double.valueOf(jTextField.getText());
					jTextField.setBackground(Color.WHITE);
				} catch (NumberFormatException e) {
					jTextField.setBackground(Color.RED);
					return false;
				}
			}
		}
		return true;
	}

	private double[] columnSum(double[][] array) {
		int size = array[0].length;
		double temp[] = new double[size];

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				temp[j] += array[i][j];
			}
		}
		return temp;
	}

	public void repopulateTextFields() throws SQLException, RDNE {
		rows = new ArrayList<>();
		lblColumnTotal.setText("");
		lblColumnTotal = null;
		remove(sep);
		sep = null;
		try (Connection connection = ConnectionManager.getConnection();) {
			createTextFields(connection);
			populateTextField(connection);
		}
		calculateTotals();
		repaint();
		validate();
	}

	public void populateTextField(Connection connection) throws SQLException, RDNE {
		DbEngine db = new DbEngine();
		HashMap<DTOProjectTimeSheet, List<DTOTime>> loggedTimeByResource = db.getLoggedTimeByResource(connection,
				Application.resource);

		for (Row row : getRows()) {
			List<DTOTime> times = null;
			for (Entry<DTOProjectTimeSheet, List<DTOTime>> entry : loggedTimeByResource.entrySet()) {
				if (row.getProjectTimesheet().equals(entry.getKey())) {
					times = entry.getValue();
					break;
				}
			}
			if (times == null) {
				System.out.println("FAILZ");
				times = new ArrayList<>();
				// System.exit(0);
			}

			List<JTextField> txtRowDay = row.getTxtRowDay();
			DateTime firstDayOfWeek = row.getDates();
			for (JTextField jTextField : txtRowDay) {
				for (DTOTime dtoTime : times) {
					if (firstDayOfWeek.withTimeAtStartOfDay().equals(dtoTime.getDate().withTimeAtStartOfDay())) {
						String valueOf = String.valueOf(dtoTime.getLogged());
						if (valueOf == null || valueOf.equals(""))
							valueOf = "0.0";
						jTextField.setText(valueOf);
					}
				}
				firstDayOfWeek = firstDayOfWeek.plusDays(1);
			}
		}

	}

	private void createTextFields(Connection connection) throws SQLException, RDNE {
		DbEngine db = new DbEngine();

		List<DTOProjectTimeSheet> allProjectsTimeSheetForResource = db.getAllProjectsTimeSheetForResource(connection,
				Application.resource);

		// Perf fix
		List<DTOProject> allProject = db.getAllProject(connection);
		int y = 1;
		for (DTOProjectTimeSheet dtoProjectTimeSheet : allProjectsTimeSheetForResource) {
			int x = 2;
			List<JTextField> txt = new ArrayList<>();
			for (int i = 0; i < 7; i++) {
				NumberFormat format = NumberFormat.getInstance();
				NumberFormatter formatter = new NumberFormatter(format);
				formatter.setMinimum(0.0);
				formatter.setMaximum(24.0);
				formatter.setCommitsOnValidEdit(true);
				formatter.setValueClass(Double.class);
				JFormattedTextField txtField = new JFormattedTextField(formatter);
				txtField.setSize(new Dimension(10, 25));
				txtField.setPreferredSize(new Dimension(10, 25));
				txtField.setSize(new Dimension(10, 25));
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = x++;
				gbc_textField.gridy = y;
				add(txtField, gbc_textField);
				txtField.setColumns(10);
				txtField.setText("0.0");
				txtField.setValue(new Double(0.0));
				txtField.addFocusListener(getFocus());

				txt.add(txtField);
			}

			JTextField txtEndOfRowTotal = new JTextField();
			txtEndOfRowTotal.setPreferredSize(new Dimension(10, 25));
			txtEndOfRowTotal.setSize(new Dimension(10, 25));
			txtEndOfRowTotal.setSize(new Dimension(10, 25));
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = x;
			gbc_textField.gridy = y;
			add(txtEndOfRowTotal, gbc_textField);
			txtEndOfRowTotal.setColumns(10);
			txtEndOfRowTotal.setText("0.0");
			txtEndOfRowTotal.setEditable(false);
			txtEndOfRows.add(txtEndOfRowTotal);

			DTOProject project = null;
			for (DTOProject dtoProject : allProject) {
				if (dtoProject.getProjectId() == dtoProjectTimeSheet.getProjectId()) {
					project = dtoProject;
				}
			}
			JLabel lblProject = new JLabel(project.getProjectName());
			GridBagConstraints gbc_lblProject = new GridBagConstraints();
			gbc_lblProject.insets = new Insets(0, 0, 5, 5);
			gbc_lblProject.anchor = GridBagConstraints.EAST;
			gbc_lblProject.gridx = 1;
			gbc_lblProject.gridy = y;
			add(lblProject, gbc_lblProject);

			y++;

			rows.add(new Row(lblProject, txt, dtoProjectTimeSheet, Utils.getFirstDateOfWeek(getDateTime())));
		}

		// Create separator
		sep = new JSeparator(JSeparator.HORIZONTAL);
		GridBagConstraints gbc_Sep = new GridBagConstraints();
		gbc_Sep.insets = new Insets(0, 0, 5, 5);
		gbc_Sep.fill = GridBagConstraints.HORIZONTAL;
		gbc_Sep.gridx = 1;
		gbc_Sep.gridwidth = 9;
		gbc_Sep.gridy = y++;
		add(sep, gbc_Sep);

		// Create total rows
		int x2 = 2;
		for (JTextField jTextField : getTotalBoxes()) {
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			jTextField.setPreferredSize(new Dimension(10, 25));
			jTextField.setSize(new Dimension(10, 25));
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = x2++;
			gbc_textField.gridy = y;
			add(jTextField, gbc_textField);
			jTextField.setEditable(false);
			jTextField.setColumns(10);
		}

		txtTotTotal = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = x2++;
		gbc_textField.gridy = y;
		txtTotTotal.setPreferredSize(new Dimension(10, 25));
		txtTotTotal.setSize(new Dimension(10, 25));
		add(txtTotTotal, gbc_textField);
		txtTotTotal.setEditable(false);
		txtTotTotal.setColumns(10);

		lblRowTotal = new JLabel("Total");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 9;
		gbc_lblNewLabel.gridy = 0;
		add(lblRowTotal, gbc_lblNewLabel);

		lblColumnTotal = new JLabel("Total");
		GridBagConstraints gbg = new GridBagConstraints();
		gbg.anchor = GridBagConstraints.EAST;
		gbg.insets = new Insets(0, 0, 5, 5);
		gbg.gridx = 1;
		gbg.gridy = y;
		add(lblColumnTotal, gbg);

	}

	private FocusListener getFocus() {
		return new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				calculateTotals();
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		};
	}

	public void labelTextInit(DateTime dateTime) {
		DateTime first = Utils.getFirstDateOfWeek(dateTime);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
		for (Component component : getLables()) {
			((JLabel) component).setText(fmt.print(first));
			first = first.plusDays(1);
		}
	}

	private void jbinit() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		// HACK ALERT!
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblDay1 = new JLabel();
		lblDay1.setText(".");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		add(lblDay1, gbc_lblNewLabel);

		lblDay2 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 3;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblDay2, gbc_lblNewLabel_1);

		lblDay3 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 4;
		gbc_lblNewLabel_2.gridy = 0;
		add(lblDay3, gbc_lblNewLabel_2);

		lblDay4 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 5;
		gbc_lblNewLabel_3.gridy = 0;
		add(lblDay4, gbc_lblNewLabel_3);

		lblDay5 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 6;
		gbc_lblNewLabel_4.gridy = 0;
		add(lblDay5, gbc_lblNewLabel_4);

		lblDay6 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 7;
		gbc_lblNewLabel_5.gridy = 0;
		add(lblDay6, gbc_lblNewLabel_5);

		lblDay7 = new JLabel(".");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 8;
		gbc_lblNewLabel_6.gridy = 0;
		add(lblDay7, gbc_lblNewLabel_6);

		txtTot1 = new JTextField();
		txtTot2 = new JTextField();
		txtTot3 = new JTextField();
		txtTot4 = new JTextField();
		txtTot5 = new JTextField();
		txtTot6 = new JTextField();
		txtTot7 = new JTextField();
		txtTotTotal = new JTextField();
	}

	public List<Row> getRows() {
		return rows;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}
