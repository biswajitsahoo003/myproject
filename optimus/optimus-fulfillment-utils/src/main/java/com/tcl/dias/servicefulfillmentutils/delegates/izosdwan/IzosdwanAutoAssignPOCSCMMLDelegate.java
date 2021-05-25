package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("autoAssignPOCSCMMLDelegate")
public class IzosdwanAutoAssignPOCSCMMLDelegate implements JavaDelegate {
	
	    private static final Logger logger = LoggerFactory.getLogger(IzosdwanAutoAssignPOCSCMMLDelegate.class);
	
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		FieldEngineerRepository fieldEngineerRepository;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;

	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		Integer orderId = (Integer) execution.getVariable(SC_ORDER_ID);
		logger.info("SdwanAutoAssignPOCSCMMLDelegate invoked for serviceCode {} with Id={}", serviceCode,execution.getId());
		String errorMessage = "";
		Task serviceTask=workFlowService.processServiceTask(execution);
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setTask(serviceTask);
		fieldEngineer.setServiceId(serviceTask.getServiceId());
		fieldEngineer.setAppointmentType(serviceTask.getMstTaskDef().getKey().replace("assign-poc-","poc-"));
		List<String> siteCountryList=scServiceDetailRepository.findDistinctDestinationCountryByScOrder_IdAndErfPrdCatalogProductName(orderId, "IZO SDWAN");
		if(siteCountryList!=null && !siteCountryList.isEmpty()){
			logger.info("SdwanAutoAssignPOCSCMMLDelegate for serviceCode {} siteCountryList exists::{}", serviceCode,siteCountryList.size());
			if(siteCountryList.contains("India") && siteCountryList.size()>1){
				logger.info("SdwanAutoAssignPOCSCMMLDelegate for serviceCode {} has both India and ROW", serviceCode);
				fieldEngineer.setName("Sonali Morje");
				fieldEngineer.setEmail("sonali.morje@tatacommunications.com");
				fieldEngineer.setSecondaryname("Sachin Kadam");
				fieldEngineer.setSecondaryemail("sachin.kadam@tatacommunications.com");
				fieldEngineerRepository.save(fieldEngineer);
			}else if(!siteCountryList.contains("India")){
				logger.info("SdwanAutoAssignPOCSCMMLDelegate for serviceCode {} has only ROW", serviceCode);
				fieldEngineer.setName("Sachin Kadam");
				fieldEngineer.setEmail("sachin.kadam@tatacommunications.com");
				fieldEngineer.setSecondaryname("Hemant Bhalerao");
				fieldEngineer.setSecondaryemail("hemant.bhalerao@tatacommunications.com");
				fieldEngineerRepository.save(fieldEngineer);
			}else{
				logger.info("SdwanAutoAssignPOCSCMMLDelegate for serviceCode {} has only India", serviceCode);
				fieldEngineer.setName("Sonali Morje");
				fieldEngineer.setEmail("sonali.morje@tatacommunications.com");
				fieldEngineer.setSecondaryname("Hemant Bhalerao");
				fieldEngineer.setSecondaryemail("hemant.bhalerao@tatacommunications.com");
				fieldEngineerRepository.save(fieldEngineer);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}

}
