package com.tcl.dias.serviceinventory.beans;

public class ContactBeans {
	
	private String businessPhone;
	
	private String businessEmail;
	
	private String siServiceDetailId;
	
	private String contactName;
	
	private String contactType;

	private String businessMobile;

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getSiServiceDetailId() {
		return siServiceDetailId;
	}

	public void setSiServiceDetailId(String siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getBusinessMobile() {
		return businessMobile;
	}

	public void setBusinessMobile(String businessMobile) {
		this.businessMobile = businessMobile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessEmail == null) ? 0 : businessEmail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactBeans other = (ContactBeans) obj;
		if (businessEmail == null) {
			if (other.businessEmail != null)
				return false;
		} else if (!businessEmail.equals(other.businessEmail))
			return false;
		return true;
	}
	
	
	

}
