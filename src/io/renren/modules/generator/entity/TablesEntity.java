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
public class TablesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer competitionid;
	//
	private String filename;

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
	public void setCompetitionid(Integer competitionid) {
		this.competitionid = competitionid;
	}
	/**
	 * 获取：
	 */
	public Integer getCompetitionid() {
		return competitionid;
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
}
