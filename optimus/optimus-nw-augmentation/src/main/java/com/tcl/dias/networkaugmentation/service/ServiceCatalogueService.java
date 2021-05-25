package com.tcl.dias.networkaugmentation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.CustomerInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.OrderInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
import com.tcl.dias.common.servicefulfillment.beans.ServiceInfoBean;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScContractInfo;
import com.tcl.dias.networkaugment.entity.entities.ScOrder;
import com.tcl.dias.networkaugment.entity.entities.ScOrderAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
//import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
//import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;

/**
 * 
 * This file contains the ServiceCatalogueService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ServiceCatalogueService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCatalogueService.class);

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
//	@Autowired
//	CommonFulfillmentUtils commonFulfillmentUtils;
//
//	@org.springframework.transaction.annotation.Transactional
//	public ServiceFulfillmentRequest processServiceFulFillmentData(Integer serviceId) {
//		ServiceFulfillmentRequest serviceFulFillmentRequest = new ServiceFulfillmentRequest();
//		try {
//			// primary Service Info
//			Optional<ScServiceDetail> serviceDetailEntity = scServiceDetailRepository.findById(serviceId);
//			if (serviceDetailEntity.isPresent()) {
//				ScServiceDetail scServiceDetail = serviceDetailEntity.get();
//				
//				Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
//								scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A"); 
//				serviceFulFillmentRequest.setPrimaryServiceInfo(extractServiceInfo(scServiceDetail,scComponentAttributesAMap));
//				serviceFulFillmentRequest.setProductName(scServiceDetail.getErfPrdCatalogProductName());
//				serviceFulFillmentRequest.setOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
//				// secondary service info
//				if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
//					ScServiceDetail secServiceDetailEntity = scServiceDetailRepository
//							.findFirstByErfOdrServiceIdOrderByIdDesc(scServiceDetail.getErdPriSecServiceLinkId());
//					
//					Map<String, String> scComponentAttributesAMaPricSec =commonFulfillmentUtils.getComponentAttributes(
//	    							secServiceDetailEntity.getId(), AttributeConstants.COMPONENT_LM, "A");
//					if (secServiceDetailEntity != null) {
//						serviceFulFillmentRequest.setSecondaryServiceInfo(extractServiceInfo(secServiceDetailEntity,scComponentAttributesAMaPricSec));
//					}
//				}
//				Optional<ScOrder> scOrderEntity = scOrderRepository.findById(scServiceDetail.getScOrder().getId());
//				if (scOrderEntity.isPresent()) {
//					List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrderEntity.get().getId());
//					// Order info
//					for (ScContractInfo scContractInfo : scContractInfos) {
//						serviceFulFillmentRequest.setOrderInfo(extractOrderInfo(scOrderEntity.get(), scContractInfo));
//						// Customer Info
//						serviceFulFillmentRequest
//								.setCustomerInfo(extractCustomerInfo(scOrderEntity.get(), scContractInfo));
//					}
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error in proccessing fulfillment ", e);
//		}
//		return serviceFulFillmentRequest;
//
//	}
//
//	/**
//	 * 
//	 * extractCustomerInfo
//	 * 
//	 * @param scOrder
//	 * @param scContractInfo
//	 * @return
//	 */
//	private CustomerInfoBean extractCustomerInfo(ScOrder scOrder, ScContractInfo scContractInfo) {
//		CustomerInfoBean customerInfo = new CustomerInfoBean();
//		customerInfo.setAccountManager(scContractInfo.getAccountManager());
//		customerInfo.setAccountManagerEmail(scContractInfo.getAccountManagerEmail());
//		customerInfo.setBillingAddress(scContractInfo.getBillingAddress());
//		customerInfo.setBillingFrequency(scContractInfo.getBillingFrequency());
//		customerInfo.setBillingMethod(scContractInfo.getBillingMethod());
//		customerInfo.setCusomerContactEmail(scContractInfo.getCustomerContactEmail());
//		customerInfo.setCustomerContact(scContractInfo.getCustomerContact());
//		customerInfo.setCustomerId(
//				scOrder.getErfCustCustomerId() != null ? String.valueOf(scOrder.getErfCustCustomerId()) : null);
//		customerInfo.setCustomerLeId(scOrder.getErfCustLeId());
//		customerInfo.setCustomerLeName(scOrder.getErfCustLeName());
//		customerInfo.setCustomerName(scOrder.getErfCustCustomerName());
//		customerInfo.setPaymentTerm(scContractInfo.getPaymentTerm());
//		customerInfo.setSfdcAccountId(scOrder.getSfdcAccountId());
//		customerInfo.setSfdcCuid(scContractInfo.getTpsSfdcCuid());
//		customerInfo.setSupplierId(scContractInfo.getErfCustSpLeId());
//		customerInfo.setSupplierName(scContractInfo.getErfCustSpLeName());
//		return customerInfo;
//	}
//
//	/**
//	 * 
//	 * extractOrderInfo
//	 * 
//	 * @param scOrder
//	 * @param scContractInfo
//	 * @return
//	 */
//	private OrderInfoBean extractOrderInfo(ScOrder scOrder, ScContractInfo scContractInfo) {
//		OrderInfoBean orderInfoBean = new OrderInfoBean();
//		orderInfoBean.setContractEndDate(scContractInfo.getContractEndDate());
//		orderInfoBean.setContractStartDate(scContractInfo.getContractStartDate());
//		orderInfoBean.setIsBundleOrder(scOrder.getIsBundleOrder());
//		orderInfoBean.setIsMultipleLe(scOrder.getIsMultipleLe());
//		orderInfoBean.setLastMacdDate(scOrder.getLastMacdDate());
//		orderInfoBean.setOptimusOrderCode(scOrder.getOpOrderCode());
//		orderInfoBean.setOrderCreatedDate(scOrder.getOrderStartDate());
//		orderInfoBean.setOptyClassification(scOrder.getOpportunityClassification());
//		List<ScOrderAttribute> scOrderAttrs = scOrderAttributeRepository.findByScOrderAndIsActive(scOrder,
//				CommonConstants.Y);
//		Map<String, String> orderAttr = new HashMap<>();
//		for (ScOrderAttribute scOrderAttribute : scOrderAttrs) {
//			orderAttr.put(scOrderAttribute.getAttributeName(), scOrderAttribute.getAttributeValue());
//		}
//		orderInfoBean.setOrderAttributes(orderAttr);
//		orderInfoBean.setOrderCategory(scOrder.getOrderCategory());
//		orderInfoBean.setOrderSource(scOrder.getOrderSource());
//		orderInfoBean.setOrderTermsInMonth(scContractInfo.getOrderTermInMonths());
//		orderInfoBean.setOrderType(scOrder.getOrderType());
//		orderInfoBean.setParentId(scOrder.getParentId());
//		orderInfoBean.setParentOptimusOrderCode(scOrder.getParentOpOrderCode());
//		orderInfoBean.setScOrderId(scOrder.getId());
//		orderInfoBean.setOrderCreatedBy(scOrder.getCreatedBy());
//		return orderInfoBean;
//	}
//
//	/**
//	 * 
//	 * extractServiceInfo
//	 * 
//	 * @param scServiceDetail
//	 * @param scComponentAttributesAMap 
//	 * @return
//	 */
//	private ServiceInfoBean extractServiceInfo(ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
//		ServiceInfoBean srviceInfo = new ServiceInfoBean();
//		srviceInfo.setArc(scServiceDetail.getArc());
//		srviceInfo.setBillingAccountId(scServiceDetail.getBillingAccountId());
//		srviceInfo.setBwPortSpeed(scComponentAttributesAMap.get("portBandwidth"));
//		srviceInfo.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
//		srviceInfo.setBwUnit(scComponentAttributesAMap.get("bwUnit"));
//		srviceInfo.setCopfId(scServiceDetail.getTpsCopfId());
//		srviceInfo.setCreatedTime(scServiceDetail.getCreatedDate());
//		srviceInfo.setDemarcationFloor(scComponentAttributesAMap.get("demarcationFloor"));
//		srviceInfo.setDemarcationRack(scComponentAttributesAMap.get("demarcationWing"));
//		srviceInfo.setDemarcationRoom(scComponentAttributesAMap.get("demarcationRoom"));
//		srviceInfo.setFeasibilityId(scComponentAttributesAMap.get("feasibilityId"));
//		srviceInfo.setLastMileBw(scComponentAttributesAMap.get("localLoopBandwidth"));
//		srviceInfo.setLastMileBwUnit(scComponentAttributesAMap.get("localLoopBandwidthUnit"));
//		srviceInfo.setLastMileProvider(scComponentAttributesAMap.get("lastMileProvider"));
//		srviceInfo.setDestinationAddressLineOne(scComponentAttributesAMap.get("destinationAddressLineOne"));
//		srviceInfo.setDestinationAddressLineTwo(scComponentAttributesAMap.get("destinationAddressLineTwo"));
//		srviceInfo.setDestinationLocality(scComponentAttributesAMap.get("destinationLocality"));
//		srviceInfo.setDestinationPincode(scComponentAttributesAMap.get("destinationPincode"));
//		srviceInfo.setDestinationCity(scComponentAttributesAMap.get("destinationCity"));
//		srviceInfo.setDestinationCountry(scComponentAttributesAMap.get("destinationCountry"));
//		srviceInfo.setSourceAddressLineOne(scComponentAttributesAMap.get("sourceAddressLineOne"));
//		srviceInfo.setSourceAddressLineTwo(scComponentAttributesAMap.get("sourceAddressLineTwo"));
//		srviceInfo.setSourceLocality(scComponentAttributesAMap.get("sourceLocality"));
//		srviceInfo.setSourcePincode(scComponentAttributesAMap.get("sourcePincode"));
//		srviceInfo.setSourceCity(scComponentAttributesAMap.get("portBandwidth"));
//		srviceInfo.setDestinationState(scComponentAttributesAMap.get("destinationState"));
//		srviceInfo.setSourceState(scComponentAttributesAMap.get("sourceState"));
//		srviceInfo.setSourceCountry(scComponentAttributesAMap.get("sourceCountry"));
//		srviceInfo.setLastMileType(scComponentAttributesAMap.get("lmType"));
//		srviceInfo.setLocalItContactEmailId(scComponentAttributesAMap.get("portBandwidth"));
//		srviceInfo.setLocalItContactMobileNo(scComponentAttributesAMap.get("portBandwidth"));
//		srviceInfo.setLocalItContactName(scComponentAttributesAMap.get("portBandwidth"));
//		srviceInfo.setLocationId(scServiceDetail.getErfLocSiteAddressId());
//		srviceInfo.setNrc(scServiceDetail.getNrc());
//		srviceInfo.setCity(scComponentAttributesAMap.get("portBandwidth"));
//		
//		String taxExemptionFlag=scComponentAttributesAMap.get("taxExemptionFlag");
//		if (taxExemptionFlag != null)
//			srviceInfo.setIsTaxAvailable(taxExemptionFlag);
//		srviceInfo.setPopLocationId(scServiceDetail.getErfLocPopSiteAddressId());
//		srviceInfo.setPopSiteAddress(scComponentAttributesAMap.get("popSiteAddress") );
//		srviceInfo.setServiceCommisionDate(scServiceDetail.getServiceCommissionedDate());
//		List<String> notInCategories=new ArrayList<String>();
//		notInCategories.add("FEASIBILITY");
//		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
//				.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(), CommonConstants.Y,notInCategories);
//		Map<String, String> serviceAttr = new HashMap<>();
//		Map<String, String> feasibilityAttr = new HashMap<>();
//		for (ScServiceAttribute scServiceAttribute : scServiceAttributes) {
//			serviceAttr.put(scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
//		}
//
//		List<ScServiceAttribute> feasibilityAttributes = scServiceAttributeRepository
//				.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
//						"FEASIBILITY");
//		for (ScServiceAttribute feasibilityAttribute : feasibilityAttributes) {
//			feasibilityAttr.put(feasibilityAttribute.getAttributeName(), feasibilityAttribute.getAttributeValue());
//		}
//		srviceInfo.setFeasibilityAttributes(feasibilityAttr);
//		srviceInfo.setServiceDetailsAttributes(serviceAttr);
//		srviceInfo.setServiceId(scServiceDetail.getId());
//		srviceInfo.setServiceCode(scServiceDetail.getUuid());
//		srviceInfo.setServiceOption(scComponentAttributesAMap.get("cpeManagementType"));
//		srviceInfo.setSiteAddress(scServiceDetail.getSiteAddress());
//		srviceInfo.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
//		return srviceInfo;
//	}

}
