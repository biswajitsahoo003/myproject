package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * SolutionAttachmentBean.
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SolutionAttachmentBean {

    private Integer attachmentId;
    private String category;
    private String name;

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

	@Override
    public String toString() {
        return "AttachmentIdBean{" +
                "attachmentId=" + attachmentId +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
