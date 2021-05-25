package com.tcl.dias.oms.renewals.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.renewals.bean.RenevalsValidateBean;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.renewals.bean.RenewalsFileStorageService;
import com.tcl.dias.oms.renewals.bean.RenewalsQuoteDetail;
import com.tcl.dias.oms.renewals.service.RenewalsGvpnQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsGvpnService;
import com.tcl.dias.oms.renewals.service.RenewalsIllQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsNplQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsNplQuoteService;
import com.tcl.dias.oms.renewals.service.RenewalsService;
import com.tcl.dias.oms.renewals.serviceV1.RenewalsServiceCommon;
import com.tcl.dias.oms.renewals.serviceV1.RenewalsServiceCommonGvpn;
import com.tcl.dias.oms.renewals.serviceV1.RenewalsServiceCommonNpl;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/renewals")
public class RenewalsController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RenewalsController.class);

	@Autowired
	private RenewalsService renewalsServices;

	@Autowired
	private RenewalsIllQuotePdfService renewalsIllQuotePdfService;

	@Autowired
	private IllQuoteService illQuoteService;

	@Autowired
	private RenewalsGvpnService renewalsGvpnService;

	@Autowired
	private RenewalsGvpnQuotePdfService renewalsGvpnQuotePdfService;

	@Autowired
	private RenewalsNplQuoteService renewalsNplQuoteService;

	@Autowired
	private RenewalsNplQuotePdfService renewalsNplQuotePdfService;
	
	@Autowired
	private RenewalsServiceCommon renewalsServiceCommon;
	@Autowired
	private RenewalsServiceCommonGvpn renewalsServiceCommonGvpn;
	
	@Autowired
	private RenewalsServiceCommonNpl renewalsServiceCommonNpl;
	
	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@RequestMapping(value = "/le/{leId}/cus/{custId}/product/{product}/validate", method = RequestMethod.POST)
	public ResponseResource<List<RenevalsValidateBean>> validayeCofPdf(@PathVariable("leId") Integer leId,
			@PathVariable("custId") Integer custId, @PathVariable("product") String product,
			HttpServletResponse response, @RequestParam(name = "file") MultipartFile file)
			throws TclCommonException, IOException {

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		List<RenevalsValidateBean> renevalsVelidateBeanList = renewalsServices.validateExcel(leId, custId, product,
				workbook);
		boolean result = renewalsServices.validate(renevalsVelidateBeanList);
		if (result) {
			return new ResponseResource<List<RenevalsValidateBean>>(ResponseResource.R_CODE_OK,
					ResponseResource.RES_SUCCESS, renevalsVelidateBeanList, Status.SUCCESS);
		} else {
			return new ResponseResource<List<RenevalsValidateBean>>(ResponseResource.R_CODE_ERROR,
					ResponseResource.RES_FAILURE, renevalsVelidateBeanList, Status.ERROR);
		}
	}

	@RequestMapping(value = "/le/{leId}/cus/{custId}/product/{product}/term/{term}/commercial/{commercial}/date/{date}/oppid/{oppid}/upload", method = RequestMethod.POST)
	public ResponseResource<RenewalsQuoteDetail> uploadCofPdf(@PathVariable("leId") Integer leId,
			@PathVariable("custId") Integer custId, @PathVariable("product") String product,
			@PathVariable("term") Integer term, @PathVariable("commercial") Character commercial,@PathVariable("date") String date,@PathVariable("oppid") String oppId,
			HttpServletResponse response, @RequestParam(name = "file") MultipartFile file)
			throws TclCommonException, IOException, ParseException {
		RenewalsQuoteDetail quoteDetail = null;
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		if (product.equalsIgnoreCase(RenewalsConstant.IAS)) {
			quoteDetail = renewalsServiceCommon.processExcel(leId, custId, product, term, commercial, date, oppId, workbook);
		} else if (product.equalsIgnoreCase(RenewalsConstant.GVPN)) {
			quoteDetail = renewalsServiceCommonGvpn.processExcel(leId, custId, product, term, commercial,date,oppId,  workbook);
		} else if (product.equalsIgnoreCase(RenewalsConstant.NPL)) {
			quoteDetail = renewalsServiceCommonNpl.processExcel(leId, custId, product, term, commercial,date,oppId, workbook);
		} else if (product.equalsIgnoreCase(RenewalsConstant.NDE)) {
			quoteDetail = renewalsServiceCommonNpl.processExcel(leId, custId, product, term, commercial,date,oppId, workbook);
		}
		return new ResponseResource<RenewalsQuoteDetail>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				quoteDetail, Status.SUCCESS);
	}
	@RequestMapping(value = "/le/{leId}/cus/{custId}/product/{product}/term/{term}/commercial/{commercial}/date/{date}/upload/old", method = RequestMethod.POST)
	public ResponseResource<RenewalsQuoteDetail> uploadCofPdfold(@PathVariable("leId") Integer leId,
			@PathVariable("custId") Integer custId, @PathVariable("product") String product,
			@PathVariable("term") Integer term, @PathVariable("commercial") Character commercial,@PathVariable("date") String date,
			HttpServletResponse response, @RequestParam(name = "file") MultipartFile file)
			throws TclCommonException, IOException {
		RenewalsQuoteDetail quoteDetail = null;
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
	    if (product.equalsIgnoreCase(RenewalsConstant.NDE)) {
			quoteDetail = renewalsNplQuoteService.processExcel(leId, custId, product, term, commercial,date, workbook);
		}
		return new ResponseResource<RenewalsQuoteDetail>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				quoteDetail, Status.SUCCESS);
	}
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/product/{product}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(value = "nat", required = false) Boolean nat, @PathVariable("product") String product)
			throws TclCommonException {
		if (product.equalsIgnoreCase(RenewalsConstant.IAS)) {
			renewalsIllQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
		} else if (product.equalsIgnoreCase(RenewalsConstant.GVPN)) {
			renewalsGvpnQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
		} else {
			renewalsNplQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities)
			throws TclCommonException {
		QuoteBean response = illQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities, null, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/product/{product}/docusign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocusignWithApprover(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("nat") Boolean nat,
			@RequestParam("name") String name, @RequestParam("email") String emailId,
			@RequestBody ApproverListBean approvers,@PathVariable("product") String product, HttpServletRequest httpServletRequest) throws TclCommonException {
		if(product.equalsIgnoreCase("IAS")) {
		renewalsIllQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approvers);
		}
		else if(product.equalsIgnoreCase("GVPN")) {
			renewalsGvpnQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approvers);
		}
		else if(product.equalsIgnoreCase("NPL")) {
			renewalsNplQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approvers);
		}
		else if(product.equalsIgnoreCase("NDE")) {
			renewalsNplQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approvers);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name = "file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = renewalsIllQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}

	@Autowired
	private RenewalsFileStorageService renewalsFileStorageService;
	@Value("classpath:renewalstemplates/IAS.xlxs")
	Resource resourceFile;

	@RequestMapping(value = "/file/product/{product}", method = RequestMethod.GET)
	public void downloadSampleCSV(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("product") String product) throws Exception {

		renewalsFileStorageService.createExcelTemplate(response, product);

	}
	
	@RequestMapping(value = "/quote/update/{quoteid}", method = RequestMethod.POST)
	public ResponseResource<Integer>  updateQuoteToLe(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("quoteid") Integer quoteid) throws Exception {

		Integer quoteId = renewalsServiceCommon.updateQuoteTole(quoteid);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteId,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/npl/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> nplRetriggerCreditCheck(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = nplQuoteService.retriggerCreditCheckNpl(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);

	}

	/**
	 *
	 * RetriggerCreditCheck query gvpn
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/gvpn/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> reTriggerCreditCheckGvpn(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = gvpnQuoteService.retriggerCreditCheck(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);
	}
	
	
	/**
	 * 
	 * RetriggerCreditCheck query
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<String> reTriggerCreditCheck(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = illQuoteService.retriggerCreditCheck(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);

	}
	@RequestMapping(value = "/quote/le/{quoteLeId}/upload", method = RequestMethod.POST)
	public void templateJpload(@PathVariable("quoteLeId") Integer quoteLeId,
			HttpServletResponse response, @RequestParam(name = "file") MultipartFile file)
			throws TclCommonException, IOException, ParseException {
			renewalsServiceCommon.attachTemplate(file, quoteLeId);
		
	}
	
}
