package com.tcl.dias.l2oworkflow.servicefulfillment.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.tcl.dias.l2oworkflowutils.constants.TaskLogConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteDetailServiceFulfilmentUpdateBean;
import com.tcl.dias.common.beans.TaskBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskPlanItem;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskPlanItemRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskCacheService;

/**
 * This file contains the ServiceFulfillmentService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceFulfillmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentService.class);

	@Autowired
	MQUtils mqUtils;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	SiteDetailRepository siteDetailRepository;

	@Autowired
	TaskCacheService taskCacheService;
	
	@Autowired
	MfDetailRepository mfDetailRepository;
	
	@Autowired
	CmmnRuntimeService cmmnRuntimeService;

	@Autowired
	MfTaskPlanItemRepository taskPlanItemRepository;


	

	/**
	 * 
	 * This method is used to Update site details
	 * 
	 * @param siteDetailServiceFulfilmentUpdateBean
	 * @return
	 */
	public String updateSiteDetails(SiteDetailServiceFulfilmentUpdateBean siteDetailServiceFulfilmentUpdateBean) {
		if (siteDetailServiceFulfilmentUpdateBean != null
				&& siteDetailServiceFulfilmentUpdateBean.getTaskId() != null) {
			Optional<Task> taskOpt = taskRepository.findById(siteDetailServiceFulfilmentUpdateBean.getTaskId());
			if (taskOpt.isPresent()) {
				SiteDetail siteDetail = taskOpt.get().getSiteDetail();
				siteDetail.setContractTerm(siteDetailServiceFulfilmentUpdateBean.getTermsInMonths());
				siteDetailRepository.saveAndFlush(siteDetail);
				return ResponseResource.RES_SUCCESS;
			}
		}
		return null;
	}
	
	/**
	 * to save manual feasibility details 
	 * @param mfDetailBean
	 * @return
	 */
	public MfDetail persistMfDetail(MfDetailsBean mfDetailBean) {
		LOGGER.info("MF Detail bean for quote code ----> {} and id---> {} is ---? {} ",
				Optional.ofNullable(mfDetailBean.getQuoteCode()), Optional.ofNullable(mfDetailBean.getQuoteId()),
				Optional.ofNullable(mfDetailBean));

		MfDetail mfDetail = null;
		if (mfDetailBean != null) {
			LOGGER.info(
					"###------------Inside SERVICEFULLFILMENTSERVICE #persistMfDetail method # mfDetail not null-----------###");
			LOGGER.info("#####Quote code {} , quoteID {} available from oms is ", mfDetailBean.getQuoteCode(),
					mfDetailBean.getQuoteId());

			List<MfDetail> mfDetailList = mfDetailRepository.findByQuoteIdAndSiteIdAndIsActiveAndSiteType(
					mfDetailBean.getQuoteId(), mfDetailBean.getSiteId(), CommonConstants.INACTIVE,
					mfDetailBean.getSiteType());
			if (CollectionUtils.isEmpty(mfDetailList)) {
				mfDetail = new MfDetail();
				LOGGER.info("MF Detail bean before going to save#No existin Mf_detail entry case");
				constructMFDetail(mfDetailBean, mfDetail);
				mfDetail = mfDetailRepository.saveAndFlush(mfDetail);
			}

			if (CollectionUtils.isNotEmpty(mfDetailList)) {
				LOGGER.info("MF Detail bean before going to save#one or more than one mf_detail entry case");
				mfDetailList.forEach(x -> {
					constructMFDetail(mfDetailBean, x);
					mfDetailRepository.saveAndFlush(x);
				});
				mfDetail = mfDetailList.get(0);
			}
		}
		return mfDetail;
	}

	private void constructMFDetail(MfDetailsBean mfDetailBean, MfDetail mfDetail) {
		LOGGER.warn("ServiceFulfillmentService#Inside construct MFDetail Method call");
		try {
		mfDetail.setCreatedTime(new Timestamp(new Date().getTime()));
		mfDetail.setUpdatedTime(new Timestamp(new Date().getTime()));
		mfDetail.setIsActive(CommonConstants.ACTIVE);
		mfDetail.setQuoteCreatedUserType(mfDetailBean.getQuoteCreatedUserType());
		mfDetail.setSiteId(mfDetailBean.getSiteId());
		mfDetail.setSiteCode(mfDetailBean.getSiteCode());
		mfDetail.setQuoteCode(mfDetailBean.getQuoteCode());
		mfDetail.setQuoteId(mfDetailBean.getQuoteId());
		mfDetail.setMfDetails(Utils.convertObjectToJson(mfDetailBean.getMfDetails()));
		mfDetail.setStatus(MasterDefConstants.IN_PROGRESS);
		mfDetail.setRegion(mfDetailBean.getRegion());
		mfDetail.setAssignedTo(mfDetailBean.getAssignedTo());
		mfDetail.setCreatedBy(mfDetailBean.getCreatedBy());
		mfDetail.setUpdatedBy(mfDetailBean.getUpdatedBy());
		mfDetail.setSiteType(mfDetailBean.getSiteType());
		// For NPL
		mfDetail.setLinkId(mfDetailBean.getLinkId());
		mfDetail.setSystemLinkResponseJson(mfDetailBean.getSystemLinkResponse());
		if(mfDetailBean.getIs3DMaps()) {
		 mfDetail.setIsPreMfTask("1");
		}
		else {
			 mfDetail.setIsPreMfTask("0");
		}
		} catch (Exception e) {
			LOGGER.warn("persistMfDetail: Error in constructing MfDetailsBean {}", e.getMessage());
		}
	}
	
	
	/**
	 * to persist plan item details for a case
	 * @param caseInstance
	 */
	public void persistPlanItemsForCase(CaseInstance caseInstance) {

		String caseInstanceId = caseInstance.getId();
		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstanceId).list();

		if (!planItemInstances.isEmpty()) {
			planItemInstances.stream().forEach(planItem -> savePlanItems(planItem, caseInstanceId));
		}
	}

	private void savePlanItems(PlanItemInstance planItem, String caseInstanceId) {
		if (planItem.getPlanItemDefinitionType().equalsIgnoreCase("humantask")) {
			/*MfTaskPlanItem existingPlanItem = taskPlanItemRepository.findByCaseInstIdAndPlanItemDefId(caseInstanceId, planItem.getPlanItemDefinitionId());
			if(existingPlanItem == null || existingPlanItem.getStatus())
			*/	
			MfTaskPlanItem taskPlanItem = new MfTaskPlanItem();
			taskPlanItem.setCaseInstId(caseInstanceId);
			taskPlanItem.setPlanItemDefId(planItem.getPlanItemDefinitionId());
			taskPlanItem.setPlanItemInstId(planItem.getId());
			taskPlanItem.setStatus(planItem.getState());
			taskPlanItemRepository.save(taskPlanItem);
		}
	}

	public void closeReturnedTask(String siteId)
	{
		if (StringUtils.isNotBlank(siteId))
		{
			List<Task> tasks = taskRepository.findBySiteId(Integer.parseInt(siteId));
			if (Objects.nonNull(tasks))
			{
				tasks.stream().forEach(task -> {
					if (task.getMstStatus().getCode().equalsIgnoreCase(TaskLogConstants.RETURNED))
					{
						task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
						taskRepository.save(task);
					}
				});
			}
		}
		else
		{
			LOGGER.info("SiteId value null");
		}
	}
	
	/**
	 *
	 * This method is used to get all tasks by siteId
	 *
	 * @param siteId
	 */
	public List<TaskBean> getAllTasksBySiteId(String siteId){
		List<TaskBean> taskBean = new ArrayList<TaskBean>();
		List<Task> taskList = new ArrayList<Task>();
		if (StringUtils.isNotBlank(siteId)){
			taskList = taskRepository.findBySiteId(Integer.parseInt(siteId));
			if (Objects.nonNull(taskList)){
				taskList.stream().forEach(item ->{
					LOGGER.info("task id {}",item.getId());
					TaskBean bean = new TaskBean();
					bean.setId(item.getId());
					bean.setMstTaskDef(item.getMstTaskDef().getKey());
					bean.setStatus(item.getMstStatus().getCode());
					bean.setSiteId(item.getSiteId());
					bean.setSiteCode(item.getSiteCode());
					bean.setQuoteId(item.getQuoteId());
					bean.setQuoteCode(item.getQuoteCode());	
					taskBean.add(bean);
				});
			}
		}else{
			LOGGER.info("No task available for this siteId {}",siteId);
		}
		return taskBean;
	}
}
