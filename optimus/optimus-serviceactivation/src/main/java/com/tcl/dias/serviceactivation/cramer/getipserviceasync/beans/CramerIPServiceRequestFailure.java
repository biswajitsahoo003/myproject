package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CramerTxServiceRequestFailure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CramerTxServiceRequestFailure"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorGenerationTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorDetails" type="{http://www.tcl.com/2012/09/csvc/xsd}CramerServiceErrorDetails" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CramerIPServiceRequestFailure", propOrder = {
    "serviceID",
    "requestID",
    "errorGenerationTime",
    "errorDetails"
})
public class CramerIPServiceRequestFailure {

    @XmlElement(name = "ServiceID")
    protected String serviceID;
    protected String requestID;
    protected String errorGenerationTime;
    protected List<CramerServiceErrorDetails> errorDetails;

    /**
     * Gets the value of the serviceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceID(String value) {
        this.serviceID = value;
    }

    /**
     * Gets the value of the errorGenerationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorGenerationTime() {
        return errorGenerationTime;
    }

    /**
     * Sets the value of the errorGenerationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorGenerationTime(String value) {
        this.errorGenerationTime = value;
    }

    /**
     * Gets the value of the errorDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CramerServiceErrorDetails }
     * 
     * 
     */
    public List<CramerServiceErrorDetails> getErrorDetails() {
        if (errorDetails == null) {
            errorDetails = new ArrayList<CramerServiceErrorDetails>();
        }
        return this.errorDetails;
    }

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
    
    

}
