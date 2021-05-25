
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for finalRelationShip.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="finalRelationShip">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="ISSUED"/>
 *     &lt;enumeration value="MARKEDFORDELETE"/>
 *     &lt;enumeration value="TERMINATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "finalRelationShip")
@XmlEnum
public enum FinalRelationShip {

    ACTIVE,
    ISSUED,
    MARKEDFORDELETE,
    TERMINATE;

    public String value() {
        return name();
    }

    public static FinalRelationShip fromValue(String v) {
        return valueOf(v);
    }

}
