
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PELNSLoopbackPath complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PELNSLoopbackPath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PERouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="PELNSLoopbackInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LoopbackInterface" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PELNSLoopbackPath", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_06", propOrder = {
    "peRouter",
    "pelnsLoopbackInterface",
    "isObjectInstanceModified"
})
public class PELNSLoopbackPath {

    @XmlElement(name = "PERouter")
    protected Router peRouter;
    @XmlElement(name = "PELNSLoopbackInterface")
    protected LoopbackInterface pelnsLoopbackInterface;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the peRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getPERouter() {
        return peRouter;
    }

    /**
     * Sets the value of the peRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setPERouter(Router value) {
        this.peRouter = value;
    }

    /**
     * Gets the value of the pelnsLoopbackInterface property.
     * 
     * @return
     *     possible object is
     *     {@link LoopbackInterface }
     *     
     */
    public LoopbackInterface getPELNSLoopbackInterface() {
        return pelnsLoopbackInterface;
    }

    /**
     * Sets the value of the pelnsLoopbackInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoopbackInterface }
     *     
     */
    public void setPELNSLoopbackInterface(LoopbackInterface value) {
        this.pelnsLoopbackInterface = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }

}
