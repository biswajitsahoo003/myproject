package com.tcl.dias.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.oms.entity.entities.Customer;

/**
 * CustomerSpecification class is to define specification for customer.
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerSpecification {
	
	private CustomerSpecification() {
	}

	public static Specification<Customer> getCustomers(final String customerName) {
		return new Specification<Customer>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1405255579752102873L;

			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();

				// Adding predicates in case of filter parameter not being null
				if (StringUtils.isNotEmpty(customerName)) {
					predicates.add(builder.like(builder.lower(root.get("customerName")), "%"+StringUtils.lowerCase(customerName)+"%"));
					}
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));

			}
		};
	}
}
