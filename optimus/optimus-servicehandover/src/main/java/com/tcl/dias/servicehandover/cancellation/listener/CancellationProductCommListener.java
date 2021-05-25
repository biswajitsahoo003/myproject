package com.tcl.dias.servicehandover.cancellation.listener;

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
import com.tcl.dias.servicehandover.cancellation.service.CancellationBillingProductCreationService;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class CancellationProductCommListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationProductCommListener.class);
	
	@Autowired
	CancellationBillingProductCreationService cancellationBillingProductCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	/**
	 * Listener for Product Commissioning
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.cancellationProductCommSync}") })
	public String createProductSync(String request) {
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode = req.split("#")[2];
			String serviceId = req.split("#")[4];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("Cancellation createIPCProdCommSync orderId{} serviceCode{}",orderId,serviceCode);
			CreateOrderResponse createOrderResponse = cancellationBillingProductCreationService.triggerCancellationProductCreation(orderId,processInstanceId,serviceType,serviceId,serviceCode);
			String status = "";
			if(createOrderResponse!=null) {
                status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
                LOGGER.info("createIPCProdCommSync Completed for orderId{} serviceCode{} with status {}",orderId,serviceCode,status);
                return status != null ? status : NetworkConstants.UNSUCCESSFUL;
            }
			LOGGER.info("Cancellation createIPCProdCommSync---- Completed for orderId{} serviceCode{} with status {}",orderId,serviceCode,status);
		} catch (Exception e) {
			LOGGER.error("Error in Cancellation createIPCProdCommSync: {}", e);
		}
		
		return "Success";
	}
}
