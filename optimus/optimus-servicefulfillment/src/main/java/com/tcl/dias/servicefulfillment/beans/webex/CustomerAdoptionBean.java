package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CustomerAdoptionBean extends TaskDetailsBaseBean {
    private String numberOfTrainingsCompleted;
    private String sharedTrainingMaterial;
    private String overallTrainingFeedbackComments;

    public String getNumberOfTrainingsCompleted() {
        return numberOfTrainingsCompleted;
    }

    public void setNumberOfTrainingsCompleted(String numberOfTrainingsCompleted) {
        this.numberOfTrainingsCompleted = numberOfTrainingsCompleted;
    }

    public String getSharedTrainingMaterial() {
        return sharedTrainingMaterial;
    }

    public void setSharedTrainingMaterial(String sharedTrainingMaterial) {
        this.sharedTrainingMaterial = sharedTrainingMaterial;
    }

    public String getOverallTrainingFeedbackComments() {
        return overallTrainingFeedbackComments;
    }

    public void setOverallTrainingFeedbackComments(String overallTrainingFeedbackComments) {
        this.overallTrainingFeedbackComments = overallTrainingFeedbackComments;
    }
}
