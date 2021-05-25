package com.tcl.dias.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;
import com.tcl.dias.oms.entity.entities.UserToPartnerLe;

public class UserMappingSpecification {

	private UserMappingSpecification() {
		// DO NOTHING
	}

	public static Specification<UserToCustomerLe> getCustomerUserMapping(final Integer userId, final String leName) {
		return new Specification<UserToCustomerLe>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<UserToCustomerLe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> andPredicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(leName)) {
					predicates.add(builder.like(builder.lower(root.get("erfCustomerLeName")),
							"%" + StringUtils.lowerCase(leName) + "%"));
				}
				if (userId != null) {
					Join<UserToCustomerLe, User> userToCustomerLeJoin = root.join("user", JoinType.INNER);
					andPredicates.add(builder.equal(userToCustomerLeJoin.get("id"), userId));
				}

				if (!predicates.isEmpty() && !andPredicates.isEmpty()) {
					return builder.and(builder.and(andPredicates.toArray(new Predicate[andPredicates.size()])),
							builder.or(predicates.toArray(new Predicate[predicates.size()])));
				} else {
					return builder.and(builder.and(andPredicates.toArray(new Predicate[andPredicates.size()])));
				}
			}
		};
	}

	public static Specification<UserToPartnerLe> getPartnerUserMapping(final Integer userId, final String leName) {
		return new Specification<UserToPartnerLe>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<UserToPartnerLe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> andPredicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(leName)) {
					predicates.add(builder.like(builder.lower(root.get("erfCusPartnerLeName")),
							"%" + StringUtils.lowerCase(leName) + "%"));
				}
				if (userId != null) {
					Join<UserToCustomerLe, User> userToCustomerLeJoin = root.join("user", JoinType.INNER);
					andPredicates.add(builder.equal(userToCustomerLeJoin.get("id"), userId));
				}
				if (!predicates.isEmpty() && !andPredicates.isEmpty()) {
					return builder.and(builder.and(andPredicates.toArray(new Predicate[andPredicates.size()])),
							builder.or(predicates.toArray(new Predicate[predicates.size()])));
				} else {
					return builder.and(builder.and(andPredicates.toArray(new Predicate[andPredicates.size()])));
				}
			}
		};
	}
}
