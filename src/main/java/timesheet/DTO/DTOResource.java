package timesheet.DTO;

import java.io.Serializable;

public class DTOResource implements Serializable {
	private static final long serialVersionUID = 1L;
	int resourceId;
	int adminLevel;
	String resourceName;

	// 0 disabled
	// 1 Normal
	// 2 admin
	// 3 adam?

	public int getAdminLevel() {
		return adminLevel;
	}

	public void setAdminLevel(int adminLevel) {
		this.adminLevel = adminLevel;
	}

	public DTOResource(int resourceId, String resourceName, int adminLevel) {
		super();
		this.resourceId = resourceId;
		this.adminLevel = adminLevel;
		this.resourceName = resourceName;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adminLevel;
		result = prime * result + resourceId;
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
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
		DTOResource other = (DTOResource) obj;
		if (adminLevel != other.adminLevel)
			return false;
		if (resourceId != other.resourceId)
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		return true;
	}

	// HACK ALERT
	@Override
	public String toString() {
		return resourceName;
	}
}
