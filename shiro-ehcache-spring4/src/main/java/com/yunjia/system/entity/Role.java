package com.yunjia.system.entity;

/***
 *  @author changlie
 */
public class Role{

	private Long id;
	private String name;
	private String num;
	private String description;
	private Boolean available;

	public void setId(Long id){
		this.id = id;
	}
	public Long getId(){
		return this.id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	
	public void setAvailable(Boolean available){
		this.available = available;
	}
	public Boolean getAvailable(){
		return this.available;
	}
	

}