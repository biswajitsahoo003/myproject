
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for relationShip.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="relationShip">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="ISSUED"/>
 *     &lt;enumeration value="MARKEDFORDELETE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "relationShip")
@XmlEnum
public enum RelationShip {

    ACTIVE,
    ISSUED,
    MARKEDFORDELETE;

    public String value() {
        return name();
    }

    public static RelationShip fromValue(String v) {
        return valueOf(v);
    }

}
