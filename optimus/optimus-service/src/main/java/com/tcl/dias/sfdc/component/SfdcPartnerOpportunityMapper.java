package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerCommonWrapInput;
import com.tcl.dias.sfdc.bean.SfdcPartnerOpportunityRequest;
import com.tcl.dias.sfdc.bean.SfdcOptyCreationRequestBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerOpportunityWrap;
import com.tcl.dias.sfdc.constants.SfdcConstants;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.tcl.dias.common.constants.CommonConstants.COMMA;

/**
 *
 * This file contains the SfdcPartnerEntityMapper.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcPartnerOpportunityMapper implements ISfdcMapper {

    @Override
    public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
        SfdcPartnerOpportunityRequest opportunityRequest = null;
        SfdcOptyCreationRequestBean sfdcfinalOptyReqeust=null;
        if (StringUtils.isNotBlank(omsInput)) {
            OpportunityBean opportunityBean = (OpportunityBean) Utils.convertJsonToObject(omsInput, OpportunityBean.class);
            opportunityRequest = new SfdcPartnerOpportunityRequest();
            opportunityRequest.setSfdcPartnerCommonWrapInput(constructSfdcCommonWrapper(opportunityBean));
            List<SfdcPartnerOpportunityWrap> sfdcPartnerOpportunityWraps=new ArrayList<>();
            sfdcPartnerOpportunityWraps.add(constructPartnerOpportunityWrap(opportunityBean));
            opportunityRequest.setSfdcPartnerOpportunityWrap(sfdcPartnerOpportunityWraps);
//            if(opportunityBean.getSubType().equalsIgnoreCase("New Order")){
//            sfdcfinalOptyReqeust=new SfdcOptyCreationRequestBean();
//            sfdcfinalOptyReqeust.setSfdcPartnerOpportunityRequest(opportunityRequest);
//            sfdcfinalOptyReqeust.setProductType(opportunityBean.getSelectProductType());
//            sfdcfinalOptyReqeust.setSource("NGP");
//            sfdcfinalOptyReqeust.setIsRetry("N");
//            sfdcfinalOptyReqeust.setOperation("Oppty Creation");
//            sfdcfinalOptyReqeust.setBmId("0");
//            sfdcfinalOptyReqeust.setServiceType(opportunityBean.getSelectProductType());
//            sfdcfinalOptyReqeust.setOptyQuoteCode(opportunityBean.getPortalTransactionId());
//            return sfdcfinalOptyReqeust;
//            }

        }
        return opportunityRequest;
    }
    /**
     * constructSfdcCommonWrapper
     *
     * @param opportunityBean
     * @return SfdcPartnerCommonWrapInput
     */
    private SfdcPartnerCommonWrapInput constructSfdcCommonWrapper(OpportunityBean opportunityBean){
        SfdcPartnerCommonWrapInput commonWrapInput = new SfdcPartnerCommonWrapInput();
        commonWrapInput.setAccountIdNGP(opportunityBean.getAccountId());
        if(Objects.nonNull(opportunityBean.getOpportunityClassification()) &&
                SfdcConstants.SELL_THROUGH.toString().equalsIgnoreCase(opportunityBean.getOpportunityClassification())) {
            commonWrapInput.setCustomerPartnerLegalEntityId(opportunityBean.getPartnerContractingId());
            commonWrapInput.setPartnerManagedCustomer(opportunityBean.getEndCustomerCuid());
        }else{
            commonWrapInput.setPartnerLegalEntity(opportunityBean.getPartnerContractingId());
        }
        commonWrapInput.setCurrencyNGP(opportunityBean.getCurrencyIsoCode());
        commonWrapInput.setModifyScenarioFlag(CommonConstants.N);
        commonWrapInput.setCustomerSignedByNGP(opportunityBean.getOwnerName() + COMMA + opportunityBean.getOwnerEmail());
        commonWrapInput.setAccountManagerNGP(opportunityBean.getOwnerName());
        commonWrapInput.setOperation("Oppty Creation");
        commonWrapInput.setProductType(opportunityBean.getSelectProductType());
        commonWrapInput.setMigrationsourcesystem("OPTIMUS");
        return commonWrapInput;
    }
    /**
     * constructPartnerOpportunityWrap
     *
     * @param opportunityBean
     *
     *  @return SfdcPartnerOpportunityWrap
     */
    private SfdcPartnerOpportunityWrap constructPartnerOpportunityWrap(OpportunityBean opportunityBean) {
        SfdcPartnerOpportunityWrap opportunityWrap = new SfdcPartnerOpportunityWrap();
        opportunityWrap.setTermOfMonthsNGP(Integer.parseInt(opportunityBean.getTermOfMonths()));
        opportunityWrap.setEndCustomerNameNGP(opportunityBean.getEndCustomerName());
        opportunityWrap.setATTR2(opportunityBean.getDealRegistrationDate());
        if("Sell With".equalsIgnoreCase(opportunityBean.getOpportunityClassification())){
            opportunityWrap.setPartnerNameNGP(opportunityBean.getPartnerContractingId());
        }
        opportunityWrap.setOpportunityCategoryNGP(opportunityBean.getOpportunityClassification());
        opportunityWrap.setPortalTransactionIdNGP(opportunityBean.getPortalTransactionId());
        opportunityWrap.setServiceTypeNGP(opportunityBean.getSelectProductType());
        /**For Deal Registration CampaignId should go in place of CampaignName*/
        opportunityWrap.setCampaignId(opportunityBean.getCampaignId());
        opportunityWrap.setSelectProductTypeNGP(opportunityBean.getSelectProductType());
        Integer termInDays=Integer.parseInt(opportunityBean.getTermOfMonths())*30;
        opportunityWrap.setTermInDaysForDemoNewNGP(termInDays.toString());
        opportunityWrap.setCustomerContractingEntityNGP(opportunityBean.getPartnerContractingId());
        opportunityWrap.setTypeNGP(opportunityBean.getSubType());
        opportunityWrap.setOpportunityNameNGP(opportunityBean.getName());
        opportunityWrap.setDescriptionNGP(opportunityBean.getDescription());
        opportunityWrap.setDealRegistrationRequired(opportunityBean.getDealRegistrationRequired());
        opportunityWrap.setProductMRCNGP(opportunityBean.getPartnerOptyMRC());
        opportunityWrap.setProductNRCNGP(opportunityBean.getPartnerOptyNRC());
        opportunityWrap.setPsamEmailID(opportunityBean.getPsamEmail());
        return opportunityWrap;

    }


    private Long addTime(Integer noTermMonths) {
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        endDate.add(Calendar.MONTH, noTermMonths);
        return daysBetween(startDate,endDate);
    }

    private static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

}
