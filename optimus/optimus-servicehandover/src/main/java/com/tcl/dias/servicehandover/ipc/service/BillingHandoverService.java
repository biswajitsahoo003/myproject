package com.tcl.dias.servicehandover.ipc.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommericalComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.CommercialVettingInputData;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
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
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.invoice.BSSEGInvoiceDetails;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoice;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.CustomerAndSupplierAddress;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.IPCComparator;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.ProductAttributeString;
import com.tcl.dias.servicehandover.util.ProductDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.dias.servicehandover.util.UniqueProductCreation;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.netty.util.Constant;

/**
 * OTB-340: BillingHandoverService class performs in-depth operations related to
 * account creation, product commissioning and invoice generation.
 * 
 * @author arjayapa
 *
 */
@Service
public class BillingHandoverService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingHandoverService.class);

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
	ServicehandoverAuditRepository servicehandoverAuditRepository;

	@Autowired
	LoadCustomerDetails loadCustomerDetails;

	@Autowired
	ProductDetails productLocationDetails;

	@Value("${getCustomer}")
	private String getCustomerOperation;

	@Value("${createOrder}")
	private String createOrderOperation;

	@Value("${invoiceGeneration}")
	private String invoiceGenerationOperation;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScProductDetailRepository scProductDetailRepository;

	@Autowired
	ScProductDetailAttributeRepository scProductDetailAttributeRepository;

	@Autowired
	ScServiceCommericalComponentRepository scServiceCommericalComponentRepository;

	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;

	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	CustomerAndSupplierAddress customerAndSupplierAddress;

	String customerAddressLocationId = "";

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	String contractGstNo="";
	

	/**
	 * This method initiates account creation process for the order placed.
	 * 
	 * @param orderId
	 * @param serviceId 
	 * @param serviceType 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	
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
	 */
	@Transactional(isolation = Isolation.DEFAULT)
	public CreateOrderResponse accountCreation(String orderId, String processInstanceId, String serviceCode,
			String  serviceId, String serviceType) throws IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException {
		CreateOrderResponse createOrderResponse = accountCreationNew(orderId, processInstanceId, serviceCode, serviceId,serviceType);
		return createOrderResponse;
	}
	
	public CreateOrderResponse accountCreationNew(String orderId, String processInstanceId, String serviceCode, String serviceId, String serviceType)
			throws IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException {

		ServicehandoverAudit audit = new ServicehandoverAudit();
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		AccountCreationRequestBO accountCreationRequestBO = new AccountCreationRequestBO();
		AccountCreationInputBO accountCreationInputBO = new AccountCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		CommonBO commonBo = new CommonBO();

		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), Constants.Y);
		scOrderAttributes.forEach((scOrderAttribute) -> {

			switch (scOrderAttribute.getAttributeName()) {

			case LeAttributesConstants.BILLING_CURRENCY:
				commonBo.setAccountingCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PAYMENT_CURRENCY:
				commonBo.setInfoCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.BILLING_CONTACT_MOBILE:
				accountCreationInputBO.setMobile(scOrderAttribute.getAttributeValue());
				accountCreationInputBO.setDayTelephone(scOrderAttribute.getAttributeValue());
				break;
				
			case LeAttributesConstants.BILLING_CONTACT_NAME:
				accountCreationInputBO.setFirstName(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.INVOICE_METHOD:
				if (scOrderAttribute.getAttributeValue().equals(Constants.PAPER_PLUS_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(Constants.PAPER_SLASH_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(Constants.PAPER_SPACE_ELECTONIC))
					accountCreationInputBO.setBillHandlingCode(Constants.PAPER_EMAIL);
				else if (scOrderAttribute.getAttributeValue().equals(Constants.ELECTRONIC))
					accountCreationInputBO.setBillHandlingCode(Constants.EMAIL);
				else
					accountCreationInputBO.setBillHandlingCode(Constants.PAPER);
				break;
			}

		});

		commonBo.setInvoicingCoName(Constants.INVOICING_CONAME);
		commonBo.setAdvanceBoo(Constants.F);// T
		commonBo.setBillingEntity(Constants.BILLING_ENTITY);
		commonBo.setCustomerRef(loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
		accountCreationInputBO.setContactName(scOrder.getErfCustLeName());
		
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		if (scContractInfo.getBillingFrequency() != null) {
			if ("monthly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				accountCreationInputBO.setBillingPeriod(Constants.ONE_M);
			else if ("quarterly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				accountCreationInputBO.setBillingPeriod(Constants.THREE_M);
			else if (scContractInfo.getBillingFrequency().toLowerCase().contains("half"))
				accountCreationInputBO.setBillingPeriod(Constants.SIX_M);
			else
				accountCreationInputBO.setBillingPeriod(Constants.TWELVE_M);
		} else {
			accountCreationInputBO.setBillingPeriod(Constants.ONE_M);
		}
	
		accountCreationInputBO.setEmail(scContractInfo.getCustomerContactEmail());
		/*accountCreationInputBO.setFirstName(scContractInfo.getCustomerContact().split(" ")[0]);
		 accountCreationInputBO.setLastName(scContractInfo.getCustomerContact().split(" ").length > 1
				? scContractInfo.getCustomerContact().split(" ")[1]
				: " ");*/
		accountCreationInputBO.setCreditClass(scContractInfo.getPaymentTerm().toLowerCase() + " from Invoice date");
		commonBo.setPaymentDueDate(scContractInfo.getOrderTermInMonths().toString());

		addressConstructor.accountAddress(scContractInfo, accountCreationInputBO);
		commonBo.setGroupId(
				Constants.OPT_ACC.concat(orderId).concat(TimeStampUtil.formatWithTimeStamp().format(new Date())));
		commonBo.setSourceSystem(Constants.SOURCE_SYSTEM);
		commonBo.setRequestType(Constants.ACCOUNT);
		commonBo.setActionType(Constants.CREATE);
		commonBo.setServiceType(Constants.SERVICE_TYPE);
		commonBo.setProviderSegment(Constants.PROVIDER_SEGMENT);

		// To be populated for International Customer
		commonBo.setSecsId(loadCustomerDetails.getSecsCode(scOrder.getErfCustLeId()));
		// commonBo.setProfileId();
		accountCreationInputBO.setCreationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		commonAttributesBO.setComReqAttributes(commonBo);
		accountCreationRequestBO.setAccountInput(accountCreationInputBO);

		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(accountCreationRequestBO);
		createOrderRequestBO.setProdCreationInput(null);
		createOrderBO.getReqOrder().add(createOrderRequestBO);
		createOrder.setCreateOrderRequestInput(createOrderBO);

		audit.setLegalEntity(scOrder.getErfCustLeName());
		audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
		
		audit.setCreatedDate(new Date());
		audit.setCrnId(commonBo.getCustomerRef());
		audit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
		audit.setRequest(createOrder.toString());
		audit.setRequestType(Constants.ACCOUNT_CREATION);
		audit.setOrderId(scOrder.getOpOrderCode());
		audit.setServiceCode(serviceCode);
		audit.setServiceId(serviceId);
		audit.setStatus(Constants.IN_PROGRESS);
		audit.setRequest(createOrder.toString());
		audit.setGenevaGrpId(commonBo.getGroupId());
		audit.setProcessInstanceId(processInstanceId);
		audit.setProviderSegment(commonBo.getProviderSegment());
		audit.setCount(1);
		audit.setServiceType(serviceType);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, audit);
		servicehandoverAuditRepository.save(audit);

		GenevaIpcOrderEntry ipcOrderEntry = genevaIpcOrderEntryMapper.createAccountToGenevaIpcOrderMapper(createOrderRequestBO);
		genevaIpcOrderEntryRepository.save(ipcOrderEntry);

		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(serviceId),
						Arrays.asList("billFreePeriod"));
		Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String billFreePeriod = attributeMap.getOrDefault("billFreePeriod", "0");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(audit.getServiceId()));
		if(Objects.nonNull(scServiceDetail)) {
			Map<String, String> attrMap = new HashMap<>();
			LOGGER.info("attributed update for {} ",scServiceDetail.get().getId());
			attrMap.put("billStartDate", TimeStampUtil.formatWithTimeStampForCommPlusDays(scServiceDetail.get().getServiceCommissionedDate().toString(),StringUtils.isNoneBlank(billFreePeriod)?Integer.valueOf(billFreePeriod):0));
			attrMap.put("commDate", TimeStampUtil.formatWithTimeStampForCommPlusDays(scServiceDetail.get().getServiceCommissionedDate().toString(), StringUtils.isNoneBlank(billFreePeriod)?Integer.valueOf(billFreePeriod):0));
			updateServiceAttributes(scServiceDetail.get().getId(), attrMap);
		}
		
		return (CreateOrderResponse) orderSoapConnector.callWebService(createOrderOperation, createOrder);
	}
	
	/**
	 * method to create account if data is modified during commercial - vetting stage
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param genevaIpcOrderEntry
	 * @param serviceId 
	 * @return
	 */
	private CreateOrderResponse accountCreationfromCommVetting(String orderId, String processInstanceId, String serviceCode,
			String serviceType,GenevaIpcOrderEntry genevaIpcOrderEntry, String serviceId) {
		LOGGER.info("Account creation started for Order id {} and Service type {}", orderId, serviceType);
		ServicehandoverAudit audit = new ServicehandoverAudit();
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		AccountCreationRequestBO accountCreationRequestBO = new AccountCreationRequestBO();
		AccountCreationInputBO accountCreationInputBO = new AccountCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		CommonBO commonBo = new CommonBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		String inputGroupId=NetworkConstants.OPT_ACC.concat(orderId).concat("_").concat(commonBo.getServiceType()).concat("_")
				.concat(scContractInfo.getErfCustLeId().toString()).concat("_") + System.currentTimeMillis();
		
		commonBo.setAccountingCurrency(genevaIpcOrderEntry.getAccountingCurrency());
		commonBo.setInfoCurrency(genevaIpcOrderEntry.getInfoCurrency());
		commonBo.setInvoicingCoName(genevaIpcOrderEntry.getInvoicing_coName());
		commonBo.setAdvanceBoo(genevaIpcOrderEntry.getAdvanceBoo());// T
		commonBo.setBillingEntity(genevaIpcOrderEntry.getBillingEntity());
		commonBo.setCustomerRef(genevaIpcOrderEntry.getCustomerRef());
		commonBo.setPaymentDueDate(genevaIpcOrderEntry.getPayment_dueDate());
		commonBo.setSourceSystem(genevaIpcOrderEntry.getSourceSystem());
		commonBo.setRequestType(genevaIpcOrderEntry.getRequestType());
		commonBo.setActionType(genevaIpcOrderEntry.getActionType());
		commonBo.setServiceType(serviceType);
		commonBo.setProviderSegment(genevaIpcOrderEntry.getProviderSegment());
		commonBo.setGroupId(inputGroupId);
		commonBo.setSecsId(genevaIpcOrderEntry.getSecsId());

		accountCreationInputBO.setMobile(genevaIpcOrderEntry.getDayTelephone());
		accountCreationInputBO.setDayTelephone(genevaIpcOrderEntry.getDayTelephone());		
		accountCreationInputBO.setBillHandlingCode(genevaIpcOrderEntry.getBillHandlingCode());
		 
		accountCreationInputBO.setBillingPeriod(genevaIpcOrderEntry.getBillingPeriod());
		accountCreationInputBO.setContactName(genevaIpcOrderEntry.getCustomerName());
		accountCreationInputBO.setEmail(genevaIpcOrderEntry.getEmail());
		accountCreationInputBO.setFirstName(genevaIpcOrderEntry.getFirstName());
		accountCreationInputBO.setLastName(genevaIpcOrderEntry.getLastName());
		accountCreationInputBO.setCreditClass(genevaIpcOrderEntry.getCreditClass());
		accountCreationInputBO.setAccAddr1(genevaIpcOrderEntry.getAccAddr1());
		accountCreationInputBO.setAccAddr2(genevaIpcOrderEntry.getAccAddr2());
		accountCreationInputBO.setAccAddr3(genevaIpcOrderEntry.getAccAddr3());
		accountCreationInputBO.setAccCity(WordUtils.capitalizeFully(genevaIpcOrderEntry.getAccCity()));
		accountCreationInputBO.setAccState(WordUtils.capitalizeFully(genevaIpcOrderEntry.getAccState()));
		accountCreationInputBO.setAccCountry(genevaIpcOrderEntry.getAccCountry());
		accountCreationInputBO.setAccZipcode(genevaIpcOrderEntry.getAccZipcode());
		accountCreationInputBO.setCreationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		commonAttributesBO.setComReqAttributes(commonBo);
		accountCreationRequestBO.setAccountInput(accountCreationInputBO);
		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(accountCreationRequestBO);
		createOrderRequestBO.setProdCreationInput(null);

		createOrderBO.getReqOrder().add(createOrderRequestBO);

		createOrder.setCreateOrderRequestInput(createOrderBO);

		audit.setLegalEntity(genevaIpcOrderEntry.getCustomerName());
		audit.setCreatedDate(new Date());
		audit.setCrnId(commonBo.getCustomerRef());
		audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
		audit.setRequestType(NetworkConstants.ACCOUNT_CREATION);
		audit.setOrderId(genevaIpcOrderEntry.getCopfId());
		audit.setServiceId(serviceId);
		audit.setServiceCode(serviceCode);
		audit.setStatus(NetworkConstants.IN_PROGRESS);
		audit.setGenevaGrpId(commonBo.getGroupId());
		audit.setProcessInstanceId("");
		audit.setServiceType(commonBo.getServiceType());
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, audit);
		servicehandoverAuditRepository.save(audit);

		genevaIpcOrderEntryRepository.save(genevaIpcOrderEntry);
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		String status =null;
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("Account Creation completed for orderId {} with status {}", orderId, status);
		return createOrderResponse;
		
	}

	/*@Deprecated
	public CreateOrderResponse productCreationNew(String orderId, String accountId, String processInstanceId,
			boolean isMacd, String serviceType, String serviceId, String serviceCode) {
		LOGGER.info("productCreation invoked for orderId {} and Service type {}", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		//ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		//ServicehandoverAudit servicehandoverAudit = servicehandoverAuditRepo.findByRequestTypeAndServiceIdAndServiceType(NetworkConstants.ACCOUNT_CREATION, serviceCode,serviceType);
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		if (isMacd) {
			List<ServicehandoverAudit> servicehandoverAuditList = servicehandoverAuditRepository
					.findByServiceIdAndServiceType(Integer.parseInt(serviceId), Constants.PRD_TERM,serviceType);
			boolean newOrderTrigger = servicehandoverAuditList.stream().anyMatch(audit-> NetworkConstants.SUCCESS.equals(audit.getStatus()));
			if(newOrderTrigger) {
				List<IpcChargeLineitem> chargeLineitems = ipcChargeLineitemRepository.findByServiceCodeAndServiceType(serviceCode,
						serviceType);
				chargeLineitems.forEach(chargeLineitem -> {
					UniqueProductCreation uniqueProductCreation = new UniqueProductCreation();
					uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
					uniqueProductCreation.setAccountId(accountId);
					uniqueProductCreation.setMrc(chargeLineitem.getMrc());
					uniqueProductCreation.setNrc(chargeLineitem.getNrc());
					uniqueProductCreation.setArc(chargeLineitem.getArc());
					uniqueProductCreation.setScOrder(scOrder);
					uniqueProductCreation.setScServiceDetail(scServiceDetail.get());
					uniqueProductCreation.setProcessInstanceId(processInstanceId);
					uniqueProductCreation.setActionType(NetworkConstants.CREATE);
					uniqueProductCreation.setOrderType(NetworkConstants.NEW);
					uniqueProductCreation.setChangeOrderType(NetworkConstants.NEW_ORDER);
					if (chargeLineitem.getChargeLineitem().contains(NetworkConstants.ADDITONAL_IP))
						uniqueProductCreation.setQuantity(chargeLineitem.getQuantity());
					else
						uniqueProductCreation.setQuantity(NetworkConstants.ONE);
					uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
					uniqueProductCreation.setComponent(chargeLineitem.getComponent());
					uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
					uniqueProductCreation.setIsMacd(false);
					uniqueProductCreation.setMhs_remarks(chargeLineitem.getComponent());
					CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation,uniqueProductCreation.getIsMacd());
					createOrderBO.getReqOrder().add(createOrderRequestBO);
				});
			}
		}else {
		
			List<IpcChargeLineitem> chargeLineitems = ipcChargeLineitemRepository.findByServiceCodeAndServiceTypeAndStatus(serviceCode,
					serviceType, IpcConstants.IPC_ACTIVE);
			chargeLineitems.forEach(chargeLineitem -> {
				UniqueProductCreation uniqueProductCreation = new UniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setAccountId(accountId);
				uniqueProductCreation.setMrc(chargeLineitem.getMrc());
				uniqueProductCreation.setNrc(chargeLineitem.getNrc());
				uniqueProductCreation.setArc(chargeLineitem.getArc());
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(scServiceDetail.get());
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.CREATE);
				uniqueProductCreation.setOrderType(NetworkConstants.NEW);
				uniqueProductCreation.setChangeOrderType(NetworkConstants.NEW_ORDER);
				if (chargeLineitem.getChargeLineitem().contains(NetworkConstants.ADDITONAL_IP))
					uniqueProductCreation.setQuantity(chargeLineitem.getQuantity());
				else
					uniqueProductCreation.setQuantity(NetworkConstants.ONE);
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(false);
				uniqueProductCreation.setMhs_remarks(chargeLineitem.getComponent());
				if( uniqueProductCreation.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
					uniqueProductCreation.setBaseBandwidth(chargeLineitem.getAdditionalParam());
				}
				CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation,uniqueProductCreation.getIsMacd());
				createOrderBO.getReqOrder().add(createOrderRequestBO);
			});
		
		}
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		String status= null;
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("productCreation completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}
	
	@Deprecated
	public CreateOrderResponse productTermination(String orderId, String processInstanceId,String serviceType,
			String serviceId, String serviceCode) {
		LOGGER.info("productTermination invoked for orderId {} and Service type {}", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		
		List<IpcChargeLineitem> ipcChargeLineitems = ipcChargeLineitemRepository.findByMaxVersionAndServiceCodeAndServiceTypeAndStatus(serviceCode,
				serviceType, IpcConstants.IPC_TERMINATE);
		ipcChargeLineitems.forEach(chargeLineitem -> {
			UniqueProductCreation uniqueProductCreation = new UniqueProductCreation();
			uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
			uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
			uniqueProductCreation.setMrc(chargeLineitem.getMrc());
			uniqueProductCreation.setNrc(chargeLineitem.getNrc());
			uniqueProductCreation.setArc(chargeLineitem.getArc());
			uniqueProductCreation.setScOrder(scOrder);
			uniqueProductCreation.setScServiceDetail(scServiceDetail.get());
			uniqueProductCreation.setProcessInstanceId(processInstanceId);
			uniqueProductCreation.setActionType(Constants.TERMINATE);
			uniqueProductCreation.setOrderType(Constants.MODIFY);
			uniqueProductCreation.setChangeOrderType(Constants.TERMINATE_ORDER);
			if (chargeLineitem.getChargeLineitem().contains(NetworkConstants.ADDITONAL_IP))
				uniqueProductCreation.setQuantity(chargeLineitem.getQuantity());
			else
				uniqueProductCreation.setQuantity(NetworkConstants.ONE);
			uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
			uniqueProductCreation.setComponent(chargeLineitem.getComponent());
			uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
			uniqueProductCreation.setIsMacd(true);
			uniqueProductCreation.setMhs_remarks(chargeLineitem.getComponent());
			if( uniqueProductCreation.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
				uniqueProductCreation.setBaseBandwidth(chargeLineitem.getAdditionalParam());
			}
			String seq = gnvOrderEntryTabRepository.findSourceProdSeq(chargeLineitem.getAccountNumber(),chargeLineitem.getChargeLineitem());
			uniqueProductCreation.setSourceProdSeq(seq);
			CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation,uniqueProductCreation.getIsMacd());
			createOrderBO.getReqOrder().add(createOrderRequestBO);
		});
		
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		String status= null;
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("productTermination completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}*/
	
	/**
	 * This method creates separate products for each of the chargeable line items
	 * attached to the order.
	 * 
	 * @param uniqueProductCreation
	 * @return
	 */
	CreateOrderRequestBO createUniqueProduct(UniqueProductCreation uniqueProductCreation, boolean isMacd) {

		ScOrder scOrder = uniqueProductCreation.getScOrder();
		ScServiceDetail scServiceDetail = uniqueProductCreation.getScServiceDetail();

		LOGGER.info("createUniqueProduct invoked for orderId {} accountId {} productName {} ", scOrder.getOpOrderCode(),
				uniqueProductCreation.getAccountId(), uniqueProductCreation.getProductName());
		ServicehandoverAudit audit = new ServicehandoverAudit();
		audit = servicehandoverAuditRepository.save(audit);
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		ProductCreationRequestBO productCreationRequestBO = new ProductCreationRequestBO();
		ProductCreationInputBO productCreationInputBO = new ProductCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		ProductAttributeString productAttributeString = new ProductAttributeString();

		CommonBO commonBo = new CommonBO();
		GenevaIpcOrderEntry ipcOrderEntry = null;

		commonBo.setGroupId(groupIdGen(uniqueProductCreation.getProcessInstanceId(),
				Constants.OPT_PROD.concat(uniqueProductCreation.getAccountId().concat("_")), isMacd));
		commonBo.setSourceSystem(Constants.SOURCE_SYSTEM);
		commonBo.setRequestType(Constants.PRODUCT);
		commonBo.setActionType(uniqueProductCreation.getActionType());
		commonBo.setCustomerRef(loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
		commonBo.setAccountNum(uniqueProductCreation.getAccountId());
		commonBo.setInvoicingCoName(Constants.INVOICING_CONAME);
		commonBo.setBillingEntity(Constants.BILLING_ENTITY);
		commonBo.setProviderSegment(Constants.PROVIDER_SEGMENT);
		commonBo.setAdvanceBoo(Constants.T);
		commonBo.setServiceType(Constants.SERVICE_TYPE);

		
		commonBo.setSourceProductSeq(String.valueOf(audit.getId()));
		productCreationInputBO.setTermEndDate("");
		productCreationInputBO.setTermReason("");
		productCreationInputBO.setDepositRefundBoo(Constants.T);

		if (uniqueProductCreation.getIsMacd()) {
			commonBo.setSourceProductSeq(uniqueProductCreation.getSourceProdSeq());
			productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStamp().format(new Date()).toString());
			productCreationInputBO.setTermReason(Constants.UPGRADE);
			productCreationInputBO.setDepositRefundBoo(Constants.F);

		}
		productCreationInputBO.setProductName(uniqueProductCreation.getProductName());

		List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), Constants.Y);

		scOrderAttributeList.forEach((scOrderAttribute) -> {

			switch (scOrderAttribute.getAttributeName()) {

			case LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY:
				customerAddressLocationId = scOrderAttribute.getAttributeValue();
				break;

			case LeAttributesConstants.BILLING_CURRENCY:
				commonBo.setAccountingCurrency(scOrderAttribute.getAttributeValue());
				productCreationInputBO.setCurrencyCode(scOrderAttribute.getAttributeValue());
				productCreationInputBO.setCurrencyCodePrd(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PAYMENT_CURRENCY:
				commonBo.setInfoCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.ACCOUNT_CUID:
				productAttributeString.setLEGAL_CUID(scOrder.getTpsSfdcCuid());
				break;

			case LeAttributesConstants.LE_STATE_GST_NO:
				productCreationInputBO.setCONTRACTINGGSTINNO(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.TAX_EXEMPTION:
				productCreationInputBO.setTaxExemptTxt(scOrderAttribute.getAttributeValue());
				productCreationInputBO.setTaxExemptRef(
						"yes".equals(productCreationInputBO.getTaxExemptTxt()) ? "Customer In Sez" : "");
				break;
			case LeAttributesConstants.TAX_EXEMPTION_ATTACHMENT:
				productCreationInputBO.setTaxationDocsURL(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PO_DATE:
				try {
					if(scOrderAttribute.getAttributeValue()!=null) {
						productAttributeString.setPO_DATE(TimeStampUtil.poDateFormatCpe(scOrderAttribute.getAttributeValue()));
						productAttributeString.setPO_START_DATE(TimeStampUtil.poDateFormatCpe(productAttributeString.getPO_DATE()));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;

			case LeAttributesConstants.PO_NUMBER:
				if (scOrderAttribute.getAttributeValue() != null) {
					productAttributeString.setPO_NUMBER(scOrderAttribute.getAttributeValue());
				}
				break;

			}

		});

		List<ScContractInfo> scContractInfoList = scContractInfoRepository.findByScOrder_id(scOrder.getId());

		scContractInfoList.forEach((scContractInfo) -> {
			commonBo.setPaymentDueDate(scContractInfo.getPaymentTerm());
			productCreationInputBO.setCustomerName(scContractInfo.getErfCustLeName());

			productCreationInputBO.setContractDuration(scContractInfo.getOrderTermInMonths().toString());
			productCreationInputBO.setCustomerOrderNum(scOrder.getOpOrderCode());

		});

		productCreationInputBO.setCONTRACTINGADDRESS(addressConstructor.contractAddress(customerAddressLocationId));

		productCreationInputBO.setTotalArc(String.valueOf(uniqueProductCreation.getArc()));
		productCreationInputBO.setTotalNrc(String.valueOf(uniqueProductCreation.getNrc()));

		productCreationInputBO.setOrderType(uniqueProductCreation.getOrderType());
		productCreationInputBO.setChangeOrderType(uniqueProductCreation.getChangeOrderType());

		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
						Arrays.asList("billFreePeriod"));
		Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String billFreePeriod = attributeMap.getOrDefault("billFreePeriod", "0");
		productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(
				scServiceDetail.getServiceCommissionedDate().toString(),
				StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0));
		productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(
				scServiceDetail.getServiceCommissionedDate().toString(),
				StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0));
		productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(
				scServiceDetail.getServiceCommissionedDate().toString(),
				StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0));
	
		String contractGstAddress = StringUtils.trimToEmpty(addressConstructor.contractGstAddress(
				StringUtils.trimToEmpty(productCreationInputBO.getCONTRACTINGGSTINNO()), scOrder.getErfCustLeId(),scOrder));
		if (StringUtils.isNotBlank(contractGstAddress)) {
			productCreationInputBO.setCONTRACTGSTINADDRESS(contractGstAddress);
		} else {
			LOGGER.info("Contract Gst for Ipc is not Available!");
			productCreationInputBO.setCONTRACTINGGSTINNO("");
			productCreationInputBO.setCONTRACTGSTINADDRESS("");

		}
		productCreationInputBO.setSITEGSTINADDRESS("");
		productCreationInputBO.setSITEGSTINNO("");
		productCreationInputBO.setSITEEND(Constants.A);

		productCreationInputBO.setCustomerType(Constants.EXISTING);
		// productCreationInputBO.setTaxExemptTxt("No");
		productCreationInputBO.setCiruitCount(Constants.ONE);

		productCreationInputBO.setCpsName("");
		productCreationInputBO.setInvocingCoNamePrd(Constants.INVOICING_CONAME);
		productCreationInputBO.setVatIdentifier("");
		productCreationInputBO.setBusinessUnit("");
		
		Map<String, String> scComponentAttributesAMap=	commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("sourceCity","sourceState","sourceCountry","sourceAddressLineOne",
						"sourceAddressLineTwo","sourceLocality","sourcePincode"), scServiceDetail.getId(), "LM", "A");
		
		/*String sourceAddressLineOne = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceAddressLineOne"));
		String sourceAddressLineTwo = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceAddressLineTwo"));
		String sourceLocality = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceLocality"));
		String sourceCity = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceCity"));
		String sourceState = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceState"));
		String sourceCountry = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceCountry"));
		String sourcePincode = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourcePincode"));*/

		productCreationInputBO.setProdAddr1(scServiceDetail.getSourceAddressLineOne().length() >= 120
										? scServiceDetail.getSourceAddressLineOne().substring(0, 120)
										: scServiceDetail.getSourceAddressLineOne());
		productCreationInputBO.setProdAddr2(scServiceDetail.getSourceAddressLineTwo().length() >= 120
										? scServiceDetail.getSourceAddressLineTwo().substring(0, 120)
										: scServiceDetail.getSourceAddressLineTwo());
		productCreationInputBO.setProdAddr3(scServiceDetail.getSourceLocality());
		productCreationInputBO.setProdCity(scServiceDetail.getSourceCity());
		productCreationInputBO.setProdState(scServiceDetail.getSourceState());
		productCreationInputBO.setProdCountry(scServiceDetail.getSourceCountry());
		productCreationInputBO.setProdZipcode(scServiceDetail.getSourcePincode());

		if (uniqueProductCreation.getProductName().contains(Constants.VM_CHARGES)
				|| uniqueProductCreation.getProductName().contains(Constants.DATA_TRANSFER_COMM)
				|| uniqueProductCreation.getProductName().contains(Constants.DATA_TRANSFER_USAGE)
				|| uniqueProductCreation.getProductName().contains(Constants.FIXED_BW)
				|| uniqueProductCreation.getProductName().contains(Constants.ADDITIONAL_IP_CHARGES)
				|| uniqueProductCreation.getProductName().contains(Constants.BACKUP_FE_VOL)
				|| uniqueProductCreation.getProductName().contains(Constants.VDOM_SMALL)
				|| uniqueProductCreation.getProductName().contains(Constants.VPN_CLIENT_SITE)
				|| uniqueProductCreation.getProductName().contains(Constants.VPN_SITE_SITE)) {
			productCreationInputBO
					.setSITEAADDRESS(idcLocation(scServiceDetail));

		}
		
		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setEventString("");

		productCreationInputBO.setScenarioType("");

		if (!uniqueProductCreation.getOrderType().equals(Constants.CREATE)) {
			productCreationInputBO.setSourceOldProdSeq(commonBo.getSourceProductSeq());
		}

		productCreationInputBO.setRateOverrideBoo("");
		productCreationInputBO.setFinalFlag("");

		productCreationInputBO.setAttr9("");
		productCreationInputBO.setAttr10("");
		
		if(productCreationInputBO.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
			productCreationInputBO.setEventSource(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setEventsourceLabel(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setEventSourceText(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setDiscountTiers(IpcConstants.IPC_DT_USAGE_DISCOUNT_TIERS);
			productCreationInputBO.setCostBandName(IpcConstants.IPC_DEFAULT_COST_BAND_NAME);
			productCreationInputBO.setTerminateName(IpcConstants.IPC_DEFAULT_TERMINATE_NAME);
			productCreationInputBO.setEventClassName(IpcConstants.IPC_DEFAULT_EVENT_CLASS_NAME);
			productCreationInputBO.setChargeSegmentName(IpcConstants.IPC_DEFAULT_CHARGE_SEGMENT_NAME);
			productCreationInputBO.setChargingRateValue("0.00");
			productAttributeString.setSLAB_WISE_BILLING(CommonConstants.YES);
			productAttributeString.setUSAGE_MODEL(IpcConstants.IPC_DATA_TRANSFER);
			productAttributeString.setBASE_BANDWIDTH(uniqueProductCreation.getBaseBandwidth().concat(" GB"));
		} else {
			productCreationInputBO.setEventSource("");
			productCreationInputBO.setEventsourceLabel("");
			productCreationInputBO.setEventSourceText("");
			productCreationInputBO.setDiscountTiers("");
			productCreationInputBO.setCostBandName("");
			productCreationInputBO.setTerminateName("");
			productCreationInputBO.setEventClassName("");
			productCreationInputBO.setChargeSegmentName("");
			productCreationInputBO.setChargingRateValue("");
		}

		productCreationInputBO.setDiscountName("");
		productCreationInputBO.setMinQtyTier("");
		productCreationInputBO.setSourceParentProductSeq("");

		productCreationInputBO.setRateEndDtm("");
		productCreationInputBO.setComponentName("");
		productCreationInputBO.setDeliveryAddr("");

		productCreationInputBO.setEquipmentType("");
		productCreationInputBO.setSystemIdicator("");
		productCreationInputBO.setCopfId(scOrder.getOpOrderCode()); // Objects.nonNull(uniqueProductCreation.getCopfId())?uniqueProductCreation.getCopfId():scOrder.getOpOrderCode()
		productCreationInputBO.setProdQuantity(uniqueProductCreation.getQuantity());
		productCreationInputBO.setUsername("");
		productCreationInputBO.setCForm("");
		productCreationInputBO.setAddEventSRC("");
		productCreationInputBO.setTermEventSRC("");
		productCreationInputBO.setISize("");
		// productCreationInputBO.setTaxationDocsURL("");

		productCreationInputBO.setProrateBoo(Constants.T);
		productCreationInputBO.setChargePeriod(Constants.ONE);

		productCreationInputBO.setRefundBoo(Constants.T);
		productCreationInputBO.setServiceId(scServiceDetail.getUuid());
		productCreationInputBO.setSiteAGeoCode("");

		Double arc = (double) (Double.parseDouble(uniqueProductCreation.getMrc()) * 12);
		productCreationInputBO.setOvrdnPeriodicPrice(arc.toString());

		productCreationInputBO.setOvrdnIntPrice(uniqueProductCreation.getNrc());

		// Constructing attribute String
		productAttributeString.setBUSINESS_AREA(Constants.INHQ);
		productAttributeString.setCIRCUIT_ID(scServiceDetail.getUuid());

		productAttributeString.setCOMMISSIONING_DATE(
				TimeStampUtil.formatWithoutTimeStamp().format(scServiceDetail.getServiceCommissionedDate()));
		productAttributeString.setCOPF_ID(scOrder.getOpOrderCode());
		productAttributeString.setGST_PRODUCT_CODE(Constants.HSN_CODE);

		productAttributeString.setMHS_REMARKS(
				Objects.nonNull(uniqueProductCreation.getMhs_remarks()) ? uniqueProductCreation.getMhs_remarks() : "");

		productAttributeString.setLOCATION(scServiceDetail.getSourceCity());

		productAttributeString.setORDER_TYPE(Constants.NEW);
		productAttributeString.setPARENT_ID(scOrder.getTpsSfdcOptyId());
		productAttributeString.setPARENT_SERVICE(Constants.SERVICE_TYPE);
		productAttributeString.setPRODUCT_OFFERING("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setPRODUCT_SUB_TYPE("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setSERVICE_TYPE_CLUBBING(Constants.SERVICE_TYPE);

		productAttributeString.setSITE_END(Constants.A);
		productAttributeString.setSITE_NAME(scServiceDetail.getSourceCountry());

		productAttributeString.setSITE_A_AddressLine1(scServiceDetail.getSourceAddressLineOne());
		productAttributeString.setSITE_A_AddressLine2(scServiceDetail.getSourceAddressLineTwo());
		productAttributeString.setSITE_A_AddressLine3("");
		productAttributeString.setSITE_A_CITY(scServiceDetail.getSourceCity());
		productAttributeString.setSITE_A_STATE(scServiceDetail.getSourceState());
		productAttributeString.setSITE_A_COUNTRY(scServiceDetail.getSourceCountry());
		productAttributeString.setSITE_A_pincode(scServiceDetail.getSourcePincode());

		productAttributeString.setUOM(Constants.MBPS);
		productAttributeString.setQUANTITY(uniqueProductCreation.getQuantity());
		productAttributeString.setOPPORTUNITY_CLASSIFICATION(Constants.SELL_TO);
		productAttributeString.setNO_OF_MAILBOXES(Constants.ONE);
		productAttributeString.setNO_OF_PORTS(Constants.ONE);
		productAttributeString.setNO_OF_SEATS(Constants.ONE);
		productAttributeString.setNO_OF_SUPERVISOR(Constants.ONE);

		productCreationInputBO.setAttributeString(productAttributeString.toString());

		commonAttributesBO.setComReqAttributes(commonBo);
		productCreationRequestBO.setProductInput(productCreationInputBO);

		createOrderRequestBO.setProdCreationInput(productCreationRequestBO);
		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(null);

		if (!commonBo.getActionType().equals(Constants.CREATE)) {

			Optional<ServicehandoverAudit> terminateAudit = servicehandoverAuditRepository
					.findById(Integer.parseInt(uniqueProductCreation.getGenevaIpcOrderEntry().getSourceProductSeq()));
			terminateAudit.get().setStatus(commonBo.getActionType().concat("-").concat(Constants.IN_PROGRESS));
			terminateAudit.get().setUpdatedDate(new Date());
			terminateAudit.get().setGenevaGrpId(commonBo.getGroupId());
			terminateAudit.get().setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
			terminateAudit.get()
					.setMacdOrderId(commonBo.getActionType().equals(Constants.TERMINATE)
							? uniqueProductCreation.getMacdOrderId()
							: scOrder.getOpOrderCode());
			// terminateAudit.get().setOrderId(scOrder.getOpOrderCode());
			if (!commonBo.getActionType().equals(Constants.TERMINATE)) {
				terminateAudit.get().setParentCloudCode(terminateAudit.get().getCloudCode());
				terminateAudit.get().setCloudCode(uniqueProductCreation.getCloudCode());
			}
			servicehandoverAuditRepository.save(terminateAudit.get());
			ipcOrderEntry = uniqueProductCreation.getGenevaIpcOrderEntry();
			ipcOrderEntry.setGroupId(commonBo.getGroupId());
			System.out.println("Updating ipc gen entry>>>>>>>>>>>>" + ipcOrderEntry.getGroupId());
			genevaIpcOrderEntryRepository.save(ipcOrderEntry);

		} else {
			audit.setLegalEntity(productCreationInputBO.getCustomerName());
			audit.setCreatedDate(new Date());
			audit.setCrnId(commonBo.getCustomerRef());
			audit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
			audit.setRequest(createOrderRequestBO.toString());
			audit.setRequestType(Constants.PRD_COMM);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setStatus(commonBo.getActionType().concat("-").concat(Constants.IN_PROGRESS));
			audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
			audit.setGenevaGrpId(commonBo.getGroupId());
			audit.setProviderSegment(Constants.PROVIDER_SEGMENT);
			audit.setServiceId(scServiceDetail.getId().toString());
			audit.setServiceCode(productCreationInputBO.getServiceId());
			audit.setServiceType(Constants.IPC);
			audit.setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
			audit.setAccountNumber(uniqueProductCreation.getAccountId());
			audit.setCount(uniqueProductCreation.getCount());
			audit.setCloudCode(uniqueProductCreation.getCloudCode());
			audit.setParentCloudCode(uniqueProductCreation.getParentCloudCode());
			audit.setSourceProdSeq(commonBo.getSourceProductSeq());
			audit.setProductName(productCreationInputBO.getProductName());
			servicehandoverAuditRepository.save(audit);

			GenevaIpcOrderEntry ipcOdrEntry = genevaIpcOrderEntryMapper.createOrderToGenevaIpcOrderMapper(createOrderRequestBO);
			genevaIpcOrderEntryRepository.save(ipcOdrEntry);

		}

		LOGGER.info("createUniqueProduct completed for orderId {} accountId {} productName {} ",
				scOrder.getOpOrderCode(), uniqueProductCreation.getAccountId(), uniqueProductCreation.getProductName());
		return createOrderRequestBO;
	}

	/**
	 * This method generates invoice for the products commissioned.
	 * @param serviceId 
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public CreateInvoiceResponse invoiceGeneration(ServicehandoverAudit audit, String processId, String orderId,
			String serviceCode, String invoiceType, String serviceId) throws ParseException {
		LOGGER.info("invoiceGeneration invoked for orderId {}", audit.getAccountNumber());
		CreateInvoice createInvoice = new CreateInvoice();
		BSSEGInvoiceDetails invoiceDetails = new BSSEGInvoiceDetails();
		CreateInvoiceResponse createInvoiceResponse = null;
		ServicehandoverAudit servicehandoverAudit = new ServicehandoverAudit();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		Optional<ScServiceDetail> scServiceDetail =scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		List<ScChargeLineitem> scChargeLineitems = chargeLineitemRepository.findByServiceIdAndServiceType(serviceId,audit.getServiceType());
		Double nrc = scChargeLineitems.stream().collect(Collectors.summingDouble(detail->Double.parseDouble(detail.getNrc())));
		Double arc = scChargeLineitems.stream().collect(Collectors.summingDouble(detail->Double.parseDouble(detail.getArc())));
		AccountCreationInputBO accountCreationInputBO = addressConstructor.accountAddress(scContractInfo, new AccountCreationInputBO());
		String billingAddress = StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr1()) +" "+  StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr2())
				+" "+  StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr3()) +" "+  accountCreationInputBO.getAccCity()
				+" "+  accountCreationInputBO.getAccState() +" "+  accountCreationInputBO.getAccZipcode()
				+" "+  accountCreationInputBO.getAccCountry();
		List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), Constants.Y);
		scOrderAttributeList.forEach((scOrderAttribute) -> {

			switch (scOrderAttribute.getAttributeName()) {

			case LeAttributesConstants.BILLING_CURRENCY:
				invoiceDetails.setBillingCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PAYMENT_CURRENCY:
				invoiceDetails.setInfoCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PO_NUMBER:
				invoiceDetails.setPoNumber(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.PO_DATE:
				invoiceDetails.setPoDate(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.BILLING_CONTACT_NAME:
				invoiceDetails.setContactPerson(scOrderAttribute.getAttributeValue());
				break;
			}

		});
		
		invoiceDetails.setCustomerName(audit.getLegalEntity());
		invoiceDetails.setSegment(audit.getProviderSegment());
		invoiceDetails.setCrnNumber(audit.getCrnId());
		invoiceDetails.setOrderStatus(invoiceType);
		invoiceDetails.setServiceType(audit.getServiceType());
		invoiceDetails.setStatus("");
		invoiceDetails.setCreatedDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		invoiceDetails.setModifiedDate("");
		invoiceDetails.setCreatedBy("");
		invoiceDetails.setModifiedBy("");
		invoiceDetails.setServiceId(audit.getServiceCode());
		invoiceDetails.setCopfId(audit.getOrderId());
		if (invoiceType.equals(Constants.GINVOICE)) {
			invoiceDetails.setAccountNo(audit.getAccountNumber());
			invoiceDetails.setCpeAccountNo("");
			//invoiceDetails.setCommissionedDate(billStartDate!=null?TimeStampUtil.formatWithTimeStampForComm(billStartDate.getAttributeValue()):"");
		}
		String orderType=OrderCategoryMapping.getOrderType(scServiceDetail.get(), scServiceDetail.get().getScOrder());

		invoiceDetails.setLegalEntity(Constants.TATA);
		invoiceDetails.setTaskName(invoiceType);
		invoiceDetails.setCpeDeliveryAddress(scServiceDetail.get().getSiteAddress());
		invoiceDetails.setOrderType(orderType);
		invoiceDetails.setTerminationDate("");
		invoiceDetails.setDmsId("");
		invoiceDetails.setSfdcOptyId(audit.getOpportunityId());
		invoiceDetails.setTotalArc(arc.toString());
		invoiceDetails.setTotalNrc(nrc.toString());
		invoiceDetails.setCircuitCount(1);
		invoiceDetails.setSource(Constants.OPTIMUS);
		invoiceDetails.setBillingAddress(billingAddress);
		invoiceDetails.setAccountingCode("");
		invoiceDetails.setParentId("");
		invoiceDetails.setParentServiceName("");
		invoiceDetails.setCeaseFlag("");
		createInvoice.setInvoiceRequest(invoiceDetails);
		JaxbMarshallerUtil.jaxbObjectToXML(createInvoice, servicehandoverAudit);
		LOGGER.info("invoiceGeneration completed for orderId {}", audit.getOrderId());
		createInvoiceResponse = (CreateInvoiceResponse) invoiceSoapConnector.callWebService(invoiceGenerationOperation,
				createInvoice);
		if (createInvoiceResponse.getAckResponse().getStatusCode().equals("Success")) {
			servicehandoverAudit.setStatus("Sent For Invoice");
		} else {
			LOGGER.info("invoiceGeneration status {}", createInvoiceResponse.getAckResponse().getStatusCode());
			LOGGER.info("invoiceGeneration error msg {}", createInvoiceResponse.getAckResponse().getErrorDescription());
			servicehandoverAudit.setStatus("Not Sent For Invoice");
		}

		servicehandoverAudit.setProcessInstanceId(processId);
		servicehandoverAudit.setOrderId(audit.getOrderId());
		servicehandoverAudit.setServiceType(audit.getServiceType());
		servicehandoverAudit.setServiceId(audit.getServiceId());
		servicehandoverAudit.setLegalEntity(audit.getLegalEntity());
		servicehandoverAudit.setCrnId(audit.getCrnId());
		servicehandoverAudit.setOpportunityId(audit.getOpportunityId());
		servicehandoverAudit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
		servicehandoverAudit.setRequestType(Constants.INVOICE_GEN);
		servicehandoverAudit.setStatus(Constants.IN_PROGRESS);
		servicehandoverAudit.setCreatedDate(new Date());
		servicehandoverAudit.setAccountNumber(audit.getAccountNumber());
		servicehandoverAudit.setProviderSegment(audit.getProviderSegment());
		servicehandoverAuditRepository.save(servicehandoverAudit);
		
		return createInvoiceResponse;
	}

	public String groupIdGen(String processInstanceId, String groupId, boolean isMacd) {
		String grpId = groupId.concat(processInstanceId.split("-")[0]);
		return isMacd ? grpId.concat("_MACD") : grpId.concat("_NEW");
	}

	public String idcLocation(ScServiceDetail scServiceDetail) {
		return "SITE_A_AddressLine1=" + StringUtils.trimToEmpty(scServiceDetail.getSourceAddressLineOne()) + ";SITE_A_AddressLine2=;"
				+ StringUtils.trimToEmpty(scServiceDetail.getSourceAddressLineTwo()) + "SITE_A_AddressLine3=;"
				+ StringUtils.trimToEmpty(scServiceDetail.getSourceLocality()) + ";SITE_A_City="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceCity()) + ";SITE_A_State="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceState()) + ";SITE_A_Country="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceCountry()) + ";SITE_A_pincode="
				+ scServiceDetail.getSourcePincode() + ";";
	}

	/**
	 * Method to get account details for Commercial - vetting
	 * 
	 * @param serviceCode
	 * @return
	 * @throws TclCommonException 
	 */
	@Deprecated
	@Transactional(isolation = Isolation.DEFAULT)
	public CommercialVettingInputData getCommercialVettingData(String taskId) throws TclCommonException {
		Optional<Task> task = taskRepository.findById(Integer.valueOf(taskId));
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.get().getServiceId());
		List<ScProductDetail> scProductDetails = scProductDetailRepository.findByScServiceDetailId(scServiceDetail.get().getId());
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.get().getScOrderUuid(), "Y");
		CommercialVettingInputData commercialVettingInputData = new CommercialVettingInputData();
		String tempAccountNumber="Temp-".concat(scOrder.getUuid());
		List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), Constants.Y);
		scOrderAttributes.forEach((scOrderAttribute) -> {

			switch (scOrderAttribute.getAttributeName()) {

			case LeAttributesConstants.BILLING_CURRENCY:
				commercialVettingInputData.setAccountCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PAYMENT_CURRENCY:
				commercialVettingInputData.setInfoCurrency(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.BILLING_CONTACT_NAME:
				commercialVettingInputData.setFirstName(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.BILLING_CONTACT_MOBILE:
				commercialVettingInputData.setContactNumber(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.BILLING_CONTACT_EMAIL:
				commercialVettingInputData.setContactEmail(scOrderAttribute.getAttributeValue());
				break;
			case LeAttributesConstants.LE_STATE_GST_NO:
				contractGstNo = scOrderAttribute.getAttributeValue();
				break;

			case LeAttributesConstants.INVOICE_METHOD:
				if (scOrderAttribute.getAttributeValue().equals(Constants.PAPER_PLUS_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(Constants.PAPER_SLASH_ELECTRONIC)
						|| scOrderAttribute.getAttributeValue().equals(Constants.PAPER_SPACE_ELECTONIC))
					commercialVettingInputData.setBillhandlingCode(Constants.PAPER_EMAIL);
				else if (scOrderAttribute.getAttributeValue().equals(Constants.ELECTRONIC))
					commercialVettingInputData.setBillhandlingCode(Constants.EMAIL);
				else
					commercialVettingInputData.setBillhandlingCode(Constants.PAPER);
				break;
			}

		});

		commercialVettingInputData.setInvoicingCoName(Constants.INVOICING_CONAME);
		commercialVettingInputData.setAdvanceBoo(Constants.F);// T
		commercialVettingInputData.setBillingEntity(Constants.BILLING_ENTITY);
		commercialVettingInputData.setCustomerRef(loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
		commercialVettingInputData.setOpportunityId(scOrder.getTpsSfdcOptyId());
		commercialVettingInputData.setContactName(scOrder.getErfCustLeName());
		commercialVettingInputData.setCopfId(scOrder.getUuid());

		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		commercialVettingInputData.setCreditClass(scContractInfo.getPaymentTerm().toLowerCase() + " from Invoice date");
		commercialVettingInputData.setPaymentDueDate(scContractInfo.getOrderTermInMonths().toString());
		if (scContractInfo.getBillingFrequency() != null) {
			if ("monthly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				commercialVettingInputData.setBillingPeriod(Constants.ONE_M);
			else if ("quarterly".equals(scContractInfo.getBillingFrequency().toLowerCase()))
				commercialVettingInputData.setBillingPeriod(Constants.THREE_M);
			else if (scContractInfo.getBillingFrequency().toLowerCase().contains("half"))
				commercialVettingInputData.setBillingPeriod(Constants.SIX_M);
			else
				commercialVettingInputData.setBillingPeriod(Constants.TWELVE_M);
		} else {
			commercialVettingInputData.setBillingPeriod(Constants.ONE_M);
		}

		commercialVettingInputData.setAccountAddr1(Objects.nonNull(scContractInfo.getBillingAddressLine1())
				? scContractInfo.getBillingAddressLine1().length() >= 120
						? scContractInfo.getBillingAddressLine1().substring(0, 120)
						: scContractInfo.getBillingAddressLine1()
				: "");
		commercialVettingInputData.setAccountAddr2(Objects.nonNull(scContractInfo.getBillingAddressLine2())
				? scContractInfo.getBillingAddressLine2().length() >= 120
						? scContractInfo.getBillingAddressLine2().substring(0, 120)
						: scContractInfo.getBillingAddressLine2()
				: "");

		commercialVettingInputData.setAccountAddr3(Objects.nonNull(scContractInfo.getBillingAddressLine3())
				? scContractInfo.getBillingAddressLine3().length() >= 120
						? scContractInfo.getBillingAddressLine3().substring(0, 120)
						: scContractInfo.getBillingAddressLine3()
				: "");
		commercialVettingInputData.setAccountAddrCity(scContractInfo.getBillingCity());
		commercialVettingInputData.setAccountAddrState(scContractInfo.getBillingState());
		commercialVettingInputData.setAccountAddrCountry(scContractInfo.getBillingCountry());
		commercialVettingInputData.setAccountAddrZipCode(scContractInfo.getBillingPincode());

		commercialVettingInputData.setSourceSystem(Constants.SOURCE_SYSTEM);
		commercialVettingInputData.setRequestType(Constants.ACCOUNT);
		commercialVettingInputData.setActionType(Constants.CREATE);
		commercialVettingInputData.setProviderSegment(Constants.PROVIDER_SEGMENT);
		commercialVettingInputData.setCreationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		Map<String, String> attrMap = new HashMap<>();
		String billingAddress = StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddr1()) + " "
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddr2())
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddr3()) + " "
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddrCity()) + " "
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddrState()) + " "
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddrCountry()) + " "
				+ StringUtils.trimToEmpty(commercialVettingInputData.getAccountAddrZipCode());
		
		String siteGstAddress = StringUtils.trimToEmpty(scServiceDetail.get().getSourceAddressLineOne()) + " "
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourceAddressLineTwo())
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourceLocality()) + " "
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourceCity()) + " "
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourceState()) + " "
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourceCountry()) + " "
				+ StringUtils.trimToEmpty(scServiceDetail.get().getSourcePincode());
		
		
		/*List<ServicehandoverAudit> audit=servicehandoverAuditRepository.findByRequestTypeAndServiceCode(Constants.ACCOUNT_CREATION, task.get().getServiceCode());
		List<String> accountList=new ArrayList<>();
		if(audit!=null) {
			accountList.add(audit.getAccountNumber());
		}*/
		//accountList.add(tempAccountNumber);
		//attrMap.put("accountNumberList", Utils.convertObjectToJson(accountList));
		attrMap.put("billingAddress", billingAddress);
		attrMap.put("customerRef", commercialVettingInputData.getCustomerRef());
		attrMap.put("siteGstAddress", siteGstAddress);
		String contractGstAddress = StringUtils.trimToEmpty(addressConstructor
				.getContractGstAddress(StringUtils.trimToEmpty(contractGstNo), scOrder.getErfCustLeId()));
		if (StringUtils.isNotBlank(contractGstAddress)) {
			attrMap.put("contractGstAddress", contractGstAddress);
		}
		attrMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
		updateServiceAttributes(scServiceDetail.get().getId(), attrMap);
		return commercialVettingInputData;
	}
	
	public void updateServiceAttributes(Integer serviceId, Map<String, String> atMap) {
		LOGGER.info("Input Service Attributes {} --- {}",serviceId,atMap);
		Optional<ScServiceDetail> scServiceDetailEntity = scServiceDetailRepository.findById(serviceId);
		if (scServiceDetailEntity.isPresent()) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key : {} and value : {}",key,value);
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeName(serviceId, key);
				if (scServiceAttribute == null) {
					LOGGER.info("ScService Attribute is not ther for key {}",key);
					createScServiceAttr(key, value, scServiceDetailEntity.get(),null);
				} else {
					LOGGER.info("ScService Attribute is there for key {}",key);
					createScServiceAttr(key, value, scServiceDetailEntity.get(),scServiceAttribute);
				}

			});
		}

	}

	private void createScServiceAttr(String key, String value, ScServiceDetail scServiceDetail,
			ScServiceAttribute scServiceAttribute) {
		if (scServiceAttribute == null) {
			scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setCreatedBy(Utils.getSource());
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		} else {
			scServiceAttribute.setUpdatedBy(Utils.getSource());
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		}
		scServiceAttribute.setAttributeAltValueLabel(value);
		scServiceAttribute.setAttributeName(key);
		scServiceAttribute.setAttributeValue(value);
		scServiceAttribute.setCategory(null);
		scServiceAttribute.setIsActive(CommonConstants.Y);
		scServiceAttribute.setIsAdditionalParam(CommonConstants.N);
		scServiceAttribute.setScServiceDetail(scServiceDetail);
		scServiceAttributeRepository.save(scServiceAttribute);
	}

	@Deprecated
	private CreateOrderResponse teminateBilledProducts(List<GenevaIpcOrderEntry> genevaIpcOrderEntryList, String serviceId) {
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		genevaIpcOrderEntryList.forEach(orderEntryAudit -> {
			CreateOrderRequestBO createOrderRequestBO = terminateProduct(orderEntryAudit,serviceId);
			createOrderBO.getReqOrder().add(createOrderRequestBO);
		});
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		String status = "";
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("product termination status for serviceId {} ", status);
		return createOrderResponse;

	}

	@Deprecated
	private CreateOrderRequestBO terminateProduct(GenevaIpcOrderEntry orderEntryAudit, String serviceId) {
		LOGGER.info("terminateUniqueProduct invoked for orderId {} accountId {} productName {} ",
				orderEntryAudit.getCopfId(), orderEntryAudit.getAccountNum(), orderEntryAudit.getProductName());
		ServicehandoverAudit terminateAudit = servicehandoverAuditRepository
				.findBySourceProdSeq(orderEntryAudit.getSourceProductSeq());
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		ProductCreationRequestBO productCreationRequestBO = new ProductCreationRequestBO();
		ProductCreationInputBO productCreationInputBO = new ProductCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		CommonBO commonBo = new CommonBO();
		commonBo.setGroupId(orderEntryAudit.getGroupId().concat("_TERM"));
		commonBo.setSourceSystem(Constants.SOURCE_SYSTEM);
		commonBo.setRequestType(Constants.PRODUCT);
		commonBo.setActionType(Constants.TERMINATE);
		commonBo.setCustomerRef(orderEntryAudit.getCustomerRef());
		commonBo.setAccountNum(orderEntryAudit.getAccountNum());
		commonBo.setInvoicingCoName(Constants.INVOICING_CONAME);
		commonBo.setBillingEntity(Constants.BILLING_ENTITY);
		commonBo.setProviderSegment(Constants.PROVIDER_SEGMENT);
		commonBo.setAdvanceBoo(Constants.T);
		commonBo.setServiceType(Constants.SERVICE_TYPE);

		commonBo.setSourceProductSeq(orderEntryAudit.getSourceProductSeq());
		productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStamp().format(new Date()).toString());
		productCreationInputBO.setTermReason(Constants.UPGRADE);
		productCreationInputBO.setDepositRefundBoo(Constants.F);
		productCreationInputBO.setProductName(orderEntryAudit.getProductName());

		commonBo.setAccountingCurrency(orderEntryAudit.getAccountingCurrency());
		productCreationInputBO.setCurrencyCode(orderEntryAudit.getAccountingCurrency());
		productCreationInputBO.setCurrencyCodePrd(orderEntryAudit.getAccountingCurrency());

		commonBo.setInfoCurrency(orderEntryAudit.getInfoCurrency());

		productCreationInputBO.setCONTRACTINGGSTINNO(orderEntryAudit.getContractGstinNo());

		productCreationInputBO.setTaxExemptTxt(orderEntryAudit.getTaxExempt_txt());
		productCreationInputBO.setTaxExemptRef(orderEntryAudit.getTaxExempt_ref());

		productCreationInputBO.setTaxationDocsURL("");

		commonBo.setPaymentDueDate(orderEntryAudit.getPayment_dueDate());
		productCreationInputBO.setCustomerName(orderEntryAudit.getCustomerName());

		productCreationInputBO.setContractDuration(orderEntryAudit.getContractDuration().toString());
		productCreationInputBO.setCustomerOrderNum(orderEntryAudit.getCustomer_orderNum());

		productCreationInputBO.setCONTRACTINGADDRESS(orderEntryAudit.getContractingAddress());

		productCreationInputBO.setTotalArc(orderEntryAudit.getTotalArc());
		productCreationInputBO.setTotalNrc(orderEntryAudit.getTotalNrc());
		productCreationInputBO.setOrderType(Constants.MODIFY);
		productCreationInputBO.setChangeOrderType(Constants.TERMINATE_ORDER);
		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		productCreationInputBO.setCommissionDate(orderEntryAudit.getCommissionDate());
		productCreationInputBO.setBillActivationDate(orderEntryAudit.getBillActivation_date());
		productCreationInputBO.setBillGenerationDate(orderEntryAudit.getBillGeneration_date());

		productCreationInputBO.setCONTRACTGSTINADDRESS(orderEntryAudit.getContractGstinAddress());
		productCreationInputBO.setSITEGSTINADDRESS("");
		productCreationInputBO.setSITEGSTINNO("");
		productCreationInputBO.setSITEEND(Constants.A);

		productCreationInputBO.setCustomerType(Constants.EXISTING);
		productCreationInputBO.setCiruitCount(Constants.ONE);

		productCreationInputBO.setCpsName("");
		productCreationInputBO.setInvocingCoNamePrd(Constants.INVOICING_CONAME);
		productCreationInputBO.setVatIdentifier("");
		productCreationInputBO.setBusinessUnit("");

		productCreationInputBO.setProdAddr1(orderEntryAudit.getProdAddr1());
		productCreationInputBO.setProdAddr2(orderEntryAudit.getProdAddr2());
		productCreationInputBO.setProdAddr3(orderEntryAudit.getProdAddr3());
		productCreationInputBO.setProdCity(orderEntryAudit.getProdCity());
		productCreationInputBO.setProdState(orderEntryAudit.getProdState());
		productCreationInputBO.setProdCountry(orderEntryAudit.getProdCountry());
		productCreationInputBO.setProdZipcode(orderEntryAudit.getProdZipcode());

		productCreationInputBO.setSITEAADDRESS(orderEntryAudit.getSiteAAddress());

		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setEventSource("");
		productCreationInputBO.setEventsourceLabel("");
		productCreationInputBO.setEventSourceText("");

		productCreationInputBO.setEventString("");

		productCreationInputBO.setScenarioType("");

		productCreationInputBO.setSourceOldProdSeq(orderEntryAudit.getSourceProductSeq());

		productCreationInputBO.setRateOverrideBoo("");
		productCreationInputBO.setFinalFlag("");

		productCreationInputBO.setAttr9("");
		productCreationInputBO.setAttr10("");

		productCreationInputBO.setDiscountName("");
		productCreationInputBO.setDiscountTiers("");
		productCreationInputBO.setMinQtyTier("");

		productCreationInputBO.setTerminateName("");
		productCreationInputBO.setSourceParentProductSeq("");
		productCreationInputBO.setEventClassName("");
		productCreationInputBO.setChargeSegmentName("");
		productCreationInputBO.setChargingRateValue("");

		productCreationInputBO.setRateEndDtm("");
		productCreationInputBO.setComponentName("");
		productCreationInputBO.setDeliveryAddr("");

		productCreationInputBO.setEquipmentType("");
		productCreationInputBO.setSystemIdicator("");
		productCreationInputBO.setCopfId(orderEntryAudit.getCopfId());
		productCreationInputBO.setProdQuantity(orderEntryAudit.getProdQuantity().toString());
		productCreationInputBO.setUsername("");
		productCreationInputBO.setCForm("");
		productCreationInputBO.setAddEventSRC("");
		productCreationInputBO.setTermEventSRC("");
		productCreationInputBO.setISize("");
		// productCreationInputBO.setTaxationDocsURL("");

		productCreationInputBO.setProrateBoo(Constants.T);
		productCreationInputBO.setChargePeriod(Constants.ONE);

		productCreationInputBO.setRefundBoo(Constants.T);
		productCreationInputBO.setServiceId(orderEntryAudit.getServiceId());
		productCreationInputBO.setSiteAGeoCode("");

		productCreationInputBO.setCostBandName("");

		productCreationInputBO.setOvrdnPeriodicPrice(orderEntryAudit.getOvrdn_periodicPrice());

		productCreationInputBO.setOvrdnIntPrice(orderEntryAudit.getOvrdn_initPrice());

		productCreationInputBO.setAttributeString(orderEntryAudit.getAttributeString());

		commonAttributesBO.setComReqAttributes(commonBo);
		productCreationRequestBO.setProductInput(productCreationInputBO);

		createOrderRequestBO.setProdCreationInput(productCreationRequestBO);
		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(null);

		terminateAudit.setStatus(commonBo.getActionType().concat("-").concat(Constants.IN_PROGRESS));
		terminateAudit.setUpdatedDate(new Date());
		terminateAudit.setGenevaGrpId(commonBo.getGroupId());
		terminateAudit.setRequestType(Constants.PRD_TERM);
		servicehandoverAuditRepository.save(terminateAudit);
		
		/*
		 * ScChargeLineitem chargeLineitem = new ScChargeLineitem(); chargeLineitem =
		 * new ScChargeLineitem();
		 * chargeLineitem.setChargeLineitem(orderEntryAudit.getProductName());
		 * chargeLineitem.setMrc(orderEntryAudit.getTotalArc());
		 * chargeLineitem.setNrc(orderEntryAudit.getTotalNrc());
		 * chargeLineitem.setArc(orderEntryAudit.getTotalArc());
		 * chargeLineitem.setQuantity(orderEntryAudit.getProdQuantity().toString());
		 * chargeLineitem.setAccountNumber("");
		 * chargeLineitem.setServiceType(orderEntryAudit.getServiceType());
		 * chargeLineitem.setServiceId(serviceId);
		 * chargeLineitem.setServiceCode(orderEntryAudit.getServiceId());
		 * chargeLineitemRepository.save(chargeLineitem);
		 */
		LOGGER.info("createUniqueProduct completed for orderId {} accountId {} productName {} ",
				orderEntryAudit.getCopfId(), orderEntryAudit.getAccountNum(), orderEntryAudit.getProductName());
		return createOrderRequestBO;
	}
	
}
