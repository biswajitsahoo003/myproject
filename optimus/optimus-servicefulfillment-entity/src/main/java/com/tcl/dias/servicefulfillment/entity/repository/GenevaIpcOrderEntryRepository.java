package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;

/**
 * 
 * Repository class for GenevaIpcOrderEntry - for entries like Account,Order 
 * 
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface GenevaIpcOrderEntryRepository extends JpaRepository<GenevaIpcOrderEntry, Integer>{
	
	List<GenevaIpcOrderEntry> findByCopfIdAndRequestType(String orderCode,String requestType);
	
	@Query(value = "select * from geneva_ipc_order_entry where source_productSeq=:seq",nativeQuery=true)
	GenevaIpcOrderEntry findBySourceProductSeq(String seq);
	
	@Query(value = "select * from geneva_ipc_order_entry where group_id=:inputGroupId order by 1 desc limit 1",nativeQuery=true)
	GenevaIpcOrderEntry findByGroupId(@Param("inputGroupId") String inputGroupId);
	
	List<GenevaIpcOrderEntry> findBySourceProductSeqIn(List<String> seqs);
	
	
	@Query(value = "select * from geneva_ipc_order_entry where group_id like %:inputGroupId% and REQUEST_TYPE='ACCOUNT' order by 1 desc limit 1",nativeQuery=true)
	GenevaIpcOrderEntry findByInputGroupId(@Param("inputGroupId") String inputGroupId);
}
