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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import timesheet.Application;
import timesheet.RDNE;
import timesheet.DTO.DTOProject;
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

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 * @throws RDNE
	 */
	public MainWindow() throws SQLException, RDNE {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 7;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 1;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		TimesheetView timesheetView = new TimesheetView();
		JScrollPane scrollFrame = new JScrollPane(timesheetView);
		scrollFrame.setPreferredSize(timesheetView.getPreferredSize());
		scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tabbedPane.addTab("Timesheet", null, scrollFrame, null);
		tabbedPane.addTab("Reports", null, new ReportView(), null);
		if (Application.resource.getAdminLevel() >= 2)
			tabbedPane.addTab("Admin", null, new AdminPanel(), null);
		tabbedPane.addTab("About", null, new AboutPanel(), null);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(arg0 -> {
			saveTimeData(timesheetView);
		});

		JButton btnAddProject = new JButton("Add Project");
		btnAddProject.addActionListener(e -> {

			try {
				DbEngine dbEngine = new DbEngine();
				List<DTOProject> projectList = dbEngine.getAllProject();

				Collections.sort(projectList, (v1, v2) -> v1.getProjectName().compareTo(v2.getProjectName()));

				DTOProject[] projectArr = new DTOProject[projectList.size()];
				projectArr = projectList.toArray(projectArr);

				DTOProject input = (DTOProject) JOptionPane.showInputDialog(null, "Choose one",
						"Add a project you have worked on", JOptionPane.QUESTION_MESSAGE, null, projectArr,
						projectArr[0]);

				if (input != null) {
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
			timesheetView.setDateTime(plusDays);
			try {
				timesheetView.repopulateTextFields();
				timesheetView.labelTextInit(plusDays);
			} catch (SQLException | RDNE ee) {
				LOGGER.log(Level.SEVERE, "", ee);
			}
		});

		// set to logout
		JButton btnDebug = new JButton("Debug");
		btnDebug.addActionListener(e -> {
			System.out.println(getSize());
			Props.deleteProperty("username");
			Props.deleteProperty("password");
			sendErrorNotification("LOGGED OUT");
		});

		GridBagConstraints gbc_lblNotify = new GridBagConstraints();
		gbc_lblNotify.anchor = GridBagConstraints.WEST;
		gbc_lblNotify.gridwidth = 7;
		gbc_lblNotify.insets = new Insets(0, 0, 0, 5);
		gbc_lblNotify.gridx = 1;
		gbc_lblNotify.gridy = 2;
		contentPane.add(lblNotify, gbc_lblNotify);
		GridBagConstraints gbc_btnDebug = new GridBagConstraints();
		gbc_btnDebug.insets = new Insets(0, 0, 0, 5);
		gbc_btnDebug.gridx = 3;
		gbc_btnDebug.gridy = 2;
		contentPane.add(btnDebug, gbc_btnDebug);
		GridBagConstraints gbc_btnMinusWeek = new GridBagConstraints();
		gbc_btnMinusWeek.insets = new Insets(0, 0, 0, 5);
		gbc_btnMinusWeek.gridx = 4;
		gbc_btnMinusWeek.gridy = 2;
		contentPane.add(btnMinusWeek, gbc_btnMinusWeek);

		JButton btnAddWeek = new JButton("+1 Week");
		btnAddWeek.addActionListener(arg0 -> {
			DateTime plusDays = timesheetView.getDateTime().plusDays(7);
			timesheetView.setDateTime(plusDays);
			try {
				timesheetView.repopulateTextFields();
				timesheetView.labelTextInit(plusDays);
			} catch (SQLException | RDNE e) {
				LOGGER.log(Level.SEVERE, "", e);
			}
		});
		GridBagConstraints gbc_btnAddWeek = new GridBagConstraints();
		gbc_btnAddWeek.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddWeek.gridx = 5;
		gbc_btnAddWeek.gridy = 2;
		contentPane.add(btnAddWeek, gbc_btnAddWeek);
		GridBagConstraints gbc_btnAddProject = new GridBagConstraints();
		gbc_btnAddProject.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddProject.gridx = 6;
		gbc_btnAddProject.gridy = 2;
		contentPane.add(btnAddProject, gbc_btnAddProject);

		GridBagConstraints gbc_btnsave = new GridBagConstraints();
		gbc_btnsave.anchor = GridBagConstraints.EAST;
		gbc_btnsave.insets = new Insets(0, 0, 0, 5);
		gbc_btnsave.gridx = 7;
		gbc_btnsave.gridy = 2;
		contentPane.add(btnSave, gbc_btnsave);

		setTitle("Hello " + Application.resource.getResourceName() + ", Welcome to timesheet!");
	}

	private void saveTimeData(TimesheetView timesheetView) {
		Date start = new Date();
		LOGGER.info("StartSave");
		try (Connection connection = ConnectionManager.getConnection();) {
			for (Row row : timesheetView.getRows()) {
				List<JFormattedTextFieldWithNotes> txtRowDay = row.getTxtRowDay();
				DateTime date = row.getDates();

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
				// TODO perf
				try {
					new DbEngine().saveTimes(connection, times, Application.resource, row.getProjectTimesheet());
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "", e);
					sendErrorNotification("Could not save your time sheet. Please try again. Sorry :(");
				}
			}
			connection.commit();
			sendNotification("Save successful!");
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
}
