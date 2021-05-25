/**
 * 
 */
package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author VISHESH AWASTHI
 *
 */

@JsonInclude(Include.NON_NULL)
public class CustomerAttachmentBean {
	private Integer id;
	private String displayName;
	private Byte isDeleted;
	private String name;

	private String uriPath;
	private String docType;
	private String productName;

	public CustomerAttachmentBean() {
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


	/**
	 * getDocType
	 * 
	 * @return
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * setDocType
	 * 
	 * @param docType
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
