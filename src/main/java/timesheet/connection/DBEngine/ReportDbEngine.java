package timesheet.connection.DBEngine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timesheet.connection.ConnectionManager;

public class ReportDbEngine {
	public String runReport(ReportParameters report) throws SQLException {
		List<Double> total = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT timelogged ");
		sb.append(" FROM time t, project_timesheet pt ");
		sb.append(" WHERE t.project_timesheet_id = pt.project_timesheet_id ");
		if (!report.isUseAllProjects())
			sb.append(" AND project_id = ? ");
		if (!report.isUseAllUsers())
			sb.append(" AND resource_id = ? ");
		sb.append(" AND date between ? and ?");

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sb.toString());) {
			int col0 = 1;
			if (!report.isUseAllProjects())
				ps.setInt(col0++, report.getProject().getProjectId());
			if (!report.isUseAllUsers())
				ps.setInt(col0++, report.getResource().getResourceId());
			ps.setDate(col0++, new Date(report.getStart().getMillis()));
			ps.setDate(col0++, new Date(report.getEnd().getMillis()));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					total.add(rs.getDouble(1));
				}
			}
		}

		int i = 0;
		for (Double doublea : total) {
			i += doublea;
		}
		return String.valueOf(i);
	}
}
