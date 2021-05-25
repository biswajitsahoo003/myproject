package com.tcl.dias.serviceinventory.service.v1;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIInfoGVPNServiceUCAAS;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service Inventory Specification for UCaaS WEBEX
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class SIInfoGVPNSpecification {

    public SIInfoGVPNSpecification() {
    }

    /**
     * Get service details specifications
     *
     * @param city
     * @param alias
     * @param searchText
     * @param customerId
     * @param partnerId
     * @return
     */
    public static Specification<SIInfoGVPNServiceUCAAS> getServiceDetails(final String city, final String alias, final String searchText, String customerId, Integer partnerId){
        return new Specification<SIInfoGVPNServiceUCAAS>() {

            /**
             * Add predicate parameters to specification
             *
             * @param root
             * @param query
             * @param criteriaBuilder
             * @return
             */
            @Override
            public Predicate toPredicate(Root<SIInfoGVPNServiceUCAAS> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                // Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isAllBlank(customerId)){
                    predicates.add(criteriaBuilder.equal(root.get(ServiceInventoryConstants.ORDER_CUSTOMER_ID), customerId));
                }
                if(Objects.nonNull(partnerId)){
                    predicates.add(criteriaBuilder.equal(root.get(ServiceInventoryConstants.ORDER_PARTNER), partnerId));
                }
                if(!StringUtils.isAllBlank(city)&&!city.equalsIgnoreCase(CommonConstants.ALL)){
                    predicates.add(criteriaBuilder.equal(root.get(ServiceInventoryConstants.SOURCE_CITY), city));
                }
                if(!StringUtils.isAllBlank(alias)&&!alias.equalsIgnoreCase(CommonConstants.ALL)) {
                    predicates.add(criteriaBuilder.equal(root.get(ServiceInventoryConstants.SITE_ALIAS), alias));
                }
                if(!StringUtils.isAllBlank(searchText)&&!searchText.equalsIgnoreCase(CommonConstants.NULL)) {
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get(ServiceInventoryConstants.SERVICE_ID)), "%"+StringUtils.lowerCase(searchText)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get(ServiceInventoryConstants.SITE_ALIAS)), "%"+StringUtils.lowerCase(searchText)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get(ServiceInventoryConstants.SOURCE_CITY)), "%"+StringUtils.lowerCase(searchText)+"%"),criteriaBuilder.like(criteriaBuilder.lower(root.get(ServiceInventoryConstants.CUSTOMER_SITE_ADDRESS)), "%"+StringUtils.lowerCase(searchText)+"%")));
                }
                predicates.add(criteriaBuilder.equal(root.get(ServiceInventoryConstants.IS_ACTIVE), CommonConstants.Y));
                predicates.add(criteriaBuilder.notEqual(root.get(ServiceInventoryConstants.SERVICE_STATUS), ServiceInventoryConstants.TERMINATED));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
