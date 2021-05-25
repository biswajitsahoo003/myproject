package com.tcl.dias.oms.entity.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
/**
 * 
 * This is the repository class for QuoteIzosdwanSite
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIzosdwanSiteRepository extends JpaRepository<QuoteIzosdwanSite, Integer>{
	
	public QuoteIzosdwanSite findByIdAndStatus(Integer id, byte status);

	public List<QuoteIzosdwanSite> findByProductSolution(ProductSolution productSolution);
	
	@Query(value="select sum(new_port_bandwidth)/2 from quote_izosdwan_sites where product_solutions_id=:solutionId",nativeQuery = true)
	public String getDefaultPortBandwidth(Integer solutionId);
	
	@Query(value="select id from quote_izosdwan_sites where product_solutions_id=:solutionId and new_cpe=:cpeName and izosdwan_site_type=:type",nativeQuery = true)
	public List<BigInteger> getSiteIds(Integer solutionId,String cpeName,String type);
	
	@Query(value="select distinct izosdwan_site_type from quote_izosdwan_sites where product_solutions_id=:id",nativeQuery = true)
	public List<String> getDistinctSiteTypesForSdwan(Integer id);
	
	@Query(value="select distinct new_cpe from quote_izosdwan_sites where product_solutions_id=:id",nativeQuery = true)
	public List<String> getDistinctCpeForSdwan(Integer id);
	
	@Query(value="SELECT CONCAT(MIN(CAST(new_port_bandwidth AS UNSIGNED INTEGER)),'-',"
			+ "MAX(CAST(new_port_bandwidth AS UNSIGNED INTEGER)))AS num FROM quote_izosdwan_sites "
			+ "where product_solutions_id=:solutionId and pri_sec=:type and izosdwan_site_type=:siteType"
			+ " and izosdwan_site_product=:product",nativeQuery = true)
	public String getBandwidthRange(Integer solutionId,String type,String siteType,String product);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndIzosdwanSiteType(ProductSolution productSolution,String type);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndIzosdwanSiteTypeAndErfLocSitebLocationId(ProductSolution productSolution,String type,Integer locationId);
	
	@Query(value="select distinct CAST(old_port_bandwidth as UNSIGNED INTEGER) from quote_izosdwan_sites where CAST(old_port_bandwidth as UNSIGNED INTEGER)<2"
			+ " and CAST(new_port_bandwidth as UNSIGNED INTEGER)=2 and product_solutions_id=:solutionId and izosdwan_site_type=:type",nativeQuery = true)
	public List<BigInteger> getUniqueBwLessThan2Mb(Integer solutionId,String type);
	
	@Query(value="select count(*) from quote_izosdwan_sites where CAST(old_port_bandwidth as UNSIGNED INTEGER)=:oldPortBw and "
			+ "CAST(new_port_bandwidth as UNSIGNED INTEGER)=2 and product_solutions_id=:solutionId and izosdwan_site_type=:type",nativeQuery = true)
	public Integer getCountBasedOnOldBw(Integer oldPortBw,Integer solutionId,String type);
	
	@Query(value="select count(a.id) from quote_izosdwan_sites a join(select CAST(old_port_bandwidth as UNSIGNED INTEGER) "
			+ "as obw, id as bid,CAST(new_port_bandwidth as UNSIGNED INTEGER) nbw from quote_izosdwan_sites where product_solutions_id=:solutionId "
			+ "and old_port_bandwidth is NOT null) b where a.id=b.bid and b.obw<2 and b.nbw>2 and a.izosdwan_site_type=:type",nativeQuery = true)
	public Integer getCountOfAutoUpgradedBwWhichWasUserUpgrade(Integer solutionId,String type);
	
	@Query(value = "select count(a.id) from quote_izosdwan_sites a join(select CAST(old_port_bandwidth as UNSIGNED INTEGER) as obw, "
			+ "id as bid,CAST(new_port_bandwidth as UNSIGNED INTEGER) nbw from quote_izosdwan_sites where product_solutions_id=:solutionId "
			+ "and old_port_bandwidth is NOT null) b where a.id=b.bid and b.obw>=2 and b.nbw=b.obw and a.izosdwan_site_type=:type",nativeQuery = true)
	public Integer getCountOfRetainedBandwidth(Integer solutionId,String type);
	
	@Query(value="select count(a.id) from quote_izosdwan_sites a join(select CAST(old_port_bandwidth as UNSIGNED INTEGER) as obw, "
			+ "id as bid,CAST(new_port_bandwidth as UNSIGNED INTEGER) nbw from quote_izosdwan_sites where product_solutions_id=:solutionId and "
			+ "old_port_bandwidth is NOT null) b where a.id=b.bid and b.obw>=2 and b.nbw!=b.obw and a.izosdwan_site_type=:type",nativeQuery = true)
	
	public Integer getCountOfReplacedBandwidth(Integer solutionId,String type);
	
	
	@Query(value = "SELECT CONCAT(MIN(CAST(new_port_bandwidth AS UNSIGNED INTEGER)),'-',"
			+ "MAX(CAST(new_port_bandwidth AS UNSIGNED INTEGER)))AS num FROM quote_izosdwan_sites "
			+ "where product_solutions_id=:solutionId and pri_sec=:type and izosdwan_site_type=:siteType"
			+ " and izosdwan_site_product=:product", nativeQuery = true)
	public String getBandwidth(Integer solutionId, String type, String siteType, String product);

	@Query(value = "SELECT CONCAT(MIN(CAST(new_lastmile_bandwidth AS UNSIGNED INTEGER)),'-',"
			+ "MAX(CAST(new_lastmile_bandwidth AS UNSIGNED INTEGER)))AS num FROM quote_izosdwan_sites "
			+ "where product_solutions_id=:solutionId and pri_sec=:type and izosdwan_site_type=:siteType"
			+ " and izosdwan_site_product=:product and new_cpe=:cpeModel", nativeQuery = true)
	public String getLocalLoopBandwidthRange(Integer solutionId, String type, String siteType, String product,
			String cpeModel);

	@Query(value = "select distinct new_cpe from quote_izosdwan_sites where product_solutions_id=:id and pri_sec=:type", nativeQuery = true)
	public List<String> getDistinctCpeTypesForSdwan(Integer id,String type);

	@Query(value = "SELECT CONCAT(MIN(CAST(new_port_bandwidth AS UNSIGNED INTEGER)),'-',"
			+ "MAX(CAST(new_port_bandwidth AS UNSIGNED INTEGER)))AS num FROM quote_izosdwan_sites "
			+ "where product_solutions_id=:solutionId  and pri_sec=:type and izosdwan_site_type=:siteType"
			+ " and izosdwan_site_product=:product and new_cpe=:cpeModel", nativeQuery = true)
	public String getPortBandWidth(Integer solutionId, String type, String siteType, String product, String cpeModel);

	@Query(value = "SELECT count(*) As num FROM quote_izosdwan_sites where product_solutions_id=:solutionId and new_cpe=:cpeName and pri_sec=:type and izosdwan_site_type=:siteType", nativeQuery = true)
	public Integer getCpeCount(Integer solutionId, String cpeName,String type,String siteType);

	@Query(value = "SELECT count(*) As num FROM quote_izosdwan_sites where product_solutions_id=:solutionId and izosdwan_site_type=:siteType", nativeQuery = true)
	public Integer getSiteTpeCount(Integer solutionId, String siteType);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatus(ProductSolution productSolution,Byte status);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(ProductSolution productSolution,Byte status,String productName,Integer isFeasibilityCheck);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatusAndIzosdwanSiteProduct(ProductSolution productSolution,Byte status,String productName);
	
	/**
	 * 
	 * Get task triggered sites by QuoteID
	 * @param quoteId
	 * @return
	 */
	@Query(value = "SELECT il.* FROM quote_izosdwan_sites as il where product_solutions_id in(SELECT id FROM product_solutions where quote_le_product_family_id in(SELECT id FROM quote_to_le_product_family where quote_to_le_id in(SELECT id FROM quote_to_le where quote_id in(select id from quote where id=:quoteId)))) and is_task_triggered=1", nativeQuery = true)
	public List<QuoteIzosdwanSite> getTaskTriggeredSites(@Param("quoteId") Integer quoteId);
	
	public List<QuoteIzosdwanSite> findByStatusAndIdIn(Byte status,List<Integer> siteIds);
	
	@Query(value="select id from quote_izosdwan_sites where product_solutions_id=:solutionId and new_cpe=:cpeName and izosdwan_site_type=:type and pri_sec=:priSec",nativeQuery = true)
	public List<BigInteger> getSiteIdsByPriSec(Integer solutionId,String cpeName,String type,String priSec);
	
	public List<QuoteIzosdwanSite> findByIdInAndStatus(List<Integer> ids, byte status);
	
	@Query(value="select erf_loc_siteb_location_id from quote_izosdwan_sites where product_solutions_id=:solutionId",nativeQuery = true)
	
	public List<Integer> getUniqueLocationIds(Integer solutionId);
	public List<QuoteIzosdwanSite> findByErfLocSitebLocationIdAndProductSolution(Integer locationId,ProductSolution productSolution);	
	public List<QuoteIzosdwanSite> findByProductSolutionAndIzosdwanSiteProduct(ProductSolution solution,String siteProduct);
	@Query(value="select service_site_address from quote_izosdwan_sites  where product_solutions_id=:solutionId and izosdwan_site_type=:siteType",nativeQuery = true)
	public List<String>  selectServiceSiteAddressByProductSolutionAndIzosdwanSiteType(Integer solutionId,String siteType);
	
	public List<QuoteIzosdwanSite> findByIzosdwanSiteProductAndProductSolutionAndServiceSiteAddressAndIzosdwanSiteTypeInOrderByCreatedTimeDesc(String type,ProductSolution solution,String address,List<String> siteTypes);

	@Query(value = "select * from quote_izosdwan_sites where product_solutions_id=:id and pri_sec=:prisec and erf_loc_siteb_location_id=:erfLocSitebLocationId", nativeQuery = true)
	public List<QuoteIzosdwanSite> getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSec(
			Integer erfLocSitebLocationId, Integer id, String prisec);
	@Query(value = "select * from quote_izosdwan_sites where product_solutions_id=:id and pri_sec=:prisec and erf_loc_siteb_location_id=:erfLocSitebLocationId and id!=:siteId", nativeQuery = true)
	public List<QuoteIzosdwanSite> getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSecIgnoreCurrentSite(
			Integer erfLocSitebLocationId, Integer id, String prisec,Integer siteId);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatusAndIsFeasiblityCheckRequired(ProductSolution productSolution,Byte status,Integer isFeasibilityCheckRequired);

	@Query(value ="SELECT distinct izosdwan_site_offering FROM quote_izosdwan_sites where product_solutions_id=:solutionId and izosdwan_site_offering is not null",nativeQuery=true)
	public List<String> getDistinctSiteOfferings(Integer solutionId);
	
	@Query(value ="SELECT distinct izosdwan_site_product FROM quote_izosdwan_sites where product_solutions_id=:solutionId and izosdwan_site_offering is not null",nativeQuery=true)
	public List<String> getDistinctSiteProducts(Integer solutionId);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndErfLocSitebLocationId(ProductSolution productSolution,Integer locationId);
	
	@Query(value = "SELECT qill.* FROM quote_izosdwan_sites qill,product_solutions ps where qill.product_solutions_id=ps.id and qill.status=:status and ps.id=:productSolutionId", nativeQuery = true)
	List<QuoteIzosdwanSite> findByProductSolutionIdAndStatus(
			@Param("productSolutionId") Integer productSolutionId,
			@Param("status") byte status);
	QuoteIzosdwanSite findByIdAndMfStatus(Integer id,String status);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequiredAndIsPricingCheckRequired(ProductSolution productSolution,Byte status,String productName,Integer isFeasibilityCheck,Integer isPricingCheck);

	public List<QuoteIzosdwanSite> findByProductSolutionAndFpStatus(ProductSolution productSolution,String fpStatus);
	
	public List<QuoteIzosdwanSite> findByProductSolutionAndStatusAndIsPricingCheckRequired(ProductSolution productSolution,Byte status,Integer isPricingCheck);
	
	public QuoteIzosdwanSite findBySiteCode(String siteCode);
	
	 @Query(value =" select * from quote_izosdwan_sites where product_solutions_id in(\r\n"
	            + "                select id from product_solutions where quote_le_product_family_id in\r\n"
	            + "                        (select id as quote_le_product_family_id from quote_to_le_product_family where quote_to_le_id in\r\n"
	            + "                                        ( select id from quote_to_le where quote_id in\r\n"
	            + "                                                        (select id from quote where id = :quoteId\r\n"
	            + "                                                        ) ) ))", nativeQuery = true)
	 public List<QuoteIzosdwanSite> getSiteCount(@Param("quoteId") Integer quoteId);

}
