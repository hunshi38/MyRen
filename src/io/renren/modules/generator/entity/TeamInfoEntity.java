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
public class TeamInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String athleteIdSet;
	//
	private String note;
	//
	private Integer signType;
	//
	private String teamCaptain;
	//
	private String teamContact;
	//
	private String teamLeader;
	//
	private String teamName;
	//
	private String filename;
	//
	private String unit;
	//
	private String company;

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
	public void setAthleteIdSet(String athleteIdSet) {
		this.athleteIdSet = athleteIdSet;
	}
	/**
	 * 获取：
	 */
	public String getAthleteIdSet() {
		return athleteIdSet;
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
	/**
	 * 设置：
	 */
	public void setSignType(Integer signType) {
		this.signType = signType;
	}
	/**
	 * 获取：
	 */
	public Integer getSignType() {
		return signType;
	}
	/**
	 * 设置：
	 */
	public void setTeamCaptain(String teamCaptain) {
		this.teamCaptain = teamCaptain;
	}
	/**
	 * 获取：
	 */
	public String getTeamCaptain() {
		return teamCaptain;
	}
	/**
	 * 设置：
	 */
	public void setTeamContact(String teamContact) {
		this.teamContact = teamContact;
	}
	/**
	 * 获取：
	 */
	public String getTeamContact() {
		return teamContact;
	}
	/**
	 * 设置：
	 */
	public void setTeamLeader(String teamLeader) {
		this.teamLeader = teamLeader;
	}
	/**
	 * 获取：
	 */
	public String getTeamLeader() {
		return teamLeader;
	}
	/**
	 * 设置：
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	/**
	 * 获取：
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * 设置：
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * 获取：
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * 设置：
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * 获取：
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * 设置：
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：
	 */
	public String getCompany() {
		return company;
	}
}
