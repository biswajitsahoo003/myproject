package com.tcl.dias.preparefulfillment.servicefulfillment.listener;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.ordertocash.beans.ScServiceDetailForOrderAmend;
import com.tcl.dias.common.ordertocash.beans.SiteToService;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.service.ServiceDashboardService;
import com.tcl.dias.preparefulfillment.service.TerminationService;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRPlanItemRequestBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.TerminationDropRequest;
import com.tcl.dias.common.fulfillment.beans.TerminationDropResponse;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.preparefulfillment.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

/**
 * 
 * This file contains the ServicefulfillmentListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServicefulfillmentListener {

	@Autowired
	ServiceFulfillmentService serviceFulfillmentService;

	@Autowired
	ServiceDashboardService serviceDashboardService;

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private TerminationService terminationService;
	
	@Autowired
	ProcessL2OService processL2OService;


	private static final Logger LOGGER = LoggerFactory.getLogger(ServicefulfillmentListener.class);

	@Async
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.o2c.fulfillmentdate}")})
	public void processFulfillment(String responseBody) {
		try {

			LOGGER.info("Input Payload received for Fulfillment received  : {}", responseBody);
			String fulfillmentData = responseBody;
			LOGGER.info("Fullfillment Data {}", fulfillmentData);
			OdrOrderBean request = (OdrOrderBean) Utils.convertJsonToObject(fulfillmentData, OdrOrderBean.class);
			
			Set<ScServiceDetail> scServiceDetails = new HashSet<>();
			if(request.getOrderType()!=null && "Cancellation".equalsIgnoreCase(request.getOrderType())){
				LOGGER.info("Inside cancellation flow from L2O for order code : {} ",request.getUuid());
				serviceFulfillmentService.processCancellationRequestFromL2O(request);
			}else if(request.getOrderType()!=null && "RENEWALS".equalsIgnoreCase(request.getOrderType())){
				LOGGER.info("Inside RENEWALS flow from L2O for order code : {} ",request.getUuid());
				ScServiceDetail renewalScServiceDetail=null;
				if(request.getOpOrderCode().startsWith("NPL")) {
					renewalScServiceDetail = serviceFulfillmentService.processRenewalfulfillmentDateForNpl(request);
				}else {
					renewalScServiceDetail = serviceFulfillmentService.processRenewalfulfillmentData(request);
				}
				serviceFulfillmentService.triggerRenewalWorkflow(renewalScServiceDetail);
			} 
			else{
				LOGGER.info("Inside other than cancellation flow from L2O for order code : {} ",request.getUuid());
				if (request.getOpOrderCode().startsWith("NPL")) {
					LOGGER.info("NPL Order-" + request.getOpOrderCode());
					//If its Termination Order We need to validate the inputs
					if(request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						LOGGER.info("Termination NPL Order validation starts for order code {}", request.getOpOrderCode());
						boolean isNotValidForTermination =  serviceFulfillmentService.isNotValidOrderForTermination(request);
						if(isNotValidForTermination) {
							LOGGER.info("Termination Order is Not an eligible for ordercode {} ", request.getOpOrderCode());
							return;
						}
					}
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateForNpl(request);
					LOGGER.info("NPL Order service details size:{}" + scServiceDetails.size());
					if (request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						LOGGER.info("NPL Order  Termination service details size:{}" + scServiceDetails.size());

						serviceFulfillmentService.triggerTerminationWorkflow(scServiceDetails);

					} else {
						serviceFulfillmentService.triggerWorkflow(scServiceDetails);

					}
				}else if(request.getOpOrderCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
					LOGGER.info("Izosdwan Order-"+request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateIzosdwan(request);
					serviceFulfillmentService.triggerSDWANWorkflow(scServiceDetails);
				}else if(request.getOpOrderCode().startsWith("UCWB")) {
					LOGGER.info("UCWB Order-"+request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateWebEx(request);
					serviceFulfillmentService.triggerWebExWorkflow(scServiceDetails);
				}else if(request.getOpOrderCode().startsWith("UCDR")) {
					LOGGER.info("UCDR Order-"+request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateForTeamsDR(request);
					serviceFulfillmentService.triggerTeamsDRWorkflow(scServiceDetails);
				}else if(request.getOpOrderCode().startsWith("GSC")) {
					scServiceDetails = serviceFulfillmentService.processfulfillmentDate(request);
					serviceFulfillmentService.triggerGSCWorkflow(scServiceDetails);
				}else if(request.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateForIpc(request);
					serviceFulfillmentService.triggerWorkflow(scServiceDetails);
				}else {
					LOGGER.info("Fulfillment Product Name-" + request.getOpOrderCode());
					//If its Termination Order We need to validate the inputs
					if(request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						LOGGER.info("Termination Order validation starts for order code {}", request.getOpOrderCode());
						boolean isNotValidForTermination =  serviceFulfillmentService.isNotValidOrderForTermination(request);
						if(isNotValidForTermination) {
							LOGGER.info("Termination Order is Not an eligible for ordercode {} ", request.getOpOrderCode());
							return;
						}
					}
					scServiceDetails = serviceFulfillmentService.processfulfillmentDate(request);
					if (("NEW".equalsIgnoreCase(request.getOrderType())
							|| ("MACD".equalsIgnoreCase(request.getOrderType()) && "ADD_SITE".equalsIgnoreCase(request.getOrderCategory())))
							&& scServiceDetails.stream().anyMatch(scServiceDetail -> scServiceDetail.getMultiVrfSolution() != null
									&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())
									&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsMultiVrf()))) {
						LOGGER.info("Inside multi vrf master trigger block with order code {} and order type {} :",request.getOpOrderCode(),request.getOrderType());
						scServiceDetails = scServiceDetails.stream()
								.filter(scServiceDetail -> scServiceDetail.getMultiVrfSolution() != null
										&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())
										&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsMultiVrf()))
								.collect(Collectors.toSet());
					}
					if (request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						serviceFulfillmentService.triggerTerminationWorkflow(scServiceDetails);

					} else {
						serviceFulfillmentService.triggerWorkflow(scServiceDetails);

					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}
	
	public void processFulfillment(OdrOrderBean request) {
		try {

			Set<ScServiceDetail> scServiceDetails = new HashSet<>();
			if(request.getOrderType()!=null && "Cancellation".equalsIgnoreCase(request.getOrderType())){
				LOGGER.info("Inside cancellation flow from L2O for order code : {} ",request.getUuid());
				serviceFulfillmentService.processCancellationRequestFromL2O(request);
			} 
			else{
				LOGGER.info("Inside other than cancellation flow from L2O for order code : {} ",request.getUuid());
				if (request.getOpOrderCode().startsWith("NPL")) {
					LOGGER.info("NPL Order-" + request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateForNpl(request);
					if (request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						serviceFulfillmentService.triggerTerminationWorkflow(scServiceDetails);

					} else {
						serviceFulfillmentService.triggerWorkflow(scServiceDetails);

					}
				}else if(request.getOpOrderCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
					LOGGER.info("Izosdwan Order-"+request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateIzosdwan(request);
					serviceFulfillmentService.triggerSDWANWorkflow(scServiceDetails);
				}else if(request.getOpOrderCode().startsWith("UCWB")) {
					LOGGER.info("UCWB Order-"+request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateWebEx(request);
					serviceFulfillmentService.triggerWebExWorkflow(scServiceDetails);
				} else if(request.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
					scServiceDetails = serviceFulfillmentService.processfulfillmentDateForIpc(request);
					serviceFulfillmentService.triggerWorkflow(scServiceDetails);
				} else {
					LOGGER.info("Fulfillment Product Name-" + request.getOpOrderCode());
					scServiceDetails = serviceFulfillmentService.processfulfillmentDate(request);
					if (request.getOrderType() != null && request.getOrderType().equalsIgnoreCase("Termination")) {
						serviceFulfillmentService.triggerTerminationWorkflow(scServiceDetails);

					} else {
						serviceFulfillmentService.triggerWorkflow(scServiceDetails);

					}
				}

				

			}
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}


	@Async
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.o2c.oe.fulfillmentdate}")})
	public void processOrderEnrichmentFulfillment(String responseBody) {
		try {

			LOGGER.info("Input Payload received for order enrichment Fulfillment received : {}", responseBody);
			String fulfillmentData = responseBody;
			LOGGER.info("OE Fullfillment Data {}", fulfillmentData);
			OdrOrderBean request = (OdrOrderBean) Utils.convertJsonToObject(fulfillmentData, OdrOrderBean.class);
			serviceFulfillmentService.processOrderEnrichmentfulfillmentDate(request);
		} catch (Exception e) {
			LOGGER.error("Error in process processOrderEnrichmentFulfillment data ", e);
		}
	}


	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.getServiceDetailsO2c.queue}")})
	public String getServicesForOrderCode(String responseBody) {
		String response ="";
		try {
			ScServiceDetailForOrderAmend scServiceDetailForOrderAmendment = new ScServiceDetailForOrderAmend();
			LOGGER.info("Input Payload received for getting services for Order Amendment : {}", responseBody);
			String orderCode = responseBody;
			String[] serviceDetailAvailable = new String[1];
			List<ScServiceDetailBean> inprogressServiceDetails = serviceDashboardService.getInprogressServiceDetails(orderCode, serviceDetailAvailable);
			List<SiteToService> siteToServices = new ArrayList<>();
			if(Objects.nonNull(inprogressServiceDetails) && !inprogressServiceDetails.isEmpty()){
				LOGGER.info("In progress Service Details queue response recieved for request ----> {} is ----> {} ", orderCode,inprogressServiceDetails);
				scServiceDetailForOrderAmendment.setOrderCode
						(inprogressServiceDetails.stream().findAny().get().getScOrderUuid());
				inprogressServiceDetails.forEach(scServiceDetailBean -> {
					SiteToService siteToService = new SiteToService();
					siteToService.setServiceId(scServiceDetailBean.getUuid());
					siteToService.setSiteCode(scServiceDetailBean.getSiteCode());
					siteToService.setPrimarySecondary(scServiceDetailBean.getPrimarySecondary());
					siteToService.setClrTask(scServiceDetailBean.getClrStageForAmendment());
					siteToServices.add(siteToService);
				});

				scServiceDetailForOrderAmendment.setSiteToServices(siteToServices);
			}

			if(serviceDetailAvailable[0]!=null && serviceDetailAvailable[0].equals(CommonConstants.NONE)) {
				response = CommonConstants.NONE;
			} else {
			response = Utils.convertObjectToJson(scServiceDetailForOrderAmendment);
			LOGGER.info("Size of site to service mappings for order code in response is ----> {} "
					, Optional.ofNullable(scServiceDetailForOrderAmendment.getSiteToServices().size()));
			LOGGER.info("Order Amendment servive details response is ----> {} ", response);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting service details for order amendment ", e);
		}
		
		return response;
	}

	@Async
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.gde.sc.fulfillmentdate}")})
	public void processFulfillmentGde(String responseBody) {
		try {

			LOGGER.info("Input Payload received for Fulfillment received  : {}", responseBody);
			String fulfillmentData = responseBody;
			LOGGER.info("Fullfillment Data {}", fulfillmentData);
			OdrOrderBean request = (OdrOrderBean) Utils.convertJsonToObject(fulfillmentData, OdrOrderBean.class);
			Set<ScServiceDetail> scServiceDetails = new HashSet<>();
			if (request.getOpOrderCode().startsWith("GDE")) {
				scServiceDetails = serviceFulfillmentService.processfulfillmentDateForGde(request);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}
	
	
	@RabbitListener(queuesToDeclare = {@Queue("${process.terminate.ten.percent}")})
	public String processTerminate10Percent(String responseBody) throws TclCommonException {
		TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();
		try {

			LOGGER.info("Input Payload received for Termination : {}", responseBody);
			String fulfillmentData = responseBody;
			LOGGER.info("Fullfillment Data {}", fulfillmentData);
			OdrOrderBean request = (OdrOrderBean) Utils.convertJsonToObject(fulfillmentData, OdrOrderBean.class);
			LOGGER.info("Input Payload received for Termination for orderCode: {}", request.getOpOrderCode());
			terminationNegotiationResponse = terminationService.intiateTreminationWith10PrencentChance(request);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
			terminationNegotiationResponse.setStatus("ERROR");
		}
		return Utils.convertObjectToJson(terminationNegotiationResponse);
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${process.drop.termination.quote}")})
	public String dropTerminationQuote(String responseBody) throws TclCommonException {
		TerminationDropResponse terminationDropResponse = new TerminationDropResponse();
		try {

			LOGGER.info("Input Payload received for Drop Quote Termination : {}", responseBody);
			String fulfillmentData = responseBody;
			LOGGER.info("Fullfillment Data {}", fulfillmentData);
			TerminationDropRequest request = (TerminationDropRequest) Utils.convertJsonToObject(fulfillmentData, TerminationDropRequest.class);
			LOGGER.info("Input Payload received for Drop Quote for orderCode: {}", request.getOpOrderCode());
			terminationDropResponse = terminationService.dropTerminationQuote(request);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
			terminationDropResponse.setStatus("ERROR");
		}
		return Utils.convertObjectToJson(terminationDropResponse);
	}
		
	@Async
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.o2c.sdwan.trigger}")})
	public void processSdwanFulfillment(String responseBody) {
		try {
			LOGGER.info("Input Payload received for processSdwanFulfillment received  : {}", responseBody);
			String orderCode = responseBody;
			processL2OService.processSDWANL2ODataToFlowable(orderCode);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}

	/**
	 * Process Teams DR fulfillment
	 *
	 * @param responseBody
	 */
	@Async
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.teamsdr.trigger}") })
	public void processTeamsDRFulfillment(String responseBody) {
		try {
			LOGGER.info("Input Payload received for processTeamsDRFulfillment received  : {}", responseBody);
			String orderCode = responseBody;
			processL2OService.processMediaGatewayL2ODataToFlowable(orderCode);
			processL2OService.processManagedServicesL2ODataToFlowable(orderCode);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}



	@RabbitListener(queuesToDeclare = {@Queue("${slave.fulfillment.trigger.queue}")})
	public void processSlaveFulFillment(String masterServiceCode) {
		try {
			LOGGER.info("Input Payload received for processSlaveFulFillment received  : {}", masterServiceCode);
			if(masterServiceCode!=null && !masterServiceCode.isEmpty()) {
				processL2OService.processSlaveWorkFlow(masterServiceCode);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processSlaveFulFillment ", e);
		}
	}

	@Async
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.gsc.fulfillment}")})
	public void processFulfillmentForGsc(String responseBody) {
		try {
			LOGGER.info("Input Payload received for Fulfillment received  : {}", responseBody);
			PlanItemRequestBean planItemRequestBean = Utils.convertJsonToObject(responseBody, PlanItemRequestBean.class);
			serviceFulfillmentService.triggerGSCWorkflow(planItemRequestBean);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}

	/**
	 * Process TeamsDR Managed Services.
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.o2c.teamsdr.managed.services}")})
	public void processFulfillmentForManagedServicesTeamsDR(String responseBody) {
		try {
			LOGGER.info("Input Payload received for processFulfillmentForManagedServicesTeamsDR : {}", responseBody);
			List<TeamsDRPlanItemRequestBean> planItemRequests = Utils
					.fromJson(responseBody, new TypeReference<List<TeamsDRPlanItemRequestBean>>() {
					});
			planItemRequests.forEach(planItemRequestBean -> {
				try {
					serviceFulfillmentService.triggerTeamsDRManagedServices(planItemRequestBean);
				} catch (TclCommonException e) {
					LOGGER.info(ExceptionConstants.COMMON_ERROR);
				}
			});
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}
}
