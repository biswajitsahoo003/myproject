package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.serviceactivation.beans.SetCLRSyncBean;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class SetClrSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(SetClrSyncListener.class);

	@Autowired
	private CramerService cramerService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.setclrsync}") })
	public String triggerSetCLRSyncCall(Message<String> responseBody) throws TclCommonException {
		logger.info("triggerSetCLRSyncCall invoked");
		Response response =null;
		
		try{
			try {
				response = cramerService.setCLR(responseBody.getPayload());
				
			} catch (Exception e) {
				logger.error("error in setClr", e);
			}
			
			SetCLRSyncBean setCLRSyncBean = (SetCLRSyncBean) Utils.convertJsonToObject(responseBody.getPayload(),
					SetCLRSyncBean.class);
			try{		
				cramerService.generateBillStartDate(null,null,setCLRSyncBean.getObjectName(),setCLRSyncBean.getServiceId());
			} catch (Exception e) {
				logger.error("error in SetClrSyncListener.generateBillStartDate", e);
			}
				
			try{
				cramerService.setActivationServiceActive(setCLRSyncBean.getServiceId(),setCLRSyncBean.getObjectName());
			} catch (Exception e) {
				logger.error("error in SetClrSyncListener.setActivationServiceActive", e);
			}
		}catch (Exception e) {
			logger.error("error in SetClrSyncListener.triggerSetCLRSyncCall", e);
		}
		return Utils.convertObjectToJson(response);
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${queue.setservice.active}") })
	public String triggerSetServiceActiveResponse(Message<String> responseBody) throws TclCommonException {
		logger.info("triggerSetServiceActiveResponse invoked");
		Response response = new Response();
		SetCLRSyncBean setCLRSyncBean = (SetCLRSyncBean) Utils.convertJsonToObject(responseBody.getPayload(),
				SetCLRSyncBean.class);	
		try{
			cramerService.setServiceActive(setCLRSyncBean.getServiceId(),setCLRSyncBean.getObjectName(),setCLRSyncBean.getOrderCode());
		} catch (Exception e) {
			logger.error("error in SetClrSyncListener.setServiceActive", e);
		}
		
		response.setStatus(true);
		return Utils.convertObjectToJson(response);
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${queue.setrenewalservice.active}") })
	public String triggerSetRenewalServiceActiveResponse(Message<String> responseBody) throws TclCommonException {
		logger.info("triggerSetRenewalServiceActiveResponse invoked");
		Response response = new Response();
		SetCLRSyncBean setCLRSyncBean = (SetCLRSyncBean) Utils.convertJsonToObject(responseBody.getPayload(),
				SetCLRSyncBean.class);	
		try{
			cramerService.setRenewalServiceActive(setCLRSyncBean.getServiceId(),setCLRSyncBean.getObjectName(),setCLRSyncBean.getOrderCode());
		} catch (Exception e) {
			logger.error("error in SetClrSyncListener.setServiceActive", e);
		}
		
		response.setStatus(true);
		return Utils.convertObjectToJson(response);
	}
}