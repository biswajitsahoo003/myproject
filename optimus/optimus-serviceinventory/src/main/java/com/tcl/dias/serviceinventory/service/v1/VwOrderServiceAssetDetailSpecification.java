package com.tcl.dias.serviceinventory.service.v1;

import com.tcl.dias.serviceinventory.entity.entities.VwOrderServiceAssetInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * VwOrderServiceAssetDetailSpecification class is to define specification for VwOrderServiceAssetInfo.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class VwOrderServiceAssetDetailSpecification {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VwOrderServiceAssetDetailSpecification.class);
	
	private VwOrderServiceAssetDetailSpecification() {
		
	}
	
	public Specification<VwOrderServiceAssetInfo> getOrders(final String city, final String cloudType, final String businessUnit, final String zone, 
			final String opptyClassification, final String partnerLeName, final String serviceId, final Set<String> additionalServiceIds, 
			final List<Integer> customerLeIds, final List<Integer> partnerLeIds, final Integer productId){
		return new Specification<VwOrderServiceAssetInfo>() {
			@Override
			public Predicate toPredicate(Root<VwOrderServiceAssetInfo> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
                final List<Predicate> predicates = new ArrayList<>();
                List<Integer> finalCustomerLeIds = null;
                List<Integer> finalPartnerLeIds = null;
                if(customerLeIds != null) {
                	finalCustomerLeIds = customerLeIds.stream().filter(customerLeId -> customerLeId != null).collect(Collectors.toList());;
                }
                if(partnerLeIds != null) {
                	finalPartnerLeIds = partnerLeIds.stream().filter(partnerLeId -> partnerLeId != null).collect(Collectors.toList());;
                }
                getPredicatesBasedOnSearchCriteria(predicates, root, criteriaBuilder, city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, 
                		serviceId, additionalServiceIds, finalCustomerLeIds, finalPartnerLeIds, productId);
                query.groupBy(root.get("orderId")).distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	private void getPredicatesBasedOnSearchCriteria(List<Predicate> predicates, Root<VwOrderServiceAssetInfo> root, CriteriaBuilder criteriaBuilder, 
			final String city, final String cloudType, final String businessUnit, final String zone, final String opptyClassification, final String partnerLeName, 
			final String serviceId, final Set<String> additionalServiceIds, final List<Integer> customerLeIds, final List<Integer> partnerLeIds, final Integer productId) {
		
		if(null!=productId) {
            predicates.add(criteriaBuilder.equal(root.get("productCatalogId"), productId));
        }
        if (!Objects.isNull(customerLeIds) && !CollectionUtils.isEmpty(customerLeIds)){
        	LOGGER.info("customerLeIds: {}", customerLeIds);
            predicates.add(root.get("customerLeId").in(customerLeIds));
        }
        if (!Objects.isNull(partnerLeIds) && !CollectionUtils.isEmpty(partnerLeIds)){
        	LOGGER.info("partnerLeIds: {}", partnerLeIds);
            predicates.add(root.get("partnerLeId").in(partnerLeIds));
        }
        if(!StringUtils.isAllBlank(city) && !city.equalsIgnoreCase("All")) {
        	predicates.add(criteriaBuilder.equal(root.get("city"), city));
        }
        if(!StringUtils.isAllBlank(cloudType) && !cloudType.equalsIgnoreCase("All")) {
        	predicates.add(criteriaBuilder.equal(root.get("siteType"), cloudType));
        }
        if(!StringUtils.isAllBlank(opptyClassification) && !opptyClassification.equalsIgnoreCase("All")) {
        	predicates.add(criteriaBuilder.equal(root.get("opportunityClassification"), opptyClassification));
        }
        if(!StringUtils.isAllBlank(partnerLeName) && !partnerLeName.equalsIgnoreCase("All")) {
        	predicates.add(criteriaBuilder.equal(root.get("partnerLeName"), partnerLeName));
        }
        if(!StringUtils.isAllBlank(serviceId)) {
        	List<Predicate> serviceIdPredicates = new ArrayList<>();
        	serviceIdPredicates.add(criteriaBuilder.like(root.get("serviceId"), serviceId + "%"));
        	if(additionalServiceIds != null && additionalServiceIds.size() > 0) {
        		for(String additionalServiceId : additionalServiceIds) {
        			serviceIdPredicates.add(criteriaBuilder.equal(root.get("serviceId"), additionalServiceId));
        		}
        	}
        	
        	predicates.add(criteriaBuilder.or(serviceIdPredicates.toArray(new Predicate[serviceIdPredicates.size()])));
        }
        if(!StringUtils.isAllBlank(businessUnit) && !businessUnit.equalsIgnoreCase("All")) {
            predicates.add(criteriaBuilder.equal(root.get("businessUnit"), businessUnit));
        }
        if(!StringUtils.isAllBlank(zone) && !zone.equalsIgnoreCase("All")) {
            predicates.add(criteriaBuilder.equal(root.get("zone"), zone));
        }
        predicates.add(criteriaBuilder.notEqual(root.get("status"), "Terminated"));
	}

	
	private void getPredicatesBasedOnSearchCriteria(List<Predicate> predicates, Root<VwOrderServiceAssetInfo> root,CriteriaBuilder criteriaBuilder,Set<String> serviceIds,Set<String> types) {
        if (!Objects.isNull(serviceIds) && !CollectionUtils.isEmpty(serviceIds)){
        	predicates.add(criteriaBuilder.in(root.get("serviceId")).value(serviceIds));
        }
        if (!Objects.isNull(types) && !CollectionUtils.isEmpty(types)){
        	predicates.add(criteriaBuilder.in(root.get("type")).value(types));
        }
	}

	
	public Specification<VwOrderServiceAssetInfo> getOrderBasedAssetDetails(final String city, final String cloudType, 
			final String businessUnit, final String zone, final String opptyClassification, final String partnerLeName, 
			final String serviceId, final Set<String> additionalServiceIds, final List<Integer> customerLeIds, final List<Integer> partnerLeIds, 
			final Integer productId, boolean isGroupBy) {
		return new Specification<VwOrderServiceAssetInfo>() {
			@Override
			public Predicate toPredicate(Root<VwOrderServiceAssetInfo> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
                final List<Predicate> predicates = new ArrayList<>();
                getPredicatesBasedOnSearchCriteria(predicates, root, criteriaBuilder, city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, 
                		serviceId, additionalServiceIds, customerLeIds, partnerLeIds, productId);
                if(isGroupBy) {
                	query.groupBy(root.get("orderId")).orderBy(criteriaBuilder.desc(root.get("orderId"))).distinct(true);
                } else {
                	query.orderBy(criteriaBuilder.desc(root.get("orderId"))).distinct(true);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public Specification<VwOrderServiceAssetInfo> getServiceBasedAssetDetails(Set<String> serviceIds,Set<String> types){
		return new Specification<VwOrderServiceAssetInfo>() {
			@Override
			public Predicate toPredicate(Root<VwOrderServiceAssetInfo> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				// Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                getPredicatesBasedOnSearchCriteria(predicates,root,criteriaBuilder,serviceIds,types);
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
