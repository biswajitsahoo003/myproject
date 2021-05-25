package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * AttachmentIdBean.
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AttachmentIdBean {

    private Integer attachmentId;
    private String category;

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

    @Override
    public String toString() {
        return "AttachmentIdBean{" +
                "attachmentId=" + attachmentId +
                ", category='" + category + '\'' +
                '}';
    }
}
