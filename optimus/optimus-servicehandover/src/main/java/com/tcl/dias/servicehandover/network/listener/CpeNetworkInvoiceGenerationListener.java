package com.tcl.dias.servicehandover.network.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.service.BillingInvoiceCreationService;

/**
 * InvoiceGenerationListener class for Invoice Generation
 * @author yomagesh
 *
 */
@Component
public class CpeNetworkInvoiceGenerationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CpeNetworkInvoiceGenerationListener.class);
	
	@Autowired
	BillingInvoiceCreationService billingInvoiceCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	/**
	 * Listener for Invoice Generation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.invoiceGenSync.network.cpe}") })
	public String generateCpeInvoiceSync(String request) {
		LOGGER.info("inside generateCpeInvoiceSync");
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			LOGGER.info("generateCpeInvoiceSync orderId{} serviceCode{}",orderId,serviceCode);
			//ServicehandoverAudit audit = servicehandoverAuditRepo.findByRequestTypeAndServiceIdAndServiceType(NetworkConstants.ACCOUNT_CREATION, serviceId,NetworkConstants.CPE);
			CreateInvoiceResponse createInvoiceResponse = billingInvoiceCreationService.invoiceGeneration(processInstanceId,orderId,serviceCode,NetworkConstants.CPE_INVOICE,serviceId,NetworkConstants.CPE);
			/*if(audit!=null){
				createInvoiceResponse = billingHandoverService.invoiceGeneration(audit,processInstanceId,orderId,serviceCode,NetworkConstants.CPE_INVOICE,serviceId);
			}*/
			if(createInvoiceResponse!=null) {
				String status = createInvoiceResponse.getAckResponse().getStatusCode();
				String errorMsg = createInvoiceResponse.getAckResponse().getErrorDescription();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in generateInvoiceSync", e);
		}
		return null;
	}
}
