package com.tcl.dias.sfdc.component;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.BundledSfdcOppurtunityRequest;
import com.tcl.dias.common.sfdc.bean.Opportunity;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcBundleOpportunityBean;
import com.tcl.dias.sfdc.bean.SfdcManagerialBundleOpportunityBean;
import com.tcl.dias.sfdc.bean.SfdcOpportunityBundleRequest;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 *
 */

@Component
public class SfdcBundleOpportunityMapper implements ISfdcMapper {

	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcOpportunityBundleRequest opportunityRequest = null;
		if (omsInput != null) {

			BundledSfdcOppurtunityRequest opportunityBean = (BundledSfdcOppurtunityRequest) Utils
					.convertJsonToObject(omsInput, BundledSfdcOppurtunityRequest.class);
			opportunityRequest = new SfdcOpportunityBundleRequest();
			SfdcManagerialBundleOpportunityBean opportunityInfo = new SfdcManagerialBundleOpportunityBean();
			opportunityInfo.setAccountId(opportunityBean.getCreateRequestV1().getAccountCuid());
			opportunityInfo
					.setCustomerContractingEntity(opportunityBean.getCreateRequestV1().getCustomerContractEntity());
			opportunityInfo.setOwnerName(opportunityBean.getCreateRequestV1().getOwnerName());
			opportunityInfo.setProductBundleName(opportunityBean.getCreateRequestV1().getProductBundleName());
			SfdcBundleOpportunityBean sfdcOpportunity = constructBundleOpportunity(
					opportunityBean.getCreateRequestV1().getOpportunity());
			opportunityInfo.setOpportunity(sfdcOpportunity);
			opportunityRequest.setCreateRequestV1(opportunityInfo);

		}

		return opportunityRequest;
	}

	private SfdcBundleOpportunityBean constructBundleOpportunity(Opportunity opportunity) {
		SfdcBundleOpportunityBean bean = new SfdcBundleOpportunityBean();
		bean.setName(opportunity.getName());
		bean.setDescription(opportunity.getDescription());
		bean.setReferralToPartnerC(opportunity.getReferralToPartnerC());
		bean.setIllAutoCreationC(opportunity.getIllAutoCreationC());
		bean.setOrderCategoryC(opportunity.getOrderCategoryC());
		bean.setType(opportunity.getType());
		bean.setSubTypeC(opportunity.getSubTypeC());
		bean.setPortalTransactionIdC(opportunity.getPortalTransactionIdC());
		bean.setOpportunityClassificationC(opportunity.getOpportunityClassificationC());
		bean.setTataBillingEntityC(opportunity.getTataBillingEntityC());
		bean.setTermsOfMonthsC(opportunity.getTermsOfMonthsC());
		bean.setCloseDate(opportunity.getCloseDate());
		bean.setCurrencyIsoCode(opportunity.getCurrencyIsoCode());
		bean.setStageName(opportunity.getStageName());
		bean.setLeadTimeToRfsc(opportunity.getLeadTimeToRfsc());
		bean.setCustomerChurnedC(opportunity.getCustomerChurnedC());
		bean.setBillingFrequencyC(opportunity.getBillingFrequencyC());
		bean.setBillingMethodC(opportunity.getBillingMethodC());
		bean.setPaymentTermC(opportunity.getPaymentTermC());
		bean.setSelectProductTypeC(opportunity.getSelectProductTypeC());
		bean.setCofTypeC(opportunity.getCofTypeC());
		bean.setMigrationSourceSystemC(opportunity.getMigrationSourceSystemC());
		bean.setCurrentCircuitServiceIdC(opportunity.getCurrentCircuitServiceIdC());
		bean.setEffectiveDateOfChangeC(opportunity.getEffectiveDateOfChangeC());
		bean.setPreviousMrcC(opportunity.getPreviousMrcC());
		bean.setPreviousNrcC(opportunity.getPreviousNrcC());
		bean.setBundledProductOneC(opportunity.getBundledProductOneC());
		bean.setBundledOrderTypeOneC(opportunity.getBundledOrderTypeOneC());
		bean.setBundledSubOrderTypeOneC(opportunity.getBundledSubOrderTypeOneC());
		bean.setBundledProductTwoC(opportunity.getBundledProductTwoC());
		bean.setBundledOrderTypeTwoC(opportunity.getBundledOrderTypeTwoC());
		bean.setBundledSubOrderTypeTwoC(opportunity.getBundledSubOrderTypeTwoC());
		bean.setBundledProductThreeC(opportunity.getBundledProductThreeC());
		bean.setBundledOrderTypeThreeC(opportunity.getBundledOrderTypeThreeC());
		bean.setBundledSubOrderTypeThreeC(opportunity.getBundledSubOrderTypeThreeC());
		bean.setBundledProductFourC(opportunity.getBundledProductFourC());
		bean.setBundledOrderTypeFourC(opportunity.getBundledOrderTypeFourC());
		bean.setBundledSubOrderTypeFourC(opportunity.getBundledSubOrderTypeFourC());
		bean.setOpportunitySpecificationC(opportunity.getOpportunitySpecificationC());
		return bean;
	}

}
