package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;

/**
 * Repository Class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface UserToCustomerLeRepository extends JpaRepository<UserToCustomerLe, Integer>  {
	
	public List<UserToCustomerLe> findByUser(User user);
	
	public Optional<UserToCustomerLe> findByUserAndErfCustomerLeId(User user, Integer erfCustomerLeId);
	
	public Optional<UserToCustomerLe> findByUserIdAndErfCustomerLeId(Integer userId, Integer erfCustomerLeId);
	
	public List<UserToCustomerLe> findByErfCustomerLeIdNotInAndUser(List<Integer> customerLeIds, User user);
	
	Page<UserToCustomerLe> findAll(Specification<UserToCustomerLe> specification, Pageable pageable);
	
	/**
	 * 
	 * Delete all customer mappings by userid
	 * @param userid
	 * @return
	 */
	@Query(value = "delete from user_to_customer_le where user_id=:userId", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteCustomerMappings(Integer userId);
	
	public List<UserToCustomerLe> findByErfCustomerLeIdIn(Set<Integer> erfCustLeIds);
	
	
	
	

}
