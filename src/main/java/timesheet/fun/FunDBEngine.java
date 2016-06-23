package timesheet.fun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

import timesheet.DTO.DTOResource;
import timesheet.DTO.DTOTime;

public class FunDBEngine {
	public List<DTOTime> getAllTimeForResource(Connection connection, DTOResource res) throws SQLException {
		List<DTOTime> pts = new ArrayList<>();

		String sql = "SELECT date, timeLogged, t.project_timesheet_id, notes FROM time t JOIN project_timesheet pt "
				+ "WHERE pt.project_timesheet_id = t.project_timesheet_id AND resource_id = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, res.getResourceId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					DateTime date = new DateTime(rs.getDate(i++));
					double rid = rs.getDouble(i++);
					int pid = rs.getInt(i++);
					String notes = rs.getString(i++);
					pts.add(new DTOTime(date, rid, pid, notes));
				}
			}
		}
		return pts;
	}

	public String getFunFact(Connection connection) throws SQLException {
		int max = 0;
		String sql = "SELECT max(idfunfact) from funFact";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				max = rs.getInt(1);
			}
		}

		sql = "SELECT funFact from funFact WHERE idfunFact = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, new Random().nextInt(max) + 1);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}
		return "FACT NOT FOUND :(";
	}

}
