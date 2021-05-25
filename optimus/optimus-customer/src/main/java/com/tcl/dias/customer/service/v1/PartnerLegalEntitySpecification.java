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
import com.tcl.dias.customer.entity.entities.Partner;
import com.tcl.dias.customer.entity.entities.PartnerLegalEntity;

/**
 * 
 * This is the specification class for Partner Legal Entity
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PartnerLegalEntitySpecification {
	private PartnerLegalEntitySpecification() {
		
	}
	
	public static Specification<PartnerLegalEntity> getAllLegalEntities(final Integer partnerId,final String leName,List<Integer> partnerLeIds) {
		return new Specification<PartnerLegalEntity>() {

			private static final long serialVersionUID = 2087739469830883894L;

			public Predicate toPredicate(Root<PartnerLegalEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				Join<Partner, PartnerLegalEntity> partnerJoin = root.join("partner",JoinType.INNER);
				// Adding predicates in case of filter parameter not being null
				if (partnerId!=null) {
					predicates.add(builder.equal(partnerJoin.get("id"),partnerId));
				}
				if (!Objects.isNull(partnerLeIds) && !CollectionUtils.isEmpty(partnerLeIds)){
	            	predicates.add(root.get("id").in(partnerLeIds));
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
