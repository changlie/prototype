package com.yunjia.basic.bean;

public class Condition {
	private String value;
	private String label;
	private String editor;
	private String dataId;
	
	public Condition() {
	}
	
	public Condition(String value, String label, String editor, String dataId) {
		super();
		this.value = value;
		this.label = label;
		this.editor = editor;
		this.dataId = dataId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	
}
