package com.tcl.dias.common.beans;

/**
 * This class contains the Attachment bean information
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AttachmentBean {

	private Integer id;
	private String path;
	private String fileName;
	private Long expiryWindow;

	/**
	 * This returns the file name
	 * 
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter for file name
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * this returns the path
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * this uset to set the path
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * @return the expiryWindow
	 */
	public Long getExpiryWindow() {
		return expiryWindow;
	}

	/**
	 * @param expiryWindow
	 *            the expiryWindow to set
	 */
	public void setExpiryWindow(Long expiryWindow) {
		this.expiryWindow = expiryWindow;
	}

}
