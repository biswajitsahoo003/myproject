package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.networkaugment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.networkaugment.entity.entities.ScComponent;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScOrderAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.repository.AppointmentRepository;
import com.tcl.dias.networkaugment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.networkaugment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.VendorsRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskAttributesBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the AttributeServiceImpl.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = true)
public class AttributeServiceImpl implements AttributeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeServiceImpl.class);

	@Autowired
	TaskAttributeMasterSingleton taskAttributeMasterSingleton;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	VendorsRepository vendorsRepository;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	
	@Autowired
    AttachmentService attachmentService;

	/**
	 * getTaskAttributes - This Method takes the taskId and give the attributes
	 * 
	 * @param //taskId
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,String siteType) throws TclCommonException {
		LOGGER.info("Entering task Attributes for taskName {}, serviceId{}",taskName, serviceId);
		Map<String, Object> categoryMapper = new HashMap<String, Object>();
		List<TaskAttributesBean> taskAttributes = taskAttributeMasterSingleton.getTaskAttribute(taskName);
		taskAttributes = (taskAttributes == null) ? new ArrayList<>() : taskAttributes;
		taskAttributes.addAll(taskAttributeMasterSingleton.getTaskAttribute(null));
		return getAttributes(serviceId, taskAttributes, categoryMapper,siteType);
	}

	@Override
	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,
			AttributeManipulator attributeManipulator,String siteType) throws TclCommonException {
		LOGGER.info("Entering task Attributes with manipulator");
		return attributeManipulator.manipulate(getTaskAttributes(taskName, serviceId,siteType));
	}

	/**
	 * getServiceAttributes
	 * 
	 * @throws TclCommonException
	 */
	private Map<String, Object> getAttributes(Integer serviceId, List<TaskAttributesBean> taskAttributes,
			Map<String, Object> categoryMapper,String siteType) throws TclCommonException {
		Map<String, Object> responseMapper = new HashMap<>();
		try {
			if (taskAttributes != null) {
				LOGGER.info("getAttributes serviceId {}",serviceId);
				Optional<ScServiceDetail> serviceDetailEntity = scServiceDetailRepository.findById(serviceId);
				if (serviceDetailEntity.isPresent()) {
					ScServiceDetail scServiceDetail = serviceDetailEntity.get();
					for (TaskAttributesBean taskAttributesBean : taskAttributes) {

						if (taskAttributesBean.getCategory().equals(AttributeConstants.ORDER)) {
							extractOrderInformations(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.ORDER_ATTRIBUTES)) {
							extractOrderAttr(categoryMapper, responseMapper, scServiceDetail, taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.SERVICE)) {
							extractServiceInformations(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.SERVICE_ATTRIBUTES)) {
							extractServiceAttributes(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean,siteType);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.CUSTOMER_CONTRACT_INFO)) {
							extractCustomerContractInfo(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.COMPONENT)) {
							extractComponentInformation(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean,siteType);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.COMPONENT_ATTRIBUTES)) {
							extractComponentAttr(categoryMapper, responseMapper, scServiceDetail, taskAttributesBean,siteType);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.VENDORS)) {
							extractVendorInformations(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.APPOINTMENT)) {
							extractAppointmentInformation(categoryMapper, responseMapper, scServiceDetail,
									taskAttributesBean);
						} else if (taskAttributesBean.getCategory().equals(AttributeConstants.FIELD_ENGINEER)) {
							extractFieldEngineer(categoryMapper, responseMapper, scServiceDetail, taskAttributesBean);
						}else if (taskAttributesBean.getCategory().equals(AttributeConstants.ATTACHMENT)) {
							extractAttachment(responseMapper, scServiceDetail, taskAttributesBean,siteType);
						}else if (taskAttributesBean.getCategory().equals(AttributeConstants.ATTACHMENTLIST)) {
							extractAttachmentList(responseMapper, scServiceDetail, taskAttributesBean,siteType);
						}
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("getAttributes EXception {}",e);
			e.printStackTrace();
			//throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return responseMapper;

	}

	private void extractCustomerContractInfo(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> scOrderEntity = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			scOrderEntity = scContractInfoRepository.findByScOrderId(scServiceDetail.getScOrder().getId());
			categoryMapper.put(taskAttributesBean.getCategory(), scOrderEntity);
		} else {
			scOrderEntity = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (scOrderEntity != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					scOrderEntity.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractComponentInformation
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractComponentInformation(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean,String siteType) {
		Map<String, Object> componentInformation = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			componentInformation = scComponentRepository.findByScServiceDetail_idAndMstComponentType_id(
					scServiceDetail.getId(), Integer.valueOf(taskAttributesBean.getSubCategory()),siteType);
			categoryMapper.put(taskAttributesBean.getCategory(), componentInformation);
		} else {
			componentInformation = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (componentInformation != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					componentInformation.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractAppointmentInformation
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractAppointmentInformation(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> appointmentList = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			appointmentList = appointmentRepository.findByServiceId(scServiceDetail.getId(),
					taskAttributesBean.getSubCategory());
			categoryMapper.put(taskAttributesBean.getCategory(), appointmentList);
		} else {
			appointmentList = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (appointmentList != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					appointmentList.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractFieldEngineer
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractFieldEngineer(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> fieldEnggList = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			fieldEnggList = fieldEngineerRepository.findByServiceId(scServiceDetail.getId(),
					taskAttributesBean.getSubCategory());
			categoryMapper.put(taskAttributesBean.getCategory(), fieldEnggList);
		} else {
			fieldEnggList = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (fieldEnggList != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					fieldEnggList.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractVendorInformations
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractVendorInformations(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> vendorsList = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			vendorsList = vendorsRepository.findByServiceId(scServiceDetail.getId(),
					taskAttributesBean.getSubCategory());
			categoryMapper.put(taskAttributesBean.getCategory(), vendorsList);
		} else {
			vendorsList = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (vendorsList != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					vendorsList.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractServiceAttributes
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractServiceAttributes(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean,String siteType) {
		
		if(siteType!=null)siteType=siteType.toLowerCase()+"_";
		List<ScServiceAttribute> scServiceAttributes = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			scServiceAttributes = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndAttributeValueIsNotNull(scServiceDetail.getId(), CommonConstants.Y);
			categoryMapper.put(taskAttributesBean.getCategory(), scServiceAttributes);
		} else {
			scServiceAttributes = (List<ScServiceAttribute>) categoryMapper.get(taskAttributesBean.getCategory());
		}

		if (!scServiceAttributes.isEmpty()) {
			for (ScServiceAttribute scServiceAttribute : scServiceAttributes) {
				if (scServiceAttribute.getAttributeName().equals(taskAttributesBean.getAttributeName()) || (scServiceAttribute.getAttributeName()).equals(siteType+taskAttributesBean.getAttributeName() )) {
					
					
					if (scServiceAttribute.getIsAdditionalParam()!=null && scServiceAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
						Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository.findById(Integer.valueOf(scServiceAttribute.getAttributeValue()));
						if (scAddParam.isPresent()) {
							/*if (taskAttributesBean.getNodeName().equalsIgnoreCase("accessRings") || taskAttributesBean.getNodeName().equalsIgnoreCase("muxDetails")) {
								try {
									ObjectMapper mapper = new ObjectMapper();
									if (taskAttributesBean.getNodeName().equalsIgnoreCase("accessRings")) {								
										if (scAddParam.get().getValue() != null) {
											List<AccessRingInfo> ringList = mapper.readValue(scAddParam.get().getValue(), new TypeReference<List<AccessRingInfo>>() {
											});
											responseMapper.put(taskAttributesBean.getNodeName(), ringList);
										} else {
											responseMapper.put(taskAttributesBean.getNodeName(), null);
										}
									} else {
										if (taskAttributesBean.getNodeName().equalsIgnoreCase("muxDetails")) {
											if (scAddParam.get().getValue() != null) {
												List<MuxDetailsItem> ringList = mapper.readValue(scAddParam.get().getValue(), new TypeReference<List<MuxDetailsItem>>() {
												});
												responseMapper.put(taskAttributesBean.getNodeName(), ringList);
											} else {
												responseMapper.put(taskAttributesBean.getNodeName(), null);
											}
										}
									}
								} catch (Exception e) {
									LOGGER.error("parsing json arror error ---->:{}", e);
								}
							} else {*/
							if (taskAttributesBean.getNodeName().equalsIgnoreCase("secsCodes")) {
								try {
									ObjectMapper mapper = new ObjectMapper();
									if (scAddParam.get().getValue() != null) {
										List<String> list = mapper.readValue(scAddParam.get().getValue(), new TypeReference<List<String>>() {});
										responseMapper.put(taskAttributesBean.getNodeName(), list);
									} else {
										responseMapper.put(taskAttributesBean.getNodeName(), null);
									}
								} catch (Exception e) {
									LOGGER.error("parsing json arror error secsCodes ---->:{}", e);
								}
							}else {
								responseMapper.put(taskAttributesBean.getNodeName(), scAddParam.get().getValue());
							}
							//}
						}
					} else {
						responseMapper.put(taskAttributesBean.getNodeName(), scServiceAttribute.getAttributeValue());						
					}
					break;
				}

			}
		}
	}

	/**
	 * extractServiceInformations
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractServiceInformations(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> scServiceEntity = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			scServiceEntity = scServiceDetailRepository.findByScServiceId(scServiceDetail.getId());
			categoryMapper.put(taskAttributesBean.getCategory(), scServiceEntity);
		} else {
			scServiceEntity = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (scServiceEntity != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					scServiceEntity.get(taskAttributesBean.getAttributeName()));
		}
	}

	/**
	 * extractOrderAttr
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractOrderAttr(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		List<ScOrderAttribute> scOrderAttrs = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			scOrderAttrs = scOrderAttributeRepository.findByScOrder_IdAndIsActive(scServiceDetail.getScOrder().getId(),
					CommonConstants.Y);
			categoryMapper.put(taskAttributesBean.getCategory(), scOrderAttrs);
		} else {
			scOrderAttrs = (List<ScOrderAttribute>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (!scOrderAttrs.isEmpty()) {
			for (ScOrderAttribute scOrderAttr : scOrderAttrs) {
				if (scOrderAttr.getAttributeName().equals(taskAttributesBean.getAttributeName())) {
					responseMapper.put(taskAttributesBean.getNodeName(), scOrderAttr.getAttributeValue());
					break;
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void extractComponentAttr(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean,String siteType) {
		Set<ScComponentAttribute> scCompoAttr = null;
		if (categoryMapper.get(taskAttributesBean.getSubCategory()) == null) {
			ScComponent  scComponent = scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(scServiceDetail.getId(), taskAttributesBean.getSubCategory(),siteType);
			if(scComponent!=null) {
			scCompoAttr = scComponent.getScComponentAttributes();
			categoryMapper.put(taskAttributesBean.getSubCategory(), scCompoAttr);
			}
		} else {
			scCompoAttr = (Set<ScComponentAttribute>) categoryMapper.get(taskAttributesBean.getSubCategory());
		}
		if (scCompoAttr!=null && !scCompoAttr.isEmpty()) {
			for (ScComponentAttribute compAttr : scCompoAttr) {
				if (compAttr.getAttributeName().equals(taskAttributesBean.getAttributeName())) {
					responseMapper.put(taskAttributesBean.getNodeName(), compAttr.getAttributeValue());
					break;
				}

			}
		}else {
			LOGGER.info("{} finalMap empty",taskAttributesBean.getNodeName());
		}
	}

	/**
	 * extractOrderInformations
	 * 
	 * @param categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 */
	@SuppressWarnings("unchecked")
	private void extractOrderInformations(Map<String, Object> categoryMapper, Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean) {
		Map<String, Object> scOrderEntity = null;
		if (categoryMapper.get(taskAttributesBean.getCategory()) == null) {
			scOrderEntity = scOrderRepository.findByScOrderId(scServiceDetail.getScOrder().getId());
			categoryMapper.put(taskAttributesBean.getCategory(), scOrderEntity);
		} else {
			scOrderEntity = (Map<String, Object>) categoryMapper.get(taskAttributesBean.getCategory());
		}
		if (scOrderEntity != null) {
			responseMapper.put(taskAttributesBean.getNodeName(),
					scOrderEntity.get(taskAttributesBean.getAttributeName()));
		}
	}
	
	/**
	 * extractAttachment
	 * 
	 * @param //categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 * @param siteType 
	 */
	private void extractAttachment(Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean, String siteType) {
		LOGGER.info("extractAttachment-scServiceDetail={},taskAttributesBean={}",scServiceDetail,taskAttributesBean);
		AttachmentBean attachmentBean = attachmentService.getAttachmentByServiceIdAndCategory(scServiceDetail.getId(),taskAttributesBean.getSubCategory(),siteType);
		if(attachmentBean!=null) {
			responseMapper.put(taskAttributesBean.getNodeName(), attachmentBean);
		}
	}
	
	/**
	 * extractAttachment
	 * 
	 * @param //categoryMapper
	 * @param responseMapper
	 * @param scServiceDetail
	 * @param taskAttributesBean
	 * @param siteType 
	 */
	private void extractAttachmentList(Map<String, Object> responseMapper,
			ScServiceDetail scServiceDetail, TaskAttributesBean taskAttributesBean, String siteType) {
		List<AttachmentBean> attachmentBeans = attachmentService.getAllAttachmentByServiceIdAndcategory(scServiceDetail.getId(),Arrays.asList(taskAttributesBean.getSubCategory().split(",")),siteType);
		responseMapper.put(taskAttributesBean.getNodeName(), attachmentBeans);
	}


}
