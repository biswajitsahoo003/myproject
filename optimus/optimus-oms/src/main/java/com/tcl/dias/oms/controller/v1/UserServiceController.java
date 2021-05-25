package com.tcl.dias.oms.controller.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ContactResponse;
import com.tcl.dias.oms.beans.CustomerRequestBean;
import com.tcl.dias.oms.beans.UserDetails;
import com.tcl.dias.oms.beans.UserInfoBean;
import com.tcl.dias.oms.beans.UserRequest;
import com.tcl.dias.oms.beans.UserSite;
import com.tcl.dias.oms.dto.SiteAndSolutionDto;
import com.tcl.dias.oms.dto.UserDto;
import com.tcl.dias.oms.npl.beans.UserLink;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class have all the user service related api
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/users")
public class UserServiceController {

	@Autowired
	UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceController.class);

	/**
	 * This method is used to get the user details getUserDetails
	 * 
	 * @param userRequest
	 * @return List<UserDetails>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_USER_DETAILS)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndSolutionDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserDetails>> getUserDetails(@RequestBody UserRequest userRequest)
			throws TclCommonException {
		List<UserDetails> response = userService.getUserDetails(userRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * getAllUserDetailsByCustomer this method is used get the user details based on
	 * the customer id
	 * 
	 * 
	 * @param customerId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_USER_DETAILS)
	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserDto>> getAllUserDetailsByCustomer(
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "quoteId", required = false) Integer quoteId) throws TclCommonException {

		List<UserDto> response = userService.getAllUsersByCustomer(customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getSiteInformation- This method returns the site locationId and LocalIT
	 * contact for that Id
	 * 
	 * @return UserSite
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_USER_SITE_DETAILS)
	@RequestMapping(value = "/orders/sites", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndSolutionDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserSite>> getSiteInformation() throws TclCommonException {
		List<UserSite> response = userService.getUserSiteDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VISHESH AWASTHI This method is used to get the user details
	 *         getUserDetails
	 * 
	 * @param customerId
	 * @return List<UserDetails>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndSolutionDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/customers/{customerId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<UserDetails>> getUserDetailsByCustId(@PathVariable("customerId") Integer customerId)
			throws TclCommonException {
		List<UserDetails> response = userService.getUserDetailsByCustId(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This api is used for persist the customer request.
	 * 
	 * @author Kusuma Kumar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @param CustomerRequestBean
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.CUSTOMER_REQ)
	@RequestMapping(value = "/customerrequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> persistCustomerRequset(@RequestBody CustomerRequestBean customerRequestBean)
			throws TclCommonException {
		userService.persistCustomerRequest(customerRequestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);

	}

	/**
	 * This api is used to return the pilot team contact details
	 * 
	 * @author ANNE NISHA
	 * @param none
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_PILOT_TEAM_CONTACT_DETAILS)
	@RequestMapping(value = "/admin/contact", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ContactResponse> getContactDetailsForPilotTeam() throws TclCommonException {
		ContactResponse response = userService.getContactDetailsForPilotTeam();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This api is used to return the customer details
	 * 
	 * @author ANNE NISHA
	 * @param none
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CUSTOMER_DETAILS)
	@RequestMapping(value = "/customerdetails", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerBean> getCustomerDetails(@RequestParam(name = "searchTxt",required = false) String searchTxt,@RequestParam(name = "limited",required = false) Boolean limited) throws TclCommonException {
		CustomerBean response = userService.getCustomerDetails(searchTxt,limited);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CUSTOMER_DETAILS)
	@RequestMapping(value = "/getcustomerdetails", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerBean> getCustomerDetailsForLoggedInUser(@RequestParam(name = "searchTxt",required = false) String searchTxt,@RequestParam(name = "limited",required = false) Boolean limited) throws TclCommonException {
		CustomerBean response = userService.getAllCustomerDetailsForLoggedInUser(searchTxt,limited);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getSiteInformation- This method returns the site locationId and LocalIT
	 * contact for that Id
	 * 
	 * @return UserSite
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_USER_LINK_DETAILS)
	@RequestMapping(value = "/orders/links", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndSolutionDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserLink>> getLinkInformation() throws TclCommonException {
		List<UserLink> response = userService.getUserLinkDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * @author vivek
	 * updateUserDetails
	 * used to update the user details
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.UPDATE_USER_DETAILS)
	@RequestMapping(value="/userdetails",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserInfoBean> updateUserDetails(@RequestBody UserInfoBean userRequest)
			throws TclCommonException {
		UserInfoBean response = userService.updateUserDetails(userRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * @author vivek
	 * updateUserDetails
	 * used to get the user details
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER_DETAILS)
	@RequestMapping(value="/userdetails",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserInfoBean> getUserDetails()
			throws TclCommonException {
		UserInfoBean response = userService.getUserDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @Auther Diksha
	 * Check if logining in for first time
	 * @pathvariable
	 * @return
	 * @throws TclCommonException
	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GENERAL_TERMS_AND_CONDITIONS)
//	@RequestMapping(value="/terms/conditions/{userName}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GeneralTermsAndConditionsBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<GeneralTermsAndConditionsBean> getTermsAndConditionsStatus(@PathVariable("userName") String userName)
//			throws TclCommonException {
//		GeneralTermsAndConditionsBean response = userService.getTermsAndConditionsStatus(userName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}

	/**
	 * @Auther Diksha
	 * Save user in terms and conditions
	 * @parm
	 * @return
	 * @throws TclCommonException
	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GENERAL_TERMS_AND_CONDITIONS)
//	@RequestMapping(value="/terms/conditions/add",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GeneralTermsAndConditionsBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<String> saveUserForGeneralTermsAndConditions(@RequestParam(name = "generalTermsAndConditionsId", required = false) Integer generalTermsAndConditionsId, @RequestParam(name = "publicIP", required = false) String publicIP)
//			throws TclCommonException {
//		String response = userService.saveUserForGeneralTermsAndConditions(generalTermsAndConditionsId, publicIP);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//
//	}
	
	
	
	
	
	/**
	 
	 * updateShowCosMessageForUser
	 * used to update the show cos message for user details
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.UPDATE_SHOW_COS_MESSAGE)
	@RequestMapping(value="/cospreference",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateShowCosMessageForUser(@RequestBody UserInfoBean userRequest)
			throws TclCommonException {
		String response = userService.updateCosMessagePreference(userRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response,
				Status.SUCCESS);

	}
	
	
	

}
