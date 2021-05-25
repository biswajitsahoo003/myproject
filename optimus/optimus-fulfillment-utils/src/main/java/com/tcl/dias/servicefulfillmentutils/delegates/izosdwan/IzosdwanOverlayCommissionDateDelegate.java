package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.repository.MstStatusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Delegate to update commissioning Date for all Underlay/Overlay for SdWan
 * 
 * @author yomagesh
 *
 */
@Component("sdwanOverlayCommissionDateDelegate")
public class IzosdwanOverlayCommissionDateDelegate implements JavaDelegate{

	private static final Logger logger = LoggerFactory.getLogger(IzosdwanOverlayCommissionDateDelegate.class);

	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	
	public void execute(DelegateExecution execution) {
		logger.info("SdwanOverlayCommissionDateDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanOverlayCommissionDateDelegate.solutionId={},serviceId={},serviceCode={},executionProcessInstId={}", solutionId,serviceId,serviceCode,execution.getProcessInstanceId());
		
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(solutionId, serviceId,serviceId,Arrays.asList("OVERLAY","UNDERLAY"), "Y");
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("SdwanOverlayCommissionDateDelegate.ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if (IzosdwanCommonConstants.BYON_INTERNET_PRODUCT
						.equals(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) 
						|| (IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT
						.equals(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) && !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))
						|| IzosdwanCommonConstants.DIA_PRODUCT
						.equals(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| (IzosdwanCommonConstants.GVPN_PRODUCT
						.equals(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) && !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))) {
					logger.info("BYON Internet or IWAN or DIA or GVPN Intl exists");
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scSolComp.getScServiceDetail1().getId()).get();
					if (scServiceDetail != null) {
						logger.info("BYON Internet or IWAN or DIA or GVPN Intl for ActivationConfig and ServiceConfig update::{}", scServiceDetail.getId());
						scServiceDetail.setActivationConfigStatus(TaskStatusConstants.ACTIVE);
						scServiceDetail.setActivationConfigDate(new Timestamp(System.currentTimeMillis()));
						if (scServiceDetail.getServiceConfigDate() == null) {
							scServiceDetail.setServiceConfigStatus(TaskStatusConstants.ACTIVE);
							scServiceDetail.setServiceConfigDate(new Timestamp(System.currentTimeMillis()));
						}
						scServiceDetailRepository.save(scServiceDetail);
					}
				}else {
					logger.info("SdwanOverlayCommissionDateDelegate.Other than IWAN or DIA or GVPN Intl or BYON Internet with Id::{}",scSolComp.getScServiceDetail1().getId());
					try {
						generateBillStartDate(scSolComp.getScServiceDetail1(), null,
								scSolComp.getScServiceDetail1().getId());
					} catch (TclCommonException e) {
						logger.error("SdwanOverlayCommissionDateDelegate Exception {}", e);
					}
				}
			}
		}
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

	public void generateBillStartDate(ScServiceDetail scServiceDetail, String commissioningDate,Integer serviceId) throws TclCommonException {
		logger.info("generateBillStartDate invoked for Underlay Service id {} ", serviceId);

		Map<String, String> atMap = new HashMap<>();

		if (scServiceDetail == null) {
			scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		}

		Date commDate = new Date();
		try {
			if (commissioningDate != null)
				commDate = new SimpleDateFormat("yyyy-MM-dd").parse(commissioningDate);
		} catch (Exception ee) {
			logger.error("commissioningDateException {}", ee);
		}

		atMap.put("commissioningDate", DateUtil.convertDateToString(commDate));
		LocalDateTime commissioningDateLD = LocalDateTime.ofInstant(commDate.toInstant(), ZoneId.systemDefault());
		ScComponentAttribute billFreePeriodComponet = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "billFreePeriod", "LM", "A");
		LocalDateTime terminationDate = commissioningDateLD.minusDays(1);
		String terminationDateStr = "";
		if (billFreePeriodComponet != null) {
			int billFreePeriod = 0;
			try {
				if (StringUtils.isNotBlank(billFreePeriodComponet.getAttributeValue()))
					billFreePeriod = Integer.parseInt(billFreePeriodComponet.getAttributeValue());
			} catch (Exception ee) {
			}
			atMap.put("billStartDate",
					DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD.plusDays(billFreePeriod))));

			terminationDate = commissioningDateLD.plusDays(billFreePeriod).minusDays(1);
		} else {
			atMap.put("billStartDate", DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD)));
		}

		if ("MACD".equalsIgnoreCase(scServiceDetail.getOrderType())
				&& Objects.nonNull(scServiceDetail.getOrderSubCategory())
				&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")) {
			terminationDateStr = DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));
			ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_duration");
			ScServiceAttribute scServiceDownTimeIndAttr = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_needed_ind");
			logger.info("parallel days scServiceDownTimeAttr={},scServiceDownTimeIndAttr={}", scServiceDownTimeAttr,
					scServiceDownTimeIndAttr);

			if (Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeIndAttr)
					&& !scServiceDownTimeAttr.getAttributeValue().isEmpty()
					&& "yes".equalsIgnoreCase(scServiceDownTimeIndAttr.getAttributeValue())) {
				try {
					Integer parallelDays = Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
					logger.info("parallel days ={}", parallelDays);
					if (parallelDays > 0)
						terminationDate = terminationDate.plusDays((parallelDays + 1));
				} catch (Exception ee) {
					logger.error("terminationDate Exception {}", ee);
				}
				terminationDateStr = DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));
			} else {
				terminationDateStr = DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));
			}
			atMap.put("terminationDate", terminationDateStr);
			logger.info("serviceCode={}, terminationDateStr ={}", scServiceDetail.getUuid(), terminationDateStr);
		}
		updateServiceStatus(scServiceDetail, atMap.get("billStartDate"), atMap.get("commissioningDate"));
		List<String> siteTypes=scComponentRepository.getSiteTypeByScServiceDetailId(serviceId);
		for(String siteType:siteTypes) {
			logger.info("Update commissioning date for Service Id={} with siteType ={}", serviceId, siteType);
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,
					siteType);
		}
	}

	private void updateServiceStatus(ScServiceDetail scServiceDetail, String billstartDate, String commissionedDate) {
		try {

			if (scServiceDetail != null) {
				scServiceDetail.setActivationConfigStatus(TaskStatusConstants.ACTIVE);
				scServiceDetail.setActivationConfigDate(new Timestamp(System.currentTimeMillis()));

				if (scServiceDetail.getServiceConfigDate() == null) {
					scServiceDetail.setServiceConfigStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setServiceConfigDate(new Timestamp(System.currentTimeMillis()));
				}
				scServiceDetail
						.setBillStartDate(new Timestamp(DateUtil.convertStringToDateYYMMDD(billstartDate).getTime()));
				scServiceDetail.setCommissionedDate(
						new Timestamp(DateUtil.convertStringToDateYYMMDD(commissionedDate).getTime()));
				scServiceDetailRepository.save(scServiceDetail);
			}

		} catch (Exception e) {
			logger.error("error in updating service config status with  error:{}", e);
		}
	}
}
