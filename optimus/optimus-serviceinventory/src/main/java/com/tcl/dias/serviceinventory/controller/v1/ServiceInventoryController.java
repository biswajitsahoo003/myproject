package com.tcl.dias.serviceinventory.controller.v1;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;
import com.lowagie.text.DocumentException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.common.beans.SuipListBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
import com.tcl.dias.common.serviceinventory.bean.RfDumpWirelessOneBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.AppLinkUtilizationBean;
import com.tcl.dias.serviceinventory.beans.ApplicationInfoRequestBean;
import com.tcl.dias.serviceinventory.beans.AttachmentBean;
import com.tcl.dias.serviceinventory.beans.CloudServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.CpeSyncStatusBean;
import com.tcl.dias.serviceinventory.beans.CustomerOrderDetailBean;
import com.tcl.dias.serviceinventory.beans.GSCConfigurationDisntictDetailsBean;
import com.tcl.dias.serviceinventory.beans.GdeSIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.GdeSIServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.GscServiceInventoryConfigurationRequestBean;
import com.tcl.dias.serviceinventory.beans.IPCInformationBean;
import com.tcl.dias.serviceinventory.beans.IpcProductSolutionBean;
import com.tcl.dias.serviceinventory.beans.IpcSolutionDetail;
import com.tcl.dias.serviceinventory.beans.NPLSIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.PagedResultWithTimestamp;
import com.tcl.dias.serviceinventory.beans.PartnerDetailBean;
import com.tcl.dias.serviceinventory.beans.ProductFamilyRequest;
import com.tcl.dias.serviceinventory.beans.SIConfigurationCountryBean;
import com.tcl.dias.serviceinventory.beans.SIDetailedInfoResponse;
import com.tcl.dias.serviceinventory.beans.SINumberConfigurationBean;
import com.tcl.dias.serviceinventory.beans.SIOrderBean;
import com.tcl.dias.serviceinventory.beans.SIProductFamilySummaryBean;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.SIServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.SISiteConfigurationBean;
import com.tcl.dias.serviceinventory.beans.SdwanAliasUpdateRequest;
import com.tcl.dias.serviceinventory.beans.SdwanApplications;
import com.tcl.dias.serviceinventory.beans.SdwanCPEInformationBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpeAllDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyDetailsRequestBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyListBean;
import com.tcl.dias.serviceinventory.beans.SdwanServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsBean;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsPerformaceBean;
import com.tcl.dias.serviceinventory.beans.SdwanSiteUtilizationDetails;
import com.tcl.dias.serviceinventory.beans.SdwanTaskDetailsBean;
import com.tcl.dias.serviceinventory.beans.SdwanTemplateDetailBean;
import com.tcl.dias.serviceinventory.beans.ServiceCatalogRequest;
import com.tcl.dias.serviceinventory.beans.ServiceIdListBean;
import com.tcl.dias.serviceinventory.beans.SiAttachmentBean;
import com.tcl.dias.serviceinventory.beans.SiteAndCpeStatusCount;
import com.tcl.dias.serviceinventory.beans.SiteDetailsSearchRequest;
import com.tcl.dias.serviceinventory.beans.TemplateCpeStatusResponse;
import com.tcl.dias.serviceinventory.beans.TemplateDetails;
import com.tcl.dias.serviceinventory.beans.TemplatePathInterfaceDetail;
import com.tcl.dias.serviceinventory.beans.UpdateDIDServiceBean;
import com.tcl.dias.serviceinventory.beans.UpdateDataServiceBean;
import com.tcl.dias.serviceinventory.beans.UpdateTollFreeServiceBean;
import com.tcl.dias.serviceinventory.beans.VersaAddressListBean;
import com.tcl.dias.serviceinventory.beans.VersaApplicationNames;
import com.tcl.dias.serviceinventory.beans.VersaUserDefAppRequest;
import com.tcl.dias.serviceinventory.beans.ViewSiServiceInfoAllBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.BandwidthUtilizationOfApp;
import com.tcl.dias.serviceinventory.izosdwan.beans.CiscoAliasUpdateRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanBandwidthUtilized;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanLinkUsages;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanPolicyBeanWithTimeStamp;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplications;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoCPEInformationBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.SdwanCiscoCpeAllDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.AssosciatedSiteDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListConfigBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteServiceDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.SiteListConfigDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyDetailsRequestBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyStatusResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters.PerformanceRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters.PerformanceResponse;
import com.tcl.dias.serviceinventory.listener.ServiceInventoryHandoverConsumer;

import com.tcl.dias.serviceinventory.service.v1.IzoSdwanCiscoDetailedInfoService;
import com.tcl.dias.serviceinventory.service.v1.IzoSdwanCiscoInventoryService;

import com.tcl.dias.serviceinventory.renewals.RenewalsQuoteDetail;
import com.tcl.dias.serviceinventory.service.v1.IpcServiceInventoryService;
import com.tcl.dias.serviceinventory.service.v1.IzoSdwanInventoryService;
import com.tcl.dias.serviceinventory.service.v1.RenewalsServiceInventoryService;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryService;
import com.tcl.dias.serviceinventory.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;


/**
 * Controller class for service inventory related API
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/si")
public class ServiceInventoryController {

	@Autowired
	ServiceInventoryService serviceInventoryService;

	@Autowired
	ServiceInventoryHandoverConsumer serviceInventoryHandoverConsumer;
	
	@Autowired
	IzoSdwanInventoryService izoSdwanInventoryService;
	
	@Autowired
	IzoSdwanCiscoInventoryService izoSdwanInventoryServiceCisco;
	
	@Autowired
	IzoSdwanCiscoDetailedInfoService izoSdwanCiscoDetailedInfoService;

	@Autowired
	IpcServiceInventoryService ipcInventoryService;
	
	@Autowired
	RenewalsServiceInventoryService renewalsServiceInventoryService;
	
	/**
	 * Method to get order dataS
	 *
	 * @return {@link List<SIOrderBean>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.SI_ORDER_DETAILS)
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SIOrderDataBean> getOrderDetails(
			@PathVariable(value = "orderId") String orderId,
			@RequestParam("assetType") String assetType,
			@RequestParam("relType") String relType) {
		SIGetOrderRequest request = new SIGetOrderRequest();
		request.setOrderId(orderId);
		request.setAssets(Boolean.TRUE);
		request.setAssetTypes(ImmutableList.of(assetType));
		request.setAssetRelationTypes(ImmutableList.of(relType));
		return serviceInventoryService.getOrderData(request)
				.map(orderDataBean -> new ResponseResource<SIOrderDataBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						orderDataBean,
						Status.SUCCESS))
				.get();
	}

	/**
	 * Method to retrieve SI order details based on erf customer id
	 *
	 * @param erfCustId
	 * @return {@link List<SIOrderBean>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.SI_ORDER_DETAILS)
	@RequestMapping(value = "/orderdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SIOrderBean>> getOrderDetails(
			@RequestParam(required = true, value = "erfCustId") Integer erfCustId) throws TclCommonException {
		List<SIOrderBean> response = serviceInventoryService.getOrderDetails(erfCustId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to retrieve SI service details based on service id
	 *
	 * @param serviceId
	 * @return {@link SIServiceDetailBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.SI_ORDER_DETAILS)
	@RequestMapping(value = "/servicedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SIServiceDetailBean> getServiceDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId,
			@RequestParam(required = false, value = "number") String number,
			@RequestParam(required = false, value = "outpulse") String outpulse,
			@RequestParam(required = false, value = "product") String product) throws TclCommonException {
		SIServiceDetailBean response = serviceInventoryService.getServiceDetails(serviceId, product, number, outpulse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to retrieve SI service details based on service id
	 *
	 * @param serviceId
	 * @return {@link SIServiceDetailBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.SI_ORDER_DETAILS)
	@RequestMapping(value = "/servicedetails/npl", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SIServiceDetailBean>> getServiceDetailsForNPL(
			@RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		List<SIServiceDetailBean> response = serviceInventoryService.getServiceDetailsForNPL(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Get statistics for specified or all product families
	 *
	 * @param productFamily
	 * @return {@link List<SIProductFamilySummaryBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.PRODUCT_FAMILY_STATS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIProductFamilySummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/stats")
	public ResponseResource<List<SIProductFamilySummaryBean>> getProductSummaryStats(
			@RequestParam(value = "productFamily", required = false) String productFamily,
			@RequestParam(value="customerId") Integer customerId) {
		return serviceInventoryService.getProductFamilyStats(customerId, productFamily).map(ResponseResource::new).get();
	}

	/**
	 * Get all configurations based on given access type
	 *
	 * @param accessType
	 * @param number
	 * @return {@link SIConfigurationCountryBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.ACCESS_TYPE_CONFIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIConfigurationCountryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/configurations/search/numbers")
	public ResponseResource<List<SIConfigurationCountryBean>> getAccessTypeBasedConfigurations(
			@RequestBody(required = false) Object any, // Added this to skip aspect logic
			@RequestParam(value = "customerId") Integer customerId,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "accessType", required = false) String accessType,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "orderId", required = false) String orderId) {
		return serviceInventoryService.getAccessTypeBasedConfigurations(customerId, productName, accessType, number, orderId)
				.map(ResponseResource::new).get();
	}

	/**
	 * Get Numbers based on configurations
	 *
	 * @param request
	 * @param productName
	 * @return {@link List<SINumberConfigurationBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/products/{productName}/configurations/numbers")
	public ResponseResource<List<SINumberConfigurationBean>> getNumbersFromConfigurations(@PathVariable String productName, @RequestBody GscServiceInventoryConfigurationRequestBean request) {
		return serviceInventoryService.getNumbersFromConfigurations(productName, request).map(ResponseResource::new).get();
	}

	/**
	 * Get configurations by given product name and order id
	 *
	 * @param orderId
	 * @param productName
	 * @return {@link SIConfigurationCountryBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/orders/{orderId}/configurations")
	public ResponseResource<List<SIConfigurationCountryBean>> getConfigurationsByProductAndOrder(@PathVariable String orderId,
																								 @RequestParam(value="customerId") Integer customerId,
																					@RequestParam String productName) {
		return serviceInventoryService.getConfigurationsByProductAndOrder(customerId, orderId, productName)
				.map(ResponseResource::new).get();
	}

	/**
	 * Get configurations by given product name
	 *
	 * @param productName
	 * @return {@link SIConfigurationCountryBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/{productName}/configurations")
	public ResponseResource<List<SIConfigurationCountryBean>> getConfigurationsByProduct(@PathVariable String productName,
																						 @RequestParam(value="customerId") Integer customerId) {
		return serviceInventoryService.getConfigurationsByProduct(customerId, productName)
				.map(ResponseResource::new).get();
	}
	
	/**
	 * Get Service details based on login user and product
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/{productId}/servicedetails")
	public ResponseResource<SIServiceInformationBean> getServiceDetailsByProduct(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,
			@RequestParam(value="partnerId",required = false) Integer partnerId,
			@RequestParam(value="customerLeId",required = false) Integer customerLeId,
			@RequestParam(value="vrfFlag",required = false) String vrfFlag,
			@RequestParam(value="isTermination", required=false) Boolean isTermination)
			 throws TclCommonException {
		SIServiceInformationBean response = serviceInventoryService.getAllServiceDetailsByProduct(productId,page,size,customerId,partnerId,customerLeId,vrfFlag,isTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Get Service details based on login user and product
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/{productId}/servicedetails/filter")
	public ResponseResource<SIServiceInformationBean> getServiceDetailsByProductFilter(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,
			@RequestParam(value="customerId",required = false) Integer customerId,
			@RequestParam(value="partnerId",required = false) Integer partnerId,
			@RequestParam(value="customerLeId",required = false) Integer customerLeId,
			@RequestParam(value="ndeFlag",required = false) boolean ndeFlag) throws TclCommonException {
		SIServiceInformationBean response = serviceInventoryService.getAllServiceDetailsByProductWithNdeFilter(productId, page, size, customerId, partnerId, customerLeId,ndeFlag);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Get Service details based on productsegment , login user and product
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_CLOUD_SERVICE_DETAILS)
    @ApiResponses(value = {
                  @ApiResponse(code = 200, message = Constants.SUCCESS, response = CloudServiceInformationBean.class),
                  @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                  @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                  @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/products/productsegment/{productId}/servicedetails/search")
    public ResponseResource<CloudServiceInformationBean> getServiceDetailsByProductSegmentsProduct(
                  @PathVariable("productId") Integer productId, @RequestParam("page") Integer page, @RequestParam("size") Integer size, 
                  @RequestParam(value="city", required=false) String city, @RequestParam(value="cloudType", required=false) String cloudType,  
                  @RequestParam(value="businessUnit", required=false) String businessUnit, @RequestParam(value="zone", required=false) String zone,
                  @RequestParam(value="opptyClassification", required=false) String opptyClassification, @RequestParam(value="partnerLeName", required=false) String partnerLeName, 
                  @RequestParam(value="searchText", required=false) String searchText) throws TclCommonException {
           CloudServiceInformationBean response = serviceInventoryService
                        .getServiceDetailsByProductSegmentsProduct(productId, page, size, city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, searchText);
           return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                        Status.SUCCESS);
    }
	
	
	/**
	 * Get Service Info
	 * @param productId and serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_PRODUCT_SI_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/productsegment/{productId}/{serviceId}/info/search")
	public ResponseResource<IPCInformationBean> getServiceInfoDetailByProductSegmentsProduct(@PathVariable("productId") Integer productId,@PathVariable("serviceId") String serviceId,@RequestParam(value="assetId",required=false) String assetId) throws TclCommonException {
		IPCInformationBean response = serviceInventoryService.getServiceInfoDetailByProductSegmentsProduct(productId,serviceId,assetId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/servicedetails/products")
	public ResponseResource<SIServiceInformationBean> getServiceDetails() throws TclCommonException {
		SIServiceInformationBean response = serviceInventoryService.getAllServiceDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/servicedetails")
	public ResponseResource<List<ProductInformationBean>> getServiceDetailsProductwise(@RequestParam(value="customerId",required = false) Integer customerId, @RequestParam(value="partnerId",required = false) Integer partnerId) throws TclCommonException {
		List<ProductInformationBean> response = serviceInventoryService.getAllProductServiceInformationCount(customerId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/delete/serviceInvRf/{id}")
	public ResponseResource<String> deleteRfInventory(@PathVariable("productId") Integer id) throws TclCommonException {
		serviceInventoryService.deleteRfInventory(id);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/suips")
	public ResponseResource<List<RfDumpWirelessOneBean>> enrichRfDetailList(@RequestBody(required = false) SuipListBean suipListBean,
																			@RequestParam(required = false) String provider) {
		List<RfDumpWirelessOneBean> response= serviceInventoryService.enrichRfDetailList(suipListBean, provider);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
				, Status.SUCCESS);
	}



	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/servicedetails/alias")
	public ResponseResource<String> updateAliasName(@RequestBody ServiceDetailBean serviceDetailBean) throws TclCommonException {
		String response = serviceInventoryService.updateAliasNameForService(serviceDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * This method is used to get
	 *  customer service details for dashboard in customer portal
	 *
	 * @return List<CustomerOrderDetailsBean>
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.PRODUCT_FAMILY_STATS)
    @RequestMapping(value = "/serviceInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<List<CustomerOrderDetailBean>> getCustomerServices(@RequestParam(value="customerId") Integer customerId) throws TclCommonException {
        List<CustomerOrderDetailBean> customerServiceDto=serviceInventoryService.getCustomerServices(customerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,customerServiceDto
                , Status.SUCCESS);

    }
	/**
	 * This method is used to get
	 *  customer service details for dashboard in customer portal
	 *
	 * @return List<CustomerOrderDetailsBean>
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.PRODUCT_FAMILY_STATS)
    @RequestMapping(value = "/trackOrderCount", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Map<String,Long>> getTrackOrderCount(@RequestParam(value="customerId") Integer customerId) throws TclCommonException {
		Map<String,Long> orderCount=serviceInventoryService.getTrackOrderCount(customerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,orderCount
                , Status.SUCCESS);

    }
	
	/**
     * Get Service details based on login user and product and listOfServiceIds
     * @param productId
     * @return
     * @throws TclCommonException
     */
     @ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
     @ApiResponses(value = {
                   @ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @RequestMapping(value = "/products/serviceId/info")
     public ResponseResource<SIServiceInformationBean> getServiceDetailsByProductAndServiceIds(@RequestBody ServiceCatalogRequest serviceCatalogRequest) throws TclCommonException {
            SIServiceInformationBean response = serviceInventoryService.getAllServiceDetailsByProductAndServiceIds(serviceCatalogRequest);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
     
     @ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
     @ApiResponses(value = {
                   @ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @GetMapping(value = "/products/{productId}/servicedetails/search")
     public ResponseResource<PagedResult<List<SIServiceInformationBean>>> getServiceDetailsByProduct(
                   @PathVariable("productId") Integer productId, @RequestParam("page") Integer page,
                   @RequestParam("size") Integer size, @RequestParam(value="city",required=false) String city, @RequestParam(value="alias",required=false) String alias,
                   @RequestParam(value="searchText",required=false) String searchText,@RequestParam(value="customerId",required = false)Integer customerId,@RequestParam(value="partnerId",required = false)Integer partnerId,@RequestParam(value="customerLeId",required = false)Integer customerLeId,
				   @RequestParam(value="opportunityMode",required=false) String opportunityMode,
				   @RequestParam(value="ndeFlag",required=false) Integer ndeFlag,
				   @RequestParam(value="vrfFlag",required=false) String vrfFlag) throws TclCommonException {
            PagedResult<List<SIServiceInformationBean>> response = serviceInventoryService
                         .getServiceDetailsWithPaginationAndSearch(productId, page, size, city, alias, searchText,customerId,partnerId,customerLeId, opportunityMode,ndeFlag,vrfFlag);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }


	/**
	 * Get service data for specified access type and GSC product
	 * @param accessType
	 * @param productName
	 * @param size
	 * @param page
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/servicedata")
	public ResponseResource<Page<Map<String, Object>>> getServiceData(
			@RequestParam("accessType") String accessType,
			@RequestParam("productName") String productName,
			@RequestParam("size") Integer size,
			@RequestParam("page") Integer page) {
		return Try.of(() -> serviceInventoryService.getServiceDataByAccessTypeAndProduct(page, size, accessType, productName))
				.map(ResponseResource::new)
				.get();
	}
	/**
	 * Method to retrieve ip related attribute details based on service id
	 * for customer portal
	 *
	 * @param serviceId
	 * @return {@link List<Map<String,Object>>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_IP_ATTRIBUTE_DETAILS)
	@RequestMapping(value = "/ipAttribute", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String,Object>>> getServiceDetailsIpAttribute(
			@RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		List<Map<String,Object>> response = serviceInventoryService.getServiceDetailsIpAttribute(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceDetail.GET_SOURCE_COUNTRY)
	@RequestMapping(value = "/sourceCountry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> getSourceCountryForVpn(
			@RequestParam(required = true, value = "vpnId") String vpnId) throws TclCommonException {
		Boolean response = serviceInventoryService.getSourceCountryForVpn(vpnId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id for ILL services
	 *
	 * @param serviceId
	 * @return 
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_DETAILS)
	@RequestMapping(value = "/ias/detailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SIDetailedInfoResponse> getILLServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		SIDetailedInfoResponse response = serviceInventoryService.getDeatiledSIInfoForILLService(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id for GVPN services
	 *
	 * @param serviceId
	 * @return 
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_DETAILS)
	@RequestMapping(value = "/gvpn/detailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SIDetailedInfoResponse> getGvpnServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		SIDetailedInfoResponse response = serviceInventoryService.getDeatiledSIInfoForGvpnService(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	

	/**
	 * Get excel for specified or all product families
	 *
	 * @param productFamily
	 * @return 
	 * @return 
	 * @return {@link List<SIProductFamilySummaryBean>}
	 * @throws Exception 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.PRODUCT_FAMILY_STATS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIProductFamilySummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getExcel")
	public ResponseEntity<HttpServletResponse> getProductFamilyDetails(
			@RequestParam(value = "productFamily", required = false) String productFamily,HttpServletResponse response) throws Exception {
		List<String> productList=Arrays.asList(productFamily.split(","));
		response = serviceInventoryService.constructInventoryExcel(productList,response);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(response);
		
		
	}

	/**
	 * 
	 * @param productFamilyRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.PRODUCT_FAMILY_STATS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/getExcelDownload")
	public ResponseResource<HttpServletResponse> getProductFamilyDetailsForDownload(
			@RequestBody ProductFamilyRequest productFamilyRequest, HttpServletResponse response) throws Exception {
		response = serviceInventoryService.constructInventoryExcelDownload(productFamilyRequest, response);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to retrieve the detailed information for the provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/{productName}/serviceDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SIServiceDetailedResponse> getServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId,@PathVariable(value = "productName") String productName,
			@RequestParam(required=false, value ="isTermination") Boolean isTermination) throws TclCommonException {
		SIServiceDetailedResponse response = serviceInventoryService.getDetailedSIInfo(serviceId, productName, isTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/npl/serviceDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NPLSIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NPLSIServiceDetailedResponse> getNPLServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId,@RequestParam(required=false, value ="isTermination") Boolean isTermination) throws TclCommonException {
		NPLSIServiceDetailedResponse response = serviceInventoryService.getNPLDetailedSIInfo(serviceId, isTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author chetchau
	 * @param orderId
	 * @param updateTollFreeServiceBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_FILTERS_DETAILS)
	@PostMapping(value = "/numbers/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIDetailedInfoResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public RestResponse getTollfreeServiceInventoryDetails(
			@PathVariable("orderId") String orderId, @RequestBody UpdateTollFreeServiceBean updateTollFreeServiceBean) throws TclCommonException{
		return serviceInventoryService.updateTollFreeServiceDetails(orderId,updateTollFreeServiceBean);
	}
	
	/**
	 * @author chetchau
	 * @param serviceId
	 * @param updateDataServiceBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_TOLLFREE_DETAILS)
	@PostMapping(value = "/circuits/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public RestResponse getDataServiceInventoryDetails(
			@PathVariable("serviceId") String serviceId, @RequestBody UpdateDataServiceBean updateDataServiceBean) throws TclCommonException{
		return serviceInventoryService.updateDataServiceDetails(serviceId,updateDataServiceBean);
	}
	
	/**
	 * @author chetchau
	 * @param DIDServiceId
	 * @param updateDIDServiceBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_DIDSERVICE_DETAILS)
	@PostMapping(value = "/DIDNumbers/{DIDServiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public RestResponse getDidServiceInventoryDetails(
			@PathVariable("DIDServiceId") String DIDServiceId, @RequestBody UpdateDIDServiceBean updateDIDServiceBean) throws TclCommonException{
		return serviceInventoryService.updateDidServiceDetails(DIDServiceId, updateDIDServiceBean);
	}
	
	
	/**
	 * @param ServiceId
	 * @param showCosMessage 
	 * updateShowCosMessage
	 * @return String
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.UPDATE_COS_MESSAGE_PREFERENCES)
	@PostMapping(value = "/showcospreference", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateShowCosMessage(@RequestBody ServiceDetailBean serviceBean) throws TclCommonException{
		String response = serviceInventoryService.updateShowCosMessage(serviceBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * Method to retrieve the list of detailed information for the provided service id
	 *
	 * @param serviceIds
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/{productName}/serviceDetailedInfoList", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SIServiceDetailedResponse>> getServiceInventoryDetailedListInfo(@RequestBody ServiceIdListBean serviceIds, @PathVariable(value = "productName") String productName) throws TclCommonException {
		List<SIServiceDetailedResponse> response = serviceInventoryService.getServiceDetailedInfoList(serviceIds.getServiceIdList(),productName, serviceIds.getIsTermination());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the list of detailed information for the provided service id
	 *
	 * @param serviceIds
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/triggersi/{ordercode}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerServiceInv(@PathVariable(value = "ordercode") String var,@RequestBody String data) throws TclCommonException {
		ScOrderBean request = (ScOrderBean) Utils.convertJsonToObject(data, ScOrderBean.class);
		serviceInventoryService.processServiceInventoryData(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "",
				Status.SUCCESS);
	}

	@RequestMapping(value = "/satsoc/details/{serviceCode}", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String,Object>>> getSatSocDetails(@PathVariable(value = "serviceCode") String serviceCode, @RequestParam(required = false, name = "src") String src) throws TclCommonException {
		List<Map<String,Object>> response =  serviceInventoryService.processServiceInventorySatSocData(serviceCode,src);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/attachments/{serviceCode}", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SiAttachmentBean>> getAttachments(@PathVariable(value = "serviceCode") String serviceCode) throws TclCommonException {
		List<SiAttachmentBean> response =  serviceInventoryService.getAllAttachments(serviceCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/slas/{serviceCode}", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String,Object>>> getServiceSlas(@PathVariable(value = "serviceCode") String serviceCode) throws TclCommonException {
		List<Map<String, Object>> response =  serviceInventoryService.getServiceSlas(serviceCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * Get Service details based on login user and GDE
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeSIServiceInformationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gde/{productId}/servicedetails")
	public ResponseResource<GdeSIServiceInformationBean> getGdeServiceDetailsByProduct(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,@RequestParam(value="partnerId",required = false) Integer partnerId,@RequestParam(value="customerLeId",required = false) Integer customerLeId) throws TclCommonException {
		GdeSIServiceInformationBean response = serviceInventoryService.getGdeServiceDetails(productId,page,size,customerId,partnerId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/gde/serviceDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeSIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<GdeSIServiceDetailedResponse> getGDEServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		GdeSIServiceDetailedResponse response = serviceInventoryService.getGDEDetailedSIInfo(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * Get Service details , login user and product
	 * @param productId
	 * @param page
	 * @param size
	 * @param siteAcity
	 * @param siteBcity
	 * @param alias
	 * @param searchText
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
    @ApiResponses(value = {
                  @ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
                  @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                  @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                  @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/gde/{productId}/servicedetails/search")
    public ResponseResource<PagedResult<List<SIServiceInformationBean>>> getGDEServiceDetailsByProduct(
                  @PathVariable("productId") Integer productId, @RequestParam("page") Integer page,
                  @RequestParam("size") Integer size, @RequestParam(value="siteAcity",required=false) String siteAcity,@RequestParam(value="siteBcity",required=false) String siteBcity, @RequestParam(value="alias",required=false) String alias,
                  @RequestParam(value="searchText",required=false) String searchText,@RequestParam(value="customerId",required = false)Integer customerId,@RequestParam(value="partnerId",required = false)Integer partnerId,@RequestParam(value="customerLeId",required = false)Integer customerLeId) throws TclCommonException {
           PagedResult<List<SIServiceInformationBean>> response = serviceInventoryService
                        .getGDEServiceDetailsWithPaginationAndSearch(productId, page, size, siteAcity,siteBcity, alias, searchText,customerId,partnerId,customerLeId);
           return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                        Status.SUCCESS);
    }

	@PostMapping("/rfinventory/{id}")
	public ResponseResource deleteRfInventory(@PathVariable("id") int id) throws TclCommonException {
		serviceInventoryService.deleteRfInventory(id);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"SUCCESS"
				, Status.SUCCESS);
	}



	 /**
     * use to download attachment based in attachment id
     *
     * @param attachmentId
     * @return
     * @throws TclCommonException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.DOWNLOAD_ATTACHMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value ="/{attachment_id}")
    public ResponseResource downloadAttachment(@PathVariable("attachment_id")Integer attachmentId, HttpServletResponse httpServletResponse) throws TclCommonException {
        String response = serviceInventoryService.downloadAttachment(attachmentId,httpServletResponse);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

	/**
	 * Get Domestic Voice Site Details
	 *
	 * @param siOrderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.UPDATE_COS_MESSAGE_PREFERENCES)
	@GetMapping(value = "/gsc/did/sites", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getGscDomesticVoiceSites(@RequestParam String siOrderId) throws TclCommonException{
		List<String> response = serviceInventoryService.getGscDomesticVoiceSites(siOrderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to update alias for GDE service id
	 * @param serviceDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gde/servicedetails/alias")
	public ResponseResource<String> updateAliasNameForGDECircuit(@RequestBody ServiceDetailBean serviceDetailBean) throws TclCommonException {
		String response = serviceInventoryService.updateAliasNameForGdeCircuit(serviceDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get GVPN service details with parent product gsc
	 *
	 * @param customerId
	 * @param page
	 * @param size
	 * @return {@link SIServiceInformationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceInformationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/servicedetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SIServiceInformationBean> getServiceDetailsOfGsc(@RequestParam(value = "customerId") Integer customerId,
																			 @RequestParam("page") Integer page,
																			 @RequestParam("size") Integer size) throws TclCommonException {
		SIServiceInformationBean response = serviceInventoryService.getServiceDetailsOfGsc(customerId, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	/**
	 * Get GSC Interconnect details
	 *
	 * @param customerId
	 * @param orgId
	 * @param page
	 * @param size
	 * @return {@link SIServiceInformationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceInformationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/interconnectDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SIServiceInformationBean> getGscInterconnectDetails(
			@RequestParam(value = "customerId") Integer customerId,
			@RequestParam(value = "orgId") Integer orgId,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws TclCommonException {
		SIServiceInformationBean gscWholesaleInterconnectDetails = serviceInventoryService
				.getGscInterconnectDetails(orgId,page,size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscWholesaleInterconnectDetails, Status.SUCCESS);
	}

	/**
	 * API to get service details of gsc by search
	 *
	 * @param page
	 * @param size
	 * @param customerId
	 * @return {@link PagedResult<List<SIServiceInformationBean>>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceInformationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/servicedetails/search")
	public ResponseResource<PagedResult<List<SIServiceInformationBean>>> getServiceDetailsByProduct(
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam(value = "circuitID", required = false) String circuitID,
			@RequestParam(value = "ipAddress", required = false) String ipAddress,
			@RequestParam(value = "sipTrunkGroup", required = false) String sipTrunkGroup,
			@RequestParam(value = "tclSwitch", required = false) String tclSwitch,
			@RequestParam(value = "customerId") Integer customerId) throws TclCommonException {
		PagedResult<List<SIServiceInformationBean>> response = serviceInventoryService.getGscServiceDetailsWithPaginationAndSearch(page, size, circuitID, ipAddress, sipTrunkGroup, tclSwitch, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Get Sites based on configurations
	 *
	 * @param accessType
	 * @param productName
	 * @param customerLeId
	 * @return {@link List<SISiteConfigurationBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_SITES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SISiteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/configurations/sites")
	public ResponseResource<List<SISiteConfigurationBean>> getSitesFromConfigurations(@RequestParam(value = "accessType") String accessType,
																					  @RequestParam(value = "customerLeId") Integer customerLeId,
																					  @RequestParam(value = "productName") String productName) {
		List<SISiteConfigurationBean> response = serviceInventoryService.getSitesFromConfigurations(accessType, customerLeId, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Get Numbers list pdf per site
	 *
	 * @param response
	 * @param accessType
	 * @param customerLeId
	 * @param productName
	 * @param siteAddress
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.NUMBER_LIST_BY_SITE_AS_PDF)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/site/numbers")
	public ResponseResource<String> getNumbersListBySiteAddress(HttpServletResponse response,
																@RequestParam(value = "accessType") String accessType,
																@RequestParam(value = "customerLeId") Integer customerLeId,
																@RequestParam(value = "productName") String productName,
																@RequestParam(value = "siteAddress") String siteAddress) throws DocumentException, IOException, TclCommonException {
		serviceInventoryService.getNumbersListBySiteAddressAsPdf(response, accessType, customerLeId, productName, siteAddress);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * API to get site configurations based on search params
	 *
	 * @param customerLeId
	 * @param accessType
	 * @param number
	 * @param productName
	 * @param customerId
	 * @return {@link List<SIConfigurationCountryBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_SITES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIConfigurationCountryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/configurations/search")
	public ResponseResource<List<SIConfigurationCountryBean>> getSiteConfigurationsBasedOnSearch(
			@RequestParam(value="customerLeId", required = false) String customerLeId,
			@RequestParam(value = "accessType", required = false) String accessType,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value="customerId") Integer customerId) {
		List<SIConfigurationCountryBean> response = serviceInventoryService.getSiteConfigurationsBasedOnSearch(customerLeId, productName, accessType, number, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/nde/serviceDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NPLSIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NPLSIServiceDetailedResponse> getNDEServiceInventoryDetails(
			@RequestParam(required = true, value = "serviceId") String serviceId, @RequestParam(required=false, value ="isTermination") Boolean isTermination) throws TclCommonException {
		NPLSIServiceDetailedResponse response = serviceInventoryService.getNDEDetailedSIInfo(serviceId, isTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the List of provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/nde/mc/serviceDetaillist", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<NPLSIServiceDetailedResponse>> getNDEMcDetailedSIInfoList(@RequestBody ServiceIdListBean serviceIds) throws TclCommonException {
		List<NPLSIServiceDetailedResponse> response = serviceInventoryService.getNDEMcDetailedSIInfoList(serviceIds.getServiceIdList(), serviceIds.getIsTermination());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * Get Product Flavor details based on login user and product
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_PRODUCT_FLAVOUR_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanSiteDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/productFlavourdetails")
	public ResponseResource<Set<String>> getProductFlavorDetails(@PathVariable("productId") Integer productId,@RequestParam(value="customerId",required = false) Integer customerId,@RequestParam(value="partnerId",required = false) Integer partnerId,@RequestParam(value="customerLeId",required = false) Integer customerLeId) throws TclCommonException {
		Set<String> response = izoSdwanInventoryServiceCisco.getproductFlavourDetails(productId,customerId, partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * Get SDWAN Service details based on login user and product
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SITE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanSiteDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/sitedetails")
	public ResponseResource<SdwanSiteDetailsBean> getSdwanSiteDetails(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,@RequestParam(value="partnerId",required = false) Integer partnerId,@RequestParam(value="customerLeId",required = false) Integer customerLeId) throws TclCommonException {
		SdwanSiteDetailsBean response = izoSdwanInventoryService.getSdwanAndNetworkSiteDetails(productId, page, size,customerId, partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Get SDWAN Service details for cisco based on login user and product
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SITE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanSiteDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/sitedetailsforcisco")
	public ResponseResource<SdwanSiteDetailsBean> getSdwanSiteDetailsForCisco(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,@RequestParam(value="partnerId",required = false) Integer partnerId,@RequestParam(value="customerLeId",required = false) Integer customerLeId) throws TclCommonException {
		SdwanSiteDetailsBean response = izoSdwanInventoryServiceCisco.getSdwanAndNetworkSiteDetailsCisco(productId, page, size,customerId, partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * Get Service details based on filters
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SITE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/site/filter")
	public ResponseResource<PagedResult<SdwanSiteDetailsBean>> getSdwanSiteDetailsBasedOnFilters(@RequestParam(value = "status", required = false) Boolean status, @RequestBody SiteDetailsSearchRequest siteDetailsSearchRequest) throws TclCommonException, IOException {
		PagedResult<SdwanSiteDetailsBean> filterData;
		if(Objects.isNull(status) || status==false)
		filterData =  izoSdwanInventoryService.getSdwanDetailsBasedOnFilters(siteDetailsSearchRequest);
		else filterData =  izoSdwanInventoryService.getSdwanDetailsBasedOnFiltersWithStatus(siteDetailsSearchRequest, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	/**
	 * Get Service details based on filters for Cisco
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SITE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/cisco/site/filter")
	public ResponseResource<PagedResult<SdwanSiteDetailsBean>> getSdwanSiteDetailsBasedOnFiltersForCisco(
			@RequestParam(value = "status", required = false) Boolean status,
			@RequestBody SiteDetailsSearchRequest siteDetailsSearchRequest) throws TclCommonException, IOException {
		PagedResult<SdwanSiteDetailsBean> filterData;
		if (Objects.isNull(status) || status == false)
			filterData = izoSdwanInventoryServiceCisco.getSdwanDetailsBasedOnFilters(siteDetailsSearchRequest);
		else
			filterData = izoSdwanInventoryServiceCisco.getSdwanDetailsBasedOnFiltersWithStatus(siteDetailsSearchRequest,status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	


	/**
	 * To fetch SDWAN Site CPE details tagged to a customer/partner
	 *
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/cpedetails")
	public ResponseResource<SdwanCPEInformationBean> getCPEDetails(@PathVariable("productId") Integer productId,
			@RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId)
			throws TclCommonException, IOException {
		SdwanCPEInformationBean response = izoSdwanInventoryService.getCPEDetails(productId, page, size, customerId,
				partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SERVICE_DETAILS)
	@RequestMapping(value = "/sdwan/{productId}/siteDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanServiceDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SdwanServiceDetailBean> getSdwanServiceInventoryDetails(@PathVariable("productId") Integer productId, @RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		SdwanServiceDetailBean response = izoSdwanInventoryService.getSdwanServiceDetailInfo(serviceId, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to retrieve the detailed information for the provided service id for Cisco
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SERVICE_DETAILS)
	@RequestMapping(value = "/sdwan/cisco/{productId}/siteDetailedInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanServiceDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CiscoSiteServiceDetailBean> getSdwanServiceInventoryDetailsCisco(@PathVariable("productId") Integer productId, @RequestParam(required = true, value = "serviceId") String serviceId) throws TclCommonException {
		CiscoSiteServiceDetailBean response = izoSdwanCiscoDetailedInfoService.getSdwanServiceDetailInfo(serviceId, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get SDWAN CPE details based on filters
	 * 
	 * @param searchText
	 * @param size
	 * @param page
	 * @param sortBy
	 * @param sortOrder
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cpe/filter")
	public ResponseResource<SdwanCPEInformationBean> getSdwanCpeDetailsBasedOnFilters(
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "productId", required = true) Integer productId,
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "partnerId", required = false) Integer partnerId)
			throws TclCommonException, IOException {
		SdwanCPEInformationBean filterData = izoSdwanInventoryService.getCpeDetailsBasedOnFilters(searchText, size, page,
				sortBy, sortOrder, productId, customerId, customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/cpeDetails/filter")
	public ResponseResource<CiscoCPEInformationBean> getCiscoSdwanCpeDetailsBasedOnFilters(
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "productId", required = true) Integer productId,
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "partnerId", required = false) Integer partnerId)
			throws TclCommonException, IOException {
		CiscoCPEInformationBean filterData = izoSdwanInventoryServiceCisco.getCpeDetailsBasedOnFilters(searchText, size, page,
				sortBy, sortOrder, productId, customerId, customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}

	/**
	 * Get SDWAN CPE full details based on asset ID
	 *
	 * @param assetId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_FULL_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanCpeAllDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cpedetails")
	public ResponseResource<SdwanCpeAllDetailBean> getSdwanCpeDetailsBasedOnName(
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "assetName") String assetName, @RequestParam(name = "productId") Integer productId)
			throws TclCommonException, IOException {
		SdwanCpeAllDetailBean filterData = izoSdwanInventoryService.getCpeDetailsBasedOnAssetName(customerId,
				customerLeId, partnerId, assetName, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	/**
	 * Get SDWAN CPE full details based on asset ID for cisco
	 *
	 * @param assetId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_FULL_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanCpeAllDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/cpedetails")
	public ResponseResource<SdwanCiscoCpeAllDetailBean> getCiscoSdwanCpeDetailsBasedOnName(
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "assetName") String assetName, @RequestParam(name = "productId") Integer productId)
			throws TclCommonException, IOException {
		SdwanCiscoCpeAllDetailBean filterData = izoSdwanCiscoDetailedInfoService.getCpeDetailsBasedOnAssetName(customerId,
				customerLeId, partnerId, assetName, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	
	/**
	 * To fetch SDWAN Site CPE details tagged to a customer/partner for cisco
	 *
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/{productId}/cpedetails")
	public ResponseResource<CiscoCPEInformationBean> getCiscoCPEDetails(@PathVariable("productId") Integer productId,
			@RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId)
			throws TclCommonException, IOException {
		CiscoCPEInformationBean response = izoSdwanInventoryServiceCisco.getCPEDetails(productId, page, size, customerId,
				partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get SDWAN template full details based on template name
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param templateName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_TEMPLATE_FULL_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/templateDetailedInfo")
	public ResponseResource<SdwanTemplateDetailBean> getSdwanTemplateDetailedInfo(
			@PathVariable("productId") Integer productId,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId,
			@RequestParam(name = "templateName") String templateName) throws TclCommonException, IOException {
		SdwanTemplateDetailBean filterData = izoSdwanInventoryService.getSdwanTemplateDetailedInfo(customerId,
				customerLeId, partnerId, templateName, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_SITELIST_CONFIG_VIEW)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteListConfigDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/{productId}/siteListConfigDetailedInfo")
	public ResponseResource<CiscoSiteListDetailBean> getCiscoSiteListConfigDetailedInfo(
			@PathVariable("productId") Integer productId,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId,
			@RequestParam(name = "siteListId") String siteListId,
			HttpServletRequest request) throws TclCommonException, IOException {
		CiscoSiteListDetailBean siteListConfigDetails = izoSdwanCiscoDetailedInfoService.getSiteListConfigDetailedInfo(customerId,
				customerLeId, partnerId, siteListId, productId,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteListConfigDetails,
				Status.SUCCESS);
	}

	/**
	 * Get list of SDWAN template based on customer ID
	 *
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_TEMPLATE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/templatedetails")
	public ResponseResource<PagedResultWithTimestamp<TemplateDetails>> getSdwanTemplateList(
			@PathVariable("productId") Integer productId, @RequestParam(required = true, name = "page") Integer page,
			@RequestParam(required = true, name = "size") Integer size,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId)
			throws TclCommonException, IOException {
		PagedResultWithTimestamp<TemplateDetails> filterData = izoSdwanInventoryService.getSdwanTemplateDetails(page, size,
				customerId, customerLeId, partnerId, productId);
		return new ResponseResource<com.tcl.dias.serviceinventory.beans.PagedResultWithTimestamp<TemplateDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	/**
	 * Getting SiteListConfigdetails for Cisco
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_TEMPLATE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/SiteListConfigdetails")
	public ResponseResource<PagedResultWithTimestamp<SiteListConfigDetails>> getSiteListConfig(
			@PathVariable("productId") Integer productId, @RequestParam(required = true, name = "page") Integer page,
			@RequestParam(required = true, name = "size") Integer size,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId)
			throws TclCommonException, IOException {
		PagedResultWithTimestamp<SiteListConfigDetails> filterData = izoSdwanInventoryServiceCisco.getSdwanTemplateDetails(page, size,
				customerId, customerLeId, partnerId, productId);
		return new ResponseResource<com.tcl.dias.serviceinventory.beans.PagedResultWithTimestamp<SiteListConfigDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	/**
	 * Get Service details based on filters
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_TEMPLATE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TemplateDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/template/filter")
	public ResponseResource<PagedResultWithTimestamp<TemplateDetails>> filterBasedOnTemplateName(@RequestBody SiteDetailsSearchRequest siteDetailsSearchRequest) throws TclCommonException {
		PagedResultWithTimestamp<TemplateDetails> filterData =  izoSdwanInventoryService.searchBasedOnTemplateNames(siteDetailsSearchRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	/**
	 * Filtering SiteListConfigurationDetails For Cisco Based on Alias
	 * @param siteDetailsSearchRequest
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANTEMPLATE.GET_TEMPLATE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TemplateDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/siteListConfig/filter")
	public ResponseResource<PagedResultWithTimestamp<SiteListConfigDetails>> filterBasedOnTSiteListName(@RequestBody SiteDetailsSearchRequest siteDetailsSearchRequest) throws TclCommonException {
		PagedResultWithTimestamp<SiteListConfigDetails> filterData =  izoSdwanInventoryServiceCisco.searchBasedOnTemplateNames(siteDetailsSearchRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	
	/**
	 * Get Service details based on filters
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLICATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanApplications.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/applications")
	public ResponseResource<SdwanApplications> sdwanApplicationList(@PathVariable("productId") Integer productId, 
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		SdwanApplications sdwanApplications = izoSdwanInventoryService.getSdwanAppplications(productId, customerId, customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanApplications,
				Status.SUCCESS);
	}
	

	/**
	 * Get Application Details For Listing- Cisco
	 * @param searchText
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLICATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanApplications.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/{productId}/applications")
	public ResponseResource<CiscoApplications> getCiscosdwanApplicationList(@PathVariable("productId") Integer productId, 
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		CiscoApplications sdwanApplications = izoSdwanInventoryServiceCisco.getSdwanAppplications(productId, customerId, customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanApplications,
				Status.SUCCESS);
	}
	/**
	 * Update cpe and Template alias
	 *
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.UPDATE_SDWAN_CPE_TEMPLATE_ALIAS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response=String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/alias/{type}")
	public ResponseResource<String> updateAliasNameForCpeAndTemplate(@PathVariable ("type") String type,@RequestBody SdwanAliasUpdateRequest request, 
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		String response = izoSdwanInventoryService.updateAliasName(request,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * Update cpe for cisco
	 *
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.UPDATE_SDWAN_CPE_TEMPLATE_ALIAS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response=String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/cisco/alias/{type}")
	 
	public ResponseResource<String> updateAliasNameForCiscoCpe(@PathVariable ("type") String type,@RequestBody CiscoAliasUpdateRequest request, 
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		String response = izoSdwanCiscoDetailedInfoService.updateAliasName(request,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * API to get partner and customer legal entity based on logged-in user
	 *
	 * @return {@link List<PartnerDetailBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PartnerCustomerLeDetail.GET_PARTNER_CUSTOMER_LE_DETAIL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/partnerle/customerle/details")
	public ResponseResource<List<PartnerDetailBean>> getCustomerLeBasedOnPartnerLe() {
		List<PartnerDetailBean> response = serviceInventoryService.getCustomerLeBasedOnPartnerLe();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}




	/**
	 * Get Service details based on login user and product for partner
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceInformationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/{productId}/partner/servicedetails")
	public ResponseResource<SIServiceInformationBean> getServiceDetailsByProductForPartner(@PathVariable("productId") Integer productId,@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,@RequestParam(value="partnerId",required = false) Integer partnerId,@RequestParam(value="customerLeId",required = false) Integer customerLeId,@RequestParam(value="partnerLeId",required = false) Integer partnerLeId) throws TclCommonException {
		SIServiceInformationBean response = serviceInventoryService.getAllServiceDetailsByProductForPartner(productId,page,size,customerId,partnerId,customerLeId,partnerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,Status.SUCCESS);
				
	}
	
	
	 /* To fetch list of policies from Versa and their templates
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_POLICY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/policies")
	public ResponseResource<SdwanPolicyBeanWithTimeStamp> sdwanPolicyList(@PathVariable("productId") Integer productId,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		SdwanPolicyBeanWithTimeStamp sdwanApplications = izoSdwanInventoryService.getPoliciesList(productId, customerId,
				customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanApplications,
				Status.SUCCESS);
	}

	/**
	 * Get detailed info of a policy
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_POLICY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/policydetail")
	public ResponseResource<SdwanPolicyDetailBean> sdwanPolicyDetails(
			@RequestBody SdwanPolicyDetailsRequestBean request)			throws TclCommonException {
		SdwanPolicyDetailBean sdwanPolicyDetailBean = izoSdwanInventoryService.getPolicyDetailedView(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanPolicyDetailBean,
				Status.SUCCESS);
	}
	/**
	 * Get detailed info of a policy for cisco
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_POLICY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/cisco/policydetail")
	public ResponseResource<CiscoPolicyDetailBean> sdwanPolicyDetails(
			@RequestBody CiscoPolicyDetailsRequestBean request,HttpServletRequest httpServletRequest,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId)	throws TclCommonException {
		CiscoPolicyDetailBean ciscoPolicyDetailBean = izoSdwanCiscoDetailedInfoService.getPolicyDetailedView(request,httpServletRequest,customerId, partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ciscoPolicyDetailBean,
				Status.SUCCESS);
	}
	
	/**
	 * API to fetch user defined application detailed info
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLICATION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanApplications.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/application/detailedInfo")
	public ResponseResource<SdwanApplications> sdwanApplicationDetailedInfo( @PathVariable("productId") Integer productId, @RequestBody ApplicationInfoRequestBean appInfoRequestBean,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException {
		SdwanApplications applicationDetailedInfo = izoSdwanInventoryService.sdwanApplicationDetailedInfo(appInfoRequestBean, customerId, partnerId, customerLeId, productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, applicationDetailedInfo,
				Status.SUCCESS);		
	}

	/**
	 * Get Service details based on filters
	 * 
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndCpeStatusCount.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/status/count")
	public ResponseResource<SiteAndCpeStatusCount> getStatusOfCpeAndSitesCount(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId)
			throws TclCommonException, IOException {
		SiteAndCpeStatusCount countData = izoSdwanInventoryService.getSiteAndCpeStatusCount(productId, customerId,
				customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, countData,
				Status.SUCCESS);
	}
	
	/**
	 * Get Status Count For Cisco
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteAndCpeStatusCount.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/status/cisco/count")
	public ResponseResource<SiteAndCpeStatusCount> getStatusOfCpeAndSitesCountForCisco(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId)
			throws TclCommonException, IOException {
		SiteAndCpeStatusCount countData = izoSdwanInventoryServiceCisco.getSiteAndCpeStatusCount(productId, customerId,
				customerLeId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, countData,
				Status.SUCCESS);
	}

	/**
	 * Edit detailed info of a policy
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.EDIT_POLICY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/editpolicydetail")
	public ResponseResource<TemplateCpeStatusResponse> editPolicyDetails(@RequestBody SdwanPolicyDetailBean request)
			throws TclCommonException {
		TemplateCpeStatusResponse sdwanPolicyDetailBean = izoSdwanInventoryService.editPolicyDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanPolicyDetailBean,
				Status.SUCCESS);
	}
	
	/**
	 * update detailed info of a policy for Cisco
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.EDIT_POLICY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/cisco/updatePolicydetail")
	public ResponseResource<CiscoPolicyStatusResponse> updatePolicyDetails(@RequestBody CiscoPolicyDetailBean request,
			HttpServletRequest httpServletRequest,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId) throws TclCommonException 
			 {
		CiscoPolicyStatusResponse sdwanPolicyDetailBean = izoSdwanCiscoDetailedInfoService
				.updatePolicyDetails(request,httpServletRequest,customerId,customerLeId,partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, sdwanPolicyDetailBean,
				Status.SUCCESS);
	}
	
	
	/**
	 * API to create user defined applications
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.CREATE_USER_DEFINED_APPLICATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/application")
	public ResponseResource<TemplateCpeStatusResponse> createUserDefinedApplication(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(value = "action") String action, @RequestBody VersaUserDefAppRequest versaUserDefAppRequest)
			throws TclCommonException {
		TemplateCpeStatusResponse response = izoSdwanInventoryService.createUserDefinedApps(productId, customerId,
				customerLeId, partnerId, versaUserDefAppRequest, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to fetch versa applications against organization
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLICATIONS_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VersaApplicationNames.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/versa/application")
	public ResponseResource<VersaApplicationNames> sdwanApplicationInfo(@PathVariable("productId") Integer productId, @RequestBody SdwanPolicyListBean sdwanPolicyListBean,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId)
			throws TclCommonException, IOException {
		VersaApplicationNames applicationNames = izoSdwanInventoryService.sdwanApplicationsFromVersa(customerId,partnerId, customerLeId, productId, sdwanPolicyListBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, applicationNames,
				Status.SUCCESS);
	}

	/**
	 * API to fetch versa applications against organization
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLICATIONS_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VersaApplicationNames.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/versa/address")
	public ResponseResource<VersaAddressListBean> sdwanAddressList(@PathVariable("productId") Integer productId,
			@RequestBody SdwanPolicyListBean sdwanPolicyListBean,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId)
			throws TclCommonException, IOException {
		VersaAddressListBean addressFromVersa = izoSdwanInventoryService.sdwanAddressFromVersa(customerId, partnerId,
				customerLeId, productId, sdwanPolicyListBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, addressFromVersa,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/le/{productId}/servicedetails/search")
	public ResponseResource<PagedResult<List<SIServiceInformationBean>>> getServiceDetailsByLe(
			@PathVariable("productId") Integer productId, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(value="city",required=false) String city, @RequestParam(value="alias",required=false) String alias,
			@RequestParam(value="searchText",required=false) String searchText,@RequestParam(value="customerId",required = false)Integer customerId,@RequestParam(value="partnerId",required = false)Integer partnerId,@RequestParam(value="partnerLeId",required = false)Integer partnerLeId,@RequestParam(value="customerLeId",required = false)Integer customerLeId,
			@RequestParam(value="opportunityMode",required=false) String opportunityMode) throws TclCommonException {
		PagedResult<List<SIServiceInformationBean>> response = serviceInventoryService
				.getLeServiceDetailsWithPaginationAndSearch(productId, page, size, city, alias, searchText,customerId,partnerId,partnerLeId,customerLeId, opportunityMode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * To fetch CPE status/availability/sync status for a given template
	 *
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param sdwanPolicyListBean
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/cpestatus")
	public ResponseResource<List<CpeSyncStatusBean>> getCPESyncStatus(@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestBody SdwanPolicyListBean sdwanPolicyListBean) throws TclCommonException, IOException {
		List<CpeSyncStatusBean> response = izoSdwanInventoryService.getCPESyncStatus(productId, customerId, partnerId,
				customerLeId, sdwanPolicyListBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * To fetch CPE status/availability/sync status for a given siteList
	 *
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param sdwanPolicyListBean
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/{productId}/siteStatus")
	public ResponseResource<List<AssosciatedSiteDetails>> getCiscoSiteListConfigStatus(@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam (value = "siteListId", required = true) String siteListId) throws TclCommonException, IOException {
		List<AssosciatedSiteDetails> response = izoSdwanCiscoDetailedInfoService.getCiscoSiteListConfigStatus(productId, customerId, partnerId,
				customerLeId, siteListId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * CPE filter including status (multithreaded)
	 *
	 * @param searchText
	 * @param size
	 * @param page
	 * @param sortBy
	 * @param sortOrder
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cpe/filter/poc")
	public ResponseResource<SdwanCPEInformationBean> getCpeDetailsByFiltersThreadPOC(
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "productId", required = true) Integer productId,
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "partnerId", required = false) Integer partnerId,
			@RequestParam(name = "groupBy", required = false) String groupBy)
			throws TclCommonException, IOException, ExecutionException, InterruptedException {
		SdwanCPEInformationBean filterData = izoSdwanInventoryService.getCpeDetailsBasedOnFiltersPoc(searchText, size,
				page, sortBy, sortOrder, productId, customerId, customerLeId, partnerId, groupBy);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_SDWAN_CPE_DETAILS_BASED_ON_FILTER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewSiServiceInfoAllBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/cisco/cpe/filter")
	public ResponseResource<CiscoCPEInformationBean> getCiscoCpeDetailsByFiltersThread(
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "productId", required = true) Integer productId,
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(name = "partnerId", required = false) Integer partnerId,
			@RequestParam(name = "groupBy", required = false) String groupBy)
			throws TclCommonException, IOException, ExecutionException, InterruptedException {
		CiscoCPEInformationBean filterData = izoSdwanInventoryServiceCisco.getCiscoCpeDetailsBasedOnFilters(searchText, size,
				page, sortBy, sortOrder, productId, customerId, customerLeId, partnerId, groupBy);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, filterData,
				Status.SUCCESS);
	}

	/**
	 * API to fetch task details by task id's
	 * @param taskIds
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_SDWAN_TASK_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/task")
	public ResponseResource<List<SdwanTaskDetailsBean>> getSdwanTaskIdDetails(@RequestBody List<Integer> taskIds)
			throws TclCommonException, IOException {
		List<SdwanTaskDetailsBean> response = izoSdwanInventoryService.getSdwanTaskIdDetails(taskIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/**
	 * API to fetch bandwidth utilization of sites
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param bwUnit
	 * @param startDate
	 * @param endDate
	 * @param httpServletRequest
	 * @param servletResponse
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_SITE_UTILIZATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/utilization/site")
	public ResponseResource<SdwanSiteUtilizationDetails> getSdwanSiteUtilizationDetails(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(value = "unit", required = false) String bwUnit,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletRequest httpServletRequest,
			HttpServletResponse servletResponse) throws TclCommonException, IOException {
		SdwanSiteUtilizationDetails response = izoSdwanInventoryService.getSdwanSiteUtilization(productId, customerId,
				customerLeId, partnerId, bwUnit, startDate, endDate, httpServletRequest, servletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to fetch top link bandwidth utilization of links associated to sites
	 * @param siteName
	 * @param org
	 * @param region
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_LINK_UTILIZATION_FOR_TOP_LINKS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/topLinkDetails")
	public ResponseResource<List<SdwanLinkUsages>> topLinkDetails(
			@RequestParam(value = "siteName", required = false) String siteName,
			@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "region", required = false) String region,
			HttpServletRequest request, @RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws TclCommonException, IOException {
		List<SdwanLinkUsages> response = izoSdwanInventoryService.getBandwidthForTopLinks(siteName, org, region,
				request, startDate, endDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to fetch bandwidth utilization of sites
	 *
	 * @param linkName
	 * @param orgName
	 * @param cpes
	 * @param endDate
	 * @param request
	 * @param startDate
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APP_UTILIZATION_BY_LINKS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/utilization/site/apps")
	public ResponseResource<AppLinkUtilizationBean> getApplicationUtilizationByLink(
			@RequestParam("linkName") String linkName, @RequestParam("orgName") String orgName,
			@RequestBody List<String> cpes, HttpServletRequest request,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws TclCommonException, IOException {
		AppLinkUtilizationBean response = izoSdwanInventoryService.getApplicationUtilizationByLink(orgName, linkName,
				cpes, request, startDate, endDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to fetch bandwidth utilization of Application for organization
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param productId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APP_UTILIZATION_BY_ORAGANIZATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AppLinkUtilizationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/applicationUtilization/byOrg")
	public ResponseResource<AppLinkUtilizationBean> getApplicationUtilizationByOrg(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletRequest request)
			throws TclCommonException {
		AppLinkUtilizationBean response = izoSdwanInventoryService
				.getApplicationUtilizedPercentageForOrganisation(productId,customerId,partnerId,customerLeId,startDate,endDate,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to fetch bandwidth utilization of application in each site of a customer
	 *
	 * @param appName
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param productId
	 * @param request
	 * @param bwUnit
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_SITE_UTILIZATION_BY_APPS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/{productId}/utilization/apps/site")
	public ResponseResource<SdwanSiteUtilizationDetails> getApplicationUtilizationByLink(
			@RequestParam("appName") String appName,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@PathVariable("productId") Integer productId, HttpServletRequest request,
			@RequestParam(value = "unit", required = false) String bwUnit,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "maxSites", required = false) Integer maxSites) throws TclCommonException {
		SdwanSiteUtilizationDetails response = izoSdwanInventoryService.getSiteUtilizationByApp(appName, productId,
				customerId, customerLeId, partnerId, request, bwUnit, startDate, endDate, maxSites);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to fetch path details for a template
	 *
	 * @param templateName
	 * @param orgName
	 * @param profileName
	 * @param region
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_SDWAN_VERSA_PATH)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/paths")
	public ResponseResource<Set<TemplatePathInterfaceDetail>> getPathInfoForTemplate(
			@RequestParam(name = "templateName") String templateName, @RequestParam(name = "region") String region,
			@RequestParam(name = "profileName") String profileName, @RequestParam(name = "orgName") String orgName)
			throws TclCommonException, IOException {
		Set<TemplatePathInterfaceDetail> response = izoSdwanInventoryService.getPathInfoForTemplate(templateName,
				region, profileName, orgName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to fetch over all utilized and total BW across orgLevel
	 * 
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param productId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APP_BW_OVERALL_PERCENTAGE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanBandwidthUtilized.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/appBWOverAllPercentage/byOrg")
	public ResponseResource<SdwanBandwidthUtilized> getApplicationBwByOrg(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletRequest request)
			throws TclCommonException {
		SdwanBandwidthUtilized response = izoSdwanInventoryService
				.getBandwidthUtilizedAndTotalBw(productId,customerId,partnerId,customerLeId,startDate,endDate,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to fetch link utilized by app
	 * @param siteName
	 * @param appName
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IzoSdwanMacd.GET_APPLS_BW_CONSUMPTION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/appConsumptionBandWidthByLink")
	public ResponseResource<List<BandwidthUtilizationOfApp>> getApplicationBwByOrg(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "siteName", required = false) String siteName,
			@RequestParam(value = "appName", required = false) String appName,
			HttpServletRequest request)
			throws TclCommonException {
		List<BandwidthUtilizationOfApp> response = izoSdwanInventoryService
				.getLinkUtilizationBandwidth(startDate,endDate,siteName,appName,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API for edit template details
	 * 
	 * @param sdwanTemplateDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/getTemplateEditDetails")
	public ResponseResource<String> getTemplateEditDetails(@RequestBody SdwanTemplateDetailBean sdwanTemplateDetailBean)
			throws TclCommonException {
		String response = izoSdwanInventoryService.editTemplateDetails(sdwanTemplateDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@GetMapping(value = "/ipc/services/{serviceId}/solutions")
	public ResponseResource<List<IpcProductSolutionBean>> getIpcExistingSolutionDetails(
			@PathVariable("serviceId") String serviceId)
			throws TclCommonException, IOException {
		List<IpcProductSolutionBean> response = ipcInventoryService.getIpcExistingSolutionDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * API for update siteListConfig details
	 * 
	 * @param sdwanTemplateDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANCPE.GET_ASSET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/cisco/updateSiteListConfigDetailedInfo")
	public ResponseResource<String> updateSiteListConfigDetailedInfo(@RequestBody CiscoSiteListConfigBean ciscoSiteListConfigBean,
			@RequestParam(required = false, name = "customerId") Integer customerId,
			@RequestParam(required = false, name = "customerLeId") Integer customerLeId,
			@RequestParam(required = false, name = "partnerId") Integer partnerId,
			HttpServletRequest request)
			throws TclCommonException {
		String response = izoSdwanCiscoDetailedInfoService.updateSiteListConfig(ciscoSiteListConfigBean,
				customerId,
				customerLeId, partnerId,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to download SDWAN inventory audit in csv file
	 * @param response
	 * @throws IOException
	 * @throws CsvDataTypeMismatchException
	 * @throws CsvRequiredFieldEmptyException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.DOWNLOAD_ATTACHMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SIProductFamilySummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/download/audit")
	public void getAuditAndTransactionCSV(HttpServletResponse response)
			throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, TclCommonException {
		izoSdwanInventoryService.downloadSdwanAudit(response);
	}
	
	/**
	 * Method to get performance details
	 * @param request
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PerformanceDetails.PERFORMANCE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/performanceDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<PerformanceResponse>> getPerformanceDetails(@RequestBody PerformanceRequest request)
			throws TclCommonException, ParseException {
		List<PerformanceResponse> response = izoSdwanInventoryService.getPerformanceDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/** MEthod to get performace site details
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SDWANSITES.GET_SDWAN_SITE_PERFORMANCE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanSiteDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/{productId}/performacesitedetails")
	public ResponseResource<SdwanSiteDetailsPerformaceBean> getSdwanSitePerformanceDetails(
			@PathVariable("productId") Integer productId,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId) throws TclCommonException {
		SdwanSiteDetailsPerformaceBean response = izoSdwanInventoryService.getSdwanPerformaceSiteDetails(productId,
				customerId, partnerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to get site by usage live data
	 * @param siteName
	 * @param cpeName
	 * @param apiType
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws ParseException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PerformanceDetails.GET_SITE_BY_USAGE_LIVE_DATA)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sdwan/siteByUsage/LiveData")
	public ResponseResource<Object> siteByUsageLiveDataDetails(
			@RequestParam(value = "siteName", required = false) String siteName,
			@RequestParam(value = "cpeName", required = false) String cpeName,
			@RequestParam(value = "apiType", required = false) String apiType
			) throws TclCommonException, IOException, ParseException {
		Object response = izoSdwanInventoryService.getLiveDatadetails(siteName, cpeName,apiType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get all le,secs id, access types of each configuration
	 *
	 * @param productName
	 * @return {@link GSCConfigurationDisntictDetailsBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.SI_ORDER_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GSCConfigurationDisntictDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/products/{productName}/configurations/details")
	public ResponseResource<GSCConfigurationDisntictDetailsBean> getAllDistinctDetailsOfEachConfigurations(@PathVariable String productName, @RequestBody GscServiceInventoryConfigurationRequestBean request) {
		GSCConfigurationDisntictDetailsBean response = serviceInventoryService.getDistinctDetailsOfEachConfigurations(productName, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Method to retrieve the detailed information for the List of provided service id
	 *
	 * @param serviceId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.SERVICE_DATA)
	@RequestMapping(value = "/npl/mc/serviceDetaillist", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIServiceDetailedResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<NPLSIServiceDetailedResponse>> getNPLMcDetailedSIInfoList(@RequestBody ServiceIdListBean serviceIds) throws TclCommonException {
		List<NPLSIServiceDetailedResponse> response = serviceInventoryService.getNPLDetailedSIInfoMc(serviceIds.getServiceIdList(), serviceIds.getIsTermination());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}




	/**
	 *
	 *return list of
	 * @param secsId
	 *
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SIOrder.GET_ACCESS_TYPE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/gsc/accesstype/names")
	public ResponseResource<Set<String>> getGscAccessTypeDetails(@RequestParam(value = "secsId") Integer secsId) throws TclCommonException {
		Set<String> response = serviceInventoryService.getGscAccessTypeDetails(secsId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@PostMapping("/renewal/test")
	public ResponseResource<String> test(@RequestBody String request) throws TclCommonException {
		String[] listString =  (request).split(",");
		RenewalsQuoteDetail quoteDetail =renewalsServiceInventoryService.constructQuoteDetailsNplForListV1(listString);
		String response = Utils.convertObjectToJson(quoteDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping("/renewal/test1")
	public ResponseResource<String> test1(@RequestBody String request) throws TclCommonException {
		String[] listString =  (request).split(",");
		RenewalsQuoteDetail quoteDetail =renewalsServiceInventoryService.constructQuoteDetailsNplForList(listString);
		String response = Utils.convertObjectToJson(quoteDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.ConfigurationDetails.CONFIGURATION_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SINumberConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/products/servicedetailscount")
	public ResponseResource<List<ProductInformationBean>> getServiceDetailsProductwiseCount(@RequestParam(value="customerId",required = false) Integer customerId, @RequestParam(value="partnerId",required = false) Integer partnerId) throws TclCommonException {
		List<ProductInformationBean> response = serviceInventoryService.getAllProductServiceInformationCount(customerId, partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.InventoryDetails.GET_SI_ASSET_DETAILS_BY_CLOUDCODE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = IpcSolutionDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ipc/assets/price")
	public ResponseResource<List<IpcSolutionDetail>> getIpcAssetDetails(@RequestBody Map<String,Object> inputReq) throws TclCommonException {
		return new ResponseResource<List<IpcSolutionDetail>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ipcInventoryService.getAssetDetailsByCloudCodes(inputReq),
				Status.SUCCESS);
	}

}

