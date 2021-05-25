package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * TeamsDR Traning details managed services bean
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 15:40
 */
public class TeamsDRTrainingDetailsBean {
	private Integer id;
	private String trainingType;
	private String trainingCustomerAdminName;
	private String customerAdminID;
	private String needAdvancedTrainingLive;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String startDate;

	private String trainingStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String trainingCompletionDate;

	private List<SolutionAttachmentBean> trainingAttIds;
	private String customerFeedback;

	public TeamsDRTrainingDetailsBean() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public String getTrainingCustomerAdminName() {
		return trainingCustomerAdminName;
	}

	public void setTrainingCustomerAdminName(String trainingCustomerAdminName) {
		this.trainingCustomerAdminName = trainingCustomerAdminName;
	}

	public String getCustomerAdminID() {
		return customerAdminID;
	}

	public void setCustomerAdminID(String customerAdminID) {
		this.customerAdminID = customerAdminID;
	}

	public String getNeedAdvancedTrainingLive() {
		return needAdvancedTrainingLive;
	}

	public void setNeedAdvancedTrainingLive(String needAdvancedTrainingLive) {
		this.needAdvancedTrainingLive = needAdvancedTrainingLive;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getTrainingStatus() {
		return trainingStatus;
	}

	public void setTrainingStatus(String trainingStatus) {
		this.trainingStatus = trainingStatus;
	}

	public String getTrainingCompletionDate() {
		return trainingCompletionDate;
	}

	public void setTrainingCompletionDate(String trainingCompletionDate) {
		this.trainingCompletionDate = trainingCompletionDate;
	}

	public List<SolutionAttachmentBean> getTrainingAttIds() {
		return trainingAttIds;
	}

	public void setTrainingAttIds(List<SolutionAttachmentBean> trainingAttIds) {
		this.trainingAttIds = trainingAttIds;
	}

	public String getCustomerFeedback() {
		return customerFeedback;
	}

	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}

	@Override
	public String toString() {
		return "TeamsDRTrainingDetailsBean{" + "trainingType='" + trainingType + '\'' + ", trainingCustomerAdminName" + "='" + trainingCustomerAdminName + '\'' + ", customerAdminID='" + customerAdminID + '\'' + ", " + "needAdvancedTrainingLive='" + needAdvancedTrainingLive + '\'' + ", startDate='" + startDate + '\'' + '}';
	}
}
