package com.tcl.dias.serviceinventory.service.v1;

import com.tcl.dias.serviceinventory.constants.MACDConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.ViewGscServiceCircuitLinkDetail;
import com.tcl.dias.serviceinventory.entity.repository.VwGscServiceCircuitLinkDetailRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author SanjeKum
 *
 */
public class SIServiceDetailSpecification {
	
	private SIServiceDetailSpecification() {
		
	}
	
	public static final Specification<SIServiceDetail> getServiceDetailsWithNdeFilter(final String city, final String alias,final String siOrderNumber,
																		 final List<Integer> customerLeIds, final List<Integer> partnerLeIds,
																		 final Integer productId,final Integer customerId,final Integer partnerId,
																		 final String opportunityMode,final String attributeValue){
		return new Specification<SIServiceDetail>() {

			@Override
			public Predicate toPredicate(Root<SIServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Join<SIOrder, SIServiceDetail> siOrderJoin = root.join("siOrder",JoinType.INNER);
				Join<SIServiceAttribute,SIServiceDetail> siAttributeJoin = root.join("siServiceAttributes",JoinType.INNER);
				// Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isAllBlank(city)&&!city.equalsIgnoreCase("All")){
                	predicates.add(criteriaBuilder.equal(root.get("sourceCity"), city));
                }
                if(productId!=null) {
                	predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
                }
                if(!StringUtils.isAllBlank(alias)&&!alias.equalsIgnoreCase("All")) {
                	predicates.add(criteriaBuilder.equal(root.get("siteAlias"), alias));
                }
                if(!StringUtils.isAllBlank(siOrderNumber)&&!siOrderNumber.equalsIgnoreCase("Null")) {
                	predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("tpsServiceId")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("siteAlias")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("sourceCity")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("siteAddress")), "%"+StringUtils.lowerCase(siOrderNumber)+"%")));
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
				if(opportunityMode!=null && !partnerLeIds.isEmpty() && !opportunityMode.equalsIgnoreCase("All")) {
					predicates.add(siOrderJoin.get("opportunityClassification").in(opportunityMode));
				}
				if(attributeValue!=null && !attributeValue.isEmpty()) {
					predicates.add(criteriaBuilder.equal(siAttributeJoin.get("attributeValue"), attributeValue));				
				}
				//predicates.add(criteriaBuilder.equal(root.get("isActive"), CommonConstants.Y));
                predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Terminated"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * Method get gsc service details based on search
	 *
	 * @param customerId
	 * @param tclSwitch
	 * @param circuitID
	 * @param ipAddress
	 * @param sipTrunkGroup
	 * @return {@link Specification<ViewGscServiceCircuitLinkDetail>}
	 */
	public static final Specification<ViewGscServiceCircuitLinkDetail> getGscServiceDetails(final Integer customerId, String circuitID, String ipAddress, String sipTrunkGroup, String tclSwitch) {
		return new Specification<ViewGscServiceCircuitLinkDetail>() {

			@Override
			public Predicate toPredicate(Root<ViewGscServiceCircuitLinkDetail> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.get("erfCusCustomerId"), customerId));
				if (Objects.nonNull(circuitID) && !StringUtils.isAllBlank(circuitID)) {
					predicates.add(criteriaBuilder.like(root.get("circuitId"), "%" + StringUtils.lowerCase(circuitID) + "%"));
				}
				if (Objects.nonNull(ipAddress) && !StringUtils.isAllBlank(ipAddress)) {
					predicates.add(criteriaBuilder.like(root.get("customerIpAddress"), "%" + StringUtils.lowerCase(ipAddress) + "%"));
				}

				if (Objects.nonNull(sipTrunkGroup) && !StringUtils.isAllBlank(sipTrunkGroup)) {
					predicates.add(criteriaBuilder.like(root.get("circuitGrCd"), "%" + StringUtils.lowerCase(sipTrunkGroup) + "%"));
				}
				if (Objects.nonNull(tclSwitch) && !StringUtils.isAllBlank(tclSwitch)) {
					predicates.add(criteriaBuilder.like(root.get("switchingUnitCd"), "%" + StringUtils.lowerCase(tclSwitch) + "%"));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static final Specification<SIServiceDetail> getServiceDetails(final String city, final String alias,final String siOrderNumber,
			final List<Integer> customerLeIds, final List<Integer> partnerLeIds,
			final Integer productId,final Integer customerId,final Integer partnerId,
			final String opportunityMode,final String vrfFlag){
		return new Specification<SIServiceDetail>() {

			@Override
			public Predicate toPredicate(Root<SIServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Join<SIOrder, SIServiceDetail> siOrderJoin = root.join("siOrder",JoinType.INNER);
				// Constructing list of filter parameters
				final List<Predicate> predicates = new ArrayList<>();
				if(!StringUtils.isAllBlank(city)&&!city.equalsIgnoreCase("All")){
					predicates.add(criteriaBuilder.equal(root.get("sourceCity"), city));
				}
				if(productId!=null) {
					predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
				}
				if(!StringUtils.isAllBlank(alias)&&!alias.equalsIgnoreCase("All")) {
					predicates.add(criteriaBuilder.equal(root.get("siteAlias"), alias));
				}
				if(!StringUtils.isAllBlank(siOrderNumber)&&!siOrderNumber.equalsIgnoreCase("Null")) {
					predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("tpsServiceId")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("siteAlias")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("sourceCity")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get("siteAddress")), "%"+StringUtils.lowerCase(siOrderNumber)+"%")));
				}
				if(vrfFlag!=null && !vrfFlag.isEmpty()) {
					predicates.add(criteriaBuilder.equal(root.get("multiVrfSolution"), vrfFlag));
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
				if(opportunityMode!=null && !partnerLeIds.isEmpty() && !opportunityMode.equalsIgnoreCase("All")) {
					predicates.add(siOrderJoin.get("opportunityClassification").in(opportunityMode));
				}
				//predicates.add(criteriaBuilder.equal(root.get("isActive"), CommonConstants.Y));
				predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Terminated"));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	
	
	/**
	 * Method to get the service details
	 * @param siteAcity
	 * @param siteBcity
	 * @param alias
	 * @param siOrderNumber
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @return
	 */
	public static final Specification<SIServiceDetail> getGDEServiceDetails(final String siteAcity, final String siteBcity,final String alias,final String siOrderNumber, final List<Integer> customerLeIds, final List<Integer> partnerLeIds, final Integer productId,final Integer customerId,final Integer partnerId){
		return new Specification<SIServiceDetail>() {

			@Override
			public Predicate toPredicate(Root<SIServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Join<SIOrder, SIServiceDetail> siOrderJoin = root.join("siOrder",JoinType.INNER);
				// Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isAllBlank(siteAcity)&&!siteAcity.equalsIgnoreCase("All")){
                	predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sourceCity"), siteAcity),criteriaBuilder.equal(root.get("siteType"), MACDConstants.SI_SITEA)));
                }
                if(!StringUtils.isAllBlank(siteBcity)&&!siteBcity.equalsIgnoreCase("All")){
                	predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sourceCity"), siteBcity),criteriaBuilder.equal(root.get("siteType"), MACDConstants.SI_SITEB)));
                }
                if(productId!=null) {
                	predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
                }
                if(!StringUtils.isAllBlank(alias)&&!alias.equalsIgnoreCase("All")) {
                	predicates.add(criteriaBuilder.equal(root.get("siteAlias"), alias));
                }
                if(!StringUtils.isAllBlank(siOrderNumber)&&!siOrderNumber.equalsIgnoreCase("Null")) {
                	predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tpsServiceId")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"));
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
                //predicates.add(criteriaBuilder.equal(root.get("isActive"), CommonConstants.Y));
                predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Terminated"));             
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * Specification for GDE search
	 * @param tpsServiceId
	 * @param alias
	 * @param siOrderNumber
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @return
	 */
	public static final Specification<SIServiceDetail> getGDEServiceDetailsForServiceId(List<String> tpsServiceId ,final String alias,final String siOrderNumber, final List<Integer> customerLeIds, final List<Integer> partnerLeIds, final Integer productId,final Integer customerId,final Integer partnerId){
		return new Specification<SIServiceDetail>() {

			@Override
			public Predicate toPredicate(Root<SIServiceDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {				
				Join<SIOrder, SIServiceDetail> siOrderJoin = root.join("siOrder",JoinType.INNER);
				// Constructing list of filter parameters	
                final List<Predicate> predicates = new ArrayList<>();
                if(tpsServiceId!=null && !tpsServiceId.isEmpty()) {
                	predicates.add(root.get("tpsServiceId").in(tpsServiceId));
                }
                if(productId!=null) {
                	predicates.add(criteriaBuilder.equal(root.get("erfPrdCatalogProductId"), productId));
                }
                if(!StringUtils.isAllBlank(alias)&&!alias.equalsIgnoreCase("All")) {
                	predicates.add(criteriaBuilder.equal(root.get("siteAlias"), alias));
                }
                if(!StringUtils.isAllBlank(siOrderNumber)&&!siOrderNumber.equalsIgnoreCase("Null")) {
                	predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tpsServiceId")), "%"+StringUtils.lowerCase(siOrderNumber)+"%"));
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
                //predicates.add(criteriaBuilder.equal(root.get("isActive"), CommonConstants.Y));
                predicates.add(criteriaBuilder.notEqual(root.get("serviceStatus"), "Terminated"));             
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}


}
