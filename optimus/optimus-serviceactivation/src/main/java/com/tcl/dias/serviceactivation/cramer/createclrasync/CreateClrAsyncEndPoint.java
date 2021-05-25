package com.tcl.dias.serviceactivation.cramer.createclrasync;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.ErrorDetails;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.SubmitFailureResponse;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.CLRCreationDetails;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.CLRCreationDetailsAcknowledgement;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.CLRcreationFailureResponse;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.GetCLRCreationDetails;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.GetCLRCreationDetailsResponse;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.SubmitCLRCreationFailureResponse;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.SubmitCLRCreationFailureResponse2;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.SubmitCLRCreationFailureResponseResponse;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Endpoint
public class CreateClrAsyncEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(CreateClrAsyncEndPoint.class);
    
    private static final String CREATE_CLR_ASYNC_ACTIVITY_ID = "enrich-service-design-async";
   
    @Autowired
    NetworkInventoryRepository networkInventoryRepository;
    
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${slave.fulfillment.trigger.queue}")
    String slaveFulfillmentTrigger;


    @PayloadRoot(namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails", localPart = "getCLRCreationDetails")
    @ResponsePayload
    @Transactional
    public GetCLRCreationDetailsResponse processCreateClrAsyncRequest(@RequestPayload GetCLRCreationDetails getCLRCreationDetails) {
        logger.info("CreateClrAsyncEndPoint::processCreateClrAsyncRequest {}", getCLRCreationDetails);
        
        Acknowledgement acknowledgement = new Acknowledgement();
        CLRCreationDetailsAcknowledgement clrCreationDetailsAcknowledgement = new CLRCreationDetailsAcknowledgement();
        clrCreationDetailsAcknowledgement.setAcknowledgement(acknowledgement);
        GetCLRCreationDetailsResponse getCLRCreationDetailsResponse =  new GetCLRCreationDetailsResponse();        
        getCLRCreationDetailsResponse.setCLRCreationDetailsAck(clrCreationDetailsAcknowledgement);
               
        NetworkInventory networkInventory = new NetworkInventory();       
        
        try {
        	if(getCLRCreationDetails !=null ) {
        		CLRCreationDetails clrCreationDetails = getCLRCreationDetails.getCLRCreationDetails();
	            networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
	            networkInventory.setServiceCode(StringUtils.trimToEmpty(clrCreationDetails.getServiceID()));
	            logger.info("processCreateClrAsyncRequest-received for serviceId={}", StringUtils.trimToEmpty(clrCreationDetails.getServiceID()));
	            networkInventory.setRequestId(StringUtils.trimToEmpty(clrCreationDetails.getRequestID()));
	            networkInventory.setType(CramerConstants.CREATE_CLR_ASYNC);
	            if (Objects.nonNull(clrCreationDetails.getRequestID())) {
	                acknowledgement.setStatus(true);
	            }else {
	            	acknowledgement.setStatus(false);
	            }
	            
	            triggerSuccessWorkFlow(clrCreationDetails,clrCreationDetails.getServiceID());
                triggerSlaveFulfillmentFlow(clrCreationDetails.getServiceID());
        	}else {
        		logger.info("getCLRCreationDetails is null ");
        		 acknowledgement.setStatus(false);
        		 acknowledgement.setErrorCode("500");
                 acknowledgement.setErrorMessage("getCLRCreationDetails is null");
        	}
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            acknowledgement.setStatus(false);
            acknowledgement.setErrorCode("500");
            acknowledgement.setErrorMessage(e.getMessage());
        }
        
        try {
        	networkInventory.setRequest(Utils.convertObjectToXmlString(getCLRCreationDetails,GetCLRCreationDetails.class));
        	networkInventory.setResponse(Utils.convertObjectToXmlString(getCLRCreationDetailsResponse,GetCLRCreationDetailsResponse.class));
            networkInventoryRepository.save(networkInventory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return getCLRCreationDetailsResponse;
    }

    @PayloadRoot(namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails", localPart = "submitCLRCreationFailureResponse")
    @ResponsePayload
    public SubmitCLRCreationFailureResponseResponse processSubmitCLRCreationFailureResponse(@RequestPayload SubmitCLRCreationFailureResponse submitCLRCreationFailureResponse) {
        logger.info("CreateClrAsyncEndPoint::processSubmitCLRCreationFailureResponse {}", submitCLRCreationFailureResponse);
        Acknowledgement acknowledgement = new Acknowledgement();
        CLRCreationDetailsAcknowledgement clrCreationDetailsAcknowledgement = new CLRCreationDetailsAcknowledgement();
        clrCreationDetailsAcknowledgement.setAcknowledgement(acknowledgement);
        SubmitCLRCreationFailureResponseResponse submitCLRCreationFailureResponseResponse = new SubmitCLRCreationFailureResponseResponse();
        submitCLRCreationFailureResponseResponse.setSubmitCLRCreationFailureAck(clrCreationDetailsAcknowledgement);
        
        NetworkInventory networkInventory = new NetworkInventory();
        try {
        	SubmitCLRCreationFailureResponse2 submitCLRCreationFailureResponse2 = submitCLRCreationFailureResponse.getSubmitCLRCreationFailure();
        	if(submitCLRCreationFailureResponse2!=null) {
	            networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
	            networkInventory.setServiceCode(StringUtils.trimToEmpty(submitCLRCreationFailureResponse2.getServiceId()));
	            networkInventory.setRequestId(StringUtils.trimToEmpty(submitCLRCreationFailureResponse2.getRequestId()));
	            networkInventory.setType(CramerConstants.CREATE_CLR_ASYNC);	    
	            acknowledgement.setStatus(true);	 
	            
	            triggerFailureWorkFlow(submitCLRCreationFailureResponse2,submitCLRCreationFailureResponse2.getServiceId());
	            
        	}else {
        		acknowledgement.setStatus(false);
        		acknowledgement.setErrorCode("000000");
                acknowledgement.setErrorMessage("SubmitCLRCreationFailureResponse is null");
        	}
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            acknowledgement.setStatus(false);
            acknowledgement.setErrorCode("000000");
            acknowledgement.setErrorMessage(e.getMessage());         
        }
        try {
        	networkInventory.setRequest(Utils.convertObjectToXmlString(submitCLRCreationFailureResponse,SubmitCLRCreationFailureResponse.class));
        	networkInventory.setResponse(Utils.convertObjectToXmlString(submitCLRCreationFailureResponseResponse,SubmitCLRCreationFailureResponseResponse.class));
            networkInventoryRepository.save(networkInventory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return submitCLRCreationFailureResponseResponse;
    }

    private void triggerSuccessWorkFlow(CLRCreationDetails clrCreationDetails, String serviceCode) {
        String processInstanceId = StringUtils.EMPTY;

         
        processInstanceId = clrCreationDetails.getRequestID();
        if (Objects.nonNull(processInstanceId)) {
        	String[] instanceId=processInstanceId.split("#");
        	processInstanceId=instanceId[1];
        	
        	try {
				Thread.sleep(5000);
			} catch (Exception e) {
				logger.error("Exception in processCreateClrAsyncRequest-threadwait", e);
			}

            Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                    .activityId(CREATE_CLR_ASYNC_ACTIVITY_ID).singleResult();
            if(execution!=null) {
                runtimeService.setVariable(execution.getId(), "serviceDesignCompleted", true);
                runtimeService.trigger(execution.getId());
                logger.info("processCreateClrAsyncRequest success triggered the process successfully  {} ", execution.getId());
            }else {
            	logger.info("processCreateClrAsyncRequest Execution is null for {}", processInstanceId);
            }
        }else {
        	logger.info("processInstanceId is null ");
        }
           
        
    }
    
	private void triggerFailureWorkFlow(SubmitCLRCreationFailureResponse2 failureResponse,String serviceCode) {
        String processInstanceId = StringUtils.EMPTY;
           
        processInstanceId = failureResponse.getRequestId();
        if (Objects.nonNull(processInstanceId)) {
        	String[] instanceId=processInstanceId.split("#");
        	processInstanceId=instanceId[1];

            Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                    .activityId(CREATE_CLR_ASYNC_ACTIVITY_ID).singleResult();
            if(execution!=null) {
                runtimeService.setVariable(execution.getId(), "serviceDesignCompleted", false);
                runtimeService.setVariable(execution.getId(),"serviceDesignCallFailureReason",failureResponse.getCLRCreationFailureResponse().getErrorLongDescription());
                runtimeService.trigger(execution.getId());
                ScServiceDetail scServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
                
                try {
                if(scServiceDetail!=null && failureResponse != null && failureResponse.getCLRCreationFailureResponse() != null) {
                	logger.info("getErrorMessageDetails condition meet for triggerFailureWorkFlow");

                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignCallFailureReason", getErrorMessageDetails(failureResponse),  AttributeConstants.ERROR_MESSAGE,CREATE_CLR_ASYNC_ACTIVITY_ID);	
                    }
                }
                catch (Exception e) {
                    logger.info("processCreateClrAsyncRequest error message capture   {} ", e);
				}
               
            
                
                logger.info("processCreateClrAsyncRequest success triggered the process successfully  {} ", execution.getId());
            }else {
            	logger.info("processCreateClrAsyncRequest Execution is null for {}", processInstanceId);
            }
        }else {
        	logger.info("processInstanceId is null ");
        }       

    }
	
	private String getErrorMessageDetails(SubmitCLRCreationFailureResponse2 failureResponse) throws TclCommonException {
		logger.info(" CreateClrAsyncEndPoint getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		if (failureResponse != null && failureResponse.getCLRCreationFailureResponse() != null) {
			logger.info("CreateClrAsyncEndPoint getErrorMessageDetails forund");
			CLRcreationFailureResponse errorDetails = failureResponse.getCLRCreationFailureResponse();
			ErrorBean errorBean = new ErrorBean();
			errorBean.setErrorLongDescription(errorDetails.getErrorLongDescription());
			errorBean.setErrorCode(errorDetails.getErrorCode());
			errorBean.setErrorShortDescription(errorDetails.getErrorShortDescription());
			errorDetailsBean.getErrorDetails().add(errorBean);
		}

		return Utils.convertObjectToJson(errorDetailsBean);

	}

	private void triggerSlaveFulfillmentFlow(String serviceCode)throws  TclCommonException{
        logger.info("inside triggerSlaveServiceFlow method : {}",serviceCode);
        ScServiceDetail scServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        if (("NEW".equalsIgnoreCase(scServiceDetail.getOrderType())
                || ("MACD".equalsIgnoreCase(scServiceDetail.getOrderType()) && "ADD_SITE".equalsIgnoreCase(scServiceDetail.getOrderCategory())))
                &&  scServiceDetail.getMultiVrfSolution() != null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution()) && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsMultiVrf())) {
            logger.info("master Vrf id : {}",scServiceDetail.getId());
            mqUtils.send(slaveFulfillmentTrigger,serviceCode);
        }
    }

}
