package com.tcl.dias.serviceinventory.helper.mapper;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.tcl.dias.common.serviceinventory.bean.CpeBean;
import com.tcl.dias.common.serviceinventory.bean.MuxDetailsBean;
import com.tcl.dias.common.serviceinventory.bean.RouterDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScComponentAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScComponentBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceQoBean;
import com.tcl.dias.common.serviceinventory.bean.UniswitchDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.serviceinventory.entity.entities.SIComponent;
import com.tcl.dias.serviceinventory.entity.entities.SIComponentAttribute;
import com.tcl.dias.serviceinventory.entity.repository.SIComponentAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.bean.ScAttachmentBean;
import com.tcl.dias.common.serviceinventory.bean.ScContractInfoBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ScProductDetailAttributesBean;
import com.tcl.dias.common.serviceinventory.bean.ScProductDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceCommercialBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceSlaBean;
import com.tcl.dias.serviceinventory.entity.entities.Attachment;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetCommercial;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetComponent;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetToService;
import com.tcl.dias.serviceinventory.entity.entities.SIAttachment;
import com.tcl.dias.serviceinventory.entity.entities.SIContractInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIOrderAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceSla;
import com.tcl.dias.serviceinventory.entity.entities.SiServiceContact;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderRepository;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 *
 */
@Component
public class ServiceInventoryHelperMapper {
	
	@Autowired
	SIOrderRepository siOrderRepository;

	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;

	@Autowired
	SIOrderAttributeRepository siOrderAttributeRepository;

	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;

	@Autowired
	SIComponentAttributeRepository siComponentAttributeRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryHelperMapper.class);

	public SIOrderAttribute getSiOrderByAttrName(SIOrder siOrder, String attributeName){
		SIOrderAttribute siOrderAttribute = siOrderAttributeRepository
				.findFirstBySiOrderAndAttributeNameOrderByIdDesc(siOrder, attributeName);
		return Objects.nonNull(siOrderAttribute) ? siOrderAttribute : null;
	}

	public SIComponentAttribute getSiComponentAttr(SIServiceDetail siServiceDetail, String attrName){
		SIComponentAttribute siComponentAttribute = siComponentAttributeRepository
				.findFirstBySiServiceDetailIdAndAttributeName(siServiceDetail.getId(), attrName);
		return Objects.nonNull(siComponentAttribute) ? siComponentAttribute : null;
	}
	
	public SIComponentAttribute getSiComponentAttrFindFirstOrderByIdDesc(SIServiceDetail siServiceDetail, String attrName){
		SIComponentAttribute siComponentAttribute = siComponentAttributeRepository
				.findFirstBySiServiceDetailIdAndAttributeNameOrderByIdDesc(siServiceDetail.getId(), attrName);
		return Objects.nonNull(siComponentAttribute) ? siComponentAttribute : null;
	}

	public SIServiceAttribute getSiServiceAttr(SIServiceDetail siServiceDetail, String attrName){
		SIServiceAttribute siServiceAttribute  = siServiceAttributeRepository
				.findBySiServiceDetailAndAttributeNameOrderByIdDesc(siServiceDetail, attrName);
		return Objects.nonNull(siServiceAttribute) ? siServiceAttribute : null;
	}

	public SIOrder mapScOrderBeanToEntity(ScOrderBean scOrder) {

		SIOrder siOrder = new SIOrder();
		siOrder.setCreatedBy(scOrder.getCreatedBy());
		siOrder.setCreatedDate(new Timestamp(new Date().getTime()));
		siOrder.setCustomerGroupName(scOrder.getCustomerGroupName());
		siOrder.setCustomerSegment(scOrder.getCustomerSegment());
		if(scOrder.getDemoFlag()!=null) {
			if(scOrder.getDemoFlag().equalsIgnoreCase("Y")) {
				siOrder.setDemoFlag("Yes");
			}else if(scOrder.getDemoFlag().equalsIgnoreCase("N")) {
				siOrder.setDemoFlag("No");
			}
		}else {
			siOrder.setDemoFlag("No");
		}
		siOrder.setErfCustCustomerId(
				scOrder.getErfCustCustomerId() != null ? String.valueOf(scOrder.getErfCustCustomerId()) : null);
		siOrder.setErfCustCustomerName(scOrder.getErfCustCustomerName());
		siOrder.setErfCustLeId(scOrder.getErfCustLeId());
		siOrder.setErfCustLeName(scOrder.getErfCustLeName());
		siOrder.setErfCustPartnerId(scOrder.getErfCustPartnerId());
		siOrder.setErfCustPartnerName(scOrder.getErfCustPartnerName());
		siOrder.setErfCustPartnerLeId(scOrder.getErfCustPartnerLeId());
		siOrder.setPartnerCuid(scOrder.getPartnerCuid());
		siOrder.setErfCustSpLeId(scOrder.getErfCustSpLeId());
		siOrder.setErfCustSpLeName(scOrder.getErfCustSpLeName());
		siOrder.setErfUserCustomerUserId(scOrder.getErfUserCustomerUserId());
		siOrder.setErfUserInitiatorId(scOrder.getErfUserInitiatorId());
		siOrder.setPartnerCuid(Objects.nonNull(scOrder.getErfCustPartnerId()) ?
				String.valueOf(scOrder.getErfCustPartnerId()) : null);
		siOrder.setUuid(scOrder.getUuid());
		siOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
		siOrder.setUpdatedBy(scOrder.getUpdatedBy());
		siOrder.setTpsSfdcCuid(scOrder.getTpsSfdcCuid());
		siOrder.setTpsSecsId(scOrder.getTpsSecsId());
		siOrder.setTpsCrmSystem(scOrder.getTpsCrmSystem());
		// Setting tpsCrmOptyId, using sfdcOptyId
		siOrder.setTpsCrmOptyId(scOrder.getSfdcOptyId());
		siOrder.setTpsCrmCofId(scOrder.getTpsCrmCofId());
		siOrder.setSfdcAccountId(scOrder.getSfdcAccountId());
		siOrder.setParentOpOrderCode(scOrder.getParentOpOrderCode());
		siOrder.setParentId(scOrder.getParentId());
		siOrder.setOrderType(scOrder.getOrderType());
		siOrder.setOrderStatus(scOrder.getOrderStatus()!=null?scOrder.getOrderStatus():"Active");
		siOrder.setOrderStartDate(
				scOrder.getOrderStartDate() != null ? new Timestamp(scOrder.getOrderStartDate().getTime()) : null);
		siOrder.setOrderSource(scOrder.getOrderSource());
		siOrder.setOrderEndDate(
				scOrder.getOrderEndDate() != null ? new Timestamp(scOrder.getOrderEndDate().getTime()) : null);
		siOrder.setOrderCategory(scOrder.getOrderCategory());
		//siOrder.setOrderSubCategory(scOrder.getOrderSubCategory());
		siOrder.setOpOrderCode(scOrder.getOpOrderCode());
		siOrder.setOpportunityClassification(scOrder.getOpportunityClassification());
		siOrder.setIsActive(scOrder.getIsActive());
		siOrder.setIsBundleOrder(scOrder.getIsBundleOrder());
		siOrder.setIsMultipleLe(scOrder.getIsMultipleLe());
		siOrder.setErfOrderLeId(scOrder.getErfOrderLeId());
		siOrder.setErfOrderId(scOrder.getErfOrderId());
		siOrder.setLastMacdDate(
				"MACD".equalsIgnoreCase(scOrder.getOrderType()) ? new Timestamp(scOrder.getCreatedDate().getTime()) : null);
		siOrder.setMacdCreatedDate(
				"MACD".equalsIgnoreCase(scOrder.getOrderType()) ? new Timestamp(scOrder.getCreatedDate().getTime()) : null);

		return siOrder;
	}

	public SIContractInfo mapContractingInfoEntityToBean(ScContractInfoBean scContractingInfo) {
		SIContractInfo siContractInfo = new SIContractInfo();
		siContractInfo.setAccountManager(scContractingInfo.getAccountManager());
		siContractInfo.setAccountManagerEmail(scContractingInfo.getAccountManagerEmail());
		siContractInfo.setArc(scContractingInfo.getArc());
		siContractInfo.setMrc(scContractingInfo.getMrc());
		siContractInfo.setNrc(scContractingInfo.getNrc());
		siContractInfo.setBillingAddress(scContractingInfo.getBillingAddress());
		siContractInfo.setBillingFrequency(scContractingInfo.getBillingFrequency());
		siContractInfo.setBillingMethod(scContractingInfo.getBillingMethod());
		siContractInfo.setContractEndDate(scContractingInfo.getContractEndDate() != null
				? new Timestamp(scContractingInfo.getContractEndDate().getTime())
				: null);
		siContractInfo.setContractStartDate(scContractingInfo.getContractStartDate() != null
				? new Timestamp(scContractingInfo.getContractStartDate().getTime())
				: null);
		siContractInfo.setCreatedBy(scContractingInfo.getCreatedBy());
		siContractInfo.setCreatedDate(new Timestamp(new Date().getTime()));
		siContractInfo.setCustomerContact(scContractingInfo.getCustomerContact());
		siContractInfo.setCustomerContactEmail(scContractingInfo.getCustomerContactEmail());
		siContractInfo.setDiscountArc(scContractingInfo.getDiscountArc());
		siContractInfo.setDiscountMrc(scContractingInfo.getDiscountMrc());
		siContractInfo.setDiscountNrc(scContractingInfo.getDiscountNrc());
		siContractInfo.setErfCustCurrencyId(scContractingInfo.getErfCustCurrencyId());
		siContractInfo.setErfCustLeId(scContractingInfo.getErfCustLeId());
		siContractInfo.setErfCustLeName(scContractingInfo.getErfCustLeName());
		siContractInfo.setErfCustSpLeId(scContractingInfo.getErfCustSpLeId());
		siContractInfo.setErfCustSpLeName(scContractingInfo.getErfCustSpLeName());
		siContractInfo.setErfLocBillingLocationId(scContractingInfo.getErfLocBillingLocationId());
		siContractInfo.setIsActive(scContractingInfo.getIsActive());
		siContractInfo.setLastMacdDate(scContractingInfo.getLastMacdDate());
		siContractInfo.setOrderTermInMonths(scContractingInfo.getOrderTermInMonths());
		siContractInfo.setPaymentTerm(scContractingInfo.getPaymentTerm());
		siContractInfo.setTpsSfdcCuid(scContractingInfo.getTpsSfdcCuid());
		siContractInfo.setUpdatedBy(scContractingInfo.getUpdatedBy());
		siContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
		siContractInfo.setBillingContactId(scContractingInfo.getBillingContactId());
		return siContractInfo;
	}

	public SIOrderAttribute mapScOrderAttrEntityToBean(ScOrderAttributeBean scOrderAttribute) {
		SIOrderAttribute siOrderAttribute = new SIOrderAttribute();
		siOrderAttribute.setAttributeAltValueLabel(scOrderAttribute.getAttributeAltValueLabel());
		siOrderAttribute.setAttributeName(scOrderAttribute.getAttributeName());
		siOrderAttribute.setAttributeValue(scOrderAttribute.getAttributeValue());
		siOrderAttribute.setCategory(scOrderAttribute.getCategory());
		siOrderAttribute.setCreatedBy(scOrderAttribute.getCreatedBy());
		siOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siOrderAttribute.setIsActive(CommonConstants.Y);
		siOrderAttribute.setUpdatedBy(scOrderAttribute.getUpdatedBy());
		siOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siOrderAttribute;
	}

	public SIServiceDetail mapScServiceEntityToBean(ScServiceDetailBean scServiceDetail) {
		SIServiceDetail siServiceDetail = new SIServiceDetail();
		siServiceDetail.setAccessType(scServiceDetail.getAccessType());
		siServiceDetail.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
		siServiceDetail.setArc(scServiceDetail.getArc());
		siServiceDetail.setMrc(scServiceDetail.getMrc());
		siServiceDetail.setNrc(scServiceDetail.getNrc());
		siServiceDetail.setBillingAccountId(scServiceDetail.getBillingAccountId());
		siServiceDetail.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		siServiceDetail.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		siServiceDetail.setBurstableBwUnit(scServiceDetail.getBurstableBwUnit());
		siServiceDetail.setBwPortspeed(scServiceDetail.getBwPortspeed());
		siServiceDetail.setBwUnit(scServiceDetail.getBwUnit());
		siServiceDetail.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		siServiceDetail.setCallType(scServiceDetail.getCallType());
		siServiceDetail.setCreatedBy(scServiceDetail.getCreatedBy());
		siServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		siServiceDetail.setCustomerServiceId(scServiceDetail.getUuid());
		siServiceDetail.setCustOrgNo(scServiceDetail.getCustOrgNo());
		siServiceDetail.setDemarcationFloor(scServiceDetail.getDemarcationFloor());
		siServiceDetail.setDemarcationRack(scServiceDetail.getDemarcationRack());
		siServiceDetail.setDestinationCity(scServiceDetail.getDestinationCity());
		siServiceDetail.setDestinationCountry(scServiceDetail.getDestinationCountry());
		siServiceDetail.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
		siServiceDetail.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
		siServiceDetail.setDiscountArc(scServiceDetail.getDiscountArc());
		siServiceDetail.setDiscountMrc(scServiceDetail.getDiscountMrc());
		siServiceDetail.setDiscountNrc(scServiceDetail.getDiscountNrc());
		siServiceDetail.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
		siServiceDetail.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
		siServiceDetail.setErfLocPopSiteAddressId(scServiceDetail.getErfLocPopSiteAddressId());
		siServiceDetail.setErfLocSiteAddressId(scServiceDetail.getErfLocSiteAddressId());
		siServiceDetail.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
		siServiceDetail.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
		siServiceDetail.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
		siServiceDetail.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		siServiceDetail
				.setErfPrdCatalogParentProductOfferingName(scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		siServiceDetail.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		siServiceDetail.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
		siServiceDetail.setErfScServiceId(scServiceDetail.getErfScServiceId());
		siServiceDetail.setFeasibilityId(scServiceDetail.getFeasibilityId());
		siServiceDetail.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
		siServiceDetail.setIsActive(StringUtils.trimToEmpty(scServiceDetail.getIsActive()));
		siServiceDetail.setIsIzo(scServiceDetail.getIsIzo());
		siServiceDetail.setLastmileBw(scServiceDetail.getLastmileBw());
		siServiceDetail.setLastmileBwAltName(scServiceDetail.getLastmileBwAltName());
		siServiceDetail.setLastmileBwUnit(scServiceDetail.getLastmileBwUnit());
		siServiceDetail.setLastmileProvider(scServiceDetail.getLastmileProvider());
		siServiceDetail.setLastmileType(scServiceDetail.getLastmileType());
		siServiceDetail.setLatLong(scServiceDetail.getLatLong());
		siServiceDetail.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
		siServiceDetail.setParentId(scServiceDetail.getParentId());
		siServiceDetail.setPopSiteAddress(scServiceDetail.getPopSiteAddress());
		siServiceDetail.setPopSiteCode(scServiceDetail.getPopSiteCode());
		if(Objects.nonNull(scServiceDetail.getPrimarySecondary()))
			siServiceDetail.setPrimarySecondary(scServiceDetail.getPrimarySecondary().toUpperCase());
		siServiceDetail.setProductReferenceId(scServiceDetail.getProductReferenceId());
		siServiceDetail.setPriSecServiceLink(scServiceDetail.getPriSecServiceLink());
		siServiceDetail.setServiceClass(scServiceDetail.getServiceClass());
		siServiceDetail.setServiceClassification(scServiceDetail.getServiceClassification());
		siServiceDetail.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate() != null
				? new Timestamp(scServiceDetail.getServiceCommissionedDate().getTime())
				: null);
		siServiceDetail.setServiceGroupId(scServiceDetail.getServiceGroupId());
		siServiceDetail.setServiceGroupType(scServiceDetail.getServiceGroupType());
		siServiceDetail.setServiceOption(scServiceDetail.getServiceOption());
		siServiceDetail.setServiceStatus("Active");
		siServiceDetail.setServiceTerminationDate(scServiceDetail.getServiceTerminationDate() != null
				? new Timestamp(scServiceDetail.getServiceTerminationDate().getTime())
				: null);
//		siServiceDetail.setServiceTopology(scServiceDetail.getServiceTopology());
		siServiceDetail.setSiteAddress(scServiceDetail.getSiteAddress());
		siServiceDetail.setSiteAlias(scServiceDetail.getSiteAlias());
		siServiceDetail.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
		siServiceDetail.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		siServiceDetail.setSiteType(scServiceDetail.getSiteType());
		siServiceDetail.setSlaTemplate(scServiceDetail.getSlaTemplate());
		siServiceDetail.setSmEmail(scServiceDetail.getSmEmail());
		siServiceDetail.setSourceCity(scServiceDetail.getSourceCity());
		siServiceDetail.setSourceCountry(scServiceDetail.getSourceCountry());
		siServiceDetail.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		siServiceDetail.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		siServiceDetail.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		siServiceDetail.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
		siServiceDetail.setBurstableBwPortspeed(scServiceDetail.getBurstableBwPortspeed());
		siServiceDetail.setTpsCopfId(scServiceDetail.getTpsCopfId());
		siServiceDetail.setTpsServiceId(scServiceDetail.getUuid());
		siServiceDetail.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		siServiceDetail.setUpdatedBy("Optimus_O2C");
		siServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));

		siServiceDetail.setUuid(scServiceDetail.getUuid());
		siServiceDetail.setVpnName(scServiceDetail.getVpnName());
		siServiceDetail.setSiOrderUuid(scServiceDetail.getScOrderUuid());
		siServiceDetail.setServiceVarient(scServiceDetail.getServiceVariant());
		siServiceDetail.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		siServiceDetail.setSmName(scServiceDetail.getSmName());
		siServiceDetail.setSiteLatLang(scServiceDetail.getSiteLatLang());
		siServiceDetail.setSiteType(scServiceDetail.getSiteType());
		siServiceDetail.setBwPortspeed(scServiceDetail.getBwPortspeed());
		siServiceDetail.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		siServiceDetail.setDemarcationApartment(scServiceDetail.getDemarcationApartment());
		siServiceDetail.setDestinationState(scServiceDetail.getDestinationState());
		siServiceDetail.setSourceState(scServiceDetail.getSourceState());
		siServiceDetail.setOrderType(scServiceDetail.getOrderType());
		siServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
		siServiceDetail.setAssignedPm(scServiceDetail.getAssignedPm());
		siServiceDetail.setOrderType(scServiceDetail.getOrderType());
		siServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
		siServiceDetail.setParentUuid(scServiceDetail.getParentUuid());
		siServiceDetail.setErfPrdCatalogFlavourName(scServiceDetail.getErfPrdCatalogOfferingName());
		// siServiceDetail.setDemarcationRoom(scServiceDetail.getDemarcationRoom());
		Set<SIServiceAttribute> siServiceAttrBeans = new HashSet<>();
		for (ScServiceAttributeBean siServiceAttribute : scServiceDetail.getScServiceAttributes()) {
			SIServiceAttribute siServiceAttr = mapScServiceAttrEntityToBean(siServiceAttribute);
			siServiceAttr.setSiServiceDetail(siServiceDetail);
			siServiceAttrBeans.add(siServiceAttr);
		}
		siServiceDetail.setSiServiceAttributes(siServiceAttrBeans);
		Set<SIServiceSla> siServiceSlaBeans = new HashSet<>();
		for (ScServiceSlaBean scServiceSla : scServiceDetail.getScServiceSlas()) {
			SIServiceSla siServiceSla = mapScServiceSlaEntityToBean(scServiceSla);
			siServiceSla.setSiServiceDetail(siServiceDetail);
			siServiceSlaBeans.add(siServiceSla);
		}
		siServiceDetail.setSiServiceSlas(siServiceSlaBeans);
		siServiceDetail.setParentUuid(scServiceDetail.getParentUuid());
		siServiceDetail.setErfPrdCatalogFlavourName(scServiceDetail.getErfPrdCatalogFlavourName());
		siServiceDetail.setTigerOrderId(scServiceDetail.getTigerOrderId());
		siServiceDetail.setContractStartDate(scServiceDetail.getContractStartDate());
		siServiceDetail.setContractEndDate(scServiceDetail.getContractEndDate());
		siServiceDetail.setAdditionalIpsReq(scServiceDetail.getAdditionalIpsReq());
		//Saving multi VRF values
		LOGGER.info("is multi VRF service id :{}",scServiceDetail.getIsMultiVrf());
		if(CommonConstants.GVPN.equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())&&
				scServiceDetail.getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())) {
			siServiceDetail.setIsmultivrf((byte)1);
			siServiceDetail.setMastervrfServiceId(String.valueOf(scServiceDetail.getMasterVrfServiceId()));
			siServiceDetail.setMultiVrfSolution(scServiceDetail.getMultiVrfSolution());
		}
		return siServiceDetail;
	}

	private SIServiceSla mapScServiceSlaEntityToBean(ScServiceSlaBean scServiceSlaBean) {
		SIServiceSla siServiceSla = new SIServiceSla();
		siServiceSla.setCreatedBy(scServiceSlaBean.getCreatedBy());
		siServiceSla.setCreatedTime(new Timestamp(new Date().getTime()));
		siServiceSla.setIsActive(scServiceSlaBean.getIsActive());
		siServiceSla.setSlaComponent(scServiceSlaBean.getSlaComponent());
		siServiceSla.setSlaValue(scServiceSlaBean.getSlaValue());
		siServiceSla.setUpdatedBy(scServiceSlaBean.getUpdatedBy());
		siServiceSla.setUpdatedTime(new Timestamp(new Date().getTime()));
		return siServiceSla;
	}

	private SIServiceAttribute mapScServiceAttrEntityToBean(ScServiceAttributeBean scServiceAttributeBean) {
		SIServiceAttribute siServiceAttribute = new SIServiceAttribute();
		siServiceAttribute.setAttributeAltValueLabel(scServiceAttributeBean.getAttributeAltValueLabel());
		siServiceAttribute.setAttributeName(scServiceAttributeBean.getAttributeName());
		siServiceAttribute.setAttributeValue(scServiceAttributeBean.getAttributeValue());
		siServiceAttribute.setCategory(scServiceAttributeBean.getCategory());
		siServiceAttribute.setCreatedBy(scServiceAttributeBean.getCreatedBy());
		siServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siServiceAttribute.setIsActive(scServiceAttributeBean.getIsActive());
		siServiceAttribute.setUpdatedBy(scServiceAttributeBean.getUpdatedBy());
		siServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siServiceAttribute;
	}

	public SIAttachment mapScServiceAttachmentToEntity(ScAttachmentBean sAttachmentBean,
			SIServiceDetail siServiceEntity) {
		Attachment attachment = new Attachment();
		attachment.setCategory(
				sAttachmentBean.getCategory() == null ? sAttachmentBean.getType() : sAttachmentBean.getCategory());
		attachment.setContentTypeHeader(sAttachmentBean.getContentTypeHeader());
		attachment.setCreatedBy(sAttachmentBean.getCreatedBy());
		attachment.setUpdatedDate(new Timestamp(new Date().getTime()));
		attachment.setUpdatedBy(sAttachmentBean.getUpdatedBy());
		attachment.setCreatedDate(sAttachmentBean.getCreatedDate());
		attachment.setIsActive(CommonConstants.Y);
		attachment.setName(sAttachmentBean.getName());
		attachment.setStoragePathUrl(sAttachmentBean.getStoragePathUrl());
		attachment.setType(sAttachmentBean.getType());
		SIAttachment siAttachment = new SIAttachment();
		siAttachment.setAttachment(attachment);
		siAttachment.setIsActive(CommonConstants.Y);
		siAttachment.setOfferingName(sAttachmentBean.getOfferingName());
		siAttachment.setOrderId(sAttachmentBean.getOrderId());
		siAttachment.setProductName(sAttachmentBean.getProductName());
		siAttachment.setSiteId(sAttachmentBean.getSiteId());
		siAttachment.setSiServiceDetail(siServiceEntity);
		return siAttachment;
	}

	public SIAsset mapScProductDetailEntityToBean(ScProductDetailBean scProductDetail) {
		SIAsset siAsset = new SIAsset();
		siAsset.setType(scProductDetail.getType());
		siAsset.setName(scProductDetail.getSolutionName());
		// siAsset.setIsActive(scProductDetail.getIsActive());
		siAsset.setCreatedBy(scProductDetail.getCreatedBy());
		siAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		siAsset.setUpdatedBy(scProductDetail.getUpdatedBy());
		siAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
		if (Objects.nonNull(scProductDetail.getCloudCode())) {
			siAsset.setCloudCode(scProductDetail.getCloudCode());
		}
		if (Objects.nonNull(scProductDetail.getParentCloudCode())) {
			siAsset.setParentCloudCode(scProductDetail.getParentCloudCode());
		}
		siAsset.setBusinessUnit(scProductDetail.getBusinessUnit());
		siAsset.setZone(scProductDetail.getZone());
		siAsset.setEnvironment(scProductDetail.getEnvironment());
		return siAsset;
	}

	public Set<SIAssetAttribute> mapScProductDetailAttr(List<ScProductDetailAttributesBean> scProductAttributes,
			SIAsset siAsset) {
		Set<SIAssetAttribute> siAssetAttributes = new HashSet<>();
		List<String> siAttrCategoryList = new ArrayList<>();
		for (ScProductDetailAttributesBean scProductDetailAttribute : scProductAttributes) {
			SIAssetAttribute siAssetAttribute = mapScProductDetailAttributeToBean(scProductDetailAttribute);
			siAttrCategoryList.add(siAssetAttribute.getCategory());
			siAssetAttribute.setSiAsset(siAsset);
			siAssetAttributes.add(siAssetAttribute);
		}
		return siAssetAttributes;
	}

	private SIAssetAttribute mapScProductDetailAttributeToBean(ScProductDetailAttributesBean scProductDetailAttribute) {
		SIAssetAttribute siAssetAttribute = new SIAssetAttribute();
		siAssetAttribute.setCategory(scProductDetailAttribute.getCategory());
		siAssetAttribute.setAttributeName(scProductDetailAttribute.getAttributeName());
		siAssetAttribute.setAttributeValue(scProductDetailAttribute.getAttributeValue());
		siAssetAttribute.setIsActive(scProductDetailAttribute.getIsActive());
		siAssetAttribute.setCreatedBy(scProductDetailAttribute.getCreatedBy());
		siAssetAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siAssetAttribute.setUpdatedBy(scProductDetailAttribute.getUpdatedBy());
		siAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siAssetAttribute;
	}

	public SIAssetToService mapSIAssetToService(SIServiceDetail siServiceEntity, SIAsset siAsset) {
		SIAssetToService siAssetToService = new SIAssetToService();
		siAssetToService.setSiServiceDetail(siServiceEntity);
		siAssetToService.setSiAsset(siAsset);
		return siAssetToService;
	}

	public SIAssetCommercial mapScProductPriceDetailToBean(ScProductDetailBean scProductDetail, SIAsset siAsset) {
		SIAssetCommercial siAssetCommercial = new SIAssetCommercial();
		siAssetCommercial.setMrc(scProductDetail.getMrc());
		siAssetCommercial.setNrc(scProductDetail.getNrc());
		siAssetCommercial.setArc(scProductDetail.getArc());
		siAssetCommercial.setSIAsset(siAsset);
		return siAssetCommercial;
	}

	public SIAssetComponent mapScServiceCommercialToComponent(ScServiceCommercialBean scServiceCommercial,
			SIAssetCommercial siAssetCommercial) {
		SIAssetComponent siAssetComponent = new SIAssetComponent();
		siAssetComponent.setSiAssetCommercial(siAssetCommercial);
		siAssetComponent.setArc(scServiceCommercial.getArc());
		siAssetComponent.setMrc(scServiceCommercial.getMrc());
		siAssetComponent.setNrc(scServiceCommercial.getNrc());
		siAssetComponent.setItem(scServiceCommercial.getItem());
		return siAssetComponent;
	}

	public SiServiceContact mapServiceContact(ScServiceDetailBean serviceDetail, ScOrderBean scOrderBean) {
		SiServiceContact siServiceContact = new SiServiceContact();
		siServiceContact.setContactName(serviceDetail.getLocalItContactName());
		siServiceContact.setBusinessEmail(serviceDetail.getLocalItContactEmail());
		siServiceContact.setBusinessMobile(serviceDetail.getLocalItContactMobile());
		siServiceContact.setCuid(scOrderBean.getTpsSfdcCuid());
		return siServiceContact;
	}

	public SIOrderAttribute mapOrderAttrEntityToBean(SIOrderAttribute prevSIOrderAttribute) {
		SIOrderAttribute siOrderAttribute = new SIOrderAttribute();
		siOrderAttribute.setAttributeAltValueLabel(prevSIOrderAttribute.getAttributeAltValueLabel());
		siOrderAttribute.setAttributeName(prevSIOrderAttribute.getAttributeName());
		siOrderAttribute.setAttributeValue(prevSIOrderAttribute.getAttributeValue());
		siOrderAttribute.setCategory(prevSIOrderAttribute.getCategory());
		siOrderAttribute.setCreatedBy(prevSIOrderAttribute.getCreatedBy());
		siOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siOrderAttribute.setIsActive(CommonConstants.Y);
		siOrderAttribute.setUpdatedBy(prevSIOrderAttribute.getUpdatedBy());
		siOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siOrderAttribute;
	}

	public SIAsset mapAssetEntityToBean(SIAsset prevSiAsset) {
		SIAsset siAsset = new SIAsset();
		siAsset.setType(prevSiAsset.getType());
		siAsset.setName(prevSiAsset.getName());
		// siAsset.setIsActive(prevSiAsset.getIsActive());
		siAsset.setCreatedBy(prevSiAsset.getCreatedBy());
		siAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		siAsset.setUpdatedBy(prevSiAsset.getUpdatedBy());
		siAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
		siAsset.setCloudCode(prevSiAsset.getCloudCode());
		siAsset.setParentCloudCode(prevSiAsset.getParentCloudCode());
		siAsset.setBusinessUnit(prevSiAsset.getBusinessUnit());
		siAsset.setZone(prevSiAsset.getZone());
		siAsset.setEnvironment(prevSiAsset.getEnvironment());
		return siAsset;
	}

	public Set<SIAssetAttribute> mapAssetAttr(Set<SIAssetAttribute> prevSiAssetAttributes, SIAsset siAsset) {
		Set<SIAssetAttribute> siAssetAttributes = new HashSet<>();
		List<String> siAttrCategoryList = new ArrayList<>();
		for (SIAssetAttribute prevSiAssetAttribute : prevSiAssetAttributes) {
			SIAssetAttribute siAssetAttribute = mapAssetAttributeToBean(prevSiAssetAttribute);
			siAttrCategoryList.add(siAssetAttribute.getCategory());
			siAssetAttribute.setSiAsset(siAsset);
			siAssetAttributes.add(siAssetAttribute);
		}
		return siAssetAttributes;
	}

	public SIAssetAttribute mapAssetAttributeToBean(SIAssetAttribute prevSiAssetAttribute) {
		SIAssetAttribute siAssetAttribute = new SIAssetAttribute();
		siAssetAttribute.setCategory(prevSiAssetAttribute.getCategory());
		siAssetAttribute.setAttributeName(prevSiAssetAttribute.getAttributeName());
		siAssetAttribute.setAttributeValue(prevSiAssetAttribute.getAttributeValue());
		siAssetAttribute.setIsActive(prevSiAssetAttribute.getIsActive());
		siAssetAttribute.setCreatedBy(prevSiAssetAttribute.getCreatedBy());
		siAssetAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siAssetAttribute.setUpdatedBy(prevSiAssetAttribute.getUpdatedBy());
		siAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siAssetAttribute;
	}

	public SIAssetCommercial mapAssetCommercialToBean(SIAssetCommercial prevSiAssetCommercial, SIAsset siAsset) {
		SIAssetCommercial siAssetCommercial = new SIAssetCommercial();
		siAssetCommercial.setMrc(prevSiAssetCommercial.getMrc());
		siAssetCommercial.setNrc(prevSiAssetCommercial.getNrc());
		siAssetCommercial.setArc(prevSiAssetCommercial.getArc());
		siAssetCommercial.setSIAsset(siAsset);
		return siAssetCommercial;
	}

	public SIAssetComponent mapAssetComponentToBean(SIAssetComponent prevSiAssetComp,
			SIAssetCommercial siAssetCommercial) {
		SIAssetComponent siAssetComponent = new SIAssetComponent();
		siAssetComponent.setSiAssetCommercial(siAssetCommercial);
		siAssetComponent.setArc(prevSiAssetComp.getArc());
		siAssetComponent.setMrc(prevSiAssetComp.getMrc());
		siAssetComponent.setNrc(prevSiAssetComp.getNrc());
		siAssetComponent.setItem(prevSiAssetComp.getItem());
		return siAssetComponent;
	}

	public SIAsset saveCpeAsset(CpeBean cpeBean, String serviceId, SIServiceDetail siServiceDetail, Map<String, String> attrMap, String cpeModel){
		SIAsset siAsset = new SIAsset();
		siAsset.setUpdatedDate(cpeBean.getLastModifiedDate());
		siAsset.setUpdatedBy(cpeBean.getModifiedBy());
		siAsset.setCircuitId(serviceId);
		siAsset.setManagementIp(cpeBean.getMgmtLoopbackV4address());
		siAsset.setName(cpeBean.getHostName());
		siAsset.setType("CPE");
		siAsset.setIsActive("Y");
		siAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		siAsset.setOemVendor(cpeBean.getMake());
		siAsset.setModel(Objects.nonNull(cpeBean.getModel()) ? cpeBean.getModel() : cpeModel);
		siAsset.setSerialNo(cpeBean.getDeviceId());
		if(attrMap.containsKey("CPE_MANAGED")){
			siAsset.setCpeManaged(attrMap.get("CPE_MANAGED"));
		}
		siAsset.setScopeOfManagement(attrMap.getOrDefault("SCOPE_OF_MANAGEMENT", null));
		siAsset.setDescription(attrMap.getOrDefault("cpeType", null));
		siAsset.setSupportType(attrMap.getOrDefault("cpeType", null));
		if(attrMap.containsKey("cpeInstallationPrDate")){
			siAsset.setDateOfInstallation(Timestamp.valueOf(attrMap.get("cpeInstallationPrDate") + " 00:00:00"));
		}
		siAsset.setCpeProvider(attrMap.getOrDefault("cpeSupplyHardwarePrVendorName", null));
		if(attrMap.containsKey("commissioningDate")){
			siAsset.setCommissionedDate(Timestamp.valueOf(attrMap.get("commissioningDate") + " 00:00:00"));
		}
		siAsset.setAssetStatus("ACTIVE");
		siAsset.setSiServiceDetail(siServiceDetail);
		return siAsset;
	}

	public SIComponent mapScComponentToEntity(ScComponentBean scComponentBean){
		SIComponent siComponent = new SIComponent();
		siComponent.setComponentName(scComponentBean.getComponentName());
		siComponent.setCreatedBy(scComponentBean.getCreatedBy());
		siComponent.setCreatedDate(new Timestamp(new Date().getTime()));
		siComponent.setIsActive(scComponentBean.getIsActive());
		siComponent.setUpdatedBy(scComponentBean.getUpdatedBy());
		siComponent.setUuid(scComponentBean.getUuid());
		siComponent.setSiteType(scComponentBean.getSiteType());
		siComponent.setUpdatedDate(new Timestamp(new Date().getTime()));
		siComponent.setSiteType(scComponentBean.getSiteType());
		return siComponent;
	}

	public SIComponentAttribute mapScComponentAttrToEntity(ScComponentAttributeBean scComponentAttributeBean,
															SIComponent siComponent){
		SIComponentAttribute siComponentAttribute = new SIComponentAttribute();
		siComponentAttribute.setAttributeAltValueLabel(scComponentAttributeBean.getAttributeAltValueLabel());
		siComponentAttribute.setAttributeName(scComponentAttributeBean.getAttributeName());
		siComponentAttribute.setAttributeValue(scComponentAttributeBean.getAttributeValue());
		siComponentAttribute.setCreatedBy(scComponentAttributeBean.getCreatedBy());
		siComponentAttribute.setIsActive(scComponentAttributeBean.getIsActive());
		siComponentAttribute.setSiComponent(siComponent);
		siComponentAttribute.setUpdatedBy(scComponentAttributeBean.getUpdatedBy());
		siComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		siComponentAttribute.setUuid(scComponentAttributeBean.getUuid());
		return siComponentAttribute;
	}

	public SIAsset saveRouterDetailAsset(RouterDetailBean routerDetailBean, String serviceId, SIServiceDetail siServiceDetail){
		SIAsset siAsset = new SIAsset();
		siAsset.setUpdatedDate(routerDetailBean.getLastModifiedDate());
		siAsset.setUpdatedBy(routerDetailBean.getModifiedBy());
		siAsset.setCircuitId(serviceId);
		siAsset.setManagementIp(routerDetailBean.getIpv4MgmtAddress());
		siAsset.setName(routerDetailBean.getRouterHostname());
		siAsset.setModel(routerDetailBean.getRouterModel());
		siAsset.setType("PEROUTER");
		siAsset.setCreatedDate(routerDetailBean.getStartDate());
		siAsset.setAssetStatus("ACTIVE");
		siAsset.setIsActive("Y");
		siAsset.setSiServiceDetail(siServiceDetail);
		return siAsset;
	}

	public SIAsset saveUniSwitchDetails(UniswitchDetailBean uniswitchDetailBean, String serviceId, SIServiceDetail siServiceDetail){
		SIAsset siAsset = new SIAsset();
		siAsset.setUpdatedDate(uniswitchDetailBean.getLastModifiedDate());
		siAsset.setUpdatedBy(uniswitchDetailBean.getModifiedBy());
		siAsset.setCircuitId(serviceId);
		siAsset.setType("UNISWITCH");
		siAsset.setCreatedDate(uniswitchDetailBean.getStartDate());
		siAsset.setAssetStatus("ACTIVE");
		siAsset.setManagementIp(uniswitchDetailBean.getMgmtIp());
		siAsset.setModel(uniswitchDetailBean.getSwitchModel());
		siAsset.setName(uniswitchDetailBean.getHostName());
		siAsset.setIsActive("Y");
		siAsset.setSiServiceDetail(siServiceDetail);
		return siAsset;
	}

	public SIAsset saveMuxDetails(MuxDetailsBean muxDetailsBean, String serviceCode, SIServiceDetail siServiceDetail) {
		SIAsset siAsset = new SIAsset();
		siAsset.setIsActive("Y");
		siAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		siAsset.setType("MUX");
		siAsset.setAssetStatus("ACTIVE");
		siAsset.setCircuitId(serviceCode);
		siAsset.setName(muxDetailsBean.getEndMuxNodePort());
		siAsset.setManagementIp(muxDetailsBean.getEndMuxNodeIp());
		siAsset.setModel(muxDetailsBean.getMuxMake());
		siAsset.setSiServiceDetail(siServiceDetail);
		return siAsset;
	}

	public List<SIServiceAttributeBean> getSiServiceAttributes(SIServiceDetail siServiceDetail){
		List<SIServiceAttributeBean> siServiceAttributeBeans=new ArrayList<>();
		List<SIServiceAttribute> siServiceAttributes  = siServiceAttributeRepository.findBySiServiceDetail(siServiceDetail);
		if(siServiceAttributes!=null && !siServiceAttributes.isEmpty()){
			siServiceAttributes.stream().filter(siServiceAttribute -> siServiceAttribute.getAttributeValue() !=null && !siServiceAttribute.getAttributeValue().isEmpty()).forEach(siComponentAttribute -> {
				SIServiceAttributeBean siServiceAttributeBean =new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(siComponentAttribute.getAttributeName());
				siServiceAttributeBean.setAttributeValue(siComponentAttribute.getAttributeValue());
				siServiceAttributeBeans.add(siServiceAttributeBean);
			});
		}
		return siServiceAttributeBeans;
	}

}
