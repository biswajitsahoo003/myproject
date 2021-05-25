
package com.tcl.dias.serviceactivation.activation.netp.beans.response;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionPerformed.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActionPerformed"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SYNC"/&gt;
 *     &lt;enumeration value="CONFIG"/&gt;
 *     &lt;enumeration value="SAVE"/&gt;
 *     &lt;enumeration value="CANCEL"/&gt;
 *     &lt;enumeration value="CPE_PROV_CONFIG"/&gt;
 *     &lt;enumeration value="PE_PROV_CONFIG"/&gt;
 *     &lt;enumeration value="CPE_PROV_SAVE"/&gt;
 *     &lt;enumeration value="PE_PROV_SAVE"/&gt;
 *     &lt;enumeration value="MODIFY_SAVE"/&gt;
 *     &lt;enumeration value="MODIFY_CONFIG"/&gt;
 *     &lt;enumeration value="BLOCK"/&gt;
 *     &lt;enumeration value="UNBLOCK"/&gt;
 *     &lt;enumeration value="TERMINATE"/&gt;
 *     &lt;enumeration value="POST_VALIDATION"/&gt;
 *     &lt;enumeration value="PREVIEW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ActionPerformed", namespace = "http://www.tcl.com/2011/11/ace/common/xsd")
@XmlEnum
public enum ActionPerformed {

    SYNC,
    CONFIG,
    SAVE,
    CANCEL,
    CPE_PROV_CONFIG,
    PE_PROV_CONFIG,
    CPE_PROV_SAVE,
    PE_PROV_SAVE,
    MODIFY_SAVE,
    MODIFY_CONFIG,
    BLOCK,
    UNBLOCK,
    TERMINATE,
    POST_VALIDATION,
    PREVIEW;

    public String value() {
        return name();
    }

    public static ActionPerformed fromValue(String v) {
        return valueOf(v);
    }

}
