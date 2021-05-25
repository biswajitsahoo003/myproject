package com.tcl.dias.l2oworkflow.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;

/**
 * used to filter the assigned task
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TaskAssignmentSpecification {

	private TaskAssignmentSpecification() {
		
		

	}

	/**
	 * filter task based on group
	 * @author vivek
	 * @param groupName
	 * @return
	 */
	
	/* need to add more criteria once we get the clarity*/
	public static final Specification<TaskAssignment> getAssignmentFilter(final String groupName,List<String> status) {
		return new Specification<TaskAssignment>() {

			@Override
			public Predicate toPredicate(Root<TaskAssignment> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				//Join<Task, TaskAssignment> taskAssignmentJoin = root.join("taskAssignments",JoinType.INNER);

				final List<Predicate> predicates = new ArrayList<>();
				if (groupName != null) {
					predicates.add(criteriaBuilder.equal(root.get("group"), groupName));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
