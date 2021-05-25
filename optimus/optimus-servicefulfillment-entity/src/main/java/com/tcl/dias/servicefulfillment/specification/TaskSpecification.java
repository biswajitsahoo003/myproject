package com.tcl.dias.servicefulfillment.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TaskSpecification {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskSpecification.class);
	
	private TaskSpecification() {

	}

	/**
	 * filter task based on group
	 *
	 * @author vivek
	 * @param serviceType, List<String> taskNames
	 * @param groupName
	 * @param orderType
	 * @param serviceCode
	 * @param city 
	 * @param state 
	 * @param string4 
	 * @param string3 
	 * @param string2 
	 * @param string 
	 * @return
	 */

	public static final Specification<Task> getTaskFilter(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType,List<String> orderCategory,List<String> orderSubCategory,List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city, List<String> country,
														  String customerName, List<String> state,List<String> lastMileScenario, List<String> lmProvider, List<String> pmName,Boolean isJeopardyTask,List<String> distributionCenterName,String deviceType,String devicePlatform,String isIpDownTimeRequired,String isTxDowntimeReqd,boolean negotiationTaskRequired,List<String> csmName) {
			
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<TaskAssignment, Task> taskAssignmentJoin=null;

				final List<Predicate> predicates = new ArrayList<>();
				
				if (status == null || status.isEmpty() || status.contains("ALL")) {
					predicates.add(mstStatusJoin.get("code").in(java.util.Arrays.asList("INPROGRESS","OPEN","ACTIVE","REOPEN","CLOSED","HOLD")));
				}else if (status != null && !status.isEmpty() && !status.contains("ALL")) {
					predicates.add(mstStatusJoin.get("code").in(status));
				}else{
					predicates.add(mstStatusJoin.get("code").in(java.util.Arrays.asList("INPROGRESS","OPEN","ACTIVE","REOPEN","HOLD")));
				}
				
				if (groupName != null || userName != null || (csmName!=null && !csmName.isEmpty())) {

					taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				}


				if (groupName != null && !groupName.isEmpty()) {
					predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), groupName));
				}

				if (userName != null && !userName.isEmpty() && !userName.contains("ALL")) {
					predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
				}

				if (csmName != null && !CollectionUtils.isEmpty(csmName)) {
					LOGGER.info("CSM name for filter:{}",csmName);
					predicates.add(taskAssignmentJoin.get("userName").in(csmName));
				}

				if (serviceId != null) {
					predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));
				}

				if (serviceCode != null && !serviceCode.isEmpty()) {
					predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));
				}

				if (orderType != null && !orderType.isEmpty() && !orderType.contains("ALL")) {
					predicates.add(root.get("orderType").in(orderType));
				}
				if (orderCategory != null && !orderCategory.isEmpty() && !orderCategory.contains("ALL")) {
					predicates.add(root.get("orderCategory").in(orderCategory));
				}
				if (orderSubCategory != null && !orderSubCategory.isEmpty() && !orderSubCategory.contains("ALL")) {
					predicates.add(root.get("orderSubCategory").in(orderSubCategory));
				}

				if (serviceType != null && !serviceType.isEmpty() && !serviceType.contains("ALL")) {
					predicates.add(root.get("serviceType").in(serviceType));
				}
				
				
				if (city != null && !city.isEmpty() && !city.contains("ALL")) {
					predicates.add(root.get("city").in(city));
				}
				
				if(state!=null && !state.isEmpty() && !state.contains("ALL")) {
					predicates.add(root.get("state").in(state));
				}

				if (country != null && !country.isEmpty() && !country.contains("ALL")) {
					predicates.add(root.get("country").in(country));
				}
				
				if (customerName != null && !customerName.isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%"));

				}

				if(lastMileScenario!=null && !lastMileScenario.isEmpty() && !lastMileScenario.contains("ALL")) {
					predicates.add(root.get("lastMileScenario").in(lastMileScenario));
				}
				if(lmProvider!=null && !lmProvider.isEmpty() && !lmProvider.contains("ALL")) {
					predicates.add(root.get("lmProvider").in(lmProvider));
				}

				if (taskKeys != null && !taskKeys.isEmpty() && !taskKeys.contains("ALL")) {
					Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);

					predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
				}
			
				Join<ScServiceDetail, Task> serviceDetailsJoin=root.join("scServiceDetail",JoinType.INNER);

				if(!CollectionUtils.isEmpty(pmName)){
					predicates.add(serviceDetailsJoin.get("assignedPM").in(pmName));
				}
				if(isJeopardyTask){
					predicates.add(criteriaBuilder.equal(root.get("isJeopardyTask"), (byte)1 ));
				}
				if (distributionCenterName != null && !distributionCenterName.isEmpty() && !distributionCenterName.contains("ALL")) {
					predicates.add(root.get("distributionCenterName").in(distributionCenterName));
				}

				Join<MstStatus, ScServiceDetail> serviceStatusJoin = serviceDetailsJoin.join("mstStatus", JoinType.INNER);

				if (negotiationTaskRequired) {
					predicates.add(serviceStatusJoin.get("code").in(
							java.util.Arrays.asList("INPROGRESS", "ACTIVE", "HOLD","JEOPARDY-INITIATED","DEFERRED-DELIVERY")));
				} else {
					predicates.add(serviceStatusJoin.get("code")
							.in(java.util.Arrays.asList("INPROGRESS", "ACTIVE","RESOURCE-RELEASED-INITIATED","CANCELLATION-INPROGRESS","TERMINATION-INITIATED","TERMINATION-INPROGRESS")));
				}
				
				if(devicePlatform!=null) {
					predicates.add(criteriaBuilder.equal(root.get("devicePlatform"), devicePlatform));

				}
				if(deviceType!=null) {
					predicates.add(criteriaBuilder.equal(root.get("deviceType"), deviceType));

				}
				if(isIpDownTimeRequired!=null) {
					predicates.add(criteriaBuilder.equal(root.get("isIpDownTimeRequired"), isIpDownTimeRequired));

				}
				if(isTxDowntimeReqd!=null) {
					predicates.add(criteriaBuilder.equal(root.get("isTxDowntimeReqd"), isTxDowntimeReqd));

				}

				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
           
	
	public static final Specification<Task> getCimUserFilter(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType, List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city, List <String> country,String customerName,List<String> state) {
			
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<TaskAssignment, Task> taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);

				final List<Predicate> predicates = new ArrayList<>();
				
				if(status!=null && !status.isEmpty())predicates.add(mstStatusJoin.get("code").in(status));
				
				if (groupName != null && !groupName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), "customer"));

				if(userName!=null && !userName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
			
				if (serviceId != null)predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));				

				if(serviceCode!=null && !serviceCode.isEmpty())predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));

				if(orderType!=null && !orderType.isEmpty()) predicates.add(root.get("orderType").in(orderType));
				
				if(serviceType!=null && !serviceType.isEmpty())predicates.add(root.get("serviceType").in(serviceType));
				
				if (city != null && !city.isEmpty())predicates.add(root.get("city").in(city));
				
				if(state!=null && !state.isEmpty()) {
					predicates.add(root.get("state").in(state));
				}

				if (country != null && !country.isEmpty())predicates.add(root.get("country").in(country));

				if(taskKeys!=null && !taskKeys.isEmpty())predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
				if(customerName!=null) {
					predicates.add(criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%"));

				}
				

				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



}
