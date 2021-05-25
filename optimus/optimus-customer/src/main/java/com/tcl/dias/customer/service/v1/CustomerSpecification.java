package com.tcl.dias.customer.service.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.customer.entity.entities.Customer;
/**
 * 
 * This is specification class for customer 
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerSpecification {
	
	private CustomerSpecification() {
		
	}
	/**
	 * 
	 * Get All filters for customer
	 * @param name
	 * @return
	 */
	public static Specification<Customer> getAllCustomers(final String name) {
		return new Specification<Customer>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				
				if(StringUtils.isNoneEmpty(name)) {
					predicates.add(builder.like(builder.lower(root.get("customerName")), "%"+StringUtils.lowerCase(name)+"%"));
				}
				if(predicates.isEmpty()) {
					predicates.add(builder.equal(root.get("status"),CommonConstants.BACTIVE));
				}
				return builder.and(builder.equal(root.get("status"), CommonConstants.BACTIVE),builder.or(predicates.toArray(new Predicate[predicates.size()])));

			}
		};
	}

}
