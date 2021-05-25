package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Protection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Protection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="circuitId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="circuitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="protectionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="workerCircuitId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="workerCircuitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Protection", propOrder = {
    "circuitId",
    "circuitName",
    "protectionType",
    "workerCircuitId",
    "workerCircuitName"
})
public class Protection {

    protected long circuitId;
    protected String circuitName;
    protected String protectionType;
    protected long workerCircuitId;
    protected String workerCircuitName;

    /**
     * Gets the value of the circuitId property.
     * 
     */
    public long getCircuitId() {
        return circuitId;
    }

    /**
     * Sets the value of the circuitId property.
     * 
     */
    public void setCircuitId(long value) {
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
     */
    public long getWorkerCircuitId() {
        return workerCircuitId;
    }

    /**
     * Sets the value of the workerCircuitId property.
     * 
     */
    public void setWorkerCircuitId(long value) {
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

    @Override
    public String toString() {
        return "Protection{" +
                "circuitId=" + circuitId +
                ", circuitName='" + circuitName + '\'' +
                ", protectionType='" + protectionType + '\'' +
                ", workerCircuitId=" + workerCircuitId +
                ", workerCircuitName='" + workerCircuitName + '\'' +
                '}';
    }
}
