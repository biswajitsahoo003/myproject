package com.tcl.dias.servicefulfillmentutils.delegates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Component("checkBlockTerminateTriggerDelegate")
public class CheckBlockTerminateTriggerDelegate implements JavaDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckBlockTerminateTriggerDelegate.class);
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
    WorkFlowService workFlowService;
	 
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> processMap = execution.getVariables();
			Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
			Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceId);
			if(scServiceDetailOptional.isPresent()) {
				ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
				Date  terminationEffectiveDate = DateUtil.convertStringToDateYYMMDD(scServiceDetail.getTerminationEffectiveDate()); //06-May-2021 
				Date  currentDate=DateUtil.convertStringToDateYYMMDD(DateUtil.convertDateToString(new Date())); //5-May-2021
				LOGGER.info("Service Id::{} with Termination Inprogress with terminationEffectiveDate::{} and  currentDate::{}", scServiceDetail.getId(),terminationEffectiveDate,currentDate);
				if(terminationEffectiveDate.compareTo(currentDate)>0) {
					execution.setVariable("terminationBlockEffectiveDate", getEffectiveDateForTermination(scServiceDetail.getTerminationEffectiveDate()));
					execution.setVariable("terminationBlockWait", "Yes");
				}else {
					execution.setVariable("terminationBlockWait", "No");
				}
				workFlowService.processServiceTask(execution);
				workFlowService.processServiceTaskCompletion(execution, "");
			}
		}catch (Exception e) {
			LOGGER.error("CheckBlockTerminateTriggerDelegate Exception {} ", e);
        }
		
	}
	
	protected String getEffectiveDateForTermination(String terminationEffectiveDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			String fromTime = "11:59";
			LOGGER.info("effective date for termination {} {} :",terminationEffectiveDate, fromTime);
			terminationEffectiveDate = terminationEffectiveDate.split(" ")[0];
			terminationEffectiveDate=terminationEffectiveDate+" "+fromTime;
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived effective date for termination::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
}
