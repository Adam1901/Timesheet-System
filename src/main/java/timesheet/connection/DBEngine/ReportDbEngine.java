package timesheet.connection.DBEngine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import timesheet.DTO.DTOResource;
import timesheet.connection.ConnectionManager;
import timesheet.utils.Utils;

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

		double i = 0.0;
		for (double doublea : total) {
			i += doublea;
		}
		return Utils.doubleValueOf(i);
	}

	public String runLargeReport(DateTime start, DateTime end) throws SQLException {
		StringBuilder ret = new StringBuilder();
		String sql = " SELECT r.resource_name, ";
		sql += "           p.project_name, ";
		sql += "           ROUND(SUM(t.timelogged), 1) ";
		sql += "      FROM time t, ";
		sql += "           resource r, ";
		sql += "           project p, ";
		sql += "           project_timesheet pt ";
		sql += "     WHERE pt.project_id = p.project_id ";
		sql += "       AND pt.resource_id = r.resource_id ";
		sql += "       AND pt.project_timesheet_id = t.project_timesheet_id ";
		if (start != null && end != null)
			sql += "       AND t.date BETWEEN ? AND ? ";
		sql += " GROUP BY resource_name, ";
		sql += "          project_name ";

		ret.append("Name, Project Name, Total time").append(System.lineSeparator());
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql.toString());) {
			if (start != null && end != null) {
				ps.setDate(1, new Date(start.getMillis()));
				ps.setDate(2, new Date(end.getMillis()));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ret.append(rs.getString(1) + ", ");
					ret.append(rs.getString(2) + ", ");
					double double1 = rs.getDouble(3);
					ret.append(double1 + System.lineSeparator());
				}
			}
		}

		return ret.toString();
	}

	public String runProjectReport(DateTime start, DateTime end) throws SQLException {
		StringBuilder title = new StringBuilder();
		StringBuilder ret = new StringBuilder();
		String sql = " SELECT  p.project_name, ROUND(SUM(t.timelogged), 1)";
		sql += "          FROM time t, ";
		sql += "           project p, ";
		sql += "           project_timesheet pt ";
		sql += "     WHERE pt.project_id = p.project_id ";
		sql += "       AND pt.resource_id = ?";
		sql += "       AND pt.project_timesheet_id = t.project_timesheet_id ";
		if (start != null && end != null)
			sql += "       AND t.date BETWEEN ? AND ? ";
		sql += " GROUP BY project_name ";

		title.append("Project Name, ");
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql.toString());) {
			List<DTOResource> allResources = new DbEngine().getAllResources(connection);
			for (DTOResource dtoResource : allResources) {
				title.append(dtoResource.getResourceName() + ", ");
				ps.setInt(1, dtoResource.getResourceId());
				if (start != null && end != null) {
					ps.setDate(2, new Date(start.getMillis()));
					ps.setDate(3, new Date(end.getMillis()));
				}

				try (ResultSet rs = ps.executeQuery()) {
					ps.clearParameters();
					while (rs.next()) {
						ret.append(rs.getString(1) + ", ");
						double double1 = rs.getDouble(2);
						ret.append(double1 + System.lineSeparator());
					}
					ret.append("0.0, ");
				}

			}
		}
		return title.toString() + System.lineSeparator() + ret.toString();
	}

}
