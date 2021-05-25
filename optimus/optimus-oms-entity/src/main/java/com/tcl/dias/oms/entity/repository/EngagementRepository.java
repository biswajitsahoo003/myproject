package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.NativeQueries;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Partner;

/**
 * 
 * This file contains the EngagementRepository.java class. Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Integer> {

	public List<Engagement> findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(Customer customerId,
			Integer erfCusCustomerLeId, MstProductFamily mstProductFamily, Byte status);

	public List<Engagement> findByErfCusCustomerLeIdInAndStatus(List<Integer> erfCusCustomerLeId, Byte status);
	
	@Query(nativeQuery = true, value = NativeQueries.GET_ENGAGEMENT_DETAILS)
	public List<Map<String, Object>> getEngagementDetailsGroupByProductFamily(@Param("customerLeIds") List<Integer> customerLeIds, @Param("status") Byte status);
	
	@Query(nativeQuery = true, value = NativeQueries.GET_ENGAGEMENT_PROD_DETAILS)
	public List<Map<String, Object>> getProductDetailsByEngagement(@Param("customerLeIds") List<Integer> customerLeIds, @Param("status") Byte status);

	/**
	 * Find Partner Engagement Details Group By ProductFamily
	 *
	 * @param partnerLeIds
	 * @param status
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(nativeQuery = true, value = NativeQueries.GET_PARTNER_ENGAGEMENT_DETAILS)
	List<Map<String, Object>> getPartnerEngagementDetailsGroupByProductFamily(List<Integer> partnerLeIds, Byte status);

	/**
	 * Find By Partner And ErfCusPartnerLeId And MstProductFamily And Status
	 *
	 * @param partner
	 * @param partnerLeId
	 * @param mstProductFamily
	 * @param bactive
	 * @return {@link Engagement}
	 */
	Engagement findByPartnerAndErfCusPartnerLeIdAndMstProductFamilyAndStatus(Partner partner, Integer partnerLeId, MstProductFamily mstProductFamily, Byte bactive);

	/**
	 * Find By Partner And Customer Details
	 *
	 * @param partnerId
	 * @param customerId
	 * @param partnerLeId
	 * @param customerLeId
	 * @param productFamilyId
	 * @param status
	 * @return {@link Engagement}
	 */
	@Query(value = "select * from engagement where partner_id =:partnerId  and customer_id=:customerId and erf_cus_partner_le_id=:partnerLeId " +
			"and erf_cus_customer_le_id=:customerLeId and product_family_id=:productFamilyId and status=:status order by id asc", nativeQuery = true)
	List<Engagement> findByPartnerAndCustomerDetails(@Param("partnerId") Integer partnerId,@Param("customerId") Integer customerId,@Param("partnerLeId") Integer partnerLeId,@Param("customerLeId") Integer customerLeId,@Param("productFamilyId") Integer productFamilyId,@Param("status") Byte status);
	
	/**
	 * MEthod to fetch partner engagements with category
	 * @param partnerLeIds
	 * @param status
	 * @return
	 */
	@Query(nativeQuery = true, value = NativeQueries.GET_PARTNER_USER_ENGAGEMENTS)
	List<Map<String, Object>> getPartnerUserEngagementDetails(@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("status") Byte status);
	
	/**
	 * MEthod to fetch user engagements with category
	 * @param customerLeIds
	 * @param status
	 * @return
	 */
	@Query(nativeQuery = true, value = NativeQueries.GET_USER_ENGAGEMENTS)
	public List<Map<String, Object>> getUserEngagementDetails(@Param("customerLeIds") List<Integer> customerLeIds, @Param("status") Byte status);

	public Engagement findById(Engagement engagement);
}

