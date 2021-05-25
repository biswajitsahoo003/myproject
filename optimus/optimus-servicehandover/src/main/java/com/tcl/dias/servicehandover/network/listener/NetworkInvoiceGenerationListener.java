package com.tcl.dias.servicehandover.network.listener;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.service.BillingInvoiceCreationService;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;

/**
 * InvoiceGenerationListener class for Invoice Generation
 * @author yomagesh
 *
 */
@Component
public class NetworkInvoiceGenerationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkInvoiceGenerationListener.class);
	
	@Autowired
	BillingInvoiceCreationService billingInvoiceCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	/**
	 * Listener for Invoice Generation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.invoiceGenSync.network}") })
	public String generateInvoiceSync(String request) {
		LOGGER.info("inside generateInvoiceSync");
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			LOGGER.info("generateInvoiceSync orderId{} serviceCode{}",orderId,serviceCode);
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			if(serviceType.equals(IzosdwanCommonConstants.IZOSDWAN)) {
				serviceType = IzosdwanCommonConstants.IZOSDWAN_NAME;
			}
			CreateInvoiceResponse createInvoiceResponse = billingInvoiceCreationService.invoiceGeneration(processInstanceId,orderId,serviceCode,NetworkConstants.G_INVOICE,serviceId,serviceType);
			
			if(createInvoiceResponse!=null) {
				String status = createInvoiceResponse.getAckResponse().getStatusCode();
				String errorMsg = createInvoiceResponse.getAckResponse().getErrorDescription();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in generateInvoiceSync", e);
		}
		return "";
	}
}
