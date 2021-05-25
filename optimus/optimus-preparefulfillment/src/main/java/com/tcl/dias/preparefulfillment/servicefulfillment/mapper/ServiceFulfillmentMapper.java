package com.tcl.dias.preparefulfillment.servicefulfillment.mapper;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.common.fulfillment.beans.OdrTeamsDRCommercialBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SIContractInfoBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAssetAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.common.fulfillment.beans.OdrAssetRelationBean;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrCommercialBean;
import com.tcl.dias.common.fulfillment.beans.OdrContractInfoBean;
import com.tcl.dias.common.fulfillment.beans.OdrGstAddressBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrProductDetail;
import com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes;
import com.tcl.dias.common.fulfillment.beans.OdrServiceAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceCommercial;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceSlaBean;
import com.tcl.dias.common.fulfillment.beans.OdrWebexCommercialBean;
import com.tcl.dias.common.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetRelation;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceSla;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ServiceFulfillmentMapper.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class ServiceFulfillmentMapper {
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepo;
	
	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScAssetRepository scAssetRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Value("${application.env}")
	String appEnv;
	
	private final String ILL_CODE = "0300";
	private final String IWAN_CODE = "0511";
	private final String IPC_CODE = "0000";
	private final String GVPN_CODE = "6230";
	private final String NPL_CODE = "1258";
	private final String SDWAN_CODE = "34990";
	private final String SOLUTION_CODE = "SDSOL";
	private final String WEBEX_CODE = "UCAAS";
	private final String WEBEX_LICENCE_CODE = "SN-CCA-";
	private final String WEBEX_SOLUTION_CODE = "SDSOL";
	private final String GSC_CODE = "0002";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentMapper.class);
	/**
	 * mapEntityToBean
	 */
	public ScOrder mapOrderBeanToEntity(OdrOrderBean odrOrder) {
		ScOrder scOrder = null;
		if (odrOrder.getOrderType() != null && odrOrder.getOrderType().equalsIgnoreCase("Termination")) {

			scOrder = scOrderRepository.findByOpOrderCodeAndIsActiveAndIsMigratedOrder(odrOrder.getOpOrderCode(), "Y", "N");
		}
		if (scOrder == null) {
			scOrder = new ScOrder();
		}
		scOrder.setCreatedBy(odrOrder.getCreatedBy());
		scOrder.setCreatedDate(new Timestamp(new Date().getTime()));
		scOrder.setCustomerGroupName(odrOrder.getCustomerGroupName());
		scOrder.setCustomerSegment(odrOrder.getCustomerSegment());
		scOrder.setDemoFlag(odrOrder.getDemoFlag());
		scOrder.setErfCustCustomerId(odrOrder.getErfCustCustomerId());
		scOrder.setErfCustCustomerName(odrOrder.getErfCustCustomerName());
		scOrder.setErfCustLeId(odrOrder.getErfCustLeId());
		scOrder.setErfCustLeName(odrOrder.getErfCustLeName());
		scOrder.setErfCustPartnerId(odrOrder.getErfCustPartnerId());
		scOrder.setErfCustPartnerName(odrOrder.getErfCustPartnerName());
		scOrder.setErfCustPartnerLeId(odrOrder.getErfCustPartnerLeId());
		scOrder.setPartnerCuid(odrOrder.getPartnerCuid());
		scOrder.setErfCustSpLeId(odrOrder.getErfCustSpLeId());
		scOrder.setErfCustSpLeName(odrOrder.getErfCustSpLeName());
		scOrder.setErfUserCustomerUserId(odrOrder.getErfUserCustomerUserId());
		scOrder.setErfUserInitiatorId(odrOrder.getErfUserInitiatorId());
		scOrder.setUuid(odrOrder.getUuid());
		scOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
		scOrder.setUpdatedBy(odrOrder.getUpdatedBy());
		scOrder.setTpsSfdcCuid(odrOrder.getTpsSfdcCuid());
		scOrder.setTpsSecsId(odrOrder.getTpsSecsId());
		scOrder.setTpsSapCrnId(odrOrder.getTpsSapCrnId());
		scOrder.setTpsCrmSystem(odrOrder.getTpsCrmSystem());
		scOrder.setTpsCrmOptyId(odrOrder.getTpsCrmOptyId());
		scOrder.setTpsCrmCofId(odrOrder.getTpsCrmCofId());
		scOrder.setTpsSfdcOptyId(odrOrder.getSfdcOptyId());
		scOrder.setErfOrderId(odrOrder.getErfOrderId());
		scOrder.setSfdcAccountId(odrOrder.getSfdcAccountId());
		scOrder.setParentOpOrderCode(odrOrder.getParentOpOrderCode());
		scOrder.setParentId(odrOrder.getParentId());
		scOrder.setOrderType(odrOrder.getOrderType());
		scOrder.setOrderStatus(odrOrder.getOrderStatus());
		scOrder.setOrderStartDate(
				odrOrder.getOrderStartDate() != null ? new Timestamp(odrOrder.getOrderStartDate().getTime()) : null);
		scOrder.setOrderSource(odrOrder.getOrderSource());
		scOrder.setOrderEndDate(
				odrOrder.getOrderEndDate() != null ? new Timestamp(odrOrder.getOrderEndDate().getTime()) : null);
		scOrder.setOrderCategory(odrOrder.getOrderCategory());
		//scOrder.setOrderSubCategory(odrOrder.getOrderSubCategory());
		scOrder.setErfOrderLeId(odrOrder.getErfOrderLeId());

		scOrder.setOpOrderCode(odrOrder.getOpOrderCode());
		scOrder.setOpportunityClassification(odrOrder.getOpportunityClassification());
		scOrder.setIsActive(odrOrder.getIsActive());
		scOrder.setIsBundleOrder(odrOrder.getIsBundleOrder());
		scOrder.setIsMultipleLe(odrOrder.getIsMultipleLe());
		scOrder.setLastMacdDate(
				odrOrder.getLastMacdDate() != null ? new Timestamp(odrOrder.getLastMacdDate().getTime()) : null);
		scOrder.setMacdCreatedDate(
				odrOrder.getMacdCreatedDate() != null ? new Timestamp(odrOrder.getMacdCreatedDate().getTime()) : null);
		scOrder.setIsMigratedOrder("N");
		scOrder.setErfCustPartnerId(odrOrder.getErfCustPartnerId());
		scOrder.setErfCustPartnerLeId(odrOrder.getErfCustPartnerLeId());
		scOrder.setErfCustPartnerName(odrOrder.getErfCustPartnerName());
		scOrder.setPartnerCuid(odrOrder.getPartnerCuid());
		scOrder.setSiteLevelBilling(odrOrder.getSiteBillingFlag()?"Y":"N");
		if(odrOrder.getOdrGstAddressBean()!=null) {
			ScGstAddress scGstAddress=new ScGstAddress();
			scGstAddress.setBuildingName(odrOrder.getOdrGstAddressBean().getBuildingName());
			scGstAddress.setBuildingNumber(odrOrder.getOdrGstAddressBean().getBuildingNumber());
			scGstAddress.setCreatedBy(odrOrder.getOdrGstAddressBean().getCreatedBy());
			scGstAddress.setCreatedTime(odrOrder.getOdrGstAddressBean().getCreatedTime());
			scGstAddress.setDistrict(odrOrder.getOdrGstAddressBean().getDistrict());
			scGstAddress.setFlatNumber(odrOrder.getOdrGstAddressBean().getFlatNumber());
			scGstAddress.setLatitude(odrOrder.getOdrGstAddressBean().getLatitude());
			scGstAddress.setLocality(odrOrder.getOdrGstAddressBean().getLocality());
			scGstAddress.setLongitude(odrOrder.getOdrGstAddressBean().getLongitude());
			scGstAddress.setPincode(odrOrder.getOdrGstAddressBean().getPincode());
			scGstAddress.setState(odrOrder.getOdrGstAddressBean().getState());
			scGstAddress.setStreet(odrOrder.getOdrGstAddressBean().getStreet());
			scOrder.setScGstAddress(scGstAddress);
		}

		return scOrder;
	}

	public ScServiceDetail mapServiceEntityToBean(OdrServiceDetailBean odrServiceDetail,OdrOrderBean odrOrderBean) throws TclCommonException {
		ScServiceDetail scServiceDetail=null;
		if (odrOrderBean.getOrderType() != null && odrOrderBean.getOrderType().equalsIgnoreCase("Termination")) {
			scServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeAndIsMigratedOrder(odrServiceDetail.getUuid(), TaskStatusConstants.TERMINATION_INITIATED, "N");
		}
		
		if(scServiceDetail==null) {
			scServiceDetail = new ScServiceDetail();
		}
		scServiceDetail.setAccessType(Objects.nonNull(odrServiceDetail.getAccessType()) && "Onnet Wireless".equalsIgnoreCase(odrServiceDetail.getAccessType()) ? "OnnetRF" : odrServiceDetail.getAccessType()); 
		scServiceDetail.setArc(odrServiceDetail.getArc());
		scServiceDetail.setScOrderUuid(odrOrderBean.getOpOrderCode());

		scServiceDetail.setOrderSubCategory(odrServiceDetail.getOrderSubCategory());
		scServiceDetail.setBillingAccountId(odrServiceDetail.getBillingAccountId());
		scServiceDetail.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
		scServiceDetail.setBillingRatioPercent(odrServiceDetail.getBillingRatioPercent());
		scServiceDetail.setBillingType(odrServiceDetail.getBillingType());
		scServiceDetail.setBurstableBw(odrServiceDetail.getBurstableBwPortspeed());
		scServiceDetail.setBurstableBwUnit(odrServiceDetail.getBurstableBwUnit());
		scServiceDetail.setBwPortspeed(odrServiceDetail.getBwPortspeed());
		scServiceDetail.setBwUnit(odrServiceDetail.getBwUnit());
		scServiceDetail.setBwPortspeedAltName(odrServiceDetail.getBwPortspeedAltName());
		scServiceDetail.setCallType(odrServiceDetail.getCallType());
		scServiceDetail.setCreatedBy(odrServiceDetail.getCreatedBy());
		scServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setCustOrgNo(odrServiceDetail.getCustOrgNo());
		scServiceDetail.setDemarcationBuildingName(odrServiceDetail.getDemarcationApartment());
		scServiceDetail.setDemarcationFloor(odrServiceDetail.getDemarcationFloor());
		scServiceDetail.setDemarcationRack(odrServiceDetail.getDemarcationRack());
		scServiceDetail.setDemarcationRoom(odrServiceDetail.getDemarcationRoom());
		scServiceDetail.setDestinationCity(odrServiceDetail.getDestinationCity());
		scServiceDetail.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		scServiceDetail.setDestinationAddressLineOne(odrServiceDetail.getDestinationAddressLineOne());
		scServiceDetail.setDestinationAddressLineTwo(odrServiceDetail.getDestinationAddressLineTwo());
		scServiceDetail.setDestinationLocality(odrServiceDetail.getDestinationLocality());
		scServiceDetail.setDestinationPincode(odrServiceDetail.getDestinationPincode());
		scServiceDetail.setDestinationCountryCode(odrServiceDetail.getDestinationCountryCode());
		scServiceDetail.setDestinationState(odrServiceDetail.getDestinationState());
		scServiceDetail.setScOrderUuid(odrOrderBean.getOpOrderCode());
		scServiceDetail.setCustomerRequestorDate(odrServiceDetail.getCustomerRequestorDate());
		scServiceDetail.setIsMigratedOrder("N");
		if(odrOrderBean.getOpOrderCode()!=null && odrOrderBean.getOpOrderCode().toLowerCase().contains("izosdwan")){
			scServiceDetail.setIsBundle("Y");
		}
		scServiceDetail.setDestinationCountryCodeRepc(odrServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetail.setDiscountArc(odrServiceDetail.getDiscountArc());
		scServiceDetail.setDiscountMrc(odrServiceDetail.getDiscountMrc());
		scServiceDetail.setDiscountNrc(odrServiceDetail.getDiscountNrc());
		scServiceDetail.setErfLocDestinationCityId(odrServiceDetail.getErfLocDestinationCityId());
		scServiceDetail.setErfLocDestinationCountryId(odrServiceDetail.getErfLocDestinationCountryId());
		scServiceDetail.setErfLocPopSiteAddressId(odrServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetail.setErfLocSiteAddressId(odrServiceDetail.getErfLocSiteAddressId());
		scServiceDetail.setErfLocSourceCityId(odrServiceDetail.getErfLocSourceCityId());
		scServiceDetail.setErfLocSrcCountryId(odrServiceDetail.getErfLocSrcCountryId());
		scServiceDetail.setErfPrdCatalogOfferingId(odrServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetail.setErfPrdCatalogOfferingName(odrServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogFlavourName(odrServiceDetail.getErfPrdCatalogFlavourName());
		scServiceDetail.setErfPrdCatalogParentProductOfferingName(
				odrServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetail.setErfPrdCatalogProductId(odrServiceDetail.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogProductName(odrServiceDetail.getErfPrdCatalogProductName());
		scServiceDetail.setFeasibilityId(odrServiceDetail.getFeasibilityId());
		scServiceDetail.setGscOrderSequenceId(odrServiceDetail.getGscOrderSequenceId());
		scServiceDetail.setIsActive(odrServiceDetail.getIsActive());
		scServiceDetail.setIsIzo(odrServiceDetail.getIsIzo());
		scServiceDetail.setLastmileBw(odrServiceDetail.getLastmileBw());
		scServiceDetail.setLastmileBwAltName(odrServiceDetail.getLastmileBwAltName());
		scServiceDetail.setLastmileBwUnit(odrServiceDetail.getLastmileBwUnit());		
		scServiceDetail.setLastmileProvider(odrServiceDetail.getLastmileProvider());
		scServiceDetail.setLastmileType(odrServiceDetail.getLastmileType());
		scServiceDetail.setParentUuid(odrServiceDetail.getParentServiceUuid());
		scServiceDetail.setOrderCategory(odrServiceDetail.getOrderCategory());
		scServiceDetail.setOrderType(odrServiceDetail.getOrderType());

		
		if(odrServiceDetail.getOdrGstAddress()!=null) {
			ScGstAddress scGstAddress=new ScGstAddress();
			scGstAddress.setBuildingName(odrServiceDetail.getOdrGstAddress().getBuildingName());
			scGstAddress.setBuildingNumber(odrServiceDetail.getOdrGstAddress().getBuildingNumber());
			scGstAddress.setCreatedBy(odrServiceDetail.getOdrGstAddress().getCreatedBy());
			scGstAddress.setCreatedTime(odrServiceDetail.getOdrGstAddress().getCreatedTime());
			scGstAddress.setDistrict(odrServiceDetail.getOdrGstAddress().getDistrict());
			scGstAddress.setFlatNumber(odrServiceDetail.getOdrGstAddress().getFlatNumber());
			scGstAddress.setLatitude(odrServiceDetail.getOdrGstAddress().getLatitude());
			scGstAddress.setLocality(odrServiceDetail.getOdrGstAddress().getLocality());
			scGstAddress.setLongitude(odrServiceDetail.getOdrGstAddress().getLongitude());
			scGstAddress.setPincode(odrServiceDetail.getOdrGstAddress().getPincode());
			scGstAddress.setState(odrServiceDetail.getOdrGstAddress().getState());
			scGstAddress.setStreet(odrServiceDetail.getOdrGstAddress().getStreet());
			scServiceDetail.setScGstAddress(scGstAddress);
		}
		
				
		try {
			if (odrOrderBean.getErfCustCustomerName() != null && !odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				LOGGER.info("odrOrderBean.getErfCustCustomerName()={}",odrOrderBean.getErfCustCustomerName());
				if("ADD_SITE".equals(odrOrderBean.getOrderCategory())){
					scServiceDetail.setVpnSolutionId(StringUtils.trimToEmpty(splitCustomerNameMax(odrOrderBean.getErfCustCustomerName())));
				}else{
					scServiceDetail.setVpnSolutionId(StringUtils.trimToEmpty(splitCustomerName(odrOrderBean.getErfCustCustomerName())));
				}
			
			}
			
			/*
			 * if(odrServiceDetail.getLastmileBw()!=null) { Integer localLoopBw =
			 * Integer.valueOf(odrServiceDetail.getLastmileBw()); if(localLoopBw <
			 * 622)scServiceDetail.setLineRate("STM4"); else if(localLoopBw > 622 &&
			 * localLoopBw < 2500)scServiceDetail.setLineRate("STM16"); else if(localLoopBw
			 * > 2500)scServiceDetail.setLineRate("STM64"); }
			 */			
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in LineRate => {}", e);
		}
	
		
		
		scServiceDetail.setLatLong(odrServiceDetail.getLatLong());
		scServiceDetail.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
		scServiceDetail.setLocalItContactMobile(odrServiceDetail.getLocalItContactMobile());
		scServiceDetail.setLocalItContactName(odrServiceDetail.getLocalItContactName());
		scServiceDetail.setMrc(odrServiceDetail.getMrc());
		scServiceDetail.setNrc(odrServiceDetail.getNrc());
		scServiceDetail.setDifferentialMrc(odrServiceDetail.getDifferentialMrc());
		scServiceDetail.setDifferentialNrc(odrServiceDetail.getDifferentialNrc());
		// odrServiceDetailBean.setOdrContractInfo(odrContractInfo);
		// odrServiceDetailBean.setOdrOrder(odrServiceDetail.geto);
		if(odrOrderBean.getOrderType() != null && odrOrderBean.getOrderType().equalsIgnoreCase("Termination")) {
			scServiceDetail.setScOrderUuid(odrOrderBean.getOpOrderCode());		

		}else {
			scServiceDetail.setScOrderUuid(odrServiceDetail.getOdrOrderUuid());
		}
		
		String connectedBuilding ="";
		String connectedCustomer = "";
		String lmScenarioType = "";
		String lmConnectionType = "Wireline";
		String additionalIps="";
		String additionalIpsType="";
		String additionalIPv4IPs ="";
		String additionalIPv6IPs="";
		int totalAdditionalIps=0;
		
		Set<ScServiceAttribute> scServiceAttrBeans = new HashSet<>();
		for (OdrServiceAttributeBean odrServiceAttribute : odrServiceDetail.getOdrServiceAttributes()) {
			
			if(odrServiceAttribute.getAttributeName().equals("Service Variant")) {
				scServiceDetail.setServiceVariant(odrServiceAttribute.getAttributeValue());
			}else if(odrServiceAttribute.getAttributeName().equals("connected_building_tag")) {
				connectedBuilding = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());				
			}else if(odrServiceAttribute.getAttributeName().equals("connected_cust_tag")) {				
				connectedCustomer = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());				
			}
			if(odrServiceAttribute.getAttributeName().equals("Additional IPs")) {
				LOGGER.info("additional ip {} ",StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue()));
				additionalIps = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IP Address Arrangement for Additional IPs")) {
				LOGGER.info("additional ip pool type{} ",StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue()));
				additionalIpsType = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IPv4 Address Pool Size for Additional IPs")) {
				 additionalIPv4IPs = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IPv6 Address Pool Size for Additional IPs") ) {
				additionalIPv6IPs =  StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if("Site Type".equals(odrServiceAttribute.getAttributeName())){
				scServiceDetail.setSiteTopology(odrServiceAttribute.getAttributeValue());
			}
			if("VPN Topology".equals(odrServiceAttribute.getAttributeName())){
				scServiceDetail.setServiceTopology(odrServiceAttribute.getAttributeValue());
			}
			ScServiceAttribute serviceAttr = mapServiceAttrEntityToBean(odrServiceAttribute);
			serviceAttr.setScServiceDetail(scServiceDetail);
			scServiceAttrBeans.add(serviceAttr);
		}
		try {
			if ("yes".equalsIgnoreCase(additionalIps) || "y".equalsIgnoreCase(additionalIps)) {
				if ("ipv4".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
				} else if ("ipv6".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv6IPs, 64);
				} else {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
					totalAdditionalIps = totalAdditionalIps + getTotalAdditionalIps(additionalIPv6IPs, 64);
				}
				scServiceDetail.setNoOfAdditionalIps(String.valueOf(totalAdditionalIps));
				scServiceDetail.setAdditionalIpPoolType(additionalIpsType);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured during additional IPs for Network products {}", exception);
		}

		String lastMileType = StringUtils.trimToEmpty(odrServiceDetail.getLastmileType());
		if (lastMileType.toLowerCase().contains("onnetrf") || lastMileType.toLowerCase().contains("onnet wireless")) {
			lmScenarioType = "Onnet Wireless";
			lmConnectionType = "Wireless";
		} else if (lastMileType.toLowerCase().contains("offnetrf") || lastMileType.toLowerCase().contains("offnet wireless")) {
			lmScenarioType = "Offnet Wireless";
			lmConnectionType = "Wireless";
		} else if (lastMileType.toLowerCase().contains("offnetwl") || lastMileType.toLowerCase().contains("offnet wireline")) {
			lmScenarioType = "Offnet Wireline";
			lmConnectionType = "Wireline";
		} else {
			lmScenarioType = "Onnet Wireline";
			lmConnectionType = "Wireline";		
			LOGGER.info("connectedBuilding={},connectedCustomer={},OrderType={},OrderCategory={}",connectedBuilding,connectedCustomer,odrOrderBean.getOrderType(),odrOrderBean.getOrderCategory());
			if (connectedCustomer.contains("1")  || (StringUtils.trimToEmpty(odrOrderBean.getOrderType()).equalsIgnoreCase("MACD")
					&& (StringUtils.trimToEmpty(odrOrderBean.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
							|| StringUtils.trimToEmpty(odrOrderBean.getOrderCategory()).equalsIgnoreCase("ADD_IP")
							|| (odrOrderBean.getOpOrderCode().toLowerCase().contains("izosdwan")
							&& "CHANGE_ORDER".equalsIgnoreCase(odrOrderBean.getOrderCategory()))))) {
				lmScenarioType = "Onnet Wireline - Connected Customer";
			} else if (connectedBuilding.contains("1")) {
				lmScenarioType = "Onnet Wireline - Connected Building";
			} else {
				lmScenarioType = "Onnet Wireline - Near Connect";
			}
		}

		scServiceDetail.setLastmileScenario(lmScenarioType);
		scServiceDetail.setLastmileConnectionType(lmConnectionType);
		scServiceDetail.setScServiceAttributes(scServiceAttrBeans);
		Set<ScServiceSla> odrServiceSlaBeans = new HashSet<>();
		for (OdrServiceSlaBean odrServiceSla : odrServiceDetail.getOdrServiceSlas()) {
			ScServiceSla serviceSla = mapServiceSlaEntityToBean(odrServiceSla);
			serviceSla.setScServiceDetail(scServiceDetail);
			odrServiceSlaBeans.add(serviceSla);
		}
		scServiceDetail.setScServiceSlas(odrServiceSlaBeans);
		scServiceDetail.setParentBundleServiceId(odrServiceDetail.getParentBundleServiceId());
		scServiceDetail.setParentId(odrServiceDetail.getParentId());
		scServiceDetail.setSourceCountry(odrServiceDetail.getSourceCountry());
		scServiceDetail.setSourceCountryCode(odrServiceDetail.getSourceCountryCode());
		scServiceDetail.setSourceCountryCodeRepc(odrServiceDetail.getSourceCountryCodeRepc());
		scServiceDetail.setPrimarySecondary(odrServiceDetail.getPrimarySecondary()!=null?odrServiceDetail.getPrimarySecondary():"Primary");
		scServiceDetail.setErdPriSecServiceLinkId(odrServiceDetail.getErfPriSecServiceLinkId());
		scServiceDetail.setProductReferenceId(odrServiceDetail.getProductReferenceId());
		scServiceDetail.setServiceClass(odrServiceDetail.getServiceClass());
		scServiceDetail.setServiceClassification(odrServiceDetail.getServiceClassification());
		scServiceDetail.setServiceCommissionedDate(odrServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(odrServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setRrfsDate(odrServiceDetail.getRrfsDate() != null
				? new Timestamp(odrServiceDetail.getRrfsDate().getTime())
				: null);
		scServiceDetail.setServiceGroupId(odrServiceDetail.getServiceGroupId());
		scServiceDetail.setServiceGroupType(odrServiceDetail.getServiceGroupType());
		scServiceDetail.setServiceStatus(odrServiceDetail.getServiceStatus());
		scServiceDetail.setServiceTerminationDate(odrServiceDetail.getServiceTerminationDate() != null
				? new Timestamp(odrServiceDetail.getServiceTerminationDate().getTime())
				: null);
		scServiceDetail.setServiceTopology(odrServiceDetail.getServiceTopology());
		scServiceDetail.setSiteAlias(odrServiceDetail.getSiteAlias());
		scServiceDetail.setSiteAddress(odrServiceDetail.getSiteAddress());
		scServiceDetail.setSiteEndInterface(odrServiceDetail.getSiteEndInterface());
		scServiceDetail.setSiteLinkLabel(odrServiceDetail.getSiteLinkLabel());
		scServiceDetail.setSiteTopology(odrServiceDetail.getSiteTopology());
		scServiceDetail.setSlaTemplate(odrServiceDetail.getSlaTemplate());
		scServiceDetail.setSmEmail(odrServiceDetail.getSmEmail());
		scServiceDetail.setSupplOrgNo(odrServiceDetail.getSupplOrgNo());
		scServiceDetail.setTpsCopfId(odrServiceDetail.getTpsCopfId());
		scServiceDetail.setTpsServiceId(odrServiceDetail.getTpsServiceId());
		scServiceDetail.setTpsSourceServiceId(odrServiceDetail.getTpsSourceServiceId());
		scServiceDetail.setUpdatedBy(odrServiceDetail.getUpdatedBy());
		scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
		if (odrServiceDetail.getIsAmended() != null
				&& odrServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
			LOGGER.info("Inside Order amended service with odr service id {} :", odrServiceDetail.getId());
			scServiceDetail.setIsAmended(CommonConstants.Y);

			String uuid = odrServiceDetail.getAmendedServiceId() != null
					&& !"NA".equalsIgnoreCase(odrServiceDetail.getAmendedServiceId())
							? odrServiceDetail.getAmendedServiceId()
							: odrServiceDetail.getUuid() != null ? odrServiceDetail.getUuid() : null;

			if (uuid == null) {
				LOGGER.info("Inside Order amended service with odr service id and uuid is null for amended order {} :",
						odrServiceDetail.getId());
				throw new TclCommonException(ExceptionConstants.ORDER_AMENDMENT_ISSUE, ResponseResource.R_CODE_ERROR);

			}
			scServiceDetail.setUuid(uuid);
		}else {
			if(odrServiceDetail.getOdrOrderUuid().toLowerCase().contains("izosdwan") || odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")){
				LOGGER.info("SDWAN Order for Service Code generation::{}",odrOrderBean.getOpOrderCode());
				scServiceDetail.setUuid(odrServiceDetail.getUuid() != null ? odrServiceDetail.getUuid() : getIzoSdwanServiceCode(odrServiceDetail.getErfPrdCatalogProductName(),
						(StringUtils.isNotBlank(odrServiceDetail.getDestinationCountry())?odrServiceDetail.getDestinationCountry():CommonConstants.EMPTY),(StringUtils.isNotBlank(odrServiceDetail.getDestinationCity())?odrServiceDetail.getDestinationCity():CommonConstants.EMPTY), String.valueOf(odrServiceDetail.getId()),odrServiceDetail.getIntlDestinationCntryCode()));
			}else if(odrServiceDetail.getOdrOrderUuid().toLowerCase().contains("ucwb")){
				scServiceDetail.setUuid(odrServiceDetail.getUuid()!=null ? odrServiceDetail.getUuid():getWebexServiceCode(odrServiceDetail.getErfPrdCatalogProductName(),
						(StringUtils.isNotBlank(odrServiceDetail.getDestinationCity())?odrServiceDetail.getDestinationCity():CommonConstants.EMPTY), String.valueOf(odrServiceDetail.getId()), odrServiceDetail.getErfPrdCatalogOfferingName()));
			}else{
				LOGGER.info("Other than SDWAN Order for Service Code generation::{}",odrOrderBean.getOpOrderCode());
				scServiceDetail.setUuid(odrServiceDetail.getUuid() != null ? odrServiceDetail.getUuid() : getServiceCode(odrServiceDetail.getErfPrdCatalogProductName(),
						(StringUtils.isNotBlank(odrServiceDetail.getDestinationCity())?odrServiceDetail.getDestinationCity():CommonConstants.EMPTY), String.valueOf(odrServiceDetail.getId())));
			} 
		}

		
		if("MACD".equals(odrOrderBean.getOrderType()) && !odrOrderBean.getOpOrderCode().toLowerCase().contains("izosdwan")){
			LOGGER.info("OrderCode::{}",odrOrderBean.getOpOrderCode());
				ScServiceDetail prevActiveServiceDetail=null;
				if(Objects.nonNull(odrServiceDetail.getUuid())){
					LOGGER.info("Link Id for UUID::{}",odrServiceDetail.getUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(odrServiceDetail.getUuid(), "ACTIVE");
				}else if(Objects.nonNull(odrServiceDetail.getParentServiceUuid())){
					LOGGER.info("Link Id for Parent UUID::{}",odrServiceDetail.getParentServiceUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(odrServiceDetail.getParentServiceUuid(), "ACTIVE");
				}
				scServiceDetail.setServiceLinkId(Objects.nonNull(prevActiveServiceDetail)?prevActiveServiceDetail.getId():null);
		}
		scServiceDetail.setErfOdrServiceId(odrServiceDetail.getId());
		scServiceDetail.setVpnName(odrServiceDetail.getVpnName());
		scServiceDetail.setOrderCategory(odrServiceDetail.getOrderCategory());
		scServiceDetail.setIntlDestinationDialCode(odrServiceDetail.getIntlDestinationCntryCode());
		scServiceDetail.setOrderType(odrServiceDetail.getOrderType());
		scServiceDetail.setOrderSubCategory(odrServiceDetail.getOrderSubCategory());
		if(odrServiceDetail.getOdrAssetBeans() != null) {
			List<ScAsset> scAssets=new ArrayList<>();
			for(OdrAssetBean assetBean:odrServiceDetail.getOdrAssetBeans())
			{
				ScAsset asset=new ScAsset();
				asset.setCreatedBy(assetBean.getCreatedBy());
				asset.setCreatedDate(DateUtil.convertStringToTimestamp(assetBean.getCreatedDate()));
				asset.setFqdn(assetBean.getFqdn());
				asset.setIsActive(assetBean.getIsActive());
				asset.setIsSharedInd(assetBean.getIsSharedInd());
				asset.setOriginnetwork(assetBean.getOriginnetwork());
				asset.setType(assetBean.getType());
				asset.setUpdatedBy(assetBean.getUpdatedBy());
				asset.setUpdatedDate(DateUtil.convertStringToTimestamp(assetBean.getUpdatedDate()));
				asset.setName(assetBean.getName());
				asset.setScServiceDetail(scServiceDetail);
				asset.setPublicIp(assetBean.getPublicIp());
				asset.setOdrAssetId(assetBean.getId());
				if(assetBean.getOdrAssetAttributeBean() != null) {
					assetBean.getOdrAssetAttributeBean().forEach(odrAssetAttributeBean -> {
						asset.addScAssetAttributes(mapScAssetAttribute(asset, odrAssetAttributeBean));	
					});
				}
				if(assetBean.getOdrAssetRelationBeans() != null) {
					assetBean.getOdrAssetRelationBeans().forEach(OdrAssetRelationBean -> {
						asset.addScAssetRelations(mapScAssetRelation(OdrAssetRelationBean));	
					});
				}
				scAssets.add(asset);
			}
			scServiceDetail.setScAssets(scAssets);
		}
		scServiceDetail.setIsMultiVrf(odrServiceDetail.getIsMultiVrf());
		scServiceDetail.setMasterVrfServiceId(odrServiceDetail.getMasterVrfServiceId());
		scServiceDetail.setMultiVrfSolution(odrServiceDetail.getMultiVrfSolution());
		scServiceDetail.setServiceOption(odrServiceDetail.getServiceOption());
		/*scAssetRepository.saveAll(scAssets);*/
		return scServiceDetail;
	}

	public ScAssetAttribute mapScAssetAttribute(ScAsset scAsset, OdrAssetAttributeBean odrAssetAttributeBean) {
		ScAssetAttribute scAssetAttribute = new ScAssetAttribute();
		scAssetAttribute.setAttributeAltValueLabel(odrAssetAttributeBean.getAttributeAltValueLabel());
		scAssetAttribute.setAttributeName(odrAssetAttributeBean.getAttributeName());
		scAssetAttribute.setAttributeValue(odrAssetAttributeBean.getAttributeValue());
		scAssetAttribute.setCategory(odrAssetAttributeBean.getCategory());
		scAssetAttribute.setCreatedBy(odrAssetAttributeBean.getCreatedBy());
		scAssetAttribute.setIsActive(odrAssetAttributeBean.getIsActive());
		scAssetAttribute.setUpdatedBy(odrAssetAttributeBean.getUpdatedBy());
		scAssetAttribute.setUpdatedDate(odrAssetAttributeBean.getUpdatedDate());
		scAssetAttribute.setScAsset(scAsset);
		return scAssetAttribute;
	}
	
	public ScAssetRelation mapScAssetRelation(OdrAssetRelationBean odrAssetRelationBean) {
		ScAssetRelation scAssetRelation = new ScAssetRelation();
		scAssetRelation.setScAssetId(odrAssetRelationBean.getOdrAssetId());
		scAssetRelation.setScRelatedAssetId(odrAssetRelationBean.getOdrRelatedAssetId());
		scAssetRelation.setBusinessRelationName(odrAssetRelationBean.getBusinessRelationName());
		scAssetRelation.setEndDate(odrAssetRelationBean.getEndDate());
		scAssetRelation.setIsActive(odrAssetRelationBean.getIsActive());
		scAssetRelation.setRelationPort(odrAssetRelationBean.getRelationPort());
		scAssetRelation.setRelationResiliency(odrAssetRelationBean.getRelationResiliency());
		scAssetRelation.setRelationType(odrAssetRelationBean.getRelationType());
		scAssetRelation.setRemarks(odrAssetRelationBean.getRemarks());
		scAssetRelation.setStartDate(odrAssetRelationBean.getStartDate());
		scAssetRelation.setCreatedBy(odrAssetRelationBean.getCreatedBy());
		scAssetRelation.setCreatedDate(odrAssetRelationBean.getCreatedDate());
		scAssetRelation.setUpdatedBy(odrAssetRelationBean.getUpdatedBy());
		scAssetRelation.setUpdatedDate(odrAssetRelationBean.getUpdatedDate());
		return scAssetRelation;
	}
	
	public ScServiceDetail mapIPCServiceEntityToBean(OdrServiceDetailBean odrServiceDetail, OdrOrderBean odrOrderBean) {
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		scServiceDetail.setAccessType(odrServiceDetail.getAccessType());
		scServiceDetail.setArc(odrServiceDetail.getArc());
		scServiceDetail.setBillingAccountId(odrServiceDetail.getBillingAccountId());
		scServiceDetail.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
		scServiceDetail.setBillingRatioPercent(odrServiceDetail.getBillingRatioPercent());
		scServiceDetail.setBillingType(odrServiceDetail.getBillingType());
		scServiceDetail.setBurstableBw(odrServiceDetail.getBurstableBwPortspeed());
		scServiceDetail.setBurstableBwUnit(odrServiceDetail.getBurstableBwUnit());
		scServiceDetail.setBwPortspeed(odrServiceDetail.getBwPortspeed());
		scServiceDetail.setBwUnit(odrServiceDetail.getBwUnit());
		scServiceDetail.setBwPortspeedAltName(odrServiceDetail.getBwPortspeedAltName());
		scServiceDetail.setCallType(odrServiceDetail.getCallType());
		scServiceDetail.setCreatedBy(odrServiceDetail.getCreatedBy());
		scServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setCustOrgNo(odrServiceDetail.getCustOrgNo());
		scServiceDetail.setDemarcationBuildingName(odrServiceDetail.getDemarcationApartment());
		scServiceDetail.setDemarcationFloor(odrServiceDetail.getDemarcationFloor());
		scServiceDetail.setDemarcationRack(odrServiceDetail.getDemarcationRack());
		scServiceDetail.setDemarcationRoom(odrServiceDetail.getDemarcationRoom());
		scServiceDetail.setDestinationCity(odrServiceDetail.getDestinationCity());
		scServiceDetail.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		scServiceDetail.setDestinationAddressLineOne(odrServiceDetail.getDestinationAddressLineOne());
		scServiceDetail.setDestinationAddressLineTwo(odrServiceDetail.getDestinationAddressLineTwo());
		scServiceDetail.setDestinationLocality(odrServiceDetail.getDestinationLocality());
		scServiceDetail.setDestinationPincode(odrServiceDetail.getDestinationPincode());
		scServiceDetail.setDestinationCountryCode(odrServiceDetail.getDestinationCountryCode());
		scServiceDetail.setDestinationState(odrServiceDetail.getDestinationState());
		scServiceDetail.setSourceState(odrServiceDetail.getSourceState());
		scServiceDetail.setDestinationCountryCodeRepc(odrServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetail.setDiscountArc(odrServiceDetail.getDiscountArc());
		scServiceDetail.setDiscountMrc(odrServiceDetail.getDiscountMrc());
		scServiceDetail.setDiscountNrc(odrServiceDetail.getDiscountNrc());
		scServiceDetail.setErfLocDestinationCityId(odrServiceDetail.getErfLocDestinationCityId());
		scServiceDetail.setErfLocDestinationCountryId(odrServiceDetail.getErfLocDestinationCountryId());
		scServiceDetail.setErfLocPopSiteAddressId(odrServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetail.setErfLocSiteAddressId(odrServiceDetail.getErfLocSiteAddressId());
		scServiceDetail.setErfLocSourceCityId(odrServiceDetail.getErfLocSourceCityId());
		scServiceDetail.setErfLocSrcCountryId(odrServiceDetail.getErfLocSrcCountryId());
		scServiceDetail.setErfPrdCatalogOfferingId(odrServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetail.setErfPrdCatalogOfferingName(odrServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogParentProductOfferingName(
				odrServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetail.setErfPrdCatalogProductId(odrServiceDetail.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogProductName(odrServiceDetail.getErfPrdCatalogProductName());
		scServiceDetail.setFeasibilityId(odrServiceDetail.getFeasibilityId());
		scServiceDetail.setGscOrderSequenceId(odrServiceDetail.getGscOrderSequenceId());
		scServiceDetail.setIsActive(odrServiceDetail.getIsActive());
		scServiceDetail.setIsIzo(odrServiceDetail.getIsIzo());
		scServiceDetail.setLastmileBw(odrServiceDetail.getLastmileBw());
		scServiceDetail.setLastmileBwAltName(odrServiceDetail.getLastmileBwAltName());
		scServiceDetail.setLastmileBwUnit(odrServiceDetail.getLastmileBwUnit());		
		scServiceDetail.setLastmileProvider(odrServiceDetail.getLastmileProvider());
		scServiceDetail.setLastmileType(odrServiceDetail.getLastmileType());
		scServiceDetail.setLatLong(odrServiceDetail.getLatLong());
		scServiceDetail.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
		scServiceDetail.setLocalItContactMobile(odrServiceDetail.getLocalItContactMobile());
		scServiceDetail.setLocalItContactName(odrServiceDetail.getLocalItContactName());
		scServiceDetail.setMrc(odrServiceDetail.getMrc());
		scServiceDetail.setNrc(odrServiceDetail.getNrc());
		scServiceDetail.setScOrderUuid(odrServiceDetail.getOdrOrderUuid());
		scServiceDetail.setParentBundleServiceId(odrServiceDetail.getParentBundleServiceId());
		scServiceDetail.setParentId(odrServiceDetail.getParentId());
		scServiceDetail.setPopSiteAddress(odrServiceDetail.getPopSiteAddress());
		scServiceDetail.setPopSiteCode(odrServiceDetail.getPopSiteCode());
		scServiceDetail.setPrimarySecondary(odrServiceDetail.getPrimarySecondary());
		scServiceDetail.setErdPriSecServiceLinkId(odrServiceDetail.getErfPriSecServiceLinkId());
		scServiceDetail.setProductReferenceId(odrServiceDetail.getProductReferenceId());
		scServiceDetail.setServiceClass(odrServiceDetail.getServiceClass());
		scServiceDetail.setServiceClassification(odrServiceDetail.getServiceClassification());
		scServiceDetail.setCommissionedDate(odrServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(odrServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setServiceCommissionedDate(odrServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(odrServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setServiceGroupId(odrServiceDetail.getServiceGroupId());
		scServiceDetail.setServiceGroupType(odrServiceDetail.getServiceGroupType());
		scServiceDetail.setServiceOption(odrServiceDetail.getServiceOption());
		scServiceDetail.setServiceStatus(odrServiceDetail.getServiceStatus());
		scServiceDetail.setServiceTerminationDate(odrServiceDetail.getServiceTerminationDate() != null
				? new Timestamp(odrServiceDetail.getServiceTerminationDate().getTime())
				: null);
		scServiceDetail.setServiceTopology(odrServiceDetail.getServiceTopology());
		scServiceDetail.setSiteAddress(odrServiceDetail.getSiteAddress());
		scServiceDetail.setSiteAlias(odrServiceDetail.getSiteAlias());
		scServiceDetail.setSiteEndInterface(odrServiceDetail.getSiteEndInterface());
		scServiceDetail.setSiteLinkLabel(odrServiceDetail.getSiteLinkLabel());
		scServiceDetail.setSiteTopology(odrServiceDetail.getSiteTopology());
		scServiceDetail.setSiteType(odrServiceDetail.getSiteType());
		scServiceDetail.setSlaTemplate(odrServiceDetail.getSlaTemplate());
		scServiceDetail.setSmEmail(odrServiceDetail.getSmEmail());
		scServiceDetail.setSourceCity(odrServiceDetail.getSourceCity());
		scServiceDetail.setSourceCountry(odrServiceDetail.getSourceCountry());
		scServiceDetail.setSourceAddressLineOne(odrServiceDetail.getSourceAddressLineOne());
		scServiceDetail.setSourceAddressLineTwo(odrServiceDetail.getSourceAddressLineTwo());
		scServiceDetail.setSourceLocality(odrServiceDetail.getSourceLocality());
		scServiceDetail.setSourcePincode(odrServiceDetail.getSourcePincode());
		scServiceDetail.setSourceCountryCode(odrServiceDetail.getSourceCountryCode());
		scServiceDetail.setSourceCountryCodeRepc(odrServiceDetail.getSourceCountryCodeRepc());
		scServiceDetail.setSupplOrgNo(odrServiceDetail.getSupplOrgNo());
		scServiceDetail.setTaxExemptionFlag(odrServiceDetail.getTaxExemptionFlag());
		scServiceDetail.setTpsCopfId(odrServiceDetail.getTpsCopfId());
		scServiceDetail.setTpsServiceId(odrServiceDetail.getTpsServiceId());
		scServiceDetail.setTpsSourceServiceId(odrServiceDetail.getTpsSourceServiceId());
		scServiceDetail.setUpdatedBy(odrServiceDetail.getUpdatedBy());
		scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setUuid(odrServiceDetail.getUuid()!=null ? odrServiceDetail.getUuid():getServiceCode(odrServiceDetail.getErfPrdCatalogProductName(),
				(StringUtils.isNotBlank(odrServiceDetail.getDestinationCity())?odrServiceDetail.getDestinationCity():CommonConstants.EMPTY), String.valueOf(odrServiceDetail.getId())));
		scServiceDetail.setErfOdrServiceId(odrServiceDetail.getId());
		scServiceDetail.setVpnName(odrServiceDetail.getVpnName());
		
		LOGGER.info("Order ID: {}, Service ID: {}", odrServiceDetail.getOdrOrderUuid(), scServiceDetail.getUuid());
		
		if(odrServiceDetail.getOdrOrderUuid() != null 
				&& odrServiceDetail.getOdrOrderUuid().startsWith(IPCServiceFulfillmentConstant.IPC)) {
			
			if((IpcConstants.MACD).equals(odrOrderBean.getOrderType())) {
				if((IPCServiceFulfillmentConstant.DR).equals(odrServiceDetail.getSiteType())) {
					LOGGER.info("DR Order - MACD");
					String dcServiceId = scServiceAttributeRepository.findAttributeValueByScServiceDetail_UuidAndAttributeName(scServiceDetail.getUuid(), 
							IPCServiceFulfillmentConstant.DC_SERVICE_ID);
					
					if(!StringUtils.isAllBlank(dcServiceId)) {
						LOGGER.info("Corresponding DC Service ID: {}", dcServiceId);
						// If DC Service ID exists, add the mapping. else, skip.
						Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
						SIAttributeBean siAttributeBean = new SIAttributeBean();
						siAttributeBean.setName(IPCServiceFulfillmentConstant.DC_SERVICE_ID);
						siAttributeBean.setValue(dcServiceId);
						siAttributeBean.setCreatedBy(odrServiceDetail.getCreatedBy());
						siAttributeBean.setUpdatedBy(odrServiceDetail.getUpdatedBy());
						ScServiceAttribute scServiceAttribute = mapServiceAttrEntityToBean(siAttributeBean);
						scServiceAttribute.setScServiceDetail(scServiceDetail);
						scServiceAttributes.add(scServiceAttribute);
						scServiceDetail.setScServiceAttributes(scServiceAttributes);
					}
				}
			} else {
				if((IPCServiceFulfillmentConstant.DR).equals(odrServiceDetail.getSiteType())) {
					LOGGER.info("DR Order - NEW");
					
					// Get Odr Order Attributes
					Set<OdrOrderAttributeBean> odrOrderAttributes = odrOrderBean.getOdrOrderAttributes();
					
					// Find the DC Order ID for the DR Order
					String dcOrderId = null;
					for(OdrOrderAttributeBean odrOrderAttribute : odrOrderAttributes) {
						if((IPCServiceFulfillmentConstant.DC_ORDER_ID).equals(odrOrderAttribute.getAttributeName())) {
							dcOrderId = odrOrderAttribute.getAttributeValue();
							break;
						}
					}
					
					LOGGER.info("Corresponding DC Order ID: {}", dcOrderId);
					
					if(dcOrderId != null) {
						// Get DC Service Detail
						Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
						ScServiceDetail dcScServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidOrderByIdDesc(dcOrderId);
						if(dcScServiceDetail != null) {
							LOGGER.info("Corresponding DC Service ID: {}", dcScServiceDetail.getUuid());
							// If DC Service Detail exists, add the mapping. else, skip.
							SIAttributeBean siAttributeBean = new SIAttributeBean();
							siAttributeBean.setName(IPCServiceFulfillmentConstant.DC_SERVICE_ID);
							siAttributeBean.setValue(dcScServiceDetail.getUuid());
							siAttributeBean.setCreatedBy(odrServiceDetail.getCreatedBy());
							siAttributeBean.setUpdatedBy(odrServiceDetail.getUpdatedBy());
							ScServiceAttribute scServiceAttribute = mapServiceAttrEntityToBean(siAttributeBean);
							scServiceAttribute.setScServiceDetail(scServiceDetail);
							scServiceAttributes.add(scServiceAttribute);
							scServiceDetail.setScServiceAttributes(scServiceAttributes);
						}
					}
				} else {
					LOGGER.info("DC Order - NEW");
					
					// Get all DR Orders for which this is a DC Order
					List<ScOrderAttribute> drScOrderAttributes = scOrderAttributeRepository
							.findByAttributeNameAndAttributeValue(IPCServiceFulfillmentConstant.DC_ORDER_ID, odrServiceDetail.getOdrOrderUuid());
					
					// Iterate DR Orders
					for(ScOrderAttribute drScOrderAttribute : drScOrderAttributes) {
						
						// Get DR Service Details
						Set<ScServiceDetail> drScServiceDetails = drScOrderAttribute.getScOrder().getScServiceDetails();
						
						// Iterate DR Service Details
						for(ScServiceDetail drScServiceDetail : drScServiceDetails) {
							
							LOGGER.info("Corresponding DR Service ID: {}", drScServiceDetail.getUuid());
							
							// Get DR Service Attributes
							Set<ScServiceAttribute> drScServiceAttributes = drScServiceDetail.getScServiceAttributes();
							
							boolean isDcDrMappingExists = false;
							
							// Iterate DR Service Attributes
							for(ScServiceAttribute drScServiceAttribute : drScServiceAttributes) {
								
								// Check if DC + DR mapping already exists
								if((IPCServiceFulfillmentConstant.DC_SERVICE_ID).equals(drScServiceAttribute.getAttributeName()) 
										&& (scServiceDetail.getUuid()).equals(drScServiceAttribute.getAttributeValue())) {
									isDcDrMappingExists = true;
									break;
								}
							}
							
							LOGGER.info("DC & DR Mapping Already Exists: {}", isDcDrMappingExists);
							
							if(!isDcDrMappingExists) {
								// If mapping does not exist, add the mapping to attributes. else, skip.
								SIAttributeBean siAttributeBean = new SIAttributeBean();
								siAttributeBean.setName(IPCServiceFulfillmentConstant.DC_SERVICE_ID);
								siAttributeBean.setValue(scServiceDetail.getUuid());
								siAttributeBean.setCreatedBy(odrServiceDetail.getCreatedBy());
								siAttributeBean.setUpdatedBy(odrServiceDetail.getUpdatedBy());
								ScServiceAttribute scServiceAttribute = mapServiceAttrEntityToBean(siAttributeBean);
								scServiceAttribute.setScServiceDetail(drScServiceDetail);
								scServiceAttributeRepository.save(scServiceAttribute);
							}
						}
					}
				}
			}
		}
		
		return scServiceDetail;
	}
	
	private String splitCustomerNameMax(String customerName) {
		LOGGER.info("splitCustomerNameMax");
		AtomicInteger atomicInteger = new AtomicInteger(0);
		if(customerName.length()<=3) {
			return customerName.replaceAll(" ", "_");
		}else {
			customerName=customerName.replaceAll(" ", "_");
			if(customerName.length()<=18){
				LOGGER.info("Less than 18::",customerName);
			  return customerName;
			}else{
				LOGGER.info("Greater than 18::",customerName);
				customerName=customerName.substring(0,18); 
			}
			return customerName;
		}
	}
	
	private String splitCustomerName(String customerName) {
		StringBuilder buffer = new StringBuilder();

		AtomicInteger atomicInteger = new AtomicInteger(0);
		if(customerName.length()<=3) {
			return customerName.replaceAll(" ", "_");
		}else {
			List<String> customerarr = Pattern.compile(" ").splitAsStream(customerName).limit(3)
					.collect(Collectors.toList());

			if(customerarr.size()==1){

				buffer.append(customerarr.get(0).length()<=10 ? customerarr.get(0) : customerarr.get(0).substring(0,10));
			}
			else {

				customerarr.forEach(cust -> {
					atomicInteger.incrementAndGet();

					if (atomicInteger.get() == 3) {
						buffer.append(cust.length() >= 4 ? (cust.substring(0, 4) + " ") : cust+ " ");
					} else {
						if (cust.length() > 7) {
							buffer.append(cust.substring(0, 6) + " ");

						} else {
							buffer.append(cust + " ");
						}
					}

				});
			}
			return buffer.toString().trim().replaceAll(" ", "_");
		}
	}
	
	/**
	 * mapServiceAttrEntityToBean
	 */
	public ScServiceAttribute mapServiceAttrEntityToBean(OdrServiceAttributeBean odrServiceAttribute) {
		ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
		scServiceAttribute.setAttributeAltValueLabel(odrServiceAttribute.getAttributeAltValueLabel());
		scServiceAttribute.setAttributeName(odrServiceAttribute.getAttributeName());
		scServiceAttribute.setAttributeValue(odrServiceAttribute.getAttributeValue());
		scServiceAttribute.setCategory(odrServiceAttribute.getCategory());
		scServiceAttribute.setCreatedBy(odrServiceAttribute.getCreatedBy());
		scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsActive(CommonConstants.Y);
		// odrServiceAttributeBean.setOdrServiceDetail(odrServiceDetail);
		scServiceAttribute.setUpdatedBy(odrServiceAttribute.getUpdatedBy());
		scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsAdditionalParam(odrServiceAttribute.getIsAdditionalParam());
		// TODO Auto-generated method stub
		if(odrServiceAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
			ScAdditionalServiceParam scAdditionalServiceParam=new ScAdditionalServiceParam();
			scAdditionalServiceParam.setAttribute(odrServiceAttribute.getOdrAdditionalServiceParam().getAttribute());
			scAdditionalServiceParam.setCategory(odrServiceAttribute.getOdrAdditionalServiceParam().getCategory());
			scAdditionalServiceParam.setCreatedBy(odrServiceAttribute.getOdrAdditionalServiceParam().getCreatedBy());
			scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
			scAdditionalServiceParam.setIsActive(CommonConstants.Y);
			scAdditionalServiceParam.setReferenceType(odrServiceAttribute.getOdrAdditionalServiceParam().getReferenceType());
			scAdditionalServiceParam.setReferenceId(odrServiceAttribute.getOdrAdditionalServiceParam().getReferenceId());
			scAdditionalServiceParam.setValue(odrServiceAttribute.getOdrAdditionalServiceParam().getValue());
			scAdditionalServiceParamRepo.save(scAdditionalServiceParam);
			scServiceAttribute.setAttributeValue(scAdditionalServiceParam.getId()+CommonConstants.EMPTY);
		}

		return scServiceAttribute;
	}

	public ScAttachment mapServiceAttachmentToEntity(OdrAttachmentBean odrAttachmentBean, ScServiceDetail serviceEntity) {
		Attachment attachment = new Attachment();
		attachment.setCategory(odrAttachmentBean.getCategory()==null?odrAttachmentBean.getType():odrAttachmentBean.getCategory());
		if("Cancellation".equalsIgnoreCase(odrAttachmentBean.getType())) {
			attachment.setCategory("CANCELEMAIL");
		}
		else {
			attachment.setCategory(odrAttachmentBean.getCategory()==null?odrAttachmentBean.getType():odrAttachmentBean.getCategory());
		}

		attachment.setContentTypeHeader(odrAttachmentBean.getContentTypeHeader());
		attachment.setCreatedBy(odrAttachmentBean.getCreatedBy());
		attachment.setCreatedDate(odrAttachmentBean.getCreatedDate());
		attachment.setIsActive(CommonConstants.Y);
		attachment.setName(odrAttachmentBean.getName());
		LOGGER.info("odrAttachmentBean.getProductName() {} and attachment.getCategory() {}" , odrAttachmentBean.getProductName(),attachment.getCategory());
		if(CommonConstants.IPC.equals(odrAttachmentBean.getProductName()) 
				&& (CommonConstants.IPC_TAX.equalsIgnoreCase(attachment.getCategory()) || CommonConstants.IPC_SOLUTION_DOCUMENT.equalsIgnoreCase(attachment.getCategory()) 
						|| CommonConstants.IPC_LICENSE_QUOTE.equalsIgnoreCase(attachment.getCategory()) || CommonConstants.IPC_SOLUTION_VALIDATION_EMAIL.equalsIgnoreCase(attachment.getCategory()))) {
			attachment.setUriPathOrUrl(odrAttachmentBean.getStoragePathUrl());
		}else if(attachment.getCategory().equalsIgnoreCase("Cancellation") || serviceEntity.getOrderType().equalsIgnoreCase("Termination")){
			LOGGER.info("Cancel order email attachment name {} :",odrAttachmentBean.getName());
		} else if (("GSIP").equals(odrAttachmentBean.getProductName())) {
			attachment.setUriPathOrUrl(odrAttachmentBean.getStoragePathUrl());
		}else {
			attachment.setUriPathOrUrl(odrAttachmentBean.getStoragePathUrl() + File.separator + odrAttachmentBean.getName());
		}
		attachment.setType(odrAttachmentBean.getType());
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(attachment);
		scAttachment.setIsActive(CommonConstants.Y);
		scAttachment.setOfferingName(odrAttachmentBean.getOfferingName());
		scAttachment.setOrderId(odrAttachmentBean.getOrderId());
		scAttachment.setProductName(odrAttachmentBean.getProductName());
		scAttachment.setScServiceDetail(serviceEntity);
		scAttachment.setServiceCode(serviceEntity.getUuid());
		scAttachment.setSiteType("A");
		scAttachment.setSiteId(odrAttachmentBean.getSiteId());
		return scAttachment;
	}
	
	public ScServiceCommercial mapOdrCommercialToServiceCommercial(OdrCommercialBean odrCommercialBean) {
		ScServiceCommercial scServiceCommercial = new ScServiceCommercial();
		scServiceCommercial.setArc(odrCommercialBean.getArc());
		scServiceCommercial.setComponentReferenceName(odrCommercialBean.getComponentReferenceName());
		scServiceCommercial.setMrc(odrCommercialBean.getMrc());
		scServiceCommercial.setNrc(odrCommercialBean.getNrc());
		scServiceCommercial.setReferenceName(odrCommercialBean.getReferenceName());
		scServiceCommercial.setReferenceType(odrCommercialBean.getReferenceType());
		scServiceCommercial.setServiceType(odrCommercialBean.getServiceType());
		return scServiceCommercial;
	}

	public ScContractInfo mapContractingInfoEntityToBean(OdrContractInfoBean odrContractInfo) {
		ScContractInfo scContractInfo = new ScContractInfo();
		scContractInfo.setAccountManager(odrContractInfo.getAccountManager());
		scContractInfo.setAccountManagerEmail(odrContractInfo.getAccountManagerEmail());
		scContractInfo.setArc(odrContractInfo.getArc());
		scContractInfo.setBillingAddress(odrContractInfo.getBillingAddress());
		scContractInfo.setBillingFrequency(odrContractInfo.getBillingFrequency());
		scContractInfo.setBillingMethod(odrContractInfo.getBillingMethod());
		scContractInfo.setContractEndDate(odrContractInfo.getContractEndDate() != null
				? new Timestamp(odrContractInfo.getContractEndDate().getTime())
				: null);
		scContractInfo.setContractStartDate(odrContractInfo.getContractStartDate() != null
				? new Timestamp(odrContractInfo.getContractStartDate().getTime())
				: null);
		scContractInfo.setCreatedBy(odrContractInfo.getCreatedBy());
		scContractInfo.setCreatedDate(new Timestamp(new Date().getTime()));
		scContractInfo.setCustomerContact(odrContractInfo.getCustomerContact());
		scContractInfo.setCustomerContactEmail(odrContractInfo.getCustomerContactEmail());
		scContractInfo.setDiscountArc(odrContractInfo.getDiscountArc());
		scContractInfo.setDiscountMrc(odrContractInfo.getDiscountMrc());
		scContractInfo.setDiscountNrc(odrContractInfo.getDiscountNrc());
		scContractInfo.setErfCustCurrencyId(odrContractInfo.getErfCustCurrencyId());
		scContractInfo.setErfCustLeId(odrContractInfo.getErfCustLeId());
		scContractInfo.setErfCustLeName(odrContractInfo.getErfCustLeName());
		scContractInfo.setPoMandatoryStatus(odrContractInfo.getPoMandatoryStatus());
		scContractInfo.setErfCustSpLeId(odrContractInfo.getErfCustSpLeId());
		scContractInfo.setErfCustSpLeName(odrContractInfo.getErfCustSpLeName());
		scContractInfo.setErfLocBillingLocationId(odrContractInfo.getErfLocBillingLocationId());
		scContractInfo.setIsActive(odrContractInfo.getIsActive());
		scContractInfo.setLastMacdDate(odrContractInfo.getLastMacdDate());
		scContractInfo.setMrc(odrContractInfo.getMrc());
		scContractInfo.setNrc(odrContractInfo.getNrc());
		scContractInfo.setBillingAddressLine1(odrContractInfo.getBillingAddressLine1());
		scContractInfo.setBillingAddressLine2(odrContractInfo.getBillingAddressLine2());
		scContractInfo.setBillingAddressLine3(odrContractInfo.getBillingAddressLine3());
		scContractInfo.setBillingCity(odrContractInfo.getBillingCity());
		scContractInfo.setBillingCountry(odrContractInfo.getBillingCountry());
		scContractInfo.setBillingCity(odrContractInfo.getBillingCity());
		scContractInfo.setBillingState(odrContractInfo.getBillingState());
		scContractInfo.setBillingPincode(odrContractInfo.getBillingPincode());
		// odrContractInfoBean.setOdrOrder(odrContractInfo.getOdrOrder());
		// odrContractInfoBean.setOdrServiceDetails(odrContractInfo.getOdrServiceDetails());
		scContractInfo.setOrderTermInMonths(odrContractInfo.getOrderTermInMonths());
		scContractInfo.setPaymentTerm(odrContractInfo.getPaymentTerm());
		scContractInfo.setTpsSfdcCuid(odrContractInfo.getTpsSfdcCuid());
		scContractInfo.setUpdatedBy(odrContractInfo.getUpdatedBy());
		scContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
		scContractInfo.setBillingContactId(odrContractInfo.getBillingContactId());
		return scContractInfo;
	}

	public ScOrderAttribute mapOrderAttrEntityToBean(OdrOrderAttributeBean odrOrderAttribute) {
		ScOrderAttribute scOrderAttribute = new ScOrderAttribute();
		scOrderAttribute.setAttributeAltValueLabel(odrOrderAttribute.getAttributeAltValueLabel());
		scOrderAttribute.setAttributeName(odrOrderAttribute.getAttributeName());
		if(odrOrderAttribute.getAttributeName().equalsIgnoreCase("PO_DATE")) {
			scOrderAttribute.setAttributeValue(convertDateFormat(odrOrderAttribute.getAttributeValue()));

		}
		else {
		scOrderAttribute.setAttributeValue(odrOrderAttribute.getAttributeValue());
		}
		scOrderAttribute.setCategory(odrOrderAttribute.getCategory());
		scOrderAttribute.setCreatedBy(odrOrderAttribute.getCreatedBy());
		scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scOrderAttribute.setIsActive(CommonConstants.Y);
		// odrOrderAttributeBean.setOdrOrder(odrOrder);
		scOrderAttribute.setUpdatedBy(odrOrderAttribute.getUpdatedBy());
		scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		// TODO Auto-generated method stub

		return scOrderAttribute;
	}

	private String convertDateFormat(String attributeValue) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

			return sdf2.format(sdf.parse(attributeValue));
		} catch (Exception e) {
			return attributeValue;
		}
	}
	


	public ScServiceSla mapServiceSlaEntityToBean(OdrServiceSlaBean odrServiceSla) {
		ScServiceSla scServiceSla = new ScServiceSla();
		scServiceSla.setCreatedBy(odrServiceSla.getCreatedBy());
		scServiceSla.setCreatedTime(new Timestamp(new Date().getTime()));
		scServiceSla.setIsActive(odrServiceSla.getIsActive());
		// odrServiceSlaBean.setOdrServiceDetail(odrServiceSla.getOdrServiceDetail());
		scServiceSla.setSlaComponent(odrServiceSla.getSlaComponent());
		scServiceSla.setSlaValue(odrServiceSla.getSlaValue());
		scServiceSla.setUpdatedBy(odrServiceSla.getUpdatedBy());
		scServiceSla.setUpdatedTime(new Timestamp(new Date().getTime()));
		return scServiceSla;
	}

	public ScProductDetail mapProductDetailEntityToBean(OdrProductDetail odrProductDetail) {		
			ScProductDetail scProductDetail = new ScProductDetail();
			scProductDetail.setType(odrProductDetail.getType());
			scProductDetail.setSolutionName(odrProductDetail.getSolutionName());
			scProductDetail.setMrc(odrProductDetail.getMrc());
			scProductDetail.setNrc(odrProductDetail.getNrc());
			scProductDetail.setArc(odrProductDetail.getArc());
			scProductDetail.setPpuRate(odrProductDetail.getPpuRate());
			scProductDetail.setIsActive(odrProductDetail.getIsActive());
			scProductDetail.setCreatedBy(odrProductDetail.getCreatedBy());
			scProductDetail.setCreatedDate(odrProductDetail.getCreatedDate());
			if(Objects.nonNull(odrProductDetail.getCloudCode())){
	        	scProductDetail.setCloudCode(odrProductDetail.getCloudCode());
			}
	        if(Objects.nonNull(odrProductDetail.getParentCloudCode())){
	        	scProductDetail.setParentCloudCode(odrProductDetail.getParentCloudCode());
			}
			return scProductDetail;
	}

	public ScProductDetailAttributes mapProductDetailAttributeToBean(OdrProductDetailAttributes odrProductDetailAttribute) {
		ScProductDetailAttributes scProductDetailAttributes = new ScProductDetailAttributes();
		scProductDetailAttributes.setCategory(odrProductDetailAttribute.getCategory());
		scProductDetailAttributes.setAttributeName(odrProductDetailAttribute.getAttributeName());
		scProductDetailAttributes.setAttributeValue(odrProductDetailAttribute.getAttributeValue());
		scProductDetailAttributes.setIsActive(odrProductDetailAttribute.getIsActive());
		return scProductDetailAttributes;
	}

	public ScServiceCommericalComponent mapServiceCommercialComponentToBean(OdrServiceCommercial odrServiceCommercial) {
		ScServiceCommericalComponent scServiceCommericalComponent = new ScServiceCommericalComponent();
		scServiceCommericalComponent.setArc(odrServiceCommercial.getArc());
		scServiceCommericalComponent.setCreatedBy(odrServiceCommercial.getCreatedBy());
		scServiceCommericalComponent.setCreatedDate(odrServiceCommercial.getCreatedDate());
		scServiceCommericalComponent.setItem(odrServiceCommercial.getItem());
		scServiceCommericalComponent.setItemType(odrServiceCommercial.getItemType());
		scServiceCommericalComponent.setMrc(odrServiceCommercial.getMrc());
		scServiceCommericalComponent.setNrc(odrServiceCommercial.getNrc());
		scServiceCommericalComponent.setParentItem(odrServiceCommercial.getParentItem());
		return scServiceCommericalComponent;
	}

	public Set<ScProductDetailAttributes> mapProductDetailAttr(List<OdrProductDetailAttributes> odrProductAttributes,ScProductDetail scProductDetail) {
		Set<ScProductDetailAttributes> scProductDetailAttributes = new HashSet<>();
		for (OdrProductDetailAttributes odrProductDetailAttribute : odrProductAttributes) {
			ScProductDetailAttributes scProductDetailAttribute = mapProductDetailAttributeToBean(odrProductDetailAttribute);
			scProductDetailAttribute.setScProductDetail(scProductDetail);
			scProductDetailAttributes.add(scProductDetailAttribute);
		}
		return scProductDetailAttributes;
	}

	public Set<ScServiceCommericalComponent> mapServiceCommercialComponents(List<OdrServiceCommercial> odrServiceCommercials, ScProductDetail scProductDetail) {
		Set<ScServiceCommericalComponent> scServiceCommericalComponents = new HashSet<>();
		for (OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
			ScServiceCommericalComponent scServiceCommericalComponent = mapServiceCommercialComponentToBean(odrServiceCommercial);
			scServiceCommericalComponent.setScProductDetail(scProductDetail);
			scServiceCommericalComponents.add(scServiceCommericalComponent);
		}
		return scServiceCommericalComponents;
	}

	private String getWebexServiceCode(String productName, String city,String primaryKeyValue, String productOfferingName) {
		
		String cityCode = CommonConstants.EMPTY;
		if (city != null
				&& city.length() > 4) {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.","").substring(0, 4);
		} else {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.", "");
		}
		String prodCode = CommonConstants.EMPTY;
		if (productName.equalsIgnoreCase("IAS")) {
			prodCode = ILL_CODE;
		} else if (productName.equals("IPC")) {
			prodCode = IPC_CODE;
		} else if(productName.equalsIgnoreCase("NPL")) {
			prodCode = NPL_CODE;
		}else if (productName.equalsIgnoreCase("UCAAS")) {
			prodCode = productOfferingName.equalsIgnoreCase("UCCEndpoint") ? WEBEX_CODE : WEBEX_LICENCE_CODE;
		} else if(productName.equalsIgnoreCase("WEBEX_SOLUTION")) {
			prodCode = WEBEX_SOLUTION_CODE;
        } else if(productName.equalsIgnoreCase("GSIP")) {
            prodCode = GSC_CODE;
		} else {
			prodCode = GVPN_CODE;
		}
		if (cityCode == null)
			cityCode = "";
		String primaryKey="";
		String serviceCode = "";
		if(productOfferingName != null && productOfferingName.equalsIgnoreCase("Cisco WebEx CCA")) {
			serviceCode = prodCode + "A";
		} else {
			serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";
		}
		
		primaryKey=primaryKeyValue;

		if (serviceCode.length() + primaryKey.length() < 19) {
			int length = 19 - (serviceCode.length() + primaryKey.length());

			StringBuilder stringBuilder = new StringBuilder();
			IntStream.range(0, length).forEach(value -> stringBuilder.append(0));
			serviceCode = serviceCode + stringBuilder + primaryKey;
		} else {
			serviceCode = serviceCode + primaryKey;

		}

		return serviceCode;
	}
	
private String getServiceCode(String productName, String city,String primaryKeyValue) {
		String cityCode = CommonConstants.EMPTY;
		if (city != null
				&& city.length() > 4) {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.","").substring(0, 4);
		} else {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.", "");
		}
		String prodCode = CommonConstants.EMPTY;
		if (productName.equalsIgnoreCase("IAS")) {
			prodCode = ILL_CODE;
		} else if (productName.equals("IPC")) {
			prodCode = IPC_CODE;
		} else if(productName.equalsIgnoreCase("NPL")) {
			prodCode = NPL_CODE;
		}
		else {
			prodCode = GVPN_CODE;
		}
		if (cityCode == null)
			cityCode = "";
		String primaryKey="";
		String serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";
			primaryKey=primaryKeyValue;

		if (serviceCode.length() + primaryKey.length() < 19) {
			int length = 19 - (serviceCode.length() + primaryKey.length());

			StringBuilder stringBuilder = new StringBuilder();
			IntStream.range(0, length).forEach(value -> stringBuilder.append(0));
			serviceCode = serviceCode + stringBuilder + primaryKey;
		} else {
			serviceCode = serviceCode + primaryKey;

		}

		return serviceCode;
	}

	private String getIzoSdwanServiceCode(String productName, String country,String city,String primaryKeyValue, String internationalDestinationDialCode) {
		LOGGER.info("getServiceCode method invoked for productName::{}",productName);
		String cityCode = CommonConstants.EMPTY;
		if (city != null
				&& city.length() > 4) {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.","").substring(0, 4);
		} else {
			cityCode = city.replaceAll(" ", "").replaceAll("\\.", "");
		}
		String prodCode = CommonConstants.EMPTY;
		if (productName.equalsIgnoreCase("IAS") || productName.equalsIgnoreCase("BYON Internet") || productName.equalsIgnoreCase("DIA")) {
			prodCode = ILL_CODE;
		} else if (productName.equals("IZO Internet WAN")) {
			prodCode = IWAN_CODE;
		}else if (productName.equals("IPC")) {
			prodCode = IPC_CODE;
		} else if(productName.equalsIgnoreCase("NPL")) {
			prodCode = NPL_CODE;
		}else if(productName.equalsIgnoreCase("IZOSDWAN") || productName.equalsIgnoreCase("IZO SDWAN") || productName.equalsIgnoreCase("IZOSDWAN_CGW")) {
			prodCode = SDWAN_CODE;
		}else if(productName.equalsIgnoreCase("IZOSDWAN_SOLUTION")) {
			prodCode = SOLUTION_CODE;
		}else {
			prodCode = GVPN_CODE;
		}
		if (cityCode == null)
			cityCode = "";
		String primaryKey="";
		String serviceCode ="";
		Boolean isInternationalCountryExists=true;
		if(!"India".equalsIgnoreCase(country) && (prodCode.equalsIgnoreCase(ILL_CODE) || prodCode.equalsIgnoreCase(SDWAN_CODE) || prodCode.equalsIgnoreCase(GVPN_CODE) || prodCode.equalsIgnoreCase(IWAN_CODE))){
			LOGGER.info("International Service Code generation for productName::{} with Country::{} for upstream Id::{}",productName,country,primaryKeyValue);
			if(internationalDestinationDialCode!=null){
				LOGGER.info("ISD Code exists::{} for productName::{} with CountryCountry::{} for upstream Id::{}",internationalDestinationDialCode,productName,country,primaryKeyValue);
				serviceCode = internationalDestinationDialCode + cityCode.toUpperCase() + prodCode + "A";
			}else{
				LOGGER.info("ISD Code not exists for productName::{} with CountryCountry::{} for upstream Id::{}",productName,country,primaryKeyValue);
				isInternationalCountryExists=false;
			}
		}else{
			LOGGER.info("Domestic Service Code generation for productName::{} with Country::{} for upstream Id::{}",productName,country,primaryKeyValue);
			serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";
		}
		primaryKey=primaryKeyValue;

		if (serviceCode.length() + primaryKey.length() < 19) {
			int length = 19 - (serviceCode.length() + primaryKey.length());

			StringBuilder stringBuilder = new StringBuilder();
			IntStream.range(0, length).forEach(value -> stringBuilder.append(0));
			serviceCode = serviceCode + stringBuilder + primaryKey;
		} else {
			serviceCode = serviceCode + primaryKey;

		}
		if(!isInternationalCountryExists){
			serviceCode="";
		}
		return serviceCode;
	}
	
	public static int getTotalAdditionalIps(String input, int ipTypeValue) {
		int totalAdditionalIps = 0;
		if(!input.isEmpty()){
			try {
				String[] splitBySlashArr = input.split("\\/");
				int calculatedValue = ipTypeValue - Integer.parseInt(splitBySlashArr[1]);
				totalAdditionalIps = (int) Math.pow(2, calculatedValue);
				}catch(Exception e) {
					LOGGER.error("Exception in getTotalAdditionalIps >> input is {} | exception is {}", input, e);
					return 0;
				}
		}
		return totalAdditionalIps;
	}
	
	public ScServiceDetail mapServiceEntityToBeanNpl(OdrServiceDetailBean odrServiceDetail,OdrOrderBean odrOrderBean) {
		ScServiceDetail scServiceDetail = null;
		if (odrOrderBean.getOrderType().equalsIgnoreCase("Termination")) {
			scServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeAndIsMigratedOrder(odrServiceDetail.getUuid(), TaskStatusConstants.TERMINATION_INITIATED, "N");


		}
		if (scServiceDetail == null) {
			scServiceDetail = new ScServiceDetail();
		}
		if(odrServiceDetail.getAccessType()!=null && odrServiceDetail.getAccessType().equals("OnnetWL_NPL"))odrServiceDetail.setAccessType("OnnetWL");
		scServiceDetail.setAccessType(odrServiceDetail.getAccessType());
		scServiceDetail.setArc(odrServiceDetail.getArc());
		scServiceDetail.setBillingAccountId(odrServiceDetail.getBillingAccountId());
		scServiceDetail.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
		scServiceDetail.setBillingRatioPercent(odrServiceDetail.getBillingRatioPercent());
		scServiceDetail.setBillingType(odrServiceDetail.getBillingType());
		scServiceDetail.setBwPortspeedAltName(odrServiceDetail.getBwPortspeedAltName());
		scServiceDetail.setBurstableBw(odrServiceDetail.getBurstableBwPortspeed());
		scServiceDetail.setBurstableBwUnit(odrServiceDetail.getBurstableBwUnit());
		scServiceDetail.setBwPortspeed(odrServiceDetail.getBwPortspeed());
		scServiceDetail.setBwUnit(odrServiceDetail.getBwUnit());
		scServiceDetail.setCallType(odrServiceDetail.getCallType());
		scServiceDetail.setCreatedBy(odrServiceDetail.getCreatedBy());
		scServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setCustOrgNo(odrServiceDetail.getCustOrgNo());
		scServiceDetail.setIsMigratedOrder("N");
		scServiceDetail.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		scServiceDetail.setDestinationCountryCodeRepc(odrServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetail.setDiscountArc(odrServiceDetail.getDiscountArc());
		scServiceDetail.setDiscountMrc(odrServiceDetail.getDiscountMrc());
		scServiceDetail.setDiscountNrc(odrServiceDetail.getDiscountNrc());
		scServiceDetail.setErfLocDestinationCityId(odrServiceDetail.getErfLocDestinationCityId());
		scServiceDetail.setErfLocDestinationCountryId(odrServiceDetail.getErfLocDestinationCountryId());
		scServiceDetail.setErfLocPopSiteAddressId(odrServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetail.setErfLocSiteAddressId(odrServiceDetail.getErfLocSiteAddressId());
		scServiceDetail.setErfLocSourceCityId(odrServiceDetail.getErfLocSourceCityId());
		scServiceDetail.setErfLocSrcCountryId(odrServiceDetail.getErfLocSrcCountryId());
		scServiceDetail.setErfPrdCatalogOfferingId(odrServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetail.setErfPrdCatalogOfferingName(odrServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogParentProductOfferingName(
				odrServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetail.setErfPrdCatalogProductId(odrServiceDetail.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogProductName(odrServiceDetail.getErfPrdCatalogProductName());
		scServiceDetail.setGscOrderSequenceId(odrServiceDetail.getGscOrderSequenceId());
		scServiceDetail.setIsActive(odrServiceDetail.getIsActive());
		scServiceDetail.setIsIzo(odrServiceDetail.getIsIzo());
		scServiceDetail.setLastmileBwAltName(odrServiceDetail.getLastmileBwAltName());
		scServiceDetail.setOrderSubCategory(odrServiceDetail.getOrderSubCategory());
		scServiceDetail.setOrderCategory(odrServiceDetail.getOrderCategory());
		scServiceDetail.setOrderType(odrServiceDetail.getOrderType());
		scServiceDetail.setCustomerRequestorDate(odrServiceDetail.getCustomerRequestorDate());
		
		if(odrServiceDetail.getOdrGstAddress()!=null) {
			ScGstAddress scGstAddress=new ScGstAddress();
			scGstAddress.setBuildingName(odrServiceDetail.getOdrGstAddress().getBuildingName());
			scGstAddress.setBuildingNumber(odrServiceDetail.getOdrGstAddress().getBuildingNumber());
			scGstAddress.setCreatedBy(odrServiceDetail.getOdrGstAddress().getCreatedBy());
			scGstAddress.setCreatedTime(odrServiceDetail.getOdrGstAddress().getCreatedTime());
			scGstAddress.setDistrict(odrServiceDetail.getOdrGstAddress().getDistrict());
			scGstAddress.setFlatNumber(odrServiceDetail.getOdrGstAddress().getFlatNumber());
			scGstAddress.setLatitude(odrServiceDetail.getOdrGstAddress().getLatitude());
			scGstAddress.setLocality(odrServiceDetail.getOdrGstAddress().getLocality());
			scGstAddress.setLongitude(odrServiceDetail.getOdrGstAddress().getLongitude());
			scGstAddress.setPincode(odrServiceDetail.getOdrGstAddress().getPincode());
			scGstAddress.setState(odrServiceDetail.getOdrGstAddress().getState());
			scGstAddress.setStreet(odrServiceDetail.getOdrGstAddress().getStreet());
			scServiceDetail.setScGstAddress(scGstAddress);
		}
		
				
		try {
			if (odrOrderBean.getErfCustCustomerName() != null && !odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				LOGGER.info("odrOrderBean.getErfCustCustomerName()={}",odrOrderBean.getErfCustCustomerName());
			scServiceDetail.setVpnSolutionId(StringUtils.trimToEmpty(splitCustomerName(odrOrderBean.getErfCustCustomerName())));
			}
			
			if(odrServiceDetail.getLastmileBw()!=null) {
				Double localLoopBw = Double.valueOf(odrServiceDetail.getLastmileBw());
				if(localLoopBw < 622)scServiceDetail.setLineRate("STM4");
				else if(localLoopBw > 622 && localLoopBw < 2500)scServiceDetail.setLineRate("STM16");
				else if(localLoopBw > 2500)scServiceDetail.setLineRate("STM64");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in LineRate => {}", e);
		}
	
		scServiceDetail.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
		scServiceDetail.setLocalItContactName(odrServiceDetail.getLocalItContactName());		
		scServiceDetail.setMrc(odrServiceDetail.getMrc());
		scServiceDetail.setNrc(odrServiceDetail.getNrc());
		scServiceDetail.setDifferentialMrc(odrServiceDetail.getDifferentialMrc());
		scServiceDetail.setDifferentialNrc(odrServiceDetail.getDifferentialNrc());
		scServiceDetail.setScOrderUuid(odrServiceDetail.getOdrOrderUuid());
		
		String connectedBuilding ="";
		String connectedCustomer = "";
		String lmScenarioType = "";
		String lmConnectionType = "Wireline";
		String additionalIps="";
		String additionalIpsType="";
		String additionalIPv4IPs ="";
		String additionalIPv6IPs="";
		int totalAdditionalIps=0;
		
		Set<ScServiceAttribute> scServiceAttrBeans = new HashSet<>();
		for (OdrServiceAttributeBean odrServiceAttribute : odrServiceDetail.getOdrServiceAttributes()) {
			
			if(odrServiceAttribute.getAttributeName().equals("Service Variant")) {
				scServiceDetail.setServiceVariant(odrServiceAttribute.getAttributeValue());
			}else if(odrServiceAttribute.getAttributeName().equals("connected_building_tag")) {
				connectedBuilding = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());				
			}else if(odrServiceAttribute.getAttributeName().equals("connected_cust_tag")) {				
				connectedCustomer = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());				
			}
			if(odrServiceAttribute.getAttributeName().equals("Additional IPs")) {
				LOGGER.info("additional ip {} ",StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue()));
				additionalIps = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IP Address Arrangement for Additional IPs")) {
				LOGGER.info("additional ip pool type{} ",StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue()));
				additionalIpsType = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IPv4 Address Pool Size for Additional IPs")) {
				 additionalIPv4IPs = StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			if(odrServiceAttribute.getAttributeName().equals("IPv6 Address Pool Size for Additional IPs") ) {
				additionalIPv6IPs =  StringUtils.trimToEmpty(odrServiceAttribute.getAttributeValue());	
			}
			ScServiceAttribute serviceAttr = mapServiceAttrEntityToBean(odrServiceAttribute);
			serviceAttr.setScServiceDetail(scServiceDetail);
			scServiceAttrBeans.add(serviceAttr);
		}
		try {
			if ("yes".equalsIgnoreCase(additionalIps) || "y".equalsIgnoreCase(additionalIps)) {
				if ("ipv4".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
				} else if ("ipv6".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv6IPs, 64);
				} else {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
					totalAdditionalIps = totalAdditionalIps + getTotalAdditionalIps(additionalIPv6IPs, 64);
				}
				scServiceDetail.setNoOfAdditionalIps(String.valueOf(totalAdditionalIps));
				scServiceDetail.setAdditionalIpPoolType(additionalIpsType);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured during additional IPs for Network products {}", exception);
		}

		String lastMileType = StringUtils.trimToEmpty(odrServiceDetail.getLastmileType());
		if (lastMileType.toLowerCase().contains("onnetrf")) {
			lmScenarioType = "Onnet Wireless";
			lmConnectionType = "Wireless";
		} else if (lastMileType.toLowerCase().contains("offnetrf")) {
			lmScenarioType = "Offnet Wireless";
			lmConnectionType = "Wireless";
		} else if (lastMileType.toLowerCase().contains("onnetwl")) {
			lmScenarioType = "Onnet Wireline";
			lmConnectionType = "Wireline";		
			LOGGER.info("connectedBuilding={},connectedCustomer={},OrderType={},OrderCategory={}",connectedBuilding,connectedCustomer,odrOrderBean.getOrderType(),odrOrderBean.getOrderCategory());
			if (connectedCustomer.contains("1")  || (StringUtils.trimToEmpty(odrOrderBean.getOrderType()).equalsIgnoreCase("MACD")
					&& (StringUtils.trimToEmpty(odrOrderBean.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
							|| StringUtils.trimToEmpty(odrOrderBean.getOrderCategory()).equalsIgnoreCase("ADD_IP")))) {
				lmScenarioType = "Onnet Wireline - Connected Customer";
			} else if (connectedBuilding.contains("1")) {
				lmScenarioType = "Onnet Wireline - Connected Building";
			} else {
				lmScenarioType = "Onnet Wireline - Near Connect";
			}
		} else if (lastMileType.toLowerCase().contains("offnetwl")) {
			lmScenarioType = "Offnet Wireline";
			lmConnectionType = "Wireline";
		}

		scServiceDetail.setLastmileScenario(lmScenarioType);
		scServiceDetail.setLastmileConnectionType(lmConnectionType);
		
		scServiceDetail.setScServiceAttributes(scServiceAttrBeans);
		Set<ScServiceSla> odrServiceSlaBeans = new HashSet<>();
		for (OdrServiceSlaBean odrServiceSla : odrServiceDetail.getOdrServiceSlas()) {
			ScServiceSla serviceSla = mapServiceSlaEntityToBean(odrServiceSla);
			serviceSla.setScServiceDetail(scServiceDetail);
			odrServiceSlaBeans.add(serviceSla);
		}
		scServiceDetail.setScServiceSlas(odrServiceSlaBeans);
		scServiceDetail.setParentBundleServiceId(odrServiceDetail.getParentBundleServiceId());
		scServiceDetail.setParentId(odrServiceDetail.getParentId());
		
		scServiceDetail.setSourceCountryCode(odrServiceDetail.getSourceCountryCode());
		scServiceDetail.setSourceCountryCodeRepc(odrServiceDetail.getSourceCountryCodeRepc());
		
				
		scServiceDetail.setPrimarySecondary(odrServiceDetail.getPrimarySecondary());
		//scServiceDetail.setPriSecServiceLink(odrServiceDetail.getPriSecServiceLink());
		scServiceDetail.setErdPriSecServiceLinkId(odrServiceDetail.getErfPriSecServiceLinkId());
		scServiceDetail.setProductReferenceId(odrServiceDetail.getProductReferenceId());
		scServiceDetail.setServiceClass(odrServiceDetail.getServiceClass());
		scServiceDetail.setServiceClassification(odrServiceDetail.getServiceClassification());
		scServiceDetail.setServiceCommissionedDate(odrServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(odrServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setRrfsDate(odrServiceDetail.getRrfsDate() != null
				? new Timestamp(odrServiceDetail.getRrfsDate().getTime())
				: null);
		scServiceDetail.setServiceGroupId(odrServiceDetail.getServiceGroupId());
		scServiceDetail.setServiceGroupType(odrServiceDetail.getServiceGroupType());
		scServiceDetail.setServiceStatus(odrServiceDetail.getServiceStatus());
		scServiceDetail.setServiceTerminationDate(odrServiceDetail.getServiceTerminationDate() != null
				? new Timestamp(odrServiceDetail.getServiceTerminationDate().getTime())
				: null);
		scServiceDetail.setServiceTopology(odrServiceDetail.getServiceTopology());
		scServiceDetail.setSiteAlias(odrServiceDetail.getSiteAlias());
		scServiceDetail.setSiteAddress(odrServiceDetail.getSiteAddress());
		scServiceDetail.setSiteEndInterface(odrServiceDetail.getSiteEndInterface());
		// scServiceDetail.setSiteLatLang(odrServiceDetail.getSiteLatLang());
		scServiceDetail.setSiteLinkLabel(odrServiceDetail.getSiteLinkLabel());
		scServiceDetail.setSiteTopology(odrServiceDetail.getSiteTopology());
		scServiceDetail.setSlaTemplate(odrServiceDetail.getSlaTemplate());
		scServiceDetail.setSmEmail(odrServiceDetail.getSmEmail());
		
		scServiceDetail.setSupplOrgNo(odrServiceDetail.getSupplOrgNo());
		scServiceDetail.setTpsCopfId(odrServiceDetail.getTpsCopfId());
		scServiceDetail.setTpsServiceId(odrServiceDetail.getTpsServiceId());
		scServiceDetail.setTpsSourceServiceId(odrServiceDetail.getTpsSourceServiceId());
		scServiceDetail.setUpdatedBy(odrServiceDetail.getUpdatedBy());
		scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
		if(odrServiceDetail.getIsAmended()!=null && odrServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)){
			LOGGER.info("Inside NPL Order amended service with odr service id {} :",odrServiceDetail.getId());
			scServiceDetail.setIsAmended(CommonConstants.Y);
			scServiceDetail.setUuid(odrServiceDetail.getAmendedServiceId() != null ? odrServiceDetail.getAmendedServiceId() : getServiceCode(odrServiceDetail.getErfPrdCatalogProductName(),
					odrServiceDetail.getDestinationCity(), String.valueOf(odrServiceDetail.getId())));
		}else {
			scServiceDetail.setUuid(odrServiceDetail.getUuid() != null ? odrServiceDetail.getUuid() : getServiceCode("NPL",
					odrServiceDetail.getDestinationCity() != null ? odrServiceDetail.getDestinationCity() : "", String.valueOf(odrServiceDetail.getId())));
		}

		if("MACD".equals(odrOrderBean.getOrderType())){
			LOGGER.info("OrderCode::{}",odrOrderBean.getOpOrderCode());
				ScServiceDetail prevActiveServiceDetail=null;
				if(Objects.nonNull(odrServiceDetail.getUuid())){
					LOGGER.info("Link Id for UUID::{}",odrServiceDetail.getUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(odrServiceDetail.getUuid(), "ACTIVE");
				}else if(Objects.nonNull(odrServiceDetail.getParentServiceUuid())){
					LOGGER.info("Link Id for Parent UUID::{}",odrServiceDetail.getParentServiceUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(odrServiceDetail.getParentServiceUuid(), "ACTIVE");
				}
				scServiceDetail.setServiceLinkId(Objects.nonNull(prevActiveServiceDetail)?prevActiveServiceDetail.getId():null);
		}
		scServiceDetail.setParentUuid(odrServiceDetail.getParentServiceUuid());
		scServiceDetail.setErfOdrServiceId(odrServiceDetail.getId());
		scServiceDetail.setVpnName(odrServiceDetail.getVpnName());

		return scServiceDetail;
	}

	public ScOrder mapOrderBeanToEntity(SIOrderDataBean siOrderData) {
		ScOrder scOrder = new ScOrder();
		scOrder.setCreatedDate(new Timestamp(new Date().getTime()));
		scOrder.setCustomerGroupName(siOrderData.getCustomerGroupName());
		scOrder.setCustomerSegment(siOrderData.getCustomerSegment());
		scOrder.setDemoFlag(siOrderData.getDemoFlag());
		scOrder.setErfCustCustomerId(Objects.nonNull(siOrderData.getErfCustCustomerId())?Integer.valueOf(siOrderData.getErfCustCustomerId()):null);
		scOrder.setErfCustCustomerName(siOrderData.getErfCustCustomerName());
		scOrder.setErfCustLeId(siOrderData.getErfCustLeId());
		scOrder.setErfCustLeName(siOrderData.getErfCustLeName());
		scOrder.setErfCustPartnerId(siOrderData.getErfCustPartnerId());
		scOrder.setErfCustPartnerName(siOrderData.getErfCustPartnerName());
		scOrder.setErfCustPartnerLeId(siOrderData.getErfCustPartnerLeId());
		scOrder.setPartnerCuid(siOrderData.getPartnerCuid());
		scOrder.setErfCustSpLeId(siOrderData.getErfCustSpLeId());
		scOrder.setErfCustSpLeName(siOrderData.getErfCustSpLeName());
		scOrder.setErfUserCustomerUserId(siOrderData.getErfUserCustomerUserId());
		scOrder.setErfUserInitiatorId(siOrderData.getErfUserInitiatorId());
		scOrder.setUuid(siOrderData.getUuid());
		scOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
		scOrder.setTpsSfdcCuid(siOrderData.getTpsSfdcCuid());
		scOrder.setTpsSecsId(siOrderData.getTpsSecsId());
		scOrder.setTpsSapCrnId(siOrderData.getTpsSapCrnId());
		scOrder.setTpsCrmSystem(siOrderData.getTpsCrmSystem());
		scOrder.setTpsCrmOptyId(siOrderData.getTpsCrmOptyId());
		scOrder.setTpsCrmCofId(siOrderData.getTpsCrmCofId());
		scOrder.setTpsSfdcOptyId(siOrderData.getTpsCrmOptyId());
		scOrder.setSfdcAccountId(siOrderData.getSfdcAccountId());
		scOrder.setParentOpOrderCode(siOrderData.getParentOpOrderCode());
		scOrder.setParentId(siOrderData.getParentId());
		scOrder.setOrderType(siOrderData.getOrderType());
		scOrder.setOrderStatus(siOrderData.getOrderStatus());
		scOrder.setOrderStartDate(
				siOrderData.getOrderStartDate() != null ? new Timestamp(siOrderData.getOrderStartDate().getTime()) : null);
		scOrder.setOrderSource(siOrderData.getOrderSource());
		scOrder.setOrderEndDate(
				siOrderData.getOrderEndDate() != null ? new Timestamp(siOrderData.getOrderEndDate().getTime()) : null);
		scOrder.setOrderCategory(siOrderData.getOrderCategory());
		scOrder.setOpOrderCode(siOrderData.getOpOrderCode());
		scOrder.setOpportunityClassification(siOrderData.getOpportunityClassification());
		scOrder.setIsActive(siOrderData.getIsActive());
		scOrder.setIsBundleOrder(siOrderData.getIsBundleOrder());
		scOrder.setIsMultipleLe(siOrderData.getIsMultipleLe());
		scOrder.setLastMacdDate(
				siOrderData.getLastMacdDate() != null ? new Timestamp(siOrderData.getLastMacdDate().getTime()) : null);
		scOrder.setMacdCreatedDate(
				siOrderData.getMacdCreatedDate() != null ? new Timestamp(siOrderData.getMacdCreatedDate().getTime()) : null);
		scOrder.setCreatedBy(siOrderData.getCreatedBy());
		scOrder.setUpdatedBy(siOrderData.getUpdatedBy());
		scOrder.setIsMigratedOrder("Y");
		return scOrder;
	}

	public ScServiceDetail mapServiceEntityToBean(SIServiceDetailDataBean serviceDetail, SIOrderDataBean siOrderData) {
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		scServiceDetail.setArc(serviceDetail.getArc());
		scServiceDetail.setBillingAccountId(serviceDetail.getBillingAccountId());
		scServiceDetail.setBillingType(serviceDetail.getBillingType());
		scServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setDestinationCountryCode(serviceDetail.getDestinationCountryCode());
		scServiceDetail.setErfPrdCatalogOfferingId(serviceDetail.getErfPrdCatalogOfferingId());
		scServiceDetail.setErfPrdCatalogOfferingName(serviceDetail.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogParentProductOfferingName(
				serviceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetail.setErfPrdCatalogProductId(serviceDetail.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogProductName(serviceDetail.getErfPrdCatalogProductName());	
		scServiceDetail.setSiteTopology(serviceDetail.getSiteTopology());
		scServiceDetail.setServiceTopology(serviceDetail.getServiceTopology());
		scServiceDetail.setSiteType(serviceDetail.getSiteType());
		scServiceDetail.setIsMigratedOrder("Y");
		scServiceDetail.setSiteAlias(serviceDetail.getAlias()); 
		
		try {
			if (siOrderData.getErfCustCustomerName() != null && !siOrderData.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				LOGGER.info("siOrderData.getErfCustCustomerName()={}",siOrderData.getErfCustCustomerName());
				scServiceDetail.setVpnSolutionId(StringUtils.trimToEmpty(splitCustomerName(siOrderData.getErfCustCustomerName())));
			}
			
			if(serviceDetail.getLastmileBw()!=null) {
				Integer localLoopBw = Integer.valueOf(serviceDetail.getLastmileBw());
				if(localLoopBw < 622)scServiceDetail.setLineRate("STM4");
				else if(localLoopBw > 622 && localLoopBw < 2500)scServiceDetail.setLineRate("STM16");
				else if(localLoopBw > 2500)scServiceDetail.setLineRate("STM64");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in LineRate => {}", e);
		}
		scServiceDetail.setMrc(serviceDetail.getMrc());
		scServiceDetail.setNrc(serviceDetail.getNrc());
		scServiceDetail.setScOrderUuid(siOrderData.getUuid());
		
		String connectedBuilding ="";
		String connectedCustomer = "";
		String additionalIps="";
		String additionalIpsType="";
		String additionalIPv4IPs ="";
		String additionalIPv6IPs="";
		int totalAdditionalIps=0;
		
		Set<ScServiceAttribute> scServiceAttrBeans = new HashSet<>();
		for (SIAttributeBean siAttributeBean : serviceDetail.getAttributes()) {
			
			if(siAttributeBean.getName().equals("Service Variant")) {
				scServiceDetail.setServiceVariant(siAttributeBean.getValue());
			}else if(siAttributeBean.getName().equals("connected_building_tag")) {
				connectedBuilding = StringUtils.trimToEmpty(siAttributeBean.getValue());				
			}else if(siAttributeBean.getName().equals("connected_cust_tag")) {				
				connectedCustomer = StringUtils.trimToEmpty(siAttributeBean.getValue());				
			}
			if(siAttributeBean.getName().equals("Additional IPs")) {
				LOGGER.info("additional ip {} ",StringUtils.trimToEmpty(siAttributeBean.getValue()));
				additionalIps = StringUtils.trimToEmpty(siAttributeBean.getValue());	
			}
			if(siAttributeBean.getName().equals("IP Address Arrangement for Additional IPs")) {
				LOGGER.info("additional ip pool type{} ",StringUtils.trimToEmpty(siAttributeBean.getValue()));
				additionalIpsType = StringUtils.trimToEmpty(siAttributeBean.getValue());	
			}
			if(siAttributeBean.getName().equals("IPv4 Address Pool Size for Additional IPs")) {
				 additionalIPv4IPs = StringUtils.trimToEmpty(siAttributeBean.getValue());	
			}
			if(siAttributeBean.getName().equals("IPv6 Address Pool Size for Additional IPs") ) {
				additionalIPv6IPs =  StringUtils.trimToEmpty(siAttributeBean.getValue());	
			}
			ScServiceAttribute serviceAttr = mapServiceAttrEntityToBean(siAttributeBean);
			serviceAttr.setScServiceDetail(scServiceDetail);
			scServiceAttrBeans.add(serviceAttr);
		}
		try {
			if ("yes".equalsIgnoreCase(additionalIps) || "y".equalsIgnoreCase(additionalIps)) {
				if ("ipv4".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
				} else if ("ipv6".equalsIgnoreCase(additionalIpsType)) {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv6IPs, 64);
				} else {
					totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
					totalAdditionalIps = totalAdditionalIps + getTotalAdditionalIps(additionalIPv6IPs, 64);
				}
				scServiceDetail.setNoOfAdditionalIps(String.valueOf(totalAdditionalIps));
				scServiceDetail.setAdditionalIpPoolType(additionalIpsType);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured during additional IPs for Network products {}", exception);
		}

		
		scServiceDetail.setScServiceAttributes(scServiceAttrBeans);
		scServiceDetail.setParentBundleServiceId(serviceDetail.getParentBundleServiceId());	
		scServiceDetail.setSourceCountryCode(serviceDetail.getSourceCountryCode());
		scServiceDetail.setServiceCommissionedDate(serviceDetail.getServiceCommissionedDate() != null
				? new Timestamp(serviceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setTpsServiceId(serviceDetail.getTpsServiceId());
		scServiceDetail.setTpsSourceServiceId(serviceDetail.getTpsSourceServiceId());
		scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setUuid(serviceDetail.getTpsServiceId());
		scServiceDetail.setErfOdrServiceId(serviceDetail.getId());
		scServiceDetail.setVpnName(serviceDetail.getVpnName());
		scServiceDetail.setCreatedBy(serviceDetail.getCreatedBy());
		scServiceDetail.setUpdatedBy(serviceDetail.getUpdatedBy());
		scServiceDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		scServiceDetail.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		scServiceDetail.setOrderType(serviceDetail.getOrderType());
		scServiceDetail.setOrderCategory(serviceDetail.getOrderCategory());
		return scServiceDetail;
	}

	private ScServiceAttribute mapServiceAttrEntityToBean(SIAttributeBean siAttributeBean) {
		ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
		scServiceAttribute.setAttributeAltValueLabel(siAttributeBean.getName());
		scServiceAttribute.setAttributeName(siAttributeBean.getName());
		scServiceAttribute.setAttributeValue(siAttributeBean.getValue());
		scServiceAttribute.setCategory(siAttributeBean.getCategory());
		scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsActive("Y");
		scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setCreatedBy(siAttributeBean.getCreatedBy());
		scServiceAttribute.setUpdatedBy(siAttributeBean.getUpdatedBy());
		return scServiceAttribute;
	}

	public ScContractInfo mapContractingInfoEntityToBean(SIContractInfoBean contractInfo) {
		ScContractInfo scContractInfo = new ScContractInfo();
		scContractInfo.setAccountManager(contractInfo.getAccountManager());
		scContractInfo.setAccountManagerEmail(contractInfo.getAccountManagerEmail());
		scContractInfo.setArc(contractInfo.getArc());
		scContractInfo.setBillingAddress(contractInfo.getBillingAddress());
		scContractInfo.setBillingFrequency(contractInfo.getBillingFrequency());
		scContractInfo.setBillingMethod(contractInfo.getBillingMethod());
		scContractInfo.setContractEndDate(contractInfo.getContractEndDate() != null
				? new Timestamp(contractInfo.getContractEndDate().getTime())
				: null);
		scContractInfo.setContractStartDate(contractInfo.getContractStartDate() != null
				? new Timestamp(contractInfo.getContractStartDate().getTime())
				: null);
		scContractInfo.setCreatedBy(contractInfo.getCreatedBy());
		scContractInfo.setCreatedDate(new Timestamp(new Date().getTime()));
		scContractInfo.setCustomerContact(contractInfo.getCustomerContact());
		scContractInfo.setCustomerContactEmail(contractInfo.getCustomerContactEmail());
		scContractInfo.setDiscountArc(contractInfo.getDiscountArc());
		scContractInfo.setDiscountMrc(contractInfo.getDiscountMrc());
		scContractInfo.setDiscountNrc(contractInfo.getDiscountNrc());
		scContractInfo.setErfCustCurrencyId(contractInfo.getErfCustCurrencyId());
		scContractInfo.setErfCustLeId(contractInfo.getErfCustLeId());
		scContractInfo.setErfCustLeName(contractInfo.getErfCustLeName());
		scContractInfo.setErfCustSpLeId(contractInfo.getErfCustSpLeId());
		scContractInfo.setErfCustSpLeName(contractInfo.getErfCustSpLeName());
		scContractInfo.setErfLocBillingLocationId(contractInfo.getErfLocBillingLocationId());
		scContractInfo.setIsActive(contractInfo.getIsActive());
		scContractInfo.setLastMacdDate(contractInfo.getLastMacdDate());
		scContractInfo.setMrc(contractInfo.getMrc());
		scContractInfo.setNrc(contractInfo.getNrc());
		scContractInfo.setOrderTermInMonths(contractInfo.getOrderTermInMonths());
		scContractInfo.setPaymentTerm(contractInfo.getPaymentTerm());
		scContractInfo.setTpsSfdcCuid(contractInfo.getTpsSfdcCuid());
		scContractInfo.setUpdatedBy(contractInfo.getUpdatedBy());
		scContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
		scContractInfo.setBillingContactId(contractInfo.getBillingContactId());
		return scContractInfo;
	}
	
	public ScServiceDetail mapServiceEntityToBeanGde(OdrServiceDetailBean odrServiceDetail,OdrOrderBean odrOrderBean) {
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		scServiceDetail.setAccessType(odrServiceDetail.getAccessType());
		scServiceDetail.setArc(odrServiceDetail.getArc());
		scServiceDetail.setBillingAccountId(odrServiceDetail.getBillingAccountId());
		scServiceDetail.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
		scServiceDetail.setBillingRatioPercent(odrServiceDetail.getBillingRatioPercent());
		scServiceDetail.setBillingType(odrServiceDetail.getBillingType());
		scServiceDetail.setBwPortspeedAltName(odrServiceDetail.getBwPortspeedAltName());
		scServiceDetail.setCallType(odrServiceDetail.getCallType());
		scServiceDetail.setCreatedBy(odrServiceDetail.getCreatedBy());
		scServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceDetail.setCustOrgNo(odrServiceDetail.getCustOrgNo());
		scServiceDetail.setIsMigratedOrder("N");
		scServiceDetail.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		scServiceDetail.setDestinationCountryCodeRepc(odrServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetail.setDiscountArc(odrServiceDetail.getDiscountArc());
		scServiceDetail.setDiscountMrc(odrServiceDetail.getDiscountMrc());
		scServiceDetail.setDiscountNrc(odrServiceDetail.getDiscountNrc());
		scServiceDetail.setErfLocDestinationCityId(odrServiceDetail.getErfLocDestinationCityId());
		scServiceDetail.setErfLocDestinationCountryId(odrServiceDetail.getErfLocDestinationCountryId());
		scServiceDetail.setErfLocPopSiteAddressId(odrServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetail.setErfLocSiteAddressId(odrServiceDetail.getErfLocSiteAddressId());
		scServiceDetail.setErfLocSourceCityId(odrServiceDetail.getErfLocSourceCityId());
		scServiceDetail.setErfLocSrcCountryId(odrServiceDetail.getErfLocSrcCountryId());
		scServiceDetail.setErfPrdCatalogOfferingId(odrServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetail.setErfPrdCatalogOfferingName(odrServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogParentProductOfferingName(
				odrServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetail.setErfPrdCatalogProductId(odrServiceDetail.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogProductName(odrServiceDetail.getErfPrdCatalogProductName());
		scServiceDetail.setGscOrderSequenceId(odrServiceDetail.getGscOrderSequenceId());
		scServiceDetail.setIsActive(odrServiceDetail.getIsActive());
		scServiceDetail.setIsIzo(odrServiceDetail.getIsIzo());
		scServiceDetail.setLastmileBwAltName(odrServiceDetail.getLastmileBwAltName());
		
		if(odrServiceDetail.getOdrGstAddress()!=null) {
			ScGstAddress scGstAddress=new ScGstAddress();
			scGstAddress.setBuildingName(odrServiceDetail.getOdrGstAddress().getBuildingName());
			scGstAddress.setBuildingNumber(odrServiceDetail.getOdrGstAddress().getBuildingNumber());
			scGstAddress.setCreatedBy(odrServiceDetail.getOdrGstAddress().getCreatedBy());
			scGstAddress.setCreatedTime(odrServiceDetail.getOdrGstAddress().getCreatedTime());
			scGstAddress.setDistrict(odrServiceDetail.getOdrGstAddress().getDistrict());
			scGstAddress.setFlatNumber(odrServiceDetail.getOdrGstAddress().getFlatNumber());
			scGstAddress.setLatitude(odrServiceDetail.getOdrGstAddress().getLatitude());
			scGstAddress.setLocality(odrServiceDetail.getOdrGstAddress().getLocality());
			scGstAddress.setLongitude(odrServiceDetail.getOdrGstAddress().getLongitude());
			scGstAddress.setPincode(odrServiceDetail.getOdrGstAddress().getPincode());
			scGstAddress.setState(odrServiceDetail.getOdrGstAddress().getState());
			scGstAddress.setStreet(odrServiceDetail.getOdrGstAddress().getStreet());
			scServiceDetail.setScGstAddress(scGstAddress);
		}
		
				
		try {
			if (odrOrderBean.getErfCustCustomerName() != null && !odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				LOGGER.info("odrOrderBean.getErfCustCustomerName()={}",odrOrderBean.getErfCustCustomerName());
			scServiceDetail.setVpnSolutionId(StringUtils.trimToEmpty(splitCustomerName(odrOrderBean.getErfCustCustomerName())));
			}
			
			if(odrServiceDetail.getLastmileBw()!=null) {
				Integer localLoopBw = Integer.valueOf(odrServiceDetail.getLastmileBw());
				if(localLoopBw < 622)scServiceDetail.setLineRate("STM4");
				else if(localLoopBw > 622 && localLoopBw < 2500)scServiceDetail.setLineRate("STM16");
				else if(localLoopBw > 2500)scServiceDetail.setLineRate("STM64");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in LineRate => {}", e);
		}
	
		scServiceDetail.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
		scServiceDetail.setLocalItContactName(odrServiceDetail.getLocalItContactName());		
		scServiceDetail.setMrc(odrServiceDetail.getMrc());
		scServiceDetail.setNrc(odrServiceDetail.getNrc());
		scServiceDetail.setScOrderUuid(odrServiceDetail.getOdrOrderUuid());
		
		Set<ScServiceAttribute> scServiceAttrBeans = new HashSet<>();
		for (OdrServiceAttributeBean odrServiceAttribute : odrServiceDetail.getOdrServiceAttributes()) {
			
			if(odrServiceAttribute.getAttributeName().equals("Service Variant")) {
				scServiceDetail.setServiceVariant(odrServiceAttribute.getAttributeValue());
			}
			ScServiceAttribute serviceAttr = mapServiceAttrEntityToBean(odrServiceAttribute);
			serviceAttr.setScServiceDetail(scServiceDetail);
			scServiceAttrBeans.add(serviceAttr);
		}
		
		scServiceDetail.setScServiceAttributes(scServiceAttrBeans);
		Set<ScServiceSla> odrServiceSlaBeans = new HashSet<>();
		for (OdrServiceSlaBean odrServiceSla : odrServiceDetail.getOdrServiceSlas()) {
			ScServiceSla serviceSla = mapServiceSlaEntityToBean(odrServiceSla);
			serviceSla.setScServiceDetail(scServiceDetail);
			odrServiceSlaBeans.add(serviceSla);
		}
		scServiceDetail.setScServiceSlas(odrServiceSlaBeans);
		scServiceDetail.setParentBundleServiceId(odrServiceDetail.getParentBundleServiceId());
		scServiceDetail.setParentId(odrServiceDetail.getParentId());
		
		scServiceDetail.setSourceCountryCode(odrServiceDetail.getSourceCountryCode());
		scServiceDetail.setSourceCountryCodeRepc(odrServiceDetail.getSourceCountryCodeRepc());
		
				
		scServiceDetail.setPrimarySecondary(odrServiceDetail.getPrimarySecondary());
		//scServiceDetail.setPriSecServiceLink(odrServiceDetail.getPriSecServiceLink());
		scServiceDetail.setErdPriSecServiceLinkId(odrServiceDetail.getErfPriSecServiceLinkId());
		scServiceDetail.setProductReferenceId(odrServiceDetail.getProductReferenceId());
		scServiceDetail.setServiceClass(odrServiceDetail.getServiceClass());
		scServiceDetail.setServiceClassification(odrServiceDetail.getServiceClassification());
		scServiceDetail.setServiceCommissionedDate(odrServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(odrServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		scServiceDetail.setServiceGroupId(odrServiceDetail.getServiceGroupId());
		scServiceDetail.setServiceGroupType(odrServiceDetail.getServiceGroupType());
		scServiceDetail.setServiceStatus(odrServiceDetail.getServiceStatus());
		scServiceDetail.setServiceTerminationDate(odrServiceDetail.getServiceTerminationDate() != null
				? new Timestamp(odrServiceDetail.getServiceTerminationDate().getTime())
				: null);
		scServiceDetail.setServiceTopology(odrServiceDetail.getServiceTopology());
		scServiceDetail.setSiteAlias(odrServiceDetail.getSiteAlias());
		scServiceDetail.setSiteAddress(odrServiceDetail.getSiteAddress());
		scServiceDetail.setSiteEndInterface(odrServiceDetail.getSiteEndInterface());
		// scServiceDetail.setSiteLatLang(odrServiceDetail.getSiteLatLang());
		scServiceDetail.setSiteLinkLabel(odrServiceDetail.getSiteLinkLabel());
		scServiceDetail.setSiteTopology(odrServiceDetail.getSiteTopology());
		scServiceDetail.setSlaTemplate(odrServiceDetail.getSlaTemplate());
		scServiceDetail.setSmEmail(odrServiceDetail.getSmEmail());
		
		scServiceDetail.setSupplOrgNo(odrServiceDetail.getSupplOrgNo());
		scServiceDetail.setTpsCopfId(odrServiceDetail.getTpsCopfId());
		scServiceDetail.setTpsServiceId(odrServiceDetail.getTpsServiceId());
		scServiceDetail.setTpsSourceServiceId(odrServiceDetail.getTpsSourceServiceId());
		scServiceDetail.setUpdatedBy(odrServiceDetail.getUpdatedBy());
		scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));//
//		scServiceDetail.setUuid(odrServiceDetail.getUuid()!=null ? odrServiceDetail.getUuid():getServiceCode("NPL
//						odrServiceDetail.getDestinationCity()!=null?odrServiceDetail.getDestinationCity():"", String.valueOf(odrServiceDetail.getId())));		
				scServiceDetail.setUuid(odrServiceDetail.getUuid());	
		
		scServiceDetail.setParentUuid(odrServiceDetail.getParentServiceUuid());
		scServiceDetail.setErfOdrServiceId(odrServiceDetail.getId());
		scServiceDetail.setVpnName(odrServiceDetail.getVpnName());
		return scServiceDetail;
	}
	
	public ScWebexServiceCommercial mapOdrCommercialToWebexCommercial(OdrWebexCommercialBean odrWebexCommercialBean) {
		ScWebexServiceCommercial scWebexServiceCommercial = new ScWebexServiceCommercial();
		scWebexServiceCommercial.setComponentName(odrWebexCommercialBean.getComponentName());
		scWebexServiceCommercial.setComponentDesc(odrWebexCommercialBean.getComponentDesc());
		scWebexServiceCommercial.setComponentType(odrWebexCommercialBean.getComponentType());
		scWebexServiceCommercial.setHsnCode(odrWebexCommercialBean.getHsnCode());
		scWebexServiceCommercial.setQuantity(odrWebexCommercialBean.getQuantity());
		scWebexServiceCommercial.setArc(odrWebexCommercialBean.getArc());
		scWebexServiceCommercial.setUnitMrc(odrWebexCommercialBean.getUnitMrc());
		scWebexServiceCommercial.setMrc(odrWebexCommercialBean.getMrc());
		scWebexServiceCommercial.setNrc(odrWebexCommercialBean.getNrc());
		scWebexServiceCommercial.setUnitNrc(odrWebexCommercialBean.getUnitNrc());
		scWebexServiceCommercial.setTcv(odrWebexCommercialBean.getTcv());
		scWebexServiceCommercial.setCiscoUnitListPrice(odrWebexCommercialBean.getCiscoUnitListPrice());
		scWebexServiceCommercial.setCiscoDiscountPercent(odrWebexCommercialBean.getCiscoDiscountPercent());
		scWebexServiceCommercial.setCiscoUnitNetPrice(odrWebexCommercialBean.getCiscoUnitNetPrice());
		scWebexServiceCommercial.setContractType(odrWebexCommercialBean.getContractType());
		scWebexServiceCommercial.setEndpointManagementType(odrWebexCommercialBean.getEndpointManagementType());
		scWebexServiceCommercial.setIsActive(odrWebexCommercialBean.getIsActive());
		scWebexServiceCommercial.setShortText(odrWebexCommercialBean.getShortText());
		return scWebexServiceCommercial;
	}

	/**
	 * Method to map bean to entity.
	 * @param odrTeamsDRCommercialBean
	 * @return
	 */
	public ScTeamsDRServiceCommercial mapOdrCommercialToTeamsDRCommercial(OdrTeamsDRCommercialBean odrTeamsDRCommercialBean) {
		ScTeamsDRServiceCommercial scTeamsDRServiceCommercial = new ScTeamsDRServiceCommercial();
		scTeamsDRServiceCommercial.setComponentName(odrTeamsDRCommercialBean.getComponentName());
		scTeamsDRServiceCommercial.setComponentDesc(odrTeamsDRCommercialBean.getComponentDesc());
		scTeamsDRServiceCommercial.setComponentType(odrTeamsDRCommercialBean.getComponentType());
		scTeamsDRServiceCommercial.setChargeItem(odrTeamsDRCommercialBean.getChargeItem());
		scTeamsDRServiceCommercial.setHsnCode(odrTeamsDRCommercialBean.getHsnCode());
		scTeamsDRServiceCommercial.setQuantity(odrTeamsDRCommercialBean.getQuantity());
		scTeamsDRServiceCommercial.setArc(odrTeamsDRCommercialBean.getArc());
		scTeamsDRServiceCommercial.setUnitMrc(odrTeamsDRCommercialBean.getUnitMrc());
		scTeamsDRServiceCommercial.setMrc(odrTeamsDRCommercialBean.getMrc());
		scTeamsDRServiceCommercial.setNrc(odrTeamsDRCommercialBean.getNrc());
		scTeamsDRServiceCommercial.setUnitNrc(odrTeamsDRCommercialBean.getUnitNrc());
		scTeamsDRServiceCommercial.setTcv(odrTeamsDRCommercialBean.getTcv());
		scTeamsDRServiceCommercial.setIsActive(odrTeamsDRCommercialBean.getIsActive());
		scTeamsDRServiceCommercial.setContractType(odrTeamsDRCommercialBean.getContractType());
		scTeamsDRServiceCommercial.setCreatedBy(odrTeamsDRCommercialBean.getCreatedBy());
		scTeamsDRServiceCommercial.setCreatedDate(new Timestamp(new Date().getTime()));
		scTeamsDRServiceCommercial.setUsage(odrTeamsDRCommercialBean.getEffectiveUsage());
		scTeamsDRServiceCommercial.setOverage(odrTeamsDRCommercialBean.getEffectiveOverage());
		return scTeamsDRServiceCommercial;
	} 
}
