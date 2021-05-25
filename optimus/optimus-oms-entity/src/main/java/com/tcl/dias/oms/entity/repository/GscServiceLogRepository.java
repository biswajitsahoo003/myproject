package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.GSCServiceLog;

/**
 *
 * Repository class for GscServiceLog entity
 *
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GscServiceLogRepository extends JpaRepository<GSCServiceLog, Integer> {

	@Query(value="select log.id as logId,log.reference_id as referencId,log.request_url as requestUrl,log.request_body as requestBody,log.response_body as responseBody,log.status as status"
			+ " from gsc_service_log log"
			+ "  inner join orders o on log.reference_id = o.id"
			+ " where o.order_code = (:orderCode)"
			+ "  order by o.id desc" ,nativeQuery=true)
	List<Map<String, Object>> findServiceDetails(@Param("orderCode") String refId);
	

}
