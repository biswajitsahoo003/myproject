package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
import java.util.Map;

public class StageCountBean
{
    List<Map<String, String>> stageCount;
    Map<String, String> orderCount;

    public List<Map<String, String>> getStageCount() {
        return stageCount;
    }

    public void setStageCount(List<Map<String, String>> stageCount) {
        this.stageCount = stageCount;
    }

    public Map<String, String> getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Map<String, String> orderCount) {
        this.orderCount = orderCount;
    }

    @Override
    public String toString() {
        return "stageCountBean{" +
                "stageCount=" + stageCount +
                ", orderCount=" + orderCount +
                '}';
    }
}
