package com.tcl.dias.oms.gsc.tiger;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tcl.dias.oms.gsc.tiger.beans.GetOrderDetail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.tiger.beans.BillingProfile;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectDetail;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.NumberDetails;
import com.tcl.dias.oms.gsc.tiger.beans.NumberListRequestBuilder;
import com.tcl.dias.oms.gsc.tiger.beans.NumberListResponse;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationRequest;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationResponse;
import com.tcl.dias.oms.gsc.tiger.beans.Organisation;
import com.tcl.dias.oms.gsc.tiger.beans.TigerServiceResponse;
import com.tcl.dias.oms.gsc.util.GscConstants;

import io.vavr.control.Try;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Helper class to interact with Tiger service APIs for GSC product
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class TigerServiceHelper {

	public static final Logger LOGGER = LoggerFactory.getLogger(TigerServiceHelper.class);

	@Value("${tiger.get.bulk.active.billing.profile.url}")
	String bulkActiveBillingProfileUrl;

	@Value("${tiger.get.number.inventory.url}")
	String getNumberInventoryUrl;

	@Value("${tiger.reserve.number.inventory.url}")
	String reserveNumberInventoryUrl;

	@Value("${tiger.place.international.order.url}")
	String placeInternationalOrderUrl;

	@Value("${tiger.place.domestic.order.url}")
	String placeDomesticOrderUrl;

	@Value("${tiger.place.interconnect.order.url}")
	String placeInterconnectOrderUrl;

	@Value("${tiger.get.orgs.by.cuid.url}")
	String getOrganisationsForAccCuIdUrl;

	@Value("${tiger.get.interconnect.list.url}")
	String getInterConnectsUrl;

	@Value("${tiger.get.status.url}")
	String getStatusUrl;

	@Autowired
	TigerServiceNumberClient tigerServiceNumberClient;

	@Autowired
	TigerServiceOrgClient tigerServiceOrgClient;

	@Autowired
	TigerServiceOrderManagementClient tigerServiceOrderManagementClient;

	@Autowired
	TigerServiceBillingClient tigerServiceBillingClient;

	@Autowired
	TigerServiceInterConnectClient tigerServiceInterConnectClient;

	@Autowired
	TigerServiceStatusClient tigerServiceStatusClient;

	private static <T extends TigerServiceResponse> Try<T> executeTry(Call<T> call) {
		try {
			Request request = call.request();
			LOGGER.info("Request Header :: {} and Request Body :: {}", request.headers(), request.body());
			Response<T> response = call.execute();
			if (response.code() == HttpStatus.OK.value() || response.code() == HttpStatus.CREATED.value()) {
				T responseBody = response.body();
				if ("200".equals(responseBody.getStatus()) || "201".equals(responseBody.getStatus())) {
					LOGGER.info("Response Body :: {}", response.toString());
					return Try.success(responseBody);
				} else {
					return Try.failure(new TCLException(
							String.format("Error occurred while calling tiger service, message: %s - %s",
									responseBody.getStatus(), responseBody.getMessage())));
				}
			} else {
				String errorBody = Try.of(response.errorBody()::string).getOrElse("");
				return Try.failure(new RuntimeException(
						String.format("Error occurred while calling Tiger service. Code: %s, Message: %s",
								response.code(), errorBody)));
			}

		} catch (Exception e) {
			return Try.failure(new RuntimeException("Error occurred while calling Tiger service", e));
		}
	}

	/**
	 * Retrieve available vanity numbers based on specified service type, origin
	 * country and city
	 * 
	 * @param service
	 * @param threeDigitCountryCode
	 * @param city
	 * @param count
	 * @return
	 */
	public List<NumberDetails> getAvailableNumbersByServiceAndOriginCountryCity(String service,
			String threeDigitCountryCode, String city, Integer count, Integer referenceId, String referenceType,
			String orgId) {
		LOGGER.info("Service :: {} and threeDigitCountryCode :: {} and city :: {} and count :: {} and referenceId :: {} and referenceType :: {} and orgId :: {}",
				service, threeDigitCountryCode, city, count, referenceId, referenceType, orgId);
		LOGGER.info("getNumberInventoryUrl :: {}", getNumberInventoryUrl);
		return Try
				.success(new NumberListRequestBuilder().setServiceType(service).setOriginCity(city)
						.setOriginCountry(threeDigitCountryCode).setStatus("Available").setLimit(count).setOrgId(orgId)
						.create())
				.map(numberListRequest -> tigerServiceNumberClient.getNumberList(getNumberInventoryUrl,
						numberListRequest, referenceId, referenceType))
				.flatMap(TigerServiceHelper::executeTry).map(NumberListResponse::getNumInv).toEither()
				.fold(throwable -> {
					LOGGER.warn("Error occurred while invoking Tiger service number API", throwable);
					return ImmutableList.of();
				}, Function.identity());
	}

	/**
	 * Get organisation billing profile based on provided customer account cuId
	 *
	 * @param cuId
	 * @return
	 */
	public Optional<Organisation> getOrganisationByCuId(String cuId, Integer referenceId, String referenceType) {
		LOGGER.info("CUID :: {} and Reference ID :: {} and ReferenceTYpe :: {}", cuId, referenceId, referenceType);
		LOGGER.info("getOrganisationsForAccCuIdUrl :: {}", getOrganisationsForAccCuIdUrl);
		return Try.success(cuId)
				.map(cuId1 -> tigerServiceOrgClient.getOrganisationsForAccountCuId(getOrganisationsForAccCuIdUrl, cuId1,
						referenceId, referenceType))
				.flatMap(TigerServiceHelper::executeTry)
				.map(organisationListResponse -> Optional.ofNullable(organisationListResponse.getOrganizations())
						.orElse(ImmutableList.of()).stream().findFirst())
				.toEither().fold(throwable -> {
					LOGGER.warn("Error occurred while calling Tiger service", throwable);
					return Optional.empty();
				}, Function.identity());
	}

	public List<InterConnectDetail> getAccessServiceInterConnectDetails(Integer orderId, String orgId) {
		LOGGER.info("getInterConnectsUrl:{}",getInterConnectsUrl);
		return Try.success(orgId)
				.map(cuId1 -> tigerServiceInterConnectClient.getAccessServiceInterConnects(getInterConnectsUrl, "InterconnectID",
						"asc", orgId, null, null, orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry)
				.map(getInterConnectResponse -> Optional.ofNullable(getInterConnectResponse.getInterconnectDetails())
						.orElse(ImmutableList.of()))
				.get();
	}

	/**
	 * To block/reserve vanity numbers for specified organisation and service type
	 * 
	 * @param orgId
	 * @param originCountryCallingCode
	 * @param serviceType
	 * @param tfns
	 * @return
	 */
	public Try<NumberReservationResponse> reserveNumbersForOrganisation(String orgId, String originCountryCallingCode,
			String serviceType, List<GscTfnBean> tfns, Integer referenceId, String referenceType,
			String reservationId) {
		LOGGER.info("ReserveNumberInventoryUrl:: {}", reserveNumberInventoryUrl);
		LOGGER.info("serviceType :: {}", serviceType);
		LOGGER.info("referenceId :: {}", referenceId);
		LOGGER.info("referenceType :: {}", referenceType);
		LOGGER.info("reservationId :: {}", reservationId);
		LOGGER.info("tfns :: {}", tfns);

		return Try.of(() -> {
			NumberReservationRequest request = new NumberReservationRequest();
			request.setOriginCountryCode(originCountryCallingCode);
			request.setServiceType(serviceType);
			request.setE164(tfns.stream().map(GscTfnBean::getNumber).collect(Collectors.toList()));
			if (!StringUtils.isEmpty(reservationId))
				request.setReservationId(reservationId);
			LOGGER.info("NumberReservationRequest :: {}", request);
			return request;
		}).map(request -> tigerServiceNumberClient.reserveNumbers(
				reserveNumberInventoryUrl.replaceAll("\\{.*\\}", orgId), request, referenceId, referenceType))
				.flatMap(TigerServiceHelper::executeTry);
		// .map(NumberReservationResponse::getNumberDetails)
		// .get();
	}

	public BillingProfile getBillingProfileForOrgId(Integer orderId, String orgId) {
		LOGGER.info("BulkActiveBillingProfileUrl:{}",bulkActiveBillingProfileUrl);
		return Try.success(orgId)
				.map(orgId1 -> tigerServiceBillingClient.getBulkActiveBillingProfiles(bulkActiveBillingProfileUrl,
						"profileRelNo", "asc", 1, orgId, orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry)
				.map(bulkActiveBillingProfileResponse -> Optional
						.ofNullable(bulkActiveBillingProfileResponse.getBillingProfiles()).orElse(ImmutableList.of())
						.stream().findFirst().orElseGet(() -> {
							BillingProfile billingProfile = new BillingProfile();
							billingProfile.setProfileRelNo("1");
							return billingProfile;
						}))
				.get();
	}

	public InternationalOrderManagementResponse placeInternationalOrder(
			InternationalOrderManagementRequest orderManagementRequest, Integer orderId) {
		LOGGER.info("PlaceInternationalOrderUrl:{}",placeInternationalOrderUrl);
		return Try.success(orderManagementRequest)
				.map(orderManagementRequest1 -> tigerServiceOrderManagementClient.placeInternationalOrder(
						placeInternationalOrderUrl, orderManagementRequest1, orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry).get();
	}

	public DomesticOrderManagementResponse placeDomesticOrder(DomesticOrderManagementRequest orderManagementRequest,
			Integer orderId) {
		LOGGER.info("PlaceDomesticOrderUrl:{}",placeDomesticOrderUrl);
		return Try.success(orderManagementRequest)
				.map(orderManagementRequest1 -> tigerServiceOrderManagementClient.placeDomesticOrder(
						placeDomesticOrderUrl, orderManagementRequest1, orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry).get();
	}

	public InternationalOrderManagementResponse placeInterConnectOrder(InterConnectOrderManagementRequest orderManagementRequest,
			Integer orderId) {
		LOGGER.info("PlaceInterconnectOrderUrl:{}",placeInterconnectOrderUrl);
		return Try.success(orderManagementRequest)
				.map(orderManagementRequest1 -> tigerServiceOrderManagementClient.placeInterConnectOrder(placeInterconnectOrderUrl,
						orderManagementRequest1, orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry).get();
	}

	public List<GetOrderDetail> getStatusDetails(Integer orderId, String orderCode) {
		LOGGER.info("getStatusUrl:{}", getStatusUrl);
		return Try.success(orderCode)
				.map(orderCode1 -> tigerServiceStatusClient.getStatus(getStatusUrl
						.concat(orderCode1), orderId, GscConstants.REF_TYPE_ORDER))
				.flatMap(TigerServiceHelper::executeTry)
				.map(getStatusResponse -> Optional.ofNullable(getStatusResponse.getOrderDetails())
						.orElse(ImmutableList.of()))
				.get();
	}
}
