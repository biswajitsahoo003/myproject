package com.tcl.dias.auth.service;

import static com.tcl.dias.auth.constants.Constants.PARTNER;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.common.keycloack.bean.KeycloakUserBean;
import com.tcl.common.keycloack.bean.RolesBean;
import com.tcl.dias.auth.beans.CustomerLe;
import com.tcl.dias.auth.beans.LoginResponse;
import com.tcl.dias.auth.beans.UmResponse;
import com.tcl.dias.auth.beans.UserBean;
import com.tcl.dias.auth.beans.UserConcernRequest;
import com.tcl.dias.auth.beans.UserInfoBean;
import com.tcl.dias.auth.beans.UserLeMappingRequest;
import com.tcl.dias.auth.beans.UserMappingBean;
import com.tcl.dias.auth.beans.UserMappingResponse;
import com.tcl.dias.auth.constants.AccessRestrictionConstants;
import com.tcl.dias.auth.constants.Constants;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.common.beans.ChangePasswordBean;
import com.tcl.dias.common.beans.ConfigureOtpRequestBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.NotificationActionBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.UserAccessDetailsBean;
import com.tcl.dias.common.beans.UserGroupBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.beans.UserNotificationRequest;
import com.tcl.dias.common.beans.UserNotificationSettingsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.UserActionsConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.ImpersonationUserAudit;
import com.tcl.dias.oms.entity.entities.ImpersonationUserMapping;
import com.tcl.dias.oms.entity.entities.MstGroupType;
import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.Partner;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserGroupToLe;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;
import com.tcl.dias.oms.entity.entities.UserToPartnerLe;
import com.tcl.dias.oms.entity.entities.UserToUserGroup;
import com.tcl.dias.oms.entity.entities.UserToUserGroups;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.ImpersonationUserAuditRepository;
import com.tcl.dias.oms.entity.repository.ImpersonationUserMappingRepository;
import com.tcl.dias.oms.entity.repository.MstGroupTypeRepository;
import com.tcl.dias.oms.entity.repository.MstUserGroupsRepository;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.entity.repository.UserGroupToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.entity.repository.UserToCustomerLeRepository;
import com.tcl.dias.oms.entity.repository.UserToPartnerLeRepository;
import com.tcl.dias.oms.entity.repository.UserToUserGroupRepository;
import com.tcl.dias.oms.entity.repository.UserToUserGroupsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * @author Manojkumar R
 *
 */
@Service
@Transactional
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserInformationAuthService userInformationAuthService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	CustomerRepository customerRepository;

	@Value("${rabbitmq.customer.le.by.id}")
	String customerLeByIdQueue;

	@Autowired
	KeycloakService keycloakService;

	@Autowired
	NotificationService notificationService;

	@Value("${rabbitmq.customer.le.by.customerids}")
	String leByCustomerIdsQueue;

	@Value("${mq.user.notification.create}")
	String userNotificationSettingsCreate;

	@Value("${mq.get.all.notification.user}")
	String getUserNotificationSettings;

	@Value("${mq.user.notification.update}")
	String userNotificationSettingsUpdate;

	@Value("${rabbitmq.customer.id.by.le.id}")
	String customerIdQueue;

	@Value("${rabbitmq.partner.id.by.le.id}")
	String partnerIdQueue;

	@Autowired
	PartnerRepository partnerRepository;

	private static MissingCellPolicy xRow;

	@Autowired
	UserToCustomerLeRepository userToCustomerLeRepository;

	@Autowired
	UserToUserGroupsRepository userToUserGroupsRepository;

	@Autowired
	UserToUserGroupRepository userToUserGroupRepository;

	@Autowired
	MstGroupTypeRepository mstGroupTypeRepository;

	@Autowired
	MstUserGroupsRepository mstUserGroupsRepository;

	@Autowired
	UserGroupToLeRepository userGroupToLeRepository;

	@Autowired
	RestClientService restClientService;

	@Value("${user.management.filter.api.url}")
	String userManagementFilterApiUrl;

	@Autowired
	ImpersonationUserAuditRepository impersonationUserAuditRepository;

	@Value("${user.management.partner.psam.action}")
	String optMasterPartnerPSamAction;

	@Value("${user.management.ipc.partner.migration.action}")
	String optMasterIpcPartnerMigrationAction;
	
	@Autowired
	UserToPartnerLeRepository userToPartnerLeRepository;

	@Autowired
	ImpersonationUserMappingRepository impersonationUserMappingRepository;

	@Value("${application.env}")
	String appEnv;
	
	@Value("${usermanagement.user.path}")
	String usermanagementPath;

	/**
	 * 
	 * @param loginRequest
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public LoginResponse getUserInfo(HttpServletRequest httpServletRequest) throws TclCommonException {
		LoginResponse loginResponse = new LoginResponse();
		try {
			loginResponse.setPlatform(appEnv);
			KeycloakPrincipal<KeycloakSecurityContext> keyPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			if (keyPrincipal != null) {
				Map<String, Object> roleMapper = getUserInformations(keyPrincipal, loginResponse);

				ImpersonationUserAudit impersonationAudit = impersonationUserAuditRepository
						.findBySessionId(Utils.getSessionState(Utils.getToken()));
				if (impersonationAudit != null) {
					LOGGER.info("This is a impersonated session from the user {}",
							impersonationAudit.getImpersonatingUser());
					loginResponse.setPmi(true);
					if (impersonationAudit.getIsImpReadOnly() != null
							&& impersonationAudit.getIsImpReadOnly().equals(CommonConstants.BACTIVE)) {
						loginResponse.setPmiro(true);
					}
				}
				loginResponse.setUsername(keyPrincipal.getName());
				List<UserToUserGroup> userToUserGroups = userToUserGroupRepository
						.findByUsername(keyPrincipal.getName());
				List<Map<String, Object>> userGroups = new ArrayList<>();
				if (userToUserGroups != null && !userToUserGroups.isEmpty()) {
					userToUserGroups.stream().forEach(userToUserGroup -> {
						if (userToUserGroup.getMstUserGroup() != null) {
							Map<String, Object> usrGrp = new HashMap<>();
							usrGrp.put("groupName", userToUserGroup.getMstUserGroup().getGroupName());
							if (userToUserGroup.getMstUserGroup().getMstGroupType() != null) {
								usrGrp.put("groupType",
										userToUserGroup.getMstUserGroup().getMstGroupType().getGroupType());
							}
							userGroups.add(usrGrp);
						}
					});
				}
				String xRefValue=httpServletRequest.getHeader(AccessRestrictionConstants.XREFVALUEHEADER);
                LOGGER.info("xRefValue value={}", xRefValue);

 

                if (xRefValue != null && xRefValue.equalsIgnoreCase(AccessRestrictionConstants.XREFVALUE)) {
                    loginResponse.setInternalUser(true);
                    LOGGER.info("xRefValue value={}", xRefValue);

 

                }
				/*
				 * List<String> userGroups = new ArrayList<>(); for (UserToUserGroup
				 * userToUserGroup : userToUserGroups) { if (userToUserGroup.getMstUserGroup()
				 * != null) { userGroups.add(userToUserGroup.getMstUserGroup().getGroupName());
				 * } }
				 */
				LOGGER.info("userGroups={}", userGroups);
				loginResponse.setUserGroups(userGroups);
				loginResponse.setEngagementProducts(userInformationAuthService.getEngagements());
				roleMapper.put("USER_GROUPS", userGroups);
				roleMapper.put("PMI", loginResponse.getPmi());
				roleMapper.put("PMIRO", loginResponse.getPmiro());
				userInformationAuthService.persistToRedisV1(keyPrincipal.getName(), null, roleMapper);
			} else {
				throw new TclCommonException(ExceptionConstants.AUTH_USER_NOT_FOUND, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.TOKEN_GENERATOR_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;
	}

	public Boolean processLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws TclCommonException {
		Boolean response = true;
		try {
			LOGGER.info("Session Logged out by {}", Utils.getSource());
		} catch (Exception e) {
			LOGGER.error("Error in Logging out", e);
		}
		return response;
	}

	/**
	 * 
	 * getUserGroupList
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	public List<UserBean> getUserGroupList(String groupName) throws TclCommonException {
		List<UserBean> userGroupUsers = new ArrayList<>();
		try {
			if (groupName != null) {
				List<UserToUserGroup> userGroups = userToUserGroupRepository.findByMstUserGroup_GroupName(groupName);

				userGroups.forEach(userGroupEntity -> {
					UserBean userBean = new UserBean();
					userBean.setEmailId(userGroupEntity.getUsername());
					Optional<User> optionalUser = userRepository.findById(userGroupEntity.getUserId());
					if (optionalUser.isPresent()) {
						User user = optionalUser.get();
						userBean.setFirstName(user.getFirstName());
						userBean.setLastName(user.getLastName());
						userBean.setContactNo(user.getContactNo());
						userBean.setUsername(user.getUsername());
					}
					userGroupUsers.add(userBean);

				});

			} else {

				List<UserToUserGroup> userToUserGroups = userToUserGroupRepository.findByUsername(Utils.getSource());
				for (UserToUserGroup userToUserGroup : userToUserGroups) {
					List<UserToUserGroup> userGroups = userToUserGroupRepository
							.findByMstUserGroup_GroupName(userToUserGroup.getMstUserGroup().getGroupName());
					for (UserToUserGroup userGroupEntity : userGroups) {
						UserBean userBean = new UserBean();
						userBean.setEmailId(userGroupEntity.getUsername());
						Optional<User> optionalUser = userRepository.findById(userGroupEntity.getUserId());
						if (optionalUser.isPresent()) {
							User user = optionalUser.get();
							userBean.setFirstName(user.getFirstName());
							userBean.setLastName(user.getLastName());
							userBean.setContactNo(user.getContactNo());
							userBean.setUsername(user.getUsername());

						}
						userGroupUsers.add(userBean);
					}
					break;
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.TOKEN_GENERATOR_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userGroupUsers;
	}

	public List<CustomerLe> getEnagementDetails() throws TclCommonException {

		List<CustomerLe> customerLe = new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				customerLe = userInformationAuthService.extractPartnerEngagementDetails();
			} else {
				customerLe = userInformationAuthService.extractEngagementDetails();
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting engagement details", e);
		}
		return customerLe;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getUserInformations(KeycloakPrincipal<KeycloakSecurityContext> keyPrincipal,
			LoginResponse loginResponse) {
		final Map<String, Object> roleMapper = userInformationAuthService.getRoles(keyPrincipal.getName(),
				Utils.getToken());
		loginResponse.setRoles((List<String>) roleMapper.get("ROLES"));
		loginResponse.setUserActions((Map<String, Object>) roleMapper.get("ACTIONS"));
		loginResponse.setUserType(roleMapper.get("USER_TYPE") != null ? (String) roleMapper.get("USER_TYPE") : null);
		com.tcl.dias.oms.entity.entities.User userEntity = userRepository
				.findByUsernameAndStatus(keyPrincipal.getName(), CommonConstants.ACTIVE);
		if (userEntity != null) {
			setUserDetailsInLoginResponse(userEntity, loginResponse);
			setPartnerIdInLoginResponse(userEntity, loginResponse);
		}
		return roleMapper;
	}

	private void setUserDetailsInLoginResponse(User userEntity, LoginResponse loginResponse) {
		if (userEntity.getCustomer() != null)
			loginResponse.setCustomerId(userEntity.getCustomer().getErfCusCustomerId());
		loginResponse.setForceChangePassword(userEntity.getForceChangePassword());
		if (userEntity.getIsOtpEnabled() != null) {
			loginResponse.setIsOtpEnabled(userEntity.getIsOtpEnabled());
		}
		if (userEntity.getFirstName() != null) {
			loginResponse.setFirstName(userEntity.getFirstName());
		}
		if (userEntity.getLastName() != null) {
			loginResponse.setLastName(userEntity.getLastName());
		}
		if (userEntity.getEmailId() != null) {
			loginResponse.setEmailId(userEntity.getEmailId());
		}
		loginResponse.setShowCosMessage(userEntity.getShowCosMessage());
	}

	private void setPartnerIdInLoginResponse(User userEntity, LoginResponse loginResponse) {
		if (PARTNER.equalsIgnoreCase(userEntity.getUserType())
				&& (loginResponse.getUserActions().containsKey(optMasterPartnerPSamAction) || loginResponse.getUserActions().containsKey(optMasterIpcPartnerMigrationAction))) {
			loginResponse.setIsPartnerGeneralTermsApproved(userEntity.getIsPartnerGeneralTermsApproved());
		} else if (PARTNER.equalsIgnoreCase(userEntity.getUserType())) {
			if (userEntity.getCustomer() != null) {
				Customer customer = customerRepository.findById(userEntity.getCustomer().getId()).get();
				LOGGER.info("Partner Info :: {}", customer.getPartner().getErfCusPartnerId());
				loginResponse.setPartnerId(String.valueOf(customer.getPartner().getErfCusPartnerId()));
			}
			if (userEntity.getPartnerId() != null) {
				Optional<Partner> partner = partnerRepository.findById(userEntity.getPartnerId());
				if (partner.isPresent()) {
					LOGGER.info("Partner Info :: {}", partner.get().getErfCusPartnerId());
					loginResponse.setPartnerId(String.valueOf(partner.get().getErfCusPartnerId()));
				}
			}
			loginResponse.setIsPartnerGeneralTermsApproved(userEntity.getIsPartnerGeneralTermsApproved());
		}
	}

	public void checkForNotificationOnAddUser(UserManagementBean userManagementBean) {
		if (userManagementBean.getIsNotifyCustomer() == null) {
			userManagementBean.setIsNotifyCustomer(true);
		}
	}

	/**
	 * @author ANANDHI VIJAY addUserToKeycloak
	 * @param userManagementBean
	 * @param user
	 * @return
	 * @throws TclCommonException
	 */
	private Boolean addUserToKeycloak(UserManagementBean userManagementBean, User user, String pwd)
			throws TclCommonException {
		if (user != null && user.getId() != null) {
			if (userManagementBean.getPassword() == null) {
				userManagementBean.setPassword(pwd);
			}
			KeycloakUserBean userBean = keycloakService.constructKeycloakUserBean(userManagementBean);
			return keycloakService.createUser(userBean, userManagementBean.getRoles(),
					userManagementBean.getPassword());
		}
		return false;
	}

	/**
	 * @author ANANDHI VIJAY constructUser
	 * @param userManagementBean
	 * @return User
	 */
	private User constructUser(UserManagementBean userManagementBean) {
		User user = new User();
		Customer customerEntity = customerRepository.findByErfCusCustomerIdAndStatus(userManagementBean.getCustomerId(),
				CommonConstants.BACTIVE);
		if (customerEntity != null) {
			Customer customer = new Customer();
			customer.setId(customerEntity.getId());
			user.setCustomer(customer);
		}
		user.setContactNo(userManagementBean.getContactNumber());
		user.setDesignation(userManagementBean.getDesignation());
		user.setEmailId(userManagementBean.getEmail());
		user.setFirstName(userManagementBean.getFirstName());
		user.setLastName(userManagementBean.getLastName());
		user.setPartnerId(null);
		user.setStatus(1);
		if (userManagementBean.getEmail().toLowerCase().contains(Constants.TATA_COM_DOMAIN)) {
			user.setForceChangePassword(CommonConstants.BDEACTIVATE);
		} else {
			user.setForceChangePassword(CommonConstants.BACTIVE);
		}
		user.setUsername(userManagementBean.getUserName());
		if (userManagementBean.getUserType().equalsIgnoreCase(Constants.CUSTOMER)) {
			user.setUserType(Constants.CUSTOMER);
		} else {
			user.setUserType(Constants.SALES);
		}
		if (userManagementBean.getUserId() != null) {
			user.setId(userManagementBean.getUserId());
		}

		return user;
	}

	public void setOtpFlagOnUserCreate(User user) {
		user.setIsOtpEnabled(CommonConstants.BDEACTIVATE);
	}

	/**
	 * @author ANANDHI VIJAY constructUserManagementBean
	 * @param user
	 * @return UserManagementBean
	 */
	@Transactional
	public static UserManagementBean constructUserManagementBean(User user) {
		UserManagementBean userManagementBean = new UserManagementBean();
		if (user.getId() != null) {
			userManagementBean.setUserId(user.getId());
		}
		userManagementBean.setContactNumber(user.getContactNo());
		if (user.getCustomer() != null) {
			userManagementBean.setCustomerId(user.getCustomer().getErfCusCustomerId());
			userManagementBean.setCustomerName(user.getCustomer().getCustomerName());
		}
		userManagementBean.setDesignation(user.getDesignation());
		userManagementBean.setEmail(user.getEmailId());
		userManagementBean.setFirstName(user.getFirstName());
		userManagementBean.setLastName(user.getLastName());
		userManagementBean.setUserName(user.getUsername());
		userManagementBean.setUserType(user.getUserType());
		if (user.getIsOtpEnabled() != null) {
			userManagementBean.setIsOtpEnabled(user.getIsOtpEnabled());
		}
		return userManagementBean;
	}

	/**
	 * @author ANANDHI VIJAY appendLeNames
	 * @param userGroupBean
	 * @param customerLeList
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	private void appendLeNames(UserGroupBean userGroupBean, String customerLeList) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call appendLeNames {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String response = (String) mqUtils.sendAndReceive(customerLeByIdQueue, customerLeList);
		if (StringUtils.isNotBlank(response)) {
			List<Map<Integer, String>> customerLeBeans = (List<Map<Integer, String>>) Utils
					.convertJsonToObject(response, List.class);
			if (customerLeBeans != null && !customerLeBeans.isEmpty()
					&& userGroupBean.getUserGroupToCustomerLes() != null
					&& !userGroupBean.getUserGroupToCustomerLes().isEmpty()) {
				userGroupBean.getUserGroupToCustomerLes().stream()
						.forEach(userGroupToCustomerLeBean -> customerLeBeans.stream().forEach(map -> {

							if (userGroupToCustomerLeBean.getErfCusCustomerLeId() != null
									&& map.containsKey(userGroupToCustomerLeBean.getErfCusCustomerLeId().toString())) {
								userGroupToCustomerLeBean.setErfCusCustomerLeName(
										map.get(userGroupToCustomerLeBean.getErfCusCustomerLeId().toString()));
							}

						}));

			}
		} else {
			LOGGER.warn("customerLeByIdQueue returned empty");
		}
	}

	/**
	 * updateUser
	 * 
	 * @param userManagementBean
	 * @return UserManagementBean
	 * @throws TclCommonException
	 */
	public UserManagementBean updateUser(UserManagementBean userManagementBean) throws TclCommonException {
		if (userManagementBean == null || (Objects.isNull(userManagementBean.getUserId())
				&& Objects.isNull(userManagementBean.getUserName()))) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		} else {
			try {
				User user = null;
				if (!Objects.isNull(userManagementBean.getUserId())) {
					user = editUserIfUserIdIsPresent(userManagementBean);
				} else if (userManagementBean.getUserName() != null) {
					user = userRepository.findByUsernameAndStatus(userManagementBean.getUserName(), 1);
					if (user != null) {
						user.setForceChangePassword(userManagementBean.getForceChangePassword());
					}
				}
				editRolesOnUserEdit(userManagementBean);
				user = userRepository.saveAndFlush(user);
				return constructUserManagementBean(user);

			} catch (Exception ex) {
				LOGGER.warn("Error in updateUser {} ", ExceptionUtils.getStackTrace(ex));
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
			}
		}
	}

	/**
	 * @author ANANDHI VIJAY editUserIfUserIdIsPresent
	 * @param userManagementBean
	 * @return
	 */
	private User editUserIfUserIdIsPresent(UserManagementBean userManagementBean) {
		Optional<User> optUser = userRepository.findById(userManagementBean.getUserId());
		User user = null;
		if (optUser.isPresent()) {
			user = optUser.get();
			if (userManagementBean.getContactNumber() != null)
				user.setContactNo(userManagementBean.getContactNumber());
			if (userManagementBean.getPartnerId() != null)
				user.setPartnerId(userManagementBean.getPartnerId());
			if (userManagementBean.getFirstName() != null)
				user.setFirstName(userManagementBean.getFirstName());
			if (userManagementBean.getLastName() != null)
				user.setLastName(userManagementBean.getLastName());
			if (userManagementBean.getDesignation() != null)
				user.setDesignation(userManagementBean.getDesignation());
			if (userManagementBean.getCustomerId() != null) {
				Customer customerEntity = customerRepository
						.findByErfCusCustomerIdAndStatus(userManagementBean.getCustomerId(), CommonConstants.BACTIVE);
				if (customerEntity != null) {
					Customer customer = new Customer();
					customer.setId(customerEntity.getId());
					user.setCustomer(customer);
				}
			}
		}
		return user;
	}

	/**
	 * @author ANANDHI VIJAY editRolesOnUserEdit
	 * @param userManagementBean
	 * @throws TclCommonException
	 */
	private void editRolesOnUserEdit(UserManagementBean userManagementBean) throws TclCommonException {
		if (userManagementBean.getRoles() != null && !userManagementBean.getRoles().isEmpty()) {
			String response = keycloakService.addOrRemoveTheRoles(userManagementBean.getUserName(),
					userManagementBean.getRoles());
			if (response != null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		}
	}

	/**
	 * @author ANANDHI VIJAY getUserDetailsByUserId
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	public UserManagementBean getUserDetailsByUserId(Integer userId) throws TclCommonException {
		UserManagementBean bean = new UserManagementBean();
		try {
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {

				User userEntity = user.get();
				bean = constructUserManagementBean(userEntity);
				List<String> rolesList = keycloakService.getUserRoleDetailsByUserName(userEntity.getUsername());
				if (rolesList != null) {
					bean.setRoles(rolesList);
				}
				bean.setIsConfigureOtpEnabled(keycloakService.checkWhetherOTPEnabledForTheUser(bean.getUserName()));
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bean;
	}

	/**
	 * @author ANANDHI VIJAY getAllUserDetails
	 * @return
	 * @throws TclCommonException
	 */
	public List<UserManagementBean> getAllUserDetails() throws TclCommonException {
		try {
			List<UserManagementBean> userManagementBeanList = new ArrayList<>();
			List<User> activeUserList = userRepository.findByStatusIn(1);
			if (activeUserList == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			activeUserList.stream().forEach(user -> userManagementBeanList.add(constructUserManagementBean(user)));
			return userManagementBeanList;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY getAllUserDetails
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<UserManagementBean> searchUser(String name, Integer page, Integer size, List<Integer> userIds)
			throws TclCommonException {
		if (page == null || size == null || page <= 0 || size <= 0) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<UserManagementBean> userManagementBeanList = new ArrayList<>();
			Page<User> users = null;
			boolean impBasicFlag = userInfoUtils.isAction(UserActionsConstants.USER_IMP_BASIC);
			boolean impAllFlag = userInfoUtils.isAction(UserActionsConstants.USER_IMP_ALL);
			boolean impSalesFlag = userInfoUtils.isAction(UserActionsConstants.USER_IMP_SALES);
			Set<Integer> salesUsers = new HashSet<>();
			Specification<User> specs = null;
			if (impSalesFlag) {
				User userEntity = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				if (userEntity != null) {
					List<ImpersonationUserMapping> impUser = impersonationUserMappingRepository
							.findBySourceUserId(userEntity);
					for (ImpersonationUserMapping impersonationUserMapping : impUser) {
						User imperUser = impersonationUserMapping.getImpUserId();
						if (imperUser != null) {
							salesUsers.add(imperUser.getId());
						}
					}
				}
				List<Integer> selUsers = new ArrayList<>();
				if (userIds != null) {
					for (Integer userId : userIds) {
						if (salesUsers.contains(userId)) {
							selUsers.add(userId);
						}
					}
				}
				if ((userIds == null || userIds.isEmpty()) && StringUtils.isBlank(name)) {
					selUsers = new ArrayList<>(salesUsers);
				}
				LOGGER.info("User List size {}", selUsers.size());
				specs = UserSpecification.getUsers(name, name, name, name, selUsers);
			} else {
				specs = UserSpecification.getUsers(name, name, name, name, userIds);
			}

			users = userRepository.findAll(specs, PageRequest.of(page - 1, size));
			if (users != null) {
				for (User user : users) {
					if (impBasicFlag) {
						if (user.getImpFlag().equals(CommonConstants.BACTIVE)) {
							userManagementBeanList.add(constructUserManagementBean(user));
						}
					} else if (impAllFlag) {
						userManagementBeanList.add(constructUserManagementBean(user));
					} else if (impSalesFlag) {
						userManagementBeanList.add(constructUserManagementBean(user));
					}

				}
			}
			if (users != null) {
				return new PagedResult(userManagementBeanList, users.getTotalElements(), users.getTotalPages());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @author ANANDHI VIJAY addUserInUserNotPresentInGroupResponse
	 * @param availableUserDetails
	 * @param response
	 */
	private void addUserInUserNotPresentInGroupResponse(List<Map<String, Object>> availableUserDetails,
			List<UserManagementBean> response) {
		availableUserDetails.stream().forEach(map -> {

			UserManagementBean userManagementBean = new UserManagementBean();
			if (map.containsKey("id")) {
				userManagementBean.setUserId((Integer) map.get("id"));
			}
			if (map.containsKey("name")) {
				userManagementBean.setFirstName((String) map.get("name"));
			}
			if (map.containsKey("username")) {
				userManagementBean.setUserName((String) map.get("username"));
			}
			response.add(userManagementBean);

		});
	}

	/**
	 * @author ANANDHI VIJAY returnAllLegalEntities
	 * @param response
	 * @return
	 */
	private List<LegalEntityBean> returnAllLegalEntities(List<Map<String, Object>> response) {
		List<LegalEntityBean> leBeanList = new ArrayList<>();
		final ObjectMapper mapper = new ObjectMapper();
		response.stream().forEach(map -> {
			LegalEntityBean leBean = mapper.convertValue(map, LegalEntityBean.class);
			leBeanList.add(leBean);
		});
		return leBeanList;
	}

	/**
	 * @author ANANDHI VIJAY addUserInOms
	 * @param userManagementBean
	 * @return
	 * @throws TclCommonException
	 */
	public UserManagementBean addUserInOms(UserManagementBean userManagementBean) throws TclCommonException {
		if (userManagementBean.getEmail() == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		try {
			return constructUserManagementBean(userRepository.save(constructUserForAddingInOms(userManagementBean)));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY constructUserForAddingInOms
	 * @param userManagementBean
	 * @return
	 */
	private static User constructUserForAddingInOms(UserManagementBean userManagementBean) {
		User user = new User();
		user.setEmailId(userManagementBean.getEmail());
		user.setUsername(userManagementBean.getEmail());
		user.setContactNo("8989898989");
		user.setStatus(CommonConstants.ACTIVE);
		return user;
	}

	/**
	 * @author ANANDHI VIJAY getAllRoles
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getAllRoles() throws TclCommonException {
		final List<String> rolesList = new ArrayList<>();
		try {

			KeyCloackRoles response = keycloakService.getAllRoles();
			if (response != null && response.getRoles() != null && !response.getRoles().isEmpty()) {
				response.getRoles().stream().forEach(role -> {

					if (role.getName().contains("optimus") || role.getName().contains("OPTIMUS")) {
						rolesList.add(role.getName());
					}

				});
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return rolesList;
	}

	/**
	 * @author ANANDHI VIJAY createRoles
	 * @param role
	 * @return
	 * @throws TclCommonException
	 */
	public String createRoles(String role) throws TclCommonException {
		if (role == null) {
			throw new TclCommonException(ExceptionConstants.ERROR_CREATING_ROLE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		String roleResponse = null;
		if (checkIfRoleAlreadyPresent(role)) {
			throw new TclCommonException(ExceptionConstants.ROLE_ALREADY_PRESENT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			RolesBean inputRole = new RolesBean();
			inputRole.setName(role);
			RolesBean roleAfterSave = keycloakService.createRoles(inputRole);
			roleResponse = roleAfterSave.getName();
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return roleResponse;
	}

	private boolean checkIfRoleAlreadyPresent(String name) throws TclCommonException {
		boolean isPresent = false;
		List<String> roles = getAllRoles();
		if (roles != null && !roles.isEmpty()
				&& roles.stream().filter(role -> role.equals(name)).findFirst().isPresent()) {
			isPresent = true;
		}
		return isPresent;
	}

	/**
	 * @author ANANDHI VIJAY changePasswordForTheUser
	 * @param changePasswordBean
	 * @return
	 * @throws TclCommonException
	 */
	public ChangePasswordBean changePasswordForTheUser(ChangePasswordBean changePasswordBean)
			throws TclCommonException {
		ChangePasswordBean passwordBean = null;
		try {
			changePasswordBean.setUserName(Utils.getSource());
			passwordBean = keycloakService.changeUserPassword(changePasswordBean);
		} catch (Exception e) {
			LOGGER.warn("Error in Change Password {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return passwordBean;
	}

	/**
	 * 
	 * impersonateUser
	 * 
	 * @param userId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public HttpHeaders impersonateUser(Integer userId, HttpServletResponse response, String puplicIp)
			throws TclCommonException {
		List<String> userInfoRoles=userInfoUtils.getUserRoles();
		if (userId == null && !userInfoRoles.contains(UserActionsConstants.OPT_OMS_USER_VIEW)) {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
		}

		ImpersonationUserAudit impersonationAudit = impersonationUserAuditRepository
				.findBySessionId(Utils.getSessionState(Utils.getToken()));
		if (impersonationAudit != null) {
			LOGGER.info("This is a impersonated session from the user {}",
					impersonationAudit.getImpersonatingUser());
			throw new TclCommonException(ExceptionConstants.ERROR_NO_IMP_IMP, ResponseResource.R_CODE_ERROR);
		}
		try {
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {
				HttpHeaders header = keycloakService.impersonateKeycloakUser(user.get().getUsername());
				List<String> setCookies = null;
				Optional<Map.Entry<String, List<String>>> cookies = header.entrySet().stream()
						.filter(entry -> entry.getKey().equalsIgnoreCase("Set-Cookie")).findFirst();
				if (cookies.isPresent()) {
					setCookies = cookies.get().getValue();
				}
				String sessionId = setCookiesInThResponse(response, setCookies);
				ImpersonationUserAudit impersonationUserAudit = new ImpersonationUserAudit();
				impersonationUserAudit.setCreatedBy(Utils.getSource());
				impersonationUserAudit.setCreatedTime(new Timestamp(new Date().getTime()));
				impersonationUserAudit.setImpersonatedUser(user.get().getUsername());
				impersonationUserAudit.setImpersonatingUser(Utils.getSource());
				impersonationUserAudit.setPublicIp(puplicIp);
				impersonationUserAudit.setSessionId(sessionId);
				if(userInfoRoles.contains(UserActionsConstants.USER_IMP_RO)) {
					impersonationUserAudit.setIsImpReadOnly(CommonConstants.BACTIVE);
				}else {
					impersonationUserAudit.setIsImpReadOnly(CommonConstants.BDEACTIVATE);
				}
				impersonationUserAuditRepository.save(impersonationUserAudit);
				syncImpersonationAudit(impersonationUserAudit);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	private void syncImpersonationAudit(ImpersonationUserAudit impersonationUserAudit) throws TclCommonException {
		try {
			String userManagementUrl = usermanagementPath + "users/pmi/OPTIMUS";
			LOGGER.info("Usermanagement Url for user {} is {}", Utils.getSource(), userManagementUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.add("rid",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			headers.add("Authorization", "Bearer " + Utils.getToken());
			RestResponse keycloakResponse = null;
			Map<String,Object> request =new HashMap<>();
			request.put("sessionId", impersonationUserAudit.getSessionId());
			request.put("impersonatedUser", impersonationUserAudit.getImpersonatedUser());
			request.put("impersonatingUser", impersonationUserAudit.getImpersonatingUser());
			request.put("isImpReadOnly", impersonationUserAudit.getIsImpReadOnly());
			request.put("publicIp", impersonationUserAudit.getPublicIp());
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				keycloakResponse = restClientService.postWithProxy(userManagementUrl,Utils.convertObjectToJson(request) ,headers);
			} else {
				keycloakResponse = restClientService.post(userManagementUrl,Utils.convertObjectToJson(request) ,headers);
			}
			if (keycloakResponse.getStatus() == Status.SUCCESS && keycloakResponse.getData() != null) {
				LOGGER.info("Impersonation sync in usermanagement");
			}else {
				LOGGER.error("Error in syncing the impersonation audit");
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY setCookiesInThResponse
	 * @param response
	 * @param setCookies
	 */
	private String setCookiesInThResponse(HttpServletResponse response, List<String> setCookies) {
		String sessionState = null;
		for (String cookies : setCookies) {
			String[] cookieSpliter = cookies.split(CommonConstants.SEMI_COMMA);
			Cookie cooki = null;
			int i = 0;
			for (String cookie : cookieSpliter) {
				String[] splitter = cookie.split(CommonConstants.EQUAL);
				String key = splitter[0].trim();
				String value = null;

				if (splitter.length == 2) {
					value = splitter[1].trim();
				} else {
					break;
				}
				if (key.equals("KEYCLOAK_IDENTITY")) {
					sessionState = Utils.getSessionState(value);
				}
				if (i == 0) {
					cooki = new Cookie(key, value);
					cooki.setMaxAge(60 * 60 * 24);
					cooki.setHttpOnly(true);
					cooki.setSecure(true);
					cooki.setVersion(1);
					i++;
				}
				if (key.equals("Path")) {
					cooki.setPath(value);
				}

			}
			if (cooki != null) {
				response.addCookie(cooki);
			}
		}
		return sessionState;
	}

	/**
	 * @author ANANDHI VIJAY enableOtpForTheUser
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */

	public String enableOtpForTheUser(User user) throws TclCommonException {
		String response = null;
		Boolean responseKeycloak = keycloakService.setRequiredActionToEnableOTP(user.getUsername());
		if (responseKeycloak != null) {
			response = ResponseResource.RES_SUCCESS;
			user.setIsOtpEnabled(CommonConstants.BACTIVE);
			userRepository.saveAndFlush(user);
		}
		return response;
	}

	/**
	 * @author ANANDHI VIJAY disableOtpCredentialsForTheUser
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */

	public String disableOtpCredentialsForTheUser(String userName) throws TclCommonException {
		String response = null;
		Boolean responseKeycloak = keycloakService.disableOtpCredetialsForUser(userName);
		if (responseKeycloak != null) {
			response = ResponseResource.RES_SUCCESS;
		}
		return response;
	}

	/**
	 * @author ANANDHI VIJAY disableOtpForTheUser
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */

	public String disableOtpForTheUser(User user) throws TclCommonException {
		String response = null;
		Boolean responseKeycloak = keycloakService.disableOtpForTheUser(user.getUsername());
		if (responseKeycloak != null) {
			response = ResponseResource.RES_SUCCESS;
			user.setIsOtpEnabled(CommonConstants.BDEACTIVATE);
			userRepository.saveAndFlush(user);
		}
		return response;
	}

	/**
	 * @author ANANDHI VIJAY enable Or Disable Otp For The User
	 * @param configureOtpRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public String enableOrDisableOtpForTheUser(ConfigureOtpRequestBean configureOtpRequestBean)
			throws TclCommonException {
		String response = null;
		if (configureOtpRequestBean == null || configureOtpRequestBean.getAction() == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		User user = getUserDetailsForEnableOrDisable(configureOtpRequestBean);
		if (user != null) {
			switch (configureOtpRequestBean.getAction()) {
			case Constants.ENABLE:
				response = enableOtpForTheUser(user);
				break;
			case Constants.DISABLE:
				disableOtpCredentialsForTheUser(user.getUsername());
				response = disableOtpForTheUser(user);
				break;
			case Constants.DISABLE_OTP:
				response = disableOtpCredentialsForTheUser(user.getUsername());
				break;
			case Constants.RECONFIGURE:
				disableOtpCredentialsForTheUser(user.getUsername());
				disableOtpForTheUser(user);
				response = enableOtpForTheUser(user);
				break;
			default:
				break;
			}
		}
		return response;
	}

	private User getUserDetailsForEnableOrDisable(ConfigureOtpRequestBean configureOtpRequestBean)
			throws TclCommonException {
		if (configureOtpRequestBean.getUserId() != null) {
			Optional<User> userOptional = userRepository.findById(configureOtpRequestBean.getUserId());
			if (!userOptional.isPresent()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			return userOptional.get();
		} else {
			String userName = Utils.getSource();
			return userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
		}
	}

	private void refractorUserGroupBean(UserGroupBean userGroupBean) {
		if (userGroupBean.getAssignedCustomers() == null) {
			CustomerBean customerBean = new CustomerBean();
			customerBean.setCustomerDetailsSet(new HashSet<>());
			userGroupBean.setAssignedCustomers(customerBean);
		}
		if (userGroupBean.getCustomerIds() == null) {
			userGroupBean.setCustomerIds(new HashSet<>());
		}
		if (userGroupBean.getUserGroupToCustomerLes() == null) {
			userGroupBean.setUserGroupToCustomerLes(new HashSet<>());
		}
		if (userGroupBean.getUserGroupToUsers() == null) {
			userGroupBean.setUserGroupToUsers(new HashSet<>());
		}
		if (userGroupBean.getLeNotPresentInTheGroup() == null) {
			userGroupBean.setLeNotPresentInTheGroup(new ArrayList<>());
		}
		if (userGroupBean.getUsersNotPresentInTheUserGroup() == null) {
			userGroupBean.setUsersNotPresentInTheUserGroup(new ArrayList<>());
		}
	}

	private void addInCustomerIdsIfCustomerPresent(UserGroupBean userGroupCustomerLeBean, UserGroupBean userGroupBean) {
		List<Integer> customerIds = new ArrayList<>();
		if (userGroupBean.getAssignedCustomers() != null
				&& userGroupBean.getAssignedCustomers().getCustomerDetailsSet() != null
				&& !userGroupBean.getAssignedCustomers().getCustomerDetailsSet().isEmpty()) {
			userGroupBean.getAssignedCustomers().getCustomerDetailsSet().stream()
					.forEach(customer -> customerIds.add(customer.getCustomerId()));
		}
		if (!customerIds.isEmpty()) {
			userGroupCustomerLeBean.setCustomerIds(new HashSet<>(customerIds));
			userGroupBean.setCustomerIds(new HashSet<>(customerIds));
		}
	}

	/**
	 * @author ANANDHI VIJAY Get Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public List<NotificationActionBean> getUserNotificationSettings() throws TclCommonException {
		List<NotificationActionBean> response = new ArrayList<>();
		try {
			String userName = Utils.getSource();
			if (userName == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String request = Utils.convertObjectToJson(constructUserNotificationRequest(user.getEmailId()));
			LOGGER.info("MDC Filter token value in before Queue call getUserNotificationSettings {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));

			response = (List<NotificationActionBean>) Utils.convertJsonToObject(
					(String) mqUtils.sendAndReceive(getUserNotificationSettings, request), List.class);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private static UserNotificationRequest constructUserNotificationRequest(String userId) {
		UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
		userNotificationRequest.setUserId(userId);
		LOGGER.info("MDD Filter Value in before queue Call : {} ", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		userNotificationRequest.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		return userNotificationRequest;
	}

	/**
	 * @author ANANDHI VIJAY Update Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	public String updateUserNotificationSubscriptionDetails(List<NotificationActionBean> notificationActionBeans)
			throws TclCommonException {
		String response = null;
		if (notificationActionBeans == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String userName = Utils.getSource();
			if (userName == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
			if (user == null || user.getId() == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String request = Utils.convertObjectToJson(
					constructUserNotificationSettingBeanOnUpdate(notificationActionBeans, user.getEmailId()));
			LOGGER.info("MDC Filter token value in before Queue call updateUserNotificationSubscriptionDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			response = (String) mqUtils.sendAndReceive(userNotificationSettingsUpdate, request);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	public Map<String, String> getPmiStatus() {
		Map<String, String> pmiResponse = new HashMap<>();
		pmiResponse.put("pmi", "yes");
		pmiResponse.put("pmiro", "no");
		ImpersonationUserAudit impersonationAudit = impersonationUserAuditRepository
				.findBySessionId(Utils.getSessionState(Utils.getToken()));
		if (impersonationAudit != null) {
			LOGGER.info("This is a impersonated session from the user {}",
					impersonationAudit.getImpersonatingUser());
			pmiResponse.put("pmi", "yes");
			if (impersonationAudit.getIsImpReadOnly() != null
					&& impersonationAudit.getIsImpReadOnly().equals(CommonConstants.BACTIVE)) {
				pmiResponse.put("pmiro", "yes");
			}
		}
		return pmiResponse;
	}

	private static UserNotificationSettingsBean constructUserNotificationSettingBeanOnUpdate(
			List<NotificationActionBean> notificationActionBeans, String userId) {
		UserNotificationSettingsBean userNotificationSettingsBean = new UserNotificationSettingsBean();
		userNotificationSettingsBean.setNotificationActionDetails(notificationActionBeans);
		userNotificationSettingsBean.setUserId(userId);
		return userNotificationSettingsBean;
	}

	private Integer checkIfUserAlreadyExistsInOms(String emailId) {
		User user = userRepository.findByEmailId(emailId);
		if (user != null && user.getId() != null) {
			return user.getId();
		}
		return null;
	}

	private String checkIfUserAlreadyExistsInKeycloak(String emailId) throws TclCommonException {
		return keycloakService.getKeycloakUserIdByUserName(emailId);
	}

	private void addOrUpdateUserInKeycloak(UserManagementBean userManagementBean, User user1, String pwd)
			throws TclCommonException {
		String keyCloakUserId = checkIfUserAlreadyExistsInKeycloak(userManagementBean.getEmail());
		if (keyCloakUserId != null) {
			editRolesOnUserEdit(userManagementBean);
		} else {
			addUserToKeycloak(userManagementBean, user1, pwd);
		}
	}

	/**
	 * @author ANANDHI VIJAY This method used to create users on bulk
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public String bulkUserCreation(MultipartFile file) throws TclCommonException {
		String response = "";
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			for (Sheet sheet : workbook) {
				LOGGER.info("last row num => {}", sheet.getLastRowNum());
				LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
				for (int i = 0; i <= getLastRowWithData(sheet); i++) {
					Row row = sheet.getRow(i);
					if (row.getRowNum() > 0) {
						Cell firstNameCell = row.getCell(0, xRow.RETURN_BLANK_AS_NULL);
						Cell lastNameCell = row.getCell(1, xRow.RETURN_BLANK_AS_NULL);
						Cell emailCell = row.getCell(2, xRow.RETURN_BLANK_AS_NULL);
						Cell userTypeCell = row.getCell(3, xRow.RETURN_BLANK_AS_NULL);
						Cell customerIdCell = row.getCell(4, xRow.RETURN_BLANK_AS_NULL);
						Cell contactNoCell = row.getCell(5, xRow.RETURN_BLANK_AS_NULL);
						if (firstNameCell != null && lastNameCell != null && emailCell != null && userTypeCell != null
								&& contactNoCell != null) {

							DataFormatter formatter = new DataFormatter();
							String emailId = formatter.formatCellValue(emailCell);
							if (isValidEmailAddress(emailId)) {
								User user = userRepository.findByEmailId(emailId);
								if (user == null) {
									user = new User();
									user.setFirstName(firstNameCell.getStringCellValue());
									user.setLastName(lastNameCell.getStringCellValue());

									String val = formatter.formatCellValue(contactNoCell);
									user.setContactNo(val);
									String userType = userTypeCell.getStringCellValue();
									if (userType.equals("customer")) {
										if (formatter.formatCellValue(customerIdCell) == null) {
											LOGGER.info("Customer details not specified for {} ", emailId);
											response = response.concat("Customer details not specified for " + emailId)
													.concat("\r\n");
											continue;
										} else {
											Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(
													Integer.valueOf(formatter.formatCellValue(customerIdCell)),
													CommonConstants.BACTIVE);
											if (customer == null) {
												LOGGER.info("Customer detail is wrongly mentioned for {}", emailId);
												response = response
														.concat("Customer detail is wrongly mentioned for " + emailId)
														.concat("\r\n");
												continue;
											}
											user.setCustomer(customer);
										}
									}
									user.setUserType(userType);
									user.setUsername(emailId);
									user.setEmailId(emailId);
									user.setIsOtpEnabled(CommonConstants.BDEACTIVATE);
									user.setStatus(CommonConstants.ACTIVE);
									user.setForceChangePassword(CommonConstants.BDEACTIVATE);
									user = userRepository.saveAndFlush(user);
									if (user != null) {
										addOrUpdateUserInKeycloak(constructUserManagementBeanForBulkUser(user), user,
												Constants.DEFAULT_PD);
									}
								} else {
									LOGGER.info("User with email ID {} already exists !!!!!!!!!!!!!", emailId);
									response = response
											.concat("User with email ID " + emailId + " already exists !!!!!!!!!!!!!")
											.concat("\r\n");
								}
							} else {
								LOGGER.info("Not a valid email ID {}", emailId);
								response = response.concat("Not a valid email ID " + emailId).concat("\r\n");
								continue;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private UserManagementBean constructUserManagementBeanForBulkUser(User user) {
		UserManagementBean userManagementBean = new UserManagementBean();
		List<String> roles = new ArrayList<>();
		roles.add("OPTIMUS_MU");
		if (user.getUserType().equals("sales")) {
			roles.add("OPTIMUS_SALES");
		}
		userManagementBean.setRoles(roles);
		userManagementBean.setPassword(Constants.DEFAULT_PD);
		userManagementBean.setEmail(user.getEmailId());
		userManagementBean.setFirstName(user.getFirstName());
		userManagementBean.setLastName(user.getLastName());
		userManagementBean.setUserName(user.getEmailId());
		return userManagementBean;
	}

	private int getLastRowWithData(Sheet currentSheet) {
		int rowCount = 0;
		Iterator<Row> iter = currentSheet.rowIterator();

		while (iter.hasNext()) {
			Row r = iter.next();
			if (!this.isRowBlank(r)) {
				rowCount = r.getRowNum();
			}
		}

		return rowCount;
	}

	private boolean isRowBlank(Row r) {
		boolean ret = true;

		/*
		 * If a row is null, it must be blank.
		 */
		if (r != null) {
			Iterator<Cell> cellIter = r.cellIterator();
			/*
			 * Iterate through all cells in a row.
			 */
			while (cellIter.hasNext()) {
				/*
				 * If one of the cells in given row contains data, the row is considered not
				 * blank.
				 */
				if (!this.isCellBlank(cellIter.next())) {
					ret = false;
					break;
				}
			}
		}

		return ret;
	}

	private boolean isCellBlank(Cell c) {
		return (c == null || c.getCellTypeEnum() == CellType.BLANK);
	}

	private boolean isCellEmpty(Cell c) {
		return c == null || c.getCellTypeEnum() == CellType.BLANK
				|| (c.getCellTypeEnum() == CellType.STRING && c.getStringCellValue().isEmpty());
	}

	/**
	 * 
	 * This method is used to download bulk user template
	 * 
	 * @author ANANDHI VIJAY
	 * @param response
	 * @throws TclCommonException
	 * @throws IOException
	 */
	public void downloadBulkUserUploadTemplate(HttpServletResponse response) throws TclCommonException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet("Users");

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("First Name");
			header.createCell(1).setCellValue("Last Name");
			header.createCell(2).setCellValue("Email ID");
			header.createCell(3).setCellValue("User Type");
			header.createCell(4).setCellValue("Customer ID");
			header.createCell(5).setCellValue("Contact No");

			for (int i = 0; i < 6; i++) {
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);

				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				sheet.setDefaultColumnStyle(i, textStyle);
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "user-bulk-upload-template.xlsx";
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {

			}

			outByteStream.flush();
			outByteStream.close();
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}

	private static boolean isValidEmailAddress(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * getCustomerByName method to get list<CustomerDetailBean> for the given string
	 * 
	 * @param customerName
	 * @return
	 * @throws TclCommonException
	 */
	public List<CustomerDetailBean> getCustomerByName(String customerName) throws TclCommonException {
		try {
			Specification<Customer> spec = CustomerSpecification.getCustomers(customerName);
			List<Customer> customerList = customerRepository.findAll(spec);
			return constructCustomerDetailBean(customerList);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * listCustomerDetailBean method to construct CustomerDetailBean
	 * 
	 * @param listCustomerDetailBean
	 * @param customerList
	 */
	private List<CustomerDetailBean> constructCustomerDetailBean(List<Customer> customerList) {
		List<CustomerDetailBean> listCustomerDetailBean = new ArrayList<>();
		customerList.forEach(customer -> {
			CustomerDetailBean customerDetailBean = new CustomerDetailBean();
			customerDetailBean.setCustomerId(customer.getErfCusCustomerId());
			customerDetailBean.setCustomerName(customer.getCustomerName());
			listCustomerDetailBean.add(customerDetailBean);
		});
		return listCustomerDetailBean;
	}

	/**
	 * getUserGroupByName method to get list<UserGroupBean> for given string
	 * 
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	public List<UserGroupBean> getUserGroupByName(String groupName) throws TclCommonException {
		List<UserGroupBean> userGroupBeanList = new ArrayList<>();
		try {
			List<MstUserGroups> mstUserGroups = mstUserGroupsRepository
					.findByGroupNameContainingIgnoreCaseAndStatus(groupName, CommonConstants.BACTIVE);
			if (!Objects.isNull(mstUserGroups) && !mstUserGroups.isEmpty()) {
				mstUserGroups.stream().forEach(userGroup -> {
					UserGroupBean userGroupBean = new UserGroupBean();
					userGroupBean.setUserGroupId(userGroup.getId());
					userGroupBean.setUserGroupName(userGroup.getGroupName());
					userGroupBeanList.add(userGroupBean);
				});

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userGroupBeanList;
	}

	@SuppressWarnings("unchecked")
	public UserInfoBean pushUserDetails(UserInfoBean userRequest) throws TclCommonException {
		LOGGER.info("In push user details, user email Id {}, action {}", userRequest.getEmailId(),
				userRequest.getAction());
		if (userRequest == null || userRequest.getEmailId() == null) {
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		}

		User user = userRepository.findByEmailId(userRequest.getEmailId());
		if (userRequest.getAction() != null && userRequest.getAction().equals("DEACTIVATE")) {
			if (user != null) {
				user.setStatus(0);
				userRepository.save(user);
				LOGGER.info("user {} deactivated successfully", user.getUsername());
			}

		} else if (userRequest.getAction() != null && userRequest.getAction().equals("ACTIVATE")) {
			if (user != null) {
				user.setStatus(1);
				userRepository.save(user);
				LOGGER.info("user {} activated successfully", user.getUsername());
			}

		} else {
			if (user == null) {
				user = new User();
				if (!userRequest.getEmailId().toLowerCase().contains(Constants.TATA_COM_DOMAIN)) {
					user.setForceChangePassword(CommonConstants.BACTIVE);
				} else {
					user.setForceChangePassword(CommonConstants.BDEACTIVATE);
				}
				if (userRequest.getUserType() != null && userRequest.getUserType().toLowerCase().contains("partner")) {
					user.setIsPartnerGeneralTermsApproved(CommonConstants.BACTIVE);
					if (userRequest.getRoleActions() != null) {
						if (userRequest.getRoleActions().contains("PARTNER_PSAM")) {
							user.setCustomer(null);
							user.setPartnerId(null);
							userRequest.setPartnerId(null);
							LOGGER.info("This is a Partner psam- So populating the customer Id");
						}
					}
					if (userRequest.getPartnerId() != null) {
						Optional<Partner> partner = partnerRepository.findByErfCusPartnerId(userRequest.getPartnerId());
						if (partner.isPresent()) {
							user.setPartnerId(partner.get().getId());
							LOGGER.info("Roles Actions Present {}", userRequest.getRoleActions());
							if (userRequest.getRoleActions() != null) {
								if (userRequest.getRoleActions().contains("PARTNER_ADMIN")) {
									List<Customer> customers = customerRepository
											.findByPartnerId(partner.get().getId());
									for (Customer customer : customers) {
										user.setCustomer(customer);
									}
									LOGGER.info("This is a Partner admin- So populating the customer Id");
								} else {
									user.setCustomer(null);
								}
							}
						}
					}
				} else {
					user.setIsPartnerGeneralTermsApproved(CommonConstants.BDEACTIVATE);
					if (userRequest.getCustomerId() != null) {
						Customer customer = customerRepository.findByErfCusCustomerId(userRequest.getCustomerId());
						if (customer != null) {
							user.setCustomer(customer);
						}
					}
				}
			}
			user.setContactNo(userRequest.getContactNo());
			user.setDesignation(userRequest.getDesignation());
			user.setEmailId(userRequest.getEmailId());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setUsername(userRequest.getUsername());
			user.setStatus(userRequest.getStatus());
			user.setIsOtpEnabled(CommonConstants.BDEACTIVATE);

			if (userRequest.getUserType() != null && userRequest.getUserType().toLowerCase().contains("internal")) {
				user.setUserType(UserType.INTERNAL_USERS.toString());
			} else {
				user.setUserType(userRequest.getUserType() != null ? userRequest.getUserType().toLowerCase() : null);
			}
			userRepository.save(user);
			/**
			 * saving data to userToCustomerLe Or UsertoPartnerle
			 */
			try {
			UserMappingBean userMappingBean = new UserMappingBean();
			if(userRequest.getUserType().equalsIgnoreCase("CUSTOMER")) {
			userMappingBean.setCustomerLeIdList(userRequest.getLeId());
			}
			else if(userRequest.getUserType().equalsIgnoreCase("PARTNER")) {
			userMappingBean.setPartnerLeIdList(userRequest.getLeId());
			}
			userMappingBean.setUserId(user.getId());
			
			saveUsersLeMappings(userMappingBean);
			
			} catch(Exception e) {
			LOGGER.error("error in saving users Le Mapping {}",e);	
			}			
						
			if (userRequest.getVendorId() != null && StringUtils.isNotBlank(userRequest.getGroupRefUsername())) {
				List<UserToUserGroup> userToUserGroups = userToUserGroupRepository
						.findByUsernameAndMstUserGroupMstGroupType_GroupTypeCode(userRequest.getGroupRefUsername(),
								"SERVICE_DELIVERY");
				for (UserToUserGroup userToUserGroup : userToUserGroups) {
					List<UserToUserGroup> nUserToUserGroups = userToUserGroupRepository
							.findByUsernameAndMstUserGroup(user.getUsername(), userToUserGroup.getMstUserGroup());
					if (nUserToUserGroups.isEmpty()) {
						UserToUserGroup nUserToUserGroup = new UserToUserGroup();
						nUserToUserGroup.setMstUserGroup(userToUserGroup.getMstUserGroup());
						nUserToUserGroup.setUserId(user.getId());
						nUserToUserGroup.setUsername(user.getUsername());
						userToUserGroupRepository.save(nUserToUserGroup);
					} else {
						LOGGER.info("Group Mapping is already there {}", nUserToUserGroups.size());
					}
				}
			}
		}

		/*
		 * if (userRequest.getAction() != null &&
		 * CommonConstants.CREATE.equalsIgnoreCase(userRequest.getAction())) {
		 * LOGGER.info("In create notification request for create user"); String pwd =
		 * Utils.generatePasscode();
		 * LOGGER.info("MDC Filter token value in before Queue call addUser {} :",
		 * MDC.get(CommonConstants.MDC_TOKEN_KEY));
		 * mqUtils.sendAndReceive(userNotificationSettingsCreate,
		 * Utils.convertObjectToJson(constructUserNotificationRequest(user.getEmailId())
		 * )); if
		 * (!userRequest.getEmailId().toLowerCase().contains(Constants.TATA_COM_DOMAIN))
		 * { notificationService.nofifyCreateUser(user.getEmailId(), pwd,
		 * user.getFirstName(), true); } }
		 */
		
		return userRequest;
	}

	public void saveUserMappingDetails(UserMappingBean userMappingBean) throws TclCommonException {
		if (Objects.isNull(userMappingBean) || Objects.isNull(userMappingBean.getUserId()))
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

		if (((!Objects.isNull(userMappingBean.getCustomerLeIdList()) && userMappingBean.getCustomerLeIdList().isEmpty())
				&& (!Objects.isNull(userMappingBean.getUserGroupId()) && (userMappingBean.getUserGroupId().isEmpty())))
				|| (!Objects.isNull(userMappingBean.getUserGroupId()) && (!userMappingBean.getUserGroupId().isEmpty())
						&& Objects.isNull(userMappingBean.getUsername())))
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

		Optional<User> user = userRepository.findById(userMappingBean.getUserId());
		if (!user.isPresent())
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		if (!Objects.isNull(userMappingBean.getCustomerLeIdList())
				&& !userMappingBean.getCustomerLeIdList().isEmpty()) {
			List<UserToCustomerLe> userToCustomerLeList = new ArrayList<>();
			userMappingBean.getCustomerLeIdList().forEach(customerLeEntry -> {
				Optional<UserToCustomerLe> userToCustomerLeOpt = userToCustomerLeRepository
						.findByUserAndErfCustomerLeId(user.get(), customerLeEntry);
				if (!userToCustomerLeOpt.isPresent()) {
					UserToCustomerLe userToCustomerLe = new UserToCustomerLe();
					userToCustomerLe.setUser(user.get());
					userToCustomerLe.setErfCustomerLeId(customerLeEntry);
					userToCustomerLeList.add(userToCustomerLe);
				}

			});
			userToCustomerLeRepository.saveAll(userToCustomerLeList);
		} else if (!Objects.isNull(userMappingBean.getUserGroupId()) && !userMappingBean.getUserGroupId().isEmpty()) {
			List<UserToUserGroups> userToUserGroupList = new ArrayList<>();
			userMappingBean.getUserGroupId().forEach(userGroupEntry -> {
				MstUserGroups userGroup = new MstUserGroups();
				userGroup.setId(userGroupEntry);
				List<UserToUserGroups> userToUserGroupsList = userToUserGroupsRepository
						.findByUserAndMstUserGroups(user.get(), userGroup);
				if (userToUserGroupsList.isEmpty()) {
					UserToUserGroups userToUserGroups = new UserToUserGroups();
					userToUserGroups.setUser(user.get());
					userToUserGroups.setUsername(userMappingBean.getUsername());
					userToUserGroups.setMstUserGroups(userGroup);
					userToUserGroupList.add(userToUserGroups);
				}
			});
			userToUserGroupsRepository.saveAll(userToUserGroupList);
		}

	}

	public void saveUserGroupDetails(com.tcl.dias.auth.beans.UserGroupBean userGroupBean) throws TclCommonException {

		if (Objects.isNull(userGroupBean.getGroupName()) || Objects.isNull(userGroupBean.getCustomerLeIdList())
				|| userGroupBean.getCustomerLeIdList().isEmpty())
			throw new TclCommonException(ExceptionConstants.INVALID_REQUEST, ResponseResource.R_CODE_ERROR);

		MstGroupType mstGroupType = mstGroupTypeRepository.findByGroupTypeCode(CommonConstants.OPTIMUS_SALES);

		if (Objects.isNull(mstGroupType))
			throw new TclCommonException(ExceptionConstants.INVALID_GROUP_TYPE, ResponseResource.R_CODE_ERROR);

		MstUserGroups mstUserGroupsOpt = mstUserGroupsRepository.findByGroupName(userGroupBean.getGroupName());
		if (mstUserGroupsOpt != null)
			throw new TclCommonException(ExceptionConstants.DUPLICATE_GROUP_NAME, ResponseResource.R_CODE_ERROR);

		MstUserGroups mstUserGroups = new MstUserGroups();
		mstUserGroups.setGroupName(userGroupBean.getGroupName());
		mstUserGroups.setMstGroupType(mstGroupType);
		mstUserGroups.setStatus(CommonConstants.BACTIVE);
		mstUserGroupsRepository.save(mstUserGroups);

		List<UserGroupToLe> userGroupToLeList = new ArrayList<>();
		userGroupBean.getCustomerLeIdList().forEach(customerLeId -> {
			List<UserGroupToLe> userGroupToLeListDup = userGroupToLeRepository
					.findByMstUserGroupsAndErfCusCustomerLeId(mstUserGroups, customerLeId);
			if (userGroupToLeListDup.isEmpty()) {
				UserGroupToLe userGroupToLe = new UserGroupToLe();
				userGroupToLe.setErfCusCustomerLeId(customerLeId);
				userGroupToLe.setMstUserGroups(mstUserGroups);
				userGroupToLeList.add(userGroupToLe);
			}
		});

		userGroupToLeRepository.saveAll(userGroupToLeList);

	}

	/**
	 * Persist partner customer details based on customerleid
	 *
	 * @param customerLeId
	 * @return {@link List<CustomerDetail>}
	 */
	public List<CustomerDetail> savePartnerCustomerdetails(Integer customerLeId) {
		Objects.requireNonNull(customerLeId, "Customer Le Id cannot be null");
		UserInformation userInformation = userInfoUtils.getUserInformation();
		return userInformationAuthService.persistCustomersToRedisV1(customerLeId, userInformation);
	}

	/**
	 * syncUsers method to save/sync usermanagement users & Oms users
	 * 
	 * @param hours
	 * @throws ParseException
	 */
	public String syncUsers(Integer hours) throws TclCommonException {
		String response = null;
		try {
			RestResponse resp = getUsersFromUserManagement(hours);
			if (resp.getStatus().getStatusCode() == 1) {
				response = getUsersToSave(resp);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in syncUsers {} : ", e);
		}
		return response;
	}

	/**
	 * getUsersToAdd method to get List<UserBean> to save in user
	 * 
	 * @param resp
	 * @return
	 * @throws ParseException
	 * @throws TclCommonException
	 */
	private String getUsersToSave(RestResponse resp) throws TclCommonException {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonParsed = (JSONObject) jsonParser.parse(resp.getData());
			JSONArray jsonObject = (JSONArray) jsonParsed.get("data");
			if (!jsonObject.isEmpty()) {
				UserBean[] userBeanArray = (UserBean[]) Utils.convertJsonToObject(jsonObject.toString(),
						UserBean[].class);
				List<UserBean> userBeans = new LinkedList<>(Arrays.asList(userBeanArray));
				userBeans.stream().forEach(userBean -> {
					User user = userRepository.findByUsernameAndStatus(userBean.getUsername(), 1);
					if (user != null) {
						userBean.setUserId(user.getId().toString());
					} else {
						userBean.setUserId(null);
					}
					try {
						constructUserBean(userBean);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
				return "Users are synched successfully";
			} else {
				return "All users are upto date";
			}
		} catch (TclCommonException | ParseException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * getUsersFromUserManagement method to fetch users from user management
	 * 
	 * @param hours
	 * @return
	 */
	private RestResponse getUsersFromUserManagement(Integer hours) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("Authorization", "Bearer " + Utils.getToken());
		return restClientService.get(userManagementFilterApiUrl + hours, headers, false);
	}

	/**
	 * constructUserBean method to construct User from UserBean
	 * 
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	public UserBean constructUserBean(UserBean userRequest) throws TclCommonException {
		if (userRequest == null || userRequest.getEmailId() == null) {
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
		User newUser = new User();
		if (userRequest.getUserId() != null) {
			newUser.setId(Integer.valueOf(userRequest.getUserId()));
		} else {
			newUser.setId(null);
		}
		newUser.setContactNo(userRequest.getContactNo());
		newUser.setDesignation(userRequest.getDesignation());
		newUser.setEmailId(userRequest.getEmailId());
		newUser.setFirstName(userRequest.getFirstName());
		newUser.setLastName(userRequest.getLastName());
		newUser.setUsername(userRequest.getUsername());
		newUser.setUserType(userRequest.getUserType());
		newUser.setStatus(userRequest.getStatus());
		newUser.setPartnerId(userRequest.getPartnerId());
		userRepository.save(newUser);
		return userRequest;
	}

	/**
	 * This method get customerLe and partnerLe details for the given userid
	 * 
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	@Deprecated
	public UserMappingBean getUserMappings(Integer userId) throws TclCommonException {
		UserMappingBean userMappingBean = new UserMappingBean();
		LOGGER.info("Entering UserService.getUserMappings to fetch customerLe and PartnerLe for the userId : {} ",
				userId);
		try {
			Optional<User> user = userRepository.findById(userId);
			if (!user.isPresent()) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}
			userMappingBean.setUserId(user.get().getId());
			userMappingBean.setUsername(user.get().getUsername());
			List<UserToCustomerLe> customerLes = userToCustomerLeRepository.findByUser(user.get());
			List<Integer> customerLeIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();
			if (customerLes != null && !customerLes.isEmpty()) {
				LOGGER.info("Fetching customerLeIds for the user : {} ", user.get().getUsername());
				customerLes.stream().forEach(customerLe -> {
					customerLeIds.add(customerLe.getErfCustomerLeId());
				});
			}
			userMappingBean.setCustomerLeIdList(customerLeIds);

			List<UserToPartnerLe> partnerLes = userToPartnerLeRepository.findByUser(user.get());
			if (partnerLes != null && !partnerLes.isEmpty()) {
				LOGGER.info("Fetching partnerLeIds for the user : {} ", user.get().getUsername());
				partnerLes.stream().forEach(partnerLe -> {
					partnerLeIds.add(partnerLe.getErfCusPartnerLeId());
				});
			}
			userMappingBean.setPartnerLeIdList(partnerLeIds);
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e, ResponseResource.R_CODE_ERROR);
		}
		return userMappingBean;
	}

	/**
	 * This method is to save customerLe and partnerLe for the userid
	 * 
	 * @param userMappingBean
	 * @throws TclCommonException
	 */
	@Deprecated
	public String saveUsersLeMappings(UserMappingBean userMappingBean) throws TclCommonException {
		LOGGER.info("Inside UserService.saveUsersLeMappings to save customerLe/partnerLe for the userId {} ",
				userMappingBean.getUserId());
		String status = CommonConstants.FAILIURE;
		try {
			Optional<User> user = userRepository.findById(userMappingBean.getUserId());
			if (!user.isPresent())
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			if (!Objects.isNull(userMappingBean.getCustomerLeIdList())
					&& !userMappingBean.getCustomerLeIdList().isEmpty()) {
				List<UserToCustomerLe> userToCustomerLes = userToCustomerLeRepository
						.findByErfCustomerLeIdNotInAndUser(userMappingBean.getCustomerLeIdList(), user.get());
				if (userToCustomerLes != null && !userToCustomerLes.isEmpty()) {
					LOGGER.info(
							"Inside UserService.saveUsersLeMappings deleting unmapped customerLes {} for the userId {} ",
							userToCustomerLes, userMappingBean.getUserId());
					userToCustomerLeRepository.deleteAll(userToCustomerLes);
				} else {
					LOGGER.info(
							"Inside UserService.saveUsersLeMappings deleting customerLes mapping for the userId {} ",
							userMappingBean.getUserId());
					userToCustomerLeRepository.deleteCustomerMappings(user.get().getId());
				}
				LOGGER.info("leIds {}", userMappingBean.getCustomerLeIdList());
				Map<Integer, Integer> responseMapper = new HashMap<>();
				String response = (String) mqUtils.sendAndReceive(customerIdQueue,
						Utils.convertObjectToJson(userMappingBean.getCustomerLeIdList()));
				if (response != null) {
					responseMapper = (Map) Utils.convertJsonToObject(response, Map.class);
					LOGGER.info("Customer Id response received {}", responseMapper);
				}
				List<UserToCustomerLe> userToCustomerLeList = new ArrayList<>();
				for (Integer customerLeEntry : userMappingBean.getCustomerLeIdList()) {
					Optional<UserToCustomerLe> userToCustomerLeOpt = userToCustomerLeRepository
							.findByUserAndErfCustomerLeId(user.get(), customerLeEntry);
					if (!userToCustomerLeOpt.isPresent()) {
						UserToCustomerLe userToCustomerLe = new UserToCustomerLe();
						userToCustomerLe.setUser(user.get());
						LOGGER.info("Searching for LeId {} and result {}", customerLeEntry,
								responseMapper.get(customerLeEntry + ""));
						if (responseMapper.get(customerLeEntry + "") != null) {
							LOGGER.info("Erf customer Id is {}", responseMapper.get(customerLeEntry + ""));
							Customer customer = customerRepository
									.findByErfCusCustomerId(responseMapper.get(customerLeEntry + ""));
							LOGGER.info("Customer Id {}", customer.getId());
							userToCustomerLe.setCustomerId(customer);
						}

						userToCustomerLe.setErfCustomerLeId(customerLeEntry);
						userToCustomerLeList.add(userToCustomerLe);
					}
				}
				userToCustomerLeRepository.saveAll(userToCustomerLeList);
			} else if (!Objects.isNull(userMappingBean.getPartnerLeIdList())
					&& !userMappingBean.getPartnerLeIdList().isEmpty()) {
				List<UserToPartnerLe> userToPartnerLeList = new ArrayList<>();
				List<UserToPartnerLe> userToPartners = userToPartnerLeRepository.findByErfCusPartnerLeIdNotInAndUserId(
						userMappingBean.getPartnerLeIdList(), user.get().getId());
				if (userToPartners != null && !userToPartners.isEmpty()) {
					LOGGER.info(
							"Inside UserService.saveUsersLeMappings deleting unmapped partnerLes {} for the userId {} ",
							userToPartners, userMappingBean.getUserId());
					userToPartnerLeRepository.deleteAll(userToPartners);
				} else {
					LOGGER.info("Inside UserService.saveUsersLeMappings deleting partnerLes mapping for the userId {} ",
							userMappingBean.getUserId());
					userToPartnerLeRepository.deletePartnerMappings(user.get().getId());
				}
				LOGGER.info("leIds {}", userMappingBean.getPartnerLeIdList());
				Map<Integer, Integer> responseMapper = new HashMap<>();
				String response = (String) mqUtils.sendAndReceive(partnerIdQueue,
						Utils.convertObjectToJson(userMappingBean.getPartnerLeIdList()));
				if (response != null) {
					responseMapper = (Map) Utils.convertJsonToObject(response, Map.class);
					LOGGER.info("Partner Id response received {}", responseMapper);
				}
				for (Integer partnerLeEntry : userMappingBean.getPartnerLeIdList()) {
					Optional<UserToPartnerLe> userToPartnerLeOpt = userToPartnerLeRepository
							.findByUserIdAndErfCusPartnerLeId(user.get().getId(), partnerLeEntry);
					if (!userToPartnerLeOpt.isPresent()) {
						UserToPartnerLe userToPartnerLe = new UserToPartnerLe();
						userToPartnerLe.setUser(user.get());
						LOGGER.info("Searching for LeId {} and result {}", partnerLeEntry,
								responseMapper.get(partnerLeEntry + ""));
						if (responseMapper.get(partnerLeEntry + "") != null) {
							LOGGER.info("Erf partner Id is {}", responseMapper.get(partnerLeEntry + ""));
							Optional<Partner> partner = partnerRepository
									.findByErfCusPartnerId(responseMapper.get(partnerLeEntry + ""));
							LOGGER.info("Partner Id {}", partner.get().getId());
							userToPartnerLe.setPartner(partner.get());
						}
						userToPartnerLe.setErfCusPartnerLeId(partnerLeEntry);
						userToPartnerLeList.add(userToPartnerLe);
					}
				}
				userMappingBean.getPartnerLeIdList().forEach(partnerLeEntry -> {
					Optional<UserToPartnerLe> userToPartnerLeOpt = userToPartnerLeRepository
							.findByUserIdAndErfCusPartnerLeId(user.get().getId(), partnerLeEntry);
					if (!userToPartnerLeOpt.isPresent()) {
						UserToPartnerLe userToPartnerLe = new UserToPartnerLe();
						userToPartnerLe.setUser(user.get());
						userToPartnerLe.setErfCusPartnerLeId(partnerLeEntry);
						userToPartnerLeList.add(userToPartnerLe);
					}
				});
				userToPartnerLeRepository.saveAll(userToPartnerLeList);
			}
			status = CommonConstants.SUCCESS;
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e, ResponseResource.R_CODE_ERROR);
		}
		return status;

	}

	public void cleanCookie(HttpServletRequest httpServletRequest, HttpServletResponse reponse) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("oidc_id_token")) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					reponse.addCookie(cookie);
				}
			}

	}

	public String processCustomerConcern(UserConcernRequest userConcernRequest) {
		if (StringUtils.isBlank(userConcernRequest.getDescription())) {
			return "Description is Mandatory";
		}
		User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
		String name = user.getFirstName() + " " + user.getLastName();
		String emailId = user.getEmailId();
		if (StringUtils.isBlank(userConcernRequest.getCustomerName())) {
			userConcernRequest.setCustomerName(
					user.getCustomer() != null ? user.getCustomer().getCustomerName() : "Internal User");
		}
		if (StringUtils.isBlank(userConcernRequest.getPhoneNumber())) {
			userConcernRequest.setCustomerName(user.getContactNo());
		}
		if (StringUtils.isBlank(userConcernRequest.getPhoneNumber())) {
			userConcernRequest.setCustomerName(user.getContactNo());
		}
		notificationService.nofifyCustomerConcernUser(name, emailId, userConcernRequest);
		return "SUCCESS";
	}
	
	public PagedResult<UserMappingResponse> getUserMappingsV2(String userMode, Integer userId, Integer page,
			Integer size, String searchTxt) throws TclCommonException {
		List<UserMappingResponse> userMapping = new ArrayList<>();
		long totalSize=0;
		Integer totalPage=null;
		try {
			String validationResponse = validateUserMappings(userMode, userId);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn(validationResponse);
				throw new TclCommonException(validationResponse, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Request for getting all the user mapping information for {}, {} ,{},{},{}", userMode, userId, page,
					size, searchTxt);
			PageRequest pageable = null;
			if (page != null && size != null) {
				pageable = PageRequest.of(page-1, size);
			}
			if(userMode.equalsIgnoreCase("customer")) {
				Specification<UserToCustomerLe> groupSpecification=UserMappingSpecification.getCustomerUserMapping(userId, searchTxt);
				Page<UserToCustomerLe> pagedUserLe = userToCustomerLeRepository.findAll(groupSpecification, pageable);
				List<UserToCustomerLe> pagedUserLes = pagedUserLe.getContent();
				for (UserToCustomerLe userToCustomerLe : pagedUserLes) {
					userMapping.add(new UserMappingResponse(userToCustomerLe.getErfCustomerLeId(),userToCustomerLe.getErfCustomerLeName()));
				}
				totalSize= pagedUserLe.getTotalElements();
				totalPage= pagedUserLe.getTotalPages();
						
			}else {
				Specification<UserToPartnerLe> groupSpecification=UserMappingSpecification.getPartnerUserMapping(userId, searchTxt);
				Page<UserToPartnerLe> pagedUserLe = userToPartnerLeRepository.findAll(groupSpecification, pageable);
				List<UserToPartnerLe> pagedUserLes = pagedUserLe.getContent();
				for (UserToPartnerLe userToPartnerLe : pagedUserLes) {
					userMapping.add(new UserMappingResponse(userToPartnerLe.getErfCusPartnerLeId(),userToPartnerLe.getErfCusPartnerLeName()));
				}
				totalSize= pagedUserLe.getTotalElements();
				totalPage= pagedUserLe.getTotalPages();
			}
			
			
		} catch (Exception e) {
			LOGGER.error("Error in getUserMappingsV2 user", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult<>(userMapping,totalSize, totalPage);
	}

	/**
	 * 
	 * @param userLeMappingRequest
	 * @return
	 * @throws TclCommonException
	 */
	public UmResponse processUserMappingV2(UserLeMappingRequest userLeMappingRequest,Integer userId) throws TclCommonException {
		UmResponse usersResponse = new UmResponse("User mapped successfully!!!");
		try {
			userLeMappingRequest.setUserId(userId);
			String validationResponse = validateUserMapping(userLeMappingRequest);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn("Validation error with message {}", validationResponse);
				usersResponse.setMessage(validationResponse);
				usersResponse.setStatus(Status.FAILURE);
				return usersResponse;
			}
			Optional<User> userEntity = userRepository.findById(userLeMappingRequest.getUserId());
			if (userEntity.isPresent()) {
				if (userLeMappingRequest.getUserMode().equalsIgnoreCase("customer")) {
					validationResponse=processCustomerGroupMapping(userLeMappingRequest, userEntity);
				}
				
				if (userLeMappingRequest.getUserMode().equalsIgnoreCase("partner")) {
					validationResponse=processPartnerUserGroupMapping(userLeMappingRequest, userEntity);
				}
				if (StringUtils.isNotBlank(validationResponse)) {
					usersResponse.setMessage(validationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processUserMappingV2 user", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return usersResponse;

	}

	@SuppressWarnings("unchecked")
	private String processCustomerGroupMapping(UserLeMappingRequest userLeMappingRequest, Optional<User> userEntity)
			throws TclCommonException {
		List<String> validationMessages = new ArrayList<>();
		LOGGER.info("customer leIds {}", userLeMappingRequest.getAdditionalLegalEntites());
		Map<Integer, Integer> responseMapper = new HashMap<>();
		if (!userLeMappingRequest.getAdditionalLegalEntites().isEmpty()) {
			String response = (String) mqUtils.sendAndReceive(customerIdQueue,
					Utils.convertObjectToJson(userLeMappingRequest.getAdditionalLegalEntites().stream().map(e->e.getId()).collect(Collectors.toList())));
			if (response != null) {
				responseMapper = (Map<Integer, Integer>) Utils.convertJsonToObject(response, Map.class);
				LOGGER.info("Customer Id response received {}", responseMapper);
			}
		}
		for (UserMappingResponse deletingLegalEntity : userLeMappingRequest.getReductionalLegalEntites()) {
			Optional<UserToCustomerLe> userToCustomerLeEntity = userToCustomerLeRepository
					.findByUserAndErfCustomerLeId(userEntity.get(), deletingLegalEntity.getId());
			if (userToCustomerLeEntity.isPresent()) {
				LOGGER.info("Deleting the entry {} with customer le {}", userToCustomerLeEntity.get().getId(),
						userToCustomerLeEntity.get().getErfCustomerLeId());
				userToCustomerLeRepository.delete(userToCustomerLeEntity.get());
			}

		}

		for (UserMappingResponse addLegalEntity : userLeMappingRequest.getAdditionalLegalEntites()) {
			Optional<UserToCustomerLe> userToCustomerLeEntity = userToCustomerLeRepository
					.findByUserAndErfCustomerLeId(userEntity.get(), addLegalEntity.getId());
			if (!userToCustomerLeEntity.isPresent()) {
				LOGGER.info("Adding the entry with customer le {}",  addLegalEntity.getId());
				@SuppressWarnings("unlikely-arg-type")
				Integer erfCustomerId = responseMapper.get( addLegalEntity.getId()+"");
				if (erfCustomerId != null) {
					Customer customerEntity = customerRepository.findByErfCusCustomerId(erfCustomerId);
					if (customerEntity != null) {
						UserToCustomerLe userToCustomerLe = new UserToCustomerLe();
						userToCustomerLe.setUser(userEntity.get());
						userToCustomerLe.setErfCustomerLeId( addLegalEntity.getId());
						userToCustomerLe.setErfCustomerLeName( addLegalEntity.getEntityName());
						userToCustomerLe.setCustomerId(customerEntity);
						userToCustomerLeRepository.save(userToCustomerLe);
					} else {
						validationMessages.add("Customer record is not locally present "+ erfCustomerId);
						LOGGER.info("Invalid Customer Id in oms {}", erfCustomerId);
					}
				} else {
					validationMessages.add("Customer Legal Entity record is not present in Master "+ addLegalEntity.getId());
					LOGGER.info("Invalid Customer Le Id {}",  addLegalEntity.getId());
				}
			} else {
				validationMessages.add("Already  "+ addLegalEntity.getEntityName() + " is mapped");
				LOGGER.info("Already the le {} is assigned to {}",  addLegalEntity.getId(), userLeMappingRequest.getUserId());
			}

		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

	@SuppressWarnings("unchecked")
	private String processPartnerUserGroupMapping(UserLeMappingRequest userLeMappingRequest, Optional<User> userEntity)
			throws TclCommonException {
		List<String> validationMessages = new ArrayList<>();
		Map<Integer, Integer> responseMapper = new HashMap<>();
		if (!userLeMappingRequest.getAdditionalLegalEntites().isEmpty()) {
			String response = (String) mqUtils.sendAndReceive(partnerIdQueue,
					Utils.convertObjectToJson(userLeMappingRequest.getAdditionalLegalEntites().stream().map(e->e.getId()).collect(Collectors.toList())));
			if (response != null) {
				responseMapper = (Map<Integer, Integer>) Utils.convertJsonToObject(response, Map.class);
				LOGGER.info("Partner Id response received {}", responseMapper);
			}
		}
		for (UserMappingResponse deletingLegalEntity : userLeMappingRequest.getReductionalLegalEntites()) {
			Optional<UserToPartnerLe> userToPartnerLeEntity = userToPartnerLeRepository
					.findByUserIdAndErfCusPartnerLeId(userEntity.get().getId(), deletingLegalEntity.getId());
			if (userToPartnerLeEntity.isPresent()) {
				LOGGER.info("Deleting the entry {} with partner le {}", userToPartnerLeEntity.get().getId(),
						userToPartnerLeEntity.get().getErfCusPartnerLeId());
				userToPartnerLeRepository.delete(userToPartnerLeEntity.get());
			}

		}

		for (UserMappingResponse addLegalEntity : userLeMappingRequest.getAdditionalLegalEntites()) {
			Optional<UserToPartnerLe> userToPartnerLeEntity = userToPartnerLeRepository
					.findByUserIdAndErfCusPartnerLeId(userEntity.get().getId(), addLegalEntity.getId());
			if (!userToPartnerLeEntity.isPresent()) {
				LOGGER.info("Adding the entry with partner le {}", addLegalEntity.getId());
				@SuppressWarnings("unlikely-arg-type")
				Integer erfPartnerId = responseMapper.get(addLegalEntity.getId()+"");
				if (erfPartnerId != null) {
					Optional<Partner> partnerEntity = partnerRepository.findByErfCusPartnerId(erfPartnerId);
					if (partnerEntity.isPresent()) {
						UserToPartnerLe userToPartnerLe = new UserToPartnerLe();
						userToPartnerLe.setUser(userEntity.get());
						userToPartnerLe.setPartner(partnerEntity.get());
						userToPartnerLe.setErfCusPartnerLeId(addLegalEntity.getId());
						userToPartnerLe.setErfCusPartnerLeName(addLegalEntity.getEntityName());
						userToPartnerLeRepository.save(userToPartnerLe);
					} else {
						validationMessages.add("Partner record is not locally present "+ erfPartnerId);
						LOGGER.info("Invalid Erf Partner Id in Oms {}", erfPartnerId);
					}
				} else {
					validationMessages.add("Partner Legal Entity record is not present in Master "+ addLegalEntity.getId());
					LOGGER.info("Invalid Partner Le Id {}", addLegalEntity.getId());
				}
			} else {
				validationMessages.add("Already  "+ addLegalEntity.getEntityName() + " is mapped");
				LOGGER.info("Already the partner le {} is assigned to {}", addLegalEntity.getId(),
						userLeMappingRequest.getUserId());
			}

		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

	/**
	 * validation 
	 * @param userLeMappingRequest
	 * @return String
	 */
	private String validateUserMapping(UserLeMappingRequest userLeMappingRequest) {
		List<String> validationMessages = new ArrayList<>();
		if (userLeMappingRequest.getAdditionalLegalEntites().isEmpty()
				&& userLeMappingRequest.getReductionalLegalEntites().isEmpty()) {
			validationMessages.add("Le's are not added nor removed");
		}
		if (StringUtils.isNotBlank(userLeMappingRequest.getUserMode())) {
			if (!(userLeMappingRequest.getUserMode().equalsIgnoreCase("customer")
					|| userLeMappingRequest.getUserMode().equalsIgnoreCase("partner"))) {
				validationMessages.add("userMode can only have CUSTOMER,PARTNER");
			}

		} else {
			validationMessages.add("userMode cannot be empty/null");
		}
		if (userLeMappingRequest.getUserId() ==null) {
			validationMessages.add("user Id is mandatory");
		}else {
			Optional<User> userEntity=userRepository.findById(userLeMappingRequest.getUserId());
			if(!userEntity.isPresent()) {
				validationMessages.add("Invalid User");
			}
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}
	
	/**
	 * validation
	 * 
	 * @param userLeMappingRequest
	 * @return String
	 */
	private String validateUserMappings(String userMode, Integer userId) {
		List<String> validationMessages = new ArrayList<>();
		if (StringUtils.isNotBlank(userMode)) {
			if (!(userMode.equalsIgnoreCase("customer") || userMode.equalsIgnoreCase("partner"))) {
				validationMessages.add("userMode can only have CUSTOMER,PARTNER");
			}

		} else {
			validationMessages.add("userMode cannot be empty/null");
		}
		if (userId == null) {
			validationMessages.add("user Id is mandatory");
		} else {
			Optional<User> userEntity = userRepository.findById(userId);
			if (!userEntity.isPresent()) {
				validationMessages.add("Invalid User");
			}
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}
	
	/**
	 * Method to return UserAccessDetailsBean for logged in user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UserAccessDetailsBean getUserAccessDetails() {
		UserAccessDetailsBean userAccessDetailsBean = new UserAccessDetailsBean();
		try {
			userAccessDetailsBean.setUserName(Utils.getSource());
			LOGGER.info("Entering getUserAccessDetails to fetch access ddetails of for user {}",  userAccessDetailsBean.getUserName());
			final Map<String, Object> roleMapper = userInformationAuthService.getRoles(Utils.getSource(),
					Utils.getToken());
			userAccessDetailsBean.setRoles((List<String>) roleMapper.get("ROLES"));
			userAccessDetailsBean.setUserActions((Map<String, Object>) roleMapper.get("ACTIONS"));
			userAccessDetailsBean.setUserType(roleMapper.get("USER_TYPE") != null ? (String) roleMapper.get("USER_TYPE") : null);
			userAccessDetailsBean.setInventoryList(userInformationAuthService.getUserEngagements());
		} catch (Exception e) {
			LOGGER.error("Error in getting inventory product details ", e);
		}
		return userAccessDetailsBean;
	}
	
	public Set<UserBean> getUserGroupListIn(List<String> groupName) throws TclCommonException {
		Set<UserBean> userGroupUsers = new HashSet<>();
		List<Integer> listOfIds = new ArrayList<Integer>();
		try {
			
			if (groupName != null) {
				List<UserToUserGroup> userGroups = userToUserGroupRepository.findByMstUserGroup_GroupNameIn(groupName);

				userGroups.forEach(user -> {
					listOfIds.add(user.getUserId());
				});
				LOGGER.info("List Of Ids", listOfIds);
				List<User> optionalUser = userRepository.findByIdIn(listOfIds);
				if (!optionalUser.isEmpty()) {
					for(User user: optionalUser) {
					UserBean userBean = new UserBean();
					userBean.setFirstName(user.getFirstName());
					userBean.setLastName(user.getLastName());
					userBean.setContactNo(user.getContactNo());
					userBean.setUsername(user.getUsername());
					userBean.setEmailId(user.getEmailId());
					userBean.setUserId(user.getId().toString());
					userGroupUsers.add(userBean);
					}
				}			

			} 
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.TOKEN_GENERATOR_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userGroupUsers;
	}
	
}
