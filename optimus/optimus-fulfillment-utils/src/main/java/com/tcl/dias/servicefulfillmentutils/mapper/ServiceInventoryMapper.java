package com.tcl.dias.servicefulfillmentutils.mapper;

import java.util.Objects;

import com.tcl.dias.common.servicefulfillment.beans.ScAttachmentBean;
import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderAttributeBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.servicefulfillment.beans.ScProductDetailAttributesBean;
import com.tcl.dias.common.servicefulfillment.beans.ScProductDetailBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceAttributeBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceCommercialBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

public class ServiceInventoryMapper {

	private ServiceInventoryMapper() {
		//Do Nothing
	}
	
	public static ScOrderBean mapEntityToOrderBean(ScOrder scOrder) {
		ScOrderBean scOrderBean = new ScOrderBean();
		scOrderBean.setId(scOrder.getId());
		scOrderBean.setCreatedBy(scOrder.getCreatedBy());
		scOrderBean.setCreatedDate(scOrder.getCreatedDate());
		scOrderBean.setCustomerGroupName(scOrder.getCustomerGroupName());
		scOrderBean.setCustomerSegment(scOrder.getCustomerSegment());
		scOrderBean.setDemoFlag(scOrder.getDemoFlag());
		scOrderBean.setErfCustCustomerId(scOrder.getErfCustCustomerId());
		scOrderBean.setErfCustCustomerName(scOrder.getErfCustCustomerName());
		scOrderBean.setErfCustLeId(scOrder.getErfCustLeId());
		scOrderBean.setErfCustLeName(scOrder.getErfCustLeName());
		scOrderBean.setErfCustPartnerId(scOrder.getErfCustPartnerId());
		scOrderBean.setErfCustPartnerName(scOrder.getErfCustPartnerName());
		scOrderBean.setErfCustPartnerLeId(scOrder.getErfCustPartnerLeId());
		scOrderBean.setPartnerCuid(scOrder.getPartnerCuid());
		scOrderBean.setErfCustSpLeId(scOrder.getErfCustSpLeId());
		scOrderBean.setErfCustSpLeName(scOrder.getErfCustSpLeName());
		scOrderBean.setErfUserCustomerUserId(scOrder.getErfUserCustomerUserId());
		scOrderBean.setErfUserInitiatorId(scOrder.getErfUserInitiatorId());
		scOrderBean.setUuid(scOrder.getUuid());
		scOrderBean.setUpdatedDate(scOrder.getUpdatedDate());
		scOrderBean.setUpdatedBy(scOrder.getUpdatedBy());
		scOrderBean.setTpsSfdcCuid(scOrder.getTpsSfdcCuid());
		scOrderBean.setTpsSecsId(scOrder.getTpsSecsId());
		scOrderBean.setTpsSapCrnId(scOrder.getTpsSapCrnId());
		scOrderBean.setTpsCrmSystem(scOrder.getTpsCrmSystem());
		scOrderBean.setTpsCrmOptyId(scOrder.getTpsCrmOptyId());
		scOrderBean.setTpsCrmCofId(scOrder.getTpsCrmCofId());
		scOrderBean.setTpsCrmOptyId(scOrder.getTpsSfdcOptyId());
		scOrderBean.setErfOrderId(scOrder.getErfOrderId());
		scOrderBean.setSfdcAccountId(scOrder.getSfdcAccountId());
		scOrderBean.setParentOpOrderCode(scOrder.getParentOpOrderCode());
		scOrderBean.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
		scOrderBean.setParentId(scOrder.getParentId());
		scOrderBean.setOrderType(scOrder.getOrderType());
		scOrderBean.setOrderStatus(scOrder.getOrderStatus());
		scOrderBean.setOrderStartDate(scOrder.getOrderStartDate());
		scOrderBean.setOrderSource(scOrder.getOrderSource());
		scOrderBean.setOrderEndDate(scOrder.getOrderEndDate());
		scOrderBean.setOrderCategory(scOrder.getOrderCategory());
		scOrderBean.setErfOrderLeId(scOrder.getErfOrderLeId());
		scOrderBean.setOpOrderCode(scOrder.getOpOrderCode());
		scOrderBean.setOpportunityClassification(scOrder.getOpportunityClassification());
		scOrderBean.setIsActive(scOrder.getIsActive());
		scOrderBean.setIsBundleOrder(scOrder.getIsBundleOrder());
		scOrderBean.setIsMultipleLe(scOrder.getIsMultipleLe());
		scOrderBean.setLastMacdDate(scOrder.getLastMacdDate());
		scOrderBean.setMacdCreatedDate(scOrder.getMacdCreatedDate());
		for (ScOrderAttribute scOrderAttribute: scOrder.getScOrderAttributes()) {
			scOrderBean.getScOrderAttributes().add(mapOrderAttrEntityToBean(scOrderAttribute));
		}
		for (ScContractInfo scContractInfo: scOrder.getScContractInfos1()) {
			scOrderBean.getScContractInfos().add(mapContractInfoEntityToBean(scContractInfo));
		}
		for (ScServiceDetail scServiceDetail: scOrder.getScServiceDetails()) {
			scOrderBean.getScServiceDetails().add(mapServiceDetailEntityToBean(scServiceDetail));
		}
		return scOrderBean;
	}

	public static ScOrderAttributeBean mapOrderAttrEntityToBean(ScOrderAttribute scOrderAttribute) {
		ScOrderAttributeBean scOrderAttributeBean = new ScOrderAttributeBean();
		scOrderAttributeBean.setAttributeAltValueLabel(scOrderAttribute.getAttributeAltValueLabel());
		scOrderAttributeBean.setAttributeName(scOrderAttribute.getAttributeName());
		scOrderAttributeBean.setAttributeValue(scOrderAttribute.getAttributeValue());
		scOrderAttributeBean.setCategory(scOrderAttribute.getCategory());
		scOrderAttributeBean.setCreatedBy(scOrderAttribute.getCreatedBy());
		scOrderAttributeBean.setCreatedDate(scOrderAttribute.getCreatedDate());
		scOrderAttributeBean.setIsActive(scOrderAttribute.getIsActive());
		scOrderAttributeBean.setUpdatedBy(scOrderAttribute.getUpdatedBy());
		scOrderAttributeBean.setUpdatedDate(scOrderAttribute.getUpdatedDate());
		return scOrderAttributeBean;
	}

	public static ScContractInfoBean mapContractInfoEntityToBean(ScContractInfo scContractInfo) {
		ScContractInfoBean scContractInfoBean = new ScContractInfoBean();
		scContractInfoBean.setAccountManager(scContractInfo.getAccountManager());
		scContractInfoBean.setAccountManagerEmail(scContractInfo.getAccountManagerEmail());
		scContractInfoBean.setArc(scContractInfo.getArc());
		scContractInfoBean.setBillingAddress(scContractInfo.getBillingAddress());
		scContractInfoBean.setBillingFrequency(scContractInfo.getBillingFrequency());
		scContractInfoBean.setBillingMethod(scContractInfo.getBillingMethod());
		scContractInfoBean.setContractEndDate(scContractInfo.getContractEndDate());
		scContractInfoBean.setContractStartDate(scContractInfo.getContractStartDate());
		scContractInfoBean.setCreatedBy(scContractInfo.getCreatedBy());
		scContractInfoBean.setCreatedDate(scContractInfo.getCreatedDate());
		scContractInfoBean.setCustomerContact(scContractInfo.getCustomerContact());
		scContractInfoBean.setCustomerContactEmail(scContractInfo.getCustomerContactEmail());
		scContractInfoBean.setDiscountArc(scContractInfo.getDiscountArc());
		scContractInfoBean.setDiscountMrc(scContractInfo.getDiscountMrc());
		scContractInfoBean.setDiscountNrc(scContractInfo.getDiscountNrc());
		scContractInfoBean.setErfCustCurrencyId(scContractInfo.getErfCustCurrencyId());
		scContractInfoBean.setErfCustLeId(scContractInfo.getErfCustLeId());
		scContractInfoBean.setErfCustLeName(scContractInfo.getErfCustLeName());
		scContractInfoBean.setErfCustSpLeId(scContractInfo.getErfCustSpLeId());
		scContractInfoBean.setErfCustSpLeName(scContractInfo.getErfCustSpLeName());
		scContractInfoBean.setErfLocBillingLocationId(scContractInfo.getErfLocBillingLocationId());
		scContractInfoBean.setIsActive(scContractInfo.getIsActive());
		scContractInfoBean.setLastMacdDate(scContractInfo.getLastMacdDate());
		scContractInfoBean.setMrc(scContractInfo.getMrc());
		scContractInfoBean.setNrc(scContractInfo.getNrc());
		scContractInfoBean.setBillingAddressLine1(scContractInfo.getBillingAddressLine1());
		scContractInfoBean.setBillingAddressLine2(scContractInfo.getBillingAddressLine2());
		scContractInfoBean.setBillingAddressLine3(scContractInfo.getBillingAddressLine3());
		scContractInfoBean.setBillingCity(scContractInfo.getBillingCity());
		scContractInfoBean.setBillingCountry(scContractInfo.getBillingCountry());
		scContractInfoBean.setBillingCity(scContractInfo.getBillingCity());
		scContractInfoBean.setBillingState(scContractInfo.getBillingState());
		scContractInfoBean.setBillingPincode(scContractInfo.getBillingPincode());
		scContractInfoBean.setOrderTermInMonths(scContractInfo.getOrderTermInMonths());
		scContractInfoBean.setPaymentTerm(scContractInfo.getPaymentTerm());
		scContractInfoBean.setTpsSfdcCuid(scContractInfo.getTpsSfdcCuid());
		scContractInfoBean.setUpdatedBy(scContractInfo.getUpdatedBy());
		scContractInfoBean.setUpdatedDate(scContractInfo.getUpdatedDate());
		scContractInfoBean.setBillingContactId(scContractInfo.getBillingContactId());
		return scContractInfoBean;
	}

	public static ScServiceDetailBean mapServiceDetailEntityToBean(ScServiceDetail scServiceDetail) {
		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		scServiceDetailBean.setErfScServiceId(scServiceDetail.getErfOdrServiceId());
		scServiceDetailBean.setId(scServiceDetail.getId());
		scServiceDetailBean.setAccessType(scServiceDetail.getAccessType());
		scServiceDetailBean.setArc(scServiceDetail.getArc());
		scServiceDetailBean.setBillingAccountId(scServiceDetail.getBillingAccountId());
		scServiceDetailBean.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		scServiceDetailBean.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		scServiceDetailBean.setBillingType(scServiceDetail.getBillingType());
		//scServiceDetailBean.setBwPortspeed(scServiceDetail.getBwPortspeed());
		scServiceDetailBean.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		//scServiceDetailBean.setBwUnit(scServiceDetail.getBwUnit());
		scServiceDetailBean.setCallType(scServiceDetail.getCallType());
		scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
		scServiceDetailBean.setCreatedDate(scServiceDetail.getCreatedDate());
		scServiceDetailBean.setCustOrgNo(scServiceDetail.getCustOrgNo());
		//scServiceDetailBean.setDemarcationFloor(scServiceDetail.getDemarcationFloor());
		//scServiceDetailBean.setDemarcationRack(scServiceDetail.getDemarcationRack());
		//scServiceDetailBean.setDemarcationRoom(scServiceDetail.getDemarcationRoom());
		//scServiceDetailBean.setDestinationCity(scServiceDetail.getDestinationCity());
		//scServiceDetailBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
		scServiceDetailBean.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
		scServiceDetailBean.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetailBean.setDiscountArc(scServiceDetail.getDiscountArc());
		scServiceDetailBean.setDiscountMrc(scServiceDetail.getDiscountMrc());
		scServiceDetailBean.setDiscountNrc(scServiceDetail.getDiscountNrc());
		scServiceDetailBean.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
		scServiceDetailBean.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
		scServiceDetailBean.setErfLocPopSiteAddressId(scServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetailBean.setErfLocSiteAddressId(scServiceDetail.getErfLocSiteAddressId());
		scServiceDetailBean.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
		scServiceDetailBean.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
		scServiceDetailBean.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetailBean.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		scServiceDetailBean.setErfPrdCatalogParentProductOfferingName(scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
		//scServiceDetailBean.setFeasibilityId(scServiceDetail.getFeasibilityId());
		scServiceDetailBean.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
		scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
		scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
		//scServiceDetailBean.setLastmileBw(scServiceDetail.getLastmileBw());
		scServiceDetailBean.setLastmileBwAltName(scServiceDetail.getLastmileBwAltName());
		//scServiceDetailBean.setLastmileBwUnit(scServiceDetail.getLastmileBwUnit());
		//scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileProvider());
		//scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileType());
		//scServiceDetailBean.setLatLong(scServiceDetail.getLatLong());
		scServiceDetailBean.setLocalItContactEmail(scServiceDetail.getLocalItContactEmail());
		//scServiceDetailBean.setLocalItContactMobile(scServiceDetail.getLocalItContactMobile());
		scServiceDetailBean.setLocalItContactName(scServiceDetail.getLocalItContactName());
		scServiceDetailBean.setMrc(scServiceDetail.getMrc());
		scServiceDetailBean.setNrc(scServiceDetail.getNrc());
		scServiceDetailBean.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
		scServiceDetailBean.setParentId(scServiceDetail.getParentId());
		//scServiceDetailBean.setPopSiteAddress(scServiceDetail.getPopSiteAddress());
		//scServiceDetailBean.setPopSiteCode(scServiceDetail.getPopSiteCode());
		//scServiceDetailBean.setPriSecServiceLink(scServiceDetail.getPriSecServiceLink()); //NOSONAR
		scServiceDetailBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
		//scServiceDetailBean.setErdPriSecServiceLinkId(scServiceDetail.getErdPriSecServiceLinkId()); //NOSONAR
		scServiceDetailBean.setProductReferenceId(scServiceDetail.getProductReferenceId());
		scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
		scServiceDetailBean.setServiceClass(scServiceDetail.getServiceClass());
		scServiceDetailBean.setServiceClassification(scServiceDetail.getServiceClassification());
		scServiceDetailBean.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate());
		scServiceDetailBean.setServiceGroupId(scServiceDetail.getServiceGroupId());
		scServiceDetailBean.setServiceGroupType(scServiceDetail.getServiceGroupType());
		//scServiceDetailBean.setServiceOption(scServiceDetail.getServiceOption());
		scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
		scServiceDetailBean.setServiceTerminationDate(scServiceDetail.getServiceTerminationDate());
		scServiceDetailBean.setServiceTopology(scServiceDetail.getServiceTopology());
		scServiceDetailBean.setSiteAddress(scServiceDetail.getSiteAddress());
		scServiceDetailBean.setSiteAlias(scServiceDetail.getSiteAlias());
		scServiceDetailBean.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
		scServiceDetailBean.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		scServiceDetailBean.setSiteTopology(scServiceDetail.getSiteTopology());
		//scServiceDetailBean.setSiteType(scServiceDetail.getSiteType());
		scServiceDetailBean.setSlaTemplate(scServiceDetail.getSlaTemplate());
		scServiceDetailBean.setSmEmail(scServiceDetail.getSmEmail());
		scServiceDetailBean.setSmName(scServiceDetail.getSmName());
		//scServiceDetailBean.setSourceCity(scServiceDetail.getSourceCity());
		//scServiceDetailBean.setSourceCountry(scServiceDetail.getSourceCountry());
		//scServiceDetailBean.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		scServiceDetailBean.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		scServiceDetailBean.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		//scServiceDetailBean.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
		scServiceDetailBean.setTpsCopfId(scServiceDetail.getTpsCopfId());
		scServiceDetailBean.setTpsServiceId(scServiceDetail.getTpsServiceId());
		scServiceDetailBean.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
		scServiceDetailBean.setUpdatedDate(scServiceDetail.getUpdatedDate());
		scServiceDetailBean.setUuid(scServiceDetail.getUuid());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnName());
		//scServiceDetailBean.setPriority(scServiceDetail.getPriority()); //NOSONAR
		//scServiceDetailBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate()); //NOSONAR
		//scServiceDetailBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate()); //NOSONAR
		//scServiceDetailBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate()); //NOSONAR
		//scServiceDetailBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate()); //NOSONAR
		//scServiceDetailBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate()); //NOSONAR
		//scServiceDetailBean.setMstStatus(scServiceDetail.getMstStatus()); //NOSONAR
		//scServiceDetailBean.setMstTemplateKey(scServiceDetail.getMstTemplateKey()); //NOSONAR
		//scServiceDetailBean.setSourceAddressLineOne(scServiceDetail.getSourceAddressLineOne());
		//scServiceDetailBean.setSourceAddressLineTwo(scServiceDetail.getSourceAddressLineTwo());
		//scServiceDetailBean.setSourceLocality(scServiceDetail.getSourceLocality());
		//scServiceDetailBean.setSourcePincode(scServiceDetail.getSourcePincode());
		//scServiceDetailBean.setDestinationAddressLineOne(scServiceDetail.getDestinationAddressLineOne());
		//scServiceDetailBean.setDestinationAddressLineTwo(scServiceDetail.getDestinationAddressLineTwo());
		//scServiceDetailBean.setDestinationLocality(scServiceDetail.getDestinationLocality());
		//scServiceDetailBean.setDestinationPincode(scServiceDetail.getDestinationPincode());
		//scServiceDetailBean.setSourceState(scServiceDetail.getSourceState());
		//scServiceDetailBean.setDestinationState(scServiceDetail.getDestinationState());
		//scServiceDetailBean.setServiceVariant(scServiceDetail.getServiceVariant()); //NOSONAR
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnSolutionId());
		//scServiceDetailBean.setLineRate(scServiceDetail.getLineRate()); //NOSONAR
		//scServiceDetailBean.setDemarcationApartment(scServiceDetail.getDemarcationBuildingName());
		//scServiceDetailBean.setBurstableBwPortspeed(scServiceDetail.getBurstableBw());
		//scServiceDetailBean.setBurstableBwUnit(scServiceDetail.getBurstableBwUnit());
		scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileScenario());
		scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileConnectionType());
		
		for(ScServiceAttribute scServiceAttribute:scServiceDetail.getScServiceAttributes()) {
			scServiceDetailBean.getScServiceAttributes().add(mapServiceDetailAttrEntityToBean(scServiceAttribute));
		}
		
		for(ScAttachment scAttachment:scServiceDetail.getScAttachments()) {
			scServiceDetailBean.getScAttachments().add(mapServiceAttachmentEntityToBean(scAttachment));
		}
		
		for(ScProductDetail scProductDetail:scServiceDetail.getScProductDetail()) {
			scServiceDetailBean.getScProductDetail().add(mapServiceProductEntityToBean(scProductDetail));
		}
		return scServiceDetailBean;
	}

	public static ScServiceAttributeBean mapServiceDetailAttrEntityToBean(ScServiceAttribute scServiceAttribute) {
		ScServiceAttributeBean scServiceAttributeBean = new ScServiceAttributeBean();
		scServiceAttributeBean.setId(scServiceAttribute.getId());
		scServiceAttributeBean.setAttributeAltValueLabel(scServiceAttribute.getAttributeAltValueLabel());
		scServiceAttributeBean.setAttributeName(scServiceAttribute.getAttributeName());
		scServiceAttributeBean.setAttributeValue(scServiceAttribute.getAttributeValue());
		scServiceAttributeBean.setCategory(scServiceAttribute.getCategory());
		scServiceAttributeBean.setCreatedBy(scServiceAttribute.getCreatedBy());
		scServiceAttributeBean.setCreatedDate(scServiceAttribute.getCreatedDate());
		scServiceAttributeBean.setIsActive(scServiceAttribute.getIsActive());
		scServiceAttributeBean.setUpdatedBy(scServiceAttribute.getUpdatedBy());
		scServiceAttributeBean.setUpdatedDate(scServiceAttribute.getUpdatedDate());
		scServiceAttributeBean.setIsAdditionalParam(scServiceAttribute.getIsAdditionalParam());
		return scServiceAttributeBean;
	}

	public static ScAttachmentBean mapServiceAttachmentEntityToBean(ScAttachment scAttachment) {
		ScAttachmentBean scAttachmentBean = new ScAttachmentBean();
		scAttachmentBean.setId(scAttachment.getId());
		
		scAttachmentBean.setIsActive(scAttachment.getIsActive());
		scAttachmentBean.setOfferingName(scAttachment.getOfferingName());
		scAttachmentBean.setOrderId(scAttachment.getOrderId());
		scAttachmentBean.setProductName(scAttachment.getProductName());
		scAttachmentBean.setServiceCode(scAttachment.getServiceCode());
		//scAttachmentBean.setErfScServiceId(scAttachment.getServiceCode()); //NOSONAR
		scAttachmentBean.setServiceId(scAttachment.getServiceCode());
		scAttachmentBean.setSiteId(scAttachment.getSiteId());
		if (Objects.nonNull(scAttachment.getAttachment())) {
			scAttachmentBean.setAttachmentId(scAttachment.getAttachment().getId());
			scAttachmentBean.setCategory(scAttachment.getAttachment().getCategory());
			scAttachmentBean.setContentTypeHeader(scAttachment.getAttachment().getContentTypeHeader());
			scAttachmentBean.setCreatedBy(scAttachment.getAttachment().getCreatedBy());
			scAttachmentBean.setCreatedDate(scAttachment.getAttachment().getCreatedDate());
			scAttachmentBean.setName(scAttachment.getAttachment().getName());
			scAttachmentBean.setStoragePathUrl(scAttachment.getAttachment().getUriPathOrUrl());
			scAttachmentBean.setType(scAttachment.getAttachment().getType());
			scAttachmentBean.setUpdatedBy(scAttachment.getAttachment().getUpdatedBy());
			scAttachmentBean.setUpdatedDate(scAttachment.getAttachment().getUpdatedDate());
		}
		return scAttachmentBean;
	}

	public static ScProductDetailBean mapServiceProductEntityToBean(ScProductDetail scProductDetail) {
		ScProductDetailBean scProductDetailBean = new ScProductDetailBean();
		scProductDetailBean.setId(scProductDetail.getId());
		scProductDetailBean.setType(scProductDetail.getType());
		scProductDetailBean.setSolutionName(scProductDetail.getSolutionName());
		scProductDetailBean.setMrc(scProductDetail.getMrc());
		scProductDetailBean.setNrc(scProductDetail.getNrc());
		scProductDetailBean.setArc(scProductDetail.getArc());
		scProductDetailBean.setPpuRate(scProductDetail.getPpuRate());
		scProductDetailBean.setIsActive(scProductDetail.getIsActive());
		scProductDetailBean.setCreatedBy(scProductDetail.getCreatedBy());
		scProductDetailBean.setCreatedDate(scProductDetail.getCreatedDate());
		scProductDetailBean.setUpdatedBy(scProductDetail.getUpdatedBy());
		scProductDetailBean.setUpdatedDate(scProductDetail.getUpdatedDate());
		scProductDetailBean.setCloudCode(scProductDetail.getCloudCode());
		scProductDetailBean.setParentCloudCode(scProductDetail.getParentCloudCode());
		//scProductDetailBean.setBusinessUnit(scProductDetail.getBusinessUnit()); //NOSONAR
		//scProductDetailBean.setZone(scProductDetail.getZone()); //NOSONAR
		//scProductDetailBean.setEnvironment(scProductDetail.getEnvironment()); //NOSONAR
		for (ScProductDetailAttributes scProductDetailAttributes : scProductDetail.getScProductDetailAttributes()) {
			scProductDetailBean.getScProductAttributes().add(mapServiceProductDetialAttrEntityToBean(scProductDetailAttributes, scProductDetailBean));
		}
		for (ScServiceCommericalComponent scServiceCommericalComponent : scProductDetail.getScServiceCommercialComponent()) {
			scProductDetailBean.getScServiceCommercials().add(mapServiceCommercialEntityToBean(scServiceCommericalComponent));
		}
		return scProductDetailBean;
	}

	public static ScProductDetailAttributesBean mapServiceProductDetialAttrEntityToBean(ScProductDetailAttributes scProductDetailAttributes, ScProductDetailBean scProductDetailBean) {
		ScProductDetailAttributesBean scProductDetailAttributesBean = new ScProductDetailAttributesBean();
		scProductDetailAttributesBean.setId(scProductDetailAttributes.getId());
		scProductDetailAttributesBean.setCategory(scProductDetailAttributes.getCategory());
		scProductDetailAttributesBean.setAttributeName(scProductDetailAttributes.getAttributeName());
		scProductDetailAttributesBean.setAttributeValue(scProductDetailAttributes.getAttributeValue());
		scProductDetailAttributesBean.setIsActive(scProductDetailAttributes.getIsActive());
		scProductDetailAttributesBean.setCreatedBy(scProductDetailAttributes.getCreatedBy());
		scProductDetailAttributesBean.setCreatedDate(scProductDetailAttributes.getCreatedDate());
		scProductDetailAttributesBean.setUpdatedBy(scProductDetailAttributes.getUpdatedBy());
		scProductDetailAttributesBean.setUpdatedDate(scProductDetailAttributes.getUpdatedDate());

		if ("IPC Common".equalsIgnoreCase(scProductDetailAttributes.getCategory())) {
			if ("Zone name".equalsIgnoreCase(scProductDetailAttributes.getAttributeName())) {
				scProductDetailBean.setZone(scProductDetailAttributes.getAttributeValue());
			} else if ("Businessunit name".equalsIgnoreCase(scProductDetailAttributes.getAttributeName())) {
				scProductDetailBean.setBusinessUnit(scProductDetailAttributes.getAttributeValue());
			} else if ("Environment name".equalsIgnoreCase(scProductDetailAttributes.getAttributeName())) {
				scProductDetailBean.setEnvironment(scProductDetailAttributes.getAttributeValue());
			}
		}

		return scProductDetailAttributesBean;
	}
	
	public static ScServiceCommercialBean mapServiceCommercialEntityToBean(ScServiceCommericalComponent scServiceCommericalComponent) {
		ScServiceCommercialBean scServiceCommercialBean = new ScServiceCommercialBean();
		scServiceCommercialBean.setId(scServiceCommericalComponent.getId());
		scServiceCommercialBean.setParentItem(scServiceCommericalComponent.getParentItem());
		scServiceCommercialBean.setItemType(scServiceCommericalComponent.getItemType());
		scServiceCommercialBean.setItem(scServiceCommericalComponent.getItem());
		scServiceCommercialBean.setMrc(scServiceCommericalComponent.getMrc());
		scServiceCommercialBean.setNrc(scServiceCommericalComponent.getNrc());
		scServiceCommercialBean.setArc(scServiceCommericalComponent.getArc());
		scServiceCommercialBean.setCreatedBy(scServiceCommericalComponent.getCreatedBy());
		scServiceCommercialBean.setCreatedDate(scServiceCommericalComponent.getCreatedDate());
		return scServiceCommercialBean;
	}

	public static ScOrderBean mapIPCEntityToOrderBean(ScOrder scOrder) {
		ScOrderBean scOrderBean = new ScOrderBean();
		scOrderBean.setId(scOrder.getId());
		scOrderBean.setCreatedBy(scOrder.getCreatedBy());
		scOrderBean.setCreatedDate(scOrder.getCreatedDate());
		scOrderBean.setCustomerGroupName(scOrder.getCustomerGroupName());
		scOrderBean.setCustomerSegment(scOrder.getCustomerSegment());
		scOrderBean.setDemoFlag(scOrder.getDemoFlag());
		scOrderBean.setErfCustCustomerId(scOrder.getErfCustCustomerId());
		scOrderBean.setErfCustCustomerName(scOrder.getErfCustCustomerName());
		scOrderBean.setErfCustLeId(scOrder.getErfCustLeId());
		scOrderBean.setErfCustLeName(scOrder.getErfCustLeName());
		scOrderBean.setErfCustPartnerId(scOrder.getErfCustPartnerId());
		scOrderBean.setErfCustPartnerName(scOrder.getErfCustPartnerName());
		scOrderBean.setErfCustPartnerLeId(scOrder.getErfCustPartnerLeId());
		scOrderBean.setPartnerCuid(scOrder.getPartnerCuid());
		scOrderBean.setErfCustSpLeId(scOrder.getErfCustSpLeId());
		scOrderBean.setErfCustSpLeName(scOrder.getErfCustSpLeName());
		scOrderBean.setErfUserCustomerUserId(scOrder.getErfUserCustomerUserId());
		scOrderBean.setErfUserInitiatorId(scOrder.getErfUserInitiatorId());
		scOrderBean.setUuid(scOrder.getUuid());
		scOrderBean.setUpdatedDate(scOrder.getUpdatedDate());
		scOrderBean.setUpdatedBy(scOrder.getUpdatedBy());
		scOrderBean.setTpsSfdcCuid(scOrder.getTpsSfdcCuid());
		scOrderBean.setTpsSecsId(scOrder.getTpsSecsId());
		scOrderBean.setTpsSapCrnId(scOrder.getTpsSapCrnId());
		scOrderBean.setTpsCrmSystem(scOrder.getTpsCrmSystem());
		scOrderBean.setTpsCrmOptyId(scOrder.getTpsCrmOptyId());
		scOrderBean.setTpsCrmCofId(scOrder.getTpsCrmCofId());
		scOrderBean.setTpsCrmOptyId(scOrder.getTpsSfdcOptyId());
		scOrderBean.setErfOrderId(scOrder.getErfOrderId());
		scOrderBean.setSfdcAccountId(scOrder.getSfdcAccountId());
		scOrderBean.setParentOpOrderCode(scOrder.getParentOpOrderCode());
		scOrderBean.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
		scOrderBean.setParentId(scOrder.getParentId());
		scOrderBean.setOrderType(scOrder.getOrderType());
		scOrderBean.setOrderStatus(scOrder.getOrderStatus());
		scOrderBean.setOrderStartDate(scOrder.getOrderStartDate());
		scOrderBean.setOrderSource(scOrder.getOrderSource());
		scOrderBean.setOrderEndDate(scOrder.getOrderEndDate());
		scOrderBean.setOrderCategory(scOrder.getOrderCategory());
		scOrderBean.setErfOrderLeId(scOrder.getErfOrderLeId());
		scOrderBean.setOpOrderCode(scOrder.getOpOrderCode());
		scOrderBean.setOpportunityClassification(scOrder.getOpportunityClassification());
		scOrderBean.setIsActive(scOrder.getIsActive());
		scOrderBean.setIsBundleOrder(scOrder.getIsBundleOrder());
		scOrderBean.setIsMultipleLe(scOrder.getIsMultipleLe());
		scOrderBean.setLastMacdDate(scOrder.getLastMacdDate());
		scOrderBean.setMacdCreatedDate(scOrder.getMacdCreatedDate());
		for (ScOrderAttribute scOrderAttribute: scOrder.getScOrderAttributes()) {
			scOrderBean.getScOrderAttributes().add(mapOrderAttrEntityToBean(scOrderAttribute));
		}
		for (ScContractInfo scContractInfo: scOrder.getScContractInfos1()) {
			scOrderBean.getScContractInfos().add(mapContractInfoEntityToBean(scContractInfo));
		}
		for (ScServiceDetail scServiceDetail: scOrder.getScServiceDetails()) {
			scOrderBean.getScServiceDetails().add(mapIPCServiceDetailEntityToBean(scServiceDetail));
		}
		return scOrderBean;
	}

	private static ScServiceDetailBean mapIPCServiceDetailEntityToBean(ScServiceDetail scServiceDetail) {
		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		scServiceDetailBean.setErfScServiceId(scServiceDetail.getErfOdrServiceId());
		scServiceDetailBean.setId(scServiceDetail.getId());
		scServiceDetailBean.setAccessType(scServiceDetail.getAccessType());
		scServiceDetailBean.setArc(scServiceDetail.getArc());
		scServiceDetailBean.setBillingAccountId(scServiceDetail.getBillingAccountId());
		scServiceDetailBean.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		scServiceDetailBean.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		scServiceDetailBean.setBillingType(scServiceDetail.getBillingType());
		scServiceDetailBean.setBwPortspeed(scServiceDetail.getBwPortspeed());
		scServiceDetailBean.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		scServiceDetailBean.setBwUnit(scServiceDetail.getBwUnit());
		scServiceDetailBean.setCallType(scServiceDetail.getCallType());
		scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
		scServiceDetailBean.setCreatedDate(scServiceDetail.getCreatedDate());
		scServiceDetailBean.setCustOrgNo(scServiceDetail.getCustOrgNo());
		scServiceDetailBean.setDemarcationFloor(scServiceDetail.getDemarcationFloor());
		scServiceDetailBean.setDemarcationRack(scServiceDetail.getDemarcationRack());
		scServiceDetailBean.setDemarcationRoom(scServiceDetail.getDemarcationRoom());
		scServiceDetailBean.setDestinationCity(scServiceDetail.getDestinationCity());
		scServiceDetailBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
		scServiceDetailBean.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
		scServiceDetailBean.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetailBean.setDiscountArc(scServiceDetail.getDiscountArc());
		scServiceDetailBean.setDiscountMrc(scServiceDetail.getDiscountMrc());
		scServiceDetailBean.setDiscountNrc(scServiceDetail.getDiscountNrc());
		scServiceDetailBean.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
		scServiceDetailBean.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
		scServiceDetailBean.setErfLocPopSiteAddressId(scServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetailBean.setErfLocSiteAddressId(scServiceDetail.getErfLocSiteAddressId());
		scServiceDetailBean.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
		scServiceDetailBean.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
		scServiceDetailBean.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetailBean.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		scServiceDetailBean.setErfPrdCatalogParentProductOfferingName(scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
		scServiceDetailBean.setFeasibilityId(scServiceDetail.getFeasibilityId());
		scServiceDetailBean.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
		scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
		scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
		scServiceDetailBean.setLastmileBw(scServiceDetail.getLastmileBw());
		scServiceDetailBean.setLastmileBwAltName(scServiceDetail.getLastmileBwAltName());
		scServiceDetailBean.setLastmileBwUnit(scServiceDetail.getLastmileBwUnit());
		scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileProvider());
		scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileType());
		scServiceDetailBean.setLatLong(scServiceDetail.getLatLong());
		scServiceDetailBean.setLocalItContactEmail(scServiceDetail.getLocalItContactEmail());
		scServiceDetailBean.setLocalItContactMobile(scServiceDetail.getLocalItContactMobile());
		scServiceDetailBean.setLocalItContactName(scServiceDetail.getLocalItContactName());
		scServiceDetailBean.setMrc(scServiceDetail.getMrc());
		scServiceDetailBean.setNrc(scServiceDetail.getNrc());
		scServiceDetailBean.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
		scServiceDetailBean.setParentId(scServiceDetail.getParentId());
		scServiceDetailBean.setPopSiteAddress(scServiceDetail.getPopSiteAddress());
		scServiceDetailBean.setPopSiteCode(scServiceDetail.getPopSiteCode());
		scServiceDetailBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
		scServiceDetailBean.setProductReferenceId(scServiceDetail.getProductReferenceId());
		scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
		scServiceDetailBean.setServiceClass(scServiceDetail.getServiceClass());
		scServiceDetailBean.setServiceClassification(scServiceDetail.getServiceClassification());
		scServiceDetailBean.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate());
		scServiceDetailBean.setServiceGroupId(scServiceDetail.getServiceGroupId());
		scServiceDetailBean.setServiceGroupType(scServiceDetail.getServiceGroupType());
		scServiceDetailBean.setServiceOption(scServiceDetail.getServiceOption());
		scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
		scServiceDetailBean.setServiceTerminationDate(scServiceDetail.getServiceTerminationDate());
		scServiceDetailBean.setServiceTopology(scServiceDetail.getServiceTopology());
		scServiceDetailBean.setSiteAddress(scServiceDetail.getSiteAddress());
		scServiceDetailBean.setSiteAlias(scServiceDetail.getSiteAlias());
		scServiceDetailBean.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
		scServiceDetailBean.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		scServiceDetailBean.setSiteTopology(scServiceDetail.getSiteTopology());
		scServiceDetailBean.setSiteType(scServiceDetail.getSiteType());
		scServiceDetailBean.setSlaTemplate(scServiceDetail.getSlaTemplate());
		scServiceDetailBean.setSmEmail(scServiceDetail.getSmEmail());
		scServiceDetailBean.setSmName(scServiceDetail.getSmName());
		scServiceDetailBean.setSourceCity(scServiceDetail.getSourceCity());
		scServiceDetailBean.setSourceCountry(scServiceDetail.getSourceCountry());
		scServiceDetailBean.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		scServiceDetailBean.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		scServiceDetailBean.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		scServiceDetailBean.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
		scServiceDetailBean.setTpsCopfId(scServiceDetail.getTpsCopfId());
		scServiceDetailBean.setTpsServiceId(scServiceDetail.getTpsServiceId());
		scServiceDetailBean.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
		scServiceDetailBean.setUpdatedDate(scServiceDetail.getUpdatedDate());
		scServiceDetailBean.setUuid(scServiceDetail.getUuid());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnName());
		scServiceDetailBean.setSourceAddressLineTwo(scServiceDetail.getSourceAddressLineTwo());
		scServiceDetailBean.setSourceLocality(scServiceDetail.getSourceLocality());
		scServiceDetailBean.setSourcePincode(scServiceDetail.getSourcePincode());
		scServiceDetailBean.setDestinationAddressLineOne(scServiceDetail.getDestinationAddressLineOne());
		scServiceDetailBean.setDestinationAddressLineTwo(scServiceDetail.getDestinationAddressLineTwo());
		scServiceDetailBean.setDestinationLocality(scServiceDetail.getDestinationLocality());
		scServiceDetailBean.setDestinationPincode(scServiceDetail.getDestinationPincode());
		scServiceDetailBean.setSourceState(scServiceDetail.getSourceState());
		scServiceDetailBean.setDestinationState(scServiceDetail.getDestinationState());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnSolutionId());
		scServiceDetailBean.setDemarcationApartment(scServiceDetail.getDemarcationBuildingName());
		scServiceDetailBean.setBurstableBwPortspeed(scServiceDetail.getBurstableBw());
		scServiceDetailBean.setBurstableBwUnit(scServiceDetail.getBurstableBwUnit());
		scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileScenario());
		scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileConnectionType());
		scServiceDetailBean.setOrderType(scServiceDetail.getOrderType());
		scServiceDetailBean.setOrderCategory(scServiceDetail.getOrderCategory());
		for(ScServiceAttribute scServiceAttribute:scServiceDetail.getScServiceAttributes()) {
			scServiceDetailBean.getScServiceAttributes().add(mapServiceDetailAttrEntityToBean(scServiceAttribute));
		}
		
		for(ScAttachment scAttachment:scServiceDetail.getScAttachments()) {
			scServiceDetailBean.getScAttachments().add(mapServiceAttachmentEntityToBean(scAttachment));
		}
		
		for(ScProductDetail scProductDetail:scServiceDetail.getScProductDetail()) {
			scServiceDetailBean.getScProductDetail().add(mapServiceProductEntityToBean(scProductDetail));
		}
		return scServiceDetailBean;
	}

}
