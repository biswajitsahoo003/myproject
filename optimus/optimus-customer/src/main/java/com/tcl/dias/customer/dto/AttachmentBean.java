package com.tcl.dias.customer.dto;

/**
 * This bean provides file attachment details like name, display name and
 * uripath.
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AttachmentBean {

	private String attributeName;

	private String attributeValue;

	private String attachmentName;

	private String attachmentDisplayName;

	private String uriPath;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentDisplayName() {
		return attachmentDisplayName;
	}

	public void setAttachmentDisplayName(String attachmentDisplayName) {
		this.attachmentDisplayName = attachmentDisplayName;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}
}
