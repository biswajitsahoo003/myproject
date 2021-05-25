package com.tcl.dias.servicehandover.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.CustomerCrnRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.service.BillingHandoverService;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.RedirectWpsToIpc;
import com.tcl.dias.servicehandover.wps.beans.OptimusAccoutInputBO;
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;
import com.tcl.dias.servicehandover.wps.beans.UpdateAccountSyncStatusResponse;
import com.tcl.dias.servicehandover.wps.beans.UpdateOrderSyncStatusResponse;

/**
 *  Service Class for WPS Async Request handling
 *  @author yogesh
 */

@Service
public class WpsAsyncServiceDev extends ServiceFulfillmentBaseService{

	@Autowired
	ServicehandoverAuditRepository servicehandoverRepository;

	@Autowired
	BillingHandoverService billingHandoverService;
	
	@Autowired
	BillingProductCreationService networkBillingHandoverService;
	
	@Autowired
	RuntimeService runtimeService;
	 
	@Autowired
	CustomerCrnRepository customerCrnRepository;
		
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	TaskCacheService taskCacheService;

	String processInstanceId="";	
	
	@Autowired
	RestClientService restClientService;
	
	@Autowired
	RedirectWpsToIpc redirectWpsToIpc;
	
	@Value("${application.env:PROD}")
	String AppEnv;
	
	String activityId="";

	Integer serviceId=0;
	
	@Value("${oms.o2c.macd.queue}")
	String omsO2CMacdQueue;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(WpsAsyncServiceDev.class);

	
	/**
	 * Method for Async Account Req from WPS for Network Products
	 * @param accountInfo
	 * @return
	 * @throws Exception 
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public UpdateAccountSyncStatusResponse accountAsyncforNetwork(OptimusAccoutInputBO accountInfo) throws Exception {
		UpdateAccountSyncStatusResponse accountSyncStatusResponse = new UpdateAccountSyncStatusResponse();
		logger.info("inside accountSync service for group id {} accountInfo={}",accountInfo.getInputGroupId(), accountInfo);
			TimeUnit.SECONDS.sleep(30);
			boolean isAccountCreated = true;
			ScChargeLineitem chargeLineitem = chargeLineitemRepository.findFirstByInputGroupId(StringUtils.trimToEmpty(accountInfo.getInputGroupId()));
			logger.info("inside accountSync service for group id {} account number={}",accountInfo.getInputGroupId(), accountInfo.getGenevaAccountNumber());
			if(Objects.nonNull(chargeLineitem)) {
				String activityId=accountInfo.getGenevaAccountNumber().contains("CPE")?"cpe_billing_account_creation_async":"billing_account_creation_async";
				logger.info("Activity picked for Account async {}", activityId);
				if(Objects.nonNull(accountInfo.getAccountSyncResponse())&& accountInfo.getAccountSyncResponse().equals(NetworkConstants.SUCCESS)) {
						Map<String, String> atMap = new HashMap<>();
						atMap.put("billingAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
						if (accountInfo.getGenevaAccountNumber().contains("CPE")) {
							atMap.put("cpeAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
						}
						else {
							atMap.put("nonCpeAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
						}
						List<ScChargeLineitem> lineitems = chargeLineitemRepository.findByInputGroupId(accountInfo.getInputGroupId());
						lineitems.stream().forEach(lineitem->{
							lineitem.setAccountNumber(accountInfo.getGenevaAccountNumber());
							chargeLineitemRepository.saveAndFlush(lineitem);
						});
						componentAndAttributeService.updateAttributes(Integer.parseInt(chargeLineitem.getServiceId()), atMap, AttributeConstants.COMPONENT_LM,"A");
						TimeUnit.SECONDS.sleep(20);
				}
				else {
					isAccountCreated = false;
				}
				Task task = getTaskData(Integer.parseInt(chargeLineitem.getServiceId()), activityId);
				if(task!=null) {
					logger.info("process instance Id {} and activity Id{}", processInstanceId, activityId);
					Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId()).activityId(activityId).singleResult();
					if (Objects.nonNull(chargeLineitem)) {
						if(execution!=null) {
							logger.info("accountAsyncforNetwork execution for Account async", execution.getProcessInstanceId());
							runtimeService.setVariable(execution.getId(), "accountCreationStatus", isAccountCreated); 
							runtimeService.trigger(execution.getId());
						}else {
							logger.info("accountAsyncforNetwork execution is null for {} ", task.getWfProcessInstId());
						}
					}
				}
			}
			accountSyncStatusResponse.setResult(Constants.RESPONSE);
			logger.info("accountSync service completed");
		return accountSyncStatusResponse;
	}
	/**
	 * Method for Async Order Req from WPS for Network products
	 * @param orderInfo
	 * @return
	 * @throws Exception 
	 */
	public UpdateOrderSyncStatusResponse orderAsyncForNetwork(OptimusProductInputBO orderInfo)
			throws Exception {
		logger.info("inside orderSync service for order number={}", orderInfo.getOrderNumber());
		logger.info("inside orderSync service for Line Items={}", orderInfo.getOrderLineItemNumberAndStatus());
		TimeUnit.SECONDS.sleep(20);
		UpdateOrderSyncStatusResponse orderSyncStatusResponse = new UpdateOrderSyncStatusResponse();
		activityId = orderInfo.getServiceType().contains("CPE")?"cpe_product_commissioning_async":"product_commissioning_async";
			String lineItems[] = orderInfo.getOrderLineItemNumberAndStatus().split(";");
			Arrays.stream(lineItems).forEach((lineItem) -> {
				String sourceProductSeq = lineItem.split("=")[0];
				ScChargeLineitem chargeLineitem = chargeLineitemRepository.findFirstBySourceProdSequence(sourceProductSeq);
				if (Objects.nonNull(chargeLineitem)) {
					chargeLineitem.setStatus(lineItem.split("=")[1].substring(0, 7));
					serviceId = Integer.parseInt(chargeLineitem.getServiceId());
					if (chargeLineitem.getActionType() != null
							&& chargeLineitem.getActionType().equals(NetworkConstants.TERMINATE)) {
						activityId = "product-termination-async";
						ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrderUuid(chargeLineitem.getServiceCode(), orderInfo.getOrderNumber());
						if(scServiceDetail!=null) {
							serviceId=scServiceDetail.getId();
						}
					}
					chargeLineitemRepository.saveAndFlush(chargeLineitem);
				}
			});
			Task task = getTaskData(serviceId, activityId);
			if(task!=null) {
				logger.info("process instance Id {} and activity Id{}", processInstanceId, activityId);
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId()).activityId(activityId).singleResult();
				if (execution != null) {
					logger.info("orderasync trigger execution for {}", execution.getProcessInstanceId());
					if (Objects.nonNull(orderInfo.getOrderSyncResponse())&& !NetworkConstants.CPE.equals(orderInfo.getServiceType())) {
						runtimeService.setVariable(execution.getId(), "productCommissioningStatus", true);
						runtimeService.trigger(execution.getId());
					}
					if (Objects.nonNull(orderInfo.getOrderSyncResponse())&& NetworkConstants.CPE.equals(orderInfo.getServiceType())) {
						runtimeService.setVariable(execution.getId(), "cpeProductCommissioningStatus", true);
						runtimeService.trigger(execution.getId());
					}
				} else {
					logger.info("orderSync execution is null for {} ", processInstanceId);
				}
			}else {
				logger.info("Task is null for service id {} and activity id{} ", serviceId,activityId);
			}
			orderSyncStatusResponse.setResult(Constants.RESPONSE);
			logger.info("orderSync service completed");
		
		return orderSyncStatusResponse;
	}
	
	/**
	 * Method for Invoice Entry in Audit table for Network Products
	 * @param isCommFailure
	 * @param audit
	 */
	public void generateInvoiceAuditEntry(OptimusProductInputBO orderInput,String processInstanceId) {
		logger.info("inside network generateInvoiceAuditEntry for order number={}", orderInput.getOrderNumber());
		ServicehandoverAudit audit = servicehandoverRepository.findByOrderId(orderInput.getOrderNumber(),orderInput.getServiceType(),NetworkConstants.ACCOUNT_CREATION);
		if(Objects.nonNull(audit)) {
			//ScChargeLineitem chargeLineitem = chargeLineitemRepository.findByServiceIdAndServiceTypeforInvoice(audit.getServiceId(),orderInput.getServiceType());
			ServicehandoverAudit invoiceEntry = new ServicehandoverAudit();
			invoiceEntry.setLegalEntity(audit.getLegalEntity());
			invoiceEntry.setOrderId(orderInput.getOrderNumber());
			invoiceEntry.setCrnId(audit.getCrnId());
			invoiceEntry.setOpportunityId(audit.getOpportunityId());
			invoiceEntry.setServiceId(audit.getServiceId());
			invoiceEntry.setCustomerType(Constants.DOMESTIC_CUSTOMER);
			invoiceEntry.setRequestType(Constants.INVOICE_GEN);
			invoiceEntry.setStatus(Constants.IN_PROGRESS);
			invoiceEntry.setCreatedDate(new Date());
			invoiceEntry.setAccountNumber(audit.getAccountNumber());
			invoiceEntry.setProcessInstanceId(processInstanceId);
			invoiceEntry.setServiceType(orderInput.getServiceType());
			invoiceEntry.setProviderSegment(audit.getProviderSegment());
			servicehandoverRepository.save(invoiceEntry);
		}
		
		logger.info("network generateInvoiceAuditEntry completed");
	}
	
}
