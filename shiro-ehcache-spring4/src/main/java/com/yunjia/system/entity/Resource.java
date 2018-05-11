package com.yunjia.system.entity;

/***
 *  @author changlie
 */
public class Resource{

	private Long id;
	private String name;
	private String num;
	private String url;
	private String permission;
	private Long parentId;
	private String parentIds;
	private String icon;
	private Integer weight;
	private Boolean available;
	private String type;

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
	
	public void setNum(String num){
		this.num = num;
	}
	public String getNum(){
		return this.num;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}
	
	public void setPermission(String permission){
		this.permission = permission;
	}
	public String getPermission(){
		return this.permission;
	}
	
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	public Long getParentId(){
		return this.parentId;
	}
	
	public void setParentIds(String parentIds){
		this.parentIds = parentIds;
	}
	public String getParentIds(){
		return this.parentIds;
	}
	
	public void setIcon(String icon){
		this.icon = icon;
	}
	public String getIcon(){
		return this.icon;
	}
	
	public void setWeight(Integer weight){
		this.weight = weight;
	}
	public Integer getWeight(){
		return this.weight;
	}
	
	public void setAvailable(Boolean available){
		this.available = available;
	}
	public Boolean getAvailable(){
		return this.available;
	}
	
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
	

}