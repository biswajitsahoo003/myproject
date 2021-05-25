package com.tcl.dias.servicehandover.cancellation.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.cancellation.listener.CancellationInvoiceGenerationListener;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.invoice.BSSEGInvoiceDetails;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoice;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

@Service
public class CancellationBillingInvoiceCreationService {
	
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
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	String poNumberIas = "";

	String poDateIas = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationInvoiceGenerationListener.class);
	
	/**
	 * This method generate invoice for Cancellation Flow
	 * @param serviceId 
	 * @param serviceType 
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public CreateInvoiceResponse triggerCancellationInvoice(String processId, String orderCode,
			String serviceCode, String invoiceType, String serviceId, String serviceType) throws ParseException {
		LOGGER.info("Cancellation Invoice invoked for service {}", serviceCode);
		CreateInvoice createInvoice = new CreateInvoice();
		BSSEGInvoiceDetails invoiceDetails = new BSSEGInvoiceDetails();
		CreateInvoiceResponse createInvoiceResponse = null;
		ServicehandoverAudit servicehandoverAudit = new ServicehandoverAudit();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		Optional<ScServiceDetail> scServiceDetail =scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
			AccountCreationInputBO accountCreationInputBO = addressConstructor.accountAddress(scContractInfo, new AccountCreationInputBO());
		String billingAddress = StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr1()) +" "+  StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr2())
				+" "+  StringUtils.trimToEmpty(accountCreationInputBO.getAccAddr3()) +" "+  accountCreationInputBO.getAccCity()
				+" "+  accountCreationInputBO.getAccState() +" "+  accountCreationInputBO.getAccZipcode()
				+" "+  accountCreationInputBO.getAccCountry();
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
		Map<String, String> scComponentAttributesAMap=	commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("billingAccountNumber","sapCode","billStartDate","terminationFlowTriggered"), scServiceDetail.get().getId(), "LM", "A");
		String customerRef = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sapCode"));
		
		invoiceDetails.setCustomerName(scContractInfo.getErfCustLeName());
		invoiceDetails.setSegment(NetworkConstants.PROVIDER_SEGMENT);
		invoiceDetails.setCrnNumber(customerRef);
		invoiceDetails.setOrderStatus(invoiceType);
		invoiceDetails.setServiceType(serviceType);
		invoiceDetails.setStatus("");
		invoiceDetails.setCreatedDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		invoiceDetails.setModifiedDate("");
		invoiceDetails.setCreatedBy("");
		invoiceDetails.setModifiedBy("");
		invoiceDetails.setServiceId(serviceCode);
		invoiceDetails.setAccountNo(scComponentAttributesAMap.get("billingAccountNumber"));
		invoiceDetails.setCpeAccountNo("");
		invoiceDetails.setCommissionedDate("");
		invoiceDetails.setLegalEntity(NetworkConstants.TATA);
		invoiceDetails.setTaskName(invoiceType);
		invoiceDetails.setCpeDeliveryAddress("");
		invoiceDetails.setOrderType(NetworkConstants.CANCELLATION);
		invoiceDetails.setCopfId(orderCode);
		invoiceDetails.setTerminationDate("");
		invoiceDetails.setDmsId("");
		invoiceDetails.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
		invoiceDetails.setTotalArc("");
		invoiceDetails.setTotalNrc("");
		invoiceDetails.setCircuitCount(1);
		invoiceDetails.setSource(NetworkConstants.OPTIMUS);
		invoiceDetails.setBillingAddress(billingAddress);
		invoiceDetails.setAccountingCode("");
		invoiceDetails.setParentId("");
		invoiceDetails.setParentServiceName("");
		invoiceDetails.setCeaseFlag("");
		createInvoice.setInvoiceRequest(invoiceDetails);
		JaxbMarshallerUtil.jaxbObjectToXML(createInvoice, servicehandoverAudit);
		LOGGER.info("Cancellation Invoice completed for orderId {}", invoiceDetails.getCopfId());
		createInvoiceResponse = (CreateInvoiceResponse) invoiceSoapConnector.callWebService(invoiceGenerationOperation,
				createInvoice);
		if (createInvoiceResponse.getAckResponse().getStatusCode().equals("Success")) {
			servicehandoverAudit.setStatus("Sent For Invoice");
		} else {
			LOGGER.info("Cancellation Invoicen status {}", createInvoiceResponse.getAckResponse().getStatusCode());
			LOGGER.info("Cancellation Invoice error msg {}", createInvoiceResponse.getAckResponse().getErrorDescription());
			servicehandoverAudit.setStatus("Not Sent For Invoice");
		}

		servicehandoverAudit.setProcessInstanceId(processId);
		servicehandoverAudit.setOrderId(invoiceDetails.getCopfId());
		servicehandoverAudit.setServiceType(invoiceDetails.getServiceType());
		servicehandoverAudit.setServiceId(serviceId);
		servicehandoverAudit.setServiceCode(serviceCode);
		servicehandoverAudit.setLegalEntity(invoiceDetails.getCustomerName());
		servicehandoverAudit.setCrnId(invoiceDetails.getCrnNumber());
		servicehandoverAudit.setOpportunityId(invoiceDetails.getSfdcOptyId());
		servicehandoverAudit.setCustomerType(Constants.DOMESTIC_CUSTOMER);
		servicehandoverAudit.setRequestType(NetworkConstants.CANCELLATION);
		servicehandoverAudit.setStatus(Constants.IN_PROGRESS);
		servicehandoverAudit.setCreatedDate(new Date());
		servicehandoverAudit.setAccountNumber("");
		servicehandoverAudit.setProviderSegment(invoiceDetails.getSegment());
		servicehandoverAuditRepo.save(servicehandoverAudit);
		
		return createInvoiceResponse;
	}
	
	
}
