package com.tcl.dias.oms.gsc.tiger;

import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_TYPE;

import com.tcl.dias.oms.gsc.tiger.beans.GetInterConnectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Retrofit interface declaration for interacting with Tiger service interconnect API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface TigerServiceInterConnectClient {

	@GET
	Call<GetInterConnectResponse> getAccessServiceInterConnects(@Url String url, @Query("sortBy") String sortBy,
			@Query("sortOrder") String sortOrder, @Query("orgId") String orgId, @Query("serviceType") String serviceType,
			@Query("interconnectType") String interconnectType,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);
}
