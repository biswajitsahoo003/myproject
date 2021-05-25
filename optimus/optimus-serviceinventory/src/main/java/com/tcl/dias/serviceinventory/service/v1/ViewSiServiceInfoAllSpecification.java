package com.tcl.dias.serviceinventory.service.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ViewSiServiceInfoAllSpecification {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewSiServiceInfoAllSpecification.class);
	
	private ViewSiServiceInfoAllSpecification() {

	}
	
	

	/**
	 * filter task based on group
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @return
	 */
	public static final Specification<SIServiceDetail> getServiceInfoAllSearchAndSort(String searchText, Integer limit, Integer offset, String sortBy, String sortOrder,
			List<Integer> customerLeIds, final List<Integer> partnerLeIds, Integer productId,Integer customerId, Integer partnerId) {
			
		return new Specification<SIServiceDetail>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SIServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				LOGGER.info("Entering getServiceInfoAllSearchAndSort to search productId {} for customerLe {} against {} sortby {} in order {} ",productId, customerLeIds, searchText, sortBy, sortOrder);
				Join<SIOrder, SIServiceDetail> siOrderJoin;
				if(Long.class.equals(query.getResultType()))
				siOrderJoin = root.join("siOrder",JoinType.INNER);
				else {
					Fetch<SIOrder, SIServiceDetail> siServiceDetailFetch = root.fetch("siOrder");
					siOrderJoin = (Join<SIOrder, SIServiceDetail>) siServiceDetailFetch;
				}
				// Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                if(productId!=null) {
                	predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
                }
				 if(customerLeIds!=null && !customerLeIds.isEmpty()) {
	                	predicates.add(siOrderJoin.get("erfCustLeId").in(customerLeIds));
	                }
					if(partnerLeIds!=null && !partnerLeIds.isEmpty()) {
						predicates.add(siOrderJoin.get("erfCustPartnerLeId").in(partnerLeIds));
					}
					if(customerId!=null) {
						predicates.add(criteriaBuilder.equal(siOrderJoin.get("erfCustCustomerId"), customerId));
					}
					if(partnerId!=null) {
						predicates.add(criteriaBuilder.equal(siOrderJoin.get("erfCustPartnerId"), partnerId));
					}
					 predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Terminated"));
					 predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Under Provisioning"));
					if(!StringUtils.isAllBlank(searchText)&&!searchText.equalsIgnoreCase("Null")) {
					predicates.add(criteriaBuilder.or(
							criteriaBuilder.like(criteriaBuilder.lower(root.get("sourceCountry")),
									"%" + StringUtils.lowerCase(searchText) + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(root.get("siteAlias")),
									"%" + StringUtils.lowerCase(searchText) + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(root.get("sourceCity")),
									"%" + StringUtils.lowerCase(searchText) + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(root.get("tpsServiceId")),
									"%" + StringUtils.lowerCase(searchText) + "%")));
					}
					String sortByColumn = null;
					if(!StringUtils.isAllBlank(sortBy)&&!StringUtils.isAllBlank(sortOrder)) {
						if(!ServiceInventoryConstants.SITE_STATUS.equalsIgnoreCase(sortBy)) {
							if (sortBy.equalsIgnoreCase("siteName")) {
								sortByColumn = "tpsServiceId";
							} else if (sortBy.equalsIgnoreCase("country")) {
								sortByColumn = "destinationCountry";
							} else if (sortBy.equalsIgnoreCase("city")) {
								sortByColumn = "destinationCity";
							} else if (sortBy.equalsIgnoreCase("sdwanSiteAlias")) {
								sortByColumn = "siteAlias";
							}
							if (sortOrder.equalsIgnoreCase("desc"))
								query.orderBy(criteriaBuilder.desc(root.get(sortByColumn)));
							else
								query.orderBy(criteriaBuilder.asc(root.get(sortByColumn)));
						}
					}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * filter task based on group
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @return
	 */
	public static final Specification<SIServiceAttribute> getTemplateDetaisBasedOnSearchText(Integer limit, Integer offset, String sortBy, String sortOrder,
			List<Integer> customerLeIds, final List<Integer> partnerLeIds, Integer productId,Integer customerId, Integer partnerId, List<String> attributeValues) {
			
		return new Specification<SIServiceAttribute>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SIServiceAttribute> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				LOGGER.info("Entering getServiceInfoAllSearchAndSort to search productId {} for customerLe {} against {} sortby {} in order {} ",productId, customerLeIds, sortBy, sortOrder);
				Join<SIServiceDetail,SIServiceAttribute> srvDetailJoin = root.join("siServiceDetail",JoinType.LEFT);
				Join<SIOrder, SIServiceDetail> siOrderJoin = srvDetailJoin.join("siOrder",JoinType.INNER);
				// Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                if(productId!=null) {
                	predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
                }
				 if(customerLeIds!=null && !customerLeIds.isEmpty()) {
	                	predicates.add(siOrderJoin.get("erfCustLeId").in(customerLeIds));
	                }
					if(partnerLeIds!=null && !partnerLeIds.isEmpty()) {
						predicates.add(siOrderJoin.get("erfCustPartnerLeId").in(partnerLeIds));
					}
					if(customerId!=null) {
						predicates.add(criteriaBuilder.equal(siOrderJoin.get("erfCustCustomerId"), customerId));
					}
					if(partnerId!=null) {
						predicates.add(criteriaBuilder.equal(siOrderJoin.get("erfCustPartnerId"), partnerId));
					}
					if(!attributeValues.isEmpty()) {
//						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("attributeName"), "Sdwan_Template_Name")));
						predicates.add(root.get("attributeValue").in(attributeValues));
					}
					predicates.add(criteriaBuilder.notEqual(srvDetailJoin.get("serviceStatus"), "Terminated"));
					String sortByColumn = null;
					if(!StringUtils.isAllBlank(sortBy)&&!StringUtils.isAllBlank(sortOrder)) {
						if(sortBy.equalsIgnoreCase("templateName")) {
							sortByColumn = "attributeValue";
						} 
						if(sortOrder.equalsIgnoreCase("desc"))
							query.orderBy(criteriaBuilder.desc(root.get(sortByColumn)));
						else
							query.orderBy(criteriaBuilder.asc(root.get(sortByColumn)));
					}
					criteriaBuilder.function("group_concat", String.class, root.get("siServiceDetail"));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
