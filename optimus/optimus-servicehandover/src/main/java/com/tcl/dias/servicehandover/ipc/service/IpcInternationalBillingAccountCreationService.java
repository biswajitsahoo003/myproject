package com.tcl.dias.servicehandover.ipc.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.converters.XMLGregorianCalendarConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.CustomerCrn;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
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
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.intl.account.beans.AuditLog;
import com.tcl.dias.servicehandover.intl.account.beans.GenevaProfileSaveInfo;
import com.tcl.dias.servicehandover.intl.account.beans.SECSProfileSaveInfo;
import com.tcl.dias.servicehandover.intl.account.beans.Security;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfile;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileInput;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileResponse;
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
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.CreateAccountJeoparyResponse;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.PopDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import javassist.expr.NewArray;

/**
 * This service handles are account logics for product across Optimus (IPC)
 *  @author ram
 */

@Service
public class IpcInternationalBillingAccountCreationService {
		
	
	@Autowired
	@Qualifier("IntlAccount")
	SOAPConnector intlAccCreationConnector;

	@Value("${intlAccountCreation}")
	private String intlAccOperation;

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
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	String customerAddressLocationId = "";
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	SetSECSProfileResponse secsProfileResponse = null;
		
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcInternationalBillingAccountCreationService.class);

	/**
	 * This method initiates account creation process for the order placed.
	 * 
	 * @param orderId
	 * @param serviceId 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Transactional(isolation = Isolation.DEFAULT)
	public SetSECSProfileResponse accountCreation(String orderCode, String processInstanceId, String serviceCode,
			String  serviceId, String serviceType) throws IllegalAccessException, InvocationTargetException {
		SetSECSProfileResponse secsProfileResponse = new SetSECSProfileResponse();
		AccountInputData accountInputData = accountCreationService.loadAccountDataFromO2C(orderCode, serviceCode, serviceType, serviceId);
		secsProfileResponse = accountCreation(orderCode, processInstanceId, serviceCode, serviceType, serviceId, "", "",accountInputData);
		return secsProfileResponse;
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
	
	private SetSECSProfileResponse accountCreation(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId, String siteType, String splitRatio,AccountInputData accountInputData) {
		LOGGER.info("Account creation started for Order id {} and Service type {}", orderId, serviceType);
		String status ="";
		String secsId="";
		String sapCode="";
		String region ="";
		String customerType ="";
		String billingFrequency="M";
		SortedMap<String, String> invoiceMethodMap= new TreeMap<String, String>();;
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn( Integer.parseInt(serviceId), Arrays.asList("secsId","sapCode")));
		if (attributeMap != null) {
			secsId = attributeMap.getOrDefault("secsId", loadCustomerDetails.getSecsCode(scOrder.getErfCustLeId()));
			sapCode = attributeMap.get("sapCode");
			customerType = sapCode.contains("ENT")?"EN":"SP";
			region = sapCode.split("_")[2].split("\\*")[0];
		}
		if (accountInputData.getBillingFrequency() != null) {
			if ("1M".equals(accountInputData.getBillingFrequency()))
				billingFrequency = "M";
			else if ("2M".equals(accountInputData.getBillingFrequency()))
				billingFrequency = "B";
			else if ("3M".equals(accountInputData.getBillingFrequency()))
				billingFrequency = "Q";
			else if ("6M".equals(accountInputData.getBillingFrequency()))
				billingFrequency = "H";
			else
				billingFrequency = "Y";
		}
		if (accountInputData.getBillHandlingCode() != null) {
			if (NetworkConstants.PAPER_SLASH_ELECTRONIC.equals(accountInputData.getInvoiceMethod())) {
				invoiceMethodMap.put("PAPML","Mail and Paper Format");
			} else if (NetworkConstants.ELECTRONIC.equals(accountInputData.getInvoiceMethod())) {
				invoiceMethodMap.put("EMAIL","Mail");
			} else {
				invoiceMethodMap.put("PAPER","Paper");
			}
		}
		
		SetSECSProfile secsProfile = new SetSECSProfile();
		SetSECSProfileInput secsProfileInput = new SetSECSProfileInput();
		SECSProfileSaveInfo secsProfileSaveInfo = new SECSProfileSaveInfo();
		Security security = new Security();
		GenevaProfileSaveInfo genevaProfileSaveInfo = new GenevaProfileSaveInfo();
		AuditLog auditLog = new AuditLog();
		
		security.setSourceSystem(NetworkConstants.SOURCE_SYSTEM);
		security.setUniqueKey(String.valueOf(System.currentTimeMillis()));
		
		secsProfileInput.setOrganizationNumber(Integer.parseInt(secsId));
		secsProfileInput.setCompanyID(customerType.concat("TIP".equals(region)?"TSN":region));
		secsProfileInput.setInputFlag(1);
		secsProfileInput.setProflieRelNum(0);
		
		auditLog.setCreatedBy("OPTIMUS ADMIN");
		auditLog.setCreatedDate(TimeStampUtil.xMLGregorianCalendar());
		auditLog.setUpdatedBy("OPTIMUS ADMIN");
		auditLog.setUpdatedDate(TimeStampUtil.xMLGregorianCalendar());
	
		secsProfileSaveInfo.setPaymentMethodCode(1);
		secsProfileSaveInfo.setBillTermCode(billingFrequency);
		secsProfileSaveInfo.setCurrency(accountInputData.getAccountingCurrency());
		secsProfileSaveInfo.setPaymentTermCode(accountInputData.getCreditClass().split(" ")[0]);
		secsProfileSaveInfo.setBillingContactTitle(" ");
		secsProfileSaveInfo.setBillingContactFirstName(accountInputData.getFirstName());
		secsProfileSaveInfo.setBillingContactLastName(accountInputData.getLastName());
		secsProfileSaveInfo.setBillingContactLandLine(accountInputData.getContactNumber());
		secsProfileSaveInfo.setBillingContactMobile(accountInputData.getContactNumber());
		secsProfileSaveInfo.setBillingContactEmailId(accountInputData.getContactEmail());
		secsProfileSaveInfo.setBillingAddressLine1(accountInputData.getAccountAddr1());
		secsProfileSaveInfo.setBillingAddressLine2(accountInputData.getAccountAddr2());
		secsProfileSaveInfo.setBillingAddressLine3(accountInputData.getAccountAddr3());
		secsProfileSaveInfo.setBillingAddressLine4("");
		secsProfileSaveInfo.setBillingCity(accountInputData.getAccountAddrCity());
		LOGGER.info("Billing Country is {}",accountInputData.getAccountAddrCountry());
		if (!"united kingdom".equals(accountInputData.getAccountAddrCountry().toLowerCase())) {
			LOGGER.info("state not required for United Kingdom");
			secsProfileSaveInfo.setBillingState(accountInputData.getAccountAddrState());
		}
		/*if (accountInputData.getAccountAddrCountry().toLowerCase().contains("united states")
				|| accountInputData.getAccountAddrCountry().toLowerCase().contains("canada")) {
			LOGGER.info("state not required for United Kingdom & Europe");
			secsProfileSaveInfo.setBillingState(accountInputData.getAccountAddrState());
		}*/
		secsProfileSaveInfo.setBillingCountry(accountInputData.getAccountAddrCountry());
		secsProfileSaveInfo.setBillingPincode(accountInputData.getAccountAddrZipCode());
		secsProfileSaveInfo.setInvoiceEmail(accountInputData.getContactEmail());
		secsProfileSaveInfo.setInvoiceMethod(invoiceMethodMap.firstKey());
		      
        genevaProfileSaveInfo.setInvoiceMethod(invoiceMethodMap.get(secsProfileSaveInfo.getInvoiceMethod()));
        genevaProfileSaveInfo.setBillTermCode(accountInputData.getBillingFrequency());
        genevaProfileSaveInfo.setBillingCurrency(accountInputData.getAccountingCurrency());
        genevaProfileSaveInfo.setInfoCurrency(accountInputData.getInfoCurrency());
        genevaProfileSaveInfo.setPaymentTermCode(secsProfileSaveInfo.getPaymentTermCode());
        genevaProfileSaveInfo.setBillingContactFirstName(secsProfileSaveInfo.getBillingContactFirstName());
        genevaProfileSaveInfo.setBillingContactLastName(secsProfileSaveInfo.getBillingContactLastName());
        genevaProfileSaveInfo.setBillingContactLandLine(secsProfileSaveInfo.getBillingContactLandLine());
        genevaProfileSaveInfo.setBillingContactMobile(secsProfileSaveInfo.getBillingContactMobile());
        genevaProfileSaveInfo.setBillingContactEmailId(secsProfileSaveInfo.getBillingContactEmailId());
        genevaProfileSaveInfo.setBillingAddressLine1(secsProfileSaveInfo.getBillingAddressLine1());
        genevaProfileSaveInfo.setBillingAddressLine2(secsProfileSaveInfo.getBillingAddressLine2());
        genevaProfileSaveInfo.setBillingAddressLine3(secsProfileSaveInfo.getBillingAddressLine3());
        genevaProfileSaveInfo.setBillingAddressLine4("");
        genevaProfileSaveInfo.setBillingCity(secsProfileSaveInfo.getBillingCity());
        genevaProfileSaveInfo.setBillingState(genevaProfileSaveInfo.getBillingCity());
    	/*if (!accountInputData.getAccountAddrCountry().toLowerCase().contains("united states")
				|| !accountInputData.getAccountAddrCountry().toLowerCase().contains("canada")) {
        	LOGGER.info("state name is as city for United Kingdom & Europe");
        	genevaProfileSaveInfo.setBillingState(genevaProfileSaveInfo.getBillingCity());
		}
		if (secsProfileSaveInfo.getBillingCountry().toLowerCase().contains("united states")) {
			genevaProfileSaveInfo.setBillingCountry("United States");
		} else {
			genevaProfileSaveInfo.setBillingCountry(secsProfileSaveInfo.getBillingCountry());
		}*/
		genevaProfileSaveInfo.setBillingCountry(secsProfileSaveInfo.getBillingCountry());
        genevaProfileSaveInfo.setBillingPincode(secsProfileSaveInfo.getBillingPincode());

        secsProfileInput.setBillingMethod("BILLING_HANDLING_CODE="+secsProfileSaveInfo.getInvoiceMethod()+";BILLING_METHOD=Mixed;SERVICE_TYPE=TCLi;PROFILE_ID=");
        secsProfileInput.setCRNID(sapCode);
        
        secsProfileInput.setSecurity(security);
        secsProfileInput.setSECSProfileSaveInfo(secsProfileSaveInfo);
        secsProfileInput.setGenevaProfileSaveInfo(genevaProfileSaveInfo);
        secsProfileInput.setAuditLog(auditLog);
        secsProfile.setSetSECSProfileInput(secsProfileInput);
       
        ServicehandoverAudit audit = new ServicehandoverAudit();
        audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
		audit.setLegalEntity(scOrder.getErfCustLeName());
		audit.setCreatedDate(new Date());
		audit.setCrnId(secsProfileInput.getCRNID());
		audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
		audit.setProviderSegment("");
		audit.setRequestType(NetworkConstants.ACCOUNT_CREATION);
		audit.setOrderId(scOrder.getOpOrderCode());
		audit.setServiceId(serviceId);
		audit.setServiceCode(serviceCode);
		audit.setStatus(NetworkConstants.IN_PROGRESS);
		audit.setGenevaGrpId(security.getUniqueKey());
		audit.setProcessInstanceId(processInstanceId);
		audit.setCount(1);
		audit.setServiceType(serviceType);
		audit.setSiteType(siteType);
		audit.setSplitRatio(splitRatio);
		JaxbMarshallerUtil.jaxbObjectToXML(secsProfile, audit);
		servicehandoverAuditRepo.save(audit);

				
		secsProfileResponse = (SetSECSProfileResponse) intlAccCreationConnector.callWebService(intlAccOperation, secsProfile);
		if (Objects.nonNull(secsProfileResponse)) {
			status = "0".equals(secsProfileResponse.getSetSECSProfileOutput().getResponseHeader().getStatus().toString()) ? "Success" :"Failure";
			if(status.equals("Success") && StringUtils.isNotEmpty(secsProfileResponse.getSetSECSProfileOutput().getAccountNumber())){
				Map<String, String> attributeAMap = new HashMap<>();
				attributeAMap.put(IpcConstants.IPC_BILLING_ACCOUNT_NUMBER, secsProfileResponse.getSetSECSProfileOutput().getAccountNumber());
				List<IpcChargeLineitem> lineItemsList = ipcChargeLineitemRepository.findByServiceIdAndServiceType(serviceId, serviceType);
				LOGGER.info("Total Charge Lineitems found {}", lineItemsList.size());
				lineItemsList.stream().forEach(lineitem->{
					lineitem.setAccountNumber(secsProfileResponse.getSetSECSProfileOutput().getAccountNumber());
					ipcChargeLineitemRepository.saveAndFlush(lineitem);
				});
				attributeAMap.put( "CRNID", secsProfileInput.getCRNID());
				componentAndAttributeService.updateServiceAttributes(scServiceDetail.get().getId(), attributeAMap);
				componentAndAttributeService.updateAttributes(scServiceDetail.get().getId(), attributeAMap,AttributeConstants.COMPONENT_LM, "A");
			}
			return secsProfileResponse;
		}
		LOGGER.info("Account Creation completed for orderId {} with status {}", orderId, status);
		return secsProfileResponse;
	}
}
