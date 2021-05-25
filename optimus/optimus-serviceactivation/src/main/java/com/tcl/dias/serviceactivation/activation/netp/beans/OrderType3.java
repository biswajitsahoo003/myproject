
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
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="HOT_UPGRADE"/>
 *     &lt;enumeration value="HOT_DOWNGRADE"/>
 *     &lt;enumeration value="TERMINATE"/>
 *     &lt;enumeration value="PARALLEL_UPGRADE"/>
 *     &lt;enumeration value="TERMINATION_ROLLBACK"/>
 *     &lt;enumeration value="IMPORT_NEW"/>
 *     &lt;enumeration value="IMPORT_TERMINATE"/>
 *     &lt;enumeration value="IMPORT_MODIFY"/>
 *     &lt;enumeration value="POSTNCCM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderType", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd")
@XmlEnum
public enum OrderType3 {

    NEW,
    HOT_UPGRADE,
    HOT_DOWNGRADE,
    TERMINATE,
    PARALLEL_UPGRADE,
    TERMINATION_ROLLBACK,
    IMPORT_NEW,
    IMPORT_TERMINATE,
    IMPORT_MODIFY,
    POSTNCCM;

    public String value() {
        return name();
    }

    public static OrderType3 fromValue(String v) {
        return valueOf(v);
    }

}
