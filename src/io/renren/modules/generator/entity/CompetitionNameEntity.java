package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public class CompetitionNameEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//竞赛名称
	private String name;
	//
	private Integer type;
	//
	private String signStartDate;
	//
	private String signEndDate;
	//
	private String limitAccount;
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
	 * 设置：竞赛名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：竞赛名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setSignStartDate(String signStartDate) {
		this.signStartDate = signStartDate;
	}
	/**
	 * 获取：
	 */
	public String getSignStartDate() {
		return signStartDate;
	}
	/**
	 * 设置：
	 */
	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}
	/**
	 * 获取：
	 */
	public String getSignEndDate() {
		return signEndDate;
	}
	/**
	 * 设置：
	 */
	public void setLimitAccount(String limitAccount) {
		this.limitAccount = limitAccount;
	}
	/**
	 * 获取：
	 */
	public String getLimitAccount() {
		return limitAccount;
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
