package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.CpeCostDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.CpeCostDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPRResponse;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.IzosdwanSapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * 
 * This file contains the IzosdwanLicensePRDelegate.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("izosdwanLicensePRDelegate")
public class IzosdwanLicensePRDelegate implements JavaDelegate{
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanLicensePRDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

	@Autowired
	IzosdwanSapService sapService;
	
	@Autowired
	CpeCostDetailsRepository cpeCostDetailsRepository;

	public void execute(DelegateExecution execution) {

		String errorMessage = "";
		String errorCode = "";
		String serviceCode="";

		Map<String, String> prMapper = new HashMap<>();
		Task task = workFlowService.processServiceTask(execution);
		try {

			Map<String, Object> processMap = execution.getVariables();
			serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			Integer componentId = (Integer) processMap.get("cpeOverlayComponentId");
			String cpeType = (String) processMap.get("cpeType");
			String vendorCode = (String) processMap.get("vendorCode");
			String vendorName = (String) processMap.get("vendorName");
			String bomName = null;
			ScServiceDetail serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
					"INPROGRESS");
			List<ScServiceAttribute> cpeChassisAttr = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndIsAdditionalParamAndAttributeValueAndCategory(
							serviceDetail.getId(), CommonConstants.Y, CommonConstants.Y, "CPE Basic Chassis", "CPE");
			for (ScServiceAttribute scServiceAttribute : cpeChassisAttr) {
				String serviceParamId = scServiceAttribute.getAttributeValue();
				if (StringUtils.isNotBlank(serviceParamId)) {
					Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf(serviceParamId));
					if (scAdditionalServiceParam.isPresent()) {
						String bomResponse = scAdditionalServiceParam.get().getValue();
						bomName = getBomName(bomResponse);
					}
				}
			}

			List<Map<String, String>> bomMapper = new ArrayList<>();
			List<String> categoryList = new ArrayList<>();
			categoryList.add("License");
			List<CpeCostDetails> cpeCostDetailsList = cpeCostDetailsRepository.findByServiceIdAndServiceCodeAndComponentIdAndVendorCodeAndCategoryIn(serviceId,serviceCode, componentId,vendorCode,categoryList);
			for (CpeCostDetails cpeCostDetails : cpeCostDetailsList) {
				Map<String, String> mstCostCatalogueMapper = new HashMap<>();
				if (cpeCostDetails.getMaterialCode() != null && cpeCostDetails.getServiceNumber()==null) {
					mstCostCatalogueMapper.put("MATERIAL_CODE", cpeCostDetails.getMaterialCode());
					mstCostCatalogueMapper.put("BOM_NAME", cpeCostDetails.getBundledBom());
					mstCostCatalogueMapper.put("PROD_CODE", cpeCostDetails.getProductCode());
					bomMapper.add(mstCostCatalogueMapper);
				}
			}

			if (!bomMapper.isEmpty()) {
				LOGGER.info("licensePRDelegate  invoked for  serviceCode={}, serviceId={}, cpeType={}", serviceCode,
						serviceId, cpeType);
				AutoPRResponse sapResponse = sapService.processAutoPr(serviceCode, cpeType, false,
						execution.getProcessInstanceId(), "License Based software/ Support", bomMapper,componentId,vendorCode);

				if (sapResponse.getPRResponse() != null
						&& (!sapResponse.getPRResponse().getPrStatus().equalsIgnoreCase("Failure")
								&& !sapResponse.getPRResponse().getPoStatus().equalsIgnoreCase("Failure"))) {

					prMapper.put("cpeLicencePoNumber", sapResponse.getPRResponse().getPoNumber());
					prMapper.put("cpeLicencePrNumber", sapResponse.getPRResponse().getPrNumber());
					prMapper.put("cpeLicencePoStatus", sapResponse.getPRResponse().getPoStatus());
					prMapper.put("cpeSupplyLicencePoDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeSupplyLicencePrDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeLicenseNeeded", "true");
					prMapper.put("cpeSupplyLicencePrType", "Auto");

					sapService.saveProCreation(serviceDetail, execution.getProcessInstanceId(), "LICENCE",
							sapResponse.getPRResponse().getPoNumber(), sapResponse.getPRResponse().getPrNumber(),
							sapResponse.getPRResponse().getPoStatus(), new Timestamp(System.currentTimeMillis()),
							execution.getCurrentActivityId(),vendorCode,vendorName,componentId);
					LOGGER.info("Persisting the PO NUMBER -> {} in CPE Cost Details",sapResponse.getPRResponse().getPoNumber());
					sapService.saveLicensePoNumberInCpeCostDetails(serviceId, componentId,
							sapResponse.getPRResponse().getPoNumber(), serviceCode, vendorCode);

					errorMessage = "CPE Licence Pr Number:" + sapResponse.getPRResponse().getPrNumber();
					errorMessage += ",CPE Licence Po Number:" + sapResponse.getPRResponse().getPoNumber();

					execution.setVariable("cpeLicencePoStatus", sapResponse.getPRResponse().getPoStatus());
					execution.setVariable("cpeLicenseNeeded", true);
				} else {
					execution.setVariable("cpeLicensePRCompleted", false);
					prMapper.put("cpeSupplyLicencePrType", "Manual");

				}
			} else {
				prMapper.put("cpeLicenseNeeded", "false");
				prMapper.put("cpeSupplyLicencePrType", "Manual");
				execution.setVariable("cpeLicenseNeeded", false);
				execution.setVariable("cpeLicencePoStatus", "NA");
				LOGGER.info("licensePRDelegate No Service Found for {}", serviceCode);
			}

		} catch (Exception e) {
			LOGGER.error("licensePRDelegate  Exception for service id:{} and exception {} ",serviceCode, e);
			execution.setVariable("cpeLicensePRCompleted", false);
			prMapper.put("cpeSupplyLicencePrType","Manual");

		}
		//componentAndAttributeService.updateAttributes(task.getServiceId(), prMapper,AttributeConstants.COMPONENT_LM,task.getSiteType());
		workFlowService.processServiceTaskCompletionWithAction(execution, errorMessage,"SUCCESS");

	}

	@SuppressWarnings("unchecked")
	private String getBomName(String bomResponse) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONArray data = (JSONArray) jsonParser.parse(bomResponse);
		Iterator<JSONObject> iterator = data.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonObj = (JSONObject) iterator.next();
			String bomName = (String) jsonObj.get("bomName");
			return bomName;
		}
		return null;
	}
}
