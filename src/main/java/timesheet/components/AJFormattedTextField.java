package timesheet.components;

import java.text.Format;

import javax.swing.JFormattedTextField;

public class AJFormattedTextField extends JFormattedTextField {
	public AJFormattedTextField() {
		super();
	}

	public AJFormattedTextField(AbstractFormatter formatter) {
		super(formatter);
	}

	public AJFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
		super(factory, currentValue);
	}

	public AJFormattedTextField(AbstractFormatterFactory factory) {
		super(factory);
	}

	public AJFormattedTextField(Format format) {
		super(format);
	}

	public AJFormattedTextField(Object value) {
		super(value);
	}

	String notes;

	@Override
	public String toString() {
		return "ATextField [notes=" + notes + "]";
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
