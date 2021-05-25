package com.tcl.dias.servicefulfillmentutils.delegates;

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
import com.tcl.dias.servicefulfillment.entity.entities.CpeMaterialRequestDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPRResponse;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("hardwarePRDelegate")
public class HardwarePRDelegate implements JavaDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(HardwarePRDelegate.class);


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
	SapService sapService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;
	

	public void execute(DelegateExecution execution) {

		String errorMessage = "";
		String errorCode = "";
		Map<String, String> prMapper = new HashMap<>();
		Task task = workFlowService.processServiceTask(execution);
		try {

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String cpeType = (String) processMap.get("cpeType");
			String vendorCode = (String) processMap.get("vendorCode");
			String bomName = null;
			LOGGER.info("HardwarePRDelegate  invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);
			ScServiceDetail serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
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
			List<MstCostCatalogue> mstCostCatalogues = mstCostCatalogueRepository.findByDistinctBundledBom(bomName);
			for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {
				Map<String, String> mstCostCatalogueMapper = new HashMap<>();
				if ((mstCostCatalogue.getSaleMaterialCode() != null
						|| mstCostCatalogue.getRentalMaterialCode() != null) && mstCostCatalogue.getServiceNumber()==null) {
					mstCostCatalogueMapper.put("SALES_CODE", mstCostCatalogue.getSaleMaterialCode());
					mstCostCatalogueMapper.put("RENTAL_CODE", mstCostCatalogue.getRentalMaterialCode());
					mstCostCatalogueMapper.put("BOM_NAME", mstCostCatalogue.getBundledBom());
					bomMapper.add(mstCostCatalogueMapper);
				}
			}
			
			if (!bomMapper.isEmpty()) {
				String typeOfExpenses = "Outright Sale";
				execution.setVariable("typeOfExpenses", typeOfExpenses);
				AutoPRResponse sapResponse = sapService.processAutoPr(serviceCode, cpeType, true,
						execution.getProcessInstanceId(), typeOfExpenses, bomMapper,vendorCode);
				
				if(sapResponse.getPRResponse()!=null && !sapResponse.getPRResponse().getPrStatus().equalsIgnoreCase("Failure") && !sapResponse.getPRResponse().getPoStatus().equalsIgnoreCase("Failure")) {				
					prMapper.put("cpeSupplyHardwarePoNumber", sapResponse.getPRResponse().getPoNumber());
					prMapper.put("cpeSupplyHardwarePrNumber", sapResponse.getPRResponse().getPrNumber());
					prMapper.put("cpeSupplyHardwarePoStatus", sapResponse.getPRResponse().getPoStatus());
					prMapper.put("cpeSupplyHardwarePrStatus", sapResponse.getPRResponse().getPrStatus());
					prMapper.put("cpeSupplyHardwarePoDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeSupplyHardwarePrDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeSupplyHardwarePrType","Auto");

					
					execution.setVariable("cpeHardwarePoNumber", sapResponse.getPRResponse().getPoNumber());
					execution.setVariable("cpeHardwarePoStatus", sapResponse.getPRResponse().getPoStatus());
					execution.setVariable("cpeHardwarePRCompleted", true);
					
					errorMessage="CPE Supply Hardware Pr Number:"+sapResponse.getPRResponse().getPrNumber() ;
					errorMessage+=", CPE Supply Hardware Po Number:"+sapResponse.getPRResponse().getPoNumber();
				}else {
					prMapper.put("cpeSupplyHardwarePoNumber", sapResponse.getPRResponse().getPoNumber());
					prMapper.put("cpeSupplyHardwarePrNumber", sapResponse.getPRResponse().getPrNumber());
					prMapper.put("cpeSupplyHardwarePoStatus", sapResponse.getPRResponse().getPoStatus());
					prMapper.put("cpeSupplyHardwarePrStatus", sapResponse.getPRResponse().getPrStatus());
					prMapper.put("cpeSupplyHardwarePoDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeSupplyHardwarePrDate", DateUtil.convertDateToString(new Date()));
					prMapper.put("cpeSupplyHardwarePrType","Manual");

					execution.setVariable("cpeHardwarePoNumber", sapResponse.getPRResponse().getPoNumber());
					execution.setVariable("cpeHardwarePoStatus", sapResponse.getPRResponse().getPoStatus());
					execution.setVariable("cpeHardwarePRCompleted", false);
				}
				
				sapService.saveProCreation(serviceDetail, execution.getProcessInstanceId(), "HARDWARE",
						sapResponse.getPRResponse().getPoNumber(), sapResponse.getPRResponse().getPrNumber(),
						sapResponse.getPRResponse().getPoStatus(), new Timestamp(System.currentTimeMillis()),
						execution.getCurrentActivityId(),vendorCode);

			} else {
				LOGGER.info("No Hardware found");
				execution.setVariable("cpeHardwarePRCompleted", false);
				prMapper.put("cpeSupplyHardwarePrStatus", "Failure");
				prMapper.put("cpeSupplyHardwarePrType","Manual");
			}
				

				
		} catch (Exception e) {
			LOGGER.error("HardwarePRDelegate  Exception {} ", e);
			execution.setVariable("cpeHardwarePRCompleted", false);
			prMapper.put("cpeSupplyHardwarePrType","Manual");
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), prMapper,AttributeConstants.COMPONENT_LM,task.getSiteType());
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
