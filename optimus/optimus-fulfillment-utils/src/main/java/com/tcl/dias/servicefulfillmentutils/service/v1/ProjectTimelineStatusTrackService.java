package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanInitiateService;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications used for status service
 */

@Component
public class ProjectTimelineStatusTrackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTimelineStatusTrackService.class);

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ProjectPlanInitiateService projectPlanInitiateService;
	

	
	/**
	 * @author vivek
	 * used for daily task completion status
	 */
	public void processTemplateCalculation() {

		LOGGER.info("processTemplateCalculation started at date:{} ", new Date());
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository
				.findByMstStatus_codeOrderByIdDesc(TaskStatusConstants.INPROGRESS);

		if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
			scServiceDetails.forEach(scDetails -> {

				try {

					LOGGER.info("processTemplateCalculation started for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

					projectPlanInitiateService.initiateDailyTracking(scDetails.getId(),null);

					LOGGER.info("processTemplateCalculation completed for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

				} catch (Exception e) {
					LOGGER.error(
							"processTemplateCalculation error in daily tracking for service id{} and servicecode:{} and exception{}",
							scDetails.getId(), scDetails.getUuid(), e);
				}
			});

		}

		LOGGER.info("processTemplateCalculation completed at date:{} ", new Date());

	}
	
	public void processTargetedDayLessThanCurrentTemplateCalculation() {

		LOGGER.info("processTargetedDayLessThanCurrentTemplateCalculation started at date:{} ", new Date());
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findTargetedDateLessThanCurrentDate();

		if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
			scServiceDetails.forEach(scDetails -> {

				try {

					LOGGER.info(
							"processTargetedDayLessThanCurrentTemplateCalculation started for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

					projectPlanInitiateService.initiateDailyTracking(scDetails.getId(), null);

					LOGGER.info(
							"processTargetedDayLessThanCurrentTemplateCalculation completed for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

				} catch (Exception e) {
					LOGGER.error(
							"processTargetedDayLessThanCurrentTemplateCalculation error in daily tracking for service id{} and servicecode:{} and exception{}",
							scDetails.getId(), scDetails.getUuid(), e);
				}
			});

		}

		LOGGER.info("processTargetedDayLessThanCurrentTemplateCalculation completed at date:{} ", new Date());

	}

	public void processTargetedDayLessThanCurrentTemplateCalculation(List<Integer> serviceIds) {


		LOGGER.info("processTargetedDayLessThanCurrentTemplateCalculation started at date for list :{} ", new Date());
		List<ScServiceDetail> scServiceDetails = (List<ScServiceDetail>) scServiceDetailRepository.findAllByIdIn(serviceIds);

		if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
			scServiceDetails.forEach(scDetails -> {

				try {

					LOGGER.info(
							"processTargetedDayLessThanCurrentTemplateCalculation started for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

					projectPlanInitiateService.initiateDailyTracking(scDetails.getId(), null);

					LOGGER.info(
							"processTargetedDayLessThanCurrentTemplateCalculation completed for serviceid:{} and servicecode:{}",
							scDetails.getId(), scDetails.getUuid());

				} catch (Exception e) {
					LOGGER.error(
							"processTargetedDayLessThanCurrentTemplateCalculation error in daily tracking for service id{} and servicecode:{} and exception{}",
							scDetails.getId(), scDetails.getUuid(), e);
				}
			});

		}

		LOGGER.info("processTargetedDayLessThanCurrentTemplateCalculation completed at date:{} ", new Date());

	
		
	}



}
