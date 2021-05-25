//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.08.12 at 10:48:57 AM IST 
//


package com.tcl.dias.servicehandover.ipc.beans.createorder;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateOrderBO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateOrderBO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="req_Order" type="{http://InstaCCLibrary/BSSEBOs}CreateOrderRequestBO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateOrderBO", propOrder = {
    "reqOrder"
})
public class CreateOrderBO {

    @XmlElement(name = "req_Order")
    protected List<CreateOrderRequestBO> reqOrder;

    /**
     * Gets the value of the reqOrder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reqOrder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReqOrder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreateOrderRequestBO }
     * 
     * 
     */
    public List<CreateOrderRequestBO> getReqOrder() {
        if (reqOrder == null) {
            reqOrder = new ArrayList<CreateOrderRequestBO>();
        }
        return this.reqOrder;
    }

}
