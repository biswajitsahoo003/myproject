package com.tcl.dias.oms.renewals.service;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.Site;
import com.tcl.dias.oms.beans.SiteDetail;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.CommercialQuoteAudit;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteVrfSites;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CommercialQuoteAuditRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionSiLinkRepository;
import com.tcl.dias.oms.entity.repository.QuoteAccessPermissionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteVrfSitesRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.renewals.bean.RenevalsQuoteTotalBean;
import com.tcl.dias.oms.renewals.bean.RenevalsValidateBean;
import com.tcl.dias.oms.renewals.bean.RenewalsAttributeDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsComponentDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.renewals.bean.RenewalsExcelBean;
import com.tcl.dias.oms.renewals.bean.RenewalsPriceBean;
import com.tcl.dias.oms.renewals.bean.RenewalsQuoteDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsSite;
import com.tcl.dias.oms.renewals.bean.RenewalsSiteDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsSolutionDetail;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.validator.services.GvpnCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class RenewalsGvpnService {

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.renewals.si.validate.details.queue}")
	String siValidateQueue;

	@Value("${rabbitmq.renewals.si.details.queue}")
	String siOrderDetailsQueue;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;
	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;
	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	PartnerService partnerService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	ProductSolutionSiLinkRepository productSolutionSiLinkRepository;

	@Autowired
	RenewalsExcelPriceMapping renewalsExcelPriceMapping;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	public static final Logger LOGGER = LoggerFactory.getLogger(RenewalsGvpnService.class);

	public List<RenevalsValidateBean> validateExcel(Integer leId, Integer custId, String product, XSSFWorkbook workbook)
			throws TclCommonException {
		LOGGER.info(" Reading Valdidation Input Excel");
		try {
			List<String> serviceIdList = new ArrayList<String>();
			XSSFSheet worksheet = workbook.getSheetAt(0);
			for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = worksheet.getRow(i);

				if (row.getCell(0).getStringCellValue() != null) {
					serviceIdList.add(row.getCell(0).getStringCellValue());
				} else {
					throw new TclCommonException(RenewalsConstant.SERVICE_ID_NA);
				}
				for (int j = 0; j < worksheet.getRow(i).getPhysicalNumberOfCells(); j++) {
					try {
						row.getCell(0).getStringCellValue();
					} catch (Exception e) {
						LOGGER.info("String Input In Number Cell In Excel");
						throw new TclCommonException(RenewalsConstant.INVALID_NUMBER);
					}
				}
			}
			List<RenevalsValidateBean> renevalsValidateBeanList = validateServiceIdWithCustomerDetail(leId, custId,
					serviceIdList, product);
			return renevalsValidateBeanList;
		} catch (Exception e) {
			throw e;
		}

	}

	public List<RenevalsValidateBean> validateServiceIdWithCustomerDetail(Integer leId, Integer custId,
			List serviceIdList, String prdName) throws TclCommonException {

		String renevalsValidateBeanString = (String) mqUtils.sendAndReceive(siValidateQueue, serviceIdList.toString());
		if (Objects.nonNull(renevalsValidateBeanString)) {
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}",
					renevalsValidateBeanString, serviceIdList);
		}
		RenevalsValidateBean[] renevalsValidateBeans = (RenevalsValidateBean[]) Utils
				.convertJsonToObject(renevalsValidateBeanString, RenevalsValidateBean[].class);
		List<RenevalsValidateBean> renevalsValidateBeanList = new ArrayList<RenevalsValidateBean>();
		for (RenevalsValidateBean renevalsValidateBean : renevalsValidateBeans) {

			if (renevalsValidateBean.getCustId().equalsIgnoreCase(custId.toString())) {
				renevalsValidateBean.setValidCustId(true);
			}
			if (renevalsValidateBean.getLeId().equalsIgnoreCase(leId.toString())) {
				renevalsValidateBean.setValidLeId(true);
			}
			if (renevalsValidateBean.getProduct().equalsIgnoreCase(prdName.toString())) {
				renevalsValidateBean.setValidProduct(true);
			}
			renevalsValidateBeanList.add(renevalsValidateBean);
		}
		return renevalsValidateBeanList;
	}

	public boolean validate(List<RenevalsValidateBean> renevalsVelidateBeanList) {

		for (RenevalsValidateBean renevalsValidateBean : renevalsVelidateBeanList) {
			if (!renevalsValidateBean.isValidProduct()) {
				return false;
			}
			if (!renevalsValidateBean.isValidCustId()) {
				return false;
			}
			if (!renevalsValidateBean.isValidCustId()) {
				return false;
			}
		}

		return true;
	}

	public RenewalsQuoteDetail processExcel(Integer leId, Integer custId, String product, Integer term,
			Character commercial,String date, XSSFWorkbook workbook) throws TclCommonException {
		LOGGER.info("Processing Excel leId-->{} Customer Id -->{}  Product --> {} Contract Term -->{}",
				leId, custId, product, term);
		List<String> serviceIdList = new ArrayList<String>();
		List<RenewalsExcelBean> renewalsExcelBeanList = new ArrayList<RenewalsExcelBean>();
		XSSFSheet worksheet = workbook.getSheetAt(0);
		UpdateRequest updateRequest = new UpdateRequest();
		RenevalsQuoteTotalBean renevalsQuoteTotalBeanFinal = new RenevalsQuoteTotalBean();

		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			DataFormatter formatter = new DataFormatter();
			String val = formatter.formatCellValue(row.getCell(0));
			if (val != null & val != "") {
				serviceIdList.add(val);
			}
		}
		RenewalsQuoteDetail rQuoteDetail = getServiceDetailIAS(serviceIdList);
		LOGGER.info("Received Data From Service Inveontory");
		Map<String, Map<String, Double>> serviceIdPriceMapping = renewalsExcelPriceMapping.constructPriceGvpn(workbook);
		if (commercial.equals('Y')) {
			LOGGER.info("Processing Data Insert From Excel");
			rQuoteDetail = compareAndInsertObject(serviceIdPriceMapping, rQuoteDetail);
		}
		LOGGER.info("Processing Quote Creation");
		QuoteResponse quoteResponse = createQuote(rQuoteDetail, rQuoteDetail.getCustomerId(), false, term, commercial);
		LOGGER.info("Cote Has Been Created QuoteId-->{}, QuoteLeId-->{}", quoteResponse.getQuoteId(),
				quoteResponse.getQuoteleId());
		rQuoteDetail.setQuoteId(quoteResponse.getQuoteId());
		rQuoteDetail.setQuoteleId(quoteResponse.getQuoteleId());
		LOGGER.info("Persting Quote To Le Attributes-->{}", updateRequest.toString());
		List<AttributeDetail> listAttributes = convertObject(rQuoteDetail.getQuoteAttributeList(), commercial, date);
		updateRequest.setAttributeDetails(listAttributes);
		updateRequest.setQuoteToLe(quoteResponse.getQuoteleId());
		QuoteDetail quoteDetail1 = persistListOfQuoteLeAttributes(updateRequest);
		LOGGER.info("Updating Site Details");
		RenewalsQuoteDetail quoteBean = updateSite(rQuoteDetail, rQuoteDetail.getCustomerId(),
				quoteResponse.getQuoteId(), "false");
		Map<Integer, String> mapper = quoteBean.getMappingDetails();
		LOGGER.info("Mapping Bean After Site Update-->{}",mapper.toString());
		QuoteToLeProductFamily quoteProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLe_Id(quoteResponse.getQuoteleId());

		List<ProductSolution> productSolutionDetails = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteProductFamily);


        LOGGER.info("Processing Price Details");
		renevalsQuoteTotalBeanFinal = updatePrice(productSolutionDetails, renewalsExcelBeanList, quoteResponse,
				serviceIdPriceMapping, mapper, term, commercial, rQuoteDetail);

		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_Id(quoteResponse.getQuoteId());

		for (QuoteToLe quoteToLe : quoteToLeList) {
			quoteToLe.setErfCusCustomerLegalEntityId(leId);
			quoteToLe.setFinalMrc(renevalsQuoteTotalBeanFinal.getQuoteTotalMrc());
			quoteToLe.setFinalNrc(renevalsQuoteTotalBeanFinal.getQuoteTotalNrc());
			quoteToLe.setFinalArc(renevalsQuoteTotalBeanFinal.getQuoteTotalArc());
			quoteToLe.setTotalTcv(renevalsQuoteTotalBeanFinal.getQuoteTotalTcv());
			quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			quoteToLeRepository.save(quoteToLe);
		}
		LOGGER.info("Processing excel Completed --> {}", rQuoteDetail.toString());
		return rQuoteDetail;
	}

	public RenewalsQuoteDetail compareAndInsertObject(Map<String, Map<String, Double>> serviceIdPriceMapping,
			RenewalsQuoteDetail quoteDetail) throws TclCommonException {
		int size = quoteDetail.getSolutions().size();
		for (int i = 0; i < size; i++) {
			List<String> listOfComponentsPrimary = new ArrayList<String>();
			List<String> listOfComponentsSecondary = new ArrayList<String>();
			List<String> listOfComponentsExcelPrimary = new ArrayList<String>();
			List<String> listOfComponentsExcelSecondary = new ArrayList<String>();
			boolean mastCostPrimary = false, mastCostSecondary = false;
			boolean brustableBandPrimary = false, brustableBandSecondary = false;
			boolean isDual = false;
			LOGGER.info("Processing Service Id-->{} ", quoteDetail.getSolutions().get(i).getServiceId());
			Map<String, Double> mappingexcel = serviceIdPriceMapping
					.get(quoteDetail.getSolutions().get(i).getServiceId());
			int mp = 0;
			int bp = 0;
			int ms = 0;
			int bs = 0;
			for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {

				if (quoteDetail.getSolutions().get(i).getComponents().get(j).getType().equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
					listOfComponentsPrimary.add(quoteDetail.getSolutions().get(i).getComponents().get(j).getName());

					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						for (RenewalsAttributeDetail attributeDetails : quoteDetail.getSolutions().get(i)
								.getComponents().get(j).getAttributes()) {
							if (attributeDetails.getName().equalsIgnoreCase(RenewalsConstant.MAST_COST)) {
								mastCostPrimary = true;

							}
						}
						mp = j;

					}

					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						for (RenewalsAttributeDetail attributeDetails : quoteDetail.getSolutions().get(i)
								.getComponents().get(j).getAttributes()) {
							if (attributeDetails.getName().equalsIgnoreCase(RenewalsConstant.BURSTABLE_BANDWIDTH)) {
								brustableBandPrimary = true;

							}
						}
						bp = j;

					}

				} else {
					isDual = true;
					listOfComponentsSecondary.add(quoteDetail.getSolutions().get(i).getComponents().get(j).getName());
					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						for (RenewalsAttributeDetail attributeDetails : quoteDetail.getSolutions().get(i)
								.getComponents().get(j).getAttributes()) {
							if (attributeDetails.getName().equalsIgnoreCase(RenewalsConstant.MAST_COST)) {
								mastCostSecondary = true;

							}
							ms = j;
						}

					}

					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						for (RenewalsAttributeDetail attributeDetails : quoteDetail.getSolutions().get(i)
								.getComponents().get(j).getAttributes()) {
							if (attributeDetails.getName().equalsIgnoreCase(RenewalsConstant.BURSTABLE_BANDWIDTH)) {
								brustableBandSecondary = true;

							}
							bs = j;
						}

					}
				}

			}

			Set<String> keys = mappingexcel.keySet();
			for (String key : keys) {
				if (key.contains(RenewalsConstant.PRIMARY_COMP_MRC) || key.contains(RenewalsConstant.PRIMARY_COMP_NRC) || key.contains(RenewalsConstant.PRIMARY_COMP_ARC)
						|| key.contains(RenewalsConstant.PRIMARY_COMP_EUC)) {
					key = key.replace(RenewalsConstant.PRIMARY_COMP_MRC, "").replace(RenewalsConstant.PRIMARY_COMP_NRC, "").replace(RenewalsConstant.PRIMARY_COMP_ARC, "").replace(RenewalsConstant.PRIMARY_COMP_EUC, "")
							.trim();
					if (!listOfComponentsExcelPrimary.contains(key)) {
						listOfComponentsExcelPrimary.add(key);
					}
				} else if (key.contains(RenewalsConstant.SECONDARY_COMP_MRC) || key.contains(RenewalsConstant.SECONDARY_COMP_NRC) || key.contains(RenewalsConstant.SECONDARY_COMP_ARC)
						|| key.contains(RenewalsConstant.SECONDARY_COMP_EUC)) {
					key = key.replace(RenewalsConstant.SECONDARY_COMP_MRC, "").replace(RenewalsConstant.SECONDARY_COMP_NRC, "").replace(RenewalsConstant.SECONDARY_COMP_ARC, "").replace(RenewalsConstant.SECONDARY_COMP_EUC, "")
							.trim();
					if (!listOfComponentsExcelSecondary.contains(key)) {
						listOfComponentsExcelSecondary.add(key);
					}
				}
			}

			if (mappingexcel.containsKey( RenewalsConstant.MAST_COST+RenewalsConstant.PRIMARY_ATTR_MRC_SPACE) || mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.PRIMARY_ATTR_NRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.PRIMARY_ATTR_ARC_SPACE) || mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.PRIMARY_ATTR_EUC_SPACE)) {
				if (!mastCostPrimary) {
					RenewalsAttributeDetail obj = new RenewalsAttributeDetail();
					obj.setName(RenewalsConstant.MAST_COST);
					obj.setValue("");
					if (!quoteDetail.getSolutions().get(i).getComponents().isEmpty() && 
							quoteDetail.getSolutions().get(i).getComponents().get(mp).getName().equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						quoteDetail.getSolutions().get(i).getComponents().get(mp).getAttributes().add(obj);
						LOGGER.info("Adding MAST_COST In Last Mile");
					} else {
						LOGGER.info("Adding Last Mile and MAST_COST In Solution");
						List<RenewalsComponentDetail> componentsList = new ArrayList<RenewalsComponentDetail>();
						List<RenewalsAttributeDetail> emptuAttribute = new ArrayList<>();
						emptuAttribute.add(obj);
						RenewalsComponentDetail component = new RenewalsComponentDetail();
						component.setName(RenewalsConstant.LAST_MILE);
						component.setType(RenewalsConstant.PRIMARY);
						component.setAttributes(emptuAttribute);
						componentsList.add(component);
						listOfComponentsPrimary.add(RenewalsConstant.LAST_MILE);
						if(quoteDetail.getSolutions().get(i).getComponents()!=null && !quoteDetail.getSolutions().get(i).getComponents().isEmpty()) {
							quoteDetail.getSolutions().get(i).getComponents().addAll(componentsList);
						}else {
						quoteDetail.getSolutions().get(i).setComponents(componentsList);
						}
					}
				}
			}

			if (mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_MRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_NRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_ARC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE)) {
				if (!brustableBandPrimary) {
					RenewalsAttributeDetail obj = new RenewalsAttributeDetail();
					obj.setName(RenewalsConstant.BURSTABLE_BANDWIDTH);
					obj.setValue("");
					if (!quoteDetail.getSolutions().get(i).getComponents().isEmpty() &&
							quoteDetail.getSolutions().get(i).getComponents().get(bp).getName().equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						quoteDetail.getSolutions().get(i).getComponents().get(bp).getAttributes().add(obj);
						LOGGER.info("Adding BURSTABLE_BANDWIDTH In Last Mile");
					} else {
						LOGGER.info("Adding INTERNET_PORT and BURSTABLE_BANDWIDTH In Solution");
						List<RenewalsComponentDetail> componentsList = new ArrayList<RenewalsComponentDetail>();
						List<RenewalsAttributeDetail> emptuAttribute = new ArrayList<>();
						emptuAttribute.add(obj);
						RenewalsComponentDetail component = new RenewalsComponentDetail();
						component.setName(RenewalsConstant.INTERNET_PORT);
						component.setType(RenewalsConstant.PRIMARY);
						component.setAttributes(emptuAttribute);
						componentsList.add(component);
						listOfComponentsSecondary.add(RenewalsConstant.INTERNET_PORT);
						if(quoteDetail.getSolutions().get(i).getComponents()!=null && !quoteDetail.getSolutions().get(i).getComponents().isEmpty()) {
							quoteDetail.getSolutions().get(i).getComponents().addAll(componentsList);
						}else {
						quoteDetail.getSolutions().get(i).setComponents(componentsList);
						}
					}
				}
			}

			if (mappingexcel.containsKey( RenewalsConstant.MAST_COST+RenewalsConstant.SECONDARY_ATTR_MRC_SPACE) || 
					mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.SECONDARY_ATTR_NRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.SECONDARY_ATTR_ARC_SPACE) || 
					mappingexcel.containsKey(RenewalsConstant.MAST_COST+RenewalsConstant.SECONDARY_ATTR_EUC_SPACE)) {
				if (!mastCostSecondary) {
					RenewalsAttributeDetail obj = new RenewalsAttributeDetail();
					obj.setName(RenewalsConstant.MAST_COST);
					obj.setValue("");
					if (!quoteDetail.getSolutions().get(i).getComponents().isEmpty() &&
							quoteDetail.getSolutions().get(i).getComponents().get(ms).getName().equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						quoteDetail.getSolutions().get(i).getComponents().get(ms).getAttributes().add(obj);
						LOGGER.info("Adding MAST_COST In Last Mile For Secondary");
					} else {
						LOGGER.info("Adding Last Mile and MAST_COST In Solution For Secondary");
						List<RenewalsComponentDetail> componentsList = new ArrayList<RenewalsComponentDetail>();
						List<RenewalsAttributeDetail> emptuAttribute = new ArrayList<>();
						emptuAttribute.add(obj);
						RenewalsComponentDetail component = new RenewalsComponentDetail();
						component.setName(RenewalsConstant.LAST_MILE);
						component.setType(RenewalsConstant.SECONDARY);
						component.setAttributes(emptuAttribute);
						componentsList.add(component);
						listOfComponentsSecondary.add(RenewalsConstant.LAST_MILE);
						if(quoteDetail.getSolutions().get(i).getComponents()!=null && !quoteDetail.getSolutions().get(i).getComponents().isEmpty()) {
							quoteDetail.getSolutions().get(i).getComponents().addAll(componentsList);
						}else {
						quoteDetail.getSolutions().get(i).setComponents(componentsList);
						}
					}
				}
			}

			if (mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_MRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_NRC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_ARC_SPACE)
					|| mappingexcel.containsKey(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE)) {
				if (!brustableBandSecondary) {   
					RenewalsAttributeDetail obj = new RenewalsAttributeDetail();
					obj.setName(RenewalsConstant.BURSTABLE_BANDWIDTH);
					obj.setValue("");
					if (!quoteDetail.getSolutions().get(i).getComponents().isEmpty() &&
							quoteDetail.getSolutions().get(i).getComponents().get(bs).getName().equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						quoteDetail.getSolutions().get(i).getComponents().get(bs).getAttributes().add(obj);
						LOGGER.info("Adding BURSTABLE_BANDWIDTH In Last Mile in Secondary");
					} else {
						LOGGER.info("Adding INTERNET_PORT and BURSTABLE_BANDWIDTH In Solution in Secondary");
						List<RenewalsComponentDetail> componentsList = new ArrayList<RenewalsComponentDetail>();
						List<RenewalsAttributeDetail> emptuAttribute = new ArrayList<>();
						emptuAttribute.add(obj);
						RenewalsComponentDetail component = new RenewalsComponentDetail();
						component.setName(RenewalsConstant.INTERNET_PORT);
						component.setType(RenewalsConstant.SECONDARY);
						component.setAttributes(emptuAttribute);
						componentsList.add(component);
						listOfComponentsSecondary.add(RenewalsConstant.INTERNET_PORT);
						if(quoteDetail.getSolutions().get(i).getComponents()!=null && !quoteDetail.getSolutions().get(i).getComponents().isEmpty()) {
							quoteDetail.getSolutions().get(i).getComponents().addAll(componentsList);
						}else {
						quoteDetail.getSolutions().get(i).setComponents(componentsList);
						}
					}
				}
			}
			listOfComponentsExcelSecondary.removeAll(listOfComponentsSecondary);
			listOfComponentsExcelPrimary.removeAll(listOfComponentsPrimary);

			if (!listOfComponentsExcelPrimary.isEmpty()) {
				LOGGER.info("Adding Components From Excel");
				for (String mapElement1 : listOfComponentsExcelPrimary) {
					LOGGER.info("Adding Components Primary From Excel, Component Name is -->{}", mapElement1.trim());
					RenewalsComponentDetail component = new RenewalsComponentDetail();
					component.setName(mapElement1.trim());
					component.setType(RenewalsConstant.PRIMARY);
					component.setAttributes(new ArrayList<RenewalsAttributeDetail>());
					quoteDetail.getSolutions().get(i).getComponents().add(component);
				}
			}
			if (!listOfComponentsExcelSecondary.isEmpty() && !quoteDetail.getSolutions().get(i).isDual()) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
			}
			if (!listOfComponentsExcelSecondary.isEmpty()) {
				for (String mapElement1 : listOfComponentsExcelPrimary) {
					LOGGER.info("Adding Components Secondary From Excel, Component Name is -->{}", mapElement1.trim());
					RenewalsComponentDetail component = new RenewalsComponentDetail();
					component.setName(mapElement1.trim());
					component.setType(RenewalsConstant.SECONDARY);
					component.setAttributes(new ArrayList<RenewalsAttributeDetail>());
					quoteDetail.getSolutions().get(i).getComponents().add(component);
				}
			}

		}		
		return quoteDetail;
	}

	public RenevalsQuoteTotalBean updatePrice(List<ProductSolution> productSolutionDetails,
			List<RenewalsExcelBean> renewalsExcelBeanList, QuoteResponse quoteResponse,
			Map<String, Map<String, Double>> serviceIdPriceMapping, Map<Integer, String> mapper, Integer term,
			Character commercial, RenewalsQuoteDetail rQuoteDetail) {
		RenevalsQuoteTotalBean renevalsQuoteTotalBeanFinal = new RenevalsQuoteTotalBean();
		for (ProductSolution procsolution : productSolutionDetails) {
			RenevalsQuoteTotalBean renevalsQuoteTotalBean = updatePrices(renewalsExcelBeanList, procsolution.getId(),
					quoteResponse.getQuoteId(), serviceIdPriceMapping, mapper, term, commercial, rQuoteDetail);
			renevalsQuoteTotalBeanFinal.setQuoteTotalMrc(
					renevalsQuoteTotalBeanFinal.getQuoteTotalMrc() + renevalsQuoteTotalBean.getQuoteTotalMrc());
			renevalsQuoteTotalBeanFinal.setQuoteTotalNrc(
					renevalsQuoteTotalBeanFinal.getQuoteTotalNrc() + renevalsQuoteTotalBean.getQuoteTotalNrc());
			renevalsQuoteTotalBeanFinal.setQuoteTotalArc(
					renevalsQuoteTotalBeanFinal.getQuoteTotalArc() + renevalsQuoteTotalBean.getQuoteTotalArc());
			renevalsQuoteTotalBeanFinal.setQuoteTotalTcv(
					renevalsQuoteTotalBeanFinal.getQuoteTotalTcv() + renevalsQuoteTotalBean.getQuoteTotalTcv());
		}

		/*
		 * renevalsQuoteTotalBeanFinal.setQuoteTotalTcv(
		 * renevalsQuoteTotalBeanFinal.getQuoteTotalTcv() +
		 * renevalsQuoteTotalBeanFinal.getQuoteTotalNrc());
		 */
		return renevalsQuoteTotalBeanFinal;
	}

	public List<AttributeDetail> convertObject(List<RenewalsAttributeDetail> renewalsList, Character commercial, String date) {
		List<AttributeDetail> attributeDetailList = new ArrayList<AttributeDetail>();
		boolean billingCurrency = false;
		boolean paymentCurrency = false;
		for (RenewalsAttributeDetail renewalsAttributeDetail : renewalsList) {
			AttributeDetail attributeDetail = new AttributeDetail();
			BeanUtils.copyProperties(renewalsAttributeDetail, attributeDetail);
			attributeDetailList.add(attributeDetail);
			if (attributeDetail.getName().equalsIgnoreCase(LeAttributesConstants.BILLING_CURRENCY)) {
				billingCurrency = true;
			}
			if (attributeDetail.getName().equalsIgnoreCase(LeAttributesConstants.PAYMENT_CURRENCY)) {
				paymentCurrency = true;
			}
		}

		if (!billingCurrency) {
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(LeAttributesConstants.BILLING_CURRENCY);
			attributeDetail.setValue("INR");
			attributeDetailList.add(attributeDetail);
		}
		if (!paymentCurrency) {
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(LeAttributesConstants.PAYMENT_CURRENCY);
			attributeDetail.setValue("INR");
			attributeDetailList.add(attributeDetail);
		}
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName(RenewalsConstant.IS_COOMERCIAL);
		if (commercial.equals(RenewalsConstant.Y)) {
			attributeDetail.setValue(RenewalsConstant.Y.toString());
		} else {
			attributeDetail.setValue(RenewalsConstant.N.toString());
		}
		attributeDetailList.add(attributeDetail);
		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setName(LeAttributesConstants.EFFECTIVE_DATE);
		attributeDetail1.setValue(date);
		attributeDetailList.add(attributeDetail1);
		return attributeDetailList;

	}

	public QuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				if (attribute.getName() != null) {
					MstOmsAttribute mstOmsAttribute = null;
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(attribute.getName(), (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						mstOmsAttribute = mstOmsAttributeList.get(0);
					}
					if (mstOmsAttribute == null) {
						mstOmsAttribute = new MstOmsAttribute();
						mstOmsAttribute.setCreatedBy(user.getUsername());
						mstOmsAttribute.setCreatedTime(new Date());
						mstOmsAttribute.setIsActive((byte) 1);
						mstOmsAttribute.setName(attribute.getName());
						mstOmsAttribute.setDescription("");
						mstOmsAttributeRepository.save(mstOmsAttribute);

					}

					saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	protected void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	private void saveLegalEntityAttributes(QuoteToLe quoteToLe, AttributeDetail attribute,
			MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	public QuoteDetail convertQuoteDetail(RenewalsQuoteDetail rQuoteDetails) {
		List<SolutionDetail> solutionsDetails = new ArrayList<SolutionDetail>();
		for (RenewalsSolutionDetail solutions : rQuoteDetails.getSolutions()) {
			List<ComponentDetail> componentDetailList = new ArrayList<ComponentDetail>();
			for (RenewalsComponentDetail components : solutions.getComponents()) {
				List<AttributeDetail> attributeList = new ArrayList<AttributeDetail>();
				for (RenewalsAttributeDetail attributes : components.getAttributes()) {
					AttributeDetail attribute = new AttributeDetail();
					BeanUtils.copyProperties(attributes, attribute);
					attributeList.add(attribute);
				}
				ComponentDetail componentDetail = new ComponentDetail();
				BeanUtils.copyProperties(components, componentDetail);
				componentDetail.setAttributes(attributeList);
				componentDetailList.add(componentDetail);
			}
			SolutionDetail solutionDetails = new SolutionDetail();
			BeanUtils.copyProperties(solutions, solutionDetails);
			solutionDetails.setComponents(componentDetailList);
			solutionsDetails.add(solutionDetails);
		}

		List<Site> siteDetails = new ArrayList<Site>();
		for (RenewalsSite sites : rQuoteDetails.getSite()) {
			List<SiteDetail> siteDetailsList = new ArrayList<SiteDetail>();
			for (RenewalsSiteDetail renewalsSiteDetail : sites.getSite()) {
				SiteDetail siteDetail = new SiteDetail();
				BeanUtils.copyProperties(renewalsSiteDetail, siteDetail);
				siteDetailsList.add(siteDetail);
			}
			Site site = new Site();
			BeanUtils.copyProperties(sites, site);
			site.setSite(siteDetailsList);
			siteDetails.add(site);
		}

		QuoteDetail quoteDetail = new QuoteDetail();
		quoteDetail.setProductName(rQuoteDetails.getProductName());
		quoteDetail.setSolutions(solutionsDetails);
		quoteDetail.setSite(siteDetails);
		return quoteDetail;
	}

	public RenevalsQuoteTotalBean updatePrices(List<RenewalsExcelBean> renewalsExcelBeanList, Integer productSolutionId,
			Integer quoteId, Map<String, Map<String, Double>> serviceIdPriceMapping, Map<Integer, String> mapper,
			int contractTerm, Character commercial, RenewalsQuoteDetail rQuoteDetail) {

		List<QuoteIllSite> quoteIllSiteList = illSiteRepository.findByProductSolution_Id(productSolutionId);
		RenevalsQuoteTotalBean renevalsQuoteTotalBean = new RenevalsQuoteTotalBean();
		Double quoteTotalMrc = 0D;
		Double quoteTotalNrc = 0D;
		Double quoteTotalArc = 0D;
		Double quoteTotalTcv = 0D;
		RenewalsPriceBean exsistingPrice = new RenewalsPriceBean();
		for (QuoteIllSite quoteIllSite : quoteIllSiteList) {
			Map<String, Double> mappingexcel = serviceIdPriceMapping.get(mapper.get(quoteIllSite.getId()));
			LOGGER.info("Processing Price serviceId -->{}, illSite Id -->{}", mapper.get(quoteIllSite.getId()), quoteIllSite.getId());
			Double totalMrc = 0D;
			Double totalNrc = 0D;
			Double totalArc = 0D;
			// boolean updateAttributePrice = false;
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceId(quoteIllSite.getId());
			for (QuoteProductComponent quoteProductComponent : quoteProductComponentList) {
				// updateAttributePrice = false;
				Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findById(quoteProductComponent.getMstProductComponent().getId());
				MstProductComponent mstprd = mstProductComponent.get();
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(RenewalsConstant.GVPN, (byte) 1);
				QuotePrice quotePrice = new QuotePrice();
				quotePrice.setQuoteId(quoteId);
				quotePrice.setMstProductFamily(mstProductFamily);
				quotePrice.setReferenceName(RenewalsConstant.COMPONENTS);
				quotePrice.setReferenceId(quoteProductComponent.getId().toString());
				LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}",
						mstprd.getName());

				if (commercial.equals(RenewalsConstant.Y)) {
					quotePrice = constructComponentPrice(mappingexcel, mstprd, quotePrice,
							quoteProductComponent.getType());
					totalMrc = quotePrice.getEffectiveMrc() + totalMrc;
					totalNrc = quotePrice.getEffectiveNrc() + totalNrc;
					totalArc = quotePrice.getEffectiveArc() + totalArc;
					if (quotePrice.getEffectiveMrc() != 0D && quotePrice.getEffectiveMrc() != 0D
							&& quotePrice.getEffectiveMrc() != 0D) {
						// updateAttributePrice = true;
					}
				} else {
					quotePrice.setEffectiveMrc(0D);
					quotePrice.setEffectiveNrc(0D);
					quotePrice.setEffectiveArc(0D);
				}

				quotePriceRepository.save(quotePrice);

				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());

				for (QuoteProductComponentsAttributeValue quotePrdAttributeValue : quoteProductComponentsAttributeValueList) {

					Optional<ProductAttributeMaster> productAttributeMaster = productAttributeMasterRepository
							.findById(quotePrdAttributeValue.getProductAttributeMaster().getId());
					ProductAttributeMaster prdAtr = productAttributeMaster.get();
					QuotePrice quotePriceA = new QuotePrice();
					quotePriceA.setQuoteId(quoteId);
					quotePriceA.setMstProductFamily(mstProductFamily);
					quotePriceA.setReferenceName(RenewalsConstant.ATTRIBUTES);
					quotePriceA.setReferenceId(quotePrdAttributeValue.getId().toString());

					if (commercial.equals(RenewalsConstant.Y) /* && updateAttributePrice */) {

						quotePriceA = constructAtributePrice(mappingexcel, prdAtr, quotePriceA,
								quoteProductComponent.getType());

						totalMrc = quotePriceA.getEffectiveMrc() + totalMrc;

						totalNrc = quotePriceA.getEffectiveNrc() + totalNrc;

						totalArc = quotePriceA.getEffectiveArc() + totalArc;

					} else {
						quotePriceA.setEffectiveMrc(0D);
						quotePriceA.setEffectiveNrc(0D);
						quotePriceA.setEffectiveArc(0D);
					}

					quotePriceRepository.save(quotePriceA);
				}
			}
			if (commercial.equals(RenewalsConstant.Y)) {
				quoteIllSite.setMrc(totalMrc);
				quoteIllSite.setNrc(totalNrc);
				quoteIllSite.setArc(totalArc);
				//quoteIllSite.setTcv((totalMrc * contractTerm) + totalNrc);
				quoteIllSite.setTcv(((totalArc/12) * contractTerm) + totalNrc);
			} else {
				exsistingPrice = rQuoteDetail.getRenewalsPriceBean().get(mapper.get(quoteIllSite.getId()));
				quoteIllSite.setMrc(exsistingPrice.getMrc() != null ? exsistingPrice.getMrc() : 0D);
				quoteIllSite.setNrc(exsistingPrice.getNrc() != null ? exsistingPrice.getNrc() : 0D);
				quoteIllSite.setArc(exsistingPrice.getArc() != null ? exsistingPrice.getArc() : 0D);
				//quoteIllSite.setTcv((exsistingPrice.getMrc() * contractTerm) + quoteIllSite.getNrc());
				quoteIllSite.setTcv(( (exsistingPrice.getArc()/12) * contractTerm) + quoteIllSite.getNrc());
			}
			illSiteRepository.save(quoteIllSite);

			if (totalMrc != null)
				quoteTotalMrc = quoteTotalMrc + totalMrc;
			if (totalNrc != null)
				quoteTotalNrc = quoteTotalNrc + totalNrc;
			if (totalArc != null)
				quoteTotalArc = quoteTotalArc + totalArc;
			quoteTotalTcv = quoteTotalTcv + quoteIllSite.getTcv();
		}
		if (commercial.equals('Y')) {
			renevalsQuoteTotalBean.setQuoteTotalMrc(quoteTotalMrc);
			renevalsQuoteTotalBean.setQuoteTotalNrc(quoteTotalNrc);
			renevalsQuoteTotalBean.setQuoteTotalArc(quoteTotalArc);
			renevalsQuoteTotalBean.setQuoteTotalTcv(quoteTotalTcv);
		} else {
			renevalsQuoteTotalBean.setQuoteTotalMrc(exsistingPrice.getMrc() != null ? exsistingPrice.getMrc() : 0D);
			renevalsQuoteTotalBean.setQuoteTotalNrc(exsistingPrice.getNrc() != null ? exsistingPrice.getNrc() : 0D);
			renevalsQuoteTotalBean.setQuoteTotalArc(exsistingPrice.getArc() != null ? exsistingPrice.getArc() : 0D);
			renevalsQuoteTotalBean.setQuoteTotalTcv(quoteTotalTcv);
		}
		return renevalsQuoteTotalBean;
	}

	public QuotePrice constructComponentPrice(Map<String, Double> mappingexcel, MstProductComponent mstprd,
			QuotePrice quotePrice, String type) {
		Double mrc;
		Double nrc;
		Double arc;
		if (type == null  || type=="" || type.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
			mrc = mappingexcel.get(mstprd.getName() + RenewalsConstant.PRIMARY_COMP_MRC_SPACE);
			nrc = mappingexcel.get(mstprd.getName() + RenewalsConstant.PRIMARY_COMP_NRC_SPACE);
			arc = mappingexcel.get(mstprd.getName() + RenewalsConstant.PRIMARY_COMP_ARC_SPACE);
			LOGGER.info("Primary Component Name-->{ Mrc --> {}, NRC --> {}, ARC -->{} }", mstprd.getName(), mrc, nrc, arc);
		} else {
			mrc = mappingexcel.get(mstprd.getName() + RenewalsConstant.SECONDARY_COMP_MRC_SPACE);
			nrc = mappingexcel.get(mstprd.getName() + RenewalsConstant.SECONDARY_COMP_NRC_SPACE);
			arc = mappingexcel.get(mstprd.getName() + RenewalsConstant.SECONDARY_COMP_ARC_SPACE);
			LOGGER.info("Secondary Component Name-->{ Mrc --> {}, NRC --> {}, ARC -->{} }", mstprd.getName(), mrc, nrc, arc);
		}

		if (mrc != null) {
			quotePrice.setEffectiveMrc(mrc);
		} else {
			quotePrice.setEffectiveMrc(0D);
		}
		if (nrc != null) {
			quotePrice.setEffectiveNrc(nrc);

		} else {
			quotePrice.setEffectiveNrc(0D);
		}
		if (arc != null) {
			quotePrice.setEffectiveArc(arc);

		} else {
			quotePrice.setEffectiveArc(0D);
		}
		return quotePrice;
	}

	public QuotePrice constructAtributePrice(Map<String, Double> mappingexcel, ProductAttributeMaster prdAtr,
			QuotePrice quotePrice, String type) {
		Double mrcA;
		Double nrcA;
		Double arcA;
		Double eucA;
		if (type == null  || type=="" || type.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
			mrcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.PRIMARY_ATTR_MRC_SPACE);
			nrcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.PRIMARY_ATTR_NRC_SPACE);
			arcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.PRIMARY_ATTR_ARC_SPACE);
			eucA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.PRIMARY_ATTR_EUC_SPACE);
			LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{}, EUC-->{} }", prdAtr.getName(), mrcA, nrcA, arcA, eucA);
		} else {
			mrcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.SECONDARY_ATTR_MRC_SPACE);
			nrcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.SECONDARY_ATTR_NRC_SPACE);
			arcA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.SECONDARY_ATTR_ARC_SPACE);
			eucA = mappingexcel.get(prdAtr.getName() + RenewalsConstant.SECONDARY_ATTR_EUC_SPACE);
			LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{}, EUC-->{} }", prdAtr.getName(), mrcA, nrcA, arcA, eucA);
		}
		if (mrcA != null) {
			quotePrice.setEffectiveMrc(mrcA);
		} else {
			quotePrice.setEffectiveMrc(0D);
		}
		if (nrcA != null) {
			quotePrice.setEffectiveNrc(nrcA);
		} else {
			quotePrice.setEffectiveNrc(0D);
		}
		if (arcA != null) {
			quotePrice.setEffectiveArc(arcA);
		} else {
			quotePrice.setEffectiveArc(0D);
		}

		if (prdAtr.getName().equalsIgnoreCase(RenewalsConstant.MAST_COST)) {

			if (eucA != null) {
				quotePrice.setEffectiveUsagePrice(eucA);
				quotePrice.setEffectiveNrc(eucA);
			} else {
				quotePrice.setEffectiveUsagePrice(0D);
				quotePrice.setEffectiveNrc(0D);
			}
		}
		if (prdAtr.getName().equalsIgnoreCase(RenewalsConstant.BURSTABLE_BANDWIDTH)) {

			if (eucA != null) {
				quotePrice.setEffectiveUsagePrice(eucA);
			} else {
				quotePrice.setEffectiveUsagePrice(0D);
			}
		}

		return quotePrice;
	}

	public RenewalsQuoteDetail updateSite(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId,
			String isColo) throws TclCommonException {
		QuoteBean quoteBean = null;
		RenewalsQuoteDetail renewalsQuoteDetail = new RenewalsQuoteDetail();
		Map<Integer, String> mappingDetail = new HashMap<Integer, String>();
		try {
			LOGGER.info("Customer Id received is {}", erfCustomerId);
			LOGGER.info("quote details received {} , for quote id {}", quoteDetail.toString(), quoteId);
			validateSiteInformation(quoteDetail);
			User user = getUserId(Utils.getSource());
			List<RenewalsSite> sites = quoteDetail.getSite();
			MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			if (quoteToLe.isPresent()) {
				for (RenewalsSite site : sites) {
					QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
					LOGGER.info("product family is {}", quoteToLeProductFamily.toString());
					String productOfferingName = site.getOfferingName();
					LOGGER.info("Offering name is {}", productOfferingName);
					Map<Integer, String> mappingDetails = processSiteDetail(user, productFamily, quoteToLeProductFamily,
							site, productOfferingName, quoteToLe.get().getQuote(), isColo, quoteToLe.get().getId());
					if (!mappingDetails.isEmpty()) {
						mappingDetail.putAll(mappingDetails);
					}
				}
				quoteDetail.setQuoteId(quoteId);
			}
			if (quoteToLe.isPresent()) {
				if (quoteToLe.get().getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
					quoteToLe.get().setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
					quoteToLeRepository.save(quoteToLe.get());
				}
			}
			quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false, null, null);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		BeanUtils.copyProperties(quoteBean, renewalsQuoteDetail);
		if (!mappingDetail.isEmpty()) {
			renewalsQuoteDetail.setMappingDetails(mappingDetail);
		}
		return renewalsQuoteDetail;
	}

	protected Quote getQuote(Integer quoteId) throws TclCommonException {

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}

		return quote;
	}

	public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		QuoteBean response = null;
		try {
			LOGGER.info("Inside Get Quote for quoteId {}", quoteId);
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			LOGGER.info("Get quote request is to fetch with non feasibile Items status {}", isFeasibleSites);
			Quote quote = getQuote(quoteId);
			LOGGER.info("Quote code for the fetched quote {} is {}", quoteId, quote.getQuoteCode());
			response = constructQuote(quote, isFeasibleSites, isSiteProperitiesRequired, siteId, manualFeasibility);

			/*
			 * Optional<QuoteToLe> quoteToLe1 = quote.getQuoteToLes().stream().findFirst()
			 * .filter(quoteToLe ->
			 * MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()));
			 */

			/*
			 * if (quoteToLe1.isPresent()) {
			 * response.setIsAmended(quoteToLe1.get().getIsAmended());
			 * response.setQuoteType(quoteToLe1.get().getQuoteType());
			 * response.setQuoteCategory(quoteToLe1.get().getQuoteCategory()); if
			 * (Objects.nonNull(quoteToLe1.get().getIsMultiCircuit())&&quoteToLe1.get().
			 * getIsMultiCircuit() == 1) response.setIsMultiCircuit(true); List<String>
			 * multiCircuitChangeBandwidthFlag = new ArrayList<>();
			 * List<QuoteIllSiteToService> quoteIllSiteToServices =
			 * quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe1.get().getId());
			 * quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> { if
			 * (Objects.nonNull(quoteIllSiteToService) &&
			 * CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
			 * multiCircuitChangeBandwidthFlag.add("true"); else
			 * multiCircuitChangeBandwidthFlag.add("false"); }); if
			 * (multiCircuitChangeBandwidthFlag.contains("false")) {
			 * response.setIsMulticircuitBandwidthChangeFlag(false); } else
			 * response.setIsMulticircuitBandwidthChangeFlag(true); }
			 */
			List<QuoteToLe> quoteLeList = quoteToLeRepository.findByQuote_Id(quoteId);

			Optional<QuoteToLe> quoteToLer = quoteLeList.stream().findFirst()
					.filter(quoteToLe -> "RENEWALS".equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLer.isPresent()) {
				response.setQuoteType(quoteToLer.get().getQuoteType());
				response.setIsCommercialChanges(findIsCommercial(quoteToLer).charAt(0));
				response.setIsMultiCircuit(quoteToLer.get().getIsMultiCircuit() == 1 ? true : false);
			}

			extractQuoteAccessPermission(response, quoteLeList, quote);
			Optional<User> user = userRepository.findById(quote.getCreatedBy());
			if (user.isPresent()) {
				response.setQuoteCreatedUserType(user.get().getUserType());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	public String findIsCommercial(Optional<QuoteToLe> quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe.get());
		Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
				.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
						.equalsIgnoreCase(LeAttributesConstants.IS_COOMERCIAL))
				.findFirst();
		return customerCodeLeVal.get().getAttributeValue();
	}

	private void extractQuoteAccessPermission(QuoteBean response, List<QuoteToLe> quoteLeList, Quote quote) {
		for (QuoteToLe quoteLe : quoteLeList) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilyList = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteLe.getId());
			for (QuoteToLeProductFamily quoteLeFamily : quoteToLeProductFamilyList) {
				List<QuoteAccessPermission> quoteAccessPermisions = quoteAccessPermissionRepository
						.findByProductFamilyIdAndTypeAndRefId(quoteLeFamily.getMstProductFamily().getId(), "QUOTE",
								quote.getQuoteCode());
				response.setQuoteAccess(QuoteAccess.FULL.toString());
				for (QuoteAccessPermission quoteAccessPermission : quoteAccessPermisions) {
					User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
					if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
						if (quoteAccessPermission.getIsCustomerView() != null
								&& quoteAccessPermission.getIsCustomerView() == CommonConstants.BACTIVE) {
							response.setQuoteAccess(QuoteAccess.FULL.toString());
						} else if (quoteAccessPermission.getIsCustomerView() != null
								&& quoteAccessPermission.getIsCustomerView() == CommonConstants.BDEACTIVATE) {
							response.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
						} else if (quoteAccessPermission.getIsCustomerView() != null
								&& quoteAccessPermission.getIsCustomerView() == CommonConstants.BTEN) {
							response.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
						}

					} else if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						if (quoteAccessPermission.getIsSalesView() != null
								&& quoteAccessPermission.getIsSalesView() == CommonConstants.BACTIVE) {
							response.setQuoteAccess(QuoteAccess.FULL.toString());
						} else if (quoteAccessPermission.getIsSalesView() != null
								&& quoteAccessPermission.getIsSalesView() == CommonConstants.BDEACTIVATE) {
							response.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
						} else if (quoteAccessPermission.getIsSalesView() != null
								&& quoteAccessPermission.getIsSalesView() == CommonConstants.BTEN) {
							response.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
						}
					} else {
						response.setQuoteAccess(QuoteAccess.FULL.toString());
					}
				}
			}
		}
	}

	protected void validateGetQuoteDetail(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	protected void validateSiteInformation(RenewalsQuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null) || quoteDetail.getSite() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	protected Map<Integer, String> processSiteDetail(User user, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, RenewalsSite site, String productOfferingName, Quote quote,
			String isColo, Integer quoteToLeId) throws TclCommonException {
		Map<Integer, String> mappingDetails = new HashMap<Integer, String>();
		try {
			MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName, user);
			LOGGER.info("Product offering {}", productOfferng);
			ProductSolutionSiLink productSolutionSiLink = productSolutionSiLinkRepository
					.findFirstByServiceIdAndQuoteToLeId(site.getServiceId(), quoteToLeId);
			Optional<ProductSolution> productSolution = productSolutionRepository
					.findById(productSolutionSiLink.getProductSolutionId());
			/*
			 * .findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily,
			 * productOfferng);
			 */
			LOGGER.info("Product solution {}", productSolution.toString());
			mappingDetails = constructIllSites(productSolution.get(), user, site, productFamily, quote, isColo);

		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return mappingDetails;
	}

	private Map<Integer, String> constructIllSites(ProductSolution productSolution, User user, RenewalsSite site,
			MstProductFamily productFamily, Quote quote, String isColo) throws TclCommonException {
		LOGGER.info("inside constructIllSites method , profile data is {}",
				productSolution.getProductProfileData().toString());
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);
		Map<Integer, String> mapingDetails = new HashMap<Integer, String>();
		List<RenewalsSiteDetail> siteInp = site.getSite();
		for (RenewalsSiteDetail siteDetail : siteInp) {
			if (siteDetail.getSiteId() == null) {
				QuoteIllSite illSite = new QuoteIllSite();
				illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
				illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
				illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
				illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
				illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 1);
				illSite.setIsTaskTriggered(0);
				illSite.setMfTaskTriggered(0);
				illSite.setCommercialRejectionStatus("0");
				illSite.setErfServiceInventoryTpsServiceId(site.getServiceId());
				// illSite.setFpStatus("FB");
				if (Objects.nonNull(isColo) && "True".equalsIgnoreCase(isColo)) {
					illSite.setIsColo((byte) 1);
				} else {
					illSite.setIsColo((byte) 0);
				}
				illSiteRepository.save(illSite);
				siteDetail.setSiteId(illSite.getId());
				mapingDetails.put(illSite.getId(), site.getServiceId());
				for (ComponentDetail componentDetail : soDetail.getComponents()) {
					processProductComponent(productFamily, illSite, componentDetail, user, isColo);
				}
				// Initializing siteProperty
				MstProductComponent sitePropComp = getMstProperties(user);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceNameAndMstProductComponent(illSite.getId(),
								QuoteConstants.GVPN_SITES.toString(), sitePropComp);
				if (quoteProductComponents.isEmpty()) {
					LOGGER.info("Entering saving quote product component");
					QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
					quoteProductComponent.setMstProductComponent(sitePropComp);
					quoteProductComponent.setMstProductFamily(productFamily);
					quoteProductComponent.setReferenceId(illSite.getId());
					quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
					quoteProductComponentRepository.save(quoteProductComponent);
					LOGGER.info("Saved Quote Product Component");
				}

				/*
				 * QuoteToLe quoteToLe = quote.getQuoteToLes().stream().findFirst().get();
				 * if(quoteToLe.getQuoteType().equalsIgnoreCase(RenewalsConstant.RENEWALS)) {
				 * List<QuoteIllSiteToService> quoteSiteToService =
				 * quoteIllSiteToServiceRepository.
				 * findByErfServiceInventoryTpsServiceIdInAndQuoteToLe(siteDetail.
				 * getErfServiceInventoryTpsServiceId(), quoteToLe); if(quoteSiteToService !=
				 * null && !quoteSiteToService.isEmpty()) {
				 * quoteSiteToService.stream().forEach(quoteSiteToServiceRecord -> { LOGGER.
				 * info("Updateing quoteIllSite data in QuoteIllSiteToService for site id {} ",
				 * illSite.getId()); quoteSiteToServiceRecord.setQuoteIllSite(illSite);
				 * quoteIllSiteToServiceRepository.save(quoteSiteToServiceRecord); }); } }
				 */

			} else {
				QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
				if (illSiteEntity != null) {
					illSiteEntity.setProductSolution(productSolution);
					illSiteRepository.save(illSiteEntity);
					removeComponentsAndAttr(illSiteEntity.getId());
					for (ComponentDetail componentDetail : soDetail.getComponents()) {
						processProductComponent(productFamily, illSiteEntity, componentDetail, user, isColo);
					}
				}
			}
		}
		return mapingDetails;

	}

	public RenewalsQuoteDetail getServiceDetailIAS(List<String> tpsId) throws TclCommonException {

		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", tpsId);
		String completeQuoteString = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, tpsId.toString());
		if (completeQuoteString.isEmpty()) {
			throw new TclCommonRuntimeException(RenewalsConstant.SI_NO_DATA_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (Objects.nonNull(completeQuoteString)) {
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}",
					completeQuoteString, tpsId);
		}
		RenewalsQuoteDetail completeQuote = (RenewalsQuoteDetail) Utils.convertJsonToObject(completeQuoteString,
				RenewalsQuoteDetail.class);

		if (completeQuote.getSolutions().isEmpty() && completeQuote.getOrderLeIds().isEmpty()
				&& completeQuote.getSite().isEmpty()) {
			throw new TclCommonRuntimeException(RenewalsConstant.SI_NO_DATA_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}

		completeQuote = desidePrimaryOrSecondary(completeQuote);
		return completeQuote;
	}

	public RenewalsQuoteDetail desidePrimaryOrSecondary(RenewalsQuoteDetail quoteDetail) {
		for (int i = 0; i < quoteDetail.getSolutions().size(); i++) {
			for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {
				quoteDetail.getSolutions().get(i)
						.setOfferingName(getOfferingName(quoteDetail.getSolutions().get(i).getComponents()));
				HashMap<String, Integer> map = new HashMap<String, Integer>();

				if (map.containsKey(quoteDetail.getSolutions().get(i).getComponents().get(j).getName())) {
					quoteDetail.getSolutions().get(i).getComponents().get(j).setType(RenewalsConstant.SECONDARY);
				} else {
					quoteDetail.getSolutions().get(i).getComponents().get(j).setType(RenewalsConstant.PRIMARY);
					map.put(quoteDetail.getSolutions().get(i).getComponents().get(j).getName(), 1);
				}

			}
		}
		return quoteDetail;
	}

	public String getOfferingName(List<RenewalsComponentDetail> listOfComponents) {
		boolean resiliencyPresent = false;
		boolean cpeManged = false;
		for (RenewalsComponentDetail component : listOfComponents) {

			for (RenewalsAttributeDetail attribute : component.getAttributes()) {
				if (attribute.getName().equalsIgnoreCase("Resiliency")
						&& attribute.getValue().equalsIgnoreCase("YES")) {
					resiliencyPresent = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE")
						&& !attribute.getValue().equalsIgnoreCase("customer provided")) {
					cpeManged = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE Management Type")
						&& attribute.getName().equalsIgnoreCase("proactive monitoring")) {
					cpeManged = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE Management Type")
						&& attribute.getName().equalsIgnoreCase("configuration management")) {
					cpeManged = true;
				}
			}

		}
		if (resiliencyPresent && cpeManged) {
			return "Dual managed GVPN";
		} else if (!resiliencyPresent && cpeManged) {
			return "Single managed GVPN";
		} else if (resiliencyPresent && !cpeManged) {
			return "Dual Unmanaged GVPN";
		} else if (!resiliencyPresent && !cpeManged) {
			return "Single Unmanaged GVPN";
		} else {
			return "Custom Configuration";
		}

	}

	public QuoteResponse createQuote(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId, Boolean ns, Integer term,
			Character isCommercial) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			if (ns == null) {
				ns = false;
			}
			validateQuoteDetail(quoteDetail);// validating the input for create Quote
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user, ns, term, isCommercial);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				if (quoteDetail.getQuoteId() == null && Objects.isNull(quoteDetail.getEngagementOptyId())) {
					// Triggering Sfdc Creation
//					omsSfdcService.processCreateRenewalsOpty(quoteTole, quoteDetail.getProductName(), isCommercial);
				}
			}
			processQuoteAccessPermissions(user, quoteTole);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private void processQuoteAccessPermissions(User user, QuoteToLe quoteTole) {
		Integer prodFamilyId = null;
		List<QuoteToLeProductFamily> quoteToLeProductFamilys = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteTole.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilys) {
			prodFamilyId = quoteToLeProductFamily.getMstProductFamily().getId();
		}
		QuoteAccessPermission quoteAccessPermission = null;
		List<QuoteAccessPermission> quoteAccessPermissions = quoteAccessPermissionRepository
				.findByProductFamilyIdAndTypeAndRefId(prodFamilyId, "QUOTE", quoteTole.getQuote().getQuoteCode());
		if (!quoteAccessPermissions.isEmpty()) {
			quoteAccessPermission = quoteAccessPermissions.get(0);
		} else {
			quoteAccessPermission = new QuoteAccessPermission();
		}
		Quote quote=quoteTole.getQuote();
		quoteAccessPermission.setCreatedBy(Utils.getSource());
		quoteAccessPermission.setCreatedTime(new Date());
		quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
		quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
		quote.setIsCustomerView(CommonConstants.BACTIVE);
		quote.setIsSalesView(CommonConstants.BACTIVE);
		quoteAccessPermission.setProductFamilyId(prodFamilyId);
		quoteAccessPermission.setRefId(quoteTole.getQuote().getQuoteCode());
		quoteAccessPermission.setType(RenewalsConstant.QUOTE);
		quoteAccessPermission.setUpdatedBy(Utils.getSource());
		quoteAccessPermission.setUpdatedTime(new Date());
		if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
			quote.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quote.setIsSalesView(CommonConstants.BACTIVE);
		} else if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BDEACTIVATE);
			quote.setIsCustomerView(CommonConstants.BACTIVE);
			quote.setIsSalesView(CommonConstants.BDEACTIVATE);
			
		}
		quoteRepository.save(quote);
		quoteAccessPermissionRepository.save(quoteAccessPermission);
	}

	public void validateQuoteDetail(RenewalsQuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {// TODO validate the inputs for quote
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	private Customer getCustomerId(Integer erfCustomerId) throws TclCommonException {
		Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(erfCustomerId, (byte) 1);
		if (customer == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	private Quote constructQuote(Customer customer, Integer userId, String productName, String engagementOptyId,
			String quoteCode, Boolean ns, Integer term) {
		Quote quoteExisting = quoteRepository.findByQuoteCode(quoteCode);
		Quote quote = new Quote();
		if (quoteExisting != null) {
			quote = quoteExisting;
		}
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setNsQuote(ns ? CommonConstants.Y : CommonConstants.N);
		quote.setQuoteCode(null != engagementOptyId ? quoteCode : Utils.generateRefId(productName.toUpperCase()));
		quote.setEngagementOptyId(engagementOptyId);
		quote.setTermInMonths(term);
		return quote;
	}

	protected QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		quoteDto.setNsQuote(quote.getNsQuote() != null
				? (quote.getNsQuote().equals(CommonConstants.Y) ? CommonConstants.Y : CommonConstants.N)
				: CommonConstants.N);
		Opportunity optyentity = opportunityRepository.findByUuid(quote.getQuoteCode());
		if (optyentity != null) {
			quoteDto.setOpportunityId(optyentity.getId() + "");
		}
		List<QuoteToLe> quoteToLe = new ArrayList<QuoteToLe>();
		quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId());
		List<CommercialQuoteAudit> audit = commercialQuoteAuditRepository.findByQuoteId(quote.getId());
		if (audit.isEmpty() || audit.size() == 0) {
			quoteDto.setIsInitialCommercialTrigger(true);
		}
		LOGGER.info("quote to le is fetched for quote id {}", quote.getId());
		if (quoteToLe.size() != 0) {
			quoteDto.setQuoteStatus(quoteToLe.get(0).getCommercialStatus());
			quoteDto.setQuoteRejectionComment(quoteToLe.get(0).getQuoteRejectionComment());
			if (quoteToLe.get(0).getCommercialQuoteRejectionStatus() != null) {
				if (quoteToLe.get(0).getCommercialQuoteRejectionStatus().equalsIgnoreCase("1")) {
					quoteDto.setQuoteRejectionStatus(true);
				} else {
					quoteDto.setQuoteRejectionStatus(false);
				}
				LOGGER.info(" quote Commercial Status and rejection status and comments  is set as  {}",
						quoteToLe.get(0).getCommercialStatus() + ":"
								+ quoteToLe.get(0).getCommercialQuoteRejectionStatus() + ":"
								+ quoteToLe.get(0).getCommercialQuoteRejectionStatus());
			}
			LOGGER.info(" getIsCommercialTriggered" + quoteToLe.get(0).getIsCommercialTriggered());
			if (quoteToLe.get(0).getIsCommercialTriggered() == null) {
				quoteDto.setIsCommercialTriggered(false);
			} else {
				quoteDto.setIsCommercialTriggered(true);
			}

		}
		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProperitiesRequired, siteId,
				quoteToLe, manualFeasibility));

		OrderConfirmationAudit auditEntity = orderConfirmationAuditRepository
				.findByOrderRefUuid(quoteDto.getQuoteCode());
		if (auditEntity != null) {
			quoteDto.setPublicIp(getPublicIp(auditEntity.getPublicIp()));
		} else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
			LOGGER.info("Audit Public IP is {} ", forwardedIp);
			if (forwardedIp != null) {
				quoteDto.setPublicIp(getPublicIp(forwardedIp));
			}
		}
		CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quoteDto.getQuoteCode(),
				Source.MANUAL_COF.getSourceType());
		if (cofDetail != null) {
			quoteDto.setIsManualCofSigned(true);
		}
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteDto.getQuoteCode());
		quoteDto.setIsDocusign(docusignAudit != null);
		quoteDto.setCustomerName(quote.getCustomer().getCustomerName());
		return quoteDto;

	}

	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<QuoteToLe> quoteToLe, Boolean manualFeasibility)
			throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		for (QuoteToLe quTle : quoteToLe) {
			LOGGER.info("Fetching quoteToLe {}", quTle.getId());
			QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
			quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
			quoteToLeDto.setCurrency(quTle.getCurrencyCode());
			quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
			LOGGER.info("To set product families in quote to le dto");
			quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
					isFeasibleSites, isSiteProperitiesRequired, siteId, manualFeasibility));
			quoteToLeDto.setClassification(quTle.getClassification());
			quoteToLeDto.setIsMultiCircuit(quTle.getIsMultiCircuit());
			if (Objects.nonNull(quTle.getIsDemo())) {
				LOGGER.info("Demo flag is not null for quote ---> {} ", quTle.getQuote().getQuoteCode());
				int result = Byte.compare(quTle.getIsDemo(), BACTIVE);
				if (result == 0) {
					LOGGER.info("Entered into the block to set demo info in get quote details for quote -----> {} ",
							quTle.getQuote().getQuoteCode());
					quoteToLeDto.setIsDemo(true);
					quoteToLeDto.setDemoType(quTle.getDemoType());
				}
			}

			quoteToLeDtos.add(quoteToLeDto);
			partnerService.setExpectedArcAndNrcForPartnerQuote(quote.getQuoteCode(), quoteToLeDto);
		}

		return quoteToLeDtos;

	}

	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			LOGGER.info("Iterating through quoteToLeProductFamilies");
			for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
				QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
				if (quFamily.getMstProductFamily() != null) {
					quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
					quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
				}
				LOGGER.info("Status and product name are {} , {} ", quoteToLeProductFamilyBean.getStatus(),
						quoteToLeProductFamilyBean.getProductName());
				List<ProductSolutionBean> solutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(quFamily), isFeasibleSites,
								isSiteProperitiesRequired, siteId, manualFeasibility));
				quoteToLeProductFamilyBean.setSolutions(solutionBeans);
				quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);

			}
		}

		return quoteToLeProductFamilyBeans;
	}

	private List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
		}

		return solutionBeans;

	}

	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
			Boolean isFeasibleSites, Boolean isSiteProperitiesRequired, Integer siteId, Boolean manualFeasibility)
			throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				LOGGER.info("IProduct solution data is {} ", solution.toString());
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();
				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if (solution.getProductProfileData() != null) {
					LOGGER.info("Product solution product profile data is {} ", solution.getProductProfileData());
					productSolutionBean.setSolution((SolutionDetail) Utils
							.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class));
				}
				LOGGER.info("Getting illSite bean");
				List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution, siteId), isFeasibleSites,
								isSiteProperitiesRequired, manualFeasibility));
				LOGGER.info("Fetched illSite bean");
				productSolutionBean.setSites(illSiteBeans);
				productSolutionBeans.add(productSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	private List<QuoteIllSiteBean> getSortedIllSiteDtos(List<QuoteIllSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(QuoteIllSiteBean::getSiteId));

		}

		return illSiteBeans;
	}

	private List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
		if (quoteComponentBeans != null) {
			quoteComponentBeans.sort(Comparator.comparingInt(QuoteProductComponentBean::getComponentId));

		}

		return quoteComponentBeans;
	}

	private List<QuoteIllSiteBean> constructIllSiteDtos(List<QuoteIllSite> illSites, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Boolean manualFeasibility) throws TclCommonException {
		if (isSiteProperitiesRequired == null) {
			isSiteProperitiesRequired = false;
		}

		List<QuoteIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					if (!isFeasibleSites && !illSite.getFeasibility().equals(CommonConstants.BACTIVE)) {
						continue;
					}
					// Quote quote =
					// illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					QuoteIllSiteBean illSiteBean = new QuoteIllSiteBean(illSite);
					illSiteBean.setQuoteSla(constructSlaDetails(illSite));
					illSiteBean.setFeasibility(constructSiteFeasibility(illSite));
					List<QuoteProductComponentBean> quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponent(illSite.getId(), false, isSiteProperitiesRequired));
					illSiteBean.setComponents(quoteProductComponentBeans);
					illSiteBean.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
					illSiteBean.setIsTaskTriggered(illSite.getIsTaskTriggered());
					// add rejection flag
					if (illSite.getCommercialRejectionStatus() != null) {
						if (illSite.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
							illSiteBean.setRejectionStatus(true);
						} else {
							illSiteBean.setRejectionStatus(false);
						}
					}
					// approve flag
					if (illSite.getCommercialApproveStatus() != null) {
						if (illSite.getCommercialApproveStatus().equalsIgnoreCase("1")) {
							illSiteBean.setApproveStatus(true);
						} else {
							illSiteBean.setApproveStatus(false);
						}
					}
					if (illSiteBean.getRejectionStatus() || illSiteBean.getApproveStatus()) {
						illSiteBean.setIsActionTaken(true);
					}
					illSiteBean.setMfStatus(illSite.getMfStatus());

					sites.add(illSiteBean);

				}
			}
		}
		return sites;
	}

	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded), id));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	private List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

		}

		return attributeBeans;
	}

	private List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIllSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(illSite,
				(byte) 1);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			for (SiteFeasibility siteFeasibility : siteFeasibilities) {
				siteFeasibilityBeans.add(constructSiteFeasibility(siteFeasibility));
			}
		}
		return siteFeasibilityBeans;
	}

	private SiteFeasibilityBean constructSiteFeasibility(SiteFeasibility siteFeasibility) {
		SiteFeasibilityBean siteFeasibilityBean = new SiteFeasibilityBean();
		siteFeasibilityBean.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
		siteFeasibilityBean.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
		siteFeasibilityBean.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
		siteFeasibilityBean.setType(siteFeasibility.getType());
		siteFeasibilityBean.setCreatedTime(siteFeasibility.getCreatedTime());
		siteFeasibilityBean.setProvider(siteFeasibility.getProvider());
		siteFeasibilityBean.setRank(siteFeasibility.getRank());
		siteFeasibilityBean.setResponseJson(siteFeasibility.getResponseJson());
		siteFeasibilityBean.setFeasibilityType(siteFeasibility.getFeasibilityType());
		siteFeasibility.setSfdcFeasibilityId(siteFeasibility.getSfdcFeasibilityId());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		return siteFeasibilityBean;
	}

	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, Boolean isSiteRequired) {
		List<QuoteProductComponentsAttributeValue> attributes = null;

		attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		} else if (isSiteRequired) {
			attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);
		} else {

			if (attributes != null) {
				return attributes.stream()
						.filter(attr -> (!attr.getProductAttributeMaster().getName()
								.equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}

	private QuotePriceBean constructComponentPriceDto(QuoteProductComponent quoteProductComponent) {
		QuotePriceBean priceDto = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			List<QuotePrice> prices = quotePriceRepository.findByReferenceNameAndReferenceId(
					QuoteConstants.COMPONENTS.toString(), String.valueOf(quoteProductComponent.getId()));
			if (prices != null && !prices.isEmpty())
				priceDto = new QuotePriceBean(prices.get(0));
		}
		return priceDto;

	}

	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues, Integer siteId) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
		// UnComment if need total ip count along with pool size
//		SolutionDetail soDetail = new SolutionDetail();
//		if(illSite.isPresent()) {
//			try {
//				soDetail = (SolutionDetail) Utils.convertJsonToObject(illSite.get().getProductSolution().getProductProfileData(),
//						SolutionDetail.class);
//			} catch (TclCommonException e) {
//				LOGGER.error("Inside illQuoteService.constructAttribute error while parsing product solution profile data for site id {}",siteId);
//			}
//			
//		}
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				QuoteProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new QuoteProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new QuoteProductComponentsAttributeValueBean(attributeValue);
				}
				// UnComment if need total ip count along with pool size
				// Concat Total ip count with CIDR notation
//				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
//					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
//						qtAttributeValue.setAttributeValues(illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					} else {
//						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues()+illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					}
//				}
//				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
//					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
//						qtAttributeValue.setAttributeValues(illQuotePdfService.setIpAttributes(attributeValue.getAttributeValues(), PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					} else {
//						qtAttributeValue.setAttributeValues(qtAttributeValue.getAttributeValues()+illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					}
//				}
//				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
//				if (productAttributeMaster != null) {
//					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
//					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
//					qtAttributeValue.setName(productAttributeMaster.getName());
//				}
				// Without concatenating total ip count.
				if (qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS)
						&& !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
					if (illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType()
							.equalsIgnoreCase("MACD")) {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					} else {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					}
				}
				if (qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS)
						&& !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
					if (illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType()
							.equalsIgnoreCase("MACD")) {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					} else {
						qtAttributeValue.setAttributeValues(qtAttributeValue.getAttributeValues());
					}
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				qtAttributeValue.setAttributeId(attributeValue.getId());
				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				quoteProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return quoteProductComponentsAttributeValueBean;
	}

	private QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			LOGGER.info("Reference Id is ----> {} ", attributeValue.getId());
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				priceDto = new QuotePriceBean(attrPrice);
			}
		}
		return priceDto;

	}

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponent> components = null;
		if (isSitePropertiesNeeded) {
			LOGGER.info("Getting quote Product Component for siteId {}", siteId);
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					siteId, IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),
					QuoteConstants.GVPN_SITES.toString());
			LOGGER.info("Fetched quote Product Component for siteId {}", siteId);
		} else if (isSitePropNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.GVPN_SITES.toString());
		} else {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.GVPN_SITES.toString());
			if (components != null) {
				return components.stream()
						.filter(cmp -> (!cmp.getMstProductComponent().getName()
								.equals(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return components;

	}

	private List<QuoteSlaBean> constructSlaDetails(QuoteIllSite illSite) {

		List<QuoteSlaBean> quoteSlas = new ArrayList<>();
		if (illSite.getQuoteIllSiteSlas() != null) {

			illSite.getQuoteIllSiteSlas().forEach(siteSla -> {
				QuoteSlaBean sla = new QuoteSlaBean();
				sla.setId(siteSla.getId());
				sla.setSlaEndDate(siteSla.getSlaEndDate());
				sla.setSlaStartDate(siteSla.getSlaStartDate());
				sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue()));
				if (siteSla.getSlaMaster() != null) {
					SlaMaster slaMaster = siteSla.getSlaMaster();
					SlaMasterBean master = new SlaMasterBean();
					master.setId(siteSla.getId());
					master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
					master.setSlaName(slaMaster.getSlaName());
					sla.setSlaMaster(master);
				}

				quoteSlas.add(sla);
			});
		}

		return quoteSlas;

	}

	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution, Integer siteId) {

		List<QuoteIllSite> illsites = new ArrayList<>();

		if (siteId != null) {

			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);

			if (quoteIllSite.isPresent()) {

				illsites.add(quoteIllSite.get());

			}

		} else {

			illsites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return illsites;

	}

	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		LOGGER.info("Getting product solution of familyid {} ", family.getId());
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		LOGGER.info("Get quote to le product family by quote id");
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		LOGGER.info("size of prod family fetched is {} ", prodFamilys.size());
		return prodFamilys;

	}

	private Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
		if (attributeValues != null) {
			LOGGER.info("getting quote to le attributes for quoteLe id {}", quTle.getId());
			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();

				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				leAttributeBeans.add(attributeBean);

			});

		}
		LOGGER.info("Legal attribute beans size {}", leAttributeBeans.size());
		return leAttributeBeans;
	}

	private MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
			mstOmsAttributeBean.setId(mstOmsAttribute.getId());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	public String getPublicIp(String publicIp) {
		String[] publicIps = publicIp.split(",");
		Pattern ipPattern = Pattern.compile(CommonConstants.PUBLIC_IP_PATTERN);
		for (String ip : publicIps) {
			if (ip.contains("%3")) {
				ip = ip.replace("%3", "");
			}
			if (ipPattern.matcher(ip).matches()) {
				return ip;
			}
		}
		return null;
	}

	protected QuoteToLe processQuote(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId, User user, Boolean ns,
			Integer term, Character isCommercial) throws TclCommonException {
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = getCustomerId(erfCustomerId);// get the customer Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;
		// Checking whether the input is for creating or updating
		if (quoteDetail.getQuoteleId() == null && quoteDetail.getQuoteId() == null) {
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(),
					quoteDetail.getEngagementOptyId(), quoteDetail.getQuoteCode(), ns, term);
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), CommonConstants.BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail, term, isCommercial);
			quoteToLe.setIsMultiVrf("No");
			if (!StringUtil.isBlank(quoteDetail.getIsmultiVrf()) && quoteDetail.getIsmultiVrf() != null) {
				LOGGER.info("vrf isenabled flag" + quoteDetail.getIsmultiVrf());
				quoteToLe.setIsMultiVrf(quoteDetail.getIsmultiVrf());
			}
			quoteToLeRepository.save(quoteToLe);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				quoteToLe.setClassification(quoteDetail.getClassification());
				quoteToLe = quoteToLeRepository.save(quoteToLe);
			}
		}

		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} else {
			removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe);
		}

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}

		for (RenewalsSolutionDetail solution : quoteDetail.getSolutions()) {
			String productOffering = solution.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
			ProductSolution productSolution;
			productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
					Utils.convertObjectToJson(solution));
			productSolution.setSolutionCode(Utils.generateUid());
			productSolutionRepository.save(productSolution);
			if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
				omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
						quoteToLe.getTpsSfdcOptyId());// adding productService
			productSolutionSiLinkRepository.save(new ProductSolutionSiLink(solution.getServiceId(),
					productSolution.getId(), solution.getAccessType(), quoteToLe.getId()));
		}
		return quoteToLe;

	}

	private MstProductComponent getMstProperties(User user) {
		LOGGER.info("Getting master properties");
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setDescription(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		LOGGER.info(" exing get master properties");
		return mstProductComponent;

	}

	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
		return quoteProductComponent;

	}

	private MstProductComponent getProductComponent(ComponentDetail component, User user) throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	private void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
			ComponentDetail component, User user, String isColo) throws TclCommonException {
		try {
			/*
			 * if (Objects.isNull((isColo)) || "False".equalsIgnoreCase(isColo) ||
			 * (Objects.nonNull(isColo) && "True".equalsIgnoreCase(isColo) &&
			 * !component.getName().equalsIgnoreCase("Last Mile"))) {
			 */
			MstProductComponent productComponent = getProductComponent(component, user);
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
					illSite.getId());
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user);
				// }
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public void removeComponentsAndAttr(Integer siteId) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(siteId, QuoteConstants.GVPN_SITES.toString());
		if (!quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {

				quoteProd.getQuoteProductComponentsAttributeValues()
						.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
				quoteProductComponentRepository.delete(quoteProd);
			});
		}
	}

	private void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user)
			throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.INVALID_ATTRIBUTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user)
			throws TclCommonException {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, AttributeDetail attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

	}

	private QuoteToLe constructQuoteToLe(Quote quote, RenewalsQuoteDetail quoteDetail, Integer term,
			Character isCommercial) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setTermInMonths(term + RenewalsConstant.MONTH);
		quoteToLe.setCurrencyCode(RenewalsConstant.INR);
		quoteToLe.setQuoteType(RenewalsConstant.RENEWALS);
		quoteToLe.setIsAmended(BDEACTIVATE);
		quoteToLe.setClassification(quoteDetail.getClassification());
		quoteToLe.setIsMultiCircuit(CommonConstants.BACTIVE);
		// quoteToLe.setIsCommercialChanges(isCommercial);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
			quoteToLe.setTpsSfdcOptyId(opportunity.getTpsOptyId());
			quoteToLe.setErfCusCustomerLegalEntityId(partnerService
					.getCustomerLeIdFromEngagementOpportunityId(Integer.valueOf(quoteDetail.getEngagementOptyId())));
		}
		return quoteToLe;
	}

	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NAME.toString(),
				user.getFirstName());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_EMAIL.toString(),
				user.getEmailId());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_ID.toString(),
				user.getUsername());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NO.toString(),
				user.getContactNo());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.DESIGNATION.toString(),
				user.getDesignation());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.RECURRING_CHARGE_TYPE.toString(), "ARC");
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED,
				"No");
	}

	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;
		if (name != null) {
			List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name,
					(byte) 1);

			if (!mstOmsAttributesList.isEmpty()) {
				mstOmsAttribute = mstOmsAttributesList.get(0);
			}
			if (mstOmsAttribute == null) {
				mstOmsAttribute = new MstOmsAttribute();
				mstOmsAttribute.setCreatedBy(userName);
				mstOmsAttribute.setCreatedTime(new Date());
				mstOmsAttribute.setIsActive((byte) 1);
				mstOmsAttribute.setName(name);
				mstOmsAttribute.setDescription(value);
				mstOmsAttributeRepository.save(mstOmsAttribute);
			}

			constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);
		}
	}

	private void constructLegaAttribute(MstOmsAttribute mstOmsAttribute, QuoteToLe quoteTole, String name,
			String value) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, name);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(value);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(name);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			updateLeAttrbute(quoteLeAttributeValues, name, value);
		}

	}

	private void updateLeAttrbute(List<QuoteLeAttributeValue> quoteLeAttributeValues, String name, String value) {
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setAttributeValue(value);
				attrVal.setDisplayValue(name);
				quoteLeAttributeValueRepository.save(attrVal);

			});
		}

	}

	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

	private QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
			QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
		quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		return quoteToLeProductFamily;

	}

	private void removeUnselectedSolution(RenewalsQuoteDetail quoteDetail,
			QuoteToLeProductFamily quoteToLeProductFamily, QuoteToLe quoteToLe) {
		List<ProductSolution> exprodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		for (ProductSolution exproductSolution : exprodSolutions) {
			boolean remove = true;
			for (RenewalsSolutionDetail solution : quoteDetail.getSolutions()) {
				if (solution.getOfferingName().equals(exproductSolution.getMstProductOffering().getProductName())) {
					remove = false;
					break;
				}
			}
			if (remove) {
				for (QuoteIllSite illSites : exproductSolution.getQuoteIllSites()) {
					removeComponentsAndAttr(illSites.getId());
					deletedIllsiteAndRelation(illSites);
				}
				// Trigger delete productSolution
				if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
					omsSfdcService.processDeleteProduct(quoteToLe, exproductSolution);
				productSolutionRepository.delete(exproductSolution);
			}

		}
	}

	public void deletedIllsiteAndRelation(QuoteIllSite quoteIllSite) {
		List<QuoteIllSiteSla> slas = quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite);
		if (slas != null && !slas.isEmpty()) {
			slas.forEach(sl -> {
				quoteIllSiteSlaRepository.delete(sl);
			});
		}
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibilities.forEach(site -> {
				List<SiteFeasibilityAudit> siteFeasibilityAuditList = siteFeasibilityAuditRepository
						.findBySiteFeasibility(site);
				if (!siteFeasibilityAuditList.isEmpty())
					siteFeasibilityAuditRepository.deleteAll(siteFeasibilityAuditList);
				siteFeasibilityRepository.delete(site);
			});
		}

		List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
				.findByQuoteIllSite(quoteIllSite);
		if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			quoteIllSiteToServiceList.stream().forEach(quoteIllSiteToService -> {
				quoteIllSiteToServiceRepository.delete(quoteIllSiteToService);
			});
		}

		illSiteRepository.delete(quoteIllSite);

		// replace opportunity id in quote to le if needed - to handle multicircuit case
		// - PIPF-22
		QuoteToLe quoteLe = quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		List<QuoteIllSiteToService> quoteSiteToServiceList = quoteIllSiteToServiceRepository
				.findByTpsSfdcParentOptyIdAndQuoteToLe(quoteLe.getTpsSfdcParentOptyId(), quoteLe);
		if (quoteSiteToServiceList != null && quoteSiteToServiceList.isEmpty()) {
			LOGGER.info("Replacing parent opty id in quote to le quoteToLeId {}", quoteLe.getId());
			List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository
					.findByQuoteToLe_Id(quoteLe.getId());
			if (siteToServiceList != null && !siteToServiceList.isEmpty()) {
				Optional<QuoteIllSiteToService> siteToService = siteToServiceList.stream().findFirst();
				if (siteToService.isPresent()) {
					quoteLe.setTpsSfdcParentOptyId(siteToService.get().getTpsSfdcParentOptyId());
					quoteToLeRepository.save(quoteLe);
					LOGGER.info("Replacing parent opty id {} in quote to le quoteToLeId {}",
							siteToService.get().getTpsSfdcParentOptyId(), quoteLe.getId());
				}
			}
		}

	}

	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName,
			User user) throws TclCommonException {
		MstProductOffering productOffering = null;

		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
				productOfferingName, (byte) 1);
		if (productOffering == null) {
			productOffering = new MstProductOffering();
			productOffering.setCreatedBy(user.getUsername());
			productOffering.setCreatedTime(new Date());
			productOffering.setMstProductFamily(mstProductFamily);
			productOffering.setProductName(productOfferingName);
			productOffering.setStatus((byte) 1);
			productOffering.setProductDescription(productOfferingName);
			mstProductOfferingRepository.save(productOffering);

		}
		return productOffering;
	}

	private ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
			QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		return productSolution;
	}

	public String getQuoteCurrency(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		List<Integer> locationIds = new ArrayList<>();
		List<Integer> nonFeasibleLocationIds = new ArrayList<>();
		String currency = "INR";
		String locCommaSeparated = StringUtils.EMPTY;

		if (Objects.isNull(quoteLeId) && Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				return quoteToLeRepository.findById(quoteLeId).get().getCurrencyCode();
			} else {
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_IdAndMstProductFamily_Name(quoteLeId, "GVPN");
				List<ProductSolution> solutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);

				solutions.forEach(sol -> {
					sol.getQuoteIllSites().forEach(quoteIllsite -> {
						if (quoteIllsite.getFeasibility() == CommonConstants.BACTIVE)
							locationIds.add(quoteIllsite.getErfLocSitebLocationId());
						else
							nonFeasibleLocationIds.add(quoteIllsite.getErfLocSitebLocationId());
					});
				});

				if (!locationIds.isEmpty()) {
					locCommaSeparated = locationIds.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					currency = getCurrencyBasedOnCountries(locCommaSeparated);
				} else {
					locCommaSeparated = nonFeasibleLocationIds.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					currency = getCurrencyBasedOnCountries(locCommaSeparated);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return currency;
	}

	@Autowired
	GvpnCofValidatorService gvpnCofValidatorService;

	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = getQuoteDetails(quoteId, null, false, null, null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = gvpnQuotePdfService.getCofAttributes(true, quoteDetail, true, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
							|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for GVPN is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = gvpnCofValidatorService.processCofValidation(variable, "GVPN",
						quoteToLe.get().getQuoteType());
				// checkFeasibilityValidityPeriod(quoteToLe, commonValidationResponse);
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the mandatory Data", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		return commonValidationResponse;
	}

	private String getCurrencyBasedOnCountries(String locationIds) throws TclCommonException {
		/*
		 * Business Logic: 1. If the list contains only india, then currency will be in
		 * 'INR' 2. Or else 'USD'
		 */
		List<LocationDetail> locDetails = Arrays.asList(getAddress(locationIds));
		Set<String> countries = locDetails.stream().map(detail -> detail.getApiAddress().getCountry())
				.collect(Collectors.toSet());
		Map<String, Integer> countriesCount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		countries.stream().forEach(country -> {
			if (Objects.nonNull(countriesCount.get(country))) {
				countriesCount.put(country, countriesCount.get(country) + 1);
			} else {
				countriesCount.put(country, 1);
			}
		});

		if (!countriesCount.containsKey("India")) {
			return "USD";
		} else if (countriesCount.containsKey("India") && countriesCount.size() > 1) {
			return "USD";
		}

		return "INR";
	}

	@Value("${rabbitmq.location.details.feasibility}")

	protected String locationDetailQueue;

	private LocationDetail[] getAddress(String locCommaSeparated) throws TclCommonException {
		try {
			String response = (String) mqUtils.sendAndReceive(locationDetailQueue, locCommaSeparated);
			LOGGER.info("Output Payload for location details {}", response);
			LocationDetail[] locDetails = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return locDetails;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	@Transactional
	public void updateVrfSiteInfo(UpdateRequest request, Integer erfCustomerId, Integer quoteId, Integer siteId)
			throws TclCommonException {

		try {
			LOGGER.info("Entered into updateVrfSiteInfo" + erfCustomerId + "quoteId" + quoteId + "siteId" + siteId
					+ "isNoOfMultiVrfChanged" + request.getIsNoOfMultiVrfChanged());
			validateVrfSiteInformation(request, siteId, quoteId);
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			MstProductFamily productFamily = getProductFamily(request.getFamilyName());
			User user = getUserId(Utils.getSource());
			if (request.getIsNoOfMultiVrfChanged()) {
				LOGGER.info("Number of VRFs changed by the user so removing previous VRF sites for site ID " + siteId);
				removeExistingMultiVrfProductComponent(quoteIllSite, productFamily);
				LOGGER.info("Removed VRFs and updating the new VRFs given by user");
			}
			for (ComponentDetail componentDetail : request.getComponentDetails()) {
				LOGGER.info("component name" + componentDetail.getName());
				if (componentDetail.getName().equalsIgnoreCase(CommonConstants.VRF_COMMON)) {
					for (AttributeDetail attributeDetail : componentDetail.getAttributes()) {
						updateVrfCommonProductAttribute(request, componentDetail, attributeDetail, productFamily);
					}
				} else {
					processMultiVrfProductComponent(productFamily, quoteIllSite, componentDetail, user);
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Error in Update vrf Site info {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	protected void validateVrfSiteInformation(UpdateRequest request, Integer siteId, Integer quoteId)
			throws TclCommonException {
		if ((request == null) || (siteId == null) || (quoteId == null) || (request.getComponentDetails() == null)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	@Autowired
	QuoteVrfSitesRepository quoteVrfSitesRepository;

	private void removeExistingMultiVrfProductComponent(QuoteIllSite illSite, MstProductFamily productFamily)
			throws TclCommonException {
		try {
			LOGGER.info("Entered in to removeExistingMultiVrfProductComponent for site ID " + illSite.getId());
			List<QuoteProductComponent> quoteProductComponent = new ArrayList<QuoteProductComponent>();
			List<QuoteProductComponent> quoteProductComponentList = new ArrayList<QuoteProductComponent>();
			List<QuoteVrfSites> vrfSiteList = quoteVrfSitesRepository.findByQuoteIllSite(illSite);
			List<MstProductComponent> mstProductComponents = new ArrayList<MstProductComponent>();
			if (!vrfSiteList.isEmpty()) {
				for (QuoteVrfSites vrfSiteListIds : vrfSiteList) {
					mstProductComponents = mstProductComponentRepository
							.findByNameAndStatus(vrfSiteListIds.getVrfName(), (byte) 1);
					if (!mstProductComponents.isEmpty()) {
						quoteProductComponent = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndReferenceName(
										vrfSiteListIds.getId(), mstProductComponents.get(0), productFamily,
										QuoteConstants.VRF_SITES.toString());
						quoteProductComponentList.addAll(quoteProductComponent);
					}
				}
			}
			Set<Integer> setOfProductComponentIds = new HashSet<Integer>();
			if (!quoteProductComponentList.isEmpty()) {
				for (QuoteProductComponent quoteProductComponentIds : quoteProductComponentList) {
					setOfProductComponentIds.add(quoteProductComponentIds.getId());
					quoteProductComponentsAttributeValueRepository
							.deleteAllByQuoteProductComponentIdIn(setOfProductComponentIds);
					quoteProductComponentRepository.deleteById(quoteProductComponentIds.getId());
					quoteVrfSitesRepository.deleteById(quoteProductComponentIds.getReferenceId());
				}
			}
			LOGGER.info(
					"Exiting removeExistingMultiVrfProductComponent method after removing entries in quote_vrf_site, quote_product_component"
							+ "quote_product_component_attribute_values for site ID " + illSite.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public void updateVrfCommonProductAttribute(UpdateRequest request, ComponentDetail cmpDetail,
			AttributeDetail attributeDetail, MstProductFamily prodFamily) throws TclCommonException {
		LOGGER.info(
				"entered into updateVrfCommonProductAttribute" + cmpDetail.getName() + "type" + cmpDetail.getType());
		try {
			QuoteProductComponent quoteProductComponent = null;
			List<QuoteProductComponent> prodComponent = null;
			Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(request.getSiteId());
			if (siteEntity.isPresent()) {
				List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
						.findByNameAndStatus(attributeDetail.getName(), CommonConstants.BACTIVE);
				ProductAttributeMaster productAttributeMaster = null;
				if (mstAttributeMaster.isEmpty()) {
					productAttributeMaster = new ProductAttributeMaster();
					productAttributeMaster.setName(attributeDetail.getName());
					productAttributeMaster.setStatus(CommonConstants.BACTIVE);
					productAttributeMaster.setDescription(attributeDetail.getName());
					productAttributeMaster.setCreatedBy(Utils.getSource());
					productAttributeMaster.setCreatedTime(new Date());
					productAttributeMasterRepository.save(productAttributeMaster);
				} else {
					productAttributeMaster = mstAttributeMaster.get(0);
				}
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(cmpDetail.getName(), (byte) 1);
				if (!mstProductComponent.isEmpty()) {
					LOGGER.info("mst product component" + mstProductComponent.get(0).getName());
					prodComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteEntity.get().getId(),
									mstProductComponent.get(0), prodFamily, cmpDetail.getType());
				}

				if (prodComponent != null) {
					quoteProductComponent = prodComponent.get(0);
				}
				if (quoteProductComponent != null) {
					List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent,
									productAttributeMaster);
					if (quoteProductComponentsAttributeValues.isEmpty()) {
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
						quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
						quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
						quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
						quoteProductComponentsAttributeValueRepository
								.save(quoteProductComponentsAttributeValue);
						LOGGER.info("-------created the attributes---new-------");
					} else {
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
								.get(0);
						quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
						quoteProductComponentsAttributeValueRepository
								.save(quoteProductComponentsAttributeValue);
						LOGGER.info("-------Updated the attribute---old-----");
					}

				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private void processMultiVrfProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
			ComponentDetail component, User user) throws TclCommonException {
		try {
			LOGGER.info("Entered into processMultiVrfProductComponent" + component.getName() + "TYPE"
					+ component.getType());
			QuoteVrfSites vrfSite = quoteVrfSitesRepository.findByQuoteIllSiteAndVrfNameAndSiteType(illSite,
					component.getName(), component.getType());
			if (vrfSite == null) {
				QuoteVrfSites quoteVrfSite = new QuoteVrfSites();
				quoteVrfSite.setQuoteIllSite(illSite);
				quoteVrfSite.setVrfName(component.getName());
				quoteVrfSite.setVrfType(component.getVrfPortType());
				quoteVrfSite.setSiteType(component.getType());
				quoteVrfSite.setCreatedTime(new Date());
				quoteVrfSite.setTpsServiceId(component.getServiceId());
				quoteVrfSite = quoteVrfSitesRepository.save(quoteVrfSite);

				MstProductComponent productComponent = getProductComponent(component, user);
				QuoteProductComponent quoteComponent = constructVrfProductComponent(productComponent, productFamily,
						quoteVrfSite.getId());
				quoteComponent.setType(component.getType());
				quoteProductComponentRepository.save(quoteComponent);
				LOGGER.info("componenet saved successfully" + quoteComponent.getMstProductComponent().getName());

				for (AttributeDetail attribute : component.getAttributes()) {
					processProductAttribute(quoteComponent, attribute, user);
				}
			} else {
				LOGGER.info("updated vrf attributes" + vrfSite.getId());
				List<QuoteProductComponent> prodComponent = null;
				QuoteProductComponent quoteComponent = null;
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(component.getName(), (byte) 1);
				if (!mstProductComponent.isEmpty()) {
					LOGGER.info("mst product componennet " + mstProductComponent.get(0).getName());
					prodComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(
									vrfSite.getId(), mstProductComponent.get(0), productFamily, component.getType(),
									QuoteConstants.VRF_SITES.toString());
				}
				if (prodComponent != null && !prodComponent.isEmpty()) {
					quoteComponent = prodComponent.get(0);
					if (quoteComponent != null) {
						for (AttributeDetail attribute : component.getAttributes()) {
							LOGGER.info("-------Attribute Name--------{} ", attribute.getName());
							ProductAttributeMaster productAttribute = null;
							List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
									.findByNameAndStatus(attribute.getName(), (byte) 1);
							// if attribute is empty need to add new attribute
							if (productAttributeMasters == null) {
								productAttribute = new ProductAttributeMaster();
								productAttribute.setCreatedBy(user.getUsername());
								productAttribute.setCreatedTime(new Date());
								productAttribute.setDescription(attribute.getName());
								productAttribute.setName(attribute.getName());
								productAttribute.setStatus((byte) 1);
								productAttribute = productAttributeMasterRepository.save(productAttribute);
							} else {
								productAttribute = productAttributeMasters.get(0);
							}
							List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponentAndProductAttributeMaster(quoteComponent,
											productAttribute);
							if (quoteProductComponentsAttributeValues.isEmpty()) {
								QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
								quoteProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
								quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttribute);
								quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteComponent);
								quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);
								LOGGER.info("-------Updated the properities---new-------");
							} else {
								QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
										.get(0);
								quoteProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
								quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);
								LOGGER.info("-------Updated the properities---old-----");
							}
						}
					}
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	protected QuoteProductComponent constructVrfProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer vrfId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(vrfId);
		quoteProductComponent.setReferenceName(QuoteConstants.VRF_SITES.toString());
		return quoteProductComponent;

	}
}
