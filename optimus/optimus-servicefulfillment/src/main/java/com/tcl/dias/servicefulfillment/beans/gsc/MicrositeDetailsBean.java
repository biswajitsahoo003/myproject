package com.tcl.dias.servicefulfillment.beans.gsc;

public class MicrositeDetailsBean {
	
    private String titleBar;
    private String micrositeUrl;
    private String colorScheme;
    private String siteAdminEmailId;
    private String attachmentId;
    
	private String webOrderId;
	private String subscriptionId;
	private String entityName;
	private String activationLink;
	
	public String getWebOrderId() {
		return webOrderId;
	}
	public void setWebOrderId(String webOrderId) {
		this.webOrderId = webOrderId;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getActivationLink() {
		return activationLink;
	}
	public void setActivationLink(String activationLink) {
		this.activationLink = activationLink;
	}
    
    public String getTitleBar() {
        return titleBar;
    }

    public void setTitleBar(String titleBar) {
        this.titleBar = titleBar;
    }

    public String getMicrositeUrl() {
        return micrositeUrl;
    }

    public void setMicrositeUrl(String micrositeUrl) {
        this.micrositeUrl = micrositeUrl;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public String getSiteAdminEmailId() {
        return siteAdminEmailId;
    }

    public void setSiteAdminEmailId(String siteAdminEmailId) {
        this.siteAdminEmailId = siteAdminEmailId;
    }

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
