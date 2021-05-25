package com.tcl.dias.serviceinventory.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.serviceinventory.entity.entities.Attachment;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentBean {

    private Integer Id;
    private String name;
    private String category;
    private String type;
    private String storagePathUrl;
    private String containerName;
    private String verified;
    private String verificationFailureReason;
    private String isNew;
    
    
    public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	private boolean swiftEnabled;

    public AttachmentBean() {
    }

    public static AttachmentBean mapToBean(Attachment attachment) {
        AttachmentBean attachmentBean = new AttachmentBean();
        attachmentBean.setId(attachment.getId());
        attachmentBean.setName(attachment.getName());
        attachmentBean.setCategory(attachment.getCategory());
        attachmentBean.setType(attachment.getType());
        attachmentBean.setStoragePathUrl(attachment.getStoragePathUrl());
      /*  attachmentBean.setVerified(attachment.getVerified());
        attachmentBean.setVerificationFailureReason(attachment.getVerificationFailureReason());*/
        return attachmentBean;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoragePathUrl() {
        return storagePathUrl;
    }

    public void setStoragePathUrl(String storagePathUrl) {
        this.storagePathUrl = storagePathUrl;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getVerificationFailureReason() {
        return verificationFailureReason;
    }

    public void setVerificationFailureReason(String verificationFailureReason) {
        this.verificationFailureReason = verificationFailureReason;
    }

    public boolean isSwiftEnabled() {
        return swiftEnabled;
    }

    public void setSwiftEnabled(boolean swiftEnabled) {
        this.swiftEnabled = swiftEnabled;
    }
    
    

    public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	@Override
    public String toString() {
        return "AttachmentBean{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", storagePathUrl='" + storagePathUrl + '\'' +
                ", verified='" + verified + '\'' +
                ", verificationFailureReason='" + verificationFailureReason + '\'' +
                '}';
    }
}
