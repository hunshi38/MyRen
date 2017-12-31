package io.renren.modules.generator.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-11-01 11:46:16
 */
public class GroupNameEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String groupName;
	//
	private Integer minMen;
	//
	private Integer maxMen;
	//
	private Integer minWomen;
	//
	private Integer maxWomen;
	//
	private Integer minSum;
	//
	private Integer maxSum;
	//
	private Date birthdayEarly;
	//
	private Date birthdayAfter;
	//
	private String note;
	private List<CompetitionInfoEntity>competitionInfoList = null;
	
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
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 获取：
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * 设置：
	 */
	public void setMinMen(Integer minMen) {
		this.minMen = minMen;
	}
	/**
	 * 获取：
	 */
	public Integer getMinMen() {
		return minMen;
	}
	/**
	 * 设置：
	 */
	public void setMaxMen(Integer maxMen) {
		this.maxMen = maxMen;
	}
	/**
	 * 获取：
	 */
	public Integer getMaxMen() {
		return maxMen;
	}
	/**
	 * 设置：
	 */
	public void setMinWomen(Integer minWomen) {
		this.minWomen = minWomen;
	}
	/**
	 * 获取：
	 */
	public Integer getMinWomen() {
		return minWomen;
	}
	/**
	 * 设置：
	 */
	public void setMaxWomen(Integer maxWomen) {
		this.maxWomen = maxWomen;
	}
	/**
	 * 获取：
	 */
	public Integer getMaxWomen() {
		return maxWomen;
	}
	/**
	 * 设置：
	 */
	public void setMinSum(Integer minSum) {
		this.minSum = minSum;
	}
	/**
	 * 获取：
	 */
	public Integer getMinSum() {
		return minSum;
	}
	/**
	 * 设置：
	 */
	public void setMaxSum(Integer maxSum) {
		this.maxSum = maxSum;
	}
	/**
	 * 获取：
	 */
	public Integer getMaxSum() {
		return maxSum;
	}
	/**
	 * 设置：
	 */
	public void setBirthdayEarly(Date birthdayEarly) {
		this.birthdayEarly = birthdayEarly;
	}
	/**
	 * 获取：
	 */
	public Date getBirthdayEarly() {
		return birthdayEarly;
	}
	/**
	 * 设置：
	 */
	public void setBirthdayAfter(Date birthdayAfter) {
		this.birthdayAfter = birthdayAfter;
	}
	/**
	 * 获取：
	 */
	public Date getBirthdayAfter() {
		return birthdayAfter;
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
	public List<CompetitionInfoEntity> getCompetitionInfoList() {
		return competitionInfoList;
	}
	public void setCompetitionInfoList(
			List<CompetitionInfoEntity> competitionInfoList) {
		this.competitionInfoList = competitionInfoList;
	}
}
