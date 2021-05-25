package com.tcl.dias.oms.activeconfig.specification;

import static com.tcl.dias.common.constants.CommonConstants.GSC;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.oms.beans.QuoteConfiguration;
import com.tcl.dias.oms.beans.QuoteConfigurations;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;

/**
 * 
 * This file contains the ActiveConfigSpecification.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class ActiveConfigSpecification {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveConfigSpecification.class);

	@Autowired
	EntityManager em;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	public QuoteConfigurations getQuoteActionConfigurations(final Set<Integer> customerIds,
			final Set<Integer> customerLeId, final Set<Integer> partnerIds, final Set<String> classifications,
			final Integer status, final String productName, String quoteCode, boolean isTermination,boolean isCustomer, int page,
			int size) {
		QuoteConfigurations quoteConfigurations = new QuoteConfigurations();
		List<QuoteConfiguration> activeQuotes = new ArrayList<>();
		quoteConfigurations.setActiveQuotes(activeQuotes);
		StringBuilder countSelectBuilder = new StringBuilder("SELECT count(1) ");
		StringBuilder selectBuilder = new StringBuilder("SELECT ");
		StringBuilder fromBuilder = new StringBuilder(" FROM ");
		StringBuilder whereBuilder = new StringBuilder(" WHERE ");
		StringBuilder orderBuilder = new StringBuilder(" ORDER BY q.id desc ");
		// Select Builder
		defaultQuoteSelectAttr(selectBuilder);
		// From Builder
		defaultQuoteFromAttr(fromBuilder);
		// Where Builder
		defaultQuoteWhereAttr(whereBuilder, customerIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
				customerLeId.stream().map(String::valueOf).collect(Collectors.joining(",")));

		if (partnerIds != null && !partnerIds.isEmpty()) {
			partnerConditions(fromBuilder, whereBuilder,
					partnerIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
					classifications != null
							? classifications.stream().map(a -> "'" + a + "'").collect(Collectors.joining(","))
							: null,
					true);
		}

		if (StringUtils.isNotBlank(quoteCode)) {
			appendAnd("q.quote_code like '%" + quoteCode + "%'", whereBuilder);
		}

		if (StringUtils.isNotBlank(productName)) {
			appendAnd("mpf.name = '" + productName + "'", whereBuilder);
		}

		if (status != null) {
			appendAnd("q.status = " + status, whereBuilder);
		}else {
			appendAnd("q.status = 1", whereBuilder);
		}

		if (isTermination) {
			appendAnd("qle.quote_type='TERMINATION'", whereBuilder);
		}
		
		if(isCustomer) {
			appendAnd("(q.is_customer_view IS NULL OR (q.is_customer_view = 1 OR q.is_customer_view = 10))",whereBuilder);
		}else {
			appendAnd("(q.is_sales_view IS NULL OR (q.is_sales_view = 1 OR q.is_sales_view = 10))",whereBuilder);
		}

		String query = selectBuilder.append(fromBuilder).append(whereBuilder).append(orderBuilder).toString();
		String countQuery = countSelectBuilder.append(fromBuilder).append(whereBuilder).toString();
		LOGGER.info("Constructed Final Query for Quote is ::: {}", query);
		List<Object[]> quoteActiveConfig = em.createNativeQuery(query).setMaxResults(size).setFirstResult((page-1) * size)
				.getResultList();

		List<BigInteger> countActiveConfig = em.createNativeQuery(countQuery).getResultList();
		LOGGER.info("Total Item {}", countActiveConfig);
		Long total = Long.valueOf((Integer) countActiveConfig.get(0).intValue());
		Integer totalPage = (total.intValue() / size);
		quoteConfigurations.setTotalElements(total);
		quoteConfigurations.setTotalPages(total!=0?totalPage+1:0);
		LOGGER.info("Total Item {}", quoteActiveConfig);
		for (Object[] obj : quoteActiveConfig) {
			LOGGER.info("Inside the config {}",obj);
			QuoteConfiguration quoteConfiguration = new QuoteConfiguration();
			quoteConfiguration.setProductName((String) obj[6]);
			quoteConfiguration.setQuoteCode((String) obj[8]);
			quoteConfiguration.setQuoteId((Integer) obj[0]);
			quoteConfiguration.setQuoteCreatedDate((Timestamp) obj[9]);
			quoteConfiguration.setNsQuote(obj[1] != null ? ((String) obj[1]) : CommonConstants.N);
			quoteConfiguration.setQuoteType((String) obj[2]);
			quoteConfiguration.setQuoteCategory((String) obj[3]);
			quoteConfiguration.setQuoteStage((String) obj[7]);
			quoteConfiguration.setCreatedDate((Timestamp) obj[9]);
			quoteConfiguration.setQuoteOptyId((String) obj[13]);
			Optional<User> user = userRepository.findById((Integer) obj[11]);
			if (user.isPresent()) {
				quoteConfiguration.setQuoteCreatedUserType(user.get().getUserType());
			}
			
			if(obj[4]!=null) {
				quoteConfiguration.setIsMulticircuit(
					(Character.toString((Character) obj[4]).equalsIgnoreCase(CommonConstants.ONE)) ? SFDCConstants.TRUE
							: SFDCConstants.FALSE);
			}else {
				quoteConfiguration.setIsMulticircuit(SFDCConstants.FALSE);
			}

			if (obj[10] != null) {
				Byte isCustomerView = (Byte) obj[10];
				if (isCustomerView.equals(CommonConstants.BACTIVE)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.FULL.toString());
				} else if (isCustomerView.equals(CommonConstants.BDEACTIVATE)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
				} else if (isCustomerView.equals(CommonConstants.BTEN)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
				}
			}

			if (obj[12] != null) {
				Byte isSalesView = (Byte) obj[12];
				if (isSalesView.equals(CommonConstants.BACTIVE)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.FULL.toString());
				} else if (isSalesView.equals(CommonConstants.BDEACTIVATE)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
				} else if (isSalesView.equals(CommonConstants.BTEN)) {
					quoteConfiguration.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
				}
			}

			if (obj[5] != null) {
				if (obj[5] instanceof Byte) {
					Byte isAmended = (Byte) obj[5];
					quoteConfiguration.setIsAmended(isAmended == CommonConstants.BACTIVE ? 1 : 0);
				} else {
					quoteConfiguration.setIsAmended(0);
				}
			}

			if ((quoteConfiguration.getQuoteCode() != null && quoteConfiguration.getQuoteCode().startsWith(GSC))) {
				QuoteLeAttributeValue gscMultiMacdAttribute = quoteLeAttributeValueRepository
						.findByQuoteIDAndMstOmsAttributeName(quoteConfiguration.getQuoteId(),
								LeAttributesConstants.IS_GSC_MULTI_MACD);
				if (Objects.nonNull(gscMultiMacdAttribute)) {
					quoteConfiguration.setIsGscMultiMacd(gscMultiMacdAttribute.getAttributeValue());
				} else {
					quoteConfiguration.setIsGscMultiMacd("No");
				}
			}

			activeQuotes.add(quoteConfiguration);
		}

		return quoteConfigurations;
	}

	public QuoteConfigurations getOrderActionConfigurations(final Set<Integer> customerIds,
			final Set<Integer> customerLeId, final Set<Integer> partnerIds, final Set<String> classifications,
			final Integer status, final String productName, String orderCode, boolean isTermination, int page,
			int size) {
		QuoteConfigurations quoteConfigurations = new QuoteConfigurations();
		List<QuoteConfiguration> activeQuotes = new ArrayList<>();
		quoteConfigurations.setActiveQuotes(activeQuotes);
		StringBuilder countSelectBuilder = new StringBuilder("SELECT count(1) ");
		StringBuilder selectBuilder = new StringBuilder("SELECT ");
		StringBuilder fromBuilder = new StringBuilder(" FROM ");
		StringBuilder whereBuilder = new StringBuilder(" WHERE ");
		StringBuilder orderBuilder = new StringBuilder(" ORDER BY o.id desc ");
		// Select Builder
		defaultOrderSelectAttr(selectBuilder);
		// From Builder
		defaultOrderFromAttr(fromBuilder);
		// Where Builder
		defaultOrderWhereAttr(whereBuilder, customerIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
				customerLeId.stream().map(String::valueOf).collect(Collectors.joining(",")));

		if (partnerIds != null && !partnerIds.isEmpty()) {
			partnerConditions(fromBuilder, whereBuilder,
					partnerIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
					classifications != null
							? classifications.stream().map(a -> "'" + a + "'").collect(Collectors.joining(","))
							: null,
					false);
		}

		if (StringUtils.isNotBlank(orderCode)) {
			appendAnd("o.order_code like '%" + orderCode + "%'", whereBuilder);
		}

		if (StringUtils.isNotBlank(productName)) {
			appendAnd("mpf.name = '" + productName + "'", whereBuilder);
		}

		if (status != null) {
			appendAnd("o.status = " + status, whereBuilder);
		}else {
			appendAnd("o.status = 1", whereBuilder);
		}
		if (isTermination) {
			appendAnd("ole.order_type='TERMINATION'", whereBuilder);
		}

		String query = selectBuilder.append(fromBuilder).append(whereBuilder).append(orderBuilder).toString();
		LOGGER.info("Constructed Final Query for Order is ::: {}", query);
		String countQuery = countSelectBuilder.append(fromBuilder).append(whereBuilder).toString();
		List<BigInteger> countActiveConfig = em.createNativeQuery(countQuery).getResultList();
		LOGGER.info("Total Item {}", countActiveConfig);
		Long total = Long.valueOf((Integer) countActiveConfig.get(0).intValue());
		Integer totalPage = (total.intValue() / size);
		quoteConfigurations.setTotalElements(total);
		quoteConfigurations.setTotalPages(total!=0?totalPage+1:0);
		List<Object[]> orderActiveConfig = em.createNativeQuery(query).setMaxResults(size).setFirstResult(page-1 * size)
				.getResultList();
		for (Object[] obj : orderActiveConfig) {
			QuoteConfiguration quoteConfig = new QuoteConfiguration();
			if(obj[3]!=null) {
				quoteConfig.setIsMulticircuit(
					(Character.toString((Character) obj[3]).equalsIgnoreCase(CommonConstants.ONE)) ? SFDCConstants.TRUE
							: SFDCConstants.FALSE);
			}else {
				quoteConfig.setIsMulticircuit(SFDCConstants.FALSE);
			}
			quoteConfig.setOrderCategory((String) obj[2]);
			quoteConfig.setOrderCode((String) obj[7]);
			quoteConfig.setOrderCreatedDate((Timestamp) obj[8]);
			quoteConfig.setOrderId((Integer) obj[0]);
			quoteConfig.setOrderStage((String) obj[6]);
			quoteConfig.setOrderType((String) obj[1]);
			quoteConfig.setProductName((String) obj[5]);
			quoteConfig.setQuoteId((Integer) obj[10]);
			quoteConfig.setOrderOptyId((String) obj[11]);
			Optional<User> user = userRepository.findById((Integer) obj[9]);
			if (user.isPresent()) {
				quoteConfig.setQuoteCreatedUserType(user.get().getUserType());
			}
			if (obj[4] != null) {
				if (obj[4] instanceof Byte) {
					Byte isAmended = (Byte) obj[4];
					quoteConfig.setIsAmended(isAmended == CommonConstants.BACTIVE ? 1 : 0);
				} else {
					quoteConfig.setIsAmended(0);
				}
			}

			if ((quoteConfig.getOrderCode() != null && quoteConfig.getOrderCode().startsWith(GSC))) {
				QuoteLeAttributeValue gscMultiMacdAttribute = quoteLeAttributeValueRepository
						.findByQuoteIDAndMstOmsAttributeName(quoteConfig.getQuoteId(),
								LeAttributesConstants.IS_GSC_MULTI_MACD);
				if (Objects.nonNull(gscMultiMacdAttribute)) {
					quoteConfig.setIsGscMultiMacd(gscMultiMacdAttribute.getAttributeValue());
				} else {
					quoteConfig.setIsGscMultiMacd("No");
				}
			}

			LOGGER.info("Quote Active Config {}", quoteConfig);
			activeQuotes.add(quoteConfig);
		}
		return quoteConfigurations;
	}

	private void partnerConditions(StringBuilder fromBuilder, StringBuilder whereBuilder, String partnerIds,
			String classifications, boolean isQuote) {
		appendComma("engagement_to_opportunity eto", fromBuilder);
		appendComma("engagement e", fromBuilder);
		if (isQuote) {
			appendAnd("q.engagement_to_opportunity_id = eto.id", whereBuilder);
			appendAnd("qle.classification in (" + classifications + ")", whereBuilder);
		} else {
			appendAnd("o.engagement_to_opportunity_id = eto.id", whereBuilder);
			appendAnd("ole.classification in (" + classifications + ")", whereBuilder);
		}
		appendAnd("eto.engagement_id = e.id", whereBuilder);
		appendAnd("e.partner_id in (" + partnerIds + ")", whereBuilder);

	}

	private void defaultQuoteWhereAttr(StringBuilder whereBuilder, String customerIds, String customerLeIds) {
		whereBuilder.append("q.id = qle.quote_id");
		appendAnd("qle.id = qlef.quote_to_le_id", whereBuilder);
		appendAnd("mpf.id = qlef.product_family_id", whereBuilder);
		appendAnd("q.customer_id IN (" + customerIds + ") AND  (qle.erf_cus_customer_legal_entity_id IN ("
				+ customerLeIds + ") OR qle.erf_cus_customer_legal_entity_id IS NULL)", whereBuilder);
		appendAnd("qle.id = qlef.quote_to_le_id", whereBuilder);
		appendAnd("qle.stage NOT IN ('Order Enrichment','Termination accepted')", whereBuilder);
		appendAnd("(qle.quote_category is null or qle.quote_category <> 'REQUEST_TERMINATION')", whereBuilder);
	
	}

	private void defaultQuoteFromAttr(StringBuilder fromBuilder) {
		fromBuilder.append("quote q");
		appendComma("quote_to_le qle", fromBuilder);
		appendComma("quote_to_le_product_family qlef", fromBuilder);
		appendComma("mst_product_family mpf", fromBuilder);
	}

	private void defaultQuoteSelectAttr(StringBuilder selectBuilder) {
		selectBuilder.append("q.id AS quoteId");
		appendComma("q.ns_quote AS nsQuote", selectBuilder);
		appendComma("qle.quote_type AS quoteType", selectBuilder);
		appendComma("qle.quote_category AS quoteCategory", selectBuilder);
		appendComma("qle.is_multicircuit AS isMulticircuit", selectBuilder);
		appendComma("qle.is_amended AS isAmended", selectBuilder);
		appendComma("mpf.name AS productName", selectBuilder);
		appendComma("qle.stage AS quoteStage", selectBuilder);
		appendComma("q.quote_code AS quoteCode", selectBuilder);
		appendComma("q.created_time AS createdTime", selectBuilder);
		appendComma("q.is_customer_view AS isCustomerView", selectBuilder);
		appendComma("q.created_by AS createdBy", selectBuilder);
		appendComma("q.is_sales_view as isSalesView", selectBuilder);
		appendComma("qle.tps_sfdc_opty_id AS quoteOptyId", selectBuilder);
	}

	private void defaultOrderWhereAttr(StringBuilder whereBuilder, String customerIds, String customerLeIds) {
		whereBuilder.append("o.id = ole.order_id");
		appendAnd("ole.id = olef.order_to_le_id", whereBuilder);
		appendAnd("mpf.id = olef.product_family_id", whereBuilder);
		appendAnd("o.customer_id IN (" + customerIds + ") AND  (ole.erf_cus_customer_legal_entity_id IN ("
				+ customerLeIds + ") OR ole.erf_cus_customer_legal_entity_id IS NULL)", whereBuilder);
		appendAnd("ole.id = olef.order_to_le_id", whereBuilder);
		appendAnd("(ole.order_category is null or ole.order_category <> 'REQUEST_TERMINATION')", whereBuilder);
		appendAnd("ole.stage = 'ORDER_CONFIRMED'", whereBuilder);
	}

	private void defaultOrderFromAttr(StringBuilder fromBuilder) {
		fromBuilder.append("orders o");
		appendComma("order_to_le ole", fromBuilder);
		appendComma("order_to_le_product_family olef", fromBuilder);
		appendComma("mst_product_family mpf", fromBuilder);
	}

	private void defaultOrderSelectAttr(StringBuilder selectBuilder) {
		selectBuilder.append("o.id AS orderId");
		appendComma("ole.order_type AS orderType", selectBuilder);
		appendComma(" ole.order_category AS orderCategory", selectBuilder);
		appendComma("ole.is_multicircuit AS isMulticircuit", selectBuilder);
		appendComma(" ole.is_amended AS isAmended", selectBuilder);
		appendComma("mpf.name AS productName", selectBuilder);
		appendComma("ole.stage AS orderStage", selectBuilder);
		appendComma("o.order_code as orderCode", selectBuilder);
		appendComma("o.created_time AS createdTime", selectBuilder);
		appendComma("o.created_by AS createdBy", selectBuilder);
		appendComma("o.quote_id AS quoteId", selectBuilder);
		appendComma("ole.tps_sfdc_copf_id AS orderOptyId", selectBuilder);
	}

	private void appendComma(String selectAttr, StringBuilder selectBuilder) {
		selectBuilder.append(",").append(selectAttr);
	}

	private void appendAnd(String selectAttr, StringBuilder selectBuilder) {
		selectBuilder.append(" and ").append(selectAttr);
	}
}
