package com.tcl.dias.common.beans;

/**
 * Created by Raja Anbazhagan on 02-08-2017.
 */
public class TempUploadUrlInfo {
    private String requestId;
    private String temporaryUploadUrl;
    private Long secondsTillUrlExpiry;
    private String containerName;
    private CommonValidationResponse commonValidationResponse;
    
  //  private Integer containerId;
  //  private List<AttachmentTagMasterVO> attachmentTags;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTemporaryUploadUrl() {
        return temporaryUploadUrl;
    }

    public void setTemporaryUploadUrl(String temporaryUploadUrl) {
        this.temporaryUploadUrl = temporaryUploadUrl;
    }

    public Long getSecondsTillUrlExpiry() {
        return secondsTillUrlExpiry;
    }

    public void setSecondsTillUrlExpiry(Long secondsTillUrlExpiry) {
        this.secondsTillUrlExpiry = secondsTillUrlExpiry;

    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
    
    public CommonValidationResponse getCommonValidationResponse() {
		return commonValidationResponse;
	}

	public void setCommonValidationResponse(CommonValidationResponse commonValidationResponse) {
		this.commonValidationResponse = commonValidationResponse;
	}
}
