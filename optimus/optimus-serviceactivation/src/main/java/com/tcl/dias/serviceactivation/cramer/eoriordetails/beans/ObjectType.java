
package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for objectType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="objectType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SERVICE"/&gt;
 *     &lt;enumeration value="IOR"/&gt;
 *     &lt;enumeration value="EOR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "objectType")
@XmlEnum
public enum ObjectType {

    SERVICE,
    IOR,
    EOR;

    public String value() {
        return name();
    }

    public static ObjectType fromValue(String v) {
        return valueOf(v);
    }

}
