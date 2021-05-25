package com.tcl.dias.oms.termination.listener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.TerminationQuoteDetailsBean;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.dashboard.constants.DashboardConstant;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TerminationStageMovementListener.java class.
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class TerminationStageMovementListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(TerminationStageMovementListener.class);
	
	@Autowired
	OmsSfdcService omsSfdcService;
	
	@Autowired
	QuoteRepository quoteRepository;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	/**
	 * To process automatic stage movement to Verbal Agreement for Termination quotes
	 *
	 * Criteria : Quotes older than 7 days & (does not have CSM response or NOT RETAINED in Negotiation Response)
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.termination.stagemovement.queue}") })
	public void processTerminationQuoteStageToVerbalAgreement(Message<String> responseBody) {
		LOGGER.info("Terminations : Inside Auto SFDC Stage Movement to Verbal Agreement");
		LocalDate termWaitPeriodDate = LocalDate.now().minusDays(MACDConstants.QUOTE_WAIT_PERIOD_IN_DAYS);
		Date reqDate = Date.from(termWaitPeriodDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		LOGGER.info("Termination : Stage will be auto moved to Verbal Aggreement for quotes older than : {} ", reqDate);
		
		List<Map<String, Object>> terminationEligibleQuoteDetails = null;
		List<TerminationQuoteDetailsBean> terminationEligibleQuoteDetailsList = new ArrayList<>();
		
		terminationEligibleQuoteDetails = quoteRepository.findEligibleTerminationQuotesForSFDCVerbalAggreementStageMovement(reqDate, MACDConstants.TERMINATION_SERVICE, MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		LOGGER.info("Query Parameter for fetching eligible quotes for stage movement : {} - {} - {} - {}", reqDate.toString(), MACDConstants.TERMINATION_SERVICE,  MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		terminationEligibleQuoteDetailsList = getTerminationQuoteDetailsList(terminationEligibleQuoteDetails);
		terminationEligibleQuoteDetailsList.stream().filter(Objects::nonNull).forEach(terminationSrvDetail -> {
			LOGGER.info("Termination Service Detail Response : {}", terminationSrvDetail);
			Boolean updateStageForCurrentQuote = false;
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(terminationSrvDetail.getQuoteToLeId());
			if (terminationSrvDetail.getSalesTaskResponse() != null) {
				TerminationNegotiationResponse termintationNegoResponse = null;
				try {
					termintationNegoResponse = Utils.convertJsonToObject(terminationSrvDetail.getSalesTaskResponse(), TerminationNegotiationResponse.class);
					if(termintationNegoResponse.getNegotiationResponse().equalsIgnoreCase("NOT RETAINED")) {
						updateStageForCurrentQuote = true;
					}
				} catch (TclCommonException e1) {
					LOGGER.error("Error retrieving Sale Task Response : {}", e1.getMessage(), e1);
				}
			} else {
				updateStageForCurrentQuote = true;
			}
			if (quoteToLe.isPresent() && Boolean.TRUE.equals(updateStageForCurrentQuote)) {
				try {
					if(quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					omsSfdcService.processUpdateOpportunity(new Date(), terminationSrvDetail.getTpsSFDCOptyId(),
							SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
					} else {
						List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
						if(optyList != null && !optyList.isEmpty()) {
						omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(), String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(),
								SFDCConstants.VERBAL_AGREEMENT_STAGE);
						}
					}
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		});
		
		//For Non O2C triggered Quotes
		terminationEligibleQuoteDetails = null;
		terminationEligibleQuoteDetailsList = new ArrayList<>();
		terminationEligibleQuoteDetails = quoteRepository.findEligibleNonO2CTerminationQuotesForSFDCVerbalAggreementStageMovement(reqDate, MACDConstants.TERMINATION_SERVICE, MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		LOGGER.info("Query Parameter for fetching non O2C eligible quotes for stage movement : {} - {} - {} - {}", reqDate.toString(), MACDConstants.TERMINATION_SERVICE,  MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		terminationEligibleQuoteDetailsList = getTerminationQuoteDetailsList(terminationEligibleQuoteDetails);
		terminationEligibleQuoteDetailsList.stream().filter(Objects::nonNull).forEach(terminationSrvDetail -> {
			LOGGER.info("Termination Service Detail Response : {}", terminationSrvDetail);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(terminationSrvDetail.getQuoteToLeId());
			if (quoteToLe.isPresent()) {
				try {
					if(quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					omsSfdcService.processUpdateOpportunity(new Date(), terminationSrvDetail.getTpsSFDCOptyId(),
							SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
					} else {
						List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
						if(optyList != null && !optyList.isEmpty()) {
						omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(), String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(),
								SFDCConstants.VERBAL_AGREEMENT_STAGE);
						}
					}
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		});		
	}
	
	private static List<TerminationQuoteDetailsBean> getTerminationQuoteDetailsList(List<Map<String, Object>> map) {
		List<TerminationQuoteDetailsBean> quoteDetailsList = new ArrayList<>();
		map.forEach(entry -> {		
			TerminationQuoteDetailsBean quoteDetails = new TerminationQuoteDetailsBean();
			if (entry.get(DashboardConstant.QUOTE_ID) != null) {
				quoteDetails.setQuoteId((Integer) entry.get(DashboardConstant.QUOTE_ID));
				quoteDetails.setQuoteCreatedTime((Date) entry.get(MACDConstants.QUOTE_CREATED_TIME));
				quoteDetails.setTerminationCreatedTime((Date) entry.get(MACDConstants.TERMINATION_CREATED_TIME));
				quoteDetails.setO2cCallInitiatedTime((Date) entry.get(MACDConstants.O2C_CALL_INITIATED_DATE));
			}
			if (entry.get(MACDConstants.QUOTE_LE_ID) != null)
				quoteDetails.setQuoteToLeId((Integer)entry.get(MACDConstants.QUOTE_LE_ID));
			if (entry.get(MACDConstants.SFDC_OPTY_ID) != null)
				quoteDetails.setTpsSFDCOptyId(entry.get(MACDConstants.SFDC_OPTY_ID).toString());
			if (entry.get(DashboardConstant.QUOTE_STAGE) != null)
				quoteDetails.setQuoteStage(entry.get(DashboardConstant.QUOTE_STAGE).toString());
			if (entry.get(DashboardConstant.QUOTE_CODE) != null)
				quoteDetails.setQuoteCode(entry.get(DashboardConstant.QUOTE_CODE).toString());
			if (entry.get(DashboardConstant.QUOTE_CATEGORY) != null)
				quoteDetails.setQuoteCategory(entry.get(DashboardConstant.QUOTE_CATEGORY).toString());
			if (entry.get(MACDConstants.SALES_TASK_RESPONSE) != null)
				quoteDetails.setSalesTaskResponse(entry.get(MACDConstants.SALES_TASK_RESPONSE).toString());
			quoteDetailsList.add(quoteDetails);
		});
					
		return quoteDetailsList;
	}	
}
