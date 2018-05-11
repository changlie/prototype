package com.yunjia.system.entity;

import com.yunjia.common.dao.PrimaryKey;
import com.yunjia.common.dao.TableName;

/***
 *  @author changlie
 */
@TableName("sys_organization")
public class Organization{

	@PrimaryKey
	private Integer id;
	private String num;
	private String name;
	private Long parentId;
	private String parentIds;
	private String icon;
	private Integer weight;
	private Boolean available;

	public static Organization instance() {
		return new Organization();
	}

	public Organization setId(Integer id){
		this.id = id;
		return this;
	}
	public Integer getId(){
		return this.id;
	}
	
	public Organization setNum(String num){
		this.num = num;
		return this;
	}
	public String getNum(){
		return this.num;
	}
	
	public Organization setName(String name){
		this.name = name;
		return this;
	}
	public String getName(){
		return this.name;
	}
	
	public Organization setParentId(Long parentId){
		this.parentId = parentId;
		return this;
	}
	public Long getParentId(){
		return this.parentId;
	}
	
	public Organization setParentIds(String parentIds){
		this.parentIds = parentIds;
		return this;
	}
	public String getParentIds(){
		return this.parentIds;
	}
	
	public Organization setIcon(String icon){
		this.icon = icon;
		return this;
	}
	public String getIcon(){
		return this.icon;
	}
	
	public Organization setWeight(Integer weight){
		this.weight = weight;
		return this;
	}
	public Integer getWeight(){
		return this.weight;
	}
	
	public Organization setAvailable(Boolean available){
		this.available = available;
		return this;
	}
	public Boolean getAvailable(){
		return this.available;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", num=" + num + ", name=" + name + ", parentId=" + parentId + ", parentIds="
				+ parentIds + ", icon=" + icon + ", weight=" + weight + ", available=" + available + "]";
	}
	
}