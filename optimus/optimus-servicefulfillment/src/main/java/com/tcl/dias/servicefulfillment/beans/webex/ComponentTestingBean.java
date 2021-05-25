package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ComponentTestingBean extends TaskDetailsBaseBean {

    private String scheduleTheMeeting;
    private String meetingPage;
    private String recording;
    private String screenSharing;
    private String meetingVideoTesting;
    private String muteFunctionality;

    public String getScheduleTheMeeting() {
        return scheduleTheMeeting;
    }

    public void setScheduleTheMeeting(String scheduleTheMeeting) {
        this.scheduleTheMeeting = scheduleTheMeeting;
    }

    public String getMeetingPage() {
        return meetingPage;
    }

    public void setMeetingPage(String meetingPage) {
        this.meetingPage = meetingPage;
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

	public String getMuteFunctionality() {
		return muteFunctionality;
	}

	public void setMuteFunctionality(String muteFunctionality) {
		this.muteFunctionality = muteFunctionality;
	}
}
