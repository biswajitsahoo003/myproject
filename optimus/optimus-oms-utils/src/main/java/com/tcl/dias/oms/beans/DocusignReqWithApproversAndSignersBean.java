package com.tcl.dias.oms.beans;

public class DocusignReqWithApproversAndSignersBean {

    ApproverListBean approverListBean = new ApproverListBean();
    SignerListBean signerListBean = new SignerListBean();

    public ApproverListBean getApproverListBean() {
        return approverListBean;
    }

    public void setApproverListBean(ApproverListBean approverListBean) {
        this.approverListBean = approverListBean;
    }

    public SignerListBean getSignerListBean() {
        return signerListBean;
    }

    public void setSignerListBean(SignerListBean signerListBean) {
        this.signerListBean = signerListBean;
    }
}
