package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.MstProductFamily;

import java.util.BitSet;
import java.util.List;

/**
 * This file contains the MstProductRepository.java class.
 * Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface MstProductFamilyRepository extends JpaRepository<MstProductFamily, Integer> {
	
	
	public MstProductFamily findByNameAndStatus(String name,byte status);


	/**
	 * Find All Product By ID
	 *
	 * @param applicableProducts
	 * @return {@link List<MstProductFamily>}
	 */
	List<MstProductFamily> findAllByIdIn(List<Integer> applicableProducts);

	@Query(value = "SELECT olprod.* from quote q\n" +
			"LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id \n" +
			"LEFT JOIN engagement e on eto.engagement_id = e.id \n" +
			"LEFT JOIN mst_product_family olprod on olprod.id = e.product_family_id\n" +
			"where e.partner_id in (:partnerId) and q.status=:status " +
			"group by olprod.id", nativeQuery = true)
	List<MstProductFamily> findAllProductByPartnerIds(@Param("partnerId") List<String> partnerId,@Param("status") Byte status);

	public List<MstProductFamily> findByNameInAndStatus(Iterable<String> name,byte status);
}
