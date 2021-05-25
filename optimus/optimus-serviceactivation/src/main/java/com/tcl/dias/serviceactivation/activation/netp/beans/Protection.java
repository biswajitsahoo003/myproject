
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Protection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Protection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="circuitId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="circuitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="protectionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workerCircuitId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="workerCircuitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Protection", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd", propOrder = {
    "circuitId",
    "circuitName",
    "protectionType",
    "workerCircuitId",
    "workerCircuitName"
})
public class Protection {

    protected Long circuitId;
    protected String circuitName;
    protected String protectionType;
    protected Long workerCircuitId;
    protected String workerCircuitName;

    /**
     * Gets the value of the circuitId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCircuitId() {
        return circuitId;
    }

    /**
     * Sets the value of the circuitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCircuitId(Long value) {
        this.circuitId = value;
    }

    /**
     * Gets the value of the circuitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitName() {
        return circuitName;
    }

    /**
     * Sets the value of the circuitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitName(String value) {
        this.circuitName = value;
    }

    /**
     * Gets the value of the protectionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtectionType() {
        return protectionType;
    }

    /**
     * Sets the value of the protectionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtectionType(String value) {
        this.protectionType = value;
    }

    /**
     * Gets the value of the workerCircuitId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getWorkerCircuitId() {
        return workerCircuitId;
    }

    /**
     * Sets the value of the workerCircuitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setWorkerCircuitId(Long value) {
        this.workerCircuitId = value;
    }

    /**
     * Gets the value of the workerCircuitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkerCircuitName() {
        return workerCircuitName;
    }

    /**
     * Sets the value of the workerCircuitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkerCircuitName(String value) {
        this.workerCircuitName = value;
    }

}
