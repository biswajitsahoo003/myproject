package com.tcl.dias.l2oworkflowutils.service.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.dias.l2oworkflowutils.factory.ProjectPlanInitiateService;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications used for status service
 */

@Component
public class ProjectTimelineStatusTrackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTimelineStatusTrackService.class);

	
	@Autowired
	ProjectPlanInitiateService projectPlanInitiateService;
	
	@Autowired
	SiteDetailRepository siteDetailRepository;

	
	/**
	 * @author vivek
	 * used for daily task completion status
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public void processDailyTrackingCalculationForL20() {

		LOGGER.info("processTemplateCalculation started ");
		List<SiteDetail> siteDetails = siteDetailRepository
				.findByStatus(TaskStatusConstants.INPROGRESS);

		if (siteDetails != null && !siteDetails.isEmpty()) {
			siteDetails.forEach(site -> {
				
				try {
				
				projectPlanInitiateService.initiateDailyTrackingForL20(site.getId());
				}
				catch (Exception e) {
					LOGGER.error("error in daily tracking for service id{} and exception{}", site.getId(), e);
				}
			});

		}

	}

}
