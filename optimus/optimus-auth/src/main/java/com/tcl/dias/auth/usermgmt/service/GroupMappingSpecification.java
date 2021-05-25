package com.tcl.dias.auth.usermgmt.service;

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

import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserGroupToLe;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;
import com.tcl.dias.oms.entity.entities.UserToPartnerLe;
import com.tcl.dias.oms.entity.entities.UserToUserGroup;
import com.tcl.dias.oms.entity.entities.UsergroupToPartnerLe;

public class GroupMappingSpecification {

	private GroupMappingSpecification() {
		// DO NOTHING
	}

	public static Specification<UserToUserGroup> getUserToUserGroupSpectification(final Integer userGroupId, final String username) {
		return new Specification<UserToUserGroup>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<UserToUserGroup> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> andPredicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(username)) {
					predicates.add(builder.like(builder.lower(root.get("username")),
							"%" + StringUtils.lowerCase(username) + "%"));
				}
				if (userGroupId != null) {
					Join<UserToUserGroup, MstUserGroups> userGroupTOUserGroupsJoin = root.join("mstUserGroup", JoinType.INNER);
					andPredicates.add(builder.equal(userGroupTOUserGroupsJoin.get("id"), userGroupId));
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
	
	public static Specification<UserGroupToLe> getUserToCustomerLeMapping(final Integer userGroupId, final String leName) {
		return new Specification<UserGroupToLe>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<UserGroupToLe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> andPredicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(leName)) {
					predicates.add(builder.like(builder.lower(root.get("erfCustomerLeName")),
							"%" + StringUtils.lowerCase(leName) + "%"));
				}
				if (userGroupId != null) {
					Join<UserToUserGroup, MstUserGroups> userGroupTOUserGroupsJoin = root.join("mstUserGroups", JoinType.INNER);
					andPredicates.add(builder.equal(userGroupTOUserGroupsJoin.get("id"), userGroupId));
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

	public static Specification<UsergroupToPartnerLe> getUserToPartnerLeMapping(final Integer userGroupId, final String leName) {
		return new Specification<UsergroupToPartnerLe>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<UsergroupToPartnerLe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> andPredicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (!StringUtils.isAllBlank(leName)) {
					predicates.add(builder.like(builder.lower(root.get("erfCusPartnerLeName")),
							"%" + StringUtils.lowerCase(leName) + "%"));
				}
				if (userGroupId != null) {
					Join<UserToUserGroup, MstUserGroups> userGroupTOUserGroupsJoin = root.join("mstUserGroup", JoinType.INNER);
					andPredicates.add(builder.equal(userGroupTOUserGroupsJoin.get("id"), userGroupId));
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
