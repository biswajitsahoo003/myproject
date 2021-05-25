
package com.tcl.dias.serviceactivation.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for objectType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="objectType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SERVICE"/>
 *     &lt;enumeration value="CIRCUIT"/>
 *     &lt;enumeration value="IOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "objectType")
@XmlEnum
public enum ObjectType {

    SERVICE,
    CIRCUIT,
    IOR;

    public String value() {
        return name();
    }

    public static ObjectType fromValue(String v) {
        return valueOf(v);
    }

}
