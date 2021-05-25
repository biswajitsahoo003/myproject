package com.tcl.dias.servicehandover.ipc.service;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;

@Service
public class IpcBillingAccountCreationService {

	@Autowired
	@Qualifier("Order")
	SOAPConnector orderSoapConnector;
	
	@Value("${createOrder}")
	private String createOrderOperation;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	@Autowired
	BillingAccountCreationService billingAccountCreationService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;

	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	String status="";
	
	@Value("${app.host}")
	private String appHost;
	
	@Value("${application.env:PROD}")
	private String appEnv;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingAccountCreationService.class);
	/**
	 * This method initiates account creation process for the order placed.
	 * 
	 * @param orderId
	 * @param serviceId 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * @throws UnknownHostException 
	 */
	@Transactional(isolation = Isolation.DEFAULT)
	@Retryable(value= {UnknownHostException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10000))
	public CreateOrderResponse accountCreation(String orderCode, String processInstanceId, String serviceCode,
			String  serviceId, String serviceType) throws IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException, UnknownHostException {
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		AccountInputData accountInputData  = billingAccountCreationService.loadAccountDataFromO2C(orderCode, serviceCode, serviceType, serviceId);
		if(accountInputData!=null) {
			createOrderResponse = accountCreationIpc(orderCode, processInstanceId, serviceCode, serviceType,serviceId,accountInputData);
		}
		return createOrderResponse;
	}
	
	
	/**
	 *  method to create account if no data is modified during commercial - vetting stage
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param serviceId 
	 * @param splitRatio 
	 * @param siteType 
	 * @return
	 */
	
	private CreateOrderResponse accountCreationIpc(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId,AccountInputData accountInputData) throws UnknownHostException{
		LOGGER.info("Account creation started for Order id {} and Service type {}", orderId, serviceType);
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		
		ServicehandoverAudit audit = new ServicehandoverAudit();
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		AccountCreationRequestBO accountCreationRequestBO = new AccountCreationRequestBO();
		AccountCreationInputBO accountCreationInputBO = new AccountCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		CommonBO commonBo = new CommonBO();
		
		commonBo.setAccountingCurrency(accountInputData.getAccountingCurrency());
		commonBo.setInfoCurrency(accountInputData.getInfoCurrency());
		accountCreationInputBO.setFirstName(accountInputData.getFirstName());
		accountCreationInputBO.setMobile(accountInputData.getContactNumber());
		accountCreationInputBO.setDayTelephone(accountInputData.getContactNumber());
		accountCreationInputBO.setEmail(accountInputData.getContactEmail());
		accountCreationInputBO.setBillHandlingCode(accountInputData.getBillHandlingCode());

		commonBo.setInvoicingCoName(Constants.INVOICING_CONAME);
		commonBo.setAdvanceBoo(Constants.F);
		ScServiceAttribute scServiceAttrBillingEntity = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(Integer.parseInt(serviceId), IpcConstants.IPC_BILLING_ENTITY);
		commonBo.setBillingEntity((scServiceAttrBillingEntity != null && scServiceAttrBillingEntity.getAttributeValue() != null ) ? scServiceAttrBillingEntity.getAttributeValue() : IpcConstants.BILLING_ENTITY);
		
		ScServiceAttribute scServiceAttrSapCode = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(Integer.parseInt(serviceId), Constants.SAP_CODE);
		String gnvOrder = gnvOrderEntryTabRepository.findCustomerRef(serviceCode);
		if (scServiceAttrSapCode != null && scServiceAttrSapCode.getAttributeValue() != null) {
			LOGGER.info("Sap Code Picked from scServiceAttr{}" ,scServiceAttrSapCode.getAttributeValue());
			commonBo.setCustomerRef(scServiceAttrSapCode.getAttributeValue());
		} else {
			if (NetworkConstants.MACD.equals(scOrder.getOrderType()) && gnvOrder != null) {
				LOGGER.info("Sap Code Picked from Geneva {}" ,gnvOrder);
				commonBo.setCustomerRef(gnvOrder);
			} else {
				String sapCde = loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId());
				commonBo.setCustomerRef(sapCde);
				LOGGER.info("Sap Code Picked from MDM {}" ,sapCde);
				
			}
		}
		
		accountCreationInputBO.setContactName(accountInputData.getContactName());
		accountCreationInputBO.setAccAttributes("");
		accountCreationInputBO.setCreditClass(accountInputData.getCreditClass());
		commonBo.setPaymentDueDate(accountInputData.getPaymentDueDate());
		accountCreationInputBO.setBillingPeriod(accountInputData.getBillingFrequency());
		
		accountCreationInputBO.setAccAddr1(accountInputData.getAccountAddr1());
		accountCreationInputBO.setAccAddr2(accountInputData.getAccountAddr2());
		accountCreationInputBO.setAccAddr3("");
		accountCreationInputBO.setAccCity(WordUtils.capitalizeFully(accountInputData.getAccountAddrCity()));
		accountCreationInputBO.setAccState(WordUtils.capitalizeFully(accountInputData.getAccountAddrState()));
		accountCreationInputBO.setAccCountry(accountInputData.getAccountAddrCountry());
		accountCreationInputBO.setAccZipcode(accountInputData.getAccountAddrZipCode());
			
		commonBo.setSourceSystem(Constants.SOURCE_SYSTEM);
		commonBo.setRequestType(Constants.ACCOUNT);
		commonBo.setActionType(Constants.CREATE);
		commonBo.setServiceType(Constants.SERVICE_TYPE);
		commonBo.setProviderSegment(Constants.PROVIDER_SEGMENT);
		commonBo.setGroupId(generateAccountGroupId(serviceId));

		accountCreationInputBO.setCreationDate(accountInputData.getCreationDate());

		commonAttributesBO.setComReqAttributes(commonBo);
		accountCreationRequestBO.setAccountInput(accountCreationInputBO);

		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(accountCreationRequestBO);
		createOrderRequestBO.setProdCreationInput(null);
		createOrderBO.getReqOrder().add(createOrderRequestBO);
		createOrder.setCreateOrderRequestInput(createOrderBO);

		audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
		audit.setLegalEntity(scOrder.getErfCustLeName());
		audit.setCreatedDate(new Date());
		audit.setCrnId(commonBo.getCustomerRef());
		audit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
		audit.setProviderSegment(commonBo.getProviderSegment());
		audit.setRequestType(Constants.ACCOUNT_CREATION);
		audit.setOrderId(scOrder.getOpOrderCode());
		audit.setServiceId(serviceId);
		audit.setServiceCode(serviceCode);
		audit.setStatus(Constants.IN_PROGRESS);
		audit.setGenevaGrpId(commonBo.getGroupId());
		audit.setProcessInstanceId(processInstanceId);
		audit.setServiceType(serviceType);
		audit.setSourceProdSeq(commonBo.getSourceProductSeq());
		audit.setSiteType("");
		audit.setSplitRatio("");
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, audit);
		servicehandoverAuditRepository.save(audit);

		GenevaIpcOrderEntry ipcOrderEntry = genevaIpcOrderEntryMapper
				.createAccountToGenevaIpcOrderMapper(createOrderRequestBO);
		genevaIpcOrderEntryRepository.save(ipcOrderEntry);
		
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("Account Creation completed for orderId {} with status {}", orderId, status);
		return createOrderResponse;
	}
	
	private String generateAccountGroupId(String serviceId) {
		String groupId = NetworkConstants.OPT_ACC.concat(IpcConstants.IPC).concat(IpcConstants.UNDERSCORE)
				.concat(serviceId).concat("_") + System.currentTimeMillis();
		LOGGER.info("appEnv: {}", appEnv);
		LOGGER.info("appHost: {}",appHost);
		if (Arrays.asList("DEV","UAT").contains(appEnv)) {
			if (appHost.contains(IpcConstants.DEV_IP_188)) {
				groupId =groupId.concat(IpcConstants.REGEX_DEV_188);
			} else if (appHost.contains(IpcConstants.DEV_IP_104)) {
				groupId =groupId.concat(IpcConstants.REGEX_DEV_104);
			}
		}
		return groupId;
	}
	
	@Recover
	public CreateOrderResponse recover(UnknownHostException e, CreateOrderResponse createOrderResponse,
			String orderCode, String serviceCode) {
		LOGGER.info("Account Creation UnknowhostException Recovered for order id {} service id {}", orderCode, serviceCode);
		return createOrderResponse;
	}

}
