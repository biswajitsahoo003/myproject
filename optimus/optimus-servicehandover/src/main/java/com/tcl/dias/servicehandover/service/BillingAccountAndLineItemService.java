package com.tcl.dias.servicehandover.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.ChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingBean;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingDetails;
import com.tcl.dias.servicefulfillmentutils.beans.SiteBBillingAddress;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicehandover.beans.renewal.RenewalCommercialChargeLineItemBean;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.AccountDetails;
import com.tcl.dias.servicehandover.util.AccountResponse;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * BillingAccountAndLineItemService will be used to load account number and store charge line items
 * 
 * @author yogesh
 */
@Service
@Transactional(readOnly = false)
public class BillingAccountAndLineItemService extends ServiceFulfillmentBaseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingAccountAndLineItemService.class);
	
	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;
	
	@Autowired 
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	BillingProductCreationServiceOld billingHandoverService;
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository; 
	
	String cpeAccountNumber=null;
	
	String nonCpeAccountNumber=null;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	FlowableBaseService flowableBaseService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	public ChargeLineItemBean billingLineItems(Integer taskId, ChargeLineItemBean chargeLineItems) throws TclCommonException {
		Task task = getTaskById(taskId);
		Map<String, String> atMap = new HashMap<>();

		validateInputs(task, chargeLineItems);
		List<LineItemDetailsBean> lineItems = chargeLineItems.getLineItemDetails();
		chargeLineitemRepository.deleteLineItems(task.getServiceId().toString());
		chargeLineitemRepository.flush();
		lineItems.forEach(lineItem -> {
			ScChargeLineitem chargeLineitem = new ScChargeLineitem();
			chargeLineitem.setNrc(lineItem.getNrc());
			chargeLineitem.setArc(lineItem.getArc());
			chargeLineitem.setMrc(lineItem.getMrc());
			chargeLineitem.setChargeLineitem(lineItem.getLineitem());
			chargeLineitem.setAccountNumber(lineItem.getAccountNumber());
			chargeLineitem.setServiceType(lineItem.getServiceType());
			chargeLineitem.setServiceId(task.getServiceId().toString());
			chargeLineitem.setServiceCode(task.getServiceCode());
			chargeLineitem.setQuantity(lineItem.getQuantity());
			chargeLineitem.setUnitOfMeasurement(lineItem.getUnitOfMeasurement());
			chargeLineitem.setBillingMethod(lineItem.getBillingMethod());
			chargeLineitem.setIsProrated(lineItem.getIsProrated());
			//chargeLineitem.setComponent(getComponent(chargeLineitem.getChargeLineitem()));
			chargeLineitem.setComponent(lineItem.getComponent());
			if (NetworkConstants.CPE.equals(lineItem.getServiceType())) {
				chargeLineitem.setCpeModel(lineItem.getCpeModel());
			} else {
				chargeLineitem.setSkuId(lineItem.getCpeModel());
			}
			chargeLineitem.setHsnCode(lineItem.getHsnCode());
			chargeLineitem.setSiteType(lineItem.getSiteType());
			chargeLineitem.setCommissioningFlag("N");
			chargeLineitem.setUsageArc(lineItem.getUsageArc());
			chargeLineitem.setBillingType(lineItem.getBillingType());
			chargeLineitem.setComponentDesc(lengthCheck(lineItem.getDescription()));
			chargeLineitem.setEffectiveOverage(lineItem.getEffectiveOverage());
			chargeLineitem.setEffectiveUsage(lineItem.getEffectiveUsage());
			chargeLineitemRepository.saveAndFlush(chargeLineitem);
		});

		Double nrc = lineItems.stream()
				.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
		Double arc = lineItems.stream()
				.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
		if (scServiceDetail != null && nrc != null && arc != null) {
			scServiceDetail.setMrc(arc / 12);
			scServiceDetail.setArc(arc);
			scServiceDetail.setNrc(nrc);
			scServiceDetailRepository.save(scServiceDetail);
		}

		boolean isAccountCreationRequired = chargeLineItems.getLineItemDetails().stream()
				.anyMatch(chargeLineItem -> chargeLineItem.getAccountNumber().contains("OPTACC_"));
		boolean isBurstableIncluded = chargeLineItems.getLineItemDetails().stream()
				.anyMatch(chargeLineItem -> chargeLineItem.getLineitem().contains("Burstable"));

		atMap.put("isAccountRequired", isAccountCreationRequired ? "Y" : "N");
		atMap.put("isBurstable", isBurstableIncluded ? "Y" : "N");

		processTaskLogDetails(task, "CLOSED", chargeLineItems.getDelayReason(), null, chargeLineItems);
	   	
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");
		Map<String, Object> wfMap = new HashMap<>();
	   	wfMap.put("action", "close");
	   	wfMap.put("isAccountRequired", isAccountCreationRequired);
	   	taxAndBillingFlag(wfMap,task.getServiceId());
	  return (ChargeLineItemBean) flowableBaseService.taskDataEntry(task, chargeLineItems,wfMap);
	}
	
	public AccountResponse getAccountNumber(String inputGroupId) throws TclCommonException{
		AccountResponse accountResponse = null;
		try{
			String accountNumber = gnvOrderEntryTabRepository.findByInputGroupId(inputGroupId);
			accountResponse = new AccountResponse();
			if (accountNumber!=null) {
				 accountResponse.setStatus(NetworkConstants.SUCCESS);
				 accountResponse.setAccountNumber(accountNumber);
			}
			else {
				accountResponse.setStatus("Account Creation not Successful!");
			}
		} catch (Exception e) {
			throw new TclCommonException(NetworkConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return accountResponse;
	}
	
	public String getComponent(String chargeLinitem) {
		String component = "";
		if ("Local Access Charges".equals(chargeLinitem) || "Mast Charges".equals(chargeLinitem)) {
			component = "Access";
		}
		if ("CPE Outright Charges".equals(chargeLinitem) || "CPE Management Charges".equals(chargeLinitem)
				|| "CPE Installation Charges".equals(chargeLinitem) || "CPE Rental Charges".equals(chargeLinitem)) {
			component = "CPE";
		}
		if ("Fixed Port Charges".equals(chargeLinitem)) {
			component = "IP Port";
		}
		if("GVPN Port Charges".equals(chargeLinitem)) {
			component="GVPN Port";
		}
		if (chargeLinitem.contains("Additional IP")) {
			component = "Add on Services";
		}
		return component;
	}
	
	public AccountResponse saveAccountData(AccountInputData accountInputData,Integer taskId) {
		Task task = getTaskById(taskId);
		AccountResponse accountResponse = new AccountResponse(); 
		Map<String, String> atMap = new HashMap<>();
		if (task != null) {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
			if (scServiceDetail != null) {
				ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), "Y");
				ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
				if (scContractInfo != null) {
					if(scOrder!=null &&  "Y".equals(scOrder.getSiteLevelBilling())) {
						atMap.put("billingAddressLineOne", StringUtils.trimToEmpty(accountInputData.getAccountAddr1()));
						atMap.put("billingAddressLineTwo", StringUtils.trimToEmpty(accountInputData.getAccountAddr2()));
						atMap.put("billingAddressLineThree", StringUtils.trimToEmpty(accountInputData.getAccountAddr3()));
						atMap.put("billingAddressCity", StringUtils.trimToEmpty(accountInputData.getAccountAddrCity()));
						atMap.put("billingAddressState", StringUtils.trimToEmpty(accountInputData.getAccountAddrState()));
						atMap.put("billingAddressCountry", StringUtils.trimToEmpty(accountInputData.getAccountAddrCountry()));
						atMap.put("billingAddressPincode", StringUtils.trimToEmpty(accountInputData.getAccountAddrZipCode()));
						
						SiteBBillingAddress siteBBillingAddress = accountInputData.getSiteBBillingAddress();
						if(siteBBillingAddress!=null) {
							Map<String, String> atMapB = new HashMap<>();
							atMapB.put("billingAddressLineOne", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr1()));
							atMapB.put("billingAddressLineTwo", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr2()));
							atMapB.put("billingAddressLineThree", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr3()));
							atMapB.put("billingAddressCity", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrCity()));
							atMapB.put("billingAddressState", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrState()));
							atMapB.put("billingAddressCountry", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrCountry()));
							atMapB.put("billingAddressPincode", StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrZipCode()));
							componentAndAttributeService.updateAttributes(task.getServiceId(), atMapB,AttributeConstants.COMPONENT_LM, "B");
						}
						
					}else {
						scContractInfo.setBillingAddressLine1(StringUtils.trimToEmpty(accountInputData.getAccountAddr1()));
						scContractInfo.setBillingAddressLine2(StringUtils.trimToEmpty(accountInputData.getAccountAddr2()));
						scContractInfo.setBillingAddressLine3(StringUtils.trimToEmpty(accountInputData.getAccountAddr3()));
						scContractInfo.setBillingCity(StringUtils.trimToEmpty(accountInputData.getAccountAddrCity()));
						scContractInfo.setBillingState(StringUtils.trimToEmpty(accountInputData.getAccountAddrState()));
						scContractInfo.setBillingCountry(StringUtils.trimToEmpty(accountInputData.getAccountAddrCountry()));
						scContractInfo.setBillingPincode(StringUtils.trimToEmpty(accountInputData.getAccountAddrZipCode()));
					}
					scContractInfo.setBillingFrequency(StringUtils.trimToEmpty(accountInputData.getBillingFrequency()));
					scContractInfo.setPaymentTerm(StringUtils.trimToEmpty(accountInputData.getCreditClass().split(" ")[0].concat(" Days")));
					scContractInfo.setBillingMethod(StringUtils.trimToEmpty(accountInputData.getBillingMethod()));
					scContractInfoRepository.save(scContractInfo);
				}

				if (accountInputData.getBillHandlingCode() != null) {
					ScOrderAttribute attribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.INVOICE_METHOD, scOrder);
					if (attribute != null) {
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getBillHandlingCode()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getBillHandlingCode()));

					} else {
						attribute = new ScOrderAttribute();
						attribute.setAttributeName(LeAttributesConstants.INVOICE_METHOD);
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getBillHandlingCode()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getBillHandlingCode()));
						attribute.setIsActive("Y");
						attribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(attribute);
				}

				if (accountInputData.getContactNumber() != null) {
					ScOrderAttribute attribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_MOBILE, scOrder);
					if (attribute != null) {
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getContactNumber()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getContactNumber()));

					} else {
						attribute = new ScOrderAttribute();
						attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_MOBILE);
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getContactNumber()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getContactNumber()));
						attribute.setIsActive("Y");
						attribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(attribute);
				}
				if (accountInputData.getContactEmail() != null) {

					ScOrderAttribute attribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_EMAIL, scOrder);
					if (attribute != null) {
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getContactEmail()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getContactEmail()));
					} else {
						attribute = new ScOrderAttribute();
						attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_EMAIL);
						attribute.setAttributeValue(StringUtils.trimToEmpty(accountInputData.getContactEmail()));
						attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(accountInputData.getContactEmail()));
						attribute.setIsActive("Y");
						attribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(attribute);
				}
				if (accountInputData.getFirstName() != null && scOrder != null) {
					ScOrderAttribute attribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_NAME, scOrder);
					if (attribute != null) {
						attribute.setAttributeValue(accountInputData.getFirstName() + " "
								+ StringUtils.trimToEmpty(accountInputData.getLastName()));
						attribute.setAttributeAltValueLabel(accountInputData.getFirstName() + " "
								+ StringUtils.trimToEmpty(accountInputData.getLastName()));
					} else {
						attribute = new ScOrderAttribute();
						attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_NAME);
						attribute.setAttributeValue(accountInputData.getFirstName() + " "
								+ StringUtils.trimToEmpty(accountInputData.getLastName()));
						attribute.setAttributeAltValueLabel(accountInputData.getFirstName() + " "
								+ StringUtils.trimToEmpty(accountInputData.getLastName()));
						attribute.setIsActive("Y");
						attribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(attribute);
				}
				if (scOrder != null && (scOrder.getOrderType() != null && "RENEWALS".equals(scOrder.getOrderType()))) {
					ScOrderAttribute attribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(BillingConstants.SAP_CODE, scOrder);
					if (attribute != null) {
						attribute.setAttributeValue(accountInputData.getCustomerRef());
						attribute.setAttributeAltValueLabel(accountInputData.getCustomerRef());
					}else{
						attribute = new ScOrderAttribute();
						attribute.setAttributeName(BillingConstants.SAP_CODE);
						attribute.setAttributeValue(accountInputData.getCustomerRef());
						attribute.setAttributeAltValueLabel(accountInputData.getCustomerRef());
						attribute.setIsActive("Y");
						attribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(attribute);
				} else {
					LOGGER.info("Sap Code updated as {}", accountInputData.getCustomerRef());
					atMap.put(BillingConstants.SAP_CODE, accountInputData.getCustomerRef());
					componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,AttributeConstants.COMPONENT_LM, "A");
				}

			}
		}
		accountResponse.setStatus("Data Modified");
		return accountResponse;
	}

	@Transactional(readOnly = false)
	public String completeTask(Integer taskId, Map<String, Object> map) throws TclCommonException {
		Task task = getTaskById(taskId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
		if (scServiceDetail != null && map.get("terminationDate").toString() != null) {
			Map<String, String> componentData = new HashMap<>();
			componentData.put("terminationDate", map.get("terminationDate").toString());
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), "terminationDate", "LM", "A");
			if (scComponentAttribute != null && scComponentAttribute.getAttributeValue() != null
					&& map.get("terminationDate").toString().equals(scComponentAttribute.getAttributeValue())) {
				map.put("terminationDate", TimeStampUtil.flowableISOTime(map.get("terminationDate").toString())
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")));
			} else {
				map.put("terminationDate",
						TimeStampUtil.flowableISOTime(map.get("terminationDate").toString()).now().plusMinutes(30)
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")));
			}
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentData,
					AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("termination Date is {} ", map.get("terminationDate"));

		}

		flowableBaseService.taskDataEntry(task, map, map);
		processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null, null);

		return "SUCCESS";
	}

	public AccountDetails loadAccountData(Integer taskId) {
		AccountDetails accountDetails = new AccountDetails();
		AccountInputData accountInputData = null;
		Task task = getTaskById(taskId);
		if(task!=null) {
			accountInputData = accountCreationService.loadAccountDataFromO2C(task.getOrderCode(), task.getServiceCode(),
					task.getServiceType(), task.getServiceId().toString());
			if (NetworkConstants.PAPER_EMAIL.equalsIgnoreCase(accountInputData.getBillHandlingCode())) {
				accountInputData.setBillHandlingCode("Paper/Electronic");
			} else if (NetworkConstants.PAPER.equalsIgnoreCase(accountInputData.getBillHandlingCode())) {
				accountInputData.setBillHandlingCode("Paper");
			} else {
				accountInputData.setBillHandlingCode("Electronic");
			}
			accountDetails.setAccountDetails(accountInputData);
			try {
				additionalAttributes(task.getServiceId(),accountInputData);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		}
		
		
		return accountDetails;
	}
	
	public void additionalAttributes(Integer serviceId, AccountInputData accountInputData) throws TclCommonException {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<String> entitySapCodes = new ArrayList<>();
		String defaultSapCode="";
		Map<String, String> atMap = new HashMap<>();
		Map<String, List<String>> secsAccountMap = new HashMap<>();
		if (scServiceDetail != null) {
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), "Y");
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			if ("Y".equals(scServiceDetail.getIsMigratedOrder()) && scOrder != null
					&& "MACD".equals(orderType) && !"ADD_SITE".equals(orderCategory)) {
				String CommDate = gnvOrderEntryTabRepository.findNewOrderCommDate(scServiceDetail.getUuid());
				if (CommDate != null) {
					atMap.put("parentUuidCommissioningDate", TimeStampUtil.optimusDateFormat(CommDate));

				}
			}
		
			String billingAddress = StringUtils.trimToEmpty(accountInputData.getAccountAddr1()) + " "
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddr2())
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddr3()) + " "
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddrCity()) + " "
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddrState()) + " "
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddrCountry()) + " "
					+ StringUtils.trimToEmpty(accountInputData.getAccountAddrZipCode());

			atMap.put("billingAddress", billingAddress);
			
			Map<String, String> countryMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
					Arrays.asList("ContractingAddressCountry","spLeCountry"),scOrder);
			Map<String, String> intlMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("isBillingInternational"), serviceId, "LM","A");
			if(intlMap!=null && intlMap.get("isBillingInternational")!=null && "Y".equals(intlMap.get("isBillingInternational"))) {
				LOGGER.info("Account Data for Intl Customer {}",intlMap.get("isBillingInternational"));
					String secsCode = loadCustomerDetails.getSecsCode(scServiceDetail.getScOrder().getErfCustLeId());
					String entity = gnvOrderEntryTabRepository.getEntity(scOrder.getErfCustSpLeName(),accountInputData.getAccountingCurrency());
					entitySapCodes = gnvOrderEntryTabRepository.getIntlCustomerRef(secsCode, entity != null ? entity : "TAM");
					if (entitySapCodes != null) {
						defaultSapCode = entitySapCodes.get(0)!=null?entitySapCodes.get(0):"No Crn Available";
						entitySapCodes.forEach(secs -> {
							List<String> accountList = gnvOrderEntryTabRepository.getIntlAccountList(secs);
							accountList.add(0, "OPTACC_".concat(scServiceDetail.getId().toString()).concat("_Non_CPE"));
							if (accountList != null) {
								secsAccountMap.put(secs, accountList);
							}
						});
						List<String> cpsNameList = gnvOrderEntryTabRepository.getCpsListNew(getCountyNameForCps(countryMap.getOrDefault("spLeCountry","")));
						atMap.put("intlAccountList", Utils.convertObjectToJson(secsAccountMap));
						atMap.put("cpsName", Utils.convertObjectToJson(cpsNameList));
						atMap.put("secsId", secsCode);
					} 
			}
			else {
				entitySapCodes = loadCustomerDetails.getAllSecsCode(scServiceDetail.getScOrder().getErfCustLeId());
				defaultSapCode =accountInputData.getCustomerRef();
			}
			atMap.put("sapCode", defaultSapCode);
			Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(Arrays.asList("sapCode"),
					serviceId, "LM", "A");
			if (attrMap != null && attrMap.get("sapCode") != null) {
				LOGGER.info("Updated Sap code is {}",attrMap.get("sapCode"));
				atMap.put("sapCode", attrMap.get("sapCode"));
			} 
			
			if (scServiceDetail != null && "RENEWALS".equals(orderType)) {
				ScOrderAttribute attribute = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder("secsCodes",
						scOrder);
				if (attribute != null) {
					attribute.setAttributeValue(Utils.convertObjectToJson(entitySapCodes));
					attribute.setAttributeAltValueLabel("secsCodes");
				} else {
					attribute = new ScOrderAttribute();
					attribute.setAttributeName("secsCodes");
					attribute.setAttributeValue(Utils.convertObjectToJson(entitySapCodes));
					attribute.setAttributeAltValueLabel("secsCodes");
					attribute.setIsActive("Y");
					attribute.setScOrder(scOrder);
				}
				scOrderAttributeRepository.save(attribute);
			}
			atMap.put("secsCodes", Utils.convertObjectToJson(entitySapCodes));
			ScServiceAttribute serviceType = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(),"Service type");
			if (serviceType != null && serviceType.getAttributeValue() != null
					&& "Usage Based".equals(serviceType.getAttributeValue())) {
				getEventSourceforBurstable(scServiceDetail);
			}
			componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("Site Wise Billing is {} for Service type {}",scOrder.getSiteLevelBilling(),scServiceDetail.getErfPrdCatalogProductName());
			if ((scOrder != null && "Y".equals(scOrder.getSiteLevelBilling()))
					&& BillingConstants.NPL.equals(scServiceDetail.getErfPrdCatalogProductName())) {
				LOGGER.info("Site Wise Billing for {}",scServiceDetail.getErfPrdCatalogProductName());
				SiteBBillingAddress siteBBillingAddress = accountInputData.getSiteBBillingAddress();
				
				if (siteBBillingAddress != null) {
					
					String addressLineOne = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr1());
					String addressLineTwo = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr2());
					String addressLineThree = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddr3());
					String addressCity = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrCity());
					String addressState = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrState());
					String addressCountry = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrCountry());
					String addressZipCode = StringUtils.trimToEmpty(siteBBillingAddress.getSiteBAccountAddrZipCode());

					String billingAddressB = addressLineOne + " " + addressLineTwo + addressLineThree + " "
							+ addressCity + " " + addressState + " " + addressCountry + " " + addressZipCode;
					LOGGER.info("Site B Billing Address {}",billingAddressB);
					Map<String, String> attrBMap = new HashMap<>();
					attrBMap.put("billingAddress", billingAddressB);
					componentAndAttributeService.updateAttributes(serviceId, attrBMap, AttributeConstants.COMPONENT_LM,	"B");

				}
			}
		}
	}

	public ChargeLineItemBean saveServiceTermination(Integer taskId, ChargeLineItemBean chargeLineItems) throws TclCommonException {
		Task task = getTaskById(taskId);
		validateInputs(task, chargeLineItems);
		List<LineItemDetailsBean> lineItems = chargeLineItems.getLineItemDetails();
		lineItems.forEach(lineItem-> {
			ScChargeLineitem chargeLineitem = new ScChargeLineitem();
			chargeLineitem.setEtcCharge(lineItem.getEtcCharge());
			//chargeLineitem.setEtcWaiver(lineItem.getEtcWaiver());
			chargeLineitem.setChargeLineitem(lineItem.getLineitem());
			chargeLineitem.setServiceId(task.getServiceId().toString());
			chargeLineitem.setServiceCode(task.getServiceCode());
			chargeLineitem.setServiceType(task.getServiceType());
			chargeLineitem.setServiceTerminationFlag("N");
			chargeLineitem.setTermDate(lineItem.getTerminationDate());
			chargeLineitemRepository.save(chargeLineitem); 
		});
		Map<String, Object> wfMap = new HashMap<>();
	   	wfMap.put("action", "close");
		return (ChargeLineItemBean) flowableBaseService.taskDataEntry(task, chargeLineItems,wfMap);
	}

	public Map<String, Object> taxAndBillingFlag(Map<String, Object> wfMap, Integer serviceId) {
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("isTaxCaptureRequired", "isBillingInternational"), serviceId, "LM","A");
		if (attrMap != null) {
			wfMap.put("isBillingInternational", attrMap.get("isBillingInternational") != null
					&& "Y".equals(attrMap.get("isBillingInternational")) ? true : false);
			wfMap.put("isTaxCaptureRequired", attrMap.get("isTaxCaptureRequired") != null
					&& "Y".equals(attrMap.get("isTaxCaptureRequired")) ? true : false);
		}
		LOGGER.info("taxAndBillingFlag::{}",wfMap);
		return wfMap;
	}


	private String lengthCheck(String value) {
		if (value != null && value.length() > 40)
			return value.substring(0, 40);
		else
			return value;
	}

	@Transactional
	public String saveServiceCancellation(Integer taskId, ChargeLineItemBean chargeLineItems) {
		Task task = getTaskById(taskId);
		validateInputs(task, chargeLineItems);
		List<LineItemDetailsBean> lineItems = chargeLineItems.getLineItemDetails();
		lineItems.forEach(lineItem-> {
			ScChargeLineitem chargeLineitem = new ScChargeLineitem();
			chargeLineitem.setNrc(lineItem.getNrc());
			chargeLineitem.setChargeLineitem(lineItem.getLineitem());
			chargeLineitem.setServiceId(task.getServiceId().toString());
			chargeLineitem.setServiceCode(task.getServiceCode());
			chargeLineitem.setServiceType(task.getServiceType());
			chargeLineitem.setHsnCode(NetworkConstants.IAS_HSN_CODE);
			chargeLineitem.setTermDate(lineItem.getCancellationDate());
			chargeLineitem.setComponentDesc(lineItem.getDescription());
			chargeLineitemRepository.save(chargeLineitem); 
		});
		chargeLineitemRepository.flush();
		Map<String, String> atMap = new HashMap<>();
		atMap.put("isAccountRequired", "Y");
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");
		return "Success";
	}
	
	@Transactional
	public ChargeLineItemBean closeCancellationCommercialVetting(Integer taskId, ChargeLineItemBean chargeLineItems) throws TclCommonException{
		Task task = getTaskById(taskId);
		Map<String, Object> wfMap = new HashMap<>();
	   	wfMap.put("action", "close");
		return (ChargeLineItemBean) flowableBaseService.taskDataEntry(task, chargeLineItems,wfMap);
	}
	
	private String getCountyNameForCps(String countryName) {
		switch (countryName) {
		case "United Kingdom":
			countryName = "UK";
			break;
		default:
			countryName = StringUtils.capitalize(countryName.toLowerCase());
			break;
		}
		return countryName;
	}
	public RenewalCommercialChargeLineItemBean billingLineItemsRenewal(Integer taskId, RenewalCommercialVettingBean renewalCommercialVettingBean) throws TclCommonException {

		LOGGER.info("billingLineItemsRenewal for service code: {}", renewalCommercialVettingBean.getServiceCode());
		Task task = getTaskById(taskId);
		Map<String, String> atMap = new HashMap<>();

		validateInputs(task, renewalCommercialVettingBean);
		Map<String, Object> wfMap = new HashMap<>();
		List<RenewalCommercialVettingDetails> renewableChargeLineItemBeans = renewalCommercialVettingBean.getRenewalCommercialVettingDetails();
		renewableChargeLineItemBeans.forEach(item ->{
			LOGGER.info("billingLineItemsRenewal for service id: {}", item.getServiceId());
			List<LineItemDetailsBean> lineItems = item.getItemDetailsBeans();
			chargeLineitemRepository.deleteLineItems(item.getServiceId().toString());
			chargeLineitemRepository.flush();
			lineItems.forEach(lineItem -> {
				ScChargeLineitem chargeLineitem = new ScChargeLineitem();
				chargeLineitem.setNrc(lineItem.getNrc());
				chargeLineitem.setArc(lineItem.getArc());
				chargeLineitem.setMrc(lineItem.getMrc());
				chargeLineitem.setChargeLineitem(lineItem.getLineitem());
				chargeLineitem.setAccountNumber(lineItem.getAccountNumber());
				chargeLineitem.setServiceType(lineItem.getServiceType());
				chargeLineitem.setServiceId(item.getServiceId().toString());
				chargeLineitem.setServiceCode(item.getServiceCode());
				chargeLineitem.setQuantity(lineItem.getQuantity());
				chargeLineitem.setUnitOfMeasurement(lineItem.getUnitOfMeasurement());
				chargeLineitem.setBillingMethod(lineItem.getBillingMethod());
				chargeLineitem.setIsProrated(lineItem.getIsProrated());
				//chargeLineitem.setComponent(getComponent(chargeLineitem.getChargeLineitem()));
				chargeLineitem.setComponent(lineItem.getComponent());
				if (NetworkConstants.CPE.equals(lineItem.getServiceType())) {
					chargeLineitem.setCpeModel(lineItem.getCpeModel());
				} else {
					chargeLineitem.setSkuId(lineItem.getCpeModel());
				}
				chargeLineitem.setHsnCode(lineItem.getHsnCode());
				chargeLineitem.setSiteType(lineItem.getSiteType());
				chargeLineitem.setCommissioningFlag("N");
				chargeLineitem.setUsageArc(lineItem.getUsageArc());
				chargeLineitem.setBillingType(lineItem.getBillingType());
				chargeLineitem.setComponentDesc(lengthCheck(lineItem.getDescription()));
				chargeLineitemRepository.saveAndFlush(chargeLineitem);
			});

			Double nrc = lineItems.stream()
					.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
			Double arc = lineItems.stream()
					.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));

			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(item.getServiceId());
			if (scServiceDetail.get() != null && nrc != null && arc != null) {
				scServiceDetail.get().setMrc(arc / 12);
				scServiceDetail.get().setArc(arc);
				scServiceDetail.get().setNrc(nrc);
				scServiceDetailRepository.save(scServiceDetail.get());
			}
			boolean isAccountCreationRequired = item.getItemDetailsBeans().stream()
					.anyMatch(chargeLineItem -> chargeLineItem.getAccountNumber().contains("OPTACC_"));
			boolean isBurstableIncluded = item.getItemDetailsBeans().stream()
					.anyMatch(chargeLineItem -> chargeLineItem.getLineitem().contains("Burstable"));

			atMap.put("isAccountRequired", isAccountCreationRequired ? "Y" : "N");
			atMap.put("isBurstable", isBurstableIncluded ? "Y" : "N");

			componentAndAttributeService.updateAttributes(item.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");

			wfMap.put("isAccountRequired", isAccountCreationRequired);
			taxAndBillingFlag(wfMap,item.getServiceId());
		});

		processTaskLogDetails(task, "CLOSED", renewalCommercialVettingBean.getDelayReason(), null,
				renewalCommercialVettingBean);
		wfMap.put("action", "close");
		return (RenewalCommercialChargeLineItemBean) flowableBaseService.taskDataEntry(task, renewalCommercialVettingBean,wfMap);
	}

	public BaseRequest renewalCustomManualTask(Integer taskId, BaseRequest baseRequest) throws TclCommonException{
		Task task = getTaskByIdAndWfTaskId(baseRequest.getTaskId(), baseRequest.getWfTaskId());
		validateInputs(task, baseRequest);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS,baseRequest.getDelayReason(),null, baseRequest);
		return (BaseRequest) flowableBaseService.taskDataEntry(task, baseRequest);
	}
	
	public String getEventSourceforBurstable(ScServiceDetail scServiceDetail) {
		LOGGER.info("EventSourceforBurstable for service id {} ",scServiceDetail.getUuid());
		String eventSource="Not Available";
		if (scServiceDetail != null && scServiceDetail.getUuid() != null) {
			eventSource = gnvOrderEntryTabRepository.getEventSource(scServiceDetail.getUuid());
			if (StringUtils.isEmpty(eventSource)) {
				String custName = scServiceDetail.getScOrder().getErfCustLeName().replaceAll("[^a-zA-Z0-9]", "")
						.substring(0, 6);
				eventSource = custName.concat("-").concat(scServiceDetail.getUuid());
			}
		}
		LOGGER.info("EventSourceforBurstable is {}", eventSource);
		return eventSource;
	}

}