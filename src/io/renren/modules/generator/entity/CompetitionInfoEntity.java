package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 赛事信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public class CompetitionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;

	private CompetitionNameEntity competitionNameEntity;
	private EventNameEntity eventNameEntity;
	private GroupNameEntity groupNameEntity;
	private SignTypeEntity signTypeEntity;
	//赛事id
	private Integer competitionId;
	//项目id
	private Integer eventId;
	//组别id
	private Integer groupId;
	//报名方式
	private Integer signType;
	//
	private String note;
	//
	private String competitionname;
	//
	private String eventname;
	//
	private String groupname;
	//
	private String type;

	@Override
	public String toString(){
		return id+"-"+competitionNameEntity+groupId+eventId+signType;
	}
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
	 * 设置：项目id
	 */
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	/**
	 * 获取：项目id
	 */
	public Integer getEventId() {
		return eventId;
	}
	/**
	 * 设置：组别id
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	/**
	 * 获取：组别id
	 */
	public Integer getGroupId() {
		return groupId;
	}
	/**
	 * 设置：报名方式
	 */
	public void setSignType(Integer signType) {
		this.signType = signType;
	}
	/**
	 * 获取：报名方式
	 */
	public Integer getSignType() {
		return signType;
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
	public void setCompetitionname(String competitionname) {
		this.competitionname = competitionname;
	}
	/**
	 * 获取：
	 */
	public String getCompetitionname() {
		return competitionname;
	}
	/**
	 * 设置：
	 */
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	/**
	 * 获取：
	 */
	public String getEventname() {
		return eventname;
	}
	/**
	 * 设置：
	 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	/**
	 * 获取：
	 */
	public String getGroupname() {
		return groupname;
	}
	/**
	 * 设置：
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public String getType() {
		return type;
	}
	public CompetitionNameEntity getCompetitionNameEntity() {
		return competitionNameEntity;
	}
	public void setCompetitionNameEntity(CompetitionNameEntity competitionNameEntity) {
		this.competitionNameEntity = competitionNameEntity;
	}
	public EventNameEntity getEventNameEntity() {
		return eventNameEntity;
	}
	public void setEventNameEntity(EventNameEntity eventNameEntity) {
		this.eventNameEntity = eventNameEntity;
	}
	public GroupNameEntity getGroupNameEntity() {
		return groupNameEntity;
	}
	public void setGroupNameEntity(GroupNameEntity groupNameEntity) {
		this.groupNameEntity = groupNameEntity;
	}
	public SignTypeEntity getSignTypeEntity() {
		return signTypeEntity;
	}
	public void setSignTypeEntity(SignTypeEntity signTypeEntity) {
		this.signTypeEntity = signTypeEntity;
	}
	public Integer getCompetitionId() {
		return competitionId;
	}
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
}
