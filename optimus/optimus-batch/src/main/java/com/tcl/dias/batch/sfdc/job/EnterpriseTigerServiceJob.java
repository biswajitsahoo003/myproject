package com.tcl.dias.batch.sfdc.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.batch.dao.ThirdPartyServiceJobDao;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.gsc.beans.EnterpriseTigerNotificationBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.GscServiceLogRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * This file contains the polling job to pick Enterprise Tiger service API call records and process accordingly
 *
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class EnterpriseTigerServiceJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseTigerServiceJob.class);

	private MQUtils mqUtils;

	private ThirdPartyServiceJobDao thirdPartyServiceJobDao;

	private ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	private Integer maxRetryCount;

	private GscServiceLogRepository gscServiceLogRepository;
	
	public void setMqUtils(MQUtils mqUtils) {
		this.mqUtils = mqUtils;
	}

	public void setThirdPartyServiceJobDao(ThirdPartyServiceJobDao thirdPartyServiceJobDao) {
		this.thirdPartyServiceJobDao = thirdPartyServiceJobDao;
	}

	public void setThirdPartyServiceJobsRepository(ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository) {
		this.thirdPartyServiceJobsRepository = thirdPartyServiceJobsRepository;
	}
	
	public void setGscServiceLogRepository(GscServiceLogRepository gscServiceLogRepository) {
		this.gscServiceLogRepository =gscServiceLogRepository;
	}

	public void setMaxRetryCount(Integer maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	private static List<String> TIGER_JOB_STATUSES = ImmutableList.of(SfdcServiceStatus.INPROGRESS.toString(),
			SfdcServiceStatus.FAILURE.toString());

	private boolean isJobStruck(ThirdPartyServiceJob job, Map<String, ThirdPartyServiceJob> inProgressTigerJobs) {
		ThirdPartyServiceJob inProgressJob  = inProgressTigerJobs.get(jobUid(job));
		return Objects.nonNull(inProgressJob) &&
				SfdcServiceStatus.FAILURE.toString().equalsIgnoreCase(inProgressJob.getServiceStatus())
				&& Optional.ofNullable(job.getRetryCount()).orElse(0) >= maxRetryCount;
	}

	private static boolean isJobInProgress(ThirdPartyServiceJob job, Map<String, ThirdPartyServiceJob> inProgressTigerJobs) {
		ThirdPartyServiceJob inProgressJob  = inProgressTigerJobs.get(jobUid(job));
		return Objects.nonNull(inProgressJob)
				&& SfdcServiceStatus.INPROGRESS.toString().equalsIgnoreCase(job.getServiceStatus());
	}

	private static String jobUid(ThirdPartyServiceJob job) {
		return String.format("%s-%s-%s", job.getRefId(), job.getThirdPartySource(), job.getServiceType());
	}

	private static String toJson(Object object) {
		try {
			return Utils.convertObjectToJson(object);
		} catch (TclCommonException e) {
			Throwables.propagate(e);
			return null;
		}
	}

	private List<ThirdPartyServiceJob> findStruckJobs(Collection<ThirdPartyServiceJob> inProgressJobs) {
		return inProgressJobs.stream()
				.filter(job -> SfdcServiceStatus.FAILURE.toString().equalsIgnoreCase(job.getServiceStatus())
						&& Optional.ofNullable(job.getRetryCount()).orElse(0) >= maxRetryCount)
				.collect(Collectors.toList());
	}

	private List<ThirdPartyServiceJob> findRetryableJobs(Collection<ThirdPartyServiceJob> inProgressJobs) {
		return inProgressJobs.stream()
				.filter(job -> SfdcServiceStatus.FAILURE.toString().equalsIgnoreCase(job.getServiceStatus())
						&& Optional.ofNullable(job.getRetryCount()).orElse(0) < maxRetryCount)
				.collect(Collectors.toList());
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		try {
			List<ThirdPartyServiceJob> enterpriseTigerServiceJobs = thirdPartyServiceJobsRepository.findEnterpriseTigerByServiceStatusBySeq(
					SfdcServiceStatus.NEW.toString());
			LOGGER.info(String.format("%s new enterprise service jobs found", enterpriseTigerServiceJobs.size()));
			List<ThirdPartyServiceJob> tigerServiceJobs = new ArrayList<>();
			if(!CollectionUtils.isEmpty(enterpriseTigerServiceJobs)) {
				tigerServiceJobs.addAll(enterpriseTigerServiceJobs.stream().map(thirdPartyServiceJob -> {
					List<ThirdPartyServiceJob> filterTigerServiceJobs = new ArrayList<>();
					if(0 == thirdPartyServiceJobsRepository.findSfdcAllSuccessJobs(thirdPartyServiceJob.getRefId()).size()) {
						filterTigerServiceJobs = thirdPartyServiceJobsRepository.findNewEnterpriseTigerJobs(thirdPartyServiceJob.getRefId());
						LOGGER.info(String.format("%s filtered new enterprise tiger service jobs found inside", filterTigerServiceJobs.size()));
					}
					LOGGER.info(String.format("%s filtered new enterprise tiger service jobs found outside", filterTigerServiceJobs.size()));
					return filterTigerServiceJobs;
				}).collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList()));
				LOGGER.info(String.format("%s new tiger service jobs found", tigerServiceJobs.size()));
			}
			Map<String, ThirdPartyServiceJob> inProgressTigerJobs = thirdPartyServiceJobsRepository.findByServiceStatusInAndThirdPartySource(
					TIGER_JOB_STATUSES, ThirdPartySource.ENTERPRISE_TIGER_ORDER.toString())
					.stream()
					.collect(Collectors.toMap(EnterpriseTigerServiceJob::jobUid, Function.identity(), (first, second) -> second));
			if(!CollectionUtils.isEmpty(inProgressTigerJobs)) {
				LOGGER.info(String.format("%s in-progress tiger service jobs found", inProgressTigerJobs.size()));
			}

			List<ThirdPartyServiceJob> struckJobs = findStruckJobs(inProgressTigerJobs.values());
			struckJobs.forEach(struckJob ->  {
				LOGGER.info(String.format("Marking tiger service job %s with id %s as %s", jobUid(struckJob), struckJob.getId(),
						SfdcServiceStatus.STRUCK.toString()));
				thirdPartyServiceJobDao.updateStruckStatusByServiceType(struckJob.getRefId(),
						ThirdPartySource.ENTERPRISE_TIGER_ORDER.toString(),
						struckJob.getServiceType());
			});

			List<ThirdPartyServiceJob> retryAbleJobs = findRetryableJobs(inProgressTigerJobs.values());
			tigerServiceJobs.addAll(retryAbleJobs);
			tigerServiceJobs.forEach(job -> {
				String jobUID = jobUid(job);
				if (isJobInProgress(job, inProgressTigerJobs)) {
					LOGGER.info(String.format("Tiger service job %s with id %s is in progress", jobUID, job.getId()));
				} else if (isJobStruck(job, inProgressTigerJobs)) {
					if (Objects.nonNull(job.getRefId())) {
						List<Map<String, Object>> gscServiceLogList = gscServiceLogRepository
								.findServiceDetails(job.getRefId());
						EnterpriseTigerNotificationBean bean = new EnterpriseTigerNotificationBean();
						if (!gscServiceLogList.isEmpty()) {
							Map<String, Object> logMap = gscServiceLogList.stream().findFirst().get();
							bean.setId((Integer) logMap.get("logId"));
							bean.setRequestUrl((String) logMap.get("requestUrl"));
							bean.setRequestBody((String) logMap.get("requestBody"));
							bean.setRequestResponse((String) logMap.get("responseBody"));
							bean.setReferenceId((Integer) logMap.get("referenceId"));
							bean.setStatus((String) logMap.get("status"));
						}
						try {
							mqUtils.send("rabbitmq_tiger_error_queue", toJson(bean));
						} catch (TclCommonException e) {
							LOGGER.warn("error in processing to queue call for tiger service job{}", e);
						}

					}
					LOGGER.info(String.format("Marking Tiger service job %s with id %s as %s", jobUID, job.getId(),
							SfdcServiceStatus.STRUCK.toString()));
					thirdPartyServiceJobDao.updateStruckStatusByServiceType(job.getRefId(),
							ThirdPartySource.ENTERPRISE_TIGER_ORDER.toString(), job.getServiceType());
				} else {
					LOGGER.info(String.format("Starting tiger service job %s with id %s retry count %s", jobUID, job.getId(),
							job.getRetryCount()));
					try {
						mqUtils.send(job.getQueueName(), toJson(job));
					} catch (Exception e) {
						LOGGER.warn("error in processing to queue call for tiger service job{}", e);
					}

					job.setServiceStatus(SfdcServiceStatus.INPROGRESS.toString());
					job.setUpdatedBy("system");
					job.setUpdatedTime(new Date());
					thirdPartyServiceJobsRepository.save(job);
					LOGGER.info(String.format("Started tiger service job %s with id %s successfully", jobUID, job.getId()));
				}
			});
		} catch (Exception e) {
			LOGGER.warn(String.format("Error occurred while executing Tiger Service Job: %s",
					jobExecutionContext.getJobDetail().getKey()), e);
		}
	}
}
