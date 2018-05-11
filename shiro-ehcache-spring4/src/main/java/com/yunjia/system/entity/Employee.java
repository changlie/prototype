package com.yunjia.system.entity;

import java.util.Date;
/***
 *  @author changlie
 */
public class Employee{

	private Long uid;
	private Boolean sex;
	private Integer age;
	private String addr;
	private String idCard;
	private String idCardAddr;
	private Date entryDate;
	private Date reportDate;
	private String depIds;
	private Integer depId1;
	private Integer depId2;
	private Integer depId3;
	private Integer depId4;
	private Integer depId5;

	public static Employee instance() {
		return new Employee();
	}

	public Employee setUid(Long uid){
		this.uid = uid;
		return this;
	}
	public Long getUid(){
		return this.uid;
	}
	
	public Employee setSex(Boolean sex){
		this.sex = sex;
		return this;
	}
	public Boolean getSex(){
		return this.sex;
	}
	
	public Employee setAge(Integer age){
		this.age = age;
		return this;
	}
	public Integer getAge(){
		return this.age;
	}
	
	public Employee setAddr(String addr){
		this.addr = addr;
		return this;
	}
	public String getAddr(){
		return this.addr;
	}
	
	public Employee setIdCard(String idCard){
		this.idCard = idCard;
		return this;
	}
	public String getIdCard(){
		return this.idCard;
	}
	
	public Employee setIdCardAddr(String idCardAddr){
		this.idCardAddr = idCardAddr;
		return this;
	}
	public String getIdCardAddr(){
		return this.idCardAddr;
	}
	
	public Employee setEntryDate(Date entryDate){
		this.entryDate = entryDate;
		return this;
	}
	public Date getEntryDate(){
		return this.entryDate;
	}
	
	public Employee setReportDate(Date reportDate){
		this.reportDate = reportDate;
		return this;
	}
	public Date getReportDate(){
		return this.reportDate;
	}
	
	
	
	public String getDepIds() {
		return depIds;
	}
	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}

	public Employee setDepId1(Integer depId1){
		this.depId1 = depId1;
		return this;
	}
	public Integer getDepId1(){
		return this.depId1;
	}
	
	public Employee setDepId2(Integer depId2){
		this.depId2 = depId2;
		return this;
	}
	public Integer getDepId2(){
		return this.depId2;
	}
	
	public Employee setDepId3(Integer depId3){
		this.depId3 = depId3;
		return this;
	}
	public Integer getDepId3(){
		return this.depId3;
	}
	
	public Employee setDepId4(Integer depId4){
		this.depId4 = depId4;
		return this;
	}
	public Integer getDepId4(){
		return this.depId4;
	}
	
	public Employee setDepId5(Integer depId5){
		this.depId5 = depId5;
		return this;
	}
	public Integer getDepId5(){
		return this.depId5;
	}
	

}