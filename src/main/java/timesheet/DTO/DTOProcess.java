package timesheet.DTO;

import java.io.Serializable;

public class DTOProcess implements Serializable {

	private static final long serialVersionUID = 1L;

	String imageName;
	int PID;
	String sessionName, sessionNum, memUsage, status, username, cpuTime, windowTitle;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionNum() {
		return sessionNum;
	}

	public void setSessionNum(String sessionNum) {
		this.sessionNum = sessionNum;
	}

	public String getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(String memUsage) {
		this.memUsage = memUsage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(String cpuTime) {
		this.cpuTime = cpuTime;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + PID;
		result = prime * result + ((cpuTime == null) ? 0 : cpuTime.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((memUsage == null) ? 0 : memUsage.hashCode());
		result = prime * result + ((sessionName == null) ? 0 : sessionName.hashCode());
		result = prime * result + ((sessionNum == null) ? 0 : sessionNum.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((windowTitle == null) ? 0 : windowTitle.hashCode());
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
		DTOProcess other = (DTOProcess) obj;
		if (PID != other.PID)
			return false;
		if (cpuTime == null) {
			if (other.cpuTime != null)
				return false;
		} else if (!cpuTime.equals(other.cpuTime))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (memUsage == null) {
			if (other.memUsage != null)
				return false;
		} else if (!memUsage.equals(other.memUsage))
			return false;
		if (sessionName == null) {
			if (other.sessionName != null)
				return false;
		} else if (!sessionName.equals(other.sessionName))
			return false;
		if (sessionNum == null) {
			if (other.sessionNum != null)
				return false;
		} else if (!sessionNum.equals(other.sessionNum))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (windowTitle == null) {
			if (other.windowTitle != null)
				return false;
		} else if (!windowTitle.equals(other.windowTitle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DTOProcess [imageName=" + imageName + ", PID=" + PID + ",  sessionName=" + sessionName + ", sessionNum="
				+ sessionNum + ", memUsage=" + memUsage + ", status=" + status + ", username=" + username + ", cpuTime="
				+ cpuTime + ", windowTitle=" + windowTitle + "]";
	}

}
