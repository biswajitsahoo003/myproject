package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserToPartnerLe;

/**
 * 
 * This is the repository class for user_to_partner_le table
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface UserToPartnerLeRepository extends JpaRepository<UserToPartnerLe, Integer>{
	
	Optional<UserToPartnerLe> findByUserIdAndErfCusPartnerLeId(Integer userId, Integer partnerLeId);
	
	List<UserToPartnerLe> findByUser(User user);
	
	List<UserToPartnerLe> findByErfCusPartnerLeIdNotInAndUserId(List<Integer> parterIds, Integer userId);
	
	Page<UserToPartnerLe> findAll(Specification<UserToPartnerLe> specification, Pageable pageable);
	
	/**
	 * 
	 * Delete all partners mappings by userid
	 * @param userid
	 * @return
	 */
	@Query(value = "delete from user_to_partner_le where user_id=:userId", nativeQuery = true)
	@Modifying
	@Transactional
	public int deletePartnerMappings(Integer userId);
	

}
