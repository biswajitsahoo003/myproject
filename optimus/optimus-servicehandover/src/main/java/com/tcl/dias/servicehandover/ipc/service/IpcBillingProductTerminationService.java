package com.tcl.dias.servicehandover.ipc.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
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
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.IPCAddressConstructor;
import com.tcl.dias.servicehandover.util.IPCChargeLineItemsUtil;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.LoadCustomerDetails;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.dias.servicehandover.util.UniqueProductCreation;

/**
 * 
 * Service to Terminte IPC Products for Macd Orders
 * 
 * 
 * @author yomagesh
 *
 */

@Service
public class IpcBillingProductTerminationService {

	@Autowired
	IpcBillingProductCreationService billingProductCreationService;

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
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	IPCAddressConstructor ipcAddressConstructor;
	
	@Autowired
	IPCChargeLineItemsUtil ipcChargeLineItemsUtil;

	private String category;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingProductTerminationService.class);
	
	/**
	 * 
	 * Method to Terminate Ipc Products
	 * 
	 * @param orderId
	 * @param processInstanceId
	 * @param serviceType
	 * @param serviceId
	 * @param serviceCode
	 * @return
	 */
	public CreateOrderResponse productTermination(String orderId, String processInstanceId,String serviceType,
			String serviceId, String serviceCode) {
		String updationRes = ipcChargeLineItemsUtil.updateLineItems(ipcChargeLineitemRepository
				.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndTerminatedFlag(serviceCode, serviceType, NetworkConstants.TERMINATE, NetworkConstants.NEW, IpcConstants.N));
		ipcChargeLineItemsUtil.checkAndUpdateLineItemsIfSameAccountChoosenForMACD(serviceId,serviceCode, serviceType);
		CreateOrder createOrder = frameProductTerminationRequest(orderId,processInstanceId,serviceType,serviceId,serviceCode);
		if( null != createOrder) {
			CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
					.callWebService(createOrderOperation, frameProductTerminationRequest(orderId,processInstanceId,serviceType,serviceId,serviceCode));
			String status= null;
			if (Objects.nonNull(createOrderResponse)) {
				status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				LOGGER.info("productTermination completed for orderId {} with status {}", orderId, status);
				return createOrderResponse;
			}
			LOGGER.info("productTermination completed for orderId {} with status {}", orderId, status);
		} else {
			return null;
		}
		return new CreateOrderResponse();
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrder frameProductTerminationRequest(String orderId, String processInstanceId,String serviceType,
			String serviceId, String serviceCode) {
		LOGGER.info("productTermination invoked for orderId {} and Service type {}", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		String groupId = NetworkConstants.OPT_PROD.concat(scOrder.getOpOrderCode())+"_"+System.currentTimeMillis()+"_T";
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
						Arrays.asList(Constants.SAP_CODE, CommonConstants.IS_IPC_BILLING_INTL, IpcConstants.IPC_BILLING_ENTITY));
		Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String sapCode=attributeMap.get(Constants.SAP_CODE);
		ScOrderAttribute contractingAddress = scOrderAttributeRepository
				.findFirstByAttributeNameAndScOrder(Constants.CONTRACTING_ADDRESS, scOrder);
		ScOrderAttribute overagesChargesDiscountTiers = scOrderAttributeRepository
				.findFirstByAttributeNameAndScOrder(Constants.IPC_DT_OVERAGE_CHARGES, scOrder);
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		if (scServiceDetail != null) {
			List<IpcChargeLineitem> ipcChargeLineitems = ipcChargeLineItemsUtil.formatLineItems(ipcChargeLineitemRepository
					.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndTerminatedFlag(serviceCode, serviceType,
							NetworkConstants.TERMINATE, NetworkConstants.IN_PROGRESS, IpcConstants.Y));
			category = ipcChargeLineItemsUtil.getCategory(ipcChargeLineitems);
			if (ipcChargeLineitems.isEmpty()) {
				return null;
			}
			ipcChargeLineitems.forEach(chargeLineitem -> {
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
				uniqueProductCreation.setActionType(Constants.TERMINATE);
				uniqueProductCreation.setOrderType(Constants.MODIFY);
				uniqueProductCreation.setChangeOrderType(Constants.TERMINATE_ORDER);
				uniqueProductCreation.setQuantity(chargeLineitem.getQuantity());
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(true);
				uniqueProductCreation.setScenarioType("");
				uniqueProductCreation.setMigParentServiceCode(chargeLineitem.getMigParentServiceCode());
				uniqueProductCreation.setBaseBandwidth(chargeLineitem.getAdditionalParam());
				uniqueProductCreation.setServicehandoverAudit(servicehandoverAuditRepository.findById(chargeLineitem.getSourceProductSequence()).get());
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setBillingEntity(attributeMap.getOrDefault(IpcConstants.IPC_BILLING_ENTITY, IpcConstants.BILLING_ENTITY));
				if(chargeLineitem.getChargeLineitem().equals(IpcConstants.DATA_TRANSFER_USAGE)) {
					uniqueProductCreation.setOverageChargesDiscountTiers(
							(overagesChargesDiscountTiers != null && overagesChargesDiscountTiers.getAttributeValue() != null) 
							? overagesChargesDiscountTiers.getAttributeValue() : IpcConstants.IPC_DT_USAGE_DISCOUNT_TIERS);
				}
				uniqueProductCreation.setCommissioningDate(TimeStampUtil
						.formatWithTimeStampForCommMinusDays(scServiceDetail.getServiceCommissionedDate().toString(), 1));
				if (contractingAddress != null && contractingAddress.getAttributeValue() != null) {
					uniqueProductCreation.setContractingAddress(contractingAddress.getAttributeValue());
				}
				uniqueProductCreation.setSapCode(
						sapCode != null ? sapCode : loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId()));
				uniqueProductCreation.setCloudCode(chargeLineitem.getCloudCode());
				CreateOrderRequestBO createOrderRequestBO = billingProductCreationService
						.createUniqueProduct(uniqueProductCreation, attributeMap.containsKey(CommonConstants.IS_IPC_BILLING_INTL) ? attributeMap.get(CommonConstants.IS_IPC_BILLING_INTL) : "N", category);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
				
			});
			createOrder.setCreateOrderRequestInput(createOrderBO);
			JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		}
		return createOrder;
	}
	
}
