
package com.tcl.dias.serviceactivation.activation.netp.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ConfigSuccessResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConfigSuccessResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestCompletionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="actionPerformed" type="{http://www.tcl.com/2011/11/ace/common/xsd}ActionPerformed" minOccurs="0"/&gt;
 *         &lt;element name="pathOfOutputFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cancellationInitiatedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigSuccessResponse", propOrder = {
    "serviceId",
    "requestId",
    "requestCompletionTime",
    "actionPerformed",
    "pathOfOutputFile",
    "cancellationInitiatedBy",
    "successMessage"
})
public class ConfigSuccessResponse {

    protected String serviceId;
    protected String requestId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestCompletionTime;
    @XmlSchemaType(name = "string")
    protected ActionPerformed actionPerformed;
    protected String pathOfOutputFile;
    protected String cancellationInitiatedBy;
    protected String successMessage;


    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the requestCompletionTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestCompletionTime() {
        return requestCompletionTime;
    }

    /**
     * Sets the value of the requestCompletionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestCompletionTime(XMLGregorianCalendar value) {
        this.requestCompletionTime = value;
    }

    /**
     * Gets the value of the actionPerformed property.
     * 
     * @return
     *     possible object is
     *     {@link ActionPerformed }
     *     
     */
    public ActionPerformed getActionPerformed() {
        return actionPerformed;
    }

    /**
     * Sets the value of the actionPerformed property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionPerformed }
     *     
     */
    public void setActionPerformed(ActionPerformed value) {
        this.actionPerformed = value;
    }

    /**
     * Gets the value of the pathOfOutputFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathOfOutputFile() {
        return pathOfOutputFile;
    }

    /**
     * Sets the value of the pathOfOutputFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathOfOutputFile(String value) {
        this.pathOfOutputFile = value;
    }

    /**
     * Gets the value of the cancellationInitiatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancellationInitiatedBy() {
        return cancellationInitiatedBy;
    }

    /**
     * Sets the value of the cancellationInitiatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancellationInitiatedBy(String value) {
        this.cancellationInitiatedBy = value;
    }

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
    
    

}
