package timesheet.panels;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.joda.time.DateTime;

import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.components.AJFormattedTextField;

public class Row {
	private JLabel title;
	private List<AJFormattedTextField> txtRowDay;
	private DTOProjectTimeSheet projectTimesheet;
	private DateTime firstDateOfWeek;

	public Row(JLabel title, List<AJFormattedTextField> txtRowDay, DTOProjectTimeSheet projectTimesheet,
			DateTime firstDateOfWeek) {
		super();
		this.title = title;
		this.txtRowDay = txtRowDay;
		this.projectTimesheet = projectTimesheet;
		this.firstDateOfWeek = firstDateOfWeek;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstDateOfWeek == null) ? 0 : firstDateOfWeek.hashCode());
		result = prime * result + ((projectTimesheet == null) ? 0 : projectTimesheet.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((txtRowDay == null) ? 0 : txtRowDay.hashCode());
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
		Row other = (Row) obj;
		if (firstDateOfWeek == null) {
			if (other.firstDateOfWeek != null)
				return false;
		} else if (!firstDateOfWeek.equals(other.firstDateOfWeek))
			return false;
		if (projectTimesheet == null) {
			if (other.projectTimesheet != null)
				return false;
		} else if (!projectTimesheet.equals(other.projectTimesheet))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (txtRowDay == null) {
			if (other.txtRowDay != null)
				return false;
		} else if (!txtRowDay.equals(other.txtRowDay))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Row [title=" + title + ", txtRowDay=" + txtRowDay + ", projectTimesheet=" + projectTimesheet
				+ ", dates=" + firstDateOfWeek + "]";
	}

	public JLabel getTitle() {
		return title;
	}

	public void setTitle(JLabel title) {
		this.title = title;
	}

	public List<AJFormattedTextField> getTxtRowDay() {
		return txtRowDay;
	}

	public void setTxtRowDay(List<AJFormattedTextField> txtRowDay) {
		this.txtRowDay = txtRowDay;
	}

	public DTOProjectTimeSheet getProjectTimesheet() {
		return projectTimesheet;
	}

	public void setProjectTimesheet(DTOProjectTimeSheet projectTimesheet) {
		this.projectTimesheet = projectTimesheet;
	}

	public DateTime getDates() {
		return firstDateOfWeek;
	}

	public void setDates(DateTime dates) {
		this.firstDateOfWeek = dates;
	}

}