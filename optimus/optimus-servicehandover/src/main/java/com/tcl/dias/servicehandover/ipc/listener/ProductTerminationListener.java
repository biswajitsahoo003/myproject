package com.tcl.dias.servicehandover.ipc.listener;

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
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingProductTerminationService;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class ProductTerminationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductTerminationListener.class);
	
	@Autowired
	IpcBillingProductTerminationService ipcBillingProductTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	/**
	 * Listener for Product Commissioning
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.productTermSync}") })
	public String terminateProductSync(String request) {
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode = req.split("#")[2];
			String orderType = req.split("#")[3];
			String serviceId = req.split("#")[4];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("terminateProductSync orderId{} serviceCode{}",orderId,serviceCode);
			CreateOrderResponse createOrderResponse = ipcBillingProductTerminationService.productTermination(orderId,processInstanceId,serviceType,serviceId,serviceCode);
			if(Objects.nonNull(createOrderResponse)) {
				String status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			} else {
				LOGGER.info("createIPCProdTermSync---- Completed for orderId{} serviceCode{} with status {}",orderId,serviceCode,IpcConstants.NO_RECORD_FOUND);
            	return IpcConstants.NO_RECORD_FOUND;
			}
		} catch (Exception e) {
			LOGGER.error("Error in terminateProductSync", e);
		}
		return "";
	}
}
