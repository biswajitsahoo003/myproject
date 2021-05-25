package com.tcl.dias.servicehandover.cancellation.service;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.NetworkUniqueProductCreation;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

/**
 * 
 * Service to Terminte IPC Products for Macd Orders
 * 
 * 
 * @author yomagesh
 *
 */

@Service
public class CancellationBillingProductTerminationService {

	@Autowired
	CancellationBillingProductCreationService billingProductCreationService;

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
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationBillingProductTerminationService.class);
	
	/**
	 * 
	 * Method to Terminate Cancellation Products
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceType
	 * @param serviceId
	 * @param serviceCode
	 * @return
	 */	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrderResponse triggerCancellationProductTermination(String orderId, String processInstanceId,String serviceType,
			String serviceId, String serviceCode) {
		LOGGER.info("Cancellation Product Termination invoked for orderId {} and Service type {}", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		String status = "";
		ScServiceDetail serviceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		if (serviceDetail != null) {
			String groupId = NetworkConstants.OPT_PROD.concat(serviceDetail.getScOrderUuid())+ "_"+ System.currentTimeMillis() + "_T";
			List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository.findAllForServiceCancellationProductTermination(serviceId,serviceType);
			chargeLineitems.forEach(chargeLineitem -> {
				NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
				uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(serviceDetail);
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.TERMINATE);
				uniqueProductCreation.setOrderType(NetworkConstants.MODIFY);
				uniqueProductCreation.setChangeOrderType(Constants.TERMINATE_ORDER);
				uniqueProductCreation.setQuantity(NetworkConstants.ONE);
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setCpeModel(StringUtils.trimToEmpty(chargeLineitem.getCpeModel()));
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(true);
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setCommissioningDate(TimeStampUtil.formatWithTimeStampForCommPlusDays(chargeLineitem.getTermDate(), 1));
				if(chargeLineitem.getSourceProdSequence()!=null) {
					uniqueProductCreation.setSourceProductSeq(chargeLineitem.getSourceProdSequence());
				}else {
					uniqueProductCreation.setSourceProductSeq(gnvOrderEntryTabRepository.findSourceProdSeq(chargeLineitem.getAccountNumber(),chargeLineitem.getChargeLineitem()));
				}
				uniqueProductCreation.setBillingType(NetworkConstants.BILLING_TYPE_ADVANCE);
				uniqueProductCreation.setMacdServiceId(serviceId);
				chargeLineitem.setActionType(NetworkConstants.TERMINATE);
				chargeLineitem.setServiceTerminationFlag(NetworkConstants.Y);
				chargeLineitemRepository.save(chargeLineitem);
				chargeLineitemRepository.flush();
				uniqueProductCreation.setServicehandoverAudit(new ServicehandoverAudit());
				CreateOrderRequestBO createOrderRequestBO = billingProductCreationService.createUniqueProduct(uniqueProductCreation);
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
		LOGGER.info("Cancellation Product Termination completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}
}
