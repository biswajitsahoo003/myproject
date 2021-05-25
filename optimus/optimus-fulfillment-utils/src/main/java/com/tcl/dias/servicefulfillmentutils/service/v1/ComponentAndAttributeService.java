package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.FlowGroupAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.FlowGroupAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumber;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumberCustRequest;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumberRequest;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited ComponentAndAttributeService used
 *            for operation related to components and attribute changes
 */

@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class ComponentAndAttributeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComponentAndAttributeService.class);


	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;
	
	@Autowired
	private FlowGroupAttributeRepository flowGroupAttributeRepository;
	
	@Autowired
	private GscFlowGroupRepository gscFlowGroupRepository;

	@Autowired
	private ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	private ScOrderRepository scOrderRepository;
	
	
	public String getAdditionalAttributes(ScServiceDetail scServiceDetail, String attributeName) {
		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), attributeName);
		
		if(scServiceAttribute!=null && scServiceAttribute.getAttributeValue()!=null && !scServiceAttribute.getAttributeValue().isEmpty()) {
			Optional<ScAdditionalServiceParam> scAdditionalServiceParam=scAdditionalServiceParamRepository.findById(Integer.valueOf(scServiceAttribute.getAttributeValue()));
			
			if(scAdditionalServiceParam.isPresent()) {
				return scAdditionalServiceParam.get().getValue();
			}
		}
		return null;
	}
	
	
	@Transactional(readOnly = false)
	public void updateAdditionalAttributes(ScServiceDetail scServiceDetail, String attributeName, String errorMessage,
			String category,String taskName) {

		try {
			LOGGER.info("updateAdditionalAttributes :{}with error message:{}", scServiceDetail,errorMessage);


			ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
					.findByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(), attributeName, category);

			LOGGER.info("updateAdditionalAttributes scServiceAttribute id:{}", scServiceDetail.getId());

			if (scServiceAttribute == null) {
				LOGGER.info("updateAdditionalAttributes scServiceAttribute is null");

				createServiceAttrAndAdditionalAttr(scServiceDetail, attributeName, errorMessage, category,taskName);
			} else {
				LOGGER.info("updateAdditionalAttributes scServiceAttribute is not null");

				updateServiceAttrAndAdditionalAttr(scServiceAttribute, attributeName, errorMessage,taskName);
			}
		} catch (Exception e) {
			LOGGER.info("updateAdditionalAttributes error-------->:{}", e);
		}

	}
	
	public void updateAdditionalAttributes(ScServiceDetail scServiceDetail, String attributeName, String attributeValue) {

		try {
			ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
					.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), attributeName);

			LOGGER.info("updateAdditionalAttributes scServiceAttribute id:{}", scServiceDetail.getId());

			if (scServiceAttribute == null) {
				LOGGER.info("updateAdditionalAttributes scServiceAttribute is null");

				createServiceAttrAndAdditionalAttr(scServiceDetail, attributeName, attributeValue);
			} else {
				LOGGER.info("updateAdditionalAttributes scServiceAttribute is not null");

				updateServiceAttrAndAdditionalAttr(scServiceAttribute, attributeName, attributeValue);
			}
		} catch (Exception e) {
			LOGGER.info("updateAdditionalAttributes error-------->:{}", e);
		}

	}
	
	public void updateComponentAdditionalAttributes(ScServiceDetail scServiceDetail, Integer componentId,String attributeName, String attributeValue) {

		try {
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndScComponent_idAndAttributeName(scServiceDetail.getId(),componentId, attributeName);

			LOGGER.info("updateComponentAdditionalAttributes scServiceAttribute id:{}", scServiceDetail.getId());

			if (scComponentAttribute == null) {
				LOGGER.info("updateComponentAdditionalAttributes scServiceAttribute is null");
				Optional<ScComponent> scComponentOptional=scComponentRepository.findById(componentId);
				if(scComponentOptional.isPresent()){
					createComponentAttrAndAdditionalAttr(scServiceDetail,scComponentOptional.get(), attributeName, attributeValue);
				}
			} else {
				LOGGER.info("updateComponentAdditionalAttributes scServiceAttribute is not null");

				updateComponentAttrAndAdditionalAttr(scComponentAttribute, attributeValue);
			}
		} catch (Exception e) {
			LOGGER.info("updateComponentAdditionalAttributes error-------->:{}", e);
		}

	}
	
	public void updateAdditionalAttributes(Integer scServiceDetailId, String attributeName, String errorMessage,
			String category,String taskName) {
		Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(scServiceDetailId);
		if (scServiceDetailOptional.isPresent()) {
		updateAdditionalAttributes(scServiceDetailOptional.get(), attributeName, errorMessage,
				category,taskName);
		}
	}
	
	public void addProcessTaskLogsError(String taskKey, ScServiceDetail scServiceDetail, String errorMessage) {

		try {
			
			LOGGER.info("addProcessTaskLogsError with taskKey :{}",taskKey);

			
			if(taskKey!=null) {

			Task task = taskRepository
					.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(scServiceDetail.getId(), taskKey);
			
			LOGGER.info("addProcessTaskLogsError with task :{}",task);


			if (task != null) {

				processTaskLogRepository.save(createProcessTaskLog(task, errorMessage));

			}
			}
		} catch (Exception e) {
			LOGGER.error("addProcessTaskLogsError for taskKey:{}", e);
		}

	}
	
	private ProcessTaskLog createProcessTaskLog(Task task,String errorMessage) {
		LOGGER.info("addProcessTaskLogsError createProcessTaskLog started :{}",task);

		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		processTaskLog.setServiceCode(task.getServiceCode());
		processTaskLog.setAction(TaskLogConstants.FAILED);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setErroMessage(errorMessage);
		return processTaskLog;

	}

	private void updateServiceAttrAndAdditionalAttr(ScServiceAttribute scServiceAttribute, String attributeName,
			String errorMessage,String taskName) {

		if (scServiceAttribute != null) {
			LOGGER.info("updateServiceAttrAndAdditionalAttr serviceId:{}",scServiceAttribute.getAttributeValue());

			Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(scServiceAttribute.getAttributeValue()));
			if (scAddParam.isPresent()) {
				LOGGER.info("updateServiceAttrAndAdditionalAttr scAddParam present:{}",scServiceAttribute.getAttributeValue());

				ScAdditionalServiceParam additionalServiceParam = scAddParam.get();
				additionalServiceParam.setValue(errorMessage);
				scAdditionalServiceParamRepository.save(additionalServiceParam);
				addProcessTaskLogsError(taskName, scServiceAttribute.getScServiceDetail(), errorMessage);

			}
		}

	}
	
	private void updateServiceAttrAndAdditionalAttr(ScServiceAttribute scServiceAttribute, String attributeName,
			String errorMessage) {

		if (scServiceAttribute != null) {
			LOGGER.info("updateServiceAttrAndAdditionalAttr serviceId:{}",scServiceAttribute.getAttributeValue());

			Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(scServiceAttribute.getAttributeValue()));
			if (scAddParam.isPresent()) {
				LOGGER.info("updateServiceAttrAndAdditionalAttr scAddParam presenyt:{}",scServiceAttribute.getAttributeValue());

				ScAdditionalServiceParam additionalServiceParam = scAddParam.get();
				additionalServiceParam.setValue(errorMessage);
				scAdditionalServiceParamRepository.save(additionalServiceParam);

			}
		}

	}

	private void createServiceAttrAndAdditionalAttr( ScServiceDetail scServiceDetail, String attributeName, String errorMessage,
			String category, String taskName) {
		
		LOGGER.info("createServiceAttrAndAdditionalAttr serviceId:{}",scServiceDetail);

		ScServiceAttribute scServiceAttribute = new ScServiceAttribute();

		ScAdditionalServiceParam scAdditionalServiceParam = saveAdditionalParam(attributeName, errorMessage, category);
		scServiceAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
		scServiceAttribute.setAttributeAltValueLabel(attributeName);
		scServiceAttribute.setAttributeName(attributeName);
		scServiceAttribute.setCategory(category);
		scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsActive("Y");
		scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsAdditionalParam("Y");
		scServiceAttribute.setScServiceDetail(scServiceDetail);
		scServiceAttributeRepository.save(scServiceAttribute);
		addProcessTaskLogsError(taskName, scServiceDetail, errorMessage);
	}
	
	private ScAdditionalServiceParam saveAdditionalParam(String attributeName, String errorMessage, String category) {
		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setAttribute(attributeName);
		scAdditionalServiceParam.setCategory(category);
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setValue(errorMessage);
		scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
		return scAdditionalServiceParam;
	}
	
	private void createServiceAttrAndAdditionalAttr( ScServiceDetail scServiceDetail, String attributeName, String errorMessage
			) {
		
		LOGGER.info("createServiceAttrAndAdditionalAttr serviceId:{}",scServiceDetail);

		ScServiceAttribute scServiceAttribute = new ScServiceAttribute();

		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setAttribute(attributeName);
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setValue(errorMessage);
		scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
		scServiceAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
		scServiceAttribute.setAttributeAltValueLabel(attributeName);
		scServiceAttribute.setAttributeName(attributeName);
		scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsActive("Y");
		scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scServiceAttribute.setIsAdditionalParam("Y");
		scServiceAttribute.setScServiceDetail(scServiceDetail);
		scServiceAttributeRepository.save(scServiceAttribute);
	}
	
	private void createComponentAttrAndAdditionalAttr( ScServiceDetail scServiceDetail, ScComponent scComponent, String attributeName, String errorMessage) {
		
		LOGGER.info("createComponentAttrAndAdditionalAttr serviceId:{}",scServiceDetail);

		ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setAttribute(attributeName);
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setValue(errorMessage);
		scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
		scComponentAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
		scComponentAttribute.setAttributeAltValueLabel(attributeName);
		scComponentAttribute.setAttributeName(attributeName);
		scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setIsActive("Y");
		scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setIsAdditionalParam("Y");
		scComponentAttribute.setScServiceDetailId(scServiceDetail.getId());
		scComponentAttribute.setScComponent(scComponent);
		scComponentAttributesRepository.save(scComponentAttribute);
	}
	
	private void updateComponentAttrAndAdditionalAttr(ScComponentAttribute scComponentAttribute,String errorMessage) {
		if (scComponentAttribute != null) {
			LOGGER.info("updateComponentAttrAndAdditionalAttr serviceId:{}",scComponentAttribute.getAttributeValue());
			Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
			if (scAddParam.isPresent()) {
				LOGGER.info("updateComponentAttrAndAdditionalAttr scAddParam presenyt:{}",scComponentAttribute.getAttributeValue());
				ScAdditionalServiceParam additionalServiceParam = scAddParam.get();
				additionalServiceParam.setValue(errorMessage);
				scAdditionalServiceParamRepository.save(additionalServiceParam);
			}
		}

	}

	/**
	 * @author Venkata Naga Sai S
	 * @param serviceId
	 * @param atMap
	 * @param componentName used to update attributes
	 * @param siteType
	 */
	public void updateComponentAndAdditionalAttributes(Integer serviceId, Map<String, String> atMap, String componentName,String siteType) {
		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,siteType);
		String userName = Utils.getSource();
		if (scComponent != null) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key : {}, Value : {}", key, value);
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, key, componentName, siteType);
				String val = StringUtils.isEmpty(value)?"":value;
				if (scComponentAttribute == null) {
					if(val.length()>45) {
						createComponentAttrAndAdditionalAttr(key, value, serviceId, scComponent, userName);
					}else {
						createComponentAttr(key, value, serviceId, scComponent, userName);
					}
				} else {
					if(val.length()>45) {
						updateComponentAttrComponentAttrAndAdditionalAttr(key, value, scComponentAttribute, userName);
					}else { 
						updateComponentAttr(key, value, scComponentAttribute, userName);
					}
				}
			});
		}
	}
	

	private void updateComponentAttrComponentAttrAndAdditionalAttr(String key, String attrValue, ScComponentAttribute scComponentAttribute, String userName) {
		if (!StringUtils.isEmpty(attrValue) && "Y".equalsIgnoreCase(scComponentAttribute.getIsAdditionalParam())) {
			Integer addServiceParamId = StringUtils.isEmpty(scComponentAttribute.getAttributeValue())?0:Integer.valueOf(scComponentAttribute.getAttributeValue());
			Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
			if(optScAddServParam.isPresent() && !attrValue.equals(optScAddServParam.get().getValue())) {
				ScAdditionalServiceParam scAddServParam = optScAddServParam.get();
				scAddServParam.setValue(attrValue);
				scAdditionalServiceParamRepository.save(scAddServParam);
			}
		}
	}


	private void createComponentAttrAndAdditionalAttr(String key, String value, Integer serviceId, ScComponent scComponent, String userName) {
		LOGGER.info("createComponentAttrAndAdditionalAttr serviceId:{}",serviceId);
		if(Objects.nonNull(value)) {
			ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
			ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
			scAdditionalServiceParam.setAttribute(key);
			scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
			scAdditionalServiceParam.setIsActive(CommonConstants.Y);
			scAdditionalServiceParam.setValue(value);
			scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
			scComponentAttribute.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
			scComponentAttribute.setAttributeAltValueLabel(key);
			scComponentAttribute.setAttributeName(key);
			scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scComponentAttribute.setIsActive("Y");
			scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scComponentAttribute.setIsAdditionalParam("Y");
			scComponentAttribute.setScServiceDetailId(serviceId);
			scComponentAttribute.setScComponent(scComponent);
			scComponentAttributesRepository.save(scComponentAttribute);
		}
	}


	/**
	 * @author vivek
	 * @param serviceId
	 * @param atMap
	 * @param componentName used to update attributes
	 */
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, String componentName,String siteType) {
		LOGGER.info("ComponentName {} and SiteType {}:",componentName,siteType);
		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,siteType);

		String userName = Utils.getSource();
		if (scComponent != null) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key : {}, Value : {}", key, value);
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, key, componentName, siteType);
				if (scComponentAttribute == null) {
					createComponentAttr(key, value, serviceId, scComponent, userName);
				} else {
					updateComponentAttr(key, value, scComponentAttribute, userName);
				}

			});
		}

	}

	public void updateAttributesEndpoint(Integer serviceId,Integer id, Map<String, String> atMap,String siteType) {

		Optional<ScComponent> scComponent = scComponentRepository.findById(id);
		
		String userName = Utils.getSource();
		if(scComponent.isPresent()) {
			if (scComponent != null) {
				atMap.forEach((key, value) -> {
					LOGGER.info("upateEndpointKey: {}, upateEndpointValue: {}", key, value);
					ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponentOrderByIdDesc(serviceId, key, scComponent.get());
					LOGGER.info("scComponentAttribute: {}", scComponentAttribute );
					if (scComponentAttribute == null) {
						createComponentAttr(key, value, serviceId, scComponent.get(), userName);
					} else {
						updateComponentAttr(key, value, scComponentAttribute, userName);
					}

				});
			}
		}
	}

//	public void updateAttributesEndpoint(Integer serviceId, Map<String, String> atMap,String siteType) {
//
//		List<ScComponent> scComponents = scComponentRepository
//				.findByScServiceDetailIdAndSiteType(serviceId,siteType);
//
//		String userName = Utils.getSource();
//		if (scComponents != null) {
//			scComponents.forEach(scComponent -> {
//				if(!scComponent.getComponentName().equalsIgnoreCase("WebEx-License")){
//					atMap.forEach((key, value) -> {
//						ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
//								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, key, scComponent.getComponentName(), siteType);
//						if (scComponentAttribute == null) {
//							createComponentAttr(key, value, serviceId, scComponent, userName);
//						} else {
//							updateComponentAttr(key, value, scComponentAttribute, userName);
//						}
//					});
//				}
//			});
//		}
//	}

	private void createServiceAttribute(String key, String value, ScServiceDetail scServiceDetail){
		if(Objects.nonNull(value) && !value.isEmpty()) {
			ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setAttributeName(key);
			scServiceAttribute.setAttributeValue(value);
			scServiceAttribute.setAttributeAltValueLabel(value);
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			scServiceAttribute.setIsActive("Y");
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttributeRepository.save(scServiceAttribute);
		}
	}

	private void updateServiceAttribute(String key, String value, ScServiceAttribute scServiceAttribute){
		if (!StringUtils.isEmpty(value) && !value.equals(scServiceAttribute.getAttributeValue())) {
			scServiceAttribute.setAttributeName(key);
			scServiceAttribute.setAttributeValue(value);
			scServiceAttribute.setAttributeAltValueLabel(value);
			scServiceAttributeRepository.save(scServiceAttribute);
		}
	}
	
	public void updateServiceAttributesWithNull(Integer scServiceDetailId,Map<String, String> attrMap){
		if(!CollectionUtils.isEmpty(attrMap)){
			attrMap.forEach((k,v)->{
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetailId, k);
				if(scServiceAttribute!=null) {
					scServiceAttribute.setAttributeName(k);
					scServiceAttribute.setAttributeValue(v);
					scServiceAttribute.setAttributeAltValueLabel(v);
					scServiceAttributeRepository.save(scServiceAttribute);
				}
			});
		}
	}


	public void updateServiceAttributes(Integer scServiceDetailId, Map<String, String> attrMap) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
		if(!CollectionUtils.isEmpty(attrMap)){
			attrMap.forEach((k,v)->{
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetailId, k);
				if(Objects.isNull(scServiceAttribute)){
					createServiceAttribute(k,v,scServiceDetail);
				}
				else{
					updateServiceAttribute(k,v,scServiceAttribute);
				}
			});
		}

	}

	public void updateFeasibilityJson(Integer scServiceDetailId, String feasibilityJson) throws TclCommonException {
		if((Objects.nonNull(feasibilityJson)) && !org.springframework.util.StringUtils.isEmpty(feasibilityJson)) {
			Map<String, String> attrMap = Utils.convertJsonToObject(feasibilityJson,
					Map.class);
			updateServiceAttributes(scServiceDetailId, attrMap, "FEASIBILITY");
		}
	}
	
	public void updateSiteTopologyServiceAttributes(Integer scServiceDetailId, Map<String, String> attrMap) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
		if(!CollectionUtils.isEmpty(attrMap)){
			attrMap.forEach((k,v)->{
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetailId, k);
				if(Objects.isNull(scServiceAttribute)){
					createServiceAttribute(k,v,scServiceDetail);
				}
				else{
					List<ScServiceAttribute> scServiceAttributeList = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(scServiceDetailId, Arrays.asList(k));
					if(Objects.nonNull(scServiceAttributeList) && !scServiceAttributeList.isEmpty()){
						scServiceAttributeList.stream().forEach(scServiceAttr -> {
							updateServiceAttribute(k,v,scServiceAttr);
						});
					}
				}
			});
		}
	}

	/**
	 * @author kaushik
	 * @param serviceId
	 * @param atMap
	 * @param category used to update attributes
	 */
	public void updateServiceAttributes(Integer serviceId, Map<String, String> atMap, String category) {

		Optional <ScServiceDetail> scServiceDetailOptional= scServiceDetailRepository.
				findById(serviceId);
		String userName = Utils.getSource();
		scServiceDetailOptional.ifPresent(scServiceDetail -> {
			atMap.forEach((key, value) -> {
				try {
					ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeName(serviceId, key);

					if (scServiceAttribute == null) {
						createServiceAttr(key, value, serviceId, scServiceDetail, userName, category);
					} else {
						updateServiceAttr(key, value, scServiceAttribute, userName, category);
					}
				} catch (Exception e) {
					scServiceAttributeRepository.deleteByScServiceDetail_idAndAttributeName(serviceId, key);
					createServiceAttr(key, value, serviceId, scServiceDetail, userName, category);
				}
			});

		});
	}

	/**
	 * @author kaushik
	 * @param serviceId
	 * @param atMap
	 * @param category used to update attributes
	 */
	public void updateScServiceAttributes(Integer serviceId, Map<String, String> atMap, Map<String, String> categoryMap,String category) {

		Optional <ScServiceDetail> scServiceDetailOptional= scServiceDetailRepository.
				findById(serviceId);
		String userName = Utils.getSource();
		scServiceDetailOptional.ifPresent(scServiceDetail -> {
			atMap.forEach((key, value) -> {
				try {
					ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeName(serviceId, key);

					if(Objects.nonNull(categoryMap)) {
						if (scServiceAttribute == null) {
							createServiceAttr(key, value, serviceId, scServiceDetail, userName, categoryMap.get(key));
						} else {
							updateServiceAttr(key, value, scServiceAttribute, userName, categoryMap.get(key));
						}
					}
					else
					{
						if (scServiceAttribute == null) {
							createServiceAttr(key, value, serviceId, scServiceDetail, userName, category);
						} else {
							updateServiceAttr(key, value, scServiceAttribute, userName, category);
						}
					}
				} catch (Exception e) {
					scServiceAttributeRepository.deleteByScServiceDetail_idAndAttributeName(serviceId, key);
					createServiceAttr(key, value, serviceId, scServiceDetail, userName, category);
				}
			});

		});
	}


	private void createServiceAttr(String attrName, String attrValue, Integer serviceId, ScServiceDetail scServiceDetail,
								   String userName, String category) {
		ScServiceAttribute scServiceAttribute = new ScServiceAttribute();

		if(Objects.nonNull(attrValue)) {

			scServiceAttribute.setAttributeValue(attrValue);
			scServiceAttribute.setAttributeAltValueLabel(attrValue);
			scServiceAttribute.setAttributeName(attrName);
			scServiceAttribute.setCategory(category);
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsActive("Y");
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsAdditionalParam("N");
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			scServiceAttributeRepository.save(scServiceAttribute);
		}
	}



	private void updateServiceAttr(String attrName, String attrValue, ScServiceAttribute scServiceAttribute,
									 String userName, String category) {

		if ((attrValue != null && !attrValue.isEmpty() && !attrValue.equals(scServiceAttribute.getAttributeValue()))||((category != null && !category.isEmpty() && !category.equals(scServiceAttribute.getCategory())))) {
			scServiceAttribute.setAttributeName(attrName);
			scServiceAttribute.setAttributeValue(attrValue);
			scServiceAttribute.setUpdatedBy(userName);
			if(category!=null){
				LOGGER.info("Category received: {}", category);
				scServiceAttribute.setCategory(category);
			}
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttributeRepository.save(scServiceAttribute);
		}
	}

	/**
	 * @author vivek
	 * @param attrName
	 * @param attrValue
	 * @param scComponentAttribute
	 * @param userName             used to update component attr
	 */
	public void updateComponentAttr(String attrName, String attrValue, ScComponentAttribute scComponentAttribute,
			String userName) {

		if (attrValue != null && !attrValue.isEmpty() && !attrValue.equals(scComponentAttribute.getAttributeValue())) {
			scComponentAttribute.setAttributeName(attrName);
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setAttributeAltValueLabel(attrValue);
			scComponentAttribute.setUpdatedBy(userName);
			scComponentAttributesRepository.save(scComponentAttribute);
		}
	}

	/**
	 * @author vivek
	 * @param attrName
	 * @param attrValue
	 * @param scServiceDetail
	 * @param scComponent
	 * @param userName        used to create attribute
	 */
	private void createComponentAttr(String attrName, String attrValue, Integer serviceId, ScComponent scComponent,
									 String userName) {
		if(Objects.nonNull(attrValue)) {
			ScComponentAttribute attribute = new ScComponentAttribute();
			attribute.setAttributeName(attrName);
			attribute.setAttributeValue(attrValue);
			attribute.setAttributeAltValueLabel(attrName);
			attribute.setScComponent(scComponent);
			attribute.setScServiceDetailId(serviceId);
			attribute.setIsActive("Y");
			attribute.setCreatedDate(new Timestamp(new Date().getTime()));
			attribute.setCreatedBy(userName);
			scComponentAttributesRepository.save(attribute);
		}
	}
	
	public void saveFlowGroupAttriputeWithAdditionalParam(String attrName, String attrValue, Integer flowgroupId, String category,
			Integer scServiceDetailId) {
		
		Optional<GscFlowGroup> optGscFlowGroup =  gscFlowGroupRepository.findById(flowgroupId);
		if(optGscFlowGroup.isPresent()) {
			GscFlowGroup gscFlowGroup = optGscFlowGroup.get();
			ScAdditionalServiceParam scAdditionalServiceParam = saveAdditionalParam(attrName, attrValue, category);
			FlowGroupAttribute flowGroupAttribute = flowGroupAttributeRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndGscFlowGroupOrderByIdDesc(scServiceDetailId, attrName, gscFlowGroup);
			if(flowGroupAttribute == null) {
				flowGroupAttribute = new FlowGroupAttribute();
			}
			flowGroupAttribute.setAttributeName(attrName);
			flowGroupAttribute.setAttributeValue(scAdditionalServiceParam.getId().toString());
			flowGroupAttribute.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			flowGroupAttribute.setGscFlowGroup(gscFlowGroup);
			flowGroupAttribute.setIsActive(CommonConstants.Y);
			flowGroupAttribute.setIsAdditionalParam(CommonConstants.Y);
			flowGroupAttribute.setScServiceDetailId(scServiceDetailId);
			flowGroupAttributeRepository.save(flowGroupAttribute);
		} else {
			LOGGER.info("GSC Flow Group Not exist for this flow group Id {} and Service Id {}", flowgroupId, scServiceDetailId);
		}
	}
	
	public String getFlowGruopAdditionalParam(String attrName, Integer flowgroupId, Integer scServiceDetailId) {
		Optional<GscFlowGroup> optGscFlowGroup =  gscFlowGroupRepository.findById(flowgroupId);
		if(optGscFlowGroup.isPresent()) {
			GscFlowGroup gscFlowGroup = optGscFlowGroup.get();
			FlowGroupAttribute flowGroupAttribute = flowGroupAttributeRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndGscFlowGroupOrderByIdDesc(scServiceDetailId, attrName, gscFlowGroup);
			if(flowGroupAttribute != null) {
				Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
						.findById(Integer.valueOf(flowGroupAttribute.getAttributeValue()));
				if(scAdditionalServiceParam.isPresent()) {
					return scAdditionalServiceParam.get().getValue();
				}else {
					LOGGER.info("GSC Flow Group Attribute Addtional Param Not exist for this attrName {} flow group Id {} and Service Id {}", attrName, flowgroupId, scServiceDetailId);
				}

			}else {
				LOGGER.info("GSC Flow Group Attribute Not exist for this attrName {} flow group Id {} and Service Id {}", attrName, flowgroupId, scServiceDetailId);
			}
		}else {
			LOGGER.info("GSC Flow Group Not exist for this flow group Id {} and Service Id {}", flowgroupId, scServiceDetailId);
		}
		return null;
	}

	
	
	public String getErrorMessageDetails(String message, String code) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(new Date().getTime()));

		LOGGER.info("getErrorMessageDetails forund");

		ErrorBean errorBean = new ErrorBean();
		errorBean.setErrorLongDescription(message);
		errorBean.setErrorCode(code);
		errorBean.setErrorShortDescription(message);
		errorDetailsBean.getErrorDetails().add(errorBean);

		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	public String getErrorMessageDetails(DidNumberRequest didNumberRequest, String code) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(new Date().getTime()));
		
		for (DidNumberCustRequest didNumberCustRequest : didNumberRequest.getCustRequests()) {
			
			LOGGER.info("getErrorMessageDetails forund");
			
			for (DidNumber didNumber : didNumberCustRequest.getNumbers()) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErrorLongDescription(didNumber.getStatusMsg());
				errorBean.setErrorCode(didNumber.getStatus());
				errorBean.setErrorShortDescription(didNumber.getStatusMsg());
				errorDetailsBean.getErrorDetails().add(errorBean);
			}
			
		}


		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	public String getErrorMessageDetails(DidNumberCustRequest didNumberCustRequest, String code) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(new Date().getTime()));
		
			
			LOGGER.info("getErrorMessageDetails forund");
			
			for (DidNumber didNumber : didNumberCustRequest.getNumbers()) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErrorLongDescription(didNumber.getStatusMsg());
				errorBean.setErrorCode(didNumber.getStatus());
				errorBean.setErrorShortDescription(didNumber.getStatusMsg());
				errorDetailsBean.getErrorDetails().add(errorBean);
			}
			


		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	public void loadServiceAttributesIfNotPresent(ScServiceDetail scServiceDetail, Map<String, String> attrMap) {
		if(!CollectionUtils.isEmpty(attrMap)){
			attrMap.forEach((k,v)->{
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), k);
				if(Objects.isNull(scServiceAttribute)){
					createServiceAttribute(k,v,scServiceDetail);
				}
			});
		}

	}
	
	public void updateAttributesByScComponent(Integer serviceId, Map<String, String> atMap,Integer componentId) {

		Optional<ScComponent> scComponent = scComponentRepository.findById(componentId);

		String userName = Utils.getSource();
		if (scComponent.isPresent()) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key" + key + "value" + value);
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScComponent_idAndAttributeName(componentId, key);
				if (scComponentAttribute == null) {
					createComponentAttr(key, value, serviceId, scComponent.get(), userName);
				} else {
					updateComponentAttr(key, value, scComponentAttribute, userName);
				}

			});
		}

	}
	
	@Transactional(readOnly = false)
	public void updateCompAttributes(Integer serviceId, Map<String, String> atMap, Integer componentId) {
		Optional<ScComponent> scComponentOpt = scComponentRepository.findById(componentId);
		String userName = Utils.getSource();
		if (scComponentOpt.isPresent()) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key"+key+"value"+value);
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScComponent_idAndAttributeName(componentId, key);
				if (scComponentAttribute == null) {
					createComponentAttr(key, value, serviceId, scComponentOpt.get(), userName);
				} else {
					updateComponentAttr(key, value, scComponentAttribute, userName);
				}
			});
		}

	}
	
	public String getAdditionalAttributesByScComponentAttr(Integer componentId, String attributeName,String vendorCode) {
		if(vendorCode!=null) {
			attributeName.concat(vendorCode);
		}
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScComponent_idAndAttributeName(componentId, attributeName);
		
		if(scComponentAttribute!=null && scComponentAttribute.getAttributeValue()!=null && !scComponentAttribute.getAttributeValue().isEmpty()) {
			Optional<ScAdditionalServiceParam> scAdditionalServiceParam=scAdditionalServiceParamRepository.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
			
			if(scAdditionalServiceParam.isPresent()) {
				return scAdditionalServiceParam.get().getValue();
			}
		}
		return null;
	}

	private void createServiceOrderAttr(String attrName, String attrValue, Integer serviceId, ScOrder scOrder,
										String userName, String category) {
		ScOrderAttribute scOrderAttribute = new ScOrderAttribute();

		if(Objects.nonNull(attrValue)) {

			scOrderAttribute.setAttributeValue(attrValue);
			scOrderAttribute.setAttributeAltValueLabel(attrValue);
			scOrderAttribute.setAttributeName(attrName);
			scOrderAttribute.setCategory(category);
			scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scOrderAttribute.setIsActive("Y");
			scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scOrderAttribute.setScOrder(scOrder);
			scOrderAttribute.setUpdatedBy(userName);
			scOrderAttribute.setCreatedBy(userName);
			scOrderAttributeRepository.save(scOrderAttribute);
		}
	}



	private void updateServiceOrderAttr(String attrName, String attrValue, ScOrderAttribute scOrderAttribute,
										String userName, String category) {

		if (attrValue != null && !attrValue.isEmpty() && !attrValue.equals(scOrderAttribute.getAttributeValue())) {
			scOrderAttribute.setAttributeName(attrName);
			scOrderAttribute.setAttributeValue(attrValue);
			scOrderAttribute.setUpdatedBy(userName);
			if(category!=null){
				LOGGER.info("Category received: {}", category);
				scOrderAttribute.setCategory(category);
			}
			scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scOrderAttributeRepository.save(scOrderAttribute);
		}
	}




	/**
	 * @author Thamizhselvi Perumal
	 * @param serviceId
	 * @param atMap
	 * @param category used to update attributes
	 */
	public void updateServiceOrderAttributes(Integer serviceId, Map<String, String> atMap) {

		Optional <ScServiceDetail> scServiceDetailOptional= scServiceDetailRepository.
				findById(serviceId);
		String userName = Utils.getSource();


		scServiceDetailOptional.ifPresent(scServiceDetail -> {
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(),"Y");
			atMap.forEach((key, value) -> {
				try {
					ScOrderAttribute scOrderAttribute = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scOrder.getId(),key);


					if (scOrderAttribute == null) {
						createServiceOrderAttr(key, value, serviceId, scOrder, userName, null);
					} else {
						updateServiceOrderAttr(key, value, scOrderAttribute, userName, null);
					}

				} catch (Exception e) {
					scOrderAttributeRepository.deleteByScOrder_idAndAttributeName(scOrder.getId(),key);
					createServiceOrderAttr(key, value, serviceId, scOrder, userName, null);
				}
			});

		});
	}

}
