package com.tcl.dias.common.beans;

public class CustomerLeAccountManagerDetails {
	public String getAccountManagerMob() {
		return accountManagerMob;
	}

	public void setAccountManagerMob(String accountManagerMob) {
		this.accountManagerMob = accountManagerMob;
	}

	public String getAccountManagerName() {
		return accountManagerName;
	}
	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}
	public String getAccountManagerEmailId() {
		return accountManagerEmailId;
	}
	public void setAccountManagerEmailId(String accountManagerEmailId) {
		this.accountManagerEmailId = accountManagerEmailId;
	}
	private String accountManagerName;
	private String accountManagerEmailId;
	private String accountManagerMob;

	@Override
	public String toString() {
		return "CustomerLeAccountManagerDetails{" +
				"accountManagerName='" + accountManagerName + '\'' +
				", accountManagerEmailId='" + accountManagerEmailId + '\'' +
				", accountManagerMob='" + accountManagerMob + '\'' +
				'}';
	}
}
