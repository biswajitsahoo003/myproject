package com.tcl.dias.oms.beans;

import com.tcl.dias.common.beans.Signers;

import java.util.ArrayList;
import java.util.List;

public class SignerListBean {

    List<Signers> signers=new ArrayList<>();

    public List<Signers> getSigners() {
        return signers;
    }

    public void setSigners(List<Signers> signers) {
        this.signers = signers;
    }
}
