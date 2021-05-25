
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NON_ROUTABLE_PUBLIC_IP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b", propOrder = {
    "nonroutablepublicip"
})
public class IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B {

    @XmlElement(name = "NON_ROUTABLE_PUBLIC_IP")
    protected String nonroutablepublicip;

    /**
     * Gets the value of the nonroutablepublicip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNONROUTABLEPUBLICIP() {
        return nonroutablepublicip;
    }

    /**
     * Sets the value of the nonroutablepublicip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNONROUTABLEPUBLICIP(String value) {
        this.nonroutablepublicip = value;
    }

}
