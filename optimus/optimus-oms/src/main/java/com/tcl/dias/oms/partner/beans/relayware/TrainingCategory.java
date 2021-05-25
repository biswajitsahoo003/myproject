package com.tcl.dias.oms.partner.beans.relayware;

import java.util.List;

/**
 * Bean for Training Category
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TrainingCategory {

    private String category;
    private List<TrainingStatus> trainingStatuses;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<TrainingStatus> getTrainingStatuses() {
        return trainingStatuses;
    }

    public void setTrainingStatuses(List<TrainingStatus> trainingStatuses) {
        this.trainingStatuses = trainingStatuses;
    }

}
