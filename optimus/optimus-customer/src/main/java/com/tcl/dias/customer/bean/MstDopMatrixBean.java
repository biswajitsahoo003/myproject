package com.tcl.dias.customer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean Class
 *
 * @author Harini Sri Reka J
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MstDopMatrixBean implements Serializable {

	private Integer id;
	private String region;
	private String levelOneName;
	private String leveOneMail;
	private String levelTwoName;
	private String levelTwoMail;
	private String levelThreeName;
	private String levelThreeMail;
	private Integer customerSegment;
	private String dopSignLevel;
	private String createdBy;
	private String createTime;

	public MstDopMatrixBean(Integer id, String region, String levelOneName, String leveOneMail, String levelTwoName, String levelTwoMail, String levelThreeName, String levelThreeMail, Integer customerSegment, String dopSignLevel, String createdBy, String createTime) {
		this.id = id;
		this.region = region;
		this.levelOneName = levelOneName;
		this.leveOneMail = leveOneMail;
		this.levelTwoName = levelTwoName;
		this.levelTwoMail = levelTwoMail;
		this.levelThreeName = levelThreeName;
		this.levelThreeMail = levelThreeMail;
		this.customerSegment = customerSegment;
		this.dopSignLevel = dopSignLevel;
		this.createdBy = createdBy;
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLevelOneName() {
		return levelOneName;
	}

	public void setLevelOneName(String levelOneName) {
		this.levelOneName = levelOneName;
	}

	public String getLeveOneMail() {
		return leveOneMail;
	}

	public void setLeveOneMail(String leveOneMail) {
		this.leveOneMail = leveOneMail;
	}

	public String getLevelTwoName() {
		return levelTwoName;
	}

	public void setLevelTwoName(String levelTwoName) {
		this.levelTwoName = levelTwoName;
	}

	public String getLevelTwoMail() {
		return levelTwoMail;
	}

	public void setLevelTwoMail(String levelTwoMail) {
		this.levelTwoMail = levelTwoMail;
	}

	public String getLevelThreeName() {
		return levelThreeName;
	}

	public void setLevelThreeName(String levelThreeName) {
		this.levelThreeName = levelThreeName;
	}

	public String getLevelThreeMail() {
		return levelThreeMail;
	}

	public void setLevelThreeMail(String levelThreeMail) {
		this.levelThreeMail = levelThreeMail;
	}

	public Integer getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(Integer customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getDopSignLevel() {
		return dopSignLevel;
	}

	public void setDopSignLevel(String dopSignLevel) {
		this.dopSignLevel = dopSignLevel;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "MstDopMatrixBean{" +
				"id=" + id +
				", region='" + region + '\'' +
				", levelOneName='" + levelOneName + '\'' +
				", leveOneMail='" + leveOneMail + '\'' +
				", levelTwoName='" + levelTwoName + '\'' +
				", levelTwoMail='" + levelTwoMail + '\'' +
				", levelThreeName='" + levelThreeName + '\'' +
				", levelThreeMail='" + levelThreeMail + '\'' +
				", customerSegment=" + customerSegment +
				", dopSignLevel='" + dopSignLevel + '\'' +
				", createdBy='" + createdBy + '\'' +
				", createTime='" + createTime + '\'' +
				'}';
	}
}
