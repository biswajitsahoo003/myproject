package com.tcl.dias.servicehandover.service;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.CustomerCrnRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.service.BillingHandoverService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountAndLineItemService;
import com.tcl.dias.servicehandover.util.RedirectWpsToIpc;
import com.tcl.dias.servicehandover.wps.beans.InvoiceOperationInput;
import com.tcl.dias.servicehandover.wps.beans.OptimusAccoutInputBO;
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;
import com.tcl.dias.servicehandover.wps.beans.UpdateAccountSyncStatusResponse;
import com.tcl.dias.servicehandover.wps.beans.UpdateInvoiceStatusResponse;
import com.tcl.dias.servicehandover.wps.beans.UpdateOrderSyncStatusResponse;

/**
 *  Service Class for WPS Async Request handling
 *  @author yogesh
 */

@Service
public class WpsAsyncService extends ServiceFulfillmentBaseService{

	@Autowired
	ServicehandoverAuditRepository servicehandoverRepository;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;

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
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	TaskCacheService taskCacheService;

	boolean isCommFailure = false;
	
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
	
    private Boolean updateServiceStatusFlag;
    
    private Boolean productCommissioningStatus;
    
    @Autowired
    WpsAsyncServiceDev wpsAsyncServiceDev;
	
	 
	 private static final Logger logger = LoggerFactory.getLogger(WpsAsyncService.class);

	/**
	 * Method for Async Account Req from WPS
	 * @param accountInfo
	 * @return
	 * @throws InterruptedException 
	 */
	public UpdateAccountSyncStatusResponse accountAsync(OptimusAccoutInputBO accountInfo) throws InterruptedException {
		logger.info("inside accountSync service for account number={} ", accountInfo.getGenevaAccountNumber());
		UpdateAccountSyncStatusResponse accountSyncStatusResponse = null;
		if (accountInfo.getInputGroupId().contains("IPC")) {
			try {
				if ("DEV".equalsIgnoreCase(AppEnv) && accountInfo.getInputGroupId().contains(IpcConstants.REGEX_DEV_188)) {
					accountInfo.setInputGroupId(accountInfo.getInputGroupId().replace(IpcConstants.REGEX_DEV_188, CommonConstants.EMPTY));
					redirectWpsToIpc.asyncAccount(IpcConstants.DEV_IP_188, accountInfo);
				} else if ("DEV".equalsIgnoreCase(AppEnv) && accountInfo.getInputGroupId().contains(IpcConstants.REGEX_DEV_104)) {
					accountInfo.setInputGroupId(accountInfo.getInputGroupId().replace(IpcConstants.REGEX_DEV_104, CommonConstants.EMPTY));
					redirectWpsToIpc.asyncAccount(IpcConstants.DEV_IP_104, accountInfo);
				} else {
					String scServiceId = accountInfo.getInputGroupId().split("_")[3];
					Optional<ScServiceDetail> scServiceDetailOpt= scServiceDetailRepository.findById(Integer.parseInt(scServiceId));
					if( scServiceDetailOpt.isPresent()) {
						ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
						TimeUnit.SECONDS.sleep(20);
						accountSyncStatusResponse = new UpdateAccountSyncStatusResponse();
						Task task = getTaskData(scServiceDetail.getId(), "ipc_billing_account_creation_async");
						if (Objects.nonNull(task)) {
							Map<String, String> attrMap = new HashMap<>();
							attrMap.put(IpcConstants.IPC_BILLING_ACCOUNT_NUMBER, accountInfo.getGenevaAccountNumber());
							billingHandoverService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
							ipcChargeLineitemRepository.updateAccountNumber(accountInfo.getGenevaAccountNumber(), task.getServiceId().toString(), task.getServiceType());
							ipcChargeLineitemRepository.flush();
							Execution execution = runtimeService.createExecutionQuery()
									.processInstanceId(task.getWfProcessInstId())
									.activityId("ipc_billing_account_creation_async").singleResult();
							if (execution != null) {
								if (Objects.nonNull(accountInfo.getGenevaAccountNumber()) && Constants.SUCCESS.equals(accountInfo.getAccountSyncResponse())) {
									runtimeService.setVariable(execution.getId(), "accountCreationStatus", true);
									runtimeService.trigger(execution.getId());
								}
							}
						}
						accountSyncStatusResponse.setResult(Constants.RESPONSE);
					} else {
						logger.info("Service Detail not found for Service ID:{}", scServiceId);
					}
				}
			} catch (Exception ex) {
				logger.error("Error occured: {}",ex.getMessage());
				ex.printStackTrace();
			}
		} else {
			try {
				accountSyncStatusResponse = accountAsyncforNetwork(accountInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("accountSync service completed");
		return accountSyncStatusResponse;
	}

	/**
	 * Method for Async Order Req from WPS
	 * @param orderInfo
	 * @return
	 * @throws InterruptedException 
	 * @throws UnknownHostException 
	 */
	public UpdateOrderSyncStatusResponse orderAsync(OptimusProductInputBO orderInfo) throws InterruptedException {
		logger.info("inside orderSync service for order number={}", orderInfo.getOrderNumber());
		logger.info("inside orderSync service for Line Items={}", orderInfo.getOrderLineItemNumberAndStatus());
		UpdateOrderSyncStatusResponse orderSyncStatusResponse = null;
		if (orderInfo.getOrderNumber().contains("IPC")) {
			ScServiceDetail scServiceDetail = null;
			String lineItems[] = orderInfo.getOrderLineItemNumberAndStatus().split(";");
			updateServiceStatusFlag = true;
            String sourceProdSeq = Arrays.stream(lineItems).findFirst().get().split("=")[0];
            List<IpcChargeLineitem> chrgeLineItmL = ipcChargeLineitemRepository.findByServiceTypeAndSourceProductSequence(IpcConstants.IPC, Integer.parseInt(sourceProdSeq));
			if(!chrgeLineItmL.isEmpty()) {
				scServiceDetail = scServiceDetailRepository.findByScOrderUuidAndId(orderInfo.getOrderNumber(), Integer.parseInt(chrgeLineItmL.get(0).getServiceId()));
			}
            if(scServiceDetail != null) {
				orderSyncStatusResponse = new UpdateOrderSyncStatusResponse();
				TimeUnit.SECONDS.sleep(30);
			    activityId="ipc_product_commissioning_async";
			    productCommissioningStatus = true;
			    updateServiceStatusFlag = true;
	            Arrays.stream(lineItems).forEach((lineItem) -> {
	                try {
		            	Integer sourceProductSeq = Integer.parseInt(lineItem.split("=")[0]);
		                List<IpcChargeLineitem> ipcChargeLineitemL = ipcChargeLineitemRepository.findByServiceTypeAndSourceProductSequence(IpcConstants.IPC, sourceProductSeq);
		                ipcChargeLineitemL.forEach(ipcChargeLineitem -> {
			                if (Objects.nonNull(ipcChargeLineitem)) {
			                    ipcChargeLineitem.setStatus(lineItem.split("=")[1].substring(0, 7));
			                    if(lineItem.split("=")[1].contains(IpcConstants.COMPLETE_THE_TAX_CAPTURE_TASK)) {
			                    	ipcChargeLineitem.setStatus(ipcChargeLineitem.getStatus()+IpcConstants.UNDERSCORE+IpcConstants.COMPLETE_THE_TAX_CAPTURE_TASK);
			                    }
			                    if (ipcChargeLineitem.getActionType() != null
			                            && ipcChargeLineitem.getActionType().equals(NetworkConstants.TERMINATE)) {
			                        activityId = "ipc-product-termination-async";
			                    }
			                    if(productCommissioningStatus) {
			                    	productCommissioningStatus = ipcChargeLineitem.getStatus().contains(IpcConstants.SUCCESS);
			                    } 
			                    if (updateServiceStatusFlag) {
			                    	updateServiceStatusFlag = ipcChargeLineitem.getStatus().equals(IpcConstants.SUCCESS);
			                    }
			                    serviceId = Integer.parseInt(ipcChargeLineitem.getServiceId());
			                    ipcChargeLineitemRepository.save(ipcChargeLineitem);
			                }
		                });
	                } catch (Exception e) {
	                	logger.error("Error Message : {}",e.getMessage());
					}
	            });
	            logger.info("service ID: {}, activity ID: {} ", serviceId, activityId);
	            Task task = getTaskData(serviceId, activityId);
		        if(task!=null) {
		            logger.info("process instance Id {} ", task.getWfProcessInstId());
		            Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId()).activityId(activityId).singleResult();
		        	if(execution!=null) {
						logger.info("orderasync trigger execution for {}", execution.getProcessInstanceId());
						if(Objects.nonNull(orderInfo.getOrderSyncResponse())) {
							runtimeService.setVariable(execution.getId(), "productCommissioningStatus", productCommissioningStatus);
							runtimeService.trigger(execution.getId());
						}
					}else {
						logger.info("orderSync execution is null for {} ", task.getWfProcessInstId());
					}
		        	if(activityId.equals("ipc_product_commissioning_async")) {
		        		if(updateServiceStatusFlag) {
		        			scServiceDetail = task.getScServiceDetail();
							ipcBillingAccountAndLineItemService.triggerPayPerUseCommissionedInfoToCatalyst(scServiceDetail);
							scServiceDetail.setServiceStatus(IpcConstants.COMPLETED);
							scServiceDetailRepository.save(scServiceDetail);
							try {
								mqUtils.send(omsO2CMacdQueue, orderInfo.getOrderNumber()+IpcConstants.SPECIAL_CHARACTER_COMMA+scServiceDetail.getPopSiteCode());
							}catch (Exception ex) {
								logger.error("Exception while trigerring mqutils call for updating the stage of macd order to MACD_ORDER_COMMISSIONED");
							}
		        		}
		        	}
	            } else {
	            	logger.info("Task is null");
	            }
				orderSyncStatusResponse.setResult(Constants.RESPONSE);
			} else if( "DEV".equalsIgnoreCase(AppEnv)) {
				try {
					logger.info("Order Code Availability Checking in: {}, for Order code:{}", IpcConstants.DEV_IP_188, orderInfo.getOrderNumber());
					String response = redirectWpsToIpc.isOrderAvailable(IpcConstants.DEV_IP_188, orderInfo.getOrderNumber(), sourceProdSeq);
					logger.info("Order code:{}, Availability: {}", orderInfo.getOrderNumber(), response);
					if(response.equals("SUCCESS")) {
						logger.info("Redirecting WPS Async Call to: {}, for Order code:{}", IpcConstants.DEV_IP_188, orderInfo.getOrderNumber());
						redirectWpsToIpc.asyncOrder(IpcConstants.DEV_IP_188, orderInfo);
					} else {
						logger.info("Redirecting WPS Async Call to: {}, for Order code:{}", IpcConstants.DEV_IP_104, orderInfo.getOrderNumber());
						redirectWpsToIpc.asyncOrder(IpcConstants.DEV_IP_104, orderInfo);
					}
				} catch (Exception ex) {
					logger.error("Error occured: {}",ex.getMessage());
					ex.printStackTrace();
				}
			}
			logger.info("orderSync service completed");
		} else {
			try {
				orderSyncStatusResponse = orderAsyncForNetwork(orderInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orderSyncStatusResponse;
	}

	/**
	 * Method for Async Invoice Req from WPS
	 * @param invoiceInfo
	 * @return
	 */
	public UpdateInvoiceStatusResponse invoiceAsync(InvoiceOperationInput invoiceInfo) {
		logger.info("inside invoiceSync service for order number={}", invoiceInfo.getTransactionId());
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrderUuid(invoiceInfo.getServiceId(),invoiceInfo.getTransactionId());
		Task task = getTaskData(scServiceDetail.getId(), "generate_cpe_invoice_async");
	    UpdateInvoiceStatusResponse invoiceStatusResponse = new UpdateInvoiceStatusResponse();
		if (Objects.nonNull(scServiceDetail) && task != null) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("cpeInvoiceNumber", String.valueOf(invoiceInfo.getInvoiceNumber()));
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId())
					.activityId("generate_cpe_invoice_async").singleResult();
			if (execution != null) {
				logger.info("invoiceAsync execution for process id {} ", execution.getProcessInstanceId());
				runtimeService.setVariable(execution.getId(), "cpeInvoiceCompleted", true);
				runtimeService.trigger(execution.getId());
			}

		}
		invoiceStatusResponse.setResult(Constants.RESPONSE);
		logger.info("invoiceSync service completed");
		return invoiceStatusResponse;
	}

	/**
	 * Method for Invoice Entry in Audit table 
	 * @param isCommFailure
	 * @param audit
	 */
	public void generateInvoiceAuditEntry(ServicehandoverAudit audit) {
		logger.info("inside generateInvoiceAuditEntry for order number={}", audit.getOrderId());
		audit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
		audit.setRequestType(Constants.INVOICE_GEN);
		audit.setStatus(Constants.IN_PROGRESS);
		audit.setCreatedDate(new Date());
		servicehandoverRepository.save(audit);
		logger.info("generateInvoiceAuditEntry completed");
	}
	
	/**
	 * Method for Async Account Req from WPS for Network Products
	 * @param accountInfo
	 * @return
	 * @throws Exception 
	 */
	/*@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public UpdateAccountSyncStatusResponse accountAsyncforNetwork(OptimusAccoutInputBO accountInfo) throws Exception {
		UpdateAccountSyncStatusResponse accountSyncStatusResponse = new UpdateAccountSyncStatusResponse();
		if("DEV".equalsIgnoreCase(AppEnv)) {
			wpsAsyncServiceDev.accountAsyncforNetwork(accountInfo);
<<<<<<< HEAD
		}
		else if ("DEV".equalsIgnoreCase(AppEnv) &&  accountInfo.getInputGroupId().contains("UCW")) {
			redirectWpsToIpc.asyncAccount("10.133.208.146", accountInfo);
=======
>>>>>>> fluorine
		}
		else{
			logger.info("inside accountSync service for group id {} accountInfo={}",accountInfo.getInputGroupId(), accountInfo);
			TimeUnit.SECONDS.sleep(20);
			boolean isAccountCreated = true;
			ServicehandoverAudit audit = servicehandoverRepository.findByGenevaGrpId(StringUtils.trimToEmpty(accountInfo.getInputGroupId()));
			logger.info("inside accountSync service for group id {} account number={} audit={}",accountInfo.getInputGroupId(), accountInfo.getGenevaAccountNumber(),Objects.nonNull(audit));
			if(Objects.nonNull(audit)) {
				String activityId=accountInfo.getGenevaAccountNumber().contains("CPE")?"cpe_billing_account_creation_async":"billing_account_creation_async";
				logger.info("Activity picked for Account async {}", activityId);
				if(Objects.nonNull(accountInfo.getAccountSyncResponse())&& accountInfo.getAccountSyncResponse().equals(NetworkConstants.SUCCESS)) {
					audit.setAccountNumber(accountInfo.getGenevaAccountNumber());
					audit.setStatus(NetworkConstants.SUCCESS);
					Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(audit.getServiceId()));
					if(Objects.nonNull(scServiceDetail)) {
						Map<String, String> atMap = new HashMap<>();
						atMap.put("billingAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
						if (accountInfo.getGenevaAccountNumber().contains("CPE")) {
							atMap.put("cpeAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
							chargeLineitemRepository.updateAccountNumber(accountInfo.getGenevaAccountNumber(), audit.getServiceId(), audit.getServiceType());
						}
						else if(accountInfo.getGenevaAccountNumber().contains("NPL")) {
							chargeLineitemRepository.updateAccountNumberNPL(accountInfo.getGenevaAccountNumber(),
									accountInfo.getServiceType(), audit.getServiceId(), "NPL", audit.getSiteType());
						}
						else {
							atMap.put("nonCpeAccountNumber", String.valueOf(accountInfo.getGenevaAccountNumber()));
							chargeLineitemRepository.updateAccountNumber(accountInfo.getGenevaAccountNumber(), audit.getServiceId(), audit.getServiceType());
						}
						chargeLineitemRepository.flush();
						componentAndAttributeService.updateAttributes(scServiceDetail.get().getId(), atMap, AttributeConstants.COMPONENT_LM,"A");
					}
				}
				else {
					audit.setStatus(NetworkConstants.FAILURE);
					isAccountCreated = false;
				}
				servicehandoverRepository.save(audit);
				if(StringUtils.isNotBlank(audit.getProcessInstanceId())){
					Execution execution = runtimeService.createExecutionQuery().processInstanceId(audit.getProcessInstanceId()).activityId(activityId).singleResult();
					if (Objects.nonNull(audit.getAccountNumber())&& Constants.SUCCESS.equals(audit.getStatus())) {
						if(execution!=null) {
							logger.info("accountAsyncforNetwork execution for Account async", execution.getProcessInstanceId());
							runtimeService.setVariable(execution.getId(), "accountCreationStatus", isAccountCreated); 
							runtimeService.trigger(execution.getId());
						}else {
							logger.info("accountAsyncforNetwork execution is null for {} ", audit.getProcessInstanceId());
						}
					}
				}
			}
			accountSyncStatusResponse.setResult(Constants.RESPONSE);
			logger.info("accountSync service completed");
		}
		return accountSyncStatusResponse;
	}*/
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public UpdateAccountSyncStatusResponse accountAsyncforNetwork(OptimusAccoutInputBO accountInfo) throws Exception {
		UpdateAccountSyncStatusResponse accountSyncStatusResponse = new UpdateAccountSyncStatusResponse();
		logger.info("inside accountSync service for group id {} accountInfo={}",accountInfo.getInputGroupId(), accountInfo);
	/*	if ("DEV".equalsIgnoreCase(AppEnv) && accountInfo.getInputGroupId().contains("-34")) {
			redirectWpsToIpc.asyncAccount("10.133.209.34", accountInfo);
		} else if ("DEV".equalsIgnoreCase(AppEnv) && accountInfo.getInputGroupId().contains("-146")) {
			redirectWpsToIpc.asyncAccount("10.133.208.146", accountInfo);
		} else if ("DEV".equalsIgnoreCase(AppEnv) && accountInfo.getInputGroupId().contains("-40")) {
			redirectWpsToIpc.asyncAccount("10.133.209.40", accountInfo);
		}else {	*/
		TimeUnit.SECONDS.sleep(100);
			boolean isAccountCreated = true;
			ScChargeLineitem chargeLineitem = chargeLineitemRepository.findFirstByInputGroupId(StringUtils.trimToEmpty(accountInfo.getInputGroupId()));
			logger.info("inside accountSync service for group id {} account number={}",accountInfo.getInputGroupId(), accountInfo.getGenevaAccountNumber());
			if(Objects.nonNull(chargeLineitem)) {
				String activityId=accountInfo.getGenevaAccountNumber().contains("CPE")?"cpe_billing_account_creation_async":"billing_account_creation_async";
				if(chargeLineitem.getChargeLineitem().equals(NetworkConstants.CANCELLATION_CHARGES)) {
					activityId = NetworkConstants.CANCELLATION_ACCOUNT_CREATION_ASYNC;
				}
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
						componentAndAttributeService.updateAttributes(Integer.parseInt(chargeLineitem.getServiceId()), atMap, AttributeConstants.COMPONENT_LM,"A");
						List<ScChargeLineitem> lineitems = chargeLineitemRepository.findByInputGroupId(accountInfo.getInputGroupId());
						lineitems.stream().forEach(lineitem->{
							lineitem.setAccountNumber(accountInfo.getGenevaAccountNumber());
							chargeLineitemRepository.save(lineitem);
						});
						chargeLineitemRepository.flush();
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
		//}
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
		if("DEV".equalsIgnoreCase(AppEnv)) {
			wpsAsyncServiceDev.orderAsyncForNetwork(orderInfo);
		}else if ("DEV".equalsIgnoreCase(AppEnv) && orderInfo.getOrderNumber().contains("UCW")) {
			redirectWpsToIpc.asyncOrder("10.133.208.146", orderInfo);
		}
		else{
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
					if(chargeLineitem.getChargeLineitem().equals(NetworkConstants.CANCELLATION_CHARGES)) {
						activityId = chargeLineitem.getActionType().equals(NetworkConstants.CREATE) ? NetworkConstants.CANCELLATION_PRODUCT_COMMISSIONING_ASYNC : NetworkConstants.CANCELLATION_PRODUCT_TERMINATION_ASYNC;
					}
					chargeLineitemRepository.save(chargeLineitem);
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
		}
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
