package com.tcl.dias.l2oworkflowutils.delegates;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.CommercialQuoteDetailBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.service.v1.NotificationService;
import com.tcl.dias.l2oworkflowutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@Component("processCommercialDiscount")
public class ProcessCommercialDiscountDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(ProcessCommercialDiscountDelegate.class);
	
	@Autowired
	MQUtils mqUtills;
	
	@Autowired
	SiteDetailRepository siteDetailRepository;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	NotificationService notificationService;
	
	@Value("${oms.get.quote.commercial}")
	String quoteDetailsQueue;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	ServiceFulfillmentBaseService serviceFulfillmentBaseService;

	@Override
	public void execute(DelegateExecution execution) {

		Map<String, Object> processMap = execution.getVariables();
		Integer siteDetailId = (Integer) processMap.get(MasterDefConstants.SITE_DETAIL_ID);
		logger.info("Before processingFinalApprovalQueue");
		//Triggering  final approval queue to oms
		serviceFulfillmentBaseService.processingFinalApprovalQueue(siteDetailId, processMap);
		Map<String, Object> map = new HashMap<>();
		logger.info("ProcessCommercialDiscountDelegate invoked for {} Id={} siteDetailId={}",
				execution.getCurrentActivityId(), execution.getId(), siteDetailId);
		String history = "";
		CommercialQuoteDetailBean commercialQuoteDetailBean = new CommercialQuoteDetailBean();
		Optional<SiteDetail> siteDetail = siteDetailRepository.findById(siteDetailId);
		if (siteDetail.isPresent()) {
			List<Integer> siteIds = new ArrayList<>();
			try {
				logger.info("Site details present {}----", siteDetail.get().getId());
				String siteInfo = siteDetail.get().getSiteDetail();
				ObjectMapper mapper = new ObjectMapper();
				List<com.tcl.dias.common.beans.SiteDetail> siteDetails = mapper.readValue(siteInfo,
						new TypeReference<List<com.tcl.dias.common.beans.SiteDetail>>() {
						});
				if (siteDetails != null) {
					siteDetails.stream().forEach(site -> {
						siteIds.add(site.getSiteId());
					});
				}
			} catch (Exception e1) {
				logger.error("Error in converting sitedetails {}", e1.getMessage());
			}
			map.put("quoteId", processMap.get("quoteId"));
			map.put("siteDetail", siteIds);
			map.put("discountApprovalLevel", processMap.get("discountApprovalLevel").toString());
			logger.info("Fetching approver details------{}", siteDetail.get().getId());
			try {
				Map<String, String> approveDetails = taskService.getDiscountDelegationDetails(siteDetail.get());
				commercialQuoteDetailBean.setAccountName(siteDetail.get().getAccountName());
				commercialQuoteDetailBean.setEmail(siteDetail.get().getQuoteCreatedBy());
				commercialQuoteDetailBean.setOptyId(siteDetail.get().getOpportunityId());
				if (approveDetails != null) {
					map.putAll(approveDetails);
					if(approveDetails.containsKey("commercial-discount-1")) {
						history = history.concat("Approver 1 :".concat(approveDetails.get("commercial-discount-1")));
					}
					if(approveDetails.containsKey("commercial-discount-2")) {
						history = history.concat(" Approver 2 :".concat(approveDetails.get("commercial-discount-2")));
					}
					if(approveDetails.containsKey("commercial-discount-3")) {
						history = history.concat(" Approver 3 :".concat(approveDetails.get("commercial-discount-3")));
					}
				}
			} catch (Exception e) {
				logger.warn("Error in fetching approver details {}", e.getMessage());
			}
		}
		try {

			/*
			 * logger.info("Before final approval queue call to oms"); String quoteCodeVal=
			 * (String)processMap.get("quoteCode"); if(quoteCodeVal!=null &&
			 * !quoteCodeVal.isEmpty()) { List<SiteDetail>
			 * sites=siteDetailRepository.findByQuoteCodeOrderByCreatedTimeDesc(quoteCodeVal
			 * ); if(!sites.isEmpty()) { sites.stream().forEach(site -> {
			 * site.setStatus("CLOSED");
			 * logger.info("Updating Site Detail Task Status To CLOSED After Final Approval"
			 * +site.getId()); siteDetailRepository.save(site);
			 * 
			 * }); } } mqUtills.send("rabbitmq_final_price", Utils.convertObjectToJson(map),
			 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); logger.
			 * info("After final approval queue to oms from preparefulfillment update SiteDetail CLOSED"
			 * ); //Update task closed status to SiteDetails
			 */		

			
				//mqUtills.send("rabbitmq_final_price", Utils.convertObjectToJson(map));
			

			if (commercialQuoteDetailBean != null) {
				notificationService.notifyCommercialFlowComplete(commercialQuoteDetailBean.getEmail(), (String)processMap.get("quoteCode"), commercialQuoteDetailBean.getOptyId(), commercialQuoteDetailBean.getAccountName(), history,commercialQuoteDetailBean.getEmail());
			}
			
			
		} catch (IllegalArgumentException e) {
			logger.warn("OMS price change ended notification failed ");
		}
	}

}
