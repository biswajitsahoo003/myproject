package com.tcl.dias.sfdc.component;

import com.tcl.dias.sfdc.constants.SfdcConstants;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcManagerialOpportunityBean;
import com.tcl.dias.sfdc.bean.SfdcOpportunityBean;
import com.tcl.dias.sfdc.bean.SfdcOpportunityRequest;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import java.util.Objects;

/**
 * This file contains the SfdcOpportunityMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcOpportunityMapper implements ISfdcMapper {
	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcOpportunityRequest opportunityRequest = null;
		if (omsInput != null) {

			OpportunityBean opportunityBean = (OpportunityBean) Utils.convertJsonToObject(omsInput,
					OpportunityBean.class);
			opportunityRequest = new SfdcOpportunityRequest();
			SfdcManagerialOpportunityBean opportunityInfo = new SfdcManagerialOpportunityBean();
			opportunityInfo.setAccountCUID(opportunityBean.getAccountCuid());
			opportunityInfo.setCommunicationRecipient(opportunityBean.getCommunicationRecipent());
			opportunityInfo.setCustomerContractingEntity(opportunityBean.getCustomerContractingId());
			opportunityInfo.setProgramManagerName(opportunityBean.getProgramManagersName());
			opportunityInfo.setParentOpportunityName(opportunityBean.getParentOpportunityName());
			SfdcOpportunityBean sdfOpportunity = constructSfdcOpportunity(opportunityBean);
			opportunityInfo.setOpportunity(sdfOpportunity);
			opportunityInfo.setParentTerminationOpportunityName(opportunityBean.getParentTerminationOpportunityName());
			opportunityInfo.setOwnerName(opportunityBean.getOwnerName());
			if (Objects.nonNull(opportunityBean.getPartnerContractingId())) {
				opportunityInfo.setPartnerCUID(opportunityBean.getPartnerContractingId());
			}
			if (Objects.nonNull(opportunityBean.getCampaignName())) {
				opportunityInfo.setCampaignName(opportunityBean.getCampaignName());
			}
			opportunityRequest.setCreateRequestV1(opportunityInfo);
			if(Objects.nonNull(opportunityBean.getQuoteToLeId())){
				opportunityRequest.setQuoteToLeId(opportunityBean.getQuoteToLeId());
			}
		}

		return opportunityRequest;
	}

	/**
	 * constructSfdcOpportunity
	 * 
	 * @param opportunityBean
	 */
	private SfdcOpportunityBean constructSfdcOpportunity(OpportunityBean opportunityBean) {
		SfdcOpportunityBean sdfOpportunity = new SfdcOpportunityBean();
		sdfOpportunity.setBillingFrequencyC(opportunityBean.getBillingFrequency());
		sdfOpportunity.setBillingMethodC(opportunityBean.getBillingMethod());
		sdfOpportunity.setCloseDate(opportunityBean.getCloseDate());
		sdfOpportunity.setCOFTypeC(opportunityBean.getCofType());
		sdfOpportunity.setCurrencyIsoCode(opportunityBean.getCurrencyIsoCode());
		sdfOpportunity.setCustomerChurnedC(opportunityBean.getCustomerChurned());
		sdfOpportunity.setILLAutoCreationC(opportunityBean.getiLLAutoCreation());
		sdfOpportunity.setIsPartnerOrderC(opportunityBean.getIsPartnerOrder());
		sdfOpportunity.setLeadTimeToRFSC(opportunityBean.getLeadTimeToRFSC());
		sdfOpportunity.setMigrationSourceSystemC(opportunityBean.getMigrationSourceSystem());
		sdfOpportunity.setAccountId(opportunityBean.getAccountId());
		sdfOpportunity.setName(opportunityBean.getName());
		sdfOpportunity.setOpportunityClassificationC(opportunityBean.getOpportunityClassification());
		sdfOpportunity.setOrderCategoryC(opportunityBean.getOrderCategory());
		sdfOpportunity.setPaymentTermC(opportunityBean.getPaymentTerm());
		sdfOpportunity.setWinLossRemarksC(opportunityBean.getWinLossRemarks());
		sdfOpportunity.setType(opportunityBean.getType());
		sdfOpportunity.setTermOfMonthsC(opportunityBean.getTermOfMonths());
		sdfOpportunity.setTATABillingEntityC(opportunityBean.getBillingEntity());
		sdfOpportunity.setSubTypeC(opportunityBean.getSubType());
		sdfOpportunity.setStageName(opportunityBean.getStageName());
		sdfOpportunity.setSelectProductTypeC(opportunityBean.getSelectProductType());
		sdfOpportunity.setReferralToPartnerC(opportunityBean.getReferralToPartner());
		sdfOpportunity.setPortalTransactionIdC(opportunityBean.getPortalTransactionId());
		if (Objects.nonNull(opportunityBean.getEndCustomerName())) {
			sdfOpportunity.setEndCustomerName(opportunityBean.getEndCustomerName());
		}
		if (Objects.nonNull(opportunityBean.getTpsOptyId())) {
			sdfOpportunity.setTpsOptyId(opportunityBean.getTpsOptyId());
		}
		if (Objects.nonNull(opportunityBean.getDescription())) {
			sdfOpportunity.setDescription(opportunityBean.getDescription());
		}
		// macd specific 
		sdfOpportunity.setEffectiveDateOfChangeC(opportunityBean.getEffectiveDateOfChange());
		sdfOpportunity.setCurrentCircuitServiceIDC(opportunityBean.getCurrentCircuitServiceId());
		sdfOpportunity.setPreviousMRCC(opportunityBean.getPreviousMRC());
		sdfOpportunity.setPreviousNRCC(opportunityBean.getPreviousNRC());
		sdfOpportunity.setCustomerMailReceivedDateC(opportunityBean.getCustomerMailReceivedDate());
		sdfOpportunity.setParentOpportunityC(opportunityBean.getParentOpportunity());
		sdfOpportunity.setNoOfParallelRunDaysC(opportunityBean.getNoOfParallelRunDays());
		sdfOpportunity.setReasonForCancellationC(opportunityBean.getReasonForTermination());
//		sdfOpportunity.setRecordType(opportunityBean.getRecordType());
		sdfOpportunity.setMainVPNIdC(opportunityBean.getMainVPNIdC());
		sdfOpportunity.setDifferentialMRCAutoC(opportunityBean.getDifferentialMRCAutoC());

		//Credit Check
		sdfOpportunity.setIccEnterpriceVoiceProductFlagC(opportunityBean.getIccEnterpriceVoiceProductFlag());
		
		// IPC Migration Order
		sdfOpportunity.setExcludeFromObReporting(opportunityBean.getExcludeFromObReporting());
		sdfOpportunity.setReasonForExcludeFromNetAcv(opportunityBean.getReasonForExcludeFromNetAcv());

		sdfOpportunity.setDummyParentTerminationOpportunity(opportunityBean.getDummyParentTerminationOpportunity());
		sdfOpportunity.setAutoCreatedTerminationOpportunity(opportunityBean.getAutoCreatedTerminationOpportunity());

		//Termination
		sdfOpportunity.setTerminationSubReasonC(opportunityBean.getTerminationSubReason());
		sdfOpportunity.setCustomerRequestedDateC(opportunityBean.getTerminationSendToTDDate());
		sdfOpportunity.setInternalCustomerC(opportunityBean.getInternalOrCustomer());
		sdfOpportunity.setCsmNoncsmC(opportunityBean.getCsmNonCsm());
		sdfOpportunity.setCommunicationRecipientC(opportunityBean.getCommunicationRecipient());
		sdfOpportunity.setHandoverToC(opportunityBean.getHandoverTo());
		sdfOpportunity.setRetentionReasonC(opportunityBean.getRetentionReason());
		sdfOpportunity.setHandoverOnC(opportunityBean.getHandoverOn());
		sdfOpportunity.setSalesAdministratorRegionC(opportunityBean.getSalesAdministratorRegion());
		sdfOpportunity.setSalesAdministratorC(opportunityBean.getSalesAdministrator());

		// for teamsdr
		if(Objects.nonNull(opportunityBean.getOpportunitySpecification())){
			sdfOpportunity.setOpportunitySpecification(opportunityBean.getOpportunitySpecification());
		}
		// for teamsdr
		if (Objects.nonNull(opportunityBean.getParentOpportunityId())){
			sdfOpportunity.setParentOptyId(opportunityBean.getParentOpportunityId());
		}
		// for teamsdr
		if(Objects.nonNull(opportunityBean.getParentServiceName())){
			sdfOpportunity.setParentServiceName(opportunityBean.getParentServiceName());
		}

		return sdfOpportunity;
	}

}
