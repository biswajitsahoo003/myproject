package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.User;

/**
 * Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {

	public User findByUsernameAndStatus(String username, Integer status);

	public User findByEmailIdAndStatus(String userId, Integer status);

	public User findByIdAndStatus(Integer userId, Integer status);

	public List<User> findByCustomerIdAndStatus(Integer customerId, Integer status);

	public List<User> findByIdInAndStatus(List<Integer> userIds, Integer status);
	
	public List<User> findByStatusIn(Integer status);
	
	Page<User> findAll(Specification<User> specification, Pageable pageable);
	
	public List<User> findByIdNotInAndStatusIn(List<Integer> userList,Integer status);
	
	
	@Query(value = "select id as id,first_name as name,username as username from user where id not in(select user_id from user_group_to_user where user_group_id=:id) and status=1", nativeQuery = true)
	List<Map<String, Object>> findUsersNotPresentInGroup(@Param("id") Integer id);
	
	public User findByEmailId(String emailId);
	
	public List<User> findByStatus(Integer status);

	@Query(value = "select ugc.erf_cus_customer_id as erf_partner_id from user u left join user_group_to_user ug on u.id = ug.user_id left join " +
			"user_group_to_customer_le ugc on ug.user_group_id = ugc.user_group_id where u.username =:userName " +
			"group by ugc.erf_cus_customer_id", nativeQuery = true)
	List<Integer> findErfPartnerId(@Param("userName") String userName);

	@Query(value = "select u.* from quote left join engagement_to_opportunity eto on quote.engagement_to_opportunity_id = eto.id " +
			"left join engagement e on eto.engagement_id = e.id left join customer c on c.partner_id = e.partner_id " +
			"left join user u on c.id = u.customer_id where quote.id =:quoteId", nativeQuery = true)
	List<User> findUserByQuoteId(@Param("quoteId") Integer quoteId);

	@Query(value = "SELECT DISTINCT oms_user.* FROM user oms_user INNER JOIN user_to_partner_le outp ON oms_user.id = outp.user_id WHERE outp.erf_cus_partner_le_id = :partnerLeId AND oms_user.partner_id IS NULL AND oms_user.customer_id IS NULL", nativeQuery = true)
	List<User>findPSAMEmailByPartnerLeId(@Param("partnerLeId") Integer partnerLeId);
	
	public List<User> findByIdIn(List<Integer> userIds);

}
