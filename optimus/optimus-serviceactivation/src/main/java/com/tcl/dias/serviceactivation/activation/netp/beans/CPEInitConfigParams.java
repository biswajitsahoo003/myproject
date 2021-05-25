
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CPEInitConfigParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CPEInitConfigParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isSendInitTemplate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="InitLoginUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InitLoginPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InitEnablePwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CPEInitConfigParams", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "isSendInitTemplate",
    "initLoginUserID",
    "initLoginPwd",
    "initEnablePwd"
})
public class CPEInitConfigParams {

    protected Boolean isSendInitTemplate;
    @XmlElement(name = "InitLoginUserID")
    protected String initLoginUserID;
    @XmlElement(name = "InitLoginPwd")
    protected String initLoginPwd;
    @XmlElement(name = "InitEnablePwd")
    protected String initEnablePwd;

    /**
     * Gets the value of the isSendInitTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSendInitTemplate() {
        return isSendInitTemplate;
    }

    /**
     * Sets the value of the isSendInitTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSendInitTemplate(Boolean value) {
        this.isSendInitTemplate = value;
    }

    /**
     * Gets the value of the initLoginUserID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitLoginUserID() {
        return initLoginUserID;
    }

    /**
     * Sets the value of the initLoginUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitLoginUserID(String value) {
        this.initLoginUserID = value;
    }

    /**
     * Gets the value of the initLoginPwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitLoginPwd() {
        return initLoginPwd;
    }

    /**
     * Sets the value of the initLoginPwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitLoginPwd(String value) {
        this.initLoginPwd = value;
    }

    /**
     * Gets the value of the initEnablePwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitEnablePwd() {
        return initEnablePwd;
    }

    /**
     * Sets the value of the initEnablePwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitEnablePwd(String value) {
        this.initEnablePwd = value;
    }

}
