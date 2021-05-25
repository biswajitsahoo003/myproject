package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * NodeToConfigureBean.class
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *
 */
public class NodeToConfigureBean {

    private Integer id;
    private Boolean isACEActionRequired;
    private Boolean isCienaActionRequired;
    private Boolean isNOCActionRequired;
    private String nodeAlias1;
    private String nodeAlias2;
    private String nodeDef;
    private long nodeDefId;
    private String nodeName;
    private String nodeType;
    private long nodeTypeId;
    private String configuredDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getACEActionRequired() {
        return isACEActionRequired;
    }

    public void setACEActionRequired(Boolean ACEActionRequired) {
        isACEActionRequired = ACEActionRequired;
    }

    public Boolean getCienaActionRequired() {
        return isCienaActionRequired;
    }

    public void setCienaActionRequired(Boolean cienaActionRequired) {
        isCienaActionRequired = cienaActionRequired;
    }

    public Boolean getNOCActionRequired() {
        return isNOCActionRequired;
    }

    public void setNOCActionRequired(Boolean NOCActionRequired) {
        isNOCActionRequired = NOCActionRequired;
    }

    public String getNodeAlias1() {
        return nodeAlias1;
    }

    public void setNodeAlias1(String nodeAlias1) {
        this.nodeAlias1 = nodeAlias1;
    }

    public String getNodeAlias2() {
        return nodeAlias2;
    }

    public void setNodeAlias2(String nodeAlias2) {
        this.nodeAlias2 = nodeAlias2;
    }

    public String getNodeDef() {
        return nodeDef;
    }

    public void setNodeDef(String nodeDef) {
        this.nodeDef = nodeDef;
    }

    public long getNodeDefId() {
        return nodeDefId;
    }

    public void setNodeDefId(long nodeDefId) {
        this.nodeDefId = nodeDefId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public long getNodeTypeId() {
        return nodeTypeId;
    }

    public void setNodeTypeId(long nodeTypeId) {
        this.nodeTypeId = nodeTypeId;
    }

    public String getConfiguredDate() {
        return configuredDate;
    }

    public void setConfiguredDate(String configuredDate) {
        this.configuredDate = configuredDate;
    }
}
