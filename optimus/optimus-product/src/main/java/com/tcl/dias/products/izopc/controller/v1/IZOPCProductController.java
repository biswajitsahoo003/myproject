package com.tcl.dias.products.izopc.controller.v1;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.izopc.beans.CloudProviderAttributeBean;
import com.tcl.dias.products.izopc.beans.CloudProviderBean;
import com.tcl.dias.products.izopc.beans.DataCenterForCloudProvidersBean;
import com.tcl.dias.products.izopc.beans.DataCenterProviderDetails;
import com.tcl.dias.products.izopc.beans.ProductDataCenterProviderBean;
import com.tcl.dias.products.izopc.service.v1.IZOPCProductService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.CloudProvider;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Products;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Class having the controller methods - IZOPCProductController.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/products/izopc")
public class IZOPCProductController {

	@Autowired
	IZOPCProductService iZOPCProductService;

	/**
	 * @author vivek 
	 * getAllCloudProviderDetails - method to get list of cloud providers
	 * @return
	 * @throws TclCommonException
	 */
	/*@ApiOperation(value = CloudProvider.GET_CLOUD_PROVIDER_DETAILS)
	@RequestMapping(value = "/clouddetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CloudProviderBean>> getAllCloudProviderDetails() throws TclCommonException {
		List<CloudProviderBean> response = iZOPCProductService.getCloudProviderDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}*/
	
	/**
	 * @author vivek 
	 * getAllCloudProviderDetails - method to get list of cloud providers
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = CloudProvider.GET_CLOUD_PROVIDER_DETAILS)
	@RequestMapping(value = "/clouddetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getAllCloudProviderDetails() throws TclCommonException {
		List<String> response = iZOPCProductService.getCloudProviderDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * @author vivek 
	 * getDataCenterDetails - method to get data center details
	 * @param providerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = CloudProvider.GET_DATA_CENTER_DETAILS)
	@RequestMapping(value = "/providername/clouddetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DataCenterProviderDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<DataCenterProviderDetails>> getDataCenterDetails(
			@RequestParam(value="providername",required=true) String providerName) throws TclCommonException {
		List<DataCenterProviderDetails> response = iZOPCProductService.getDataCenterDetails(providerName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Prabhu 
	 * method to get datacenter details for cloud provider list
	 * @param providerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = CloudProvider.GET_DATA_CENTER_DETAILS)
	@RequestMapping(value = "/dataCenter/cloudProviders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<DataCenterForCloudProvidersBean>> getDataCenterDetailsForCloudProviderList(
			@RequestBody List<String> providerNameList) throws TclCommonException {
		List<DataCenterForCloudProvidersBean> response = iZOPCProductService.getDataCenterDetailsForCloudProviderList(providerNameList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Dinahar V 
	 * getCloudProviderAttributes - method to get attribute details of cloud provider
	 * @param providerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = CloudProvider.GET_CLOUD_PROVIDER_ATTRIBUTES)
	@RequestMapping(value = "/providername/attributedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CloudProviderAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CloudProviderAttributeBean>> getCloudProviderAttributes(
			@RequestParam(value="providername",required=true) String providerName,
			@RequestParam(value="attributename",required=true) String attributeName) throws TclCommonException {
		List<CloudProviderAttributeBean> response = iZOPCProductService.getCloudProviderAttribute(providerName,attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author vivek 
	 * getDataCenterDetails - method to get data center details
	 * @param providerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = CloudProvider.GET_DATA_CENTER_DETAIL_FOR_DCCODE)
	@RequestMapping(value = "/dcdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DataCenterProviderDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DataCenterProviderDetails> getDataCenter(@RequestParam(value="dcCode",required=true) String dcCode) throws TclCommonException {
		DataCenterProviderDetails response = iZOPCProductService.getDataCenter(dcCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Dinahar V 
	 * getSlaDetails - method to get SLA details for IZOPC
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_SLA_VALUE)
	@RequestMapping(value = "/sladetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CloudProviderAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SLADto>> getSlaDetails() throws TclCommonException {
		List<SLADto> response = iZOPCProductService.getSlaDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
