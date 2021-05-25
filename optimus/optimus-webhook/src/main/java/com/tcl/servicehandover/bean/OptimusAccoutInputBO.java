package com.tcl.servicehandover.bean;

public class OptimusAccoutInputBO {

	private String inputGroupId;
	
	private String accountNumber;
    
    private String accountSyncResponse;
    
    private String genevaAccountNumber;
    
    private String genevaProfileId;
    
    private String serviceType;
    
    private String status;

	public String getInputGroupId() {
		return inputGroupId;
	}

	public void setInputGroupId(String inputGroupId) {
		this.inputGroupId = inputGroupId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountSyncResponse() {
		return accountSyncResponse;
	}

	public void setAccountSyncResponse(String accountSyncResponse) {
		this.accountSyncResponse = accountSyncResponse;
	}

	public String getGenevaAccountNumber() {
		return genevaAccountNumber;
	}

	public void setGenevaAccountNumber(String genevaAccountNumber) {
		this.genevaAccountNumber = genevaAccountNumber;
	}

	public String getGenevaProfileId() {
		return genevaProfileId;
	}

	public void setGenevaProfileId(String genevaProfileId) {
		this.genevaProfileId = genevaProfileId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "OptimusAccoutInputBO [inputGroupId=" + inputGroupId + ", accountNumber=" + accountNumber
				+ ", accountSyncResponse=" + accountSyncResponse + ", genevaAccountNumber=" + genevaAccountNumber
				+ ", genevaProfileId=" + genevaProfileId + ", serviceType=" + serviceType + ", status=" + status + "]";
	}
	
	

}
