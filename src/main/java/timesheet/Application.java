package timesheet;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import timesheet.DTO.DTOResource;
import timesheet.connection.DBEngine.DbEngine;
import timesheet.panels.MainWindow;
import timesheet.utils.Props;
import timesheet.utils.Utils;

public class Application {
	public static String name = null;
	public static DTOResource resource;
	public static double HOURS_IN_DAY = 7.5;
	public static double HOURS_IN_WEEK = 37.5;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, RDNE {
		try {
			UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		String propertyName = Props.getProperty("username");
		String nameToUse = null;
		boolean useLoginForm = false;

		if (Utils.isStringNullOrEmpty(propertyName)) {
			useLoginForm = true;
		} else {
			nameToUse = propertyName;
		}
		if (nameToUse != null) {
			Application.name = nameToUse;
		} else {
			useLoginForm = true;
		}

		if (!useLoginForm) {
			login();
		} else {
			LoginFrame lFrm = new LoginFrame(args);
			lFrm.setVisible(true);
			lFrm.setLocationRelativeTo(null);
		}
	}

	public static boolean login() throws SQLException, RDNE {
		try {
			Application.resource = new DbEngine().getResource(Application.name);
		} catch (RDNE e) {
			// try again
			Application.name = null;
			Props.setProperty("username", "");
			JOptionPane.showMessageDialog(null, "Login Failed", "Login Failed. Please relaunch the application",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (Application.resource.getAdminLevel() == 0) {
			JOptionPane.showConfirmDialog(null, "Account disabled");
			System.exit(0);
		}
		Props.setProperty("username", Application.name);
		Props.setProperty("resID", String.valueOf(Application.resource.getResourceId()));
		MainWindow frame = new MainWindow();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return true;
	}

}