package com.tcl.dias.servicehandover.ipc.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommericalComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.ChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.IpcBillingChargeLineItemService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.util.AccountDetails;
import com.tcl.dias.servicehandover.util.AccountResponse;
import com.tcl.dias.servicehandover.util.CatalystPayPerUseDetailRequest;
import com.tcl.dias.servicehandover.util.IPCChargeLineItemsUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * BillingAccountAndLineItemService will be used to load account number and
 * store charge line items
 * 
 * @author yogesh
 */
@Service
public class IpcBillingAccountAndLineItemService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingAccountAndLineItemService.class);

	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;

	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;

	@Autowired
	BillingHandoverService billingHandoverService;

	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;

	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;

	@Autowired
	ScProductDetailRepository scProductDetailRepository;

	@Autowired
	ScProductDetailAttributeRepository scProductDetailAttributeRepository;

	@Autowired
	ScServiceCommericalComponentRepository scServiceCommericalComponentRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	FlowableBaseService flowableBaseService;
	
	@Autowired
	IPCChargeLineItemsUtil ipcChargeLineItemsUtil;

	@Autowired
	RestClientService restClientService;
	
	String quantity, mrc, nrc, arc;

	@Value("${catalyst.payPerUse.serviceCommissionURL}")
	private String payPerUseInfoCataystUrl;
	
	@Autowired
	IpcBillingChargeLineItemService ipcBillingChargeLineItemService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository; 
	
	public ChargeLineItemBean billingLineItems(Integer taskId, ChargeLineItemBean chargeLineItems)
			throws TclCommonException {
		Task task = getTaskById(taskId);
		if(!chargeLineItems.getLineItemDetails().isEmpty()) {
			LOGGER.info("Choosen Account Number: {}", chargeLineItems.getLineItemDetails().get(0).getAccountNumber());
			chargeLineItems.setAccountNumber(chargeLineItems.getLineItemDetails().get(0).getAccountNumber());
		}
		chargeLineItems.setLineItemDetails(ipcBillingChargeLineItemService.loadLineItems( false, taskId));
		validateInputs(task, chargeLineItems);
		List<LineItemDetailsBean> lineItems = chargeLineItems.getLineItemDetails();
		formatAndSaveIpcLineItems(task, lineItems, chargeLineItems.getAccountNumber());
		// processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,muxDetailBean.getDelayReason(),null);
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");
		if (IpcConstants.MACD.equals(task.getOrderType())) {
			LOGGER.info("Ipc optimusMacd is {} ", true);
			wfMap.put("optimusMacd", true);
		}
		updateProcessVarMapAndSrvAttributes(chargeLineItems, wfMap, task);
		flowableBaseService.taskDataEntry(task, chargeLineItems, wfMap);
		processTaskLogDetails(task,TaskLogConstants.CLOSED,"",null, constructBaseRequest(wfMap));
		return new ChargeLineItemBean() ;
	}

	@Transactional
	private void updateProcessVarMapAndSrvAttributes(ChargeLineItemBean chargeLineItems, Map<String, Object> wfMap, Task task) {
		ScServiceAttribute serviceAttribute = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(task.getServiceId(), CommonConstants.IS_IPC_BILLING_INTL);
		if(serviceAttribute != null && NetworkConstants.Y.equals(serviceAttribute.getAttributeValue())) {
			if(chargeLineItems.getAccountNumber().contains("OPTACC_")) {
				wfMap.put("isIpcIntlAccountCreationRequired",true);
			} else {
				wfMap.put("isIpcIntlAccountCreationRequired",false);
				Map<String, String> attrMap = new HashMap<>();
				attrMap.put(IpcConstants.IPC_BILLING_ACCOUNT_NUMBER, chargeLineItems.getAccountNumber());
				billingHandoverService.updateServiceAttributes(task.getServiceId(), attrMap);
			}
		}
	}

	private String formatLineItemName(String lineItemName, String pricingModel) {
		if(lineItemName.contains(IpcConstants.VM) && !IpcConstants.PRICING_MODEL_RESERVED.equals(pricingModel)) {
			return IpcConstants.VM_USAGE_CHARGES;
		}
		return lineItemName;
	}
	
	@Transactional
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
			ipcAdditionalAttributes(task.getServiceId(), accountInputData);
		}
		return accountDetails;
	}
	
	private void ipcAdditionalAttributes(Integer serviceId, AccountInputData accountInputData) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<String> entitySapCodes = new ArrayList<>();
		String defaultSapCode="";
		Map<String, String> serAttr = new HashMap<>();
		Map<String, List<String>> secsAccountMap = new HashMap<>();
		if (scServiceDetail != null) {
			try {
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(serviceId, Constants.SAP_CODE);
				if( scServiceAttribute != null && scServiceAttribute.getAttributeValue() != null) {
					accountInputData.setCustomerRef(scServiceAttribute.getAttributeValue());
				}
				Map<String, String> scOrderAttrMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
						Arrays.asList(CommonConstants.IS_IPC_BILLING_INTL),scServiceDetail.getScOrder());
				if(scOrderAttrMap!=null && scOrderAttrMap.get(CommonConstants.IS_IPC_BILLING_INTL)!=null && "Y".equals(scOrderAttrMap.get(CommonConstants.IS_IPC_BILLING_INTL))) {
					LOGGER.info("Account Data for IPC Intl Customer {}",scOrderAttrMap.get(CommonConstants.IS_IPC_BILLING_INTL));
					String tempAccountNumber = "OPTACC_".concat(scServiceDetail.getScOrderUuid());
					String secsCode = loadCustomerDetails.getSecsCode(scServiceDetail.getScOrder().getErfCustLeId());
					String entity = gnvOrderEntryTabRepository.getEntityForIpcIntl(scServiceDetail.getScOrder().getErfCustSpLeName().toUpperCase(),accountInputData.getAccountingCurrency());
					entitySapCodes = gnvOrderEntryTabRepository.getIntlCustomerRef(secsCode, entity != null ? entity : "");
					if (entitySapCodes != null) {
						if (scServiceAttribute != null) {
							defaultSapCode = scServiceAttribute.getAttributeValue();
						} else {
							defaultSapCode = !entitySapCodes.isEmpty()?entitySapCodes.get(0):"No Crn Available";
							accountInputData.setCustomerRef(defaultSapCode);
						}
						
						entitySapCodes.forEach(sapCode -> {
							List<String> accountList = gnvOrderEntryTabRepository.getIntlAccountList(sapCode);
							accountList.add(0, tempAccountNumber);
							if (accountList != null) {
								secsAccountMap.put(sapCode, accountList);
							}
						});
					}
					List<String> cpsNameList = gnvOrderEntryTabRepository.getCpsListForIpc(getCountyNameForCps(scServiceDetail.getSourceCountry()));
					LOGGER.info("cpsNameList: {}",cpsNameList);
					serAttr.put(IpcConstants.IPC_BILLING_ENTITY, "EN"+entity);
					serAttr.put("cpsName", Utils.convertObjectToJson(cpsNameList));
					serAttr.put("secsId", secsCode);
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "intlAccountList",
							String.join(",", (new ArrayList<>(secsAccountMap.get(defaultSapCode)))));
					//Load FinalCps Value for MACD Order
					if ( scServiceDetail.getOrderType().equals(IpcConstants.MACD)) {
						List<ScServiceDetail> scServiceDetailL = scServiceDetailRepository.findByUuidOrderByIdDesc(scServiceDetail.getUuid());
						if( scServiceDetailL.size() > 1 && scServiceDetailL.get(1) != null) {
							ScServiceAttribute newOdrFinalCps = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetailL.get(1).getId(), CommonConstants.FINAL_CPS);
							serAttr.put( CommonConstants.FINAL_CPS, newOdrFinalCps.getAttributeValue());
						}
					}
				} else {
					serAttr.put(IpcConstants.IPC_BILLING_ENTITY, IpcConstants.BILLING_ENTITY);
					entitySapCodes = loadCustomerDetails.getAllSecsCode(scServiceDetail.getScOrder().getErfCustLeId());
					defaultSapCode = accountInputData.getCustomerRef();
				}
				serAttr.put(IpcConstants.BILLING_ADDRESS_ATTRIBUTE, frameBillingAddress(accountInputData));
				serAttr.put(Constants.SAP_CODE, defaultSapCode);
				componentAndAttributeService.loadServiceAttributesIfNotPresent(scServiceDetail,serAttr);
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "secsCodes",
						Utils.convertObjectToJson(entitySapCodes));
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public AccountResponse saveAccountData(AccountInputData accountInputData,Integer taskId) {
		Task task = getTaskById(taskId);
		AccountResponse accountResponse = new AccountResponse(); 
		if (task != null) {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
			if (scServiceDetail != null) {
				ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), "Y");
				ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
				if (scContractInfo != null) {
					scContractInfo.setBillingAddressLine1(StringUtils.trimToEmpty(accountInputData.getAccountAddr1()));
					scContractInfo.setBillingAddressLine2(StringUtils.trimToEmpty(accountInputData.getAccountAddr2()));
					scContractInfo.setBillingAddressLine3(StringUtils.trimToEmpty(accountInputData.getAccountAddr3()));
					scContractInfo.setBillingCity(StringUtils.trimToEmpty(accountInputData.getAccountAddrCity()));
					scContractInfo.setBillingState(StringUtils.trimToEmpty(accountInputData.getAccountAddrState()));
					scContractInfo.setBillingCountry(StringUtils.trimToEmpty(accountInputData.getAccountAddrCountry()));
					scContractInfo.setBillingPincode(StringUtils.trimToEmpty(accountInputData.getAccountAddrZipCode()));
					scContractInfo.setBillingFrequency(StringUtils.trimToEmpty(accountInputData.getBillingFrequency()));
					scContractInfo.setPaymentTerm(StringUtils.trimToEmpty(accountInputData.getCreditClass().split(" ")[0].concat(" Days")));
					scContractInfo.setBillingMethod(StringUtils.trimToEmpty(accountInputData.getBillingMethod()));
					scContractInfoRepository.save(scContractInfo);
					
					String billingAddress = frameBillingAddress(accountInputData);
					ScServiceAttribute scServiceAttrBillingAddress = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), IpcConstants.BILLING_ADDRESS_ATTRIBUTE);
					scServiceAttrBillingAddress.setAttributeValue(billingAddress);
					scServiceAttrBillingAddress.setAttributeAltValueLabel(billingAddress);
					scServiceAttrBillingAddress.setUpdatedDate(new Timestamp(new Date().getTime()));
					scServiceAttributeRepository.save(scServiceAttrBillingAddress);
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
				if (accountInputData.getCustomerRef() != null) {
					LOGGER.info("Sap Code updated as {}",accountInputData.getCustomerRef());
					ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), Constants.SAP_CODE);
					scServiceAttribute.setAttributeValue(accountInputData.getCustomerRef());
					scServiceAttribute.setAttributeAltValueLabel(accountInputData.getCustomerRef());
					scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
					scServiceAttributeRepository.save(scServiceAttribute);
				}
			}
		}
		accountResponse.setStatus("Data Modified");
		return accountResponse;
	}
	
	
	private String frameBillingAddress(AccountInputData accountInputData) {
		return StringUtils.trimToEmpty(accountInputData.getAccountAddr1()) + IpcConstants.SINGLE_SPACE
				+ StringUtils.trimToEmpty(accountInputData.getAccountAddr2()) + IpcConstants.SINGLE_SPACE
				+ StringUtils.trimToEmpty(accountInputData.getAccountAddrCity()) + IpcConstants.SINGLE_SPACE
				+ StringUtils.trimToEmpty(accountInputData.getAccountAddrState()) + IpcConstants.SINGLE_SPACE
				+ StringUtils.trimToEmpty(accountInputData.getAccountAddrCountry()) + IpcConstants.SINGLE_SPACE
				+ StringUtils.trimToEmpty(accountInputData.getAccountAddrZipCode());
	}
	
	@Transactional
	public void triggerPayPerUseCommissionedInfoToCatalyst(ScServiceDetail scServiceDetail) {
		try {
			LOGGER.info("Entering triggerPayPerUseCommissionedInfoToCatalyst method");
			LOGGER.info("scServiceDetail: {}",scServiceDetail);
			LOGGER.info("Commissioned Date: {}",scServiceDetail.getServiceCommissionedDate());
			ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
			CatalystPayPerUseDetailRequest catalystPayPerUseDetailRequest = new CatalystPayPerUseDetailRequest();
			catalystPayPerUseDetailRequest.setSfdcId(scServiceDetail.getScOrder().getTpsSfdcOptyId());
			List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
					.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
							Arrays.asList("billFreePeriod"));
			Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
			String billFreePeriod = attributeMap.getOrDefault("billFreePeriod", "0");
			String commissionedDate = TimeStampUtil.formatWithTimeStampForCommPlusDays(
					scServiceDetail.getServiceCommissionedDate().toString(),
					StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0);
			LOGGER.info("Commissioned Date: {}", commissionedDate);
			catalystPayPerUseDetailRequest.setCommisionedDate(commissionedDate);
			catalystPayPerUseDetailRequest.setCustomerName(scContractInfo.getErfCustLeName());
			catalystPayPerUseDetailRequest.setDcLocationCode(scServiceDetail.getPopSiteCode());
			LOGGER.info("Inside triggerPayPerUseCommissionedInfoToCatalyst method: Setting up headers");
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			//headers.put("Authorization", "Bearer " + Utils.getToken());
			RestResponse response = null;
			String request = Utils.convertObjectToJson(catalystPayPerUseDetailRequest);
			LOGGER.info("Catalyst Pay Per Use VM Info API with URl: {}, Request: {}, Headers: {}", payPerUseInfoCataystUrl, request, headers);
			response = restClientService.put(payPerUseInfoCataystUrl, request, headers);
			if (response != null && response.getStatus().equals(Status.SUCCESS)
					&& StringUtils.isNotEmpty(response.getData())) {
				LOGGER.info("Got response from Catalyst Pay Per Use VM Info API {}" , response);
			} else {
				LOGGER.info("Got Failure response from Catalyst Pay Per Use VM Info API {}" , response);
			}
			LOGGER.info("triggerPayPerUseCommissionedInfoToCatalyst Method Completed");
		} catch (Exception e) {
			LOGGER.error("Error while hitting Catalyst Pay Per Use VM Info API {}", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String saveBillingLineItems(Integer taskId, String accountNumber)
			throws TclCommonException {
		Task task = getTaskById(taskId);
		List<LineItemDetailsBean> lineItems = ipcBillingChargeLineItemService.loadLineItems( false, taskId);
		formatAndSaveIpcLineItems(task, lineItems, accountNumber);
		return "SUCCESS";
	}
	
	@Transactional
	public String formatAndSaveIpcLineItems( Task task, List<LineItemDetailsBean> lineItems, String inptAccountNumber) {
		if (!ipcChargeLineitemRepository.findByServiceId(task.getServiceId().toString()).isEmpty()) {
			return "Line Items Entries Already Created";
		}
		ScServiceAttribute isIpcIntl = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(task.getServiceId(), CommonConstants.IS_IPC_BILLING_INTL);
		Integer maxAvailableVersion = ipcChargeLineitemRepository
				.findLatestVersionByServiceCodeAndActionType(task.getServiceCode(), NetworkConstants.CREATE);
		List<IpcChargeLineitem> ipcChargeLineitemsFromDB = ipcChargeLineitemRepository
				.findByServiceCodeAndVersionAndActionType(task.getServiceCode(), maxAvailableVersion,
						NetworkConstants.CREATE);
		//set Zero value for prev nrc
		ipcChargeLineitemsFromDB.stream().filter(x -> !IpcConstants.STRING_ZERO.equals(x.getNrc())).forEach(ipcLi -> {
			ipcLi.setOldNrc(ipcLi.getNrc());
			ipcLi.setNrc(IpcConstants.STRING_ZERO);
			ipcChargeLineitemRepository.save(ipcLi);
		});
		
		Set<String> parentCloudCodeSet = new HashSet<>();
		StringBuffer accountNumber = new StringBuffer((inptAccountNumber != null) ? inptAccountNumber : "");
		quantity = null;
		mrc = null;
		nrc = null;
		arc = null;

		// Iterating input lineitems(Since preference is for the input lineitems) and
		// saving IPCChargeLineItem in DB with incremented version
		lineItems.forEach(lineItem -> {
			if( lineItem.getComponent().equals(IpcConstants.COMPONENT_IPC_ADDON)) {
				if( lineItem.getLineitem().equals(IpcConstants.DATABASE_LICENSE_CHARGES)
						|| lineItem.getLineitem().equals(IpcConstants.DR_LICENSE_CHARGES)) {
					parentCloudCodeSet.add(lineItem.getLineitem().concat(IpcConstants.UNDERSCORE).concat(lineItem.getDescription())
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getMrc())/Integer.valueOf(lineItem.getQuantity())))
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getNrc())/Integer.valueOf(lineItem.getQuantity())))
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getArc())/Integer.valueOf(lineItem.getQuantity()))));
				} else {
					parentCloudCodeSet.add(lineItem.getLineitem());
				}
			} else {
				parentCloudCodeSet.add(lineItem.getParentCloudCode());
			}
			if(IpcConstants.MACD.equals(task.getOrderType()) && IpcConstants.DELETE_VM.equals(task.getScServiceDetail().getScOrder().getOrderCategory()) && !IpcConstants.EARLY_TERMINATION_CHARGES.equals(lineItem.getLineitem())) {
				return;
			}
			accountNumber.append(accountNumber.length() == 0 ? lineItem.getAccountNumber() : "");
			String emptyScenarioType = "";
			quantity = lineItem.getQuantity();
			mrc = lineItem.getMrc();
			nrc = lineItem.getNrc();
			arc = lineItem.getArc();
			
			if(Arrays.asList(IpcConstants.ADDITIONAL_IP_CHARGES, IpcConstants.VPN_CLIENT_SITE, IpcConstants.VPN_SITE_SITE, IpcConstants.DATABASE_LICENSE_CHARGES, IpcConstants.DR_LICENSE_CHARGES).contains(lineItem.getLineitem())) {
				ipcChargeLineitemsFromDB.stream().filter(ipcLineItem -> ipcLineItem.getChargeLineitem().equals(lineItem.getLineitem()))
					.forEach(ipcLineItem -> {
						String prevQuantity = ipcLineItem.getQuantity();
						if(ipcLineItem.getDescription().contains(IpcConstants.QUANTITY)
								|| ipcLineItem.getDescription().contains(IpcConstants.SITE_TO_SITE)
								|| ipcLineItem.getDescription().contains(IpcConstants.CLIENT_TO_SITE)) {
							prevQuantity = ipcLineItem.getDescription().split(": ")[1];
						}
						if(((ipcLineItem.getChargeLineitem().equals(IpcConstants.DATABASE_LICENSE_CHARGES) || ipcLineItem.getChargeLineitem().equals(IpcConstants.DR_LICENSE_CHARGES))
								&& ipcLineItem.getDescription().equals(lineItem.getDescription()) 
								&& String.valueOf(Double.parseDouble(ipcLineItem.getMrc())/Integer.valueOf(ipcLineItem.getQuantity())).equals(String.valueOf(Double.parseDouble(lineItem.getMrc())/Integer.valueOf(lineItem.getQuantity()))) 
								&& String.valueOf(Double.parseDouble(ipcLineItem.getNrc())/Integer.valueOf(ipcLineItem.getQuantity())).equals(String.valueOf(Double.parseDouble(lineItem.getNrc())/Integer.valueOf(lineItem.getQuantity()))) 
								&& String.valueOf(Double.parseDouble(ipcLineItem.getArc())/Integer.valueOf(ipcLineItem.getQuantity())).equals(String.valueOf(Double.parseDouble(lineItem.getArc())/Integer.valueOf(lineItem.getQuantity())))) ||
								!(ipcLineItem.getChargeLineitem().equals(IpcConstants.DATABASE_LICENSE_CHARGES) || ipcLineItem.getChargeLineitem().equals(IpcConstants.DR_LICENSE_CHARGES))) {
							quantity = String.valueOf(Integer.parseInt(lineItem.getQuantity()) + Integer.parseInt(prevQuantity));
							mrc = String.valueOf(ipcChargeLineItemsUtil.addDoubleValueAndRoundOff(lineItem.getMrc(), ipcLineItem.getMrc()));
							nrc = String.valueOf(ipcChargeLineItemsUtil.addDoubleValueAndRoundOff(lineItem.getNrc(), ipcLineItem.getNrc()));
							arc = String.valueOf(ipcChargeLineItemsUtil.addDoubleValueAndRoundOff(lineItem.getArc(), ipcLineItem.getArc()));
						}
					});
			}

			if (lineItem.getLineitem().equalsIgnoreCase(IpcConstants.DATA_TRANSFER_COMM)) {
				IpcChargeLineitem ipcChargeLineitem = new IpcChargeLineitem(accountNumber.toString(),
						IpcConstants.STRING_ZERO, IpcConstants.DATA_TRANSFER_USAGE, lineItem.getDescription(), IpcConstants.STRING_ZERO,
						IpcConstants.STRING_ZERO, lineItem.getPpuRate(), null, null,
						task.getServiceId().toString(), lineItem.getServiceType(),
						lineItem.getBillingMethod(), lineItem.getUnitOfMeasurement(), lineItem.getQuantity(),
						lineItem.getIsProrated(), lineItem.getComponent(), lineItem.getCpeModel(),
						getHsnCode(lineItem.getHsnCode(), isIpcIntl), task.getServiceCode(), lineItem.getMigParentServiceCode(), lineItem.getCloudCode(),
						lineItem.getParentCloudCode(), lineItem.getAdditionalParam(),
						maxAvailableVersion + IpcConstants.NUMBER_ONE, NetworkConstants.CREATE, NetworkConstants.NEW,
						IpcConstants.N, IpcConstants.N, lineItem.getParentCloudCode() != null ? Constants.NCO_USAGE : emptyScenarioType);
				ipcChargeLineitemRepository.save(ipcChargeLineitem);
			}
			IpcChargeLineitem ipcChargeLineitem = new IpcChargeLineitem(accountNumber.toString(), arc,
					formatLineItemName(lineItem.getLineitem(), lineItem.getPricingModel()), lineItem.getDescription(), mrc, nrc, 
					lineItem.getPpuRate(), lineItem.getPricingModel(), lineItem.getProductDescription(), task.getServiceId().toString(),
					lineItem.getServiceType(), lineItem.getBillingMethod(), lineItem.getUnitOfMeasurement(),
					quantity, lineItem.getIsProrated(), lineItem.getComponent(), lineItem.getCpeModel(),
					getHsnCode(lineItem.getHsnCode(), isIpcIntl), task.getServiceCode(), lineItem.getMigParentServiceCode(), lineItem.getCloudCode(),
					lineItem.getParentCloudCode(), lineItem.getAdditionalParam(),
					maxAvailableVersion + IpcConstants.NUMBER_ONE, NetworkConstants.CREATE, NetworkConstants.NEW,
					IpcConstants.N, IpcConstants.N, emptyScenarioType);
			ipcChargeLineitemRepository.save(ipcChargeLineitem);
		});

		ipcChargeLineitemsFromDB.forEach(lineItem -> {
			// checking whether the set already contains the lineitem using cloud Code (to
			// avoid creating duplicates and Modified products)
			String code = null;
			if( lineItem.getComponent().equals(IpcConstants.COMPONENT_IPC_ADDON)) {
				if( lineItem.getChargeLineitem().equals(IpcConstants.DATABASE_LICENSE_CHARGES)
						|| lineItem.getChargeLineitem().equals(IpcConstants.DR_LICENSE_CHARGES)) {
					code = lineItem.getChargeLineitem().concat(IpcConstants.UNDERSCORE).concat(lineItem.getDescription())
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getMrc())/Integer.valueOf(lineItem.getQuantity())))
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getNrc())/Integer.valueOf(lineItem.getQuantity())))
							.concat(IpcConstants.UNDERSCORE).concat(String.valueOf(Double.parseDouble(lineItem.getArc())/Integer.valueOf(lineItem.getQuantity())));
				} else {
					code = lineItem.getChargeLineitem();
				}
			} else {
				code = lineItem.getCloudCode();
			}
			
			if ((!parentCloudCodeSet.contains(code) && !IpcConstants.EARLY_TERMINATION_CHARGES.equals(lineItem.getChargeLineitem()) && 0 != Integer.parseInt(lineItem.getQuantity()))
					|| IpcConstants.HYBRID_CONNECTION_CHARGES.equals(lineItem.getChargeLineitem())) {
				String scenarioType = (lineItem.getChargeLineitem().equals(IpcConstants.VM_USAGE_CHARGES) 
											|| lineItem.getChargeLineitem().equals(IpcConstants.DATA_TRANSFER_USAGE)) ? Constants.NCO_USAGE : "";
				IpcChargeLineitem ipcChargeLineitem = new IpcChargeLineitem(accountNumber.toString(), lineItem.getArc(),
						lineItem.getChargeLineitem(), getDescription(lineItem), lineItem.getMrc(), IpcConstants.STRING_ZERO,
						lineItem.getPpuRate(), lineItem.getPricingModel(), lineItem.getProductDescription(),
						task.getServiceId().toString(), lineItem.getServiceType(), lineItem.getBillingMethod(),
						lineItem.getUnitOfMeasurement(), getQuantity(lineItem), lineItem.getIsProrated(),
						lineItem.getComponent(), lineItem.getCpeModel(), IpcConstants.HSN_CODE,
						lineItem.getServiceCode(), null, lineItem.getCloudCode(), lineItem.getParentCloudCode(),
						lineItem.getAdditionalParam(), maxAvailableVersion + IpcConstants.NUMBER_ONE,
						NetworkConstants.CREATE, NetworkConstants.NEW, IpcConstants.N, IpcConstants.N, scenarioType);
				ipcChargeLineitem.setSourceProductSequence(lineItem.getSourceProductSequence());
				ipcChargeLineitemRepository.save(ipcChargeLineitem);
			}
			lineItem.setActionType(NetworkConstants.TERMINATE);
			lineItem.setStatus(NetworkConstants.NEW);
			if(IpcConstants.EARLY_TERMINATION_CHARGES.equals(lineItem.getChargeLineitem())) {
				lineItem.setStatus(NetworkConstants.SUCCESS);
				lineItem.setTerminatedFlag(IpcConstants.Y);
			}
			ipcChargeLineitemRepository.save(lineItem);
		});
		return IpcConstants.SUCCESS;
	}
	
	private String getQuantity(IpcChargeLineitem lineItem) {
		if( Arrays.asList(IpcConstants.ADDITIONAL_IP_CHARGES, IpcConstants.VPN_CLIENT_SITE, IpcConstants.VPN_SITE_SITE).contains(lineItem.getChargeLineitem())) {
			if(lineItem.getDescription().contains(IpcConstants.QUANTITY)
					|| lineItem.getDescription().contains(IpcConstants.SITE_TO_SITE)
					|| lineItem.getDescription().contains(IpcConstants.CLIENT_TO_SITE)) {
				return lineItem.getDescription().split(": ")[1];
			}
		}
		return lineItem.getQuantity();
	}

	private String getDescription(IpcChargeLineitem lineItem) {
		if (IpcConstants.ADDITIONAL_IP_CHARGES.equals(lineItem.getChargeLineitem())) {
			return IpcConstants.ADDITIONAL_IP_DESC;
		} else if (IpcConstants.VPN_CLIENT_SITE.equals(lineItem.getChargeLineitem())) {
			return IpcConstants.IPC_DESC_CLIENT_TO_SITE;
		} else if (IpcConstants.VPN_SITE_SITE.equals(lineItem.getChargeLineitem())) {
			return IpcConstants.IPC_DESC_SITE_TO_SITE;
		} else {
			return lineItem.getDescription();
		}
	}

	private String getCountyNameForCps(String countryName) {
		LOGGER.info("getCountyNameForCps : {}", countryName);
		switch (countryName) {
			case "United Kingdom":
				countryName = "UK";
				break;
			case "Dubai":
				countryName = "Middle East";
				break;
			case "United Arab Emirates":
				countryName = "Middle East";
				break;
			default:
				countryName = StringUtils.capitalize(countryName.toLowerCase());
				break;
		}
		LOGGER.info("getCountyNameForCps : {}", countryName);
		return countryName;
	}
	
	private String getHsnCode(String hsnCode, ScServiceAttribute isIpcIntl) {
		if(Objects.nonNull(isIpcIntl) && NetworkConstants.Y.equals(isIpcIntl.getAttributeValue())) {
			return "";
		}
		return hsnCode;
	}
	
}