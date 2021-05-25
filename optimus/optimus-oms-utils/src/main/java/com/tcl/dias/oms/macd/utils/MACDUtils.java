package com.tcl.dias.oms.macd.utils;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.ContractTermBean;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.macd.constants.MACDConstants;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the MACDUtils.java class. This class contains MACD utils
 * functionalities
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class MACDUtils {

	@Value("${rabbitmq.si.order.get.queue}")
	String getSIOrderQueue;

	@Value("${rabbitmq.si.order.get.service.queue}")
	String getSIOrderServiceIdQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MacdDetailRepository macdDetailRepository;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

	@Value("${rabbitmq.si.order.add.site.queue}")
	String siOrderDetailsAddSiteQueue;

	@Value("${rabbitmq.si.npl.details.queue}")
	String siNplServiceDetailsQueue;

	@Value("${rabbitmq.si.order.details.primary.secondary.queue}")
	String siPriSecOrderDetailsQueue;

	@Value("${rabbitmq.si.order.add.site.pri.sec.queue}")
	String siOrderDetailsAddSitePriSecQueue;
	
	
	@Value("${rabbitmq.si.nde.attributes.details.queue}")
	String siServiceDetailsNdeQueue;
	
	@Value("${rabbitmq.cpe.bom.product.catalog.queue}")
	String cpeBomPrdCatalogQueue;
	
	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Value("${rabbitmq.si.service.underprov.queue}")
	String underProvServiceDetail;
	
	@Value("${rabbitmq.si.order.details.inactive.queue}")
	String inactiveServiceDetialsQueue;
	
	@Value("${rabbitmq.si.npl.details.inactive.queue}")
	String inactiveServiceDetailsNplQueue;

	@Value("${rabbitmq.si.price.revision.detail.queue}")
	String priceRevisionDetailQueue;

	private static final Logger LOGGER = LoggerFactory.getLogger(MACDUtils.class);

	/**
	 * Method to get siOrderData based on siOrderId
	 *
	 * @param siOrderId
	 * @return
	 * @throws TclCommonException
	 */
	public SIOrderDataBean getSiOrderData(String siOrderId) throws TclCommonException {
		SIOrderDataBean siOrderData = null;
		if (StringUtils.isNotBlank(siOrderId)) {
			SIGetOrderRequest getOrderRequest = new SIGetOrderRequest();
			getOrderRequest.setOrderId(siOrderId);
			String requestPayload = Utils.toJson(getOrderRequest);
			LOGGER.info("Sending request to getSIOrder:: {}", requestPayload);
			String response = (String) mqUtils.sendAndReceive(getSIOrderQueue, requestPayload);
			LOGGER.info("Received response from getSIOrder:: {}", response);
			SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
			if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
				siOrderData = orderResponse.getOrder();
			} else {
				LOGGER.error("Error in retrieving getSIOrder:: {}", orderResponse.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
			}
		}
		return siOrderData;
	}

	/**
	 * Method to get service Inventory detail based on service detail Id from
	 * siOrderData
	 *
	 * @param siOrder
	 * @param serviceDetailId
	 * @return
	 */
	public SIServiceDetailDataBean getSiServiceDetailBean(SIOrderDataBean siOrder, Integer serviceDetailId) {
		SIServiceDetailDataBean[] serviceDetailArray = { null };
		if (Objects.nonNull(siOrder) && Objects.nonNull(serviceDetailId)) {
			siOrder.getServiceDetails().stream().forEach(serviceDetailData -> {
				if (serviceDetailData.getId().equals(serviceDetailId)) {
					serviceDetailArray[0] = serviceDetailData;
					return;
				}

			});
		}

		return serviceDetailArray[0];
	}

	/**
	 * Method to get service Inventory detail based on service Id from siOrderData
	 *
	 * @param siOrder
	 * @param serviceId
	 * @return
	 */
	public SIServiceDetailDataBean getSiServiceDetailBeanBasedOnServiceId(SIOrderDataBean siOrder, String serviceId) {
		SIServiceDetailDataBean[] serviceDetailArray = { null };
		if (Objects.nonNull(siOrder) && Objects.nonNull(serviceId)) {
			siOrder.getServiceDetails().stream().forEach(serviceDetailData -> {
				if (serviceDetailData.getTpsServiceId().equals(serviceId)) {
					serviceDetailArray[0] = serviceDetailData;
					return;
				}

			});
		}

		return serviceDetailArray[0];
	}

	/**
	 * Method to get macd initiated status
	 *
	 * @param serviceIds
	 * @return
	 */
	public Map<String, Object> getMacdInitiatedStatus(Map<String, String> serviceIds) {
		Map<String, Object> macdStatus = new HashMap<>();
		if (Objects.nonNull(serviceIds)) {
			serviceIds.entrySet().forEach(entry -> {
				String serviceId1 = entry.getKey();
				String serviceId2 = entry.getValue();
				LOGGER.info("Processing serviceId 1 {} and serviceId 2 {}",serviceId1,serviceId2);
				MacdDetail macdDetail = macdDetailRepository.findByTpsServiceIdAndStageAndOrderCategoryNot(serviceId1,
						MACDConstants.MACD_ORDER_INITIATED, MACDConstants.ADD_SITE_SERVICE);
				LOGGER.info("Fetching the macddetails (add-site-service) for serviceId 1 {}",serviceId1);
				if (Objects.nonNull(macdDetail)) {
					macdStatus.put(serviceId1, true);
				} else {
					LOGGER.info("Checking ,since add service is null for serviceId1 {} ",serviceId1);
					MacdDetail macdDetailEntry = macdDetailRepository.findByTpsServiceIdAndStageAndOrderType(serviceId1, MACDConstants.MACD_ORDER_INITIATED,  MACDConstants.TERMINATION);
					LOGGER.info("Fetching the macddetails (termination) for serviceId 1 {}",serviceId1);
					if(Objects.nonNull(macdDetailEntry)){
						macdStatus.put(serviceId1, true);
					} else {
						macdStatus.put(serviceId1, false);
					}
				}

				if (!serviceId2.equalsIgnoreCase("NIL")) {
					MacdDetail macdDetail2 = macdDetailRepository.findByTpsServiceIdAndStageAndOrderCategoryNot(
							serviceId2, MACDConstants.MACD_ORDER_INITIATED, MACDConstants.ADD_SITE_SERVICE);
					LOGGER.info("Fetching the macddetails (add-site-service)  for serviceId 2 {}",serviceId2);
					if (Objects.nonNull(macdDetail2)) {
						macdStatus.put(serviceId2, true);
					} else {
						LOGGER.info("Checking ,since add service is null for serviceId2 {} ",serviceId2);
						MacdDetail macdDetailEntry = macdDetailRepository.findByTpsServiceIdAndStageAndOrderType(serviceId2, MACDConstants.MACD_ORDER_INITIATED, MACDConstants.TERMINATION);
						LOGGER.info("Fetching the macddetails (termination)  for serviceId 2 {}",serviceId2);
						if(Objects.nonNull(macdDetailEntry)){
							macdStatus.put(serviceId2, true);
						} else {
							macdStatus.put(serviceId2, false);
						}
					}
				}
				LOGGER.info("Processed serviceId 1 {} and serviceId 2 {}",serviceId1,serviceId2);
			});
		}
		return macdStatus;
	}
	
	
	/**
	 * Method to get macd initiated status
	 *
	 * @param serviceIds
	 * @return
	 */
	public Map<String, Object> getIPCMacdInitiatedStatus(List<String> serviceIds) {
		Map<String, Object> macdStatus = new HashMap<>();
		if (!Objects.isNull(serviceIds) && !CollectionUtils.isEmpty(serviceIds)){
			List<String> ipcOrderCategoryList = new ArrayList<>();
			ipcOrderCategoryList.add(MACDConstants.ADD_CLOUDVM_SERVICE);
			ipcOrderCategoryList.add(MACDConstants.CONNECTIVITY_UPGRADE_SERVICE);
			ipcOrderCategoryList.add(MACDConstants.ADDITIONAL_SERVICE_UPGRADE);
			ipcOrderCategoryList.add(MACDConstants.REQUEST_FOR_TERMINATION_SERVICE);
			ipcOrderCategoryList.add(MACDConstants.UPGRADE_VM_SERVICE);
			ipcOrderCategoryList.add(MACDConstants.DELETE_VM_SERVICE);
			List<MacdDetail> macdDetailList = macdDetailRepository.findByTpsServiceIdInAndOrderCategoryInAndStageNotInAndIsActiveNotIn(serviceIds,ipcOrderCategoryList,MACDConstants.MACD_ORDER_COMMISSIONED,CommonConstants.BDEACTIVATE);
			for(MacdDetail macdDetail:macdDetailList){
				LOGGER.info("ServiceId:: {}", macdDetail.getTpsServiceId());
				macdStatus.put(macdDetail.getTpsServiceId(), true);
			}
			//Condition for disabling new orders which is In progress
			List<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository.findByUuidInAndServiceStatusNotIn(serviceIds, MACDConstants.COMPLETED);
			for(OdrServiceDetail odrServiceDetail:odrServiceDetails){
				LOGGER.info("ServiceId:: {}", odrServiceDetail.getUuid());
				macdStatus.put(odrServiceDetail.getUuid(), true);
			}
		}
		return macdStatus;
	}

	/**
	 * Method to get related serviceIds
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, String> getRelatedServiceIds(String serviceId) throws TclCommonException {
		Map<String, String> serviceIds = new HashMap<>();
		SIServiceInfoBean[] siDetailedInfoResponse = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;

		String queueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, serviceId);

		if (StringUtils.isNotBlank(queueResponse)) {
			siDetailedInfoResponse = (SIServiceInfoBean[]) Utils.convertJsonToObject(queueResponse,
					SIServiceInfoBean[].class);
			siServiceInfoResponse = Arrays.asList(siDetailedInfoResponse);

			siServiceInfoResponse.stream().forEach(detailedInfo -> {
				if (!detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
						&& !detailedInfo.getTpsServiceId().equalsIgnoreCase(serviceId)) {
					serviceIds.put(serviceId, detailedInfo.getTpsServiceId());
				} else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
					serviceIds.put(serviceId, "NIL");
			});
		}
		return serviceIds;
	}

	public void updateO2CMACDOrder(String orderCode, String dcLocationCode) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(orderCode);
		quoteToLes.stream().findFirst().ifPresent( quoteToLe -> {
			MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
			LOGGER.info("O2C Macd detail bean : {}.", macdDetail); 
			if (Objects.nonNull(macdDetail)) {
				macdDetail.setStage(MACDConstants.MACD_ORDER_COMMISSIONED);
				macdDetailRepository.save(macdDetail);
				LOGGER.info("Macd detail bean has been updated: {}.", macdDetail);
			}
			if(orderCode.contains("IPC")) {
				OdrServiceDetail odrServiceDetail = odrServiceDetailRepository.findByOdrOrderUuidAndPopSiteCode(orderCode, dcLocationCode);
				LOGGER.info("odrServiceDetail bean : {}.", odrServiceDetail); 
				if (Objects.nonNull(odrServiceDetail)) {
					odrServiceDetail.setServiceStatus(MACDConstants.COMPLETED);
					odrServiceDetailRepository.save(odrServiceDetail);
					LOGGER.info("odrServiceDetail bean has been updated: {}.", odrServiceDetail);
				}
			}
		});
	}


	/**
	 * Method to get list of service Ids
	 *
	 * @param quoteToLe
	 * @return
	 */
	public List<String> getServiceIdListBasedOnQuoteToLe(QuoteToLe quoteToLe)
	{
		List<String> serviceIds=new ArrayList<>();
		List<String> serviceIdsToRemove = new ArrayList<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		boolean isUnique=isServiceIdsUnique(quoteIllSiteToServices);
		if(!isUnique) {
			QuoteIllSiteToService quoteIllSiteToService = quoteIllSiteToServices.stream().findFirst().get();
			quoteIllSiteToServices.clear();
			quoteIllSiteToServices.add(quoteIllSiteToService);
		}
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				if(!(MACDConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType()) && CommonConstants.ACTIVE.equals(quoteIllSiteToService.getIsDeleted()))) {
					serviceIds.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
				}
			});
		}
		LOGGER.info("serviceIds"+serviceIds);
		serviceIds.stream().forEach(serviceId->{
			try {
				Map<String, String> relatedServiceIds = getRelatedServiceIds(serviceId);
				String relatedServiceId=relatedServiceIds.get(serviceId);
				if(relatedServiceIds != null&&!serviceIdsToRemove.contains(serviceId))
					serviceIdsToRemove.add(relatedServiceId);
				LOGGER.info("serviceIdsToRemove"+serviceIdsToRemove);
			}

			catch(Exception e)
			{
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
						ResponseResource.R_CODE_ERROR);
			}
		});

		if(!serviceIdsToRemove.isEmpty())
			serviceIds.removeAll(serviceIdsToRemove);

		LOGGER.info("serviceIdsAFterRemove"+serviceIds);
		if(serviceIds.isEmpty())
		{
			serviceIds.add(quoteToLe.getErfServiceInventoryTpsServiceId());
		}

		return serviceIds;
	}
	
	

	/**
	 * Method to get list of service Ids
	 *
	 * @param quoteToLe
	 * @return
	 */
	public List<String> getServiceIds(QuoteToLe quoteToLe)
	{
		List<String> serviceIds=new ArrayList<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		boolean isUnique=isServiceIdsUnique(quoteIllSiteToServices);
		if(!isUnique) {
			QuoteIllSiteToService quoteIllSiteToService = quoteIllSiteToServices.stream().findFirst().get();
			quoteIllSiteToServices.clear();
			quoteIllSiteToServices.add(quoteIllSiteToService);
		}
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				if(!(MACDConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType()) && CommonConstants.ACTIVE.equals(quoteIllSiteToService.getIsDeleted()))) {
					serviceIds.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
				}
			});
		}

		if(serviceIds.isEmpty())
		{
			serviceIds.add(quoteToLe.getErfServiceInventoryTpsServiceId());
		}
		LOGGER.info("Service Ids in getServiceIds method {}",serviceIds);
		return serviceIds;
	}

	/**
	 * Method to get service detail based on service Id
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailDataBean getServiceDetail(String serviceId, String quoteCategory)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		if(quoteCategory != null && !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteCategory)) {
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceId);
		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		} else {
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsAddSiteQueue, serviceId);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			
		}
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		

		SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBean(serviceDetail, quoteCategory);

		return serviceDetailDataBean;
	}
	/**
	 * Method to return serviceDetailBean for primary and secondary
	 * @param serviceId
	 * @return
	 */

	public HashMap<String, SIServiceDetailDataBean> getPrimarySecondaryServiceDetail(String serviceId, String quoteCategory)throws TclCommonException
	{

		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = new ArrayList<>();
		Set<SIServiceDetailsBean> serviceDetailsBeanSet = null;
		LOGGER.info("Inside getPrimarySecondaryServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		if(quoteCategory != null && !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteCategory)) {
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siPriSecOrderDetailsQueue, serviceId);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		} else {
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsAddSitePriSecQueue, serviceId);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			serviceDetailsList = Arrays.asList(serviceDetailBeanArray);

		}
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);

		HashMap<String , SIServiceDetailDataBean> serviceDetailDataHashMap = new HashMap<>();

		if (Objects.nonNull(serviceDetailsList))
		{
			serviceDetailsList.stream().forEach(siServiceDetailsBean ->
			{
				if (siServiceDetailsBean.getLinkType().equalsIgnoreCase(CommonConstants.PRIMARY))
				{
					SIServiceDetailDataBean primaryServiceDetailDataBean=mapServiceDetailBean(siServiceDetailsBean, quoteCategory);
					serviceDetailDataHashMap.put(PDFConstants.PRIMARY,primaryServiceDetailDataBean);
					LOGGER.info("Value of nrc in primary serviceDetailDataHashMap {}" ,serviceDetailDataHashMap.get(PDFConstants.PRIMARY).getNrc());
				}

				else if (siServiceDetailsBean.getLinkType().equalsIgnoreCase(CommonConstants.SECONDARY))
				{
					SIServiceDetailDataBean secondaryServiceDetailDataBean=mapServiceDetailBean(siServiceDetailsBean, quoteCategory);
					serviceDetailDataHashMap.put(PDFConstants.SECONDARY,secondaryServiceDetailDataBean);
					LOGGER.info("Value of nrc in secondary serviceDetailDataHashMap {}" ,serviceDetailDataHashMap.get(PDFConstants.SECONDARY).getNrc());
				}
				else
				{
					SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBean(siServiceDetailsBean, quoteCategory);
					serviceDetailDataHashMap.put(PDFConstants.PRIMARY,serviceDetailDataBean);
					LOGGER.info("Value of nrc in primary serviceDetailDataHashMap {}" ,serviceDetailDataHashMap.get(PDFConstants.PRIMARY).getNrc());
				}
			});
		}


		return serviceDetailDataHashMap;
	}

		/**
		 * Method to map serviceDetailBean
		 * @param siServiceDetail
		 * @return
		 */
	private SIServiceDetailDataBean mapServiceDetailBean(SIServiceDetailsBean siServiceDetail, String quoteCategory)
	{
		LOGGER.info("Inside mapServiceDetailBean to construct SIServiceDetailDataBean for serviceid {} ", siServiceDetail.getTpsServiceId());
		SIServiceDetailDataBean serviceDetail=new SIServiceDetailDataBean();
		List<SIAttributeBean> serviceAttributesList = new ArrayList<>();
		serviceDetail.setAccessType(siServiceDetail.getAccessType());
		serviceDetail.setAccessProvider(siServiceDetail.getAccessProvider());
		serviceDetail.setArc(siServiceDetail.getArc()!= null?siServiceDetail.getArc():0);
		serviceDetail.setBillingFrequency(siServiceDetail.getBillingFrequency());
		serviceDetail.setBillingMethod(siServiceDetail.getBillingMethod());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		serviceDetail.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
		serviceDetail.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
		serviceDetail.setErfPrdCatalogProductName(siServiceDetail.getErfPrdCatalogProductName());
		serviceDetail.setLastmileBw(siServiceDetail.getLastmileBw());
		serviceDetail.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
		serviceDetail.setPortBw(siServiceDetail.getPortBw());
		serviceDetail.setPortBwUnit(siServiceDetail.getPortBwUnit());
		if(siServiceDetail.getLinkType() != null)
				serviceDetail.setLinkType(siServiceDetail.getLinkType().toLowerCase());
		serviceDetail.setTpsServiceId(siServiceDetail.getTpsServiceId());
		serviceDetail.setPriSecServLink(siServiceDetail.getPriSecServLink());
		serviceDetail.setNrc(siServiceDetail.getNrc()!=null?siServiceDetail.getNrc():0);
		serviceDetail.setReferenceOrderId(Objects.nonNull(siServiceDetail.getReferenceOrderId())?Integer.toString(siServiceDetail.getReferenceOrderId()):null);
		serviceDetail.setVpnName(siServiceDetail.getVpnName());
		LOGGER.info("mapServiceDetailBean locationId : {} for the serviceId : quoteCategory {}",siServiceDetail.getErfLocSiteAddressId(),siServiceDetail.getTpsServiceId()+quoteCategory);

		if(!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteCategory) && (siServiceDetail.getErfLocSiteAddressId() != null))
			serviceDetail.setErfLocSiteAddressId(Integer.toString(siServiceDetail.getErfLocSiteAddressId()));

		serviceDetail.setSiteAddress(siServiceDetail.getSiteAddress());
		serviceDetail.setLatLong(siServiceDetail.getLatLong());
		serviceDetail.setId(siServiceDetail.getId());
		LOGGER.info("Mrc inventory value "+siServiceDetail.getMrc());
		serviceDetail.setMrc(siServiceDetail.getMrc()!=null?siServiceDetail.getMrc():0);
		serviceDetail.setArc(siServiceDetail.getArc()!=null?siServiceDetail.getArc():0);
		serviceDetail.setBillingType(siServiceDetail.getBillingType());
		serviceDetail.setContractStartDate(siServiceDetail.getContractStartDate());
		serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		serviceDetail.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
		serviceDetail.setDemarcationRoom(siServiceDetail.getDemarcationRoom());
		serviceDetail.setDemarcationFloor(siServiceDetail.getDemarcationFloor());
		serviceDetail.setDemarcationApartment(siServiceDetail.getDemarcationApartment());
		serviceDetail.setDemarcationRack(siServiceDetail.getDemarcationRack());
		serviceDetail.setLmType(siServiceDetail.getLastMileType());
		serviceDetail.setTpsCopfId(siServiceDetail.getTpsCopfId());
		serviceDetail.setTpsSfdcCuId(siServiceDetail.getTpsSfdcCuid());
		serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
		serviceDetail.setOrderCategory(siServiceDetail.getOrderCategory());
		serviceDetail.setCircuitExpiryDate(siServiceDetail.getCircuitExpiryDate());
		serviceDetail.setSourceCity(siServiceDetail.getSourceCity());
		serviceDetail.setDestinationCity(siServiceDetail.getDestinationCity());
		serviceDetail.setAccountManager(siServiceDetail.getAccountManager());
		serviceDetail.setSiteEndInterface(siServiceDetail.getSiteEndInterface());
		serviceDetail.setLastMileProvider(siServiceDetail.getAccessProvider());
		if(Objects.nonNull(siServiceDetail.getAssetAttributes()) && !siServiceDetail.getAssetAttributes().isEmpty()) {
			siServiceDetail.getAssetAttributes().stream().forEach(serviceAttribute -> {
				LOGGER.info("Si Service Atrribute bean is ---> {} ", serviceAttribute);
				SIAttributeBean siAttribute = new SIAttributeBean();
				siAttribute.setName(serviceAttribute.getAttributeName());
				siAttribute.setValue(serviceAttribute.getAttributeValue());
				serviceAttributesList.add(siAttribute);
				
			});
			serviceDetail.setAttributes(serviceAttributesList);
		}
		
		
		serviceDetail.setComponents(siServiceDetail.getComponentBean());
		
		//added for gvpn multi vrf macd
		serviceDetail.setTotalVrfBandwith(siServiceDetail.getTotalVrfBandwith());

		serviceDetail.setOrderCode(siServiceDetail.getOrderCode());
		serviceDetail.setPortMode(siServiceDetail.getPortMode());
		serviceDetail.setBillingCurrency(siServiceDetail.getBillingCurrency());
		serviceDetail.setsCommisionDate(siServiceDetail.getsCommisionDate());
		serviceDetail.setCurrentOpportunityType(siServiceDetail.getCurrentOpportunityType());
		serviceDetail.setIpv4AddressPoolsize(siServiceDetail.getIpv4AddressPoolsize());
		LOGGER.info("Service detail data bean for service ID ---> is ----> {} ", serviceDetail.getTpsServiceId(), serviceDetail);
		LOGGER.info("To be mapped Service detail data bean for service ID ---> {} is ----> {} and origin si service detail bean is ---> {}  ", serviceDetail.getTpsServiceId(), serviceDetail, siServiceDetail);
		return serviceDetail;
	}

	/**
	 * Method to get list of service Ids based on orderToLe
	 *
	 * @param orderToLe
	 * @return
	 */
	public List<String> getServiceIdListBasedOnOrderToLe(OrderToLe orderToLe)
	{
		List<String> serviceIds=new ArrayList<>();
		List<String> serviceIdsToRemove = new ArrayList<>();
		List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
		boolean isUnique=isServiceIdsUniqueForOrder(orderIllSiteToServices);
		if(!isUnique) {
			OrderIllSiteToService orderIllSiteToService = orderIllSiteToServices.stream().findFirst().get();
			orderIllSiteToServices.clear();
			orderIllSiteToServices.add(orderIllSiteToService);
		}
		if(!orderIllSiteToServices.isEmpty()) {
			orderIllSiteToServices.stream().forEach(orderIllSiteToService -> {
				serviceIds.add(orderIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}
		LOGGER.info("serviceIds"+serviceIds);
		serviceIds.stream().forEach(serviceId->{
			try {
				Map<String, String> relatedServiceIds = getRelatedServiceIds(serviceId);
				String relatedServiceId=relatedServiceIds.get(serviceId);
				if(relatedServiceIds != null&&!serviceIdsToRemove.contains(serviceId))
					serviceIdsToRemove.add(relatedServiceId);
				LOGGER.info("serviceIdsToRemove"+serviceIdsToRemove);
			}

			catch(Exception e)
			{
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
						ResponseResource.R_CODE_ERROR);
			}
		});

		if(!serviceIdsToRemove.isEmpty())
			serviceIds.removeAll(serviceIdsToRemove);

		LOGGER.info("serviceIdsAFterRemove"+serviceIds);

		QuoteToLe quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
		if(serviceIds.isEmpty())
		{
			serviceIds.add(quoteToLe.getErfServiceInventoryTpsServiceId());
		}

		return serviceIds;
	}

	/**
	 * Method to get serviceID based on quoteIllSite
	 * @param quoteIllSite
	 * @param quoteToLe
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnQuoteSite(QuoteIllSite quoteIllSite,QuoteToLe quoteToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		LOGGER.info("site id is ---> {} and quote to le is ---> {} ", quoteIllSite.getId(),  quoteToLe.getId());
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndQuoteToLe_Id(quoteIllSite.getId(), quoteToLe.getId());
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				if(!(MACDConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType()) && CommonConstants.ACTIVE.equals(quoteIllSiteToService.getIsDeleted())))
				{
				LOGGER.info("Quote ill site to service s id is ----> {}", quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
				serviceIdWithType.put(quoteIllSiteToService.getType(), quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
				}
			});
		}

		return serviceIdWithType;
	}
	
	/**
	 * Method to get serviceID based on quoteNplLink
	 * @param Npllinks
	 * @param quoteToLe
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnQuoteLink(QuoteNplLink Npllinks,QuoteToLe quoteToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndQuoteToLe_Id(Npllinks.getId(),quoteToLe.getId());
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceIdWithType.put(quoteIllSiteToService.getType(), quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}

	/**
	 * Method to get serviceID based on orderIllSite
	 * @param orderIllSite
	 * @param orderToLe
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnOrderSite(OrderIllSite orderIllSite, OrderToLe orderToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderIllSite_IdAndOrderToLe_Id(orderIllSite.getId(),orderToLe.getId());
		if(!orderIllSiteToServices.isEmpty()) {
			orderIllSiteToServices.stream().forEach(orderIllSiteToService -> {
				serviceIdWithType.put(orderIllSiteToService.getType(), orderIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}
	
	public List<SIServiceDetailsBean> getServiceDetailsBeanList(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {
		List<SIServiceDetailsBean> serviceDetailsList = null;
		String serviceIdsMultiple = null;
		LOGGER.info("Input quoteToLe - {}, opportunityBean {}", quoteToLe.getId(), opportunityBean.getPortalTransactionId());

		List<QuoteIllSiteToService> quoteIllSiteToServiceList ;
		if("TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())){
			quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(quoteToLe.getId(),CommonConstants.INACTIVE);
		} else {
			quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		}

		  String serviceIdsList = quoteIllSiteToServiceList.stream().map(i ->
		  i.getErfServiceInventoryTpsServiceId().trim()).distinct()
		  .collect(Collectors.joining(","));
		 
		String serviceIds = quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
		if(!"TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())){
        if(CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
            serviceIdsMultiple = serviceIds + "- Multiple"; 
            }
        else {
            serviceIdsMultiple = serviceIds;
        }
		
		
		LOGGER.info("serviceIds input to queue {}", serviceIdsMultiple);
		opportunityBean.setCurrentCircuitServiceId(serviceIdsMultiple);
		}
		LOGGER.info("MDC Filter token value in before Queue call getOpportunityBean GVPN Mapper {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		if((quoteToLe.getQuoteCategory() != null && !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) || MACDConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
		String OrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIdsList);
		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(OrderDetailsQueue, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		} else {
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsAddSiteQueue, serviceIdsList);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			
		}
		
		return serviceDetailsList;
	}



	/**
	 * Method to get list of all service Ids
	 *
	 * @param quoteToLe
	 * @return
	 */
	public List<String> getAllServiceIdListBasedOnQuoteToLe(QuoteToLe quoteToLe)
	{
		List<String> serviceIds=new ArrayList<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		LOGGER.info("QuoteillSiteToService size is {} ", quoteIllSiteToServices.size());
		boolean isUnique=isServiceIdsUnique(quoteIllSiteToServices);
		if(!isUnique) {
				QuoteIllSiteToService quoteIllSiteToService = quoteIllSiteToServices.stream().findFirst().get();
				quoteIllSiteToServices.clear();
				quoteIllSiteToServices.add(quoteIllSiteToService);
		}
			if (!quoteIllSiteToServices.isEmpty()) {
				quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
					if(!(MACDConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType()) && CommonConstants.ACTIVE.equals(quoteIllSiteToService.getIsDeleted()))) {
						serviceIds.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
					}
				});
			}

		if(serviceIds.isEmpty())
		{
			serviceIds.add(quoteToLe.getErfServiceInventoryTpsServiceId());
		}

		return serviceIds;
	}

	/**
	 * Method to check serviceIds are unique
	 *
	 * @param quoteIllSiteToServices
	 * @return
	 */
	public boolean isServiceIdsUnique(List<QuoteIllSiteToService> quoteIllSiteToServices)
	{
		boolean isUnique=true;
		if(!quoteIllSiteToServices.isEmpty()&&quoteIllSiteToServices.size()>1) {
			if (quoteIllSiteToServices.get(0).getErfServiceInventoryTpsServiceId().equalsIgnoreCase(quoteIllSiteToServices.get(1).getErfServiceInventoryTpsServiceId())) {
				isUnique = false;
			}
		}
		LOGGER.info("Is unique service id  {} ", isUnique);
		return isUnique;
	}

	/**
	 * Method to check serviceIds are unique
	 *
	 * @param orderIllSiteToServices
	 * @return
	 */
	public boolean isServiceIdsUniqueForOrder(List<OrderIllSiteToService> orderIllSiteToServices)
	{
		boolean isUnique=true;
		if(!orderIllSiteToServices.isEmpty()&&orderIllSiteToServices.size()>1) {
			if (orderIllSiteToServices.get(0).getErfServiceInventoryTpsServiceId().equalsIgnoreCase(orderIllSiteToServices.get(1).getErfServiceInventoryTpsServiceId())) {
				isUnique = false;
			}
		}
		return isUnique;
	}

	/**
	 * Method to get contract info
	 * @param quoteToLe
	 * @return
	 */
	private List<ContractTermBean> getContractInfo(QuoteToLe quoteToLe)
	{
		List<String> serviceIds=getServiceIds(quoteToLe);
		List<ContractTermBean> contracts=new ArrayList<>();
		if(!serviceIds.isEmpty())
		{
			serviceIds.stream().forEach(serviceId->{
				try {
					SIServiceDetailDataBean serviceDetailDataBean = getServiceDetail(serviceId,quoteToLe.getQuoteCategory());
					ContractTermBean contractTermBean = new ContractTermBean();
					contractTermBean.setServiceId(serviceId);
					contractTermBean.setContractTerm(serviceDetailDataBean.getContractTerm());
					contractTermBean.setCommissionedDate(serviceDetailDataBean.getServiceCommissionedDate());
					contractTermBean.setContractEndDate(serviceDetailDataBean.getContractStartDate());
					contractTermBean.setContractEndDate(serviceDetailDataBean.getContractEndDate());
					contracts.add(contractTermBean);
				}
				catch(Exception e)
				{
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
			});

		}
		return contracts;
	}

	public List<ContractTermBean> getContractTermResponse(Integer quoteToLeId) throws TclCommonException
	{
		List<ContractTermBean> contractTermBeans=new ArrayList<>();
		if (Objects.isNull(quoteToLeId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		Optional<QuoteToLe> quoteToLeOptional=quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLeOptional.isPresent())
		{
			 contractTermBeans=getContractInfo(quoteToLeOptional.get());

		}
			return contractTermBeans;
	}
	
	
	/**
	 * Method to get service detail based on service Id
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailDataBean getServiceDetailIAS(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceId);
		if(Objects.nonNull(orderDetailsQueueResponse)){
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}", orderDetailsQueueResponse,serviceId);
		}

		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		

		SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBean(serviceDetail,null);

		return serviceDetailDataBean;
	}
	
	/**
	 * Method to get service detail based on service Id
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailDataBean getServiceDetailIWAN(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceId);
		if(Objects.nonNull(orderDetailsQueueResponse)){
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}", orderDetailsQueueResponse,serviceId);
		}

		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		

		SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBean(serviceDetail,null);

		return serviceDetailDataBean;
	}
	
	/**
	 * Method to get serviceID based on quoteIllSite
	 * @param link
	 * @param quoteToLe
	 * @return
	 */
	public List<String> getServiceIdBasedOnLink(QuoteNplLink link,QuoteToLe quoteToLe)
	{
		List<String> serviceIdWithType=new ArrayList<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(link.getId(), quoteToLe);
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceIdWithType.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}

	/**
	 * Method to get serviceID based on quoteIllSite
	 * @param orderToLe
	 * @param link
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnOrderLink(OrderNplLink link, OrderToLe orderToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderNplLink_IdAndOrderToLe(link.getId(), orderToLe);
		if(!orderIllSiteToServices.isEmpty()) {
			orderIllSiteToServices.stream().forEach(orderIllSiteToService -> {
				serviceIdWithType.put(orderIllSiteToService.getType(),orderIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}

	public Map<String,String> getServiceIdBasedOnOrderLe(OrderToLe orderToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
		if(!orderIllSiteToServices.isEmpty()) {
			orderIllSiteToServices.stream().forEach(orderIllSiteToService -> {
				serviceIdWithType.put(orderIllSiteToService.getType(),orderIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}
	/**
	 * Method to get service detail based on service Id for NPL 
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SIServiceDetailDataBean> getServiceDetailNPL(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceInfoBean> serviceDetailsList = new ArrayList<>();
		List<SIServiceDetailDataBean> serviceDetailDataList = new ArrayList<>();;
		LOGGER.info("Inside getServiceDetailNPL to fetch SIServiceDetailDataBean List for serviceid {} ", serviceId);
		
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siNplServiceDetailsQueue, serviceId);

		LOGGER.info("Response received in OMS getServiceDetailNPL: {}",orderDetailsQueueResponse);

		SIServiceInfoBean[] serviceDetailBeanArray = (SIServiceInfoBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceInfoBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		
		mapServiceDetailBeanNPL(serviceDetailsList, serviceDetailDataList);

		return serviceDetailDataList;
	}

	/**
	 * Method to get siOrderData based on service Id for NPL
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIOrderDataBean getSiOrderDataBasedOnServiceId(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		SIOrderDataBean siOrderDataBean;
		LOGGER.info("Inside getSiOrderDataBasedOnServiceId to fetch SiOrderDataBean for service id {} ", serviceId);


		LOGGER.info("Sending request to getSIOrder:: {}", serviceId);
		String response = (String) mqUtils.sendAndReceive(getSIOrderServiceIdQueue, serviceId);
		LOGGER.info("Received response from getSIOrder:: {}", response);
		SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
		if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
			siOrderDataBean= orderResponse.getOrder();
		} else {
			LOGGER.error("Error in retrieving getSIOrder:: {}", orderResponse.getMessage());
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		}

		return siOrderDataBean;
	}

	/**
	 * Method to map serviceDetailBean
	 * @param siServiceDetail
	 * @return
	 */
	private void mapServiceDetailBeanNPL(List<SIServiceInfoBean> siServiceDetailList, List<SIServiceDetailDataBean> serviceDetailDataList)
	{
		siServiceDetailList.stream().forEach(siServiceDetail -> {
		LOGGER.info("Inside mapServiceDetailBean to construct SIServiceDetailDataBean for serviceid {} ", siServiceDetail.getTpsServiceId());
		List<SIAttributeBean> serviceAttributesList = new ArrayList<>();
		SIServiceDetailDataBean serviceDetail=new SIServiceDetailDataBean();
		serviceDetail.setAccessType(siServiceDetail.getLastMileType());
		serviceDetail.setAccessProvider(siServiceDetail.getLastMileProvider());
		serviceDetail.setArc(siServiceDetail.getArc() != null?siServiceDetail.getArc():0);
		serviceDetail.setBillingFrequency(siServiceDetail.getBillingFrequency());
		serviceDetail.setBillingMethod(siServiceDetail.getBillingMethod());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		serviceDetail.setErfPrdCatalogOfferingId(siServiceDetail.getParentProductOfferingId());
		serviceDetail.setErfPrdCatalogOfferingName(siServiceDetail.getProductOfferingName());
		serviceDetail.setErfPrdCatalogProductName(siServiceDetail.getProductName());
		LOGGER.info("LMBANDWIDTH"+siServiceDetail.getLastMileBandwidth());
		LOGGER.info("PORTBANDWIDTH"+siServiceDetail.getBandwidthPortSpeed());
		serviceDetail.setLastmileBw(siServiceDetail.getLastMileBandwidth());
		serviceDetail.setLastmileBwUnit(siServiceDetail.getLastMileBandwidthUnit());
		serviceDetail.setPortBw(siServiceDetail.getBandwidthPortSpeed());
		serviceDetail.setPortBwUnit(siServiceDetail.getBandwidthUnit());
		LOGGER.info("afterLMBANDWIDTH"+serviceDetail.getLastmileBw());
		LOGGER.info("afterPORTBANDWIDTH"+serviceDetail.getPortBw());
		serviceDetail.setLinkType(siServiceDetail.getPrimaryOrSecondary().toLowerCase());
		serviceDetail.setTpsServiceId(siServiceDetail.getTpsServiceId());
		serviceDetail.setPriSecServLink(siServiceDetail.getPriSecServiceLink());
		serviceDetail.setNrc(siServiceDetail.getNrc()!=null?siServiceDetail.getNrc():0);
		serviceDetail.setReferenceOrderId(Objects.nonNull(siServiceDetail.getSiOrderId())?Integer.toString(siServiceDetail.getSiOrderId()):null);
		serviceDetail.setVpnName(siServiceDetail.getVpnName());
		LOGGER.info("mapServiceDetailBean locationId : {} for the serviceId : {}",siServiceDetail.getLocationId(),siServiceDetail.getTpsServiceId());

		serviceDetail.setErfLocSiteAddressId(siServiceDetail.getLocationId()!=null?Integer.toString(siServiceDetail.getLocationId()):null);

		serviceDetail.setLatLong(siServiceDetail.getLatLong());
		serviceDetail.setId(siServiceDetail.getId());
		serviceDetail.setMrc(siServiceDetail.getMrc()!=null?siServiceDetail.getMrc():0);
		serviceDetail.setArc(siServiceDetail.getArc()!=null?siServiceDetail.getArc():0);
		serviceDetail.setContractStartDate(siServiceDetail.getContractStartDate());
		serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		if(siServiceDetail.getServiceCommissionedDate() != null)
			serviceDetail.setServiceCommissionedDate(Timestamp.valueOf(siServiceDetail.getServiceCommissionedDate()));
		serviceDetail.setSiteEndInterface(siServiceDetail.getSiteEndInterface());
		serviceDetail.setSourceCity(siServiceDetail.getSourceCity());
		LOGGER.info("site type from inventory"+siServiceDetail.getSiteType()+"INTEERFACE"+serviceDetail.getSiteEndInterface());
		serviceDetail.setSiteType(siServiceDetail.getSiteType());
		serviceDetail.setSiteAddress(siServiceDetail.getSiteAddress());
		serviceDetail.setTpsCrmCofId(siServiceDetail.getTpsCrmCofId());
		serviceDetail.setOrderCode(siServiceDetail.getOrderCode());
		serviceDetail.setTpsCopfId(siServiceDetail.getTpsCopfId());
		serviceDetail.setTpsSfdcCuId(siServiceDetail.getTpsSfdcCuId());
		serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
		serviceDetail.setOrderCategory(siServiceDetail.getOrderCategory());
		
		serviceDetail.setPortMode(siServiceDetail.getPortMode());
		serviceDetail.setBillingCurrency(siServiceDetail.getBillingCurrency());
		serviceDetail.setCircuitExpiryDate(siServiceDetail.getCircuitExpiryDate());

		serviceDetail.setDestinationCity(siServiceDetail.getDestinationCity());
		serviceDetail.setAccountManager(siServiceDetail.getAccountManager());
		serviceDetail.setsCommisionDate(siServiceDetail.getServiceCommissionedDate());
		serviceDetail.setCurrentOpportunityType(siServiceDetail.getCurrentOpportunityType());

		//added for NPL macd MF
		serviceDetail.setLastMileProvider(siServiceDetail.getLastMileProvider());
		serviceDetail.setCrossConnectType(siServiceDetail.getCrossConnectType());
		
		serviceDetailDataList.add(serviceDetail);
		if(Objects.nonNull(siServiceDetail.getAttributes()) && !siServiceDetail.getAttributes().isEmpty()) {
			siServiceDetail.getAttributes().stream().forEach(serviceAttribute -> {
				SIAttributeBean siAttribute = new SIAttributeBean();
				siAttribute.setName(serviceAttribute.getAttributeName());
				siAttribute.setValue(serviceAttribute.getAttributeValue());
				serviceAttributesList.add(siAttribute);
				
			});
			serviceDetail.setAttributes(serviceAttributesList);
		}
		
		
		serviceDetail.setComponents(siServiceDetail.getComponentBean());
		
		
		});
		
	}
	
	/**
	 * Method to get serviceID based on quoteIllSite
	 * @param quoteIllSite
	 * @param quoteToLe
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnQuotetoLe(QuoteToLe quoteToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceIdWithType.put(quoteIllSiteToService.getType(), quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}
	
	/**
	 * Method to get macd initiated status
	 *
	 * @param serviceIds
	 * @return
	 */
	public Map<String, Object> getMacdInitiatedStatusForNPL(String serviceId) {
		Map<String, Object> macdStatus = new HashMap<>();
		if (Objects.nonNull(serviceId)) {
			
				
				MacdDetail macdDetail = macdDetailRepository.findByTpsServiceIdAndStageAndOrderCategoryNot(serviceId,
						MACDConstants.MACD_ORDER_INITIATED, MACDConstants.ADD_SITE_SERVICE);
				if (Objects.nonNull(macdDetail)) {
					macdStatus.put(serviceId, true);
				} else {
					MacdDetail macdDetailEntry = macdDetailRepository.findByTpsServiceIdAndStageAndOrderType(serviceId, MACDConstants.MACD_ORDER_INITIATED,  MACDConstants.TERMINATION);
					if(Objects.nonNull(macdDetailEntry)){
						macdStatus.put(serviceId, true);
					} else {
					macdStatus.put(serviceId, false);
					}
				}
			
		}
		return macdStatus;
	}
	
	
	public BomInventoryCatalogAssocResponse getProductCatalogCpeBomEquivalentForInvrentoryBom(String oldCpe) throws TclCommonException{
		BomInventoryCatalogAssocResponse bomInventoryCatalogAssocResponse = null;
		LOGGER.info("Inside getProductCatalogCpeBomEquivalentForInvrentoryBom for old bom {} ", oldCpe);
		if(Objects.nonNull(oldCpe)) {
		String cpeBomResponse = (String) mqUtils.sendAndReceive(cpeBomPrdCatalogQueue, oldCpe);
		LOGGER.info("Response received in getProductCatalogCpeBomEquivalentForInvrentoryBom: {}"+cpeBomResponse);
		if(cpeBomResponse != null) {
			bomInventoryCatalogAssocResponse = (BomInventoryCatalogAssocResponse) Utils
				.convertJsonToObject(cpeBomResponse, BomInventoryCatalogAssocResponse.class);
		}
		}
		return bomInventoryCatalogAssocResponse;
		
	}



	/**
	 * Method to get serviceID based on quoteIllSiteID
	 * @param siteId
	 * @param quoteToLeId
	 * @return
	 */
	public List<String> getServiceIdBasedOnQuoteSiteId(Integer siteId,Integer quoteToLeId)
	{
		List<String> serviceIdList=new ArrayList<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndQuoteToLe_Id(siteId,quoteToLeId);
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceIdList.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdList;
	}

		
	
	

	
	/**
	 * Method to get service detail based on service Id for NPL 
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceInfoBean getServicAttributesNde(String serviceId)throws TclCommonException
	{
		SIServiceInfoBean serviceDetailBeanArray = new SIServiceInfoBean();
		LOGGER.info("Inside getServicAttributesNde to fetch SIServiceattributebean for serviceid {} ", serviceId);
		try {

			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siServiceDetailsNdeQueue, serviceId);
			LOGGER.info("Response received in OMS getServiceDetailNde: {}" + orderDetailsQueueResponse);

			serviceDetailBeanArray = (SIServiceInfoBean) Utils.convertJsonToObject(orderDetailsQueueResponse,
					SIServiceInfoBean.class);
		} catch (Exception e) {
			LOGGER.info("exception in inventory queue");
		}

		return serviceDetailBeanArray;
	}
	
	
	/**
	 * Method to get serviceID based on quoteNplLink
	 * @param quoteIllSite
	 * @param quoteToLe
	 * @return
	 */
	public Map<String,String> getServiceIdBasedOnQuoteNplLink(QuoteNplLink quoteNplLink,QuoteToLe quoteToLe)
	{
		Map<String,String> serviceIdWithType=new HashMap<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(quoteNplLink.getId(),quoteToLe);
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceIdWithType.put(quoteIllSiteToService.getType(), quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}

		return serviceIdWithType;
	}
	
	
	/**
	 * Method to get list of service Ids for nde mc
	 *
	 * @param quoteToLe
	 * @return
	 */
	public List<String> getServiceIdsMc(QuoteToLe quoteToLe)
	{
		List<String> serviceIds=new ArrayList<>();
		Set<String> serviceMcids=new HashSet<>();
		List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
		if(!quoteIllSiteToServices.isEmpty()) {
			quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
				serviceMcids.add(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			});
		}
		LOGGER.info("Service Ids in getServiceIdsMc method set list {}",serviceMcids);
		if(serviceMcids.size()!=0) {
			 serviceIds = serviceMcids.stream().collect(Collectors.toList());
		}
		LOGGER.info("Service Ids in getServiceIdsMc method list of ids {}",serviceIds);
		return serviceIds;
	}
	
	/**
	 * Method to get service detail based on service Id after an order is placed even if the service id is inactive
	 * Fix for PIPF-167 
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailDataBean getServiceDetailForInactiveServicesOnceOrderIsPlaced(String serviceId, String quoteCategory, QuoteToLe quoteToLe)throws TclCommonException
	{
		LOGGER.info("Entering getServiceDetailForInactiveServicesOnceOrderIsPlaced");
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		SIServiceDetailDataBean serviceDetailDataBean = null;
		LOGGER.info("Inside getServiceDetailForInactiveServicesOnceOrderIsPlaced to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		Optional<Order> order = orderRepository.findByQuote(quoteToLe.getQuote());
		if(order.isPresent()) {
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsAddSiteQueue, serviceId);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		

		serviceDetailDataBean=mapServiceDetailBean(serviceDetail, quoteCategory);
		} else {
			serviceDetailDataBean = getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
		}


		return serviceDetailDataBean;
	}
	@Transactional
    public void updateO2CMacdService(String serviceCode) {
        LOGGER.info("O2C Macd sservice detail code : {}", serviceCode);
        if(Objects.nonNull(serviceCode) && !serviceCode.isEmpty()) {
			List<MacdDetail> macdDetails = macdDetailRepository.findByTpsServiceIdAndStageIn(serviceCode,
					Arrays.asList(MACDConstants.MACD_ORDER_INITIATED, MACDConstants.MACD_ORDER_IN_PROGRESS));
            if (Objects.nonNull(macdDetails)) {
                for (MacdDetail macdDetail : macdDetails) {
                    LOGGER.info("O2C Macd sservice detail id : {}", macdDetail.getId());
                    macdDetail.setStage(MACDConstants.MACD_ORDER_CANCELED);
                    macdDetail.setUpdatedTime(new Date());
                    macdDetailRepository.save(macdDetail);
                    LOGGER.info("Macd detail bean has been updated");
                }
            }else{
                LOGGER.info("No MACD order found for this service code :{}", serviceCode);
            }
        }
     }
	
	/**
	 * Method to get service detail based on service Id
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailDataBean getUnderProvisioningServiceDetail(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(underProvServiceDetail, serviceId);
		LOGGER.info("getUnderProvisioningServiceDetail response {} ",orderDetailsQueueResponse);
		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		
		SIServiceDetailDataBean serviceDetailDataBean=mapUnderProvServiceDetailBean(serviceDetail);

		return serviceDetailDataBean;
	}
	
	/**
	 * Method to map serviceDetailBean
	 * @param siServiceDetail
	 * @return
	 */
private SIServiceDetailDataBean mapUnderProvServiceDetailBean(SIServiceDetailsBean siServiceDetail)
{
	LOGGER.info("Inside mapUnderProvServiceDetailBean to construct SIServiceDetailDataBean for serviceid {} ", siServiceDetail.getTpsServiceId());
	SIServiceDetailDataBean serviceDetail=new SIServiceDetailDataBean();
	List<SIAttributeBean> serviceAttributesList = new ArrayList<>();
	serviceDetail.setAccessType(siServiceDetail.getAccessType());
	serviceDetail.setAccessProvider(siServiceDetail.getAccessProvider());
	serviceDetail.setArc(siServiceDetail.getArc());
	serviceDetail.setBillingFrequency(siServiceDetail.getBillingFrequency());
	serviceDetail.setBillingMethod(siServiceDetail.getBillingMethod());
	serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
	serviceDetail.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
	serviceDetail.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
	serviceDetail.setLastmileBw(siServiceDetail.getLastmileBw());
	serviceDetail.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
	serviceDetail.setPortBw(siServiceDetail.getPortBw());
	serviceDetail.setPortBwUnit(siServiceDetail.getPortBwUnit());
	if(siServiceDetail.getLinkType() != null)
			serviceDetail.setLinkType(siServiceDetail.getLinkType().toLowerCase());
	serviceDetail.setTpsServiceId(siServiceDetail.getTpsServiceId());
	serviceDetail.setPriSecServLink(siServiceDetail.getPriSecServLink());
	serviceDetail.setNrc(siServiceDetail.getNrc());
	serviceDetail.setReferenceOrderId(Objects.nonNull(siServiceDetail.getReferenceOrderId())?Integer.toString(siServiceDetail.getReferenceOrderId()):null);
	serviceDetail.setVpnName(siServiceDetail.getVpnName());
	LOGGER.info("mapServiceDetailBean locationId : {} for the serviceId  {}",siServiceDetail.getErfLocSiteAddressId(),siServiceDetail.getTpsServiceId());
	serviceDetail.setErfLocSiteAddressId(siServiceDetail.getErfLocSiteAddressId()!=null?Integer.toString(siServiceDetail.getErfLocSiteAddressId()):null);
	serviceDetail.setSiteAddress(siServiceDetail.getSiteAddress());
	serviceDetail.setLatLong(siServiceDetail.getLatLong());
	serviceDetail.setId(siServiceDetail.getId());
	LOGGER.info("Mrc inventory value "+siServiceDetail.getMrc());
	serviceDetail.setMrc(siServiceDetail.getMrc());
	serviceDetail.setArc(siServiceDetail.getArc());
	serviceDetail.setBillingType(siServiceDetail.getBillingType());
	serviceDetail.setContractStartDate(siServiceDetail.getContractStartDate());
	serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
	serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
	serviceDetail.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
	serviceDetail.setDemarcationRoom(siServiceDetail.getDemarcationRoom());
	serviceDetail.setDemarcationFloor(siServiceDetail.getDemarcationFloor());
	serviceDetail.setDemarcationApartment(siServiceDetail.getDemarcationApartment());
	serviceDetail.setDemarcationRack(siServiceDetail.getDemarcationRack());
	serviceDetail.setLmType(siServiceDetail.getLastMileType());
	serviceDetail.setLastMileProvider(siServiceDetail.getAccessProvider());
//	if(Objects.nonNull(siServiceDetail.getAssetAttributes()) && !siServiceDetail.getAssetAttributes().isEmpty()) {
//		siServiceDetail.getAssetAttributes().stream().forEach(serviceAttribute -> {
//			LOGGER.info("Si Service Atrribute bean is ---> {} ", serviceAttribute);
//			SIAttributeBean siAttribute = new SIAttributeBean();
//			siAttribute.setName(serviceAttribute.getAttributeName());
//			siAttribute.setValue(serviceAttribute.getAttributeValue());
//			serviceAttributesList.add(siAttribute);
//			
//		});
//		serviceDetail.setAttributes(serviceAttributesList);
//	}
	//added for gvpn multi vrf macd
	serviceDetail.setTotalVrfBandwith(siServiceDetail.getTotalVrfBandwith());
	LOGGER.info("To be mapped Service detail data bean for service ID ---> {} is ----> {} and origin si service detail bean is ---> {}  ", serviceDetail.getTpsServiceId(), serviceDetail, siServiceDetail);
	return serviceDetail;
}


/**
 * Method to get service detail based on service Id irrespective of whether the service id is active/inactive
 * Specific for Termination to pick the latest data 
 *
 * @param serviceId
 * @return
 * @throws TclCommonException
 */
public SIServiceDetailDataBean getServiceDetailIASTermination(String serviceId)throws TclCommonException
{
	SIServiceDetailsBean serviceDetail = null;
	List<SIServiceDetailsBean> serviceDetailsList = null;
	LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
	String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(inactiveServiceDetialsQueue, serviceId);
	if(Objects.nonNull(orderDetailsQueueResponse)){
		LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}", orderDetailsQueueResponse,serviceId);
	}

	SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
			.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
	serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
	
	if(serviceDetailsList.isEmpty())
		throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
	serviceDetail=serviceDetailsList.stream().findFirst().get(); 
	

	SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBean(serviceDetail,null);

	return serviceDetailDataBean;
}


/**
 * Method to get service detail based on service Id for NPL 
 *
 * @param serviceId
 * @return
 * @throws TclCommonException
 */
public List<SIServiceDetailDataBean> getServiceDetailNPLTermination(String serviceId)throws TclCommonException
{
	SIServiceDetailsBean serviceDetail = null;
	List<SIServiceInfoBean> serviceDetailsList = new ArrayList<>();
	List<SIServiceDetailDataBean> serviceDetailDataList = new ArrayList<>();;
	LOGGER.info("Inside getServiceDetailNPL to fetch SIServiceDetailDataBean List for serviceid {} ", serviceId);
	
	String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(inactiveServiceDetailsNplQueue, serviceId);

	LOGGER.info("Response received in OMS getServiceDetailNPL: {}",orderDetailsQueueResponse);

	SIServiceInfoBean[] serviceDetailBeanArray = (SIServiceInfoBean[]) Utils
			.convertJsonToObject(orderDetailsQueueResponse, SIServiceInfoBean[].class);
	serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
	
	if(serviceDetailsList.isEmpty())
		throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
	
	mapServiceDetailBeanNPL(serviceDetailsList, serviceDetailDataList);

	return serviceDetailDataList;
}

	/**
	 * Method to get price revision detail based on service Id
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SIPriceRevisionDetailDataBean> getPriceRevisionDetailForTermination(String serviceId)throws TclCommonException
	{
		List<SIPriceRevisionDetailDataBean> priceRevisionDetailList = new ArrayList<>();
		LOGGER.info("Inside getPriceRevisionDetailForTermination to fetch Price Revision Details for serviceid {} ", serviceId);
		
		String priceRevisionDetailQueueResponse = (String) mqUtils.sendAndReceive(priceRevisionDetailQueue, serviceId);
		LOGGER.info("Response received in OMS getPriceRevisionDetailForTermination: {}", priceRevisionDetailQueueResponse);
	
		SIPriceRevisionDetailDataBean[] serviceDetailBeanArray = Utils.convertJsonToObject(priceRevisionDetailQueueResponse, SIPriceRevisionDetailDataBean[].class);
		priceRevisionDetailList = Arrays.asList(serviceDetailBeanArray);
		
		if(priceRevisionDetailList.isEmpty())
			return Collections.emptyList();
		
		return priceRevisionDetailList;
	}
}
