package com.tcl.dias.servicefulfillment.beans;

/**
 * Bean class for RoadTypeLengthBean
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class RoadTypeLengthBean {

    private String typeOfRoad;
    private Double length;

    public String getTypeOfRoad() {
        return typeOfRoad;
    }

    public void setTypeOfRoad(String typeOfRoad) {
        this.typeOfRoad = typeOfRoad;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "RoadTypeLengthBean{" +
                "typeOfRoad='" + typeOfRoad + '\'' +
                ", length=" + length +
                '}';
    }
}
