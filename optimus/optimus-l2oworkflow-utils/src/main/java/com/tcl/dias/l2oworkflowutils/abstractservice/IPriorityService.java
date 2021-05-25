package com.tcl.dias.l2oworkflowutils.abstractservice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the 
 */
public interface IPriorityService {

	/**
	 * @param stageDefkey
	 * @param serviceId
	 * @return
	 * used to get the priority details
	 */
	public Float getPriorityDetails(StagePlan stagePlan);
	
	/**
	 * @param plannedEntDate
	 * @param targetedEndDate
	 * @return
	 * used to calculate the priority
	 */
	default Float calculatePriority(Timestamp plannedEntDate, Timestamp targetedEndDate) {
		Float targetedDelay=1.0F;
		targetedDelay	=(float) (targetedEndDate.getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime());
		if(targetedDelay==0.0F) {
			targetedDelay=1.0F;
		}
		
		Float priority = (float) ((plannedEntDate.getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime())
				/ (targetedDelay));

		return priority;

	}

}
