
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INSERT_NODE"/>
 *     &lt;enumeration value="DELETE_NODE"/>
 *     &lt;enumeration value="INSERT_CARD"/>
 *     &lt;enumeration value="DELETE_CARD"/>
 *     &lt;enumeration value="REGULARIZE_NODE"/>
 *     &lt;enumeration value="ERPS_POSTVALIDATION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderType", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd")
@XmlEnum
public enum OrderType {

    INSERT_NODE,
    DELETE_NODE,
    INSERT_CARD,
    DELETE_CARD,
    REGULARIZE_NODE,
    ERPS_POSTVALIDATION;

    public String value() {
        return name();
    }

    public static OrderType fromValue(String v) {
        return valueOf(v);
    }

}
