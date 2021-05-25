package com.tcl.dias.l2oworkflow.specification;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.tcl.dias.l2oworkflow.entity.entities.MstStatus;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;

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
	 * @return
	 */

	public static final Specification<Task> getTaskFilter(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType, List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city) {
			
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<TaskAssignment, Task> taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);

				final List<Predicate> predicates = new ArrayList<>();
				
				if(status!=null && !status.isEmpty())predicates.add(mstStatusJoin.get("code").in(status));
				
				if (groupName != null && !groupName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), groupName));

				if(userName!=null && !userName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
			
				if (serviceId != null)predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));				

				if(serviceCode!=null && !serviceCode.isEmpty())predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));

				if(orderType!=null && !orderType.isEmpty()) predicates.add(root.get("orderType").in(orderType));
				
				if(serviceType!=null && !serviceType.isEmpty())predicates.add(root.get("serviceType").in(serviceType));
				
				if (city != null && !city.isEmpty())predicates.add(root.get("city").in(city));

				if(taskKeys!=null && !taskKeys.isEmpty())predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
				

				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
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
	 * @return
	 */

	public static final Specification<Task> getTaskFilterForMF(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType, List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city, String searchText,String createdTimeFrom, String createdTimeTo,String productName,Boolean workBench) {
			LOGGER.info("workbench status {}",workBench);
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<TaskAssignment, Task> taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);
				final List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));
				
				if(status!=null && !status.isEmpty())predicates.add(mstStatusJoin.get("code").in(status));
				
				if (groupName != null && !groupName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), groupName));

				if(userName!=null && !userName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
			
				if (serviceId != null)predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));				

				if(serviceCode!=null && !serviceCode.isEmpty())predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));

				if(orderType!=null && !orderType.isEmpty()) predicates.add(root.get("orderType").in(orderType));
				
				if(serviceType!=null && !serviceType.isEmpty())predicates.add(root.get("serviceType").in(serviceType));
				
				if (city != null && !city.isEmpty())predicates.add(root.get("city").in(city));

				if(taskKeys!=null && !taskKeys.isEmpty())predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
				
				if (workBench != null && workBench) {
					LOGGER.info("Workbench related Changes");
					Join<SiteDetail , Task> siteDetailJoin = root.join("siteDetail",JoinType.INNER);
					predicates.add(criteriaBuilder.isNotNull(root.get("siteDetail")));
					
					predicates.add(criteriaBuilder.isNotNull(siteDetailJoin.get("siteDetail")));
					
					predicates.add(criteriaBuilder.notEqual(siteDetailJoin.get("siteDetail"),"[]"));
					
					predicates.add(criteriaBuilder.notEqual(siteDetailJoin.get("accountName"), "Dev Test Services & Sons"));
					
					predicates.add(criteriaBuilder.notEqual(siteDetailJoin.get("accountName"), "Optimus Tiger Internal Testing"));
					
					if(productName!=null && !productName.equalsIgnoreCase("all")) {
						predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("quoteCode")), productName.toLowerCase()+"%"));
					}
				}
								
				if (searchText != null && !searchText.isEmpty()) {
					predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("feasibilityId"), searchText), 
							criteriaBuilder.equal(root.get("quoteCode"), searchText), criteriaBuilder.equal(root.get("siteCode"), searchText)));
				}
				if (createdTimeFrom != null && !createdTimeFrom.isEmpty()) {
					Timestamp end = null;
					Timestamp start = convertStringToTimeStamp(createdTimeFrom+" "+"00:00:00");
					if(createdTimeTo != null && !createdTimeTo.isEmpty()) {
						end = convertStringToTimeStamp(createdTimeTo+" "+"23:59:59");
					} else {
						SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
						end = convertStringToTimeStamp(sm.format(new Date(System.currentTimeMillis()))+" "+"23:59:59");
					}
					predicates.add(criteriaBuilder.between(root.get("createdTime"), start, end));
				}
				query.orderBy(criteriaBuilder.desc(root.get("updatedTime")));	
				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static final Specification<Task> getTaskSummary(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType, List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city, String searchText,
			String createdTimeFrom, String createdTimeTo, String owner) {
			
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Join<TaskAssignment, Task> taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);
				
				Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);				
				
				final List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));
				if(status!=null && !status.isEmpty())predicates.add(mstStatusJoin.get("code").in(status));
				
				if (groupName != null && !groupName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), groupName));

				if(userName!=null && !userName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
			
				if(owner!=null && !owner.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("owner"), owner));

			
				if (serviceId != null)predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));				

				if(serviceCode!=null && !serviceCode.isEmpty())predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));

				if(orderType!=null && !orderType.isEmpty()) predicates.add(root.get("orderType").in(orderType));
				
				if(serviceType!=null && !serviceType.isEmpty())predicates.add(root.get("serviceType").in(serviceType));
				
				if (city != null && !city.isEmpty())predicates.add(root.get("city").in(city));

				if(taskKeys!=null && !taskKeys.isEmpty())predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
								
				if (searchText != null && !searchText.isEmpty()) {
					predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("feasibilityId"), searchText), criteriaBuilder.equal(root.get("quoteCode"), searchText)));
				}
				
				if (createdTimeFrom != null && !createdTimeFrom.isEmpty()) {
					Timestamp end = null;
					Timestamp start = convertStringToTimeStamp(createdTimeFrom+" "+"00:00:00");
					if(createdTimeTo != null && !createdTimeTo.isEmpty()) {
						end = convertStringToTimeStamp(createdTimeTo+" "+"23:59:59");
					} else {
						end = convertStringToTimeStamp(createdTimeFrom+" "+"23:59:59");
					}
					predicates.add(criteriaBuilder.between(root.get("createdTime"), start, end));
				}
				query.orderBy(criteriaBuilder.desc(root.get("updatedTime")));	
				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	private static Timestamp convertStringToTimeStamp(String date) {
		Date formattedDate = new Date();
		Timestamp time = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			formattedDate = df.parse(date);
			time = new Timestamp(formattedDate.getTime());
		} catch (ParseException parseEx) {
			LOGGER.info("Exception while parsing string to timestamp : {} ", parseEx.getMessage());
		}
		return time;
	}
	public static final Specification<Task> getCimUserFilter(final String groupName, final List<String> status, String userName, 
			Integer serviceId, List<String> orderType, List<String> serviceType, List<String> taskKeys,String serviceCode, List<String> city) {
			
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<TaskAssignment, Task> taskAssignmentJoin = root.join("taskAssignments", JoinType.INNER);
				Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<MstTaskDef, Task> mstStatusTaskDefJoin = root.join("mstTaskDef", JoinType.INNER);

				final List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));
				if(status!=null && !status.isEmpty())predicates.add(mstStatusJoin.get("code").in(status));
				
				if (groupName != null && !groupName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("groupName"), "customer"));

				if(userName!=null && !userName.isEmpty())predicates.add(criteriaBuilder.equal(taskAssignmentJoin.get("userName"), userName));
			
				if (serviceId != null)predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));				

				if(serviceCode!=null && !serviceCode.isEmpty())predicates.add(criteriaBuilder.equal(root.get("serviceCode"), serviceCode));

				if(orderType!=null && !orderType.isEmpty()) predicates.add(root.get("orderType").in(orderType));
				
				if(serviceType!=null && !serviceType.isEmpty())predicates.add(root.get("serviceType").in(serviceType));
				
				if (city != null && !city.isEmpty())predicates.add(root.get("city").in(city));

				if(taskKeys!=null && !taskKeys.isEmpty())predicates.add(mstStatusTaskDefJoin.get("key").in(taskKeys));
				

				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
