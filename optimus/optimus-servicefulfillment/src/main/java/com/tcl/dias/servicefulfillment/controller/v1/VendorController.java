package com.tcl.dias.servicefulfillment.controller.v1;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.VendorDetail;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillment.beans.VendorResponseBean;
import com.tcl.dias.servicefulfillment.beans.VendorsBean;
import com.tcl.dias.servicefulfillment.service.VendorService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * VendorController this class is used to get the vendor details
 * 
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;

	/**
	 * @author vivek
	 * @param name
	 * @param type
	 * @param city
	 * @param country
	 * @return
	 * @throws TclCommonException used to get master vendor detail
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.GET_VENDOR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<VendorsBean>> getVendorDetails(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "city", required = false) String city) throws TclCommonException {
		List<VendorsBean> response = vendorService.getVendorDetails(name, type, country, state, city);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to get list of all vendors
	 * 
	 * @author Mayank
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.GET_ALL_VENDOR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<VendorsBean>> getAllVendors() {
		List<VendorsBean> response = vendorService.getAllVendors();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to add a new vendor
	 *
	 * @param vendorsBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.ADD_VENDOR)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VendorsBean> addVendor(@RequestBody VendorsBean vendorsBean) {
		VendorsBean response = vendorService.addVendor(vendorsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to modify vendor details
	 *
	 * @param vendorsBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.MODIFY_VENDOR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VendorsBean> updateVendor(@RequestBody VendorsBean vendorsBean) throws TclCommonException {
		VendorsBean response = vendorService.updateVendor(vendorsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to delete a vendor
	 * 
	 * @author Mayank sharma
	 * @param vendorsBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.DELETE_VENDOR)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@DeleteMapping(value = "/delete/{vendorid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VendorsBean> deleteVendor(@PathVariable("vendorid") Integer vendorId) {
		VendorsBean response = vendorService.deleteVendorById(vendorId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to get/search vendor details from AKANA.
	 * 
	 * @author diksha garg
	 * 
	 * @param type
	 * @param city
	 * @param state
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Vendor.SEARCH_VENDOR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<VendorDetail>> searchVendorDetails(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "state", required = false) String state) throws TclCommonException {

		VendorResponseBean response = vendorService.searchVendorDetails(type, state, city);
		if (response != null && response.getVendorDetails() != null && !response.getVendorDetails().isEmpty()) {
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					response.getVendorDetails(), Status.SUCCESS);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_NOT_FOUND, ResponseResource.RES_NO_DATA,Status.ERROR);
	}
}
