package com.tcl.dias.preparefulfillment.specification;

import java.sql.Timestamp;
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

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class ServiceDetailSpecification {
	
	
private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDetailSpecification.class);
	
	private ServiceDetailSpecification() {

	}
	


	public static final Specification<ScServiceDetail> getServiceDetailsFilter(List<String> orderType,List<String> orderCategory,List<String> orderSubCategory,List<String> status,String customerName,List<String> lastMileScenario,List<String> productName,List<String> groupName,List<String> assignedPM ,String orderCode,String serviceCode,String internationalServiceCode,Boolean isJeopardyTask, List<String> serviceConfigStatus, List<String> activationConfigStatus, List<String> billingStatus,String billStartDate,String billEndDate,String commissionedStartDate,String commissionedEndDate,String billingCompletionStartDate,String billingCompletionEndDate,String serviceConfigStartDate,String serviceConfigEndDate) {
			
		return new Specification<ScServiceDetail>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ScServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Join<MstStatus, ScServiceDetail> mstStatusJoin = root.join("mstStatus", JoinType.INNER);

				Join<ScOrder, ScServiceDetail> scOrderJoin = root.join("scOrder", JoinType.INNER);
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> jeopardyPredicates = new ArrayList<>();


				if (status != null && !status.isEmpty()) {
					predicates.add(mstStatusJoin.get("code").in(status));
				} else {
					predicates
							.add(mstStatusJoin.get("code").in(java.util.Arrays.asList("INPROGRESS","HOLD","CANCELLED","ACTIVE","INACTIVE","AMENDED","MOVETOM6","TERMINATE","TERMINATION-INITIATED")));
				}

				if (orderType != null && !orderType.isEmpty()) {
					predicates.add(root.get("orderType").in(orderType));
				}

				if (lastMileScenario != null && !lastMileScenario.isEmpty()) {

					predicates.add(root.get("lastmileScenario").in(lastMileScenario));

				}

				if (assignedPM != null && !assignedPM.isEmpty()) {

					predicates.add(root.get("assignedPM").in(assignedPM));

				}

				if (customerName != null) {

					predicates.add(
							criteriaBuilder.like(scOrderJoin.get("erfCustCustomerName"), "%" + customerName + "%"));

				}

				if (orderCategory != null && !orderCategory.isEmpty()) {

					predicates.add(root.get("orderCategory").in(orderCategory));

				}
				if (orderSubCategory != null && !orderSubCategory.isEmpty()) {

					predicates.add(root.get("orderSubCategory").in(orderSubCategory));

				}

				if (productName != null && !productName.isEmpty()) {
					predicates.add(root.get("erfPrdCatalogProductName").in(productName));

				}
				if (serviceCode != null && !serviceCode.isEmpty()) {
					predicates.add(root.get("uuid").in(serviceCode));

				}
				if (internationalServiceCode != null && !internationalServiceCode.isEmpty()) {
					predicates.add(root.get("tpsServiceId").in(internationalServiceCode));

				}
				if (orderCode != null && !orderCode.isEmpty()) {
					predicates.add(root.get("scOrderUuid").in(orderCode));
				}
				if (serviceConfigStatus != null && !serviceConfigStatus.isEmpty()) {
					predicates.add(root.get("serviceConfigStatus").in(serviceConfigStatus));
				}
				if (activationConfigStatus != null && !activationConfigStatus.isEmpty()) {
					predicates.add(root.get("activationConfigStatus").in(activationConfigStatus));
				}
				if (billingStatus != null && !billingStatus.isEmpty()) {
					predicates.add(root.get("billingStatus").in(billingStatus));
				}

				if (billStartDate != null || billEndDate != null) {

					predicates.add(criteriaBuilder.between(root.get("billStartDate"),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(billStartDate).getTime()),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(billEndDate).getTime())));
				}
				if (commissionedStartDate != null || commissionedEndDate != null) {
					predicates.add(criteriaBuilder.between(root.get("commissionedDate"),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(commissionedStartDate)
									.getTime()),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(commissionedStartDate)
									.getTime())));
				}

				if (billingCompletionStartDate != null || billingCompletionEndDate != null) {
					predicates.add(criteriaBuilder.between(root.get("billingCompletedDate"),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(billingCompletionStartDate)
									.getTime()),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(billingCompletionEndDate)
									.getTime())));
				}

				if (serviceConfigStartDate != null || serviceConfigEndDate != null) {
					predicates.add(criteriaBuilder.between(root.get("serviceConfigDate"),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(serviceConfigStartDate)
									.getTime()),
							new Timestamp(DateUtil.convertStringToDateYYMMDDIfNUllReturnDate(serviceConfigEndDate)
									.getTime())));
				}

				if (groupName != null) {
					Root<Task> taskRoot = query.from(Task.class);
					predicates.add(criteriaBuilder.equal(taskRoot.get("serviceId"), (root.get("id"))));

					Join<MstStatus, Task> taskMstStatusJoin = taskRoot.join("mstStatus", JoinType.INNER);

					Join<MstTaskDef, Task> mstTaskDef = taskRoot.join("mstTaskDef", JoinType.INNER);

					predicates.add(taskMstStatusJoin.get("code")
							.in(java.util.Arrays.asList("INPROGRESS", "OPEN", "ACTIVE", "REOPEN")));

					predicates.add(criteriaBuilder.equal(taskRoot.get("serviceId"), (root.get("id"))));

					predicates.add(mstTaskDef.get("assignedGroup").in(groupName));

				}

				predicates.add(criteriaBuilder.equal(root.get("isMigratedOrder"), ("N")));

				query.orderBy(criteriaBuilder.asc(root.get("serviceCommissionedDate")));

				if (isJeopardyTask != null && isJeopardyTask) {

					Join<Task, ScServiceDetail> taskJoin = root.join("tasks", JoinType.INNER);
					jeopardyPredicates.add(criteriaBuilder.equal(taskJoin.get("isJeopardyTask"), (byte) 1));

					Join<MstStatus, Task> mstTaskStatusJoin = taskJoin.join("mstStatus", JoinType.INNER);
					jeopardyPredicates.add(
							mstTaskStatusJoin.get("code").in(java.util.Arrays.asList("INPROGRESS", "OPEN", "REOPEN")));

					predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("isJeopardyTask"), (byte) 1),
							criteriaBuilder.and(jeopardyPredicates.toArray(new Predicate[jeopardyPredicates.size()]))));

				}

				query.distinct(true);

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
