package timesheet.DTO;

import java.io.Serializable;

public class DTOProjectTimeSheet implements Serializable {
	private static final long serialVersionUID = 1L;
	int project_timesheet_id, projectId, resourceId;

	public DTOProjectTimeSheet() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + projectId;
		result = prime * result + project_timesheet_id;
		result = prime * result + resourceId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOProjectTimeSheet other = (DTOProjectTimeSheet) obj;
		if (projectId != other.projectId)
			return false;
		if (project_timesheet_id != other.project_timesheet_id)
			return false;
		if (resourceId != other.resourceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DTOProjectTimeSheet [project_timesheet_id=" + project_timesheet_id + ", projectId=" + projectId
				+ ", resourceId=" + resourceId + "]";
	}

	public DTOProjectTimeSheet(int project_timesheet_id, int projectId, int resourceId) {
		super();
		this.project_timesheet_id = project_timesheet_id;
		this.projectId = projectId;
		this.resourceId = resourceId;
	}

	public int getProject_timesheet_id() {
		return project_timesheet_id;
	}

	public void setProject_timesheet_id(int project_timesheet_id) {
		this.project_timesheet_id = project_timesheet_id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

}
