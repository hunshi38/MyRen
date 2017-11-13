package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 远动员报名信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public class SignInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//赛事信息id
	private Integer competitionId;
	//队伍id
	private Integer teamId;
	//审查状态
	private Integer checkStatus;
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
	 * 设置：赛事信息id
	 */
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
	/**
	 * 获取：赛事信息id
	 */
	public Integer getCompetitionId() {
		return competitionId;
	}
	/**
	 * 设置：队伍id
	 */
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	/**
	 * 获取：队伍id
	 */
	public Integer getTeamId() {
		return teamId;
	}
	/**
	 * 设置：审查状态
	 */
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：审查状态
	 */
	public Integer getCheckStatus() {
		return checkStatus;
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
