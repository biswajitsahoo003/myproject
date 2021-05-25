package com.tcl.dias.auth.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.common.keycloack.bean.KeycloakUserBean;
import com.tcl.common.keycloack.bean.RolesBean;
import com.tcl.common.keycloack.bean.keycloakAuthBean;
import com.tcl.dias.auth.beans.EngagementBean;
import com.tcl.dias.auth.beans.ForgotPasswordRequest;
import com.tcl.dias.auth.beans.ResetPasswordRequest;
import com.tcl.dias.auth.constants.Constants;
import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;
import com.tcl.dias.common.beans.ChangePasswordBean;
import com.tcl.dias.common.beans.ConfigureOtpRequestBean;
import com.tcl.dias.common.beans.CustomerLeListBean;
import com.tcl.dias.common.beans.CustomerLeListListBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.RoleAddRequestBean;
import com.tcl.dias.common.beans.UserGroupBean;
import com.tcl.dias.common.beans.UserGroupCustomerLegalEntityBean;
import com.tcl.dias.common.beans.UserGroupSearchBean;
import com.tcl.dias.common.beans.UserGroupToCustomerLeBean;
import com.tcl.dias.common.beans.UserGroupToUserBean;
import com.tcl.dias.common.beans.UserGroupUpdateBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.beans.UserSearchBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.TokenExpire;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the ObjectCreator.java class. This is for mocking auth
 * micro service dao calls
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ObjectCreator {
	
	
	public RolesBean getRolesBeanForUser() {
		RolesBean role = new RolesBean();
		role.setId("2a222254-f3a4-4a3b-a372-b517fc450afa");
		role.setName("TestRoleAnandhi");
		return role;
	}
	
	
	public UserManagementBean constructUserManagementWithoutId() {
		UserManagementBean userManagementBean = new UserManagementBean();
		userManagementBean.setContactNumber("8939525572");
		userManagementBean.setCustomerId(1);
		userManagementBean.setCustomerName("Test");
		userManagementBean.setDesignation("TEST_DESTINATION");
		userManagementBean.setEmail("anandhi.vai@gmail.com");
		userManagementBean.setFirstName("Anandhi");
		userManagementBean.setForceChangePassword((byte)0);
		userManagementBean.setLastName("Vijayaraghavan");
		userManagementBean.setPartnerId(1);
		userManagementBean.setRoles(getRolesList());
		userManagementBean.setUserName("optimus_user");
		userManagementBean.setUserType("test");
		userManagementBean.setUserGroupIds(getIds());
		return userManagementBean;
	}
	
	public UserManagementBean constructUserManagementWithId() {
		UserManagementBean userManagementBean = new UserManagementBean();
		userManagementBean.setContactNumber("8939525572");
		userManagementBean.setCustomerId(1);
		userManagementBean.setCustomerName("Test");
		userManagementBean.setDesignation("TEST_DESTINATION");
		userManagementBean.setEmail("anandhi.vai@gmail.com");
		userManagementBean.setFirstName("Anandhi");
		userManagementBean.setForceChangePassword((byte)0);
		userManagementBean.setLastName("Vijayaraghavan");
		userManagementBean.setPartnerId(1);
		userManagementBean.setRoles(getRolesList());
		userManagementBean.setUserName("optimus_user");
		userManagementBean.setUserType("test");
		userManagementBean.setUserGroupIds(getIds());
		userManagementBean.setUserId(1);
		return userManagementBean;
	}
	
	public List<String> getRolesList(){
		List<String> rolesList = new ArrayList<>();
		rolesList.add("OPTIMUS_ADMIN");
		rolesList.add("OPTIMUS_MU");
		return rolesList;
	}
	
	public List<Integer> getIds(){
		List<Integer> ids = new ArrayList<>();
		ids.add(1);
		ids.add(2);
		return ids;
	}
	
	
	
	public User constructUser() {
		User user = new User();
		user.setId(1);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setCustomerName("Test");
		user.setCustomer(customer);
		user.setDesignation("TEst");
		user.setEmailId("a.p@gmail.com");
		user.setFirstName("a");
		user.setForceChangePassword(CommonConstants.BACTIVE);
		user.setLastName("p");
		user.setPartnerId(1);
		user.setStatus(CommonConstants.ACTIVE);
		user.setUsername("optimux");
		return user;
	}
	
	
	public List<RolesBean> rolesBeanList(){
		List<RolesBean> rolesBeans = new ArrayList<>();
		RolesBean rolesBean = getRolesBeanForUser();
		rolesBeans.add(rolesBean);
		return rolesBeans;
	}
	
	public List<RolesBean> rolesBeanListEmpty(){
		List<RolesBean> rolesBeans = new ArrayList<>();
		RolesBean rolesBean = new RolesBean();
		rolesBeans.add(rolesBean);
		return rolesBeans;
	}
	
	public KeyCloackRoles constructKeycloakRoles() {
		KeyCloackRoles keyCloackRoles = new KeyCloackRoles();
		keyCloackRoles.setRoles(rolesBeanList());
		return keyCloackRoles;
	}
	
	public keycloakAuthBean returnKeycloakAuthBean() {
		keycloakAuthBean keycloakAuthBean = new keycloakAuthBean();
		keycloakAuthBean.setAccessToken("e23412sdfefef");
		return keycloakAuthBean;
	}
	
	public RestResponse restResponseForGetAllRoles() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		restResponse.setData(Utils.convertObjectToJson(rolesBeanList()));
		return restResponse;
	}
	
	public RestResponse restResponseForGetAllRolesWithoutRole() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		restResponse.setData(Utils.convertObjectToJson(rolesBeanListEmpty()));
		return restResponse;
	}
	
	public RestResponse restResponseForGetToken() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		restResponse.setData(Utils.convertObjectToJson(returnKeycloakAuthBean()));
		return restResponse;
	}
	
	public RestResponse restResponseWithSuccess() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.SUCCESS);
		restResponse.setData(Status.SUCCESS.toString());
		return restResponse;
	}
	
	public Optional<User> returnOptionalOfUser(){
		return Optional.of(constructUser());
	}
	
	public ForgotPasswordRequest password() {
		ForgotPasswordRequest request = new ForgotPasswordRequest();
		request.setEmailId("optimus@tcl.com");
		return request;
	}
	
	public KeycloakUserBean getKeyCloakUserBean() {
		KeycloakUserBean keycloakUserBean = new KeycloakUserBean();
		keycloakUserBean.setId("1233434dfefefd");
		keycloakUserBean.setEnabled(true);
		keycloakUserBean.setUsername("test");
		return keycloakUserBean;
	}
	
	public ResetUserInfoBean resetTokenUserInfoProcess() throws TclCommonException{
		ResetUserInfoBean bean = new ResetUserInfoBean();
		bean.setUserId("11");
		bean.setResetToken("resetToken");
		return bean;
	}
	
	
	public ResetPasswordRequest resetPasswordRequest() {
		ResetPasswordRequest request = new ResetPasswordRequest();
		request.setPassword("Optimus12@34");
		return request;
	}
	
	public KeycloakUserBean getKeyCloakUserBeanWithNullId() {
		KeycloakUserBean keycloakUserBean = new KeycloakUserBean();
		keycloakUserBean.setId(null);
		keycloakUserBean.setEnabled(true);
		keycloakUserBean.setUsername("test");
		return keycloakUserBean;
	}
	
	public RestResponse getKeycloakUserIdByUserNameResponse() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		restResponse.setData(Utils.convertObjectToJson(getKeyCloakUserBean()));
		restResponse.setStatus(Status.SUCCESS);
		return restResponse;
	}
	
	public RoleAddRequestBean constructRoleAddRequestBean() {
		RoleAddRequestBean roleAddRequestBean = new RoleAddRequestBean();
		roleAddRequestBean.setRoleName("Test");
		return roleAddRequestBean;
	}
	
	public RoleAddRequestBean constructRoleAddRequestBeanWithoutName() {
		RoleAddRequestBean roleAddRequestBean = new RoleAddRequestBean();
		roleAddRequestBean.setRoleName(null);
		return roleAddRequestBean;
	}
	
	public RestResponse getUserByUserNameKeycloak() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		List<KeycloakUserBean> keycloakUserBeans = new ArrayList<>();
		keycloakUserBeans.add(getKeyCloakUserBean());
		restResponse.setData(Utils.convertObjectToJson(keycloakUserBeans));
		restResponse.setStatus(Status.SUCCESS);
		return restResponse;
	}
	
	public RestResponse getUserByUserNameKeycloakWithDataNull() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		List<KeycloakUserBean> keycloakUserBeans = new ArrayList<>();
		keycloakUserBeans.add(getKeyCloakUserBeanWithNullId());
//		restResponse.setData(Utils.convertObjectToJson(keycloakUserBeans));
		restResponse.setStatus(Status.SUCCESS);
		return restResponse;
	}
	
	public RestResponse getUserByUserNameKeycloakWithoutId() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		List<KeycloakUserBean> keycloakUserBeans = new ArrayList<>();
		keycloakUserBeans.add(getKeyCloakUserBeanWithNullId());
		restResponse.setData(Utils.convertObjectToJson(keycloakUserBeans));
		restResponse.setStatus(Status.SUCCESS);
		return restResponse;
	}
	
	public List<User> returnUserList(){
		List<User> userList = new ArrayList<>();
		userList.add(constructUser());
		return userList;
	}
	
	public UserSearchBean constructUserSearchBean() {
		UserSearchBean userSearchBean = new UserSearchBean();
		userSearchBean.setPage(1);
		userSearchBean.setSize(10);
		userSearchBean.setUsername(null);
		return userSearchBean;
	}
	
	public Page<User> constructPageUser(){
		List<User> userList = new ArrayList<>();
		userList.add(constructUser());
		Page<User> page = new PageImpl<>(userList);
		return page;
	}
	
	public ChangePasswordBean constructChangePasswordBean() {
		ChangePasswordBean changePasswordBean = new ChangePasswordBean();
		changePasswordBean.setNewPassword("test");
		changePasswordBean.setOldPassword("test");
		changePasswordBean.setUserName("test");
		return changePasswordBean;
	}
	
	public UserGroupBean constructUserGroupBean() {
		UserGroupBean userGroupBean =  new UserGroupBean();
		userGroupBean.setUserGroupName("Test");
		return userGroupBean;
	}
	
	public UserGroupBean constructUserGroupBeanWithoutName() {
		UserGroupBean userGroupBean =  new UserGroupBean();
		return userGroupBean;
	}
	
	public UserGroupBean userGroupBeanForUpdate() {
		UserGroupBean userGroupBean = new UserGroupBean();
		userGroupBean.setIsActive(CommonConstants.BACTIVE);
		userGroupBean.setCustomerIds(new HashSet<>(getIds()));
		userGroupBean.setNoOfLegalEntities(2);
		userGroupBean.setNoOfUsers(1);
		userGroupBean.setUserGroupId(1);
		userGroupBean.setUserGroupName("Test");
		userGroupBean.setUserGroupType("Type");
		userGroupBean.setUserGroupToUsers(new HashSet<>(constructUserGroupToUserList()));
		userGroupBean.setUserGroupToCustomerLes(new HashSet<>(constructUserGroupToCustomerLeBeanList()));
		return userGroupBean;
	}
	
	public List<UserGroupToUserBean> constructUserGroupToUserList(){
		List<UserGroupToUserBean> userGroupToUserBeans = new ArrayList<>();
		UserGroupToUserBean userGroupToUserBean = new UserGroupToUserBean();
		userGroupToUserBean.setId(1);
		userGroupToUserBean.setIsActive(CommonConstants.BACTIVE);
		userGroupToUserBean.setUserGroups(constructUserGroupBean());
		userGroupToUserBean.setUsername("Test");
		userGroupToUserBeans.add(userGroupToUserBean);
		return userGroupToUserBeans;
	}
	
	public List<UserGroupToCustomerLeBean> constructUserGroupToCustomerLeBeanList(){
		List<UserGroupToCustomerLeBean> userGroupToCustomerLeBeans = new ArrayList<>();
		UserGroupToCustomerLeBean userGroupToCustomerLeBean = new UserGroupToCustomerLeBean();
		userGroupToCustomerLeBean.setErfCusCustomerId(1);
		userGroupToCustomerLeBean.setErfCusCustomerLeId(1);
		userGroupToCustomerLeBean.setId(1);
		userGroupToCustomerLeBean.setIsActive(CommonConstants.BACTIVE);
		userGroupToCustomerLeBeans.add(userGroupToCustomerLeBean);
		return userGroupToCustomerLeBeans;
	}
	
	
	public List<CustomerLeListBean> custBean() {
		List<CustomerLeListBean> list = new ArrayList<>();
		CustomerLeListBean bean = new CustomerLeListBean();
		bean.setCustomerId(11);
		bean.setLeId(12);
		list.add(bean);
		return list;
	}
	 
	public CustomerLeListListBean cust() {
		CustomerLeListListBean list = new CustomerLeListListBean();
		list.setCustomerLeLists(custBean());
		return list;
	}
	public UserGroupUpdateBean constructUserGroupUpdateBEan() {
		UserGroupUpdateBean userGroupUpdateBean = new UserGroupUpdateBean();
		userGroupUpdateBean.setUserGroupId(1);
		userGroupUpdateBean.setUserGroupLegalEntity(constructCustomerLeBeanList());
		userGroupUpdateBean.setUserGroupToUser(getIds());
		return userGroupUpdateBean;
	}
	
	public UserGroupUpdateBean constructUserGroupUpdateBEanWithoutId() {
		UserGroupUpdateBean userGroupUpdateBean = new UserGroupUpdateBean();
		userGroupUpdateBean.setUserGroupId(null);
		userGroupUpdateBean.setUserGroupLegalEntity(constructCustomerLeBeanList());
		userGroupUpdateBean.setUserGroupToUser(getIds());
		return userGroupUpdateBean;
	}
	
	public User findByEmailIdAndStatus() {
		User user = new User();
		user.setUsername("username");
		user.setId(11);
		return user;
	}
	
	public Set<String> token(){
		Set<String> set= new HashSet<>();
		set.add("1313045029");
		set.add("1313045029");
		return set;
	}
	
	
	public TokenExpire tokenExpire() {
		TokenExpire token = new TokenExpire();
		token.setToken("token");
		token.setActualTimeStamp(new Timestamp(new Date().getTime()));
		token.setChild(true);
		return token;
	}
	
	public TokenExpire tokenExpireChildNull() {
		TokenExpire token = new TokenExpire();
		token.setToken("token");
		token.setActualTimeStamp(new Timestamp(new Date().getTime()));
		token.setChild(false);
		return token;
	}
	
	public CustomerDetail custDetail() {
		CustomerDetail cust = new CustomerDetail();
		cust.setCustomerAcId("AcId");
		cust.setCustomerCode("Code");
		cust.setCustomerEmailId("optimus@tcl.com");
		cust.setCustomerId(11);
		cust.setCustomerLeId(12);
		cust.setCustomerName("optimus");
		cust.setErfCustomerId(13);
		cust.setStatus((byte) 1);
		return cust;
	}
	
	public List<CustomerDetail> getCustomerDetails(){
		
		List<CustomerDetail> list = new ArrayList<>();
		list.add(custDetail());
		return list;
	}
	
	
	
	public List<UserGroupCustomerLegalEntityBean> constructCustomerLeBeanList(){
		List<UserGroupCustomerLegalEntityBean> userGroupCustomerLegalEntityBeans = new ArrayList<>();
		UserGroupCustomerLegalEntityBean userGroupCustomerLegalEntityBean = new UserGroupCustomerLegalEntityBean();
		userGroupCustomerLegalEntityBean.setCustomerId(1);
		userGroupCustomerLegalEntityBean.setLegalEntityId(1);
		userGroupCustomerLegalEntityBeans.add(userGroupCustomerLegalEntityBean);
		return userGroupCustomerLegalEntityBeans;
	}
	
	public UserGroupSearchBean userGroupSearchBean() {
		
		UserGroupSearchBean bean = new UserGroupSearchBean();
		bean.setName("Optimus");
		bean.setPage(1);
		bean.setSize(1); 
		return bean;
	}
	
	
	public List<Map<Integer, String>> getcustomerLeDetailsQueue(){
		List<Map<Integer,String>> mapList = new ArrayList<>();
		Map<Integer,String> map1 = new HashMap<>();
		map1.put(1, "Test1");
		Map<Integer,String> map2 = new HashMap<>();
		map2.put(1, "Test1");
		mapList.add(map1);
		mapList.add(map2);
		return mapList;
	}
	
	public Customer findByErfCusCustomerIdAndStatus() {
		Customer cust = new Customer();
		cust.setErfCusCustomerId(11);
		cust.setStatus((byte) 1);
		return cust;
	}
	
	
	public List<Customer> findByErfCusCustomerIdInAndStatusCom() {
		List<Customer> list = new ArrayList<>();
		Customer cust = new Customer();
		cust.setCustomerCode("customerCode");
		cust.setCustomerEmailId("optimus@tcl.com");
		cust.setCustomerName("customerName");
		cust.setId(1);
		cust.setErfCusCustomerId(11);
		cust.setStatus((byte) 1);
		list.add(cust);
		return list;
	}
	
	
	public Optional<Customer> getCustomerOptional(){
		Customer customer = new Customer();
		customer.setId(1);
		customer.setCustomerName("Test");
		return Optional.of(customer);
	}
	
	
	
	public List<Map<String, Object>> getUsersNotPresentInTheGroup(){
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("id", 1);
		map1.put("username", "anandhi");
		map1.put("name", "anandhi");
		mapList.add(map1);
		return mapList;
	}
	
	public EngagementBean engBean() {
		EngagementBean bean = new EngagementBean();
		bean.setProductId(1);
		bean.setProductName("productFamilyName");
		return bean;
	}
	
	public List<Map<String, Object>> getEngagementDetailsGroupByProductFamily(){
		
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("productFamilyName", engBean());
		map.put("productFamilyId", engBean());
		list.add(map);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLeQueueDetails(){
		List<Map<String, Object>> mapList = new ArrayList<>();
		LegalEntityBean legalEntityBean = new LegalEntityBean();
		legalEntityBean.setCustomerId(1);
		legalEntityBean.setLegalEntityId(1);
		legalEntityBean.setLegalEntityName("test");
		LegalEntityBean legalEntityBean1 = new LegalEntityBean();
		legalEntityBean1.setCustomerId(2);
		legalEntityBean1.setLegalEntityId(2);
		legalEntityBean1.setLegalEntityName("test");
		ObjectMapper oMapper = new ObjectMapper();
		Map<String,Object> map1=oMapper.convertValue(legalEntityBean, Map.class);
		Map<String,Object> map2=oMapper.convertValue(legalEntityBean1, Map.class);
		mapList.add(map1);
		mapList.add(map2);
		return mapList;
	}
	public RestResponse restResponseWithoutSuccess() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.FAILURE);
		restResponse.setData(Status.FAILURE.toString());
		return restResponse;
	}
	
	public UserManagementBean constructBeanForUserAddOnlyInOms() {
		UserManagementBean bean = new UserManagementBean();
		bean.setEmail("anandhi.vijayaraghavan@tatacomminucations.com");
		return bean;
	}
	
	public UserManagementBean constructBeanForUserAddOnlyInOmsWithoutEmail() {
		UserManagementBean bean = new UserManagementBean();
		return bean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForEnable() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(Constants.ENABLE);
		configureOtpRequestBean.setUserId(1);
		return configureOtpRequestBean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForDisable() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(Constants.DISABLE);
		configureOtpRequestBean.setUserId(1);
		return configureOtpRequestBean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForDisableOtp() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(Constants.DISABLE_OTP);
		configureOtpRequestBean.setUserId(1);
		return configureOtpRequestBean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForReconfigure() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(Constants.RECONFIGURE);
		configureOtpRequestBean.setUserId(1);
		return configureOtpRequestBean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForNullAction() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(null);
		configureOtpRequestBean.setUserId(1);
		return configureOtpRequestBean;
	}
	
	public ConfigureOtpRequestBean constructConfigureOtpRequestBeanForNullUserId() {
		ConfigureOtpRequestBean configureOtpRequestBean = new ConfigureOtpRequestBean();
		configureOtpRequestBean.setAction(Constants.RECONFIGURE);
		configureOtpRequestBean.setUserId(null);
		return configureOtpRequestBean;
	}
}
