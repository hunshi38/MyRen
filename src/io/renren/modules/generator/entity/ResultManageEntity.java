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
public class ResultManageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String additionalWord;
	//
	private Integer competitionId;
	//
	private String resultFilePath;
	//
	private String resultName;

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
	public void setAdditionalWord(String additionalWord) {
		this.additionalWord = additionalWord;
	}
	/**
	 * 获取：
	 */
	public String getAdditionalWord() {
		return additionalWord;
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
	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
	/**
	 * 获取：
	 */
	public String getResultFilePath() {
		return resultFilePath;
	}
	/**
	 * 设置：
	 */
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	/**
	 * 获取：
	 */
	public String getResultName() {
		return resultName;
	}
}
