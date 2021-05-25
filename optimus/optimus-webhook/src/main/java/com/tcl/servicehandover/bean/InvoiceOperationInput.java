package com.tcl.servicehandover.bean;

public class InvoiceOperationInput {

    private String ceaseFlag;
    
    private String errorMessage;
    
    private String invoiceNumber;
    
    private String status;

    private String taskType;

    private String transactionId;
    
    private String serviceId;
    
    private String serviceType;

	public String getCeaseFlag() {
		return ceaseFlag;
	}

	public void setCeaseFlag(String ceaseFlag) {
		this.ceaseFlag = ceaseFlag;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	} 

}
