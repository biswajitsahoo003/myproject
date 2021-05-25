package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLeBillingInfo;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLeBillingInfoRepository extends JpaRepository<CustomerLeBillingInfo, Integer> {

	List<CustomerLeBillingInfo> findByCustomerLegalEntity_IdAndIsactive(Integer customerleid,String isActive);
	
    @Query(value="select customer_le_id as customerLeId ,fname as firstName, lname as lastName,email_id as emailId, phone_number as phoneNumber,mobile_number as mobileNumber from customer_le_billing_info where customer_le_id in (:customerLeIds) and isactive=:isActive",nativeQuery=true)
	List<Map<String,Object>> findByCustomerLegalEntity_IdInAndIsactive(@Param("customerLeIds") List<Integer> customerleids,@Param("isActive") String isActive);
    
    @Query(value="select bill_acc_no from customer_le_billing_info where customer_le_id in (:customerLeIds) and isactive=:isActive",nativeQuery=true)
	List<Map<String, Object>> findBillingAccountForCustomerLegalEntity_IdInAndIsactive(@Param("customerLeIds") List<Integer> customerleids,@Param("isActive") String isActive);
	
	CustomerLeBillingInfo findByIdAndCustomerLegalEntity_Id(Integer billingInfoId,Integer customerleId);
	
	CustomerLeBillingInfo findByIdAndCustomerLegalEntity(Integer billingInfoId,Integer customerleId);
	
	Optional<CustomerLeBillingInfo> findById(Integer billingInfoId);


}
