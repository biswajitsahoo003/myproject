package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.beans.TerminationWaiverBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.EtcRecordBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateWaiverRequest;
import com.tcl.dias.sfdc.bean.SfdcWaiverRequestBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SfdcUpdateWaiverMapper implements ISfdcMapper {

    @Override
    public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {

        SfdcUpdateWaiverRequest sfdcUpdateWaiverRequest = new SfdcUpdateWaiverRequest();
        if (omsInput != null) {

            TerminationWaiverBean terminationWaiverBean = (TerminationWaiverBean) Utils.convertJsonToObject(omsInput,
                    TerminationWaiverBean.class);

            List<SfdcWaiverRequestBean> sfdcWaiverRequestBeanList = new ArrayList<>();
         //   for (TerminationWaiverBean terminationWaiverBean : terminationWaiverBeanList) {
                SfdcWaiverRequestBean sfdcWaiverRequestBean = new SfdcWaiverRequestBean();
             //   sfdcWaiverRequestBean.setRecordTypeName(terminationWaiverBean.getRecordTypeName());
             //   sfdcWaiverRequestBean.setApproverEmail(terminationWaiverBean.getApproverEmail());
                EtcRecordBean etcRecord = constructEtcRecord(terminationWaiverBean);
                sfdcWaiverRequestBean.setEtcRecord(etcRecord);
                sfdcWaiverRequestBeanList.add(sfdcWaiverRequestBean);
          //  }

            sfdcUpdateWaiverRequest.setUpdateRequestV1(sfdcWaiverRequestBeanList);
        }


        return sfdcUpdateWaiverRequest;

    }


    private EtcRecordBean constructEtcRecord(TerminationWaiverBean terminationWaiverBean) {
        EtcRecordBean etcRecordBean = new EtcRecordBean();
        etcRecordBean.setId(terminationWaiverBean.getEtcRecord().getId());
        etcRecordBean.setOpportunityNameC(terminationWaiverBean.getEtcRecord().getOpportunityName());
        etcRecordBean.setEtcWaiverTypeProposedBySalesC(terminationWaiverBean.getEtcRecord().getEtcWaiverTypeProposedBySales());
        etcRecordBean.setEtcValueProposedBySalesC(terminationWaiverBean.getEtcRecord().getEtcValueProposedBySales());
        etcRecordBean.setEtcWaiverBasedOnEtcPolicyC(terminationWaiverBean.getEtcRecord().getEtcWaiverBasedOnEtcPolicy());
        etcRecordBean.setAgreeWithWaiverProposedBySalesC(terminationWaiverBean.getEtcRecord().getAgreeWithWaiverProposedBySales());
        etcRecordBean.setFinalEtcAmountToBeInvoicedC(terminationWaiverBean.getEtcRecord().getFinalEtcAmountToBeInvoiced());
        etcRecordBean.setLmProviderPayoutC(terminationWaiverBean.getEtcRecord().getLmProviderPayout());
        etcRecordBean.setCommentsC(terminationWaiverBean.getEtcRecord().getComments());

        return etcRecordBean;
    }


}
