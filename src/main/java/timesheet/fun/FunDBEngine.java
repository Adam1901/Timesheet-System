package timesheet.fun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import timesheet.DTO.DTOResource;
import timesheet.DTO.DTOTime;
import timesheet.connection.ConnectionManager;

public class FunDBEngine {
	public List<DTOTime> getAllTimeForResource(DTOResource res) throws SQLException {
		List<DTOTime> pts = new ArrayList<>();

		String sql = "SELECT date, timeLogged, t.project_timesheet_id, notes FROM time t JOIN project_timesheet pt "
				+ "WHERE pt.project_timesheet_id = t.project_timesheet_id AND resource_id = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
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

}
