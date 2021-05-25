package com.tcl.dias.servicehandover.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestAckBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ResponseHeader;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.NetworkProductAttributeString;
import com.tcl.dias.servicehandover.util.NetworkUniqueProductCreation;
import com.tcl.dias.servicehandover.util.PopDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;


/**
 * Service Class for Network Product Commissioning
 * 
 * @author yomagesh
 *
 */
@Service
public class BillingProductCreationServiceOld {

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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingProductCreationService.class);

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
	
	/**
	 * Method for Product Commissioning
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param serviceId 
	 * @return
	 */
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrderResponse productCreation(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId, String siteType) {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		Set<String> accountList = chargeLineitemRepository.findByTotalAccountNumbers(serviceId, serviceType);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		List<Future<CreateOrderResponse>> list = new ArrayList<Future<CreateOrderResponse>>();
		if(accountList!=null) {
			LOGGER.info("Total no of accounts available for the service id {} ", accountList.size());
			accountList.forEach(accountno->{
				LOGGER.info("Account number picked for the product creation is {} ", accountno);
				List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository.findByServiceIdAndServiceTypeAndCommissioningFlag(serviceId,
						serviceType,accountno,siteType);
				CreateOrderBO createOrderBO = new CreateOrderBO();
				String groupId = generateProductGroupId(scOrder.getOpOrderCode(), AppEnv, appHost);
				chargeLineitems.forEach(chargeLineitem -> {
					ServicehandoverAudit audit = new ServicehandoverAudit();
					audit = servicehandoverAuditRepo.save(audit);
					NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
					uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
					uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
					uniqueProductCreation.setMrc(Float.parseFloat(chargeLineitem.getMrc()));
					uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
					uniqueProductCreation.setArc(Float.parseFloat(chargeLineitem.getArc()));
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
					uniqueProductCreation.setBillingType(chargeLineitem.getBillingMethod());
					if(NetworkConstants.NPL.equals(scServiceDetail.getErfPrdCatalogProductName())) {
						uniqueProductCreation.setServiceType(
								StringUtils.isNotEmpty(chargeLineitem.getBillingType())? chargeLineitem.getBillingType()
										: NetworkConstants.ACC_NPL_INTRACITY);
						uniqueProductCreation.setSiteType(chargeLineitem.getSiteType());
					}
					/*if (NetworkConstants.NPL.equals(scServiceDetail.getErfPrdCatalogProductName())) {
						Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
								.getComponentAttributesDetails(
										Arrays.asList("serviceSubType"),Integer.parseInt(serviceId), "LM", "A");
						uniqueProductCreation.setSiteType(chargeLineitem.getSiteType());
						String serviceSubType = scComponentAttributesAMap.get("serviceSubType");
						if(NetworkConstants.NPL_INTRACITY.equals(serviceSubType)) {
							uniqueProductCreation.setServiceType(NetworkConstants.ACC_NPL_INTRACITY);
						}else {
							uniqueProductCreation.setServiceType(NetworkConstants.ACC_NPL_INTERCITY);
						}
					}*/
					uniqueProductCreation.setSourceProductSeq(audit.getId().toString());
					uniqueProductCreation.setServicehandoverAudit(audit);
					String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
					uniqueProductCreation.setOptimusOrderType(orderType);					
					if (uniqueProductCreation.getProductName().contains("Burstable")) {
						Double usageMrc = Double.parseDouble(chargeLineitem.getUsageArc()) / 12;
						uniqueProductCreation.setUsageArc(usageMrc.toString());
					}
					chargeLineitem.setCommissioningFlag("Y");
					chargeLineitem.setSourceProdSequence(audit.getId().toString());
					chargeLineitem.setActionType(NetworkConstants.CREATE);
					chargeLineitemRepository.save(chargeLineitem);
					chargeLineitemRepository.flush();
					 CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation);
					 createOrderBO.getReqOrder().add(createOrderRequestBO);
					 
				});
				CreateProductPerAccount createProductPerAccount = new CreateProductPerAccount(createOrderBO,
						orderSoapConnector, createOrderOperation);
				Future<CreateOrderResponse> future = executorService.submit(createProductPerAccount);
				list.add(future);
			});
			
		}
		list.forEach(future -> {
			String status;
			try {
				LOGGER.info("Status of the products creation is {} ", future.get().getCreateOrderRequestOutput().getAcknowledge().getStatus());
				status = future.get().getCreateOrderRequestOutput().getAcknowledge().getStatus();
				if (status != null && status.equals("Success")) {
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
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		});
		return createOrderResponse;
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
		
		String billStartDate="";
		String iasCommDate="";
		String cpeWareHouseCity = "";
		String cpeWareHouseState = "";
		String billActiveStartDate="";
		
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
		if (uniqueProductCreation.getBillingType() != null
				&& uniqueProductCreation.getBillingType().equalsIgnoreCase("arrear")) {
			commonBo.setAdvanceBoo(NetworkConstants.F);
		} else {
			commonBo.setAdvanceBoo(NetworkConstants.T);
		}
		commonBo.setServiceType(uniqueProductCreation.getServiceType());
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
				Arrays.asList("taxExemption", NetworkConstants.CPE_SERIAL_NO, NetworkConstants.CPE_DISPACTH_DATE,
						NetworkConstants.CPE_WAREHOUSE_CITY, NetworkConstants.CPE_WAREHOUSE_STATE,
						NetworkConstants.BILL_START_DATE, NetworkConstants.IAS_PROD_COMM_DATE,
						LeAttributesConstants.PO_DATE, LeAttributesConstants.PO_NUMBER, NetworkConstants.EVENT_SOURCE,
						NetworkConstants.PORT_BANDWIDTH, NetworkConstants.BURSTABLE_BANDWIDTH,
						NetworkConstants.BURSTABLE_BANDWIDTH_UOM,"isBurstableProductTerminated"),
				scServiceDetail.getId(), "LM", "A");
		
		if (uniqueProductCreation.getServiceType().contains(NetworkConstants.NPL)) {
			ScComponentAttribute siteTaxExemption = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), "TAX_EXCEMPTED_FILENAME", "LM",uniqueProductCreation.getSiteType());
			if (siteTaxExemption != null && StringUtils.isNotBlank(siteTaxExemption.getAttributeValue())) { 
				productCreationInputBO.setTaxExemptTxt("yes");
				productCreationInputBO.setTaxExemptRef("Customer in SEZ");
			}
			
		}else {
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
		}
		 			
		List<ScContractInfo> scContractInfoList = scContractInfoRepository.findByScOrder_id(scOrder.getId());

		scContractInfoList.forEach((scContractInfo) -> {
			commonBo.setPaymentDueDate(scContractInfo.getPaymentTerm());
			productCreationInputBO.setCustomerName(scContractInfo.getErfCustLeName());

			productCreationInputBO.setContractDuration(scContractInfo.getOrderTermInMonths().toString());
			productCreationInputBO.setCustomerOrderNum(scOrder.getOpOrderCode());
		});

		
		productCreationInputBO.setCONTRACTINGADDRESS(addressConstructor.contractAddress(customerAddressLocationId));
		productCreationInputBO.setTotalArc(uniqueProductCreation.getArc().toString());
		productCreationInputBO.setTotalNrc(uniqueProductCreation.getNrc().toString());
		productCreationInputBO.setOrderType(uniqueProductCreation.getOrderType());
		productCreationInputBO.setChangeOrderType(uniqueProductCreation.getChangeOrderType());

		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		if (NetworkConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			cpeWareHouseCity=StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.CPE_WAREHOUSE_CITY));
			cpeWareHouseState=StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.CPE_WAREHOUSE_STATE));
			String cpeDispatchDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.CPE_DISPACTH_DATE));
			if(StringUtils.isBlank(cpeDispatchDate)) {
				uniqueProductCreation.setCpeDispatchDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
			}else {
				uniqueProductCreation.setCpeDispatchDate(TimeStampUtil.commDateforAttributeString(cpeDispatchDate));
			}
			
			uniqueProductCreation.setCpeSerialNo(StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.CPE_SERIAL_NO)));
			
			productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
			productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
			productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		} else {
			billStartDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.BILL_START_DATE));
			iasCommDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(NetworkConstants.IAS_PROD_COMM_DATE));

			productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStampForComm(billStartDate));
			productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStampForComm(billStartDate));
			productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStampForComm(billStartDate));

		}
		
		uniqueProductCreation.setPoNumber(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_NUMBER,"")));
		uniqueProductCreation.setPoDate(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_DATE,"")));
	
		productCreationInputBO.setSITEEND(NetworkConstants.B.equals(uniqueProductCreation.getSiteType())?NetworkConstants.Z:NetworkConstants.A);

		productCreationInputBO.setCustomerType(NetworkConstants.EXISTING);
		productCreationInputBO.setCiruitCount(NetworkConstants.ONE);

		productCreationInputBO.setCpsName("");
		productCreationInputBO.setInvocingCoNamePrd(NetworkConstants.INVOICING_CONAME);
		productCreationInputBO.setVatIdentifier("");

		productCreationInputBO.setBusinessUnit(NetworkConstants.TATA);

		addressConstructor.productAddress(scServiceDetail,productCreationInputBO);

		if (BillingConstants.CISCO_WEBEX.equals(uniqueProductCreation.getBillingType())) {
			productCreationInputBO.setSITEGSTINADDRESS("");
			productCreationInputBO.setSITEGSTINNO("");
		}else {
			String siteGstAddress= StringUtils.trimToEmpty(addressConstructor.siteGstAddress(scServiceDetail,uniqueProductCreation.getServiceType(),"A"));
			String siteType = StringUtils.isNotEmpty(uniqueProductCreation.getSiteType()) && "B".equals(uniqueProductCreation.getSiteType()) ? "B" :"A";
			Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("siteGstNumber"), scServiceDetail.getId(), "LM", siteType);
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
		
		productCreationInputBO.setCpeAccountNums(uniqueProductCreation.getAccountId());
		productCreationInputBO.setCpeDeliveryAdds(scServiceDetail.getSiteAddress());
		productCreationInputBO.setSITEAADDRESS(addressConstructor.siteAddressGen(scServiceDetail,"A"));
		if (uniqueProductCreation.getServiceType().contains(NetworkConstants.NPL)) {
			productCreationInputBO.setSITEBADDRESS(addressConstructor.siteAddressGen(scServiceDetail,"B"));
			
		}
		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setScenarioType("");
		productCreationInputBO.setChargingRateValue("");
		
		if (uniqueProductCreation.getProductName().contains("Burstable")){
			productCreationInputBO.setEventSource(scComponentAttributesAMap.get(NetworkConstants.EVENT_SOURCE));
			productCreationInputBO.setEventsourceLabel(scComponentAttributesAMap.get(NetworkConstants.EVENT_SOURCE));
			productCreationInputBO.setEventSourceText(scComponentAttributesAMap.get(NetworkConstants.EVENT_SOURCE));
			if (NetworkConstants.IAS.equals(uniqueProductCreation.getServiceType())) {
				productCreationInputBO.setCostBandName("ILL Default CB");
				productCreationInputBO.setTerminateName("ILL Default TR");
				productCreationInputBO.setEventClassName("ILL Default EC");
				productCreationInputBO.setChargeSegmentName("ILL Constant CS");
			}
			if (NetworkConstants.GVPN.equals(uniqueProductCreation.getServiceType())) {
				productCreationInputBO.setCostBandName("Burstable GVPN Standard CB");
				productCreationInputBO.setTerminateName("Burstable GVPN standard TR");
				productCreationInputBO.setEventClassName("Burstable GVPN Standard");
				productCreationInputBO.setChargeSegmentName("Burstable GVPN Constant CS");
				productCreationInputBO.setProductName("BURSTABLE GVPN");
			}
			uniqueProductCreation.setBaseBandwidth(scComponentAttributesAMap.get(NetworkConstants.PORT_BANDWIDTH)
					.concat(" ").concat(scComponentAttributesAMap.get(NetworkConstants.BURSTABLE_BANDWIDTH_UOM)));
			uniqueProductCreation.setMaxBandwidth(scComponentAttributesAMap.get(NetworkConstants.BURSTABLE_BANDWIDTH)
					.concat(" ").concat(scComponentAttributesAMap.get(NetworkConstants.BURSTABLE_BANDWIDTH_UOM)));
			uniqueProductCreation.setUsageModel(NetworkConstants.USAGE_MODEL); 
			productCreationInputBO.setChargingRateValue(uniqueProductCreation.getUsageArc());
			if (uniqueProductCreation.getOptimusOrderType() != null
					&& NetworkConstants.MACD.equals(uniqueProductCreation.getOptimusOrderType())
					&& scComponentAttributesAMap.get("isBurstableProductTerminated") != null
					&& "Yes".equals(scComponentAttributesAMap.get("isBurstableProductTerminated"))) {
				productCreationInputBO.setScenarioType("Nco_Usage");
			}
			
		}
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

		
		productCreationInputBO.setOvrdnPeriodicPrice(uniqueProductCreation.getArc().toString());
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

		if (uniqueProductCreation.getServiceType().equals(NetworkConstants.CPE)) {
			productCreationInputBO.setWarehouseAddr("WAREHOUSE_CITY=" + cpeWareHouseCity + ";WAREHOUSE_STATE=" +cpeWareHouseState + ";");
			uniqueProductCreation.setWareHouseState(cpeWareHouseState);
		} else
			productCreationInputBO.setWarehouseAddr("");

		productCreationInputBO
				.setAttributeString(generateProductAttributeString(scOrder, scServiceDetail, uniqueProductCreation));
		
		if(uniqueProductCreation.getIsMacd())
		{
			Map<String, String> atMap = new HashMap<>();
			productCreationInputBO.setSourceOldProdSeq(uniqueProductCreation.getSourceProductSeq());
			if (uniqueProductCreation.isParallelMacd() && uniqueProductCreation.getParallelRunDays() > 0) {
				productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(uniqueProductCreation.getTermEndDate(), uniqueProductCreation.getParallelRunDays()));
			} else {
				productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStampForCommMinusDays(uniqueProductCreation.getTermEndDate(), 1));
				//atMap.put("terminationDate", TimeStampUtil.optimusDateFormat(TimeStampUtil.formatWithTimeStampForCommMinusDays(uniqueProductCreation.getTermEndDate(),1)));
			}
			productCreationInputBO.setTermReason(NetworkConstants.CHANGE_BANDWIDTH);
			productCreationInputBO.setDepositRefundBoo(NetworkConstants.F);
			if(uniqueProductCreation.getProductName().contains("Burstable") && uniqueProductCreation.getMacdServiceId()!=null) {
				atMap.put("isBurstableProductTerminated", "Yes");
			}
			componentAndAttributeService.updateAttributes(Integer.parseInt(uniqueProductCreation.getMacdServiceId()), atMap, AttributeConstants.COMPONENT_LM, "A");
			
			
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
		
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		
		if (uniqueProductCreation.getServiceType().contains(NetworkConstants.NPL)) {
			scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("sourceCity", "sourceState", "sourceCountry", "destinationCity", "destinationState",
							"destinationCountry", "popSiteCode", "portBandwidth", "localLoopBandwidth","commissioningDate","bwUnit","localLoopBandwidthUnit"),
					scServiceDetail.getId(), "LM", "A");
			Map<String, String> scComponentAttributesBMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("destinationCity", "destinationState", "destinationCountry",
							"destinationAddressLineOne", "destinationAddressLineTwo", "destinationLocality",
							"destinationPincode", "popSiteCode", "portBandwidth", "localLoopBandwidth"),
					scServiceDetail.getId(), "LM", "B");
			networkProductAttributeString.setSiteACity(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
			networkProductAttributeString.setSiteAState(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
			networkProductAttributeString.setSiteACountry(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			networkProductAttributeString.setSourceLocation(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			
			networkProductAttributeString.setSiteZCity(StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCity")));
			networkProductAttributeString.setSiteZState(StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState")));
			networkProductAttributeString.setSiteZCountry(StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));
			networkProductAttributeString.setDestinationLocation(StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));
			//addressConstructor.siteBAddressGen(networkProductAttributeString,scComponentAttributesBMap);
			
		} else {
			scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("sourceCity", "sourceState", "sourceCountry", "destinationCity", "destinationState",
							"destinationCountry", "popSiteCode", "portBandwidth", "localLoopBandwidth","commissioningDate","bwUnit","localLoopBandwidthUnit"),
					scServiceDetail.getId(), "LM", "A");
			networkProductAttributeString.setSiteACity(StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceCity")));
			networkProductAttributeString.setSiteAState(StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceState")));
			networkProductAttributeString.setSiteACountry(StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceCountry")));
			networkProductAttributeString.setSourceLocation(StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceCountry")));
			networkProductAttributeString.setSiteZCity(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
			networkProductAttributeString.setSiteZState(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
			networkProductAttributeString.setSiteZCountry(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			networkProductAttributeString.setDestinationLocation(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));

		}
					 
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
		networkProductAttributeString.setPopIdSiteA(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setPopGeoCode(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setMrcPrice(StringUtils.trimToEmpty(uniqueProductCreation.getMrc().toString()));
		// networkProductAttributeString.setARC_PRICE(uniqueProductCreation.getArc().toString());
		networkProductAttributeString.setServiceType(uniqueProductCreation.getServiceType());
		networkProductAttributeString.setBillingType("S");
		networkProductAttributeString.setServiceTypeClubbing(StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
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
		
		if (NetworkConstants.FIXED_PORT_IAS.equals(uniqueProductCreation.getProductName())
				|| NetworkConstants.FIXED_PORT_GVPN.equals(uniqueProductCreation.getProductName())
				|| NetworkConstants.NPL_BANDWIDTH_CHARGES.equals(uniqueProductCreation.getProductName())) {
			String portBwUnit = scComponentAttributesAMap.get("bwUnit")!=null ? scComponentAttributesAMap.get("bwUnit") :"Mbps";
			String portBandwidth= scComponentAttributesAMap.get("portBandwidth");
			if (scComponentAttributesAMap.get("portBandwidth").equals("0.125")) {
				portBandwidth = "128";
				portBwUnit = "Kbps";
			}
			if (scComponentAttributesAMap.get("portBandwidth").equals("0.25")) {
				portBandwidth = "256";
				portBwUnit = "Kbps";
			}
			if (scComponentAttributesAMap.get("portBandwidth").equals("0.5")) {
				portBandwidth = "512";
				portBwUnit = "Kbps";
			}
			networkProductAttributeString.setBandwidth(portBandwidth +" "+portBwUnit);
			
		}
		if (NetworkConstants.LOCAL_ACCESS.equals(uniqueProductCreation.getProductName())
				|| NetworkConstants.NPL_CONNECTION_CHARGES.equals(uniqueProductCreation.getProductName())) {
			if(StringUtils.isNotEmpty(scComponentAttributesAMap.get("localLoopBandwidth"))) {
				String loopBwUnit = scComponentAttributesAMap.get("localLoopBandwidthUnit")!=null ? scComponentAttributesAMap.get("localLoopBandwidthUnit") :"Mbps";
				String loopBandwidth = scComponentAttributesAMap.get("localLoopBandwidth");
				if (scComponentAttributesAMap.get("localLoopBandwidth").equals("0.125")) {
					loopBandwidth = "128";
					loopBwUnit = "Kbps";
				}
				if (scComponentAttributesAMap.get("localLoopBandwidth").equals("0.25")) {
					loopBandwidth = "256";
					loopBwUnit = "Kbps";
				}
				if (scComponentAttributesAMap.get("localLoopBandwidth").equals("0.5")) {
					loopBandwidth = "512";
					loopBwUnit = "Kbps";
				}
				networkProductAttributeString.setBandwidth(loopBandwidth+" "+loopBwUnit);
			}
		}
		if (NetworkConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			networkProductAttributeString.setMakeAndModel(
					NetworkConstants.CISCO.concat(uniqueProductCreation.getCpeModel()));
			networkProductAttributeString.setCpeOutright(NetworkConstants.OUTRIGHT);
			networkProductAttributeString.setWarehouseState(uniqueProductCreation.getWareHouseState());
			networkProductAttributeString.setDispatchDate(uniqueProductCreation.getCpeDispatchDate());
			networkProductAttributeString.setCpeSerialNumber(uniqueProductCreation.getCpeSerialNo());
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
		}else {
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.commDateforAttributeString(scComponentAttributesAMap.get("commissioningDate")));
		}
		if (uniqueProductCreation.getProductName().contains("Burstable")) {
			networkProductAttributeString.setBaseBandwidth(uniqueProductCreation.getBaseBandwidth());
			networkProductAttributeString.setMaxBandwidth(uniqueProductCreation.getMaxBandwidth());
			if (NetworkConstants.GVPN.equals(uniqueProductCreation.getServiceType())) {
				networkProductAttributeString.setPortBandwidth(uniqueProductCreation.getMaxBandwidth());
			} 
			networkProductAttributeString.setUsageModel(uniqueProductCreation.getUsageModel());
			networkProductAttributeString.setBusinessArea(networkProductAttributeString.getSiteZCity());
			networkProductAttributeString.setLocation(networkProductAttributeString.getSiteZCity());
		}
		
		return networkProductAttributeString.toString();
	}

	public String insertSeq(String serviceType) {
		List<ScChargeLineitem> scChargeLineitems = chargeLineitemRepository.findByServiceType(serviceType);
		scChargeLineitems.forEach(lineitem -> {
			String seq = gnvOrderEntryTabRepository.findSourceProdSeq(lineitem.getAccountNumber(),
					lineitem.getChargeLineitem());
			LOGGER.info("Account number: {} Product {} with Sequence {}" + lineitem.getAccountNumber(),
					lineitem.getChargeLineitem(), seq);
			if (seq != null && lineitem.getSourceProdSequence() == null) {
				lineitem.setSourceProdSequence(seq);
				lineitem.setCommissioningFlag("Y");
				chargeLineitemRepository.save(lineitem);
			}
		});
		return "SUCCESS";

	}

	public String generateProductGroupId(String orderCode, String appEnv, String appHost) {
		String groupId = NetworkConstants.OPT_PROD.concat(orderCode) + "_" + System.currentTimeMillis();
		if ("DEV".equalsIgnoreCase(AppEnv)) {
			if (appHost.contains(".34")) {
				groupId.concat("-34");
			} else if (appHost.contains(".40")) {
				groupId.concat("-40");
			} else if (appHost.contains(".146")) {
				groupId.concat("-146");
			}
		}
		return groupId;
	}
}
