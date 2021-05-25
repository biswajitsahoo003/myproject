
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EOR_IP"/>
 *     &lt;enumeration value="EOR_TX"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderCategory", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd")
@XmlEnum
public enum OrderCategory2 {

    EOR_IP,
    EOR_TX;

    public String value() {
        return name();
    }

    public static OrderCategory2 fromValue(String v) {
        return valueOf(v);
    }

}
