package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import java.util.Arrays;

import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.ProcessTaskLog;
import com.tcl.dias.networkaugment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.networkaugment.entity.entities.ScComponent;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.networkaugment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
//		processTaskLog.setQuoteCode(task.getQuoteCode());
//		processTaskLog.setQuoteId(task.getQuoteId());
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

		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setAttribute(attributeName);
		scAdditionalServiceParam.setCategory(category);
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setValue(errorMessage);
		scAdditionalServiceParamRepository.save(scAdditionalServiceParam);
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

	/**
	 * @author vivek
	 * @param serviceId
	 * @param atMap
	 * @param componentName used to update attributes
	 */
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, String componentName,String siteType) {

		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,siteType);

		String userName = Utils.getSource();
		if (scComponent != null) {
			atMap.forEach((key, value) -> {
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

		if (attrValue != null && !attrValue.isEmpty() && !attrValue.equals(scServiceAttribute.getAttributeValue())) {
			scServiceAttribute.setAttributeName(attrName);
			scServiceAttribute.setAttributeValue(attrValue);
			scServiceAttribute.setUpdatedBy(userName);
			if(category!=null){
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
	private void updateComponentAttr(String attrName, String attrValue, ScComponentAttribute scComponentAttribute,
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
	 * @param //scServiceDetail
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

	
	
	public String getErrorMessageDetails(String message, String code) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		LOGGER.info("getErrorMessageDetails forund");

		ErrorBean errorBean = new ErrorBean();
		errorBean.setErrorLongDescription(message);
		errorBean.setErrorCode(code);
		errorBean.setErrorShortDescription(message);
		errorDetailsBean.getErrorDetails().add(errorBean);

		return Utils.convertObjectToJson(errorDetailsBean);

	}
}
