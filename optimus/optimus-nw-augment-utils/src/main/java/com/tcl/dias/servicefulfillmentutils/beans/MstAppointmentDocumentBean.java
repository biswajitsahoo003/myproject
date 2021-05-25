package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.networkaugment.entity.entities.MstAppointmentDocuments;

/**
 * Bean class for MstAppointmentDocument.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MstAppointmentDocumentBean {

    private Integer id;
    private String documentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public static MstAppointmentDocumentBean mapToBean(MstAppointmentDocuments mstAppointmentDocuments) {
        MstAppointmentDocumentBean mstAppointmentDocumentBean = new MstAppointmentDocumentBean();
        mstAppointmentDocumentBean.setId(mstAppointmentDocuments.getId());
        mstAppointmentDocumentBean.setDocumentName(mstAppointmentDocuments.getDocumentName());
        return mstAppointmentDocumentBean;
    }

    @Override
    public String toString() {
        return "MstAppointmentDocumentBean{" +
                "id=" + id +
                ", documentName='" + documentName + '\'' +
                '}';
    }
}
