package com.tcl.dias.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.User;

public class UserSpecification {

	private UserSpecification() {
		// DO NOTHING
	}

	public static Specification<User> getUsers(final String username, final String contactNo, final String emailId,
			final String firstName,List<Integer> userIds) {
		return new Specification<User>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(username)) {
					predicates.add(builder.like(builder.lower(root.get("username")), "%"+StringUtils.lowerCase(username)+"%"));
				}

				if (!StringUtils.isAllBlank(contactNo)) {
					predicates.add(builder.like(builder.lower(root.get("contactNo")), "%"+StringUtils.lowerCase(contactNo)+"%"));
				}

				if (!StringUtils.isAllBlank(emailId)) {
					predicates.add(builder.like(builder.lower(root.get("emailId")), "%"+StringUtils.lowerCase(emailId)+"%"));
				}

				if (!StringUtils.isAllBlank(firstName)) {
					predicates.add(builder.like(builder.lower(root.get("firstName")), "%"+StringUtils.lowerCase(firstName)+"%"));
				}
				if (!Objects.isNull(userIds) && !CollectionUtils.isEmpty(userIds)){
	            	predicates.add(root.get("id").in(userIds));
	            }
				if(predicates.isEmpty()) {
				predicates.add(builder.equal(root.get("status"), CommonConstants.ACTIVE));
				}
				return builder.and(builder.equal(root.get("status"), CommonConstants.ACTIVE),builder.or(predicates.toArray(new Predicate[predicates.size()])));

			}
		};
	}
}
