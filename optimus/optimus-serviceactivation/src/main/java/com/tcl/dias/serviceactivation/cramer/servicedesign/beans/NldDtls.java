
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nldDtls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nldDtls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ALLOWEDTIER1HOPS" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="ALLOW_TTSL_NODES" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="EgressNode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IngressNode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OTHER_SEGMENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TIER1_TO_TIER1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nldDtls", propOrder = {
    "allowedtier1HOPS",
    "allowttslnodes",
    "egressNode",
    "ingressNode",
    "othersegment",
    "tier1TOTIER1"
})
public class NldDtls {

    @XmlElement(name = "ALLOWEDTIER1HOPS")
    protected long allowedtier1HOPS;
    @XmlElement(name = "ALLOW_TTSL_NODES")
    protected boolean allowttslnodes;
    @XmlElement(name = "EgressNode")
    protected String egressNode;
    @XmlElement(name = "IngressNode")
    protected String ingressNode;
    @XmlElement(name = "OTHER_SEGMENT")
    protected String othersegment;
    @XmlElement(name = "TIER1_TO_TIER1")
    protected String tier1TOTIER1;

    /**
     * Gets the value of the allowedtier1HOPS property.
     * 
     */
    public long getALLOWEDTIER1HOPS() {
        return allowedtier1HOPS;
    }

    /**
     * Sets the value of the allowedtier1HOPS property.
     * 
     */
    public void setALLOWEDTIER1HOPS(long value) {
        this.allowedtier1HOPS = value;
    }

    /**
     * Gets the value of the allowttslnodes property.
     * 
     */
    public boolean isALLOWTTSLNODES() {
        return allowttslnodes;
    }

    /**
     * Sets the value of the allowttslnodes property.
     * 
     */
    public void setALLOWTTSLNODES(boolean value) {
        this.allowttslnodes = value;
    }

    /**
     * Gets the value of the egressNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEgressNode() {
        return egressNode;
    }

    /**
     * Sets the value of the egressNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEgressNode(String value) {
        this.egressNode = value;
    }

    /**
     * Gets the value of the ingressNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIngressNode() {
        return ingressNode;
    }

    /**
     * Sets the value of the ingressNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIngressNode(String value) {
        this.ingressNode = value;
    }

    /**
     * Gets the value of the othersegment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOTHERSEGMENT() {
        return othersegment;
    }

    /**
     * Sets the value of the othersegment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOTHERSEGMENT(String value) {
        this.othersegment = value;
    }

    /**
     * Gets the value of the tier1TOTIER1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIER1TOTIER1() {
        return tier1TOTIER1;
    }

    /**
     * Sets the value of the tier1TOTIER1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIER1TOTIER1(String value) {
        this.tier1TOTIER1 = value;
    }

}
