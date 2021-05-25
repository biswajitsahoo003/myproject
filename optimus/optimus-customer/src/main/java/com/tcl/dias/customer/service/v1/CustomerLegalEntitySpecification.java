package com.tcl.dias.customer.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.customer.entity.entities.Customer;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
/**
 * 
 * This is the spec class for the Customer legalentity
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLegalEntitySpecification {
	private CustomerLegalEntitySpecification() {
		
	}
	public static Specification<CustomerLegalEntity> getAllLegalEntities(final Integer customerId,final String leName,List<Integer> customerLeIds) {
		return new Specification<CustomerLegalEntity>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<CustomerLegalEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				Join<Customer, CustomerLegalEntity> customerJoin = root.join("customer",JoinType.INNER);
				// Adding predicates in case of filter parameter not being null
				if (customerId!=null) {
					predicates.add(builder.equal(customerJoin.get("id"),customerId));
				}
				if (!Objects.isNull(customerLeIds) && !CollectionUtils.isEmpty(customerLeIds)){
	            	predicates.add(root.get("id").in(customerLeIds));
	            }
				if(StringUtils.isNoneEmpty(leName)) {
					predicates.add(builder.like(builder.lower(root.get("entityName")), "%"+StringUtils.lowerCase(leName)+"%"));
				}
				if(predicates.isEmpty()) {
					predicates.add(builder.equal(root.get("status"),CommonConstants.BACTIVE));
				}
				return builder.and(builder.equal(root.get("status"), CommonConstants.BACTIVE),builder.or(predicates.toArray(new Predicate[predicates.size()])));

			}
		};
	}
}
