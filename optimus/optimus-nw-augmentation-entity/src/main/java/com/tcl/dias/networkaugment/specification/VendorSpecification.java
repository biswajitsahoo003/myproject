package com.tcl.dias.networkaugment.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.networkaugment.entity.entities.MstStatus;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.entities.MstVendor;


/**
 * @author vivek
 * VendorSpecification used for the vendor specification
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class VendorSpecification {

	private VendorSpecification() {

	}

	/**
	 * filter task based on group
	 * 
	 * @author vivek
	 * @param groupName
	 * @return
		 */
	
		public static final Specification<MstVendor> getVendorFilter(final String name, final String status,String country,String type,String state,String city) {
			return new Specification<MstVendor>() {
	
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
				public Predicate toPredicate(Root<MstVendor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					Join<MstStatus, Task> mstStatusJoin = root.join("mstStatus", JoinType.INNER);
					final List<Predicate> predicates = new ArrayList<>();
					/*if(status!=null && !status.isEmpty()) {
						predicates.add(criteriaBuilder.equal(mstStatusJoin.get("code"), status));
	
					}*/
	
					if (type != null) {
						predicates.add(criteriaBuilder.equal(root.get("type"), type));
	
					}
					
				/*	if (name != null) {
						predicates.add(criteriaBuilder.equal(root.get("name"), name));
	
					}
					
					if (city != null) {
						predicates.add(criteriaBuilder.equal(root.get("city"), city));
	
					}
					
					if (country!=null) {
						predicates.add(criteriaBuilder.equal(root.get("country"), country));
	
					}
					
					if(state!=null) {
						predicates.add(criteriaBuilder.equal(root.get("state"), state));
					}*/
					
					
				if ((state != null) && (city!=null)) {
					predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("circle"), "%"+city+"%"),criteriaBuilder.like(root.get("circle"), "%"+state+"%")));
				}
					
					
					query.distinct(true);
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				
				}
			};
			
		}
}
