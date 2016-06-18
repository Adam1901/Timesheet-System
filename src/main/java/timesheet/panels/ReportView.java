package timesheet.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOResource;
import timesheet.connection.ConnectionManager;
import timesheet.connection.DBEngine.DbEngine;
import timesheet.connection.DBEngine.Report;
import timesheet.utils.DateUtils;

public class ReportView extends JPanel {
	private static final long serialVersionUID = 1L;

	JComboBox<DTOProject> cmbProjectList = new JComboBox<DTOProject>();
	JComboBox<DTOResource> cmbUserList = new JComboBox<DTOResource>();

	JTextPane textPane;

	public ReportView() throws SQLException {
		jbInit();
		fillCmbBoxes();
	}

	private void fillCmbBoxes() throws SQLException {
		DbEngine db = new DbEngine();
		try (Connection connection = ConnectionManager.getConnection();) {
			List<DTOProject> allProject = db.getAllProject(connection);
			List<DTOResource> allResources = db.getAllResources(connection);
			for (DTOResource dtoResource : allResources) {
				cmbUserList.addItem(dtoResource);
			}
			for (DTOProject dtoProject : allProject) {
				cmbProjectList.addItem(dtoProject);
			}
		}
	}

	private void jbInit() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 5, 0, 0, 0, 0, 0, 5, 0 };
		gridBagLayout.rowHeights = new int[] { 5, 0, 0, 0, 0, 0, 5, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("User");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		JCheckBox chcUseUsers = new JCheckBox("Use all users?");
		chcUseUsers.addActionListener(e -> {
			if (chcUseUsers.isSelected()) {
				cmbUserList.setEnabled(false);
				cmbUserList.setSelectedIndex(-1);
			} else {
				cmbUserList.setEnabled(true);
				cmbUserList.setSelectedIndex(0);
			}
		});
		GridBagConstraints gbc_chcUseUsers = new GridBagConstraints();
		gbc_chcUseUsers.insets = new Insets(0, 0, 5, 5);
		gbc_chcUseUsers.gridx = 2;
		gbc_chcUseUsers.gridy = 1;
		add(chcUseUsers, gbc_chcUseUsers);

		GridBagConstraints gbc_cmbUserList = new GridBagConstraints();
		gbc_cmbUserList.gridwidth = 3;
		gbc_cmbUserList.insets = new Insets(0, 0, 5, 5);
		gbc_cmbUserList.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbUserList.gridx = 3;
		gbc_cmbUserList.gridy = 1;
		add(cmbUserList, gbc_cmbUserList);

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		DateTime date = DateUtils.getFirstDateOfWeek(new DateTime());
		JDatePanelImpl datePanel = new JDatePanelImpl(createDateModel(date), p);

		Properties p1 = new Properties();
		p1.put("text.today1", "Today");
		p1.put("text.month1", "Month");
		p1.put("text.year1", "Year");
		date = date.plusDays(6);
		JDatePanelImpl datePanel2 = new JDatePanelImpl(createDateModel(date), p);

		JLabel lblNewLabel_1 = new JLabel("StartDate:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		// Don't know about the formatter, but there it is...
		JDatePickerImpl startDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		GridBagConstraints gbc_Date1 = new GridBagConstraints();
		gbc_Date1.gridwidth = 2;
		gbc_Date1.insets = new Insets(0, 0, 5, 5);
		gbc_Date1.fill = GridBagConstraints.HORIZONTAL;
		gbc_Date1.gridx = 2;
		gbc_Date1.gridy = 2;
		add(startDatePicker, gbc_Date1);

		JLabel lblEndDate = new JLabel("End Date:");
		GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
		gbc_lblEndDate.anchor = GridBagConstraints.EAST;
		gbc_lblEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndDate.gridx = 4;
		gbc_lblEndDate.gridy = 2;
		add(lblEndDate, gbc_lblEndDate);

		JDatePickerImpl endDatePicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		GridBagConstraints gbc_Date2 = new GridBagConstraints();
		gbc_Date2.insets = new Insets(0, 0, 5, 5);
		gbc_Date2.fill = GridBagConstraints.HORIZONTAL;
		gbc_Date2.gridx = 5;
		gbc_Date2.gridy = 2;
		add(endDatePicker, gbc_Date2);

		JLabel lblProject = new JLabel("Project");
		GridBagConstraints gbc_lblProject = new GridBagConstraints();
		gbc_lblProject.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblProject.gridx = 1;
		gbc_lblProject.gridy = 3;
		add(lblProject, gbc_lblProject);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Use All projects?");
		chckbxNewCheckBox.addActionListener(arg0 -> {
			if (chckbxNewCheckBox.isSelected()) {
				cmbProjectList.setEnabled(false);
				cmbProjectList.setSelectedIndex(-1);
			} else {
				cmbProjectList.setEnabled(true);
				cmbProjectList.setSelectedIndex(0);
			}
		});
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 2;
		gbc_chckbxNewCheckBox.gridy = 3;
		add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);

		GridBagConstraints gbc_cmbProjectList = new GridBagConstraints();
		gbc_cmbProjectList.gridwidth = 3;
		gbc_cmbProjectList.insets = new Insets(0, 0, 5, 5);
		gbc_cmbProjectList.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbProjectList.gridx = 3;
		gbc_cmbProjectList.gridy = 3;
		add(cmbProjectList, gbc_cmbProjectList);

		JButton btnRunReport = new JButton("Run Report");
		btnRunReport.addActionListener(arg0 -> {
			DbEngine db = new DbEngine();
			try {
				Report report = new Report();
				if (chckbxNewCheckBox.isSelected()) {
					report.setUseAllProjects(true);
				} else {
					report.setUseAllProjects(false);
					report.setProject((DTOProject) cmbProjectList.getSelectedItem());
				}
				if (chcUseUsers.isSelected()) {
					report.setUseAllUsers(true);
				} else {
					report.setUseAllUsers(false);
					report.setResource((DTOResource) cmbUserList.getSelectedItem());
				}

				report.setEnd(getDateTime(endDatePicker));
				report.setStart(getDateTime(startDatePicker));
				String runReport = db.runReport(report);
				System.out.println(runReport);

				DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(textPane.getText());
				stringBuilder.append("Report ran for: \"").append(chcUseUsers.isSelected() ? "All"
						: ((DTOResource) cmbUserList.getSelectedItem()).getResourceName());
				stringBuilder.append("\". For project: \"").append(chckbxNewCheckBox.isSelected() ? "All"
						: ((DTOProject) cmbProjectList.getSelectedItem()).getProjectName());
				stringBuilder.append("\". The time was logged between: ").append(fmt.print(report.getStart()));
				stringBuilder.append(" - ").append(fmt.print(report.getEnd()));
				stringBuilder.append(". The amount of time looged is: ").append(runReport).append(" hours.");
				stringBuilder.append("\n");
				textPane.setText(stringBuilder.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog(null, "FAILED");
			}
		});
		GridBagConstraints gbc_btnRunReport = new GridBagConstraints();
		gbc_btnRunReport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRunReport.gridwidth = 5;
		gbc_btnRunReport.insets = new Insets(0, 0, 5, 5);
		gbc_btnRunReport.gridx = 1;
		gbc_btnRunReport.gridy = 4;
		add(btnRunReport, gbc_btnRunReport);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		add(scrollPane, gbc_scrollPane);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
	}

	private UtilDateModel createDateModel(DateTime date) {
		UtilDateModel utilDateModel = new UtilDateModel();
		int year = date.getYear();
		int monthOfYear = date.getMonthOfYear() - 1; // Stupid java 0 based
														// month /facepalm
		int dayOfMonth = date.getDayOfMonth();
		utilDateModel.setDate(year, monthOfYear, dayOfMonth);
		utilDateModel.setSelected(true);
		return utilDateModel;
	}

	private DateTime getDateTime(JDatePickerImpl date) {
		Date value = (Date) date.getModel().getValue();
		return new DateTime(value.getTime());
	}

	public class DateLabelFormatter extends AbstractFormatter {
		private static final long serialVersionUID = 1L;
		private String datePattern = "dd/MM/yyyy";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

	}
}
