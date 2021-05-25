package com.tcl.dias.servicehandover.service;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStateToDistributionCenterMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScGstAddressRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.SiteBBillingAddress;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestAckBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ResponseHeader;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.PopDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

/**
 * This service handles are account logics for product across Optimus (IAS,GVPN,NPL)
 *  @author yogesh
 */

@Service
public class BillingAccountCreationService {
		
	@Autowired
	@Qualifier("Customer")
	SOAPConnector customerSoapConnector;

	@Autowired
	@Qualifier("Order")
	SOAPConnector orderSoapConnector;

	@Autowired
	@Qualifier("Invoice")
	SOAPConnector invoiceSoapConnector;
	
	@Autowired
	@Qualifier("Terminate")
	SOAPConnector terminateSoapConnector;

	@Value("${getCustomer}")
	private String getCustomerOperation;

	@Value("${createOrder}")
	private String createOrderOperation;

	@Value("${invoiceGeneration}")
	private String invoiceGenerationOperation;
	
	@Value("${terminateService}")
	private String terminateServiceOperation;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;

	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;

	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;

	@Autowired
	LoadCustomerDetails loadCustomerDetails;

	@Autowired
	MstStateToDistributionCenterMappingRepository mstStateToDistributionCenterMappingRepository;

	@Autowired
	PopDetails popDetails;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	String nonCpeAccountNumber = null;

	String cpeAccountNumber = null;

	String status = "";

	String customerAddressLocationId = "";

	ScServiceAttribute scServiceAttribute;

	String poNumberIas= "" ;
	
	String poDateIas =null;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScGstAddressRepository gstAddressRepository;
	
	@Value("${app.host}")
	private String appHost;
	
	@Value("${application.env:PROD}")
	String AppEnv;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingAccountCreationService.class);

	/**
	 * This method initiates account creation process for the order placed.
	 * 
	 * @param orderId
	 * @param serviceId 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	@Retryable(value = { UnknownHostException.class }, maxAttempts = 3, backoff = @Backoff(delay = 10000))
	public CreateOrderResponse accountCreation(String orderCode, String processInstanceId, String serviceCode,
			String serviceType, String serviceId,String siteType) throws IllegalAccessException, InvocationTargetException,UnknownHostException {
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		AccountInputData accountInputData  = loadAccountDataFromO2C(orderCode, serviceCode, serviceType, serviceId);
		LOGGER.info("Trying Account creation for {} Time for order code {} and Service code {}",orderCode,serviceCode);
		try {
			if (scServiceDetail != null
					&& NetworkConstants.NPL.equals(scServiceDetail.get().getErfPrdCatalogProductName())) {
				createOrderResponse = accountCreationNewNPL(orderCode, processInstanceId, serviceCode, serviceType,
						serviceId, accountInputData);
			} else {
				createOrderResponse = accountCreationNew(orderCode, processInstanceId, serviceCode, serviceType,
						serviceId, siteType, "", accountInputData);
			}
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
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
	
	private CreateOrderResponse accountCreationNew(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId, String siteType, String splitRatio,AccountInputData accountInputData) throws UnknownHostException {
		LOGGER.info("Account creation started for Order id {} and Service type {} and site type {} ", orderId, serviceType,siteType);
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		List<ScChargeLineitem> lineItemsList = null;
		ServicehandoverAudit audit = new ServicehandoverAudit();
		audit.setServiceType(serviceType);
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

		commonBo.setInvoicingCoName(NetworkConstants.INVOICING_CONAME);
		commonBo.setAdvanceBoo(NetworkConstants.F);// T
		commonBo.setBillingEntity(NetworkConstants.BILLING_ENTITY);
		commonBo.setCustomerRef(accountInputData.getCustomerRef());
		
		
		accountCreationInputBO.setContactName(accountInputData.getContactName());
		accountCreationInputBO.setAccAttributes(accountInputData.getAccountAttributes());
		
		accountCreationInputBO.setCreditClass(accountInputData.getCreditClass());
		
		commonBo.setPaymentDueDate(accountInputData.getPaymentDueDate());
		accountCreationInputBO.setBillingPeriod(accountInputData.getBillingFrequency());
		
		accountCreationInputBO.setAccAddr1(accountInputData.getAccountAddr1());
		accountCreationInputBO.setAccAddr2(accountInputData.getAccountAddr2());
		accountCreationInputBO.setAccAddr3(accountInputData.getAccountAddr3());
		accountCreationInputBO.setAccCity(WordUtils.capitalizeFully(accountInputData.getAccountAddrCity()));
		accountCreationInputBO.setAccState(WordUtils.capitalizeFully(accountInputData.getAccountAddrState()));
		accountCreationInputBO.setAccCountry(NetworkConstants.INDIA);
		accountCreationInputBO.setAccZipcode(accountInputData.getAccountAddrZipCode());
			
		commonBo.setSourceSystem(NetworkConstants.SOURCE_SYSTEM);
		commonBo.setRequestType(NetworkConstants.ACCOUNT);
		commonBo.setActionType(NetworkConstants.CREATE);
		commonBo.setServiceType(getServiceType(serviceType));
		commonBo.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
		commonBo.setGroupId(generateAccountGroupId(orderId, AppEnv, appHost));

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
		audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
		audit.setProviderSegment(commonBo.getProviderSegment());
		audit.setRequestType(NetworkConstants.ACCOUNT_CREATION);
		audit.setOrderId(scOrder.getOpOrderCode());
		audit.setServiceId(serviceId);
		audit.setServiceCode(serviceCode);
		audit.setStatus(NetworkConstants.IN_PROGRESS);
		audit.setGenevaGrpId(commonBo.getGroupId());
		audit.setProcessInstanceId(processInstanceId);
		audit.setCount(1);
		audit.setSourceProdSeq(commonBo.getSourceProductSeq());
		audit.setSiteType(siteType);
		audit.setSplitRatio(splitRatio);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, audit);
		servicehandoverAuditRepo.save(audit);

		GenevaIpcOrderEntry ipcOrderEntry = genevaIpcOrderEntryMapper
				.createAccountToGenevaIpcOrderMapper(createOrderRequestBO);
		genevaIpcOrderEntryRepository.save(ipcOrderEntry);
		
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			if (status != null && "Success".equals(status)) {
				if (StringUtils.isNotEmpty(siteType)) {
					lineItemsList = chargeLineitemRepository.findByServiceIdAndServiceTypeAndSiteType(serviceId,
							serviceType.contains("NPL") ? BillingConstants.NPL : serviceType, siteType);
				} else {
					lineItemsList = chargeLineitemRepository.findByServiceIdAndServiceType(serviceId, serviceType);
				}
				lineItemsList.stream().forEach(lineitem -> {
					lineitem.setInputGroupId(createOrderResponse.getCreateOrderRequestOutput().getReqUniqueId());
					lineitem.setCustomerRef(accountInputData.getCustomerRef());
					lineitem.setBillingType(commonBo.getServiceType());
					chargeLineitemRepository.save(lineitem);
				});
			}
			chargeLineitemRepository.flush();
			return createOrderResponse;
		}
		LOGGER.info("Account Creation completed for orderId {} with status {}", orderId, status);
		return createOrderResponse;	
		}
	
	
	private CreateOrderResponse accountCreationNewNPL(String orderCode, String processInstanceId, String serviceCode,
			String serviceType, String serviceId, AccountInputData accountInputData) throws UnknownHostException{
		LOGGER.info("Account creation started for Order id {} and Service type {}", orderCode, serviceType);
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("accountNoRequired", "nplBillingType"),
				Integer.parseInt(serviceId), "LM", "A");
		String splitRatio = "50:50";
		ScOrderAttribute ratio = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_RATIO, scOrder);
		if(ratio!=null && ratio.getAttributeValue()!= null) {
			 splitRatio = ratio.getAttributeValue();
		}
		String totalAccReq = attrMap.getOrDefault("accountNoRequired","One");
	    serviceType = attrMap.getOrDefault("nplBillingType",NetworkConstants.ACC_NPL_INTRACITY);
	    if (totalAccReq.equals(NetworkConstants.TWO_ACCOUNT)) {
			createOrderResponse = twoAccountCreation(orderCode, processInstanceId, serviceCode, serviceType, serviceId,
					accountInputData, splitRatio);
		} else {
			createOrderResponse = accountCreationNew(orderCode, processInstanceId, serviceCode, serviceType, serviceId,
					"A", "100", accountInputData);
		}
		return createOrderResponse;
	}

	/**
	 *  NPL account number creation based on Intercity / IntraCity
	 * 
	 * @param orderCode
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param serviceId
	 * @param splitRatio2 
	 * @return
	 */
	
	public CreateOrderResponse twoAccountCreation(String orderCode, String processInstanceId, String serviceCode,
			String serviceType, String serviceId,AccountInputData accountInputData, String splitRatio) throws UnknownHostException{
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		boolean isSiteWiseBilling = false;
		if (scOrder != null && "Y".equals(scOrder.getSiteLevelBilling())) {
			isSiteWiseBilling = true;
		}
		
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		CreateOrderResponse createOrderResponseSiteA = new CreateOrderResponse();
		CreateOrderResponse createOrderResponseSiteB = new CreateOrderResponse();
		createOrderResponseSiteA = accountCreationNew(orderCode, processInstanceId, serviceCode, serviceType, serviceId,
				"A", splitRatio.split(":")[0],accountInputData);
		createOrderResponseSiteB = accountCreationNew(orderCode, processInstanceId, serviceCode, serviceType, serviceId,
				"B", splitRatio.split(":")[1],isSiteWiseBilling ? getSiteBBillingAddress(accountInputData) : accountInputData);
		if (createOrderResponseSiteA != null && createOrderResponseSiteB != null) {
			String siteAstatus = createOrderResponseSiteA.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			String siteBstatus = createOrderResponseSiteA.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			if (siteAstatus != null && siteBstatus != null && siteAstatus.equals("Success")
					&& siteBstatus.equals("Success")) {
				CreateOrderRequestAckBO createOrderRequestAckBO = new CreateOrderRequestAckBO();
				ResponseHeader responseHeader = new ResponseHeader();
				responseHeader.setStatus("Success");
				createOrderRequestAckBO.setAcknowledge(responseHeader);
				createOrderResponse.setCreateOrderRequestOutput(createOrderRequestAckBO);
			} else {
				CreateOrderRequestAckBO createOrderRequestAckBO = new CreateOrderRequestAckBO();
				ResponseHeader responseHeader = new ResponseHeader();
				responseHeader.setStatus("Failure");
				responseHeader.setErrorMsg("Site A or Site B creation Failed");
				createOrderRequestAckBO.setAcknowledge(responseHeader);
				createOrderResponse.setCreateOrderRequestOutput(createOrderRequestAckBO);
			}
		}
		return createOrderResponse;
	}
	
	public AccountInputData loadAccountDataFromO2C(String orderCode, String serviceCode,
			String serviceType, String serviceId) {
		LOGGER.info("Account Creation-O2C updated Data Load Started for Order {} and service code {}",orderCode,serviceCode);
		AccountInputData accountInputData = new AccountInputData();
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		ScOrder scOrder = scServiceDetail.getScOrder();
		boolean isSiteWiseBilling = false;
		if (scOrder != null && "Y".equals(scOrder.getSiteLevelBilling())) {
			isSiteWiseBilling = true;
		}
		List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), NetworkConstants.Y);
		scOrderAttributes.forEach((scOrderAttribute) -> {

			switch (scOrderAttribute.getAttributeName()) {

			case LeAttributesConstants.BILLING_CURRENCY:
				accountInputData.setAccountingCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PAYMENT_CURRENCY:
				accountInputData.setInfoCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.BILLING_CONTACT_NAME:
				accountInputData.setFirstName(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.BILLING_CONTACT_MOBILE:
				accountInputData.setContactNumber(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.BILLING_CONTACT_EMAIL:
				accountInputData.setContactEmail(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.INVOICE_METHOD:
				if (scOrderAttribute.getAttributeValue().equals(NetworkConstants.PAPER_PLUS_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(NetworkConstants.PAPER_SLASH_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(NetworkConstants.PAPER_SPACE_ELECTONIC))
					accountInputData.setBillHandlingCode(NetworkConstants.PAPER_EMAIL);
				else if (scOrderAttribute.getAttributeValue().equals(NetworkConstants.ELECTRONIC))
					accountInputData.setBillHandlingCode(NetworkConstants.EMAIL);
				else
					accountInputData.setBillHandlingCode(NetworkConstants.PAPER);
				break;
			// Will be only for Renewals
			case BillingConstants.SAP_CODE:
				accountInputData.setCustomerRef(scOrderAttribute.getAttributeValue());
				break;
								
							
			}

		});

		accountInputData.setInvoicingCoName(NetworkConstants.INVOICING_CONAME);
		accountInputData.setAdvanceBoo(NetworkConstants.F);// T
		accountInputData.setBillingEntity(NetworkConstants.BILLING_ENTITY);
		
		ScComponentAttribute sapCode = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						Integer.parseInt(serviceId), BillingConstants.SAP_CODE, "LM", "A");
		ScComponentAttribute isBurstable = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						Integer.parseInt(serviceId), NetworkConstants.IS_BURSTABLE, "LM", "A");
		String gnvOrder = gnvOrderEntryTabRepository.findCustomerRef(serviceCode);
		if (sapCode != null && sapCode.getAttributeValue() != null) {
			LOGGER.info("Sap Code Picked from attr{}" ,sapCode.getAttributeAltValueLabel());
			accountInputData.setCustomerRef(sapCode.getAttributeValue());
		}else if(Objects.isNull(accountInputData.getCustomerRef())) {
			if (NetworkConstants.MACD.equals(scOrder.getOrderType()) && gnvOrder != null
					&& !"Novation".equals(scServiceDetail.getOrderSubCategory())) {
				LOGGER.info("Sap Code Picked from Geneva {}" ,gnvOrder);
				accountInputData.setCustomerRef(gnvOrder);
			} else {
				String sapCde = loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId());
				accountInputData.setCustomerRef(sapCde);
				LOGGER.info("Sap Code Picked from MDM {}" ,sapCde);
				
			}
		}
		accountInputData.setOpportunityId(scOrder.getTpsSfdcOptyId());
		accountInputData.setContactName(scOrder.getErfCustLeName());
		if (BillingConstants.GVPN.equals(serviceType) || BillingConstants.IZOPC.equals(serviceType)) {
			accountInputData.setAccountAttributes(
					"Chennai;PORTAL_TRANSACTION_ID=;PROFILE_ID=;COPF_ID=;p_LastMile=F;PAYMENT_METHOD=DD;CHANNEL_PARTNER_NAME=;NORMAL_BILLING=T;PURCHASE_ORDER_NUM=;AA_CODE=None;SITE_FLAG=;BILLING_TYPE=S;");

		}
		LOGGER.info("serviceType=> {} isBurstable=>{}",serviceType,isBurstable);
		if(NetworkConstants.IAS.equals(serviceType) && isBurstable!=null && isBurstable.getAttributeValue()!=null &&"Y".equals(isBurstable.getAttributeValue())) {
			accountInputData.setAccountAttributes(";BILLING_TYPE=ILLSC;");
		}
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		accountInputData.setCreditClass(scContractInfo.getPaymentTerm().toLowerCase() + " from Invoice date");
		accountInputData.setPaymentDueDate(scContractInfo.getOrderTermInMonths().toString());
		accountInputData.setBillingMethod(scContractInfo.getBillingMethod());
		if (scContractInfo.getBillingFrequency() != null) {
			if ("monthly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				accountInputData.setBillingFrequency(NetworkConstants.ONE_M);
			else if (scContractInfo.getBillingFrequency().toLowerCase().contains("bi"))
				accountInputData.setBillingFrequency(NetworkConstants.TWO_M);
			else if ("quarterly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				accountInputData.setBillingFrequency(NetworkConstants.THREE_M);
			else if (scContractInfo.getBillingFrequency().toLowerCase().contains("half"))
				accountInputData.setBillingFrequency(NetworkConstants.SIX_M);
			else
				accountInputData.setBillingFrequency(NetworkConstants.TWELVE_M);
		} else {
			accountInputData.setBillingFrequency(NetworkConstants.ONE_M);
		}
		
		boolean isTwoAccountsRequired = false;
		
		if (BillingConstants.NPL.equals(serviceType)) {
			Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("destinationState", "siteGstNumber"), Integer.parseInt(serviceId), "LM", "A");
			Map<String, String> attrBMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("destinationState", "siteGstNumber"), Integer.parseInt(serviceId), "LM", "B");

			String siteAState = attrMap.get("destinationState");
			String siteBState = attrBMap.get("destinationState");
			String siteAGstNumber = attrMap.get("siteGstNumber");
			String siteBGstNumber = attrBMap.get("siteGstNumber");

			if (siteAState != null & siteBState != null && siteAState.equalsIgnoreCase(siteBState)
					&& siteAGstNumber != null && siteBGstNumber != null && siteAGstNumber.equals(siteBGstNumber)) {
				isTwoAccountsRequired = false;
			}else {
				isTwoAccountsRequired = true;
			}
			LOGGER.info("isTwoAccountsRequired {}",isTwoAccountsRequired);
		}
	
		addressConstructor.accountAddress(scContractInfo, accountInputData,isSiteWiseBilling,serviceId,isTwoAccountsRequired);
		
		accountInputData.setSourceSystem(NetworkConstants.SOURCE_SYSTEM);
		accountInputData.setRequestType(NetworkConstants.ACCOUNT);
		accountInputData.setActionType(NetworkConstants.CREATE);
		accountInputData.setServiceType(serviceType);
		accountInputData.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
		//accountInputData.setInputGroupId(generateAccountGroupId(orderCode, AppEnv, appHost));
		accountInputData.setCreationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		LOGGER.info("Account Creation-O2C updated Data Load Completed for Order {} and service code {}",orderCode,serviceCode);
		return accountInputData;
	}
	
	public boolean isAccountCreationRequired(String orderId, String serviceCode, String serviceId) {
		ScComponentAttribute accountCreationRequired = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						Integer.parseInt(serviceId), "isAccountRequired", "LM", "A");
		if (accountCreationRequired != null && accountCreationRequired.getAttributeValue() != null
				&& "Y".equals(accountCreationRequired.getAttributeValue())) {
			return true;
		} else {
			return false;
		}

	}
	
	public String getServiceType(String serviceType) {
		if (BillingConstants.IZOSDWAN.equals(serviceType) || BillingConstants.IZO_SDWAN.equals(serviceType)) {
			return BillingConstants.IZO_SDWAN;
		}
		if (NetworkConstants.IZOSDWAN_CGW.equals(serviceType)) {
			return BillingConstants.IZO_SDWAN;
		}
		if (BillingConstants.UCAAS.equals(serviceType)) {
			return BillingConstants.CISCO_WEBEX;
		}
		if (BillingConstants.MIRCOSOFT_CLOUD_SOLNS.equals(serviceType)) {
			return BillingConstants.DIRECT_ROUTING;
		}
		if (BillingConstants.IZOPC.equals(serviceType)) {
			return BillingConstants.GVPN;
		}
		return serviceType;
	}
	
	private AccountInputData getSiteBBillingAddress(AccountInputData accountInputData) {

		SiteBBillingAddress siteBBillingAddress = accountInputData.getSiteBBillingAddress();
		if (siteBBillingAddress != null) {
			String addressLineOne = siteBBillingAddress.getSiteBAccountAddr1();
			String addressLineTwo = siteBBillingAddress.getSiteBAccountAddr2();
			String addressLineThree = siteBBillingAddress.getSiteBAccountAddr3();
			String addressCity = siteBBillingAddress.getSiteBAccountAddrCity();
			String addressState = siteBBillingAddress.getSiteBAccountAddrState();
			String addressCountry = siteBBillingAddress.getSiteBAccountAddrCountry();
			String addressZipCode = siteBBillingAddress.getSiteBAccountAddrZipCode();

			accountInputData.setAccountAddr1(addressLineOne.length() >= 120 ? addressLineOne.substring(0, 120) : addressLineOne);
			accountInputData.setAccountAddr2(addressLineTwo.length() >= 120 ? addressLineTwo.substring(0, 120) : addressLineTwo);
			accountInputData.setAccountAddr3(addressLineThree.length() >= 120 ? addressLineThree.substring(0, 120) : addressLineThree);
			accountInputData.setAccountAddrCity(addressCity);
			accountInputData.setAccountAddrState(addressState);
			accountInputData.setAccountAddrCountry(addressCountry);
			accountInputData.setAccountAddrZipCode(addressZipCode);
		}

		return accountInputData;
	}

	private String generateAccountGroupId(String orderCode, String appEnv, String appHost) {
		String groupId = NetworkConstants.OPT_ACC.concat(orderCode).concat("_") + System.currentTimeMillis();
		LOGGER.info("App Env is {} and App Host {} ",appEnv,appHost);
		if ("DEV".equalsIgnoreCase(appEnv)) {
			if (appHost.contains(".34")) {
				groupId =groupId.concat("-34");
				LOGGER.info("group id for .34 {} ", groupId);
			} else if (appHost.contains(".40")) {
				groupId =groupId.concat("-40");
			} else if (appHost.contains(".146")) {
				groupId =groupId.concat("-146");
			}else if (appHost.contains(".33")) {
				groupId =groupId.concat("-33");
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
