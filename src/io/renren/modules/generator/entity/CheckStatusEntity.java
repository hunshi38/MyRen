package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 审查状态
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:17
 */
public class CheckStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//审查状态名称
	private String statusName;
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
	 * 设置：审查状态名称
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * 获取：审查状态名称
	 */
	public String getStatusName() {
		return statusName;
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
