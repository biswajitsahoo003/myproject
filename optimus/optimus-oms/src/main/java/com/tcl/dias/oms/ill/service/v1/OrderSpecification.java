package com.tcl.dias.oms.ill.service.v1;

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

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;

/**
 * To Fetch Order Based on given specification
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderSpecification {

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
    public static Specification<Order> getOrders(final String stage, final Integer productFamilyId,
                                                 final Integer legalEntity, final String startDate, final String endDate) {
        return new Specification<Order>() {
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                Join<Order, OrderToLe> orderToLeJoin = root.join("orderToLes", JoinType.INNER);
                Join<OrderToLe, OrderToLeProductFamily> orderToLeOrderToLeProductFamilyJoin = orderToLeJoin
                        .join("orderToLeProductFamilies", JoinType.INNER);
                Join<OrderToLeProductFamily, MstProductFamily> orderToLeProductFamilyMstProductFamilyJoin = orderToLeOrderToLeProductFamilyJoin
                        .join("mstProductFamily", JoinType.INNER);

                // Constructing list of filter parameters
                final List<Predicate> predicates = new ArrayList<>();

                // Adding predicates in case of filter parameter not being null
                if (StringUtils.isNotEmpty(stage)) {
                    predicates.add(builder.equal(orderToLeJoin.get("stage"), stage));
                }

                if (Optional.ofNullable(legalEntity).orElse(0) != 0) {
                    predicates.add(builder.equal(orderToLeJoin.get("erfCusCustomerLegalEntityId"), legalEntity));
                }

                if (StringUtils.isNotEmpty(startDate)) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("createdTime"),
                            DateUtil.convertStringToDate(startDate)));
                }

                if (StringUtils.isNotEmpty(endDate)) {
                    predicates.add(
                            builder.lessThanOrEqualTo(root.get("createdTime"), DateUtil.convertStringToDate(endDate)));
                }

                if (Optional.ofNullable(productFamilyId).orElse(0) != 0) {
                    predicates.add(builder.equal(orderToLeProductFamilyMstProductFamilyJoin.get("id"), productFamilyId));
                }

                return builder.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        };
    }
}