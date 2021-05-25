package com.tcl.dias.servicefulfillmentutils.beans.klm;

import java.util.List;

public class ReservedTimeSlotObjects {
    private String nniId;
    private List<FrameRateIdentifier> frameRateIdentifier;

    public String getNniId() {
        return nniId;
    }

    public void setNniId(String nniId) {
        this.nniId = nniId;
    }

    public List<FrameRateIdentifier> getFrameRateIdentifier() {
        return frameRateIdentifier;
    }

    public void setFrameRateIdentifier(List<FrameRateIdentifier> frameRateIdentifier) {
        this.frameRateIdentifier = frameRateIdentifier;
    }
}
