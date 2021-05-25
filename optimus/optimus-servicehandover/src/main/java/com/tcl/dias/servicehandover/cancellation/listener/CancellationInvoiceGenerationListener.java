package com.tcl.dias.servicehandover.cancellation.listener;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicehandover.cancellation.service.CancellationBillingInvoiceCreationService;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;

/**
 * InvoiceGenerationListener class for Invoice Generation
 * @author yomagesh
 *
 */
@Component
public class CancellationInvoiceGenerationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationInvoiceGenerationListener.class);
	
	@Autowired
	CancellationBillingInvoiceCreationService cancellationBillingInvoiceCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	/**
	 * Listener for Invoice Generation for IPC
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.cancellationInvoiceGenSync}") })
	public String generateInvoiceSync(String request) {
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			LOGGER.info("Cancellation generateInvoiceSync orderId{} serviceCode{}",orderId,serviceCode);
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			CreateInvoiceResponse createInvoiceResponse = cancellationBillingInvoiceCreationService.triggerCancellationInvoice(processInstanceId,orderId,serviceCode,Constants.GINVOICE,serviceId,serviceType);
			if(Objects.nonNull(createInvoiceResponse)) {
				String status = createInvoiceResponse.getAckResponse().getStatusCode();
				String errorMsg = createInvoiceResponse.getAckResponse().getErrorDescription();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in Cancellation generateInvoiceSync", e);
		}
		return null;
	}
}
