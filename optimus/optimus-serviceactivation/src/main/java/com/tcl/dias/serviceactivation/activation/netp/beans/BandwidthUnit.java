
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BandwidthUnit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BandwidthUnit">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="KBPS"/>
 *     &lt;enumeration value="MBPS"/>
 *     &lt;enumeration value="GBPS"/>
 *     &lt;enumeration value="BPS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BandwidthUnit", namespace = "http://www.tcl.com/2011/11/ace/common/xsd")
@XmlEnum
public enum BandwidthUnit {

    KBPS,
    MBPS,
    GBPS,
    BPS;

    public String value() {
        return name();
    }

    public static BandwidthUnit fromValue(String v) {
        return valueOf(v);
    }

}
