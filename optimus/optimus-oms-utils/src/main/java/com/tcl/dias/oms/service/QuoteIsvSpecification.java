package com.tcl.dias.oms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
/**
 * 
 * This is the Specification class for ISV get all quotes
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteIsvSpecification {

	/**
	 * Get Quotes By Specification
	 *
	 * @param stage
	 * @param productFamilyId
	 * @param legalEntity
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Specification<Quote> getQuotes(final Integer legalEntity, final Integer customerId,
			final String optyId, final String quoteCode) {
		return new Specification<Quote>() {
			public Predicate toPredicate(Root<Quote> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				Join<Quote, QuoteToLe> quoteToLeJoin = root.join("quoteToLes", JoinType.INNER);
				Join<Quote, Customer> customerJoin = root.join("customer", JoinType.INNER);

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				final List<Predicate> stpredicates = new ArrayList<>();
				final List<Predicate> customerPredicates = new ArrayList<>();
				if (legalEntity!=null && customerId != null) {
					customerPredicates.add(builder.and(builder.equal(quoteToLeJoin.get("erfCusCustomerLegalEntityId"), legalEntity),builder.equal(customerJoin.get("erfCusCustomerId"), customerId)));
				}else if(legalEntity!=null && customerId==null) {
					customerPredicates.add(builder.equal(quoteToLeJoin.get("erfCusCustomerLegalEntityId"),legalEntity));
				}else if(legalEntity==null && customerId!=null) {
					customerPredicates.add(builder.equal(customerJoin.get("erfCusCustomerId"), customerId));
				}
				
				
				if (StringUtils.isNotEmpty(optyId)) {
					//predicates.add(builder.equal(quoteToLeJoin.get("tpsSfdcOptyId"), optyId));
					predicates.add(builder.like(builder.lower(quoteToLeJoin.get("tpsSfdcOptyId")),
							"%" + StringUtils.lowerCase(optyId) + "%"));
				}

				if (StringUtils.isNotEmpty(quoteCode)) {
					predicates.add(builder.like(builder.lower(root.get("quoteCode")),
							"%" + StringUtils.lowerCase(quoteCode) + "%"));
				}
				//if (legalEntity==null && customerId == null && (!StringUtils.isNotEmpty(optyId)|| !StringUtils.isNotEmpty(quoteCode))) {
				stpredicates.add(builder.equal(root.get("status"), CommonConstants.BACTIVE));
				//}
				query.orderBy(builder.desc(root.get("createdTime")));

				if(!customerPredicates.isEmpty() && !predicates.isEmpty()) {
					return builder.and(builder.or(customerPredicates.toArray(new Predicate[customerPredicates.size()])), builder.or(predicates.toArray(new Predicate[predicates.size()])),builder.or(stpredicates.toArray(new Predicate[stpredicates.size()])));
				}else if(!customerPredicates.isEmpty() && predicates.isEmpty()) {
					return builder.and(builder.or(customerPredicates.toArray(new Predicate[customerPredicates.size()])), builder.or(stpredicates.toArray(new Predicate[stpredicates.size()])));
				}else {
					return builder.and(builder.or(predicates.toArray(new Predicate[predicates.size()])), builder.or(stpredicates.toArray(new Predicate[stpredicates.size()])));

				}    

			}
		};
	}

}
