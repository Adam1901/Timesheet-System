package timesheet.connection.DBEngine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import timesheet.RDNE;
import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.DTO.DTOResource;
import timesheet.DTO.DTOTime;
import timesheet.connection.ConnectionManager;

public class DbEngine {
	// Lazy day, todo refactor out

	public HashMap<DTOProjectTimeSheet, List<DTOTime>> getLoggedTimeByResource(Connection connection, DTOResource res,
			DateTime dateTime) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"SELECT date, timelogged, t.project_timesheet_id, resource_id, project_id, t.notes FROM time t ");
		stringBuilder.append(
				"join project_timesheet pt where t.project_timesheet_id = pt.project_timesheet_id and pt.resource_id = ? ");
		if (dateTime != null)
			stringBuilder.append("AND t.date between ? AND ? ");
		String sql = stringBuilder.toString();
		HashMap<DTOProjectTimeSheet, List<DTOTime>> ret = new HashMap<>();
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, res.getResourceId());
			DateTime dateTime2 = dateTime;
			if (dateTime != null) {
				ps.setDate(2, new Date(dateTime2.getMillis()));
				dateTime2 = dateTime2.plusDays(7);
				ps.setDate(3, new Date(dateTime2.getMillis()));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					Date date = rs.getDate(i++);
					double timeLogged = rs.getDouble(i++);
					int ptid = rs.getInt(i++);
					int rid = rs.getInt(i++);
					int pid = rs.getInt(i++);
					String notes = rs.getString(i++);

					DTOProjectTimeSheet dtoPt = new DTOProjectTimeSheet(ptid, pid, rid);
					DTOTime dtoTime = new DTOTime(new DateTime(date.getTime()), timeLogged, ptid, notes);

					boolean containsKey = ret.containsKey(dtoPt);
					if (containsKey) {
						List<DTOTime> list2 = ret.get(dtoPt);
						list2.add(dtoTime);
					} else {
						List<DTOTime> value = new ArrayList<>();
						value.add(dtoTime);
						ret.put(dtoPt, value);
					}
				}
			}
		}
		return ret;
	}

	public List<DTOProjectTimeSheet> getAllProjectsTimeSheetForResource(Connection connection, DTOResource res)
			throws SQLException {
		List<DTOProjectTimeSheet> pts = new ArrayList<>();

		String sql = "SELECT project_timesheet_id, resource_id, project_id FROM project_timesheet WHERE resource_id = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, res.getResourceId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					int ptid = rs.getInt(i++);
					int rid = rs.getInt(i++);
					int pid = rs.getInt(i++);
					pts.add(new DTOProjectTimeSheet(ptid, pid, rid));
				}
			}
		}
		return pts;
	}

	public List<DTOResource> getAllResources(Connection connection) throws SQLException {
		List<DTOResource> resources = new ArrayList<DTOResource>();

		String sql = "SELECT resource_id, resource_name FROM resource";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resources.add(new DTOResource(rs.getInt(1), rs.getString(2)));
			}
		}
		return resources;
	}

	public DTOResource getResource(String name) throws SQLException, RDNE {
		String sql = "SELECT resource_id, resource_name FROM resource where resource_name = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return new DTOResource(rs.getInt(1), rs.getString(2));
				}
			}
		}
		throw new RDNE(name);
	}

	public List<DTOProject> getAllProject() throws SQLException {
		try (Connection connection = ConnectionManager.getConnection();) {
			return getAllProject(connection);
		}
	}

	public List<DTOProject> getAllProject(Connection connection) throws SQLException {
		List<DTOProject> projects = new ArrayList<>();
		String sql = "SELECT project_id, project_name FROM project";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				projects.add(new DTOProject(rs.getInt(1), rs.getString(2)));
			}
		}
		return projects;
	}

	public boolean addProject(String name) throws SQLException {
		String sql = "INSERT INTO project (project_name) VALUES (?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, name);
			int executeUpdate = ps.executeUpdate();
			if (executeUpdate == 1) {
				connection.commit();
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean addProjectTimeSheet(int resourceId, int projectId) throws SQLException {
		String sql = "INSERT INTO project_timesheet (`resource_id`, `project_id`) VALUES (?, ?)";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resourceId);
			ps.setInt(2, projectId);
			int executeUpdate = ps.executeUpdate();
			if (executeUpdate == 1) {
				connection.commit();
				return true;
			} else {
				return false;
			}
		}
	}

	public void saveTimes(Connection connection, List<DTOTime> times, DTOResource resource,
			DTOProjectTimeSheet projectTimesheet) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO time ");
		sb.append(" (date, timelogged, project_timesheet_id, notes) ");
		sb.append(" VALUES ");
		sb.append("       (?, ?, ?, ?) ");
		sb.append(" ON DUPLICATE KEY UPDATE ");
		sb.append("  timelogged = VALUES(timelogged), ");
		sb.append("  notes      = VALUES(notes) ");

		for (DTOTime dtoTime : times) {
			try (PreparedStatement ps = connection.prepareStatement(sb.toString());) {
				ps.setDate(1, new Date(dtoTime.getDate().getMillis()));
				ps.setDouble(2, dtoTime.getLogged());
				ps.setInt(3, projectTimesheet.getProject_timesheet_id());
				ps.setString(4, dtoTime.getNotes());
				ps.executeUpdate();
			}
		}
	}

	public boolean addResource(String text) throws SQLException {
		String sql = "INSERT INTO resource (resource_name) VALUES (?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, text);
			int executeUpdate = ps.executeUpdate();
			if (executeUpdate == 1) {
				connection.commit();
				return true;
			} else {
				return false;
			}
		}
	}

	// Report SQL
	// SELECT t.date, r.resource_name, p.project_name , sum(t.timelogged) from
	// time t, resource r, project p, project_timesheet pt
	// where pt.project_id = p.project_id and pt.resource_id = r.resource_id and
	// pt.project_timesheet_id = t.project_timesheet_id group by resource_name,
	// project_name;

}
