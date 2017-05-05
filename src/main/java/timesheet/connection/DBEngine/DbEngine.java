package timesheet.connection.DBEngine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import timesheet.RDNE;
import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.DTO.DTOResource;
import timesheet.DTO.DTOTime;
import timesheet.connection.ConnectionManager;

public class DbEngine {
	// Lazy day, TODO refactor out

	public HashMap<DTOProjectTimeSheet, List<DTOTime>> getLoggedTimeByResource(Connection connection, DTOResource res,
			DateTime dateTime, Integer project) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"SELECT date, timelogged, t.project_timesheet_id, resource_id, project_id, t.notes FROM time t ");
		stringBuilder.append(
				"join project_timesheet pt where t.project_timesheet_id = pt.project_timesheet_id and pt.resource_id = ? ");
		if (dateTime != null)
			stringBuilder.append("AND t.date between ? AND ? ");
		if (project != null)
			stringBuilder.append(" AND pt.project_id = ?");
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
			if (project != null)
				ps.setInt(4, project);
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

		String sql = "SELECT resource_id, resource_name, adminLevel FROM resource";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resources.add(new DTOResource(rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
		}
		return resources;
	}

	public DTOResource getResource(Connection connection, String name) throws SQLException, RDNE {
		String sql = "SELECT resource_id, resource_name, adminLevel FROM resource where resource_name = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return new DTOResource(rs.getInt(1), rs.getString(2), rs.getInt(3));
				}
			}
		}
		throw new RDNE(name);
	}

	public DTOResource getResource(String name) throws SQLException, RDNE {
		try (Connection connection = ConnectionManager.getConnection();) {
			return getResource(connection, name);
		}
	}

	public List<DTOProject> getAllProjects(Connection connection) throws SQLException {
		String sql = "SELECT project_id, project_name FROM project";
		return returnSortedProjects(connection, sql);
	}

	public List<DTOProject> getAllProjectsForCombo(Connection connection) throws SQLException {
		String sql = "SELECT project_id, project_name FROM project "
				+ " WHERE LOWER(project_name) <> 'admin' AND LOWER(project_name) <> 'holiday'";
		return returnSortedProjects(connection, sql);
	}

	private List<DTOProject> returnSortedProjects(Connection connection, String sql) throws SQLException {
		List<DTOProject> projects = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				projects.add(new DTOProject(rs.getInt(1), rs.getString(2)));
			}
			Collections.sort(projects, (v1, v2) -> v1.getProjectName().compareTo(v2.getProjectName()));
			return projects;
		}
	}

	public boolean addProject(String name, double assignedTime) throws SQLException {
		String sql = "INSERT INTO project (project_name, assignedTime) VALUES (?, ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, name);
			ps.setDouble(2, assignedTime);
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
		try (Connection connection = ConnectionManager.getConnection();) {
			return addProjectTimeSheet(connection, resourceId, projectId);
		}
	}

	public boolean addProjectTimeSheet(Connection connection, int resourceId, int projectId) throws SQLException {
		String sql = "INSERT INTO project_timesheet (resource_id, project_id) VALUES (?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
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

	public void saveTimes(Connection connection, DTOResource resource, HashMap<DTOProjectTimeSheet, List<DTOTime>> ma)
			throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO time ");
		sb.append(" (date, timelogged, project_timesheet_id, notes) ");
		sb.append(" VALUES ");
		sb.append("       (?, ?, ?, ?) ");
		sb.append(" ON DUPLICATE KEY UPDATE ");
		sb.append("  timelogged = VALUES(timelogged), ");
		sb.append("  notes      = VALUES(notes) ");

		for (Entry<DTOProjectTimeSheet, List<DTOTime>> entry : ma.entrySet()) {
			List<DTOTime> value = entry.getValue();
			for (DTOTime dtoTime : value) {
				try (PreparedStatement ps = connection.prepareStatement(sb.toString());) {
					ps.setDate(1, new Date(dtoTime.getDate().getMillis()));
					ps.setDouble(2, dtoTime.getLogged());
					ps.setInt(3, entry.getKey().getProject_timesheet_id());
					ps.setString(4, dtoTime.getNotes());
					ps.executeUpdate();
				}
			}
		}

	}

	public boolean addResource(String text, int level) throws SQLException {
		boolean b = false;
		String sql = "INSERT INTO resource (resource_name, adminLevel) VALUES (?, ?)";
		try (Connection connection = ConnectionManager.getConnection();) {
			try (PreparedStatement ps = connection.prepareStatement(sql);) {
				ps.setString(1, text);
				ps.setInt(2, level);
				int executeUpdate = ps.executeUpdate();
				if (executeUpdate == 1) {
					b = true;
				}
			}

			if (b) {
				// Adds initial project to user
				DTOResource resource = getResource(connection, text);
				for (DTOProject dtoProject : getAllProjects(connection)) {
					if ("admin".equalsIgnoreCase(dtoProject.getProjectName())) {
						addProjectTimeSheet(connection, resource.getResourceId(), dtoProject.getProjectId());
					} else if ("holiday".equalsIgnoreCase(dtoProject.getProjectName())) {
						addProjectTimeSheet(connection, resource.getResourceId(), dtoProject.getProjectId());
					}
				}
				connection.commit();
			}
		} catch (RDNE e) {
			e.printStackTrace();
		}
		return b;
	}
}
