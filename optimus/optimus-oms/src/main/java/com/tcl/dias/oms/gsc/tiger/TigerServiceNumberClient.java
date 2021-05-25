package com.tcl.dias.oms.gsc.tiger;

import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_TYPE;

import com.tcl.dias.oms.gsc.tiger.beans.NumberListRequest;
import com.tcl.dias.oms.gsc.tiger.beans.NumberListResponse;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationRequest;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Retrofit interface declaration for interacting with Tiger service number API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface TigerServiceNumberClient {

	@GET
	Call<NumberListResponse> getNumberList(@Url String url, @Query("sortBy") String sortBy,
			@Query("sortOrder") String sortOrder, @Query("offset") Integer offset, @Query("limit") Integer limit,
			@Query("serviceType") String serviceType, @Query("status") String status, @Query("orgId") String orgId,
			@Query("reservationId") String reservationId, @Query("originCountry") String originCountry,
			@Query("originState") String originState, @Query("originCity") String originCity,
			@Query("quantity") Integer quantity, @Query("sequence") String sequence,
			@Query("rangeFrom") String rangeFrom, @Query("rangeTo") String rangeTo, @Query("contains") String contains,
			@Query("e164") String e164, @Query("originCountryCode") String originCountryCode, @Query("NPA") String npa,
			@Query("NXX") String nxx, @Query("number") String number,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);

	default Call<NumberListResponse> getNumberList(String url, NumberListRequest numberListRequest, Integer referenceId,
			String referenceType) {
		return getNumberList(url, numberListRequest.getSortBy(), numberListRequest.getSortOrder(),
				numberListRequest.getOffset(), numberListRequest.getLimit(), numberListRequest.getServiceType(),
				numberListRequest.getStatus(), numberListRequest.getOrgId(), numberListRequest.getReservationId(),
				numberListRequest.getOriginCountry(), numberListRequest.getOriginState(),
				numberListRequest.getOriginCity(), numberListRequest.getQuantity(), numberListRequest.getSequence(),
				numberListRequest.getRangeFrom(), numberListRequest.getRangeTo(), numberListRequest.getContains(),
				numberListRequest.getE164(), numberListRequest.getOriginCountryCode(), numberListRequest.getNpa(),
				numberListRequest.getNxx(), numberListRequest.getNumber(), referenceId, referenceType);
	}

	@PUT
	Call<NumberReservationResponse> reserveNumbers(@Url String url, @Body NumberReservationRequest reservationRequest,
			@Header(HEADER_GSC_LOG_REFERENCE_ID) Integer referenceId,
			@Header(HEADER_GSC_LOG_REFERENCE_TYPE) String referenceType);
}
