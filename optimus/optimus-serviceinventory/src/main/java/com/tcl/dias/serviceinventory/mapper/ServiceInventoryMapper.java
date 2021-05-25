package com.tcl.dias.serviceinventory.mapper;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.ScAttachmentBean;
import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderAttributeBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.servicefulfillment.beans.ScProductDetailBean;
import com.tcl.dias.common.servicefulfillment.beans.ScProductDetailAttributesBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceAttributeBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceCommercialBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceDetailBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceSlaBean;
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

/**
 * This file contains the ServiceInventoryMapper.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class ServiceInventoryMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryMapper.class);

	public SIOrder mapScOrderBeanToEntity(ScOrderBean scOrder) {
		SIOrder siOrder = new SIOrder();
		siOrder.setCreatedBy(scOrder.getCreatedBy());
		siOrder.setCreatedDate(new Timestamp(new Date().getTime()));
		siOrder.setCustomerGroupName(scOrder.getCustomerGroupName());
		siOrder.setCustomerSegment(scOrder.getCustomerSegment());
		siOrder.setDemoFlag(scOrder.getDemoFlag());
		siOrder.setErfCustCustomerId(scOrder.getErfCustCustomerId()!=null? String.valueOf(scOrder.getErfCustCustomerId()):null);
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
		siOrder.setUuid(scOrder.getUuid());
		siOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
		siOrder.setUpdatedBy(scOrder.getUpdatedBy());
		siOrder.setTpsSfdcCuid(scOrder.getTpsSfdcCuid());
		siOrder.setTpsSecsId(scOrder.getTpsSecsId());
		siOrder.setTpsSapCrnId(scOrder.getTpsSapCrnId());
		siOrder.setTpsCrmSystem(scOrder.getTpsCrmSystem());
		//Setting tpsCrmOptyId, using sfdcOptyId
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
		siOrder.setOpOrderCode(scOrder.getOpOrderCode());
		siOrder.setOpportunityClassification(scOrder.getOpportunityClassification());
		siOrder.setIsActive(scOrder.getIsActive());
		siOrder.setIsBundleOrder(scOrder.getIsBundleOrder());
		siOrder.setIsMultipleLe(scOrder.getIsMultipleLe());
		if (!scOrder.getOpOrderCode().startsWith("IPC")) {
			siOrder.setLastMacdDate(
					scOrder.getLastMacdDate() != null ? new Timestamp(scOrder.getLastMacdDate().getTime())
							: new Timestamp(scOrder.getMacdCreatedDate().getTime()));
			siOrder.setMacdCreatedDate(
					scOrder.getMacdCreatedDate() != null ? new Timestamp(scOrder.getMacdCreatedDate().getTime())
							: null);
		}
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
		siServiceDetail.setArc(scServiceDetail.getArc());
		siServiceDetail.setMrc(scServiceDetail.getMrc());
		siServiceDetail.setNrc(scServiceDetail.getNrc());
		siServiceDetail.setBillingAccountId(scServiceDetail.getBillingAccountId());
		siServiceDetail.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		siServiceDetail.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		siServiceDetail.setBillingType(scServiceDetail.getBillingType());
		siServiceDetail.setBurstableBwUnit(scServiceDetail.getBurstableBwUnit());
		siServiceDetail.setBwPortspeed(scServiceDetail.getBwPortspeed());
		siServiceDetail.setBwUnit(scServiceDetail.getBwUnit());
		siServiceDetail.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		siServiceDetail.setCallType(scServiceDetail.getCallType());
		siServiceDetail.setCreatedBy(scServiceDetail.getCreatedBy());
		siServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
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
		siServiceDetail.setErfPrdCatalogParentProductOfferingName(
				scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		siServiceDetail.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		siServiceDetail.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
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
		if(Objects.nonNull(scServiceDetail.getPrimarySecondary())) {
			siServiceDetail.setPrimarySecondary(scServiceDetail.getPrimarySecondary().toUpperCase());
		}
		siServiceDetail.setProductReferenceId(scServiceDetail.getProductReferenceId());
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
		siServiceDetail.setServiceTopology(scServiceDetail.getServiceTopology());
		siServiceDetail.setSiteAddress(scServiceDetail.getSiteAddress());
		siServiceDetail.setSiteAlias(scServiceDetail.getSiteAlias());
		siServiceDetail.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
		siServiceDetail.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		siServiceDetail.setSiteTopology(scServiceDetail.getSiteTopology());
		siServiceDetail.setSiteType(scServiceDetail.getSiteType());
		siServiceDetail.setSlaTemplate(scServiceDetail.getSlaTemplate());
		siServiceDetail.setSmEmail(scServiceDetail.getSmEmail());
		siServiceDetail.setSourceCity(scServiceDetail.getSourceCity());
		siServiceDetail.setSourceCountry(scServiceDetail.getSourceCountry());
		siServiceDetail.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		siServiceDetail.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		siServiceDetail.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		siServiceDetail.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
		siServiceDetail.setTpsCopfId(scServiceDetail.getTpsCopfId());
		siServiceDetail.setTpsServiceId(scServiceDetail.getErfPrdCatalogProductName().equals("IPC")?scServiceDetail.getUuid():scServiceDetail.getTpsServiceId());
		siServiceDetail.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		siServiceDetail.setUpdatedBy("Optimus_O2C");
		siServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
		siServiceDetail.setUuid(scServiceDetail.getUuid());
		siServiceDetail.setVpnName(scServiceDetail.getVpnName());
		siServiceDetail.setSiOrderUuid(scServiceDetail.getScOrderUuid());
		siServiceDetail.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		siServiceDetail.setSmName(scServiceDetail.getSmName());
		siServiceDetail.setSiteLatLang(scServiceDetail.getSiteLatLang());
		siServiceDetail.setSiteType(scServiceDetail.getSiteType());
		siServiceDetail.setBwPortspeed(scServiceDetail.getBwPortspeed());
		siServiceDetail.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		siServiceDetail.setDemarcationApartment(scServiceDetail.getDemarcationApartment());
		siServiceDetail.setDemarcationRoom(scServiceDetail.getDemarcationRoom());
		siServiceDetail.setOrderType(scServiceDetail.getOrderType());
		siServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
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
		return siServiceDetail;
	}

	private SIServiceSla mapScServiceSlaEntityToBean(ScServiceSlaBean scServiceSla) {
		SIServiceSla siServiceSla = new SIServiceSla();
		siServiceSla.setCreatedBy(scServiceSla.getCreatedBy());
		siServiceSla.setCreatedTime(new Timestamp(new Date().getTime()));
		siServiceSla.setIsActive(scServiceSla.getIsActive());
		siServiceSla.setSlaComponent(scServiceSla.getSlaComponent());
		siServiceSla.setSlaValue(scServiceSla.getSlaValue());
		siServiceSla.setUpdatedBy(scServiceSla.getUpdatedBy());
		siServiceSla.setUpdatedTime(new Timestamp(new Date().getTime()));
		return siServiceSla;
	}

	private SIServiceAttribute mapScServiceAttrEntityToBean(ScServiceAttributeBean scServiceAttribute) {
		SIServiceAttribute siServiceAttribute = new SIServiceAttribute();
		siServiceAttribute.setAttributeAltValueLabel(scServiceAttribute.getAttributeAltValueLabel());
		siServiceAttribute.setAttributeName(scServiceAttribute.getAttributeName());
		siServiceAttribute.setAttributeValue(scServiceAttribute.getAttributeValue());
		siServiceAttribute.setCategory(scServiceAttribute.getCategory());
		siServiceAttribute.setCreatedBy(scServiceAttribute.getCreatedBy());
		siServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siServiceAttribute.setIsActive(scServiceAttribute.getIsActive());
		siServiceAttribute.setUpdatedBy(scServiceAttribute.getUpdatedBy());
		siServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siServiceAttribute;
	}

	public SIAttachment mapScServiceAttachmentToEntity(ScAttachmentBean sAttachmentBean,
			SIServiceDetail siServiceEntity) {
		Attachment attachment = new Attachment();
		attachment.setCategory(sAttachmentBean.getCategory()==null?sAttachmentBean.getType():sAttachmentBean.getCategory());
		attachment.setContentTypeHeader(sAttachmentBean.getContentTypeHeader());
		attachment.setCreatedBy(sAttachmentBean.getCreatedBy());
		attachment.setCreatedDate(sAttachmentBean.getCreatedDate());
		attachment.setIsActive(CommonConstants.Y);
		attachment.setName(sAttachmentBean.getName());
		attachment.setStoragePathUrl(sAttachmentBean.getStoragePathUrl()+ File.separator+sAttachmentBean.getName());
		attachment.setType(sAttachmentBean.getType());
		SIAttachment siAttachment = new SIAttachment();
		siAttachment.setAttachment(attachment);
		siAttachment.setIsActive(CommonConstants.Y);
		siAttachment.setOfferingName(sAttachmentBean.getOfferingName());
		siAttachment.setOrderId(sAttachmentBean.getOrderId());
		siAttachment.setProductName(sAttachmentBean.getProductName());
		siAttachment.setSiteId(sAttachmentBean.getSiteId());
		return siAttachment;
	}

	public SIAsset mapScProductDetailEntityToBean(ScProductDetailBean scProductDetail) {
		SIAsset siAsset = new SIAsset();
		siAsset.setType(scProductDetail.getType());
		siAsset.setName(scProductDetail.getSolutionName());
		siAsset.setIsActive(scProductDetail.getIsActive());
		siAsset.setCreatedBy(scProductDetail.getCreatedBy());
		siAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		siAsset.setUpdatedBy(scProductDetail.getUpdatedBy());
		siAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
		if(Objects.nonNull(scProductDetail.getCloudCode())){
        	siAsset.setCloudCode(scProductDetail.getCloudCode());
		}
        if(Objects.nonNull(scProductDetail.getParentCloudCode())){
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
		for (ScProductDetailAttributesBean scProductDetailAttribute : scProductAttributes) {
			SIAssetAttribute siAssetAttribute = mapScProductDetailAttributeToBean(scProductDetailAttribute);
			siAssetAttribute.setSiAsset(siAsset);
			siAssetAttributes.add(siAssetAttribute);
		}
		return siAssetAttributes;
	}

	private void getScPreviousAssetAttributes(SIAsset siAsset, SIOrder prevSiOrder,
			Set<SIAssetAttribute> siAssetAttributes,List<String> siAttrCategoryList) {
		prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail ->{
			prevSiServiceDetail.getSiAssets().stream()
				.filter(prevSiAsset -> prevSiAsset.getName().equals(siAsset.getName()))
				.forEach(prevSiAsset -> {
					prevSiAsset.getSiAssetAttributes().stream().forEach(prevSiAssetAttr -> {
								if(!siAttrCategoryList.contains(prevSiAssetAttr.getCategory())){
									prevSiAssetAttr.setSiAsset(prevSiAsset);
								}else if(prevSiAssetAttr.getCategory().equals("Additional Ip") && siAttrCategoryList.contains(prevSiAssetAttr.getCategory())){
									getUpdatedAdditionalIpDetails(siAssetAttributes,prevSiAssetAttr);
								}
								prevSiAssetAttr.setSiAsset(prevSiAsset);
								siAssetAttributes.add(prevSiAssetAttr);
							});
				});
		});
	}

	private void getUpdatedAdditionalIpDetails(Set<SIAssetAttribute> siAssetAttributes,
			SIAssetAttribute prevSiAssetAttr) {
		siAssetAttributes.stream()
		.filter(siAssetAttr -> siAssetAttr.getCategory().equals("Additional Ip"))
			.forEach(siAssetAttr ->{
				prevSiAssetAttr.setAttributeValue(String.valueOf(Integer.valueOf(prevSiAssetAttr.getAttributeValue())+
						Integer.valueOf(siAssetAttr.getAttributeValue())));
			});
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
		siAssetCommercial.setPpuRate(scProductDetail.getPpuRate());
		siAssetCommercial.setNrc(scProductDetail.getNrc());
		siAssetCommercial.setArc(scProductDetail.getArc());
		siAssetCommercial.setSIAsset(siAsset);
		return siAssetCommercial;
	}

	private void getUpdatedAssetCommercial(SIAsset siAsset, SIOrder prevSiOrder, ScProductDetailBean scProductDetail,
			SIAssetCommercial siAssetCommercial) {
		prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail ->{
			prevSiServiceDetail.getSiAssets().stream()
				.filter(prevSiAsset -> prevSiAsset.getName().equals(siAsset.getName()))
				.forEach(prevSiAsset -> {
					prevSiAsset.getSiAssetCommercials().stream().forEach(prevSiAssetCommercial ->{
						siAssetCommercial.setMrc(scProductDetail.getMrc()+prevSiAssetCommercial.getMrc());
						siAssetCommercial.setNrc(scProductDetail.getNrc()+prevSiAssetCommercial.getNrc());
						siAssetCommercial.setArc(scProductDetail.getArc()+prevSiAssetCommercial.getArc());
					});
				});
		});
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

	private void getUpdatedAssetComponent(SIOrder prevSiOrder, SIAssetComponent siAssetComponent,ScServiceCommercialBean scServiceCommercial) {
		prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail ->{
			prevSiServiceDetail.getSiAssets().stream()
				.filter(prevSiAsset -> prevSiAsset.getName().equals("IPC addon"))
				.forEach(prevSiAsset -> {
					prevSiAsset.getSiAssetCommercials().stream().forEach(prevSiAssetCommercial ->{
						prevSiAssetCommercial.getSiAssetComponents().stream()
							.filter(prevSiAssetComponent -> prevSiAssetComponent.getItem().equals(scServiceCommercial.getItem()))
								.forEach(prevSiAssetComponent ->  {
									siAssetComponent.setArc(prevSiAssetComponent.getArc()+scServiceCommercial.getArc());
									siAssetComponent.setMrc(prevSiAssetComponent.getMrc()+scServiceCommercial.getMrc());
									siAssetComponent.setNrc(prevSiAssetComponent.getNrc()+scServiceCommercial.getNrc());
								});
					});
				});
		});
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
		siAsset.setIsActive(prevSiAsset.getIsActive());
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
		List<String> siAttrCategoryList= new ArrayList<>();
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
		siAssetCommercial.setPpuRate(prevSiAssetCommercial.getPpuRate());
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

}
