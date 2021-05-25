package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean is used for Customer CPE Installation Confirmation.
 * 
 *  @author yogesh
 */
public class CustomerCPEInstallationConfirmationBean extends TaskDetailsBaseBean {

	private String installationConfimation;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String tentativeCpeInstallationConfirmationDate;

	public String getInstallationConfimation() {
		return installationConfimation;
	}

	public void setInstallationConfimation(String installationConfimation) {
		this.installationConfimation = installationConfimation;
	}

	public String getTentativeCpeInstallationConfirmationDate() {
		return tentativeCpeInstallationConfirmationDate;
	}

	public void setTentativeCpeInstallationConfirmationDate(String tentativeCpeInstallationConfirmationDate) {
		this.tentativeCpeInstallationConfirmationDate = tentativeCpeInstallationConfirmationDate;
	}

	
}
