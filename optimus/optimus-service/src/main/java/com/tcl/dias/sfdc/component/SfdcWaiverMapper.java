package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.beans.TerminationWaiverBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.EtcRecordBean;
import com.tcl.dias.sfdc.bean.SfdcWaiverRequest;
import com.tcl.dias.sfdc.bean.SfdcWaiverRequestBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SfdcWaiverMapper implements ISfdcMapper {
    @Override
    public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {

        SfdcWaiverRequest sfdcWaiverRequest = new SfdcWaiverRequest();
        if (omsInput != null) {

            TerminationWaiverBean terminationWaiverBean = (TerminationWaiverBean) Utils.convertJsonToObject(omsInput,
                    TerminationWaiverBean.class);

            List<SfdcWaiverRequestBean> sfdcWaiverRequestBeanList = new ArrayList<>();
          //  for (TerminationWaiverBean terminationWaiverBean : terminationWaiverBeanList) {
                SfdcWaiverRequestBean sfdcWaiverRequestBean = new SfdcWaiverRequestBean();
                sfdcWaiverRequestBean.setRecordTypeName(terminationWaiverBean.getRecordTypeName());
                sfdcWaiverRequestBean.setApproverEmail(terminationWaiverBean.getApproverEmail());
                sfdcWaiverRequestBean.setOpportunityId(terminationWaiverBean.getOpportunityId());
                EtcRecordBean etcRecord = constructEtcRecord(terminationWaiverBean);
                sfdcWaiverRequestBean.setEtcRecord(etcRecord);
                sfdcWaiverRequestBeanList.add(sfdcWaiverRequestBean);
          //  }

            sfdcWaiverRequest.setCreateRequestV1(sfdcWaiverRequestBeanList);
        }


        return sfdcWaiverRequest;
    }


    private EtcRecordBean constructEtcRecord(TerminationWaiverBean terminationWaiverBean) {
        EtcRecordBean etcRecordBean = new EtcRecordBean();
    //    etcRecordBean.setOpportunityNameC(terminationWaiverBean.getEtcRecord().getOpportunityName());
        etcRecordBean.setEtcWaiverTypeProposedBySalesC(terminationWaiverBean.getEtcRecord().getEtcWaiverTypeProposedBySales());
        etcRecordBean.setEtcValueProposedBySalesC(terminationWaiverBean.getEtcRecord().getEtcValueProposedBySales());
        etcRecordBean.setEtcWaiverBasedOnEtcPolicyC(terminationWaiverBean.getEtcRecord().getEtcWaiverBasedOnEtcPolicy());
        etcRecordBean.setAgreeWithWaiverProposedBySalesC(terminationWaiverBean.getEtcRecord().getAgreeWithWaiverProposedBySales());
        etcRecordBean.setFinalEtcAmountToBeInvoicedC(terminationWaiverBean.getEtcRecord().getFinalEtcAmountToBeInvoiced());
        etcRecordBean.setLmProviderPayoutC(terminationWaiverBean.getEtcRecord().getLmProviderPayout());
        etcRecordBean.setCorrespondingCircuitIdC(terminationWaiverBean.getEtcRecord().getCompensatoryDetails());
        etcRecordBean.setCommentsC(terminationWaiverBean.getEtcRecord().getComments());

        return etcRecordBean;
    }
}
