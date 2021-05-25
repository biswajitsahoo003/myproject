package com.tcl.dias.batch.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;

/**
 * This file contains the ThirdPartyServiceJobDao.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class ThirdPartyServiceJobDao {

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	/**
	 * 
	 * updateStruckStatus
	 * 
	 * @param refId
	 */
	@Transactional
	public void updateStruckStatus(String refId,String source) {
		thirdPartyServiceJobsRepository.updateServiceStatusByRefIdAndThirdPartySource(refId, SfdcServiceStatus.STRUCK.toString(),source);
	}

	/**
	 * Update struck status by ref id and source and service type combination
	 * @param refId
	 * @param source
	 * @param serviceType
	 */
	@Transactional
	public void updateStruckStatusByServiceType(String refId, String source, String serviceType) {
		thirdPartyServiceJobsRepository.updateServiceStatusByRefIdAndThirdPartySourceAndServiceType(refId,
				SfdcServiceStatus.STRUCK.toString(),source, serviceType);
	}

}
