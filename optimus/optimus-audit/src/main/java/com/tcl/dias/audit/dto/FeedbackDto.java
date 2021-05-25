/**
 * 
 */
package com.tcl.dias.audit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.audit.entity.entities.Feedback;
/**
 * @author kthayuma
 *
 */
@JsonInclude(Include.NON_NULL)
public class FeedbackDto {
	private Integer id;
	private String pageURL;
	private String comments;
	private String createdBy;
	private String createdTime;
	private String status;
	
	public FeedbackDto() {
		
	}
	
	public FeedbackDto (Feedback fb) {
		FeedbackDto dto = new FeedbackDto();
        dto.setId(fb.getId());
        dto.setPageURL(fb.getPageURL());
        dto.setCreatedBy(fb.getCreatedBy());
        dto.setComments(fb.getComments());
        dto.setCreatedTime(fb.getCreatedTime().toString());
        dto.setStatus(fb.getStatus());
}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the pageURL
	 */
	public String getPageURL() {
		return pageURL;
	}

	/**
	 * @param pageURL the pageURL to set
	 */
	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
