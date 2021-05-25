package com.tcl.dias.oms.webex.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.webex.beans.OutboundPriceConversionBean;
import com.tcl.dias.common.webex.beans.WebexEndpointCatalogBean;
import com.tcl.dias.common.webex.beans.WebexPriceConversionBean;
import com.tcl.dias.oms.config.SOAPConnector;
import com.tcl.dias.oms.entity.entities.QuoteUcaasDetail;
import com.tcl.dias.oms.entity.entities.QuoteUcaasSiteDetails;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasSiteDetailsRepository;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.webex.beans.CiscoDealAttributesBean;
import com.tcl.dias.oms.webex.beans.EndpointDetails;
import com.tcl.dias.oms.webex.beans.SiteAddress;
import com.tcl.dias.oms.webex.component.WebexHttpHeaderCallback;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.common.webex.beans.SIInfoSearchBean;
import com.tcl.dias.common.webex.beans.SkuDetailsRequestBean;
import com.tcl.dias.common.webex.beans.SkuDetailsResponseBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.cisco.beans.ApplicationAreaType;
import com.tcl.dias.oms.cisco.beans.B2BServiceB2BAcquireQuotePortType;
import com.tcl.dias.oms.cisco.beans.B2BServiceB2BListQuotePortType;
import com.tcl.dias.oms.cisco.beans.CiscoB2BService;
import com.tcl.dias.oms.cisco.beans.ConfigurationMessagesType;
import com.tcl.dias.oms.cisco.beans.DocumentIDType;
import com.tcl.dias.oms.cisco.beans.ExpressionType;
import com.tcl.dias.oms.cisco.beans.GetQuoteDataAreaType;
import com.tcl.dias.oms.cisco.beans.GetQuoteType;
import com.tcl.dias.oms.cisco.beans.GetType;
import com.tcl.dias.oms.cisco.beans.IdentifierType2;
import com.tcl.dias.oms.cisco.beans.ItemIDType;
import com.tcl.dias.oms.cisco.beans.QuoteHeaderType;
import com.tcl.dias.oms.cisco.beans.QuoteLineType;
import com.tcl.dias.oms.cisco.beans.QuoteType;
import com.tcl.dias.oms.cisco.beans.SenderType;
import com.tcl.dias.oms.cisco.beans.ShowQuoteType;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.factory.impl.OmsSfdcUcaasMapper;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.webex.beans.ExistingGVPNInfo;
import com.tcl.dias.oms.webex.beans.QuoteUcaasBean;
import com.tcl.dias.oms.webex.beans.WebexQuoteDataBean;
import com.tcl.dias.oms.webex.beans.WebexSolutionBean;
import com.tcl.dias.oms.webex.common.UcaasOmsSfdcComponent;
import com.tcl.dias.oms.webex.util.AuthBean;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * WebexQuoteService has the service level implementation of CRUD operations
 * related to WEBEX product.
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebexQuoteService.class);

	@Value("${ccw.username}")
	private String username;

	@Value("${ccw.password}")
	private String password;

	@Value("${ccw.url.listquote}")
	private String listQuote;

	@Value("${ccw.url.acquirequote}")
	private String acquireQuote;

	@Autowired
	UserService userService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	GscQuoteDetailService gscQuoteDetailService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	GscQuoteService gscQuoteService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	UcaasOmsSfdcComponent ucaasOmsSfdcComponent;

	@Autowired
	OmsSfdcUcaasMapper omsSfdcUcaasMapper;

	@Value("${rabbitmq.gvpn.si.info.webex.queue}")
	private String siInfoQueue;

	@Value("${ccw.api.client.id}")
	private String clientId;

	@Value("${ccw.api.client.secret}")
	private String clientSecret;

	@Value("${ccw.url.auth}")
	private String authUrl;

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	@Value("${rabbitmq.product.webex.sku.queue}")
	String skuQueue;

	@Value("${rabbitmq.search.gvpn.si.info.webex.queue}")
	private String siInfoSearchQueue;

	@Value("${rabbitmq.download.gvpn.si.info.webex.queue}")
	private String siInfoDownloadQueue;

	@Value("${rabbitmq.gvpn.si.info.serviceid.webex.queue}")
	private String siInfoServiceId;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	CurrencyConversionRepository currencyConversionRepository;

	@Value("${rabbitmq.product.webex.endpoint.hsn.code}")
	String getEndpntHsnCode;

	@Value("${rabbitmq.product.webex.endpoint.list}")
	String getEndpointList;

	@Autowired
	QuoteUcaasDetailsRepository quoteUcaasDetailsRepository;

	@Value("${rabbitmq.location.webex.details}")
	private String locationDetailQueue;

	@Autowired
	QuoteUcaasSiteDetailsRepository quoteUcaasSiteDetailsRepository;

	@Value("${rabbitmq.location.detail}")
	private String locationQueue;

	@Autowired
	@Qualifier("WebexSoapConnector")
	SOAPConnector soapConnector;

	/**
	 * Create quote bean for Webex.
	 *
	 * @param quoteBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public WebexQuoteDataBean createQuote(WebexQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, GscConstants.QUOTE_NULL_MESSAGE);

		WebexQuoteContext context = createContext(quoteBean);
		saveQuote(context);
		saveQuoteToLe(context);
		saveQuoteUcaasConfiguration(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamily(context);
		createQuoteToLeProductFamily(context);
		saveProductSolutions(context);
		createOpportunityInSfdc(context);
		return context.webexQuoteDataBean;
	}

	/**
	 * saveQuoteUcaas
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext saveQuoteUcaasConfiguration(WebexQuoteContext context) {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setQuoteToLe(context.quoteToLe);
		quoteUcaas.setLicenseProvider(context.webexQuoteDataBean.getLicenseProvider());
		quoteUcaas.setIsConfig((byte) 1);
		quoteUcaas.setName(WebexConstants.CONFIGURATION);
		quoteUcaas.setDescription(WebexConstants.CONFIGURATION_DETAILS);
		quoteUcaas.setPrimaryRegion(context.webexQuoteDataBean.getPrimaryBridge());
		quoteUcaas.setAudioModel(context.webexQuoteDataBean.getAudioGreeting());
		quoteUcaas.setPaymentModel(context.webexQuoteDataBean.getPaymentModel());
		quoteUcaas.setAudioType(context.webexQuoteDataBean.getAudioType());
		quoteUcaas.setCugRequired(context.webexQuoteDataBean.getCugRequired() ? (byte) 1 : (byte) 0);
		quoteUcaas.setGvpnMode(context.webexQuoteDataBean.getGvpnMode());
		quoteUcaas.setDialIn(context.webexQuoteDataBean.getDialIn() ? (byte) 1 : (byte) 0);
		quoteUcaas.setDialOut(context.webexQuoteDataBean.getDialOut() ? (byte) 1 : (byte) 0);
		quoteUcaas.setDialBack(context.webexQuoteDataBean.getDialBack() ? (byte) 1 : (byte) 0);
		quoteUcaas.setIsLns(context.webexQuoteDataBean.getIsLns() ? (byte) 1 : (byte) 0);
		quoteUcaas.setIsItfs(context.webexQuoteDataBean.getIsItfs() ? (byte) 1 : (byte) 0);
		quoteUcaas.setIsOutbound(context.webexQuoteDataBean.getIsOutbound() ? (byte) 1 : (byte) 0);
		quoteUcaas.setStatus((byte) 1);
		quoteUcaas.setQuoteVersion(1);
		quoteUcaas.setCreatedTime(new Date());
		quoteUcaas.setCreatedBy(WebexConstants.UCAAS);
		quoteUcaasRepository.save(quoteUcaas);
		return context;
	}

	/**
	 * saveProductSolutions
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext saveProductSolutions(WebexQuoteContext context) {
		createGscSolutionBean(context);
		createWebexSolutionBean(context);

		// create product solution for GSC offerings
		context.webexQuoteDataBean.getSolutions().forEach(solution -> {
			try {
				createProductSolution(solution, context);
			} catch (TclCommonException e) {
				LOGGER.warn("Exception occured : {}", e.getMessage());
			}
		});

		// create product solution for WEBEX offerings
		try {
			createProductSolutionForWebex(context.webexQuoteDataBean.getWebexSolution(), context);
		} catch (TclCommonException e) {
			LOGGER.warn("Exception occured : {}", e.getMessage());
		}

		return context;
	}

	/**
	 * createProductSolution
	 *
	 * @param solution
	 * @param context
	 * @return
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private GscSolutionBean createProductSolution(GscSolutionBean solution, WebexQuoteContext context)
			throws TclCommonException {

		MstProductOffering masterProductOffering = getProductOffering(context.productFamily, solution.getOfferingName(),
				context.user);
		ProductSolution productSolution = createProductSolution(masterProductOffering, context.quoteToLeProductFamily,
				GscUtils.toJson(solution));
		productSolutionRepository.save(productSolution);
		solution.setSolutionId(productSolution.getId());

		QuoteGsc quoteGsc = quoteGscRepository.save(createGscQuote(context.webexQuoteDataBean, productSolution,
				context.quoteToLe, context.customer, solution.getSolutionCode(), context.user));
		solution.setGscQuotes(ImmutableList.of(GscQuoteBean.fromQuoteGsc(quoteGsc)));
		solution.setProductName(quoteGsc.getProductName());
		solution.setSolutionCode(productSolution.getSolutionCode());
		return solution;
	}

	/**
	 * create Product Solution for webex offering
	 *
	 * @param solution
	 * @param context
	 * @return
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private WebexSolutionBean createProductSolutionForWebex(WebexSolutionBean solution, WebexQuoteContext context)
			throws TclCommonException {

		MstProductOffering masterProductOffering = getProductOffering(context.productFamily, solution.getOfferingName(),
				context.user);
		ProductSolution productSolution = createProductSolution(masterProductOffering, context.quoteToLeProductFamily,
				GscUtils.toJson(solution));
		productSolutionRepository.save(productSolution);
		solution.setSolutionId(productSolution.getId());
		solution.setSolutionCode(productSolution.getSolutionCode());

		// update quoteUcaas with product solution id.
		QuoteUcaas quoteUcaas = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(context.quoteToLe.getId(),
				WebexConstants.CONFIGURATION, GscConstants.STATUS_ACTIVE);
		quoteUcaas.setProductSolutionId(productSolution);
		quoteUcaasRepository.save(quoteUcaas);

		return solution;
	}

	/**
	 * createGscQuote
	 *
	 * @param quoteDataBean
	 * @param productSolution
	 * @param quoteToLe
	 * @param customer
	 * @param solutionCode
	 * @param user
	 * @return
	 */
	private QuoteGsc createGscQuote(WebexQuoteDataBean quoteDataBean, ProductSolution productSolution,
			QuoteToLe quoteToLe, Customer customer, String solutionCode, User user) {
		QuoteGsc quoteGsc = new QuoteGsc();
		quoteGsc.setAccessType(quoteDataBean.getAccessType());
		quoteGsc.setCreatedBy(String.valueOf(user.getId()));
		quoteGsc.setProductSolution(productSolution);
		quoteGsc.setQuoteToLe(quoteToLe);
		quoteGsc.setStatus(GscConstants.STATUS_ACTIVE);
		String productName = solutionCode.replaceAll(quoteDataBean.getAccessType(), "");
		quoteGsc.setProductName(productName);
		quoteGsc.setName(String.format("%s_%s", quoteDataBean.getProductFamilyName(), solutionCode));
		quoteGsc.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		return quoteGsc;
	}

	/**
	 * createProductSolution
	 *
	 * @param mstProductOffering
	 * @param quoteToLeProductFamily
	 * @param productProfileData
	 * @return
	 */
	private ProductSolution createProductSolution(final MstProductOffering mstProductOffering,
			final QuoteToLeProductFamily quoteToLeProductFamily, final String productProfileData) {
		final ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		productSolution.setSolutionCode(Utils.generateUid());
		return productSolution;
	}

	/**
	 * getProductOffering
	 *
	 * @param mstProductFamily
	 * @param productOfferingName
	 * @param user
	 * @return
	 */
	private MstProductOffering getProductOffering(final MstProductFamily mstProductFamily,
			final String productOfferingName, final User user) {
		MstProductOffering productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(
				mstProductFamily, productOfferingName, GscConstants.STATUS_ACTIVE);
		return productOffering;
	}

	/**
	 * create webex solution bean.
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext createWebexSolutionBean(WebexQuoteContext context) {
		context.webexQuoteDataBean.setWebexSolution(createWebex());
		return context;
	}

	/**
	 * create solution bean.
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext createGscSolutionBean(WebexQuoteContext context) {
		List<GscSolutionBean> solutions = new ArrayList<>();
		if (context.webexQuoteDataBean.getProductFamilyName().equals(WebexConstants.UCAAS_FAMILY_NAME)) {

			if (context.webexQuoteDataBean.getPaymentModel().equals(WebexConstants.PAYPER_SEAT)) {

				if (context.webexQuoteDataBean.getCugRequired().equals(false)) {
					switch (context.webexQuoteDataBean.getAudioType()) {

					case WebexConstants.TOLL_DIAL_IN:
						solutions.add(createLnsPstn());
						break;

					case WebexConstants.TOLL_DIAL_IN_DIAL_OUT:
						solutions.add(createLnsPstn());
						solutions.add(createObPstn());
						break;

					case WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT:
						solutions.add(createLnsPstn());
						solutions.add(createObPstn());
						break;
					}
				} else {
					switch (context.webexQuoteDataBean.getAudioType()) {

					case WebexConstants.TOLL_DIAL_IN:
						solutions.add(createLnsMpls());
						break;

					case WebexConstants.TOLL_DIAL_IN_DIAL_OUT:
						solutions.add(createLnsMpls());
						solutions.add(createObMpls());
						break;

					case WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT:
						solutions.add(createLnsMpls());
						solutions.add(createObMpls());
						break;
					}
				}

			} else if (context.webexQuoteDataBean.getPaymentModel().equals(WebexConstants.PAYPER_USE)) {
				if (context.webexQuoteDataBean.getCugRequired().equals(false)) {
					if (context.webexQuoteDataBean.getIsLns())
						solutions.add(createLnsPstn());
					if (context.webexQuoteDataBean.getIsOutbound())
						solutions.add(createObPstn());
					if (context.webexQuoteDataBean.getIsItfs())
						solutions.add(createItfsPstn());
				} else {
					if (context.webexQuoteDataBean.getIsLns())
						solutions.add(createLnsMpls());
					if (context.webexQuoteDataBean.getIsOutbound())
						solutions.add(createObMpls());
					if (context.webexQuoteDataBean.getIsItfs())
						solutions.add(createItfsMpls());
				}
			}

			context.webexQuoteDataBean.setSolutions(solutions);
		}
		return context;
	}

	/**
	 * Create solution bean for WEBEX.
	 *
	 * @return
	 */
	private WebexSolutionBean createWebex() {
		WebexSolutionBean webex = new WebexSolutionBean();
		webex.setOfferingName(WebexConstants.WEBEX);
		webex.setProductName(WebexConstants.WEBEX);
		webex.setSolutionCode(WebexConstants.WEBEX);
		return webex;
	}

	/**
	 * Create solution bean for ITFS on PSTN.
	 *
	 * @return
	 */
	private GscSolutionBean createItfsPstn() {
		GscSolutionBean itfsPstn = new GscSolutionBean();
		itfsPstn.setOfferingName(GscConstants.ITFS.concat(" on ").concat(GscConstants.PSTN));
		itfsPstn.setProductName(GscConstants.ITFS);
		itfsPstn.setAccessType(GscConstants.PSTN);
		itfsPstn.setSolutionCode(GscConstants.ITFS.concat(GscConstants.PSTN));
		return itfsPstn;
	}

	/**
	 * Create solution bean for Global Outbound on PSTN.
	 *
	 * @return
	 */
	private GscSolutionBean createObPstn() {
		GscSolutionBean obPstn = new GscSolutionBean();
		obPstn.setOfferingName(GscConstants.GLOBAL_OUTBOUND.concat(" on ").concat(GscConstants.PSTN));
		obPstn.setProductName(GscConstants.GLOBAL_OUTBOUND);
		obPstn.setAccessType(GscConstants.PSTN);
		obPstn.setSolutionCode(GscConstants.GLOBAL_OUTBOUND.concat(GscConstants.PSTN));
		return obPstn;
	}

	/**
	 * Create solution bean for LNS on PSTN.
	 *
	 * @return
	 */
	private GscSolutionBean createLnsPstn() {
		GscSolutionBean lnsPstn = new GscSolutionBean();
		lnsPstn.setOfferingName(GscConstants.LNS.concat(" on ").concat(GscConstants.PSTN));
		lnsPstn.setProductName(GscConstants.LNS);
		lnsPstn.setAccessType(GscConstants.PSTN);
		lnsPstn.setSolutionCode(GscConstants.LNS.concat(GscConstants.PSTN));
		return lnsPstn;
	}

	/**
	 * Create solution bean for LNS on MPLS.
	 *
	 * @return
	 */
	private GscSolutionBean createLnsMpls() {
		GscSolutionBean lnsMpls = new GscSolutionBean();
		lnsMpls.setOfferingName(GscConstants.LNS.concat(" on ").concat(GscConstants.MPLS));
		lnsMpls.setProductName(GscConstants.LNS);
		lnsMpls.setAccessType(GscConstants.MPLS);
		lnsMpls.setSolutionCode(GscConstants.LNS.concat(GscConstants.MPLS));
		return lnsMpls;
	}

	/**
	 * Create solution bean for ITFS on MPLS.
	 *
	 * @return
	 */
	private GscSolutionBean createItfsMpls() {
		GscSolutionBean itfsMpls = new GscSolutionBean();
		itfsMpls.setOfferingName(GscConstants.ITFS.concat(" on ").concat(GscConstants.MPLS));
		itfsMpls.setProductName(GscConstants.ITFS);
		itfsMpls.setAccessType(GscConstants.MPLS);
		itfsMpls.setSolutionCode(GscConstants.ITFS.concat(GscConstants.MPLS));
		return itfsMpls;
	}

	/**
	 * Create solution bean for Global Outbound on MPLS.
	 *
	 * @return
	 */
	private GscSolutionBean createObMpls() {
		GscSolutionBean obMpls = new GscSolutionBean();
		obMpls.setOfferingName(GscConstants.GLOBAL_OUTBOUND.concat(" on ").concat(GscConstants.MPLS));
		obMpls.setProductName(GscConstants.GLOBAL_OUTBOUND);
		obMpls.setAccessType(GscConstants.MPLS);
		obMpls.setSolutionCode(GscConstants.GLOBAL_OUTBOUND.concat(GscConstants.MPLS));
		return obMpls;
	}

	/**
	 * createQuoteToLeProductFamily
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext createQuoteToLeProductFamily(WebexQuoteContext context) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(context.productFamily);
		quoteToLeProductFamily.setQuoteToLe(context.quoteToLe);
		context.quoteToLeProductFamily = quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		return context;
	}

	/**
	 * populateProductFamily
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext populateProductFamily(WebexQuoteContext context) throws TclCommonException {
		context.productFamily = fetchMstProductFamily(context.webexQuoteDataBean.getProductFamilyName());
		return context;
	}

	/**
	 * fetchMstProductFamily
	 *
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	private MstProductFamily fetchMstProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;
	}

	/**
	 * Construct context bean.
	 *
	 * @param request
	 * @return
	 */
	private WebexQuoteContext createContext(WebexQuoteDataBean request) {
		Integer customerId = request.getCustomerId();
		WebexQuoteContext context = new WebexQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		context.customer = !Objects.isNull(customerId)
				? customerRepository.findByErfCusCustomerIdAndStatus(customerId, (byte) 1)
				: context.user.getCustomer();
		context.webexQuoteDataBean = request;
		context.isAccessTypeChange = false;
		return context;
	}

	/**
	 * Persist default QuoteToLe Attributes.
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext persistDefaultQuoteLeAttributes(WebexQuoteContext context) {
		gscQuoteAttributeService.saveQuoteToLeAttributes(context.quote, context.quoteToLe,
				getDefaultQuoteLeAttributes(context));
		return context;
	}

	/**
	 * Get default quote le attributes.
	 *
	 * @param context
	 * @return
	 */
	private List<GscQuoteProductComponentsAttributeValueBean> getDefaultQuoteLeAttributes(WebexQuoteContext context) {
		Map<String, String> attributes = ImmutableMap.<String, String>builder()
				.put(LeAttributesConstants.CONTACT_NAME, context.user.getFirstName())
				.put(LeAttributesConstants.CONTACT_EMAIL, context.user.getEmailId())
				.put(LeAttributesConstants.CONTACT_ID, context.user.getUsername())
				.put(LeAttributesConstants.CONTACT_NO, Optional.ofNullable(context.user.getContactNo()).orElse(""))
				.put(LeAttributesConstants.DESIGNATION, Optional.ofNullable(context.user.getDesignation()).orElse(""))
				.build();
		return attributes.entrySet().stream().map(entry -> {
			GscQuoteProductComponentsAttributeSimpleValueBean bean = new GscQuoteProductComponentsAttributeSimpleValueBean();
			bean.setAttributeName(entry.getKey());
			bean.setAttributeValue(entry.getValue());
			return bean;
		}).collect(Collectors.toList());
	}

	/**
	 * saveQuote
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext saveQuote(WebexQuoteContext context) {
		LOGGER.info("Inside saveQuote()");
		Customer customer = context.customer;
		User user = context.user;
		context.quote = quoteRepository.save(constructQuote(customer, user, context));
		context.webexQuoteDataBean.setQuoteId(context.quote.getId());
		context.webexQuoteDataBean.setQuoteCode(context.quote.getQuoteCode());
		LOGGER.info("saveQuote() completed {}" + context.quote.getId());
		return context;
	}

	/**
	 * saveQuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext saveQuoteToLe(WebexQuoteContext context) {
		// there is no ACANS & ACDTFS flow for UCAAS hence defaulting paymentCurrency to
		// USD.
		String paymentCurrency = LeAttributesConstants.USD;
		context.quoteToLe = quoteToLeRepository.save(constructQuoteToLe(context, paymentCurrency));
		context.webexQuoteDataBean.setQuoteLeId(context.quoteToLe.getId());
		return context;
	}

	/**
	 * constructQuoteToLe
	 *
	 * @param context
	 * @param paymentCurrency
	 * @return QuoteToLe
	 */
	private QuoteToLe constructQuoteToLe(WebexQuoteContext context, String paymentCurrency) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(context.quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_SERVICES.getConstantCode());
		quoteToLe.setCurrencyCode(paymentCurrency);
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setQuote(context.quote);
		quoteToLe.setQuoteType(context.webexQuoteDataBean.getQuoteType());
		quoteToLe.setQuoteCategory(Objects.isNull(context.webexQuoteDataBean.getQuoteCategory()) ? null
				: context.webexQuoteDataBean.getQuoteCategory());
		quoteToLe.setClassification(context.webexQuoteDataBean.getClassification());
		return quoteToLe;
	}

	/**
	 * constructQuote
	 *
	 * @param customer
	 * @param user
	 * @param context
	 * @return
	 */
	private Quote constructQuote(final Customer customer, final User user, WebexQuoteContext context) {
		Quote quote = new Quote();
		quote.setCustomer(customer);
		quote.setCreatedBy(user.getId());
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setEngagementOptyId(context.webexQuoteDataBean.getEngagementOptyId());
		quote.setQuoteCode(
				null != context.webexQuoteDataBean.getEngagementOptyId() ? context.webexQuoteDataBean.getQuoteCode()
						: Utils.generateRefId(WebexConstants.UCAAS_WEBEX.toUpperCase()));
		return quote;
	}

	/**
	 * Context class for webex quote service
	 */
	private static class WebexQuoteContext {
		User user;
		Customer customer;
		Quote quote;
		QuoteToLe quoteToLe;
		WebexQuoteDataBean webexQuoteDataBean;
		MstProductFamily productFamily;
		QuoteToLeProductFamily quoteToLeProductFamily;
		Integer supplierLegalId;
		Opportunity opportunity;
		Boolean previousCugRequired;
		Boolean isAccessTypeChange;
	}

	/**
	 * Update quote.
	 *
	 * @param quoteBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public WebexQuoteDataBean updateQuote(WebexQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, GscConstants.QUOTE_NULL_MESSAGE);
		WebexQuoteContext context = createContext(quoteBean);
		populateQuote(context);
		populateQuoteToLe(context);
		populatePreviousCugRequired(context);
		populateQuoteUcaasConfiguration(context);
		populateUcaasQuotes(context);
		saveQuoteToLeClassification(context);
		checkAndProcessGvpnSolution(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamily(context);
		populateQuoteToLeProductFamily(context);
		constructGscSolutionBean(context);
		checkAccessTypeAndProcessProductSolutions(context);
		updateProductServiceInSfdc(context);
		return context.webexQuoteDataBean;
	}

	/**
	 * Populate previous value of cug required in Context
	 *
	 * @param context
	 */
	private void populatePreviousCugRequired(WebexQuoteContext context) {
		QuoteUcaas quoteUcaas = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(context.quoteToLe.getId(),
				WebexConstants.CONFIGURATION, GscConstants.STATUS_ACTIVE);
		context.previousCugRequired = quoteUcaas.getCugRequired() == (byte) 1 ? true : false;
	}

	/**
	 * Check whether GVPN is NEW or EXISTING
	 *
	 * @param context
	 */
	private void checkAndProcessGvpnSolution(WebexQuoteContext context) {

		if (Objects.nonNull(context.webexQuoteDataBean.getExistingGVPNInfo()))
			context.quoteToLe.setErfServiceInventoryTpsServiceId(
					context.webexQuoteDataBean.getExistingGVPNInfo().getServiceId());
		else
		    context.quoteToLe.setErfServiceInventoryTpsServiceId(null);

		List<QuoteToLeProductFamily> families = quoteToLeProductFamilyRepository
				.findByQuoteToLe(context.quoteToLe.getId());

		if (WebexConstants.EXISTING.equals(context.webexQuoteDataBean.getGvpnMode()) && families.size() > 1) {
			Integer quoteToLeProductFamilyId = families.stream()
					.filter(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))
					.map(QuoteToLeProductFamily::getId).findFirst().get();
			try {
				gvpnQuoteService.deleteProductFamily(quoteToLeProductFamilyId);
			} catch (TclCommonException e) {
				Throwables.propagate(e);
			}
		}

		quoteToLeRepository.save(context.quoteToLe);
	}

	/**
	 * populate ucaas quote list
	 *
	 * @param context
	 * @throws TclCommonException
	 */
	private void populateUcaasQuotes(WebexQuoteContext context) throws TclCommonException {

		QuoteUcaas ucaasConfigurations = getQuoteUcaasConfiguration(context.webexQuoteDataBean.getQuoteLeId());
		List<QuoteUcaas> ucaasQuotes = getExistingUcaasQuotes(context.quoteToLe);
		List<QuoteUcaasBean> quoteUcaasBeans = new ArrayList<>();

		context.webexQuoteDataBean.getWebexSolution()
				.setDealId(ucaasConfigurations.getDealId() != null ? ucaasConfigurations.getDealId().toString() : null);
		context.webexQuoteDataBean.getWebexSolution()
				.setStatus(ucaasConfigurations.getDeal_status() != null ? ucaasConfigurations.getDeal_status() : null);
		context.webexQuoteDataBean.getWebexSolution().setMessage(
				ucaasConfigurations.getDeal_message() != null ? ucaasConfigurations.getDeal_message() : null);
		context.webexQuoteDataBean.getWebexSolution().setDealAttributes(ucaasConfigurations.getDealAttributes() != null
				? Utils.convertJsonToObject(ucaasConfigurations.getDealAttributes(), CiscoDealAttributesBean.class)
				: null);
		if (ucaasQuotes != null && !ucaasQuotes.isEmpty()) {
			ucaasQuotes.forEach(quoteLines -> {
				quoteUcaasBeans.add(QuoteUcaasBean.toQuoteUcaasBean(quoteLines));
			});
			context.webexQuoteDataBean.getWebexSolution().setUcaasQuotes(quoteUcaasBeans);
		}
	}

	/**
	 * get ucaas quote list
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	private List<QuoteUcaas> getExistingUcaasQuotes(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteUcaas> existingLineItems = quoteUcaasRepository.findByQuoteToLeAndIsConfig(quoteToLe, (byte) 0);
		return existingLineItems;
	}

	/**
	 * constructGscSolutionBean
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext constructGscSolutionBean(WebexQuoteContext context) throws TclCommonException {

		QuoteUcaas quoteUcaasConfig = getQuoteUcaasConfiguration(context.webexQuoteDataBean.getQuoteLeId());
		List<GscSolutionBean> gscSolutions = context.webexQuoteDataBean.getSolutions();

		Predicate<GscSolutionBean> outboundCheck = gsc -> gsc.getOfferingName().contains(GscConstants.GLOBAL_OUTBOUND);

		boolean hasOutbound = false;
		boolean hasLns = false;
		boolean hasItfs = false;

		Predicate<GscSolutionBean> itfsCheck = gsc -> gsc.getOfferingName().contains(GscConstants.ITFS);

		Predicate<GscSolutionBean> lnsCheckForMpls = gsc -> gsc.getOfferingName().contains(WebexConstants.LNS_ON_MPLS);

		Predicate<GscSolutionBean> itfsCheckForMpls = gsc -> gsc.getOfferingName()
				.contains(WebexConstants.ITFS_ON_MPLS);

		Predicate<GscSolutionBean> outBoundCheckForMpls = gsc -> gsc.getOfferingName()
				.contains(WebexConstants.GLOBAL_OUTBOUND_ON_MPLS);

		Predicate<GscSolutionBean> lnsCheckForPstn = gsc -> gsc.getOfferingName().contains(WebexConstants.LNS_ON_PSTN);

		Predicate<GscSolutionBean> outBoundCheckForPstn = gsc -> gsc.getOfferingName()
				.contains(WebexConstants.GLOBAL_OUTBOUND_ON_PSTN);

		Predicate<GscSolutionBean> itfsCheckForPstn = gsc -> gsc.getOfferingName()
				.contains(WebexConstants.ITFS_ON_PSTN);

		if (quoteUcaasConfig.getPaymentModel().equals(WebexConstants.PAYPER_SEAT)) {

			if (quoteUcaasConfig.getCugRequired().equals((byte) 0)) {
				switch (quoteUcaasConfig.getAudioType()) {

				case WebexConstants.TOLL_DIAL_IN:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(outboundCheck);
					gscSolutions.removeIf(lnsCheckForMpls);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForPstn);
					if (!hasLns) {
						gscSolutions.add(createLnsPstn());
					}

					break;

				case WebexConstants.TOLL_DIAL_IN_DIAL_OUT:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(outBoundCheckForMpls);
					gscSolutions.removeIf(lnsCheckForMpls);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForPstn);
					if (!hasLns) {
						gscSolutions.add(createLnsPstn());
					}

					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForPstn);
					if (!hasOutbound) {
						gscSolutions.add(createObPstn());
					}

					break;

				case WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(outBoundCheckForMpls);
					gscSolutions.removeIf(lnsCheckForMpls);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForPstn);
					if (!hasLns) {
						gscSolutions.add(createLnsPstn());
					}

					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForPstn);
					if (!hasOutbound) {
						gscSolutions.add(createObPstn());
					}

					break;
				}
			} else {

				switch (quoteUcaasConfig.getAudioType()) {

				case WebexConstants.TOLL_DIAL_IN:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(outboundCheck);
					gscSolutions.removeIf(lnsCheckForPstn);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForMpls);
					if (!hasLns) {
						gscSolutions.add(createLnsMpls());
					}

					break;

				case WebexConstants.TOLL_DIAL_IN_DIAL_OUT:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(lnsCheckForPstn);
					gscSolutions.removeIf(outBoundCheckForPstn);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForMpls);
					if (!hasLns) {
						gscSolutions.add(createLnsMpls());
					}

					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForMpls);
					if (!hasOutbound) {
						gscSolutions.add(createObMpls());
					}

					break;

				case WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT:

					gscSolutions.removeIf(itfsCheck);
					gscSolutions.removeIf(lnsCheckForPstn);
					gscSolutions.removeIf(outBoundCheckForPstn);

					hasLns = gscSolutions.stream().anyMatch(lnsCheckForMpls);
					if (!hasLns) {
						gscSolutions.add(createLnsMpls());
					}

					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForMpls);
					if (!hasOutbound) {
						gscSolutions.add(createObMpls());
					}

					break;

				}

			}

		} else if (quoteUcaasConfig.getPaymentModel().equals(WebexConstants.PAYPER_USE)) {
			if (quoteUcaasConfig.getCugRequired().equals((byte) 0)) {
				gscSolutions.removeIf(itfsCheckForMpls);
				gscSolutions.removeIf(lnsCheckForMpls);
				gscSolutions.removeIf(outBoundCheckForMpls);

				if (context.webexQuoteDataBean.getIsLns()) {
					hasLns = gscSolutions.stream().anyMatch(lnsCheckForPstn);
					if (!hasLns) {
						gscSolutions.add(createLnsPstn());
					}
				} else {
					gscSolutions.removeIf(lnsCheckForPstn);
				}

				if (context.webexQuoteDataBean.getIsOutbound()) {
					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForPstn);
					if (!hasOutbound) {
						gscSolutions.add(createObPstn());
					}
				} else {
					gscSolutions.removeIf(outBoundCheckForPstn);
				}

				if (context.webexQuoteDataBean.getIsItfs()) {
					hasItfs = gscSolutions.stream().anyMatch(itfsCheckForPstn);
					if (!hasItfs) {
						gscSolutions.add(createItfsPstn());
					}
				} else {
					gscSolutions.removeIf(itfsCheckForPstn);
				}
			} else {
				gscSolutions.removeIf(itfsCheckForPstn);
				gscSolutions.removeIf(lnsCheckForPstn);
				gscSolutions.removeIf(outBoundCheckForPstn);

				if (context.webexQuoteDataBean.getIsLns()) {
					hasLns = gscSolutions.stream().anyMatch(lnsCheckForMpls);
					if (!hasLns) {
						gscSolutions.add(createLnsMpls());
					}
				} else {
					gscSolutions.removeIf(lnsCheckForMpls);
				}

				if (context.webexQuoteDataBean.getIsOutbound()) {
					hasOutbound = gscSolutions.stream().anyMatch(outBoundCheckForMpls);
					if (!hasOutbound) {
						gscSolutions.add(createObMpls());
					}
				} else {
					gscSolutions.removeIf(outBoundCheckForMpls);
				}

				if (context.webexQuoteDataBean.getIsItfs()) {
					hasItfs = gscSolutions.stream().anyMatch(itfsCheckForMpls);
					if (!hasItfs) {
						gscSolutions.add(createItfsMpls());
					}
				} else {
					gscSolutions.removeIf(itfsCheckForMpls);
				}

			}
		}
		return context;
	}

	/**
	 * populateQuoteToLe
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext populateQuoteUcaasConfiguration(WebexQuoteContext context) throws TclCommonException {

		// updating existing configuration with latest value from UI
		QuoteUcaas existingConfiguration = getQuoteUcaasConfiguration(context.webexQuoteDataBean.getQuoteLeId());

		existingConfiguration.setLicenseProvider(context.webexQuoteDataBean.getLicenseProvider());
		existingConfiguration.setPrimaryRegion(context.webexQuoteDataBean.getPrimaryBridge());
		existingConfiguration.setAudioModel(context.webexQuoteDataBean.getAudioGreeting());
		existingConfiguration.setPaymentModel(context.webexQuoteDataBean.getPaymentModel());
		existingConfiguration.setAudioType(context.webexQuoteDataBean.getAudioType());
		existingConfiguration.setCugRequired(context.webexQuoteDataBean.getCugRequired() ? (byte) 1 : (byte) 0);
		existingConfiguration.setGvpnMode(context.webexQuoteDataBean.getGvpnMode());
		existingConfiguration.setDialIn(context.webexQuoteDataBean.getDialIn() ? (byte) 1 : (byte) 0);
		existingConfiguration.setDialOut(context.webexQuoteDataBean.getDialOut() ? (byte) 1 : (byte) 0);
		existingConfiguration.setDialBack(context.webexQuoteDataBean.getDialBack() ? (byte) 1 : (byte) 0);
		existingConfiguration.setIsLns(context.webexQuoteDataBean.getIsLns() ? (byte) 1 : (byte) 0);
		existingConfiguration.setIsItfs(context.webexQuoteDataBean.getIsItfs() ? (byte) 1 : (byte) 0);
		existingConfiguration.setIsOutbound(context.webexQuoteDataBean.getIsOutbound() ? (byte) 1 : (byte) 0);
		quoteUcaasRepository.save(existingConfiguration);

		return context;
	}

	/**
	 * Get quote ucaas
	 *
	 * @param quoteLeId
	 * @return {@link Quote}
	 * @throws TclCommonException
	 */
	public QuoteUcaas getQuoteUcaasConfiguration(Integer quoteLeId) throws TclCommonException {
		Objects.requireNonNull(quoteLeId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		QuoteUcaas existingConfiguration = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(quoteLeId,
				WebexConstants.CONFIGURATION, GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(existingConfiguration)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return existingConfiguration;
	}

	/**
	 * processAccessTypeChange
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext processAccessTypeChange(WebexQuoteContext context) {
		QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;
		List<ProductSolution> savedProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		List<QuoteToLeProductFamily> families = quoteToLeProductFamilyRepository
				.findByQuoteToLe(context.quoteToLe.getId());
		if (families.size() > 1) {
			Integer quoteToLeProductFamilyId = families.stream()
					.filter(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))
					.map(QuoteToLeProductFamily::getId).findFirst().get();
			try {
				gvpnQuoteService.deleteProductFamily(quoteToLeProductFamilyId);
			} catch (TclCommonException e) {
				Throwables.propagate(e);
			}
		}

		if (!context.webexQuoteDataBean.getCugRequired()) {
			context.webexQuoteDataBean.setExistingGVPNInfo(null);
			context.quoteToLe.setErfServiceInventoryTpsServiceId(null);
			quoteToLeRepository.save(context.quoteToLe);
		}

		Set<Integer> solutionIdsToDelete = savedProductSolutions.stream()
				.filter(productSolution -> !productSolution.getProductProfileData().contains(WebexConstants.WEBEX))
				.map(ProductSolution::getId).collect(Collectors.toSet());
		deleteProductSolutions(context, solutionIdsToDelete);
		context.webexQuoteDataBean.getSolutions().forEach(solutionBean -> {
			try {
				createProductSolution(solutionBean, context);
			} catch (TclCommonException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			}
		});
		List<ProductSolution> acquiredProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		context.webexQuoteDataBean.setSolutions(acquiredProductSolutions.stream()
				.filter(productSolution -> !productSolution.getProductProfileData().contains(WebexConstants.WEBEX))
				.map(productSolution -> createProductSolutionBean(productSolution)).collect(Collectors.toList()));
		return context;
	}

	/**
	 * checkAccessTypeAndProcessProductSolutions
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext checkAccessTypeAndProcessProductSolutions(WebexQuoteContext context) {

		// While updating PSTN/MPLS to MPLS/PSTN
		String accessType = context.previousCugRequired ? GscConstants.MPLS : GscConstants.PSTN;
		if (accessType.equals(context.webexQuoteDataBean.getAccessType())) {
			return processProductSolutions(context);
		} else {
			context.isAccessTypeChange = true;
			return processAccessTypeChange(context);
		}
	}

	/**
	 * processProductSolutions
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext processProductSolutions(WebexQuoteContext context) {

		QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;

		List<ProductSolution> savedProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		List<GscSolutionBean> requestSolutions = context.webexQuoteDataBean.getSolutions();

		// skip webex product solution from deletion as it is a mandatory component.
		Predicate<ProductSolution> webexCheck = ps -> !ps.getProductProfileData().contains(WebexConstants.WEBEX);
		Set<Integer> savedSolutionIds = savedProductSolutions.stream().filter(webexCheck).map(ProductSolution::getId)
				.collect(Collectors.toSet());

		Set<Integer> requestSolutionIds = requestSolutions.stream().map(GscSolutionBean::getSolutionId)
				.filter(Objects::nonNull).collect(Collectors.toSet());
		Set<Integer> solutionIdsToDelete = Sets.difference(savedSolutionIds, requestSolutionIds);

		List<GscSolutionBean> newSolutions = requestSolutions.stream()
				.filter(solution -> Objects.isNull(solution.getSolutionId())).collect(Collectors.toList());
		// delete all unwanted solutions
		deleteProductSolutions(context, solutionIdsToDelete);
		// create new solutions
		newSolutions.forEach(solutionBean -> {
			try {
				createProductSolution(solutionBean, context);
			} catch (TclCommonException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			}
		});
		// skip webex product solution from being populated in gscQuotes
		context.webexQuoteDataBean
				.setSolutions(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily).stream()
						.filter(webexCheck).map(this::createProductSolutionBean).collect(Collectors.toList()));
		return context;
	}

	/**
	 * Populate GSC product components
	 *
	 * @param configurationBean
	 * @param quoteId
	 * @param quoteGscId
	 * @return
	 */
	private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
			Integer quoteId, Integer quoteGscId) {
		List<GscProductComponentBean> components = gscQuoteAttributeService
				.getProductComponentAttributes(quoteId, quoteGscId, configurationBean.getId()).get();
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	/**
	 * deleteProductSolutions
	 *
	 * @param productSolutionIds
	 */

	private void deleteProductSolutions(WebexQuoteContext context, Set<Integer> productSolutionIds) {
		List<ProductSolution> productSolutions = productSolutionRepository.findAllById(productSolutionIds);
		productSolutions.forEach(productSolution -> {
			List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
					GscConstants.STATUS_ACTIVE);

			quoteGscs.forEach(gscQuoteDetailService::deleteQuoteGscDetailsByQuoteGsc);

			quoteGscRepository.deleteByProductSolution(productSolution);

			productSolutionRepository.delete(productSolution);
		});

		if (!CollectionUtils.isEmpty(productSolutions)) {
			ucaasOmsSfdcComponent.getOmsSfdcService().processDeleteProduct(context.quoteToLe,
					productSolutions.stream().findFirst().get());
		}
	}

	/**
	 * populateQuoteToLeProductFamily
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext populateQuoteToLeProductFamily(WebexQuoteContext context) {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(context.quoteToLe, context.productFamily);
		context.quoteToLeProductFamily = quoteToLeProductFamily;
		return context;
	}

	/**
	 * Save classification to quote to le.
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext saveQuoteToLeClassification(WebexQuoteContext context) {
		context.quoteToLe.setClassification(context.webexQuoteDataBean.getClassification());
		context.quoteToLe = quoteToLeRepository.save(context.quoteToLe);
		return context;
	}

	/**
	 * populateQuoteToLe
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext populateQuoteToLe(WebexQuoteContext context) throws TclCommonException {
		context.quoteToLe = getQuoteToLe(context.webexQuoteDataBean.getQuoteLeId());
		return context;
	}

	/**
	 * Get quote to le by ID
	 *
	 * @param quoteToLeId
	 * @return {@link QuoteToLe}
	 * @throws TclCommonException
	 */
	public QuoteToLe getQuoteToLe(Integer quoteToLeId) throws TclCommonException {
		Objects.requireNonNull(quoteToLeId, GscConstants.QUOTE_LE_ID_NULL_MESSAGE);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (Objects.isNull(quoteToLe.get())) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteToLe.get();
	}

	/**
	 * populateQuote
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteContext populateQuote(WebexQuoteContext context) throws TclCommonException {
		context.quote = getQuote(context.webexQuoteDataBean.getQuoteId());
		return context;
	}

	/**
	 * Get quote by id
	 *
	 * @param quoteId
	 * @return {@link Quote}
	 * @throws TclCommonException
	 */
	public Quote getQuote(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
	}

	/**
	 * getQuoteData
	 *
	 * @param quoteId
	 * @return WebexQuoteDataBean
	 * @throws TclCommonException
	 */
	private WebexQuoteDataBean getQuoteData(Integer quoteId) throws TclCommonException {
		LOGGER.info("Inside getQuoteData()");
		WebexQuoteDataBean webexQuoteDataBean = new WebexQuoteDataBean();
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		if (Objects.nonNull(quote)) {
			webexQuoteDataBean.setQuoteId(quoteId);
			webexQuoteDataBean.setQuoteCode(quote.getQuoteCode());
			webexQuoteDataBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		} else {
			webexQuoteDataBean.setEngagementOptyId(quote.getEngagementOptyId());
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		final List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		if (Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()) {
			quoteToLes.forEach(quoteToLe -> {
				webexQuoteDataBean.setQuoteLeId(quoteToLe.getId());
				webexQuoteDataBean.setQuoteType(quoteToLe.getQuoteType());
				webexQuoteDataBean.setClassification(quoteToLe.getClassification());
			});
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		webexQuoteDataBean.setLegalEntities(getLegalEntities(quote));
		return webexQuoteDataBean;
	}

	/**
	 * getLegalEntities
	 *
	 * @param quote
	 * @return GscQuoteToLeBean
	 */

	private List<GscQuoteToLeBean> getLegalEntities(Quote quote) throws TclCommonException {
		return Optional.ofNullable(quoteToLeRepository.findByQuote(quote)).orElse(ImmutableList.of()).stream()
				.map(GscQuoteToLeBean::fromQuoteToLe).collect(Collectors.toList());
	}

	/**
	 * getProductFamily
	 *
	 * @param webexQuoteDataBean
	 * @return WebexQuoteDataBean
	 * @throws TclCommonException
	 */
	private WebexQuoteDataBean getProductFamily(WebexQuoteDataBean webexQuoteDataBean) throws TclCommonException {
		LOGGER.info("Inside getProductFamily()");
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeRepository.findById(webexQuoteDataBean.getQuoteLeId())
				.map(quoteToLe -> {
					MstProductFamily mstProductFamily = mstProductFamilyRepository
							.findByNameAndStatus(WebexConstants.UCAAS, GscConstants.STATUS_ACTIVE);
					return quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(quoteToLe,
							mstProductFamily);
				}).orElse(null);
		if (Objects.nonNull(quoteToLeProductFamily)) {
			MstProductFamily mstProductFamily = quoteToLeProductFamily.getMstProductFamily();
			webexQuoteDataBean.setProductFamilyName(mstProductFamily.getName());
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);

//			predicate for Eliminating webex solutions
			Predicate<ProductSolution> webexPsUnAvailabilityCheck = ps -> !ps.getProductProfileData()
					.contains(WebexConstants.WEBEX);
//			Predicate for getting only Webex solutions
			Predicate<ProductSolution> webexPsAvailabilityCheck = ps -> ps.getProductProfileData()
					.contains(WebexConstants.WEBEX);
			webexQuoteDataBean.setSolutions(productSolutions.stream().filter(webexPsUnAvailabilityCheck)
					.map(this::createProductSolutionBean).collect(Collectors.toList()));
			webexQuoteDataBean.setWebexSolution(
					productSolutions.stream().filter(webexPsAvailabilityCheck).findAny().map(productSolution -> {
						try {
							return createWebexSolutionBean(productSolution, webexQuoteDataBean);
						} catch (TclCommonException e) {
							LOGGER.info("Error : {}", e.getMessage());
						}
						return null;
					}).get());
			webexQuoteDataBean.getWebexSolution()
					.setContractPeriod(quoteToLeProductFamily.getQuoteToLe().getTermInMonths());
			return webexQuoteDataBean;
		} else {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * createProductSolutionBeanForWebex
	 *
	 * @param productSolution
	 * @param webexQuoteDataBean
	 * @return WebexSolutionBean
	 */
	private WebexSolutionBean createWebexSolutionBean(ProductSolution productSolution,
			WebexQuoteDataBean webexQuoteDataBean) throws TclCommonException {

		WebexSolutionBean webexSolutionBean = new WebexSolutionBean();
		webexSolutionBean.setSolutionId(productSolution.getId());
		webexSolutionBean.setSolutionCode(productSolution.getSolutionCode());
		webexSolutionBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
		webexSolutionBean.setProductName(productSolution.getMstProductOffering().getProductName());
		List<QuoteUcaas> quoteUcaasList = quoteUcaasRepository.findByQuoteToLeId(webexQuoteDataBean.getQuoteLeId());
		QuoteUcaas ucaasConfiguration = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(
				webexQuoteDataBean.getQuoteLeId(), WebexConstants.CONFIGURATION, GscConstants.STATUS_ACTIVE);
		List<QuoteUcaasBean> ucaasQuoteBeanList = new ArrayList<>();
		quoteUcaasList.forEach(quoteUcaas -> {
			Boolean isconfig = quoteUcaas.getIsConfig() == ((byte) 1) ? true : false;
			if (!isconfig) {
				QuoteUcaasBean quoteUcaasBean = QuoteUcaasBean.toQuoteUcaasBean(quoteUcaas);
				try {
					ucaasQuoteBeanList.add(populateSiteAddress(quoteUcaasBean));
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		});
		webexSolutionBean
				.setDealId(ucaasConfiguration.getDealId() != null ? ucaasConfiguration.getDealId().toString() : null);
		webexSolutionBean
				.setMessage(ucaasConfiguration.getDeal_message() != null ? ucaasConfiguration.getDeal_message() : null);
		webexSolutionBean
				.setStatus(ucaasConfiguration.getDeal_status() != null ? ucaasConfiguration.getDeal_status() : null);
		webexSolutionBean.setUcaasQuotes(ucaasQuoteBeanList);
		webexSolutionBean.setDealAttributes(Objects.nonNull(ucaasConfiguration.getDealAttributes())
				? Utils.convertJsonToObject(ucaasConfiguration.getDealAttributes(), CiscoDealAttributesBean.class)
				: null);
		return webexSolutionBean;
	}

	/**
	 * createProductSolutionBean
	 *
	 * @param productSolution
	 * @return GscSolutionBean
	 */
	public GscSolutionBean createProductSolutionBean(ProductSolution productSolution) {
		GscSolutionBean gscSolutionBean = new GscSolutionBean();
		gscSolutionBean.setSolutionId(productSolution.getId());
		gscSolutionBean.setSolutionCode(productSolution.getSolutionCode());
		gscSolutionBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
				GscConstants.STATUS_ACTIVE);
		quoteGscs.stream().findFirst().ifPresent(quoteGsc -> {
			gscSolutionBean.setAccessType(quoteGsc.getAccessType());
			gscSolutionBean.setProductName(quoteGsc.getProductName());
		});
		gscSolutionBean.setGscQuotes(quoteGscs.stream().map(this::fromQuoteGsc).collect(Collectors.toList()));
		return gscSolutionBean;
	}

	/**
	 * fromQuoteGsc
	 *
	 * @param quoteGsc
	 * @return GscQuoteBean
	 */
	public GscQuoteBean fromQuoteGsc(QuoteGsc quoteGsc) {
		Objects.requireNonNull(quoteGsc, GscConstants.QUOTE_GSC_NULL_MESSAGE);
		GscQuoteBean gscQuoteBean = GscQuoteBean.fromQuoteGsc(quoteGsc);
		gscQuoteBean.setConfigurations(getGscQuoteConfigurationBean(quoteGsc));
		return gscQuoteBean;
	}

	/**
	 * getGscQuoteConfigurationBean
	 *
	 * @param quoteGsc
	 * @return List<GscQuoteConfiguration>
	 */
	private List<GscQuoteConfigurationBean> getGscQuoteConfigurationBean(QuoteGsc quoteGsc) {
		return quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail)
				.map(gscQuoteConfigurationBean -> populateProductComponents(gscQuoteConfigurationBean,
						quoteGsc.getQuoteToLe().getQuote().getId(), quoteGsc.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * pickRatePerMinuteFromAttributes
	 *
	 * @param gscQuoteConfigurationBean
	 * @param gscQuoteProductComponentsAttributeValueBean
	 * @return
	 */
	private void pickRatePerMinuteFromAttributes(GscQuoteConfigurationBean gscQuoteConfigurationBean,
			GscQuoteProductComponentsAttributeValueBean gscQuoteProductComponentsAttributeValueBean) {
		if ((GscConstants.RATE_PER_MINUTE_FIXED)
				.equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
			if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
				if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
					gscQuoteConfigurationBean.setRatePerMinFixed(
							Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
				}
			}
		}
		if ((GscConstants.RATE_PER_MINUTE_SPECIAL)
				.equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
			if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
				if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
					gscQuoteConfigurationBean.setRatePerMinSpecial(
							Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
				}
			}
		}
		if ((GscConstants.RATE_PER_MINUTE_MOBILE)
				.equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
			if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
				if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
					gscQuoteConfigurationBean.setRatePerMinMobile(
							Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
				}
			}
		}
	}

	/**
	 * isNumeric
	 *
	 * @param pricingRate
	 * @return boolean
	 */
	private static boolean isNumeric(String pricingRate) {
		try {
			Double.parseDouble(pricingRate);
			Integer.parseInt(pricingRate);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * setRatesForConfiguration
	 *
	 * @param webexQuoteDataBean
	 * @return
	 */
	private WebexQuoteDataBean setRatesForConfigurations(WebexQuoteDataBean webexQuoteDataBean) {
		LOGGER.info("Inside setRatesForConfigurations()");
		webexQuoteDataBean.getSolutions().forEach(gscSolutionBean -> gscSolutionBean.getGscQuotes()
				.forEach(gscQuoteBean -> gscQuoteBean.getConfigurations()
						.forEach(gscQuoteConfigurationBean -> gscQuoteConfigurationBean.getProductComponents().stream()
								.findFirst()
								.ifPresent(gscProductComponentBean -> gscProductComponentBean.getAttributes().forEach(
										gscQuoteProductComponentsAttributeValueBean -> pickRatePerMinuteFromAttributes(
												gscQuoteConfigurationBean,
												gscQuoteProductComponentsAttributeValueBean))))));
		return webexQuoteDataBean;
	}

	/**
	 * getWebexAttributes
	 *
	 * @param webexQuoteDataBean
	 * @return WebexQuoteDataBean
	 * @throws TclCommonException
	 */
	private WebexQuoteDataBean getWebexConfiguration(WebexQuoteDataBean webexQuoteDataBean) {
		LOGGER.info("Inside getWebexAttributes()");
		QuoteUcaas quoteUcaas = quoteUcaasRepository.findByQuoteToLeId(webexQuoteDataBean.getQuoteLeId()).stream()
				.findAny().get();
		webexQuoteDataBean.setLicenseProvider(quoteUcaas.getLicenseProvider());
		webexQuoteDataBean.setAudioGreeting(quoteUcaas.getAudioModel());
		webexQuoteDataBean.setAudioType(quoteUcaas.getAudioType());
		webexQuoteDataBean.setPaymentModel(quoteUcaas.getPaymentModel());
		webexQuoteDataBean.setGvpnMode(quoteUcaas.getGvpnMode());
		webexQuoteDataBean.setDialIn(quoteUcaas.getDialIn() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setCugRequired(quoteUcaas.getCugRequired() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setDialOut(quoteUcaas.getDialOut() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setDialBack(quoteUcaas.getDialBack() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setPrimaryBridge(quoteUcaas.getPrimaryRegion());
		webexQuoteDataBean.setIsLns(quoteUcaas.getIsLns() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setIsItfs(quoteUcaas.getIsItfs() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setIsOutbound(quoteUcaas.getIsOutbound() == ((byte) 1) ? true : false);
		webexQuoteDataBean.setGvpnMode(quoteUcaas.getGvpnMode());
		webexQuoteDataBean.setLicenseQuantity(quoteUcaas.getQuantity());
		webexQuoteDataBean
				.setAccessType(quoteUcaas.getCugRequired() == (byte) 1 ? GscConstants.MPLS : GscConstants.PSTN);
		return webexQuoteDataBean;
	}

	/**
	 * getWebexQuoteById
	 *
	 * @param quoteId
	 * @return WebexQuoteDataBean
	 * @throws TclCommonException
	 */
	public WebexQuoteDataBean getWebexQuoteById(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		LOGGER.info("QuoteId {}", quoteId);
		WebexQuoteDataBean webexQuoteDataBean = getQuoteData(quoteId);
		getProductFamily(webexQuoteDataBean);
		getWebexConfiguration(webexQuoteDataBean);
		getGVPNServiceInventoryDetails(webexQuoteDataBean);
		setRatesForConfigurations(webexQuoteDataBean);
		LOGGER.info("getWebexQuoteById response {}", Utils.convertObjectToJson(webexQuoteDataBean));
		return webexQuoteDataBean;
	}

	/**
	 * Get Existing GVPN details from service inventory
	 *
	 * @param webexQuoteDataBean
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuoteDataBean getGVPNServiceInventoryDetails(WebexQuoteDataBean webexQuoteDataBean)
			throws TclCommonException {

		QuoteToLe quoteToLe = getQuoteToLe(webexQuoteDataBean.getQuoteLeId());

		if (Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId())) {
			ExistingGVPNInfo existingGVPNInfo = new ExistingGVPNInfo();
			String response = (String) mqUtils.sendAndReceive(siInfoServiceId,
					quoteToLe.getErfServiceInventoryTpsServiceId());
			SIServiceInfoGVPNBean siExistingGVPNBean = Utils.convertJsonToObject(response, SIServiceInfoGVPNBean.class);
			existingGVPNInfo.setServiceId(siExistingGVPNBean.getServiceId());
			existingGVPNInfo.setAliasName(siExistingGVPNBean.getSiteAlias());
			existingGVPNInfo.setBandwidth(siExistingGVPNBean.getBandwidth());
			existingGVPNInfo.setSiteAddress(siExistingGVPNBean.getCustomerSiteAddress());
			existingGVPNInfo.setLocationId(siExistingGVPNBean.getErfLocSiteAddressId());
			webexQuoteDataBean.setExistingGVPNInfo(existingGVPNInfo);
		}
		return webexQuoteDataBean;
	}

	/**
	 * Get product solution by id
	 *
	 * @param solutionId
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public ProductSolution getProductSolution(Integer solutionId) throws TclCommonException {
		Objects.requireNonNull(solutionId, GscConstants.SOLUTION_ID_NULL_MESSAGE);
		Optional<ProductSolution> productSolution = productSolutionRepository.findById(solutionId);
		if (Objects.isNull(productSolution)) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productSolution.get();
	}

	/**
	 * Get Quote to legal entity id from quote id
	 *
	 * @param quoteId
	 * @return
	 */
	private Integer getQuoteToleIdFromQuoteId(Integer quoteId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst();
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		return quoteToLe.get().getId();
	}

	/**
	 * Method for getting bom details from Cisco CCW
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param dealId
	 * @return
	 * @throws JAXBException, TclCommonException
	 */
	public WebexSolutionBean getBomDetails(Integer quoteId, Integer solutionId, String dealId)
			throws JAXBException, TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, GscConstants.SOLUTION_ID_NULL_MESSAGE);

		List<QuoteLineType> bomDetails = new ArrayList<>();
		final Integer quoteToLeId = getQuoteToleIdFromQuoteId(quoteId);
		WebexSolutionBean solutionBean = new WebexSolutionBean();
		solutionBean.setSolutionId(solutionId);
		ProductSolution productSolution = getProductSolution(solutionId);
		solutionBean.setOfferingName(WebexConstants.WEBEX);
		solutionBean.setSolutionCode(productSolution.getSolutionCode());
		solutionBean.setProductName(WebexConstants.WEBEX);
		solutionBean.setDealId(dealId);

		// Get token to pass authorization gateway
		Map<String, List<String>> headers = new HashMap<>();
		AuthBean authBean;
		try {
			authBean = Objects.requireNonNull(getAccessToken(0));
			headers.put(WebexConstants.AUTHORIZATION, Collections
					.singletonList(WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken()));
		} catch (Exception e) {
			solutionBean.setStatus(Status.FAILURE.toString());
			if (e instanceof TclCommonException) {
				LOGGER.info("Error in ccw authorization : {}", e);
				solutionBean.setMessage(e.getMessage());
			} else {
				LOGGER.info("Unknown error occurred in ccw authorization : {}", e);
				solutionBean.setMessage(WebexConstants.TRY_AGAIN_AFTER_SOMETIME_MESSAGE);
			}
			return solutionBean;
		}

		// Fetch quote details with the help of deal id
		ShowQuoteType listQuoteResponse = null;
		ShowQuoteType acquireQuoteResponse = null;

		GetQuoteType listQuoteRequest = constructListQuoteRequest(dealId);
		try {
			listQuoteResponse = getListQuoteResponse(listQuoteRequest, authBean, 0);
			LOGGER.info("CCW ListQuote Response {}",
					Objects.nonNull(listQuoteResponse)
							? Utils.convertObjectToXmlString(listQuoteResponse, ShowQuoteType.class)
							: null);
			List<ConfigurationMessagesType> configurationMessagesTypes = listQuoteResponse.getDataArea().getQuote()
					.stream().filter(quoteType -> Objects.nonNull(quoteType)).findAny().get().getQuoteHeader()
					.getUserArea().getCiscoExtensions().getCiscoHeader().getConfigurationMessages();
			if (Objects.nonNull(listQuoteResponse) && configurationMessagesTypes.stream()
					.anyMatch(configurationMessagesType -> WebexConstants.CISCO_SUCCESS_CODE
							.equals(configurationMessagesType.getID().getValue()))) {
				String ciscoQuoteId = listQuoteResponse.getDataArea().getQuote().stream()
						.filter(quote -> !quote.getQuoteHeader().getDocumentID().getID().getValue().isEmpty()).findAny()
						.map(id -> id.getQuoteHeader().getDocumentID().getID().getValue()).get();
				try {
					GetQuoteType acquireQuoteRequest = constructAcquireQuoteRequest(ciscoQuoteId);
					acquireQuoteResponse = getAcquireQuoteResponse(acquireQuoteRequest, authBean, 0);
					LOGGER.info("CCW AcquireQuote response : {}",
							Utils.convertObjectToXmlString(acquireQuoteResponse, ShowQuoteType.class));
					bomDetails = Objects.requireNonNull(acquireQuoteResponse.getDataArea().getQuote().stream()
							.filter(quote -> !quote.getQuoteLine().isEmpty()).findAny().map(QuoteType::getQuoteLine)
							.orElse(null));
				} catch (Exception e) {
					solutionBean.setStatus(Status.FAILURE.toString());
					if (Objects.nonNull(acquireQuoteResponse)) {
						LOGGER.warn("AcquireQuote error response : {}",
								Utils.convertObjectToXmlString(acquireQuoteResponse, ShowQuoteType.class));
						if (WebexConstants.CISCO_EXPIRED_QUOTE_ERROR_CODE
								.equals(acquireQuoteResponse.getDataArea().getQuote().stream().findFirst().get()
										.getQuoteHeader().getUserArea().getCiscoExtensions().getCiscoHeader()
										.getConfigurationMessages().stream().findFirst().get().getID().getValue()))
							solutionBean.setMessage(String.format(WebexConstants.QUOTE_EXPIRED_MESSAGE, dealId));
						else if (WebexConstants.CISCO_UNAVAILABLE_QUOTE_ERROR_CODE
								.equals(acquireQuoteResponse.getDataArea().getQuote().stream().findFirst().get()
										.getQuoteHeader().getUserArea().getCiscoExtensions().getCiscoHeader()
										.getConfigurationMessages().stream().findFirst().get().getID().getValue()))
							solutionBean.setMessage(String.format(WebexConstants.QUOTE_UNAVAILABLE_MESSAGE, dealId));
					} else if (e instanceof TclCommonException) {
						LOGGER.info("Error occurred in acquireQuote {}", e);
						solutionBean.setMessage(e.getMessage());
					} else {
						LOGGER.info("Error occurred in acquireQuote {}",
								WebexConstants.TRY_AGAIN_AFTER_SOMETIME_MESSAGE);
						solutionBean.setMessage(WebexConstants.TRY_AGAIN_AFTER_SOMETIME_MESSAGE);
					}
					populateQuoteUcaas(solutionBean, null, quoteToLeId,null);
					return solutionBean;
				}
			} else
				throw new TclCommonException(ResponseResource.RES_FAILURE);
		} catch (Exception e) {
			solutionBean.setStatus(Status.FAILURE.toString());

			if (Objects.nonNull(listQuoteResponse)) {
				LOGGER.warn("ListQuote error response : {}",
						Utils.convertObjectToXmlString(listQuoteResponse, ShowQuoteType.class));
				if (WebexConstants.CISCO_EXPIRED_QUOTE_ERROR_CODE.equals(listQuoteResponse.getDataArea().getQuote()
						.stream().findFirst().get().getQuoteHeader().getUserArea().getCiscoExtensions().getCiscoHeader()
						.getConfigurationMessages().stream().findFirst().get().getID().getValue()))
					solutionBean.setMessage(String.format(WebexConstants.QUOTE_EXPIRED_MESSAGE, dealId));
				else if (WebexConstants.CISCO_UNAVAILABLE_QUOTE_ERROR_CODE.equals(listQuoteResponse.getDataArea()
						.getQuote().stream().findFirst().get().getQuoteHeader().getUserArea().getCiscoExtensions()
						.getCiscoHeader().getConfigurationMessages().stream().findFirst().get().getID().getValue()))
					solutionBean.setMessage(String.format(WebexConstants.QUOTE_UNAVAILABLE_MESSAGE, dealId));
			} else if (e instanceof TclCommonException) {
				LOGGER.info("Error occurred in ListQuote {}", e);
				solutionBean.setMessage(e.getMessage());
			} else {
				LOGGER.info("Error occurred in ListQuote {}", WebexConstants.TRY_AGAIN_AFTER_SOMETIME_MESSAGE);
				solutionBean.setMessage(WebexConstants.TRY_AGAIN_AFTER_SOMETIME_MESSAGE);
			}
			populateQuoteUcaas(solutionBean, null, quoteToLeId,null);
			return solutionBean;
		}
		solutionBean.setStatus(Status.SUCCESS.toString());

		// For validating initial Term and cisco voice components.
		validateCiscoVoiceAndContractPeriod(bomDetails, solutionBean);

		if (Status.SUCCESS.toString().equals(solutionBean.getStatus())) {
			solutionBean.setMessage(String.format(WebexConstants.BOM_DETAILS_RECIEVED_SUCCESS_MESSAGE, dealId));
		}

		// populating End Customer details (name,address)
		CiscoDealAttributesBean dealAttributes = new CiscoDealAttributesBean();
		acquireQuoteResponse.getDataArea().getQuote().stream()
				.anyMatch(quoteType -> quoteType.getQuoteHeader().getParty().stream()
						.peek(partyType -> partyType.getName().stream()
								.forEach(nameType2 -> dealAttributes.setCustomerName(nameType2.getValue())))
						.anyMatch(partyType -> WebexConstants.END_CUSTOMER.equals(partyType.getRole())));

		// fetch customer address
		acquireQuoteResponse.getDataArea().getQuote().stream().anyMatch(quoteType -> quoteType
				.getQuoteHeader().getParty().stream().peek(
						partyType -> partyType.getLocation().stream()
								.forEach(locationType -> locationType.getAddress().forEach(addressType -> dealAttributes
										.setCustomerAddress((StringUtils.isNotEmpty(addressType.getLineOne().getValue())
												? addressType.getLineOne().getValue() + CommonConstants.SPACE
												: CommonConstants.EMPTY)
												+ (StringUtils.isNotEmpty(addressType.getLineTwo().getValue())
														? addressType.getLineTwo().getValue() + CommonConstants.SPACE
														: CommonConstants.EMPTY)
												+ (StringUtils.isNotEmpty(addressType.getLineThree().getValue())
												? addressType.getLineThree().getValue() + CommonConstants.SPACE
												: CommonConstants.EMPTY)
												+ addressType.getCityName().getValue() + CommonConstants.SPACE
												+ addressType.getCountryCode().getValue() + CommonConstants.HYPHEN
												+ addressType.getPostalCode().getValue()))))
				.anyMatch(partyType -> WebexConstants.END_CUSTOMER.equals(partyType.getRole())));
		solutionBean.setDealAttributes(dealAttributes);

		// Populate QuoteUcaas from BOM Details
		populateQuoteUcaas(solutionBean, bomDetails, quoteToLeId,acquireQuoteResponse);
		return solutionBean;
	}

	/**
	 * Get list quote response
	 *
	 * @param listQuoteRequest
	 * @param authBean
	 * @param retryCount
	 * @return
	 * @throws Exception
	 */
	private ShowQuoteType getListQuoteResponse(GetQuoteType listQuoteRequest, AuthBean authBean,
											   int retryCount) throws TclCommonException, InterruptedException {
//		CiscoB2BService ciscoB2BService = new CiscoB2BService();
		JAXBElement<ShowQuoteType> quoteResponse = null;
//		B2BServiceB2BListQuotePortType listQuotePort = ciscoB2BService.getB2BNGCServiceB2BListQuoteEndpoint();
//		BindingProvider provider = (BindingProvider) listQuotePort;
//	    provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, listQuote);
//	    provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);

		try {
			if (retryCount != 0 && retryCount <= 3)
				TimeUnit.SECONDS.sleep(2);
			quoteResponse = (JAXBElement<ShowQuoteType>) soapConnector.webServiceWithCallAction(
					listQuote, listQuoteRequest, new WebexHttpHeaderCallback(WebexConstants.AUTHORIZATION,
							WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken(),
							CommonConstants.EMPTY));
//			quoteResponse = listQuotePort.meshaListQuoteProxyH(listQuoteRequest);
		} catch (Exception e) {
			LOGGER.info("Failed connecting to listquote api...trying again " + retryCount);
			LOGGER.info("Error in list quote API : {}", e);

			if (retryCount >= 3) {
				Set<String> errorCodes = Sets.newHashSet(WebexConstants.ERROR_CODE_401, WebexConstants.ERROR_CODE_403,
						WebexConstants.ERROR_CODE_404, WebexConstants.ERROR_CODE_408, WebexConstants.ERROR_CODE_503,
						WebexConstants.ERROR_CODE_504);
				Optional<String> statusCode = errorCodes.stream().filter(key -> e.getMessage().contains(key)).findAny();
				if (statusCode.isPresent()) {
					switch (statusCode.get()) {
						case WebexConstants.ERROR_CODE_401:
							throw new TclCommonException(ExceptionConstants.WEBEX_NOT_AUTHORIZED_EXCEPTION);
						case WebexConstants.ERROR_CODE_403:
							throw new TclCommonException(ExceptionConstants.WEBEX_FORBIDDEN_EXCEPTION);
						case WebexConstants.ERROR_CODE_404:
							throw new TclCommonException(ExceptionConstants.WEBEX_URL_NOT_FOUND_EXCEPTION);
						case WebexConstants.ERROR_CODE_408:
							throw new TclCommonException(ExceptionConstants.WEBEX_CLIENT_TIMEOUT_EXCEPTION);
						case WebexConstants.ERROR_CODE_503:
							throw new TclCommonException(ExceptionConstants.WEBEX_SERVER_UNAVAILABLE_EXCEPTION);
						case WebexConstants.ERROR_CODE_504:
							throw new TclCommonException(ExceptionConstants.WEBEX_SERVER_TIMEOUT_EXCEPTION);
					}
				} else
					return null;
			}
			getListQuoteResponse(listQuoteRequest, authBean, ++retryCount);
		}
		return quoteResponse.getValue();
	}

	/**
	 * Get acquire quote response
	 *
	 * @param acquireQuoteRequest
	 * @param authBean
	 * @param retryCount
	 * @return
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private ShowQuoteType getAcquireQuoteResponse(GetQuoteType acquireQuoteRequest, AuthBean authBean,
												  int retryCount) throws TclCommonException, InterruptedException {
//		CiscoB2BService ciscoB2BService = new CiscoB2BService();
		JAXBElement<ShowQuoteType> quoteResponse = null;
//		B2BServiceB2BAcquireQuotePortType acquireQuotePort = ciscoB2BService.getB2BNGCServiceB2BAcquireQuoteEndpoint();
//		BindingProvider provider = (BindingProvider) acquireQuotePort;
//		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, acquireQuote);
//		provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		try {
			if (retryCount != 0 && retryCount <= 3)
				TimeUnit.SECONDS.sleep(2);
			quoteResponse = (JAXBElement<ShowQuoteType>) soapConnector.webServiceWithCallAction(
					acquireQuote, acquireQuoteRequest, new WebexHttpHeaderCallback(WebexConstants.AUTHORIZATION,
							WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken(),
							CommonConstants.EMPTY));
		} catch (Exception e) {
			LOGGER.info("Failed connecting to acquirequote api...trying again " + retryCount);
			LOGGER.info("Error in acquire quote API : {}", e);
			if (retryCount >= 3) {
				Set<String> errorCodes = Sets.newHashSet(WebexConstants.ERROR_CODE_401, WebexConstants.ERROR_CODE_403,
						WebexConstants.ERROR_CODE_404, WebexConstants.ERROR_CODE_408, WebexConstants.ERROR_CODE_503,
						WebexConstants.ERROR_CODE_504);
				Optional<String> statusCode = errorCodes.stream().filter(key -> e.getMessage().contains(key)).findAny();
				if (statusCode.isPresent()) {
					switch (statusCode.get()) {
						case WebexConstants.ERROR_CODE_401:
							throw new TclCommonException(ExceptionConstants.WEBEX_NOT_AUTHORIZED_EXCEPTION);
						case WebexConstants.ERROR_CODE_403:
							throw new TclCommonException(ExceptionConstants.WEBEX_FORBIDDEN_EXCEPTION);
						case WebexConstants.ERROR_CODE_404:
							throw new TclCommonException(ExceptionConstants.WEBEX_URL_NOT_FOUND_EXCEPTION);
						case WebexConstants.ERROR_CODE_408:
							throw new TclCommonException(ExceptionConstants.WEBEX_CLIENT_TIMEOUT_EXCEPTION);
						case WebexConstants.ERROR_CODE_503:
							throw new TclCommonException(ExceptionConstants.WEBEX_SERVER_UNAVAILABLE_EXCEPTION);
						case WebexConstants.ERROR_CODE_504:
							throw new TclCommonException(ExceptionConstants.WEBEX_SERVER_TIMEOUT_EXCEPTION);
					}
				} else
					return null;
			}
			getAcquireQuoteResponse(acquireQuoteRequest, authBean, ++retryCount);
		}
		return quoteResponse.getValue();
	}

	/**
	 * Get access token for Cisco CCW API access
	 *
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException
	 */
	public AuthBean getAccessToken(Integer retryCount) throws TclCommonException, InterruptedException {
		AuthBean auth = new AuthBean();
		LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
		formBody.add(WebexConstants.CLIENT_ID, clientId);
		formBody.add(WebexConstants.CLIENT_SECRET, clientSecret);
		formBody.add(WebexConstants.GRANT_TYPE, WebexConstants.CLIENT_CREDENTIALS);
		formBody.add(WebexConstants.USERNAME, username);
		formBody.add(WebexConstants.PASSWORD, password);
		if (retryCount != 0 && retryCount <= 3)
			TimeUnit.SECONDS.sleep(4);
		RestResponse response = restClientService.postWithoutHeader(authUrl, formBody);
		if (response.getStatus() == Status.SUCCESS && Objects.nonNull(response.getData())) {
			auth = (AuthBean) Utils.convertJsonToObject(response.getData(), AuthBean.class);
			LOGGER.info("CCW Authorization : {}", auth.getAccessToken());
		} else {
			LOGGER.info("Error getting authorization token...retrying {}", retryCount);
			if (retryCount >= 3) {
				Optional<String> statusCode = Sets
						.newHashSet(WebexConstants.ERROR_CODE_401, WebexConstants.ERROR_CODE_403).stream()
						.filter(errorCodes -> response.getErrorMessage().contains(errorCodes)).findAny();
				if (statusCode.isPresent())
					switch (statusCode.get()) {
					case WebexConstants.ERROR_CODE_401:
						throw new TclCommonException(ExceptionConstants.WEBEX_NOT_AUTHORIZED_EXCEPTION);
					case WebexConstants.ERROR_CODE_403:
						throw new TclCommonException(ExceptionConstants.WEBEX_FORBIDDEN_EXCEPTION);
					}
				else
					throw new TclCommonException(ExceptionConstants.WEBEX_AUTH_TOKEN_EXCEPTION);
			} else
				getAccessToken(++retryCount);
		}
		return auth;
	}

	/**
	 * populate quote ucaas values
	 *
	 * @param solutionBean
	 * @param bomDetails
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	private void populateQuoteUcaas(WebexSolutionBean solutionBean, List<QuoteLineType> bomDetails, Integer quoteToLeId,
									ShowQuoteType acquireQuoteResponse)throws TclCommonException {

		QuoteToLe quoteToLe = getQuoteToLe(quoteToLeId);
		deleteAllQuoteUcaasDetailsAndSiteDetails(quoteToLe);
		quoteUcaasRepository.deleteAll(getExistingUcaasQuotes(quoteToLe));
		QuoteUcaas configurationData = quoteUcaasRepository.findByQuoteToLeAndIsConfig(quoteToLe, (byte) 1).stream()
				.findAny().get();
		configurationData
				.setQuantity(Objects.nonNull(configurationData.getQuantity()) ? null : configurationData.getQuantity());
		configurationData.setDealId(Integer.parseInt(solutionBean.getDealId()));
		configurationData
				.setDeal_status(Status.SUCCESS.toString().equals(solutionBean.getStatus()) ? WebexConstants.SUCCESS
						: WebexConstants.FAILURE);
		String response = (Objects.nonNull(acquireQuoteResponse)?
				Utils.convertObjectToJson(acquireQuoteResponse):null);
		configurationData.setDealAttributes(Utils.convertObjectToJson(solutionBean.getDealAttributes()));
		configurationData.setDeal_message(solutionBean.getMessage());
		configurationData.setMrc(0.0);
		configurationData.setNrc(0.0);
		configurationData.setTcv(0.0);
		QuoteUcaas savedUcaasConfig = quoteUcaasRepository.save(configurationData);
		saveQuoteUcaasDetail(savedUcaasConfig,quoteToLe,response);
		String endpointResponse = (String) mqUtils.sendAndReceive(getEndpointList, "");
		List<WebexEndpointCatalogBean> webexEndpoints = Utils.fromJson(endpointResponse,
				new TypeReference<List<WebexEndpointCatalogBean>>() {
				});
		if (Status.SUCCESS.toString().equals(solutionBean.getStatus())) {
			List<QuoteUcaasBean> quoteUcaasBean;
			List<QuoteUcaas> saveQuoteUcaas = new ArrayList<>();
			Set<String> skuNames = Sets.newHashSet(WebexConstants.A_FLEX_EACM, WebexConstants.A_FLEX_AUCM,
					WebexConstants.A_FLEX_NUCM);

			// For setting license quantity and userType.
			bomDetails.stream().anyMatch(quoteLineType -> {
				if (quoteLineType.getItem().getItemID().stream()
						.filter(itemIDType -> WebexConstants.PARTNUMBER.equals(itemIDType.getID().getSchemeName()))
						.anyMatch(itemIDType -> (skuNames.stream().anyMatch(skuName -> {
							if (itemIDType.getID().getValue().contains(skuName)) {
								// For setting userType based on SkuName.
								try {
									saveSkuID(WebexConstants.skuUserTypes.get(skuName), quoteToLeId, solutionBean,
											saveQuoteUcaas, quoteLineType.getQuantity().getValue().intValue());
									solutionBean.getDealAttributes()
											.setLicenseType(WebexConstants.licenseTypes.get(skuName));
								} catch (TclCommonException e) {
									e.printStackTrace();
								}
								return true;
							}
							return false;
						})))) {
					configurationData.setQuantity(quoteLineType.getQuantity().getValue().intValue());
					return true;
				} else
					configurationData.setQuantity(null);
				return false;
			});
			quoteUcaasRepository.save(configurationData);
			bomDetails.forEach(bom -> {
				QuoteUcaas quoteUcaas = new QuoteUcaas();
				bom.getItem().getItemID().stream()
						.filter(itemIDType -> WebexConstants.PARTNUMBER.equals(itemIDType.getID().getSchemeName()))
						.peek(item -> quoteUcaas.setName(item.getID().getValue())).collect(Collectors.toList());
				Double price = bom.getItem().getSpecification().stream().findFirst().get().getProperty().stream()
						.filter(propertyType -> WebexConstants.UNITNETPRICE
								.equals(propertyType.getNameValue().getName()))
						.map(propertyType -> Double.parseDouble(propertyType.getNameValue().getValue())).findAny()
						.get();
				// unit net price as mrc, because both are same at this point
				quoteUcaas.setCiscoUnitNetPrice(price);

				// fetching ciscoDiscountPercent
				bom.getPaymentTerm().stream().anyMatch(paymentTerm -> paymentTerm.getDiscount().stream()
						.anyMatch(discount -> discount.getType().stream().anyMatch(type -> {
							if (type.getListName().contains(WebexConstants.CISCO_DISCOUNT_TYPE)
									&& type.getValue().contains(WebexConstants.TOTAL_DISCOUNT)) {
								quoteUcaas.setCiscoDiscountPercent(Objects.nonNull(discount.getDiscountPercent())
										? discount.getDiscountPercent().doubleValue()
										: null);
								return true;
							} else
								return false;
						})));
				final Integer quantity = bom.getQuantity().getValue().intValue();
				final Double nrc = 0.0;
				final Double tcv = nrc + price;
				quoteUcaas.setDescription(bom.getItem().getDescription().stream().findFirst().get().getValue());
				quoteUcaas.setQuantity(quantity);
				// fetching unit net price
				Double unitPrice = Objects.nonNull(bom.getUnitPrice().getAmount().getValue())
						? bom.getUnitPrice().getAmount().getValue().doubleValue()
						: null;
				quoteUcaas.setCiscoUnitListPrice(unitPrice);
				quoteUcaas.setDealId(Integer.parseInt(solutionBean.getDealId()));
				quoteUcaas.setDeal_status(WebexConstants.SUCCESS);
				quoteUcaas.setDeal_message(solutionBean.getMessage());
				quoteUcaas.setTcv(tcv);
				quoteUcaas.setCreatedTime(new Date());
				quoteUcaas.setIsConfig((byte) 0);
				quoteUcaas.setStatus((byte) 1);
				quoteUcaas.setLicenseProvider(WebexConstants.WEBEX);
				quoteUcaas.setQuoteVersion(1);
				quoteUcaas.setCreatedTime(new Date());
				quoteUcaas.setCreatedBy(WebexConstants.UCAAS);
				if(Objects.nonNull(quoteUcaas.getName())){
					Boolean isEndPoint = webexEndpoints.stream()
							.anyMatch(endpoint -> quoteUcaas.getName().equalsIgnoreCase(endpoint.getSku()));
					if(isEndPoint){
						LOGGER.info("Endpoint Bom::{}",bom);
						bom.getUserArea().getCiscoExtensions().getCiscoLine().getParty().stream().
								forEach(partyType -> {
									LOGGER.info("PartyType::{}",partyType);
										partyType.getLocation().forEach(locationType -> {
										LOGGER.info("LocationType::{}",locationType);
										locationType.getAddress().stream().forEach(addressType -> {
											LOGGER.info("Address Type::{}",addressType);
											LocationDetail locationDetail = new LocationDetail();
											AddressDetail address = new AddressDetail();
											address.setAddressLineOne(addressType.getLineOne().getValue());
											String addressLineTwo = (StringUtils.isNotEmpty(addressType.getLineTwo().getValue())
													? addressType.getLineTwo().getValue()
													: CommonConstants.EMPTY);
											String addressLineThree = (StringUtils.isNotEmpty(addressType.getLineThree().getValue())
													? addressType.getLineTwo().getValue()
													: CommonConstants.EMPTY);
											address.setAddressLineTwo(addressLineTwo);
											address.setLocality(addressLineThree);
											address.setCity(addressType.getCityName().getValue());
											address.setPincode(addressType.getPostalCode().getValue());
											address.setState(addressType.getCountrySubDivisionCode().stream().findAny().get().getValue());
											address.setCountry(addressType.getCountryCode().getValue());
											locationDetail.setAddress(address);
											locationDetail.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
											try {
												String responseFromQueue = (String) mqUtils.sendAndReceive(locationDetailQueue,Utils.convertObjectToJson(locationDetail));
												LOGGER.info("Response From Queue::{}",responseFromQueue);
												Integer locationId = !StringUtils.isEmpty(responseFromQueue)?
														Integer.valueOf(responseFromQueue):null;
												quoteUcaas.setEndpointLocationId(locationId);
											} catch (TclCommonException e) {
												e.printStackTrace();
											}
										});
									});
								});
						quoteUcaas.setUom(WebexConstants.ENDPOINT);
						quoteUcaas.setIsUpdated((byte) 0);
						quoteUcaas.setUnitMrc(null);
						quoteUcaas.setUnitNrc(unitPrice);
					}else{
						quoteUcaas.setUnitMrc(price);
						quoteUcaas.setUnitNrc(nrc);
						quoteUcaas.setUom(WebexConstants.LICENSE);
					}
				}
				try {
					quoteUcaas.setQuoteToLe(getQuoteToLe(quoteToLeId));
					quoteUcaas.setProductSolutionId(getProductSolution(solutionBean.getSolutionId()));
				} catch (TclCommonException e) {
					LOGGER.warn(e.getMessage());
				}
				QuoteUcaas savedQuoteUcaas = quoteUcaasRepository.save(quoteUcaas);
				try {
					saveQuoteUcaasDetail(savedQuoteUcaas,quoteToLe,Utils.convertObjectToJson(bom));
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				saveQuoteUcaas.add(savedQuoteUcaas);
			});

			Set<Integer> locationIds = quoteUcaasRepository.findByQuoteToLeIdAndLocationNotNull(quoteToLeId).stream().
					map(quoteUcaas -> quoteUcaas.getEndpointLocationId()).collect(Collectors.toSet());
			ProductSolution productSolution = getProductSolution(solutionBean.getSolutionId());
			if(Objects.nonNull(locationIds) && !locationIds.isEmpty()){
				locationIds.forEach(id -> {
					QuoteUcaasSiteDetails quoteUcaasSiteDetails = new QuoteUcaasSiteDetails();
					quoteUcaasSiteDetails.setSiteCode(Utils.generateUid());
					quoteUcaasSiteDetails.setEndpointLocationId(id);
					quoteUcaasSiteDetails.setCreatedTime(new Date());
					quoteUcaasSiteDetails.setCreatedBy(WebexConstants.UCAAS);
					quoteUcaasSiteDetails.setProductSolution(productSolution);
					quoteUcaasSiteDetailsRepository.save(quoteUcaasSiteDetails);
				});
			}

			saveQuoteUcaas.sort(Comparator.comparing(QuoteUcaas::getName));
			quoteUcaasBean = saveQuoteUcaas.stream().map(QuoteUcaasBean::toQuoteUcaasBean).map(ucaasBean -> {
				try {
					ucaasBean =  populateSiteAddress(ucaasBean);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				return ucaasBean;
			}).collect(Collectors.toList());

			quoteToLe.setTermInMonths(solutionBean.getContractPeriod() + WebexConstants.SPACE + WebexConstants.MONTHS);
			quoteToLeRepository.save(quoteToLe);

			solutionBean.setUcaasQuotes(quoteUcaasBean);
		}
	}

	/**
	 * Get site address from location service using location id
	 * And setting in quoteUcaasBean.
	 * @param quoteUcaasBean
	 * @return
	 */
	public QuoteUcaasBean populateSiteAddress(QuoteUcaasBean quoteUcaasBean) throws TclCommonException {
		if(quoteUcaasBean.getIsEndpoint()){
			EndpointDetails endpointDetails = quoteUcaasBean.getEndpointDetails();
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(endpointDetails.getLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
			SiteAddress siteAddress = new SiteAddress();
			siteAddress.setAddressLineOne(addressDetail.getAddressLineOne());
			siteAddress.setAddressLineTwo(addressDetail.getAddressLineTwo());
			siteAddress.setAddressLineThree(addressDetail.getLocality());
			siteAddress.setCity(addressDetail.getCity());
			siteAddress.setCountry(addressDetail.getCountry());
			siteAddress.setPinCode(addressDetail.getPincode());
			siteAddress.setState(addressDetail.getState());
			endpointDetails.setSiteAddress(siteAddress);
			quoteUcaasBean.setEndpointDetails(endpointDetails);
		}
		return quoteUcaasBean;
	}

	/**
	 * Delete all quote ucaas details
	 * @param quoteToLe
	 */
	public void deleteAllQuoteUcaasDetailsAndSiteDetails(QuoteToLe quoteToLe){
		List<QuoteUcaas> quoteUcaasList = quoteUcaasRepository.findByQuoteToLeId(quoteToLe.getId());
		Integer productSolutionId = quoteUcaasList.stream().findAny().get().getProductSolutionId().getId();
		List<QuoteUcaasSiteDetails> quoteUcaasSiteDetails = quoteUcaasSiteDetailsRepository.
				findByProductSolutionId(productSolutionId);
		quoteUcaasSiteDetailsRepository.deleteAll(quoteUcaasSiteDetails);
		if(Objects.nonNull(quoteUcaasList) && !quoteUcaasList.isEmpty()){
			quoteUcaasList.forEach(quoteUcaas -> {
				Optional<QuoteUcaasDetail> quoteUcaasDetail = quoteUcaasDetailsRepository.findByQuoteUcaasId(quoteUcaas.getId());
				if(quoteUcaasDetail.isPresent()){
					quoteUcaasDetailsRepository.delete(quoteUcaasDetail.get());
				}
			});
		}
	}

	/**
	 * Method to save quote ucaas details
	 * @param quoteUcaas
	 * @param quoteToLe
	 * @param response
	 */
	public void saveQuoteUcaasDetail(QuoteUcaas quoteUcaas, QuoteToLe quoteToLe,String response){
		QuoteUcaasDetail quoteUcaasDetail = new QuoteUcaasDetail();
		quoteUcaasDetail.setQuoteUcaas(quoteUcaas);
		quoteUcaasDetail.setResponse(response);
		quoteUcaasDetail.setCreatedTime(new Date());
		quoteUcaasDetail.setCreatedBy(WebexConstants.UCAAS);
		quoteUcaasDetailsRepository.save(quoteUcaasDetail);
	}

	/**
	 * saveSkuID
	 *
	 * @param userType
	 * @param quoteToLeId
	 * @return
	 *
	 */
	public void saveSkuID(String userType, Integer quoteToLeId, WebexSolutionBean webexSolutionBean,
			List<QuoteUcaas> ucaasQuotes, Integer quantity) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		SkuDetailsResponseBean skuDetails = null;
		if (quoteToLe.isPresent()) {
			QuoteUcaas quoteUcaas = quoteUcaasRepository.findByQuoteToLeAndIsConfig(quoteToLe.get(), (byte) 1).stream()
					.findAny().get();

			if (Objects.nonNull(userType) && Objects.nonNull(WebexConstants.audioTypes.get(quoteUcaas.getAudioType()))
					&& Objects.nonNull(WebexConstants.bridgeRegions.get(quoteUcaas.getPrimaryRegion()))) {
				SkuDetailsRequestBean requestBean = new SkuDetailsRequestBean();
				requestBean.setAudioPlan(WebexConstants.audioTypes.get(quoteUcaas.getAudioType()));
				requestBean.setBridgeRegion(WebexConstants.bridgeRegions.get(quoteUcaas.getPrimaryRegion()));
				requestBean.setLicenseType(userType);
				skuDetails = Utils.convertJsonToObject(
						(String) mqUtils.sendAndReceive(skuQueue, Utils.convertObjectToJson(requestBean) + ""),
						SkuDetailsResponseBean.class);
			}
			if (Objects.nonNull(skuDetails)) {
				QuoteUcaas newQuoteUcaas = new QuoteUcaas();
				newQuoteUcaas.setDealId(Integer.parseInt(webexSolutionBean.getDealId()));
				newQuoteUcaas.setIsConfig((byte) 0);
				newQuoteUcaas.setName(skuDetails.getSkuName());
				newQuoteUcaas.setDescription(skuDetails.getDescription());
				newQuoteUcaas.setUnitMrc(skuDetails.getMrc().doubleValue());
				newQuoteUcaas.setQuoteToLe(quoteUcaas.getQuoteToLe());
				newQuoteUcaas.setProductSolutionId(quoteUcaas.getProductSolutionId());
				newQuoteUcaas.setQuoteVersion(quoteUcaas.getQuoteVersion());
				newQuoteUcaas.setCreatedTime(new Date());
				newQuoteUcaas.setLicenseProvider(WebexConstants.WEBEX);
				newQuoteUcaas.setStatus((byte) 1);
				newQuoteUcaas.setCreatedBy(WebexConstants.UCAAS);
				newQuoteUcaas.setQuantity(quantity);
				newQuoteUcaas.setUom(WebexConstants.SUBSCRIPTION);
				quoteUcaasRepository.save(newQuoteUcaas);
				ucaasQuotes.add(newQuoteUcaas);
			}
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * Constructing request object for list quote API
	 *
	 * @param dealId
	 * @return
	 */
	private GetQuoteType constructListQuoteRequest(String dealId) throws JAXBException {
		GetQuoteType listQuoteRequest = new GetQuoteType();
		ApplicationAreaType applicationAreaType = new ApplicationAreaType();
		SenderType senderType = new SenderType();
		IdentifierType2 id = new IdentifierType2();
		id.setValue(WebexConstants.UCAAS_FAMILY_NAME);
		senderType.setComponentID(id);
		applicationAreaType.setSender(senderType);
		listQuoteRequest.setApplicationArea(applicationAreaType);
		GetQuoteDataAreaType dataAreaType = new GetQuoteDataAreaType();
		GetType getType = new GetType();
		getType.setMaxItems(BigInteger.valueOf(WebexConstants.MAX_ITEMS));
		ExpressionType expressionType = new ExpressionType();
		expressionType.setExpressionLanguage(WebexConstants.DEAL_ID);
		expressionType.setValue(dealId);
		getType.getExpression().add(expressionType);
		dataAreaType.setGet(getType);
		listQuoteRequest.setDataArea(dataAreaType);
		LOGGER.info("CCW ListQuoteRequest {}", Utils.convertObjectToXmlString(listQuoteRequest, GetQuoteType.class));
		return listQuoteRequest;
	}

	/**
	 * Constructing request object for Acquire Quote API
	 *
	 * @param ciscoQuoteId
	 * @return
	 */
	private GetQuoteType constructAcquireQuoteRequest(String ciscoQuoteId) throws JAXBException {
		GetQuoteType acquireQuoteRequest = new GetQuoteType();
		ApplicationAreaType applicationAreaTypeQuote = new ApplicationAreaType();
		SenderType senderTypeQuote = new SenderType();
		IdentifierType2 idQuote = new IdentifierType2();
		idQuote.setValue(WebexConstants.UCAAS_FAMILY_NAME);
		senderTypeQuote.setComponentID(idQuote);
		applicationAreaTypeQuote.setSender(senderTypeQuote);
		acquireQuoteRequest.setApplicationArea(applicationAreaTypeQuote);
		GetQuoteDataAreaType dataAreaTypeQuote = new GetQuoteDataAreaType();
		QuoteType quoteType = new QuoteType();
		QuoteHeaderType quoteHeaderType = new QuoteHeaderType();
		DocumentIDType documentIDType = new DocumentIDType();
		IdentifierType2 identifierType = new IdentifierType2();
		identifierType.setValue(ciscoQuoteId);
		documentIDType.setID(identifierType);
		quoteHeaderType.setDocumentID(documentIDType);
		quoteType.setQuoteHeader(quoteHeaderType);
		dataAreaTypeQuote.getQuote().add(quoteType);
		acquireQuoteRequest.setDataArea(dataAreaTypeQuote);
		LOGGER.info("CCW AcquireQuoteRequest {}",
				Utils.convertObjectToXmlString(acquireQuoteRequest, GetQuoteType.class));
		return acquireQuoteRequest;
	}

	/**
	 * Attach the Quote pdf in E-Mail
	 *
	 * @param email
	 * @param quoteId
	 * @return
	 *
	 */
	public ServiceResponse sendQuoteViaEmail(Integer quoteId, String email, HttpServletResponse response)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(email, GscConstants.EMAIL_NULL_MESSAGE);
		String fileName = "Quote_" + quoteId + ".pdf";
		String quotePdf = webexQuotePdfService.processQuoteHtml(quoteId, response);
		notificationService.processShareQuoteNotification(email,
				java.util.Base64.getEncoder().encodeToString(quotePdf.getBytes()), userInfoUtils.getUserFullName(),
				fileName, CommonConstants.GSC);
		ServiceResponse serviceResponse = new ServiceResponse();
		serviceResponse.setFileName(fileName);
		serviceResponse.setStatus(Status.SUCCESS);
		return serviceResponse;
	}

	/**
	 * Validate contract period.
	 *
	 * @param quoteLineType
	 * @param solutionBean
	 * @param lineCounter
	 *
	 */
	public Boolean validateContractPeriod(QuoteLineType quoteLineType, WebexSolutionBean solutionBean,
			Integer lineCounter) {
		List<String> validContractPeriods = Arrays.asList("12", "24", "36", "48", "60");
		Boolean isValid = true;
		String contractPeriod;
		if (Objects.nonNull(quoteLineType.getUserArea().getCiscoExtensions().getCiscoLine().getInitialTerm())) {
			contractPeriod = quoteLineType.getUserArea().getCiscoExtensions().getCiscoLine().getInitialTerm();
			if (solutionBean.getContractPeriod() == null)
				solutionBean.setContractPeriod(contractPeriod);
			if (!validContractPeriods.contains(contractPeriod)) {
				solutionBean.setStatus(Status.FAILURE.toString());
				solutionBean.setMessage(String.format(WebexConstants.CONTRACT_PERIOD_MISMATCH_MESSAGE, contractPeriod));
				isValid = false;
			} else if (!contractPeriod.equals(solutionBean.getContractPeriod())) {
				solutionBean.setStatus(Status.FAILURE.toString());
				solutionBean.setMessage(WebexConstants.CONTRACT_PERIOD_UNSUPPORTED_MESSAGE);
				isValid = false;
			} else if (lineCounter == 1 && Objects.isNull(solutionBean.getContractPeriod())) {
				solutionBean.setStatus(Status.FAILURE.toString());
				solutionBean.setMessage(WebexConstants.CONTRACT_PERIOD_ABSENT_MESSAGE);
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Cisco Voice component validation
	 *
	 * @param quoteLineType
	 * @param solutionBean
	 */
	private Boolean validateCiscoVoice(QuoteLineType quoteLineType, WebexSolutionBean solutionBean) {
		List<String> skuNames = Arrays.asList(WebexConstants.A_FLEX_CCA_SP_USER);
		Predicate<ItemIDType> partNumber = itemIDType -> WebexConstants.PARTNUMBER
				.equals(itemIDType.getID().getSchemeName());
		ItemIDType partNumbers = quoteLineType.getItem().getItemID().stream().filter(partNumber).findAny().get();
		boolean isTataVoice = skuNames.contains(partNumbers.getID().getValue());
		return isTataVoice;
	}

	/**
	 * Validate for Cloud Meeting License
	 *
	 * @param quoteLineType
	 * @param webexSolutionBean
	 * @return
	 */
	private Boolean validateForCloudMeetingLicense(QuoteLineType quoteLineType, WebexSolutionBean webexSolutionBean) {
		Set<String> skuNames = Sets.newHashSet(WebexConstants.A_FLEX_EACM, WebexConstants.A_FLEX_AUCM,
				WebexConstants.A_FLEX_NUCM);
		return quoteLineType.getItem().getItemID().stream().anyMatch(
				itemIDType -> skuNames.stream().anyMatch(skuName -> itemIDType.getID().getValue().contains(skuName)));
	}

	/**
	 * Validate for cisco voice components and contract period in Bom details
	 *
	 * @param bomDetails
	 * @param solutionBean
	 */
	private void validateCiscoVoiceAndContractPeriod(List<QuoteLineType> bomDetails, WebexSolutionBean solutionBean) {
		Boolean isValidContractPeriod = true;
		Boolean isTataVoice = false;
		Boolean containsCloudMeetingLicense = false;
		Integer lineCounter = bomDetails.size();

		for (QuoteLineType quoteLine : bomDetails) {
			if (!containsCloudMeetingLicense)
				containsCloudMeetingLicense = validateForCloudMeetingLicense(quoteLine, solutionBean);
			if (isValidContractPeriod)
				isValidContractPeriod = validateContractPeriod(quoteLine, solutionBean, --lineCounter);
			if (!isTataVoice)
				isTataVoice = validateCiscoVoice(quoteLine, solutionBean);
		}

		if (!containsCloudMeetingLicense) {
			solutionBean.setStatus(Status.FAILURE.toString());
			solutionBean.setMessage(
					String.format(WebexConstants.CLOUD_MEETING_LICENSE_ABSENT_MESSAGE, solutionBean.getDealId()));
		} else if (!isTataVoice) {
			solutionBean.setStatus(Status.FAILURE.toString());
			solutionBean.setMessage(
					String.format(WebexConstants.TATA_VOICE_VALIDATION_ERROR_MESSAGE, solutionBean.getDealId()));
		} else if (!isValidContractPeriod && !isTataVoice) {
			solutionBean.setStatus(Status.FAILURE.toString());
			solutionBean.setMessage(String.format(WebexConstants.CONTRACT_PERIOD_CISCO_VOICE_ERROR_MESSAGE,
					solutionBean.getContractPeriod()));
		}
	}

	/**
	 * Get existing GVPN service inventory details from service inventory
	 *
	 * @param custId
	 * @param page
	 * @param size
	 * @return
	 */
	public SIExistingGVPNBean getExistingGVPNDetails(String custId, Integer page, Integer size)
			throws TclCommonException {
		Objects.requireNonNull(custId, WebexConstants.CUSTOMER_ID_NULL_MESSAGE);
		SIExistingGVPNBean existingGVPNRequest = new SIExistingGVPNBean();
		existingGVPNRequest.setCustomerId(custId);
		existingGVPNRequest.setPage(page);
		existingGVPNRequest.setSize(size);
		String response = (String) mqUtils.sendAndReceive(siInfoQueue, Utils.convertObjectToJson(existingGVPNRequest));
		if (Objects.nonNull(response) && StringUtils.isNotBlank(response)) {
			existingGVPNRequest = Utils.convertJsonToObject(response, SIExistingGVPNBean.class);
			existingGVPNRequest.setStatus(CommonConstants.SUCCESS);
			existingGVPNRequest.setMessage(WebexConstants.INVENTORY_RECEIVED_MESSAGE);
		} else {
			existingGVPNRequest.setStatus(CommonConstants.FAILIURE);
			existingGVPNRequest.setMessage(WebexConstants.INVENTORY_NOT_AVAILABLE_MESSAGE);
		}
		return existingGVPNRequest;
	}

	/**
	 * Get existing GVPN service inventory details from service inventory by search
	 * criteria
	 *
	 * @param custId
	 * @param page
	 * @param size
	 * @param city
	 * @param alias
	 * @param searchText
	 * @return
	 */
	public SIExistingGVPNBean getSIDetailBySearchCriteria(String custId, Integer page, Integer size, String city,
			String alias, String searchText) throws TclCommonException {
		Objects.requireNonNull(custId, WebexConstants.CUSTOMER_ID_NULL_MESSAGE);
		SIExistingGVPNBean existingGVPNRequest = new SIExistingGVPNBean();
		SIInfoSearchBean searchBean = new SIInfoSearchBean();
		searchBean.setPage(page);
		searchBean.setSize(size);
		searchBean.setCustomerId(custId);
		searchBean.setCity(city);
		searchBean.setSearchText(searchText);
		String response = (String) mqUtils.sendAndReceive(siInfoSearchQueue, Utils.convertObjectToJson(searchBean));
		if (Objects.nonNull(response) && StringUtils.isNotBlank(response)) {
			existingGVPNRequest = Utils.convertJsonToObject(response, SIExistingGVPNBean.class);
			existingGVPNRequest.setStatus(CommonConstants.SUCCESS);
			existingGVPNRequest.setMessage(WebexConstants.INVENTORY_RECEIVED_MESSAGE);
		} else {
			existingGVPNRequest.setStatus(CommonConstants.FAILIURE);
			existingGVPNRequest.setMessage(WebexConstants.INVENTORY_NOT_AVAILABLE_MESSAGE);
		}
		return existingGVPNRequest;
	}

	/**
	 * Context class for excel
	 */
	public static class Context {
		Integer rowCount = 0;
		Row row;
	}

	/**
	 * Download GVPN service inventory details from service inventory in excel
	 * format
	 *
	 * @param custId
	 * @param response
	 * @return
	 */
	public HttpServletResponse constructInventoryExcel(String custId, HttpServletResponse response)
			throws TclCommonException, IOException {
		Objects.requireNonNull(custId, WebexConstants.CUSTOMER_ID_NULL_MESSAGE);
		SIExistingGVPNBean existingGVPNRequest = new SIExistingGVPNBean();
		existingGVPNRequest.setCustomerId(custId);
		String queueResponse = (String) mqUtils.sendAndReceive(siInfoDownloadQueue,
				Utils.convertObjectToJson(existingGVPNRequest));
		List<SIServiceInfoGVPNBean> serviceInfoBeans = new ArrayList<>();
		if (Objects.nonNull(queueResponse) && StringUtils.isNotBlank(queueResponse)) {
			serviceInfoBeans = Utils.convertJsonToObject(queueResponse, SIExistingGVPNBean.class).getServiceInfos();
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		String[] columns = { "AccountName", "Legal Entity", "ServiceType", "Product Flavor", "Order Id",
				"CustomerServiceId", "Alias", "Final Status", "CommissioningDate", "Service option type",
				"Scope of management", "Primary/Secondary", "service link", "Circuit_Bandwidth", "Service Topology",
				"Routing Protocol", "A End Interface", "B End Interface", "A_End_LAST_MILE_Provider",
				"A_END_LAST_MILE_BANDWIDTH", "B_End_LAST_MILE_Provider", "B_END_LAST_MILE_BANDWIDTH", "A_END_SITE_CITY",
				"A_END_SITE_ADDRESS", "B_END_SITE_CITY", "B_END_SITE_ADDRESS" };
		Sheet sheet = workbook.createSheet("GVPN");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// context class obj for initializing row and rowCount
		Context context = new Context();

		// Create a Row
		Row headerRow = sheet.createRow(0);

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		// Create cells
		// Create Other rows and cells with product family data
		serviceInfoBeans.forEach(nextBean -> {
			context.rowCount++;
			context.row = sheet.createRow(context.rowCount);
			context.row.createCell(0).setCellValue(nextBean.getOrderCustomer());
			context.row.createCell(1).setCellValue(nextBean.getOrderCustLeName());
			context.row.createCell(2).setCellValue(nextBean.getProductFamilyName());
			context.row.createCell(3).setCellValue(nextBean.getProductOfferingName());
			context.row.createCell(4).setCellValue(nextBean.getOrderSysId());
			context.row.createCell(5).setCellValue(nextBean.getCustomerServiceId());
			context.row.createCell(6).setCellValue(nextBean.getSiteAlias());
			context.row.createCell(7).setCellValue(nextBean.getServiceStatus());
			if (nextBean.getCommissionedDate() != null) {
				DateFormat formatter = new SimpleDateFormat(WebexConstants.DDMMYYYY);
				context.row.createCell(8).setCellValue(formatter.format(nextBean.getCommissionedDate()));
			}
			context.row.createCell(9).setCellValue(nextBean.getServiceManagementOption());
			context.row.createCell(10).setCellValue("");
			context.row.createCell(11).setCellValue(nextBean.getPriSec());
			context.row.createCell(12).setCellValue(nextBean.getPriSecLink());
			context.row.createCell(13)
					.setCellValue(nextBean.getBandwidth() + WebexConstants.SPACE + nextBean.getBandwidthUnit());
			context.row.createCell(14).setCellValue(nextBean.getAccessTopology());
			context.row.createCell(15).setCellValue(nextBean.getRoutingProtocol());
			context.row.createCell(16).setCellValue(nextBean.getSiteEndInterface());
			context.row.createCell(17).setCellValue(" ");
			context.row.createCell(18).setCellValue(nextBean.getLastmileProvider());
			context.row.createCell(19).setCellValue(
					nextBean.getLastmileBandwidth() + WebexConstants.SPACE + nextBean.getLastmileBandwidthUnit());
			context.row.createCell(20).setCellValue(" ");
			context.row.createCell(21).setCellValue(" ");
			context.row.createCell(22).setCellValue(nextBean.getSourceCity());

			CellStyle style = workbook.createCellStyle(); // Create new style
			style.setWrapText(true); // Set wordwrap
			context.row.createCell(23).setCellValue(nextBean.getCustomerSiteAddress());
			context.row.getCell(23).setCellStyle(style);
			context.row.createCell(24).setCellValue(nextBean.getDestinationCity());
			context.row.createCell(25).setCellValue(nextBean.getPopAddress());
			context.row.getCell(25).setCellStyle(style);
		});
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "GVPN" + ".xlsx" + "\"");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			try {
				outByteStream.flush();
				outByteStream.close();
			} catch (Exception e) {
				LOGGER.warn(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * create Opportunity in sfdc
	 *
	 * @param context
	 * @return
	 */
	private WebexQuoteContext createOpportunityInSfdc(WebexQuoteContext context) {
		try {
			// Triggering Sfdc Opportunity Creation
			if (Objects.nonNull(context.quoteToLe)
					&& StringUtils.isEmpty(context.webexQuoteDataBean.getEngagementOptyId())) {
				ucaasOmsSfdcComponent.getOmsSfdcService().processCreateOpty(context.quoteToLe,
						context.webexQuoteDataBean.getProductFamilyName());
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return context;
	}

	/**
	 * create product service in sfdc
	 *
	 * @param context
	 */
	private WebexQuoteContext updateProductServiceInSfdc(WebexQuoteContext context) {
		try {
			if (context.isAccessTypeChange) {
				String productSolutionCode = context.webexQuoteDataBean.getSolutions().stream().findFirst().get()
						.getSolutionCode();
				ProductSolution productSolution = productSolutionRepository.findBySolutionCode(productSolutionCode)
						.stream().findFirst().get();
				ucaasOmsSfdcComponent.getOmsSfdcService().processProductServiceForSolution(context.quoteToLe,
						productSolution, context.quoteToLe.getTpsSfdcOptyId());
				ProductSolution ucaasSolution = productSolutionRepository
						.findBySolutionCode(context.webexQuoteDataBean.getWebexSolution().getSolutionCode()).stream()
						.findFirst().get();
				ucaasOmsSfdcComponent.getOmsSfdcService().processUpdateProductForUCAAS(context.quoteToLe,
						ucaasSolution);
			} else {
				ucaasOmsSfdcComponent.getOmsSfdcService().processUpdateProductForUcaasAndGsc(context.quoteToLe);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
		return context;
	}

	/**
	 * Get product family name.
	 *
	 * @param quoteToLe
	 * @return
	 */
	public String getProductFamily(QuoteToLe quoteToLe) {
		return quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()).stream().findFirst().get()
				.getMstProductFamily().getName();
	}


	/**
	 *  Method to update endpoint site address and
	 *  populating unique location ids in quote ucaas site details.
	 * @param quoteUcaasBeans
	 * @param quoteId
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	public List<QuoteUcaasBean> updateEndpointSiteAddress(List<QuoteUcaasBean> quoteUcaasBeans,Integer quoteId,
														   Integer solutionId)
			throws TclCommonException {

		Quote quote = getQuote(quoteId);
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).stream().findFirst().get();

		// Deleting all the unique location Ids in quote ucaas site details
		List<QuoteUcaasSiteDetails> quoteUcaasSiteDetails = quoteUcaasSiteDetailsRepository
				.findByProductSolutionId(solutionId);
		quoteUcaasSiteDetailsRepository.deleteAll(quoteUcaasSiteDetails);

		// Updating site address in location through queue.
		quoteUcaasBeans.forEach(quoteUcaasBean -> {
			if(quoteUcaasBean.getIsEndpoint()){
				QuoteUcaas quoteUcaas = quoteUcaasRepository.findById(quoteUcaasBean.getId()).get();
				quoteUcaas.setIsUpdated((quoteUcaasBean.getIsUpdated()) ? (byte) 1 : (byte) 0);
				LocationDetail locationDetail = new LocationDetail();
				AddressDetail addressDetail = new AddressDetail();
				SiteAddress siteAddress = quoteUcaasBean.getEndpointDetails().getSiteAddress();
				EndpointDetails endpointDetails = quoteUcaasBean.getEndpointDetails();
				addressDetail.setAddressLineOne(siteAddress.getAddressLineOne());
				addressDetail.setAddressLineTwo(siteAddress.getAddressLineTwo());
				addressDetail.setLocality(siteAddress.getAddressLineThree());
				addressDetail.setCity(siteAddress.getCity());
				addressDetail.setCountry(siteAddress.getCountry());
				addressDetail.setState(siteAddress.getState());
				addressDetail.setPincode(siteAddress.getPinCode());
				locationDetail.setAddress(addressDetail);
				locationDetail.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				try {
					String responseFromQueue = (String) mqUtils.sendAndReceive(locationDetailQueue,Utils.convertObjectToJson(locationDetail));
					LOGGER.info("Response From Queue::{}",responseFromQueue);
					Integer locationId = !StringUtils.isEmpty(responseFromQueue)?
							Integer.valueOf(responseFromQueue):null;
					quoteUcaasBean.getEndpointDetails().setLocationId(locationId);
					quoteUcaas.setEndpointLocationId(locationId);
					quoteUcaas.setEndpointManagementType(endpointDetails.getEndpointManagementType());
					quoteUcaas.setContractType(endpointDetails.getContractType());
					quoteUcaasRepository.save(quoteUcaas);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		});


		Set<Integer> locationIds = quoteUcaasRepository.findByQuoteToLeIdAndLocationNotNull(quoteToLe.getId()).stream().
				map(quoteUcaas -> quoteUcaas.getEndpointLocationId()).collect(Collectors.toSet());

		ProductSolution productSolution = getProductSolution(solutionId);

		// Storing all the unique ids in quote ucaas site details.
		if(Objects.nonNull(locationIds) && !locationIds.isEmpty()){
			locationIds.forEach(id -> {
				QuoteUcaasSiteDetails quoteUcaasSiteDetail = new QuoteUcaasSiteDetails();
				quoteUcaasSiteDetail.setSiteCode(Utils.generateUid());
				quoteUcaasSiteDetail.setEndpointLocationId(id);
				quoteUcaasSiteDetail.setCreatedTime(new Date());
				quoteUcaasSiteDetail.setCreatedBy(WebexConstants.UCAAS);
				quoteUcaasSiteDetail.setProductSolution(productSolution);
				quoteUcaasSiteDetailsRepository.save(quoteUcaasSiteDetail);
			});
		}
		return quoteUcaasBeans;
	}

	/**
	 * Method to get countries from product catalogue.
	 * @return
	 */
	public List<String> getCountriesFromProductCatalogue(){

		// Hardcoding as India since no product catalogue is build.
		// Will be done once product catalogue is in place.
		// Refer jira UW-202.
		List<String> countries = new ArrayList<>();
		countries.add(WebexConstants.INDIA);
		return countries;
	}

	/**
	 * To convert currency value
	 *
	 * @param currencyValue
	 * @param inputCurrency
	 * @param exstnCurConvsnRate
	 * @param inputCurConvsnRate
	 * @return
	 */
	private Double convertCurrencyValue(Double currencyValue, String inputCurrency, String exstnCurConvsnRate,
			String inputCurConvsnRate) {
		Double usdConversion = currencyValue / Double.parseDouble(exstnCurConvsnRate);
		LOGGER.info("Input Currency is :  {}  and currency value is : {}   ", inputCurrency, currencyValue);
		currencyValue = usdConversion * Double.parseDouble(inputCurConvsnRate);
		LOGGER.info(" currency value after is  : {} ", currencyValue);
		return currencyValue;
	}

	/**
	 * Convert Webex Voice prices from existing to input Currency
	 *
	 * @param pricing
	 * @return
	 */
	public List<WebexPriceConversionBean> convertWebexVoicePrices(List<WebexPriceConversionBean> pricing) {
		if (Objects.nonNull(pricing)) {
			String existingConvRate = omsUtilService
					.findCurrencyConversionRate(pricing.stream().findAny().get().getExistingCurrency());
			String inputCurrRate = omsUtilService
					.findCurrencyConversionRate(pricing.stream().findAny().get().getInputCurrency());
			pricing.forEach(productPrices -> {
				if (Objects.nonNull(productPrices.getMrc()))
					productPrices.setMrc(BigDecimal.valueOf(convertCurrencyValue(productPrices.getMrc().doubleValue(),
							productPrices.getInputCurrency(), existingConvRate, inputCurrRate)));
				if (Objects.nonNull(productPrices.getNrc()))
					productPrices.setNrc(BigDecimal.valueOf(convertCurrencyValue(productPrices.getNrc().doubleValue(),
							productPrices.getInputCurrency(), existingConvRate, inputCurrRate)));
				if (Objects.nonNull(productPrices.getHighRate()))
					productPrices.setHighRate(
							BigDecimal.valueOf(convertCurrencyValue(productPrices.getHighRate().doubleValue(),
									productPrices.getInputCurrency(), existingConvRate, inputCurrRate)));
			});
		}

		return pricing;
	}

	/**
	 * Convert Outbound prices from existing to input Currency
	 *
	 * @param pricing
	 * @return
	 */
	public List<OutboundPriceConversionBean> convertOutboundPrices(List<OutboundPriceConversionBean> pricing) {
		if (Objects.nonNull(pricing)) {
			String existingConvRate = omsUtilService
					.findCurrencyConversionRate(pricing.stream().findAny().get().getExistingCurrency());
			String inputCurrRate = omsUtilService
					.findCurrencyConversionRate(pricing.stream().findAny().get().getInputCurrency());
			pricing.stream().filter(price -> Objects.nonNull(price.getHighRate())).forEach(
					price -> price.setEnterpriseSalesFloor(convertCurrencyValue(price.getEnterpriseSalesFloor(),
							price.getInputCurrency(), existingConvRate, inputCurrRate)));
		}
		return pricing;
	}

	/**
	 * Method to test list quote response.
	 *
	 * @param dealId
	 * @return
	 * @throws JAXBException
	 * @throws TclCommonException
	 * @throws InterruptedException
	 */
	public ShowQuoteType testListQuoteResponse(String dealId)
			throws JAXBException, TclCommonException, InterruptedException {
		AuthBean authBean = getAccessToken(0);
		GetQuoteType listQuoteRequest = constructListQuoteRequest(dealId);
//		ShowQuoteType listQuoteResponse = getListQuoteResponse(listQuoteRequest, headers, 0);
		JAXBElement<ShowQuoteType> listQuoteResponse = (JAXBElement<ShowQuoteType>) soapConnector.webServiceWithCallAction(
				listQuote,listQuoteRequest,new WebexHttpHeaderCallback(WebexConstants.AUTHORIZATION,
						WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken(), ""));
		return listQuoteResponse.getValue();
	}

	/**
	 * Method to test acquire quote response.
	 *
	 * @param dealId
	 * @return
	 * @throws InterruptedException
	 * @throws JAXBException
	 * @throws TclCommonException
	 */
	public ShowQuoteType testAcquireQuoteResponse(String dealId)
			throws InterruptedException, JAXBException, TclCommonException {
		AuthBean authBean = getAccessToken(0);
		Map<String, List<String>> headers = new HashMap<>();
		headers.put(WebexConstants.AUTHORIZATION,
				Collections.singletonList(WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken()));

		ShowQuoteType listQuoteResponse = testListQuoteResponse(dealId);
		JAXBElement<ShowQuoteType> acquireQuoteResponse = null;
		List<ConfigurationMessagesType> configurationMessagesTypes = listQuoteResponse.getDataArea().getQuote().stream()
				.filter(quoteType -> Objects.nonNull(quoteType)).findAny().get().getQuoteHeader().getUserArea()
				.getCiscoExtensions().getCiscoHeader().getConfigurationMessages();
		if (Objects.nonNull(listQuoteResponse) && configurationMessagesTypes.stream()
				.anyMatch(configurationMessagesType -> WebexConstants.CISCO_SUCCESS_CODE
						.equals(configurationMessagesType.getID().getValue()))) {
			String ciscoQuoteId = listQuoteResponse.getDataArea().getQuote().stream()
					.filter(quote -> !quote.getQuoteHeader().getDocumentID().getID().getValue().isEmpty()).findAny()
					.map(id -> id.getQuoteHeader().getDocumentID().getID().getValue()).get();
			GetQuoteType acquireQuoteRequest = constructAcquireQuoteRequest(ciscoQuoteId);
//			acquireQuoteResponse = getAcquireQuoteResponse(acquireQuoteRequest, headers, 0);

			acquireQuoteResponse = (JAXBElement<ShowQuoteType>) soapConnector.webServiceWithCallAction(
					acquireQuote,acquireQuoteRequest,new WebexHttpHeaderCallback(WebexConstants.AUTHORIZATION,
							WebexConstants.BEARER + WebexConstants.SPACE + authBean.getAccessToken(), ""));
		}
		return acquireQuoteResponse.getValue();
	}
}
