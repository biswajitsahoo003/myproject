package com.tcl.dias.common.beans;

/**
 * This class contains the Attachment bean information
 *
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OmsAttachBean {

	private Integer qouteLeId;
	private Integer orderLeId;
	private String attachmentType;
	private Integer attachmentId;
	private String referenceName;
	private Integer referenceId;
	private String fileName;
	private String path;

	public OmsAttachBean() {
	}

	public OmsAttachBean(Integer attachmentId, String referenceName,String attachmentType) {
		this.attachmentId = attachmentId;
		this.referenceName = referenceName;
		this.attachmentType = attachmentType;
	}

	/**
	 * This returns referenceId
	 *
	 * @return referenceId
	 */
	public Integer getReferenceId() {
		return referenceId;
	}

	/**
	 * setter for referenceIdss
	 *
	 * @param referenceId
	 */
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * This returns
	 *
	 * @return attachmentId
	 */
	public Integer getAttachmentId() {
		return attachmentId;
	}

	/**
	 * Setter for attachmentId
	 *
	 * @param attachmentId
	 */
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * This Sets the qouteLeId
	 *
	 * @param qouteLeId
	 */
	public void setQouteLeId(Integer qouteLeId) {
		this.qouteLeId = qouteLeId;
	}

	/**
	 * getOrderLeId
	 *
	 * @return orderLeId
	 */

	public Integer getOrderLeId() {
		return orderLeId;
	}

	/**
	 * setOrderLeId
	 *
	 * @param orderLeId
	 */
	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	/**
	 * getAttachmentType
	 *
	 * @return String
	 */
	public String getAttachmentType() {
		return attachmentType;
	}

	/**
	 * setAttachmentType
	 *
	 * @param attachmentType
	 */

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	/**
	 * getReferenceName
	 *
	 * @return referenceName
	 */

	public String getReferenceName() {
		return referenceName;
	}

	/**
	 * setReferenceName
	 *
	 * @param referenceName
	 */

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public Integer getQouteLeId() {
		return qouteLeId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
