package com.tcl.dias.servicehandover.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.GeoCodeAddress;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.beans.sureaddress.AddressRequest;
import com.tcl.dias.servicehandover.beans.sureaddress.SoapRequest;
import com.tcl.dias.servicehandover.beans.sureaddress.SoapRequestResponse;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.SiteAddress;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This service handles are account logics for product across Optimus (IAS,GVPN,NPL)
 *  @author yogesh
 */

@Service
public class SureTaxService {
		
	
	@Autowired
	@Qualifier("SureAddress")
	SOAPConnector sureAddressConnector;

	@Value("${sureAddress}")
	private String sureAddressOperation;
	
	@Value("${sureAddressAction}")
	private String sureAddressOperationAction;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(SureTaxService.class);

	/**
	 * 
	 * Fetch Geo Code for USA and Canada for Taxation
	 * 
	 * @param geoCodeAddress
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public String generateGeoCode(GeoCodeAddress geoCodeAddress){
		LOGGER.info("generateGeoCode for address {} ", geoCodeAddress.toString());
		String geoCode = "Geo Code Not Available";
		SoapRequest soapRequest = new SoapRequest();
		AddressRequest addressRequest = new AddressRequest();
		addressRequest.setClientNumber("4001060472");
		addressRequest.setBusinessUnit("bizUnit");
		addressRequest.setFirmName("TCL");
		addressRequest.setValidationKey("7BD850F6-B16E-4D87-9BF8-461250200A57");
		addressRequest.setPrimaryAddressLine(StringUtils.trimToEmpty(geoCodeAddress.getAddressLineOne()));
		addressRequest.setSecondaryAddressLine(StringUtils.trimToEmpty(geoCodeAddress.getAddressLineTwo()));
		addressRequest.setUrbanization(StringUtils.trimToEmpty(geoCodeAddress.getAddressLineThree()));
		addressRequest.setCity(StringUtils.trimToEmpty(geoCodeAddress.getAddressCity()));
		addressRequest.setState(StringUtils.trimToEmpty(geoCodeAddress.getAddressState()));
		addressRequest.setCounty(StringUtils.trimToEmpty(geoCodeAddress.getAddressCountry()));
		addressRequest.setZIPCode(StringUtils.trimToEmpty(geoCodeAddress.getAddressZipCode()));
		addressRequest.setZIPPlus4("");
		addressRequest.setClientTracking("");
		addressRequest.setMatchCount(1);
		addressRequest.setResponseType("S");
		soapRequest.setRequest(addressRequest);
		JaxbMarshallerUtil.jaxbObjectToXML(soapRequest, new ServicehandoverAudit());
		SoapRequestResponse soapRequestResponse = (SoapRequestResponse) sureAddressConnector.webServiceWithCallAction(
				sureAddressOperation, soapRequest, new SoapActionCallback(sureAddressOperationAction));
		if(soapRequestResponse!=null) {
			geoCode = soapRequestResponse.getSoapRequestResult().getGeoCode();
		}
		LOGGER.info("Geo Code for the Provided Address is {}",geoCode);
		return geoCode;
		
	
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${queue.geo.code}") })
	public String geoCode(Message<String> responseBody) {
		LOGGER.info("inside Geo Code Generator for address {} ", responseBody);
		String geoCode = "";
		try {
			GeoCodeAddress geoCodeAddress = (GeoCodeAddress) Utils.convertJsonToObject(responseBody.getPayload(),
					GeoCodeAddress.class);
			if (geoCodeAddress != null) {
				geoCode = generateGeoCode(geoCodeAddress);
			}

		} catch (TclCommonException e) {
			LOGGER.error("Error in Fetching Geo Code for the Provided Address", e);
		}
		return geoCode;
	}
		
}
