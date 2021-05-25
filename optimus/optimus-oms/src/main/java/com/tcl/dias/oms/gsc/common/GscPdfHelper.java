package com.tcl.dias.oms.gsc.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscQuotePdfBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Component to handle GSC pdf related functionality
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class GscPdfHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscPdfHelper.class);

	@Autowired
	GscOrderService gscOrderService;

	@Autowired
	GscQuotePdfService gscQuotePdfService;

	@Autowired
	GscQuoteService gscQuoteService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${cof.manual.upload.path}")
	String cofManualUploadPath;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${swift.api.container}")
	String swiftApiContainer;

	private GscPdfHelper() {
	}

	/**
	 * return cof pdf
	 * 
	 * @author Mayank Sharma
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	public void generateGscCof(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
			GscQuoteDataBean quoteDetail = gscQuoteService.getGscQuoteById(quoteId).get();
			GscQuotePdfBean quotePdfRequest = new GscQuotePdfBean();
			constructVariable(quoteDetail, quotePdfRequest);
			Map<String, Object> variable = objectMapper.convertValue(quotePdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("gscquote_template", context);
			LOGGER.warn(html);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "Quote_" + quoteDetail.getQuoteId() + ".pdf";
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			bos.flush();
			bos.close();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * constructBasicDetailsForQuotes
	 *
	 * @param quoteDetail
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private static void constructVariable(GscQuoteDataBean quoteDetail, GscQuotePdfBean cofPdfRequest) {
		cofPdfRequest.setQuoteId(quoteDetail.getQuoteId());
		cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		cofPdfRequest.setCustomerId(quoteDetail.getCustomerId());
		cofPdfRequest.setQuoteLeId(quoteDetail.getQuoteLeId());
		cofPdfRequest.setProductFamilyName(quoteDetail.getProductFamilyName());
		cofPdfRequest.setAccessType(quoteDetail.getAccessType());
		cofPdfRequest.setProfileName(quoteDetail.getProfileName());
		cofPdfRequest.setSolutions(quoteDetail.getSolutions());
		cofPdfRequest.setLegalEntities(quoteDetail.getLegalEntities());

	}

	/**
	 * Upload cof pdf
	 * 
	 * @param quoteId
	 * @param file
	 * @throws TclCommonException
	 */
	public TempUploadUrlInfo uploadCofPdf(Integer quoteId, MultipartFile file, String action)
			throws TclCommonException {
		if (action.isEmpty() || !(action.equals(GscConstants.UPLOAD))) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteEntity.get().getId())
							.stream().findFirst();
					if (quoteToLe.isPresent()) {
						List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
								.findByQuoteToLe(quoteToLe.get());
						Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
										.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
								.findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
								.stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
										.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
								.findFirst();
						if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent())
							tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
									Long.parseLong(tempUploadUrlExpiryWindow),
									customerCodeLeVal.get().getAttributeValue(),
									customerLeCodeLeVal.get().getAttributeValue(),false);
					}
				} else {
					// Get the file and save it somewhere
					String cofPath = cofManualUploadPath + quoteEntity.get().getQuoteCode().toLowerCase();
					File filefolder = new File(cofPath);
					if (!filefolder.exists()) {
						filefolder.mkdirs();

					}
					Path path = Paths.get(cofPath);
					Path filePath = path.resolve(file.getOriginalFilename());
					if (filePath != null)
						Files.deleteIfExists(filePath);
					Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteEntity.get().getQuoteCode());
					if (cofDetails == null) {
						cofDetails = new CofDetails();
						cofDetails.setOrderUuid(quoteEntity.get().getQuoteCode());
						cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
						cofDetails.setSource(Source.MANUAL_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					} else {
						cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
						cofDetails.setSource(Source.MANUAL_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempUploadUrlInfo;
	}
}
