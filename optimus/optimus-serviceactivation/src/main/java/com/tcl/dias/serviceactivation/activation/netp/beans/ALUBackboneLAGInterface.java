
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackboneLAGInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackboneLAGInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="LAGID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="svlan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUBackboneLAGInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "lagid",
    "svlan"
})
public class ALUBackboneLAGInterface
    extends ALUBackboneRouterInterface
{

    @XmlElement(name = "LAGID")
    protected String lagid;
    protected String svlan;

    /**
     * Gets the value of the lagid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAGID() {
        return lagid;
    }

    /**
     * Sets the value of the lagid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAGID(String value) {
        this.lagid = value;
    }

    /**
     * Gets the value of the svlan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvlan() {
        return svlan;
    }

    /**
     * Sets the value of the svlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvlan(String value) {
        this.svlan = value;
    }

}
