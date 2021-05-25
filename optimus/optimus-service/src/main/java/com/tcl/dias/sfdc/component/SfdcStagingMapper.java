package com.tcl.dias.sfdc.component;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdStagingBean;
import com.tcl.dias.sfdc.bean.SfdcStaging;
import com.tcl.dias.sfdc.bean.SfdcStagingOpportunityBean;
import com.tcl.dias.sfdc.constants.SfdcConstants;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import java.util.Objects;

/**
 * This file contains the SfdcStagingMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component
public class SfdcStagingMapper implements ISfdcMapper {

	/**
	 * transfortToSfdcRequest
	 * 
	 * @param omsInput
	 * @return
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdStagingBean request = new SfdStagingBean();

		UpdateOpportunityStage stage = (UpdateOpportunityStage) Utils.convertJsonToObject(omsInput,
				UpdateOpportunityStage.class);
		SfdcStagingOpportunityBean updateOpportunityBean = new SfdcStagingOpportunityBean();
		SfdcStaging updateOpportunity = new SfdcStaging();
		updateOpportunity.setName(stage.getName());
		updateOpportunity.setCopfIdC(stage.getCopfIdC());
		updateOpportunity.setType(stage.getType());
		updateOpportunity.setSubTypeC(stage.getSub_Type__c());
		updateOpportunity.setBillingFrequencyC(stage.getBillingFrequency());
		updateOpportunity.setBillingMethodC(stage.getBillingMethod());
		updateOpportunity.setCloseDate(stage.getCloseDate());
		updateOpportunity.setCurrencyIsoCode(stage.getCurrencyIsoCode());
		updateOpportunity.setLeadTimeToRFSC(stage.getLeadTimeToRFSC());
		updateOpportunity.setPaymentTermC(stage.getPaymentTerm());
		updateOpportunity.settATABillingEntityC(stage.getBillingEntity());
		updateOpportunity.setTermOfMonthsC(stage.getTermOfMonths());
		updateOpportunityBean.setOpportunity(updateOpportunity);
		updateOpportunity.setOpportunityIDC(stage.getOpportunityId());
		updateOpportunity.setStageName(stage.getStageName());
		updateOpportunity.setCofSignedDate(stage.getCofSignedDate());
		updateOpportunity.setPreparedBy(stage.getDsPreparedBy());
		updateOpportunity.setSalesAdmin(stage.getSalesAdministrator());
		updateOpportunity.setWinReasons(stage.getWinReason());
		updateOpportunity.setSalesAdminRegion(stage.getSalesAdministratorRegion());
		updateOpportunity.setOwnerName(stage.getOwnerName());
		if (Objects.nonNull(stage.getEndCustomerName())) {
			updateOpportunity.setEndCustomerName(stage.getEndCustomerName());
		}

		//SFDC Update Opportunity CLOSED-DROPPED
		updateOpportunity.setWinLossDropKeyReasonC(stage.getWinLossDropKeyReason());
		updateOpportunity.setDropReasonsC(stage.getDropReasons());
		updateOpportunity.setDroppingReasonC(stage.getDroppingReason());
		updateOpportunity.setStatusCreditControl(stage.getStatusOfCreditControl());
		updateOpportunity.setLossReasonsC(stage.getLossReasons());

		//SFDC Update Opportunity CLOSED-DROPPED partner condition addition
		if (Objects.nonNull(stage.getCompetitor())&&Objects.nonNull(stage.getQuoteByCompetitor())) {
			updateOpportunity.setCompetitor(stage.getCompetitor());
			updateOpportunity.setQuoteByCompetitor(stage.getQuoteByCompetitor());
		}

		//end
		
		//Credit Check
		updateOpportunity.setIccEnterpriceVoiceProductFlagC(stage.getIccEnterpriceVoiceProductFlag());
		updateOpportunity.setIsPreApprovedOpportunityC(stage.getIsPreapprovedOpportunity());
		updateOpportunity.setNotifyCreditControlTeamC(stage.getNotifyCreditControlTeam());
		updateOpportunity.setMrcNrcC(stage.getLastApprovedMrcNrc());
		
		//Cancellation
		updateOpportunity.setCancellationChargesC(stage.getCancellationCharges());

		//Termination
		updateOpportunity.setReasonForTerminationC(stage.getReasonForTermination());
		updateOpportunity.setTerminationSubReasonC(stage.getTerminationSubReason());
		updateOpportunity.setCustomerRequestedDateC(stage.getTerminationSendToTDDate());
		updateOpportunity.setInternalCustomerC(stage.getInternalOrCustomer());
		updateOpportunity.setCsmNoncsmC(stage.getCsmNonCsm());
		updateOpportunity.setCommunicationRecipientC(stage.getCommunicationRecipient());
		updateOpportunity.setHandoverToC(stage.getHandoverTo());
		updateOpportunity.setDummyParentTerminationOpportunityC(stage.getDummyParentTerminationOpportunity());
		updateOpportunity.setRetentionReasonC(stage.getRetentionReason());
		updateOpportunity.setTerminationRemarkC(stage.getTerminationRemarks());
		updateOpportunity.setRegrettedNonRegrettedTerminationC(stage.getRegrettedNonRegrettedTermination());
		updateOpportunity.setEffectiveDateOfChangeC(stage.getEffectiveDateOfChange());
		updateOpportunity.setEtcSystemGeneratedC(stage.getEarlyTerminationCharges());
		updateOpportunity.setEarlyTerminationChargesAmountC(stage.getActualEtcToBeCharged());
		updateOpportunity.setEtcWaiverTypeC(stage.getEtcWaiverType());
		updateOpportunity.setEtcWaiverC(stage.getEtcWaived());
		updateOpportunity.setEtcRemarksC(stage.getEtcRemarks());
		updateOpportunity.setEarlyTerminationChargesC(stage.getEarlyTerminationChargesApplicable());


		if (SfdcConstants.CLOSED_WON_COF_RECI.getConstantCode().equals(stage.getStageName())
				&& CommonConstants.IPC.equals(stage.getProductType())) {
			updateOpportunity.setE2eCommentsC(SfdcConstants.CLOSED_WON_COF_RECI.getConstantCode());
		}
		//sfdc opportunity bundle product update sdwan
		if (Objects.nonNull(stage.getBundledProductTwoC()) && Objects.nonNull(stage.getBundledOrderTypeTwoC())) {
			updateOpportunity.setBundledProductTwoC(stage.getBundledProductTwoC());
			updateOpportunity.setBundledOrderTypeTwoC(stage.getBundledOrderTypeTwoC());
		}
		if (Objects.nonNull(stage.getBundledProductThreeC()) && Objects.nonNull(stage.getBundledOrderTypeThreeC())) {
			updateOpportunity.setBundledProductThreeC(stage.getBundledProductThreeC());
			updateOpportunity.setBundledOrderTypeThreeC(stage.getBundledOrderTypeThreeC());
		}
		if (Objects.nonNull(stage.getBundledProductFourC()) && Objects.nonNull(stage.getBundledOrderTypeFourC())) {
			updateOpportunity.setBundledProductFourC(stage.getBundledProductFourC());
			updateOpportunity.setBundledOrderTypeFourC(stage.getBundledOrderTypeFourC());
		}
		
		updateOpportunityBean.setOpportunity(updateOpportunity);
		updateOpportunityBean.setProgramManagerName(stage.getProgramManager());
		updateOpportunityBean.setCustomerContractingEntity(stage.getCustomerContractingId());
		updateOpportunityBean.setAccountCUID(stage.getAccountCuid());

		// Setting attributes required for UCAAS
		if (CommonConstants.UCAAS.equals(stage.getProductType())) {
			if (SfdcConstants.CLOSED_WON_COF_RECI.getConstantCode().equals(stage.getStageName())) {
				updateOpportunity
						.setE2eCommentsC(Objects.nonNull(stage.getE2eCommentsC()) ? stage.getE2eCommentsC() : null);
			} else if (SfdcConstants.VERBAL_AGREEMENT.getConstantCode().equals(stage.getStageName())) {
				updateOpportunity.
						setOpportunitySpecificationC(Objects.nonNull(stage.getOpportunitySpecification()) ?
								stage.getOpportunitySpecification() : null);
			}
		}


		if (Objects.nonNull(stage.getPartnerContractingId())) {
			updateOpportunityBean.setPartnerCUID(stage.getPartnerContractingId());
		}
		
		if (Objects.nonNull(stage.getRfsInDaysMhsMssC())) {
			updateOpportunity.setRfsInDaysMhsMssC((stage.getRfsInDaysMhsMssC()).toString());
		}
		
		request.setUpdateRequestV1(updateOpportunityBean);

		// For teamsdr.
		if(Objects.nonNull(stage.getE2eCommentsC())){
			updateOpportunity.setE2eCommentsC(stage.getE2eCommentsC());
		}

		// For teamsdr
		if(Objects.nonNull(stage.getParentQuoteToLeId())){
			request.setParentQuoteToLeId(stage.getParentQuoteToLeId());
		}

		return request;
	}

}
