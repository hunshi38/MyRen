package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//账户名
	private String userName;
	//
	private String password;
	//0 超级管理员  1系统管理员 2裁判 3区县管理员（竞技报名） 4其他报名人员
	private Integer userType;
	//备注
	private String note;
	//
	private String ut;
	
	private String md5;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：账户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：账户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：0 超级管理员  1系统管理员 2裁判 3区县管理员（竞技报名） 4其他报名人员
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	/**
	 * 获取：0 超级管理员  1系统管理员 2裁判 3区县管理员（竞技报名） 4其他报名人员
	 */
	public Integer getUserType() {
		return userType;
	}
	/**
	 * 设置：备注
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：备注
	 */
	public String getNote() {
		return note;
	}
	/**
	 * 设置：
	 */
	public void setUt(String ut) {
		this.ut = ut;
	}
	/**
	 * 获取：
	 */
	public String getUt() {
		return ut;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
