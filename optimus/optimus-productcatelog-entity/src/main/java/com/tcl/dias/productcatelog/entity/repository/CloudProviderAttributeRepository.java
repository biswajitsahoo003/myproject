package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

/**
 * Repository class for CloudProviderAttribute entity
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CloudProviderAttribute;
import com.tcl.dias.productcatelog.entity.entities.CloudProviderAttributeViewId;

@Repository
public interface CloudProviderAttributeRepository extends JpaRepository<CloudProviderAttribute, CloudProviderAttributeViewId>{

	public List<CloudProviderAttribute> findByCloudProviderNameAndAttributeName(String providerName, String attributeName);
}
