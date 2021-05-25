package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRPlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants.*;

/**
 * Service class for generating flow group and flow group attributes
 *
 * @author srraghav
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TeamsDRService {

	Logger LOGGER = LoggerFactory.getLogger(TeamsDRService.class);

	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;

	@Autowired
	FlowGroupAttributeRepository flowGroupAttributeRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	/**
	 * Calculate total batch count
	 *
	 * @param serviceId
	 * @param scComponent
	 * @return
	 */
	public Integer calculateTotalBatchCount(Integer serviceId, ScComponent scComponent) {
		AtomicReference<Integer> totalBatchCount = new AtomicReference<>(0);
		gscFlowGroupRepository.findByRefIdAndRefType(scComponent.getId().toString(), COMPONENT).forEach(flowGroup -> {
			LOGGER.info("calculateTotalBatchCount() : serviceId : {} , gscFlowGroupKey : {} ", serviceId,
					flowGroup.getFlowGroupKey());
			List<FlowGroupAttribute> flowGroupAttributes = flowGroupAttributeRepository
					.findByScServiceDetailIdAndAttributeNameAndGscFlowGroup(serviceId,
							AttributeConstants.BATCH_USER_COUNT, flowGroup);
			flowGroupAttributes.stream().filter(fga -> Objects.nonNull(fga.getAttributeValue())).forEach(
					flowGroupAttribute -> totalBatchCount
							.set(totalBatchCount.get() + Integer.parseInt(flowGroupAttribute.getAttributeValue())));
		});
		return totalBatchCount.get();
	}

	/**
	 * Return total users on count
	 *
	 * @param serviceId
	 * @param scComponent
	 * @return
	 */
	public Integer returnTotalUsersOnCount(Integer serviceId, ScComponent scComponent) {
		Integer noOfUsersOnSite = 0;
		if (Objects.nonNull(scComponent)) {
			LOGGER.info("returnTotalUsersOnCount() : serviceId : {} , siteName : {} ", serviceId,
					scComponent.getComponentName());
			List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
					.findByAttributeNameAndScComponent_id(AttributeConstants.NO_OF_USERS_ON_SITE, scComponent.getId());
			noOfUsersOnSite = scComponentAttributes.stream().filter(scA -> Objects.nonNull(scA.getAttributeValue()))
					.mapToInt(scA -> Integer.parseInt(scA.getAttributeValue())).sum();
		}
		return noOfUsersOnSite;
	}

	public GscFlowGroup generateFlowGroup(String flowGroupKey, Integer parentFlowId, String refId, String refType,
			String userName) {
		GscFlowGroup flowGroup = new GscFlowGroup();
		flowGroup.setFlowGroupKey(flowGroupKey);
		flowGroup.setParentId(parentFlowId);
		flowGroup.setRefId(refId);
		flowGroup.setRefType(refType);
		flowGroup.setIsActive((byte) 1);
		flowGroup.setCreatedBy(userName);
		flowGroup.setCreatedDate(new Timestamp(new Date().getTime()));
		return gscFlowGroupRepository.save(flowGroup);
	}

	/**
	 * Create flow group attributes
	 *
	 * @param serviceDetailId
	 * @param attributeName
	 * @param attributeValue
	 * @param flowGroup
	 * @param userName
	 * @return
	 */
	public FlowGroupAttribute createFlowGroupAttributes(Integer serviceDetailId, String attributeName,
			String attributeValue, GscFlowGroup flowGroup, String userName) {
		FlowGroupAttribute flowGroupAttribute = new FlowGroupAttribute();
		flowGroupAttribute.setScServiceDetailId(serviceDetailId);
		flowGroupAttribute.setGscFlowGroup(flowGroup);
		flowGroupAttribute.setAttributeName(attributeName);
		flowGroupAttribute.setAttributeValue(attributeValue);
		flowGroupAttribute.setAttributeAltValueLabel(attributeValue);
		flowGroupAttribute.setIsActive(CommonConstants.Y);
		flowGroupAttribute.setCreatedBy(userName);
		flowGroupAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		flowGroupAttribute.setUpdatedBy(userName);
		flowGroupAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return flowGroupAttributeRepository.save(flowGroupAttribute);
	}

	/**
	 * Update flow group attributes
	 *
	 * @param serviceDetailId
	 * @param attributeName
	 * @param attributeValue
	 * @param flowGroup
	 * @param userName
	 * @return
	 */
	public FlowGroupAttribute updateFlowGroupAttributes(Integer serviceDetailId, String attributeName,
			String attributeValue, GscFlowGroup flowGroup, String userName) {
		FlowGroupAttribute flowGroupAttribute = flowGroupAttributeRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndGscFlowGroupOrderByIdDesc(serviceDetailId,
						attributeName, flowGroup);
		flowGroupAttribute.setAttributeValue(attributeValue);
		flowGroupAttribute.setAttributeAltValueLabel(attributeValue);
		flowGroupAttribute.setIsActive(CommonConstants.Y);
		flowGroupAttribute.setCreatedBy(userName);
		flowGroupAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		flowGroupAttribute.setUpdatedBy(userName);
		flowGroupAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		return flowGroupAttributeRepository.save(flowGroupAttribute);
	}

	/**
	 * Update attributes
	 *
	 * @param serviceId
	 * @param atMap
	 * @param flowGroup
	 */
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, GscFlowGroup flowGroup) {
		String userName = Utils.getSource();
		if (flowGroup != null) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key : {}, Value : {}", key, value);
				FlowGroupAttribute flowGroupAttribute = flowGroupAttributeRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndGscFlowGroupOrderByIdDesc(serviceId, key,
								flowGroup);
				if (flowGroupAttribute == null) {
					createFlowGroupAttributes(serviceId, key, value, flowGroup, userName);
				} else {
					updateFlowGroupAttributes(serviceId, key, value, flowGroup, userName);
				}
			});
		}
	}

	/**
	 * To fetch Gsc Flow Group
	 *
	 * @param scComponent
	 * @return
	 */
	public GscFlowGroup getLatestFlowGroup(ScComponent scComponent) {
		return gscFlowGroupRepository.findFirstByRefIdAndRefTypeOrderByIdDesc(String.valueOf(scComponent.getId()),
				COMPONENT);
	}

	/**
	 * To fetch Gsc Flow Group
	 *
	 * @param flowGroupId
	 * @return
	 */
	public GscFlowGroup getFlowGroupById(Integer flowGroupId) {
		if (Objects.nonNull(flowGroupId)) {
			Optional<GscFlowGroup> flowGroup = gscFlowGroupRepository.findById(flowGroupId);
			return flowGroup.orElse(null);
		} else
			return null;
	}

	/**
	 * To fetch Gsc Flow Group
	 *
	 * @param refId
	 * @param refType
	 * @return
	 */
	public List<GscFlowGroup> getFlowGroupByRefIdAndType(String refId, String refType) {
		if (Objects.nonNull(refId) && Objects.nonNull(refType)) {
			List<GscFlowGroup> flowGroup = gscFlowGroupRepository.findByRefIdAndRefType(refId, refType);
			if (!flowGroup.isEmpty())
				return flowGroup;
			else
				return null;
		} else
			return null;
	}

	/**
	 * To fetch Gsc Flow Group
	 *
	 * @param flowGroup
	 * @return
	 */
	public List<FlowGroupAttribute> getFlowGroupAttributes(GscFlowGroup flowGroup) {
		return flowGroupAttributeRepository.findByGscFlowGroup(flowGroup);
	}

	/**
	 * Method to fetch total site user count
	 * @param componentId
	 * @return
	 */
	public Integer getTotalSiteUserCount(Integer componentId) {
		return gscFlowGroupRepository.findByRefIdAndRefType(String.valueOf(componentId), COMPONENT)
				.stream().flatMap(gscFlowGroup -> getFlowGroupAttributes(gscFlowGroup).stream())
				.filter(gscFlowGroupAttr ->
						AttributeConstants.BATCH_USER_COUNT.equals(gscFlowGroupAttr.getAttributeName()) &&
								Objects.nonNull(gscFlowGroupAttr.getAttributeValue()))
				.mapToInt(flowGroupAttr -> Integer.parseInt(flowGroupAttr.getAttributeValue()))
				.sum();
	}

	/**
	 * Fetch totalUserCount from scComponent
	 * @param componentId
	 * @return
	 */
	public Integer fetchTotalUserCount(Integer componentId){
		Optional<ScComponentAttribute> scComponentAttribute = scComponentAttributesRepository.findByScComponent_id(componentId)
				.stream().filter(attr-> TOTAL_USERS.equals(attr.getAttributeName()) && Objects.nonNull(attr.getAttributeValue()))
				.findAny();
		if(scComponentAttribute.isPresent()){
			return Integer.parseInt(scComponentAttribute.get().getAttributeValue());
		}
		return null;
	}

	/**
	 * Method to update Pending user count.
	 * @param serviceId
	 * @param scComponent
	 */
	public void updatePendingUserCount(Integer serviceId,ScComponent scComponent){
		Integer totalSiteUserCount = returnTotalUsersOnCount(serviceId,scComponent);
		Integer totalBatchUserCount = calculateTotalBatchCount(serviceId, scComponent);
		LOGGER.info("totalSiteUserCount :: {},totalBatchUserCount :: {}", totalSiteUserCount, totalBatchUserCount);
		if (Objects.nonNull(totalSiteUserCount) && Objects.nonNull(totalBatchUserCount)) {
			Integer pendingCount = totalSiteUserCount - totalBatchUserCount;
			Map<String,String> atMap = new HashMap<>();
			atMap.put(PENDING_USER_COUNT,String.valueOf(pendingCount));
			componentAndAttributeService.updateAttributesByScComponent(serviceId,atMap,scComponent.getId());
		}
	}
	private ScComponentAttribute constructScComponentAttr(ScComponent scComponent,ScServiceDetail scServiceDetail,String attributeName,
                                          String attributeValue){
	    ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
	    scComponentAttribute.setScServiceDetailId(scServiceDetail.getId());
	    scComponentAttribute.setScComponent(scComponent);
	    scComponentAttribute.setUuid(scComponent.getUuid());
	    scComponentAttribute.setAttributeName(attributeName);
	    scComponentAttribute.setIsActive(CommonConstants.Y);
	    scComponentAttribute.setCreatedBy(Utils.getSource());
	    scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
	    scComponentAttribute.setIsAdditionalParam(CommonConstants.Y);
	    return scComponentAttributesRepository.save(scComponentAttribute);
    }

	/**
	 * Method to create or update additional service param
	 * @param attributeName
	 * @param attributeValue
	 * @param scServiceDetail
	 */
	public ScAdditionalServiceParam createOrUpdateAdditionalParam(String attributeName,String attributeValue,ScServiceDetail scServiceDetail){
		AtomicReference<ScAdditionalServiceParam> finalAdditionalServiceParam = new AtomicReference<>();
		if(Objects.nonNull(attributeName) && Objects.nonNull(scServiceDetail)){
		    Optional<ScComponent> scComponent = scComponentRepository.findByScServiceDetailId(scServiceDetail.getId())
                    .stream().filter(comp-> COMPONENT_LM.equals(comp.getComponentName())).findAny();
		    if(scComponent.isPresent()){
                Optional<ScComponentAttribute> optionalScComponentAttribute = scComponentAttributesRepository.findByScComponent(scComponent.get()).stream()
					.filter(scComponentAttribute -> attributeName.equals(scComponentAttribute.getAttributeName()))
                        .findAny();
                if(optionalScComponentAttribute.isPresent()){
                    ScComponentAttribute scComponentAttribute = optionalScComponentAttribute.get();
                    if(Objects.nonNull(scComponentAttribute.getIsAdditionalParam()) &&
                            CommonConstants.Y.equals(scComponentAttribute.getIsAdditionalParam()) && Objects.nonNull(scComponentAttribute.getAttributeValue())){
                        scAdditionalServiceParamRepository.findById(Integer.parseInt(scComponentAttribute.getAttributeValue())).ifPresent(scAdditionalServiceParam -> {
                            scAdditionalServiceParam.setValue(attributeValue);
                            scAdditionalServiceParam.setUpdatedBy(Utils.getSource());
                            scAdditionalServiceParam.setUpdatedTime(new Timestamp(new Date().getTime()));
                            finalAdditionalServiceParam.set(scAdditionalServiceParamRepository.save(scAdditionalServiceParam));
                        });
                    }else{
                        finalAdditionalServiceParam.set(constructAdditionalServiceParam(scComponentAttribute.getId(),attributeName,attributeValue,"SERVICE_ATTRIBUTES","REQUEST-JSON"));
						scComponentAttribute.setIsAdditionalParam(CommonConstants.Y);
						scComponentAttribute.setAttributeValue(String.valueOf(finalAdditionalServiceParam.get().getId()));
						scComponentAttributesRepository.save(scComponentAttribute);
                    }
                }else{
                    ScComponentAttribute scComponentAttribute = constructScComponentAttr(scComponent.get(),scServiceDetail,attributeName,attributeValue);
                    finalAdditionalServiceParam.set(constructAdditionalServiceParam(scComponentAttribute.getId(),attributeName,attributeValue,"SERVICE_ATTRIBUTES","REQUEST-JSON"));
                    scComponentAttribute.setAttributeValue(String.valueOf(finalAdditionalServiceParam.get().getId()));
					scComponentAttributesRepository.save(scComponentAttribute);
                }
            }
		}
		LOGGER.info("ServiceParamId fetched/generated is :: {}",finalAdditionalServiceParam.get().getId());
		return finalAdditionalServiceParam.get();
	}

    /**
     * Method to construct additional service param.
     * @param referenceId
     * @param attributeName
     * @param attributeValue
     * @param referenceType
     * @param category
     * @return
     */
	private ScAdditionalServiceParam constructAdditionalServiceParam(Integer referenceId,String attributeName,
                                                                     String attributeValue,String referenceType,String category){
        ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
        scAdditionalServiceParam.setReferenceId(Objects.nonNull(referenceId) ? String.valueOf(referenceId):null);
        scAdditionalServiceParam.setReferenceType(referenceType);
        scAdditionalServiceParam.setCategory(category);
        scAdditionalServiceParam.setAttribute(attributeName);
        scAdditionalServiceParam.setValue(attributeValue);
        scAdditionalServiceParam.setCreatedBy(Utils.getSource());
        scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
        scAdditionalServiceParam.setIsActive(CommonConstants.Y);
        return scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
    }

	/**
	 * Construct teamsDR Plan item request
	 *
	 * @param caseInstanceId
	 * @param scComponent
	 * @param flowGroup
	 * @param serviceDetailId
	 * @param newPlanItem
	 * @param planItemDefinitionId
	 * @return
	 */
	public TeamsDRPlanItemRequestBean constructTeamsDRPlanItemRequestBean(String caseInstanceId,
			ScComponent scComponent, GscFlowGroup flowGroup, Integer serviceDetailId, String newPlanItem,
			String planItemDefinitionId) {
		TeamsDRPlanItemRequestBean planItemRequestBean = new TeamsDRPlanItemRequestBean();
		if(Objects.nonNull(scComponent))
		planItemRequestBean.setComponentId(scComponent.getId());
		planItemRequestBean.setCaseInstanceId(caseInstanceId);
		planItemRequestBean.setPlanItem(newPlanItem);
		planItemRequestBean.setServiceId(serviceDetailId);
		planItemRequestBean.setPlanItemDefinitionId(planItemDefinitionId);
		if (Objects.nonNull(flowGroup))
			planItemRequestBean.setFlowGroupId(flowGroup.getId());
		return planItemRequestBean;
	}

	/**
	 * Method to check if pending count is zero in all dr sites.
	 * @param componentId
	 * @return
	 */
	public Boolean checkPendingCount(Integer componentId) {
		LOGGER.info("ScComponentId :: {}", componentId);
		Optional<ScComponent> scComponentOptional = scComponentRepository.findById(componentId);
		if (scComponentOptional.isPresent()) {
			ScComponent scComponent = scComponentOptional.get();
			List<ScComponent> drSites = scComponentRepository.findByScServiceDetailId(scComponent.getScServiceDetailId()).stream().
					filter(scComp -> DR_SITE.equals(scComp.getSiteType())).collect(Collectors.toList());
			List<ScComponentAttribute> attributes = new ArrayList<>();
			drSites.forEach(drSite-> attributes.addAll(scComponentAttributesRepository.findByScComponentAndAttributeName(drSite,PENDING_USER_COUNT)));
			LOGGER.info("Size of drSites :: {}, Size of attributes :: {}",drSites.size(),attributes.size());
			if(attributes.size() == drSites.size()){
				return attributes.stream().filter(scComponentAttribute -> Objects.nonNull(scComponentAttribute.getAttributeValue()))
						.peek(scComponentAttribute -> LOGGER.info("ScComponent Attribute id :: {}, pendingCount :: {}",
								scComponentAttribute.getId(), scComponentAttribute.getAttributeValue()))
						.noneMatch(scComponentAttribute -> Objects.nonNull(scComponentAttribute.getAttributeValue()) &&
								Integer.parseInt(scComponentAttribute.getAttributeValue()) >= 1);
			}
		}
		return false;
	}
}
