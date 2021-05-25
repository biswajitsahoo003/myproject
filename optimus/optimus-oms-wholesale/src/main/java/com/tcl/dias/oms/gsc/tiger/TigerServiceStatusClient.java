package com.tcl.dias.oms.gsc.tiger;

import com.tcl.dias.oms.gsc.tiger.beans.GetStatusResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_TYPE;

/**
 * Retrofit interface declaration for interacting with Tiger service status API
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public interface TigerServiceStatusClient {

    @GET
    Call<GetStatusResponse> getStatus(@Url String url,
                                      @Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
                                      @Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);

}
