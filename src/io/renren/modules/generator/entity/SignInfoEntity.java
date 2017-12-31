package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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

	private CompetitionInfoEntity competitionInfoEntity;
	private TeamInfoEntity teamInfoEntity;
    //审查状态
	private CheckStatusEntity checkStatusEntity;
	//
	//赛事信息id
		private Integer competitionId;
		//队伍id
		private Integer teamId;
		//审查状态
		private Integer checkStatus;
	private String note;
	private Timestamp signTime;
	
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
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：
	 */
	public String getNote() {
		return note;
	}
	public CompetitionInfoEntity getCompetitionInfoEntity() {
		return competitionInfoEntity;
	}
	public void setCompetitionInfoEntity(CompetitionInfoEntity competitionInfoEntity) {
		this.competitionInfoEntity = competitionInfoEntity;
	}
	public TeamInfoEntity getTeamInfoEntity() {
		return teamInfoEntity;
	}
	public void setTeamInfoEntity(TeamInfoEntity teamInfoEntity) {
		this.teamInfoEntity = teamInfoEntity;
	}
	public CheckStatusEntity getCheckStatusEntity() {
		return checkStatusEntity;
	}
	public void setCheckStatusEntity(CheckStatusEntity checkStatusEntity) {
		this.checkStatusEntity = checkStatusEntity;
	}
	public Timestamp getSignTime() {
		return signTime;
	}
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	public Integer getCompetitionId() {
		return competitionId;
	}
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
}
