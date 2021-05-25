package com.tcl.dias.l2oworkflowutils.service.v1;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;
import com.tcl.dias.l2oworkflow.entity.repository.StagePlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.abstractservice.IPriorityService;

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
	TaskRepository taskRepository;

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
		LOGGER.info("processServiceDelivery serviceId {} serviceDeliveryStagePlan {}", serviceId,
				serviceDeliveryStagePlan);
		return null;

	}

}
