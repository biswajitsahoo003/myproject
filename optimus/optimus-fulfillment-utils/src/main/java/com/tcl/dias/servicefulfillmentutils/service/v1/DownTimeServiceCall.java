package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class DownTimeServiceCall {
	private static final Logger logger = LoggerFactory.getLogger(DownTimeServiceCall.class);

	@Autowired
	DownTimeDetailsRepository downTimeDetailsRepository;


	@Transactional(readOnly = false)
	public void processTimeOutTask(DelegateExecution execution) throws TclCommonException {
		Map<String, Object> processMap = execution.getVariables();
		Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
		Integer solutionId = (Integer) processMap.get("solutionId");
		String productName = (String) processMap.get("productName");
		logger.info("processTimeOutTask serviceId :{}",serviceId);
		logger.info("DownTimeServiceCall  with current activity:{}",execution.getCurrentActivityId());	
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId,serviceId);
		if (downTimeDetails != null) {
			logger.info("DownTime Details::{}",downTimeDetails.getId());	
			if (execution.getCurrentActivityId().equalsIgnoreCase("cpe-downtime")) {
				logger.info("serviceId::{},cpe downtime::{}",serviceId,execution.getCurrentActivityId());	
				downTimeDetails.setIsCpeReadyForDownTime("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("sdwan-customer-appointment")) {
				logger.info("serviceId::{},sdwan customer appointment::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsCpeReadyForCustomerAppointment("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("byon-downtime")) {
				logger.info("serviceId::{},byon downtime::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsByonReadyForDownTime("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("byon-customer-appointment")) {
				logger.info("serviceId::{},byon customer appointment::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsByonReadyForCustomerAppointment("Y");
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("iwan-downtime") 
				    || execution.getCurrentActivityId().equalsIgnoreCase("dia-downtime")
				    || execution.getCurrentActivityId().equalsIgnoreCase("gvpn-international-downtime")) {
				logger.info("serviceId::{},underlay product::{} with downtime::{}",serviceId,productName,execution.getCurrentActivityId());
				downTimeDetails.setIsProvisionReadyForDownTime("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("iwan-customer-appointment")
					|| execution.getCurrentActivityId().equalsIgnoreCase("dia-customer-appointment")
					|| execution.getCurrentActivityId().equalsIgnoreCase("gvpn-international-customer-appointment")) {
				logger.info("serviceId::{},underlay product::{} with customer appointment::{}",serviceId,productName,execution.getCurrentActivityId());
				downTimeDetails.setIsProvisionReadyForCustomerAppointment("Y");
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("sdwan-ill-macd-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-ill-bso-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-offnet-bso-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-offnet-upgrade-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-p2p-bso-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-p2p-macd-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("cpe-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-ill-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-offnet-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-p2p-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-dia-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-iwan-config")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-gvpn-intl-config")) {
				logger.info("serviceId::{},sdwan config downtime::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsConfigCompleted("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("sdwan-ill-customer-appointment")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-offnet-customer-appointment")
					|| execution.getCurrentActivityId().equalsIgnoreCase("sdwan-p2p-customer-appointment")) {
				logger.info("serviceId::{},sdwan new downtime::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsLMReadyForCustomerAppointment("Y");
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("ip-downtime")
					|| execution.getCurrentActivityId().equalsIgnoreCase("activity-go-ahead-downtime")) {
				logger.info("serviceId::{},ip downtime::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsIpReadyForDownTime("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("tx-downtime")) {
				logger.info("serviceId::{},tx downtime::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsTxReadyForDownTime("Y");
				downTimeDetails.setIsTxDownTimeRequired("Y");
			} else if (execution.getCurrentActivityId().equalsIgnoreCase("tx-downtime-end")) {
				logger.info("serviceId::{},tx downtime end activity Id::{}",serviceId,execution.getCurrentActivityId());
				if(!downTimeDetails.getIsTxDownTimeRequired().equalsIgnoreCase("Y")){
					logger.info("serviceId::{},tx downtime end::{}",serviceId,downTimeDetails.getIsTxDownTimeRequired());
					downTimeDetails.setIsTxDownTimeRequired("N");
				}
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("ip-downtime-end")) {
				logger.info("serviceId::{},ip downtime end activity Id::{}",serviceId,execution.getCurrentActivityId());
				if(!downTimeDetails.getIsIpDownTimeRequired().equalsIgnoreCase("Y")){
					logger.info("serviceId::{},ip downtime end::{}",serviceId,downTimeDetails.getIsIpDownTimeRequired());
					downTimeDetails.setIsIpDownTimeRequired("N");
				}
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("cpe-managed-ip")) {
				logger.info("serviceId::{},cpe-managed-ip end activity Id::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsCpeAlreadyManaged("Y");
			}else if (execution.getCurrentActivityId().equalsIgnoreCase("sdwan-e2e-testing")) {
				logger.info("serviceId::{},sdwan-e2e-testing::{}",serviceId,execution.getCurrentActivityId());
				downTimeDetails.setIsE2ECompleted("Y");
			}
			downTimeDetails.setUpdatedBy("OPTIMUS");
			downTimeDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
			downTimeDetailsRepository.save(downTimeDetails);
		}
	}

}
