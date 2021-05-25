package com.tcl.dias.serviceinventory.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

public class SIServiceAssetSpecification {
	private static final Logger LOGGER = LoggerFactory.getLogger(SIServiceAssetSpecification.class);
	public static final Specification<SIAsset> getSdwanServiceDetails(final String searchKey,
			final List<Integer> customerLeIds, final List<Integer> partnerLeIds, final Integer productId,
			final Integer customerId, final Integer partnerId, String sortBy, String sortOrder, Boolean isGroupByName) {
		return new Specification<SIAsset>() {

			@Override
			public Predicate toPredicate(Root<SIAsset> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Join<SIServiceDetail, SIAsset> siAssetSIServiceDetailJoin;
				Join<SIAssetAttribute, SIAsset> assetAttributeSIAssetJoin;
//				Join<SIServiceAttribute, SIAsset> siServiceAttributeJoin;
				final List<Predicate> predicates = new ArrayList<>();
				// if condition to avoid count query from doing JOIN FETCH (throws error) and
				// JOIN FETCH is used to avoid N+1 problem
				if (Long.class != query.getResultType()) {
					LOGGER.info(" SIServiceAssetSpecification In Specification method {} ", query.getResultType());
					Fetch<SIServiceDetail, SIAsset> siAssetSIServiceDetailJoinFetch = root.fetch("siServiceDetail",
							JoinType.LEFT);
					siAssetSIServiceDetailJoin = (Join<SIServiceDetail, SIAsset>) siAssetSIServiceDetailJoinFetch;
					Fetch<SIAssetAttribute, SIAsset> assetAttributeSIAssetFetch = root.fetch("siAssetAttributes",
							JoinType.LEFT);
					assetAttributeSIAssetJoin = (Join<SIAssetAttribute, SIAsset>) assetAttributeSIAssetFetch;
//					Fetch<SIServiceAttribute, SIAsset> siServiceAttributeFetch = siAssetSIServiceDetailJoin
//							.fetch("siServiceAttributes", JoinType.LEFT);
//					siServiceAttributeJoin = (Join<SIServiceAttribute, SIAsset>) siServiceAttributeFetch;
					LOGGER.info(" SIServiceAssetSpecification Group by  {} ", isGroupByName);
					if(Objects.isNull(isGroupByName) || isGroupByName)
					query.groupBy(root.get("name"));
				} else {
					LOGGER.info(" SIServiceAssetSpecification In else condition - Specification method {} ", isGroupByName);
					siAssetSIServiceDetailJoin = root.join("siServiceDetail", JoinType.LEFT);
					assetAttributeSIAssetJoin = root.join("siAssetAttributes", JoinType.LEFT);
//					siServiceAttributeJoin = siAssetSIServiceDetailJoin.join("siServiceAttributes", JoinType.LEFT);
					LOGGER.info(" SIServiceAssetSpecification In else Group by  {} ", isGroupByName);
					query.distinct(true);
				}
				Subquery<SIServiceDetail> subquery = query.subquery(SIServiceDetail.class);
				Root<SIServiceDetail> subRoot = subquery.from(SIServiceDetail.class);
				Join<SIOrder, SIServiceDetail> siServiceDetailJoin = subRoot.join("siOrder", JoinType.LEFT);
				// Constructing list of filter parameters
				final List<Predicate> subQueryPredicates = new ArrayList<>();
//				predicates.add(siServiceAttributeJoin.get("attributeName").in("Sdwan_Template_name"));
				if (!StringUtils.isAllBlank(searchKey) && !searchKey.equalsIgnoreCase("Null")) {
					predicates.add(criteriaBuilder.or(
							criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
									"%" + StringUtils.lowerCase(searchKey) + "%"),
							criteriaBuilder.like(
									criteriaBuilder.lower(assetAttributeSIAssetJoin.get("attributeName")
											.equals(ServiceInventoryConstants.SDWAN_CPE_ALIAS)
													? assetAttributeSIAssetJoin.get("attributeValue")
													: assetAttributeSIAssetJoin.get("attributeValue")),
									"%" + StringUtils.lowerCase(searchKey) + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(siAssetSIServiceDetailJoin.get("sourceCity")),
									"%" + StringUtils.lowerCase(searchKey) + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(siAssetSIServiceDetailJoin.get("sourceCountry")),
									"%" + StringUtils.lowerCase(searchKey) + "%"),
							criteriaBuilder.like(
									criteriaBuilder.lower(siAssetSIServiceDetailJoin.get("izoSdwanSrvcId")),
									"%" + StringUtils.lowerCase(searchKey) + "%")));
				}
				if (customerLeIds != null && !customerLeIds.isEmpty()) {
					subQueryPredicates.add(siServiceDetailJoin.get("erfCustLeId").in(customerLeIds));
				}
				if (partnerLeIds != null && !partnerLeIds.isEmpty()) {
					subQueryPredicates.add(siServiceDetailJoin.get("erfCustPartnerLeId").in(partnerLeIds));
				}
				if (customerId != null) {
					subQueryPredicates
							.add(criteriaBuilder.equal(siServiceDetailJoin.get("erfCustCustomerId"), customerId));
				}
				if (partnerId != null) {
					subQueryPredicates
							.add(criteriaBuilder.equal(siServiceDetailJoin.get("erfCustPartnerId"), partnerId));
				}
				if (productId != null) {
					subQueryPredicates.add(criteriaBuilder.equal(subRoot.get("erfPrdCatalogProductId"), productId));
					subQueryPredicates.add(
							criteriaBuilder.notEqual(subRoot.get("serviceStatus"), "Terminated"));
					subQueryPredicates.add(
							criteriaBuilder.notEqual(subRoot.get("serviceStatus"), "Under Provisioning"));
					predicates.add(siAssetSIServiceDetailJoin.get("izoSdwanSrvcId")
							.in(subquery.select(subRoot.get("tpsServiceId")).where(criteriaBuilder
									.and(subQueryPredicates.toArray(new Predicate[subQueryPredicates.size()])))));
				}
				predicates.add(criteriaBuilder.equal(root.get("assetTag"), "SDWAN CPE"));
				predicates.add(criteriaBuilder.notEqual(siAssetSIServiceDetailJoin.get("serviceStatus"), "Terminated"));

				String sortByColumn = null;
				if (!StringUtils.isAllBlank(sortBy) && !StringUtils.isAllBlank(sortOrder)) {
					if (sortBy.equalsIgnoreCase("cpeName")) {
						sortByColumn = "name";
					} else if (sortBy.equalsIgnoreCase("country")) {
						sortByColumn = "sourceCountry";
					} else if (sortBy.equalsIgnoreCase("city")) {
						sortByColumn = "sourceCity";
					} else if (sortBy.equalsIgnoreCase("alias")) {
						sortByColumn = "siteAlias";
					} else if (sortBy.equalsIgnoreCase("siteName")) {
						sortByColumn = "izoSdwanSrvcId";
					}

					if (sortBy.equalsIgnoreCase("cpeName")) {
						if (sortOrder.equalsIgnoreCase("desc"))
							query.orderBy(criteriaBuilder.desc(root.get(sortByColumn)));
						else
							query.orderBy(criteriaBuilder.asc(root.get(sortByColumn)));
					} else if (!sortBy.equalsIgnoreCase("status")) {
						if (sortOrder.equalsIgnoreCase("desc"))
							query.orderBy(criteriaBuilder.desc(siAssetSIServiceDetailJoin.get(sortByColumn)));
						else
							query.orderBy(criteriaBuilder.asc(siAssetSIServiceDetailJoin.get(sortByColumn)));
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}

