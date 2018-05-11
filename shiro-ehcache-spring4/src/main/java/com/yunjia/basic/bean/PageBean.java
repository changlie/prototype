package com.yunjia.basic.bean;

import java.util.List;

public class PageBean<T> {

	private List<T> list;
	private long count;
	
	public PageBean() {
	}
	
	public PageBean(List<T> list, long count) {
		super();
		this.list = list;
		this.count = count;
	}
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "PageBean [list=" + list + ", count=" + count + "]";
	}
	
}
