package com.yunjia.basic.bean;

public class FormField {
	private String fieldName;
	private String label;
	private String editor;
	private String dataId;
	public FormField() {
		// TODO Auto-generated constructor stub
	}
	
	public FormField(String fieldName, String label, String editor, String dataId) {
		super();
		this.fieldName = fieldName;
		this.label = label;
		this.editor = editor;
		this.dataId = dataId;
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	@Override
	public String toString() {
		return "FormField [fieldName=" + fieldName + ", label=" + label + ", editor=" + editor + ", dataId=" + dataId
				+ "]";
	}
}
