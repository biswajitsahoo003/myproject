
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
 *     &lt;enumeration value="INTERNAL_ORDER"/>
 *     &lt;enumeration value="CUSTOMER_ORDER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderCategory", namespace = "http://www.tcl.com/2011/11/ace/common/xsd")
@XmlEnum
public enum OrderCategory {

    INTERNAL_ORDER,
    CUSTOMER_ORDER;

    public String value() {
        return name();
    }

    public static OrderCategory fromValue(String v) {
        return valueOf(v);
    }

}
