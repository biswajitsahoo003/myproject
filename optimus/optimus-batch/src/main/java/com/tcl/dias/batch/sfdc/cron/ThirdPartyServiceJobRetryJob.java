package com.tcl.dias.batch.sfdc.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.batch.odr.notification.NotificationService;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;

/**
 * This file contains the ThirdPartyServiceJobRetryJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ThirdPartyServiceJobRetryJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyServiceJobRetryJob.class);

	/**
	 * execute
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
					.getBean(ThirdPartyServiceJobsRepository.class);

			NotificationService notificationService = appCtx.getBean(NotificationService.class);

			List<String> serviceStatuses = new ArrayList<String>();
			serviceStatuses.add("FAILURE");
			// serviceStatuses.add("STRUCK");
			List<ThirdPartyServiceJob> thirdPartyServices = thirdServiceJobsRepository
					.findByServiceStatusAndRetryCountAndIsActive(serviceStatuses, 11);
			Map<String, Map<Integer, ThirdPartyServiceJob>> thirdPartyJobsMapper = new HashMap<String, Map<Integer, ThirdPartyServiceJob>>();
			LOGGER.info("Total Number of Failures {}", thirdPartyServices.size());
			for (ThirdPartyServiceJob thirdPartyServiceJob : thirdPartyServices) {
				if (thirdPartyServiceJob.getServiceStatus().equals("FAILURE")) {
					thirdPartyServiceJob.setRetryCount(
							thirdPartyServiceJob.getRetryCount() != null ? thirdPartyServiceJob.getRetryCount() + 1
									: 1);
				}
				thirdPartyServiceJob.setServiceStatus("NEW");
				thirdPartyServiceJob.setUpdatedTime(new Date());
				List<ThirdPartyServiceJob> struckJobs = thirdServiceJobsRepository
						.findByRefIdAndServiceStatusAndThirdPartySource(thirdPartyServiceJob.getRefId(), "STRUCK",
								"SFDC");
				if (thirdPartyJobsMapper.get(thirdPartyServiceJob.getRefId()) == null) {
					Map<Integer, ThirdPartyServiceJob> thirdPartyJobs = new HashMap<>();
					thirdPartyJobs.put(thirdPartyServiceJob.getId(), thirdPartyServiceJob);
					thirdPartyJobsMapper.put(thirdPartyServiceJob.getRefId(), thirdPartyJobs);
				} else {
					Map<Integer, ThirdPartyServiceJob> thirdPartyJobs = thirdPartyJobsMapper
							.get(thirdPartyServiceJob.getRefId());
					thirdPartyJobs.put(thirdPartyServiceJob.getId(), thirdPartyServiceJob);
				}
				for (ThirdPartyServiceJob thirdPartyServiceJob2 : struckJobs) {
					thirdPartyServiceJob2.setServiceStatus("NEW");
					thirdPartyServiceJob2.setUpdatedTime(new Date());
					thirdPartyJobsMapper.get(thirdPartyServiceJob2.getRefId()).put(thirdPartyServiceJob2.getId(),
							thirdPartyServiceJob2);
				}
			}
			for (Entry<String, Map<Integer, ThirdPartyServiceJob>> tpsList : thirdPartyJobsMapper.entrySet()) {
				if (!tpsList.getValue().isEmpty())
					thirdServiceJobsRepository.saveAll(tpsList.getValue().values());
				LOGGER.info("Updated Status for {}", tpsList.getKey());
			}
			LOGGER.info("All the ThirdPartyJobs are updated as New {}", thirdPartyServices.size());

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -45);
			Date dateBf = cal.getTime();
			List<ThirdPartyServiceJob> inprogressTps = thirdServiceJobsRepository
					.findByServiceStatusAndUpdatedTime("INPROGRESS", dateBf);
			LOGGER.info("Total Number of cases struck in INPROGRESS {}", inprogressTps.size());
			Map<Integer, Map<String, String>> inprogressNotifierMapper = new HashMap<>();
			for (ThirdPartyServiceJob thirdPartyServiceJob : inprogressTps) {
				Map<String, String> elementMapper = new HashMap<>();
				elementMapper.put("serviceType", thirdPartyServiceJob.getServiceType());
				elementMapper.put("refId", thirdPartyServiceJob.getRefId());
				inprogressNotifierMapper.put(thirdPartyServiceJob.getId(), elementMapper);
			}
			String errorMessage = "";
			for (Entry<Integer, Map<String, String>> inprogressNm : inprogressNotifierMapper.entrySet()) {
				Map<String, String> val = inprogressNm.getValue();
				errorMessage = errorMessage + "Id : " + inprogressNm.getKey() + " --> RefId : " + val.get("refId")
						+ "  -- > ServiceType : " + val.get("serviceType") + "###############";
			}
			if (StringUtils.isNotBlank(errorMessage)) {
				notificationService.notifySfdcError("Struck in INPROGRESS state Status", "" + errorMessage, "NA",
						"NA");
			}
		} catch (Exception e) {
			LOGGER.error("Error in executing component " + context.getJobDetail().getKey(), e);
		}
		LOGGER.trace("SfdcRetriggerJob.execute method end.");

	}
}
