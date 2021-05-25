package com.tcl.dias.oms.gsc.tiger;

import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_TYPE;

import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TigerServiceOrderManagementClient {
	@POST
	Call<InternationalOrderManagementResponse> placeInternationalOrder(@Url String url,
			@Body InternationalOrderManagementRequest orderManagementRequest,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);

	@POST
	Call<DomesticOrderManagementResponse> placeDomesticOrder(@Url String url,
			@Body DomesticOrderManagementRequest orderManagementRequest,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);

	@POST
	Call<InternationalOrderManagementResponse> placeInterConnectOrder(@Url String url,
			@Body InterConnectOrderManagementRequest orderManagementRequest,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);
}
