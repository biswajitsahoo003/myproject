package com.tcl.dias.oms.gsc.tiger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.net.Proxy.Type.HTTP;

/**
 * Spring configuration for Tiger service API interaction
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
public class TigerServiceClientConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TigerServiceClientConfiguration.class);

	public static final String TIGER_SERVICE_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	public static final String TIGER_SERVICE_NUMBER_INVENTORY = "TIGER:NUMBER_INVENTORY";
	public static final String TIGER_SERVICE_ORGANISATIONS = "TIGER:ORGANISATIONS";
	public static final String TIGER_SERVICE_ORDER_MANAGEMENT = "TIGER:ORDER_MGMT";
	public static final String TIGER_SERVICE_BILLING = "TIGER:SECS_BILLING";
	public static final String TIGER_SERVICE_INTERCONNECT = "TIGER:INTERCONNECT";
	public static final String TIGER_SERVICE_GET_STATUS = "TIGER:GETSTATUS";

	@Value("${tiger.service.url}")
	String tigerServiceBaseUrl;

	@Value("${tiger.service.ssl.disable.verification:false}")
	Boolean disableSslChecks;

	@Value("${tiger.service.read.timeout.seconds:120}")
	Integer readTimeout;

	@Value("${tiger.service.connection.timeout.seconds:30}")
	Integer connectionTimeout;

	@Value("${tiger.service.log.data:false}")
	Boolean logCalls;

	@Value("${tiger.service.numbers.auth.api.id}")
	String numbersAuthApiId;

	@Value("${tiger.service.numbers.auth.api.secret}")
	String numbersAuthApiSecret;

	@Value("${tiger.service.organisations.auth.api.id}")
	String orgAuthApiId;

	@Value("${tiger.service.organisations.auth.api.secret}")
	String orgAuthApiSecret;

	@Value("${tiger.service.ordermanagement.auth.api.id}")
	String orderMgmtApiId;

	@Value("${tiger.service.ordermanagement.auth.api.secret}")
	String orderMgmtApiSecret;

	@Value("${tiger.service.billing.auth.api.id}")
	String billingApiId;

	@Value("${tiger.service.billing.auth.api.secret}")
	String billingApiSecret;

	@Value("${tiger.service.interconnect.auth.api.id}")
	String interConnectApiId;

	@Value("${tiger.service.interconnect.auth.api.secret}")
	String interConnectApiSecret;

	@Value("${tiger.service.xauth.api.id}")
	String xAuthApiId;

	@Value("${tiger.service.xauth.api.secret}")
	String xAuthApiSecret;

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	private String orgServiceAuthHeader;
	private String numberServiceAuthHeader;
	private String orderMgmtServiceAuthHeader;
	private String billingServiceAuthHeader;
	private String interConnectServiceAuthHeader;
	private String getStatusServiceAuthHeader;
	private String xAuthHeader;

	private static String encodeHeader(String apiId, String secret) {
		String headerPlain = String.format("%s:%s", apiId, secret);
		return String.format("Basic %s", Base64.getEncoder().encodeToString(headerPlain.getBytes()));
	}

	@PostConstruct
	public void setupAuthHeaders() {
		numberServiceAuthHeader = encodeHeader(numbersAuthApiId, numbersAuthApiSecret);
		orgServiceAuthHeader = encodeHeader(orgAuthApiId, orgAuthApiSecret);
		orderMgmtServiceAuthHeader = encodeHeader(orderMgmtApiId, orderMgmtApiSecret);
		billingServiceAuthHeader = encodeHeader(billingApiId, billingApiSecret);
		interConnectServiceAuthHeader = encodeHeader(interConnectApiId, interConnectApiSecret);
		getStatusServiceAuthHeader = encodeHeader(orderMgmtApiId, orderMgmtApiSecret);
		xAuthHeader = encodeHeader(xAuthApiId, xAuthApiSecret);
	}

	private OkHttpClient.Builder disableSslChecks(OkHttpClient.Builder builder) {
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };
		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return builder;
	}

	private OkHttpClient createOkHttpClient(boolean sendXAuthHeader, String serviceAuthHeader,
			GscServiceLogInterceptor gscServiceLogInterceptor) {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(logCalls ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BASIC);
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().proxy(setProxy()).addInterceptor(gscServiceLogInterceptor)
				.addInterceptor(chain -> {
					Request.Builder requestBuilder = chain.request().newBuilder()
							.header("Authorization", serviceAuthHeader).header("Accept", "application/json");
					if (sendXAuthHeader) {
						requestBuilder.header("XAuthorization", xAuthHeader);
					}
					return chain.proceed(requestBuilder.build());
				}).addInterceptor(logging).readTimeout(readTimeout, TimeUnit.SECONDS)
				.connectTimeout(connectionTimeout, TimeUnit.SECONDS);
		if (disableSslChecks) {
			clientBuilder = disableSslChecks(clientBuilder);
		}
		return clientBuilder.build();
	}

	// TODO Check in other env
	private Proxy setProxy() {
		InetSocketAddress socketAddress = new InetSocketAddress(proxyHost, proxyPort);
		Proxy proxy = new Proxy(HTTP, socketAddress);
		return proxy;
	}

	private static JacksonConverterFactory createJacksonConverterFactory() {
		SimpleDateFormat df = new SimpleDateFormat(TIGER_SERVICE_DATE_FORMAT);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(df);
		return JacksonConverterFactory.create(objectMapper);
	}

	@Bean(name = "numberInventoryServiceHttpClient")
	public OkHttpClient numberInventoryServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_NUMBER_INVENTORY);
		return createOkHttpClient(false, numberServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "numberInventoryRetrofit")
	public Retrofit numberInventoryRetrofit(@Qualifier("numberInventoryServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceNumberClient numberInventoryService(@Qualifier("numberInventoryRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceNumberClient.class);
	}

	@Bean(name = "orgServiceHttpClient")
	public OkHttpClient orgServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_ORGANISATIONS);
		return createOkHttpClient(false, orgServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "orgServiceRetrofit")
	public Retrofit orgServiceRetrofit(@Qualifier("orgServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceOrgClient orgService(@Qualifier("orgServiceRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceOrgClient.class);
	}

	@Bean(name = "orderMgmtServiceHttpClient")
	public OkHttpClient orderMgmtServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_ORDER_MANAGEMENT);
		return createOkHttpClient(false, orderMgmtServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "orderMgmtServiceRetrofit")
	public Retrofit orderMgmtServiceRetrofit(@Qualifier("orderMgmtServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceOrderManagementClient orderMgmtService(
			@Qualifier("orderMgmtServiceRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceOrderManagementClient.class);
	}

	@Bean(name = "billingServiceHttpClient")
	public OkHttpClient billingServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_BILLING);
		return createOkHttpClient(false, billingServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "billingServiceRetrofit")
	public Retrofit billingServiceRetrofit(@Qualifier("billingServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceBillingClient billingService(@Qualifier("billingServiceRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceBillingClient.class);
	}

	@Bean(name = "interConnectServiceHttpClient")
	public OkHttpClient interConnectServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_INTERCONNECT);
		return createOkHttpClient(false, interConnectServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "interConnectServiceRetrofit")
	public Retrofit interConnectServiceRetrofit(@Qualifier("interConnectServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceInterConnectClient interConnectService(@Qualifier("interConnectServiceRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceInterConnectClient.class);
	}

	@Bean(name = "getStatusServiceHttpClient")
	public OkHttpClient getStatusServiceHttpClient(GscServiceLogInterceptor gscServiceLogInterceptor) {
		gscServiceLogInterceptor.setServiceName(TIGER_SERVICE_GET_STATUS);
		return createOkHttpClient(false, getStatusServiceAuthHeader, gscServiceLogInterceptor);
	}

	@Bean(name = "getStatusServiceRetrofit")
	public Retrofit getStatusServiceRetrofit(@Qualifier("getStatusServiceHttpClient") OkHttpClient okHttpClient) {
		LOGGER.info("TigerServiceBaseUrl:{}",tigerServiceBaseUrl);
		return new Retrofit.Builder().baseUrl(String.format("%s/", tigerServiceBaseUrl)).client(okHttpClient)
				.addConverterFactory(createJacksonConverterFactory()).build();
	}

	@Bean
	public TigerServiceStatusClient getStatusService(@Qualifier("getStatusServiceRetrofit") Retrofit retrofit) {
		return retrofit.create(TigerServiceStatusClient.class);
	}
}
