/**
 * 
 */
package com.tcl.dias.customer.bean;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.customer.entity.entities.Attachment;

/**
 * @author VISHESH AWASTHI
 *
 */

@JsonInclude(Include.NON_NULL)
public class AttachmentBean {

	private Integer id;
	private String displayName;
	private Byte isDeleted;
	private String name;
	@Column(name = "uri_path_or_url")
	private String uriPath;
	private String docType;

	public AttachmentBean() {
		// do nothing
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the isDeleted
	 */
	public Byte getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the uriPath
	 */
	public String getUriPath() {
		return uriPath;
	}

	/**
	 * @param uriPath
	 *            the uriPath to set
	 */
	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public AttachmentBean(Attachment attachment,String docType) {
		if(attachment!=null) {
			this.setId(attachment.getId());
			this.setName((attachment.getName()));
			this.setDisplayName((attachment.getDisplayName()));
			this.setIsDeleted(attachment.getIsDeleted());
			this.setUriPath(attachment.getUriPathOrUrl());
			this.setDocType(docType);
			
		}
	}

	/**
	 * getDocType
	 * @return
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * setDocType
	 * @param docType
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}


}
