package com.tcl.dias.auth.usermgmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.auth.beans.UmResponse;
import com.tcl.dias.auth.beans.UserGroupToLeUpdateRequest;
import com.tcl.dias.auth.beans.UserGroupUpdateRequest;
import com.tcl.dias.auth.beans.UserLeMappingRequest;
import com.tcl.dias.auth.beans.UserMappingResponse;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.auth.service.MstUserGroupSpecification;
import com.tcl.dias.auth.usermgmt.beans.GroupUsersBean;
import com.tcl.dias.auth.usermgmt.beans.UserDetailBean;
import com.tcl.dias.common.beans.MstGroupTypeBean;
import com.tcl.dias.common.beans.MstUserGroupBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstGroupType;
import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.Partner;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserGroupToLe;
import com.tcl.dias.oms.entity.entities.UserToUserGroup;
import com.tcl.dias.oms.entity.entities.UsergroupToPartnerLe;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstGroupTypeRepository;
import com.tcl.dias.oms.entity.repository.MstUserGroupsRepository;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.entity.repository.UserGroupToLeRepository;
import com.tcl.dias.oms.entity.repository.UserGroupToPartnerLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.entity.repository.UserToUserGroupRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the UserGroupService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class UserGroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupService.class);

	@Autowired
	UserToUserGroupRepository userToUserGroupRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MstGroupTypeRepository mstGroupTypeRepository;
	
	@Autowired
	MstUserGroupsRepository mstUserGroupsRepository;
	
	@Autowired
	UserGroupToLeRepository userGroupToLeRepository;
	
	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.id.by.le.id}")
	String customerIdQueue;
	
	@Value("${rabbitmq.partner.id.by.le.id}")
	String partnerIdQueue;
	
	@Autowired
	CustomerRepository customerRepository;
	
	
	@Autowired
	UserGroupToPartnerLeRepository userGroupToPartnerLeRepository;
	
	@Autowired
	PartnerRepository partnerRepository;

	/**
	 * 
	 * getUserGroupUsers - Gives the userGroupUsers
	 * 
	 * @return
	 */
	@Transactional
	public Map<String, List<Map<String, String>>> getUserGroupUsers() {
		Map<String, List<Map<String, String>>> usergroupResponse = new HashMap<>();
		String username = Utils.getSource();
		LOGGER.info("Entering the get users list for the username {}", username);
		List<UserToUserGroup> userGroups = userToUserGroupRepository.findByUsername(username);
		for (UserToUserGroup userToUserGroup : userGroups) {
			ArrayList<Map<String, String>> userMapping = new ArrayList<>();
			usergroupResponse.put(userToUserGroup.getMstUserGroup().getGroupName(), userMapping);
			List<UserToUserGroup> userTList = userToUserGroupRepository
					.findByMstUserGroup_GroupName(userToUserGroup.getMstUserGroup().getGroupName());
			for (UserToUserGroup user : userTList) {
				User userEntity = userRepository.findByUsernameAndStatus(user.getUsername(), 1);
				if (userEntity != null) {
					Map<String, String> userM = new HashMap<>();
					userM.put("username", user.getUsername());
					userM.put("emailId", userEntity.getEmailId());
					userM.put("mobile", userEntity.getContactNo());
					userM.put("firstName", userEntity.getFirstName());
					userM.put("lastName", userEntity.getLastName());
					userM.put("usergroup", userToUserGroup.getMstUserGroup().getGroupName());
					userMapping.add(userM);
				}
			}
		}
		LOGGER.info("User Group List for Response {}", usergroupResponse);
		return usergroupResponse;
	}
	
	/**
	 * 
	 * Add or Modify group type
	 * @param mstGroupTypeBean
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String addOrModifyGroupTypeBasedOnAction(MstGroupTypeBean mstGroupTypeBean,String action) throws TclCommonException {
		if(mstGroupTypeBean==null || action==null) {
			LOGGER.error("Invalid input for adding or modifying Mst Group Type");
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (action.equals("delete")) {
			deactivateMstGroupType(mstGroupTypeBean);
		} else if(action.equals("create")) {
			persistMstGroupType(mstGroupTypeBean);
		}else if(action.equals("update")) {
			modifyMstGroupType(mstGroupTypeBean);
		}
		return null;
	}
	
	private void persistMstGroupType(MstGroupTypeBean mstGroupTypeBean) throws TclCommonException {
		try {
			MstGroupType mstGroupType = new MstGroupType();
			mstGroupType.setGroupType(mstGroupTypeBean.getGroupType());
			mstGroupType.setGroupTypeCode(mstGroupTypeBean.getGroupTypeCode());
			mstGroupType.setStatus(CommonConstants.BACTIVE);
			mstGroupTypeRepository.saveAndFlush(mstGroupType);
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		
	}
	
	private void modifyMstGroupType(MstGroupTypeBean mstGroupTypeBean) throws TclCommonException {
		if(mstGroupTypeBean.getId()==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			
			Optional<MstGroupType> mstGroupType = mstGroupTypeRepository.findById(mstGroupTypeBean.getId());
			if(mstGroupType!=null) {
				mstGroupType.get().setGroupType(mstGroupTypeBean.getGroupType());
				mstGroupType.get().setGroupTypeCode(mstGroupTypeBean.getGroupTypeCode());
				mstGroupType.get().setStatus(CommonConstants.BACTIVE);
				mstGroupTypeRepository.saveAndFlush(mstGroupType.get());
			}
			
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		
	}
	
	private void deactivateMstGroupType(MstGroupTypeBean mstGroupTypeBean) throws TclCommonException {
		if(mstGroupTypeBean.getId()==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			
			Optional<MstGroupType> mstGroupType = mstGroupTypeRepository.findById(mstGroupTypeBean.getId());
			if(mstGroupType!=null) {
				mstGroupType.get().setStatus(CommonConstants.BDEACTIVATE);
				mstGroupTypeRepository.saveAndFlush(mstGroupType.get());
			}
			
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		
	}
	
	/**
	 * 
	 * Get all MstGroupType
	 * @param searchBy
	 * @return
	 * @throws TclCommonException
	 */
	public List<MstGroupTypeBean> getAllMstGroupType(String searchBy) throws TclCommonException{
		List<MstGroupTypeBean> beans = new ArrayList<>();
		List<MstGroupType> mstGroupTypes = new ArrayList<>();
		try {
		if(!StringUtils.isAllBlank(searchBy)) {
			mstGroupTypes = mstGroupTypeRepository.searchByTypeOfTheGroup(searchBy);
		}else {
			mstGroupTypes = mstGroupTypeRepository.findAll();
		}
		if(mstGroupTypes!=null && !mstGroupTypes.isEmpty()) {
			mstGroupTypes.stream().forEach(mstGroupType->{
				beans.add(constructMstGroupTypeBean(mstGroupType));
			});
		}
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		return beans;
	}
	
	private MstGroupTypeBean constructMstGroupTypeBean(MstGroupType mstGroupType) {
		MstGroupTypeBean mstGroupTypeBean = new MstGroupTypeBean();
		mstGroupTypeBean.setGroupType(mstGroupType.getGroupType());
		mstGroupTypeBean.setGroupTypeCode(mstGroupType.getGroupTypeCode());
		mstGroupTypeBean.setId(mstGroupType.getId());
		return mstGroupTypeBean;
	}
	
	/**
	 * 
	 * Add or Modify Mst User Group
	 * @param mstUserGroupBean
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	public String addOrModifyUserGroup(MstUserGroupBean mstUserGroupBean,String action) throws TclCommonException {
		if(mstUserGroupBean==null || action==null) {
			LOGGER.error("Invalid input for adding or modifying Mst Group Type");
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (action.equals("delete")) {
			deactivateMstUserGroup(mstUserGroupBean);
		} else if(action.equals("create")) {
			persistMstUserGroup(mstUserGroupBean);
		}else if(action.equals("update")) {
			modifyMstUserGroup(mstUserGroupBean);
		}
		return null;
	}
	
	private void persistMstUserGroup(MstUserGroupBean mstUserGroupBean) throws TclCommonException {
		try {
			MstUserGroups mstUserGroup = new MstUserGroups();
			mstUserGroup.setGroupName(mstUserGroupBean.getUserGroupName());
			if (mstUserGroupBean.getGroupTypeId() != null) {
				Optional<MstGroupType> mstGroupType = mstGroupTypeRepository
						.findById(mstUserGroupBean.getGroupTypeId());
				if (mstGroupType.isPresent()) {
					mstUserGroup.setMstGroupType(mstGroupType.get());
				}
			}
			mstUserGroup.setStatus(CommonConstants.BACTIVE);
			mstUserGroupsRepository.saveAndFlush(mstUserGroup);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void modifyMstUserGroup(MstUserGroupBean mstUserGroupBean) throws TclCommonException {
		if(mstUserGroupBean.getUserGroupId()==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<MstUserGroups> mstUserGroup = mstUserGroupsRepository.findById(mstUserGroupBean.getUserGroupId());
			if(mstUserGroup.isPresent()) {
				mstUserGroup.get().setGroupName(mstUserGroupBean.getUserGroupName());
				if (mstUserGroupBean.getGroupTypeId() != null) {
					Optional<MstGroupType> mstGroupType = mstGroupTypeRepository
							.findById(mstUserGroupBean.getGroupTypeId());
					if (mstGroupType.isPresent()) {
						mstUserGroup.get().setMstGroupType(mstGroupType.get());
					}
				}
				mstUserGroupsRepository.saveAndFlush(mstUserGroup.get());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void deactivateMstUserGroup(MstUserGroupBean mstUserGroupBean) throws TclCommonException {
		if(mstUserGroupBean.getUserGroupId()==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<MstUserGroups> mstUserGroup = mstUserGroupsRepository.findById(mstUserGroupBean.getUserGroupId());
			if(mstUserGroup.isPresent()) {
				mstUserGroup.get().setStatus(CommonConstants.BDEACTIVATE);
				mstUserGroupsRepository.saveAndFlush(mstUserGroup.get());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * 
	 * Get all user group with Pagination
	 * @param page
	 * @param size
	 * @param groupName
	 * @param groupType
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<MstUserGroupBean> getAllUserGroup(Integer page,Integer size,String groupName,String groupType,String status) throws TclCommonException{
		if(page==null && size==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<MstUserGroupBean> mstUserGroupBeans = new ArrayList<>();
			Page<MstUserGroups> mstUserGroups = null;
			Byte bstatus=CommonConstants.BACTIVE;
			if (status!=null && status.equals(CommonConstants.DSTATUS)) {
				bstatus=CommonConstants.BDEACTIVATE;
			}
			Specification<MstUserGroups> specs = MstUserGroupSpecification.geMsttUserGroups(groupName, groupType,bstatus);
			mstUserGroups = mstUserGroupsRepository.findAll(specs, PageRequest.of(page - 1, size));
			if(mstUserGroups!=null) {
				mstUserGroups.stream().forEach(mstUserGroup->{
					mstUserGroupBeans.add(constructMstUserGroupBean(mstUserGroup));
				});
				return new PagedResult<>(mstUserGroupBeans, mstUserGroups.getTotalElements(), mstUserGroups.getTotalPages());
			}
			return null;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private MstUserGroupBean constructMstUserGroupBean(MstUserGroups mstUserGroup) {
		Integer userCount=userToUserGroupRepository.countByMstGroupId(mstUserGroup.getId());
		MstUserGroupBean mstUserGroupBean = new MstUserGroupBean();
		if (mstUserGroup.getMstGroupType() != null && mstUserGroup.getMstGroupType().getGroupType() != null) {
			mstUserGroupBean.setGroupTypeName(mstUserGroup.getMstGroupType().getGroupType());
			mstUserGroupBean.setGroupTypeId(mstUserGroup.getMstGroupType().getId());
		}
		mstUserGroupBean.setUserGroupId(mstUserGroup.getId());
		mstUserGroupBean.setUserGroupName(mstUserGroup.getGroupName());
		mstUserGroupBean.setUserCount(userCount);
		return mstUserGroupBean;
	}
	
	private MstUserGroupBean constructMstUserGroupBeansFromMstUserGroupBeans(MstUserGroups mstUserGroup,
			List<Map<String,Object>> leIds,List<Map<String,Object>> partnerLeIds,List<Map<String,Object>> userIds) throws TclCommonException, IllegalArgumentException {
		MstUserGroupBean mstUserGroupBean = new MstUserGroupBean();
		if (mstUserGroup.getMstGroupType() != null && mstUserGroup.getMstGroupType().getGroupType() != null) {
			mstUserGroupBean.setGroupTypeName(mstUserGroup.getMstGroupType().getGroupType());
			mstUserGroupBean.setGroupTypeId(mstUserGroup.getMstGroupType().getId());
		}
		mstUserGroupBean.setLeIds(leIds);
		mstUserGroupBean.setPartnerLeIds(partnerLeIds);
		mstUserGroupBean.setUserIds(userIds);
		mstUserGroupBean.setUserGroupId(mstUserGroup.getId());
		mstUserGroupBean.setUserGroupName(mstUserGroup.getGroupName());
		return mstUserGroupBean;
	}
	
	/**
	 * 
	 * Get All Mst User Group without Pagination
	 * @param search
	 * @return
	 * @throws TclCommonException
	 */
	public List<MstUserGroupBean> getAllMstUserGroup(String search) throws TclCommonException{
		try {
			List<MstUserGroupBean> mstUserGroupBeans = new ArrayList<>();
			List<MstUserGroups> mstUserGroups = new ArrayList<>();
			if(StringUtils.isNotEmpty(search)) {
				mstUserGroups = mstUserGroupsRepository.searchByNameOfTheGroup(search);
			}else {
				mstUserGroups = mstUserGroupsRepository.findAll();
			}
			if(mstUserGroups!=null && !mstUserGroups.isEmpty()) {
				mstUserGroups.stream().forEach(mstUserGroup ->{
					mstUserGroupBeans.add(constructMstUserGroupBean(mstUserGroup));
				});
			}
			return mstUserGroupBeans;
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	public MstUserGroupBean getUserGroupById(Integer userGroupId) throws TclCommonException {
		if(userGroupId==null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<MstUserGroups> mstUserGroups = mstUserGroupsRepository.findById(userGroupId);
			List<Map<String,Object>> leIds = new ArrayList<>();
			List<Map<String,Object>> partnerLeIds = new ArrayList<>();
			List<Map<String,Object>> userIds = new ArrayList<>();
			if(mstUserGroups.isPresent()) {
				List<UserGroupToLe> userGroupToLes = userGroupToLeRepository.findByMstUserGroups(mstUserGroups.get());
				if(userGroupToLes!=null && !userGroupToLes.isEmpty()) {
					userGroupToLes.stream().forEach(userGroupToLe->{
						if(userGroupToLe.getErfCusCustomerLeId()!=null) {
							Map<String,Object> leIdsMap = new HashMap<>();
							leIdsMap.put("mappingId", userGroupToLe.getId());
							leIdsMap.put("customerLeId", userGroupToLe.getErfCusCustomerLeId());
							leIds.add(leIdsMap);
						}
					});
				}
				List<UsergroupToPartnerLe> usergroupToPartnerLes = userGroupToPartnerLeRepository.findByMstUserGroup(mstUserGroups.get());
				if(usergroupToPartnerLes!=null && !usergroupToPartnerLes.isEmpty()) {
					usergroupToPartnerLes.stream().forEach(userGroupToPartnerLe->{
						if(userGroupToPartnerLe.getErfCusPartnerLeId()!=null) {
							Map<String,Object> leIdsMap = new HashMap<>();
							leIdsMap.put("mappingId", userGroupToPartnerLe.getId());
							leIdsMap.put("partnerLeId", userGroupToPartnerLe.getErfCusPartnerLeId());
							partnerLeIds.add(leIdsMap);
						}
					});
				}
				List<UserToUserGroup> userToUserGroups = userToUserGroupRepository.findByMstUserGroup(mstUserGroups.get());
				if(userToUserGroups!=null && !userToUserGroups.isEmpty()) {
					userToUserGroups.stream().forEach(userToUserGroup->{
						if(userToUserGroup.getUserId()!=null) {
							Map<String,Object> leIdsMap = new HashMap<>();
							leIdsMap.put("mappingId", userToUserGroup.getId());
							leIdsMap.put("userId", userToUserGroup.getUserId());
							leIdsMap.put("userName", userToUserGroup.getUsername());
							userIds.add(leIdsMap);
						}
					});
				}
				return constructMstUserGroupBeansFromMstUserGroupBeans(mstUserGroups.get(),leIds,partnerLeIds,userIds);
			}
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}
	
	/**
	 * 
	 * Adds or modifies Existing user group to le mapping
	 * @param userGroupUpdateRequest
	 * @return
	 * @throws TclCommonException
	 */
	public String updateTheLeAndGroupMapping(UserGroupUpdateRequest userGroupUpdateRequest) throws TclCommonException {
		if (userGroupUpdateRequest == null || userGroupUpdateRequest.getUserGroupId() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<MstUserGroups> mstUserGroupsOpt = mstUserGroupsRepository
					.findById(userGroupUpdateRequest.getUserGroupId());
			if (mstUserGroupsOpt.isPresent()) {
				if (userGroupUpdateRequest.getUserGroupToLeUpdateRequests() != null
						&& !userGroupUpdateRequest.getUserGroupToLeUpdateRequests().isEmpty()) {
					addOrModifyTheUserGroupToCustomerLe(userGroupUpdateRequest, mstUserGroupsOpt.get());
				}
				if (userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests() != null
						&& !userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests().isEmpty()) {
					addOrModifyTheUserGroupToPartnerLe(userGroupUpdateRequest, mstUserGroupsOpt.get());
				}
				if (userGroupUpdateRequest.getUserGroupToUserUpdateRequests() != null
						&& !userGroupUpdateRequest.getUserGroupToUserUpdateRequests().isEmpty()) {
					addOrModifyUserToUserGroup(userGroupUpdateRequest, mstUserGroupsOpt.get());
				}
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private void addOrModifyTheUserGroupToCustomerLe(UserGroupUpdateRequest userGroupUpdateRequest,MstUserGroups mstUserGroups)
			throws TclCommonException {
		try {
			if (userGroupUpdateRequest.getUserGroupToLeUpdateRequests() != null
					&& !userGroupUpdateRequest.getUserGroupToLeUpdateRequests().isEmpty()) {
				List<Integer> mappingIds = getAllUserGroupMappingIdsForTheRequest(userGroupUpdateRequest);
				if (mappingIds != null && !mappingIds.isEmpty()) {
					List<UserGroupToLe> userGroupToLes = userGroupToLeRepository.findByIdNotInAndMstUserGroups(mappingIds,mstUserGroups);
					if(userGroupToLes!=null && !userGroupToLes.isEmpty()) {
						userGroupToLeRepository.deleteAll(userGroupToLes);
					}
				}else {
					userGroupToLeRepository.deleteUserGroupMappings(mstUserGroups.getId());
				}
				List<Integer> leIds=new ArrayList<>();
				for (UserGroupToLeUpdateRequest userGroupReq : userGroupUpdateRequest.getUserGroupToLeUpdateRequests()) {
					leIds.add(userGroupReq.getLeId());
				}
				LOGGER.info("leIds {}",leIds);
				Map<Integer,Integer> responseMapper=new HashMap<>();
				String response=(String)mqUtils.sendAndReceive(customerIdQueue, Utils.convertObjectToJson(leIds));
				if(response!=null) {
					responseMapper=(Map)Utils.convertJsonToObject(response, Map.class);
					LOGGER.info("Customer Id response received {}",responseMapper);
				}
				for (UserGroupToLeUpdateRequest updateReq : userGroupUpdateRequest.getUserGroupToLeUpdateRequests()) {
					UserGroupToLe userGroupToLe = new UserGroupToLe();

					if (updateReq.getLeMappingId() != null) {
						Optional<UserGroupToLe> userGroupToLeOpt = userGroupToLeRepository
								.findById(updateReq.getLeMappingId());
						if (userGroupToLeOpt.isPresent()) {
							userGroupToLe = userGroupToLeOpt.get();
						}
					}
					LOGGER.info("Searching for LeId {} and result {}",updateReq.getLeId(),responseMapper.get(updateReq.getLeId()+""));
					if (responseMapper.get(updateReq.getLeId()+"") != null) {
						LOGGER.info("Erf customer Id is {}",responseMapper.get(updateReq.getLeId()+""));
						Customer customer=customerRepository.findByErfCusCustomerId(responseMapper.get(updateReq.getLeId()+""));
						LOGGER.info("Customer Id {}",customer.getId());
						userGroupToLe.setCustomer(customer);
					}
					userGroupToLe.setErfCusCustomerLeId(updateReq.getLeId());
					userGroupToLe.setMstUserGroups(mstUserGroups);
					userGroupToLeRepository.saveAndFlush(userGroupToLe);

				}
			
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private List<Integer> getAllUserGroupMappingIdsForTheRequest(UserGroupUpdateRequest userGroupUpdateRequest){
		List<Integer> mappingIds = new ArrayList<>();
		userGroupUpdateRequest.getUserGroupToLeUpdateRequests().stream().forEach(request->{
			if(request.getLeMappingId()!=null) {
				mappingIds.add(request.getLeMappingId());
			}
		});
		return mappingIds;
	}
	
	private List<Integer> getAllUserGroupMappingIdsForTheRequestPartner(UserGroupUpdateRequest userGroupUpdateRequest){
		List<Integer> mappingIds = new ArrayList<>();
		userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests().stream().forEach(request->{
			if(request.getLeMappingId()!=null) {
				mappingIds.add(request.getLeMappingId());
			}
		});
		return mappingIds;
	}
	
	private void addOrModifyTheUserGroupToPartnerLe(UserGroupUpdateRequest userGroupUpdateRequest,MstUserGroups mstUserGroups)
			throws TclCommonException {
		try {
			if (userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests() != null
					&& !userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests().isEmpty()) {
				List<Integer> mappingIds = getAllUserGroupMappingIdsForTheRequestPartner(userGroupUpdateRequest);
				if (mappingIds != null && !mappingIds.isEmpty()) {
					List<UsergroupToPartnerLe> usergroupToPartnerLes = userGroupToPartnerLeRepository
							.findByIdNotInAndMstUserGroup(mappingIds,mstUserGroups);
					if (usergroupToPartnerLes != null && !usergroupToPartnerLes.isEmpty()) {
						userGroupToPartnerLeRepository.deleteAll(usergroupToPartnerLes);
					}
				}else {
					userGroupToPartnerLeRepository.deleteUserGroupMappings(mstUserGroups.getId());
				}
				List<Integer> leIds=new ArrayList<>();
				for (UserGroupToLeUpdateRequest userGroupReq : userGroupUpdateRequest.getUserGroupToPartnerLeUpdateRequests()) {
					leIds.add(userGroupReq.getLeId());
				}
				LOGGER.info("leIds {}",leIds);
				Map<Integer,Integer> responseMapper=new HashMap<>();
				String response=(String)mqUtils.sendAndReceive(partnerIdQueue, Utils.convertObjectToJson(leIds));
				if(response!=null) {
					responseMapper=(Map)Utils.convertJsonToObject(response, Map.class);
					LOGGER.info("Partner Id response received {}",responseMapper);
				}
				for (UserGroupToLeUpdateRequest updateReq : userGroupUpdateRequest
						.getUserGroupToPartnerLeUpdateRequests()) {
					UsergroupToPartnerLe usergroupToPartnerLe = new UsergroupToPartnerLe();

					if (updateReq.getLeMappingId() != null) {
						Optional<UsergroupToPartnerLe> usergroupToPartnerLeOpt = userGroupToPartnerLeRepository
								.findById(updateReq.getLeMappingId());
						if (usergroupToPartnerLeOpt.isPresent()) {
							usergroupToPartnerLe = usergroupToPartnerLeOpt.get();
						}
					}
					LOGGER.info("Searching for LeId {} and result {}",updateReq.getLeId(),responseMapper.get(updateReq.getLeId()+""));
					if (responseMapper.get(updateReq.getLeId() +"") != null) {
						LOGGER.info("Erf partner Id is {}",responseMapper.get(updateReq.getLeId()+""));
						Optional<Partner> partner=partnerRepository.findByErfCusPartnerId(responseMapper.get(updateReq.getLeId()+""));
						LOGGER.info("Partner Id {}",partner.get().getId());
						usergroupToPartnerLe.setPartner(partner.get());
					}
					usergroupToPartnerLe.setErfCusPartnerLeId(updateReq.getLeId());
					usergroupToPartnerLe.setMstUserGroup(mstUserGroups);
					try {
						userGroupToPartnerLeRepository.saveAndFlush(usergroupToPartnerLe);
					} catch (Exception e) {
						LOGGER.error("Error in saving {}",e);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private List<Integer> getAllUserGroupMappingIdsForTheUser(UserGroupUpdateRequest userGroupUpdateRequest){
		List<Integer> mappingIds = new ArrayList<>();
		userGroupUpdateRequest.getUserGroupToUserUpdateRequests().stream().forEach(req->{
			if(req.getUserMappingId()!=null) {
				mappingIds.add(req.getUserMappingId());
			}
		});
		return mappingIds;
	}
	
	
	private void addOrModifyUserToUserGroup(UserGroupUpdateRequest userGroupUpdateRequest,MstUserGroups mstUserGroups)
			throws TclCommonException {
		try {
			if (userGroupUpdateRequest.getUserGroupToUserUpdateRequests() != null
					&& !userGroupUpdateRequest.getUserGroupToUserUpdateRequests().isEmpty()) {
				List<Integer> mappingIds = getAllUserGroupMappingIdsForTheUser(userGroupUpdateRequest);
				if (mappingIds != null && !mappingIds.isEmpty()) {
					List<UserToUserGroup> userToUserGroups = userToUserGroupRepository.findByIdNotInAndMstUserGroup(mappingIds,mstUserGroups);
					if (userToUserGroups != null && !userToUserGroups.isEmpty()) {
						userToUserGroupRepository.deleteAll(userToUserGroups);
					}
				}else {
					userToUserGroupRepository.deleteUserGroupMappings(mstUserGroups.getId());
				}
				userGroupUpdateRequest.getUserGroupToUserUpdateRequests().stream().forEach(updateReq -> {
					UserToUserGroup userToUserGroup = new UserToUserGroup();

					if (updateReq.getUserMappingId() != null) {
						Optional<UserToUserGroup> userToUserGroupOptional = userToUserGroupRepository
								.findById(updateReq.getUserMappingId());
						if (userToUserGroupOptional.isPresent()) {
							userToUserGroup = userToUserGroupOptional.get();
						}
					}
					Optional<MstUserGroups> mstUserGroup = mstUserGroupsRepository.findById(mstUserGroups.getId());

					userToUserGroup.setMstUserGroup(mstUserGroup.get());
					userToUserGroup.setUserId(updateReq.getUserId());
					userToUserGroup.setUsername(updateReq.getUserName());
					try {
						userToUserGroupRepository.saveAndFlush(userToUserGroup);
					} catch (Exception e) {
						LOGGER.error("Error in saving userToUser Group",e);
					}

				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * 
	 * @param userGroupId--This method gets the userGroup details and updates the
	 *                          status
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
	public String updateStatusOfUserGroup(Integer userGroupId, String status) throws TclCommonException {
		if (userGroupId == null && status.equals(null)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT);
		}
		try {
			Optional<MstUserGroups> mstUserGroup = mstUserGroupsRepository.findById(userGroupId);
			if (mstUserGroup.isPresent()) {
				LOGGER.info("Getting The Particular User group details{}", mstUserGroup.get());
				MstUserGroups mstUser = mstUserGroup.get();
				if (status.equals(CommonConstants.ASTATUS)) {
					mstUser.setStatus(CommonConstants.BACTIVE);
				} else if (status.equals(CommonConstants.DSTATUS)) {
					mstUser.setStatus(CommonConstants.BDEACTIVATE);
				} else {
					return CommonConstants.INVALID;
				}
				mstUserGroupsRepository.save(mstUser);

			} else {
				return CommonConstants.FSTATUS;
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return CommonConstants.SUCCESS;
	}
	
	public UmResponse processUserGroupMapping(GroupUsersBean groupUsersBean, Integer userGroupId)
			throws TclCommonException {
		UmResponse usersResponse = new UmResponse("User Group mapped successfully!!!");
		List<String> validationMessages = new ArrayList<>();
		try {
			String validationResponse = validateUserGroupMapping(groupUsersBean, userGroupId);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn("Validation error with message {}", validationResponse);
				usersResponse.setMessage(validationResponse);
				usersResponse.setStatus(Status.FAILURE);
				return usersResponse;
			}
			Optional<MstUserGroups> mstUserGroupEntity = mstUserGroupsRepository.findById(userGroupId);
			if (mstUserGroupEntity.isPresent()) {
				for (UserDetailBean deletingUsers : groupUsersBean.getReductionalUsers()) {
					List<UserToUserGroup> userToUserGroupEntity = userToUserGroupRepository
							.findByUsernameAndMstUserGroup(deletingUsers.getUserName(), mstUserGroupEntity.get());
					for (UserToUserGroup userToUserGroup : userToUserGroupEntity) {
						LOGGER.info("Deleting the entry {} with userName {}", userToUserGroup.getId(),
								deletingUsers.getUserName());
						userToUserGroupRepository.delete(userToUserGroup);
					}
				}

				for (UserDetailBean addUsers : groupUsersBean.getAdditionalUsers()) {
					List<UserToUserGroup> userToUserGroupEntity = userToUserGroupRepository
							.findByUsernameAndMstUserGroup(addUsers.getUserName(), mstUserGroupEntity.get());
					if (userToUserGroupEntity.isEmpty()) {
						LOGGER.info("Adding the entry with user {}", addUsers.getUserName());
						UserToUserGroup userToUserGroup = new UserToUserGroup();
						userToUserGroup.setMstUserGroup(mstUserGroupEntity.get());
						userToUserGroup.setUserId(addUsers.getUserId());
						userToUserGroup.setUsername(addUsers.getUserName());
						userToUserGroupRepository.save(userToUserGroup);
					}else {
						validationMessages.add("Already  "+ addUsers.getUserName() + " is mapped");
						LOGGER.info("Already the usergroup {} is assigned to {}", userGroupId, addUsers.getUserId());
					}

				}
				validationResponse= validationMessages.stream().collect(Collectors.joining(","));
				if (StringUtils.isNotBlank(validationResponse)) {
					usersResponse.setMessage(validationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processUserGroupMapping user", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return usersResponse;

	}
	
	private String validateUserGroupMapping(GroupUsersBean groupUsersBean,Integer userGroupId) {
		List<String> validationMessages = new ArrayList<>();
		if (groupUsersBean.getAdditionalUsers().isEmpty()
				&& groupUsersBean.getReductionalUsers().isEmpty()) {
			validationMessages.add("users are not added nor removed");
		}
		
		if(!groupUsersBean.getAdditionalUsers().isEmpty()) {
			for (UserDetailBean userDetail : groupUsersBean.getAdditionalUsers()) {
				if(StringUtils.isBlank(userDetail.getUserName()) && userDetail.getUserId() ==null) {
					validationMessages.add("Either EmailId or UserId is Mandatory");
				}else {
					if(StringUtils.isNotBlank(userDetail.getUserName())) {
						User userEntity=userRepository.findByUsernameAndStatus(userDetail.getUserName(), CommonConstants.ACTIVE);
						if(userEntity!=null) {
							userDetail.setUserId(userEntity.getId());
						}else {
							validationMessages.add("invalid email/username "+userDetail.getUserName());
						}
					}else {
						Optional<User> userEntity=userRepository.findById(userDetail.getUserId());
						if(!userEntity.isPresent()) {
							userDetail.setUserName(userEntity.get().getUsername());
						}else {
							validationMessages.add("invalid userId "+userDetail.getUserId());
						}
					}
				}
			}
		}
	
		if (userGroupId ==null) {
			validationMessages.add("user group Id is mandatory");
		}else {
			Optional<MstUserGroups> mstGroupsEntity=mstUserGroupsRepository.findById(userGroupId);
			if(!mstGroupsEntity.isPresent()) {
				validationMessages.add("Invalid UserGroup");
			}
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}
	
	private String validateUserGroupMapping(Integer userGroupId) {
		List<String> validationMessages = new ArrayList<>();
		if (userGroupId ==null) {
			validationMessages.add("user group Id is mandatory");
		}else {
			Optional<MstUserGroups> mstGroupsEntity=mstUserGroupsRepository.findById(userGroupId);
			if(!mstGroupsEntity.isPresent()) {
				validationMessages.add("Invalid UserGroup");
			}
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}
	
	/**
	 * 
	 * @param userGroupId
	 * @param page
	 * @param size
	 * @param searchTxt
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<UserDetailBean> getGroupUserMappings(Integer userGroupId, Integer page,
			Integer size, String searchTxt) throws TclCommonException {
		List<UserDetailBean> userDetailBeans = new ArrayList<>();
		long totalSize=0;
		Integer totalPage=null;
		try {
			String validationResponse = validateUserGroupMapping(userGroupId);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn(validationResponse);
				throw new TclCommonException(validationResponse, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Request for getting all the user mapping information for {} ,{},{},{}",  userGroupId, page,
					size, searchTxt);
			PageRequest pageable = null;
			if (page != null && size != null) {
				pageable = PageRequest.of(page-1, size);
			}
				Specification<UserToUserGroup> groupSpecification=GroupMappingSpecification.getUserToUserGroupSpectification(userGroupId, searchTxt);
				Page<UserToUserGroup> pagedUsers = userToUserGroupRepository.findAll(groupSpecification, pageable);
				List<UserToUserGroup> users = pagedUsers.getContent();
				for (UserToUserGroup userToUserGroup : users) {
					userDetailBeans.add(new UserDetailBean(userToUserGroup.getUsername(),userToUserGroup.getUserId()));
				}
				totalSize= pagedUsers.getTotalElements();
				totalPage= pagedUsers.getTotalPages();
		} catch (Exception e) {
			LOGGER.error("Error in getGroupUserMappings user", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult<>(userDetailBeans,totalSize, totalPage);
	}
	
	public PagedResult<UserMappingResponse> getUserGroupMappings(String userMode, Integer userGroupId, Integer page,
			Integer size, String searchTxt) throws TclCommonException {
		List<UserMappingResponse> userMapping = new ArrayList<>();
		long totalSize=0;
		Integer totalPage=null;
		try {
			String validationResponse = validateUserGroupMappings(userMode, userGroupId);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn(validationResponse);
				throw new TclCommonException(validationResponse, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Request for getting all the user group mapping information for {}, {} ,{},{},{}", userMode, userGroupId, page,
					size, searchTxt);
			PageRequest pageable = null;
			if (page != null && size != null) {
				pageable = PageRequest.of(page-1, size);
			}
			if(userMode.equalsIgnoreCase("customer")) {
				Specification<UserGroupToLe> groupSpecification=GroupMappingSpecification.getUserToCustomerLeMapping(userGroupId, searchTxt);
				Page<UserGroupToLe> pagedUserGroupLe = userGroupToLeRepository.findAll(groupSpecification, pageable);
				List<UserGroupToLe> pagedUserGroupLes = pagedUserGroupLe.getContent();
				for (UserGroupToLe userGroupToCustomerLe : pagedUserGroupLes) {
					userMapping.add(new UserMappingResponse(userGroupToCustomerLe.getErfCusCustomerLeId(),userGroupToCustomerLe.getErfCustomerLeName()));
				}
				totalSize= pagedUserGroupLe.getTotalElements();
				totalPage= pagedUserGroupLe.getTotalPages();
						
			}else {
				Specification<UsergroupToPartnerLe> groupSpecification=GroupMappingSpecification.getUserToPartnerLeMapping(userGroupId, searchTxt);
				Page<UsergroupToPartnerLe> pagedUserGroupLe = userGroupToPartnerLeRepository.findAll(groupSpecification, pageable);
				List<UsergroupToPartnerLe> pagedUserGroupLes = pagedUserGroupLe.getContent();
				for (UsergroupToPartnerLe usergroupToPartnerLe : pagedUserGroupLes) {
					userMapping.add(new UserMappingResponse(usergroupToPartnerLe.getErfCusPartnerLeId(),usergroupToPartnerLe.getErfCusPartnerLeName()));
				}
				totalSize= pagedUserGroupLe.getTotalElements();
				totalPage= pagedUserGroupLe.getTotalPages();
			}
			
			
		} catch (Exception e) {
			LOGGER.error("Error in getUserGroupMappings user", e);
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
	public UmResponse processUserGroupMapping(UserLeMappingRequest userLeMappingRequest,Integer userGroupId) throws TclCommonException {
		UmResponse usersResponse = new UmResponse("User Group mapped successfully!!!");
		try {
			String validationResponse = validateUserGroupMapping(userLeMappingRequest,userGroupId);
			if (StringUtils.isNotBlank(validationResponse)) {
				LOGGER.warn("Validation error with message {}", validationResponse);
				usersResponse.setMessage(validationResponse);
				usersResponse.setStatus(Status.FAILURE);
				return usersResponse;
			}
			Optional<MstUserGroups> mstUserGroupEntity = mstUserGroupsRepository.findById(userGroupId);
			if (mstUserGroupEntity.isPresent()) {
				if (userLeMappingRequest.getUserMode().equalsIgnoreCase("customer")) {
					validationResponse=processCustomerGroupMapping(userLeMappingRequest, mstUserGroupEntity);
				}
				
				if (userLeMappingRequest.getUserMode().equalsIgnoreCase("partner")) {
					validationResponse=processPartnerUserGroupMapping(userLeMappingRequest, mstUserGroupEntity);
				}
				if (StringUtils.isNotBlank(validationResponse)) {
					usersResponse.setMessage(validationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processUserGroupMapping user", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return usersResponse;

	}

	@SuppressWarnings("unchecked")
	private String processCustomerGroupMapping(UserLeMappingRequest userLeMappingRequest, Optional<MstUserGroups> mstUserGroupEntity)
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
			List<UserGroupToLe> userGroupToLes = userGroupToLeRepository
					.findByMstUserGroupsAndErfCusCustomerLeId(mstUserGroupEntity.get(), deletingLegalEntity.getId());
			for (UserGroupToLe userGroupToLe : userGroupToLes) {
				LOGGER.info("Deleting the entry {} with customer le {}", userGroupToLe.getId(),
						userGroupToLe.getErfCusCustomerLeId());
				userGroupToLeRepository.delete(userGroupToLe);
			}
		}

		for (UserMappingResponse addLegalEntity : userLeMappingRequest.getAdditionalLegalEntites()) {
			List<UserGroupToLe> userGroupToLes = userGroupToLeRepository
					.findByMstUserGroupsAndErfCusCustomerLeId(mstUserGroupEntity.get(), addLegalEntity.getId());
			if (userGroupToLes.isEmpty()) {
				LOGGER.info("Adding the entry with customer le {}",  addLegalEntity.getId());
				@SuppressWarnings("unlikely-arg-type")
				Integer erfCustomerId = responseMapper.get( addLegalEntity.getId()+"");
				if (erfCustomerId != null) {
					Customer customerEntity = customerRepository.findByErfCusCustomerId(erfCustomerId);
					if (customerEntity != null) {
						UserGroupToLe userGroupToLe = new UserGroupToLe();
						userGroupToLe.setMstUserGroups(mstUserGroupEntity.get());
						userGroupToLe.setErfCusCustomerLeId( addLegalEntity.getId());
						userGroupToLe.setErfCustomerLeName( addLegalEntity.getEntityName());
						userGroupToLe.setCustomer(customerEntity);
						userGroupToLeRepository.save(userGroupToLe);
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
	private String processPartnerUserGroupMapping(UserLeMappingRequest userLeMappingRequest, Optional<MstUserGroups> mstUserGroupEntity)
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
			List<UsergroupToPartnerLe> userGroupToPartnerLeEntity = userGroupToPartnerLeRepository
					.findByMstUserGroupAndErfCusPartnerLeId(mstUserGroupEntity.get(), deletingLegalEntity.getId());
			for (UsergroupToPartnerLe userGroupToLe : userGroupToPartnerLeEntity) {
				LOGGER.info("Deleting the entry {} with partner le {}", userGroupToLe.getId(),
						userGroupToLe.getErfCusPartnerLeId());
				userGroupToPartnerLeRepository.delete(userGroupToLe);
			}
		}

		for (UserMappingResponse addLegalEntity : userLeMappingRequest.getAdditionalLegalEntites()) {
			List<UsergroupToPartnerLe> userGroupToPartnerLeEntity = userGroupToPartnerLeRepository
					.findByMstUserGroupAndErfCusPartnerLeId(mstUserGroupEntity.get(), addLegalEntity.getId());
			if (userGroupToPartnerLeEntity.isEmpty()) {
				LOGGER.info("Adding the entry with partner le {}", addLegalEntity.getId());
				@SuppressWarnings("unlikely-arg-type")
				Integer erfPartnerId = responseMapper.get(addLegalEntity.getId()+"");
				if (erfPartnerId != null) {
					Optional<Partner> partnerEntity = partnerRepository.findByErfCusPartnerId(erfPartnerId);
					if (partnerEntity.isPresent()) {
						UsergroupToPartnerLe usergroupToPartnerLe = new UsergroupToPartnerLe();
						usergroupToPartnerLe.setMstUserGroup(mstUserGroupEntity.get());
						usergroupToPartnerLe.setPartner(partnerEntity.get());
						usergroupToPartnerLe.setErfCusPartnerLeId(addLegalEntity.getId());
						usergroupToPartnerLe.setErfCusPartnerLeName(addLegalEntity.getEntityName());
						userGroupToPartnerLeRepository.save(usergroupToPartnerLe);
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
				LOGGER.info("Already the partner le {} is assigned", addLegalEntity.getId());
			}

		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

	/**
	 * validation 
	 * @param userLeMappingRequest
	 * @return String
	 */
	private String validateUserGroupMapping(UserLeMappingRequest userLeMappingRequest,Integer userGroupId) {
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
		if (userGroupId == null) {
			validationMessages.add("user Id is mandatory");
		} else {
			Optional<MstUserGroups> userGroupEntity = mstUserGroupsRepository.findById(userGroupId);
			if (!userGroupEntity.isPresent()) {
				validationMessages.add("Invalid User Group");
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
	private String validateUserGroupMappings(String userMode, Integer userGroupId) {
		List<String> validationMessages = new ArrayList<>();
		if (StringUtils.isNotBlank(userMode)) {
			if (!(userMode.equalsIgnoreCase("customer") || userMode.equalsIgnoreCase("partner"))) {
				validationMessages.add("userMode can only have CUSTOMER,PARTNER");
			}

		} else {
			validationMessages.add("userMode cannot be empty/null");
		}
		if (userGroupId == null) {
			validationMessages.add("user Id is mandatory");
		} else {
			Optional<MstUserGroups> userGroupEntity = mstUserGroupsRepository.findById(userGroupId);
			if (!userGroupEntity.isPresent()) {
				validationMessages.add("Invalid User Group");
			}
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

}
