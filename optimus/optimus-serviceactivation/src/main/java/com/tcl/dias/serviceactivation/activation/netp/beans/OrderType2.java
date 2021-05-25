
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
 *     &lt;enumeration value="BLOCKING"/>
 *     &lt;enumeration value="UNBLOCKING"/>
 *     &lt;enumeration value="PARALLEL_DOWNGRADE"/>
 *     &lt;enumeration value="NON_COMMERCIAL_CHANGE_ORDER"/>
 *     &lt;enumeration value="CHANGE_ORDER"/>
 *     &lt;enumeration value="LM_SHIFTING_CHANGE_ORDER"/>
 *     &lt;enumeration value="POSTNCCM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderType")
@XmlEnum
public enum OrderType2 {

    NEW,
    HOT_UPGRADE,
    HOT_DOWNGRADE,
    TERMINATE,
    PARALLEL_UPGRADE,
    BLOCKING,
    UNBLOCKING,
    PARALLEL_DOWNGRADE,
    NON_COMMERCIAL_CHANGE_ORDER,
    CHANGE_ORDER,
    LM_SHIFTING_CHANGE_ORDER,
    POSTNCCM;

    public String value() {
        return name();
    }

    public static OrderType2 fromValue(String v) {
        return valueOf(v);
    }

}
