package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Customer;

/**
 * Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	
	public Customer findByIdAndStatus(Integer customer, Byte status);

	public Customer findByErfCusCustomerIdAndStatus(Integer erfCustomerId,Byte status);
	
	public Customer findByErfCusCustomerId(Integer erfCustomerId);
	
	public List<Customer> findByPartnerId(Integer partnerId);
	
	public List<Customer> findByErfCusCustomerIdInAndStatus(List<Integer> erfCustomerIds, Byte status);
	
	public List<Customer> findAll(Specification<Customer> specification);
	
	public List<Customer> findAllById(List<Integer> customerIds);

	/**
	 * Find All Customer Details By Partner
	 *
	 * @param partnerId
	 * @return {@link List<Customer>}
	 */
	@Query(value = "select c.* from customer c inner join quote q on c.id = q.customer_id " +
			"inner join engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id " +
			"inner join engagement e on eto.engagement_id = e.id " +
			"where e.partner_id =:partnerId group by c.id", nativeQuery = true)
	List<Customer> findAllCustomerByPartnerDetails(@Param("partnerId") String partnerId);



	/**
	 * Find All Customer Details By Partner
	 *
	 * @param partnerId
	 * @return {@link List<Customer>}
	 */
	@Query(value = "select c.* from customer c inner join quote q on c.id = q.customer_id " +
			"inner join engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id " +
			"inner join engagement e on eto.engagement_id = e.id " +
			"where e.partner_id in (:partnerId) group by c.id", nativeQuery = true)
	List<Customer> findAllCustomerByPartnerIds(@Param("partnerId") List<String> partnerId);
	/**
	 * Method to get customer details by sending engagement customer (erf customerid - for partner)
	 *
	 * @param engagementId
	 * @return
	 */
	@Query(value = "select c.* from engagement e left join customer c on e.customer_id = c.erf_cus_customer_id where e.id=:engagementId limit 1", nativeQuery = true)
	Customer findCustomerByEngagementId(@Param("engagementId") Integer engagementId);



	@Query(value = "select c.* from customer c where c.customer_name like %:customerSubName% and c.status=1", nativeQuery = true)
	List<Customer> findAllCustomerBySubString(@Param("customerSubName") String customerSubName);


	/**
	 * Find All Customer Details By Partner
	 *
	 * @param partnerId
	 * @return {@link List<Customer>}
	 */
	@Query(value = "select c.* from opportunity o " +
			"inner join engagement_to_opportunity eto on o.id =  eto.opty_id " +
			"inner join engagement e on eto.engagement_id = e.id " +
			"inner join customer c on e.customer_id=c.id "+
			"where e.partner_id in (:partnerId) and o.is_active=:status and o.is_orderlite='Y' group by c.id", nativeQuery = true)
	List<Customer> findAllCustomerForOptyByPartnerIds(@Param("partnerId") List<String> partnerId,@Param("status") String status);

}
