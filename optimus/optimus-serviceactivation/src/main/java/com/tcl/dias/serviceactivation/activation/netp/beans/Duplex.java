
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Duplex.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Duplex">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FULL"/>
 *     &lt;enumeration value="HALF"/>
 *     &lt;enumeration value="AUTO"/>
 *     &lt;enumeration value="NOT_APPLICABLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Duplex", namespace = "http://www.tcl.com/2011/11/ace/common/xsd")
@XmlEnum
public enum Duplex {

    FULL,
    HALF,
    AUTO,
    NOT_APPLICABLE;

    public String value() {
        return name();
    }

    public static Duplex fromValue(String v) {
        return valueOf(v);
    }

}
