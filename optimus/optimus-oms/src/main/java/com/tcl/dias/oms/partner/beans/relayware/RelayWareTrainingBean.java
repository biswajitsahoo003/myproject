package com.tcl.dias.oms.partner.beans.relayware;

import java.util.List;
import java.util.Map;

/**
 * Bean for relay ware training bean
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class RelayWareTrainingBean {

    private Integer partnerId;
    private List<TrainingCategory> trainingCategories;
    private Map<String, Long> courseStatusCount;

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public List<TrainingCategory> getTrainingCategories() {
        return trainingCategories;
    }

    public void setTrainingCategories(List<TrainingCategory> trainingCategories) {
        this.trainingCategories = trainingCategories;
    }

    public Map<String, Long> getCourseStatusCount() {
        return courseStatusCount;
    }

    public void setCourseStatusCount(Map<String, Long> courseStatusCount) {
        this.courseStatusCount = courseStatusCount;
    }
}
