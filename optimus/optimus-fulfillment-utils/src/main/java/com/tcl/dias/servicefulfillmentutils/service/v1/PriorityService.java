package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.abstractservice.IPriorityService;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 *            this class is used to get the priority
 *
 */

@Component
public class PriorityService implements IPriorityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriorityService.class);

	@Autowired
	StagePlanRepository stagePlanRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;


	@Autowired
	TaskRepository taskRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.odr.oe.status.queue}")
	String odrOrderEnrichStatusQueue;

	/**
	 * @author vivek
	 * @param scServiceDetail
	 * @param priority        getPriorityDeatils is used to get the priority details
	 */
	@Override
	public Float getPriorityDetails(StagePlan stagePlan) {
		LOGGER.info("Stage plan details{}", stagePlan);
		Float priority = calculatePriority(stagePlan.getPlannedEndTime(), stagePlan.getTargettedEndTime());

		LOGGER.info("priority{}", priority);

		LOGGER.info("Stage plan details{}", stagePlan);

		return priority;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public Timestamp processServiceDelivery(Integer serviceId) {
		
		StagePlan serviceDeliveryStagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId,
				"service_delivery_stage");
		StagePlan experienceSurveyStagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId,
				"experience_survey_stage");
		
		LOGGER.info("processServiceDelivery serviceId {} serviceDeliveryStagePlan {}", serviceId,
				serviceDeliveryStagePlan);
		
		LOGGER.info("processServiceDelivery serviceId {} experienceSurveyStagePlan {}", serviceId,
				experienceSurveyStagePlan);
		
		if (experienceSurveyStagePlan!=null && experienceSurveyStagePlan.getTargettedEndTime() != null) {
			serviceDeliveryStagePlan = experienceSurveyStagePlan;
			LOGGER.info(
					"processServiceDelivery serviceId {} experienceSurveyStagePlan updated instead of serviceDeliveryStagePlan {}",
					serviceId, experienceSurveyStagePlan);
		}
		
		Timestamp serviceDeliveryDate = null;
		
		if (serviceDeliveryStagePlan != null) {
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(serviceId);

			if (optionalServiceDetails.isPresent()) {
				ScServiceDetail scServiceDetail = optionalServiceDetails.get();
				Float priority = getPriorityDetails(serviceDeliveryStagePlan);

				LOGGER.info(
						"update CommittedDeliveryDate={} TargetedDeliveryDate={} serviceId={} scServiceDetail={} priority={}",
						serviceDeliveryStagePlan.getPlannedEndTime(), serviceDeliveryStagePlan.getTargettedEndTime(),
						serviceId, scServiceDetail, priority);
				// scServiceDetail.setTargetedDeliveryDate(serviceDeliveryStagePlan.getTargettedEndTime());
				// if ((scServiceDetail.getTargetedDeliveryDate() == null) ||
				// (scServiceDetail.getTargetedDeliveryDate().after(serviceDeliveryStagePlan.getTargettedEndTime())))
				// {
				if (serviceDeliveryStagePlan.getTargettedEndTime() != null && serviceDeliveryStagePlan
						.getTargettedEndTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
					scServiceDetail.setTargetedDeliveryDate(serviceDeliveryStagePlan.getTargettedEndTime());
				} else {
					if (serviceDeliveryStagePlan.getTargettedEndTime() != null) {
						scServiceDetail.setTargetedDeliveryDate(new Timestamp(new Date().getTime()));
					}

				}
				// }

				scServiceDetail.setPriority(priority);
				scServiceDetail.setActualDeliveryDate(serviceDeliveryStagePlan.getActualEndTime());
				scServiceDetail.setEstimatedDeliveryDate(serviceDeliveryStagePlan.getEstimatedEndTime());
				serviceDeliveryDate = serviceDeliveryStagePlan.getPlannedEndTime();
				scServiceDetail.setCommittedDeliveryDate(serviceDeliveryStagePlan.getPlannedEndTime());
				scServiceDetailRepository.save(scServiceDetail);
				LOGGER.info("processServiceDelivery completed");
				LOGGER.info("Completed the process So updating the common flow status for erfOdrServiceId {}",
						scServiceDetail.getErfOdrServiceId());
				String productName = scServiceDetail.getErfPrdCatalogProductName();
				String username = scServiceDetail.getCreatedBy();
				try {
					if (StringUtils.isBlank(scServiceDetail.getLocalItContactEmail())
							&& (scServiceDetail.getIsOeCompleted() != null
									&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.N))) {
						Map<String, Object> requestparam = new HashMap<>();
						requestparam.put("siteId", scServiceDetail.getErfOdrServiceId());
						requestparam.put("productName", productName);
						requestparam.put("userName", username);
						mqUtils.send(odrOrderEnrichStatusQueue, Utils.convertObjectToJson(requestparam));
					}
				} catch (Exception e) {
					LOGGER.error("Error in initiating {}", e);
				}
			}
		}
		return serviceDeliveryDate;

	}

}
