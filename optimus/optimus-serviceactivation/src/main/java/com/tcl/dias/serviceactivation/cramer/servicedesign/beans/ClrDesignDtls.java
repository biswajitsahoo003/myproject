
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clrDesignDtls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clrDesignDtls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ILD_Dtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}ildDtls" minOccurs="0"/&gt;
 *         &lt;element name="MAXHOP" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="NLD_Dtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}nldDtls" minOccurs="0"/&gt;
 *         &lt;element name="PRIMARYSECONDARY" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="USEHANGINGNTWKCIRCUITS" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clrDesignDtls", propOrder = {
    "ildDtls",
    "maxhop",
    "nldDtls",
    "primarysecondary",
    "usehangingntwkcircuits"
})
public class ClrDesignDtls {

    @XmlElement(name = "ILD_Dtls")
    protected IldDtls ildDtls;
    @XmlElement(name = "MAXHOP")
    protected long maxhop;
    @XmlElement(name = "NLD_Dtls")
    protected NldDtls nldDtls;
    @XmlElement(name = "PRIMARYSECONDARY")
    protected boolean primarysecondary;
    @XmlElement(name = "USEHANGINGNTWKCIRCUITS")
    protected boolean usehangingntwkcircuits;

    /**
     * Gets the value of the ildDtls property.
     * 
     * @return
     *     possible object is
     *     {@link IldDtls }
     *     
     */
    public IldDtls getILDDtls() {
        return ildDtls;
    }

    /**
     * Sets the value of the ildDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link IldDtls }
     *     
     */
    public void setILDDtls(IldDtls value) {
        this.ildDtls = value;
    }

    /**
     * Gets the value of the maxhop property.
     * 
     */
    public long getMAXHOP() {
        return maxhop;
    }

    /**
     * Sets the value of the maxhop property.
     * 
     */
    public void setMAXHOP(long value) {
        this.maxhop = value;
    }

    /**
     * Gets the value of the nldDtls property.
     * 
     * @return
     *     possible object is
     *     {@link NldDtls }
     *     
     */
    public NldDtls getNLDDtls() {
        return nldDtls;
    }

    /**
     * Sets the value of the nldDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link NldDtls }
     *     
     */
    public void setNLDDtls(NldDtls value) {
        this.nldDtls = value;
    }

    /**
     * Gets the value of the primarysecondary property.
     * 
     */
    public boolean isPRIMARYSECONDARY() {
        return primarysecondary;
    }

    /**
     * Sets the value of the primarysecondary property.
     * 
     */
    public void setPRIMARYSECONDARY(boolean value) {
        this.primarysecondary = value;
    }

    /**
     * Gets the value of the usehangingntwkcircuits property.
     * 
     */
    public boolean isUSEHANGINGNTWKCIRCUITS() {
        return usehangingntwkcircuits;
    }

    /**
     * Sets the value of the usehangingntwkcircuits property.
     * 
     */
    public void setUSEHANGINGNTWKCIRCUITS(boolean value) {
        this.usehangingntwkcircuits = value;
    }

}
