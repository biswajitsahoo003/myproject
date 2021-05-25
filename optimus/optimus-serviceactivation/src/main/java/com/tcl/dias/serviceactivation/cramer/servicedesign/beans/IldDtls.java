
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ildDtls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ildDtls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CABLE_STATION" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="EXTENSION_CABLE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="MAIN_CABLE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="STATIC_PROTECTION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ildDtls", propOrder = {
    "cablestation",
    "extensioncable",
    "maincable",
    "staticprotection"
})
public class IldDtls {

    @XmlElement(name = "CABLE_STATION")
    protected List<String> cablestation;
    @XmlElement(name = "EXTENSION_CABLE")
    protected List<String> extensioncable;
    @XmlElement(name = "MAIN_CABLE")
    protected List<String> maincable;
    @XmlElement(name = "STATIC_PROTECTION")
    protected String staticprotection;

    /**
     * Gets the value of the cablestation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cablestation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCABLESTATION().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCABLESTATION() {
        if (cablestation == null) {
            cablestation = new ArrayList<String>();
        }
        return this.cablestation;
    }

    /**
     * Gets the value of the extensioncable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extensioncable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEXTENSIONCABLE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEXTENSIONCABLE() {
        if (extensioncable == null) {
            extensioncable = new ArrayList<String>();
        }
        return this.extensioncable;
    }

    /**
     * Gets the value of the maincable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the maincable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMAINCABLE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMAINCABLE() {
        if (maincable == null) {
            maincable = new ArrayList<String>();
        }
        return this.maincable;
    }

    /**
     * Gets the value of the staticprotection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATICPROTECTION() {
        return staticprotection;
    }

    /**
     * Sets the value of the staticprotection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATICPROTECTION(String value) {
        this.staticprotection = value;
    }

}
