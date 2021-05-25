package com.tcl.dias.servicehandover.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



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
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.invoice.BSSEGInvoiceDetails;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoice;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

@Service
public class BillingInvoiceCreationService {

	@Autowired
	@Qualifier("Invoice")
	SOAPConnector invoiceSoapConnector;
	
	@Value("${invoiceGeneration}")
	private String invoiceGenerationOperation;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	String poNumberIas = "";

	String poDateIas = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingInvoiceCreationService.class);
	/**
	 * This method generates invoice for the products commissioned.
	 * @param serviceId 
	 * @param serviceType 
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateInvoiceResponse invoiceGeneration(String processId, String orderId,
			String serviceCode, String invoiceType, String serviceId, String serviceType) throws ParseException {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("billingAccountNumber","cpeAccountNumber","sapCode"),
				scServiceDetail.getId(), "LM","A");
		CreateInvoiceResponse createInvoiceResponse = new CreateInvoiceResponse();
		if (attrMap != null) {
			
			CreateInvoice createInvoice = new CreateInvoice();
			BSSEGInvoiceDetails invoiceDetails = new BSSEGInvoiceDetails();
			ServicehandoverAudit servicehandoverAudit = new ServicehandoverAudit();
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
			ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
			List<ScChargeLineitem> scChargeLineitems = chargeLineitemRepository.findByServiceIdAndServiceType(serviceId,
					serviceType);
			Double nrc = scChargeLineitems.stream()
					.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
			Double arc = scChargeLineitems.stream()
					.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));
			AccountCreationInputBO accountCreationInputBO = addressConstructor.accountAddress(scContractInfo,
					new AccountCreationInputBO());
			String billingAddress = StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr1()) + " "
					+ StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr2()) + " "
					+ StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr3()) + " "
					+ accountCreationInputBO.getAccCity() + " " + accountCreationInputBO.getAccState() + " "
					+ accountCreationInputBO.getAccZipcode() + " " + accountCreationInputBO.getAccCountry();
			ScComponentAttribute billStartDate = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), NetworkConstants.BILL_START_DATE, "LM", "A");
			List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
					.findByScOrder_IdAndIsActive(scOrder.getId(), NetworkConstants.Y);
			scOrderAttributeList.forEach((scOrderAttribute) -> {

				switch (scOrderAttribute.getAttributeName()) {

				case LeAttributesConstants.BILLING_CURRENCY:
					invoiceDetails.setBillingCurrency(scOrderAttribute.getAttributeValue());
					break;

				case LeAttributesConstants.PAYMENT_CURRENCY:
					invoiceDetails.setInfoCurrency(scOrderAttribute.getAttributeValue());
					break;

				case LeAttributesConstants.PO_NUMBER:
					poNumberIas=scOrderAttribute.getAttributeValue();
					break;
				case LeAttributesConstants.PO_DATE:
					poDateIas=scOrderAttribute.getAttributeValue();
					break;
				case LeAttributesConstants.BILLING_CONTACT_NAME:
					invoiceDetails.setContactPerson(scOrderAttribute.getAttributeValue());
					break;
				}

			});
			if(poDateIas!= null && poNumberIas!=null) {
				 invoiceDetails.setPoDate(poDateIas); 
				 invoiceDetails.setPoNumber(poNumberIas);
			}
			else {
				 invoiceDetails.setPoDate(""); 
				 invoiceDetails.setPoNumber(""); 
			}
			invoiceDetails.setCustomerName(scOrder.getErfCustLeName());
			invoiceDetails.setSegment(NetworkConstants.PROVIDER_SEGMENT);
			if(attrMap.get("sapCode")!=null) {
				invoiceDetails.setCrnNumber(attrMap.get("sapCode"));
			}else {
				invoiceDetails.setCrnNumber(loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
			}
			invoiceDetails.setCrnNumber(attrMap.get("sapCode"));
			invoiceDetails.setOrderStatus(invoiceType);
			invoiceDetails.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
			invoiceDetails.setStatus("");
			invoiceDetails.setCreatedDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
			invoiceDetails.setModifiedDate("");
			invoiceDetails.setCreatedBy("");
			invoiceDetails.setModifiedBy("");
			invoiceDetails.setServiceId(scServiceDetail.getUuid());
			invoiceDetails.setCopfId(scOrder.getUuid());
			if (invoiceType.equals(NetworkConstants.G_INVOICE)) {
				invoiceDetails.setAccountNo(attrMap.get("billingAccountNumber"));
				invoiceDetails.setCpeAccountNo("");
				invoiceDetails.setCommissionedDate(billStartDate != null
						? TimeStampUtil.formatWithTimeStampForComm(billStartDate.getAttributeValue())
						: "");
				invoiceDetails.setCpeDeliveryAddress("");
				servicehandoverAudit.setAccountNumber(invoiceDetails.getAccountNo());
				
				
			}
			if (invoiceType.equals(NetworkConstants.CPE_INVOICE)) {
				invoiceDetails.setAccountNo("");
				invoiceDetails.setCpeAccountNo(attrMap.get("cpeAccountNumber"));
				invoiceDetails.setCommissionedDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
				invoiceDetails.setCpeDeliveryAddress(scServiceDetail.getSiteAddress());
				servicehandoverAudit.setAccountNumber(invoiceDetails.getCpeAccountNo());
			}
			invoiceDetails.setLegalEntity(NetworkConstants.TATA);
			invoiceDetails.setTaskName(invoiceType);
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

			if (orderType != null && NetworkConstants.NEW.equals(orderType)) {
				invoiceDetails.setOrderType(NetworkConstants.NEW);
			} else {
				String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

				invoiceDetails.setOrderType(
						scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory()
								: orderCategory);
			}
			
			invoiceDetails.setTerminationDate("");
			invoiceDetails.setDmsId("");
			invoiceDetails.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
			invoiceDetails.setTotalArc(arc.toString());
			invoiceDetails.setTotalNrc(nrc.toString());
			invoiceDetails.setCircuitCount(1);
			invoiceDetails.setSource(NetworkConstants.OPTIMUS);
			invoiceDetails.setBillingAddress(billingAddress.length()>240?billingAddress.substring(0, 240):billingAddress);
			invoiceDetails.setAccountingCode("");
			invoiceDetails.setParentId("");
			invoiceDetails.setParentServiceName("");
			invoiceDetails.setCeaseFlag("");
			createInvoice.setInvoiceRequest(invoiceDetails);
			JaxbMarshallerUtil.jaxbObjectToXML(createInvoice, servicehandoverAudit);
			LOGGER.info("invoiceGeneration completed for orderId {}", scOrder.getUuid());
			createInvoiceResponse = (CreateInvoiceResponse) invoiceSoapConnector
					.callWebService(invoiceGenerationOperation, createInvoice);
			if (createInvoiceResponse.getAckResponse().getStatusCode().equals("Success")) {
				servicehandoverAudit.setStatus("Sent For Invoice");
			} else {
				LOGGER.info("invoiceGeneration status {}", createInvoiceResponse.getAckResponse().getStatusCode());
				LOGGER.info("invoiceGeneration error msg {}",
						createInvoiceResponse.getAckResponse().getErrorDescription());
				servicehandoverAudit.setStatus("Not Sent For Invoice");
			}

			servicehandoverAudit.setProcessInstanceId(processId);
			servicehandoverAudit.setOrderId(invoiceDetails.getCopfId());
			servicehandoverAudit.setServiceType(invoiceDetails.getServiceType());
			servicehandoverAudit.setServiceId(invoiceDetails.getServiceId());
			servicehandoverAudit.setLegalEntity(invoiceDetails.getLegalEntity());
			servicehandoverAudit.setCrnId(invoiceDetails.getCrnNumber());
			servicehandoverAudit.setOpportunityId(invoiceDetails.getSfdcOptyId());
			servicehandoverAudit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
			servicehandoverAudit.setRequestType(Constants.INVOICE_GEN);
			servicehandoverAudit.setStatus(Constants.IN_PROGRESS);
			servicehandoverAudit.setCreatedDate(new Date());
			servicehandoverAudit.setProviderSegment(invoiceDetails.getSegment());
			servicehandoverAuditRepo.save(servicehandoverAudit);
		}
		return createInvoiceResponse;
	}
}
