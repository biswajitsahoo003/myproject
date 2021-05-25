
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HVPLS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HVPLS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}IPService">
 *       &lt;sequence>
 *         &lt;element name="customerSiteId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hubPE" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HVPLS", propOrder = {
    "customerSiteId",
    "hubPE"
})
public class HVPLS
    extends IPService
{

    protected String customerSiteId;
    protected Router hubPE;

    /**
     * Gets the value of the customerSiteId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    /**
     * Sets the value of the customerSiteId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerSiteId(String value) {
        this.customerSiteId = value;
    }

    /**
     * Gets the value of the hubPE property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getHubPE() {
        return hubPE;
    }

    /**
     * Sets the value of the hubPE property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setHubPE(Router value) {
        this.hubPE = value;
    }

}
