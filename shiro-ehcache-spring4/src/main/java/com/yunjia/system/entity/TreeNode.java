package com.yunjia.system.entity;

import java.util.List;

public class TreeNode {
	private long id;
	private String title;
	private List<TreeNode> subs;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<TreeNode> getSubs() {
		return subs;
	}

	public void setSubs(List<TreeNode> subs) {
		this.subs = subs;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", title=" + title + ", subs=" + subs + "]";
	}

}
