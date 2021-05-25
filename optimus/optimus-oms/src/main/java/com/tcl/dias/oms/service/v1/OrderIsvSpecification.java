package com.tcl.dias.oms.service.v1;

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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
/**
 * 
 * This is the Specification class for ISV get orders api
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderIsvSpecification {

	/**
	 * Get Orders By Specification
	 *
	 * @param stage
	 * @param productFamilyId
	 * @param legalEntity
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Specification<Order> getOrders(final Integer legalEntity, final Integer customerId,
			final String optyId, final String orderCode) {
		return new Specification<Order>() {
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				Join<Order, OrderToLe> orderToLeJoin = root.join("orderToLes", JoinType.INNER);
				Join<Order, Customer> customerJoin = root.join("customer", JoinType.INNER);

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				
				final List<Predicate> customerPredicates = new ArrayList<>();

				if (legalEntity!=null && customerId != null) {
					customerPredicates.add(builder.and(builder.equal(orderToLeJoin.get("erfCusCustomerLegalEntityId"), legalEntity),builder.equal(customerJoin.get("erfCusCustomerId"), customerId)));
				}else if(legalEntity != null && customerId==null) {
					customerPredicates.add(builder.equal(orderToLeJoin.get("erfCusCustomerLegalEntityId"),legalEntity));
				}else if(legalEntity == null && customerId!=null) {
					customerPredicates.add(builder.equal(customerJoin.get("erfCusCustomerId"), customerId));
				}
				
				if (StringUtils.isNotEmpty(optyId)) {
					//predicates.add(builder.equal(orderToLeJoin.get("tpsSfdcCopfId"), optyId));
					predicates.add(builder.like(builder.lower(orderToLeJoin.get("tpsSfdcCopfId")),
							"%" + StringUtils.lowerCase(optyId) + "%"));
				}

				if (StringUtils.isNotEmpty(orderCode)) {
					predicates.add(builder.like(builder.lower(root.get("orderCode")),
							"%" + StringUtils.lowerCase(orderCode) + "%"));
				}
				if (legalEntity==null && customerId == null && !StringUtils.isNotEmpty(optyId) && !StringUtils.isNotEmpty(orderCode)) {
				predicates.add(builder.equal(root.get("status"), CommonConstants.BACTIVE));
				}
				query.groupBy(root.get("id"));
				query.orderBy(builder.desc(root.get("createdTime")));


				if(!customerPredicates.isEmpty() && !predicates.isEmpty()) {
					return builder.and(builder.or(customerPredicates.toArray(new Predicate[customerPredicates.size()])), builder.or(predicates.toArray(new Predicate[predicates.size()])));
				}else if(!customerPredicates.isEmpty() && predicates.isEmpty()) {
					return builder.or(customerPredicates.toArray(new Predicate[customerPredicates.size()]));
				}else {
					return builder.or(predicates.toArray(new Predicate[predicates.size()]));

				}    

			}
		};
	}

}
