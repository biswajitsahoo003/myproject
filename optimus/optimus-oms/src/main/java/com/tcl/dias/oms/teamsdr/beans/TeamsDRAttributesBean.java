package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;

/**
 * Bean for teams DR attributes
 * @author Srinivasa Raghavan
 */
public class TeamsDRAttributesBean {

    private Integer attributeId;
    private String attributeName;
    private String attributeValue;
    private String description;

    public TeamsDRAttributesBean() {
        // default constructor
    }

    /**
     * QuoteLeAttributeValue to TeamsDRAttributesBean
     *
     * @param quoteLeAttributeValue
     * @return
     */
    public static TeamsDRAttributesBean toTeamsDRAttributesBean(QuoteLeAttributeValue quoteLeAttributeValue)
    {
        TeamsDRAttributesBean teamsDRAttributesBean = new TeamsDRAttributesBean();
        teamsDRAttributesBean.setAttributeId(quoteLeAttributeValue.getId());
        teamsDRAttributesBean.setAttributeName(quoteLeAttributeValue.getMstOmsAttribute().getName());
        teamsDRAttributesBean.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
        teamsDRAttributesBean.setDescription(quoteLeAttributeValue.getMstOmsAttribute().getDescription());
        return teamsDRAttributesBean;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String toString() {
        return "TeamsDRAttributesBean{" +
                "attributeId=" + attributeId +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
