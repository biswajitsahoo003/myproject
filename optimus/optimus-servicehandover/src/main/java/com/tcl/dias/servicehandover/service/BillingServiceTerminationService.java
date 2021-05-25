package com.tcl.dias.servicehandover.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
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
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.TimeStampUtil;

/**
 * Service Class Terminating Optimus Circuits
 * Generation
 * 
 * @author yomagesh
 *
 */
@Service
public class BillingServiceTerminationService {

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
	ServicehandoverAuditRepository servicehandoverAuditRepo;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	String status = "";

 	private static final Logger LOGGER = LoggerFactory.getLogger(BillingServiceTerminationService.class);

	@Transactional(isolation = Isolation.DEFAULT)
	public CreateOrderResponse serviceTerminationOptimus(String orderCode, String processInstanceId, String serviceCode,
			String serviceType, String serviceId)
			throws ClassNotFoundException, SQLException, ParseException, InterruptedException {
		LOGGER.info("service termination started for orderId {} with status {}", orderCode, status);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		CreateOrder createOrder = new CreateOrder();
		CreateOrderResponse createOrderResponse = new CreateOrderResponse();
		if (scServiceDetail != null) {
			ScChargeLineitem chargeLineitem = chargeLineitemRepository.findFirstByServiceTermination(serviceId,serviceType);
			Map<String, String> scComponentAttributesAMap=	commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("contractEndDate"), scServiceDetail.getId(), "LM", "A");
			String custRef = "";
			if (chargeLineitem != null) {
				CreateOrderBO createOrderBO = new CreateOrderBO();
				CreateOrderRequestBO createOrderRequestBO = new CreateOrderRequestBO();
				ProductCreationRequestBO productCreationRequestBO = new ProductCreationRequestBO();
				ProductCreationInputBO productCreationInputBO = new ProductCreationInputBO();
				CommonAttributesBO commonAttributesBO = new CommonAttributesBO();
				CommonBO commonBo = new CommonBO();
				
				String etcWaiver = "NO";
				String etcWaiverType = "No";
				String etcCharge=chargeLineitem.getEtcCharge();
				String contractEndDate = scComponentAttributesAMap.get("contractEndDate");

				/*switch (chargeLineitem.getEtcWaiver()) {
				case "Yes":
					etcWaiver = "YES";
					etcWaiverType = "Full";
					break;

				case "Partial":
					etcWaiver = "YES";
					etcWaiverType = "Partial";
					break;

				}*/
				
				LOGGER.info("Wavier flag given is : {}",etcWaiverType);
				commonBo.setGroupId(NetworkConstants.OPT_SERVICE_TERM + System.currentTimeMillis());
				commonBo.setSourceSystem(NetworkConstants.SOURCE_SYSTEM);
				commonBo.setRequestType(NetworkConstants.SERVICE);
				commonBo.setActionType(NetworkConstants.TERMINATE);
				commonBo.setInvoicingCoName(NetworkConstants.INVOICING_CONAME);
				
				String gnvOrder = gnvOrderEntryTabRepository.findCustomerRef(serviceCode);
				if (gnvOrder != null) {
					custRef = gnvOrder;
				}
				/*if("Full".equals(etcWaiverType)) {
					etcCharge="0.00";
				}*/
				productCreationInputBO.setServiceId(chargeLineitem.getServiceCode());
				productCreationInputBO.setCopfId(scServiceDetail.getScOrderUuid());
				productCreationInputBO.setScenarioType("TERMINATE#|#ETC Charges=" + etcCharge
						+ "#|#ETC WAIVE Flag=" + etcWaiver + "#|#ETC WAIVE TYPE=" + etcWaiverType
						+ " Waiver#|#System Generated ETC=" + etcCharge + "#|#Contract End Date="
						+ TimeStampUtil.formatTerminationDateForLR(contractEndDate) + "#|#");
				
				productCreationInputBO.setTermReason(NetworkConstants.SERVICE_TERMINATION_REASON);
				productCreationInputBO.setTermProposedDate(TimeStampUtil.formatWithTimeStampForComm(chargeLineitem.getTermDate()));
				productCreationInputBO.setTermCharges("0");
				productCreationInputBO.setRefundBoo(NetworkConstants.F);
				productCreationInputBO.setUsername("EAI");
				
				commonBo.setCustomerRef(custRef);
				commonAttributesBO.setComReqAttributes(commonBo);
				productCreationRequestBO.setProductInput(productCreationInputBO);

				createOrderRequestBO.setProdCreationInput(productCreationRequestBO);
				createOrderRequestBO.setCommonInput(commonAttributesBO);
				createOrderRequestBO.setAccCreationInput(null);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
				createOrder.setCreateOrderRequestInput(createOrderBO);

			}
		}
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());

		createOrderResponse = (CreateOrderResponse) orderSoapConnector.callWebService(createOrderOperation,
				createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("service termination completed for orderId {} with status {}", orderCode, status);
		return createOrderResponse;
	}

}
