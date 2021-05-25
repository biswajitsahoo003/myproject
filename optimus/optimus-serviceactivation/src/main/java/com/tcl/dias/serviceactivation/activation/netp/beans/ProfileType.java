
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProfileType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProfileType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SIGNAL_SNCP"/>
 *     &lt;enumeration value="DTL"/>
 *     &lt;enumeration value="EXPLICIT"/>
 *     &lt;enumeration value="SINGLE_NODE_SNC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProfileType", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd")
@XmlEnum
public enum ProfileType {

    SIGNAL_SNCP,
    DTL,
    EXPLICIT,
    SINGLE_NODE_SNC;

    public String value() {
        return name();
    }

    public static ProfileType fromValue(String v) {
        return valueOf(v);
    }

}
