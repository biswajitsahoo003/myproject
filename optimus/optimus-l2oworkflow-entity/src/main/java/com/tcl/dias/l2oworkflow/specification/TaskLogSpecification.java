package com.tcl.dias.l2oworkflow.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.Task;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * used for the Log filter
 */
public class TaskLogSpecification {
	
	private TaskLogSpecification() {

	}

	/**
	 * filter task based on group
	 * 
	 * @author vivek
	 * @param groupName
	 * @param userName 
	 * @return
	 */

	public static final Specification<ProcessTaskLog> getTaskLogFilter(Integer taskId, String groupName,
			String orderCode, Integer serviceId, String userName) {
		return new Specification<ProcessTaskLog>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ProcessTaskLog> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				final List<Predicate> predicates = new ArrayList<>();

				if (taskId != null) {
					Join<Task, ProcessTaskLog> taskJoinJoin = root.join("task", JoinType.INNER);
					predicates.add(criteriaBuilder.equal(taskJoinJoin.get("id"), taskId));

				}

				if (groupName != null && !StringUtils.isEmpty(groupName)) {

					predicates.add(criteriaBuilder.equal(root.get("groupFrom"), groupName));

				}

				if (userName != null) {
					predicates.add(criteriaBuilder.equal(root.get("actionFrom"), userName));

				}

				if (orderCode != null && !StringUtils.isEmpty(orderCode)) {

					predicates.add(criteriaBuilder.equal(root.get("orderCode"), orderCode));

				}
				if (serviceId != null) {
					predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));

				}
				query.orderBy(criteriaBuilder.desc(root.get("createdTime")));
				query.distinct(true);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	


}
