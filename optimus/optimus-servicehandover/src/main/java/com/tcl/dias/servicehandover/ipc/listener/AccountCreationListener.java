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
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcInternationalBillingAccountCreationService;

/**
 * AccountCreationListener for Account Creation
 * @author yomagesh
 *
 */
@Component
public class AccountCreationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountCreationListener.class);
	
	@Autowired
	IpcBillingAccountCreationService ipcBillingAccountCreationService;
	
	@Autowired
	IpcInternationalBillingAccountCreationService ipcInternationalBillingAccountCreationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	/**
	 * Listener for Account Creation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.accountCreateSync}") })
	public String createAccountSync(String request) {
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId= req.split("#")[3];
			String isInternationalBilling = req.split("#")[4];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("create Ipc AccountSync orderId: {} serviceCode: {} serviceType: {} serviceId: {} isInternationalBilling: {} ",orderId,serviceCode,serviceType,serviceId, Boolean.valueOf(isInternationalBilling));
			String status;
			if(!Boolean.valueOf(isInternationalBilling)) {
				CreateOrderResponse createOrderResponse = ipcBillingAccountCreationService.accountCreation(orderId,processInstanceId,serviceCode,serviceId,serviceType);
				if(Objects.nonNull(createOrderResponse)) {
					status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
					String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
					return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
				}
			} else {
				SetSECSProfileResponse secsProfileResponse = ipcInternationalBillingAccountCreationService
						.accountCreation(orderId, processInstanceId, serviceCode, serviceId, serviceType);
				if (secsProfileResponse != null) {
					status = "0".equals(secsProfileResponse.getSetSECSProfileOutput().getResponseHeader().getStatus().toString()) ? "Success" : "Failure";
					return status;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		return null;
	}
}
