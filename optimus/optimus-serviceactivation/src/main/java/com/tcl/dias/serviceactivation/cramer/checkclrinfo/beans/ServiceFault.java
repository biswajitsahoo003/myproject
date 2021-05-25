
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import com.tcl.dias.serviceactivation.cramer.checkipclr.beans.Throwable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ServiceFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceFault">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorLongDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorShortDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="suppressed" type="{http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr}throwable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = {
    "errorCode",
    "errorLongDescription",
    "errorShortDescription",
    "message",
    "suppressed"
})
public class ServiceFault {

    protected String errorCode;
    protected String errorLongDescription;
    protected String errorShortDescription;
    protected String message;
    protected List<Throwable> suppressed;

    /**
     * Gets the value of the errorCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorLongDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorLongDescription() {
        return errorLongDescription;
    }

    /**
     * Sets the value of the errorLongDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorLongDescription(String value) {
        this.errorLongDescription = value;
    }

    /**
     * Gets the value of the errorShortDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorShortDescription() {
        return errorShortDescription;
    }

    /**
     * Sets the value of the errorShortDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorShortDescription(String value) {
        this.errorShortDescription = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the suppressed property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suppressed property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuppressed().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Throwable }
     *
     *
     */
    public List<Throwable> getSuppressed() {
        if (suppressed == null) {
            suppressed = new ArrayList<Throwable>();
        }
        return this.suppressed;
    }

}
