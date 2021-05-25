package com.tcl.dias.oms.gst.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.gst.utils.GstAuthTokenUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GstinService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class GstInService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GstInService.class);

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	ApplicationContext appCtx;

	@Autowired
	RestClientService restClientService;

	@Value("${location.address.state.codevalidation.queue}")
	String validateStateQueue;

	@Autowired
	MQUtils mqUtils;
	
	@Value("${oms.gst.addr.url}")
	String gstAddressUrl;

	@Value("${oms.gst.auth.token.clientid}")
	String clientId;

	@Value("${oms.gst.auth.token.username}")
	String username;

	@Value("${oms.gst.auth.token.password}")
	String password;

	@Value("${oms.gst.auth.token.grant.type}")
	String grantType;

	@Value("${oms.gst.auth.token.client.secret}")
	String clientSecret;

	@Autowired
	IllSiteRepository quoteIllSitesRepository;

	/**
	 * The method get gst address for the given gstin
	 * 
	 * @param gstIn
	 * @return
	 * @throws TclCommonException
	 */
	public GstAddressBean getGstData(String gstIn, Integer orderId, Integer orderToLeId, Integer siteId)
			throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(siteId);
			LOGGER.info("Order Ill sites received {}", orderIllSite);
			if (orderIllSite.isPresent()) {
				Integer locationId = orderIllSite.get().getErfLocSitebLocationId();
				Map<String, Object> locationMapper = new HashMap<>();
				locationMapper.put("LOCATION_ID", locationId);
				locationMapper.put("STATE_CODE", gstIn.substring(0, 2));
				Boolean status = (Boolean) mqUtils.sendAndReceive(validateStateQueue,
						Utils.convertObjectToJson(locationMapper));
				if (status==null || !status) {
					gstAddressBean.setErrorMessage("Given GST is not associated with the state");
					return gstAddressBean;
				}
			} else {
				gstAddressBean.setErrorMessage("Unauthorised Access");
				return gstAddressBean;
			}
			getGstAddress(gstIn, gstAddressBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstAddressBean;
	}

	/**
	 * The method get gst address for the given gstin
	 *
	 * @param gstIn
	 * @return
	 * @throws TclCommonException
	 */
	public GstAddressBean getGstDataQuote(String gstIn, Integer quoteId, Integer quoteToLeId, Integer siteId)
			throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			Optional<QuoteIllSite> quoteIllSite = quoteIllSitesRepository.findById(siteId);
			LOGGER.info("Quote Ill sites received {}", quoteIllSite);
			if (quoteIllSite.isPresent()) {
				Integer locationId = quoteIllSite.get().getErfLocSitebLocationId();
				Map<String, Object> locationMapper = new HashMap<>();
				locationMapper.put("LOCATION_ID", locationId);
				locationMapper.put("STATE_CODE", gstIn.substring(0, 2));
				Boolean status = (Boolean) mqUtils.sendAndReceive(validateStateQueue,
						Utils.convertObjectToJson(locationMapper));
				if (status==null || !status) {
					gstAddressBean.setErrorMessage("Given GST is not associated with the state");
					return gstAddressBean;
				}
			} else {
				gstAddressBean.setErrorMessage("Unauthorised Access");
				return gstAddressBean;
			}
			getGstAddress(gstIn, gstAddressBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstAddressBean;
	}

	/**
	 * getGstAddress
	 * @param gstIn
	 * @param gstAddressBean
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public void getGstAddress(String gstIn, GstAddressBean gstAddressBean) throws TclCommonException, ParseException {
		String authToken = GstAuthTokenUtils.getGstAuthToken(appCtx);
		if (authToken != null) {
			Map<String, String> params = new HashMap<>();
			params.put("gstin", gstIn);
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer" + " " + authToken);
			headers.set("client_id", clientId);
			headers.set("content-type", "application/json");
			LOGGER.info("Calling url {}",gstAddressUrl);
			RestResponse response = restClientService
					.getWithQueryParamWithProxy(gstAddressUrl, params, headers);
			if (response != null) {
				LOGGER.info("Response for the gst address {}", response.getData());
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getData());
				Boolean errorFlg = (Boolean) jsonObj.get("error");
				if (!errorFlg) {
					if (jsonObj.get("data") != null) {
						JSONObject jsonObjData = (JSONObject) jsonObj.get("data");
						JSONObject jsonObjPradr = (JSONObject) jsonObjData.get("pradr");
						if (jsonObjPradr == null) {
							LOGGER.info("Constructing pradr data {}", jsonObjPradr);
							extractSecGstAddre(gstAddressBean, jsonObjData);
						} else {
							LOGGER.info("Constructing addr data");
							extractPrimAddress(gstAddressBean, jsonObjPradr);
						}
					}
				} else {
					gstAddressBean.setErrorMessage("The GSTIN passed in the request is invalid.");
				}
			}
		}
	}

	/**
	 * extractPrimAddress
	 * 
	 * @param gstAddressBean
	 * @param jsonObjPradr
	 */
	private void extractPrimAddress(GstAddressBean gstAddressBean, JSONObject jsonObjPradr) {
		JSONObject jsonObjPrAddr = (JSONObject) jsonObjPradr.get("addr");
		gstAddressBean.setState((String) jsonObjPrAddr.get("stcd"));
		gstAddressBean.setLocality((String) jsonObjPrAddr.get("loc"));
		gstAddressBean.setStreet((String) jsonObjPrAddr.get("st"));
		gstAddressBean.setPinCode((String) jsonObjPrAddr.get("pncd"));
		gstAddressBean.setBuildingNumber((String) jsonObjPrAddr.get("bno"));
		gstAddressBean.setBuildingName((String) jsonObjPrAddr.get("bnm"));
		gstAddressBean.setDistrict((String) jsonObjPrAddr.get("dst"));
		gstAddressBean.setLatitude((String) jsonObjPrAddr.get("lt"));
		gstAddressBean.setLongitude((String) jsonObjPrAddr.get("lg"));
		gstAddressBean.setFlatNumber((String) jsonObjPrAddr.get("flno"));
		gstAddressBean.setFullAddress(constructFullAddress(gstAddressBean));
	}

	/**
	 * extractSecGstAddre
	 * 
	 * @param gstAddressBean
	 * @param jsonObjData
	 */
	private void extractSecGstAddre(GstAddressBean gstAddressBean, JSONObject jsonObjData) {
		JSONArray jsonArrayAdadr = (JSONArray) jsonObjData.get("adadr");
		JSONObject jsonObjAdadr = (JSONObject) jsonArrayAdadr.get(0);
		extractPrimAddress(gstAddressBean, jsonObjAdadr);
	}

	private String constructFullAddress(GstAddressBean gstAddressBean) {
		String address = concatAddress(CommonConstants.EMPTY, gstAddressBean.getBuildingName());
		address = concatAddress(address, gstAddressBean.getBuildingNumber());
		address = concatAddress(address, gstAddressBean.getFlatNumber());
		address = concatAddress(address, gstAddressBean.getStreet());
		address = concatAddress(address, gstAddressBean.getLocality());
		address = concatAddress(address, gstAddressBean.getCity());
		address = concatAddress(address, gstAddressBean.getDistrict());
		address = concatAddress(address, gstAddressBean.getState());
		address = concatAddress(address, gstAddressBean.getPinCode());
		return address;
	}

	private String concatAddress(String address, String attr) {
		if (StringUtils.isBlank(address)) {
			return attr;
		}
		if (StringUtils.isNotBlank(attr)) {
			return address.concat(",").concat(attr);
		} else {
			return address;
		}
	}

}
