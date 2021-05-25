
package com.tcl.dias.serviceactivation.activation.netp.beans.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ConfigFailureResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConfigFailureResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="actionPerformed" type="{http://www.tcl.com/2011/11/ace/common/xsd}ActionPerformed" minOccurs="0"/&gt;
 *         &lt;element name="errorGenerationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="pathOfOutputFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorDetails" type="{http://www.tcl.com/2011/11/netpsvc/xsd}ErrorDetails" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigFailureResponse", propOrder = {
    "serviceId",
    "requestId",
    "actionPerformed",
    "errorGenerationTime",
    "pathOfOutputFile",
    "errorDetails"
})
public class ConfigFailureResponse {

    protected String serviceId;
    protected String requestId;
    @XmlSchemaType(name = "string")
    protected ActionPerformed actionPerformed;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar errorGenerationTime;
    protected String pathOfOutputFile;
    protected List<ErrorDetails> errorDetails;

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
     * Gets the value of the errorGenerationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getErrorGenerationTime() {
        return errorGenerationTime;
    }

    /**
     * Sets the value of the errorGenerationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setErrorGenerationTime(XMLGregorianCalendar value) {
        this.errorGenerationTime = value;
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
     * {@link ErrorDetails }
     * 
     * 
     */
    public List<ErrorDetails> getErrorDetails() {
        if (errorDetails == null) {
            errorDetails = new ArrayList<ErrorDetails>();
        }
        return this.errorDetails;
    }

}
