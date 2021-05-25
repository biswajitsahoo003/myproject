package com.tcl.dias.l2oworkflow.servicefulfillment.listener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.SiteDetailServiceFulfilmentUpdateBean;
import com.tcl.dias.common.beans.TaskBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;

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
	RuntimeService runtimeService;

	@Autowired
	SiteDetailRepository siteDetailRepository;

	@Autowired
	CmmnRuntimeService cmmnRuntimeService;
	
	@Autowired
	MfDetailRepository mfDetailRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ServicefulfillmentListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.discount.price.request}") })
	public void processCommercialPriceUpdate(String responseBody) {
		try {

			LOGGER.info("Input Payload received for commercial price update received ");
			String request =responseBody;
			if (request == null) {
				LOGGER.error("NO request data to create task for Commericial price approval flow");
			} else {
				PriceDiscountBean priceDiscountBean = Utils.convertJsonToObject(request, PriceDiscountBean.class);
				SiteDetail siteDetail = new SiteDetail();
				siteDetail.setQuoteCreatedUserType(priceDiscountBean.getQuoteCreatedUserType());
				siteDetail.setCreatedTime(new Timestamp(new Date().getTime()));
				siteDetail.setUpdatedTime(new Timestamp(new Date().getTime()));
				siteDetail.setIsActive(CommonConstants.ACTIVE);
				siteDetail.setQuoteCode(priceDiscountBean.getQuoteCode());
				siteDetail.setQuoteId(priceDiscountBean.getQuoteId());
				if (priceDiscountBean.getSiteDetail() != null) {
					siteDetail.setSiteDetail(Utils.convertObjectToJson(priceDiscountBean.getSiteDetail()));
				}
				siteDetail.setStatus(MasterDefConstants.IN_PROGRESS);
				siteDetail.setAccountName(priceDiscountBean.getAccountName());
				siteDetail.setContractTerm(priceDiscountBean.getContractTerm());
				siteDetail.setQuoteCreatedBy(priceDiscountBean.getQuoteCreatedBy());
				siteDetail.setQuoteType(priceDiscountBean.getQuoteType());
				siteDetail.setOpportunityId(priceDiscountBean.getOptyId());
				siteDetail.setRegion(priceDiscountBean.getRegion());
				siteDetail.setDiscountApprovalLevel(priceDiscountBean.getDiscountApprovalLevel());
				siteDetail = siteDetailRepository.saveAndFlush(siteDetail);

				if (siteDetail.getId() != null) {
					Map<String, Object> processVar = new HashMap<>();
					processVar.put("discountApprovalLevel", priceDiscountBean.getDiscountApprovalLevel());
					processVar.put("quoteCode", priceDiscountBean.getQuoteCode());
					processVar.put("quoteId", priceDiscountBean.getQuoteId());
					// processVar.put("siteCode", priceDiscountBean.getSiteCode());
					// processVar.put("siteId", priceDiscountBean.getSiteId());
					processVar.put(MasterDefConstants.SITE_DETAIL_ID, siteDetail.getId());
					processVar.put("processType", "computeProjectPLan");
					processVar.put("root_endDate", new Timestamp(System.currentTimeMillis()));
					runtimeService.startProcessInstanceByKey("commercial_discount_workflow", processVar);
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in process commercial price update data ", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.manual.feasibility.request}") })
	public void processManualFeasibility(String responseBody) {
		try {

			LOGGER.info("Input Payload received for manual feasibility ");
			String request = responseBody;
			if (request == null) {
				LOGGER.error("NO request data to trigger workflow for manual feasibility");
			} else {
				MfDetailsBean mfDetailBean = Utils.convertJsonToObject(request, MfDetailsBean.class);

				MfDetail mfDetail = serviceFulfillmentService.persistMfDetail(mfDetailBean);
				
				// check qoute code and quote ID is saved..
				if (mfDetail.getId() != null) {
					
					LOGGER.info("Quote code and quoteID from mfDetail table after save ###");
					Optional<MfDetail> savedMfDetail = mfDetailRepository.findById(mfDetail.getId());
					
					if(savedMfDetail.isPresent()) {
						LOGGER.info("#####Quote code {}",savedMfDetail.get().getQuoteCode());
						LOGGER.info("#####Quote Id {}",savedMfDetail.get().getQuoteId());
					}
					Map<String, Object> processVar = new HashMap<>();
					processVar.put(ManualFeasibilityWFConstants.ASSIGNED_TO, mfDetailBean.getAssignedTo());
					processVar.put(MasterDefConstants.QUOTE_CODE, mfDetailBean.getQuoteCode());
					processVar.put(MasterDefConstants.QUOTE_ID, mfDetailBean.getQuoteId());
					processVar.put(MasterDefConstants.SITE_CODE, mfDetailBean.getSiteCode());
					processVar.put(MasterDefConstants.SITE_ID, mfDetailBean.getSiteId());
					processVar.put("PRV", false);
					processVar.put("GIMEC", false);
					processVar.put("OSP", false);
					processVar.put("PRVTX", false);
					processVar.put("MAN", false);
					processVar.put("txEngg", false);
					processVar.put("AFM", false);
					processVar.put(ManualFeasibilityWFConstants.START_STAGE, false);
					processVar.put(ManualFeasibilityWFConstants.INITIAL_RESPONSE_COMPLETE, false);
					processVar.put(ManualFeasibilityWFConstants.PROCESS_FINAL_RESPONSE, false);
					processVar.put(ManualFeasibilityWFConstants.IS_RESPONSE_COMPLETE, false);
					processVar.put(MasterDefConstants.PRODUCT_NAME, mfDetailBean.getProductName());
					processVar.put(ManualFeasibilityWFConstants.MF_DETAIL_ID, mfDetail.getId());
					processVar.put(ManualFeasibilityWFConstants.REGION, mfDetailBean.getRegion());
					processVar.put(ManualFeasibilityWFConstants.FROM_PRV, false);
					processVar.put(ManualFeasibilityWFConstants.SITESTATUS, new HashMap<Integer, String>());
					processVar.put(ManualFeasibilityWFConstants.SUBJECT, "Check Local loop & Bandwidth");
					processVar.put(ManualFeasibilityWFConstants.AFM_SUBJECT, "Check Last Mile Feasibility");

					if (mfDetailBean != null && mfDetailBean.getMfDetails() != null) {
						if (mfDetailBean.getCreatedBy() != null) {
							processVar.put(ManualFeasibilityWFConstants.ASSIGNED_FROM,
									mfDetailBean.getCreatedByEmail());
						} else {
							processVar.put(ManualFeasibilityWFConstants.ASSIGNED_FROM, "System");
						}
						if (mfDetailBean.getMfDetails().getOppurtunityAccountEmail() != null) {
							processVar.put(ManualFeasibilityWFConstants.OPP_ACC_EMAIL,
									mfDetailBean.getMfDetails().getOppurtunityAccountEmail());
						}
					}
					processVar.put(ManualFeasibilityWFConstants.PRV_STATUS, "NA");
					/*
					 * if(mfDetail.getAssignedTo().equalsIgnoreCase("AFM"))
					 * processVar.put("notifyTeam",
					 * mfDetail.getAssignedTo().concat("_").concat(mfDetail.getRegion())); else
					 * processVar.put("notifyTeam", mfDetail.getAssignedTo());
					 */
					processVar.put(ManualFeasibilityWFConstants.REQUESTOR_COMMENTS, "Manual Feasibility Request");
					CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
							.caseDefinitionKey("manual_feasibility_workflow").variables(processVar).start();
					LOGGER.info("Case Instance Id in listener : {}", caseInstance.getId());
					List<PlanItemInstance> planItems = cmmnRuntimeService.createPlanItemInstanceQuery()
							.caseInstanceId(caseInstance.getId()).list();
					if (planItems.isEmpty())
						LOGGER.info("Plan Items Empty in trigger workflow ");
					for (PlanItemInstance planItem : planItems) {
						if (planItem.getPlanItemDefinitionType()
								.equalsIgnoreCase(ManualFeasibilityWFConstants.HUMAN_TASK))
							LOGGER.info("Plan Item Id : {} , planItem def id : {} , planItem state : ",
									planItem.getId(), planItem.getPlanItemDefinitionId(), planItem.getState());
					}
					cmmnRuntimeService.setVariable(caseInstance.getId(), ManualFeasibilityWFConstants.START_STAGE,
							true);
					// serviceFulfillmentService.persistPlanItemsForCase(caseInstance);

				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in process manual feasibility ", e);
		}
	}

	/**
	 * 
	 * This method is used to Update site details
	 * 
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.sitedetail}") })
	public void updateSiteDetails(String responseBody) {
		try {

			LOGGER.info("Input Payload received for order enrichment Fulfillment received ");
			String fulfillmentData =responseBody;
			SiteDetailServiceFulfilmentUpdateBean siteDetailServiceFulfilmentUpdateBean = (SiteDetailServiceFulfilmentUpdateBean) Utils
					.convertJsonToObject(fulfillmentData, SiteDetailServiceFulfilmentUpdateBean.class);
			serviceFulfillmentService.updateSiteDetails(siteDetailServiceFulfilmentUpdateBean);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}

	/**
	 *
	 * This method is used to Close returned task
	 *
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.related.task.deletion}") })
	public void closeReturnTask(String responseBody) {
		try {

			LOGGER.info("Input Payload received for closing task received ");
			String siteId =responseBody;
			serviceFulfillmentService.closeReturnedTask(siteId);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
	}
	
	
	/**
	 *
	 * This method is used to get all tasks by siteId
	 *
	 * @param siteId
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.mf.task.triggered.queue}") })
	public String getAllTasksBySiteId(String siteId) {
		String response = "";
		List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
		try {
			LOGGER.info("inside getAllTaskBySiteId Listener{}",siteId);
			taskBeanList = serviceFulfillmentService.getAllTasksBySiteId(siteId);
			response = Utils.convertObjectToJson(taskBeanList);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
		return response;
	}

}
