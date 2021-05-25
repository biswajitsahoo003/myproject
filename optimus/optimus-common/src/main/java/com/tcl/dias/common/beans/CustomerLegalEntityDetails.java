package com.tcl.dias.common.beans;

public class CustomerLegalEntityDetails
{
	private Integer legalEntityId;
	private String legalEntityName;

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	@Override
	public String toString() {
		return "CustomerLegalEntityDetails{" +
				"legalEntityId=" + legalEntityId +
				", legalEntityName='" + legalEntityName + '\'' +
				'}';
	}
}
