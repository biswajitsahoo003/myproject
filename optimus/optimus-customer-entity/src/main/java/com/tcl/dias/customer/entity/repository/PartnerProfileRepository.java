package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerProfile;

import java.util.List;

/**
 * Repository class for Partner profile
 * 
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Lim
 */
@Repository
public interface PartnerProfileRepository extends JpaRepository<PartnerProfile, Integer> {

    @Query(value="select pp.* from partner p inner join partner_profile pp on pp.id=p.partner_profile_id and pp.is_active='1' where p.id =:partnerID",nativeQuery = true)
    List<PartnerProfile> findProfileByPartnerId(@Param("partnerID") Integer partnerID);
}
