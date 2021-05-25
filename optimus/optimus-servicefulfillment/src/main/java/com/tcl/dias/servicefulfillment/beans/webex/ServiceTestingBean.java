package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ServiceTestingBean extends TaskDetailsBaseBean {
    private String scheduleTheMeeting;
    private String e2eServiceTestingCompleteStatus;
    private String meetingPage;
    private String callInTeleconference;
    private String callback;
    private String dedicatedNumber;
    private String cugCalling;
    private String recording;
    private String screenSharing;
    private String meetingVideoTesting;
    private String cmrTesting;
    private List<AttachmentIdBean> documentIds;

    public String getScheduleTheMeeting() {
        return scheduleTheMeeting;
    }

    public void setScheduleTheMeeting(String scheduleTheMeeting) {
        this.scheduleTheMeeting = scheduleTheMeeting;
    }

    public String getE2eServiceTestingCompleteStatus() {
        return e2eServiceTestingCompleteStatus;
    }

    public void setE2eServiceTestingCompleteStatus(String e2eServiceTestingCompleteStatus) {
        this.e2eServiceTestingCompleteStatus = e2eServiceTestingCompleteStatus;
    }

    public String getMeetingPage() {
        return meetingPage;
    }

    public void setMeetingPage(String meetingPage) {
        this.meetingPage = meetingPage;
    }

    public String getCallInTeleconference() {
        return callInTeleconference;
    }

    public void setCallInTeleconference(String callInTeleconference) {
        this.callInTeleconference = callInTeleconference;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getDedicatedNumber() {
        return dedicatedNumber;
    }

    public void setDedicatedNumber(String dedicatedNumber) {
        this.dedicatedNumber = dedicatedNumber;
    }

    public String getCugCalling() {
        return cugCalling;
    }

    public void setCugCalling(String cugCalling) {
        this.cugCalling = cugCalling;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }

    public String getScreenSharing() {
        return screenSharing;
    }

    public void setScreenSharing(String screenSharing) {
        this.screenSharing = screenSharing;
    }

    public String getMeetingVideoTesting() {
        return meetingVideoTesting;
    }

    public void setMeetingVideoTesting(String meetingVideoTesting) {
        this.meetingVideoTesting = meetingVideoTesting;
    }

    public String getCmrTesting() {
        return cmrTesting;
    }

    public void setCmrTesting(String cmrTesting) {
        this.cmrTesting = cmrTesting;
    }

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
