package com.tcl.dias.servicehandover.ipc.service;

import java.sql.Timestamp;
import java.text.ParseException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationRequestBO;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.IPCAddressConstructor;
import com.tcl.dias.servicehandover.util.IPCChargeLineItemsUtil;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.NetworkProductAttributeString;
import com.tcl.dias.servicehandover.util.ProductAttributeString;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.dias.servicehandover.util.UniqueProductCreation;

@Service
public class IpcBillingProductCreationService {

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
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	IPCAddressConstructor ipcAddressConstructor;
		
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	IPCChargeLineItemsUtil ipcChargeLineItemsUtil;
	
	private String category;
	
	String customerAddressLocationId = "";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingProductCreationService.class);
	
	public CreateOrderResponse productCreation(String orderCode,String processInstanceId,String serviceType, String serviceId, String serviceCode) {
		String updationRes = ipcChargeLineItemsUtil.updateLineItems(ipcChargeLineitemRepository.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndCommissionedFlag(serviceCode, serviceType, NetworkConstants.CREATE, NetworkConstants.NEW, IpcConstants.N));
		if(IpcConstants.SUCCESS.equals(updationRes)) {
			CreateOrder createOrder = frameProductCreationRequest(orderCode,processInstanceId,serviceType,serviceId,serviceCode);
			if (null != createOrder) {
				CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
						.callWebService(createOrderOperation, frameProductCreationRequest(orderCode,processInstanceId,serviceType,serviceId,serviceCode));
				String status = null;
				if (Objects.nonNull(createOrderResponse)) {
					status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
					LOGGER.info("productCreation completed for orderId {} with status {}", orderCode, status);
					return createOrderResponse;
				}
				LOGGER.info("productCreation completed for orderId {} with status {}", orderCode, status);
			} else {
				return null;
			}
		}
		return new CreateOrderResponse();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrder frameProductCreationRequest(String orderCode,String processInstanceId,String serviceType, String serviceId, String serviceCode) {
		LOGGER.info("productCreation invoked for orderId {} and Service type {}", orderCode, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		if (scServiceDetail != null) {
			LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
			String groupId = NetworkConstants.OPT_PROD.concat(scOrder.getOpOrderCode()) + "_"
					+ System.currentTimeMillis();
			List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
					.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
							Arrays.asList("billFreePeriod",Constants.SAP_CODE, CommonConstants.IS_IPC_BILLING_INTL, IpcConstants.IPC_BILLING_ENTITY));
			Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
			String billFreePeriod = attributeMap.getOrDefault("billFreePeriod", "0");
			String sapCode=attributeMap.get(Constants.SAP_CODE);
			ScOrderAttribute contractingAddress = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(Constants.CONTRACTING_ADDRESS, scOrder);
			ScOrderAttribute overagesChargesDiscountTiers = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(Constants.IPC_DT_OVERAGE_CHARGES, scOrder);
			List<IpcChargeLineitem> chargeLineitems = ipcChargeLineItemsUtil.formatLineItems(ipcChargeLineitemRepository
					.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndCommissionedFlag(serviceCode, serviceType,
							NetworkConstants.CREATE, NetworkConstants.IN_PROGRESS, IpcConstants.Y));
			category = ipcChargeLineItemsUtil.getCategory(chargeLineitems);
			if (chargeLineitems.isEmpty()) {
				return null;
			}
			chargeLineitems.forEach(chargeLineitem -> {
				UniqueProductCreation uniqueProductCreation = new UniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setProductDescription(chargeLineitem.getProductDescription());
				uniqueProductCreation.setMhs_remarks(chargeLineitem.getDescription().split(IpcConstants.SEMI_COLON_WITH_SPACE)[0]);
				uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
				uniqueProductCreation.setMrc(chargeLineitem.getMrc());
				uniqueProductCreation.setNrc(chargeLineitem.getNrc());
				uniqueProductCreation.setArc(chargeLineitem.getArc());
				uniqueProductCreation.setPpuRate(chargeLineitem.getPpuRate());
				uniqueProductCreation.setPricingModel(chargeLineitem.getPricingModel());
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(scServiceDetail);
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.CREATE);
				uniqueProductCreation.setOrderType(NetworkConstants.NEW);
				uniqueProductCreation.setChangeOrderType(NetworkConstants.NEW_ORDER);
				uniqueProductCreation.setQuantity(chargeLineitem.getQuantity());
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(false);
				uniqueProductCreation.setScenarioType("");
				uniqueProductCreation.setMigParentServiceCode(chargeLineitem.getMigParentServiceCode());
				uniqueProductCreation.setBaseBandwidth(chargeLineitem.getAdditionalParam());
				uniqueProductCreation.setScenarioType(chargeLineitem.getScenarioType());
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setBillingEntity(attributeMap.getOrDefault(IpcConstants.IPC_BILLING_ENTITY, IpcConstants.BILLING_ENTITY));
				uniqueProductCreation
						.setServicehandoverAudit(servicehandoverAuditRepository.findById(chargeLineitem.getSourceProductSequence()).get());
				uniqueProductCreation.setCommissioningDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(
						scServiceDetail.getServiceCommissionedDate().toString(),
						StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0));
				if (contractingAddress != null && contractingAddress.getAttributeValue() != null) {
					uniqueProductCreation.setContractingAddress(contractingAddress.getAttributeValue());
				}
				if (chargeLineitem.getChargeLineitem().equals(IpcConstants.DATA_TRANSFER_USAGE)) {
					uniqueProductCreation.setOverageChargesDiscountTiers(
							(overagesChargesDiscountTiers != null && overagesChargesDiscountTiers.getAttributeValue() != null) 
							? overagesChargesDiscountTiers.getAttributeValue() : IpcConstants.IPC_DT_USAGE_DISCOUNT_TIERS);
				}
				uniqueProductCreation.setSapCode(
						sapCode != null ? sapCode : loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
				uniqueProductCreation.setCloudCode(chargeLineitem.getCloudCode());
				CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation, attributeMap.containsKey(CommonConstants.IS_IPC_BILLING_INTL) ? attributeMap.get(CommonConstants.IS_IPC_BILLING_INTL) : "N", category);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
			});
			createOrder.setCreateOrderRequestInput(createOrderBO);
			JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		}
		return createOrder;
	}

	/**
	 * This method creates separate products for each of the chargeable line items
	 * attached to the order.
	 * 
	 * @param uniqueProductCreation
	 * @param category 
	 * @return
	 */
	CreateOrderRequestBO createUniqueProduct(UniqueProductCreation uniqueProductCreation, String intlBillingFlag, String category) {
		
		ScOrder scOrder = uniqueProductCreation.getScOrder();
		ScServiceDetail scServiceDetail = uniqueProductCreation.getScServiceDetail();

		LOGGER.info("createUniqueProduct invoked for orderId {} accountId {} productName {} ", scOrder.getOpOrderCode(),
				uniqueProductCreation.getAccountId(), uniqueProductCreation.getProductName());
		ServicehandoverAudit audit = uniqueProductCreation.getServicehandoverAudit();
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		ProductCreationRequestBO productCreationRequestBO = new ProductCreationRequestBO();
		ProductCreationInputBO productCreationInputBO = new ProductCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();

		CommonBO commonBo = new CommonBO();
		GenevaIpcOrderEntry ipcOrderEntry = null;

		commonBo.setGroupId(uniqueProductCreation.getInputGroupId());
		commonBo.setSourceSystem(Constants.SOURCE_SYSTEM);
		commonBo.setRequestType(Constants.PRODUCT);
		commonBo.setActionType(uniqueProductCreation.getActionType());
		commonBo.setCustomerRef(uniqueProductCreation.getSapCode());
		commonBo.setAccountNum(uniqueProductCreation.getAccountId());
		commonBo.setBillingEntity(uniqueProductCreation.getBillingEntity());
		commonBo.setProviderSegment(Constants.PROVIDER_SEGMENT);
		commonBo.setAdvanceBoo(Constants.T);
		commonBo.setServiceType(Constants.SERVICE_TYPE);


		boolean isIntlBilling = "Y".equals(intlBillingFlag) ? true : false;
		LOGGER.info("Billing will be done for {} Entity", isIntlBilling ? BillingConstants.INTL_CUSTOMER : BillingConstants.DOMESTIC_CUSTOMER);
		uniqueProductCreation.setBillingInternational(isIntlBilling);
		commonBo.setInvoicingCoName(isIntlBilling ? BillingConstants.INVOICING_CONAME_INTL :BillingConstants.INVOICING_CONAME);
		
		commonBo.setSourceProductSeq(String.valueOf(audit.getId()));
		productCreationInputBO.setTermEndDate("");
		productCreationInputBO.setTermReason("");
		productCreationInputBO.setDepositRefundBoo(Constants.T);

		if (uniqueProductCreation.getIsMacd()) {
			productCreationInputBO.setTermEndDate(uniqueProductCreation.getCommissioningDate());
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

			case LeAttributesConstants.LE_STATE_GST_NO:
				if (isIntlBilling) {
					productCreationInputBO.setCONTRACTINGGSTINNO("");
				} else {
					productCreationInputBO.setCONTRACTINGGSTINNO(scOrderAttribute.getAttributeValue());
				}
				break;
			case LeAttributesConstants.TAX_EXEMPTION:
				if (!isIntlBilling) {
					productCreationInputBO.setTaxExemptTxt(scOrderAttribute.getAttributeValue());
					if ("YES".equals(productCreationInputBO.getTaxExemptTxt().toUpperCase())) {
						ScOrderAttribute scOdrAttr = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scOrder.getId(), IpcConstants.TAX_EXEMPTION_REASON);
						if (scOdrAttr != null)
							productCreationInputBO.setTaxExemptRef(
									WordUtils.capitalize(scOdrAttr.getAttributeValue().toLowerCase()));
					}
				}
				break;
			case LeAttributesConstants.TAX_EXEMPTION_ATTACHMENT:
				productCreationInputBO.setTaxationDocsURL(scOrderAttribute.getAttributeValue());
				break;

			case LeAttributesConstants.PO_DATE:
				try {
					if(scOrderAttribute.getAttributeValue()!=null) {
						uniqueProductCreation.setPoDate(TimeStampUtil.poDateFormatCpe(scOrderAttribute.getAttributeValue()));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;

			case LeAttributesConstants.PO_NUMBER:
				if (scOrderAttribute.getAttributeValue() != null) {
					uniqueProductCreation.setPoNumber(scOrderAttribute.getAttributeValue());
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
			if (IpcConstants.ARREAR.equals(scContractInfo.getBillingMethod())) {
				commonBo.setAdvanceBoo(Constants.F);
			}

		});

		productCreationInputBO.setCONTRACTINGADDRESS(uniqueProductCreation.getContractingAddress());

		productCreationInputBO.setTotalArc(String.valueOf(uniqueProductCreation.getArc()));
		productCreationInputBO.setTotalNrc(String.valueOf(uniqueProductCreation.getNrc()));

		productCreationInputBO.setOrderType(uniqueProductCreation.getOrderType());
		productCreationInputBO.setChangeOrderType(uniqueProductCreation.getChangeOrderType());

		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		
		
		productCreationInputBO.setCommissionDate(uniqueProductCreation.getCommissioningDate());
		productCreationInputBO.setBillActivationDate(uniqueProductCreation.getCommissioningDate());
		productCreationInputBO.setBillGenerationDate(uniqueProductCreation.getCommissioningDate());
	
		String contractGstAddress = StringUtils.trimToEmpty(ipcAddressConstructor.contractGstAddress(
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
		productCreationInputBO.setCiruitCount(Constants.ONE);
		

		productCreationInputBO.setVatIdentifier("");
		if (isIntlBilling) {
			productCreationInputBO.setCpsName("Tax-Exempt");
			productCreationInputBO.setInvocingCoNamePrd(BillingConstants.INVOICING_CONAME_INTL);
			productCreationInputBO.setBusinessUnit("");
		} else {
			productCreationInputBO.setCpsName("");
			productCreationInputBO.setInvocingCoNamePrd(BillingConstants.INVOICING_CONAME);
			productCreationInputBO.setBusinessUnit(BillingConstants.TATA);
		}

		productCreationInputBO.setProdAddr1((scServiceDetail.getSourceAddressLineOne()!=null && scServiceDetail.getSourceAddressLineOne().length() >= 120)
										? scServiceDetail.getSourceAddressLineOne().substring(0, 120)
										: scServiceDetail.getSourceAddressLineOne());
		productCreationInputBO.setProdAddr2((scServiceDetail.getSourceAddressLineTwo()!=null && scServiceDetail.getSourceAddressLineTwo().length() >= 120)
										? scServiceDetail.getSourceAddressLineTwo().substring(0, 120)
										: scServiceDetail.getSourceAddressLineTwo());
		productCreationInputBO.setProdAddr3(scServiceDetail.getSourceLocality());
		productCreationInputBO.setProdCity(scServiceDetail.getSourceCity());
		productCreationInputBO.setProdState(scServiceDetail.getSourceState());
		productCreationInputBO.setProdCountry(scServiceDetail.getSourceCountry());
		productCreationInputBO.setProdZipcode(scServiceDetail.getSourcePincode());
		productCreationInputBO.setSITEAADDRESS(idcLocation(scServiceDetail));
		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setEventString("");

		productCreationInputBO.setScenarioType(uniqueProductCreation.getScenarioType());

		if (!uniqueProductCreation.getOrderType().equals(Constants.CREATE)) {
			productCreationInputBO.setSourceOldProdSeq(commonBo.getSourceProductSeq());
		}

		productCreationInputBO.setRateOverrideBoo("");
		productCreationInputBO.setFinalFlag("");

		productCreationInputBO.setAttr9("");
		productCreationInputBO.setAttr10("");
		

		productCreationInputBO.setEventSource("");
		productCreationInputBO.setEventsourceLabel("");
		productCreationInputBO.setEventSourceText("");
		productCreationInputBO.setDiscountTiers("");
		productCreationInputBO.setCostBandName("");
		productCreationInputBO.setTerminateName("");
		productCreationInputBO.setEventClassName("");
		productCreationInputBO.setChargeSegmentName("");
		productCreationInputBO.setChargingRateValue("");
		
		if(productCreationInputBO.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
			productCreationInputBO.setEventSource(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setEventsourceLabel(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setEventSourceText(scServiceDetail.getUuid()+CommonConstants.UNDERSCORE+Constants.DT);
			productCreationInputBO.setDiscountTiers(uniqueProductCreation.getOverageChargesDiscountTiers());
			productCreationInputBO.setCostBandName(IpcConstants.IPC_DEFAULT_COST_BAND_NAME);
			productCreationInputBO.setTerminateName(IpcConstants.IPC_DEFAULT_TERMINATE_NAME);
			productCreationInputBO.setEventClassName(IpcConstants.IPC_DEFAULT_EVENT_CLASS_NAME);
			productCreationInputBO.setChargeSegmentName(IpcConstants.IPC_DEFAULT_CHARGE_SEGMENT_NAME);
			productCreationInputBO.setChargingRateValue("0.00");
		} else if (productCreationInputBO.getProductName().equals(Constants.VM_USAGE_CHARGES)){
			productCreationInputBO.setEventSource(uniqueProductCreation.getCloudCode());
			productCreationInputBO.setEventsourceLabel(uniqueProductCreation.getCloudCode());
			productCreationInputBO.setEventSourceText(uniqueProductCreation.getCloudCode());
			productCreationInputBO.setCostBandName(IpcConstants.IPC_PPU_DEFAULT_CB);
			productCreationInputBO.setTerminateName(IpcConstants.IPC_PPU_DEFAULT_TR);
			productCreationInputBO.setEventClassName(uniqueProductCreation.getPricingModel());
			productCreationInputBO.setChargeSegmentName(IpcConstants.IPC_PPU_DEFAULT);
			productCreationInputBO.setChargingRateValue(uniqueProductCreation.getPpuRate());
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

		Double arc = Double.valueOf(uniqueProductCreation.getArc()) / Integer.valueOf(uniqueProductCreation.getQuantity());
		if(isIntlBilling) {
			arc=arc/12;
		}
		productCreationInputBO.setOvrdnPeriodicPrice(arc.toString());

		productCreationInputBO.setOvrdnIntPrice(uniqueProductCreation.getNrc());

		// Constructing attribute String
		productCreationInputBO.setAttributeString(generateProductAttributeString(scOrder, scServiceDetail, uniqueProductCreation, category).toString(uniqueProductCreation));
		commonAttributesBO.setComReqAttributes(commonBo);
		productCreationRequestBO.setProductInput(productCreationInputBO);

		createOrderRequestBO.setProdCreationInput(productCreationRequestBO);
		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(null);

		if (!commonBo.getActionType().equals(Constants.CREATE)) {
			ServicehandoverAudit terminateAudit = uniqueProductCreation.getServicehandoverAudit();
			if(terminateAudit!=null) {
				terminateAudit.setStatus(Constants.IN_PROGRESS);
				terminateAudit.setUpdatedDate(new Date());
				terminateAudit.setGenevaGrpId(commonBo.getGroupId());
				terminateAudit.setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
				terminateAudit.setMacdOrderId(commonBo.getActionType().equals(Constants.TERMINATE)
								? uniqueProductCreation.getMacdOrderId()
								: scOrder.getOpOrderCode());
				if (!commonBo.getActionType().equals(Constants.TERMINATE)) {
					terminateAudit.setParentCloudCode(terminateAudit.getCloudCode());
					terminateAudit.setCloudCode(uniqueProductCreation.getCloudCode());
				}
				terminateAudit.setActionType(Constants.TERMINATE);
				servicehandoverAuditRepository.save(terminateAudit);
			
			}
		} else {
			audit.setLegalEntity(productCreationInputBO.getCustomerName());
			audit.setCreatedDate(new Date());
			audit.setCrnId(commonBo.getCustomerRef());
			audit.setCustomerType(isIntlBilling ? BillingConstants.INTL_CUSTOMER : BillingConstants.DOMESTIC_CUSTOMER);
			audit.setRequest(createOrderRequestBO.toString());
			audit.setRequestType(Constants.PRD_COMM);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setStatus(Constants.IN_PROGRESS);
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
			audit.setActionType(Constants.CREATE);
			servicehandoverAuditRepository.save(audit);

			GenevaIpcOrderEntry ipcOdrEntry = genevaIpcOrderEntryMapper.createOrderToGenevaIpcOrderMapper(createOrderRequestBO);
			genevaIpcOrderEntryRepository.save(ipcOdrEntry);

		}

		LOGGER.info("createUniqueProduct completed for orderId {} accountId {} productName {} ",
				scOrder.getOpOrderCode(), uniqueProductCreation.getAccountId(), uniqueProductCreation.getProductName());
		return createOrderRequestBO;
	}

	
	//Generate Product Attribute String For Indian Billing
	private ProductAttributeString generateProductAttributeString(ScOrder scOrder, ScServiceDetail scServiceDetail,
			UniqueProductCreation uniqueProductCreation, String category) {
		ProductAttributeString productAttributeString = new ProductAttributeString();
		productAttributeString.setBUSINESS_AREA(Constants.INHQ);
		productAttributeString.setCIRCUIT_ID(scServiceDetail.getUuid());
		productAttributeString.setLEGAL_CUID(scOrder.getTpsSfdcCuid());
		productAttributeString.setOPPORTUNITY_ID(scOrder.getTpsSfdcOptyId());
		productAttributeString.setCATEGORY(category);
		productAttributeString.setOPPORTUNITY_CLASSIFICATION(scOrder.getOpportunityClassification());
		if(scOrder.getErfCustPartnerId() != null) {
			productAttributeString.setIS_PARTNER("YES");
			productAttributeString.setPARTNER_NAME((scOrder.getErfCustPartnerName() != null && scOrder.getErfCustPartnerName().length()>40) ? scOrder.getErfCustPartnerName().substring(0, 40) : scOrder.getErfCustPartnerName());
			productAttributeString.setPARTNER_REFERENCE(scOrder.getPartnerCuid().toString());
		}
		productAttributeString.setCOPF_ID(scOrder.getOpOrderCode());
		productAttributeString.setPO_DATE(uniqueProductCreation.getPoDate());
		productAttributeString.setPO_START_DATE(uniqueProductCreation.getPoDate());
		productAttributeString.setPO_NUMBER(uniqueProductCreation.getPoNumber());
		productAttributeString.setGST_PRODUCT_CODE(uniqueProductCreation.getHsnCode());
		if(uniqueProductCreation.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
			productAttributeString.setSLAB_WISE_BILLING(CommonConstants.YES);
			productAttributeString.setUSAGE_MODEL(IpcConstants.IPC_DATA_TRANSFER);
		} else if (uniqueProductCreation.getProductName().equals(Constants.VM_USAGE_CHARGES)){
			productAttributeString.setCOMPONENT_ID(uniqueProductCreation.getCloudCode());
			productAttributeString.setPRODUCT_DESCRIPTION(uniqueProductCreation.getProductDescription());
		}
		productAttributeString.setVIZNET_ID(uniqueProductCreation.getMigParentServiceCode() != null ? uniqueProductCreation.getMigParentServiceCode() : "");
		productAttributeString.setBASE_BANDWIDTH(uniqueProductCreation.getBaseBandwidth()!=null?uniqueProductCreation.getBaseBandwidth().concat(" GB"):uniqueProductCreation.getBaseBandwidth());
		productAttributeString.setMINIMUM_COMMITMENT(uniqueProductCreation.getBaseBandwidth()!=null?uniqueProductCreation.getBaseBandwidth().concat(" GB"):uniqueProductCreation.getBaseBandwidth());
		
		productAttributeString.setMHS_REMARKS(
				Objects.nonNull(uniqueProductCreation.getMhs_remarks()) ? uniqueProductCreation.getMhs_remarks().trim() : "");

		productAttributeString.setLOCATION(scServiceDetail.getSourceCity());

		productAttributeString.setORDER_TYPE(Constants.NEW);
		productAttributeString.setPARENT_ID(scOrder.getTpsSfdcOptyId());
		productAttributeString.setPARENT_SERVICE(Constants.SERVICE_TYPE);
		productAttributeString.setPRODUCT_OFFERING("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setPRODUCT_SUB_TYPE("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setSERVICE_TYPE_CLUBBING(Constants.SERVICE_TYPE);

		productAttributeString.setSITE_END(Constants.A);
		productAttributeString.setSITE_NAME(Constants.IPC_DC_SITE);
		productAttributeString.setSITE_LOCATION(scServiceDetail.getSourceCity());

		productAttributeString.setSITE_A_AddressLine1(scServiceDetail.getSourceAddressLineOne());
		productAttributeString.setSITE_A_AddressLine2(scServiceDetail.getSourceAddressLineTwo());
		productAttributeString.setSITE_A_AddressLine3("");
		productAttributeString.setSITE_A_CITY(scServiceDetail.getSourceCity());
		productAttributeString.setSITE_A_STATE(scServiceDetail.getSourceState());
		productAttributeString.setSITE_A_COUNTRY(scServiceDetail.getSourceCountry());
		productAttributeString.setSITE_A_pincode(scServiceDetail.getSourcePincode());

		productAttributeString.setUOM(Constants.MBPS);
		productAttributeString.setQUANTITY(uniqueProductCreation.getQuantity());
		productAttributeString.setNO_OF_MAILBOXES(Constants.ONE);
		productAttributeString.setNO_OF_PORTS(Constants.ONE);
		productAttributeString.setNO_OF_SEATS(Constants.ONE);
		productAttributeString.setNO_OF_SUPERVISOR(Constants.ONE);
		productAttributeString.setCOMMISSIONING_DATE(TimeStampUtil.convertWithTimeStampToWithoutTimeStamp(uniqueProductCreation.getCommissioningDate()));
		return productAttributeString;
	}

	//Generate Product Attribute String For International Billing
	private ProductAttributeString generateProductAttributeStringIntl(ScOrder scOrder, ScServiceDetail scServiceDetail,
			UniqueProductCreation uniqueProductCreation) {
		LOGGER.info("generateProductAttributes for International is invoked");
		ProductAttributeString productAttributeString = new ProductAttributeString();
		productAttributeString.setBUSINESS_AREA(Constants.INHQ);
		productAttributeString.setCIRCUIT_ID(scServiceDetail.getUuid());
		productAttributeString.setLEGAL_CUID(scOrder.getTpsSfdcCuid());
		productAttributeString.setCOPF_ID(scOrder.getOpOrderCode());
		productAttributeString.setPO_DATE(uniqueProductCreation.getPoDate());
		productAttributeString.setPO_START_DATE(uniqueProductCreation.getPoDate());
		productAttributeString.setPO_NUMBER(uniqueProductCreation.getPoNumber());
		productAttributeString.setGST_PRODUCT_CODE(uniqueProductCreation.getHsnCode());
		productAttributeString.setOPPORTUNITY_ID(scOrder.getTpsSfdcOptyId());
		productAttributeString.setOPPORTUNITY_CLASSIFICATION(scOrder.getOpportunityClassification());
		if(scOrder.getErfCustPartnerId() != null) {
			productAttributeString.setIS_PARTNER("YES");
			productAttributeString.setPARTNER_NAME(scOrder.getErfCustPartnerName());
			productAttributeString.setPARTNER_REFERENCE(scOrder.getPartnerCuid().toString());
		}
		if(uniqueProductCreation.getProductName().equals(Constants.DATA_TRANSFER_USAGE)) {
			productAttributeString.setSLAB_WISE_BILLING(CommonConstants.YES);
			productAttributeString.setUSAGE_MODEL(IpcConstants.IPC_DATA_TRANSFER);
		} else if (uniqueProductCreation.getProductName().equals(Constants.VM_USAGE_CHARGES)){
			productAttributeString.setCOMPONENT_ID(uniqueProductCreation.getCloudCode());
			productAttributeString.setPRODUCT_DESCRIPTION(uniqueProductCreation.getProductDescription());
		}
		productAttributeString.setVIZNET_ID(uniqueProductCreation.getMigParentServiceCode() != null ? uniqueProductCreation.getMigParentServiceCode() : "");
		productAttributeString.setBASE_BANDWIDTH(uniqueProductCreation.getBaseBandwidth()!=null?uniqueProductCreation.getBaseBandwidth().concat(" GB"):uniqueProductCreation.getBaseBandwidth());
		productAttributeString.setMINIMUM_COMMITMENT(uniqueProductCreation.getBaseBandwidth()!=null?uniqueProductCreation.getBaseBandwidth().concat(" GB"):uniqueProductCreation.getBaseBandwidth());
		
		productAttributeString.setMHS_REMARKS(
				Objects.nonNull(uniqueProductCreation.getMhs_remarks()) ? uniqueProductCreation.getMhs_remarks().trim() : "");

		productAttributeString.setLOCATION(scServiceDetail.getSourceCity());

		productAttributeString.setORDER_TYPE(Constants.NEW);
		productAttributeString.setPARENT_ID(scOrder.getTpsSfdcOptyId());
		productAttributeString.setPARENT_SERVICE(Constants.SERVICE_TYPE);
		productAttributeString.setPRODUCT_OFFERING("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setPRODUCT_SUB_TYPE("IPC " + uniqueProductCreation.getProductName());
		productAttributeString.setSERVICE_TYPE_CLUBBING(Constants.SERVICE_TYPE);

		productAttributeString.setSITE_END(Constants.A);
		productAttributeString.setSITE_NAME(Constants.IPC_DC_SITE);
		productAttributeString.setSITE_LOCATION(scServiceDetail.getSourceCity());

		productAttributeString.setSITE_A_AddressLine1(scServiceDetail.getSourceAddressLineOne());
		productAttributeString.setSITE_A_AddressLine2(scServiceDetail.getSourceAddressLineTwo());
		productAttributeString.setSITE_A_AddressLine3("");
		productAttributeString.setSITE_A_CITY(scServiceDetail.getSourceCity());
		productAttributeString.setSITE_A_STATE(scServiceDetail.getSourceState());
		productAttributeString.setSITE_A_COUNTRY(scServiceDetail.getSourceCountry());
		productAttributeString.setSITE_A_pincode(scServiceDetail.getSourcePincode());

		productAttributeString.setUOM(Constants.MBPS);
		productAttributeString.setQUANTITY(uniqueProductCreation.getQuantity());
		productAttributeString.setNO_OF_MAILBOXES(Constants.ONE);
		productAttributeString.setNO_OF_PORTS(Constants.ONE);
		productAttributeString.setNO_OF_SEATS(Constants.ONE);
		productAttributeString.setNO_OF_SUPERVISOR(Constants.ONE);
		productAttributeString.setCOMMISSIONING_DATE(TimeStampUtil.convertWithTimeStampToWithoutTimeStamp(uniqueProductCreation.getCommissioningDate()));
		
		return productAttributeString;
	}

	public String idcLocation(ScServiceDetail scServiceDetail) {
		return "SITE_A_AddressLine1=,;SITE_A_AddressLine2=;SITE_A_AddressLine3=;SITE_A_City="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceCity()) + ";SITE_A_State="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceState()) + ";SITE_A_Country="
				+ WordUtils.capitalizeFully(scServiceDetail.getSourceCountry()) + ";SITE_A_pincode=,;";
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

	public CreateOrderResponse productCreationRequestFormationTest(String orderCode,String processInstanceId,String serviceType, String serviceId, String serviceCode) {
		ipcChargeLineItemsUtil.updateLineItems(ipcChargeLineitemRepository
				.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndCommissionedFlag(serviceCode, serviceType, NetworkConstants.CREATE, NetworkConstants.NEW, IpcConstants.N));
		CreateOrder createOrder = frameProductCreationRequest(orderCode,processInstanceId,serviceType,serviceId,serviceCode);
		LOGGER.info("Order Request: ", createOrder);
		LOGGER.info("productCreation completed");
		return new CreateOrderResponse();
	}
	
}
