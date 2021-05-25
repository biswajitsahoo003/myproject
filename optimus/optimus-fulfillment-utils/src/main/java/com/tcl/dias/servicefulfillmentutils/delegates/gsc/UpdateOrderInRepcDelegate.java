package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsByCallTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsBySupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumbersBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("UpdateOrderInRepcDelegate")
public class UpdateOrderInRepcDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(UpdateOrderInRepcDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScAssetRepository scAssetRepository;
	
	@Value("${gsc.patchorderinrepc.url}")
	private String patchOrderInRepcUrl;
	
	@Value("${gsc.patchorderinrepc.authorization}")
	private String patchOrderInRepcAuthorization;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SimpleDateFormat o2cDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private List<String> scAssetAttributesList = Arrays.asList("supplierId", "routingNoReservationId", "vasNumberId",
			"detailsByCallType", "supplierActivationDate");

	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside Update order in REPC delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			// String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			requestForPatchOrderInRepc(serviceId, execution);
		} catch (Exception e) {
			logger.error("Exception in Update order in REPC {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

	private void requestForPatchOrderInRepc(Integer serviceId, DelegateExecution execution)
			throws TclCommonException, ParseException {
		Map<String, Object> executionVariables = execution.getVariables();
		String serviceCode = (String) executionVariables.get(SERVICE_CODE);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");
		headers.set("Authorization", patchOrderInRepcAuthorization);
		headers.set("Host", "");
		
		String taskDefKey = execution.getCurrentActivityId();
		taskDefKey = taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");

		HashMap<String, NumbersBean> requests = constructRequestForPatchOrderInRepc(executionVariables,
				serviceId, taskDefKey);
		for (Map.Entry<String, NumbersBean> entry : requests.entrySet()) {
			NumbersBean numbersBean = entry.getValue();
			String getUrl = getUrl(patchOrderInRepcUrl, new Object[] {entry.getKey()});
			String patchRequest = Utils.convertObjectToJson(numbersBean);
			AuditLog auditLog = gscService.saveAuditLog(patchRequest, null, serviceCode,
					"UpdateOrderInRepc-" + entry.getKey(), execution.getProcessInstanceId());
			logger.info("Patch update in REPC - request {}", patchRequest);
			RestResponse response = restClientService.patch(getUrl, patchRequest, headers);
			logger.info("Patch update in REPC - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				NumbersBean resNumbersBean = Utils.fromJson(response.getData(), new TypeReference<NumbersBean>() { });
				String processRepcResponse = processRepcUpdateResponse(numbersBean.getDetailsBySupplier(), resNumbersBean.getDetailsBySupplier());
				if(processRepcResponse.isEmpty()) {
					if(taskDefKey.equals("gsc-repc-order-billing-update")) {
						execution.setVariable(GscConstants.KEY_UPDATE_ORDER_IN_REPC_STATUS, GscConstants.VALUE_SUCCESS);
					} else {
						execution.setVariable(GscConstants.KEY_UPDATE_ORDER_IN_REPC_STATUS, "successNcallNas");
					}
					
					//to be set only when configured using temp outpluse
					execution.setVariable(GscConstants.KEY_FLOW_UPDATEREPCREQUIRED, GscConstants.NO);
				} else {
					execution.setVariable(GscConstants.KEY_UPDATE_ORDER_IN_REPC_STATUS, GscConstants.VALUE_FAILED);
					try {
						logger.info("Update order in REPC - error log started");
						componentAndAttributeService.updateAdditionalAttributes(serviceId,
								"UpdateOrderInRepcCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(processRepcResponse, response.getStatus().toString()),
								AttributeConstants.ERROR_MESSAGE, "gsc-repc-order-update");
					} catch (Exception e) {
						logger.error("Update order in REPC - error message details {}", e);
					}
				}
			} else {
				execution.setVariable(GscConstants.KEY_UPDATE_ORDER_IN_REPC_STATUS, GscConstants.VALUE_FAILED);
				try {
					logger.info("Update order in REPC - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"UpdateOrderInRepcCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-repc-order-update");
				} catch (Exception e) {
					logger.error("Update order in REPC - error message details {}", e);
				}
			}
		}
	}

	private HashMap<String, NumbersBean> constructRequestForPatchOrderInRepc(
			Map<String, Object> variables, Integer serviceId, String taskDefKey) throws TclCommonException, ParseException {
		HashMap<String, NumbersBean> requestBean = new HashMap<String, NumbersBean>();
		Integer flowGroupId = (Integer) variables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
		String accessType = (String) variables.get(GscConstants.ACCESS_TYPE);
		String orderType = "NEW";		
		if(variables.get(ORDER_TYPE) != null && !((String) variables.get(ORDER_TYPE)).isEmpty()) {
			orderType = (String) variables.get(ORDER_TYPE);
		}
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("cmsId", "terminationNumberIsdCode", "workingTemporaryTerminationNumber"));
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCode", "");
		if(terminationNumberIsdCode != null && !terminationNumberIsdCode.isEmpty()) {
			terminationNumberIsdCode = terminationNumberIsdCode.replace("+", "");
		} else {
			terminationNumberIsdCode = "";
		}
		scComponentAttributesmap.put("terminationNumberIsdCodeWithoutSymbol", terminationNumberIsdCode);
		List<GscScAsset> torScAssets;
		if(taskDefKey.equals("gsc-repc-order-billing-update")) {
			torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(serviceId, "Toll-Free", flowGroupId, GscNumberStatus.IN_SERVICE_ACCEPTED);
		} else {
			torScAssets = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(serviceId, flowGroupId, GscNumberStatus.IN_REPC_ORDER_UPDATE);
		}
		if(accessType != null && (accessType.equalsIgnoreCase("PUBLIC IP") || accessType.equalsIgnoreCase("MPLS"))) {
			accessType = "direct connection";
		} else {
			accessType = "pstn";
		}
		for (GscScAsset torScAsset : torScAssets) {
			constructDetailsBySupplierBean(torScAsset, requestBean, serviceId, accessType, scComponentAttributesmap, orderType);
		}
		return requestBean;
	}

	public DetailsBySupplierBean constructDetailsBySupplierBean(GscScAsset gscScAsset,
			HashMap<String, NumbersBean> requestBean, Integer serviceId, String accessType, Map<String, String> scComponentAttributesmap, String orderType) throws TclCommonException, ParseException {
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCodeWithoutSymbol", "");
		String vasNumberId = null;
		
		NumbersBean numbersBean = new NumbersBean();
		numbersBean.setDetailsBySupplier(new ArrayList<DetailsBySupplierBean>());
		ScAssetAttribute tollScAssetAttribute = scAssetAttributeRepository
				.findByScAsset_IdAndAttributeName(gscScAsset.getTollfreeId(), "billingStartDate");
		if(tollScAssetAttribute != null && !StringUtils.isEmpty(tollScAssetAttribute.getAttributeValue())) {
			Date date = o2cDateFormat.parse(tollScAssetAttribute.getAttributeValue());
			numbersBean.setInServiceDate(sdf.format(date));
		}
		
		DetailsBySupplierBean detailsBySupplierBean = new DetailsBySupplierBean();
		numbersBean.getDetailsBySupplier().add(detailsBySupplierBean);
		if(!gscScAsset.getRoutingName().equals("N/A")) {
			detailsBySupplierBean.setRoutingNo(gscScAsset.getRoutingName());
		}
		detailsBySupplierBean.setRoutingId(gscScAsset.getRoutingId());
		detailsBySupplierBean.setAccessNumber(terminationNumberIsdCode + gscScAsset.getTollfreeName());
		detailsBySupplierBean.setPlatformUpdateDate(sdf.format(new Date()));
		List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
				.findByScAsset_IdAndAttributeNameIn(gscScAsset.getRoutingId(), scAssetAttributesList);
		for (ScAssetAttribute scAssetAttribute : scAssetAttributes) {
			if (scAssetAttribute.getAttributeName().equals("supplierId")) {
				detailsBySupplierBean.setSupplierId(Integer.parseInt(scAssetAttribute.getAttributeValue()));
			} else if (scAssetAttribute.getAttributeName().equals("routingNoReservationId")) {
				detailsBySupplierBean.setRoutingNoReservationId(scAssetAttribute.getAttributeValue());
			} else if (scAssetAttribute.getAttributeName().equals("detailsByCallType")) {
				/*List<DetailsByCallTypeBean> detailsByCallTypeBean = Utils.fromJson(scAssetAttribute.getAttributeValue(),
						new TypeReference<List<DetailsByCallTypeBean>>() {
						});
				detailsBySupplierBean.setDetailsByCallType(detailsByCallTypeBean);*/
				detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, gscScAsset.getOutpulseName(), accessType, gscScAsset.getTollfreeName(), scComponentAttributesmap, orderType));
			} else if (scAssetAttribute.getAttributeName().equals("supplierActivationDate")) {
				Date date = o2cDateFormat.parse(scAssetAttribute.getAttributeValue());
				detailsBySupplierBean.setSupplierReadyDate(sdf.format(date));
			} else if (scAssetAttribute.getAttributeName().equals("vasNumberId")) {
				vasNumberId = scAssetAttribute.getAttributeValue();
			} else if (scAssetAttribute.getAttributeName().equals("platformUpdateDate")) {
				Date date = o2cDateFormat.parse(scAssetAttribute.getAttributeValue());
				detailsBySupplierBean.setPlatformUpdateDate(sdf.format(date));
			}
		}
		if (vasNumberId != null) {
			/*if (requestBean.containsKey(vasNumberId)) {
				requestBean.get(vasNumberId).add(detailsBySupplierBean);
			} else {
				List<DetailsBySupplierBean> detailsBySupplierBeans = new ArrayList<DetailsBySupplierBean>();
				detailsBySupplierBeans.add(detailsBySupplierBean);
				requestBean.put(vasNumberId, detailsBySupplierBeans);
			}*/
			requestBean.put(vasNumberId, numbersBean);
		}
		return detailsBySupplierBean;
	}

	public List<DetailsByCallTypeBean> constructDetailsByCallTypeBean(Integer serviceId, String outpulse, String accessType,  String accessNumber, Map<String, String> scComponentAttributesmap, String orderType)
			throws TclCommonException {
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCodeWithoutSymbol", "");
		List<DetailsByCallTypeBean> detailsByCallType = new ArrayList<>();
		List<String> repcCallTypeList = gscService.getRepcCallTypeList(serviceId);
		repcCallTypeList.forEach(callType -> {
			DetailsByCallTypeBean detailsByCallTypeBean = new DetailsByCallTypeBean();
			detailsByCallTypeBean.setCallType(callType);
			detailsByCallTypeBean.setBlocked("N");
			detailsByCallTypeBean.setCustOutpulsedDigits(outpulse);
			if("Same As Number".equalsIgnoreCase(outpulse)) {
				if(accessNumber != null) {
					detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + accessNumber);
				} else {
					detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + scComponentAttributesmap.getOrDefault("workingTemporaryTerminationNumber", ""));
				}
			} else {
				detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + outpulse);
			}
			detailsByCallTypeBean.setAccessType(accessType);
			// detailsByCallTypeBean.setPstnRoutingCMS(44091);
			if(accessType.equals("direct connection")) {
				ScServiceDetail sipServiceDetailByChildService;
				if(orderType.equalsIgnoreCase("MACD")) {
					sipServiceDetailByChildService = gscService.getParentServiceDetail(serviceId);
				} else {
					sipServiceDetailByChildService = gscService.getSipServiceDetailByChildService(serviceId);
				}
				Map<String, String> sipAttributes = commonFulfillmentUtils.getComponentAttributes(sipServiceDetailByChildService.getId(), AttributeConstants.COMPONENT_GSCLICENSE,
						"A", Arrays.asList("interconnectId"));
				detailsByCallTypeBean.setDirectConnectionId(Integer.parseInt(sipAttributes.getOrDefault("interconnectId", "0")));
			}
			detailsByCallTypeBean
					.setPstnRoutingCMS(Integer.parseInt(scComponentAttributesmap.getOrDefault("cmsId", null)));
			detailsByCallType.add(detailsByCallTypeBean);
		});
		return detailsByCallType;
	}
	
	public String getUrl(String baseUrl, Object[] args) {
		return MessageFormat.format(baseUrl, args);
	}
	
	private String processRepcUpdateResponse(List<DetailsBySupplierBean> requestBean, List<DetailsBySupplierBean> responseBean) throws TclCommonException {
    	StringBuilder response = new StringBuilder();
		if (responseBean != null) {
			HashMap<String, DetailsBySupplierBean> responseMapper = new HashMap<String, DetailsBySupplierBean>();
			for (DetailsBySupplierBean supplierDetail : requestBean) {
				responseMapper.put(extractRepcNumberKey(supplierDetail), supplierDetail);
			}

			for (DetailsBySupplierBean supplierDetail : responseBean) {
				Boolean errorFlag = false;
				for (DetailsByCallTypeBean callTypeDetail : supplierDetail.getDetailsByCallType()) {
					if (callTypeDetail.getStatus() != null
							&& callTypeDetail.getStatus().equalsIgnoreCase("FAILED")) {
						response.append(callTypeDetail.getStatusMsg()+"\n");	
						errorFlag = true;
					}
				}
				DetailsBySupplierBean detailsBySupplierBean = responseMapper.get(extractRepcNumberKey(supplierDetail));
				if(detailsBySupplierBean != null) {
					Map<String, String> atMap = new HashMap<String, String>();
					if(supplierDetail.getDetailsByCallType() != null) {
						atMap.put("detailsByCallType", Utils.convertObjectToJson(supplierDetail.getDetailsByCallType()));
					}
					if(errorFlag) {
						atMap.put("repcOrderUpdateStatus", "FAILED");	
					} else {
						atMap.put("repcOrderUpdateStatus", "SUCCESS");
					}
					if(detailsBySupplierBean.getAccessNumberId() != null) {
						gscService.updatedScAssetAttribute(detailsBySupplierBean.getAccessNumberId(), atMap, "system");
					}
					if(detailsBySupplierBean.getRoutingId() != null) {
						gscService.updatedScAssetAttribute(detailsBySupplierBean.getRoutingId(), atMap, "system");
					}
				}
			}
		}
		return response.toString();
    }
    
    private String extractRepcNumberKey(DetailsBySupplierBean supplierDetail) {
    	StringBuilder key = new StringBuilder();
    	/*if(supplierDetail.getSupplierId() != null) {
    		key.append(supplierDetail.getSupplierId());
    	}
    	if(supplierDetail.getInventoryBankNumberId() != null) {
    		key.append(supplierDetail.getInventoryBankNumberId());
    	}*/
    	if(supplierDetail.getAccessNumber() != null) {
    		key.append(supplierDetail.getAccessNumber());
    	}
    	if(supplierDetail.getRoutingNo() != null) {
    		key.append(supplierDetail.getRoutingNo());
    	}
    	return key.toString();
    }
}
