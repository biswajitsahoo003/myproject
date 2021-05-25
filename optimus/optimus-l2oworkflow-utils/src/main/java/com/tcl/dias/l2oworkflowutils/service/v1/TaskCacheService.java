package com.tcl.dias.l2oworkflowutils.service.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.entity.entities.MstStatus;
import com.tcl.dias.l2oworkflow.entity.repository.MstStatusRepository;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited this class is used for the
 *            cacheimplementation of for task service
 */

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class TaskCacheService {

	@Autowired
	MstStatusRepository mstStatusRepository;

	private Map<String, MstStatus> STATUSMAPPER = new HashMap<>();

	/**
	 * @author vivek
	 * @param status
	 * @return this is used to get status from cache
	 */
	public MstStatus getMstStatus(String status) {

		if (STATUSMAPPER.get(status) != null) {
			return STATUSMAPPER.get(status);
		} else {
			MstStatus mstStatus = mstStatusRepository.findByCode(status);
			STATUSMAPPER.put(status, mstStatus);
			return mstStatus;
		}
	}
}
