package com.tcl.dias.oms.service;


import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.GeoCodeRequestBean;
import com.tcl.dias.oms.beans.GeoCodeResponseBean;
import com.tcl.dias.oms.entity.entities.GeoCode;
import com.tcl.dias.oms.entity.repository.GeoCodeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class to deal with suretax api and geo code for address
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
@Service
public class GeoCodeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoCodeService.class);

	@Autowired
	RestClientService restClient;
	
	@Value("${sure.tax.api.url}")
	String sureTaxApiUrl;
	
	@Value("${sure.tax.api.validation.key}")
	String sureTaxApikey;
	
	@Value("${sure.tax.api.client.number}")
	String clientNumber;
	
	@Value("${sure.tax.api.host}")
	String sureTaxApiHost;
	
	@Autowired
	MstProductFamilyRepository productFamilyRepository;
	
	@Autowired
	MstProductComponentRepository productComponentRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	GeoCodeRepository geoCodeRepository;
	
	
	/**
	 * This method is to hit the suretax api and get the geo code for an address
	 * @author PARUNACH
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public GeoCodeResponseBean getGeoCodeForSite(GeoCodeRequestBean request) throws TclCommonException {
		LOGGER.debug("Sure tax api call Begins ");
		if (!validateRequest(request))
			throw new IllegalArgumentException(ExceptionConstants.REQUEST_INVALID);
		GeoCodeResponseBean response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Host", sureTaxApiHost);
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		try {
			request.setValidationKey(sureTaxApikey);
			request.setClientNumber(clientNumber);
			requestMap.add("Request", Utils.convertObjectToJson(request));
			Optional<GeoCode> geoCodeExisting = geoCodeRepository.findByZipcode(request.getZIPCode());
			if (!geoCodeExisting.isPresent()) {
				RestResponse restResponse = restClient.postWithUrlEncodedRequest(sureTaxApiUrl, requestMap, headers);
				if (restResponse != null && restResponse.getStatus().equals(Status.SUCCESS)
						&& !StringUtils.isEmpty(restResponse.getData())) {
					LOGGER.info("Received response from sure tax api ");
					String xmlResponse = restResponse.getData();
					org.json.JSONObject jsonObject = XML.toJSONObject(xmlResponse);
					jsonObject = jsonObject.getJSONObject("string");
					response = (GeoCodeResponseBean) Utils.convertJsonToObject(jsonObject.getString("content"),
							GeoCodeResponseBean.class);
					GeoCode geoCode = new GeoCode();
					geoCode.setZipcode(request.getZIPCode());
					geoCode.setGeoCode(Utils.convertObjectToJson(response));
					geoCodeRepository.save(geoCode);
				}
			} else
				response = (GeoCodeResponseBean) Utils.convertJsonToObject(geoCodeExisting.get().getGeoCode(),
						GeoCodeResponseBean.class);
		} catch (Exception exception) {
			LOGGER.error("Error while calling SureTax Api");
			throw new TclCommonException(exception.getMessage(), ResponseResource.R_CODE_ERROR);
		}
		LOGGER.debug("Sure tax api call ends");
		return response;
	}
	
	private boolean validateRequest(GeoCodeRequestBean request) {
		if(request == null || StringUtils.isEmpty(request.getPrimaryAddressLine()) || StringUtils.isEmpty(request.getCity()) || StringUtils.isEmpty(request.getState()) || StringUtils.isEmpty(request.getZIPCode()))
			return false;
		return true;
					
	}
	
	/*private void saveAsSiteProperty(Integer siteId, String productName, String geoCode) {
		
		MstProductFamily mstProductFamily = productFamilyRepository.findByNameAndStatus(productName,CommonConstants.BACTIVE);
		MstProductComponent mstProductComponent = productComponentRepository.findByName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
		ProductAttributeMaster productAttribute = productAttributeRepository.findByNameAndStatus(IllSitePropertiesConstants.GEO_CODE.getSiteProperties(), CommonConstants.BACTIVE).get(0);
		
		QuoteProductComponent quoteProductComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, mstProductComponent, mstProductFamily).get(0);
		if(quoteProductComponent == null ) {
			quoteProductComponent = new QuoteProductComponent();
			quoteProductComponent.setMstProductComponent(mstProductComponent);
			quoteProductComponent.setMstProductFamily(mstProductFamily);
			quoteProductComponent.setReferenceId(siteId);
			quoteProductComponentRepository.save(quoteProductComponent);
		}
		
		QuoteProductComponentsAttributeValue attributeValue = 
	}*/
}
