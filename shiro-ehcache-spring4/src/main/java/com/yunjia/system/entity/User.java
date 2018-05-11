package com.yunjia.system.entity;

import java.util.Date;
/***
 *  @author changlie
 */
public class User{

	private Integer id;
	private String username;
	private String nickname;
	private String email;
	private String tel;
	private String password;
	private String salt;
	private Date createtime;
	private Short type;
	private Boolean available;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return this.id;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	public String getUsername(){
		return this.username;
	}
	
	public void setNickname(String nickname){
		this.nickname = nickname;
	}
	public String getNickname(){
		return this.nickname;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return this.email;
	}
	
	public void setTel(String tel){
		this.tel = tel;
	}
	public String getTel(){
		return this.tel;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}
	
	public void setSalt(String salt){
		this.salt = salt;
	}
	public String getSalt(){
		return this.salt;
	}
	
	public void setCreatetime(Date createtime){
		this.createtime = createtime;
	}
	public Date getCreatetime(){
		return this.createtime;
	}
	
	public void setType(Short type){
		this.type = type;
	}
	public Short getType(){
		return this.type;
	}
	
	public void setAvailable(Boolean available){
		this.available = available;
	}
	public Boolean getAvailable(){
		return this.available;
	}
	

}