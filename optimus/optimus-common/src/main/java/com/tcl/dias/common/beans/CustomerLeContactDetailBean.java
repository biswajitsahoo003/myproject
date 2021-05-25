package com.tcl.dias.common.beans;

public class CustomerLeContactDetailBean {
	private Integer id;
	private String name;
	private String address;
	private String emailId;
	private String mobilePhone;
	private String homePhone;
	private String otherPhone;
	private String fax;
	private String title;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getOtherPhone() {
		return otherPhone;
	}

	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "CustomerLeContactDetailBean{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", emailId='" + emailId + '\'' +
				", mobilePhone='" + mobilePhone + '\'' +
				", homePhone='" + homePhone + '\'' +
				", otherPhone='" + otherPhone + '\'' +
				", fax='" + fax + '\'' +
				'}';
	}
}
