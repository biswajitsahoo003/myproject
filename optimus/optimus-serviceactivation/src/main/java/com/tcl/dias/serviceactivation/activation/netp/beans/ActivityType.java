
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActivityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SA"/>
 *     &lt;enumeration value="NSA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActivityType", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd")
@XmlEnum
public enum ActivityType {

    SA,
    NSA;

    public String value() {
        return name();
    }

    public static ActivityType fromValue(String v) {
        return valueOf(v);
    }

}
