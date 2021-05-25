package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.CustomerTeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTeamMembersRepository extends JpaRepository<CustomerTeamMembers, Integer> {

    public List<CustomerTeamMembers> findByCustomerIdAndTeamRoleAndIsTeamMember(Integer customerId, String teamRole, String isTeamMember);
    
    public List<CustomerTeamMembers> findByCustomerIdAndTeamRole(Integer customerId, String teamRole);

    public List<CustomerTeamMembers> findByEmail(String emailId);

}
