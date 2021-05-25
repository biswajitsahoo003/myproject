package com.tcl.dias.serviceinventory.dao.v1;

import com.google.common.base.Joiner;
import com.tcl.dias.serviceinventory.beans.GscServiceInventoryConfigurationRequestBean;
import com.tcl.dias.serviceinventory.beans.SIConfigurationByLeBean;
import com.tcl.dias.serviceinventory.beans.SICountryBean;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;


/**
 * DAO class to execute custom dynamic queries for service inventory data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public class ServiceInventoryDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryDao.class);

	private static final String GROUP_BY_STRING_FOR_FETCH_CONFIGURATIONS_QUERY = " group by sd.source_country, sd.destination_country, pr.access_type";

	/*private static final String FETCH_CONFIGURATIONS_QUERY = "SELECT sd.source_country AS origin,\n"
			+ "         sd.destination_country AS destination,\n" + "        pr.access_type AS access_type,\n"
			+ "         count(*) AS no_count, o.id as order_id\n"
			+ "FROM si_service_detail sd, si_product_reference pr, si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats\n"
			+ "WHERE sd.product_reference_id=pr.id\n" + "        AND o.id=ci.SI_order_id\n"
			+ "        AND asset.ID=ats.SI_Asset_ID\n" + "        AND ats.si_service_detail_id=sd.id\n"
			+ "        AND ci.erf_cust_le_id IN (:customerLeIds)\n" + "        AND pr.sub_variant = :productName\n"
			+ "        AND %s\n"
			+ "GROUP BY  sd.source_country, sd .destination_country, pr.access_type";*/

	private static final String FETCH_CONFIGURATIONS_QUERY = "select sd.source_country as origin, sd.destination_country as destination, pr.access_type as" +
			" access_type ,count(*) as no_count, o.id as order_id from si_order o inner join si_service_detail sd on " +
			" sd.SI_order_id = o.id inner join si_product_reference pr on sd.product_reference_id = pr.id inner join" +
			" si_asset_to_service ats on sd.id = ats.si_service_detail_id inner join si_asset asset on asset.id = ats.SI_Asset_ID" +
			" where o.erf_cust_le_id IN (:customerLeIds) and" +
			" o.erf_cust_customer_id IN (:customerId) and" +
			" asset.type = :assetType and" +
			" pr.sub_variant =:productName and o.tps_secs_id is not null and" +
			" o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y' ";

	private static final String FETCH_SITE_CONFIGURATIONS_QUERY = "select o.erf_cust_le_name as customer_le_name, o.erf_cust_le_id as customer_le_id, pr.access_type as access_type, count(*) as no_count, " +
			"count(distinct sd.site_address) as site_count " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where pr.sub_variant = :productName and o.tps_secs_id is not null " +
			"and asset.type = :assetType " +
			"and o.erf_cust_customer_id = :customerId " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y' ";

	private static final String GROUP_BY_STRING_FOR_FETCH_SITE_CONFIGURATIONS_QUERY = " group by o.erf_cust_le_id";
	public static final String ALL = "ALL";


	@Autowired
	EntityManager entityManager;

	private SICountryBean toCountry(Object[] row) {
		SICountryBean bean = new SICountryBean();
		bean.setOrigin((String) row[0]);
		bean.setDestination((String) row[1]);
		bean.setAccessType(Optional.ofNullable(row[2]).map(String::valueOf).orElse(""));
		bean.setNumbersCount(Optional.ofNullable(row[3]).map(String::valueOf).orElse("0"));
		bean.setOrderId((Integer) row[4]);
		return bean;
	}

	public List<SICountryBean> findConfigurationsByParams(String productName, String assetType, GscServiceInventoryConfigurationRequestBean request, List<Integer> customerLeIds) {
		List<String> whereFragments = new ArrayList<>();
		whereFragments.add("asset.type = :assetType");
		if(Objects.nonNull(request.getAccessType()) && !ALL.equalsIgnoreCase(request.getAccessType())) {
			whereFragments.add("pr.access_type = :accessType");
		}
		if(Objects.nonNull(request.getNumber())) {
			whereFragments.add("asset.fqdn like :tollFreeNumber");
		}
		if(Objects.nonNull(request.getSecsId()) && !ALL.equalsIgnoreCase(request.getSecsId())) {
			whereFragments.add("o.tps_secs_id like :secsId");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(FETCH_CONFIGURATIONS_QUERY).append(" AND ").append(Joiner.on(" AND ").join(whereFragments)).append(GROUP_BY_STRING_FOR_FETCH_CONFIGURATIONS_QUERY);
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("productName", productName);
		query.setParameter("customerLeIds", customerLeIds);
		query.setParameter("customerId", request.getCustomerId());
		query.setParameter("assetType", assetType);

		if(Objects.nonNull(request.getAccessType()) && !ALL.equalsIgnoreCase(request.getAccessType())) {
			query.setParameter("accessType", request.getAccessType());
		}
		if(Objects.nonNull(request.getNumber())) {
			query.setParameter("tollFreeNumber",  MatchMode.ANYWHERE.toMatchString(request.getNumber()));
		}
		if(Objects.nonNull(request.getSecsId()) && !ALL.equalsIgnoreCase(request.getSecsId())) {
			query.setParameter("secsId", request.getSecsId());
		}

		LOGGER.info("Final sql for configuration search {}", sql);
		List<Object[]> result = query.getResultList();
		return result.stream()
				.map(this::toCountry)
				.collect(Collectors.toList());
	}

	/**
	 * Method to fetch site configurations by params
	 *
	 * @param productName
	 * @param assetType
	 * @param request
	 * @return {@link List<SIConfigurationByLeBean}
	 */
	public List<SIConfigurationByLeBean> findSiteConfigurationsByParams(String productName, String assetType, GscServiceInventoryConfigurationRequestBean request) {
		List<String> whereFragments = new ArrayList<>();
		if(Objects.nonNull(request.getAccessType())  && !ALL.equalsIgnoreCase(request.getAccessType())) {
			whereFragments.add("pr.access_type = :accessType");
		}
		if(Objects.nonNull(request.getNumber())) {
			whereFragments.add("asset.fqdn like :tollFreeNumber");
		}
		if(Objects.nonNull(request.getCustomerLeId()) && !ALL.equalsIgnoreCase(request.getCustomerLeId())) {
			whereFragments.add("o.erf_cust_le_id = :customerLeId");
		}

		if(Objects.nonNull(request.getSecsId()) && !ALL.equalsIgnoreCase(request.getSecsId())) {
			whereFragments.add("o.tps_secs_id  = :secsId");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(FETCH_SITE_CONFIGURATIONS_QUERY).append(" AND ").append(Joiner.on(" AND ").join(whereFragments)).append(GROUP_BY_STRING_FOR_FETCH_SITE_CONFIGURATIONS_QUERY);
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("productName", productName);
		query.setParameter("assetType", assetType);
		query.setParameter("customerId", request.getCustomerId());

		if(Objects.nonNull(request.getAccessType())  && !ALL.equalsIgnoreCase(request.getAccessType())) {
			query.setParameter("accessType", request.getAccessType());
		}
		if(Objects.nonNull(request.getCustomerLeId()) && !ALL.equalsIgnoreCase(request.getCustomerLeId())) {
			query.setParameter("customerLeId", request.getCustomerLeId());
		}
		if(Objects.nonNull(request.getNumber())) {
			query.setParameter("tollFreeNumber", MatchMode.ANYWHERE.toMatchString(request.getNumber()));
		}
		if(Objects.nonNull(request.getSecsId()) && !ALL.equalsIgnoreCase(request.getSecsId())) {
			query.setParameter("secsId", request.getSecsId());
		}

		LOGGER.info("Final sql for site configuration search {}", sql);
		List<Object[]> result = query.getResultList();
		return result.stream().map(this::toCustomerLe).collect(Collectors.toList());
	}

	/**
	 * Method to map the configuration output to bean
	 *
	 * @param row
	 * @return {@link SIConfigurationByLeBean}
	 */
	private SIConfigurationByLeBean toCustomerLe(Object[] row) {
		SIConfigurationByLeBean configuration = new SIConfigurationByLeBean();
		configuration.setCustomerLeName((String) row[0]);
		configuration.setCustomerLeId((Integer) row[1]);
		configuration.setAccessType((String)(row[2]));
		configuration.setNumbersCount(valueOf(row[3]));
		configuration.setSiteCount(valueOf(row[4]));
		return configuration;
	}
}
