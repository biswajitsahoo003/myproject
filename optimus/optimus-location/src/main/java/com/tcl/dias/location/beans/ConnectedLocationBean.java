package com.tcl.dias.location.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Connected Location bean
 *
 *
 * @author Thamizhsevi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ConnectedLocationBean {

    private List<String> latLongList=new ArrayList<>();
    private Integer distance;

    public List<String> getLatLongList() {
        return latLongList;
    }

    public void setLatLongList(List<String> latLongList) {
        this.latLongList = latLongList;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
