package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 运动员
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public class AthleteEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String idCard;
	//注册运动员编号
	private String registerCode;
	//
	private String name;
	//
	private Integer gender;
	//
	private Date birthday;
	//所属单位
	private String company;
	//
	private String phoneNumber;
	//球衣号码
	private Integer jerseyNumber;
	//带训教练
	private String coach;
	//
	private String note;

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
	 * 设置：
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * 获取：
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * 设置：注册运动员编号
	 */
	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}
	/**
	 * 获取：注册运动员编号
	 */
	public String getRegisterCode() {
		return registerCode;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
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
	 * 设置：所属单位
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：所属单位
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置：
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 获取：
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * 设置：球衣号码
	 */
	public void setJerseyNumber(Integer jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}
	/**
	 * 获取：球衣号码
	 */
	public Integer getJerseyNumber() {
		return jerseyNumber;
	}
	/**
	 * 设置：带训教练
	 */
	public void setCoach(String coach) {
		this.coach = coach;
	}
	/**
	 * 获取：带训教练
	 */
	public String getCoach() {
		return coach;
	}
	/**
	 * 设置：
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：
	 */
	public String getNote() {
		return note;
	}
}
