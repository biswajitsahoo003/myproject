package com.tcl.dias.batch.odr.gde;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAdditionalServiceParamBean;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrCommercialBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrContractInfoBean;
import com.tcl.dias.common.fulfillment.beans.OdrGstAddressBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceCommercial;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceSlaBean;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrGstAddress;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
import com.tcl.dias.oms.entity.entities.OdrProductDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercialComponent;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentRepository;

/**
 * 
 * This file is used to map all the odr details to odr bean
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OdrGdeMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(OdrGdeMapper.class);
	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;
	
	@Autowired
	OdrComponentRepository odrComponentRepository;
	
	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;
	

	/**
	 * mapEntityToBean
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
	public OdrOrderBean mapOrderEntityToBean(OdrOrder odrOrder, List<OdrContractInfo> odrContractInfoEntity,
			List<OdrOrderAttribute> odrOrderAttributeEntity, List<OdrServiceDetail> odrServiceDetailEntity,
			List<com.tcl.dias.oms.entity.entities.OdrServiceCommercial> odrServiceCommercials,
			List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes) {
		OdrOrderBean odrOrderBean = new OdrOrderBean();
		odrOrderBean.setCreatedBy(odrOrder.getCreatedBy());
		odrOrderBean.setCreatedDate(odrOrder.getCreatedDate());
		odrOrderBean.setCustomerGroupName(odrOrder.getCustomerGroupName());
		odrOrderBean.setCustomerSegment(odrOrder.getCustomerSegment());
		odrOrderBean.setDemoFlag(odrOrder.getDemoFlag());
		odrOrderBean.setErfCustCustomerId(odrOrder.getErfCustCustomerId());
		odrOrderBean.setErfCustCustomerName(odrOrder.getErfCustCustomerName());
		odrOrderBean.setErfCustLeId(odrOrder.getErfCustLeId());
		odrOrderBean.setErfCustLeName(odrOrder.getErfCustLeName());
		odrOrderBean.setErfCustPartnerId(odrOrder.getErfCustPartnerId());
		odrOrderBean.setErfCustPartnerName(odrOrder.getErfCustPartnerName());
		odrOrderBean.setErfCustSpLeId(odrOrder.getErfCustSpLeId());
		odrOrderBean.setErfCustSpLeName(odrOrder.getErfCustSpLeName());
		odrOrderBean.setErfUserCustomerUserId(odrOrder.getErfUserCustomerUserId());
		odrOrderBean.setErfUserInitiatorId(odrOrder.getErfUserInitiatorId());
		odrOrderBean.setId(odrOrder.getId());
		odrOrderBean.setUuid(odrOrder.getUuid());
		odrOrderBean.setUpdatedDate(odrOrder.getUpdatedDate());
		odrOrderBean.setUpdatedBy(odrOrder.getUpdatedBy());
		odrOrderBean.setTpsSfdcCuid(odrOrder.getTpsSfdcCuid());
		odrOrderBean.setTpsSecsId(odrOrder.getTpsSecsId());
		odrOrderBean.setTpsSapCrnId(odrOrder.getTpsSapCrnId());
		odrOrderBean.setTpsCrmSystem(odrOrder.getTpsCrmSystem());
		odrOrderBean.setTpsCrmOptyId(odrOrder.getTpsCrmOptyId());
		odrOrderBean.setTpsCrmCofId(odrOrder.getTpsCrmCofId());
		odrOrderBean.setSfdcAccountId(odrOrder.getSfdcAccountId());
		odrOrderBean.setParentOpOrderCode(odrOrder.getParentOpOrderCode());
		odrOrderBean.setParentId(odrOrder.getParentId());
		odrOrderBean.setOrderType(odrOrder.getOrderType());
		odrOrderBean.setOrderStatus(odrOrder.getOrderStatus());
		odrOrderBean.setOrderStartDate(odrOrder.getOrderStartDate());
		odrOrderBean.setOrderSource(odrOrder.getOrderSource());
		odrOrderBean.setOrderEndDate(odrOrder.getOrderEndDate());
		odrOrderBean.setOrderCategory(odrOrder.getOrderCategory());
		odrOrderBean.setErfOrderId(odrOrder.getErfOrderId());
		odrOrderBean.setSfdcOptyId(odrOrder.getTpsSfdcOptyId());
		odrOrderBean.setErfOrderLeId(odrOrder.getErfOrderLeId());
//		odrOrderBean.setOrderSubCategory(odrOrder.getOrderSubCategory());
		Set<OdrServiceDetailBean> odrServiceDetailBeans = new HashSet<>();
		for (OdrServiceDetail odrServiceDetail : odrServiceDetailEntity) {
			odrServiceDetailBeans.add(mapServiceEntityToBean(odrServiceDetail));
		}

		Set<OdrContractInfoBean> odrContractInfoBean = new HashSet<>();
		odrContractInfoEntity.stream().forEach(odrContractInfo->{
			odrContractInfoBean.add(mapContractingInfoEntityToBean(odrContractInfo));
		});
		odrOrderBean.setOdrContractInfos(odrContractInfoBean);
		Set<OdrOrderAttributeBean> odrOrderAttributeBeans = new HashSet<>();
		for (OdrOrderAttribute odrOrderAttribute : odrOrderAttributeEntity) {
			odrOrderAttributeBeans.add(mapOrderAttrEntityToBean(odrOrderAttribute));
		}
		odrOrderBean.setOdrOrderAttributes(odrOrderAttributeBeans);
		odrOrderBean.setOdrServiceDetails(odrServiceDetailBeans);
		odrOrderBean.setOpOrderCode(odrOrder.getOpOrderCode());
		odrOrderBean.setOpportunityClassification(odrOrder.getOpportunityClassification());
		odrOrderBean.setIsActive(odrOrder.getIsActive());
		odrOrderBean.setIsBundleOrder(odrOrder.getIsBundleOrder());
		odrOrderBean.setIsMultipleLe(odrOrder.getIsMultipleLe());
		odrOrderBean.setLastMacdDate(odrOrder.getLastMacdDate());
		odrOrderBean.setMacdCreatedDate(odrOrder.getMacdCreatedDate());
		if(odrOrder.getOdrGstAddress()!=null){
			OdrGstAddressBean odrGstAddressBean = new OdrGstAddressBean();
			OdrGstAddress odrGst = odrOrder.getOdrGstAddress();
			odrGstAddressBean.setBuildingName(odrGst.getBuildingName());
			odrGstAddressBean.setBuildingNumber(odrGst.getBuildingNumber());
			odrGstAddressBean.setCreatedBy(odrGst.getCreatedBy());
			odrGstAddressBean.setCreatedTime(odrGst.getCreatedTime());
			odrGstAddressBean.setDistrict(odrGst.getDistrict());
			odrGstAddressBean.setFlatNumber(odrGst.getFlatNumber());
			odrGstAddressBean.setId(odrGst.getId());
			odrGstAddressBean.setLatitude(odrGst.getLatitude());
			odrGstAddressBean.setLocality(odrGst.getLocality());
			odrGstAddressBean.setLongitude(odrGst.getLongitude());
			odrGstAddressBean.setPincode(odrGst.getPincode());
			odrGstAddressBean.setState(odrGst.getState());
			odrGstAddressBean.setStreet(odrGst.getStreet());
			odrOrderBean.setOdrGstAddressBean(odrGstAddressBean);
		}
		Set<OdrCommercialBean> commercialBeans = new HashSet<>();
		odrServiceCommercials.stream().forEach(odrServiceCommercial->{
			commercialBeans.add(mapOdrServiceCommercialToBean(odrServiceCommercial));
		});
		odrOrderBean.setOdrCommercialBean(commercialBeans);
		
		return odrOrderBean;
	}

	/**
	 * mapOdrServiceCommercialToBean
	 */
	private OdrCommercialBean mapOdrServiceCommercialToBean(
			com.tcl.dias.oms.entity.entities.OdrServiceCommercial odrServiceCommercial) {
		OdrCommercialBean odrCommercialBean = new OdrCommercialBean();
		odrCommercialBean.setArc(odrServiceCommercial.getArc());
		odrCommercialBean.setComponentReferenceName(odrServiceCommercial.getComponentReferenceName());
		odrCommercialBean.setMrc(odrServiceCommercial.getMrc());
		odrCommercialBean.setNrc(odrServiceCommercial.getNrc());
		odrCommercialBean.setId(odrServiceCommercial.getId());
		odrCommercialBean.setReferenceName(odrServiceCommercial.getReferenceName());
		odrCommercialBean.setReferenceType(odrServiceCommercial.getReferenceType());
		odrCommercialBean.setServiceId(odrServiceCommercial.getOdrServiceDetail().getId());
		odrCommercialBean.setServiceType(odrServiceCommercial.getServiceType());
		return odrCommercialBean;
	}

	public com.tcl.dias.common.fulfillment.beans.OdrProductDetail mapProductDetailEntityToBean(
			com.tcl.dias.oms.entity.entities.OdrProductDetail odrProductDetail) {
		com.tcl.dias.common.fulfillment.beans.OdrProductDetail scProductDetail = new com.tcl.dias.common.fulfillment.beans.OdrProductDetail();
		scProductDetail.setType(odrProductDetail.getType());
		scProductDetail.setSolutionName(odrProductDetail.getSolutionName());
		scProductDetail.setMrc(odrProductDetail.getMrc());
		scProductDetail.setNrc(odrProductDetail.getNrc());
		scProductDetail.setArc(odrProductDetail.getArc());
		scProductDetail.setIsActive(odrProductDetail.getIsActive());
		scProductDetail.setCreatedBy(odrProductDetail.getCreatedBy());
		scProductDetail.setCreatedDate(odrProductDetail.getCreatedDate());
		if (Objects.nonNull(odrProductDetail.getCloudCode())) {
			scProductDetail.setCloudCode(odrProductDetail.getCloudCode());
		}
		if (Objects.nonNull(odrProductDetail.getParentCloudCode())) {
			scProductDetail.setParentCloudCode(odrProductDetail.getParentCloudCode());
		}
		return scProductDetail;
	}

	public com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes mapProductDetailAttributeToBean(
			com.tcl.dias.oms.entity.entities.OdrProductDetailAttributes odrProductDetailAttributes) {
		com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes scProductDetailAttributes = new com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes();
		scProductDetailAttributes.setCategory(odrProductDetailAttributes.getCategory());
		scProductDetailAttributes.setAttributeName(odrProductDetailAttributes.getAttributeName());
		scProductDetailAttributes.setAttributeValue(odrProductDetailAttributes.getAttributeValue());
		scProductDetailAttributes.setIsActive(odrProductDetailAttributes.getIsActive());
		return scProductDetailAttributes;
	}

	public com.tcl.dias.common.fulfillment.beans.OdrServiceCommercial mapServiceCommercialComponentToBean(
			com.tcl.dias.oms.entity.entities.OdrServiceCommercialComponent odrServiceCommercialComponent) {
		com.tcl.dias.common.fulfillment.beans.OdrServiceCommercial scServiceCommericalComponent = new com.tcl.dias.common.fulfillment.beans.OdrServiceCommercial();
		scServiceCommericalComponent.setArc(odrServiceCommercialComponent.getArc());
		scServiceCommericalComponent.setCreatedBy(odrServiceCommercialComponent.getCreatedBy());
		scServiceCommericalComponent.setCreatedDate(odrServiceCommercialComponent.getCreatedDate());
		scServiceCommericalComponent.setItem(odrServiceCommercialComponent.getItem());
		scServiceCommericalComponent.setItemType(odrServiceCommercialComponent.getItemType());
		scServiceCommericalComponent.setMrc(odrServiceCommercialComponent.getMrc());
		scServiceCommericalComponent.setNrc(odrServiceCommercialComponent.getNrc());
		scServiceCommericalComponent.setParentItem(odrServiceCommercialComponent.getParentItem());
		return scServiceCommericalComponent;
	}

	public OdrServiceDetailBean mapServiceEntityToBean(OdrServiceDetail odrServiceDetail) {
		OdrServiceDetailBean odrServiceDetailBean = new OdrServiceDetailBean();
		odrServiceDetailBean.setAccessType(odrServiceDetail.getAccessType());
		odrServiceDetailBean.setArc(odrServiceDetail.getArc());
		odrServiceDetailBean.setBillingAccountId(odrServiceDetail.getBillingAccountId());
		odrServiceDetailBean.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
		odrServiceDetailBean.setBillingRatioPercent(odrServiceDetail.getBillingRatioPercent());
		odrServiceDetailBean.setBillingType(odrServiceDetail.getBillingType());
		odrServiceDetailBean.setBurstableBwPortspeed(odrServiceDetail.getBurstableBwPortspeed());
		odrServiceDetailBean.setBurstableBwPortspeedAltName(odrServiceDetail.getBurstableBwPortspeedAltName());
		odrServiceDetailBean.setBurstableBwUnit(odrServiceDetail.getBurstableBwUnit());
		odrServiceDetailBean.setBwPortspeed(odrServiceDetail.getBwPortspeed());
		odrServiceDetailBean.setBwUnit(odrServiceDetail.getBwUnit());
		odrServiceDetailBean.setBwPortspeedAltName(odrServiceDetail.getBwPortspeedAltName());
		odrServiceDetailBean.setParentServiceUuid(odrServiceDetail.getParentUuid());
		odrServiceDetailBean.setCallType(odrServiceDetail.getCallType());
		odrServiceDetailBean.setCreatedBy(odrServiceDetail.getCreatedBy());
		odrServiceDetailBean.setCreatedDate(odrServiceDetail.getCreatedDate());
		odrServiceDetailBean.setCustOrgNo(odrServiceDetail.getCustOrgNo());
		odrServiceDetailBean.setDemarcationApartment(odrServiceDetail.getDemarcationApartment());
		odrServiceDetailBean.setDemarcationFloor(odrServiceDetail.getDemarcationFloor());
		odrServiceDetailBean.setDemarcationRack(odrServiceDetail.getDemarcationRack());
		odrServiceDetailBean.setDemarcationRoom(odrServiceDetail.getDemarcationRoom());
		odrServiceDetailBean.setDestinationCity(odrServiceDetail.getDestinationCity());
		odrServiceDetailBean.setDestinationState(odrServiceDetail.getDestinationState());
		odrServiceDetailBean.setSourceState(odrServiceDetail.getSourceState());
		odrServiceDetailBean.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		odrServiceDetailBean.setDestinationCountryCode(odrServiceDetail.getDestinationCountryCode());
		odrServiceDetailBean.setDestinationCountryCodeRepc(odrServiceDetail.getDestinationCountryCodeRepc());
		odrServiceDetailBean.setDiscountArc(odrServiceDetail.getDiscountArc());
		odrServiceDetailBean.setDiscountMrc(odrServiceDetail.getDiscountMrc());
		odrServiceDetailBean.setDiscountNrc(odrServiceDetail.getDiscountNrc());
		odrServiceDetailBean.setErfLocDestinationCityId(odrServiceDetail.getErfLocDestinationCityId());
		odrServiceDetailBean.setErfLocDestinationCountryId(odrServiceDetail.getErfLocDestinationCountryId());
		odrServiceDetailBean.setErfLocPopSiteAddressId(odrServiceDetail.getErfLocPopSiteAddressId());
		odrServiceDetailBean.setErfLocSiteAddressId(odrServiceDetail.getErfLocSiteAddressId());
		odrServiceDetailBean.setErfLocSourceCityId(odrServiceDetail.getErfLocSourceCityId());
		odrServiceDetailBean.setErfLocSrcCountryId(odrServiceDetail.getErfLocSrcCountryId());
		odrServiceDetailBean.setErfPrdCatalogOfferingId(odrServiceDetail.getErfPrdCatalogOfferingId());
		odrServiceDetailBean.setErfPrdCatalogOfferingName(odrServiceDetail.getErfPrdCatalogOfferingName());
		odrServiceDetailBean.setErfPrdCatalogParentProductOfferingName(
				odrServiceDetail.getErfPrdCatalogParentProductOfferingName());
		odrServiceDetailBean.setErfPrdCatalogProductId(odrServiceDetail.getErfPrdCatalogProductId());
		odrServiceDetailBean.setErfPrdCatalogProductName(odrServiceDetail.getErfPrdCatalogProductName());
		odrServiceDetailBean.setFeasibilityId(odrServiceDetail.getFeasibilityId());
		odrServiceDetailBean.setGscOrderSequenceId(odrServiceDetail.getGscOrderSequenceId());
		odrServiceDetailBean.setId(odrServiceDetail.getId());
		odrServiceDetailBean.setIsActive(odrServiceDetail.getIsActive());
		odrServiceDetailBean.setIsIzo(odrServiceDetail.getIsIzo());
		odrServiceDetailBean.setLastmileBw(odrServiceDetail.getLastmileBw());
		odrServiceDetailBean.setLastmileBwAltName(odrServiceDetail.getLastmileBwAltName());
		odrServiceDetailBean.setLastmileBwUnit(odrServiceDetail.getLastmileBwUnit());
		odrServiceDetailBean.setLastmileProvider(odrServiceDetail.getLastmileProvider());
		odrServiceDetailBean.setLastmileType(odrServiceDetail.getLastmileType());
		odrServiceDetailBean.setLatLong(odrServiceDetail.getLatLong());
		odrServiceDetailBean.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
		odrServiceDetailBean.setLocalItContactMobile(odrServiceDetail.getLocalItContactMobile());
		odrServiceDetailBean.setLocalItContactName(odrServiceDetail.getLocalItContactName());
		odrServiceDetailBean.setMrc(odrServiceDetail.getMrc());
		odrServiceDetailBean.setNrc(odrServiceDetail.getNrc());
		odrServiceDetailBean.setDifferentialMrc(odrServiceDetail.getDifferentialMrc());
		odrServiceDetailBean.setDifferentialNrc(odrServiceDetail.getDifferentialNrc());
		if (odrServiceDetail.getOdrGstAddress() != null) {
			OdrGstAddressBean odrGstAddressBean = new OdrGstAddressBean();
			OdrGstAddress odrGst = odrServiceDetail.getOdrGstAddress();
			odrGstAddressBean.setBuildingName(odrGst.getBuildingName());
			odrGstAddressBean.setBuildingNumber(odrGst.getBuildingNumber());
			odrGstAddressBean.setCreatedBy(odrGst.getCreatedBy());
			odrGstAddressBean.setCreatedTime(odrGst.getCreatedTime());
			odrGstAddressBean.setDistrict(odrGst.getDistrict());
			odrGstAddressBean.setFlatNumber(odrGst.getFlatNumber());
			odrGstAddressBean.setId(odrGst.getId());
			odrGstAddressBean.setLatitude(odrGst.getLatitude());
			odrGstAddressBean.setLocality(odrGst.getLocality());
			odrGstAddressBean.setLongitude(odrGst.getLongitude());
			odrGstAddressBean.setPincode(odrGst.getPincode());
			odrGstAddressBean.setState(odrGst.getState());
			odrGstAddressBean.setStreet(odrGst.getStreet());
			odrServiceDetailBean.setOdrGstAddress(odrGstAddressBean);
		}
		odrServiceDetailBean.setOdrContractInfo(mapContractingInfoEntityToBean(odrServiceDetail.getOdrContractInfo()));
		// odrServiceDetailBean.setOdrContractInfo(odrContractInfo);
		// odrServiceDetailBean.setOdrOrder(odrServiceDetail.geto);
		odrServiceDetailBean.setOdrOrderUuid(odrServiceDetail.getOdrOrderUuid());
		Set<OdrServiceAttributeBean> odrServiceAttrBeans = new HashSet<>();
		for (OdrServiceAttribute odrServiceAttribute : odrServiceDetail.getOdrServiceAttributes()) {
			odrServiceAttrBeans.add(mapServiceAttrEntityToBean(odrServiceAttribute));
		}
		odrServiceDetailBean.setOdrServiceAttributes(odrServiceAttrBeans);
		List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
		// LOGGER.info("COMMERCIAL COMP
		// SIZE:::::::::::::::::::::::::::"+odrServiceDetail.getOdrServiceCommercialComponent().size());
		// LOGGER.info("MRC:::::::::::::::::::::::::::"+odrServiceDetail.getMrc());
		// LOGGER.info("NRC:::::::::::::::::::::::::::"+odrServiceDetail.getNrc());
		/*
		 * for (OdrServiceCommercialComponent odrServiceCommercialComponent :
		 * odrServiceDetail.getOdrServiceCommercialComponent()) {
		 * odrServiceCommercials.add(mapOdrServiceCommercialsBean(
		 * odrServiceCommercialComponent)); }
		 * odrServiceDetailBean.setOdrServiceCommercials(odrServiceCommercials);
		 */

		List<com.tcl.dias.common.fulfillment.beans.OdrProductDetail> odrProductDetails = new ArrayList<>();
		for (OdrProductDetail odrProductDetail : odrServiceDetail.getOdrProductDetail()) {
			com.tcl.dias.common.fulfillment.beans.OdrProductDetail odrProductDetailBean = mapProductDetailEntityToBean(
					odrProductDetail);
			List<com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes> odrProductDetailAttributesList = new ArrayList<>();
			List<OdrServiceCommercial> odrServiceCommercialList = new ArrayList<>();
			for (com.tcl.dias.oms.entity.entities.OdrProductDetailAttributes odrProductDetailAttributes : odrProductDetail
					.getOdrProductDetailAttributes()) {
				odrProductDetailAttributesList.add(mapProductDetailAttributeToBean(odrProductDetailAttributes));
			}
			odrProductDetailBean.setOdrProductAttributes(odrProductDetailAttributesList);
			for (OdrServiceCommercialComponent odrServiceCommercial : odrProductDetail
					.getOdrServiceCommercialComponent()) {
				odrServiceCommercialList.add(mapServiceCommercialComponentToBean(odrServiceCommercial));
			}
			odrProductDetailBean.setOdrServiceCommercials(odrServiceCommercialList);
			odrProductDetails.add(odrProductDetailBean);
		}
		odrServiceDetailBean.setOdrProductDetail(odrProductDetails);
		Set<OdrServiceSlaBean> odrServiceSlaBeans = new HashSet<>();
//		odrServiceDetail.getOdrServiceSlas().stream().forEach(odrServiceSla->{
//			odrServiceSlaBeans.add(mapServiceSlaEntityToBean(odrServiceSla));
//		});
		
		LOGGER.info("Getting attachment details for id {}", odrServiceDetail.getId());
		List<OdrAttachment> odrAttachments = odrAttachmentRepository.findByOdrServiceDetailId(odrServiceDetail.getId());
		List<OdrAttachmentBean> odrAttachmentBeans = new ArrayList<OdrAttachmentBean>();
		odrAttachments.stream().forEach(odrAttachment->{
			LOGGER.info("Getting odr attachment details for id {}", odrAttachment.getAttachmentId());
			Optional<Attachment> attachmentEntity = attachmentRepository.findById(odrAttachment.getAttachmentId());
			if (attachmentEntity.isPresent()) {
				OdrAttachmentBean odrAttachmentBean = new OdrAttachmentBean();
				odrAttachmentBean.setAttachmentId(odrAttachment.getAttachmentId());
				odrAttachmentBean.setCategory(attachmentEntity.get().getCategory());
				odrAttachmentBean.setContentTypeHeader(attachmentEntity.get().getContentTypeHeader());
				odrAttachmentBean.setCreatedBy(attachmentEntity.get().getCreatedBy());
				odrAttachmentBean.setCreatedDate(attachmentEntity.get().getCreatedDate());
				odrAttachmentBean.setName(attachmentEntity.get().getName());
				odrAttachmentBean.setOfferingName(odrAttachment.getOfferingName());
				odrAttachmentBean.setOrderId(odrAttachment.getOrderId());
				odrAttachmentBean.setProductName(odrAttachment.getProductName());
				// odrAttachmentBean.setServiceCode(odrAttachment.getServiceCode());
				odrAttachmentBean.setErfOdrServiceId(odrAttachment.getOdrServiceDetail().getId());
				odrAttachmentBean.setServiceId(odrAttachment.getServiceId());
				odrAttachmentBean.setSiteId(odrAttachment.getSiteId());
				odrAttachmentBean.setStoragePathUrl(attachmentEntity.get().getStoragePathUrl());
				odrAttachmentBean.setType(attachmentEntity.get().getType());
				odrAttachmentBeans.add(odrAttachmentBean);
			}
		});
		odrServiceDetailBean.setOdrAttachments(odrAttachmentBeans);
		odrServiceDetailBean.setOdrServiceSlas(odrServiceSlaBeans);
		odrServiceDetailBean.setParentBundleServiceId(odrServiceDetail.getParentBundleServiceId());
		odrServiceDetailBean.setParentId(odrServiceDetail.getParentId());
		odrServiceDetailBean.setPopSiteAddress(odrServiceDetail.getPopSiteAddress());
		odrServiceDetailBean.setPopSiteCode(odrServiceDetail.getPopSiteCode());
		odrServiceDetailBean.setPrimarySecondary(odrServiceDetail.getPrimarySecondary());
		// odrServiceDetailBean.setPriSecServiceLink(odrServiceDetail.getPriSecServiceLink());
		odrServiceDetailBean.setErfPriSecServiceLinkId(odrServiceDetail.getErfPriSecServiceLinkId());
		odrServiceDetailBean.setProductReferenceId(odrServiceDetail.getProductReferenceId());
		odrServiceDetailBean.setServiceClass(odrServiceDetail.getServiceClass());
		odrServiceDetailBean.setServiceClassification(odrServiceDetail.getServiceClassification());
		odrServiceDetailBean.setServiceCommissionedDate(odrServiceDetail.getServiceCommissionedDate());
		odrServiceDetailBean.setServiceGroupId(odrServiceDetail.getServiceGroupId());
		odrServiceDetailBean.setServiceGroupType(odrServiceDetail.getServiceGroupType());
		odrServiceDetailBean.setServiceOption(odrServiceDetail.getServiceOption());
		odrServiceDetailBean.setServiceStatus(odrServiceDetail.getServiceStatus());
		odrServiceDetailBean.setServiceTerminationDate(odrServiceDetail.getServiceTerminationDate());
		odrServiceDetailBean.setServiceTopology(odrServiceDetail.getServiceTopology());
		odrServiceDetailBean.setSiteAddress(odrServiceDetail.getSiteAddress());
		odrServiceDetailBean.setSiteAlias(odrServiceDetail.getSiteAlias());
		odrServiceDetailBean.setSiteEndInterface(odrServiceDetail.getSiteEndInterface());
		odrServiceDetailBean.setSiteLatLang(odrServiceDetail.getSiteLatLang());
		odrServiceDetailBean.setSiteLinkLabel(odrServiceDetail.getSiteLinkLabel());
		odrServiceDetailBean.setSiteTopology(odrServiceDetail.getSiteTopology());
		odrServiceDetailBean.setSiteType(odrServiceDetail.getSiteType());
		odrServiceDetailBean.setSlaTemplate(odrServiceDetail.getSlaTemplate());
		odrServiceDetailBean.setSmEmail(odrServiceDetail.getSmEmail());
		odrServiceDetailBean.setSourceCity(odrServiceDetail.getSourceCity());
		odrServiceDetailBean.setSourceCountry(odrServiceDetail.getSourceCountry());
		odrServiceDetailBean.setSourceCountryCode(odrServiceDetail.getSourceCountryCode());
		odrServiceDetailBean.setSourceCountryCodeRepc(odrServiceDetail.getSourceCountryCodeRepc());
		odrServiceDetailBean.setSupplOrgNo(odrServiceDetail.getSupplOrgNo());
		odrServiceDetailBean.setTaxExemptionFlag(odrServiceDetail.getTaxExemptionFlag());
		odrServiceDetailBean.setTpsCopfId(odrServiceDetail.getTpsCopfId());
		odrServiceDetailBean.setTpsServiceId(odrServiceDetail.getTpsServiceId());
		odrServiceDetailBean.setTpsSourceServiceId(odrServiceDetail.getTpsSourceServiceId());
		odrServiceDetailBean.setUpdatedBy(odrServiceDetail.getUpdatedBy());
		odrServiceDetailBean.setUpdatedDate(odrServiceDetail.getUpdatedDate());
		odrServiceDetailBean.setUuid(odrServiceDetail.getUuid());

		odrServiceDetailBean.setErfOdrServiceId(odrServiceDetail.getId());
		odrServiceDetailBean.setVpnName(odrServiceDetail.getVpnName());
		odrServiceDetailBean.setDestinationAddressLineOne(odrServiceDetail.getDestinationAddressLineOne());
		odrServiceDetailBean.setDestinationAddressLineTwo(odrServiceDetail.getDestinationAddressLineTwo());
		odrServiceDetailBean.setDestinationCity(odrServiceDetail.getDestinationCity());
		odrServiceDetailBean.setDestinationCountry(odrServiceDetail.getDestinationCountry());
		odrServiceDetailBean.setDestinationLocality(odrServiceDetail.getDestinationLocality());
		odrServiceDetailBean.setDestinationPincode(odrServiceDetail.getDestinationPincode());
		odrServiceDetailBean.setSourceAddressLineOne(odrServiceDetail.getSourceAddressLineOne());
		odrServiceDetailBean.setSourceAddressLineTwo(odrServiceDetail.getSourceAddressLineTwo());
		odrServiceDetailBean.setSourceCity(odrServiceDetail.getSourceCity());
		odrServiceDetailBean.setSourceCountry(odrServiceDetail.getSourceCountry());
		odrServiceDetailBean.setSourceLocality(odrServiceDetail.getSourceLocality());
		odrServiceDetailBean.setSourcePincode(odrServiceDetail.getSourcePincode());
		/*
		 * for (OdrServiceCommercialComponent odrServiceCommercialComponent :
		 * odrServiceDetail .getOdrServiceCommercialComponent()) {
		 * odrServiceDetailBean.getOdrServiceCommercials()
		 * .add(mapServiceCommercialToBean(odrServiceCommercialComponent)); }
		 */
		
		List<OdrComponent> odrComponentsList = odrComponentRepository.findByOdrServiceDetail(odrServiceDetail);
		if(odrComponentsList!=null && !odrComponentsList.isEmpty()) {
			Set<OdrComponentBean> odrComponentBeans = new HashSet<>();
			odrComponentsList.stream().forEach(comp->{
				odrComponentBeans.add(mapOdrComponentEntityToBean(comp));
			});
			odrServiceDetailBean.setOdrComponentBeans(odrComponentBeans);
		}
		
		return odrServiceDetailBean;
	}

	public OdrServiceCommercial mapServiceCommercialToBean(
			OdrServiceCommercialComponent odrServiceCommercialComponent) {
		OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
		if (odrServiceCommercialComponent != null) {
			odrServiceCommercial.setArc(odrServiceCommercialComponent.getArc());
			odrServiceCommercial.setCreatedBy(odrServiceCommercialComponent.getCreatedBy());
			odrServiceCommercial.setCreatedDate(odrServiceCommercialComponent.getCreatedDate());
			odrServiceCommercial.setId(odrServiceCommercialComponent.getId());
			odrServiceCommercial.setItem(odrServiceCommercialComponent.getItem());
			odrServiceCommercial.setItemType(odrServiceCommercialComponent.getItemType());
			odrServiceCommercial.setMrc(odrServiceCommercialComponent.getMrc());
			odrServiceCommercial.setNrc(odrServiceCommercialComponent.getNrc());
			odrServiceCommercial.setParentItem(odrServiceCommercialComponent.getParentItem());
		}
		return odrServiceCommercial;
	}

	/**
	 * mapServiceAttrEntityToBean
	 */
	public OdrServiceAttributeBean mapServiceAttrEntityToBean(OdrServiceAttribute odrServiceAttribute) {
		OdrServiceAttributeBean odrServiceAttributeBean = new OdrServiceAttributeBean();
		odrServiceAttributeBean.setAttributeAltValueLabel(odrServiceAttribute.getAttributeAltValueLabel());
		odrServiceAttributeBean.setAttributeName(odrServiceAttribute.getAttributeName());
		odrServiceAttributeBean.setAttributeValue(odrServiceAttribute.getAttributeValue());
		odrServiceAttributeBean.setCategory(odrServiceAttribute.getCategory());
		odrServiceAttributeBean.setCreatedBy(odrServiceAttribute.getCreatedBy());
		odrServiceAttributeBean.setCreatedDate(odrServiceAttribute.getCreatedDate());
		odrServiceAttributeBean.setId(odrServiceAttribute.getId());
		odrServiceAttributeBean.setCreatedDate(odrServiceAttribute.getCreatedDate());
		odrServiceAttributeBean.setIsActive(odrServiceAttribute.getIsActive());
		// odrServiceAttributeBean.setOdrServiceDetail(odrServiceDetail);
		odrServiceAttributeBean.setUpdatedBy(odrServiceAttribute.getUpdatedBy());
		odrServiceAttributeBean.setUpdatedDate(odrServiceAttribute.getUpdatedDate());
		if (odrServiceAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
			Optional<OdrAdditionalServiceParam> odrAddiParm = odrAdditionalServiceParamRepository
					.findById(Integer.valueOf(odrServiceAttribute.getAttributeValue()));
			if (odrAddiParm.isPresent()) {
				OdrAdditionalServiceParamBean odrAdditBean = new OdrAdditionalServiceParamBean();
				odrAdditBean.setAttribute(odrAddiParm.get().getAttribute());
				odrAdditBean.setCategory(odrAddiParm.get().getCategory());
				odrAdditBean.setCreatedBy(odrAddiParm.get().getCreatedBy());
				odrAdditBean.setCreatedTime(odrAddiParm.get().getCreatedTime());
				odrAdditBean.setId(odrAddiParm.get().getId());
				odrAdditBean.setIsActive(odrAddiParm.get().getIsActive());
				odrAdditBean.setReferenceId(odrAddiParm.get().getReferenceId());
				odrAdditBean.setReferenceType(odrAddiParm.get().getReferenceType());
				odrAdditBean.setValue(odrAddiParm.get().getValue());
				odrServiceAttributeBean.setOdrAdditionalServiceParam(odrAdditBean);
			}
		}
		odrServiceAttributeBean.setIsAdditionalParam(odrServiceAttribute.getIsAdditionalParam());
		
		// TODO Auto-generated method stub
		return odrServiceAttributeBean;
	}

	public OdrServiceCommercial mapOdrServiceCommercialsBean(
			OdrServiceCommercialComponent odrServiceCommercialComponent) {
		OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
		odrServiceCommercial.setId(odrServiceCommercialComponent.getId());
		odrServiceCommercial.setParentItem(odrServiceCommercialComponent.getParentItem());
		odrServiceCommercial.setItemType(odrServiceCommercialComponent.getItemType());
		odrServiceCommercial.setItem(odrServiceCommercialComponent.getItem());
		odrServiceCommercial.setNrc(odrServiceCommercialComponent.getNrc());
		odrServiceCommercial.setMrc(odrServiceCommercialComponent.getMrc());
		odrServiceCommercial.setArc(odrServiceCommercialComponent.getArc());
		odrServiceCommercial.setCreatedBy(odrServiceCommercialComponent.getCreatedBy());
		odrServiceCommercial.setCreatedDate(odrServiceCommercialComponent.getCreatedDate());
		return odrServiceCommercial;
	}

	public OdrContractInfoBean mapContractingInfoEntityToBean(OdrContractInfo odrContractInfo) {
		OdrContractInfoBean odrContractInfoBean = new OdrContractInfoBean();
		odrContractInfoBean.setAccountManager(odrContractInfo.getAccountManager());
		odrContractInfoBean.setAccountManagerEmail(odrContractInfo.getAccountManagerEmail());
		odrContractInfoBean.setArc(odrContractInfo.getArc());
		odrContractInfoBean.setBillingAddress(odrContractInfo.getBillingAddress());
		odrContractInfoBean.setBillingFrequency(odrContractInfo.getBillingFrequency());
		odrContractInfoBean.setBillingMethod(odrContractInfo.getBillingMethod());
		odrContractInfoBean.setContractEndDate(odrContractInfo.getContractEndDate());
		odrContractInfoBean.setContractStartDate(odrContractInfo.getContractStartDate());
		odrContractInfoBean.setCreatedBy(odrContractInfo.getCreatedBy());
		odrContractInfoBean.setCreatedDate(odrContractInfo.getCreatedDate());
		odrContractInfoBean.setCustomerContact(odrContractInfo.getCustomerContact());
		odrContractInfoBean.setCustomerContactEmail(odrContractInfo.getCustomerContactEmail());
		odrContractInfoBean.setDiscountArc(odrContractInfo.getDiscountArc());
		odrContractInfoBean.setDiscountMrc(odrContractInfo.getDiscountMrc());
		odrContractInfoBean.setDiscountNrc(odrContractInfo.getDiscountNrc());
		odrContractInfoBean.setErfCustCurrencyId(odrContractInfo.getErfCustCurrencyId());
		odrContractInfoBean.setErfCustLeId(odrContractInfo.getErfCustLeId());
		odrContractInfoBean.setErfCustLeName(odrContractInfo.getErfCustLeName());
		odrContractInfoBean.setErfCustSpLeId(odrContractInfo.getErfCustSpLeId());
		odrContractInfoBean.setErfCustSpLeName(odrContractInfo.getErfCustSpLeName());
		odrContractInfoBean.setErfLocBillingLocationId(odrContractInfo.getErfLocBillingLocationId());
		odrContractInfoBean.setId(odrContractInfo.getId());
		odrContractInfoBean.setIsActive(odrContractInfo.getIsActive());
		odrContractInfoBean.setLastMacdDate(odrContractInfo.getLastMacdDate());
		odrContractInfoBean.setMrc(odrContractInfo.getMrc());
		odrContractInfoBean.setNrc(odrContractInfo.getNrc());
		odrContractInfoBean.setBillingAddressLine1(odrContractInfo.getBillingAddressLine1());
		odrContractInfoBean.setBillingAddressLine2(odrContractInfo.getBillingAddressLine2());
		odrContractInfoBean.setBillingAddressLine3(odrContractInfo.getBillingAddressLine3());
		odrContractInfoBean.setBillingCity(odrContractInfo.getBillingCity());
		odrContractInfoBean.setBillingCountry(odrContractInfo.getBillingCountry());
		odrContractInfoBean.setBillingPincode(odrContractInfo.getBillingPincode());
		odrContractInfoBean.setBillingState(odrContractInfo.getBillingState());
		odrContractInfoBean.setPoMandatoryStatus(odrContractInfo.getPoMandatoryStatus());
		// odrContractInfoBean.setOdrOrder(odrContractInfo.getOdrOrder());
		// odrContractInfoBean.setOdrServiceDetails(odrContractInfo.getOdrServiceDetails());
		odrContractInfoBean.setOrderTermInMonths(odrContractInfo.getOrderTermInMonths());
		odrContractInfoBean.setPaymentTerm(odrContractInfo.getPaymentTerm());
		odrContractInfoBean.setTpsSfdcCuid(odrContractInfo.getTpsSfdcCuid());
		odrContractInfoBean.setUpdatedBy(odrContractInfo.getUpdatedBy());
		odrContractInfoBean.setUpdatedDate(odrContractInfo.getUpdatedDate());
		odrContractInfoBean.setBillingContactId(odrContractInfo.getBillingContactId());
		return odrContractInfoBean;
	}

	public OdrOrderAttributeBean mapOrderAttrEntityToBean(OdrOrderAttribute odrOrderAttribute) {
		OdrOrderAttributeBean odrOrderAttributeBean = new OdrOrderAttributeBean();
		odrOrderAttributeBean.setAttributeAltValueLabel(odrOrderAttribute.getAttributeAltValueLabel());
		odrOrderAttributeBean.setAttributeName(odrOrderAttribute.getAttributeName());
		odrOrderAttributeBean.setAttributeValue(odrOrderAttribute.getAttributeValue());
		odrOrderAttributeBean.setCategory(odrOrderAttribute.getCategory());
		odrOrderAttributeBean.setCreatedBy(odrOrderAttribute.getCreatedBy());
		odrOrderAttributeBean.setCreatedDate(odrOrderAttribute.getCreatedDate());
		odrOrderAttributeBean.setId(odrOrderAttribute.getId());
		odrOrderAttributeBean.setCreatedDate(odrOrderAttribute.getCreatedDate());
		odrOrderAttributeBean.setIsActive(odrOrderAttribute.getIsActive());
		// odrOrderAttributeBean.setOdrOrder(odrOrder);
		odrOrderAttributeBean.setUpdatedBy(odrOrderAttribute.getUpdatedBy());
		odrOrderAttributeBean.setUpdatedDate(odrOrderAttribute.getUpdatedDate());
		// TODO Auto-generated method stub

		return odrOrderAttributeBean;
	}

	public OdrServiceSlaBean mapServiceSlaEntityToBean(OdrServiceSla odrServiceSla) {
		OdrServiceSlaBean odrServiceSlaBean = new OdrServiceSlaBean();
		odrServiceSlaBean.setCreatedBy(odrServiceSla.getCreatedBy());
		odrServiceSlaBean.setCreatedTime(odrServiceSla.getCreatedTime());
		odrServiceSlaBean.setId(odrServiceSla.getId());
		odrServiceSlaBean.setIsActive(odrServiceSla.getIsActive());
		// odrServiceSlaBean.setOdrServiceDetail(odrServiceSla.getOdrServiceDetail());
		odrServiceSlaBean.setSlaComponent(odrServiceSla.getSlaComponent());
		odrServiceSlaBean.setSlaValue(odrServiceSla.getSlaValue());
		odrServiceSlaBean.setUpdatedBy(odrServiceSla.getUpdatedBy());
		odrServiceSlaBean.setUpdatedTime(odrServiceSla.getUpdatedTime());
		return odrServiceSlaBean;
	}
	
	public OdrComponentBean mapOdrComponentEntityToBean(OdrComponent odrComponent) {
		OdrComponentBean odrComponentBean = new OdrComponentBean();
		odrComponentBean.setComponentName(odrComponent.getComponentName());
		odrComponentBean.setSiteType(odrComponent.getSiteType());
		Set<OdrComponentAttributeBean> odrComponentAttributesBeans = new HashSet<>();
		List<OdrComponentAttribute> odrComponentAttributes = odrComponentAttributeRepository.findByOdrComponent(odrComponent);
		if(odrComponentAttributes!=null && !odrComponentAttributes.isEmpty()) {
			odrComponentAttributes.stream().forEach(odrComponentAttr->{
				odrComponentAttributesBeans.add(mapOdrComponentAttributeToBen(odrComponentAttr));
			});
			
		}
		odrComponentBean.setOdrComponentAttributeBeans(odrComponentAttributesBeans);
		return odrComponentBean;
	}
	
	public OdrComponentAttributeBean mapOdrComponentAttributeToBen(OdrComponentAttribute odrComponentAttribute) {
		OdrComponentAttributeBean odrComponentAttributeBean = new OdrComponentAttributeBean();
		odrComponentAttributeBean.setId(odrComponentAttribute.getId());
		odrComponentAttributeBean.setName(odrComponentAttribute.getAttributeName());
		odrComponentAttributeBean.setValue(odrComponentAttribute.getAttributeValue());
		return odrComponentAttributeBean;
	}

}
