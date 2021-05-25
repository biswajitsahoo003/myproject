package com.tcl.dias.servicehandover.ipc.service;

import java.net.UnknownHostException;
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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.invoice.BSSEGInvoiceDetails;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoice;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.util.IPCAddressConstructor;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

@Service
public class IpcBillingInvoiceCreationService {

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
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	@Autowired
	IPCAddressConstructor ipcAddressConstructor;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingInvoiceCreationService.class);
	
	/**
	 * This method generates invoice for the products commissioned.
	 * @param serviceId 
	 * @param serviceType 
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@Retryable(value = { UnknownHostException.class }, maxAttempts = 3, backoff = @Backoff(delay = 10000))
	public CreateInvoiceResponse invoiceGeneration(String processId, String orderCode,
			String serviceCode, String invoiceType, String serviceId, String serviceType) throws ParseException, UnknownHostException {
		LOGGER.info("invoiceGeneration invoked for orderId {}", orderCode);
		CreateInvoice createInvoice = new CreateInvoice();
		BSSEGInvoiceDetails invoiceDetails = new BSSEGInvoiceDetails();
		CreateInvoiceResponse createInvoiceResponse = null;
		ServicehandoverAudit servicehandoverAudit = new ServicehandoverAudit();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		ScServiceDetail scServiceDetail =scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		List<IpcChargeLineitem> scChargeLineitems = ipcChargeLineitemRepository.findByServiceIdAndServiceType(serviceId,serviceType);
		Double nrc = scChargeLineitems.stream().collect(Collectors.summingDouble(detail->Double.parseDouble(detail.getNrc())));
		Double arc = scChargeLineitems.stream().collect(Collectors.summingDouble(detail->Double.parseDouble(detail.getArc())));
		AccountCreationInputBO accountCreationInputBO = ipcAddressConstructor.accountAddress(scContractInfo, new AccountCreationInputBO());
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
		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
						Arrays.asList("billFreePeriod", "sapCode", IpcConstants.IPC_BILLING_ACCOUNT_NUMBER));
		Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String billFreePeriod = attributeMap.getOrDefault("billFreePeriod", "0");	
		String crnNumber =  attributeMap.getOrDefault("sapCode", "");
		String accountNumber =  attributeMap.getOrDefault(IpcConstants.IPC_BILLING_ACCOUNT_NUMBER, "");
		invoiceDetails.setCustomerName(scOrder.getErfCustLeName());
		invoiceDetails.setSegment(NetworkConstants.PROVIDER_SEGMENT);
		invoiceDetails.setCrnNumber(crnNumber);
		invoiceDetails.setOrderStatus(invoiceType);
		invoiceDetails.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
		invoiceDetails.setStatus("");
		invoiceDetails.setCreatedDate(TimeStampUtil.formatWithTimeStamp().format(new Date()));
		invoiceDetails.setModifiedDate("");
		invoiceDetails.setCreatedBy("");
		invoiceDetails.setModifiedBy("");
		invoiceDetails.setServiceId(scServiceDetail.getUuid());
		invoiceDetails.setCopfId(scOrder.getUuid());
		invoiceDetails.setAccountNo(accountNumber);
		invoiceDetails.setCpeAccountNo("");
		invoiceDetails.setLegalEntity(Constants.TATA);
		invoiceDetails.setTaskName(invoiceType);
		invoiceDetails.setCpeDeliveryAddress("");
		invoiceDetails.setOrderType(scOrder.getOrderType());
		invoiceDetails.setTerminationDate("");
		invoiceDetails.setDmsId("");
		invoiceDetails.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
		invoiceDetails.setTotalArc(arc.toString());
		invoiceDetails.setTotalNrc(nrc.toString());
		invoiceDetails.setCircuitCount(1);
		invoiceDetails.setSource(Constants.OPTIMUS);
		invoiceDetails.setBillingAddress(billingAddress);
		invoiceDetails.setAccountingCode("");
		invoiceDetails.setParentId("");
		invoiceDetails.setParentServiceName("");
		invoiceDetails.setCeaseFlag("");
		invoiceDetails.setCommissionedDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(
						scServiceDetail.getServiceCommissionedDate().toString(),
						StringUtils.isNoneBlank(billFreePeriod) ? Integer.valueOf(billFreePeriod) : 0));
		createInvoice.setInvoiceRequest(invoiceDetails);
		JaxbMarshallerUtil.jaxbObjectToXML(createInvoice, servicehandoverAudit);
		LOGGER.info("invoiceGeneration completed for orderId {}", scOrder.getUuid());
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
		servicehandoverAuditRepository.save(servicehandoverAudit);
			
		return createInvoiceResponse;
	}
	
	@Recover
	public CreateInvoiceResponse recover(UnknownHostException e, CreateInvoiceResponse createInvoiceResponse,
			String orderCode, String serviceCode) {
		LOGGER.info("Invoice Creation UnknowhostException Recovered for order id {} service id {}", orderCode, serviceCode);
		return createInvoiceResponse;
	}
}
