package com.tcl.dias.servicehandover.cancellation.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationRequestBO;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.NetworkProductAttributeString;
import com.tcl.dias.servicehandover.util.NetworkUniqueProductCreation;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

@Service
public class CancellationBillingProductCreationService {

	@Autowired
	@Qualifier("Order")
	SOAPConnector orderSoapConnector;

	@Value("${createOrder}")
	private String createOrderOperation;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;

	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;

	@Autowired
	LoadCustomerDetails loadCustomerDetails;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationBillingProductCreationService.class);

	String status = "";

	String customerAddressLocationId = "";

	ScServiceAttribute scServiceAttribute;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	/**
	 * Method for Cancellation Product Commissioning
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param serviceId 
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrderResponse triggerCancellationProductCreation(String orderId, String processInstanceId,
			String serviceType, String serviceId, String serviceCode) {
		LOGGER.info("Cancellation ProductCreation invoked for orderId {} and Service type {}", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		String groupId = NetworkConstants.OPT_PROD.concat(scOrder.getOpOrderCode())+"_"+System.currentTimeMillis();
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		if (scServiceDetail!=null) {
			List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository.findAllForServiceCancellationProductCommissioning(serviceId,serviceType);
			chargeLineitems.forEach(chargeLineitem -> {
				ServicehandoverAudit audit = new ServicehandoverAudit();
				audit = servicehandoverAuditRepo.save(audit);
				NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
				uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(scServiceDetail);
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.CREATE);
				uniqueProductCreation.setOrderType(NetworkConstants.NEW);
				uniqueProductCreation.setChangeOrderType(NetworkConstants.NEW_ORDER);
				uniqueProductCreation.setQuantity(NetworkConstants.ONE);
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setCpeModel(StringUtils.trimToEmpty(chargeLineitem.getCpeModel()));
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(false);
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setBillingMethod(NetworkConstants.BILLING_TYPE_ADVANCE);
				uniqueProductCreation.setBillingType(chargeLineitem.getBillingType());
				uniqueProductCreation.setSourceProductSeq(audit.getId().toString());
				uniqueProductCreation.setServicehandoverAudit(audit);
				uniqueProductCreation.setCommissioningDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(chargeLineitem.getTermDate(), 0));
				chargeLineitem.setActionType(NetworkConstants.CREATE);
				chargeLineitem.setCommissioningFlag(NetworkConstants.Y);
				chargeLineitem.setServiceTerminationFlag(NetworkConstants.N);
				chargeLineitem.setSourceProdSequence(audit.getId().toString());
				chargeLineitemRepository.save(chargeLineitem);
				chargeLineitemRepository.flush();
				CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
			});
		}

		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("Cancellation ProductCreation completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}
	
	/**
	 * This method creates separate products for each of the chargeable line items
	 * attached to the order.
	 * 
	 * @param uniqueProductCreation
	 * @return
	 */
	public CreateOrderRequestBO createUniqueProduct(NetworkUniqueProductCreation uniqueProductCreation) {
		LOGGER.info("createUniqueProduct invoked for orderId {} accountId {} productName {} ",
				uniqueProductCreation.getScOrder().getOpOrderCode(), uniqueProductCreation.getAccountId(),
				uniqueProductCreation.getProductName());

		ScOrder scOrder = uniqueProductCreation.getScOrder();
		ScServiceDetail scServiceDetail = uniqueProductCreation.getScServiceDetail();
		ServicehandoverAudit audit = uniqueProductCreation.getServicehandoverAudit();
		CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
		ProductCreationRequestBO productCreationRequestBO = new ProductCreationRequestBO();
		ProductCreationInputBO productCreationInputBO = new ProductCreationInputBO();
		CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
		
		CommonBO commonBo = new CommonBO();
		commonBo.setGroupId(uniqueProductCreation.getInputGroupId());
		commonBo.setSourceSystem(NetworkConstants.SOURCE_SYSTEM);
		commonBo.setRequestType(NetworkConstants.PRODUCT);
		commonBo.setActionType(uniqueProductCreation.getActionType());
		ScComponentAttribute sapCode = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "sapCode", "LM","A");
		
		if(sapCode!=null && sapCode.getAttributeValue()!=null) {
			commonBo.setCustomerRef(sapCode.getAttributeValue());
		}else {
			commonBo.setCustomerRef(loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
		}
		commonBo.setAccountNum(uniqueProductCreation.getAccountId());
		commonBo.setInvoicingCoName(NetworkConstants.INVOICING_CONAME);
		commonBo.setBillingEntity(NetworkConstants.BILLING_ENTITY);
		commonBo.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
		commonBo.setAdvanceBoo(NetworkConstants.T);
		
		commonBo.setServiceType(uniqueProductCreation.getBillingType());
		commonBo.setSourceProductSeq(uniqueProductCreation.getSourceProductSeq());

		productCreationInputBO.setProductName(uniqueProductCreation.getProductName());

		List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), NetworkConstants.Y);

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
				productCreationInputBO
						.setCONTRACTINGGSTINNO(NetworkConstants.CPE.equals(uniqueProductCreation.getServiceType())
								? ""
								: scOrderAttribute.getAttributeValue());
				break;
			}

		});
		
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("taxExemption", NetworkConstants.BILL_START_DATE, LeAttributesConstants.PO_DATE, LeAttributesConstants.PO_NUMBER, NetworkConstants.EVENT_SOURCE),
				scServiceDetail.getId(), "LM", "A");
		
		String taxExemption=StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));
		if("Y".equals(taxExemption.toUpperCase())){
			productCreationInputBO.setTaxExemptTxt("yes");
			scServiceAttribute = serviceAttributeRepository.findByScServiceDetail_idAndAttributeName(
					uniqueProductCreation.getScServiceDetail().getId(),
					LeAttributesConstants.TAX_EXEMPTION_REASON);
			if (scServiceAttribute != null)
				productCreationInputBO.setTaxExemptRef(
						WordUtils.capitalize(scServiceAttribute.getAttributeValue().toLowerCase()));
		}
		 			
		List<ScContractInfo> scContractInfoList = scContractInfoRepository.findByScOrder_id(scOrder.getId());

		scContractInfoList.forEach((scContractInfo) -> {
			commonBo.setPaymentDueDate(scContractInfo.getPaymentTerm());
			productCreationInputBO.setCustomerName(scContractInfo.getErfCustLeName());

			productCreationInputBO.setContractDuration(scContractInfo.getOrderTermInMonths().toString());
			productCreationInputBO.setCustomerOrderNum(scOrder.getOpOrderCode());
		});

		
		productCreationInputBO.setCONTRACTINGADDRESS(addressConstructor.contractAddress(customerAddressLocationId));
		//productCreationInputBO.setTotalArc(uniqueProductCreation.getArc().toString());
		productCreationInputBO.setTotalNrc(uniqueProductCreation.getNrc().toString());
		productCreationInputBO.setOrderType(uniqueProductCreation.getOrderType());
		productCreationInputBO.setChangeOrderType(uniqueProductCreation.getChangeOrderType());

		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		productCreationInputBO.setCommissionDate(uniqueProductCreation.getCommissioningDate());
		productCreationInputBO.setBillActivationDate(uniqueProductCreation.getCommissioningDate());
		productCreationInputBO.setBillGenerationDate(uniqueProductCreation.getCommissioningDate());
		
		uniqueProductCreation.setPoNumber(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_NUMBER,"")));
		uniqueProductCreation.setPoDate(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_DATE,"")));
	
		productCreationInputBO.setCustomerType(NetworkConstants.EXISTING);
		productCreationInputBO.setCiruitCount(NetworkConstants.ONE);

		productCreationInputBO.setCpsName("");
		productCreationInputBO.setInvocingCoNamePrd(NetworkConstants.INVOICING_CONAME);
		productCreationInputBO.setVatIdentifier("");

		productCreationInputBO.setBusinessUnit(NetworkConstants.TATA);

		addressConstructor.productAddress(scServiceDetail,productCreationInputBO);

		if(BillingConstants.CISCO_WEBEX.equals(uniqueProductCreation.getBillingType())) {
			productCreationInputBO.setSITEGSTINADDRESS("");
			productCreationInputBO.setSITEGSTINNO("");
		}else {
			String siteGstAddress= StringUtils.trimToEmpty(addressConstructor.siteGstAddress(scServiceDetail,uniqueProductCreation.getServiceType(),"A"));
			Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("siteGstNumber"), scServiceDetail.getId(), "LM", "A");
			if (StringUtils.isNotBlank(siteGstAddress)) {
				productCreationInputBO.setSITEGSTINADDRESS(siteGstAddress);
				productCreationInputBO.setSITEGSTINNO(siteGstMap.get("siteGstNumber"));
			} else {
				LOGGER.info("Site Gst not Available!");
				productCreationInputBO.setSITEGSTINADDRESS("");
				productCreationInputBO.setSITEGSTINNO("");
			}
		}
		
		if (StringUtils.isNotEmpty(productCreationInputBO.getCONTRACTINGGSTINNO())) {
			String contractGstAddress = StringUtils.trimToEmpty(addressConstructor.contractGstAddress(
					StringUtils.trimToEmpty(productCreationInputBO.getCONTRACTINGGSTINNO()), scOrder.getErfCustLeId(),
					scOrder));
			if (StringUtils.isNotBlank(contractGstAddress)) {
				productCreationInputBO.setCONTRACTGSTINADDRESS(contractGstAddress);
			} else {
				LOGGER.info("Contract Gst is not Available!");
				productCreationInputBO.setCONTRACTINGGSTINNO("");
				productCreationInputBO.setCONTRACTGSTINADDRESS("");

			}
		}
		productCreationInputBO.setSITEAADDRESS(addressConstructor.siteAddressGen(scServiceDetail,"A"));
		if (uniqueProductCreation.getServiceType().contains(NetworkConstants.NPL)) {
			productCreationInputBO.setSITEBADDRESS(addressConstructor.siteAddressGen(scServiceDetail,"B"));
			
		}
		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setScenarioType("");
		productCreationInputBO.setChargingRateValue("");
		
		productCreationInputBO.setEventString("");
		productCreationInputBO.setSourceOldProdSeq("");
		productCreationInputBO.setRateOverrideBoo("");
		productCreationInputBO.setFinalFlag("");
		productCreationInputBO.setAttr9("");
		productCreationInputBO.setAttr10("");
		productCreationInputBO.setDiscountName("");
		productCreationInputBO.setDiscountTiers("");
		productCreationInputBO.setMinQtyTier("");
		productCreationInputBO.setSourceParentProductSeq("");
		productCreationInputBO.setRateEndDtm("");
		productCreationInputBO.setComponentName(uniqueProductCreation.getComponent()); // we have only LM for now
		productCreationInputBO.setDeliveryAddr("");

		
		productCreationInputBO.setOvrdnPeriodicPrice("");
		productCreationInputBO.setOvrdnIntPrice(String.valueOf(uniqueProductCreation.getNrc()));

		productCreationInputBO.setEquipmentType("");
		productCreationInputBO.setSystemIdicator("");
		productCreationInputBO.setCopfId(scOrder.getOpOrderCode());
		productCreationInputBO.setProdQuantity(uniqueProductCreation.getQuantity());
		productCreationInputBO.setUsername("");
		productCreationInputBO.setCForm("");
		productCreationInputBO.setAddEventSRC("");
		productCreationInputBO.setTermEventSRC("");
		productCreationInputBO.setISize("");
		productCreationInputBO.setTaxationDocsURL("");

		productCreationInputBO.setProrateBoo(NetworkConstants.T);
		productCreationInputBO.setChargePeriod(NetworkConstants.ONE);

		productCreationInputBO.setTermEndDate("");
		productCreationInputBO.setTermReason("");
		productCreationInputBO.setDepositRefundBoo(NetworkConstants.T);

		productCreationInputBO.setRefundBoo(NetworkConstants.T);
		productCreationInputBO.setServiceId(scServiceDetail.getUuid());
		productCreationInputBO.setSiteAGeoCode("");

		productCreationInputBO
				.setAttributeString(generateProductAttributeString(scOrder, scServiceDetail, uniqueProductCreation));
		
		if (uniqueProductCreation.getIsMacd()) {
			productCreationInputBO.setSourceOldProdSeq(uniqueProductCreation.getSourceProductSeq());
			productCreationInputBO.setTermEndDate(uniqueProductCreation.getCommissioningDate());
			productCreationInputBO.setTermReason(NetworkConstants.CHANGE_BANDWIDTH);
			productCreationInputBO.setDepositRefundBoo(NetworkConstants.F);
		}
		
		commonAttributesBO.setComReqAttributes(commonBo);
		productCreationRequestBO.setProductInput(productCreationInputBO);

		createOrderRequestBO.setProdCreationInput(productCreationRequestBO);
		createOrderRequestBO.setCommonInput(commonAttributesBO);
		createOrderRequestBO.setAccCreationInput(null);

		if (!uniqueProductCreation.getIsMacd()) {
			audit.setLegalEntity(productCreationInputBO.getCustomerName());
			audit.setCreatedDate(new Date());
			audit.setCrnId(commonBo.getCustomerRef());
			audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
			audit.setRequest(createOrderRequestBO.toString());
			audit.setRequestType(NetworkConstants.PRD_COMM);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setStatus(NetworkConstants.IN_PROGRESS);
			audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
			audit.setGenevaGrpId(commonBo.getGroupId());
			audit.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
			audit.setServiceId(uniqueProductCreation.getScServiceDetail().getId().toString());
			audit.setServiceCode(productCreationInputBO.getServiceId());
			audit.setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
			audit.setAccountNumber(uniqueProductCreation.getAccountId());
			audit.setCount(uniqueProductCreation.getCount());
			audit.setSourceProdSeq(commonBo.getSourceProductSeq());
			audit.setServiceType(uniqueProductCreation.getServiceType());
			audit.setActionType(NetworkConstants.CREATE);
			servicehandoverAuditRepo.save(audit);
		}else {
			ServicehandoverAudit auditMacd = servicehandoverAuditRepo.findBySourceProdSeq(uniqueProductCreation.getSourceProductSeq());
			if(auditMacd!=null) {
				auditMacd.setStatus(NetworkConstants.IN_PROGRESS);
				auditMacd.setActionType(NetworkConstants.TERMINATE);
				auditMacd.setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
				servicehandoverAuditRepo.save(auditMacd);
			}
		}

		GenevaIpcOrderEntry OrderEntry = genevaIpcOrderEntryMapper.createOrderToGenevaIpcOrderMapper(createOrderRequestBO);
		genevaIpcOrderEntryRepository.save(OrderEntry);

		LOGGER.info("createUniqueProduct completed for orderId {} accountId {} productName {} ",
				scOrder.getOpOrderCode(), uniqueProductCreation.getAccountId(), uniqueProductCreation.getProductName());
		return createOrderRequestBO;
	}
	
	public String generateProductAttributeString(ScOrder scOrder, ScServiceDetail scServiceDetail,
			NetworkUniqueProductCreation uniqueProductCreation) {
		NetworkProductAttributeString networkProductAttributeString = null;
		networkProductAttributeString = new NetworkProductAttributeString();
		
		networkProductAttributeString.setSiteACity("");
		networkProductAttributeString.setSiteAState("");
		networkProductAttributeString.setSiteACountry("");
		networkProductAttributeString.setSourceLocation("");
		networkProductAttributeString.setSiteZCity("");
		networkProductAttributeString.setSiteZState("");
		networkProductAttributeString.setSiteZCountry("");
		networkProductAttributeString.setDestinationLocation("");
					 
		networkProductAttributeString.setBandwidth("");
		networkProductAttributeString.setMaxBandwidth("");
		networkProductAttributeString.setUsageModel("");	
		networkProductAttributeString.setSiteEnd("D");
		networkProductAttributeString.setTigerId("NA");
		networkProductAttributeString.setOpportunityClassification(NetworkConstants.SELL_TO);
		networkProductAttributeString.setIsPartner("");
		networkProductAttributeString.setPartnerName("");
		networkProductAttributeString.setCopfId(scOrder.getOpOrderCode());
		networkProductAttributeString.setParentId("");
		networkProductAttributeString.setParentService("");//NetworkConstants.SERVICE_NAME);
		networkProductAttributeString.setParentServiceName("");//NetworkConstants.SERVICE_NAME);
		networkProductAttributeString.setOpportunityId(StringUtils.trimToEmpty(scOrder.getTpsSfdcOptyId()));
		networkProductAttributeString.setPopIdSiteA("");
		networkProductAttributeString.setPopGeoCode("");
		networkProductAttributeString.setMrcPrice("");
		networkProductAttributeString.setServiceType(uniqueProductCreation.getBillingType());
		networkProductAttributeString.setBillingType("S");
		networkProductAttributeString.setServiceTypeClubbing("");
		networkProductAttributeString.setOrderType(NetworkConstants.NEW);
		networkProductAttributeString.setBundled(uniqueProductCreation.getProductName().contains("Additional IP")
				? scServiceDetail.getAdditionalIpPoolType()
				: "");
		networkProductAttributeString.setCircuitId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setServiceId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setComponentId("");
		networkProductAttributeString.setPoLineItemDescription("");
		networkProductAttributeString.setPoLineItemDescription("");
		networkProductAttributeString.setPoStartDate("");
		networkProductAttributeString.setPoEndDate("");
		networkProductAttributeString.setTaxCompletionDate("");
		networkProductAttributeString.setViznetId(NetworkConstants.GVPN.equals(uniqueProductCreation.getServiceType())
				? uniqueProductCreation.getScServiceDetail().getUuid()
				: "");
		networkProductAttributeString.setDmsWorkFlowId("");
		networkProductAttributeString.setAssignDate("");
		networkProductAttributeString.setUserName("");
		networkProductAttributeString.setIsPartner("");
		networkProductAttributeString.setPartnerReference("");
		networkProductAttributeString.setPartnerEntity("");
		networkProductAttributeString.setPartnerName("");
		networkProductAttributeString.setReferralFromPartner("");
		networkProductAttributeString.setReferralToPartner("");
		networkProductAttributeString.setEndCustomerName("");
		networkProductAttributeString.setSiteLocationId("");
		networkProductAttributeString.setServiceTypeFlavor("");
		networkProductAttributeString.setSiteGstNo("");
		networkProductAttributeString.setContractGstNo("");
		networkProductAttributeString.setWarehouseState("");
		networkProductAttributeString.setDispatchDate("");
		networkProductAttributeString.setBandwidth("");
		networkProductAttributeString.setPoNumber(StringUtils.trimToEmpty(uniqueProductCreation.getPoNumber()));
		networkProductAttributeString.setPoDate(StringUtils.isNotEmpty(uniqueProductCreation.getPoDate())
				? TimeStampUtil.commDateforAttributeString(uniqueProductCreation.getPoDate())
				: "");
		networkProductAttributeString.setGstProductCode(uniqueProductCreation.getHsnCode());
		networkProductAttributeString.setBandwidth("");
		
		/*if (NetworkConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			networkProductAttributeString.setMakeAndModel(
					NetworkConstants.CISCO.concat(uniqueProductCreation.getCpeModel()));
			networkProductAttributeString.setCpeOutright(NetworkConstants.OUTRIGHT);
			networkProductAttributeString.setWarehouseState(uniqueProductCreation.getWareHouseState());
			networkProductAttributeString.setDispatchDate(uniqueProductCreation.getCpeDispatchDate());
			networkProductAttributeString.setCpeSerialNumber(uniqueProductCreation.getCpeSerialNo());
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
		}*/
		
		networkProductAttributeString.setCommissioningDate(TimeStampUtil.convertWithTimeStampToWithoutTimeStamp(uniqueProductCreation.getCommissioningDate()));
		
		/*if (uniqueProductCreation.getProductName().contains("Burstable")) {
			networkProductAttributeString.setBaseBandwidth(uniqueProductCreation.getBaseBandwidth());
			networkProductAttributeString.setMaxBandwidth(uniqueProductCreation.getMaxBandwidth());
			if (NetworkConstants.GVPN.equals(uniqueProductCreation.getServiceType())) {
				networkProductAttributeString.setPortBandwidth(uniqueProductCreation.getMaxBandwidth());
			} 
			networkProductAttributeString.setUsageModel(uniqueProductCreation.getUsageModel());
			networkProductAttributeString.setBusinessArea(networkProductAttributeString.getSiteZCity());
			networkProductAttributeString.setLocation(networkProductAttributeString.getSiteZCity());
		}*/
		
		return networkProductAttributeString.toString();
	}

}
