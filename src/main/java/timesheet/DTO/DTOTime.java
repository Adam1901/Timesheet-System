package timesheet.DTO;

import java.io.Serializable;

import org.joda.time.DateTime;

public class DTOTime implements Serializable {
	private static final long serialVersionUID = 1L;
	DateTime date;
	double logged;
	int projectTimsheetId;
	String notes;

	public DTOTime() {
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public double getLogged() {
		return logged;
	}

	public void setLogged(double logged) {
		this.logged = logged;
	}

	public int getProjectTimsheetId() {
		return projectTimsheetId;
	}

	public void setProjectTimsheetId(int projectTimsheetId) {
		this.projectTimsheetId = projectTimsheetId;
	}

	@Override
	public String toString() {
		return "DTOTime [date=" + date + ", logged=" + logged + ", projectTimsheetId=" + projectTimsheetId + ", notes="
				+ notes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(logged);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + projectTimsheetId;
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
		DTOTime other = (DTOTime) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(logged) != Double.doubleToLongBits(other.logged))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (projectTimsheetId != other.projectTimsheetId)
			return false;
		return true;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public DTOTime(DateTime date, double logged, int projectTimsheetId, String notes) {
		super();
		this.date = date;
		this.logged = logged;
		this.projectTimsheetId = projectTimsheetId;
		this.notes = notes;
	}

}
