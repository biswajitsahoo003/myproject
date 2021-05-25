package com.tcl.dias.servicehandover.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
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
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
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
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonAttributesBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestAckBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ResponseHeader;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.AddressConstructorIntl;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.NetworkProductAttributeString;
import com.tcl.dias.servicehandover.util.NetworkUniqueProductCreation;
import com.tcl.dias.servicehandover.util.PopDetails;
import com.tcl.dias.servicehandover.util.TeamsDrProductCode;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.servicehandover.entity.GnvOrderEntryTab;


/**
 * Service Class for Network Product Commissioning
 * 
 * @author yomagesh
 *
 */
@Service
public class BillingProductCreationService {

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
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Value("${app.host}")
	private String appHost;
	
	@Value("${application.env:PROD}")
	String AppEnv;
	
	//@Autowired
	//AddressConstructorIntl addressConstructorIntl;
	@Autowired
	AddressConstructorIntl addressConstructorIntl;

		
	
	/**
	 * Method for Product Commissioning
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceCode
	 * @param serviceType
	 * @param serviceId 
	 * @param siteType 
	 * @return
	 */
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
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
				CreateOrderBO createOrderBO = new CreateOrderBO();
				LOGGER.info("Commissioning Products for Order {} and service {}", orderId, serviceCode);
				List<ScChargeLineitem> chargeLineitems = null;
				if (BillingConstants.NPL.equals(serviceType)) {
					chargeLineitems = chargeLineitemRepository.findByServiceIdAndServiceTypeAndCommissioningFlagNPL(
							serviceId, BillingConstants.NPL, accountno);
				} else {
					chargeLineitems = chargeLineitemRepository.findByServiceIdAndServiceTypeAndCommissioningFlag(
							serviceId, getServiceType(serviceType), accountno, siteType);
				}
				String groupId = generateProductGroupId(scOrder.getOpOrderCode(), AppEnv, appHost);
				List<String> commissioniedList = gnvOrderEntryTabRepository.checkForDuplicateProducts(serviceCode, orderId, serviceType,String.valueOf(chargeLineitems.size()));
				boolean isFailed = commissioniedList.stream().anyMatch(status -> BillingConstants.FAILURE.equals(status));
				LOGGER.info("Commissioning {} Products for service {} and previous commissioning status is {}", chargeLineitems.size(), serviceCode,isFailed);
				if (commissioniedList.isEmpty() || isFailed) {
					chargeLineitems.forEach(chargeLineitem -> {
						ServicehandoverAudit audit = new ServicehandoverAudit();
						NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
						uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
						uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
						uniqueProductCreation.setMrc(Float.parseFloat(chargeLineitem.getMrc()));
						uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
						uniqueProductCreation.setArc(Float.parseFloat(chargeLineitem.getArc()));
						uniqueProductCreation.setScOrder(scOrder);
						uniqueProductCreation.setScServiceDetail(scServiceDetail);
						uniqueProductCreation.setProcessInstanceId(processInstanceId);
						uniqueProductCreation.setActionType(BillingConstants.CREATE);
						uniqueProductCreation.setOrderType(BillingConstants.NEW);
						uniqueProductCreation.setChangeOrderType(BillingConstants.NEW_ORDER);
						uniqueProductCreation.setQuantity(
								StringUtils.isNotEmpty(chargeLineitem.getQuantity()) ? chargeLineitem.getQuantity()
										: BillingConstants.ONE);
						if (BillingConstants.ADDITIONAL_IP.equals(chargeLineitem.getChargeLineitem())) {
							uniqueProductCreation.setQuantity(BillingConstants.ONE);
						} else {
							uniqueProductCreation.setQuantity(
									StringUtils.isNotEmpty(chargeLineitem.getQuantity()) ? chargeLineitem.getQuantity()
											: BillingConstants.ONE);
						}
						uniqueProductCreation.setComponent(chargeLineitem.getComponent());
						uniqueProductCreation.setCpeModel(StringUtils.trimToEmpty(chargeLineitem.getCpeModel()));
						uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
						uniqueProductCreation.setIsMacd(false);
						uniqueProductCreation.setInputGroupId(groupId);
						uniqueProductCreation.setBillingMethod(chargeLineitem.getBillingMethod());
						uniqueProductCreation.setBillingType(chargeLineitem.getBillingType());
						uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
						uniqueProductCreation.setDescription(chargeLineitem.getComponentDesc());
						uniqueProductCreation.setSkuId(chargeLineitem.getSkuId());
						uniqueProductCreation.setCustomerRef(chargeLineitem.getCustomerRef());
						if (BillingConstants.NPL.equals(scServiceDetail.getErfPrdCatalogProductName())) {
							uniqueProductCreation.setSiteType(chargeLineitem.getSiteType());
						}
						if (StringUtils.isEmpty(chargeLineitem.getSourceProdSequence())) {
							audit = servicehandoverAuditRepo.save(audit);
							uniqueProductCreation.setSourceProductSeq(audit.getId().toString());
							chargeLineitem.setSourceProdSequence(audit.getId().toString());
						} else {
							uniqueProductCreation.setSourceProductSeq(chargeLineitem.getSourceProdSequence());
						}
						uniqueProductCreation.setServicehandoverAudit(audit);
						String orderType = OrderCategoryMapping.getOrderType(scServiceDetail,
								scServiceDetail.getScOrder());
						uniqueProductCreation.setOptimusOrderType(orderType);
						if (uniqueProductCreation.getProductName().contains("Burstable")) {
							Double usageMrc = Double.parseDouble(chargeLineitem.getUsageArc()) / 12;
							uniqueProductCreation.setUsageArc(usageMrc.toString());
							LOGGER.info("Burstable Charge for {} is {}", uniqueProductCreation.getProductName(),
									usageMrc);
						}

						Double effectiveUsage = Double.valueOf(
								chargeLineitem.getEffectiveUsage() != null ? chargeLineitem.getEffectiveUsage() : "0");
						Double effectiveOverage = Double.valueOf(
								chargeLineitem.getEffectiveOverage() != null ? chargeLineitem.getEffectiveOverage()
										: "0");

						if (uniqueProductCreation.getProductName().contains("Usage") || effectiveUsage > 0) {
							uniqueProductCreation.setEffectiveUsage(chargeLineitem.getEffectiveUsage());
							LOGGER.info("Usage Charge for {} is {}", uniqueProductCreation.getProductName(),
									uniqueProductCreation.getEffectiveUsage());
						}
						if (uniqueProductCreation.getProductName().contains("Overage") || effectiveOverage > 0) {
							uniqueProductCreation.setEffectiveOverage(chargeLineitem.getEffectiveOverage());
							LOGGER.info("Overage Charge for {} is {}", uniqueProductCreation.getProductName(),
									uniqueProductCreation.getEffectiveOverage());
						}
						chargeLineitem.setCommissioningFlag("Y");
						chargeLineitem.setActionType(BillingConstants.CREATE);
						chargeLineitemRepository.save(chargeLineitem);
						chargeLineitemRepository.flush();

						CreateOrderRequestBO createOrderRequestBO = createUniqueProduct(uniqueProductCreation);
						createOrderBO.getReqOrder().add(createOrderRequestBO);
					});
				} else {
					LOGGER.info("Products for Order {} and service {} already exist in Geneva", orderId, serviceCode);
				}
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
		
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributesDetails(
						Arrays.asList(BillingConstants.TAX_EXEMPTION, BillingConstants.CPE_SERIAL_NO,
								BillingConstants.CPE_DISPACTH_DATE, BillingConstants.CPE_WAREHOUSE_CITY,
								BillingConstants.CPE_WAREHOUSE_STATE, BillingConstants.PROD_BILL_START_DATE,
								BillingConstants.PROD_COMM_DATE, LeAttributesConstants.PO_DATE,
								LeAttributesConstants.PO_NUMBER, BillingConstants.EVENT_SOURCE,
								BillingConstants.PORT_BANDWIDTH, BillingConstants.BURSTABLE_BANDWIDTH,
								BillingConstants.BURSTABLE_BANDWIDTH_UOM, BillingConstants.IS_BURSTABLE_TERMINATED,
								BillingConstants.IS_INTL_BILLING, BillingConstants.GEO_CODE,BillingConstants.ADMIN_ACCESS,
								BillingConstants.DEMO_BILL_START_DATE, BillingConstants.DEMO_BILL_END_DATE),
						scServiceDetail.getId(), "LM", "A");
		
		boolean isIntlBilling = scComponentAttributesAMap.get(BillingConstants.IS_INTL_BILLING) != null
				&& "Y".equals(scComponentAttributesAMap.get(BillingConstants.IS_INTL_BILLING)) ? true : false;
		
		boolean isDemoOrder = scOrder.getDemoFlag()!=null && "Y".equals(scOrder.getDemoFlag()) ? true : false;
		
		LOGGER.info("Billing will be done for {} Entity", isIntlBilling ? BillingConstants.INTL_CUSTOMER : BillingConstants.DOMESTIC_CUSTOMER);
		uniqueProductCreation.setBillingInternational(isIntlBilling);
		
		String wareHouseCity = "";
		String wareHouseState = "";
		String dispatchDate ="";
		String serialNumber="";
		String prodBillStartDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.PROD_BILL_START_DATE));
		String prodCommDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.PROD_COMM_DATE));
	
		CommonBO commonBo = new CommonBO();
		commonBo.setGroupId(uniqueProductCreation.getInputGroupId());
		commonBo.setSourceSystem(BillingConstants.SOURCE_SYSTEM);
		commonBo.setRequestType(BillingConstants.PRODUCT);
		commonBo.setActionType(uniqueProductCreation.getActionType());
		commonBo.setCustomerRef(uniqueProductCreation.getCustomerRef());
		commonBo.setAccountNum(uniqueProductCreation.getAccountId());
		commonBo.setInvoicingCoName(isIntlBilling ? BillingConstants.INVOICING_CONAME_INTL :BillingConstants.INVOICING_CONAME);
		commonBo.setBillingEntity(BillingConstants.BILLING_ENTITY);
		commonBo.setProviderSegment(BillingConstants.PROVIDER_SEGMENT);
		if (uniqueProductCreation.getBillingMethod() != null
				&& uniqueProductCreation.getBillingMethod().equalsIgnoreCase("arrear")) {
			commonBo.setAdvanceBoo(BillingConstants.F);
		} else {
			commonBo.setAdvanceBoo(BillingConstants.T);
		}
		commonBo.setServiceType(uniqueProductCreation.getBillingType());
		commonBo.setSourceProductSeq(uniqueProductCreation.getSourceProductSeq());

		productCreationInputBO.setProductName(uniqueProductCreation.getProductName());

		List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
				.findByScOrder_IdAndIsActive(scOrder.getId(), BillingConstants.Y);

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
					productCreationInputBO.setCONTRACTINGGSTINNO(
							BillingConstants.CPE.equals(uniqueProductCreation.getServiceType()) ? ""
									: scOrderAttribute.getAttributeValue().trim());
				}
				break;
			}

		});
		
		if (!isIntlBilling) {
			if (uniqueProductCreation.getServiceType().contains(BillingConstants.NPL)) {

				ScComponentAttribute siteTaxExemption = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								scServiceDetail.getId(), "TAX_EXCEMPTED_FILENAME", "LM",
								uniqueProductCreation.getSiteType());
				if (siteTaxExemption != null && StringUtils.isNotBlank(siteTaxExemption.getAttributeValue())) {
					productCreationInputBO.setTaxExemptTxt("yes");
					productCreationInputBO.setTaxExemptRef("Customer in SEZ");
				}

			} else {
				String taxExemption = StringUtils
						.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.TAX_EXEMPTION));
				if ("Y".equals(taxExemption.toUpperCase())) {
					productCreationInputBO.setTaxExemptTxt("yes");
					scServiceAttribute = serviceAttributeRepository.findByScServiceDetail_idAndAttributeName(
							uniqueProductCreation.getScServiceDetail().getId(),
							LeAttributesConstants.TAX_EXEMPTION_REASON);
					if (scServiceAttribute != null)
						productCreationInputBO.setTaxExemptRef(
								WordUtils.capitalize(scServiceAttribute.getAttributeValue().toLowerCase()));
				}
			}
		}
		 			
		List<ScContractInfo> scContractInfoList = scContractInfoRepository.findByScOrder_id(scOrder.getId());

		scContractInfoList.forEach((scContractInfo) -> {
			commonBo.setPaymentDueDate(scContractInfo.getPaymentTerm());
			productCreationInputBO.setCustomerName(scContractInfo.getErfCustLeName());

			productCreationInputBO.setContractDuration(scContractInfo.getOrderTermInMonths().toString());
			productCreationInputBO.setCustomerOrderNum(scOrder.getOpOrderCode());
		});

		

		productCreationInputBO.setCONTRACTINGADDRESS(isIntlBilling ? addressConstructorIntl.contractAddress(scOrder)
				: addressConstructor.contractAddress(customerAddressLocationId));
		//productCreationInputBO.setCONTRACTINGADDRESS(addressConstructor.contractAddress(customerAddressLocationId));
		productCreationInputBO.setTotalArc(uniqueProductCreation.getArc().toString());
		productCreationInputBO.setTotalNrc(uniqueProductCreation.getNrc().toString());
		productCreationInputBO.setOrderType(uniqueProductCreation.getOrderType());
		productCreationInputBO.setChangeOrderType(uniqueProductCreation.getChangeOrderType());

		productCreationInputBO.setCreationDatePrd(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		if (BillingConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			wareHouseCity = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.CPE_WAREHOUSE_CITY));
			wareHouseState = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.CPE_WAREHOUSE_STATE));
			if (BillingConstants.CPE_OUTRIGHT_CHARGE.equals(uniqueProductCreation.getProductName())) {
				dispatchDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.CPE_DISPACTH_DATE));
				serialNumber = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.CPE_SERIAL_NO));
				uniqueProductCreation.setCpeModel(uniqueProductCreation.getCpeModel());
			} else {
				ScComponent scComponent = scComponentRepository.findById(Integer.parseInt(uniqueProductCreation.getComponent())).get();
				if (scComponent != null) {
					Map<String, String> endPointAttributeMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("endpointDispatchDate", "serialNumber"), scServiceDetail.getId(),
							scComponent);
					dispatchDate = StringUtils.trimToEmpty(endPointAttributeMap.get("endpointDispatchDate"));
					serialNumber = StringUtils.trimToEmpty(endPointAttributeMap.get("serialNumber"));
				}
			}
			if (StringUtils.isBlank(dispatchDate)) {
				uniqueProductCreation.setCpeDispatchDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
			} else {
				uniqueProductCreation.setCpeDispatchDate(TimeStampUtil.commDateforAttributeString(dispatchDate));
			}

			uniqueProductCreation.setCpeSerialNo(serialNumber);
			productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
			productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
			productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));

		} else {
			if(isDemoOrder) {
				String demoBillStartDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.DEMO_BILL_START_DATE));
				productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStampForComm(demoBillStartDate));
				productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStampForComm(demoBillStartDate));
				productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStampForComm(demoBillStartDate));
			}else {
				productCreationInputBO.setCommissionDate(TimeStampUtil.formatWithTimeStampForComm(prodBillStartDate));
				productCreationInputBO.setBillActivationDate(TimeStampUtil.formatWithTimeStampForComm(prodBillStartDate));
				productCreationInputBO.setBillGenerationDate(TimeStampUtil.formatWithTimeStampForComm(prodBillStartDate));
			}
			
		}
		
		uniqueProductCreation.setPoNumber(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_NUMBER,"")));
		uniqueProductCreation.setPoDate(StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault(LeAttributesConstants.PO_DATE,"")));
	
		productCreationInputBO.setSITEEND(BillingConstants.B.equals(uniqueProductCreation.getSiteType())?BillingConstants.Z:BillingConstants.A);

		productCreationInputBO.setCustomerType(BillingConstants.EXISTING);
		productCreationInputBO.setCiruitCount(BillingConstants.ONE);

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

		addressConstructor.productAddress(scServiceDetail,productCreationInputBO);

		if (BillingConstants.CISCO_WEBEX.equals(uniqueProductCreation.getBillingType()) || isIntlBilling) {
			productCreationInputBO.setSITEGSTINADDRESS("");
			productCreationInputBO.setSITEGSTINNO("");
		}else {
			String siteType = StringUtils.isNotEmpty(uniqueProductCreation.getSiteType()) && "B".equals(uniqueProductCreation.getSiteType()) ? "B" :"A";
			String siteGstAddress= StringUtils.trimToEmpty(addressConstructor.siteGstAddress(scServiceDetail,uniqueProductCreation.getServiceType(),siteType));
			Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("siteGstNumber"), scServiceDetail.getId(), "LM", siteType);
			if (StringUtils.isNotBlank(siteGstAddress)) {
				productCreationInputBO.setSITEGSTINADDRESS(siteGstAddress);
				productCreationInputBO.setSITEGSTINNO(siteGstMap.get("siteGstNumber").trim());
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
		if (!BillingConstants.CISCO_WEBEX.equals(uniqueProductCreation.getBillingType())) {
			productCreationInputBO.setSITEAADDRESS(addressConstructor.siteAddressGen(scServiceDetail, "A"));
		}
		if (uniqueProductCreation.getServiceType().contains(BillingConstants.NPL)) {
			productCreationInputBO.setSITEBADDRESS(addressConstructor.siteAddressGen(scServiceDetail,"B"));
			
		}
		productCreationInputBO.setTermProposedDate("");
		productCreationInputBO.setTermCharges("");
		productCreationInputBO.setScenarioType("");
		productCreationInputBO.setChargingRateValue("");
		
		if (uniqueProductCreation.getProductName().contains("Burstable")){
			productCreationInputBO.setEventSource(scComponentAttributesAMap.get(BillingConstants.EVENT_SOURCE));
			productCreationInputBO.setEventsourceLabel(scComponentAttributesAMap.get(BillingConstants.EVENT_SOURCE));
			productCreationInputBO.setEventSourceText(scComponentAttributesAMap.get(BillingConstants.EVENT_SOURCE));
			if (BillingConstants.IAS.equals(uniqueProductCreation.getServiceType())) {
				productCreationInputBO.setCostBandName(BillingConstants.IAS_BURSTABLE_COST_BAND);
				productCreationInputBO.setTerminateName(BillingConstants.IAS_BURSTABLE_TERMINATE_NAME);
				productCreationInputBO.setEventClassName(BillingConstants.IAS_BURSTABLE_EVENT_CLASS_NAME);
				productCreationInputBO.setChargeSegmentName(BillingConstants.IAS_BURSTABLE_CHARGE_SEG_NAME);
			}
			if (BillingConstants.GVPN.equals(uniqueProductCreation.getServiceType())) {
				productCreationInputBO.setCostBandName(BillingConstants.GVPN_BURSTABLE_COST_BAND);
				productCreationInputBO.setTerminateName(BillingConstants.GVPN_BURSTABLE_TERMINATE_NAME);
				productCreationInputBO.setEventClassName(BillingConstants.GVPN_BURSTABLE_EVENT_CLASS_NAME);
				productCreationInputBO.setChargeSegmentName(BillingConstants.GVPN_BURSTABLE_CHARGE_SEG_NAME);
				productCreationInputBO.setProductName(BillingConstants.GVPN_GENEVA_PROD_NAME);
			}
			uniqueProductCreation.setBaseBandwidth(scComponentAttributesAMap.get(BillingConstants.PORT_BANDWIDTH)
					.concat(" ").concat(scComponentAttributesAMap.get(BillingConstants.BURSTABLE_BANDWIDTH_UOM)));
			uniqueProductCreation.setMaxBandwidth(scComponentAttributesAMap.get(BillingConstants.BURSTABLE_BANDWIDTH)
					.concat(" ").concat(scComponentAttributesAMap.get(BillingConstants.BURSTABLE_BANDWIDTH_UOM)));
			uniqueProductCreation.setUsageModel(BillingConstants.USAGE_MODEL); 
			productCreationInputBO.setChargingRateValue(uniqueProductCreation.getUsageArc());
			if (uniqueProductCreation.getOptimusOrderType() != null
					&& BillingConstants.MACD.equals(uniqueProductCreation.getOptimusOrderType())
					&& scComponentAttributesAMap.get("isBurstableProductTerminated") != null
					&& "Yes".equals(scComponentAttributesAMap.get("isBurstableProductTerminated"))) {
				productCreationInputBO.setScenarioType(BillingConstants.BURSTABLE_SCENARIO_TYPE);
			}
			
		}
		Double effectiveOverage = Double.valueOf(uniqueProductCreation.getEffectiveOverage()!=null?uniqueProductCreation.getEffectiveOverage():"0");
		Double effectiveUsage= Double.valueOf(uniqueProductCreation.getEffectiveUsage()!=null ? uniqueProductCreation.getEffectiveUsage():"0");
		
		if (effectiveUsage > 0 || effectiveOverage > 0) {
			productCreationInputBO.setEventSource(scServiceDetail.getUuid().concat("_")
					.concat(TeamsDrProductCode.getCodebyProductName(uniqueProductCreation.getProductName())));
			productCreationInputBO.setEventsourceLabel(scServiceDetail.getUuid().concat("_")
					.concat(TeamsDrProductCode.getCodebyProductName(uniqueProductCreation.getProductName())));
			productCreationInputBO.setEventSourceText(scServiceDetail.getUuid().concat("_")
					.concat(TeamsDrProductCode.getCodebyProductName(uniqueProductCreation.getProductName())));
			if (uniqueProductCreation.getProductName().equals("Remote Simple Ser Request Overage Chgs")
					|| uniqueProductCreation.getProductName().equals("Expedited Simple Service Request Charges")) {
				productCreationInputBO.setCostBandName(BillingConstants.TEAMS_DR_COST_BAND_SIMPLE);
				productCreationInputBO.setTerminateName(BillingConstants.TEAMS_DR_TERMINATE_NAME_SIMPLE);
				productCreationInputBO.setEventClassName(BillingConstants.TEAMS_DR_EVENT_CLASS_NAME_SIMPLE);
				productCreationInputBO.setChargeSegmentName(BillingConstants.TEAMS_DR_CHARGE_SEG_NAME_SIMPLE);
			}
			else if (uniqueProductCreation.getProductName().contains("Professional")) {
				productCreationInputBO.setCostBandName(BillingConstants.TEAMS_DR_COST_BAND_COMPLEX);
				productCreationInputBO.setTerminateName(BillingConstants.TEAMS_DR_TERMINATE_NAME_COMPLEX);
				productCreationInputBO.setEventClassName(BillingConstants.TEAMS_DR_EVENT_CLASS_NAME_COMPLEX);
				productCreationInputBO.setChargeSegmentName(BillingConstants.TEAMS_DR_CHARGE_SEG_NAME_COMPLEX);
			} else {

				productCreationInputBO.setCostBandName(BillingConstants.TEAMS_DR_COST_BAND);
				productCreationInputBO.setTerminateName(BillingConstants.TEAMS_DR_TERMINATE_NAME);
				productCreationInputBO.setEventClassName(BillingConstants.TEAMS_DR_EVENT_CLASS_NAME);
				productCreationInputBO.setChargeSegmentName(BillingConstants.TEAMS_DR_CHARGE_SEG_NAME);
			}
			
			productCreationInputBO.setChargingRateValue(effectiveUsage > 0 ? uniqueProductCreation.getEffectiveUsage()
							: uniqueProductCreation.getEffectiveOverage());
			
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

		Float arc = uniqueProductCreation.getArc();
		if(isIntlBilling) {
			arc=uniqueProductCreation.getMrc();
		}
		Float ovrArc = arc / Float.parseFloat(uniqueProductCreation.getQuantity());
		productCreationInputBO.setOvrdnPeriodicPrice(ovrArc.toString());
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

		productCreationInputBO.setProrateBoo(BillingConstants.T);
		productCreationInputBO.setChargePeriod(BillingConstants.ONE);

		productCreationInputBO.setTermEndDate("");
		productCreationInputBO.setTermReason("");
		productCreationInputBO.setDepositRefundBoo(BillingConstants.T);

		productCreationInputBO.setRefundBoo(BillingConstants.T);
		productCreationInputBO.setServiceId(scServiceDetail.getUuid());
		productCreationInputBO.setSiteAGeoCode("");
		if (isIntlBilling && scComponentAttributesAMap.get(BillingConstants.GEO_CODE) != null) {
			productCreationInputBO.setSiteAGeoCode(scComponentAttributesAMap.get(BillingConstants.GEO_CODE));
		}
		

		if (uniqueProductCreation.getServiceType().equals(BillingConstants.CPE)) {
			productCreationInputBO.setWarehouseAddr("WAREHOUSE_CITY=" + wareHouseCity + ";WAREHOUSE_STATE=" +wareHouseState + ";");
			uniqueProductCreation.setWareHouseState(wareHouseState);
		} else
			productCreationInputBO.setWarehouseAddr("");

		uniqueProductCreation.setCustomerCuId(scOrder.getTpsSfdcCuid());
		
		productCreationInputBO.setAttributeString(
				isIntlBilling ? generateProductAttributeStringIntl(scOrder, scServiceDetail, uniqueProductCreation)
						: generateProductAttributeString(scOrder, scServiceDetail, uniqueProductCreation));

		if(uniqueProductCreation.getIsMacd())
		{
			Map<String, String> atMap = new HashMap<>();
			productCreationInputBO.setSourceOldProdSeq(uniqueProductCreation.getSourceProductSeq());
			if(isDemoOrder) {
				String demoBillEndDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get(BillingConstants.DEMO_BILL_END_DATE));
				productCreationInputBO.setTermReason(BillingConstants.DEMO_ORDER);
				productCreationInputBO.setDepositRefundBoo(BillingConstants.F);
				productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStampForComm(demoBillEndDate));
			}else {
				if (uniqueProductCreation.isParallelMacd() && uniqueProductCreation.getParallelRunDays() > 0) {
					productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(uniqueProductCreation.getTermEndDate(), uniqueProductCreation.getParallelRunDays()));
				} else {
					productCreationInputBO.setTermEndDate(TimeStampUtil.formatWithTimeStampForCommMinusDays(uniqueProductCreation.getTermEndDate(), 1));
					//atMap.put("terminationDate", TimeStampUtil.optimusDateFormat(TimeStampUtil.formatWithTimeStampForCommMinusDays(uniqueProductCreation.getTermEndDate(),1)));
				}
				productCreationInputBO.setTermReason(BillingConstants.CHANGE_BANDWIDTH);
				productCreationInputBO.setDepositRefundBoo(BillingConstants.F);
				if(uniqueProductCreation.getProductName().contains("Burstable") && uniqueProductCreation.getMacdServiceId()!=null) {
					atMap.put(BillingConstants.IS_BURSTABLE_TERMINATED, "Yes");
				}
				componentAndAttributeService.updateAttributes(Integer.parseInt(uniqueProductCreation.getMacdServiceId()), atMap, AttributeConstants.COMPONENT_LM, "A");
			}
			
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
			audit.setCustomerType(isIntlBilling ? BillingConstants.INTL_CUSTOMER : BillingConstants.DOMESTIC_CUSTOMER);
			audit.setRequest(createOrderRequestBO.toString());
			audit.setRequestType(BillingConstants.PRD_COMM);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setStatus(BillingConstants.IN_PROGRESS);
			audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
			audit.setGenevaGrpId(commonBo.getGroupId());
			audit.setProviderSegment(BillingConstants.PROVIDER_SEGMENT);
			audit.setServiceId(uniqueProductCreation.getScServiceDetail().getId().toString());
			audit.setServiceCode(productCreationInputBO.getServiceId());
			audit.setProcessInstanceId(uniqueProductCreation.getProcessInstanceId());
			audit.setAccountNumber(uniqueProductCreation.getAccountId());
			audit.setCount(uniqueProductCreation.getCount());
			audit.setSourceProdSeq(commonBo.getSourceProductSeq());
			audit.setServiceType(uniqueProductCreation.getServiceType());
			audit.setActionType(BillingConstants.CREATE);
			servicehandoverAuditRepo.save(audit);
		}else {
			ServicehandoverAudit auditMacd = servicehandoverAuditRepo.findFirstBySourceProdSeq(uniqueProductCreation.getSourceProductSeq());
			if(auditMacd!=null) {
				auditMacd.setStatus(BillingConstants.IN_PROGRESS);
				auditMacd.setActionType(BillingConstants.TERMINATE);
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
		NetworkProductAttributeString networkProductAttributeString = new NetworkProductAttributeString();
		
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		
		if (uniqueProductCreation.getServiceType().contains(BillingConstants.NPL)) {
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
							"destinationCountry", "popSiteCode", "portBandwidth", "localLoopBandwidth","commissioningDate","bwUnit","localLoopBandwidthUnit","delegateAdminAccess"),
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
		networkProductAttributeString.setOpportunityClassification(BillingConstants.SELL_TO);
		networkProductAttributeString.setIsPartner("");
		networkProductAttributeString.setPartnerName("");
		networkProductAttributeString.setCopfId(scOrder.getOpOrderCode());
		networkProductAttributeString.setParentId(uniqueProductCreation.getBillingType().equals(BillingConstants.DIRECT_ROUTING)?scOrder.getTpsSfdcOptyId():"");
	    networkProductAttributeString.setParentService(uniqueProductCreation.getBillingType().equals(BillingConstants.DIRECT_ROUTING)?uniqueProductCreation.getBillingType():"");//BillingConstants.SERVICE_NAME);
		networkProductAttributeString.setParentServiceName("");//BillingConstants.SERVICE_NAME);
		networkProductAttributeString.setOpportunityId(StringUtils.trimToEmpty(scOrder.getTpsSfdcOptyId()));
		networkProductAttributeString.setPopIdSiteA(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setPopGeoCode(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setMrcPrice(StringUtils.trimToEmpty(uniqueProductCreation.getMrc().toString()));
		// networkProductAttributeString.setARC_PRICE(uniqueProductCreation.getArc().toString());
		networkProductAttributeString.setServiceType(uniqueProductCreation.getBillingType());
		networkProductAttributeString.setBillingType("S");
		networkProductAttributeString.setServiceTypeClubbing(StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		networkProductAttributeString.setOrderType(BillingConstants.NEW);
		networkProductAttributeString.setBundled(uniqueProductCreation.getProductName().contains("Additional IP")
				? scServiceDetail.getAdditionalIpPoolType()
				: "");
		networkProductAttributeString.setCircuitId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setServiceId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setComponentId("");
		networkProductAttributeString.setPoLineItemDescription(
				StringUtils.isNotEmpty(uniqueProductCreation.getDescription())
						? uniqueProductCreation.getDescription().length() >= BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT
										? uniqueProductCreation.getDescription().substring(0, BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT)
										: uniqueProductCreation.getDescription(): "");
		networkProductAttributeString.setPoStartDate("");
		networkProductAttributeString.setPoEndDate("");
		networkProductAttributeString.setTaxCompletionDate("");
		networkProductAttributeString.setViznetId(BillingConstants.GVPN.equals(uniqueProductCreation.getBillingType())
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
		networkProductAttributeString.setPoNumber(StringUtils.isNotEmpty(uniqueProductCreation.getPoNumber())
				? uniqueProductCreation.getPoNumber().length() >= BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT
						? uniqueProductCreation.getPoNumber().substring(0, BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT)
						: uniqueProductCreation.getPoNumber()
				: "");
		networkProductAttributeString.setPoDate(StringUtils.isNotEmpty(uniqueProductCreation.getPoDate())
				? TimeStampUtil.commDateforAttributeString(uniqueProductCreation.getPoDate())
				: "");
		networkProductAttributeString.setGstProductCode(uniqueProductCreation.getHsnCode());
		networkProductAttributeString.setQuantity(uniqueProductCreation.getQuantity());
		
		if(BillingConstants.MIRCOSOFT_CLOUD_SOLNS.equals(uniqueProductCreation.getServiceType())) {
			Map<String, String> poDetailsMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
					Arrays.asList("cofReferenceId","cofCreatedTime"),scOrder);
			networkProductAttributeString.setExternalReferenceId(uniqueProductCreation.getCustomerCuId());
			networkProductAttributeString.setExternalId(uniqueProductCreation.getCustomerCuId());
			networkProductAttributeString.setProductCode(TeamsDrProductCode.getCodebyProductName(uniqueProductCreation.getProductName()));
			networkProductAttributeString.setAdminDelegateAccess(scComponentAttributesAMap.get("delegateAdminAccess") != null
							? scComponentAttributesAMap.get("delegateAdminAccess")
							: "No");
			if(uniqueProductCreation.getProductName().contains("Overage")){
				networkProductAttributeString.setMinimumCommitment(uniqueProductCreation.getQuantity());
			}
			if(poDetailsMap!=null) {
				String poNumber = poDetailsMap.get("cofReferenceId");
				String poDate = poDetailsMap.get("cofCreatedTime");
				networkProductAttributeString.setPoNumber(StringUtils.isNotEmpty(poNumber)? poNumber.length() >= 40 ? poNumber.substring(0, 40) : poNumber : "");
				networkProductAttributeString.setPoDate(StringUtils.isNotEmpty(poDate) ? TimeStampUtil.commDateforAttributeString(poDate) : "");
			}
		}
		if (BillingConstants.FIXED_PORT_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.GVPN_PORT_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.BANDWIDTH_CHARGE.equals(uniqueProductCreation.getProductName())) {
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
		if (BillingConstants.LOCAL_ACCESS_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.CROSS_CONNECTION_CHARGE.equals(uniqueProductCreation.getProductName())) {
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
		}if (BillingConstants.WEBEX_LICENSE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.WEBEX_SUBSCRIPTION.equals(uniqueProductCreation.getProductName())) {
			if (StringUtils.isNotEmpty(uniqueProductCreation.getSkuId())
					&& StringUtils.isNotEmpty(uniqueProductCreation.getDescription())) {
				networkProductAttributeString.setSkuId(uniqueProductCreation.getSkuId());
				networkProductAttributeString.setPoLineItemDescription(uniqueProductCreation.getDescription());
			}
		}
		if (BillingConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			networkProductAttributeString.setMakeAndModel(uniqueProductCreation.getCpeModel());
			networkProductAttributeString.setCpeOutright(BillingConstants.OUTRIGHT);
			networkProductAttributeString.setWarehouseState(uniqueProductCreation.getWareHouseState());
			networkProductAttributeString.setDispatchDate(uniqueProductCreation.getCpeDispatchDate());
			networkProductAttributeString.setCpeSerialNumber(uniqueProductCreation.getCpeSerialNo());
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
			networkProductAttributeString
					.setPoLineItemDescription(StringUtils.isNotEmpty(uniqueProductCreation.getDescription())
							? uniqueProductCreation.getDescription()
							: "");
		}else {
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.commDateforAttributeString(scComponentAttributesAMap.get("commissioningDate")));
		}
		if (uniqueProductCreation.getProductName().contains("Burstable")) {
			networkProductAttributeString.setBaseBandwidth(uniqueProductCreation.getBaseBandwidth());
			networkProductAttributeString.setMaxBandwidth(uniqueProductCreation.getMaxBandwidth());
			if (BillingConstants.GVPN.equals(uniqueProductCreation.getServiceType())) {
				networkProductAttributeString.setPortBandwidth(uniqueProductCreation.getMaxBandwidth());
			} 
			networkProductAttributeString.setUsageModel(uniqueProductCreation.getUsageModel());
			networkProductAttributeString.setBusinessArea(networkProductAttributeString.getSiteZCity());
			networkProductAttributeString.setLocation(networkProductAttributeString.getSiteZCity());
		}
		
		return networkProductAttributeString.toString(uniqueProductCreation);
	}
	
	public String generateProductAttributeStringIntl(ScOrder scOrder, ScServiceDetail scServiceDetail,
			NetworkUniqueProductCreation uniqueProductCreation) {
		LOGGER.info("generateProductAttributes for International is invoked");
		NetworkProductAttributeString networkProductAttributeString = new NetworkProductAttributeString();
		
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("sourceCity", "sourceState", "sourceCountry", "destinationCity", "destinationState",
						"destinationCountry", "popSiteCode", "portBandwidth", "localLoopBandwidth","commissioningDate","bwUnit","localLoopBandwidthUnit"),
				scServiceDetail.getId(), "LM", "A");
		networkProductAttributeString.setBandwidth("");
		networkProductAttributeString.setMaxBandwidth("");
		networkProductAttributeString.setUsageModel("");	
		networkProductAttributeString.setSiteEnd("D");
		networkProductAttributeString.setTigerId("NA");
		networkProductAttributeString.setOpportunityClassification(BillingConstants.SELL_TO);
		networkProductAttributeString.setIsPartner("");
		networkProductAttributeString.setPartnerName("");
		networkProductAttributeString.setCopfId(scOrder.getOpOrderCode());
		networkProductAttributeString.setParentId(uniqueProductCreation.getBillingType().equals(BillingConstants.DIRECT_ROUTING)?scOrder.getTpsSfdcOptyId():"");
		networkProductAttributeString.setParentService(uniqueProductCreation.getBillingType());//BillingConstants.SERVICE_NAME);
		networkProductAttributeString.setParentServiceName("");//BillingConstants.SERVICE_NAME);
		networkProductAttributeString.setOpportunityId(StringUtils.trimToEmpty(scOrder.getTpsSfdcOptyId()));
		networkProductAttributeString.setPopIdSiteA(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setPopGeoCode(StringUtils.trimToEmpty(scComponentAttributesAMap.get("popSiteCode")));
		networkProductAttributeString.setServiceType(uniqueProductCreation.getBillingType());
		networkProductAttributeString.setBillingType("S");
		networkProductAttributeString.setServiceTypeClubbing(uniqueProductCreation.getBillingType());
		networkProductAttributeString.setOrderType(BillingConstants.NEW);
		networkProductAttributeString.setBundled(uniqueProductCreation.getProductName().contains("Additional IP")
				? scServiceDetail.getAdditionalIpPoolType()
				: "");
		networkProductAttributeString.setCircuitId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setServiceId(uniqueProductCreation.getScServiceDetail().getUuid());
		networkProductAttributeString.setComponentId("");
		networkProductAttributeString.setPoLineItemDescription(
				StringUtils.isNotEmpty(uniqueProductCreation.getDescription())
						? uniqueProductCreation.getDescription().length() >= BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT
										? uniqueProductCreation.getDescription().substring(0,BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT)
										: uniqueProductCreation.getDescription(): "");
		networkProductAttributeString.setPoStartDate("");
		networkProductAttributeString.setPoEndDate("");
		networkProductAttributeString.setTaxCompletionDate("");
		networkProductAttributeString.setViznetId(BillingConstants.GVPN.equals(uniqueProductCreation.getBillingType())
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
		networkProductAttributeString.setPoNumber(StringUtils.isNotEmpty(uniqueProductCreation.getPoNumber())
				? uniqueProductCreation.getPoNumber().length() >= BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT
						? uniqueProductCreation.getPoNumber().substring(0, BillingConstants.GENEVA_ATTRIBUTE_STRING_LIMIT)
						: uniqueProductCreation.getPoNumber()
				: "");
		networkProductAttributeString.setPoDate(StringUtils.isNotEmpty(uniqueProductCreation.getPoDate())
				? TimeStampUtil.commDateforAttributeString(uniqueProductCreation.getPoDate())
				: "");
		networkProductAttributeString.setGstProductCode(uniqueProductCreation.getHsnCode());
		
		if(BillingConstants.MIRCOSOFT_CLOUD_SOLNS.equals(uniqueProductCreation.getServiceType())) {
			Map<String, String> poDetailsMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
					Arrays.asList("cofReferenceId","cofCreatedTime"),scOrder);
			networkProductAttributeString.setExternalReferenceId(uniqueProductCreation.getCustomerCuId());
			networkProductAttributeString.setExternalId(uniqueProductCreation.getCustomerCuId());
			networkProductAttributeString.setProductCode(TeamsDrProductCode.getCodebyProductName(uniqueProductCreation.getProductName()));
			networkProductAttributeString.setAdminDelegateAccess(scComponentAttributesAMap.get("delegateAdminAccess") != null
							? scComponentAttributesAMap.get("delegateAdminAccess")
							: "No");
			if(uniqueProductCreation.getProductName().contains("Overage")){
				networkProductAttributeString.setMinimumCommitment(uniqueProductCreation.getQuantity());
			}
			if(poDetailsMap!=null) {
				String poNumber = poDetailsMap.get("cofReferenceId");
				String poDate = poDetailsMap.get("cofCreatedTime");
				networkProductAttributeString.setPoNumber(StringUtils.isNotEmpty(poNumber)? poNumber.length() >= 40 ? poNumber.substring(0, 40) : poNumber : "");
				networkProductAttributeString.setPoDate(StringUtils.isNotEmpty(poDate) ? TimeStampUtil.commDateforAttributeString(poDate) : "");
			}
		}
		if (BillingConstants.FIXED_PORT_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.GVPN_PORT_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.BANDWIDTH_CHARGE.equals(uniqueProductCreation.getProductName())) {
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
		if (BillingConstants.LOCAL_ACCESS_CHARGE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.CROSS_CONNECTION_CHARGE.equals(uniqueProductCreation.getProductName())) {
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
		}if (BillingConstants.WEBEX_LICENSE.equals(uniqueProductCreation.getProductName())
				|| BillingConstants.WEBEX_SUBSCRIPTION.equals(uniqueProductCreation.getProductName())) {
			if (StringUtils.isNotEmpty(uniqueProductCreation.getSkuId())
					&& StringUtils.isNotEmpty(uniqueProductCreation.getDescription())) {
				networkProductAttributeString.setSkuId(uniqueProductCreation.getSkuId());
				networkProductAttributeString.setPoLineItemDescription(uniqueProductCreation.getDescription());
			}
		}
		if (BillingConstants.CPE.equals(uniqueProductCreation.getServiceType())) {
			networkProductAttributeString.setMakeAndModel(uniqueProductCreation.getCpeModel());
			networkProductAttributeString.setCpeOutright(BillingConstants.OUTRIGHT);
			networkProductAttributeString.setWarehouseState(uniqueProductCreation.getWareHouseState());
			networkProductAttributeString.setDispatchDate(uniqueProductCreation.getCpeDispatchDate());
			networkProductAttributeString.setCpeSerialNumber(uniqueProductCreation.getCpeSerialNo());
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.formatWithoutTimeStamp().format(new Date()));
			networkProductAttributeString
					.setPoLineItemDescription(StringUtils.isNotEmpty(uniqueProductCreation.getDescription())
							? uniqueProductCreation.getDescription()
							: "");
		}else {
			networkProductAttributeString.setCommissioningDate(TimeStampUtil.commDateforAttributeString(scComponentAttributesAMap.get("commissioningDate")));
		}
		if(uniqueProductCreation.getProductName().contains("Burstable")) {
			networkProductAttributeString.setBaseBandwidth(uniqueProductCreation.getBaseBandwidth());
			networkProductAttributeString.setMaxBandwidth(uniqueProductCreation.getMaxBandwidth());
			networkProductAttributeString.setUsageModel(uniqueProductCreation.getUsageModel());
			networkProductAttributeString.setBusinessArea(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		}
		networkProductAttributeString.setLocation(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		
		return networkProductAttributeString.toString(uniqueProductCreation);
	}

	
	public String generateProductGroupId(String orderCode, String appEnv, String appHost) {
		String groupId = BillingConstants.OPT_PROD.concat(orderCode) + "_" + System.currentTimeMillis();
		if ("DEV".equalsIgnoreCase(appEnv)) {
			if (appHost.contains(".34")) {
				groupId = groupId.concat("-34");
			} else if (appHost.contains(".40")) {
				groupId = groupId.concat("-40");
			} else if (appHost.contains(".146")) {
				groupId = groupId.concat("-146");
			}else if (appHost.contains(".33")) {
				groupId =groupId.concat("-33");
			}
		}
		return groupId;
	}
	
	public String getServiceType(String serviceType) {
		return BillingConstants.IZOSDWAN.equals(serviceType) || BillingConstants.IZO_SDWAN.equals(serviceType)
				? BillingConstants.IZO_SDWAN
				: serviceType;

	}

}
