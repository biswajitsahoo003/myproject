
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for uccServDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="uccServDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UCC_Service" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}uccService" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uccServDetails", propOrder = {
    "uccService"
})
public class UccServDetails {

    @XmlElement(name = "UCC_Service")
    protected List<UccService> uccService;

    /**
     * Gets the value of the uccService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uccService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUCCService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UccService }
     * 
     * 
     */
    public List<UccService> getUCCService() {
        if (uccService == null) {
            uccService = new ArrayList<UccService>();
        }
        return this.uccService;
    }

}
