package com.tcl.dias.servicefulfillment.specification;

import com.tcl.dias.servicefulfillment.entity.entities.Stg0SfdcVendorC;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Thamizhselvi Perumal
 * SfdcVendorSpecification used for the lmProvider specification
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SfdcVendorSpecification {

    private SfdcVendorSpecification() {

    }

    /**
     * search for sfdc vendor (lmprovider)
     *
     * @author Thamizhselvi Perumal
     * @param lmProvider
     * @return
     */

    public static final Specification<Stg0SfdcVendorC> getLmProvider(final String lmProvider) {
        return new Specification<Stg0SfdcVendorC>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Stg0SfdcVendorC> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                final List<Predicate> predicates = new ArrayList<>();

				if (Objects.nonNull(lmProvider) && !lmProvider.isEmpty()) {

					predicates.add(criteriaBuilder.or(
							criteriaBuilder.like(criteriaBuilder.lower(root.get("sfdcProviderNameC")),
									"%" + lmProvider.toLowerCase() + "%"),
							criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
									"%" + lmProvider.toLowerCase() + "%")));

				}
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        };

    }
}
