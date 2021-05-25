
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vpnIllPortAttributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vpnIllPortAttributes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Attribute" type="{http://com.tatacommunications.cramer.reverseisc.ws}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vpnIllPortAttributes", propOrder = {
    "attribute"
})
public class VpnIllPortAttributes {

    @XmlElement(name = "Attribute")
    protected List<Attribute> attribute;

	public List<Attribute> getAttribute() {
		return attribute;
	}

	public void setAttribute(List<Attribute> attribute) {
		this.attribute = attribute;
	}

   

}
