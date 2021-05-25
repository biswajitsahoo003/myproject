package com.tcl.dias.servicefulfillment.beans.gsc;

public class DocumentBean {

	private Integer attachmentId;
	private String category;
	private String name;
	private String url;

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "DocumentBean [attachmentId=" + attachmentId + ", category=" + category + ", name=" + name + ", url="
				+ url + "]";
	}

}
