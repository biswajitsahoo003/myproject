package com.tcl.dias.serviceactivation.offnetdetails;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.offnetdetails.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.offnetdetails.beans.SetElectricalHandOffDetails;
import com.tcl.dias.serviceactivation.offnetdetails.beans.SetElectricalHandOffDetailsResponse;

/**
 * This class has methods to handle data of Offnet details (async)
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Endpoint
public class OffnetDetails {

	private static final Logger logger = LoggerFactory.getLogger(OffnetDetails.class);

	@Autowired
	RuntimeService runtimeService;	

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;

	 @PayloadRoot(namespace = "http://ACE_BSOHandOff_RadwinLMHandling_Module_TimeSlotsConsumptionExport1_1", localPart = "setElectricalHandOffDetails")
	    @ResponsePayload
	    public SetElectricalHandOffDetailsResponse processSetElectricalHandOffDetails(@RequestPayload SetElectricalHandOffDetails setElectricalHandOffDetails) {
	        logger.info("SetElectricalHandOffDetailsEndPoint::processSetElectricalHandOffDetails {}", setElectricalHandOffDetails);
	        
	        Acknowledgement acknowledgement = new Acknowledgement();
	        
	        SetElectricalHandOffDetailsResponse setElectricalHandOffDetailsResponse =  new SetElectricalHandOffDetailsResponse();        
	        setElectricalHandOffDetailsResponse.setAcknowledgement(acknowledgement);
	               
	        NetworkInventory networkInventory = new NetworkInventory();       
	        
	        /*try {
	        	if(setElectricalHandOffDetails !=null ) {
		            networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		            networkInventory.setServiceCode(StringUtils.trimToEmpty(setElectricalHandOffDetails.getHeader().getServiceID()));
		            networkInventory.setRequestId(StringUtils.trimToEmpty(setElectricalHandOffDetails.getHeader().getRequestID()));
		            networkInventory.setType(CramerConstants.SET_ELECTRICAL_HANDOFF);
		            if (Objects.nonNull(setElectricalHandOffDetails.getHeader().getRequestID())) {
		                acknowledgement.setStatus(true);
		            }else {
		            	acknowledgement.setStatus(false);
		            }		            
		           // triggerSuccessWorkFlow(setElectricalHandOffDetails);
	        	}else {
	        		logger.info("setElectricalHandOffDetails is null ");
	        		 acknowledgement.setStatus(false);
	        		 acknowledgement.setErrorCode("500");
	                 acknowledgement.setErrorMessage("setElectricalHandOffDetails is null");
	        	}
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            acknowledgement.setStatus(false);
	            acknowledgement.setErrorCode("500");
	            acknowledgement.setErrorMessage(e.getMessage());
	        }*/
	        
	        try {
	        	networkInventory.setRequest(Utils.convertObjectToXmlString(setElectricalHandOffDetails,SetElectricalHandOffDetails.class));
	        	networkInventory.setResponse(Utils.convertObjectToXmlString(setElectricalHandOffDetailsResponse,SetElectricalHandOffDetailsResponse.class));
	            networkInventoryRepository.save(networkInventory);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	        return setElectricalHandOffDetailsResponse;
	    }

}
