package com.tcl.dias.oms.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;

public class QuoteSpecification {

	public static Specification<Quote> getQuotes(final String status) {
		return new Specification<Quote>() {
			public Predicate toPredicate(Root<Quote> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();

				// Adding predicates in case of filter parameter not being null
				if (StringUtils.isNotEmpty(status)) {
					predicates.add(builder.equal(builder.lower(root.get("status")),1));
					query.orderBy(builder.desc(root.get("id")));
					Subquery<Order> subQuery = query.subquery(Order.class);
					Root<Order> subRoot = subQuery.from(Order.class);
					subQuery.select(subRoot);
					Predicate orderPredicate = builder.equal(root.get("id"), subRoot.get("quote").get("id"));
					subQuery.select(subRoot).where(orderPredicate);
					predicates.add(builder.exists(subQuery).not());
				}

				return builder.and(predicates.toArray(new Predicate[predicates.size()]));

			}
		};
	}

}
