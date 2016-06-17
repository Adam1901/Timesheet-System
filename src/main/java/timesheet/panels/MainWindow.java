package timesheet.panels;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import timesheet.Application;
import timesheet.RDNE;
import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOTime;
import timesheet.connection.DBEngine.DbEngine;

import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 * @throws RDNE
	 */
	public MainWindow() throws SQLException, RDNE {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 763, 537);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 3;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 1;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		TimesheetView timesheetView = new TimesheetView();
		JScrollPane scrollFrame = new JScrollPane(timesheetView);
		scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		tabbedPane.addTab("Timesheet", null, scrollFrame, null);
		tabbedPane.addTab("Reports", null, new ReportView(), null);
		tabbedPane.addTab("Admin", null, new AdminPanel(), null);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Row row : timesheetView.getRows()) {
					List<JTextField> txtRowDay = row.getTxtRowDay();
					DateTime date = row.getDates();

					List<DTOTime> times = new ArrayList<>();

					for (JTextField jTextField : txtRowDay) {
						String text = jTextField.getText();
						if (text.trim().equals("") || text == null)
							text = "0.0";
						DTOTime time = new DTOTime(date, Double.valueOf(text),
								row.getProjectTimesheet().getProject_timesheet_id());
						times.add(time);
						date = date.plusDays(1);
						try {
							new DbEngine().saveTimes(times, Application.resource, row.getProjectTimesheet());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Add Project");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					DbEngine dbEngine = new DbEngine();
					List<DTOProject> projectList = dbEngine.getAllProject();

					Collections.sort(projectList, (v1, v2) -> v1.getProjectName().compareTo(v2.getProjectName()));

					DTOProject[] projectArr = new DTOProject[projectList.size()];
					projectArr = projectList.toArray(projectArr);

					DTOProject input = (DTOProject) JOptionPane.showInputDialog(null, "Choose one",
							"Add a project you have worked on", JOptionPane.QUESTION_MESSAGE, null, projectArr,
							projectArr[0]);
					System.out.println(input);

					if (input != null) {
						dbEngine.addProjectTimeSheet(Application.resource.getResourceId(), input.getProjectId());
						timesheetView.repopulateTextFields();
					}

				} catch (SQLException | RDNE e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 2;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 2;
		contentPane.add(btnSave, gbc_btnNewButton);
	}

}
