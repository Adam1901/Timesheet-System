package timesheet.panels;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.joda.time.DateTime;

import timesheet.Application;
import timesheet.RDNE;
import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.DTO.DTOTime;
import timesheet.components.JFormattedTextFieldWithNotes;
import timesheet.connection.ConnectionManager;
import timesheet.connection.DBEngine.DbEngine;
import timesheet.utils.Props;
import timesheet.utils.Utils;

public class MainWindow extends JFrame {
	private final static Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JLabel lblNotify = new JLabel("Status: Loading");
	public static boolean madeChanges = false;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 * @throws RDNE
	 */
	public MainWindow() throws SQLException, RDNE {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		LOGGER.info("test");
		setBounds(100, 100, 763, 537);
		setSize(1090, 372);

		try {
			createImages();
		} catch (Exception e2) {
			System.out.println("Couldn't create image for icon");
			e2.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 100, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 10;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 1;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		TimesheetView timesheetView = new TimesheetView();

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		DateTime date = Utils.getFirstDateOfWeek(timesheetView.getDateTime());
		UtilDateModel createDateModel = Utils.createDateModel(date);
		JDatePanelImpl datePanel = new JDatePanelImpl(createDateModel, p);
		datePanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DateTime dt = new DateTime(createDateModel.getValue().getTime());
				dt = Utils.getFirstDateOfWeek(dt);
				setDate(dt, timesheetView, createDateModel);
			}
		});

		tabbedPane.addTab("Timesheet", null, timesheetView.getPanel(), null);
		tabbedPane.addTab("Reports", null, new ReportView(), null);
		if (Application.resource.getAdminLevel() >= 2)
			tabbedPane.addTab("Admin", null, new AdminPanel(), null);
		tabbedPane.addTab("About", null, new AboutPanel(), null);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(arg0 -> {
			saveTimeData(timesheetView);
		});

		JButton btnAddProject = new JButton("Add or Show Project");
		btnAddProject.setToolTipText("Click to add a new project to your timesheet or show an existing one.");
		btnAddProject.addActionListener(e -> {

			try {
				DbEngine dbEngine = new DbEngine();
				List<DTOProject> projectList = dbEngine.getAllProjects();

				Collections.sort(projectList, (v1, v2) -> v1.getProjectName().compareTo(v2.getProjectName()));

				DTOProject[] projectArr = new DTOProject[projectList.size()];
				projectArr = projectList.toArray(projectArr);

				DTOProject input = (DTOProject) JOptionPane.showInputDialog(null, "Choose one",
						"Add a project you have worked on", JOptionPane.QUESTION_MESSAGE, null, projectArr,
						projectArr[0]);

				if (input != null) {
					String property = Props.getProperty(TimesheetView.HIDE_PROPERTY);
					if (property != null && property.contains(",")) {
						List<String> list = new ArrayList<>(Arrays.asList(property.split(",")));
						for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
							String string = iterator.next();
							int id = Integer.valueOf(string);
							if (id == input.getProjectId()) {
								// Remove from prop
								iterator.remove();
								Props.setProperty(TimesheetView.HIDE_PROPERTY, String.join(",", list));
								if (list.size() == 1) {
									Props.setProperty(TimesheetView.HIDE_PROPERTY, id + ",");
								}
								timesheetView.repopulateTextFields();
								return;
							}
						}
					}

					dbEngine.addProjectTimeSheet(Application.resource.getResourceId(), input.getProjectId());
					timesheetView.repopulateTextFields();
				}

			} catch (SQLException | RDNE e1) {
				LOGGER.log(Level.SEVERE, "", e1);

			}
		});

		JButton btnMinusWeek = new JButton("-1 Week");
		btnMinusWeek.addActionListener(e -> {
			DateTime plusDays = timesheetView.getDateTime().plusDays(-7);
			setDate(plusDays, timesheetView, createDateModel);
		});
		JDatePickerImpl startDatePicker = new JDatePickerImpl(datePanel, new Utils().new DateLabelFormatter());

		GridBagConstraints gbc_datePanel = new GridBagConstraints();
		gbc_datePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_datePanel.gridwidth = 4;
		gbc_datePanel.insets = new Insets(0, 0, 5, 5);
		gbc_datePanel.gridx = 2;
		gbc_datePanel.gridy = 2;
		contentPane.add(startDatePicker, gbc_datePanel);
		GridBagConstraints gbc_btnMinusWeek = new GridBagConstraints();
		gbc_btnMinusWeek.insets = new Insets(0, 0, 5, 5);
		gbc_btnMinusWeek.gridx = 6;
		gbc_btnMinusWeek.gridy = 2;
		contentPane.add(btnMinusWeek, gbc_btnMinusWeek);

		JButton btnAddWeek = new JButton("+1 Week");
		btnAddWeek.addActionListener(arg0 -> {
			DateTime plusDays = timesheetView.getDateTime().plusDays(7);
			setDate(plusDays, timesheetView, createDateModel);
		});

		JButton btnToday = new JButton("Today");
		btnToday.addActionListener(arg0 -> {
			setDate(Utils.getFirstDateOfWeek(new DateTime()), timesheetView, createDateModel);
		});
		GridBagConstraints gbc_btnToday = new GridBagConstraints();
		gbc_btnToday.insets = new Insets(0, 0, 5, 5);
		gbc_btnToday.gridx = 7;
		gbc_btnToday.gridy = 2;
		contentPane.add(btnToday, gbc_btnToday);
		GridBagConstraints gbc_btnAddWeek = new GridBagConstraints();
		gbc_btnAddWeek.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddWeek.gridx = 8;
		gbc_btnAddWeek.gridy = 2;
		contentPane.add(btnAddWeek, gbc_btnAddWeek);
		GridBagConstraints gbc_btnAddProject = new GridBagConstraints();
		gbc_btnAddProject.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddProject.gridx = 9;
		gbc_btnAddProject.gridy = 2;
		contentPane.add(btnAddProject, gbc_btnAddProject);

		GridBagConstraints gbc_btnsave = new GridBagConstraints();
		gbc_btnsave.anchor = GridBagConstraints.EAST;
		gbc_btnsave.insets = new Insets(0, 0, 5, 5);
		gbc_btnsave.gridx = 10;
		gbc_btnsave.gridy = 2;
		contentPane.add(btnSave, gbc_btnsave);

		GridBagConstraints gbc_lblNotify = new GridBagConstraints();
		gbc_lblNotify.gridwidth = 8;
		gbc_lblNotify.anchor = GridBagConstraints.WEST;
		gbc_lblNotify.insets = new Insets(0, 0, 0, 5);
		gbc_lblNotify.gridx = 1;
		gbc_lblNotify.gridy = 3;
		contentPane.add(lblNotify, gbc_lblNotify);

		setTitle("Hello " + Application.resource.getResourceName() + ", Welcome to timesheet!");
		madeChanges = false;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (madeChanges) {
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"You have unsaved changes! Do you want to quit without saving?", "Warning",
							JOptionPane.WARNING_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					System.exit(0);
				}
			}
		});
	}

	private void setDate(DateTime dt, TimesheetView timesheetView, UtilDateModel createDateModel) {
		timesheetView.setDateTime(dt);
		try {
			timesheetView.repopulateTextFields();
			timesheetView.labelTextInit(dt);
			setDate(dt, createDateModel);
		} catch (SQLException | RDNE e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}

	private void saveTimeData(TimesheetView timesheetView) {
		Date start = new Date();
		LOGGER.info("StartSave");
		HashMap<DTOProjectTimeSheet, List<DTOTime>> ma = new HashMap<>();
		try (Connection connection = ConnectionManager.getConnection();) {
			for (Row row : timesheetView.getRows()) {
				List<JFormattedTextFieldWithNotes> txtRowDay = row.getTxtRowDay();
				DateTime date = row.getDate();

				List<DTOTime> times = new ArrayList<>();

				for (JFormattedTextFieldWithNotes jTextField : txtRowDay) {
					String text = jTextField.getText();
					if (Utils.isStringNullOrEmpty(text))
						text = "0.0";
					DTOTime time = new DTOTime(date, Double.valueOf(text),
							row.getProjectTimesheet().getProject_timesheet_id(), jTextField.getNotes());
					times.add(time);
					date = date.plusDays(1);
				}
				ma.put(row.getProjectTimesheet(), times);
			}
			new DbEngine().saveTimes(connection, Application.resource, ma);
			connection.commit();
			sendNotification("Save successful!");
			madeChanges = false;
		} catch (SQLException e1) {
			LOGGER.log(Level.SEVERE, "", e1);
			sendErrorNotification("Could not save your time sheet. Please try again. Sorry :(");
		}
		Date end = new Date();
		LOGGER.info("EndSave " + (end.getTime() - start.getTime()));
	}

	private void createImages() throws Exception {
		ImageIcon createImageIcon = createImageIcon("icon.png", "a");
		Image image = createImageIcon.getImage();
		setIconImage(image);

		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		MenuItem aboutMessageItem = new MenuItem("About");
		aboutMessageItem.addActionListener(e -> {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Hi\n");
			stringBuilder.append("This is an early version of a simple timesheet.\n");
			stringBuilder.append("Can you find the easter egg?\n");
			stringBuilder.append("Please send any feedback to Adam");
			JOptionPane.showMessageDialog(null, stringBuilder.toString());
		});

		MenuItem closeItem = new MenuItem("Close");
		closeItem.addActionListener(e -> System.exit(0));

		MenuItem showHideItem = new MenuItem("Show/Hide");
		showHideItem.addActionListener(e -> {
			setVisible(!isShowing());
		});

		PopupMenu menu = new PopupMenu();
		menu.add(showHideItem);
		menu.add(aboutMessageItem);
		menu.add(closeItem);

		TrayIcon icon = new TrayIcon(image, "Timesheet", menu);
		icon.setImageAutoSize(true);

		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(icon);
		} catch (AWTException e1) {
			LOGGER.log(Level.SEVERE, "", e1);
		}
	}

	protected ImageIcon createImageIcon(String path, String description) {
		ClassLoader classLoader = getClass().getClassLoader();
		java.net.URL imgURL = classLoader.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			return null;
		}
	}

	public static void sendNotification(String text) {
		lblNotify.setText("Status: " + text);
		lblNotify.setForeground(Color.BLACK);
	}

	public static void sendErrorNotification(String text) {
		lblNotify.setText("Status: " + text);
		lblNotify.setForeground(Color.RED);
	}

	private void setDate(DateTime dt, UtilDateModel mod) {
		mod.setDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());
	}
}
