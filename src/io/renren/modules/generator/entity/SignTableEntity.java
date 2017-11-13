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
public class SignTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer checkStatus;
	//
	private Integer competitionId;
	//
	private String fileName;
	//
	private String filePath;
	//
	private Integer frontUserId;
	//
	private String note;
	//
	private Integer type;
	//
	private String comname;
	//
	private Integer time;

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
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：
	 */
	public Integer getCheckStatus() {
		return checkStatus;
	}
	/**
	 * 设置：
	 */
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
	/**
	 * 获取：
	 */
	public Integer getCompetitionId() {
		return competitionId;
	}
	/**
	 * 设置：
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取：
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置：
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * 获取：
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * 设置：
	 */
	public void setFrontUserId(Integer frontUserId) {
		this.frontUserId = frontUserId;
	}
	/**
	 * 获取：
	 */
	public Integer getFrontUserId() {
		return frontUserId;
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
	public void setComname(String comname) {
		this.comname = comname;
	}
	/**
	 * 获取：
	 */
	public String getComname() {
		return comname;
	}
	/**
	 * 设置：
	 */
	public void setTime(Integer time) {
		this.time = time;
	}
	/**
	 * 获取：
	 */
	public Integer getTime() {
		return time;
	}
}
