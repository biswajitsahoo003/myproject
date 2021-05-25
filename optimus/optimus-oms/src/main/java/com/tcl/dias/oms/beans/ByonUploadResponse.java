package com.tcl.dias.oms.beans;

/**
 * This Bean contains the status details of byon upload excel
 * @author vpachava
 *
 */
public class ByonUploadResponse {

	private boolean byonUploadError;

	private boolean byonProfileValid;
	
	private boolean isFileEmpty;

	public boolean isByonUploadError() {
		return byonUploadError;
	}

	public void setByonUploadError(boolean byonUploadError) {
		this.byonUploadError = byonUploadError;
	}

	public boolean isByonProfileValid() {
		return byonProfileValid;
	}

	public void setByonProfileValid(boolean byonProfileValid) {
		this.byonProfileValid = byonProfileValid;
	}

	public boolean isFileEmpty() {
		return isFileEmpty;
	}

	public void setFileEmpty(boolean isFileEmpty) {
		this.isFileEmpty = isFileEmpty;
	}

	
}
