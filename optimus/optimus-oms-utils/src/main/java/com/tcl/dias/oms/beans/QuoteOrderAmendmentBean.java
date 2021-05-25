package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

public class QuoteOrderAmendmentBean {

   boolean isQuoteCreated;
   List<CheckAmendmentQuoteBean> checkQuoteBeanForAmendmentList = new ArrayList<>();

    public boolean isQuoteCreated() {
        return isQuoteCreated;
    }

    public void setQuoteCreated(boolean quoteCreated) {
        isQuoteCreated = quoteCreated;
    }

    public List<CheckAmendmentQuoteBean> getCheckQuoteBeanForAmendmentList() {
        return checkQuoteBeanForAmendmentList;
    }

    public void setCheckQuoteBeanForAmendmentList(List<CheckAmendmentQuoteBean> checkQuoteBeanForAmendmentList) {
        this.checkQuoteBeanForAmendmentList = checkQuoteBeanForAmendmentList;
    }

    @Override
    public String toString() {
        return "IsQuoteCreatedForOABean{" +
                "isQuoteCreated=" + isQuoteCreated +
                ", checkQuoteBeanForAmendmentList=" + checkQuoteBeanForAmendmentList +
                '}';
    }
}
