package com.tcl.dias.preparefulfillment.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.servicefulfillmentutils.beans.CaseDetails;
import com.tcl.dias.servicefulfillmentutils.beans.PlanItemDetail;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;

@RestController
public class CmmnTasksTestController {
	private static final Logger logger = LoggerFactory.getLogger(CmmnTasksTestController.class);

	@Autowired
	CmmnHelperService cmmnHelperService;

	@Autowired
	ProcessL2OService processL2OService;
	
	@GetMapping(path = "/sayhello")
	public String sayHello() {
		return "hello there";
	}

	@PostMapping(path = "/startCase/{caseId}")
	public CaseDetails createCaseInstance(@PathVariable("caseId") String caseId,
			@RequestBody Map<String, Object> params) {

		logger.info("params :" + params);
		if (params.get("serviceId")!=null) {
			params.put("serviceId", new Integer(params.get("serviceId").toString()));
		} 
		return cmmnHelperService.createCaseInstance(caseId, params);
	}

	@GetMapping(path = "/getCaseInstanceDetails/{caseInstId}")
	public List<PlanItemDetail> getCaseInstanceDetails(@PathVariable("caseInstId") String caseInstId) {

		return cmmnHelperService.getCaseInstanceDetails(caseInstId);
	}

	@PostMapping(path = "/bpmn/closeTask/{taskId}")
	public void closeBpmnTask(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> params) {

		cmmnHelperService.closeBpmnTask(taskId, params);
	}

	@PostMapping(path = "/cmmn/closeTask/{taskId}")
	public void closeCmmnTask(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> params) {
		cmmnHelperService.closeCmmnTask(taskId, params);
	}

	@PostMapping(path = "/startPlanItem/{planInstId}")
	public void startPlanItem(@PathVariable("planInstId") String planInstId, @RequestBody Map<String, Object> params) {
		cmmnHelperService.startPlanItem(planInstId, params);
	}

	@PostMapping(path = "/enablePlanItem/{planInstId}")
	public void enablePlanItem(@PathVariable("planInstId") String planInstId, @RequestBody Map<String, Object> params) {
		cmmnHelperService.enablePlanItem(planInstId, params);
	}

	@PostMapping(path = "/triggerPlanItem/{planInstId}")
	public void triggerPlanItem(@PathVariable("planInstId") String planInstId,
			@RequestBody Map<String, Object> params) {
		cmmnHelperService.triggerPlanItem(planInstId, params);
	}

	@PostMapping(path = "/createPlanItem/{caseInstId}/{planItemDefinitionId}")
	public String createPlanItem(@PathVariable("caseInstId") String caseInstId,
			@PathVariable("planItemDefinitionId") String planItemDefinitionId, @RequestBody Map<String, Object> params) {
		return cmmnHelperService.createPlanItem(caseInstId, planItemDefinitionId, params);
	}
	
	@PostMapping(path = "/startAndCreateNewPlanItem/{caseInstId}/{planItemDefinitionId}")
	public String startAndCreateNewPlanItem(@PathVariable("caseInstId") String caseInstId,
			@PathVariable("planItemDefinitionId") String planItemDefinitionId, @RequestBody Map<String, Object> params) {
		return cmmnHelperService.startAndCreateNewPlanItem(caseInstId, planItemDefinitionId, params);
	}
	
	@PostMapping(path = "/processGSC/PSTN")
	public void processGSCPSTN(@RequestBody PlanItemRequestBean planItemBean) {

		logger.info("params :" + planItemBean);
		
		processL2OService.processGSCL2ODataToFlowable(planItemBean.getServiceId(),null,planItemBean.getPlanItemDefinitionId(),planItemBean.getPlanItem());
	
	}
}
