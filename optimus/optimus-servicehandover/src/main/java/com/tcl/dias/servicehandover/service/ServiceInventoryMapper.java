package com.tcl.dias.servicehandover.service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
import com.tcl.dias.common.serviceinventory.bean.ScAttachmentBean;
import com.tcl.dias.common.serviceinventory.bean.ScCommercialBean;
import com.tcl.dias.common.serviceinventory.bean.ScContractInfoBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceSlaBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.*;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.*;
import com.tcl.dias.serviceactivation.entity.repository.MstCambiumDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRadwinDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceSla;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstStatusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.beans.LmComponentBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.RfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.*;

import static com.tcl.dias.common.redis.service.RedisCacheServiceImpl.logger;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 *
 */

@Component
public class ServiceInventoryMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryMapper.class);

	@Autowired
	RfService rfService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskDataRepository taskDataRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	MstRadwinDetailsRepository mstRadwinDetailsRepository;

	@Autowired
	MstCambiumDetailsRepository mstCambiumDetailsRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	public ScOrderBean mapEntityToOrderBean(ScOrder scOrder, ScServiceDetail scServiceDetail,Map<String, String> scComponentsAttrMapA) throws TclCommonException {
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
		scOrderBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scOrder));
		scOrderBean.setOrderStatus(scOrder.getOrderStatus());
		scOrderBean.setOrderStartDate(scOrder.getOrderStartDate());
		scOrderBean.setOrderSource(scOrder.getOrderSource());
		scOrderBean.setOrderEndDate(scOrder.getOrderEndDate());
		scOrderBean.setOrderCategory(OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder));
		//scOrderBean.setOrderSubCategory(scOrder.getOrderSubCategory());
		scOrderBean.setErfOrderLeId(scOrder.getErfOrderLeId());
		scOrderBean.setOpOrderCode(scOrder.getOpOrderCode());
		scOrderBean.setOpportunityClassification(scOrder.getOpportunityClassification());
		scOrderBean.setIsActive(scOrder.getIsActive());
		scOrderBean.setIsBundleOrder(scOrder.getIsBundleOrder());
		scOrderBean.setIsMultipleLe(scOrder.getIsMultipleLe());
		scOrderBean.setLastMacdDate(scOrder.getLastMacdDate());
		scOrderBean.setMacdCreatedDate(scOrder.getMacdCreatedDate());

		for (ScOrderAttribute scOrderAttribute : scOrder.getScOrderAttributes()) {
			scOrderBean.getScOrderAttributes().add(mapOrderAttrEntityToBean(scOrderAttribute));
		}
		for (ScContractInfo scContractInfo : scOrder.getScContractInfos1()) {
			scOrderBean.getScContractInfos().add(mapContractInfoEntityToBean(scContractInfo));
		}
		scOrderBean.getScServiceDetails().add(mapServiceDetailEntityToBean(scServiceDetail,scComponentsAttrMapA));
		return scOrderBean;
	}

	private ScOrderAttributeBean mapOrderAttrEntityToBean(ScOrderAttribute scOrderAttribute) {
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

	private ScContractInfoBean mapContractInfoEntityToBean(ScContractInfo scContractInfo) {
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
		scContractInfoBean.setId(scContractInfo.getId());
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

	private ScServiceDetailBean mapServiceDetailEntityToBean(ScServiceDetail scServiceDetail,Map<String, String> components) throws TclCommonException {
		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		scServiceDetailBean.setErfScServiceId(scServiceDetail.getErfOdrServiceId());
		scServiceDetailBean.setId(scServiceDetail.getId());
		scServiceDetailBean.setAccessType(scServiceDetail.getAccessType());
		scServiceDetailBean.setArc(scServiceDetail.getArc());
		scServiceDetailBean.setBurstableBwPortspeed(components.get("burstableBandwidth"));
		scServiceDetailBean.setBurstableBwPortspeedAltName(components.get("burstableBandwidth"));
		scServiceDetailBean.setBurstableBwUnit(components.get("burstableBwUnit"));
		scServiceDetailBean.setBwPortspeed(components.get("portBandwidth"));
		scServiceDetailBean.setBwPortspeedAltName(components.get("portBandwidth"));
		scServiceDetailBean.setBwUnit(components.get("bwUnit"));
		scServiceDetailBean.setDemarcationApartment(components.get("demarcationBuildingName"));
		scServiceDetailBean.setDemarcationFloor(components.get("demarcationFloor"));
		scServiceDetailBean.setDemarcationRack(components.get("bwUnit"));
		scServiceDetailBean.setDestinationAddressLineOne(components.get("destinationAddressLineOne"));
		scServiceDetailBean.setDestinationAddressLineTwo(components.get("destinationAddressLineTwo"));
		scServiceDetailBean.setDestinationLocality(components.get("destinationLocality"));
		scServiceDetailBean.setDestinationPincode(components.get("destinationPincode"));
		scServiceDetailBean.setDestinationCity(components.get("destinationCity"));
		scServiceDetailBean.setDestinationState(components.get("destinationState"));
		scServiceDetailBean.setDestinationCountry(components.get("destinationCountry"));
		scServiceDetailBean.setBillingAccountId(scServiceDetail.getBillingAccountId());
		scServiceDetailBean.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		scServiceDetailBean.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		scServiceDetailBean.setBillingType(scServiceDetail.getBillingType());
		scServiceDetailBean.setCallType(scServiceDetail.getCallType());
		scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
		scServiceDetailBean.setCreatedDate(scServiceDetail.getCreatedDate());
		scServiceDetailBean.setCustOrgNo(scServiceDetail.getCustOrgNo());
		scServiceDetailBean.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
		scServiceDetailBean.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
		scServiceDetailBean.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetailBean.setDiscountArc(scServiceDetail.getDiscountArc());
		scServiceDetailBean.setDiscountMrc(scServiceDetail.getDiscountMrc());
		scServiceDetailBean.setDiscountNrc(scServiceDetail.getDiscountNrc());
		scServiceDetailBean.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
		scServiceDetailBean.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
		scServiceDetailBean.setErfLocPopSiteAddressId(components.get("popSiteAddressId"));
		scServiceDetailBean.setErfLocSiteAddressId(components.get("siteAddressId"));
		scServiceDetailBean.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
		scServiceDetailBean.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
		scServiceDetailBean.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetailBean.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		scServiceDetailBean
				.setErfPrdCatalogParentProductOfferingName(scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
		scServiceDetailBean.setFeasibilityId(components.get("feasibilityId"));
		scServiceDetailBean.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
		scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
		scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
		scServiceDetailBean.setLastmileBw(components.get("localLoopBandwidth"));
		scServiceDetailBean.setLastmileBwAltName(components.get("localLoopBandwidth"));
		scServiceDetailBean.setLastmileBwUnit(components.get("localLoopBandwidthUnit"));
		scServiceDetailBean.setLastmileProvider(components.get("lastMileProvider"));
		scServiceDetailBean.setLastmileType(components.get("lmType"));
		scServiceDetailBean.setLatLong(components.get("latLong"));
		scServiceDetailBean.setLocalItContactEmail(components.get("localItContactEmailId"));
		scServiceDetailBean.setLocalItContactMobile(components.get("localItContactMobile"));
		scServiceDetailBean.setLocalItContactName(components.get("localItContactName"));
		scServiceDetailBean.setMrc(scServiceDetail.getMrc());
		scServiceDetailBean.setNrc(scServiceDetail.getNrc());
		scServiceDetailBean.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
		scServiceDetailBean.setParentId(scServiceDetail.getParentId());
		scServiceDetailBean.setPopSiteAddress(components.get("popSiteAddress"));
		scServiceDetailBean.setPopSiteCode(components.get("popSiteCode"));
		scServiceDetailBean.setPriSecServiceLink(scServiceDetail.getPriSecServiceLink());
		scServiceDetailBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
		scServiceDetailBean.setOrderCategory(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder()));
		// //NOSONAR
		scServiceDetailBean.setPrimarySecondary(
				null != scServiceDetail.getPrimarySecondary() ? scServiceDetail.getPrimarySecondary().toUpperCase()
						: "");
		if (null != scServiceDetail.getPrimarySecondary()
				&& scServiceDetail.getPrimarySecondary().equalsIgnoreCase("primary") 
				&& scServiceDetail.getPriSecServiceLink() == null) {
				scServiceDetailBean.setPrimarySecondary("Single");
		}
		scServiceDetailBean.setSiteAddress(components.get("siteAddress"));
		scServiceDetailBean.setSiteEndInterface(components.get("interface"));
		scServiceDetailBean.setSiteLatLang(components.get("latLong"));
		
		scServiceDetailBean.setProductReferenceId(scServiceDetail.getProductReferenceId());
		scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
		scServiceDetailBean.setServiceClass(scServiceDetail.getServiceClass());
		scServiceDetailBean.setServiceClassification(scServiceDetail.getServiceClassification());
		
		try {
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			scServiceDetailBean.setServiceCommissionedDate(components.get("commissioningDate")!=null?formatter1.parse(components.get("commissioningDate")):null);
		}catch(Exception ee) {
			LOGGER.error("Exception in commissioningDate",ee);
		}
		scServiceDetailBean.setServiceGroupId(scServiceDetail.getServiceGroupId());
		scServiceDetailBean.setServiceGroupType(scServiceDetail.getServiceGroupType());
		scServiceDetailBean.setServiceOption(components.get("cpeManagementType"));
		scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
		scServiceDetailBean.setServiceTerminationDate(scServiceDetail.getServiceTerminationDate());
		scServiceDetailBean.setServiceTopology(scServiceDetail.getServiceTopology());
		scServiceDetailBean.setSiteAlias(scServiceDetail.getSiteAlias());
		scServiceDetailBean.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		scServiceDetailBean.setSiteTopology(scServiceDetail.getSiteTopology());
		scServiceDetailBean.setSiteType(scServiceDetail.getSiteType());
		scServiceDetailBean.setSlaTemplate(scServiceDetail.getSlaTemplate());
		scServiceDetailBean.setSmEmail(scServiceDetail.getSmEmail());
		scServiceDetailBean.setSmName(scServiceDetail.getSmName());
		scServiceDetailBean.setSourceAddressLineOne(components.get("sourceAddressLineOne"));
		scServiceDetailBean.setSourceAddressLineTwo(components.get("sourceAddressLineTwo"));
		scServiceDetailBean.setSourceLocality(components.get("sourceLocality"));
		scServiceDetailBean.setSourcePincode(components.get("sourcePincode"));
		scServiceDetailBean.setSourceCity(components.getOrDefault("sourceCity",scServiceDetail.getSourceCity()));
		scServiceDetailBean.setSourceState(components.getOrDefault("sourceState",scServiceDetail.getSourceState()));
		scServiceDetailBean.setSourceCountry(components.getOrDefault("sourceCountry", scServiceDetail.getSourceCountry()));
		scServiceDetailBean.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		scServiceDetailBean.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		scServiceDetailBean.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		scServiceDetailBean.setTaxExemptionFlag(components.get("taxExemption"));
		scServiceDetailBean.setTpsCopfId(scServiceDetail.getTpsCopfId());
		scServiceDetailBean.setTpsServiceId(scServiceDetail.getTpsServiceId());
		scServiceDetailBean.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
		scServiceDetailBean.setUpdatedDate(scServiceDetail.getUpdatedDate());
		scServiceDetailBean.setUuid(scServiceDetail.getUuid());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnName());
		if(scServiceDetail.getOrderSubCategory() != null && !scServiceDetail.getOrderSubCategory().isEmpty() &&
				scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel") &&
				scServiceDetail.getParentUuid() != null && !scServiceDetail.getParentUuid().isEmpty()) {
			scServiceDetailBean.setParentUuid(scServiceDetail.getParentUuid());
		}
		scServiceDetailBean.setServiceVariant(scServiceDetail.getServiceVariant());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnSolutionId());
		scServiceDetailBean.setAssignedPm(scServiceDetail.getAssignedPM());
		for (ScServiceAttribute scServiceAttribute : scServiceDetail.getScServiceAttributes()) {
			scServiceDetailBean.getScServiceAttributes().add(mapServiceDetailAttrEntityToBean(scServiceDetail, scServiceAttribute));
		}
		for (ScAttachment scAttachment : scServiceDetail.getScAttachments()) {
			scServiceDetailBean.getScAttachments().add(mapServiceAttachmentEntityToBean(scAttachment));
		}
		for(ScServiceSla scServiceSla : scServiceDetail.getScServiceSlas()){
			scServiceDetailBean.getScServiceSlas().add(mapServiceSlaToBean(scServiceSla, scServiceDetailBean));
		}
		if ("OnnetRf".equalsIgnoreCase(scServiceDetail.getAccessType()) || ("onnet wireless".equalsIgnoreCase(scServiceDetail.getAccessType()))) {
			logger.info("ES- Push NEW order to RF Inventory");
			   OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
			ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(scServiceDetail.getUuid(),
					Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE));

			String wirelessScenario= null;
			ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"solution_type");
			if(Objects.isNull(scServiceAttribute)) scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"closest_provider_bso_name");
			if(Objects.nonNull(scServiceAttribute)) wirelessScenario= scServiceAttribute.getAttributeValue();
			if(Objects.nonNull(wirelessScenario) && (wirelessScenario.equalsIgnoreCase("Radwin from TCL POP") || wirelessScenario.toLowerCase().contains("p2p")) && !(wirelessScenario.toLowerCase().contains("pmp")))
			{
			   //List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetail.getId(), "rf-config-p2p");
			  List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(scServiceDetail.getId(), "rf-config-p2p","DELETED");
			  if (!tasks.isEmpty()) { //P2PData
				   logger.info("RF P2P to ES Inventory");
				   Task task = tasks.stream().findFirst().get();
				   String p2pRfData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(task.getId())
						   .getData();
				   optimusRfDataBean = Utils.convertJsonToObject(p2pRfData, OptimusRfDataBean.class);
				   optimusRfDataBean.setCircuitId(scServiceDetail.getUuid());
				   optimusRfDataBean.setServiceType(getServiceType(scServiceDetail));
				   ScOrder scOrder = scServiceDetail.getScOrder();
				  String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

				   if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
					   optimusRfDataBean.setTypeOfOrder(orderCategory);
				   optimusRfDataBean.setActionType(scServiceDetail.getOrderSubCategory());
				   optimusRfDataBean.setLmAction(OrderCategoryMapping.getOrderType(scServiceDetail, scOrder));
				   optimusRfDataBean.setLmType(OrderCategoryMapping.getOrderType(scServiceDetail, scOrder));
				   optimusRfDataBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
				   String state = components.get("destinationState");
				   if (org.springframework.util.StringUtils.isEmpty(state))
					   state=scServiceDetail.getDestinationState();
				   if (!org.springframework.util.StringUtils.isEmpty(state))
					   optimusRfDataBean.setState(state);
				   String city = components.get("destinationCity");
				   if (org.springframework.util.StringUtils.isEmpty(city))
					   city=scServiceDetail.getDestinationCity();
				   if (!org.springframework.util.StringUtils.isEmpty(city))
					   optimusRfDataBean.setCity(city);
				   optimusRfDataBean.setCustomerAddress(components.get("siteAddress"));
				   optimusRfDataBean.setQosBw(setLastMileBwInKbps(serviceDetails.getServiceBandwidth().toString(), scServiceDetail.getBwUnit()));
				   optimusRfDataBean.setVendor("Radwin");
				   optimusRfDataBean.setProvider("Radwin from TCL POP");
				   setOptimusRfTaskStageData(optimusRfDataBean);}
			   }
			else { //PMPData
				   logger.info("RF PMP to ES Inventory: {}",scServiceDetail.getUuid());
					optimusRfDataBean = E2EPMPRFtoInv(scServiceDetail.getUuid(), components);
					optimusRfDataBean.setProvider("TCL UBR PMP");
			   }
				scServiceDetailBean.setUuid(scServiceDetail.getUuid());
			   scServiceDetailBean.setOptimusRfDataBean(optimusRfDataBean);
		}

		scServiceDetailBean.setTigerOrderId(scServiceDetail.getTigerOrderId());
		scServiceDetailBean.setContractStartDate(scServiceDetail.getContractStartDate());
		scServiceDetailBean.setContractEndDate(scServiceDetail.getContractEndDate());
		scServiceDetailBean.setAdditionalIpsReq(components.get("additionalIpsReq"));
		if(CommonConstants.GVPN.equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())
				&& scServiceDetail.getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())){
			LOGGER.info("Inside mapper class for Multi Vrf : {}",scServiceDetail.getUuid());
			scServiceDetailBean.setIsMultiVrf(scServiceDetail.getIsMultiVrf());
			scServiceDetailBean.setMultiVrfSolution(scServiceDetail.getMultiVrfSolution());
			scServiceDetailBean.setMasterVrfServiceId(scServiceDetail.getMasterVrfServiceId());
		}
		return scServiceDetailBean;
	}

	private ScServiceSlaBean mapServiceSlaToBean(ScServiceSla scServiceSla, ScServiceDetailBean scServiceDetailBean) {
		ScServiceSlaBean scServiceSlaBean = new ScServiceSlaBean();
		scServiceSlaBean.setCreatedBy(scServiceSla.getCreatedBy());
		scServiceSlaBean.setCreatedTime(scServiceSla.getCreatedTime());
		scServiceSlaBean.setIsActive(scServiceSla.getIsActive());
		scServiceSlaBean.setSlaComponent(scServiceSla.getSlaComponent());
		scServiceSlaBean.setSlaValue(scServiceSla.getSlaValue());
		scServiceSlaBean.setUpdatedBy(scServiceSla.getUpdatedBy());
		scServiceSlaBean.setUpdatedTime(scServiceSla.getUpdatedTime());
		return scServiceSlaBean;
	}

	private ScServiceAttributeBean mapServiceDetailAttrEntityToBean(ScServiceDetail scServiceDetail, ScServiceAttribute scServiceAttribute) throws TclCommonException {
		ScServiceAttributeBean scServiceAttributeBean = new ScServiceAttributeBean();
		scServiceAttributeBean.setId(scServiceAttribute.getId());
		
		if("CPE Basic Chassis".equalsIgnoreCase(scServiceAttribute.getAttributeName())){
			String additionalServiceParamValue = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, "CPE Basic Chassis");
			if(StringUtils.isNotBlank(additionalServiceParamValue)){
				scServiceAttributeBean.setAttributeValue(additionalServiceParamValue);
			}
		}else if(scServiceDetail.getPriSecServiceLink()!=null && "Resiliency".equalsIgnoreCase(scServiceAttribute.getAttributeName())){
			scServiceAttributeBean.setAttributeValue("Yes");
		}
		else{
			scServiceAttributeBean.setAttributeValue(scServiceAttribute.getAttributeValue());
		}
		scServiceAttributeBean.setAttributeAltValueLabel(scServiceAttribute.getAttributeAltValueLabel());
		scServiceAttributeBean.setAttributeName(scServiceAttribute.getAttributeName());
		scServiceAttributeBean.setCategory(scServiceAttribute.getCategory());
		scServiceAttributeBean.setCreatedBy(scServiceAttribute.getCreatedBy());
		scServiceAttributeBean.setCreatedDate(scServiceAttribute.getCreatedDate());
		scServiceAttributeBean.setIsActive(scServiceAttribute.getIsActive());
		scServiceAttributeBean.setUpdatedBy(scServiceAttribute.getUpdatedBy());
		scServiceAttributeBean.setUpdatedDate(scServiceAttribute.getUpdatedDate());
		scServiceAttributeBean.setIsAdditionalParam(scServiceAttribute.getIsAdditionalParam());
		return scServiceAttributeBean;
	}

	private ScAttachmentBean mapServiceAttachmentEntityToBean(ScAttachment scAttachment) {
		ScAttachmentBean scAttachmentBean = new ScAttachmentBean();
		scAttachmentBean.setId(scAttachment.getId());

		scAttachmentBean.setIsActive(scAttachment.getIsActive());
		scAttachmentBean.setOfferingName(scAttachment.getOfferingName());
		scAttachmentBean.setOrderId(scAttachment.getOrderId());
		scAttachmentBean.setProductName(scAttachment.getProductName());
		scAttachmentBean.setServiceCode(scAttachment.getServiceCode());
		// scAttachmentBean.setErfScServiceId(scAttachment.getServiceCode()); //NOSONAR
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

	public ServiceDetailBean mapServiceDetailAttributes(ServiceDetail serviceDetail, ScServiceDetail scServiceDetail) {
		ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
		if((scServiceDetail.getScOrder().getOpOrderCode().toLowerCase().contains("izosdwan"))
				|| ((scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS") || scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) 
						&& scServiceDetail.getScOrder().getDemoFlag().equalsIgnoreCase("Y") && scServiceDetail.getOrderType().equalsIgnoreCase("MACD")
						&& scServiceDetail.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION"))
				|| ("RENEWALS".equalsIgnoreCase(scServiceDetail.getOrderType()))
				|| ("MACD".equalsIgnoreCase(scServiceDetail.getOrderType()) && scServiceDetail.getOrderSubCategory()!=null && scServiceDetail.getOrderSubCategory().toLowerCase().contains("novation"))){
			LOGGER.info("serviceActivationDetail not exists Izosdwan order::{} with Service Id::{}",scServiceDetail.getScOrder().getOpOrderCode(),scServiceDetail.getId());
			serviceDetailBean.setUuid(scServiceDetail.getUuid());
			serviceDetailBean.setServiceStatus(scServiceDetail.getMstStatus().getCode());
			serviceDetailBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
			serviceDetailBean.setOrderCategory(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder()));
			if(scServiceDetail.getOrderSubCategory() != null && !scServiceDetail.getOrderSubCategory().isEmpty() && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel") && scServiceDetail.getParentUuid() != null && !scServiceDetail.getParentUuid().isEmpty()) {
				serviceDetailBean.setParentUuid(scServiceDetail.getParentUuid());
			}
		}else if(Objects.nonNull(serviceDetail)) {
			LOGGER.info("serviceActivationDetail exists::{}",serviceDetail.getId());
			serviceDetailBean.setScopeOfManagement(serviceDetail.getScopeOfMgmt());
			serviceDetailBean.setSvcLinkServiceId(serviceDetail.getSvclinkSrvid());
			serviceDetailBean.setRedundancyRole(serviceDetail.getRedundancyRole());
			OrderDetail orderDetail = serviceDetail.getOrderDetail();
			if(Objects.nonNull(orderDetail)) {
				serviceDetailBean.setAsdOpportunity(orderDetail.getAsdOpptyFlag());
				serviceDetailBean.setOptyBidCategory(orderDetail.getOptyBidCategory());
			}
			serviceDetailBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
			serviceDetailBean.setOrderCategory(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder()));
			serviceDetailBean.setServiceStatus(scServiceDetail.getMstStatus().getCode());
			serviceDetailBean.setUuid(serviceDetail.getServiceId());
			if(scServiceDetail.getOrderSubCategory() != null && !scServiceDetail.getOrderSubCategory().isEmpty() && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel") && scServiceDetail.getParentUuid() != null && !scServiceDetail.getParentUuid().isEmpty()) {
				serviceDetailBean.setParentUuid(scServiceDetail.getParentUuid());
			}
		}
		return serviceDetailBean;
	}

	public  ScCommercialBean mapServiceCommercialEntityToBean(
			ScServiceCommercial scServiceCommericalComponent) {
		ScCommercialBean scServiceCommercialBean = new ScCommercialBean();
		scServiceCommercialBean.setId(scServiceCommericalComponent.getId());
		scServiceCommercialBean.setMrc(scServiceCommericalComponent.getMrc());
		scServiceCommercialBean.setNrc(scServiceCommericalComponent.getNrc());
		scServiceCommercialBean.setArc(scServiceCommericalComponent.getArc());
		scServiceCommercialBean.setComponentReferenceName(scServiceCommericalComponent.getComponentReferenceName());
		scServiceCommercialBean.setReferenceName(scServiceCommericalComponent.getReferenceName());
		scServiceCommercialBean.setReferenceType(scServiceCommericalComponent.getReferenceType());
		return scServiceCommercialBean;
	}

	public void fetchLMAttributesfromLastMile(OptimusRfDataBean optimusRfDataBean, com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean migratedOrderDetailBean,  Map<String, String> components) {
		if (Objects.nonNull(migratedOrderDetailBean.getServiceDetailBeans())
				&& !migratedOrderDetailBean.getServiceDetailBeans().isEmpty()) {
			Optional<com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean> serviceDetailBeanOptional = migratedOrderDetailBean
					.getServiceDetailBeans().stream().findFirst();
			if (serviceDetailBeanOptional.isPresent()) {
				com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean serviceDetailBean = serviceDetailBeanOptional.get();
				Map<String, String> atMap = new HashMap<>();
				if (Objects.nonNull(serviceDetailBean.getLmComponentBeans())
						&& !serviceDetailBean.getLmComponentBeans().isEmpty()) {
					if (Objects
							.nonNull(serviceDetailBean.getLmComponentBeans().get(0).getCambiumLastmiles())
							&& !serviceDetailBean.getLmComponentBeans().get(0).getCambiumLastmiles()
							.isEmpty()) {
						Optional<com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean> cambiumOptional = serviceDetailBean
								.getLmComponentBeans().get(0).getCambiumLastmiles().stream().findFirst();
						if (cambiumOptional.isPresent()) {
							com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean cambiumLastmileBean = cambiumOptional.get();
							optimusRfDataBean.setApIp(cambiumLastmileBean.getBsIp());
							optimusRfDataBean.setBsName(cambiumLastmileBean.getBsName());
							optimusRfDataBean.setMac(cambiumLastmileBean.getSuMacAddress());
							optimusRfDataBean.setSsIp(cambiumLastmileBean.getMgmtIpForSsSu());
							if(Objects.isNull(optimusRfDataBean.getVendor()) || !(components.get("rfMake").toLowerCase().contains("cambium"))) {
								optimusRfDataBean.setVendor(cambiumLastmileBean.getDeviceType());
								atMap.put("rfMake", cambiumLastmileBean.getDeviceType());
								atMap.put("rfTechnology", cambiumLastmileBean.getDeviceType());
								componentAndAttributeService.updateAttributes(serviceDetailBean.getScServiceDetailId(), atMap,
										AttributeConstants.COMPONENT_LM, "A");}
						}

					} else if (Objects.nonNull(serviceDetailBean.getLmComponentBeans().get(0).getRadwinLastmiles())
							&& !serviceDetailBean.getLmComponentBeans().get(0).getRadwinLastmiles()
							.isEmpty()) {
						Optional<com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean> radwinOptional = serviceDetailBean
								.getLmComponentBeans().get(0).getRadwinLastmiles().stream().findFirst();
						if (radwinOptional.isPresent()) {
							com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean radwinLastmileBean = radwinOptional.get();
							optimusRfDataBean.setSsIp(radwinLastmileBean.getHsuIp());
							optimusRfDataBean.setApIp(radwinLastmileBean.getBtsIp());
							optimusRfDataBean.setBsName(radwinLastmileBean.getBtsName());
							optimusRfDataBean.setSectorId(radwinLastmileBean.getSectorId());
							optimusRfDataBean.setMac(radwinLastmileBean.getHsuMacAddr());
							if (Objects.isNull(optimusRfDataBean.getLatitude()))
								optimusRfDataBean.setLatitude(radwinLastmileBean.getSiteLat());
							if (Objects.isNull(optimusRfDataBean.getLongitude()))
								optimusRfDataBean.setLongitude(radwinLastmileBean.getSiteLong());
							/*if(Objects.isNull(optimusRfDataBean.getVendor()) || !(components.get("rfMake").toLowerCase().contains("radwin")))
							{
								if(!optimusRfDataBean.getVendor().toLowerCase().contains("radwin"))
									optimusRfDataBean.setVendor("Radwin");
								atMap.put("rfMake", "Radwin");
								atMap.put("rfTechnology", "Radwin");
								componentAndAttributeService.updateAttributes(serviceDetailBean.getScServiceDetailId(), atMap,
										AttributeConstants.COMPONENT_LM, "A");
							}*/

						}
					}
				}
			}

		}
	}

	private String getServiceType(ScServiceDetail scServiceDetail) {
		if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS"))
			return "ILL";

		else
			return scServiceDetail.getErfPrdCatalogProductName();
	}
	private String setLastMileBwInKbps(String lastmileBw,String lastmileBwUnit) {
		if ("Mbps".equalsIgnoreCase(lastmileBwUnit))
			lastmileBw = String.valueOf(Float.valueOf(lastmileBw) * 1024);
		else if ("Gbps".equalsIgnoreCase(lastmileBwUnit))
			lastmileBw = String.valueOf(Float.valueOf(lastmileBw) * 1024 * 1024);
		return lastmileBw;
	}

	@Transactional(readOnly = false)
	public OptimusRfDataBean E2EPMPRFtoInv(String serviceCode, Map<String, String> components) throws TclCommonException {

			ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
			TaskBean migratedTaskBean = new TaskBean();
			String wirelessScenario = null;
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "ACTIVE");
			if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"INPPROGRESS");
			ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"solution_type");
			if(Objects.isNull(scServiceAttribute)) scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"closest_provider_bso_name");
			if(Objects.nonNull(scServiceAttribute)) wirelessScenario= scServiceAttribute.getAttributeValue();
			OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
			if(!StringUtils.isEmpty(wirelessScenario))
				optimusRfDataBean.setProvider(wirelessScenario);
			String lmType = components.get("lmType");
			if (org.springframework.util.StringUtils.isEmpty(lmType) && Objects.nonNull(scServiceDetail.getAccessType())) lmType=scServiceDetail.getAccessType();
			if (("OnnetRf".equalsIgnoreCase(lmType) || ("onnet wireless".equalsIgnoreCase(lmType)))
					&&	(Objects.nonNull(wirelessScenario) &&
					!(wirelessScenario.equalsIgnoreCase("Radwin from TCL POP"))
					&& (wirelessScenario.toLowerCase().contains("pmp")))) {
				optimusRfDataBean.setEthernetExtender(components.getOrDefault("ethernetExtenderUsed", "No"));
				MstCambiumDetails mstCambiumDetails = mstCambiumDetailsRepository
						.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode, "Y");
				if (Objects.nonNull(mstCambiumDetails)) {
					logger.info("RF CAMBIUM MIGRATED ORDER to ES Inventory");

					ServiceDetail serviceDetail = new ServiceDetail();
					serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,
							Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE));
					if ((Objects.nonNull(serviceDetail)) && !org.springframework.util.StringUtils.isEmpty(serviceDetail)) {
						optimusRfDataBean.setCustomerAddress(serviceDetail.getAddressLine1());
						optimusRfDataBean.setServiceType(serviceDetail.getServiceType());
						optimusRfDataBean.setActionType(Objects.nonNull(scServiceDetail.getOrderSubCategory()) ? scServiceDetail.getOrderSubCategory() : serviceDetail.getOrderSubCategory());
						ScOrder scOrder = scServiceDetail.getScOrder();
						String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);						if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
							optimusRfDataBean.setTypeOfOrder(orderCategory);
						optimusRfDataBean.setCustomerName(scOrder.getErfCustLeName());
						Map<String, String> attributeMap = commonFulfillmentUtils
								.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
						String latLong = attributeMap.get("latLong");
						if (!org.springframework.util.StringUtils.isEmpty(latLong)) {
							String[] latlong = latLong.split(",");
							optimusRfDataBean.setLatitude(latlong[0]);
							optimusRfDataBean.setLongitude(latlong[1]);
						}
						String city = attributeMap.get("destinationCity");
						if (org.springframework.util.StringUtils.isEmpty(city))
							city=scServiceDetail.getDestinationCity();
						if (!org.springframework.util.StringUtils.isEmpty(city))
							optimusRfDataBean.setCity(city);
						String state = attributeMap.get("destinationState");
						if (org.springframework.util.StringUtils.isEmpty(state))
							state=scServiceDetail.getDestinationState();
						if (!org.springframework.util.StringUtils.isEmpty(state))
							optimusRfDataBean.setState(state);
						String structureType = attributeMap.get("structureType");
						if (!org.springframework.util.StringUtils.isEmpty(structureType))
							optimusRfDataBean.setStructureType(structureType);
						String rfMakeModel = attributeMap.get("rfMake");
						if (!org.springframework.util.StringUtils.isEmpty(rfMakeModel))
							optimusRfDataBean.setVendor(rfMakeModel);
						else
							optimusRfDataBean.setVendor(mstCambiumDetails.getDeviceType());
						String bwUnit = serviceDetail.getServiceBandwidthUnit();
						Float portBandwidth = serviceDetail.getServiceBandwidth();
						if ((!org.springframework.util.StringUtils.isEmpty(bwUnit))
								&& (!org.springframework.util.StringUtils.isEmpty(portBandwidth))) {
							if ("Mbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024));
							else if ("Gbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024 * 1024));
							else
								optimusRfDataBean.setQosBw(String.valueOf(portBandwidth));
						}
					}
					migratedTaskBean = getIpServiceDetails(scServiceDetail.getUuid());
					com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
					if (migratedOrderDetailBean != null) fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean, components);
					optimusRfDataBean.setDeviceType(null);
					optimusRfDataBean.setCircuitId(mstCambiumDetails.getServiceCode());
					if(Objects.isNull(optimusRfDataBean.getMac())) optimusRfDataBean.setMac(mstCambiumDetails.getHsuMac());
					if(Objects.isNull(optimusRfDataBean.getApIp())) optimusRfDataBean.setApIp(mstCambiumDetails.getBtsIp());
					if(Objects.isNull(optimusRfDataBean.getSsIp())) optimusRfDataBean.setSsIp(mstCambiumDetails.getHsuIp());
					String sectorId= components.getOrDefault("sectorId","1");
					optimusRfDataBean.setSectorId(Objects.nonNull(mstCambiumDetails.getSectorId()) ? mstCambiumDetails.getSectorId() : sectorId);
					if(Objects.isNull(optimusRfDataBean.getBsName())) optimusRfDataBean.setBsName(mstCambiumDetails.getBtsName());
					optimusRfDataBean.setAntennaHeight(mstCambiumDetails.getAntennaHeight());

				}
				 else {
					MstRadwinDetails mstRadwinDetails = mstRadwinDetailsRepository
							.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode, "Y");
					if (Objects.nonNull(mstRadwinDetails)) {
						logger.info("RF RADWIN MIGRATED ORDER to ES Inventory");
						ServiceDetail serviceDetail = new ServiceDetail();
						serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,
								Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE));
						if ((Objects.nonNull(serviceDetail))
								&& !org.springframework.util.StringUtils.isEmpty(serviceDetail)) {
							optimusRfDataBean.setCustomerAddress(serviceDetail.getAddressLine1());
							optimusRfDataBean.setServiceType(serviceDetail.getServiceType());
							optimusRfDataBean.setActionType(Objects.nonNull(scServiceDetail.getOrderSubCategory()) ? scServiceDetail.getOrderSubCategory() : serviceDetail.getOrderSubCategory());
							ScOrder scOrder = scServiceDetail.getScOrder();
							String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
							if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
								optimusRfDataBean.setTypeOfOrder(orderCategory);
							optimusRfDataBean.setCustomerName(scOrder.getErfCustLeName());
							Map<String, String> attributeMap = commonFulfillmentUtils
									.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
							String latLong = attributeMap.get("latLong");
							if (!org.springframework.util.StringUtils.isEmpty(latLong)) {
								String[] latlong = latLong.split(",");
								optimusRfDataBean.setLatitude(latlong[0]);
								optimusRfDataBean.setLongitude(latlong[1]);
							}
							String city = attributeMap.get("destinationCity");
							if (org.springframework.util.StringUtils.isEmpty(city))
								city=scServiceDetail.getDestinationCity();
							if (!org.springframework.util.StringUtils.isEmpty(city))
								optimusRfDataBean.setCity(city);
							String state = attributeMap.get("destinationState");
							if (org.springframework.util.StringUtils.isEmpty(state))
								state=scServiceDetail.getDestinationState();
							if (!org.springframework.util.StringUtils.isEmpty(state))
								optimusRfDataBean.setState(state);
							String structureType = attributeMap.get("structureType");
							if (!org.springframework.util.StringUtils.isEmpty(structureType))
								optimusRfDataBean.setStructureType(structureType);
							String rfMakeModel = attributeMap.get("rfMakeModel");
							if (!org.springframework.util.StringUtils.isEmpty(rfMakeModel))
								optimusRfDataBean.setVendor(rfMakeModel);
							else
								optimusRfDataBean.setVendor(mstRadwinDetails.getDeviceType());
							String bwUnit = serviceDetail.getServiceBandwidthUnit();
							Float portBandwidth = serviceDetail.getServiceBandwidth();
							if ((!org.springframework.util.StringUtils.isEmpty(bwUnit))
									&& (!org.springframework.util.StringUtils.isEmpty(portBandwidth))) {
								if ("Mbps".equalsIgnoreCase(bwUnit))
									optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024));
								else if ("Gbps".equalsIgnoreCase(bwUnit))
									optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024 * 1024));
								else
									optimusRfDataBean.setQosBw(String.valueOf(portBandwidth));
							}
						}
						migratedTaskBean = getIpServiceDetails(scServiceDetail.getUuid());
						com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
						if (migratedOrderDetailBean != null) fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean, components);
						optimusRfDataBean.setDeviceType(null);
						optimusRfDataBean.setCircuitId(mstRadwinDetails.getServiceCode());
						if(Objects.isNull(optimusRfDataBean.getMac())) optimusRfDataBean.setMac(mstRadwinDetails.getHsuMac().toLowerCase());
						if(Objects.isNull(optimusRfDataBean.getApIp())) optimusRfDataBean.setApIp(mstRadwinDetails.getBtsIp());
						if(Objects.isNull(optimusRfDataBean.getSsIp())) optimusRfDataBean.setSsIp(mstRadwinDetails.getHsuIp());
						String sectorId= components.getOrDefault("sectorId","1");
						if(Objects.isNull(optimusRfDataBean.getSectorId()))
							optimusRfDataBean.setSectorId(Objects.nonNull(mstRadwinDetails.getSectorId()) ? mstRadwinDetails.getSectorId() : sectorId);
						if(Objects.isNull(optimusRfDataBean.getBsName())) optimusRfDataBean.setBsName(mstRadwinDetails.getBtsName());
						optimusRfDataBean.setAntennaHeight(mstRadwinDetails.getAntennaHeight());

					}
					else
						{
						migratedTaskBean = getIpServiceDetails(scServiceDetail.getUuid());
						logger.info("RF MigratedTaskBean MIGRATED ORDER to ES Inventory");
						com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
						if (migratedOrderDetailBean != null) {
							optimusRfDataBean = rfService.enrichRfDetails(scServiceDetail, "ES");
							fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean, components);
						}
					}
				}
			}
			setOptimusRfTaskStageData(optimusRfDataBean);
			optimusRfDataBean.setActionType(scServiceDetail.getOrderSubCategory());
			optimusRfDataBean.setCommissionDate(scServiceDetail.getCommissionedDate().toString());
			optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

			optimusRfDataBean.setLmAction(orderType);
			optimusRfDataBean.setLmType(orderType);
			return optimusRfDataBean;
		}

	@Transactional(readOnly = false)
	public TaskBean getIpServiceDetails( String serviceCode)  {

		ServiceDetail serviceDetail = new ServiceDetail();
		TaskBean taskBean = new TaskBean();

		boolean required = true;
		serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,
					Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE));

		com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean bean = new com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean();
		if(Objects.nonNull(serviceDetail)){
			com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean serviceDetailBean = new com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean();
			constructLmComponentDetails(serviceDetail, serviceDetailBean, required);
			bean.getServiceDetailBeans().add(serviceDetailBean);
		}

		taskBean.setOrderDetails(bean);
		return taskBean;
	}


	private void constructLmComponentDetails(ServiceDetail serviceDetail, com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean serviceDetailBean, boolean required) {
		if(!CollectionUtils.isEmpty(serviceDetail.getLmComponents())){
			serviceDetail.getLmComponents().forEach(lmComponent -> {
				if(required || Objects.isNull(lmComponent.getEndDate())){
					com.tcl.dias.servicefulfillmentutils.beans.LmComponentBean lmComponentBean = new com.tcl.dias.servicefulfillmentutils.beans.LmComponentBean();
					setLmComponentBean(lmComponentBean, lmComponent);
					if(!CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())){
						lmComponent.getRadwinLastmiles().forEach(radwinLastmile -> {
							if(required || Objects.isNull(radwinLastmile.getEndDate())) {
								com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean radwinLastmileBean = new com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean();
								setRadwinLastmileBean(radwinLastmileBean, radwinLastmile);
								lmComponentBean.getRadwinLastmiles().add(radwinLastmileBean);
							}
						});
					}
					if(!CollectionUtils.isEmpty(lmComponent.getCambiumLastmiles())){
						lmComponent.getCambiumLastmiles().forEach(cambiumLastmile -> {
							if(required || Objects.isNull(cambiumLastmile.getEndDate())){
								com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean cambiumLastmileBean = new com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean();
								setCambiumLastmileBean(cambiumLastmileBean, cambiumLastmile);
								lmComponentBean.getCambiumLastmiles().add(cambiumLastmileBean);
							}
						});
					}

					serviceDetailBean.getLmComponentBeans().add(lmComponentBean);
				}
			});
		}
	}

	public void setLmComponentBean(LmComponentBean lmComponentBean, LmComponent lmComponent) {
		lmComponentBean.setLmComponentId(lmComponent.getLmComponentId());
		lmComponentBean.setLmOnwlProvider(lmComponent.getLmOnwlProvider());
		lmComponentBean.setModifiedBy(lmComponent.getModifiedBy());
		lmComponentBean.setRemarks(lmComponent.getRemarks());
		lmComponentBean.setVersion(lmComponent.getVersion());
		lmComponentBean.setStartDate(lmComponent.getStartDate());
		lmComponentBean.setEndDate(lmComponent.getEndDate());
		lmComponentBean.setLastModifiedDate(lmComponent.getLastModifiedDate());
	}

	public com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean setRadwinLastmileBean(com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean bean, RadwinLastmile radwinLastmile) {
		bean.setRadwinLastmileId(radwinLastmile.getRadwinLastmileId());
		bean.setAllowedVlanid(radwinLastmile.getAllowedVlanid());
		bean.setBtsIp(radwinLastmile.getBtsIp());
		bean.setBtsName(radwinLastmile.getBtsName());
		bean.setCustomerLocation(radwinLastmile.getCustomerLocation());
		bean.setDataVlanPriority(radwinLastmile.getDataVlanPriority());
		bean.setDataVlanid(radwinLastmile.getDataVlanid());
		bean.setEndDate(radwinLastmile.getEndDate());
		bean.setEthernet_Port_Config(radwinLastmile.getEthernet_Port_Config());
		bean.setFrequency(radwinLastmile.getFrequency());
		bean.setGatewayIp(radwinLastmile.getGatewayIp());
		bean.setHsuEgressTraffic(radwinLastmile.getHsuEgressTraffic());
		bean.setHsuIngressTraffic(radwinLastmile.getHsuIngressTraffic());
		bean.setHsuIp(radwinLastmile.getHsuIp());
		bean.setHsuMacAddr(radwinLastmile.getHsuMacAddr());
		bean.setHsuSubnet(radwinLastmile.getHsuSubnet());
		bean.setLastModifiedDate(radwinLastmile.getLastModifiedDate());
		bean.setMgmtVlanid(radwinLastmile.getMgmtVlanid());
		bean.setMirDl(radwinLastmile.getMirDl());
		bean.setMirUl(radwinLastmile.getMirUl());
		bean.setModifiedBy(radwinLastmile.getModifiedBy());
		bean.setNtpOffset(radwinLastmile.getNtpOffset());
		bean.setNtpServerIp(radwinLastmile.getNtpServerIp());
		bean.setProtocolSnmp(radwinLastmile.getProtocolSnmp());
		bean.setProtocolTelnet(radwinLastmile.getProtocolTelnet());
		bean.setProtocolWebinterface(radwinLastmile.getProtocolWebinterface());
		bean.setReqd_tx_power(radwinLastmile.getReqd_tx_power());
		bean.setSectorId(radwinLastmile.getSectorId());
		bean.setSiteContact(radwinLastmile.getSiteContact());
		bean.setSiteLat(radwinLastmile.getSiteLat());
		bean.setSiteLong(radwinLastmile.getSiteLong());
		bean.setSiteName(radwinLastmile.getSiteName());
		bean.setStartDate(radwinLastmile.getStartDate());
		bean.setUntagVlanId(radwinLastmile.getUntagVlanId());
		bean.setVlanMode(radwinLastmile.getVlanMode());

		return bean;
	}

	public com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean setCambiumLastmileBean(com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean bean, CambiumLastmile cambiumLastmile) {
		bean.setAuthenticationKey(cambiumLastmile.getAuthenticationKey());
		bean.setBsIp(cambiumLastmile.getBsIp());
		bean.setBsName(cambiumLastmile.getBsName());
		bean.setBwDownlinkSustainedRate(cambiumLastmile.getBwDownlinkSustainedRate());
		bean.setBwUplinkSustainedRate(cambiumLastmile.getBwDownlinkSustainedRate());
		bean.setCambiumLastmileId(cambiumLastmile.getCambiumLastmileId());
		if(cambiumLastmile.getColorCode1()!=null) {
			bean.setColorCode1(cambiumLastmile.getColorCode1().toString());
		}
		bean.setCustomRadioFrequencyList(cambiumLastmile.getCustomRadioFrequencyList());
		bean.setDefaultPortVid(cambiumLastmile.getDefaultPortVid());
		bean.setDeviceType(cambiumLastmile.getDeviceType());
		bean.setDownlinkBurstAllocation(cambiumLastmile.getDownlinkBurstAllocation());
		bean.setEndDate(cambiumLastmile.getEndDate());
		bean.setEnforceAuthentication(cambiumLastmile.getEnforceAuthentication());
		bean.setFrameTimingPulseGated((byte) 1);

		bean.setHipriorityChannel(cambiumLastmile.getHipriorityChannel());
		bean.setHipriorityDownlinkCir(cambiumLastmile.getHipriorityDownlinkCir());
		bean.setHipriorityUplinkCir(cambiumLastmile.getHipriorityUplinkCir());
		bean.setHomeRegion(cambiumLastmile.getHomeRegion());
		if (cambiumLastmile.getInstallationColorCode() == "Enable") {
			bean.setInstallationColorCode((byte) 1);
		} else {
			bean.setInstallationColorCode((byte) 0);
		}
		bean.setLastModifiedDate(cambiumLastmile.getLastModifiedDate());
		bean.setLatitudeSettings(cambiumLastmile.getLatitudeSettings());
		bean.setLinkSpeed(cambiumLastmile.getLinkSpeed());
		bean.setLongitudeSettings(cambiumLastmile.getLongitudeSettings());
		bean.setLowpriorityDownlinkCir(cambiumLastmile.getLowpriorityDownlinkCir());
		bean.setLowpriorityUplinkCir(cambiumLastmile.getLowpriorityUplinkCir());
		bean.setMappedVid2(cambiumLastmile.getMappedVid2()!=null ? new Integer(cambiumLastmile.getMappedVid2()).toString() :null);
		bean.setMgmtIpForSsSu(cambiumLastmile.getMgmtIpForSsSu());
		bean.setMgmtIpGateway(cambiumLastmile.getMgmtIpGateway());
		bean.setMgmtSubnetForSsSu(cambiumLastmile.getMgmtSubnetForSsSu());
		bean.setMgmtVid(cambiumLastmile.getMgmtVid());
		bean.setModifiedBy(cambiumLastmile.getModifiedBy());
		bean.setPortSpeed(cambiumLastmile.getPortSpeed());
		bean.setPortSpeedUnit(cambiumLastmile.getPortSpeedUnit());
		bean.setProviderVid(cambiumLastmile.getProviderVid()!=null ? new Integer(cambiumLastmile.getProviderVid()).toString() :null);
		bean.setRealm(cambiumLastmile.getRealm());
		bean.setRegion(cambiumLastmile.getRegion());
		bean.setRegionCode(cambiumLastmile.getRegionCode());
		bean.setSiteContact(cambiumLastmile.getSiteContact());
		bean.setSiteLocation(cambiumLastmile.getSiteLocation());
		bean.setSiteName(cambiumLastmile.getSiteName());
		bean.setSmHeight(cambiumLastmile.getSmHeight());
		bean.setStartDate(cambiumLastmile.getStartDate());
		bean.setSuMacAddress(cambiumLastmile.getSuMacAddress());
		bean.setUplinkBurstAllocation(cambiumLastmile.getUplinkBurstAllocation());
		bean.setDownlinkPlan(cambiumLastmile.getDownlinkPlan());
		bean.setUplinkPlan(cambiumLastmile.getUplinkPlan());
		bean.setWeight(cambiumLastmile.getWeight());
		bean.setUserLockModulation(cambiumLastmile.getUserLockModulation());
		bean.setLockModulation(cambiumLastmile.getLockModulation());
		bean.setThersholdModulation(cambiumLastmile.getThersholdModulation());
		bean.setPrioritizationGroup(cambiumLastmile.getPrioritizationGroup());
		return bean;
	}

	public void setOptimusRfTaskStageData(OptimusRfDataBean optimusRfDataBean) {
		optimusRfDataBean.setLastUpdatedBy("OPTIMUS");
		optimusRfDataBean.setLastUpdatedDate((new Timestamp(new Date().getTime())).toString());
		optimusRfDataBean.setTaskStage("ES");
		optimusRfDataBean.setStatus("ACTIVE");
		optimusRfDataBean.setServiceStatus("ACTIVE");
	}
}
