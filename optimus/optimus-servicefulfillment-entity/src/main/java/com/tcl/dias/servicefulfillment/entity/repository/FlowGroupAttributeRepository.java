package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.FlowGroupAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository class for Flow Group Attribute
 *
 * @author srraghav
 */
@Repository
public interface FlowGroupAttributeRepository extends JpaRepository<FlowGroupAttribute, Integer> {

	FlowGroupAttribute findFirstByScServiceDetailIdAndAttributeNameAndGscFlowGroupOrderByIdDesc(Integer serviceDetailId, String attributeName, GscFlowGroup gscFlowGroup);

	List<FlowGroupAttribute> findByScServiceDetailIdAndAttributeNameAndGscFlowGroup(Integer serviceDetailId, String attributeName, GscFlowGroup gscFlowGroup);
	
	List<FlowGroupAttribute> findByGscFlowGroup(GscFlowGroup flowGroup);

}
