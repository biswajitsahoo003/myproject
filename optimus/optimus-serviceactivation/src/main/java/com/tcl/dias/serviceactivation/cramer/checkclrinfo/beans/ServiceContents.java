
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceContents.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="serviceContents">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Tx"/>
 *     &lt;enumeration value="Access"/>
 *     &lt;enumeration value="TxAndAccess"/>
 *     &lt;enumeration value="TxAndMplsTP"/>
 *     &lt;enumeration value="AccessAndMplsTP"/>
 *     &lt;enumeration value="MplsTP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "serviceContents")
@XmlEnum
public enum ServiceContents {

    @XmlEnumValue("Tx")
    TX("Tx"),
    @XmlEnumValue("Access")
    ACCESS("Access"),
    @XmlEnumValue("TxAndAccess")
    TX_AND_ACCESS("TxAndAccess"),
    @XmlEnumValue("TxAndMplsTP")
    TX_AND_MPLS_TP("TxAndMplsTP"),
    @XmlEnumValue("AccessAndMplsTP")
    ACCESS_AND_MPLS_TP("AccessAndMplsTP"),
    @XmlEnumValue("MplsTP")
    MPLS_TP("MplsTP");
    private final String value;

    ServiceContents(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceContents fromValue(String v) {
        for (ServiceContents c: ServiceContents.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
