package timesheet.panels;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.joda.time.DateTime;

import timesheet.DTO.DTOProjectTimeSheet;
import timesheet.components.JFormattedTextFieldWithNotes;

public class Row {
	private JLabel title;
	private List<JFormattedTextFieldWithNotes> txtRowDay;
	private DTOProjectTimeSheet projectTimesheet;
	private DateTime firstDateOfWeek;
	private JButton hideBtn;

	@Override
	public String toString() {
		return "Row [title=" + title + ", txtRowDay=" + txtRowDay + ", projectTimesheet=" + projectTimesheet
				+ ", firstDateOfWeek=" + firstDateOfWeek + ", hideBtn=" + hideBtn + "]";
	}

	public Row(JLabel title, List<JFormattedTextFieldWithNotes> txtRowDay, DTOProjectTimeSheet projectTimesheet,
			DateTime firstDateOfWeek, JButton hideBtn) {
		super();
		this.title = title;
		this.txtRowDay = txtRowDay;
		this.projectTimesheet = projectTimesheet;
		this.firstDateOfWeek = firstDateOfWeek;
		this.hideBtn = hideBtn;
	}

	public JLabel getTitle() {
		return title;
	}

	public void setTitle(JLabel title) {
		this.title = title;
	}

	public List<JFormattedTextFieldWithNotes> getTxtRowDay() {
		return txtRowDay;
	}

	public void setTxtRowDay(List<JFormattedTextFieldWithNotes> txtRowDay) {
		this.txtRowDay = txtRowDay;
	}

	public DTOProjectTimeSheet getProjectTimesheet() {
		return projectTimesheet;
	}

	public void setProjectTimesheet(DTOProjectTimeSheet projectTimesheet) {
		this.projectTimesheet = projectTimesheet;
	}

	public DateTime getDate() {
		return firstDateOfWeek;
	}

	public void setFirstDateOfWeek(DateTime firstDateOfWeek) {
		this.firstDateOfWeek = firstDateOfWeek;
	}

	public JButton getHideBtn() {
		return hideBtn;
	}

	public void setHideBtn(JButton hideBtn) {
		this.hideBtn = hideBtn;
	}

}