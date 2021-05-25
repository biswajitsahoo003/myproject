package com.tcl.dias.oms.service.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.User;

public class UserSpecification {

	public static Specification<User> getUsers(final String username, final String contactNo, final String emailId,
			final String firstName) {
		return new Specification<User>() {
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();

				// Adding predicates in case of filter parameter not being null
				if (StringUtils.isNotEmpty(username)) {
					predicates.add(builder.equal(root.get("username"), username));
				}

				if (StringUtils.isNotEmpty(contactNo)) {
					predicates.add(builder.equal(root.get("contactNo"), contactNo));
				}

				if (StringUtils.isNotEmpty(emailId)) {
					predicates.add(builder.equal(root.get("emailId"), emailId));
				}

				if (StringUtils.isNotEmpty(firstName)) {
					predicates.add(builder.equal(root.get("firstName"), firstName));
				}
				predicates.add(builder.equal(root.get("status"), CommonConstants.ACTIVE));

				return builder.and(predicates.toArray(new Predicate[predicates.size()]));

			}
		};
	}
}
