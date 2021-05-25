package com.tcl.dias.oms.partner.beans.relayware;

import java.util.List;

/**
 * Bean for Training Status
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TrainingStatus {

    private String status;
    private List<TrainingDetail> trainingDetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TrainingDetail> getTrainingDetails() {
        return trainingDetails;
    }

    public void setTrainingDetails(List<TrainingDetail> trainingDetails) {
        this.trainingDetails = trainingDetails;
    }

}
