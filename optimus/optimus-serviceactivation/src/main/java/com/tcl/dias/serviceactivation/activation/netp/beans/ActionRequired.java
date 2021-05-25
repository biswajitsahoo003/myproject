
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionRequired.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActionRequired">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SYNC"/>
 *     &lt;enumeration value="CONFIG"/>
 *     &lt;enumeration value="SAVE"/>
 *     &lt;enumeration value="CANCEL"/>
 *     &lt;enumeration value="CPE_PROV_CONFIG"/>
 *     &lt;enumeration value="PE_PROV_CONFIG"/>
 *     &lt;enumeration value="CPE_PROV_SAVE"/>
 *     &lt;enumeration value="PE_PROV_SAVE"/>
 *     &lt;enumeration value="MODIFY_SAVE"/>
 *     &lt;enumeration value="MODIFY_CONFIG"/>
 *     &lt;enumeration value="BLOCK"/>
 *     &lt;enumeration value="UNBLOCK"/>
 *     &lt;enumeration value="TERMINATE"/>
 *     &lt;enumeration value="POST_VALIDATION"/>
 *     &lt;enumeration value="CPE_DISCOVER"/>
 *     &lt;enumeration value="NCCM_VERIFY_PLAN"/>
 *     &lt;enumeration value="NCCM_NSA_X_CON"/>
 *     &lt;enumeration value="NCCM_EXECUTE_PLAN"/>
 *     &lt;enumeration value="NCCM_VERIFY_SERVICE"/>
 *     &lt;enumeration value="NCCM_HOTCUT_ROLLBACK"/>
 *     &lt;enumeration value="NCCM_DELETE_OLD_CROSSCONNECT"/>
 *     &lt;enumeration value="NCCM_NETWORK_UPLOAD"/>
 *     &lt;enumeration value="NCCM_SYNC_SERVICE"/>
 *     &lt;enumeration value="POST_NCCM"/>
 *     &lt;enumeration value="TERMINATE_SAVE"/>
 *     &lt;enumeration value="PE_CONFIG_CAPTURE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActionRequired", namespace = "http://www.tcl.com/2011/11/ace/common/xsd")
@XmlEnum
public enum ActionRequired {

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
    CPE_DISCOVER,
    NCCM_VERIFY_PLAN,
    NCCM_NSA_X_CON,
    NCCM_EXECUTE_PLAN,
    NCCM_VERIFY_SERVICE,
    NCCM_HOTCUT_ROLLBACK,
    NCCM_DELETE_OLD_CROSSCONNECT,
    NCCM_NETWORK_UPLOAD,
    NCCM_SYNC_SERVICE,
    POST_NCCM,
    TERMINATE_SAVE,
    PE_CONFIG_CAPTURE;

    public String value() {
        return name();
    }

    public static ActionRequired fromValue(String v) {
        return valueOf(v);
    }

}
