package com.tcl.dias.customer.dto;

import com.tcl.dias.customer.entity.entities.Attachment;

/**
 * This file contains the AttacmentDto.java class.
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AttachmentDto {

	private String name;

	private String displayName;

	private String uriPath;

	private Integer id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AttachmentDto(Attachment attachment) {
		this.setId(attachment.getId());
		this.setName(attachment.getName());
		this.setDisplayName(attachment.getDisplayName());
		this.setUriPath(attachment.getUriPathOrUrl());
	}

}
