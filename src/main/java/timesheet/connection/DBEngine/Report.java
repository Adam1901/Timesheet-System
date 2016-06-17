package timesheet.connection.DBEngine;

import org.joda.time.DateTime;

import timesheet.DTO.DTOProject;
import timesheet.DTO.DTOResource;

public class Report {
	private DTOResource resource;
	private DTOProject project;
	private DateTime start;
	private DateTime end;
	boolean useAllProjects, useAllUsers;

	public boolean isUseAllUsers() {
		return useAllUsers;
	}

	public void setUseAllUsers(boolean useAllUsers) {
		this.useAllUsers = useAllUsers;
	}

	public DTOResource getResource() {
		return resource;
	}

	public void setResource(DTOResource resource) {
		this.resource = resource;
	}

	public DTOProject getProject() {
		return project;
	}

	public void setProject(DTOProject project) {
		this.project = project;
	}

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public boolean isUseAllProjects() {
		return useAllProjects;
	}

	public void setUseAllProjects(boolean useAllProjects) {
		this.useAllProjects = useAllProjects;
	}

}
