package com.tcl.dias.batch.odr.mapper;

import com.tcl.dias.common.beans.SolutionComponentBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAdditionalServiceParamBean;
import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrCommercialBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
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
import com.tcl.dias.common.fulfillment.beans.OdrTeamsDRCommercialBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAsset;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrGstAddress;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercialComponent;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.entities.OdrSolutionComponent;
import com.tcl.dias.oms.entity.entities.OdrTeamsDRServiceCommercial;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAssetRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrContractInfoRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceAttributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This file contains the OdrTeamsDRMapper.java class.
 *
 * @author Syed Ali
 *
 */

@Component
public class OdrTeamsDRMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(OdrTeamsDRMapper.class);

	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	OdrServiceAttributeRepository odrServiceAttributeRepository;

	@Autowired
	OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;

	@Autowired
	OdrContractInfoRepository odrContractInfoRepository;

	@Autowired
	OdrAssetRepository odrAssetRepository;

	/**
	 * Method to map entity to bean
	 *
	 * @param odrOrder
	 * @param odrContractInfoEntity
	 * @param odrOrderAttributeEntity
	 * @param odrServiceDetailEntity
	 * @param odrServiceCommercials
	 * @param odrSolutionComponents
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public OdrOrderBean mapOrderEntityToBean(OdrOrder odrOrder, List<OdrContractInfo> odrContractInfoEntity,
			List<OdrOrderAttribute> odrOrderAttributeEntity, List<OdrServiceDetail> odrServiceDetailEntity,
			List<com.tcl.dias.oms.entity.entities.OdrServiceCommercial> odrServiceCommercials,
			List<OdrSolutionComponent> odrSolutionComponents) {

		OdrOrderBean odrOrderBean = new OdrOrderBean();
		LOGGER.info("Contract entity size {}", odrContractInfoEntity.size());
		Set<OdrContractInfoBean> odrContractInfoBean = new HashSet<>();
		for (OdrContractInfo odrContractInfo : odrContractInfoEntity) {
			odrContractInfoBean.add(mapContractingInfoEntityToBean(odrContractInfo));
		}
		odrOrderBean.setOdrContractInfos(odrContractInfoBean);
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
		// odrOrderBean.setOrderSubCategory(odrOrder.getOrderSubCategory());
		odrOrderBean.setErfOrderId(odrOrder.getErfOrderId());
		odrOrderBean.setSfdcOptyId(odrOrder.getTpsSfdcOptyId());
		odrOrderBean.setErfOrderLeId(odrOrder.getErfOrderLeId());
		if (odrOrder.getOdrGstAddress() != null) {
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
		Set<OdrServiceDetailBean> odrServiceDetailBeans = new HashSet<>();
		for (OdrServiceDetail odrServiceDetail : odrServiceDetailEntity) {
			odrServiceDetailBeans.add(mapServiceEntityToBean(odrServiceDetail));
		}

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
		Set<OdrCommercialBean> commercialBeans = new HashSet<>();
		for (com.tcl.dias.oms.entity.entities.OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
			commercialBeans.add(mapOdrServiceCommercialToBean(odrServiceCommercial));
		}
		Set<SolutionComponentBean> solutionComponentBeans = new HashSet<>();
		if (odrSolutionComponents != null && !odrSolutionComponents.isEmpty()) {
			odrSolutionComponents.stream().forEach(odrSolutionComponent -> {
				solutionComponentBeans.add(mapOdrSolutionComponentToBean(odrSolutionComponent));
			});
		}
		odrOrderBean.setSolutionComponentBeans(solutionComponentBeans);
		odrOrderBean.setOdrCommercialBean(commercialBeans);
		return odrOrderBean;
	}

	/**
	 * Method to mapOdrServiceCommercialToBean
	 *
	 * @param odrServiceCommercial
	 * @return
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

	/**
	 * Method to map ProductDetailEntityToBean
	 *
	 * @param odrProductDetail
	 * @return
	 */
	public OdrProductDetail mapProductDetailEntityToBean(
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

	/**
	 * Method to map ProductDetailAttributeToBean
	 *
	 * @param odrProductDetailAttributes
	 * @return
	 */
	public OdrProductDetailAttributes mapProductDetailAttributeToBean(
			com.tcl.dias.oms.entity.entities.OdrProductDetailAttributes odrProductDetailAttributes) {
		com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes scProductDetailAttributes = new com.tcl.dias.common.fulfillment.beans.OdrProductDetailAttributes();
		scProductDetailAttributes.setCategory(odrProductDetailAttributes.getCategory());
		scProductDetailAttributes.setAttributeName(odrProductDetailAttributes.getAttributeName());
		scProductDetailAttributes.setAttributeValue(odrProductDetailAttributes.getAttributeValue());
		scProductDetailAttributes.setIsActive(odrProductDetailAttributes.getIsActive());
		return scProductDetailAttributes;
	}

	/**
	 * Method to map ServiceCommercialComponentToBean
	 *
	 * @param odrServiceCommercialComponent
	 * @return
	 */
	public OdrServiceCommercial mapServiceCommercialComponentToBean(
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

	/**
	 * Method to map ServiceEntityToBean
	 *
	 * @param odrServiceDetail
	 * @return
	 */
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
		odrServiceDetailBean.setOrderSubCategory(odrServiceDetail.getOrderSubCategory());
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
		odrServiceDetailBean.setErfPrdCatalogFlavourName(odrServiceDetail.getErfPrdCatalogFlavourName());
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
		odrServiceDetailBean.setOrderType(odrServiceDetail.getOrderType());
		odrServiceDetailBean.setOrderCategory(odrServiceDetail.getOrderCategory());
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
		// odrServiceDetailBean.setOdrContractInfo(mapContractingInfoEntityToBean(odrServiceDetail.getOdrContractInfo
		// ()));
		// odrServiceDetailBean.setOdrContractInfo(odrContractInfo);
		// odrServiceDetailBean.setOdrOrder(odrServiceDetail.geto);
		odrServiceDetailBean.setOdrOrderUuid(odrServiceDetail.getOdrOrderUuid());
		Set<OdrServiceAttributeBean> odrServiceAttrBeans = new HashSet<>();

		List<OdrServiceAttribute> odrServiceAttributeList = odrServiceAttributeRepository
				.findByOdrServiceDetail_Id(odrServiceDetail.getId());
		LOGGER.info("serviceID={}  odrServiceDetail.getId()={} odrServiceAttributeList={}", odrServiceDetail.getUuid(),
				odrServiceDetail.getId(), odrServiceAttributeList.size());
		for (OdrServiceAttribute odrServiceAttribute : odrServiceAttributeList) {
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
		for (com.tcl.dias.oms.entity.entities.OdrProductDetail odrProductDetail : odrServiceDetail
				.getOdrProductDetail()) {
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
		if (odrServiceDetail.getOdrServiceSlas() != null) {
			for (OdrServiceSla odrServiceSla : odrServiceDetail.getOdrServiceSlas()) {
				odrServiceSlaBeans.add(mapServiceSlaEntityToBean(odrServiceSla));
			}
		}
		LOGGER.info("Getting attachment details for id {}", odrServiceDetail.getId());
		List<OdrAttachment> odrAttachments = odrAttachmentRepository.findByOdrServiceDetailId(odrServiceDetail.getId());
		List<OdrAttachmentBean> odrAttachmentBeans = new ArrayList<OdrAttachmentBean>();
		for (OdrAttachment odrAttachment : odrAttachments) {
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
		}
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
		Set<OdrComponent> odrComponentsList = odrServiceDetail.getOdrComponents();
		if (odrComponentsList != null && !odrComponentsList.isEmpty()) {
			Set<OdrComponentBean> odrComponentBeans = new HashSet<>();
			odrComponentsList.stream().forEach(comp -> {
				odrComponentBeans.add(mapOdrComponentEntityToBean(comp));
			});
			odrServiceDetailBean.setOdrComponentBeans(odrComponentBeans);
		}

		List<OdrAsset> odrAssets = odrAssetRepository.findByOdrServiceDetail(odrServiceDetail);
		List<OdrAssetBean> odrAssetBeans = new ArrayList<>();
		if (Objects.nonNull(odrAssets) && !odrAssets.isEmpty()) {
			for (OdrAsset asset : odrAssets) {
				OdrAssetBean odrAssetBean = new OdrAssetBean();
				odrAssetBean.setId(asset.getId());
				odrAssetBean.setCreatedBy(asset.getCreatedBy());
				odrAssetBean.setCreatedDate(DateUtil.convertTimestampToDate(asset.getCreatedDate()));
				odrAssetBean.setFqdn(asset.getFqdn());
				odrAssetBean.setIsActive(asset.getIsActive());
				odrAssetBean.setIsSharedInd(asset.getIsSharedInd());
				odrAssetBean.setOriginnetwork(asset.getOriginnetwork());
				odrAssetBean.setType(asset.getType());
				odrAssetBean.setUpdatedBy(asset.getUpdatedBy());
				odrAssetBean.setUpdatedDate(DateUtil.convertTimestampToDate(asset.getUpdatedDate()));
				odrAssetBean.setName(asset.getName());
				odrAssetBean.setOdrServiceDetailId(odrServiceDetail.getId());
				odrAssetBean.setPublicIp(asset.getPublicIp());
				odrAssetBeans.add(odrAssetBean);
			}
		}
		odrServiceDetailBean.setOdrAssetBeans(odrAssetBeans);

		/*
		 * for (OdrServiceCommercialComponent odrServiceCommercialComponent :
		 * odrServiceDetail .getOdrServiceCommercialComponent()) {
		 * odrServiceDetailBean.getOdrServiceCommercials()
		 * .add(mapServiceCommercialToBean(odrServiceCommercialComponent)); }
		 */
		return odrServiceDetailBean;
	}

	/**
	 * Method to map OdrComponentEntityToBean
	 *
	 * @param odrComponent
	 * @return
	 */
	public OdrComponentBean mapOdrComponentEntityToBean(OdrComponent odrComponent) {
		OdrComponentBean odrComponentBean = new OdrComponentBean();
		odrComponentBean.setComponentName(odrComponent.getComponentName());
		odrComponentBean.setSiteType(odrComponent.getSiteType());
		odrComponentBean.setId(odrComponent.getId());
		Set<OdrComponentAttributeBean> odrComponentAttributesBeans = new HashSet<>();
		Set<OdrComponentAttribute> odrComponentAttributes = odrComponent.getOdrComponentAttributes();
		if (odrComponentAttributes != null && !odrComponentAttributes.isEmpty()) {
			odrComponentAttributes.forEach(odrComponentAttr -> odrComponentAttributesBeans
					.add(mapOdrComponentAttributeToBen(odrComponentAttr)));
		}
		odrComponentBean.setOdrComponentAttributeBeans(odrComponentAttributesBeans);
		return odrComponentBean;
	}

	/**
	 * Method to map odrComopnentAttributeToBean
	 *
	 * @param odrComponentAttribute
	 * @return
	 */
	public OdrComponentAttributeBean mapOdrComponentAttributeToBen(OdrComponentAttribute odrComponentAttribute) {
		OdrComponentAttributeBean odrComponentAttributeBean = new OdrComponentAttributeBean();
		odrComponentAttributeBean.setId(odrComponentAttribute.getId());
		odrComponentAttributeBean.setName(odrComponentAttribute.getAttributeName());
		odrComponentAttributeBean.setValue(odrComponentAttribute.getAttributeValue());
		odrComponentAttributeBean.setIsAdditionalParam(odrComponentAttribute.getIsAdditionalParam());
		if (Objects.nonNull(odrComponentAttribute.getIsAdditionalParam())
				&& CommonConstants.Y.equals(odrComponentAttribute.getIsAdditionalParam())) {
			odrAdditionalServiceParamRepository.findById(Integer.valueOf(odrComponentAttribute.getAttributeValue()))
					.ifPresent(odrAdditionalServiceParam -> {
						OdrAdditionalServiceParamBean additionalServiceParamBean = new OdrAdditionalServiceParamBean();
						additionalServiceParamBean.setAttribute(odrAdditionalServiceParam.getAttribute());
						additionalServiceParamBean.setCategory(odrAdditionalServiceParam.getCategory());
						additionalServiceParamBean.setCreatedBy(odrAdditionalServiceParam.getCreatedBy());
						additionalServiceParamBean.setCreatedTime(odrAdditionalServiceParam.getCreatedTime());
						additionalServiceParamBean.setUpdatedBy(odrAdditionalServiceParam.getUpdatedBy());
						additionalServiceParamBean.setUpdatedTime(odrAdditionalServiceParam.getUpdatedTime());
						additionalServiceParamBean.setValue(odrAdditionalServiceParam.getValue());
						additionalServiceParamBean.setId(odrAdditionalServiceParam.getId());
						odrComponentAttributeBean.setOdrAdditionalServiceParam(additionalServiceParamBean);
					});
		}
		return odrComponentAttributeBean;
	}

	/**
	 * Method to map service commercial to bean
	 *
	 * @param odrServiceCommercialComponent
	 * @return
	 */
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
	 * Method to map service attr entity to bean
	 *
	 * @param odrServiceAttribute
	 * @return
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
		return odrServiceAttributeBean;
	}

	/**
	 * Method to map odrservicecommercials bean
	 *
	 * @param odrServiceCommercialComponent
	 * @return
	 */
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

	/**
	 * Method to map contract entity info entity to bean
	 *
	 * @param odrContractInfo
	 * @return
	 */
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

	/**
	 * Method to map OrderAttrEntityToBean
	 *
	 * @param odrOrderAttribute
	 * @return
	 */
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

	/**
	 * Method to map services sla entity to bean.
	 *
	 * @param odrServiceSla
	 * @return
	 */
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

	/**
	 * Method to map odrSolutionComponentTobean
	 *
	 * @param odrSolutionComponent
	 * @return
	 */
	public SolutionComponentBean mapOdrSolutionComponentToBean(OdrSolutionComponent odrSolutionComponent) {
		SolutionComponentBean solutionComponentBean = new SolutionComponentBean();
		solutionComponentBean.setComponentGroup(odrSolutionComponent.getComponentGroup());
		solutionComponentBean.setCpeComponentId(odrSolutionComponent.getCpeComponentId());
		solutionComponentBean.setIsActive(odrSolutionComponent.getIsActive());
		if (!Objects.isNull(odrSolutionComponent.getOdrOrder())) {
			solutionComponentBean.setOdrOrderId(odrSolutionComponent.getOdrOrder().getId());
		}
		solutionComponentBean.setOrderCode(odrSolutionComponent.getOrderCode());
		if (!Objects.isNull(odrSolutionComponent.getOdrServiceDetail2())) {
			solutionComponentBean.setParentServiceDetailId(odrSolutionComponent.getOdrServiceDetail2().getId());
		}
		if (!Objects.isNull(odrSolutionComponent.getOdrServiceDetail1())) {
			solutionComponentBean.setServiceDetailId(odrSolutionComponent.getOdrServiceDetail1().getId());
		}
		if (!Objects.isNull(odrSolutionComponent.getOdrServiceDetail3())) {
			solutionComponentBean.setSolutionServiceDetailId(odrSolutionComponent.getOdrServiceDetail3().getId());
		}
		solutionComponentBean.setSolutionComponentid(odrSolutionComponent.getId());
		return solutionComponentBean;
	}

	/**
	 * Method to map orderentitytobean
	 *
	 * @param odrOrder
	 * @param odrContractInfoEntity
	 * @param odrOrderAttributeEntity
	 * @param odrServiceDetailEntity
	 * @param odrServiceCommercials
	 * @param odrSolutionComponents
	 * @param odrTeamsDRServiceCommercials
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public OdrOrderBean mapOrderEntityToBean(OdrOrder odrOrder, List<OdrContractInfo> odrContractInfoEntity,
			List<OdrOrderAttribute> odrOrderAttributeEntity, List<OdrServiceDetail> odrServiceDetailEntity,
			List<com.tcl.dias.oms.entity.entities.OdrServiceCommercial> odrServiceCommercials,
			List<OdrSolutionComponent> odrSolutionComponents,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials) {

		OdrOrderBean mapOrderEntityToBean = mapOrderEntityToBean(odrOrder, odrContractInfoEntity,
				odrOrderAttributeEntity, odrServiceDetailEntity, odrServiceCommercials, odrSolutionComponents);

		Set<OdrTeamsDRCommercialBean> odrTeamsDRCommercialBeans = new HashSet<>();
		if (odrTeamsDRServiceCommercials != null && !odrTeamsDRServiceCommercials.isEmpty()) {
			odrTeamsDRServiceCommercials.forEach(odrSolutionComponent -> {
				odrTeamsDRCommercialBeans.add(mapOdrTeamsDRCommercialToBean(odrSolutionComponent));
			});
		}

		mapOrderEntityToBean.setOdrTeamsDRCommercialBean(odrTeamsDRCommercialBeans);

		return mapOrderEntityToBean;
	}

	/**
	 * Method to map OdrTeamsDRCommercialToBean
	 *
	 * @param odrTeamsDRServiceCommercial
	 * @return
	 */
	public OdrTeamsDRCommercialBean mapOdrTeamsDRCommercialToBean(
			OdrTeamsDRServiceCommercial odrTeamsDRServiceCommercial) {
		OdrTeamsDRCommercialBean odrTeamsDRCommercialBean = new OdrTeamsDRCommercialBean();
		odrTeamsDRCommercialBean.setServiceId(odrTeamsDRServiceCommercial.getOdrServiceDetail().getId());
		odrTeamsDRCommercialBean.setComponentName(odrTeamsDRServiceCommercial.getComponentName());
		odrTeamsDRCommercialBean.setComponentDesc(odrTeamsDRServiceCommercial.getComponentDesc());
		odrTeamsDRCommercialBean.setComponentType(odrTeamsDRServiceCommercial.getComponentType());
		odrTeamsDRCommercialBean.setChargeItem(odrTeamsDRServiceCommercial.getChargeItem());
		odrTeamsDRCommercialBean.setHsnCode(odrTeamsDRServiceCommercial.getHsnCode());
		odrTeamsDRCommercialBean.setQuantity(odrTeamsDRServiceCommercial.getQuantity());
		odrTeamsDRCommercialBean.setArc(odrTeamsDRServiceCommercial.getArc());
		odrTeamsDRCommercialBean.setUnitMrc(odrTeamsDRServiceCommercial.getUnitMrc());
		odrTeamsDRCommercialBean.setMrc(odrTeamsDRServiceCommercial.getMrc());
		odrTeamsDRCommercialBean.setNrc(odrTeamsDRServiceCommercial.getNrc());
		odrTeamsDRCommercialBean.setUnitNrc(odrTeamsDRServiceCommercial.getUnitNrc());
		odrTeamsDRCommercialBean.setTcv(odrTeamsDRServiceCommercial.getTcv());
		odrTeamsDRCommercialBean.setIsActive(odrTeamsDRServiceCommercial.getIsActive());
		odrTeamsDRCommercialBean.setContractType(odrTeamsDRServiceCommercial.getContractType());
		odrTeamsDRCommercialBean.setCreatedBy(odrTeamsDRServiceCommercial.getCreatedBy());
		odrTeamsDRCommercialBean.setEffectiveUsage(odrTeamsDRServiceCommercial.getUsage());
		odrTeamsDRCommercialBean.setEffectiveOverage(odrTeamsDRServiceCommercial.getOverage());
		return odrTeamsDRCommercialBean;
	}

}
