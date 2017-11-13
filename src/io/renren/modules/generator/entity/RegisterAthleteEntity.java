package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 注册运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public class RegisterAthleteEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//身份证号码
	private String idCard;
	//注册远动员编号
	private String registerCode;
	//姓名
	private String name;
	//
	private Integer gender;
	//
	private Date birthday;
	//所属单位ID
	private String company;

	/**
	 * 设置：身份证号码
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * 获取：身份证号码
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * 设置：注册远动员编号
	 */
	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}
	/**
	 * 获取：注册远动员编号
	 */
	public String getRegisterCode() {
		return registerCode;
	}
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	/**
	 * 获取：
	 */
	public Integer getGender() {
		return gender;
	}
	/**
	 * 设置：
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取：
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * 设置：所属单位ID
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：所属单位ID
	 */
	public String getCompany() {
		return company;
	}
}
