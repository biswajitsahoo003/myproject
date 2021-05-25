package com.tcl.dias.servicehandover.network.listener;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.service.BillingInvoiceTerminationService;

/**
 * InvoiceGenerationListener class for Invoice Generation
 * @author yomagesh
 *
 */
@Component
public class InvoiceTerminationLRListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceTerminationLRListener.class);
	
	@Autowired
	BillingInvoiceTerminationService billingInvoiceTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	/**
	 * Listener for Invoice Generation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.invoice.termination.lr}") })
	public String terminationInvoiceSync(String request) {
		LOGGER.info("inside terminationInvoiceSync");
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			LOGGER.info("terminationInvoiceSync orderId{} serviceCode{}",orderCode,serviceCode);
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			
			CreateInvoiceResponse createInvoiceResponse = billingInvoiceTerminationService.invoiceTermination(processInstanceId,orderCode,serviceCode,NetworkConstants.G_INVOICE,serviceId,serviceType);
			if(createInvoiceResponse!=null) {
				String status = createInvoiceResponse.getAckResponse().getStatusCode();
				String errorMsg = createInvoiceResponse.getAckResponse().getErrorDescription();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in terminationInvoiceSync", e);
		}
		return null;
	}
}
