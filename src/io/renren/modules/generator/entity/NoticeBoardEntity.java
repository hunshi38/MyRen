package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 公告管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:45:56
 */
public class NoticeBoardEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String title;
	//
	private String content;
	//内容地址
	private String contenUrl;
	//
	private Date createTime;
	//
	private String picUrl;
	//
	private Integer readNumber;
	//
	private Integer cId;

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
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：内容地址
	 */
	public void setContenUrl(String contenUrl) {
		this.contenUrl = contenUrl;
	}
	/**
	 * 获取：内容地址
	 */
	public String getContenUrl() {
		return contenUrl;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	/**
	 * 获取：
	 */
	public String getPicUrl() {
		return picUrl;
	}
	/**
	 * 设置：
	 */
	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}
	/**
	 * 获取：
	 */
	public Integer getReadNumber() {
		return readNumber;
	}
	/**
	 * 设置：
	 */
	public void setCId(Integer cId) {
		this.cId = cId;
	}
	/**
	 * 获取：
	 */
	public Integer getCId() {
		return cId;
	}
}
