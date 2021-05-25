package com.tcl.dias.serviceactivation.cramer.downtime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.GetServiceDowntimeDetails;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.GetServiceDowntimeDetailsResponse;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.ServiceDownDtlsResponse;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.ServiceDowntimeSubmitionFault;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.SubmitServiceDowntimeFault;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.SubmitServiceDowntimeFault2;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.SubmitServiceDowntimeFaultResponse;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.SubmitServiceDowntimeFaultResponse2;
import com.tcl.dias.serviceactivation.cramer.downtimeasync.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.GetMuxInfo;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.GetMuxInfoAck;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.SubmitMuxDetailFailureResponse;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Endpoint
public class GetTxDownTimeEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(GetTxDownTimeEndPoint.class);

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    @Autowired
    ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
    
    @Autowired
	ServiceDetailRepository serviceDetailRepository;

    @PayloadRoot(namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", localPart = "getServiceDowntimeDetails")
    @ResponsePayload
	public GetServiceDowntimeDetailsResponse processTxDownTimeSuccessRequest(@RequestPayload GetServiceDowntimeDetails request)
				throws TclCommonException {
    	logger.info("processTxDownTimeSuccessRequest started {} ", request);
    	NetworkInventory networkInventory = new NetworkInventory();
    	GetServiceDowntimeDetailsResponse getServiceDowntimeDetailsResponse= new GetServiceDowntimeDetailsResponse();
    	ServiceDownDtlsResponse serviceDownDtlsResponse = new ServiceDownDtlsResponse();
    	Acknowledgement ack = new Acknowledgement();
    	String errorMessage="";
    	String errorCode="";
    	 try {
    		 networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
    		 networkInventory.setType(CramerConstants.GET_DOWNTIME_ASYNC);
		    	if(Objects.nonNull(request)){
		    		if(Objects.nonNull(request.getServiceDowntimeDtls())){
		    			if (Objects.nonNull(request.getServiceDowntimeDtls().getRequestId())){
		    				logger.info("Tx Downtime RequestId exists");
		    				String requestId=request.getServiceDowntimeDtls().getRequestId();
		    				 String[] instanceId = requestId.split("#");
		    				 try {
		    						Thread.sleep(25000);
		    					} catch (Exception e) {
		    						logger.error("Exception in processTxDownTimeSuccessRequest-threadwait", e);
		    					}
		    				Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
		                            .activityId("get-tx-downtime-info-async").singleResult();
		    				 if (execution != null) {
		    					 logger.info("Execution exists for tx downtime");
		    					 runtimeService.setVariable(execution.getId(), "getTxDownTimeSuccess", true);
		                         runtimeService.setVariable(execution.getId(), "isTxDownTimeRequired", request.getServiceDowntimeDtls().isDowntimeFlag());
		                         ServiceDetail serviceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(request.getServiceDowntimeDtls().getServiceID(),"ISSUED");
		                 		if(Objects.nonNull(serviceDetail) && Objects.nonNull(request.getServiceDowntimeDtls().isDowntimeFlag())){
		                 			 logger.info("Set Downtime from Cramer");
		                 			serviceDetail.setIstxdowntimeReqd(request.getServiceDowntimeDtls().isDowntimeFlag()==true?(byte)1:(byte)0);
		                 			serviceDetailRepository.save(serviceDetail);
		                 			 Map<String, String> txDowntimeReqdMap = new HashMap<>();
			                 		 txDowntimeReqdMap.put("isTxDowntimeReqd", request.getServiceDowntimeDtls().isDowntimeFlag()==true?"true":"false");
			                 		 componentAndAttributeService.updateAttributes(serviceDetail.getScServiceDetailId(),txDowntimeReqdMap, "LM","A");
		                 		}else if(Objects.nonNull(serviceDetail) && Objects.isNull(request.getServiceDowntimeDtls().isDowntimeFlag())){
		                 			logger.info("Set Downtime");
		                 			serviceDetail.setIstxdowntimeReqd((byte)0);
		                 			serviceDetailRepository.save(serviceDetail);
		                 			Map<String, String> txDowntimeReqdMap = new HashMap<>();
			                 		txDowntimeReqdMap.put("isTxDowntimeReqd", "false");
			                 		componentAndAttributeService.updateAttributes(serviceDetail.getScServiceDetailId(),txDowntimeReqdMap, "LM","A");
		                 		}
		    				 }else{
		    					 logger.info("Execution not exists for tx downtime::",instanceId[1]);
		    				 }
		    				runtimeService.trigger(execution.getId());
		    				ack.setStatus(true);
		    				networkInventory.setServiceCode(StringUtils.trimToEmpty(request.getServiceDowntimeDtls().getServiceID()));
		    	            networkInventory.setRequestId(StringUtils.trimToEmpty(request.getServiceDowntimeDtls().getRequestId()));
		    			}else{
		    				logger.info("Request Id not exists ");
		    				ack.setStatus(false);
		    				ack.setErrorMessage("Request Id not exists");
		    			}
		    		}else{
		    			logger.info("Service DownTime Details not exists ");
		    			ack.setStatus(false);
		    			ack.setErrorMessage("DownTime Details not exists");
		    		}
		    	}else{
		    		logger.info("Request not exists ");
		    		ack.setStatus(false);
		    		ack.setErrorMessage("Request is empty");
		    	}
    	 } catch (Exception e) {
             logger.error(e.getMessage(), e);
             ack.setStatus(false);
             ack.setErrorCode("500");
             errorMessage = com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
 			 errorCode = "500";
             ack.setErrorMessage(e.getMessage());
         }
    	 serviceDownDtlsResponse.setAcknowledgement(ack);
    	 getServiceDowntimeDetailsResponse.setServiceDowntimeDtlsResponse(serviceDownDtlsResponse);
    	 try {
    		 logger.info("Saving txDowntime in network inventory");
             networkInventory.setRequest(Utils.convertObjectToXmlString(request, GetServiceDowntimeDetails.class));
             networkInventory.setResponse(Utils.convertObjectToXmlString(getServiceDowntimeDetailsResponse, GetServiceDowntimeDetailsResponse.class));
             networkInventoryRepository.save(networkInventory);
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
         }
    	 try {
             ScServiceDetail scServiceDetail = scServiceDetailRepository
                     .findByUuidAndMstStatus_code(request.getServiceDowntimeDtls().getServiceID(),TaskStatusConstants.INPROGRESS);
             if (scServiceDetail != null && !errorMessage.isEmpty() && !errorCode.isEmpty()) {
                 logger.info("getErrorMessageDetails condition meet for processTxDownTimeSuccessRequest");
                 componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
							"TxDownTimeErrorMessage",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "get-tx-downtime-info-async");
             }
         } catch (Exception e) {
             logger.info("processTxDownTimeFailureRequest error message capture   {} ", e);
         }
    	 logger.info("Return downtimedetail response",getServiceDowntimeDetailsResponse);
    	 return getServiceDowntimeDetailsResponse;
    }
    
    
    @PayloadRoot(namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", localPart = "submitServiceDowntimeFault")
    @ResponsePayload
	public SubmitServiceDowntimeFaultResponse processTxDownTimeFailureRequest(@RequestPayload SubmitServiceDowntimeFault request)
				throws TclCommonException {
    	logger.info("processTxDownTimeFailureRequest started {} ", request);
    	NetworkInventory networkInventory = new NetworkInventory();
    	SubmitServiceDowntimeFaultResponse submitServiceDowntimeFaultResponse= new SubmitServiceDowntimeFaultResponse();
    	SubmitServiceDowntimeFaultResponse2 submitServiceDowntimeFaultResponse2 = new SubmitServiceDowntimeFaultResponse2();
    	Acknowledgement ack = new Acknowledgement();
    	 try {
    		 networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
    		 networkInventory.setType(CramerConstants.GET_DOWNTIME_ASYNC);
		    	if(Objects.nonNull(request)){
		    		if(Objects.nonNull(request.getSubmitFault())){
		    			if (Objects.nonNull(request.getSubmitFault().getHeader())){
		    				logger.info("Tx Downtime Header exists");
		    				String requestId=request.getSubmitFault().getHeader().getRequestId();
		    				 String[] instanceId = requestId.split("#");
		    				 try {
		    						Thread.sleep(25000);
		    					} catch (Exception e) {
		    						logger.error("Exception in processTxDownTimeSuccessRequest-threadwait", e);
		    					}
		    				Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
		                            .activityId("get-tx-downtime-info-async").singleResult();
		    				 if (execution != null) {
		    					 logger.info("Execution exists for tx downtime fault");
		    					 runtimeService.setVariable(execution.getId(), "getTxDownTimeSuccess", false);		
		    					 runtimeService.setVariable(execution.getId(), "getTxDownTimeErrorMessage", request.getSubmitFault().getFault().getErrorLongDescription());
		    					 runtimeService.trigger(execution.getId());
		    				 }else{
		    					 logger.info("Execution not exists for tx downtime fault::{}",instanceId[1]);
		    				 }
		    				
		    				ack.setStatus(true);
		    				networkInventory.setServiceCode(StringUtils.trimToEmpty(request.getSubmitFault().getHeader().getServiceId()));
		    	            networkInventory.setRequestId(StringUtils.trimToEmpty(request.getSubmitFault().getHeader().getRequestId()));
		    	            
		    	            try {
		    	                ScServiceDetail scServiceDetail = scServiceDetailRepository
		    	                        .findByUuidAndMstStatus_code(request.getSubmitFault().getHeader().getServiceId(),TaskStatusConstants.INPROGRESS);

		    	                logger.info("getErrorMessageDetails condition meet for processTxDownTimeFailureRequest with serviceId:{}", request.getSubmitFault().getHeader().getServiceId());


		    	                if (scServiceDetail != null && request != null && request.getSubmitFault() != null) {
		    	                    logger.info("getErrorMessageDetails condition meet for processTxDownTimeFailureRequest");

		    	                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "TxDownTimeErrorMessage",
		    	                            getErrorMessageDetails(request.getSubmitFault()), AttributeConstants.ERROR_MESSAGE,
		    	                            "get-tx-downtime-info-async");
		    	                }
		    	            } catch (Exception e) {
		    	                logger.info("processTxDownTimeFailureRequest error message capture   {} ", e);
		    	            }
		    			}else{
		    				logger.info("Header not exists ");
		    				ack.setStatus(false);
		    				ack.setErrorMessage("Header not exists");
		    			}
		    		}else{
		    			logger.info("Fault not exists ");
		    			ack.setStatus(false);
		    			ack.setErrorMessage("Fault Details not exists");
		    		}
		    	}else{
		    		logger.info("Request not exists ");
		    		ack.setStatus(false);
		    		ack.setErrorMessage("Request is empty");
		    	}
    	 } catch (Exception e) {
             logger.error(e.getMessage(), e);
             ack.setStatus(false);
             ack.setErrorCode("00000");
             ack.setErrorMessage(e.getMessage());
         }
    	 submitServiceDowntimeFaultResponse2.setAcknowledgement(ack);
    	 submitServiceDowntimeFaultResponse.setSubmitFaultResponse(submitServiceDowntimeFaultResponse2);
    	 try {
    		 logger.info("Saving txDowntime fault response in network inventory");
             networkInventory.setRequest(Utils.convertObjectToXmlString(request, SubmitServiceDowntimeFault.class));
             networkInventory.setResponse(Utils.convertObjectToXmlString(submitServiceDowntimeFaultResponse, SubmitServiceDowntimeFaultResponse.class));
             networkInventoryRepository.save(networkInventory);
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
         }
    	 logger.info("Return downtimedetail fault response",submitServiceDowntimeFaultResponse);
    	 return submitServiceDowntimeFaultResponse;
    }
    
    private String getErrorMessageDetails(SubmitServiceDowntimeFault2 submitServiceDowntimeFault)
            throws TclCommonException {
        logger.info("getErrorMessageDetails started for downtime response");

        ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
        errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

        if (submitServiceDowntimeFault != null && submitServiceDowntimeFault.getFault() != null) {

            logger.info("getErrorMessageDetails forund");
            ServiceDowntimeSubmitionFault  message = submitServiceDowntimeFault.getFault();
            ErrorBean errorBean = new ErrorBean();
            errorBean.setErrorLongDescription(message.getErrorLongDescription());
            errorBean.setErrorCode(message.getErrorCode());
            errorBean.setErrorShortDescription(message.getErrorShortDescription());
            errorDetailsBean.getErrorDetails().add(errorBean);
        }

        return Utils.convertObjectToJson(errorDetailsBean);

    }
}