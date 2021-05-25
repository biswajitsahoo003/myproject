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

import com.tcl.dias.oms.entity.entities.MstGroupType;
import com.tcl.dias.oms.entity.entities.MstUserGroups;

/**
 * 
 * Mst User group details Criteria class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstUserGroupSpecification {

	private MstUserGroupSpecification() {

	}

	/**
	 * 
	 * Get Mst User group details using search text
	 * 
	 * @param userGroupName
	 * @param groupType
	 * @return
	 */
	public static Specification<MstUserGroups> geMsttUserGroups(final String userGroupName, final String groupType,
			final Byte status) {
		return new Specification<MstUserGroups>() {

			private static final long serialVersionUID = 1210881532726380557L;

			public Predicate toPredicate(Root<MstUserGroups> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				// Adding predicates in case of filter parameter not being null
				if (StringUtils.isNotEmpty(userGroupName)) {
					predicates.add(builder.like(builder.lower(root.get("groupName")),
							"%" + StringUtils.lowerCase(userGroupName) + "%"));
				}

				if (status!=null) {
					predicates.add(builder.equal(builder.lower(root.get("status")), status));
				}

				Join<MstUserGroups, MstGroupType> groupTypeJoin = root.join("mstGroupType", JoinType.INNER);

				if (StringUtils.isNotEmpty(groupType)) {
					predicates.add(builder.like(builder.lower(groupTypeJoin.get("groupType")),
							"%" + StringUtils.lowerCase(groupType) + "%"));
				}

				return builder.and(predicates.toArray(new Predicate[predicates.size()]));

			}
		};
	}
}
