package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.GSC_FLOW_GROUP_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscNotificationService;

/**
 * This file contains the SendNotificationToSupplierDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("SendNotificationToSupplier")
public class SendNotificationToSupplierDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(SendNotificationToSupplierDelegate.class);
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	GscNotificationService notificationService;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	/**
	 * execute
	 * @param execution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("###### Inside ###### SendNotificationToSupplierDelegate");
		logger.info("SendNotificationToSupplierDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		
		Map<String, Object> processMap = execution.getVariables();
		Integer serviceId = (Integer) processMap.get(SERVICE_ID);
		Optional<ScServiceDetail> optionalService=scServiceDetailRepository.findById(serviceId);
		ScServiceDetail scServiceDetail = optionalService.get();
		String status = scServiceDetail.getMstStatus().getCode();
		String serviceCode = (String) processMap.get(SERVICE_CODE);
		Integer gscFlowGroupId = (Integer) processMap.get(GSC_FLOW_GROUP_ID);
		
		logger.info("Inside GscNotificationDelegate for serviceCode {} and task_def_key {}", serviceCode, execution.getCurrentActivityId());
		
		notificationService.notifyPlaceOrderToSupplier(null, serviceCode, gscFlowGroupId);
		/*
		if (execution.getCurrentActivityId() != null && (status.equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
				|| status.equalsIgnoreCase(TaskStatusConstants.ACTIVE))) {
			String taskDefKey = StringUtils.trimToEmpty(execution.getCurrentActivityId());
			
			if (taskDefKey.contains("notify-place-order-to-supplier")) {
				logger.info("Inside Notify for Place Order to Supplier for serviceCode {} and task def key {}", serviceCode, execution.getCurrentActivityId());
				List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
				for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
					if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
						notificationService.notifyPlaceOrderToSupplier(mstTaskRegion.getEmail(), serviceCode, gscFlowGroupId);
					}
			    } 
			}
		}*/
	}

}
