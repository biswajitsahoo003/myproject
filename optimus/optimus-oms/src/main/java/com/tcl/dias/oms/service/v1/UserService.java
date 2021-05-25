package com.tcl.dias.oms.service.v1;

import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserBean;
import com.tcl.dias.common.beans.UserGroupBean;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.beans.UserGroupToCustomerLeBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.beans.UserSearchBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.ContactResponse;
import com.tcl.dias.oms.beans.CustomerRequestBean;
import com.tcl.dias.oms.beans.UserDetails;
import com.tcl.dias.oms.beans.UserInfoBean;
import com.tcl.dias.oms.beans.UserRequest;
import com.tcl.dias.oms.beans.UserSite;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.dto.UserDto;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStage;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserServiceRequest;
import com.tcl.dias.oms.entity.entities.UserToUserGroups;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.CustomerRequestRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStatusRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.entity.repository.UserToCustomerLeRepository;
import com.tcl.dias.oms.entity.repository.UserToUserGroupsRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.npl.beans.UserLink;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class have all the userDetails
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	CustomerRequestRepository customerRequestRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderRepository orderRepository;

	@Value("${pilot.team.name}")
	String pilotTeamName;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${pilot.team.mobile}")
	String pilotTeamMobile;

	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;

	@Value("${rabbitmq.partner.details.queue}")
	String partnerQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	CustomerRepository customerRepository;

	@Value("${rabbitmq.customer.le.by.id}")
	String customerLeByIdQueue;

	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	MstOrderLinkStatusRepository mstOrderLinkStatusRepository;

	@Autowired
	MstOrderLinkStageRepository mstOrderLinkStageRepository;
	

	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	UserToCustomerLeRepository userToCustomerLeRepository;

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Value("${user.management.partner.psam.action}")
	String optMasterPartnerPSamAction;
	
	@Value("${user.management.ipc.partner.migration.action}")
	String optMasterIpcPartnerMigrationAction;
	
	@Autowired
	UserToUserGroupsRepository userToUserGroupsRepository;

	/**
	 * This method is used to get the user details getUserDetails
	 * 
	 * @param userRequest
	 * @return List<UserDetails>
	 * @throws TclCommonException
	 */
	public List<UserDetails> getUserDetails(UserRequest userRequest) throws TclCommonException {
		List<UserDetails> userDetails = new ArrayList<>();
		try {
			validateUserRequest(userRequest);
			List<User> users = userRepository.findByIdInAndStatus(userRequest.getUserIds(), 1);
			userDetails = users.parallelStream().map(UserDetails::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDetails;
	}

	/**
	 * getAllUsersByCustomer-This method is used to fetch all the user details based
	 * on customer getAllUsersByCustomer
	 * 
	 * @return UserDto List
	 * @throws TclCommonException
	 */
	public List<UserDto> getAllUsersByCustomer(Integer customerId, Integer quoteId) throws TclCommonException {
		List<UserDto> userDtos = null;
		try {
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString()) && customerId != null) {
				Optional<Customer> customer = customerRepository.findById(customerId);
				if (customer.isPresent()) {
					userDtos = customer.get().getUsers().stream().map(UserDto::new).collect(Collectors.toList());
				}
			} else if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				Map<String, Object> actions = (Map<String, Object>) userInfoUtils.getUserInformation().getActions();
				if (actions.containsKey(optMasterPartnerPSamAction) || actions.containsKey(optMasterIpcPartnerMigrationAction)) {
					List<User> users = userRepository.findUserByQuoteId(quoteId);
					userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
				}
			} else {
				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
				userDtos = user.getCustomer().getUsers().stream().map(UserDto::new).collect(Collectors.toList());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDtos;
	}

	/**
	 * This method is used to validate the userDetails validateUserRequest
	 * 
	 * @param userRequest
	 * @throws TclCommonException
	 */
	private void validateUserRequest(UserRequest userRequest) throws TclCommonException {
		if (userRequest == null || userRequest.getUserIds() == null || userRequest.getUserIds().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * 
	 * getUserSiteDetails - get the user site details
	 * 
	 * @return List<UserSite>
	 * @throws TclCommonException
	 */
	public List<UserSite> getUserSiteDetails() throws TclCommonException {
		List<UserSite> userDetails = new ArrayList<>();
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		Set<Integer> orderProductSolutionIds = new HashSet<>();
		try {
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				List<Map<String, Object>> mapProductSolutions = orderRepository
						.getProductSolutionsBasedOnUserType(user.getId());

				if (!mapProductSolutions.isEmpty()) {
					mapProductSolutions.stream().forEach(map -> {
						orderProductSolutionIds.add((Integer) map.get("orderProductSolutionId"));
					});
				}
				List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository.findByIdIn(orderProductSolutionIds);
				if (orderProductSolutions!=null && !orderProductSolutions.isEmpty()) {
					orderProductSolutions.stream().forEach(solution->{
						getUserSite(userDetails, mstProductComponents, solution);
					});
				}
			}

			else {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();
				List<Integer> customerLeId = new ArrayList<>();
				if(customerDetails!=null) {
					customerDetails.stream().forEach(customer->{
						customerLeId.add(customer.getCustomerLeId());
					});
				}
					List<OrderToLe> orderToLes = orderToLeRepository
							.findByErfCusCustomerLegalEntityIdIn(customerLeId);
					for (OrderToLe orderToLe : orderToLes) {

						for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
							for (OrderProductSolution solution : family.getOrderProductSolutions()) {
								getUserSite(userDetails, mstProductComponents, solution);
							}
						}

					}
				
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDetails;
	}

	/**
	 * getUserSite
	 * 
	 * @param userDetails
	 * @param mstProductComponents
	 * @param solution
	 */
	private void getUserSite(List<UserSite> userDetails, List<MstProductComponent> mstProductComponents,
			OrderProductSolution solution) {
		for (OrderIllSite illSite : solution.getOrderIllSites()) {
			UserSite userSite = new UserSite();
			getLocalITConatct(mstProductComponents, illSite, userSite);
			userSite.setLocationId(illSite.getErfLocSitebLocationId());
			userSite.setSiteId(illSite.getId());
			if (illSite.getMstOrderSiteStatus() != null) {
				Optional<MstOrderSiteStatus> mstOrderSiteStatus = mstOrderSiteStatusRepository
						.findById(illSite.getMstOrderSiteStatus().getId());
				if (mstOrderSiteStatus.isPresent()) {
					userSite.setOrderStatus(mstOrderSiteStatus.get().getName());
				}
			}
			if (illSite.getMstOrderSiteStage() != null) {
				Optional<MstOrderSiteStage> mstOrderSiteStage = mstOrderSiteStageRepository
						.findById(illSite.getMstOrderSiteStage().getId());
				if (mstOrderSiteStage.isPresent()) {
					userSite.setOrderStage(mstOrderSiteStage.get().getName());
				}
			}
			userDetails.add(userSite);
		}
	}

	/**
	 * getLocalITConatct
	 * 
	 * @param mstProductComponents
	 * @param illSite
	 * @param userSite
	 */
	private void getLocalITConatct(List<MstProductComponent> mstProductComponents, OrderIllSite illSite,
			UserSite userSite) {
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			List<OrderProductComponent> orderComponents = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent(illSite.getId(),
							mstProductComponents.get(0));
			if (orderComponents != null && !orderComponents.isEmpty()) {
				List<ProductAttributeMaster> productAttributes = productAttributeMasterRepository
						.findByNameAndStatus(IllSitePropertiesConstants.LOCATION_IT_CONTACT.toString(), (byte) 1);
				if (productAttributes != null && !productAttributes.isEmpty()) {
					List<OrderProductComponentsAttributeValue> orderProductAttributes = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster(orderComponents.get(0),
									productAttributes.get(0));
					if (orderProductAttributes != null && !orderProductAttributes.isEmpty()) {
						userSite.setLocalItContact(Integer.valueOf(orderProductAttributes.get(0).getAttributeValues()));
					}
				}
			}
		}
	}

	/**
	 * This method is used to get the user details getUserDetails by passing
	 * customer id
	 * 
	 * @param request{customerId}
	 * @return List<UserDetails>
	 * @throws TclCommonException
	 */
	public List<UserDetails> getUserDetailsByCustId(Integer customerId) throws TclCommonException {
		if (customerId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		List<UserDetails> userDetails = new ArrayList<>();
		try {
			List<User> users = userRepository.findByCustomerIdAndStatus(customerId, 1);
			userDetails = users.parallelStream().map(UserDetails::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDetails;
	}

	/**
	 * This method is used to persist the customer request customer id
	 * 
	 * @param CustomerRequestBean
	 * @throws TclCommonException
	 */
	public void persistCustomerRequest(CustomerRequestBean customerRequest) throws TclCommonException {
		try {
			if (Objects.isNull(customerRequest))
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

			UserServiceRequest request = new UserServiceRequest();
			request.setMessage(customerRequest.getMessage());

			if (Objects.isNull(customerRequest.getRequestedTime()) || customerRequest.getRequestedTime().isEmpty()) {
				request.setRequestedTime(new Date());
			} else {
				request.setRequestedTime(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(customerRequest.getRequestedTime()));
			}

			String username = Utils.getSource();

			User user = userRepository.findByUsernameAndStatus(username, 1);

			if (Objects.isNull(user))
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			request.setUser(user.getUsername());
			request.setSubject(customerRequest.getSubject());
			request.setCreatedTime(new Date());

			customerRequestRepository.save(request);
			notificationService.helpTicketNotification(user.getEmailId(), customerSupportEmail,
					customerRequest.getSubject(), customerRequest.getMessage(),CommonConstants.ALL);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * This method is used to return the contact details of pilot team
	 * 
	 * 
	 * @param none
	 * @return UserDetails
	 * @throws TclCommonException
	 */
	public ContactResponse getContactDetailsForPilotTeam() throws TclCommonException {
		ContactResponse contactResponse = new ContactResponse();
		try {
			contactResponse.setPhoneNo(pilotTeamMobile);
			contactResponse.setEmail(customerSupportEmail);
			contactResponse.setName(pilotTeamName);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return contactResponse;
	}

	/**
	 * This method is used to return the customer details based on the user type
	 * 
	 * 
	 * @param none
	 * @return UserDetails
	 * @throws TclCommonException
	 */
	public CustomerBean getCustomerDetails(String searchTxt,Boolean isLimited) throws TclCommonException {
		CustomerBean customerBean = null;
		try {

			Set<String> customerIds = new HashSet<>();
			String userType = userInfoUtils.getUserType();
			if (userType.equals(UserType.INTERNAL_USERS.toString())) {
				List<CustomerDetail> customerDetailsList = userInfoUtils.getCustomerDetails();
				if (StringUtils.isNotBlank(searchTxt)) {
					customerDetailsList = customerDetailsList.stream()
							.filter(customerDetail -> customerDetail.getCustomerName() != null
									&& customerDetail.getCustomerName().toLowerCase().contains(searchTxt.toLowerCase()))
							.collect(Collectors.toList());
				} else {
					customerDetailsList = customerDetailsList.stream()
							.sorted((e1, e2) -> e1.getCustomerName().compareTo(e2.getCustomerName()))
							.collect(Collectors.toList());
					if (isLimited != null && isLimited) {
						customerDetailsList = customerDetailsList.stream().limit(20).collect(Collectors.toList());//limiting default to 20
					}
				}
				
				customerDetailsList.stream().forEach(customerDetail -> {
					Optional<Customer> customer = customerRepository.findById(customerDetail.getCustomerId());
					if (customer.isPresent()) {
						customerIds.add(customer.get().getErfCusCustomerId().toString());
					}
				});
				String customerIdsCommaSeparated = String.join(",", customerIds);
				LOGGER.info("MDC Filter token value in before Queue call getCustomerDetails {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerQueue, customerIdsCommaSeparated);
				customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerBean;
	}


	/**
	 * @author ANANDHI VIJAY constructUser
	 * @param userManagementBean
	 * @return User
	 */
	public User constructUser(UserManagementBean userManagementBean) {
		User user = new User();
		Customer customerEntity = customerRepository.findByErfCusCustomerIdAndStatus(userManagementBean.getCustomerId(),
				CommonConstants.BACTIVE);
		if (customerEntity != null) {
			Customer customer = new Customer();
			customer.setId(customerEntity.getId());
			user.setCustomer(customer);
			user.setContactNo(userManagementBean.getContactNumber());
			user.setDesignation(userManagementBean.getDesignation());
			user.setEmailId(userManagementBean.getEmail());
			user.setFirstName(userManagementBean.getFirstName());
			user.setLastName(userManagementBean.getLastName());
			user.setPartnerId(null);
			user.setStatus(1);
			user.setUsername(userManagementBean.getUserName());
			user.setUserType(userManagementBean.getUserType());
			if (userManagementBean.getUserId() != null) {
				user.setId(userManagementBean.getUserId());
			}
		}
		return user;
	}

	/**
	 * @author ANANDHI VIJAY constructUserManagementBean
	 * @param user
	 * @return UserManagementBean
	 */
	@Transactional
	public UserManagementBean constructUserManagementBean(User user) {
		UserManagementBean userManagementBean = new UserManagementBean();
		userManagementBean.setUserId(user.getId());
		userManagementBean.setContactNumber(user.getContactNo());
		if (user.getCustomer() != null) {
			userManagementBean.setCustomerId(user.getCustomer().getId());
			userManagementBean.setCustomerName(user.getCustomer().getCustomerName());
		}
		userManagementBean.setDesignation(user.getDesignation());
		userManagementBean.setEmail(user.getEmailId());
		userManagementBean.setFirstName(user.getFirstName());
		userManagementBean.setLastName(user.getLastName());
		userManagementBean.setUserName(user.getUsername());
		userManagementBean.setUserType(user.getUserType());
		return userManagementBean;
	}

	



	public void appendLeNames(UserGroupBean userGroupBean, String customerLeList) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call appendLeNames {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String response = (String) mqUtils.sendAndReceive(customerLeByIdQueue, customerLeList);
		if (StringUtils.isNotBlank(response)) {
			List<Map<Integer, String>> customerLeBeans = (List<Map<Integer, String>>) Utils
					.convertJsonToObject(response, List.class);
			if (customerLeBeans != null && !customerLeBeans.isEmpty()) {

				if (userGroupBean.getUserGroupToCustomerLes() != null
						&& !userGroupBean.getUserGroupToCustomerLes().isEmpty()) {
					for (UserGroupToCustomerLeBean userGroupToCustomerLeBean : userGroupBean
							.getUserGroupToCustomerLes()) {
						for (Map<Integer, String> map : customerLeBeans) {
							if (userGroupToCustomerLeBean.getErfCusCustomerLeId() != null) {
								if (map.containsKey(userGroupToCustomerLeBean.getErfCusCustomerLeId().toString())) {
									userGroupToCustomerLeBean.setErfCusCustomerLeName(
											map.get(userGroupToCustomerLeBean.getErfCusCustomerLeId().toString()));
									break;
								}
							}
						}
					}
				}

			}
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
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		} else {
			try {
				User user = null;
				if (!Objects.isNull(userManagementBean.getUserId())) {
					Optional<User> optUser = userRepository.findById(userManagementBean.getUserId());
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
							Customer customer = new Customer();
							customer.setId(userManagementBean.getCustomerId());
							user.setCustomer(customer);
						}
					}
				} else if (userManagementBean.getUserName() != null) {
					user = userRepository.findByUsernameAndStatus(userManagementBean.getUserName(), 1);
					if (user != null) {

						user.setForceChangePassword(userManagementBean.getForceChangePassword());
					}
				}

				user = userRepository.save(user);
				return constructUserManagementBean(user);

			} catch (Exception ex) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ex, ResponseResource.R_CODE_ERROR);
			}
		}
	}


	/**
	 * @author ANANDHI VIJAY deleteUser
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	public String deleteUser(Integer userId) throws TclCommonException {
		try {
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {
				String userName = user.get().getUsername();
				User userEntity = user.get();
				userEntity.setStatus(0);
				userRepository.save(userEntity);
				return userName;
			} else {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
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
			} else {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
		return bean;
	}

	public List<UserManagementBean> getAllUserDetails() throws TclCommonException {
		try {
			List<UserManagementBean> userManagementBeanList = new ArrayList<>();
			List<User> activeUserList = userRepository.findByStatusIn(1);
			if (activeUserList == null) {
				throw new TclCommonException(ExceptionConstants.USER_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			for (User user : activeUserList) {
				userManagementBeanList.add(constructUserManagementBean(user));
			}
			return userManagementBeanList;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY getAllUserDetails
	 * @return
	 * @throws TclCommonException
	 */
	public List<UserManagementBean> searchUser(UserSearchBean userSearchBean) throws TclCommonException {
		try {
			List<UserManagementBean> userManagementBeanList = new ArrayList<>();
			Page<User> users = null;
			Specification<User> specs = UserSpecification.getUsers(userSearchBean.getUsername(),
					userSearchBean.getContactNo(), userSearchBean.getEmailId(), userSearchBean.getFirstName());
			users = userRepository.findAll(specs, PageRequest.of(userSearchBean.getPage(), userSearchBean.getSize()));

			for (User user : users) {
				userManagementBeanList.add(constructUserManagementBean(user));
			}
			return userManagementBeanList;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}
	

	/**
	 * getAllCustomerDetailsForLoggedInUser
	 * 
	 * @author ANANDHI VIJAY
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerBean getAllCustomerDetailsForLoggedInUser(String searchTxt,Boolean isLimited) throws TclCommonException {
		LOGGER.info("searchTxt {}",searchTxt);
		if(StringUtils.isNotBlank(searchTxt) && searchTxt.contains("$AND$")) {
			searchTxt=searchTxt.replaceAll("\\$AND\\$", "\\&");
		}
		LOGGER.info("post searchTxt {}",searchTxt);
		CustomerBean customerBean = new CustomerBean();
		final String fsearchTxt=searchTxt;
		try {
            // Partner and PSAM
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				Map<String, Object> actions = (Map<String, Object>) userInfoUtils.getUserInformation().getActions();
				if(actions.containsKey(optMasterPartnerPSamAction) || actions.containsKey(optMasterIpcPartnerMigrationAction)) {
//					List<Integer> erfPartnerIds = userRepository.findErfPartnerId(Utils.getSource());
					UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
					List<PartnerDetail> partnerDetailsList = userInformation.getPartners();
					if (StringUtils.isNotBlank(searchTxt)) {
						partnerDetailsList = partnerDetailsList.stream()
								.filter(partnerDetail -> partnerDetail.getPartnerName() != null
										&& partnerDetail.getPartnerName().toLowerCase().contains(fsearchTxt.toLowerCase()))
								.collect(Collectors.toList());
					} else {
						partnerDetailsList = partnerDetailsList.stream()
								.sorted((e1, e2) -> e1.getPartnerName().compareTo(e2.getPartnerName()))
								.collect(Collectors.toList());
						if (isLimited != null && isLimited) {

							int count = 0;
							Map<String, List<PartnerDetail>> partnerDetailsListMap = new HashMap<>();
							for (PartnerDetail partnerDetail : partnerDetailsList) {
								if (partnerDetailsListMap.get(partnerDetail.getPartnerName()) == null) {
									if (count == 20) {
										break;
									}
									List<PartnerDetail> tmpList = new ArrayList<>();
									tmpList.add(partnerDetail);
									partnerDetailsListMap.put(partnerDetail.getPartnerName(), tmpList);
									count++;
								} else {
									partnerDetailsListMap.get(partnerDetail.getPartnerName()).add(partnerDetail);
								}
							}
							partnerDetailsList = new ArrayList<>();
							for (List<PartnerDetail> partnerDetail : partnerDetailsListMap.values()) {
								partnerDetailsList.addAll(partnerDetail);
							}
						}
					}
					
					Set<Integer> erfPartnerIds = partnerDetailsList.stream().map(PartnerDetail::getErfPartnerId).collect(Collectors.toSet());
					String response = (String) mqUtils.sendAndReceive(partnerQueue, GscUtils.toJson(erfPartnerIds));
					List<PartnerDetailsBean> partnerDetailsBeans = GscUtils.fromJson(response, new TypeReference<List<PartnerDetailsBean>>() {
					});

					Set<CustomerDetailBean> customerDetailsSet = partnerDetailsBeans.stream().map(partnerDetailsBean -> {
						CustomerDetailBean customerDetailBean = new CustomerDetailBean();
						customerDetailBean.setCustomerId(partnerDetailsBean.getPartnerId());
						customerDetailBean.setCustomerName(partnerDetailsBean.getPartnerName());
						customerDetailBean.setCuid(partnerDetailsBean.getCuid());
						return customerDetailBean;
					}).collect(Collectors.toSet());
					customerBean.setCustomerDetailsSet(customerDetailsSet);
				}
			} else {
				// Customer and Sales
				Set<String> customerIds = new HashSet<>();
				List<CustomerDetail> customerDetailsList = userInfoUtils.getCustomerDetails();
				LOGGER.info("Customer Details :: {}", customerDetailsList.toString());
				if (StringUtils.isNotBlank(searchTxt)) {
					customerDetailsList = customerDetailsList.stream()
							.filter(customerDetail -> customerDetail.getCustomerName() != null
									&& customerDetail.getCustomerName().toLowerCase().contains(fsearchTxt.toLowerCase()))
							.collect(Collectors.toList());
				} else {
					customerDetailsList = customerDetailsList.stream()
							.sorted((e1, e2) -> e1.getCustomerName().compareTo(e2.getCustomerName()))
							.collect(Collectors.toList());
					if (isLimited != null && isLimited) {
						int count = 0;
						Map<String, List<CustomerDetail>> customerDetailsListMap = new HashMap<>();
						for (CustomerDetail customerDetail : customerDetailsList) {
							if (customerDetailsListMap.get(customerDetail.getCustomerName()) == null) {
								if (count == 20) {
									break;
								}
								List<CustomerDetail> tmpList = new ArrayList<>();
								tmpList.add(customerDetail);
								customerDetailsListMap.put(customerDetail.getCustomerName(), tmpList);
								count++;
							} else {
								customerDetailsListMap.get(customerDetail.getCustomerName()).add(customerDetail);
							}
						}
						customerDetailsList = new ArrayList<>();
						for (List<CustomerDetail> customerDetail : customerDetailsListMap.values()) {
							customerDetailsList.addAll(customerDetail);
						}

					}
				}
				
				customerDetailsList.stream().forEach(customerDetail -> {
					Optional<Customer> customer = customerRepository.findById(customerDetail.getCustomerId());
					if (customer.isPresent()) {
						customerIds.add(customer.get().getErfCusCustomerId().toString());
					}
				});

				String customerIdsCommaSeparated = String.join(",", customerIds);
				LOGGER.info("customerIdsCommaSeparated :: {}", customerIdsCommaSeparated);
				LOGGER.info("MDC Filter token value in before Queue call getAllCustomerDetailsForLoggedInUser {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerQueue, customerIdsCommaSeparated);
				customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);
				if (customerBean != null && customerBean.getCustomerDetailsSet() != null) {
					SortedSet<CustomerDetailBean> customerDetailSet = new TreeSet<>(
							Comparator.comparing(CustomerDetailBean::getCustomerName));
					customerDetailSet.addAll(customerBean.getCustomerDetailsSet());
					customerBean.setCustomerDetailsSet(customerDetailSet);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerBean;

	}

	/**
	 * @author ANANDHI VIJAY getNotificationSubscriptionDetails
	 * @param userNotificationBean
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getNotificationSubscriptionDetails(MailNotificationRequest mailNotificationRequest)
			throws TclCommonException {
		List<String> subscribedEmails = new ArrayList<>();
		if (mailNotificationRequest == null || mailNotificationRequest.getTo() == null
				|| mailNotificationRequest.getTo().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST, ResponseResource.R_CODE_ERROR);
		}
		try {
			List<String> inputEmailLists = mailNotificationRequest.getTo();
			for (String email : inputEmailLists) {
				User user = userRepository.findByEmailIdAndStatus(email, 1);
				if (user == null) {
					subscribedEmails.add(email);
				} else {
					// TO-DO Check subscription for the notification type,
					// Note: Null check not done for Notification type
					Boolean isSubscribed = true;
					if (isSubscribed) {
						subscribedEmails.add(email);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return subscribedEmails;
	}

	/**
	 *
	 * Get User details by username
	 *
	 * @param username
	 * @return User
	 */
	public User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * 
	 * getUserSiteDetails - get the user site details
	 * 
	 * @return List<UserSite>
	 * @throws TclCommonException
	 */
	public List<UserLink> getUserLinkDetails() throws TclCommonException {
		List<UserLink> userDetails = new ArrayList<>();
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		Set<Integer> orderProductSolutionIds = new HashSet<>();
		try {
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				List<Map<String, Object>> mapProductSolutions = orderRepository
						.getProductSolutionsBasedOnUserType(user.getId());

				if (!mapProductSolutions.isEmpty()) {
					mapProductSolutions.stream().forEach(map -> {
						orderProductSolutionIds.add((Integer) map.get("orderProductSolutionId"));
					});
				}
				for (Integer orderProductSolutionId : orderProductSolutionIds) {
					Optional<OrderProductSolution> solution = orderProductSolutionRepository
							.findById(orderProductSolutionId);
					if (solution.isPresent())
						getUserLinks(userDetails, mstProductComponents, solution.get());
				}
			}

			else {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();

				for (CustomerDetail customerDetail : customerDetails) {
					List<OrderToLe> orderToLes = orderToLeRepository
							.findByErfCusCustomerLegalEntityId(customerDetail.getCustomerLeId());
					for (OrderToLe orderToLe : orderToLes) {

						for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
							for (OrderProductSolution solution : family.getOrderProductSolutions()) {
								getUserLinks(userDetails, mstProductComponents, solution);
							}
						}

					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDetails;
	}

	/**
	 * getUserLinks
	 * 
	 * @param userDetails
	 * @param mstProductComponents
	 * @param solution
	 */
	private void getUserLinks(List<UserLink> userDetails, List<MstProductComponent> mstProductComponents,
			OrderProductSolution solution) {
		List<OrderNplLink> orderNplLinkList = orderNplLinkRepository.findByProductSolutionId(solution.getId());
		for (OrderNplLink orderNplLink : orderNplLinkList) {
			UserLink userLink = new UserLink();
			UserSite userSiteA = new UserSite();
			UserSite userSiteB = new UserSite();
			Optional<OrderIllSite> orderIllSiteA = orderIllSitesRepository.findById(orderNplLink.getSiteAId());
			if (orderIllSiteA.isPresent()) {
				getLocalITConatct(mstProductComponents, orderIllSiteA.get(), userSiteA);
				userSiteA.setLocationId(orderIllSiteA.get().getErfLocSitebLocationId());
			}

			Optional<OrderIllSite> orderIllSiteB = orderIllSitesRepository.findById(orderNplLink.getSiteBId());
			if (orderIllSiteB.isPresent()) {
				getLocalITConatct(mstProductComponents, orderIllSiteB.get(), userSiteB);
				userSiteB.setLocationId(orderIllSiteB.get().getErfLocSitebLocationId());
			}
			userLink.setLinkId(orderNplLink.getId());
			userLink.setUserSiteA(userSiteA);
			userLink.setUserSiteA(userSiteB);

			if (orderNplLink.getMstOrderLinkStatus() != null) {
				Optional<MstOrderLinkStatus> mstOrderLinkStatus = mstOrderLinkStatusRepository
						.findById(orderNplLink.getMstOrderLinkStatus().getId());
				if (mstOrderLinkStatus.isPresent()) {
					userLink.setOrderStatus(mstOrderLinkStatus.get().getName());
				}
			}
			if (orderNplLink.getMstOrderLinkStage() != null) {
				Optional<MstOrderLinkStage> mstOrderLinkStage = mstOrderLinkStageRepository
						.findById(orderNplLink.getMstOrderLinkStage().getId());
				if (mstOrderLinkStage.isPresent()) {
					userLink.setOrderStage(mstOrderLinkStage.get().getName());
				}
			}
			userDetails.add(userLink);
		}
	}


	/**
	 * @author vivek
	 * updateUserDetails
	 * used to update the user details
	 * @param userRequest
	 * @return
	 */
	@Transactional
	public UserInfoBean updateUserDetails(UserInfoBean userRequest) throws TclCommonException {
		UserInfoBean userInfoBean = new UserInfoBean();

		User user = getUserId(Utils.getSource());
		validateUser(user);
		updateUserDetails(user, userRequest);
		updateUserDetailsAgaintsLegalEntity(userRequest);

		BeanUtils.copyProperties(user, userInfoBean);

		return userInfoBean;
	}
	
	private void updateUserDetailsAgaintsLegalEntity(UserInfoBean userRequest) {
		if(userRequest.getQuoteToLeId()!=null) {
			Optional<QuoteToLe> optionalQuote=quoteToLeRepository.findById(userRequest.getQuoteToLeId());
			if(optionalQuote.isPresent()) {
				QuoteToLe quoteToLe=optionalQuote.get();
			
				
				quoteToLe.getQuoteLeAttributeValues().forEach( attr ->{
					updateQuoteLeAttr(userRequest, attr);
					
				});
				
			}
		}
		
	}

	/**
	 * @author vivek
	 * getUserDetails
	 * used to get the user details
	 * @return
	 * @throws TclCommonException
	 */
	public UserInfoBean getUserDetails() throws TclCommonException {
		UserInfoBean userInfoBean = new UserInfoBean();
		User user = getUserId(Utils.getSource());
		validateUser(user);
		BeanUtils.copyProperties(user, userInfoBean);
		return userInfoBean;
	}
	

	/**
	 * updateUserDetails
	 * update the user info details
	 * @param user
	 * @param userRequest
	 * @return
	 */
	private User updateUserDetails(User user, UserInfoBean userRequest) {
		if (userRequest.getContactNo() != null) {
			user.setContactNo(userRequest.getContactNo());
		}
		if (userRequest.getDesignation() != null) {
			user.setDesignation(userRequest.getDesignation());
		}
		if (userRequest.getFirstName() != null) {
			user.setFirstName(userRequest.getFirstName());
		}
		if (userRequest.getLastName() != null) {
			user.setLastName(userRequest.getLastName());

		}
		if (userRequest.getUsername() != null) {
			user.setUsername(userRequest.getUsername());

		}
		userRepository.save(user);
		return user;
	}
	
	
	/**
	 * updateUserDetails
	 * update the user info details
	 * @param user
	 * @param userRequest
	 * @return
	 */
	private void updateQuoteLeAttr( UserInfoBean userRequest, QuoteLeAttributeValue atr) {
		if (userRequest.getContactNo() != null && atr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CONTACT_NO)) {
			atr.setAttributeValue(userRequest.getContactNo());
		}
		if (userRequest.getDesignation() != null && atr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.DESIGNATION)) {
			atr.setAttributeValue(userRequest.getDesignation());
		}
		
		if (userRequest.getDesignation() != null && atr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.DESIGNATION)) {
			atr.setAttributeValue(userRequest.getDesignation());
		}
	
	
		if (userRequest.getUsername() != null &&  atr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CONTACT_ID)) {
			atr.setAttributeValue(userRequest.getUsername());

		}
		if (userRequest.getFirstName() != null &&  atr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CONTACT_NAME)) {
			atr.setAttributeValue(userRequest.getFirstName());

		}
		quoteLeAttributeValueRepository.save(atr);
	}

	/**
	 * validateUser
	 * @param user
	 * @throws TclCommonException
	 */
	private void validateUser(User user) throws TclCommonException {
		if (user == null) {

			throw new TclCommonException(ExceptionConstants.USER_EMPTY, ResponseResource.R_CODE_ERROR);

		}
	}
	
	public String updateCosMessagePreference(UserInfoBean userRequest) throws TclCommonException {
		LOGGER.info("Entering to update the cos preference for username {}, cos preference {}", userRequest.getUsername(), userRequest.getShowCosMessage());
		if(userRequest.getUsername() == null)
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		User user = getUserId(Utils.getSource());
		validateUser(user);
		user.setShowCosMessage(userRequest.getShowCosMessage());
		userRepository.save(user);
		LOGGER.info("Cos preference saved {} for user {}", userRequest.getShowCosMessage(), userRequest.getUsername());
		return CommonConstants.SUCCESS;	
	}
	
	public UserGroupBeans getUserDetailsByGroupName(String groupName) throws TclCommonException {
		UserGroupBeans userGroup=new UserGroupBeans();
		try {
			
			List<UserToUserGroups> userGroups=userToUserGroupsRepository.findByGroupName(groupName);
			for (UserToUserGroups userToUserGroups : userGroups) {
				UserBean userBean=new UserBean();
				userBean.setUserName(userToUserGroups.getUsername());
				userBean.setUserId(userToUserGroups.getUsername());
				userGroup.getUser().add(userBean);
			}		
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userGroup;
	}


}
