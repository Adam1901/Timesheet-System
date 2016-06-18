package timesheet;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import timesheet.DTO.DTOResource;
import timesheet.connection.DBEngine.DbEngine;
import timesheet.panels.MainWindow;

public class Application {
	public static String name = null;
	public static DTOResource resource;
	public static double HOURS_IN_DAY = 7.5;
	public static double HOURS_IN_WEEK = 37.5;

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, RDNE, UnsupportedLookAndFeelException {
		Props.setProperty("test", "test");
		UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		String propertyName = args.length == 0 ? Props.getProperty("username") : null;
		String nameToUse = null;
		if (propertyName == null || propertyName.trim().equals("")) {
			nameToUse = JOptionPane.showInputDialog(null, "Please enter your name to log in.");
		} else {
			nameToUse = propertyName;
		}
		if (nameToUse != null) {
			name = nameToUse;
		} else {
			System.exit(0);
		}
		Props.setProperty("username", name);

		resource = new DbEngine().getResource(Application.name);
		Props.setProperty("name", name);
		Props.setProperty("resName", resource.getResourceName());
		MainWindow frame = new MainWindow();
		frame.setVisible(true);
	}

}
