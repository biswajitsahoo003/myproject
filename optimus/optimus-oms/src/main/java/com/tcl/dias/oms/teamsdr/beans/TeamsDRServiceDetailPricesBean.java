package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Teams DR Service detail price
 * @author Srinivasa Raghavan
 */
@JsonInclude(Include.NON_NULL)
public class TeamsDRServiceDetailPricesBean {

    @JsonProperty("component_variant")
    private
    String compVariant;

    @JsonProperty("component_sub_variant")
    private
    String subVariant;

    @JsonProperty("usage_in_usd")
    private Double usage;

    @JsonProperty("uom")
    private
    String billedBy;

    public String getCompVariant() {
        return compVariant;
    }

    public void setCompVariant(String compVariant) {
        this.compVariant = compVariant;
    }

    public String getSubVariant() {
        return subVariant;
    }

    public void setSubVariant(String subVariant) {
        this.subVariant = subVariant;
    }

    public Double getUsage() {
        return usage;
    }

    public void setUsage(Double usage) {
        this.usage = usage;
    }

    public String getBilledBy() {
        return billedBy;
    }

    public void setBilledBy(String billedBy) {
        this.billedBy = billedBy;
    }

    @Override
    public String toString() {
        return "TeamsDRServiceDetailPricesBean{" +
                "compVariant='" + compVariant + '\'' +
                ", subVariant='" + subVariant + '\'' +
                ", usage='" + usage + '\'' +
                ", billedBy='" + billedBy + '\'' +
                '}';
    }
}
