package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NodeToBeConfigured complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NodeToBeConfigured"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isACEActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCienaActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNOCActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="nodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeDefId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="nodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeTypeId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeToBeConfigured", propOrder = {
    "isACEActionRequired",
    "isCienaActionRequired",
    "isNOCActionRequired",
    "nodeAlias1",
    "nodeAlias2",
    "nodeDef",
    "nodeDefId",
    "nodeName",
    "nodeType",
    "nodeTypeId"
})
public class NodeToBeConfigured {

    protected Boolean isACEActionRequired;
    protected Boolean isCienaActionRequired;
    protected Boolean isNOCActionRequired;
    protected String nodeAlias1;
    protected String nodeAlias2;
    protected String nodeDef;
    protected long nodeDefId;
    protected String nodeName;
    protected String nodeType;
    protected long nodeTypeId;

    /**
     * Gets the value of the isACEActionRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsACEActionRequired() {
        return isACEActionRequired;
    }

    /**
     * Sets the value of the isACEActionRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsACEActionRequired(Boolean value) {
        this.isACEActionRequired = value;
    }

    /**
     * Gets the value of the isCienaActionRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCienaActionRequired() {
        return isCienaActionRequired;
    }

    /**
     * Sets the value of the isCienaActionRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCienaActionRequired(Boolean value) {
        this.isCienaActionRequired = value;
    }

    /**
     * Gets the value of the isNOCActionRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNOCActionRequired() {
        return isNOCActionRequired;
    }

    /**
     * Sets the value of the isNOCActionRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNOCActionRequired(Boolean value) {
        this.isNOCActionRequired = value;
    }

    /**
     * Gets the value of the nodeAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeAlias1() {
        return nodeAlias1;
    }

    /**
     * Sets the value of the nodeAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeAlias1(String value) {
        this.nodeAlias1 = value;
    }

    /**
     * Gets the value of the nodeAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeAlias2() {
        return nodeAlias2;
    }

    /**
     * Sets the value of the nodeAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeAlias2(String value) {
        this.nodeAlias2 = value;
    }

    /**
     * Gets the value of the nodeDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeDef() {
        return nodeDef;
    }

    /**
     * Sets the value of the nodeDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeDef(String value) {
        this.nodeDef = value;
    }

    /**
     * Gets the value of the nodeDefId property.
     * 
     */
    public long getNodeDefId() {
        return nodeDefId;
    }

    /**
     * Sets the value of the nodeDefId property.
     * 
     */
    public void setNodeDefId(long value) {
        this.nodeDefId = value;
    }

    /**
     * Gets the value of the nodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Gets the value of the nodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeType(String value) {
        this.nodeType = value;
    }

    /**
     * Gets the value of the nodeTypeId property.
     * 
     */
    public long getNodeTypeId() {
        return nodeTypeId;
    }

    /**
     * Sets the value of the nodeTypeId property.
     * 
     */
    public void setNodeTypeId(long value) {
        this.nodeTypeId = value;
    }


    @Override
    public String toString() {
        return "NodeToBeConfigured{" +
                "isACEActionRequired=" + isACEActionRequired +
                ", isCienaActionRequired=" + isCienaActionRequired +
                ", isNOCActionRequired=" + isNOCActionRequired +
                ", nodeAlias1='" + nodeAlias1 + '\'' +
                ", nodeAlias2='" + nodeAlias2 + '\'' +
                ", nodeDef='" + nodeDef + '\'' +
                ", nodeDefId=" + nodeDefId +
                ", nodeName='" + nodeName + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", nodeTypeId=" + nodeTypeId +
                '}';
    }
}
