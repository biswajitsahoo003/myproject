package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscCommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetWithStatus;
import com.tcl.dias.servicefulfillment.entity.custom.IGscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroupToAsset;
import com.tcl.dias.servicefulfillment.entity.entities.MstPartnerAbbreviation;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetRelation;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AuditLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupRepository;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupToAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstPartnerAbbreviationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRelationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.CNMAttributeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.CityWiseQuantity;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsByCallTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.GetRoutingNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.InRoutesbean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.OutRouteEndPointsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderSupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.ReserveVanityNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.RoutingDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SuppliersBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SwitchingDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GscService {

	private static final Logger logger = LoggerFactory.getLogger(GscService.class);

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScAssetRepository scAssetRepository;

	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;

	@Autowired
	ScAssetRelationRepository scAssetRelationRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	AuditLogRepository auditLogRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;

	@Autowired
	GscFlowGroupToAssetRepository gscFlowGroupToAssetRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	MstPartnerAbbreviationRepository mstPartnerAbbreviationRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;

	public AuditLog saveAuditLog(String request, String response, String serviceCode, String type, String requestId) {
		AuditLog auditLog = new AuditLog();
		auditLog.setRequest(request);
		auditLog.setResponse(response);
		auditLog.setType(type);
		auditLog.setServiceCode(serviceCode);
		auditLog.setRequestId(requestId);
		auditLog.setCreatedDate(new Timestamp(new Date().getTime()));
		return auditLogRepository.save(auditLog);
	}

	public void updateAuditLog(AuditLog auditLog, String response) {
		auditLog.setResponse(response);
		auditLogRepository.save(auditLog);
	}

	public boolean isReservedNoAvailable(Integer serviceId) {
		String reservedNumberId = getReservationId(serviceId);
		if (reservedNumberId != null && !reservedNumberId.isEmpty()) {
			return true;
		}
		return false;
	}

	public String getReservationId(Integer serviceId) {
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		return scComponentAttributesmap.getOrDefault("tfnreservationid", null);
	}

	public boolean isPortingNoAvailable(Integer serviceId) {
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		Integer portingNumber = 0;
		if(!scComponentAttributesmap.getOrDefault("listOfNumbersToBePorted", "0").isEmpty()) {
			try {
				portingNumber = Integer.parseInt(scComponentAttributesmap.getOrDefault("listOfNumbersToBePorted", "0"));
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (portingNumber > 0) {
			return true;
		}
		return false;
	}

	public Integer getRequiredNumberQty(Integer serviceId) throws NumberFormatException {
		// Map<String, String> scComponentAttributesmap =
		// commonFulfillmentUtils.getComponentAttributes(serviceId,
		// AttributeConstants.COMPONENT_GSCLICENSE, "A");
		// Integer totalNumber =
		// Integer.parseInt(scComponentAttributesmap.getOrDefault("Quantity Of Numbers",
		// "0"));
		// Integer portingNumber =
		// Integer.parseInt(scComponentAttributesmap.getOrDefault("List of numbers to be
		// ported", "0"));
		// return totalNumber - portingNumber;
		return scAssetRepository.findRequiredQty(serviceId);
	}

	public Integer getTotalNumberQty(Integer serviceId) throws NumberFormatException {
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		Integer totalNumber = Integer.parseInt(scComponentAttributesmap.getOrDefault("quantityOfNumbers", "0"));
		return totalNumber;
	}

	public void persistComponentAttributes(Integer serviceId, Map<String, String> atMap) {
//		componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		componentAndAttributeService.updateComponentAndAdditionalAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
	}

	public void persistGetSuppliersResponse(Integer serviceId, String responseJson) {
		Map<String, String> atMap = new HashMap<>();
		atMap.put("getSuppliersRes", responseJson);
		persistComponentAttributes(serviceId, atMap);
	}
	
	public void persistGetSitesResponse(Integer serviceId, String responseJson) {
		Map<String, String> atMap = new HashMap<>();
		atMap.put("filteredSitesResponse", responseJson);
		persistComponentAttributes(serviceId, atMap);
	}
	
	public void persistCreateNewSiteResponse(Integer serviceId, String custRequest,String repcResponse,String siteId) {
        Map<String, String> atMap = new HashMap<>();
        atMap.put("createdNewSiteRequest", custRequest);
        atMap.put("createdNewSiteResponse", repcResponse);
        atMap.put("selectedSiteId", siteId);
        persistComponentAttributes(serviceId, atMap);
        
    }

	public SuppliersBean getSuppliersSelected(Integer serviceId, String taskDefKey) {
		SuppliersBean vasSupplierBean = new SuppliersBean();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String suppliersSelected;
		if(taskDefKey.contains("port")) {
			suppliersSelected = scComponentAttributesmap.getOrDefault("suppliersSelected-Port", null);
		} else {
			suppliersSelected = scComponentAttributesmap.getOrDefault("suppliersSelected", null);
		}
		if (suppliersSelected != null) {
			vasSupplierBean = Utils.fromJson(suppliersSelected, new TypeReference<SuppliersBean>() {
			});
		}
		return vasSupplierBean;
	}

	public String getPlaceOrderSuppliersFlow(Integer serviceId) {
		List<String> attributes = new ArrayList<String>();
		attributes.add("supplierOrderFlow");
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A", attributes);
		return scComponentAttributesmap.getOrDefault("supplierOrderFlow", null);
	}

	public List<PlaceOrderSupplierBean> getPlaceOrderSuppliersDetails(Integer serviceId) {
		List<PlaceOrderSupplierBean> suppliers = new ArrayList<PlaceOrderSupplierBean>();
		List<String> attributes = new ArrayList<String>();
		attributes.add("placeOrderRes");
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A", attributes);
		String suppliersSelected = scComponentAttributesmap.getOrDefault("placeOrderRes", null);
		if (suppliersSelected != null) {
			suppliers = Utils.fromJson(suppliersSelected, new TypeReference<List<PlaceOrderSupplierBean>>() {
			});
		}
		return suppliers;
	}

	public List<ScServiceDetail> getChildServiceDetails(Integer parentServiceId) {
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByParentId(parentServiceId);
		return serviceDetails;
	}

	public List<Integer> getCountryLevelFlowService(Integer parentServiceId) {
		List<Integer> serviceIds = new ArrayList<>();
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByParentId(parentServiceId);
		for (ScServiceDetail serviceDetail : serviceDetails) {
			serviceIds.add(serviceDetail.getId());
		}
		return serviceIds;
	}

	public ScAsset createScAsset(Integer serviceId, String number, String type, String userName) {
		Optional<ScServiceDetail> scOptional = scServiceDetailRepository.findById(serviceId);
		if (scOptional.isPresent()) {
			return generateScAsset(scOptional.get(), number, type, userName);
		}
		return null;
	}
	
	public ScAsset createScAssetWithStatus(Integer serviceId, String number, String type, String userName,String status) {
		Optional<ScServiceDetail> scOptional = scServiceDetailRepository.findById(serviceId);
		if (scOptional.isPresent()) {
			return generateScAssetWithStatus(scOptional.get(), number, type, userName,status);
		}
		return null;
	}

	public ScAsset generateScAsset(ScServiceDetail scServiceDetail, String number, String type, String userName) {
		ScAsset scAsset = new ScAsset();
		scAsset.setScServiceDetail(scServiceDetail);
		scAsset.setName(number);
		scAsset.setFqdn(StringUtils.EMPTY);
		scAsset.setPublicIp(StringUtils.EMPTY);
		scAsset.setIsActive(CommonConstants.Y);
		scAsset.setCreatedBy(userName);
		scAsset.setUpdatedBy(userName);
		scAsset.setIsSharedInd(CommonConstants.N);
		scAsset.setOriginnetwork("Fixed");
		scAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		scAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
		scAsset.setType(type);
		return scAsset;
	}
	
	public ScAsset generateScAssetWithStatus(ScServiceDetail scServiceDetail, String number, String type, String userName,String status) {
		ScAsset scAsset = new ScAsset();
		scAsset.setScServiceDetail(scServiceDetail);
		scAsset.setName(number);
		scAsset.setFqdn(StringUtils.EMPTY);
		scAsset.setPublicIp(StringUtils.EMPTY);
		scAsset.setIsActive(CommonConstants.Y);
		scAsset.setCreatedBy(userName);
		scAsset.setUpdatedBy(userName);
		scAsset.setIsSharedInd(CommonConstants.N);
		scAsset.setStatus(status);
		scAsset.setOriginnetwork("Fixed");
		scAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		scAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
		scAsset.setType(type);
		return scAsset;
	}

	public ScAssetAttribute generateScAssetsAttribute(Integer scAssetId, String attributeName, String attributeValue,
			String userName) {
		Optional<ScAsset> scAssetOptional = scAssetRepository.findById(scAssetId);
		if (scAssetOptional.isPresent()) {
			return generateScAssetsAttribute(scAssetOptional.get(), attributeName, attributeValue, userName);
		}
		return null;
	}

	public ScAssetAttribute generateScAssetsAttribute(ScAsset scAsset, String attributeName, String attributeValue,
			String userName) {
		ScAssetAttribute scAssetAttribute = new ScAssetAttribute();
		scAssetAttribute.setAttributeName(attributeName);
		scAssetAttribute.setAttributeValue(attributeValue);
		scAssetAttribute.setIsActive(CommonConstants.Y);
		scAssetAttribute.setCreatedBy(userName);
		scAssetAttribute.setUpdatedBy(userName);
		scAssetAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scAssetAttribute.setScAsset(scAsset);
		return scAssetAttribute;
	}
	
	public ScAssetAttribute generateScAssetsAttributeAdditionalAttributes(ScAsset scAsset, String attributeName, String attributeValue,
			String userName) {
		ScAdditionalServiceParam scAdditionalServiceParam = generateScAdditionalServiceParam(attributeName, attributeValue, userName);
		
		ScAssetAttribute scAssetAttribute = new ScAssetAttribute();
		scAssetAttribute.setAttributeName(attributeName);
		scAssetAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
		scAssetAttribute.setIsActive(CommonConstants.Y);
		scAssetAttribute.setCreatedBy(userName);
		scAssetAttribute.setUpdatedBy(userName);
		scAssetAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scAssetAttribute.setIsAdditionalParam(CommonConstants.Y);
		scAssetAttribute.setScAsset(scAsset);
		return scAssetAttribute;
	}
	
	public ScAdditionalServiceParam generateScAdditionalServiceParam(String attributeName, String attributeValue,
			String userName) {
		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setAttribute(attributeName);
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setValue(attributeValue);
		return scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
	}

	public ScAssetRelation generateScAssetRelations(Integer scAsset, Integer relatedScAsset, String relationshipType,
			String userName) {
		ScAssetRelation scAssetRelation = new ScAssetRelation();
		scAssetRelation.setScAssetId(scAsset);
		scAssetRelation.setScRelatedAssetId(relatedScAsset);
		scAssetRelation.setRelationType(relationshipType);
		scAssetRelation.setIsActive(CommonConstants.Y);
		scAssetRelation.setCreatedBy(userName);
		scAssetRelation.setCreatedDate(new Timestamp(new Date().getTime()));
		scAssetRelation.setUpdatedBy(userName);
		scAssetRelation.setUpdatedDate(new Timestamp(new Date().getTime()));
		return scAssetRelation;
	}

	public ScAsset updatedScAsset(Integer scAssetId, String number, String userName) {
		Optional<ScAsset> scAssetOptional = scAssetRepository.findById(scAssetId);
		if (scAssetOptional.isPresent()) {
			ScAsset scAsset = scAssetOptional.get();
			scAsset.setName(number);
			scAsset.setUpdatedBy(userName);
			scAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
			scAssetRepository.save(scAsset);
			return scAsset;
		}
		return null;
	}

	public ScAsset updatedScAssetStatus(Integer scAssetId, String status, String userName) {
		Optional<ScAsset> scAssetOptional = scAssetRepository.findById(scAssetId);
		if (scAssetOptional.isPresent()) {
			ScAsset scAsset = scAssetOptional.get();
			scAsset.setStatus(status);
			scAsset.setUpdatedBy(userName);
			scAsset.setUpdatedDate(new Timestamp(new Date().getTime()));
			scAssetRepository.save(scAsset);
			return scAsset;
		}
		return null;
	}

	public ScAssetAttribute updatedScAssetAttribute(Integer scAssetId, String attributeName, String attributeValue,
			String userName) {
		Optional<ScAsset> scOptional = scAssetRepository.findById(scAssetId);
		if (scOptional.isPresent()) {
			return updatedScAssetAttribute(scOptional.get(), attributeName, attributeValue, userName);
		}
		return null;
	}

	public ScAssetAttribute updatedScAssetAttribute(ScAsset scAsset, String attributeName, String attributeValue,
			String userName) {
		ScAssetAttribute scAssetAttribute = scAssetAttributeRepository.findByScAsset_IdAndAttributeName(scAsset.getId(),
				attributeName);
		if (scAssetAttribute != null) {
			if (!StringUtils.isEmpty(attributeValue) && "Y".equalsIgnoreCase(scAssetAttribute.getIsAdditionalParam())) {
				Integer addServiceParamId = StringUtils.isEmpty(scAssetAttribute.getAttributeValue())?0:Integer.valueOf(scAssetAttribute.getAttributeValue());
				Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
				if(optScAddServParam.isPresent() && !attributeValue.equals(optScAddServParam.get().getValue())) {
					ScAdditionalServiceParam scAddServParam = optScAddServParam.get();
					scAddServParam.setValue(attributeValue);
					scAdditionalServiceParamRepository.save(scAddServParam);
				}
			} else {
				if(attributeValue.length()>45) {
					ScAdditionalServiceParam scAdditionalServiceParam = generateScAdditionalServiceParam(attributeName, attributeValue, userName);
					scAssetAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					scAssetAttribute.setIsAdditionalParam(CommonConstants.Y);
				} else {
					scAssetAttribute.setAttributeValue(attributeValue);
				}
				scAssetAttribute.setUpdatedBy(userName);
				scAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			}
		} else {
			if(attributeValue.length()>45) {
				scAssetAttribute = generateScAssetsAttributeAdditionalAttributes(scAsset, attributeName, attributeValue, userName);
			} else {
				scAssetAttribute = generateScAssetsAttribute(scAsset, attributeName, attributeValue, userName);
			}
		}
		scAssetAttributeRepository.save(scAssetAttribute);
		return scAssetAttribute;
	}

	public void updatedScAssetAttribute(Integer scAssetId, Map<String, String> atMap, String userName) {
		Optional<ScAsset> scOptional = scAssetRepository.findById(scAssetId);
		if (scOptional.isPresent()) {
			updatedScAssetAttribute(scOptional.get(), atMap, userName);
		}
	}

	public void updatedScAssetAttribute(ScAsset scAsset, Map<String, String> atMap, String userName) {
		atMap.forEach((key, value) -> {
			logger.info("Key : {}, Value : {}", key, value);
			updatedScAssetAttribute(scAsset, key, value, userName);
		});
	}

	public void persistReservedVanityNumber(Integer serviceId, ReserveVanityNumberBean reserveVanityNumberBean,
			String userName, String cityCode) {
		Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
		if (scServiceDetailOpt.isPresent() && reserveVanityNumberBean != null) {
			ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
			if (reserveVanityNumberBean.getNumberDetails() != null) {
				List<ScAsset> scAssets = new ArrayList<ScAsset>();
				List<ScAsset> routingScAssets = new ArrayList<ScAsset>();
				List<ScAssetAttribute> scAssetAttributes = new ArrayList<ScAssetAttribute>();
				reserveVanityNumberBean.getNumberDetails().forEach(numberDetailsBean -> {
					ScAsset scAsset = generateScAsset(scServiceDetail, numberDetailsBean.getE164(), "Toll-Free",
							userName);
					scAssets.add(scAsset);
					//Creating dummy routing number
					ScAsset routingScAsset = generateScAsset(scServiceDetail, "N/A", "Routing-Number", userName);
					routingScAssets.add(routingScAsset);
					scAssetAttributes.add(generateScAssetsAttribute(scAsset, "isReservedNumber", "yes", userName));
					scAssetAttributes.add(generateScAssetsAttribute(scAsset, "reservationId",
							reserveVanityNumberBean.getReservationId(), userName));
					scAssetAttributes.add(generateScAssetsAttribute(scAsset, "reservationExpiryDate",
							reserveVanityNumberBean.getReservationExpiryDate(), userName));
					if(cityCode != null && !cityCode.isEmpty()) {
						scAssetAttributes.add(generateScAssetsAttribute(scAsset, "cityCode", cityCode, userName));
					}
				});
				scAssetRepository.saveAll(scAssets);
				scAssetRepository.saveAll(routingScAssets);
				scAssetAttributeRepository.saveAll(scAssetAttributes);
				List<IGscScAsset> unAssociatedOutpulse = scAssetRepository.getUnAssociatedOutpulse(serviceId);
				if (!scAssets.isEmpty() && !unAssociatedOutpulse.isEmpty()) {
					List<ScAssetRelation> scAssetRelations = new ArrayList<ScAssetRelation>();
					// int count =
					// scAssets.size()<unAssociatedOutpulse.size()?scAssets.size():unAssociatedOutpulse.size();
					for (int i = 0; i < scAssets.size(); i++) {
						ScAsset tollFreeAsset = scAssets.get(i);
						IGscScAsset outpulseAsset = unAssociatedOutpulse.get(i);
						scAssetRelations.add(generateScAssetRelations(tollFreeAsset.getId(), outpulseAsset.getAssetId(),
								"Outpulse", userName));
						scAssetRelations.add(generateScAssetRelations(outpulseAsset.getAssetId(), tollFreeAsset.getId(),
								"Toll-Free", userName));
						//building relationship between routing number and outpulse
						ScAsset routingAsset = routingScAssets.get(i);
						scAssetRelations.add(generateScAssetRelations(routingAsset.getId(), outpulseAsset.getAssetId(), "Outpulse", userName));
						scAssetRelations.add(generateScAssetRelations(outpulseAsset.getAssetId(), routingAsset.getId(), "Routing-Number", userName));
					}
					scAssetRelationRepository.saveAll(scAssetRelations);
				}
			}
		}
	}

	public List<IGscScAsset> getRoutingOutpulse(Integer serviceId, String offeringName) {
		List<IGscScAsset> outpulse = scAssetRepository.getUnAssociatedOutpulse(serviceId);
		outpulse.addAll(scAssetRepository.getPortingOutpulse(serviceId));
		if(offeringName.toUpperCase().contains("UIFN")) {
			outpulse.addAll(scAssetRepository.getUifnProcuredOutpulse(serviceId));
		}
		return outpulse;
	}

	public void persistReservedRoutingNumber(Integer serviceId,
			HashMap<SupplierBean, GetRoutingNumberBean> routingNumberBeans, List<IGscScAsset> outpulseList,
			String userName) {
		Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
		if (scServiceDetailOpt.isPresent() && routingNumberBeans != null) {
			ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
			for (Map.Entry<SupplierBean, GetRoutingNumberBean> routingNumberBean : routingNumberBeans.entrySet()) {
				List<ScAsset> scAssets = new ArrayList<ScAsset>();
				List<ScAssetAttribute> scAssetAttributes = new ArrayList<ScAssetAttribute>();
				SupplierBean supplierBean = routingNumberBean.getKey();
				GetRoutingNumberBean rouNumberBean = routingNumberBean.getValue();
				rouNumberBean.getRoutingNo().forEach(routingNo -> {
					ScAsset scAsset = generateScAsset(scServiceDetail, routingNo, "Routing-Number", userName);
					scAssets.add(scAsset);
					if (Objects.nonNull(rouNumberBean.getRoutingNoReservationId()))
						scAssetAttributes.add(generateScAssetsAttribute(scAsset, "routingNoReservationId",
								rouNumberBean.getRoutingNoReservationId(), userName));
					if (Objects.nonNull(rouNumberBean.getRoutingNoReservationEndDate()))
						scAssetAttributes.add(generateScAssetsAttribute(scAsset, "routingNoReservationEndDate",
								rouNumberBean.getRoutingNoReservationEndDate(), userName));
					scAssetAttributes.add(generateScAssetsAttribute(scAsset, "supplierId",
							supplierBean.getSupplierId() + "", userName));
					scAssetAttributes.add(generateScAssetsAttribute(scAsset, "supplierName",
							supplierBean.getSupplierName(), userName));
				});
				scAssetRepository.saveAll(scAssets);
				scAssetAttributeRepository.saveAll(scAssetAttributes);
				if (!scAssets.isEmpty() && !outpulseList.isEmpty()) {
					List<ScAssetRelation> scAssetRelations = new ArrayList<ScAssetRelation>();
					for (int i = 0; i < scAssets.size(); i++) {
						ScAsset routingNumberAsset = scAssets.get(i);
						IGscScAsset outpulseAsset = outpulseList.get(i);
						scAssetRelations.add(generateScAssetRelations(routingNumberAsset.getId(),
								outpulseAsset.getAssetId(), "Outpulse", userName));
						scAssetRelations.add(generateScAssetRelations(outpulseAsset.getAssetId(),
								routingNumberAsset.getId(), "Routing-Number", userName));
					}
					scAssetRelationRepository.saveAll(scAssetRelations);
				}
			}
		}
	}

	public List<GscScAssetReserved> getRepcReservedTollNumbers(Integer serviceId) {
		return scAssetRepository.getReservedTollFreeNumber(serviceId);
	}

	public List<GscScAssetReserved> getRepcReservedRoutingNumbers(Integer serviceId) {
		return scAssetRepository.getReservedRoutingNumber(serviceId);
	}

	public List<GscScAssetReserved> getAllRoutingNumbers(Integer serviceId) {
		return scAssetRepository.getAllRoutingNumber(serviceId);
	}

	public List<GscScAssetReserved> getAllRoutingNumbers(Integer serviceId, List<Integer> gscFlowGroupAssets) {
		return scAssetRepository.getAllRoutingNumber(serviceId, gscFlowGroupAssets);
	}

	public List<GscScAssetReserved> getAllRoutingNumbers(Integer serviceId, String supplierId) {
		return scAssetRepository.getAllRoutingNumber(serviceId, supplierId);
	}
	
	public List<GscScAsset> getTollFreeAndRoutingFromOutpuseByOutpluseFlowGrpID(Integer serviceId, Integer flowGroupId) {
		return scAssetRepository.getTollFreeAndRoutingFromOutpuseAndOutpluseFlowGrpID(serviceId, flowGroupId);
	}

	public List<GscScAsset> getTollFreeAndRoutingFromOutpuseByFlowGrpID(Integer serviceId, Integer flowGroupId) {
		return scAssetRepository.getTollFreeAndRoutingFromOutpuseAndFlowGrpID(serviceId, flowGroupId);
	}
	
	public List<GscScAssetWithStatus> getTollFreeAndRoutingFromOutpuseByFlowGrpIDWithStatus(Integer serviceId, Integer flowGroupId) {
		return scAssetRepository.getTollFreeAndRoutingFromOutpuseAndFlowGrpIDWithStatus(serviceId, flowGroupId);
	}

	public List<GscScAsset> getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(Integer serviceId, Integer flowGroupId,
			String status) {
		return scAssetRepository.getTollFreeAndRoutingFromOutpuseAndFlowGrpIDAndStatus(serviceId, flowGroupId, status);
	}

	public List<String> getRepcCallTypeList(Integer serviceId) {
		List<String> callTypeList = new ArrayList<String>();
		List<String> attributesList = Arrays.asList("isratePerMinutefixed", "isratePerMinutemobile",
				"isratePerMinutespecial");
		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
		if (Objects.nonNull(scComponent)) {
			Optional.ofNullable(scComponent.getScComponentAttributes()).get().stream()
					.filter(component -> attributesList.contains(component.getAttributeName())).forEach(component -> {
						if (component.getAttributeValue().equalsIgnoreCase("yes")) {
							if ("isratePerMinutefixed".equalsIgnoreCase(component.getAttributeName())) {
								callTypeList.add("fixed");
							} else if ("isratePerMinutemobile".equalsIgnoreCase(component.getAttributeName())) {
								callTypeList.add("mobile");
							} else if ("isratePerMinutespecial".equalsIgnoreCase(component.getAttributeName())) {
								callTypeList.add("payphone");
							}
						}
					});
		}
		return callTypeList;
	}
	
	public GscFlowGroup generateGscFlowGroup(String flowGroupKey, String refId, String refType, String userName) {
		return generateGscFlowGroup(flowGroupKey, null, refId, refType, userName);
	}

	public GscFlowGroup generateGscFlowGroup(String flowGroupKey, Integer parentFlowId, String refId, String refType, String userName) {
		GscFlowGroup gscFlowGroup = new GscFlowGroup();
		gscFlowGroup.setFlowGroupKey(flowGroupKey);
		gscFlowGroup.setParentId(parentFlowId);
		gscFlowGroup.setRefId(refId);
		gscFlowGroup.setRefType(refType);
		gscFlowGroup.setIsActive((byte) 1);
		gscFlowGroup.setCreatedBy(userName);
		gscFlowGroup.setCreatedDate(new Timestamp(new Date().getTime()));
		return gscFlowGroup;
	}

	public GscFlowGroupToAsset generateGscFlowGroupToAsset(Integer gscFlowGroupId, Integer scAssetId, String userName) {
		GscFlowGroupToAsset gscFlowGroupToAsset = new GscFlowGroupToAsset();
		gscFlowGroupToAsset.setGscFlowGroupId(gscFlowGroupId);
		gscFlowGroupToAsset.setScAssetId(scAssetId);
		gscFlowGroupToAsset.setIsActive((byte) 1);
		gscFlowGroupToAsset.setCreatedBy(userName);
		gscFlowGroupToAsset.setCreatedDate(new Timestamp(new Date().getTime()));
		return gscFlowGroupToAsset;
	}

	public GscFlowGroup createGscFlowGroup(String flowGroupKey, String refId, String refType, String userName,
			List<Integer> scAssets) {
		GscFlowGroup gscFlowGroup = generateGscFlowGroup(flowGroupKey, refId, refType, userName);
		gscFlowGroupRepository.save(gscFlowGroup);
		List<GscFlowGroupToAsset> gscFlowGroupToAssets = new ArrayList<GscFlowGroupToAsset>();
		scAssets.forEach(scAsset -> {
			gscFlowGroupToAssets.add(generateGscFlowGroupToAsset(gscFlowGroup.getId(), scAsset, userName));
		});
		gscFlowGroupToAssetRepository.saveAll(gscFlowGroupToAssets);
		return gscFlowGroup;
	}

	public List<Integer> getGscFlowGroupToAsset(Integer gscFlowGroupId) {
		List<Integer> gscFlowGroupAssets = new ArrayList<Integer>();
		List<GscFlowGroupToAsset> gscFlowGroupToAssets = gscFlowGroupToAssetRepository
				.findByGscFlowGroupId(gscFlowGroupId);
		gscFlowGroupToAssets.forEach(gscFlowGroupToAsset -> {
			gscFlowGroupAssets.add(gscFlowGroupToAsset.getScAssetId());
		});
		return gscFlowGroupAssets;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> createConfigNmbrMappingRequest(Map<String, Object> variables, String serviceCode,
			Integer serviceId) throws TclCommonException {

		List<Map<String, Object>> configureNmbrMappingBeans;
		List<Map<String, String>> attributeMapList = new LinkedList<>();
		Map<String, Object> configureNmbrMappingBeansByCallType = new LinkedHashMap<>();

		/*
		 * if(variables.get("orderInRepcRes")!=null) { CustomerOdrCreationBean
		 * customerOdrCreationBean = Utils.fromJson((String)
		 * variables.get("orderInRepcRes"), new
		 * TypeReference<CustomerOdrCreationBean>(){});
		 * 
		 * if(customerOdrCreationBean!=null) {
		 * 
		 * String orgId = scServiceDetailRepository.findOrgIdbyServiceId(serviceId);
		 * 
		 * for(CustRequestsBean custRequest:customerOdrCreationBean.getCustRequests()) {
		 * for(NumbersBean number:custRequest.getNumbers()) { for(DetailsBySupplierBean
		 * supplierDetail:number.getDetailsBySupplier()) { for(DetailsByCallTypeBean
		 * callTypeDetail: supplierDetail.getDetailsByCallType()) {
		 * if(callTypeDetail.getRoutingDetails() != null) { for(RoutingDetailsBean
		 * routingDetail : callTypeDetail.getRoutingDetails()) {
		 * 
		 * Map<String,String> attributeMap = constructAttributesMap(routingDetail);
		 * List<SwitchingDetailsBean> switchingDetails =
		 * callTypeDetail.getSwitchingDetails();
		 * 
		 * if(switchingDetails!=null) {
		 * switchingDetails.stream().filter(s->"CM".equalsIgnoreCase(s.getId()))
		 * .forEach(s->attributeMap.put("cmsId", s.getValue())); }
		 * 
		 * attributeMap.put("orderNo", serviceCode); attributeMap.put("orderName",
		 * Utils.convertAsString(custRequest.getEndCustomerName()));
		 * attributeMap.put("orgID", orgId);
		 * 
		 * attributeMapList.add(attributeMap); } } else {
		 * logger.info("Error - Routing details not found"); } } } } }
		 * 
		 * 
		 * if(attributeMapList!=null && !attributeMapList.isEmpty()) {
		 * 
		 * for(Map<String,String> attributeMap :attributeMapList) {
		 * 
		 * Map<String,Object> configureNmbrMappingBean = new HashMap<>();
		 * List<CNMAttributeBean> attributesList = new LinkedList<>();
		 * 
		 * attributeMap.forEach((key,value) -> {
		 * if(!GscConstants.ACTION.equalsIgnoreCase(key) &&
		 * !GscConstants.GROUPING_KEY.equalsIgnoreCase(key)) { CNMAttributeBean
		 * cnmAttributeBean = new CNMAttributeBean(); cnmAttributeBean.setName(key);
		 * cnmAttributeBean.setValue(value); attributesList.add(cnmAttributeBean); } });
		 * 
		 * configureNmbrMappingBean.put("serviceCharacteristic", attributesList);
		 * 
		 * if("POST".equals(attributeMap.get(GscConstants.ACTION))) {
		 * 
		 * String key = attributeMap.get(GscConstants.GROUPING_KEY);
		 * if(configureNmbrMappingBeansByCallType.get(key)!=null) {
		 * configureNmbrMappingBeans = (List<Map<String, Object>>)
		 * configureNmbrMappingBeansByCallType.get(key);
		 * configureNmbrMappingBeans.add(configureNmbrMappingBean);
		 * configureNmbrMappingBeansByCallType.put(key,configureNmbrMappingBeans); }else
		 * { configureNmbrMappingBeans = new LinkedList<>();
		 * configureNmbrMappingBeans.add(configureNmbrMappingBean);
		 * configureNmbrMappingBeansByCallType.put(key,configureNmbrMappingBeans); }
		 * }else {
		 * 
		 * String key = attributeMap.get(GscConstants.GROUPING_KEY);
		 * if(configureNmbrMappingBeansByCallType.get(key)!=null) {
		 * configureNmbrMappingBeans = (List<Map<String, Object>>)
		 * configureNmbrMappingBeansByCallType.get(key);
		 * configureNmbrMappingBeans.add(configureNmbrMappingBean);
		 * configureNmbrMappingBeansByCallType.put(key,configureNmbrMappingBeans); }else
		 * { configureNmbrMappingBeans = new LinkedList<>();
		 * configureNmbrMappingBeans.add(configureNmbrMappingBean);
		 * configureNmbrMappingBeansByCallType.put(key,configureNmbrMappingBeans); } } }
		 * }
		 * 
		 * }
		 * 
		 * }
		 */

		List<DetailsByCallTypeBean> detailsByCallTypeList = constructDetailsByCallTypeBeans(variables, serviceId);

		if (Objects.nonNull(detailsByCallTypeList) && !detailsByCallTypeList.isEmpty()) {

			String orgId = scServiceDetailRepository.findOrgIdbyServiceId(serviceId);
			for (DetailsByCallTypeBean callTypeDetail : detailsByCallTypeList) {
				if (callTypeDetail.getRoutingDetails() != null) {
					for (RoutingDetailsBean routingDetail : callTypeDetail.getRoutingDetails()) {

						Map<String, String> attributeMap = constructAttributesMap(routingDetail);
						List<SwitchingDetailsBean> switchingDetails = callTypeDetail.getSwitchingDetails();

						if (switchingDetails != null) {
							switchingDetails.stream().filter(s -> "CM".equalsIgnoreCase(s.getId()))
									.forEach(s -> attributeMap.put("cmsId", s.getValue()));
						}

						attributeMap.put("orderNo", serviceCode);
						attributeMap.put("orderName", "TEST OPTIMUS-O2C-VAS-PSTN");
						attributeMap.put("orgID", orgId);

						attributeMapList.add(attributeMap);
					}
				} else {
					logger.info("Error - Routing details not found");
				}
			}

			if (attributeMapList != null && !attributeMapList.isEmpty()) {

				for (Map<String, String> attributeMap : attributeMapList) {

					Map<String, Object> configureNmbrMappingBean = new HashMap<>();
					List<CNMAttributeBean> attributesList = new LinkedList<>();

					attributeMap.forEach((key, value) -> {
						if (!GscConstants.ACTION.equalsIgnoreCase(key)
								&& !GscConstants.GROUPING_KEY.equalsIgnoreCase(key)) {
							CNMAttributeBean cnmAttributeBean = new CNMAttributeBean();
							cnmAttributeBean.setName(key);
							cnmAttributeBean.setValue(value);
							attributesList.add(cnmAttributeBean);
						}
					});

					configureNmbrMappingBean.put("serviceCharacteristic", attributesList);

					if ("POST".equals(attributeMap.get(GscConstants.ACTION))) {

						String key = attributeMap.get(GscConstants.GROUPING_KEY);
						if (configureNmbrMappingBeansByCallType.get(key) != null) {
							configureNmbrMappingBeans = (List<Map<String, Object>>) configureNmbrMappingBeansByCallType
									.get(key);
							configureNmbrMappingBeans.add(configureNmbrMappingBean);
							configureNmbrMappingBeansByCallType.put(key, configureNmbrMappingBeans);
						} else {
							configureNmbrMappingBeans = new LinkedList<>();
							configureNmbrMappingBeans.add(configureNmbrMappingBean);
							configureNmbrMappingBeansByCallType.put(key, configureNmbrMappingBeans);
						}
					} else {

						String key = attributeMap.get(GscConstants.GROUPING_KEY);
						if (configureNmbrMappingBeansByCallType.get(key) != null) {
							configureNmbrMappingBeans = (List<Map<String, Object>>) configureNmbrMappingBeansByCallType
									.get(key);
							configureNmbrMappingBeans.add(configureNmbrMappingBean);
							configureNmbrMappingBeansByCallType.put(key, configureNmbrMappingBeans);
						} else {
							configureNmbrMappingBeans = new LinkedList<>();
							configureNmbrMappingBeans.add(configureNmbrMappingBean);
							configureNmbrMappingBeansByCallType.put(key, configureNmbrMappingBeans);
						}
					}
				}
			}

		}

		return configureNmbrMappingBeansByCallType;
	}

	public Map<String, String> constructAttributesMap(RoutingDetailsBean routingDetailsBean) {

		Map<String, String> attributeMap = new LinkedHashMap<>();
		Map<String, String> groupedAttr = new LinkedHashMap<>();
		Map<String, String> nonGroupedAttr = new LinkedHashMap<>();

		groupedAttr.put("requiredAction", routingDetailsBean.getRequiredAction());
		String callType = getCallType(routingDetailsBean.getCallTypeNo());
		groupedAttr.put("callType", callType);
		groupedAttr.put("inMainDigits", routingDetailsBean.getMainIncomingDigits());
		groupedAttr.put("forcedRoute", routingDetailsBean.getForcedOutRouteLabelId());
		groupedAttr.put("routeTries", routingDetailsBean.getOutRouteNbRoutesPerCall());
		groupedAttr.put("routePrioritization", routingDetailsBean.getOutRoutePrioritizationType());
		groupedAttr.put("routeLabelAction", routingDetailsBean.getOutRouteAction());
		groupedAttr.put("routeLabelscriptId", routingDetailsBean.getOutRouteScriptId());
		groupedAttr.put("routeLabelAttributes", routingDetailsBean.getOutRouteAttributes());
		groupedAttr.put("routeLabelPartition", routingDetailsBean.getOutRoutePartition());
		groupedAttr.put("routeLabelRoutingCriteria", routingDetailsBean.getOutRouteRoutingCriteria());
		groupedAttr.put("routeLabelRoutesRequested", routingDetailsBean.getOutRouteNbRoutesRequested());
		groupedAttr.put("routeServiceStatus", routingDetailsBean.getOutRouteEndPointsRequired());
		groupedAttr.put("cid", routingDetailsBean.getCid());
		groupedAttr.put("sid", routingDetailsBean.getSid());

		List<OutRouteEndPointsBean> outRoutes = routingDetailsBean.getOutRouteEndPoints();
		int outRouteCount = 1;
		for (OutRouteEndPointsBean outRoute : outRoutes) {
			String defaultStatus = "1";
			if (outRouteCount == 1) {
				groupedAttr.put("routeSequence", String.valueOf(outRoute.getSequence()));
				nonGroupedAttr.put("routeType", "");
				nonGroupedAttr.put("routeProportion", "");
				groupedAttr.put("routeServiceStatus", defaultStatus);
				groupedAttr.put("routeEndpoint1", outRoute.getEndPoint1());
				nonGroupedAttr.put("routeEndpoint2", "");
			} else {
				groupedAttr.put("routeSequence-" + outRouteCount, String.valueOf(outRoute.getSequence()));
				nonGroupedAttr.put("routeType-" + outRouteCount, "");
				nonGroupedAttr.put("routeProportion-" + outRouteCount, "");
				groupedAttr.put("routeServiceStatus-" + outRouteCount, defaultStatus);
				groupedAttr.put("routeEndpoint1-" + outRouteCount, outRoute.getEndPoint1());
				nonGroupedAttr.put("routeEndpoint2-" + outRouteCount, "");
			}
			outRouteCount++;
		}

		groupedAttr.put("outpulseType", routingDetailsBean.getOutpulseType());
		groupedAttr.put("outpulse", routingDetailsBean.getOutpulseCustomDigits());
		groupedAttr.put("outpulseDigitsToRemove", String.valueOf(routingDetailsBean.getOutpulseNbDigits()));
		groupedAttr.put("clid", routingDetailsBean.getClidType());
		groupedAttr.put("customClid", routingDetailsBean.getClidCustomDigits());
		groupedAttr.put("clidNatureOfAddress", routingDetailsBean.getClidNatureOfAddressType());
		groupedAttr.put("clidNumberingPlanIndicator", routingDetailsBean.getClidNumberingPlanIndicatorType());
		groupedAttr.put("clidPresentation", routingDetailsBean.getClidPresentationType());
		groupedAttr.put("clidScreening", routingDetailsBean.getClidScreeningType());
		groupedAttr.put("billingNumberType", routingDetailsBean.getBillingNumberType());
		groupedAttr.put("billingNumberCustomized", routingDetailsBean.getBillingNumberCustomDigits());
		groupedAttr.put("billingNumberNatureOfAddress", routingDetailsBean.getBillingNumberNatureOfAddressType());
		groupedAttr.put("billingNumberNumberingPlanIndicator",
				routingDetailsBean.getBillingNumberNumberingPlanIndicatorType());
		groupedAttr.put("oliType", routingDetailsBean.getOliType());
		groupedAttr.put("outpulsePccPosition", routingDetailsBean.getOutpulseCustomDigitsPccPosition());
		groupedAttr.put("clidPccPosition", routingDetailsBean.getClidCustomDigitsPccPosition());
		groupedAttr.put("billingNumberPccPosition", routingDetailsBean.getBillingNumberCustomDigitsPccPosition());
		groupedAttr.put("connectionDtgHeader", routingDetailsBean.getConnectionDtgHeader());

		nonGroupedAttr.put("operMode", "full");
		nonGroupedAttr.put("serviceName", routingDetailsBean.getServiceName());
		nonGroupedAttr.put("forcedRouteUpdate", "false");
		nonGroupedAttr.put("pcc", routingDetailsBean.getPcc());
		nonGroupedAttr.put("tollFreeNumber", routingDetailsBean.getTollfreeNumber());

		List<InRoutesbean> inRoutes = routingDetailsBean.getInRoutes();
		int routeCount = 1;
		for (InRoutesbean inRoute : inRoutes) {
			String standardRoutePartition = Utils.convertAsString(inRoute.getPartition());
			if (routeCount == 1) {
				nonGroupedAttr.put("destinationNationalId", inRoute.getDestNationalId());
				nonGroupedAttr.put("standardRoutePartition",
						"".equals(Utils.convertAsString(standardRoutePartition)) ? "VTS" : standardRoutePartition);
			} else {
				nonGroupedAttr.put("destinationNationalId-" + routeCount, inRoute.getDestNationalId());
				nonGroupedAttr.put("standardRoutePartition-" + routeCount,
						"".equals(Utils.convertAsString(standardRoutePartition)) ? "VTS" : standardRoutePartition);
			}
			routeCount++;
		}

		nonGroupedAttr.put("serviceTag", "P");

		nonGroupedAttr.put("didNumber", "");
		nonGroupedAttr.put("supplierTrunkGroup", "");
		nonGroupedAttr.put("destinationCountryCode", "");

		String groupingKey = groupedAttr.values().stream().filter(value -> !"".equals(Utils.convertAsString(value)))
				.collect(Collectors.joining());

		groupedAttr.forEach((key, value) -> {
			if (!"".equals(Utils.convertAsString(value))) {
				attributeMap.put(key, Utils.convertAsString(value));
			}
		});

		nonGroupedAttr.forEach((key, value) -> {
			if (!"".equals(Utils.convertAsString(value)))
				attributeMap.put(key, Utils.convertAsString(value));
		});

		attributeMap.put("groupingKey", groupingKey);

		return attributeMap;
	}

	private String getCallType(Integer callTypeNo) {
		if (callTypeNo == 1)
			return "fixed";
		else if (callTypeNo == 2)
			return "mobile";
		else if (callTypeNo == 3)
			return "payphone";
		else
			return "";
	}

	public List<DetailsByCallTypeBean> constructDetailsByCallTypeBeans(Map<String, Object> variables, Integer serviceId)
			throws TclCommonException {
		List<DetailsByCallTypeBean> detailsByCallTypeList = new LinkedList<>();
		Integer flowGroupId = (Integer) variables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
		List<GscScAsset> torScAssets = getTollFreeAndRoutingFromOutpuseByFlowGrpID(serviceId, flowGroupId);
		for (GscScAsset torScAsset : torScAssets) {
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(torScAsset.getRoutingId(), Arrays.asList("detailsByCallType"));
			if (scAssetAttributes != null && !scAssetAttributes.isEmpty()) {
				List<DetailsByCallTypeBean> detailsByCallTypeBean = Utils.fromJson(
						getScAssetAttributeValue(scAssetAttributes.get(0)), new TypeReference<List<DetailsByCallTypeBean>>() {});
				detailsByCallTypeList.addAll(detailsByCallTypeBean);
			}
		}
		return detailsByCallTypeList;
	}
	
	public String getScAssetAttributeValue(ScAssetAttribute scAssetAttribute) {
		String attributeValue = null;
		if (scAssetAttribute != null) {
			attributeValue = scAssetAttribute.getAttributeValue();
			if("Y".equalsIgnoreCase(scAssetAttribute.getIsAdditionalParam())) {
				Integer addServiceParamId = StringUtils.isEmpty(attributeValue)?0:Integer.valueOf(attributeValue);
				Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
				if(optScAddServParam.isPresent()) {
					attributeValue = optScAddServParam.get().getValue();
				}
			}
		}
		return attributeValue;
	}

	public String getServiceAttrValue(ScServiceDetail scServiceDetail, String attributeName) {
		String serviceAttrValue = "";
		Set<ScServiceAttribute> serviceAttributes = scServiceDetail.getScServiceAttributes();
		Optional<ScServiceAttribute> serviceAttr = serviceAttributes.stream().filter(servAttr -> attributeName.equalsIgnoreCase(servAttr.getAttributeName())).findFirst();
		if(serviceAttr.isPresent()) {
			serviceAttrValue = serviceAttr.get().getAttributeValue();
		}
		return serviceAttrValue;
	}

	public List<ScServiceDetail> getChildServiceDetailForDID(String productType, String serviceId) {
		//return scServiceDetailRepository.findByProductNameAndParentId(productType, serviceId);
		return scServiceDetailRepository.findByServiceTypeAndParentId(productType, serviceId);
	}
	
	public ScServiceDetail getParentServiceDetail(Integer serviceId) {
		ScServiceDetail serviceDetail = getServiceDetail(serviceId);
		if(serviceDetail != null && serviceDetail.getParentId() != null) {
			return getServiceDetail(serviceDetail.getParentId());
		}
		return null;
	}
	
	public ScServiceDetail getServiceDetail(Integer serviceId) {
		Optional<ScServiceDetail> serviOptional = scServiceDetailRepository.findById(serviceId);
		if(serviOptional.isPresent()) {
			return serviOptional.get();
		}
		return null;
	}
	
	public ScServiceDetail getSipServiceDetailByChildService(Integer childServiceId) {
		Optional<ScServiceDetail> serviOptional = scServiceDetailRepository.findById(childServiceId);
		if(serviOptional.isPresent()) {
			return getSipServiceDetail(serviOptional.get().getParentId());
		}
		return null;
	}
	
	public ScServiceDetail getSipServiceDetail(Integer parentServiceId) {
		List<ScServiceDetail> childServiceDetails = getChildServiceDetails(parentServiceId);
		for(ScServiceDetail childServiceDetail : childServiceDetails) {
			if(childServiceDetail.getErfPrdCatalogOfferingName().equals(GscCommonConstants.GSC_SIP)) {
				return childServiceDetail;
			}
		}
		return null;
	}
	
	public void persistCreateCircuitGroupResponse(Integer serviceId, String responseJson) {
		Map<String, String> atMap = new HashMap<>();
		atMap.put("createCircuitGroupRes", responseJson);
		persistComponentAttributes(serviceId, atMap);
	}
	
	public HashMap<String, String> getCityCodeAndCountries(Integer serviceId) throws TclCommonException {
		HashMap<String, String> cityCodeCountry = new HashMap<String, String>();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String cityName = scComponentAttributesmap.getOrDefault("cityName", null);
		if(cityName != null && !cityName.isEmpty()) {
			List<String> cityList = new ArrayList<String>();
			if(cityName.startsWith("[")) {
				cityList = Utils.convertJsonToObject(cityName, ArrayList.class);
			} else {
				cityList.add(cityName);
			}
			for(String element : cityList) {
				String[] value = element.split(":"); 
				cityCodeCountry.put(value[0].toUpperCase(), value[1]);
			}
		}
		return cityCodeCountry;
	}
	
	public List<CityWiseQuantity> getCityCodeAndCountriesQuantityOfNumbers(Integer serviceId) throws TclCommonException {
		List<CityWiseQuantity> cityWiseQuantityList = new ArrayList<CityWiseQuantity>();
		HashMap<String, String> cityCodeCountry = new HashMap<String, String>();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String cityName = scComponentAttributesmap.getOrDefault("cityName", null);
		if(cityName != null && !cityName.isEmpty()) {
			List<String> cityList = new ArrayList<String>();
			if(cityName.startsWith("[")) {
				cityList = Utils.convertJsonToObject(cityName, ArrayList.class);
			} else {
				cityList.add(cityName);
			}
			for(String element : cityList) {
				String[] value = element.split(":"); 
				cityCodeCountry.put(value[0].toUpperCase(), value[1]);
			}
		}
		HashMap<String, String> cityWiseAreaCodeMap = new HashMap<String, String>();
		String cityWiseAreaCode = scComponentAttributesmap.getOrDefault("cityWiseAreaCode", null);
		if (cityWiseAreaCode != null && !cityWiseAreaCode.isEmpty()) {
			List<String> areaCodeList = new ArrayList<String>();
			if (cityWiseAreaCode.startsWith("[")) {
				areaCodeList = Utils.convertJsonToObject(cityWiseAreaCode, ArrayList.class);
			} else {
				areaCodeList.add(cityWiseAreaCode);
			}
			for (String element : areaCodeList) {
				String[] value = element.split(":");
				cityWiseAreaCodeMap.put(value[0].toUpperCase(), value[1]);
			}
		}
		
		String cityWiseQuantityOfNumbers = scComponentAttributesmap.getOrDefault("cityWiseQuantityOfNumbers", null);
		if(cityWiseQuantityOfNumbers != null && !cityWiseQuantityOfNumbers.isEmpty()) {
			List<String> cityWiseQuantityOfNumbersList = new ArrayList<String>();
			if(cityWiseQuantityOfNumbers.startsWith("[")) {
				cityWiseQuantityOfNumbersList = Utils.convertJsonToObject(cityWiseQuantityOfNumbers, ArrayList.class);
			} else {
				cityWiseQuantityOfNumbersList.add(cityWiseQuantityOfNumbers);
			}
			for(String element : cityWiseQuantityOfNumbersList) {
				String[] value = element.split(":"); 
				CityWiseQuantity cityWiseQuantity = new CityWiseQuantity();
				cityWiseQuantity.setCityCode(value[0].toUpperCase());
				cityWiseQuantity.setQuantity(value.length-1);
				cityWiseQuantity.setCityName(cityCodeCountry.getOrDefault(cityWiseQuantity.getCityCode(), ""));
				cityWiseQuantity.setNpa(cityWiseAreaCodeMap.getOrDefault(cityWiseQuantity.getCityCode(),""));
				
				cityWiseQuantityList.add(cityWiseQuantity);
			}
		}
		return cityWiseQuantityList;
	}

	public void persistGetDIDSuppliersResponse(Integer serviceId, String responseJson) {
		Map<String, String> atMap = new HashMap<>();
		atMap.put("getSuppliersRes", responseJson);
		List<ScServiceDetail> scServiceDetails = getChildServiceDetailForDID(GscConstants.DOMESTIC_VOICE, String.valueOf(serviceId));
		ScServiceDetail scServiceDetail = null;
		if(!CollectionUtils.isEmpty(scServiceDetails)) {
			scServiceDetail = scServiceDetails.get(0);
			logger.debug("Get supplier response persisted against service ID: -> {}",scServiceDetail.getId());
			persistComponentAttributes(scServiceDetail.getId(), atMap);
		}
	}
	
	public String getRepcPartnerAbbreviationName(String name) {
		 MstPartnerAbbreviation mstPartnerAbbreviation=mstPartnerAbbreviationRepository.findByName(name);
		 if(mstPartnerAbbreviation!=null) {
			 return mstPartnerAbbreviation.getRepcAbbreviationName();
		 }
		 return null;
	}
	
	public String getRepcPartnerAbbreviationName(Integer scOrderId) {
		Optional<ScOrder> scOptional = scOrderRepository.findById(scOrderId);
		if(scOptional.isPresent()) {
			ScOrder scOrder = scOptional.get();
			if(scOrder.getErfCustPartnerName() != null) {
				return getRepcPartnerAbbreviationName(scOrder.getErfCustPartnerName());
			}
		}
		return null;
	}
	
	public List<IGscScAsset> getPortingNumbers(Integer serviceId) {
		return scAssetRepository.getPortingOutpulse(serviceId);
	}
	
	public List<IGscScAsset> getUnAssociatedOutpulse(Integer serviceId) {
		return scAssetRepository.getUnAssociatedOutpulse(serviceId);
	}
	
	public String getServiceAttrAndAddParamValue(ScServiceDetail scServiceDetail, String attributeName) {
		String serviceAttrValue = "";
		Set<ScServiceAttribute> serviceAttributes = scServiceDetail.getScServiceAttributes();
		Optional<ScServiceAttribute> serviceAttr = serviceAttributes.stream().filter(servAttr -> attributeName.equalsIgnoreCase(servAttr.getAttributeName())).findFirst();
		if(serviceAttr.isPresent()) {
			ScServiceAttribute servAttr = serviceAttr.get();
			if("Y".equals(servAttr.getIsAdditionalParam())) {
				Integer addServiceParamId = StringUtils.isEmpty(servAttr.getAttributeValue())?0:Integer.valueOf(servAttr.getAttributeValue());
				Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
				if(optScAddServParam.isPresent()) {
					serviceAttrValue = optScAddServParam.get().getValue();
				}
			}else {
				serviceAttrValue = servAttr.getAttributeValue();
			}
		}
		return serviceAttrValue;
	}
	
	
	public String getAbsoluteAttrValue(ScComponentAttribute scComponentAttr) {
		String value = scComponentAttr.getAttributeValue();
		if(!StringUtils.isEmpty(value) && GscConstants.Y.equals(scComponentAttr.getIsAdditionalParam())) {
			Optional<ScAdditionalServiceParam> optAdditionParam = scAdditionalServiceParamRepository.findById(Integer.parseInt(value));
			if(optAdditionParam.isPresent()) {
				value = optAdditionParam.get().getValue();
			}
		}
		return value;
	}
}
