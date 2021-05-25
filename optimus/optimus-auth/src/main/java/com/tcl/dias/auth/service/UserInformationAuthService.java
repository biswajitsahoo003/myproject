package com.tcl.dias.auth.service;

import static com.tcl.dias.auth.constants.Constants.PARTNER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.auth.beans.CustomerLe;
import com.tcl.dias.auth.beans.EngagementBean;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeListBean;
import com.tcl.dias.common.beans.CustomerLeListListBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.UserAccessDetailsBean;
import com.tcl.dias.common.beans.UserProductsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.service.AuthTokenService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.ImpersonationUserAudit;
import com.tcl.dias.oms.entity.entities.Partner;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserGroupToLe;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;
import com.tcl.dias.oms.entity.entities.UserToPartnerLe;
import com.tcl.dias.oms.entity.entities.UserToUserGroup;
import com.tcl.dias.oms.entity.entities.UsergroupToPartnerLe;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.ImpersonationUserAuditRepository;
import com.tcl.dias.oms.entity.repository.MstGroupTypeRepository;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.entity.repository.UserGroupToLeRepository;
import com.tcl.dias.oms.entity.repository.UserGroupToPartnerLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.entity.repository.UserToCustomerLeRepository;
import com.tcl.dias.oms.entity.repository.UserToPartnerLeRepository;
import com.tcl.dias.oms.entity.repository.UserToUserGroupRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * @author Manojkumar R
 *
 */
@Service
@Transactional
public class UserInformationAuthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserInformationAuthService.class);

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	UserGroupToPartnerLeRepository userGroupToPartnerLeRepository;

	@Autowired
	private UserRepository userRepository;

	@Value("${token.purge.time}")
	private long purgeTime;

	@Value("${rabbitmq.customer.get.all.usg}")
	String getAllCustomerUsgAll;
	
	@Value("${rabbitmq.product.get.all}")
	String productDetailsQueue;

	@Autowired
	RestClientService restClientService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	UserService userService;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	EngagementRepository enagagementRepository;

	@Value("${rabbitmq.customerlename.queue}")
	String customerIdsDetailsQueue;

	@Value("${usermanagement.user.path}")
	String usermanagementPath;

	@Autowired
	KeycloakService keyCloakService;

	@Autowired
	UserToCustomerLeRepository userToCustomerLeRepository;

	@Autowired
	PartnerRepository partnerRepository;

	@Value("${rabbitmq.get.partner.legal.entities}")
	String partnerIdsDetailsQueue;

	@Value("${rabbitmq.customer.details.by.le.queue}")
	String customerLeDetailsQueue;

	@Value("${application.env}")
	String appEnv;
	
	@Autowired
	MstGroupTypeRepository mstGroupTypeRepository;
	
	@Autowired
	UserGroupToLeRepository userGroupToLeRepository;
	
	@Autowired
	UserToUserGroupRepository userToUserGroupRepository;
	
	@Autowired
	UserToPartnerLeRepository userToPartnerLeRepository;
	
	@Autowired
	ImpersonationUserAuditRepository impersonationUserAuditRepository;

	@SuppressWarnings("unchecked")
	public boolean persistToRedisV1(String userName, String token, Map<String, Object> roleMapper)
			throws TclCommonException, IllegalArgumentException {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId(userName);
		if (Objects.isNull(roleMapper)) {
			roleMapper = getRoles(userName, token);
			ImpersonationUserAudit impersonationAudit = impersonationUserAuditRepository
					.findBySessionId(Utils.getSessionState(token));
			if (impersonationAudit != null) {
				LOGGER.info("This is a impersonated session from the user {}",
						impersonationAudit.getImpersonatingUser());
				roleMapper.put("PMI", true);
				if (impersonationAudit.getIsImpReadOnly() != null
						&& impersonationAudit.getIsImpReadOnly().equals(CommonConstants.BACTIVE)) {
					roleMapper.put("PMIRO", true);
				}
			}
		}
		userInformation.setRole((ArrayList<String>) roleMapper.get("ROLES"));
		userInformation.setActions((Map<String, Object>) roleMapper.get("ACTIONS"));
		userInformation.setPmi((Boolean) roleMapper.get("PMI"));
		userInformation.setPmiro((Boolean) roleMapper.get("PMIRO"));
		com.tcl.dias.oms.entity.entities.User user = populateUser(userInformation, userName);

		if (PARTNER.equalsIgnoreCase(user.getUserType())) {
			getPartnerDetailsV1(userInformation, user, roleMapper);
		} else {
			getCustomerDetailsV1(userInformation, user, roleMapper);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void getCustomerDetailsV1(UserInformation userInformation, User userEntity, Map<String, Object> roleMapper)
			throws TclCommonException, IllegalArgumentException {
		List<CustomerDetail> customers = new ArrayList<>();
		userInformation.setCustomers(customers);
		if (userEntity != null) {
			List<UserToUserGroup> userAllUserGroups = userToUserGroupRepository.findByUserIdAndMstUserGroupAndGroupName(
					userEntity.getId(), "OPTIMUS_SALES", CommonConstants.USG_ALL);
			if (!userAllUserGroups.isEmpty()) {
				mapAllCustomersForUserGroupAll(customers);
			} else {
				LOGGER.info("Inside else condition for getting customer details for userId {}", userEntity.getId());
				List<UserToUserGroup> userToUserGroups = userToUserGroupRepository
						.findByUserIdAndMstUserGroup(userEntity.getId(), "OPTIMUS_SALES");
				LOGGER.info("Total userToUserGroups fetched  {}", userToUserGroups.size());
				for (UserToUserGroup userToUserGroup : userToUserGroups) {
					List<UserGroupToLe> userGroupToLes = userGroupToLeRepository
							.findByMstUserGroups(userToUserGroup.getMstUserGroup());
					LOGGER.info("Total userGroupToLes fetched  {}", userGroupToLes.size());
					for (UserGroupToLe userGroupToLe : userGroupToLes) {
						if (userGroupToLe.getCustomer() != null) {
							CustomerDetail customerDetail = new CustomerDetail();
							customerDetail.setCustomerLeId(userGroupToLe.getErfCusCustomerLeId());
							customerDetail.setCustomerCode(userGroupToLe.getCustomer().getCustomerCode());
							customerDetail.setCustomerEmailId(userGroupToLe.getCustomer().getCustomerEmailId());
							customerDetail.setCustomerId(userGroupToLe.getCustomer().getId());
							customerDetail.setErfCustomerId(userGroupToLe.getCustomer().getErfCusCustomerId());
							customerDetail.setStatus(userGroupToLe.getCustomer().getStatus());
							customerDetail.setCustomerName(userGroupToLe.getCustomer().getCustomerName());
							customers.add(customerDetail);
						}
					}
				}
				List<UserToCustomerLe> userToCustomerLeList = userToCustomerLeRepository.findByUser(userEntity);
				for (UserToCustomerLe userToCustomerLe : userToCustomerLeList) {
					if (userToCustomerLe.getCustomerId() != null) {
						Customer customer = userToCustomerLe.getCustomerId();
						CustomerDetail customerDetail = new CustomerDetail();
						customerDetail.setCustomerLeId(userToCustomerLe.getErfCustomerLeId());
						customerDetail.setCustomerId(customer.getId());
						customerDetail.setCustomerCode(customer.getCustomerCode());
						customerDetail.setCustomerEmailId(customer.getCustomerEmailId());
						customerDetail.setErfCustomerId(customer.getErfCusCustomerId());
						customerDetail.setStatus(customer.getStatus());
						customerDetail.setCustomerName(customer.getCustomerName());
						customers.add(customerDetail);
					}
				}
			}
			if (roleMapper != null && roleMapper.get("USER_GROUPS") == null) {
				List<UserToUserGroup> userGroups = userToUserGroupRepository.findByUsername(userEntity.getUsername());
				ArrayList<String> usersTouserGroups = new ArrayList<>();
				for (UserToUserGroup userToUserGroup : userGroups) {
					usersTouserGroups.add(userToUserGroup.getMstUserGroup().getGroupName());
				}
				userInformation.setUserToUserGroupName(usersTouserGroups);
			} else {
				userInformation.setUserToUserGroupName((ArrayList<String>) roleMapper.get("USER_GROUPS"));
			}
			LOGGER.info("userInformation {}", userInformation);
			authTokenService.save(userInformation, purgeTime);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getPartnerDetailsV1(UserInformation userInformation, User userEntity, Map<String, Object> roleMapper) {
		List<PartnerDetail> partners = new ArrayList<>();
		List<UserToUserGroup> userToUserGroups = userToUserGroupRepository
				.findByUserIdAndMstUserGroup(userEntity.getId(), "OPTIMUS_SALES");
		for (UserToUserGroup userToUserGroup : userToUserGroups) {
			List<UsergroupToPartnerLe> userGroupToLes = userGroupToPartnerLeRepository
					.findByMstUserGroup(userToUserGroup.getMstUserGroup());
			for (UsergroupToPartnerLe userGroupToLe : userGroupToLes) {
				if (userGroupToLe.getPartner() != null) {
					PartnerDetail partnerDetail = new PartnerDetail();
					partnerDetail.setErfPartnerId(userGroupToLe.getPartner().getErfCusPartnerId());
					partnerDetail.setPartnerLeId(userGroupToLe.getErfCusPartnerLeId());
					if (StringUtils.isBlank(userInformation.getPartnerId())) {
						userInformation.setPartnerId(userGroupToLe.getPartner().getErfCusPartnerId() + "");
					}
					partnerDetail.setPartnerCode(userGroupToLe.getPartner().getPartnerCode());
					partnerDetail.setPartnerEmailId(userGroupToLe.getPartner().getPartnerEmailId());
					partnerDetail.setPartnerId(userGroupToLe.getPartner().getId());
					partnerDetail.setStatus(userGroupToLe.getPartner().getStatus());
					partnerDetail.setPartnerName(userGroupToLe.getPartner().getPartnerName());
					partners.add(partnerDetail);
				}
			}
		}
		List<UserToPartnerLe> userToPartnerLes = userToPartnerLeRepository.findByUser(userEntity);
		for (UserToPartnerLe userToPartnerLe : userToPartnerLes) {
			if (userToPartnerLe.getPartner() != null) {
				Partner partner = userToPartnerLe.getPartner();
				PartnerDetail partnerDetail = new PartnerDetail();
				partnerDetail.setErfPartnerId(partner.getErfCusPartnerId());
				if (StringUtils.isBlank(userInformation.getPartnerId())) {
					userInformation.setPartnerId(partner.getErfCusPartnerId() + "");
				}
				partnerDetail.setPartnerLeId(userToPartnerLe.getErfCusPartnerLeId());
				partnerDetail.setPartnerCode(partner.getPartnerCode());
				partnerDetail.setPartnerEmailId(partner.getPartnerEmailId());
				partnerDetail.setPartnerId(partner.getId());
				partnerDetail.setStatus(partner.getStatus());
				partnerDetail.setPartnerName(partner.getPartnerName());
				partners.add(partnerDetail);
			}
		}
		userInformation.setPartners(partners);

		if (roleMapper != null && roleMapper.get("USER_GROUPS") == null) {
			List<UserToUserGroup> userGroups = userToUserGroupRepository.findByUsername(userEntity.getUsername());
			ArrayList<String> usersTouserGroups = new ArrayList<>();
			for (UserToUserGroup userToUserGroup : userGroups) {
				usersTouserGroups.add(userToUserGroup.getMstUserGroup().getGroupName());
			}
			userInformation.setUserToUserGroupName(usersTouserGroups);
		} else {
			userInformation.setUserToUserGroupName((ArrayList<String>) roleMapper.get("USER_GROUPS"));
		}
		authTokenService.save(userInformation, purgeTime);
	}


	private com.tcl.dias.oms.entity.entities.User populateUser(UserInformation userInformation, String userName) {
		User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
		userInformation.setFirstName(user.getFirstName());
		userInformation.setLastName(user.getLastName());
		userInformation.setUserType(user.getUserType());
		return user;
	}

	public List<Map<String, Object>> getEngagementProducts() {
		List<Map<String, Object>> engagementMapper = new ArrayList<>();
		Map<Integer, Map<String, Object>> products = new HashMap<>();
		try {

			List<CustomerLe> enge = extractEngagementDetails();
			for (CustomerLe customerLe : enge) {
				for (EngagementBean eng : customerLe.getEnagements()) {
					if (products.get(eng.getProductId()) == null) {
						Map<String, Object> engMapper = new HashMap<>();
						engMapper.put("productId", eng.getProductId());
						engMapper.put("productName", eng.getProductName());
						products.put(eng.getProductId(), engMapper);
					}
				}

			}
			engagementMapper.addAll(products.values());
		} catch (Exception e) {
			LOGGER.error("Error in getting engagement details ", e);
		}
		return engagementMapper;
	}

	/**
	 * getRoles
	 * 
	 * @param userName
	 * @param token
	 */
	public Map<String, Object> getRoles(String userName, String token) {
		LOGGER.info("inside get Roles with username as {}  and input token as {}", userName, token);
		Map<String, Object> roleMapper = new HashMap<>();
		List<String> roles = new ArrayList<>();
		roleMapper.put("ROLES", roles);
		try {
			String userManagementUrl = usermanagementPath + "users/details/OPTIMUS?username=" + userName;
			LOGGER.info("Usermanagement Url for user {} is {}", userName, userManagementUrl);
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.put("rid",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			headers.put("Authorization", "Bearer " + (token != null ? token : Utils.getToken()));
			RestResponse keycloakResponse = null;
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				keycloakResponse = restClientService.getWithProxy(userManagementUrl, headers,false);
			} else {
				keycloakResponse = restClientService.get(userManagementUrl, headers,false);
			}
			if (keycloakResponse.getStatus() == Status.SUCCESS && keycloakResponse.getData() != null) {
				LOGGER.info("Keycloak response is Success");
				String data = keycloakResponse.getData();
				JSONParser jsonParser = new JSONParser();
				JSONObject dataEnvelopeObj = (JSONObject) jsonParser.parse(data);
				JSONObject dataObj = (JSONObject) dataEnvelopeObj.get("data");
				JSONArray applications = (JSONArray) dataObj.get("application");
				for (Object application : applications) {
					Map<String, Object> actionMapper = new HashMap<>();
					JSONObject app = (JSONObject) application;
					for (Object appRoleObj : (JSONArray) app.get("appRole")) {
						JSONObject appRole = (JSONObject) appRoleObj;
						String roleName = (String) appRole.get("roleCode");
						String userType = appRole.get("userType") != null ? (String) appRole.get("userType") : null;
						JSONArray modules = (JSONArray) appRole.get("modules");
						for (Object moduleObj : modules) {
							JSONObject mod = (JSONObject) moduleObj;
							for (Object actionsObj : (JSONArray) mod.get("actions")) {
								JSONObject action = (JSONObject) actionsObj;
								String act = (String) action.get("actionCode");
								LOGGER.info("role name : {}", roleName);
								roles.add(act);
							}
						}
						actionMapper.put(roleName, modules);
						roleMapper.put("USER_TYPE", userType);
					}
					roleMapper.put("ACTIONS", actionMapper);

				}
				LOGGER.info("Data receieved from User Management {}", data);
			} else {
				LOGGER.info("Keycloak response failed with reason {}  or the data received is  {}",
						keycloakResponse.getErrorMessage(), keycloakResponse.getData());
			}
		} catch (Exception e) {
			LOGGER.warn("Exception in parsing the usermanagement {}", ExceptionUtils.getStackTrace(e));
		}

		/*
		 * // TO REMOVE KeycloakAuthenticationToken keyCloakAuth =
		 * (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
		 * .getAuthentication(); if (keyCloakAuth != null) { for (String role :
		 * keyCloakAuth.getAccount().getRoles()) { roles.add("ROLE_" + role); } }
		 */
		return roleMapper;
	}

	public List<Map<String, Object>> getEngagements() throws TclCommonException {
		List<Map<String, Object>> engMapper = new ArrayList<>();
		try {
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				engMapper = getPartnerEngagements(engMapper);
			} else {
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				List<Integer> customerLeIds = new ArrayList<>();
				if (customerDetails != null) {
					customerLeIds.addAll(customerDetails.stream()
							.map(CustomerDetail::getCustomerLeId).collect(Collectors.toList()));
					if (customerLeIds != null && !customerLeIds.isEmpty()) {
						LOGGER.info("Getting Engagement details for customer le Id {}", customerLeIds);
						List<Map<String, Object>> mstProducts = enagagementRepository
								.getProductDetailsByEngagement(customerLeIds, CommonConstants.BACTIVE);
						LOGGER.info("Response Received from Query {}", mstProducts);
						engMapper.addAll(mstProducts.stream().map(mstProduct ->  {
							Map<String, Object> engMap = new HashMap<>();
							engMap.put("productName", mstProduct.get("name"));
							engMap.put("productId", mstProduct.get("id"));
							return engMap;
						}).collect(Collectors.toList()));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting engagements ", e);
		}
		return engMapper;
	}

	private List<Map<String, Object>> getPartnerEngagements(List<Map<String, Object>> engagementMapper) {
		List<PartnerDetail> partnerDetails = userInfoUtils.getPartnerDetails();
		if (!CollectionUtils.isEmpty(partnerDetails)) {
			List<Integer> partnerLeIds = partnerDetails.stream().map(PartnerDetail::getPartnerLeId)
					.collect(Collectors.toList());
			List<Map<String, Object>> engagements = enagagementRepository
					.getPartnerEngagementDetailsGroupByProductFamily(partnerLeIds, CommonConstants.BACTIVE);
			engagementMapper.addAll(engagements.stream().map(engagement -> constructPartnerEngagementObjectMapper(engagement)).collect(Collectors.toSet()));
		}
		return engagementMapper;
	}

	private Map<String, Object> constructPartnerEngagementObjectMapper(Map<String, Object> engagement) {
		Map<String, Object> updatedEngagement = new HashMap<>();
		updatedEngagement.put("productName", engagement.get("product_family_name"));
		updatedEngagement.put("productId", engagement.get("product_family_id"));
		return updatedEngagement;
	}

	public List<CustomerLe> extractEngagementDetails() throws TclCommonException {
		List<CustomerLe> customerLeList = new ArrayList<>();
		try {
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			List<Integer> customerLeIds = new ArrayList<>();
			Set<Integer> customerLeIdSet = new HashSet<>();
			for (CustomerDetail customerDetail : customerDetails) {
				customerLeIds.add(customerDetail.getCustomerLeId());
			}
			Map<Integer, List<EngagementBean>> enagementMapper = new HashMap<>();
			/*
			 * List<Engagement> enagements =
			 * enagagementRepository.findByErfCusCustomerLeIdInAndStatus(customerLeIds,
			 * CommonConstants.BACTIVE);
			 */
			/**
			 * added productInformationBeansList for setting isMacdEnabledFlag
			 */
			Map<Integer, String> productDetailsMapper= getListOfProductCatalogDetails();
			
			
			if (customerLeIds != null && !customerLeIds.isEmpty()) {
				List<Map<String, Object>> enagements = enagagementRepository
						.getEngagementDetailsGroupByProductFamily(customerLeIds, CommonConstants.BACTIVE);

				enagements.stream().forEach(entry -> {
					if (enagementMapper.get((Integer) entry.get("customerLeId")) == null) {
						List<EngagementBean> engagementBeans = new ArrayList<>();
						EngagementBean engagementBean = new EngagementBean();
						if ((String) entry.get("productFamilyName") != null) {
							engagementBean.setProductId((Integer) entry.get("productFamilyId"));
							engagementBean.setProductName((String) entry.get("productFamilyName"));
							engagementBean.setIsMacdEnabledFlag(productDetailsMapper.get((Integer) entry.get("productFamilyId")));
							engagementBeans.add(engagementBean);
						}
						enagementMapper.put((Integer) entry.get("customerLeId"), engagementBeans);
					} else {
						List<EngagementBean> engagementBeans = enagementMapper.get((Integer) entry.get("customerLeId"));
						EngagementBean engagementBean = new EngagementBean();
						if ((String) entry.get("productFamilyName") != null) {
							engagementBean.setProductId((Integer) entry.get("productFamilyId"));
							engagementBean.setProductName((String) entry.get("productFamilyName"));
							engagementBean.setIsMacdEnabledFlag(productDetailsMapper.get((Integer) entry.get("productFamilyId")));
							engagementBeans.add(engagementBean);
						}
					}
					customerLeIdSet.add((Integer) entry.get("customerLeId"));
				});
				String response = returnCustomerInformations((new ArrayList<>(customerLeIdSet)));
				Map<Integer, String> customerDetailsMapper = new HashMap<>();
				if (response != null) {
					LOGGER.info("Response received from customer Le for Name {}", response);
					CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
							.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
					for (CustomerLeBean customerLedetail : customerLegalEntityDetailsBean.getCustomerLeDetails()) {
						customerDetailsMapper.put(customerLedetail.getLegalEntityId(),
								customerLedetail.getLegalEntityName());
					}
				}
				for (Entry<Integer, List<EngagementBean>> engag : enagementMapper.entrySet()) {
					CustomerLe customerLe = new CustomerLe();
					customerLe.setCustomerLeId(engag.getKey());
					customerLe.setEnagements(engag.getValue());
					customerLe.setCustomerLeName(customerDetailsMapper.get(engag.getKey()));
					customerLeList.add(customerLe);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerLeList;
	}

	private Map<Integer, String> getListOfProductCatalogDetails() {
		LOGGER.info("Entered in to getListOfProductCatalogDetails()");
		List<ProductInformationBean> productInformationBeansList= new ArrayList<>();
		Map<Integer, String> productDetailsMapper = new HashMap<>();
		
		String queueResponse = null;
		try {
			queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			LOGGER.info("queueResponse in getListOfProductCatalogDetails ::{}",productDetailsMapper);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (StringUtils.isNotBlank(queueResponse)) {
			productInformationBeansList = Utils.fromJson(queueResponse, new TypeReference<List<ProductInformationBean>>() {
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		
		LOGGER.info("CONSTRUCTING PRODUCT DETAILS MAPPER productDetailsMapper::{}",productDetailsMapper);
		productInformationBeansList.stream().forEach(productfamilyBean -> productDetailsMapper
				.put(productfamilyBean.getProductId(), productfamilyBean.getIsMacdEnabledFlag()));
		return productDetailsMapper;
	}
	
	//sobhan-CP product short name [CST-585]
	private Map<Integer, String> getListOfProductCatalogShortNames() {
		LOGGER.info("Entered in to getListOfProductCatalogShortNames()");
		List<ProductInformationBean> productInformationBeansList = new ArrayList<>();
		Map<Integer, String> productDetailsMapper = new HashMap<>();

		String queueResponse = null;
		try {
			queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			LOGGER.info("queueResponse in getListOfProductCatalogShortNames ::{}", productDetailsMapper);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (StringUtils.isNotBlank(queueResponse)) {
			productInformationBeansList = Utils.fromJson(queueResponse,
					new TypeReference<List<ProductInformationBean>>() {
					});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}

		LOGGER.info("CONSTRUCTING PRODUCT DETAILS MAPPER productDetailsMapper for Short names ::{}", productDetailsMapper);
		productInformationBeansList.stream().forEach(productfamilyBean -> productDetailsMapper
				.put(productfamilyBean.getProductId(), productfamilyBean.getProductShortName()));
		
		return productDetailsMapper;

	}

	/**
	 * Extract partner engagement details
	 *
	 * @return {@link List<CustomerLe>}
	 */
	public List<CustomerLe> extractPartnerEngagementDetails() {
		List<CustomerLe> customerLeList = new ArrayList<>();
		List<PartnerDetail> partnerDetails = userInfoUtils.getPartnerDetails();
		List<Integer> partnerLeIds = partnerDetails.stream().map(PartnerDetail::getPartnerLeId)
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(partnerLeIds)) {
			return getPartnerList(partnerLeIds);
		}
		return customerLeList;
	}

	/**
	 * Get partner list for partner leIds
	 *
	 * @param partnerLeIds
	 * @return {@link List<CustomerLe>}
	 */
	private List<CustomerLe> getPartnerList(List<Integer> partnerLeIds) {
		Map<Integer, List<EngagementBean>> enagementMapper = new HashMap<>();
		Map<Integer, String> partnerDetailsMapper = new HashMap<>();
		List<CustomerLe> customerLeList;

		List<Map<String, Object>> engagements = enagagementRepository
				.getPartnerEngagementDetailsGroupByProductFamily(partnerLeIds, CommonConstants.BACTIVE);
		engagements.stream().forEach(entry -> constructPartnerEngagementMapper(enagementMapper, entry));

		List<PartnerLegalEntityBean> partnerLegalEntityBeans = returnPartnerInformations(
				partnerLeIds.stream().collect(Collectors.toSet()));
		partnerLegalEntityBeans.stream().forEach(partnerLegalEntityBean -> partnerDetailsMapper
				.put(partnerLegalEntityBean.getId(), partnerLegalEntityBean.getEntityName()));

		customerLeList = enagementMapper.entrySet().stream().map(entry -> {
			CustomerLe customerLe = new CustomerLe();
			customerLe.setCustomerLeId(entry.getKey());
			customerLe.setEnagements(entry.getValue());
			customerLe.setCustomerLeName(partnerDetailsMapper.get(entry.getKey()));
			return customerLe;
		}).collect(Collectors.toList());
		return customerLeList;
	}

	/**
	 * Construct Partner Engagement Mapper
	 *
	 * @param enagementMapper
	 * @param entry
	 */
	private void constructPartnerEngagementMapper(Map<Integer, List<EngagementBean>> enagementMapper,
			Map<String, Object> entry) {
		if (Objects.isNull(enagementMapper.get(entry.get("partner_le_id")))) {
			List<EngagementBean> engagementBeans = new ArrayList<>();
			EngagementBean engagementBean = getPartnerEngagementBean(entry);
			engagementBeans.add(engagementBean);
			enagementMapper.put((Integer) entry.get("partner_le_id"), engagementBeans);
		} else {
			List<EngagementBean> engagementBeans = enagementMapper.get(entry.get("partner_le_id"));
			EngagementBean engagementBean = getPartnerEngagementBean(entry);
			engagementBeans.add(engagementBean);
		}
	}

	/**
	 * Get engagement bean from Map
	 *
	 * @param entry
	 * @return {@link EngagementBean}
	 */
	private EngagementBean getPartnerEngagementBean(Map<String, Object> entry) {
		Map<Integer, String> productDetailsMapper= getListOfProductCatalogDetails();
		EngagementBean engagementBean = new EngagementBean();
		engagementBean.setProductId((Integer) entry.get("product_family_id"));
		engagementBean.setProductName((String) entry.get("product_family_name"));
		engagementBean.setIsMacdEnabledFlag(productDetailsMapper.get((Integer) entry.get("product_family_id")));
		return engagementBean;
	}

	/**
	 * Get partner information from partner le ids
	 *
	 * @param partnerLeIds
	 * @return {@link List<PartnerLegalEntityBean>}
	 */
	private List<PartnerLegalEntityBean> returnPartnerInformations(Set<Integer> partnerLeIds) {
		List<PartnerLegalEntityBean> partnerLegalEntityBeans;
		String response;
		List<Integer> partnerLes = partnerLeIds.stream().collect(Collectors.toList());
		try {
			response = (String) mqUtils.sendAndReceive(partnerIdsDetailsQueue, Utils.convertObjectToJson(partnerLes));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LE_MQ_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (StringUtils.isNotBlank(response)) {
			partnerLegalEntityBeans = Utils.fromJson(response, new TypeReference<List<PartnerLegalEntityBean>>() {
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LE_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return partnerLegalEntityBeans;
	}

	public String returnCustomerInformations(List<Integer> customerLeIds) {
		try {
			String request = "";
			for (Integer cusLe : customerLeIds) {
				request = request + cusLe + CommonConstants.COMMA;
			}
			LOGGER.info("MDC Filter token value in before Queue call returnCustomerName {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(customerIdsDetailsQueue, request);
		} catch (Exception e) {
			LOGGER.warn("No Customer Le Name {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void mapAllCustomersForUserGroupAll(List<CustomerDetail> customers)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Customer service queue call {} ", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String response = (String) mqUtils.sendAndReceive(getAllCustomerUsgAll, null);
		if (StringUtils.isNotBlank(response)) {
			CustomerLeListListBean customerLeListListBean = (CustomerLeListListBean) Utils.convertJsonToObject(response,
					CustomerLeListListBean.class);
			if (customerLeListListBean != null && customerLeListListBean.getCustomerLeLists() != null
					&& !customerLeListListBean.getCustomerLeLists().isEmpty()) {
				List<Customer> customerEntities = getCustomerDetailsFromTheList(
						customerLeListListBean.getCustomerLeLists());
				customerLeListListBean.getCustomerLeLists().stream().forEach(customerLeListBean -> {
					if (customerLeListBean.getCustomerId() != null && customerLeListBean.getLeId() != null) {
						Optional<Customer> customerEntity = customerEntities.stream().filter(customerEnt -> customerEnt
								.getErfCusCustomerId().equals(customerLeListBean.getCustomerId())).findFirst();
						if (customerEntity.isPresent()) {
							CustomerDetail customerDetail = new CustomerDetail();
							customerDetail.setCustomerLeId(customerLeListBean.getLeId());
							customerDetail.setCustomerCode(customerEntity.get().getCustomerCode());
							customerDetail.setCustomerEmailId(customerEntity.get().getCustomerEmailId());
							customerDetail.setCustomerId(customerEntity.get().getId());
							customerDetail.setErfCustomerId(customerEntity.get().getErfCusCustomerId());
							customerDetail.setStatus(customerEntity.get().getStatus());
							customerDetail.setCustomerName(customerEntity.get().getCustomerName());
							customers.add(customerDetail);
						}
					}
				});
			}
		}
	}

	private List<Customer> getCustomerDetailsFromTheList(List<CustomerLeListBean> customerLeListBeans) {
		List<Customer> customers = new ArrayList<>();
		TreeSet<Integer> treeSet = new TreeSet<>();
		customerLeListBeans.stream().forEach(custLeList -> {
			treeSet.add(custLeList.getCustomerId());
		});
		if (!treeSet.isEmpty()) {
			List<Integer> customerList = new ArrayList<>(treeSet);
			customers = customerRepository.findByErfCusCustomerIdInAndStatus(customerList, CommonConstants.BACTIVE);
		}
		return customers;
	}

	/**
	 * getUserInformation
	 * 
	 * @return
	 */
	public UserInformation getUserInformation(String userName) {
		Map<String, Object> userInfo = authTokenService.find(userName);
		if (userInfo != null && userInfo.size() > 0) {
			return (UserInformation) userInfo.get(AuthTokenService.USER_INFORMATION);
		}
		return null;
	}

	/**
	 * Persist selected customer information in redis for partner
	 *
	 * @param customerLeId
	 * @param userInformation
	 * @return {@link List<CustomerDetail>}
	 */
	public List<CustomerDetail> persistCustomersToRedisV1(Integer customerLeId, UserInformation userInformation) {
		List<CustomerDetailBean> customerDetailBeans = getCustomerInformation(Arrays.asList(customerLeId));

		CustomerDetail customerDetail = customerDetailBeans.stream().findFirst().map(customerDetailBean -> {
			Customer customerEntity = customerRepository
					.findByErfCusCustomerIdAndStatus(customerDetailBean.getCustomerId(), CommonConstants.BACTIVE);
			if (Objects.isNull(customerEntity)) {
				Customer customer = new Customer();
				customer.setCustomerName(customerDetailBean.getCustomerName());
				customer.setErfCusCustomerId(customerDetailBean.getCustomerId());
				customer.setCustomerCode(customerDetailBean.getCustomercode());
				customer.setStatus(customerDetailBean.getStatus().byteValue());
				customerRepository.save(customer);
				return constructCustomerDetailsFromCustomer(customerLeId, customer);
			} else {
				return constructCustomerDetailsFromCustomer(customerLeId, customerEntity);
			}
		}).get();

		List<CustomerDetail> customerDetails = Arrays.asList(customerDetail);
		userInformation.setCustomers(customerDetails);
		LOGGER.info("Customer Details :: {}", userInformation.getCustomers());
		authTokenService.save(userInformation, purgeTime);
		return customerDetails;
	}

	/**
	 * Construct customerdetail bean from customer entity
	 *
	 * @param customerLeId
	 * @param customerEntity
	 * @return {@link CustomerDetail}
	 */
	private CustomerDetail constructCustomerDetailsFromCustomer(Integer customerLeId, Customer customerEntity) {
		CustomerDetail customerDetail = new CustomerDetail();
		LOGGER.info("Customer Information {} ", customerEntity.getId());
		customerDetail.setCustomerLeId(customerLeId);
		customerDetail.setCustomerCode(customerEntity.getCustomerCode());
		customerDetail.setCustomerEmailId(customerEntity.getCustomerEmailId());
		customerDetail.setCustomerId(customerEntity.getId());
		customerDetail.setErfCustomerId(customerEntity.getErfCusCustomerId());
		customerDetail.setStatus(customerEntity.getStatus());
		customerDetail.setCustomerName(customerEntity.getCustomerName());
		return customerDetail;
	}

	/**
	 * Get Customer details from customer le Queue
	 *
	 * @param customerLeIds
	 * @return {@link List<CustomerDetailBean>}
	 */
	private List<CustomerDetailBean> getCustomerInformation(List<Integer> customerLeIds) {
		List<CustomerDetailBean> customerDetailBeans;
		String response;
		try {
			response = (String) mqUtils.sendAndReceive(customerLeDetailsQueue, Utils.convertObjectToJson(customerLeIds));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (StringUtils.isNotBlank(response)) {
			customerDetailBeans = Utils.fromJson(response, new TypeReference<List<CustomerDetailBean>>() {
			});
			LOGGER.info("Customer Information from customerLeDetailsQueue{} ",
					customerDetailBeans.stream().findFirst().get().getCustomerId());
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return customerDetailBeans;
	}
	
	/**
	 * Method to fetch users engagements
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unused")
	public List<UserProductsBean> getUserEngagements() throws TclCommonException {
		List<UserProductsBean> userProdutsBeans = new ArrayList<>();
		List<Map<String, Object>> engagements = new ArrayList<>();
		Map<Integer, List<UserProductsBean>> enagementMapper = new HashMap<>();
		/** queue call for CP product short names[CST-585] added extra field productShortName*/
		Map<Integer, String> productDetailsMapper= getListOfProductCatalogShortNames();
		try {
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				LOGGER.info("Inside getUserEngagements to fetch engagement details for partner user {}", Utils.getSource());
				List<PartnerDetail> partnerDetails = userInfoUtils.getPartnerDetails();
				if (!CollectionUtils.isEmpty(partnerDetails)) {
					List<Integer> partnerLeIds = partnerDetails.stream().map(PartnerDetail::getPartnerLeId)
							.collect(Collectors.toList());
					LOGGER.info("Getting User Engagement details for partner le Id {}", partnerLeIds);
					engagements = enagagementRepository
							.getPartnerUserEngagementDetails(partnerLeIds, CommonConstants.BACTIVE);
					LOGGER.info("Response Received from Query in getUserEngagements for partner {}", engagements);
				}
			} else {
				LOGGER.info("Inside getUserEngagements to fetch engagement details for user {}", Utils.getSource());
				LOGGER.info("UserType: {}",userInfoUtils.getUserType());
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				List<Integer> customerLeIds = new ArrayList<>();
				if (customerDetails != null) {
					customerLeIds.addAll(customerDetails.stream()
							.map(CustomerDetail::getCustomerLeId).collect(Collectors.toList()));
					if (customerLeIds != null && !customerLeIds.isEmpty()) {
						LOGGER.info("Getting User Engagement details for customer le Id {}", customerLeIds);
						engagements = enagagementRepository
								.getUserEngagementDetails(customerLeIds, CommonConstants.BACTIVE);
						LOGGER.info("Response Received from Query in getUserEngagements for users {}", engagements);
					}
				}
			}
			if(!engagements.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				engagements.stream().forEach(engagement->
				userProdutsBeans.add(mapper.convertValue(engagement, UserProductsBean.class)));
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting Logged in users engagements {} ", e);
		}
		/* queue call for cp-product short names [CST-585]*/
		if (!PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			LOGGER.info("UserType:{}",userInfoUtils.getUserType());
		userProdutsBeans.stream().forEach(userprod->{
			userprod.setProductShortName(productDetailsMapper.get((Integer) userprod.getProductId()));
		});
		}
		return userProdutsBeans;
	}
}
