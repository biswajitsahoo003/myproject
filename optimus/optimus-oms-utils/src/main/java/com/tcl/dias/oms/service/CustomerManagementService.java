package com.tcl.dias.oms.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MDMOmsRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * CustomerManagementService.java for 
 * Save the customer Update billing details in Third party Service jobs
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class CustomerManagementService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagementService.class);
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	
	@Value("${cmd.billing.update.queuename}")
	String queueName;

	

	

	/**
	 *  Save the customer Update billing details in Third party Service jobs
	 *  before calling MDM Third Party Api
	 * @author Muthuselvi S
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public void cmdBillingDetailsUpdate(MDMOmsRequestBean mDMRequestBean)
			throws TclCommonException {
		 
		try {			
			String Payload=Utils.convertObjectToJson(mDMRequestBean);
			ThirdPartyServiceJob thirdPartyServiceJob=new ThirdPartyServiceJob();
			thirdPartyServiceJob.setCreatedBy(Utils.getSource());
			thirdPartyServiceJob.setCreatedTime(new Date());
			thirdPartyServiceJob.setQueueName(queueName);
			thirdPartyServiceJob.setRefId(mDMRequestBean.getQuoteCode());
			thirdPartyServiceJob.setRequestPayload(Payload);
			thirdPartyServiceJob.setSeqNum(0);
			thirdPartyServiceJob.setServiceStatus("NEW");
			thirdPartyServiceJob.setServiceType("CMD_UPDATE_BILLING");
			thirdPartyServiceJob.setThirdPartySource("CMD");
			thirdPartyServiceJob.setIsComplete((byte) 0);
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob);
			
		} catch (Exception e) {

			throw new TclCommonException(e.getMessage(), e, ResponseResource.R_CODE_ERROR);
		}

				
		
	}

}



