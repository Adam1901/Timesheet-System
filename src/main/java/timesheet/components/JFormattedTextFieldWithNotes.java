package timesheet.components;

import java.text.Format;

import javax.swing.JFormattedTextField;

public class JFormattedTextFieldWithNotes extends JFormattedTextField {
	private static final long serialVersionUID = 1L;
	String notes;

	public JFormattedTextFieldWithNotes() {
		super();
	}

	public JFormattedTextFieldWithNotes(AbstractFormatter formatter) {
		super(formatter);
	}

	public JFormattedTextFieldWithNotes(AbstractFormatterFactory factory, Object currentValue) {
		super(factory, currentValue);
	}

	public JFormattedTextFieldWithNotes(AbstractFormatterFactory factory) {
		super(factory);
	}

	public JFormattedTextFieldWithNotes(Format format) {
		super(format);
	}

	public JFormattedTextFieldWithNotes(Object value) {
		super(value);
	}

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
