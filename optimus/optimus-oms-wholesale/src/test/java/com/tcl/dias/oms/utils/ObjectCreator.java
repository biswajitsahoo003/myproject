package com.tcl.dias.oms.utils;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CommonDocusignResponse;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponse;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductsserviceResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteLocationResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteResponseBean;
import com.tcl.dias.common.sfdc.response.bean.StagingResponseBean;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.BillingAttributesBean;
import com.tcl.dias.oms.beans.BillingRequest;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OrderConfiguration;
import com.tcl.dias.oms.beans.OrderConfigurations;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteConfiguration;
import com.tcl.dias.oms.beans.QuoteConfigurations;
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
import com.tcl.dias.oms.beans.SlaUpdateRequest;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
//import com.tcl.dias.oms.dashboard.constants.DashboardConstant;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.CurrencyConversion;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.LeProductSla;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SfdcJob;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This file contains the ObjectCreator.java class. This class consist of the
 * test case values
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ObjectCreator {


    QuoteToLe quoteToLe = new QuoteToLe();

    public OpportunityResponseBean getOpportunityResponseBean() {
        OpportunityResponseBean bean = new OpportunityResponseBean();
        bean.setCustomOptyId("id");
        bean.setMessage("msg");
        bean.setOpportunity(new OpportunityResponse());
        bean.setStatus("status");
        return bean;
    }

    public MailNotificationRequest getMailNotificationRequest() {
        MailNotificationRequest req = new MailNotificationRequest();
        req.setAttachementHtml("html");
        req.setAttachmentName("name");
        req.setAttachmentPath("path");
        req.setFrom("from");
        return req;
    }

    public List<String> getNotificationSubscriptionDetails() {
        List<String> toList = new ArrayList<>();
        toList.add("a");
        return toList;
    }

    public String getDocuSignResponse() throws TclCommonException {
        CommonDocusignResponse response = new CommonDocusignResponse();
        response.setDocusignStatus("status");
        response.setEnvelopeId("env id");
        response.setEnvelopeResponse("envelopeResponse");
        response.setErrorMessage("errorMessage");
        response.setIp("10.168.121.13");
        response.setName("name");
        response.setPath("path");
        return Utils.convertObjectToJson(response);
    }

    /**
     * getQuoteDetail-mock values
     *
     * @return QuoteDetail
     */
    public QuoteDetail getQuoteDetailNew() {
        QuoteDetail quoteDetail = new QuoteDetail();
        /* quoteDetail.toString(); */
        quoteDetail.setCustomerId(1);
        quoteDetail.setSite(getSite());
        quoteDetail.setProductName("IAS");
        quoteDetail.setSite(getSite());
        quoteDetail.setQuoteleId(1);
        quoteDetail.setQuoteId(2);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        // solution.toString();
        solution.setOfferingName("new");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);
        return quoteDetail;
    }

    /**
     * getQuoteDetail-mock values
     *
     * @return QuoteDetail
     */
    public QuoteDetail getQuoteDetail() {
        QuoteDetail quoteDetail = new QuoteDetail();
        /* quoteDetail.toString(); */
        quoteDetail.setCustomerId(1);
        quoteDetail.setSite(getSite());
        quoteDetail.setProductName("IAS");
        quoteDetail.setSite(getSite());
        quoteDetail.setQuoteleId(1);
        quoteDetail.setQuoteId(1);
        quoteDetail.setQuoteId(2);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /*
         * component.toString();
         */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /*
         * attributeDetail.toString();
         */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteDetail getQuoteDetail3() {
        QuoteDetail quoteDetail = new QuoteDetail();
        /* quoteDetail.toString(); */
        quoteDetail.setCustomerId(1);
        quoteDetail.setProductName("IAS");
        quoteDetail.setSite(getSite2());
        quoteDetail.setQuoteleId(1);
        quoteDetail.setQuoteId(1);
        quoteDetail.setQuoteId(2);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /*
         * component.toString();
         */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /*
         * attributeDetail.toString();
         */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteDetail getQuoteDetail2() {
        QuoteDetail quoteDetail = new QuoteDetail();
        quoteDetail.setProductName("IAS");
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);
        return quoteDetail;
    }

    /**
     * getQuoteDetail-mock values
     *
     * @return QuoteDetail
     */
    public QuoteDetail getQuoteDetailWithoutSiteID() {
        QuoteDetail quoteDetail = new QuoteDetail();
        /* quoteDetail.toString(); */
        quoteDetail.setCustomerId(1);
        quoteDetail.setSite(getSiteWithoutId());
        quoteDetail.setProductName("IAS");
        quoteDetail.setSite(getSiteWithoutId());
        quoteDetail.setQuoteleId(1);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Single Internet Access");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);
        return quoteDetail;
    }

    /**
     * getSite-mock values
     *
     * @return List<Site>
     */
    private List<Site> getSiteWithoutId() {
        List<Site> siList = new ArrayList<>();
        SiteDetail siteDetail = new SiteDetail();
        siteDetail.setLocationId(1);
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        siteDetail.setComponents(components);
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("IAS");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /* site.toString(); */
        site.setSite(getSitDetailsWithSiteIdEmpty());
        siList.add(site);

        return siList;
    }

    /**
     * getSite-mock values
     *
     * @return List<Site>
     */
    public List<Site> getSite() {
        List<Site> siList = new ArrayList<>();
        SiteDetail siteDetail = new SiteDetail();
        siteDetail.setSiteId(1);
        siteDetail.setLocationId(1);
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        siteDetail.setComponents(components);
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("IAS");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /* site.toString(); */
        site.setSite(getSitDetails());
        siList.add(site);

        return siList;
    }

    public List<Site> getSite2() {
        List<Site> siList = new ArrayList<>();
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("NPL1");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /* site.toString(); */
        site.setSite(getSitDetails());
        siList.add(site);
        Site site2 = new Site();
        site2.setBandwidth("f");
        site2.setOfferingName("NPL2");
        site2.setBandwidthUnit("daf");
        site2.setImage("adgdag");
        /* site.toString(); */
        site2.setSite(getSitDetails());
        siList.add(site2);

        return siList;
    }

    /**
     * getSite-mock values
     *
     * @return List<Site>
     */
    private List<Site> getNplSite() {
        List<Site> siList = new ArrayList<>();
        /*
         * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
         * siteDetail.setLocationId(1); List<ComponentDetail> components = new
         * ArrayList<>(); ComponentDetail component = new ComponentDetail();
         * component.toString(); components.add(component); List<AttributeDetail>
         * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
         * AttributeDetail(); attributeDetail.setName("Model No");
         * attributeDetail.setValue("CISCO"); attributeDetail.toString();
         * attributeDetails.add(attributeDetail);
         * component.setAttributes(attributeDetails); component.setIsActive("Y");
         * component.setName("CPE"); siteDetail.setComponents(components);
         */
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("Standard point-to-point conectivity");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /*
         * site.toString();
         */
        site.setSite(getSitDetails());
        Site site1 = new Site();
        site1.setBandwidth("f");
        site1.setOfferingName("Standard point-to-point conectivity");
        site1.setBandwidthUnit("daf");
        site1.setImage("adgdag");
        /* site1.toString(); */
        site1.setSite(getSitDetails());
        siList.add(site);
        siList.add(site1);

        return siList;
    }

    /**
     * getSite-mock values
     *
     * @return List<Site>
     */
    private List<Site> getNplSite1() {
        List<Site> siList = new ArrayList<>();
        /*
         * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
         * siteDetail.setLocationId(1); List<ComponentDetail> components = new
         * ArrayList<>(); ComponentDetail component = new ComponentDetail();
         * component.toString(); components.add(component); List<AttributeDetail>
         * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
         * AttributeDetail(); attributeDetail.setName("Model No");
         * attributeDetail.setValue("CISCO"); attributeDetail.toString();
         * attributeDetails.add(attributeDetail);
         * component.setAttributes(attributeDetails); component.setIsActive("Y");
         * component.setName("CPE"); siteDetail.setComponents(components);
         */
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("Standard point_to_point connectivity");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /*
         * site.toString();
         */
        site.setSite(getSitDetails());

        siList.add(site);

        return siList;
    }

    /**
     * getSite-mock values
     *
     * @return List<Site>
     */
    private List<Site> getNplSite2() {
        List<Site> siList = new ArrayList<>();
        /*
         * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
         * siteDetail.setLocationId(1); List<ComponentDetail> components = new
         * ArrayList<>(); ComponentDetail component = new ComponentDetail();
         * component.toString(); components.add(component); List<AttributeDetail>
         * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
         * AttributeDetail(); attributeDetail.setName("Model No");
         * attributeDetail.setValue("CISCO"); attributeDetail.toString();
         * attributeDetails.add(attributeDetail);
         * component.setAttributes(attributeDetails); component.setIsActive("Y");
         * component.setName("CPE"); siteDetail.setComponents(components);
         */
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("Buy India Point to Point Connectivity-2");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /*
         * site.toString();
         */
        site.setSite(getSitDetails());

        siList.add(site);
        siList.add(new Site());

        return siList;
    }

    private List<Site> getNplSite3() {
        List<Site> siList = new ArrayList<>();
        /*
         * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
         * siteDetail.setLocationId(1); List<ComponentDetail> components = new
         * ArrayList<>(); ComponentDetail component = new ComponentDetail();
         * component.toString(); components.add(component); List<AttributeDetail>
         * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
         * AttributeDetail(); attributeDetail.setName("Model No");
         * attributeDetail.setValue("CISCO"); attributeDetail.toString();
         * attributeDetails.add(attributeDetail);
         * component.setAttributes(attributeDetails); component.setIsActive("Y");
         * component.setName("CPE"); siteDetail.setComponents(components);
         */
        Site site = new Site();
        site.setBandwidth("f");
        site.setOfferingName("Buy India Point to Point Connectivity-2");
        site.setBandwidthUnit("daf");
        site.setImage("adgdag");
        /*
         * site.toString();
         */
        site.setSite(getSitDetails());
        site.getSite().get(0).setSiteId(null);
        siList.add(site);

        Site site1 = new Site();
        site1.setBandwidth("f");
        site1.setOfferingName("Buy India Point to Point Connectivity-2");
        site1.setBandwidthUnit("daf");
        site1.setImage("adgdag");
        /*
         * site.toString();
         */
        site1.setSite(getSitDetails());
        site1.getSite().get(0).setSiteId(null);

        siList.add(site1);

        return siList;
    }

    /**
     * getSitDetails
     *
     * @return
     */
    private List<SiteDetail> getSitDetails() {
        List<SiteDetail> siteDetails = new ArrayList<>();
        SiteDetail detail = new SiteDetail();
        detail.setSiteId(1);
        detail.setLocationId(1);
        detail.setLocationCode("123");
        detail.setComponents(getcompDetails());
        siteDetails.add(detail);

        return siteDetails;
    }

    private List<SiteDetail> getSitDetailsWithSiteIdEmpty() {
        List<SiteDetail> siteDetails = new ArrayList<>();
        SiteDetail detail = new SiteDetail();
        detail.setLocationId(1);
        detail.setLocationCode("123");
        detail.setComponents(getcompDetails());
        siteDetails.add(detail);
        return siteDetails;
    }

    /**
     * getcompDetails
     *
     * @return
     */
    public List<ComponentDetail> getcompDetails() {
        List<ComponentDetail> componentDetails = new ArrayList<>();
        ComponentDetail componentDetail = new ComponentDetail();
        componentDetail.setComponentId(1);
        componentDetail.setAttributes(getAttributes());
        componentDetail.setIsActive("Y");
        componentDetail.setName("CPE");
        componentDetails.add(componentDetail);
        return componentDetails;
    }

    private List<ComponentDetail> getcompDetails1() {
        List<ComponentDetail> componentDetails = new ArrayList<>();
        ComponentDetail componentDetail = new ComponentDetail();
        componentDetail.setComponentId(1);
        componentDetail.setAttributes(getAttributes1());
        componentDetail.setIsActive("Y");
        componentDetail.setName("CPE");
        componentDetails.add(componentDetail);
        return componentDetails;
    }

    private List<ComponentDetail> getcompDetails3() {
        List<ComponentDetail> componentDetails = new ArrayList<>();
        ComponentDetail componentDetail = new ComponentDetail();
        componentDetail.setComponentId(1);
        componentDetail.setAttributes(getAttributes4());
        componentDetail.setIsActive("Y");
        componentDetail.setName("CPE");
        componentDetails.add(componentDetail);
        return componentDetails;
    }

    /**
     * getAttribute
     *
     * @return
     */
    protected List<AttributeDetail> getAttributes() {
        List<AttributeDetail> arAttributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setAttributeId(1);
        attributeDetail.setName("IP");
        attributeDetail.setValue("Valse");
        arAttributeDetails.add(attributeDetail);
        return arAttributeDetails;
    }

    private List<AttributeDetail> getAttributes3() {
        List<AttributeDetail> arAttributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("IP");
        attributeDetail.setValue("Valse");
        arAttributeDetails.add(attributeDetail);
        return arAttributeDetails;
    }

    private List<AttributeDetail> getAttributes1() {
        List<AttributeDetail> arAttributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("CPE");
        attributeDetail.setValue("Customer provided");
        arAttributeDetails.add(attributeDetail);
        return arAttributeDetails;
    }

    private List<AttributeDetail> getAttributes4() {
        List<AttributeDetail> arAttributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setAttributeId(1);
        attributeDetail.setName("CPE");
        attributeDetail.setValue("Customer provided");
        arAttributeDetails.add(attributeDetail);
        return arAttributeDetails;
    }

    /**
     * getIllSitesId-mock values
     *
     * @return Integer
     */
    public List<Integer> getIllSitesId() {
        List<Integer> illsitesIds = new ArrayList<>();
        illsitesIds.add(1);
        illsitesIds.add(2);
        return illsitesIds;

    }

    /**
     * getDocumentDto-mock values
     *
     * @return {@link CreateDocumentDto}
     */
    public CreateDocumentDto getDocumentDto1() {
        CreateDocumentDto documentDto = new CreateDocumentDto();
        documentDto.setCustomerId(1);
        documentDto.setCustomerLegalEntityId(1);
        documentDto.setIllSitesIds(getIllSitesId());
        documentDto.setCustomerId(1);
        documentDto.setSupplierLegalEntityId(1);
        documentDto.setQuoteId(1);
        documentDto.setQuoteLeId(1);
        /* documentDto.toString(); */
        documentDto.setIllsiteId(1);
        documentDto.setQuoteLeId(1);
        documentDto.setFamilyName("NPL");
        return documentDto;
    }

    public CreateDocumentDto getDocumentDto() {
        CreateDocumentDto documentDto = new CreateDocumentDto();
        documentDto.setCustomerId(1);
        documentDto.setCustomerLegalEntityId(1);
        documentDto.setIllSitesIds(getIllSitesId());
        documentDto.setCustomerId(1);
        documentDto.setSupplierLegalEntityId(1);
        documentDto.setQuoteId(1);
        documentDto.setQuoteLeId(1);
        documentDto.toString();
        documentDto.setIllsiteId(1);
        documentDto.setQuoteLeId(1);
        documentDto.setFamilyName("ILL");

        return documentDto;
    }

    /**
     * getQuote-mock values
     *
     * @return {@link Quote}
     */
    public Quote getQuote() {
        Quote quote = new Quote();

        quote.setId(1);
        quote.setQuoteCode("TCXVE23CV");
        quote.setCreatedBy(0);
        quote.setEffectiveDate(new Date());
        for (QuoteToLe quoteToLe : getQuotesToLead()) {
            quoteToLe.setQuote(quote);
        }
        quote.setQuoteToLes(getQuotesToLead());
        quote.setStatus((byte) 1);
        // quote.toString();
        quote.setTermInMonths(12);

        Customer customer = new Customer();
        customer.setCustomerCode("123");
        customer.setStatus((byte) 0);
        customer.setId(1);
        customer.setCustomerEmailId("vick@gmail.com");
        customer.setCustomerName("Vivek");
        // customer.toString();
        customer.setUsers(getUserEntity());
        quote.setCustomer(customer);

        quote.setCustomer(getCustomerWithOutQuote());
        return quote;
    }

    /**
     * getQuote-mock values
     *
     * @return {@link Quote}
     */
    public List<Quote> getQuoteList() {
        List<Quote> listQuote = new ArrayList<>();
        listQuote.add(getQuote());
        return listQuote;
    }

    /**
     * getQuote-mock values
     *
     * @return {@link Quote}
     */
    public Quote getQuote1() {
        Quote quote = new Quote();
        quote.setId(1);
        quote.setCreatedBy(0);
        quote.setEffectiveDate(new Date());
        quote.setStatus((byte) 1);
        quote.toString();
        quote.setTermInMonths(12);
        quote.setQuoteCode("1");
        return quote;
    }

    public Quote getQuote2() {
        Quote quote = new Quote();
        quote.setId(1);
        quote.setCreatedBy(0);
        quote.setEffectiveDate(new Date());
        quote.setQuoteToLes(getQuotesToLead2());
        quote.setStatus((byte) 1);
        quote.toString();
        quote.setTermInMonths(12);
        quote.setCustomer(getCustomer());
        quote.setQuoteToLes(getQuotesToLead());
        return quote;
    }

    /**
     * getCustomer-mock values
     *
     * @return {@link Customer}
     */
    public Customer getCustomer() {
        Customer customer = new Customer();
        customer.setCustomerCode("123");
        customer.setStatus((byte) 0);
        customer.setId(1);
        customer.setCustomerEmailId("vick@gmail.com");
        customer.setErfCusCustomerId(1);
        customer.setCustomerName("Vivek");
        // customer.toString();
        customer.setUsers(getUserEntity());
        Set<Quote> set = new HashSet<>();
        set.add(getQuote());
        customer.setQuotes(set);
        return customer;
    }

    /**
     * getCustomer-mock values
     *
     * @return {@link Customer}
     */
    public Customer getCustomerWithOutQuote() {
        Customer customer = new Customer();
        customer.setCustomerCode("123");
        customer.setStatus((byte) 0);
        customer.setId(1);
        customer.setCustomerEmailId("vick@gmail.com");
        customer.setCustomerName("Vivek");
        customer.toString();
        customer.setUsers(getUserEntity());
        customer.setErfCusCustomerId(1);
        return customer;
    }

    /**
     * getQuoteToLe-mock values
     *
     * @return {@link QuoteToLe}
     */
    public QuoteToLe getQuoteToLe() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);
        /* quoteToLe.toString(); */
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
        // quoteToLe.setStage("Get Quote");
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        return quoteToLe;
    }

    public QuoteToLe getQuoteToLeWithOutSolutions() {

        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);
        /* quoteToLe.toString(); */
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());

        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
        // quoteToLe.setStage("Get Quote");
        quoteToLe.setStage("Add Locations");

        return quoteToLe;
    }

    public QuoteToLe getNplQuoteToLe() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);
        /* quoteToLe.toString(); */
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setStage("Select Configuration");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily1());

        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);

        return quoteToLe;
    }

    public Optional<QuoteToLe> getOptionalQuoteToLe() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setQuote(getQuote());
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        Optional<QuoteToLe> quoteLe = Optional.of(quoteToLe);

        return quoteLe;
    }

    public Optional<QuoteToLe> getOptionalQuoteToLeWithSites() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setQuote(getQuote());
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        Optional<QuoteToLe> quoteLe = Optional.of(quoteToLe);

        return quoteLe;
    }

    /**
     * getQuoteToLe-mock values
     *
     * @return {@link QuoteToLe}
     */
    public QuoteToLe getQuoteToLe1() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);
        quoteToLe.toString();
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
        quoteToLe.setQuote(getQuote1());
        quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
        return quoteToLe;
    }

    /**
     * getQuotesToLead-mock values
     *
     * @return {@link QuoteToLe}
     */
    public Set<QuoteToLe> getQuotesToLead() {
        Set<QuoteToLe> mockQuotesSet = new HashSet<>();
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(3.5D);
        quoteToLe.setFinalNrc(34.5D);
        quoteToLe.setProposedMrc(23.6D);
        quoteToLe.setId(2);
        quoteToLe.setFinalNrc(67.4D);
        quoteToLe.setFinalArc(102.1D);
        quoteToLe.setProposedMrc(78.6D);
        quoteToLe.setProposedNrc(98.7D);

        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
        quoteToLe.setStage("Order Form");

        quoteToLe.setStage("Order Form");

        mockQuotesSet.add(quoteToLe);

        return mockQuotesSet;
    }

    /**
     * getQuotesToLead-mock values
     *
     * @return {@link QuoteToLe}
     */
    public Set<QuoteToLe> getQuotesToLead2() {
        Set<QuoteToLe> mockQuotesSet = new HashSet<>();
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(3.5D);
        quoteToLe.setFinalNrc(34.5D);
        quoteToLe.setProposedMrc(23.6D);
        quoteToLe.setId(2);
        quoteToLe.setFinalNrc(67.4D);
        quoteToLe.setProposedMrc(78.6D);
        quoteToLe.setProposedNrc(98.7D);
        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
        quoteToLe.setStage("Order Form");
        quoteToLe.setTpsSfdcOptyId("-1");
        quoteToLe.setQuote(getQuote1());
        return mockQuotesSet;
    }

    /**
     * getQuoteFamiles-mock values
     *
     * @return {@link QuoteToLeProductFamily}
     */
    public Set<QuoteToLeProductFamily> getQuoteFamiles() {
        Set<QuoteToLeProductFamily> mockQoteFamily = new HashSet<>();
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        /* quoTefamily.toString(); */
        quoTefamily.setProductSolutions(getProductSolution());
        quoTefamily.setMstProductFamily(getMstProductFamily());
        mockQoteFamily.add(quoTefamily);
        return mockQoteFamily;
    }

    /**
     * getQuoteToLeFamily-mock values
     *
     * @return {@link QuoteToLeProductFamily}
     */
    public QuoteToLeProductFamily getQuoteToLeFamily() {
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        quoTefamily.setId(1);
        /* quoTefamily.toString(); */
        quoTefamily.setProductSolutions(getProductSolution());
        quoTefamily.setMstProductFamily(getMstProductFamilyIAS());
        quoTefamily.setQuoteToLe(getQuoteToLe());
        return quoTefamily;

    }

    /**
     * getQuoteToLeFamily-mock values
     *
     * @return {@link QuoteToLeProductFamily}
     */
    public QuoteToLeProductFamily getQuoteToLeFamilyIAS() {
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        quoTefamily.setId(1);
        /* quoTefamily.toString(); */
        quoTefamily.setProductSolutions(getProductSolution());
        quoTefamily.setMstProductFamily(getMstProductFamilyIAS());
        quoTefamily.setQuoteToLe(getQuoteToLe());
        return quoTefamily;

    }

    /**
     * getQuoteToLeFamily-mock values
     *
     * @return {@link QuoteToLeProductFamily}
     */
    public QuoteToLeProductFamily getQuoteToLeFamily1() {
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        quoTefamily.setId(1);
        quoTefamily.toString();
        quoTefamily.setQuoteToLe(getQuoteToLe1());
        return quoTefamily;

    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     */
    public Set<ProductSolution> getProductSolution() {

        Set<ProductSolution> productSolutions = new HashSet<>();
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setMstProductOffering(getMstOffering());
        productSolution.setQuoteIllSites(getIllsitesList());
        ProductSolution productSolution1 = new ProductSolution();
        productSolution1.setId(1);
        productSolution1.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution1.setMstProductOffering(getMstOffering());
        productSolution1.setQuoteIllSites(getIllsitesList());
        productSolutions.add(productSolution);
        productSolutions.add(productSolution1);
        /* productSolution.toString(); */
        return productSolutions;
    }

    public List<ProductSolution> getProductSolutionList() {

        List<ProductSolution> productSolutions = new ArrayList<>();
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setMstProductOffering(getMstOffering());
        productSolution.setQuoteIllSites(getIllsitesList());
        ProductSolution productSolution1 = new ProductSolution();
        productSolution1.setId(1);
        productSolution1.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution1.setMstProductOffering(getMstOffering());
        productSolution1.setQuoteIllSites(getIllsitesList());
        productSolutions.add(productSolution);
        productSolutions.add(productSolution1);
        /* productSolution.toString(); */
        return productSolutions;
    }

    /**
     * getSolution-mock values
     *
     * @return {@link ProductSolution}
     */
    public ProductSolution getSolution() {
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        /* productSolution.toString(); */
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setMstProductOffering(getMstOffering());
        productSolution.setQuoteIllSites(getIllsitesList());
        productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily());
        productSolution.setTpsSfdcProductId("1");
        productSolution.setTpsSfdcProductName("sfdc");

        return productSolution;
    }

    /**
     * getSolution-mock values
     *
     * @return {@link ProductSolution}
     */
    public ProductSolution getSolution1() {
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");

        productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily1());
        return productSolution;
    }

    public ProductSolution getSolution2() {
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"Service Variant\",\"value\":\"CISCO\"}]}]}");

        productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily1());
        return productSolution;
    }

    /**
     * getIllsites-mock values
     *
     * @return {@link IllSite}
     */
    public QuoteIllSite getIllsites() {
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        site.setStatus((byte) 1);

        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        site.setErfLocSiteaLocationId(1);
        site.setErfLocSitebLocationId(1);

        site.setProductSolution(getSolution1());

        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);
        site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
        return site;
    }

    /**
     * getIllsites-mock values
     *
     * @return {@link IllSite}
     */
    public QuoteIllSite getIllsites4() {
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        site.setStatus((byte) 1);
        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        site.setErfLocSiteaLocationId(1);
        site.setErfLocSitebLocationId(1);

        site.setProductSolution(getSolution1());

        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);
        site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSetWithSlaMaster());
        return site;
    }

    /**
     * getIllsites-mock values
     *
     * @return {@link IllSite}
     */
    public QuoteIllSite getIllsites1() {
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        site.setStatus((byte) 1);
        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        // site.setErfLocSiteaLocationId(1);
        // site.setErfLocSitebLocationId(1);

        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);

        site.setErfLocSiteaLocationId(2);
        // site.setErfLocSiteaSiteCode("1");
        site.setErfLocSitebLocationId(2);
        site.setErfLocSitebSiteCode("2");
        return site;
    }

    /**
     * getIllsites-mock values
     *
     * @return {@link IllSite}
     */
    public QuoteIllSite getIllsites2() {
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        site.setStatus((byte) 1);
        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);
        // site.setErfLocSiteaLocationId(2);
        // site.setErfLocSitebLocationId(2);
        return site;
    }

    /**
     * getIllsitesList-mock values
     *
     * @return {@link IllSite}
     */
    public Set<QuoteIllSite> getIllsitesList() {
        Set<QuoteIllSite> illSites = new HashSet<>();
        illSites.add(getIllsites1());
        illSites.add(getIllsites2());
        return illSites;
    }

    public Set<QuoteIllSite> getIllsitesList1() {
        Set<QuoteIllSite> illSites = new HashSet<>();
        illSites.add(getIllsites1());
        return illSites;
    }

    /**
     * getIllsitesMock-mock values
     *
     * @return {@link IllSite}
     */
    public List<QuoteIllSite> getIllsitesMock() {
        List<QuoteIllSite> illSites = new ArrayList<>();
        illSites.add(getIllsites());
        return illSites;
    }

    /**
     * getQuoteProductComponent-mock values
     *
     * @return {@link QuoteProductComponent}
     */
    public List<QuoteProductComponent> getQuoteProductComponent() {
        List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
        QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
        quoteProductComponent.setId(1);
        quoteProductComponent.setReferenceId(1);
        quoteProductComponent.setMstProductComponent(getMstProductComponent());
        quoteProductComponents.add(quoteProductComponent);
        quoteProductComponent.setMstProductFamily(geProductFamily());
        /* quoteProductComponent.toString(); */
        quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
        quoteProductComponents.add(quoteProductComponent);
        return quoteProductComponents;
    }

    /**
     * getQuoteComponentsAttributeValues-mock values
     *
     * @return {@link QuoteProductComponentsAttributeValue}
     */
    public Set<QuoteProductComponentsAttributeValue> getQuoteComponentsAttributeValues() {
        Set<QuoteProductComponentsAttributeValue> mockProductComponentAttr = new HashSet<>();

        mockProductComponentAttr.add(getAttribute());
        return mockProductComponentAttr;
    }

    public QuoteProductComponentsAttributeValue getAttribute() {
        QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
        attributeValue.setId(1);
        attributeValue.setDisplayValue("CPE");
        attributeValue.setAttributeValues("1");
        attributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
        /* attributeValue.toString(); */

        return attributeValue;
    }

    /**
     * getProductAtrributeMaster-mock values
     *
     * @return {@link ProductAttributeMaster}
     */
    public List<ProductAttributeMaster> getProductAtrributeMaster() {
        List<ProductAttributeMaster> attributeMasters = new ArrayList<>();
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("CPE");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(1);
        attributeMasters.add(productAttributeMaster);

        return attributeMasters;
    }

    public List<ProductAttributeMaster> getProductAtrributeMasterForNull() {
        List<ProductAttributeMaster> attributeMasters = new ArrayList<>();
        attributeMasters.add(null);
        return attributeMasters;
    }

    public ProductAttributeMaster getProductAtrributeMas() {
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("Burstable Bandwidth");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(1);

        return productAttributeMaster;
    }

    public ProductAttributeMaster getProductAtrributeMas2() {
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("Burstable Bandwidth");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(2);

        return productAttributeMaster;
    }

    /**
     * return mst product component lsit
     *
     * @return
     */
    public List<MstProductComponent> getMstProductComponentList() {
        List<MstProductComponent> list = new ArrayList<>();
        list.add(getMstProductComponent());
        list.add(getMstProductComponent());
        return list;
    }

    public List<MstProductComponent> getMstProductComponentListForNull() {
        List<MstProductComponent> list = new ArrayList<>();
        list.add(null);
        return list;
    }

    /**
     * getMstProductComponent-mock values
     *
     * @return {@link MstProductComponent}
     */
    public MstProductComponent getMstProductComponent() {
        MstProductComponent component = new MstProductComponent();
        component.setDescription("ILL descriptive Value");
        component.setId(1);
        component.setName("CPE");
        // component.toString();
        return component;
    }

    /**
     * getMstProductComponent-mock values
     *
     * @return {@link MstProductComponent}
     */
    public MstProductComponent getMstProductComponent1() {
        MstProductComponent component = new MstProductComponent();
        component.setDescription("Last mile");
        component.setId(1);
        component.setName("Last mile");
        component.toString();
        return component;
    }

    /**
     * getMstProductComponent-mock values
     *
     * @return {@link MstProductComponent}
     */
    public MstProductComponent getMstProductComponent2() {
        MstProductComponent component = new MstProductComponent();
        component.setDescription("Internet Port");
        component.setId(1);
        component.setName("Internet Port");
        component.toString();
        return component;
    }

    /**
     * getMstProductComponent-mock values
     *
     * @return {@link MstProductComponent}
     */
    public MstProductComponent getMstProductComponent3() {
        MstProductComponent component = new MstProductComponent();
        component.setDescription("Addon");
        component.setId(1);
        component.setName("Addon");
        component.toString();
        return component;
    }

    /**
     * geQuotePrice-mock values
     *
     * @return {@link QuotePrice}
     */
    public QuotePrice geQuotePrice() {

        QuotePrice quotePrice = new QuotePrice();
        quotePrice.setId(1);
        quotePrice.setCatalogMrc(90.3D);
        quotePrice.setCatalogNrc(89.6D);
        quotePrice.setReferenceName("CPE");
        quotePrice.setReferenceId(String.valueOf(1));
        quotePrice.setComputedMrc(676D);
        quotePrice.setEffectiveArc(101.1D);
        quotePrice.setEffectiveNrc(40.6D);
        quotePrice.setComputedNrc(34D);
        quotePrice.setDiscountInPercent(314.7D);
        quotePrice.setQuoteId(1);
        quotePrice.setMinimumMrc(767.7D);
        quotePrice.setMinimumNrc(787.7D);
        /* quotePrice.toString(); */
        return quotePrice;
    }

    public Set<Engagement> geEngagementSet() {
        Set<Engagement> setAttri = new HashSet<>();
        setAttri.add(getEngagement());
        return setAttri;
    }

    public Engagement getEngagement() {
        Engagement engagement = new Engagement();
        engagement.setId(1);
        engagement.setEngagementName("test");
        // engagement.setMstProductFamily(getMstProductFamily());
        engagement.setStatus((byte) 1);
        return engagement;
    }

    public Set<MstProductOffering> geMstProductOfferingSet() {
        Set<MstProductOffering> setAttri = new HashSet<>();
        setAttri.add(getMstProductOffering());
        return setAttri;
    }

    public MstProductOffering getMstProductOffering() {
        MstProductOffering engagement = new MstProductOffering();
        engagement.setErfProductOfferingId(1);
        engagement.setId(1);
        engagement.setMstProductFamily(geProductFamily());
        engagement.setProductDescription("description");
        engagement.setProductName("ias");
        engagement.setProductSolutions(getProductSolution());
        engagement.setStatus((byte) 1);
        return engagement;
    }

    /**
     * geProductFamily -mock values
     *
     * @return {@link MstProductFamily}
     */
    public MstProductFamily geProductFamily() {
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("Cpe");
        mstProductFamily.setStatus((byte) 0);
        mstProductFamily.setEngagements(geEngagementSet());
        mstProductFamily.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
        // mstProductFamily.setMstProductOfferings(geMstProductOfferingSet());
        // mstProductFamily.setOrderPrices(getOrderPriceSet());
        // mstProductFamily.setOrderProductComponents(getOrderProductComponentSet());
        // mstProductFamily.setOrderToLeProductFamilies(createOrderToLeProductFamilySet());
        return mstProductFamily;
    }

    /**
     * getUser -mock values
     *
     * @return- getUser
     */
    public User getUser() {
        User user = new User();

        user.setCustomer(getCustomer());
        /* user.toString(); */
        user.setId(1);
        user.setEmailId("vivek@mail.com");
        user.setEmailId("abc@tata.com");
        user.setFirstName("abc");
        user.setUsername("abc");
        return user;
    }

    /**
     * getMstOffering-mock values
     *
     * @return {@link MstProductOffering}
     */
    public MstProductOffering getMstOffering() {
        MstProductOffering mstProductOffering = new MstProductOffering();
        mstProductOffering.setId(1);
        mstProductOffering.setErfProductOfferingId(1);
        mstProductOffering.setProductDescription("viek");
        mstProductOffering.setProductName("Vive");
        mstProductOffering.setMstProductFamily(geProductFamily());
        return mstProductOffering;
    }

    public Customer getUserList() {
        Set<User> users = new HashSet<>();
        User user = new User();
        Customer customer = new Customer();
        user.setContactNo("1234567890");
        user.setEmailId("demo@demouser.com");
        user.setFirstName("Demo");
        user.setId(1);
        user.setLastName("User");
        user.setStatus(1);
        user.setUsername("Demo User");
        user.setUserType("OTHERS");
        user.setPartnerId(1);
        users.add(user);
        QuoteToLe quoteToLes = new QuoteToLe();
        quoteToLes.setQuote(getQuote());
        quoteToLes.setCurrencyId(1);
        quoteToLes.setErfCusCustomerLegalEntityId(1);
        quoteToLes.setFinalMrc(124.3D);
        quoteToLes.setFinalNrc(123.4D);
        quoteToLes.setId(1);
        quoteToLes.setErfCusSpLegalEntityId(1);
        quoteToLes.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        Set<Quote> quoteSet = new HashSet<>();
        quoteSet.add(getQuote());

        customer.setCustomerCode("FEDEX");
        customer.setCustomerEmailId("enquiry@fedex.com");
        customer.setCustomerName("FedEx");
        customer.setId(1);
        customer.setStatus((byte) 2);
        customer.setUsers(users);
        customer.setQuotes(quoteSet);

        return customer;
    }

    public QuoteDelegation getQuoteDelegation() {
        QuoteDelegation quoteDelegation = new QuoteDelegation();
        quoteDelegation.setAssignTo(3);
        Timestamp timeStamp = new Timestamp(0);
        quoteDelegation.setId(1);
        quoteDelegation.setCreatedTime(timeStamp);
        quoteDelegation.setInitiatedBy(1);
        quoteDelegation.setIpAddress("127.0.0.1");
        quoteDelegation.setIsActive((byte) 1);
        quoteDelegation.setParentId(0);
        quoteDelegation.setQuoteToLe(getQuoteToLe());
        quoteDelegation.setRemarks("Remarks");
        quoteDelegation.setStatus("Rdf");
        quoteDelegation.setTargetDate(timeStamp);
        quoteDelegation.setType("TY");
        /* quoteDelegation.toString(); */
        return quoteDelegation;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamily() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("ILL");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolution());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamily1() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("GVPN");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolution());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    /**
     * getUserEntity- user entity objects
     *
     * @return List<User>
     */
    public Set<User> getUserEntity() {
        Set<User> users = new HashSet<>();
        User user1 = new User();
        user1.setContactNo("000010101");
        Customer customer = new Customer();
        customer.setId(1);
        user1.setCustomer(customer);
        user1.setEmailId("test@e.com");
        user1.setFirstName("test");
        user1.setLastName("test");
        user1.setPartnerId(1);
        user1.setStatus(1);
        user1.setId(1);
        user1.setUsername("test");
        user1.setUserType("customer");
        users.add(user1);
        return users;
    }

    /**
     * getUpdateRequest
     *
     * @return
     */
    public UpdateRequest getUpdateRequest() {
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setFamilyName("test");
        request.setFamilyName("Test");
        request.getComponentDetails().addAll(getcompDetails());
        request.setAttributeDetails(getAttributes());
        return request;
    }

    public List<UpdateRequest> getUpdateRequestList() {
        List<UpdateRequest> list = new ArrayList<>();
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setFamilyName("test");
        request.setFamilyName("Test");
        request.getComponentDetails().addAll(getcompDetails());
        request.setAttributeDetails(getAttributes());
        list.add(request);
        return list;
    }

    /**
     * getUpdateRequest
     *
     * @return
     */
    public UpdateRequest getUpdateRequest2() {
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setFamilyName("test");
        request.setFamilyName("Test");
        request.getComponentDetails().addAll(getcompDetails());
        request.setAttributeDetails(getAttributes());
        return request;
    }

    /**
     * getUpdateRequest
     *
     * @return
     */
    public UpdateRequest getUpdateRequest1() {
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setFamilyName("test");
        request.setFamilyName("Test");
        request.getComponentDetails().addAll(getcompDetails1());
        request.setAttributeDetails(getAttributes3());
        return request;
    }

    /**
     * getUpdateRequest
     *
     * @return
     */
    public UpdateRequest getUpdateRequest3() {
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setFamilyName("test");
        request.setFamilyName("Test");
        request.getComponentDetails().addAll(getcompDetails3());
        request.setAttributeDetails(getAttributes3());
        return request;
    }

    /**
     * getUpdateRequestList
     *
     * @return
     */
    public UpdateRequest getNplUpdateRequest() {
        UpdateRequest nplRequest = new UpdateRequest();
        List<UpdateRequest> requests = new ArrayList<UpdateRequest>();
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.setOrderToLeId(1);
        request.setAttributeName("testAttribute");
        request.setFamilyName("NPL");
        request.setSiteId(1);
        request.setLocalITContactId(1);
        request.setQuoteId(1);
        request.setQuoteToLe(1);
        request.getComponentDetails().addAll(getcompDetails());
        requests.add(request);
        return nplRequest;
    }

    /**
     * This Returns Oms AttachmenRequest
     *
     * @return
     */
    public OmsListenerBean createOmsAttachment() {
        OmsListenerBean bean = new OmsListenerBean();
        List<OmsAttachBean> beanList = new ArrayList<>();
        OmsAttachBean request = new OmsAttachBean();

        request.setAttachmentId(1);
        request.setAttachmentType("Tax");
        request.setOrderLeId(1);
        request.setQouteLeId(1);
        request.setReferenceId(1);
        request.setReferenceName("Site");
        beanList.add(request);
        bean.setOmsAttachBean(beanList);
        return bean;
    }

    public OmsAttachment createOmsAttachMent() {

        OmsAttachment oa = new OmsAttachment();
        oa.setAttachmentType("Tax");
        oa.setErfCusAttachmentId(1);
        oa.setId(1);
        oa.setQuoteToLe(getQuoteToLe());
        oa.setOrderToLe(getOrderToLes());
        oa.setQuoteToLe(getQuoteToLe());
        oa.setReferenceId(1);

        return oa;

    }

    public OmsAttachment createOmsAttachMentWithoutQuoteToLe() {

        OmsAttachment oa = new OmsAttachment();
        oa.setAttachmentType("Tax");
        oa.setErfCusAttachmentId(1);
        oa.setId(1);
        oa.setQuoteToLe(null);
        oa.setOrderToLe(null);
        oa.setQuoteToLe(null);
        oa.setReferenceId(1);

        return oa;

    }

    public OrderProductSolution getOrderProductSolution() {

        OrderProductSolution orderProductSolution = new OrderProductSolution();
        orderProductSolution.setId(1);

        orderProductSolution.setMstProductOffering(getMstOffering());
        Set set = new HashSet<>();
        set.add(getOrderIllSite());
        orderProductSolution.setOrderIllSites(set);
        return orderProductSolution;
    }

    public MstProductFamily getMstProductFamily() {
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("Cpe");
        return mstProductFamily;
    }

    public MstProductFamily getMstProductFamilyIAS() {
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("IAS");
        return mstProductFamily;
    }

    public OrderToLeProductFamily getorderToLeProductFamilies() {
        OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
        orderToLeProductFamily.setId(1);
        orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
        Set<OrderProductSolution> set = new HashSet<>();
        set.add(getOrderProductSolution());
        orderToLeProductFamily.setOrderProductSolutions(set);
        orderToLeProductFamily.setOrderToLe(getOrderToLes1());
        return orderToLeProductFamily;
    }

    public OrderToLeProductFamily getorderToLeProductFamilies1() {
        OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
        orderToLeProductFamily.setId(1);
        orderToLeProductFamily.setMstProductFamily(getMstProductFamily());

        return orderToLeProductFamily;
    }

    public Set<OrderToLeProductFamily> createOrderToLeProductFamilySet() {
        Set<OrderToLeProductFamily> setAttri = new HashSet<>();
        setAttri.add(getorderToLeProductFamilies());
        return setAttri;
    }

    public OrderToLe getOrderToLes() {
        OrderToLe orderToLe = new OrderToLe();
        Set set = new HashSet<>();
        set.add(getorderToLeProductFamilies());
        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);

        orderToLe.setOrder(getOrder());
        Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
        setAttri.add(getOrdersLeAttributeValue());
        orderToLe.setOrdersLeAttributeValues(setAttri);
        orderToLe.setOrderToLeProductFamilies(set);

        return orderToLe;
    }

    public OrderToLe getOrderToLes2() {
        OrderToLe orderToLe = new OrderToLe();
        Set set = new HashSet<>();
        set.add(getorderToLeProductFamilies1());

        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setOrderToLeProductFamilies(set);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);
        return orderToLe;
    }

    public List<OrdersLeAttributeValue> returnOrdersLeAttributeValueList() {
        List<OrdersLeAttributeValue> ordersLeAttributeList = new ArrayList<>();
        ordersLeAttributeList.add(getOrdersLeAttributeValue());
        return ordersLeAttributeList;
    }

    public OrderToLe getOrderToLesForGetOrder(Order orders) {
        OrderToLe orderToLe = new OrderToLe();
        Set set = new HashSet<>();
        set.add(getorderToLeProductFamilies());
        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);
        orderToLe.setOrder(orders);
        orderToLe.setOrderToLeProductFamilies(set);

        orderToLe.setOrder(getOrder());
        Set ordersLeAttributeValue = new HashSet<>();
        ordersLeAttributeValue.add(getOrdersLeAttributeValue());
        orderToLe.setOrdersLeAttributeValues(ordersLeAttributeValue);
        return orderToLe;
    }

    public Order getOrders() {
        Order orders = new Order();

        Set set = new HashSet<>();
        set.add(getOrderToLesForGetOrder(orders));
        orders.setId(1);
        orders.setCustomer(getCustomer());
        orders.setOrderToLes(set);

        orders.setStatus((byte) 1);
        Set set1 = new HashSet<>();
        set1.add(getOrderToLesForGetOrder(orders));
        orders.setOrderToLes(set1);
        return orders;
    }

    public List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(getOrder());
        return orderList;
    }

    public OrderToLe getOrderToLes1() {
        OrderToLe orderToLe = new OrderToLe();
        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);

        orderToLe.setOrder(getOrder());
        return orderToLe;
    }

    public OrderProductComponentsAttributeValue createOrderProducts() {

        OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
        orderProductComponentsAttributeValue.setId(1);

        orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
        return orderProductComponentsAttributeValue;
    }

    public List<OrderProductComponentsAttributeValue> createOrderProductsList() {
        List<OrderProductComponentsAttributeValue> list = new ArrayList<>();
        OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
        orderProductComponentsAttributeValue.setId(1);

        orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
        orderProductComponentsAttributeValue.setAttributeValues("1");
        list.add(orderProductComponentsAttributeValue);
        return list;
    }

    public OrderIllSite getOrderIllSite() {
        OrderIllSite orderIllSite = new OrderIllSite();
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setRequestorDate(new Date());

        orderIllSite.setId(1);
        orderIllSite.setCreatedBy(1);
        orderIllSite.setCreatedTime(new Date());
        orderIllSite.setEffectiveDate(new Date());
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setErfLocSiteaSiteCode("code");
        orderIllSite.setFeasibility((byte) 1);
        orderIllSite.setStatus((byte) 1);
        orderIllSite.setStage("PROVISION_SITES");
        orderIllSite.setSiteCode("TestCode");
        orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
        orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
        orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());

        return orderIllSite;
    }

    public List<OrderToLe> getOrderToLesList() {
        List<OrderToLe> list = new ArrayList<>();
        list.add(getOrderToLes());
        list.add(getOrderToLes());
        list.add(getOrderToLes());
        return list;
    }

    public List<OrderProductSolution> getOrderProductSolutionList() {
        List<OrderProductSolution> list = new ArrayList<>();
        list.add(getOrderProductSolution());
        return list;
    }

    public List<OrderIllSite> getOrderIllSiteList() {
        List<OrderIllSite> list = new ArrayList<>();
        list.add(getOrderIllSite());
        list.add(getOrderIllSite());
        return list;
    }

    public OrderProductComponent getOrderProductComponent() {
        OrderProductComponent orderProductComponent = new OrderProductComponent();
        orderProductComponent.setId(1);

        orderProductComponent.setMstProductComponent(getMstProductComponent());
        Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
        set.add(createOrderProducts());
        orderProductComponent.setOrderProductComponentsAttributeValues(set);
        return orderProductComponent;

    }

    public Set<OrderProductComponent> getOrderProductComponentSet() {
        Set<OrderProductComponent> set = new HashSet<>();
        set.add(getOrderProductComponent());
        return set;
    }

    public List<OrderProductComponent> getOrderProductComponentList() {
        List<OrderProductComponent> list = new ArrayList<>();
        list.add(getOrderProductComponent());
        return list;
    }

    public List<OrderToLeProductFamily> getorderToLeProductFamiliesList() {
        List<OrderToLeProductFamily> list = new ArrayList<>();
        list.add(getorderToLeProductFamilies());
        return list;
    }

    public OrderPrice getOrderPrice() {
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setId(1);
        orderPrice.setQuoteId(1);
        orderPrice.setVersion(1);
        return orderPrice;
    }

    public Set<OrderPrice> getOrderPriceSet() {
        Set<OrderPrice> set = new HashSet<>();
        set.add(getOrderPrice());
        return set;
    }

    public List<OmsAttachment> getAttachmentsList() {
        List<OmsAttachment> list = new ArrayList<>();
        list.add(createOmsAttachMent());
        return list;
    }

    public List<OmsAttachment> getAttachmentsListNegative() {
        List<OmsAttachment> list = new ArrayList<>();
        list.add(createOmsAttachMentWithoutQuoteToLe());
        return list;
    }

    /**
     * getRootUser -mock values
     *
     * @return- getUser
     */
    public User getRootUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("root");
        /* user.toString(); */
        user.setEmailId("root@gmail.com");
        return user;
    }

    /**
     * getRootUser with quote -mock values
     *
     * @return- getUser
     */
    public User getRootUserWithQuote() {
        User user = new User();
        user.setId(1);
        user.setUsername("root");
        user.setCustomer(getCustomerWithQuote());
        /* user.toString(); */
        user.setEmailId("root@gmail.com");
        return user;
    }

    /**
     * getCustomer with quote -mock values
     *
     * @return {@link Customer}
     */
    public Customer getCustomerWithQuote() {
        Customer customer = new Customer();
        customer.setCustomerCode("123");
        customer.setStatus((byte) 0);
        customer.setId(1);
        customer.setCustomerEmailId("vick@gmail.com");
        customer.setCustomerName("Vivek");
        /* customer.toString(); */
        customer.setUsers(getUserEntity());
        Quote quote = new Quote();
        Set<Quote> quoteSet = new HashSet<Quote>();
        quoteSet.add(getQuote());
        customer.setQuotes(quoteSet);
        return customer;
    }

    /**
     * getOrder with object
     *
     * @return orders
     */
    public Order getOrder() {
        Order orders = new Order();

        Timestamp timeStamp = new Timestamp(0);

        orders.setCreatedBy(1);
        orders.setCreatedTime(timeStamp);
        orders.setCustomer(getCustomer());
        orders.setEffectiveDate(timeStamp);
        orders.setEndDate(timeStamp);
        orders.setId(1);
        Set<OrderToLe> orderToLes = new HashSet<>();
        orderToLes.add(getOrderToLes2());
        orders.setOrderToLes(orderToLes);

        orders.setQuote(getQuote());

        orders.setStage("aa");
        orders.setStartDate(timeStamp);
        orders.setStatus((byte) 1);

        return orders;
    }

    /**
     * getUpdateRequest with Quote id null
     *
     * @return
     */
    public UpdateRequest getUpdateRequestQuoteIdNull() {
        UpdateRequest request = new UpdateRequest();
        request.setQuoteId(null);
        request.getComponentDetails().addAll(getcompDetails());
        return request;
    }

    /**
     * getQuoteToLeList-mock values
     *
     * @return quoteToLeList
     */
    public List<QuoteToLe> getQuoteToLeList() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);

        quoteToLe.setFinalMrc(5d);
        quoteToLe.setFinalNrc(67d);
        quoteToLe.setProposedMrc(78d);
        /* quoteToLe.toString(); */

        quoteToLe.setProposedNrc(98d);

        quoteToLe.setFinalMrc(5d);
        quoteToLe.setFinalNrc(67d);
        quoteToLe.setProposedMrc(78d);
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily1());

        quoteToLe.setQuote(getQuote());
        List<QuoteToLe> quoteToLeList = new ArrayList<>();
        quoteToLeList.add(quoteToLe);

        return quoteToLeList;
    }

    public User craeteUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("test");
        user.setId(1);
        user.setStatus(1);
        user.setCustomer(craeteCustomer());

        return user;

    }

    public User craeteUserForInactiveOrders() {
        User user = new User();
        user.setId(1);
        user.setFirstName("test");
        user.setStatus(1);
        user.setCustomer(craeteCustomerForInActiveOrders());

        return user;

    }

    public Customer craeteCustomer() {

        Customer customer = new Customer();
        customer.setId(1);
        customer.setCustomerEmailId("test@gmail.com");
        Set<Order> set = new HashSet<>();
        set.add(getOrders());
        customer.setOrders(set);
        return customer;
    }

    public QuoteProductComponent craeteQuoteProductComponent() {
        QuoteProductComponent quoteProductComponent = new QuoteProductComponent();

        quoteProductComponent.setId(1);
        quoteProductComponent.setReferenceId(1);
        quoteProductComponent.setMstProductComponent(getMstProductComponent());
        quoteProductComponent.setMstProductFamily(geProductFamily());
        /* quoteProductComponent.toString(); */
        quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
        return quoteProductComponent;
    }

    public List<QuoteProductComponentsAttributeValue> createQuoteProductAttributeValue() {
        List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
        list.add(createQuoteProductComponentsAttributeValue());
        list.add(createQuoteProductComponentsAttributeValue());
        return list;
    }

    public List<QuoteProductComponentsAttributeValue> createQuoteProductAttributeValueGVPN() {
        List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
        list.add(createQuoteProductComponentsAttributeValue());
        list.add(createQuoteProductComponentsAttributeValue2());
        return list;
    }

    public QuoteProductComponentsAttributeValue createQuoteProductComponentsAttributeValue() {
        QuoteProductComponentsAttributeValue qPCAV = new QuoteProductComponentsAttributeValue();
        qPCAV.setAttributeValues("1");
        qPCAV.setDisplayValue("TEST");
        qPCAV.setProductAttributeMaster(getProductAtrributeMas());
        qPCAV.setId(1);
        return qPCAV;
    }

    public QuoteProductComponentsAttributeValue createQuoteProductComponentsAttributeValue2() {
        QuoteProductComponentsAttributeValue qPCAV = new QuoteProductComponentsAttributeValue();
        qPCAV.setAttributeValues("1");
        qPCAV.setDisplayValue("TEST");
        qPCAV.setProductAttributeMaster(getProductAtrributeMas2());
        qPCAV.setId(2);
        return qPCAV;
    }

    public QuotePrice getQuotePrice() {

        QuotePrice qp = new QuotePrice();
        qp.setId(1);
        return qp;

    }

    public QuoteIllSite getQuoteIllSite() {
        QuoteIllSite qIs = new QuoteIllSite();
        qIs.setId(1);
        qIs.setStatus((byte) 1);

        qIs.setErfLocSitebLocationId(2);
        qIs.setProductSolution(getSolution());
        qIs.setQuoteIllSiteSlas(getQuoteIllSiteSlaSet());
        qIs.setFeasibility((byte) 1);
        return qIs;

    }

    public List<QuoteIllSite> getQuoteIllSiteList() {
        List<QuoteIllSite> sites = new ArrayList<>();
        sites.add(getQuoteIllSite());
        return sites;

    }

    public List<QuoteProductComponentsAttributeValue> getquoteProductComponentAttributeValues() {
        List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();

        list.add(craeteproductComponentAttribute());

        return list;
    }

    public QuoteProductComponentsAttributeValue craeteproductComponentAttribute() {
        QuoteProductComponentsAttributeValue qpa = new QuoteProductComponentsAttributeValue();
        qpa.setId(1);
        qpa.setDisplayValue("test");

        qpa.setAttributeValues("test");
        qpa.setProductAttributeMaster(getProductAtrributeMas());
        return qpa;

    }

    public List<ProductSolution> createProductSolutions() {
        List<ProductSolution> list = new ArrayList<>();
        list.add(craeteproductSolutions());

        return list;

    }

    public ProductSolution craeteproductSolutions() {
        ProductSolution p = new ProductSolution();
        p.setId(1);
        p.setMstProductOffering(getMstOffering());
        p.setQuoteIllSites(getIllsitesList());

        return p;
    }

    public Order getOrdersForInActiveStatus() {
        Order orders = new Order();
        Set set = new HashSet<>();
        set.add(getOrderToLes());
        orders.setId(1);
        orders.setCustomer(getCustomer());
        orders.setOrderToLes(set);

        orders.setStatus((byte) 0);
        Set set1 = new HashSet<>();
        set1.add(getOrderToLes());
        orders.setOrderToLes(set1);
        return orders;
    }

    public Customer craeteCustomerForInActiveOrders() {

        Customer customer = new Customer();
        customer.setId(1);
        customer.setCustomerEmailId("test@gmail.com");
        Set<Order> set = new HashSet<>();
        set.add(getOrdersForInActiveStatus());
        customer.setOrders(set);
        return customer;

    }

    public List<QuoteToLeProductFamily> getQuoteToLeFamilyList() {
        List<QuoteToLeProductFamily> list = new ArrayList<>();
        list.add(getQuoteToLeFamily());
        return list;

    }

    public List<QuoteToLeProductFamily> getQuoteToLeFamilyListIAS() {
        List<QuoteToLeProductFamily> list = new ArrayList<>();
        list.add(getQuoteToLeFamilyIAS());
        return list;

    }

    public List<ProductSolution> getSolutionList() {
        List<ProductSolution> list = new ArrayList<>();
        list.add(getSolution());
        return list;

    }

    public List<QuoteIllSite> getListOfQouteIllSites() {

        List<QuoteIllSite> list = new ArrayList<>();
        list.add(getQuoteIllSite());
        list.add(getQuoteIllSite());
        return list;

    }

    public List<QuoteIllSite> getListOfQouteIllSites2() {

        List<QuoteIllSite> list = new ArrayList<>();
        QuoteIllSite site = new QuoteIllSite();
        site.setStatus((byte) 1);
        site.setFeasibility((byte) 1);
        site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
        site.setId(1);
        list.add(site);
        return list;

    }

    public Optional<OrderToLe> returnOrderToLeForUpdateStatus() {
        OrderToLe le = new OrderToLe();
        le.setId(1);
        le.setCurrencyId(1);
        le.setEndDate(new Date());
        le.setErfCusCustomerLegalEntityId(1);
        le.setErfCusSpLegalEntityId(1);
        le.setOrder(getOrdersForInActiveStatus());
        le.setStage("Sample");
        return Optional.of(le);
    }

    public Optional<QuoteToLe> returnQuoteToLeForUpdateStatus() {
        QuoteToLe le = new QuoteToLe();
        le.setId(1);
        le.setCurrencyId(1);
        le.setErfCusCustomerLegalEntityId(1);
        le.setErfCusSpLegalEntityId(1);
        le.setStage("Sample");
        le.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        le.setTpsSfdcOptyId("tpsSfdcOptyId");
        le.setQuote(getQuote1());
        return Optional.of(le);
    }

    public Map<String, Object> getUSerInfp() {
        UserInformation value = new UserInformation();
        value.setUserId("1");
        value.setCustomers(getCustomerList());
        Map<String, Object> map = new HashMap<>();
        map.put("USER_INFORMATION", value);

        return map;
    }

    public Optional<OrderIllSite> returnQuoteDetailForUpdateStatus() {
        OrderIllSite le = new OrderIllSite();
        le.setId(1);
        le.setStage("Sample");
        le.setCreatedBy(1);
        le.setErfLocSiteaLocationId(1);
        le.setErfLocSiteaLocationId(1);
        le.setErfLocSitebLocationId(1);
        return Optional.of(le);
    }

    public Optional<OrderIllSite> getOrderIllSiteOptional() {
        OrderIllSite le = new OrderIllSite();
        le.setId(1);
        le.setStage("Sample");
        le.setCreatedBy(1);
        le.setErfLocSiteaLocationId(1);
        le.setErfLocSiteaLocationId(1);
        le.setErfLocSitebLocationId(1);
        le.setMstOrderSiteStage(getMstOrderSiteStage());
        le.setMstOrderSiteStatus(getMstOrderSiteStatus());
        return Optional.of(le);
    }

    public MstOmsAttribute getMstAttribute() {
        MstOmsAttribute moa = new MstOmsAttribute();
        moa.setId(1);
        moa.setDescription("test");
        moa.setName("Payment Currency");
        return moa;

    }

    public OrdersLeAttributeValue getOrdersLeAttributeValue() {

        OrdersLeAttributeValue slav = new OrdersLeAttributeValue();
        slav.setAttributeValue("Test");
        slav.setDisplayValue("Test");
        slav.setId(1);

        slav.setAttributeValue("rakesh");
        slav.setDisplayValue("Rakesh");
        slav.setMstOmsAttribute(getMstOmsAttribute());

        // slav.setOrderToLe(getOrderToLes());
        slav.setMstOmsAttribute(getMstOmsAttribute());

        return slav;
    }

    public MstOmsAttribute getMstOmsAttribute() {
        MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
        mstOmsAttribute.setId(1);
        mstOmsAttribute.setIsActive((byte) 1);
        mstOmsAttribute.setCreatedBy("Anandhi");
        mstOmsAttribute.setCreatedTime(new Date());
        mstOmsAttribute.setDescription("Description");
        mstOmsAttribute.setName("Test");
        return mstOmsAttribute;

    }

    public QuoteLeAttributeValue getQuoteLeAttributeValue() {
        QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
        quoteLeAttributeValue.setId(1);
        quoteLeAttributeValue.setAttributeValue("IAS");
        quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
        quoteLeAttributeValue.setDisplayValue("display Value");
        return quoteLeAttributeValue;

    }

    public QuoteLeAttributeValue getQuoteLeAttributeValue1() {
        QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
        quoteLeAttributeValue.setId(1);
        quoteLeAttributeValue.setAttributeValue("INR");
        quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
        quoteLeAttributeValue.setDisplayValue("Payment Currency");
        return quoteLeAttributeValue;

    }

    public Set<QuoteLeAttributeValue> getQuoteLeAttributeValueSet() {
        Set<QuoteLeAttributeValue> quoteLeAttributeValueSet = new HashSet<>();
        QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
        quoteLeAttributeValue.getMstOmsAttribute().setName("TermInMonths");

        QuoteLeAttributeValue quoteLeAttributeValue1 = getQuoteLeAttributeValue();
        quoteLeAttributeValue1.setAttributeValue("1");
        quoteLeAttributeValue1.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_ID);

        QuoteLeAttributeValue quoteLeAttributeValue2 = getQuoteLeAttributeValue();
        quoteLeAttributeValue2.setAttributeValue("iv");
        quoteLeAttributeValue2.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NAME);

        QuoteLeAttributeValue quoteLeAttributeValue3 = getQuoteLeAttributeValue();
        quoteLeAttributeValue3.setAttributeValue("1");
        quoteLeAttributeValue3.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_EMAIL);

        QuoteLeAttributeValue quoteLeAttributeValue4 = getQuoteLeAttributeValue();
        quoteLeAttributeValue4.setAttributeValue("1");
        quoteLeAttributeValue4.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NO);

        QuoteLeAttributeValue quoteLeAttributeValue5 = getQuoteLeAttributeValue();
        quoteLeAttributeValue5.setAttributeValue("1");
        quoteLeAttributeValue5.getMstOmsAttribute().setName(LeAttributesConstants.DESIGNATION);
        quoteLeAttributeValueSet.add(quoteLeAttributeValue);
        quoteLeAttributeValueSet.add(quoteLeAttributeValue1);
        quoteLeAttributeValueSet.add(quoteLeAttributeValue2);

        quoteLeAttributeValueSet.add(quoteLeAttributeValue3);

        quoteLeAttributeValueSet.add(quoteLeAttributeValue4);
        quoteLeAttributeValueSet.add(quoteLeAttributeValue5);

        return quoteLeAttributeValueSet;

    }

    public List<CustomerDetail> getCustomerList() {
        List<CustomerDetail> list = new ArrayList<>();
        list.add(createCustomerDetails());
        return list;

    }

    public CustomerDetail createCustomerDetails() {
        CustomerDetail cd = new CustomerDetail();
        cd.setCustomerAcId("test");
        cd.setCustomerEmailId("test@gmail.com");

        cd.setCustomerId(1);
        cd.setCustomerLeId(1);
        cd.setErfCustomerId(1);
        cd.setStatus((byte) 1);
        return cd;

    }

    public UserInformation getUserInformation() {
        UserInformation userInformation = new UserInformation();
        userInformation.setUserId("admin");
        userInformation.setCustomers(getCustomerList());
        userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
        return userInformation;

    }

    public DashBoardBean getDashboardBean() {
        DashBoardBean bean = new DashBoardBean();
        bean.setActiveOrders(1L);
        bean.setActiveSites(1L);
        bean.setTotalOrders(10L);
        bean.setTotalSites(10L);
        return bean;
    }

    public QuoteConfigurations getQuoteConfigurations() {
        QuoteConfigurations quoteconfig = new QuoteConfigurations();
        quoteconfig.setActiveQuotes(getQuoteConfigurationList());
        return quoteconfig;
    }

    public List<QuoteConfiguration> getQuoteConfigurationList() {
        List<QuoteConfiguration> configList = new ArrayList<>();
        QuoteConfiguration config = new QuoteConfiguration();
        config.setCreatedDate(new Date());
        config.setOrderCode("order code");
        config.setOrderCreatedDate(new Date());
        config.setOrderId(1);
        config.setOrderStage("stage");
        config.setProductName("productname");
        config.setQuoteCode("quotecode");
        config.setQuoteCreatedDate(new Date());
        config.setQuoteId(1);
        config.setQuoteStage("stage");
        config.setSiteCount(BigInteger.valueOf(1));
        configList.add(config);
        return configList;
    }

    public OrderConfigurations getOrderConfigurations() {
        OrderConfigurations orderConfig = new OrderConfigurations();
        orderConfig.setOrders(getOrderConfigurationList());
        return orderConfig;
    }

    public List<OrderConfiguration> getOrderConfigurationList() {
        List<OrderConfiguration> configList = new ArrayList<>();
        OrderConfiguration config = new OrderConfiguration();
        config.setOrderCode("order code");
        config.setOrderCreatedDate(new Date());
        config.setOrderId(1);
        config.setProductName("productname");
        configList.add(config);
        return configList;
    }

    public OrdersBean getOrdersBean() {
        OrdersBean bean = new OrdersBean();
        bean.setCheckList("chklist");
        bean.setCreatedBy(1);
        bean.setCreatedTime(new Date());
        bean.setEffectiveDate(new Date());
        bean.setEndDate(new Date());
        bean.setId(1);
        bean.setOrderCode("ordercode");
        return bean;
    }

    /**
     * getOrder with object
     *
     * @return orders
     */
    public Order getOrderWithOutOrderTole() {
        Order orders = new Order();

        Timestamp timeStamp = new Timestamp(0);

        orders.setCreatedBy(1);
        orders.setCreatedTime(timeStamp);
        orders.setCustomer(getCustomer());
        orders.setEffectiveDate(timeStamp);
        orders.setEndDate(timeStamp);
        orders.setId(1);

        orders.setQuote(getQuote());

        orders.setStage("aa");
        orders.setStartDate(timeStamp);
        orders.setStatus((byte) 1);

        return orders;
    }

    public List<OrderToLe> getOrderToLesListForDashBoard() {
        List<OrderToLe> list = new ArrayList<>();
        OrderToLe orderToLe = new OrderToLe();
        Set set = new HashSet<>();
        set.add(getorderToLeProductFamilies());
        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);
        orderToLe.setOrder(getOrder());
        orderToLe.setOrderToLeProductFamilies(set);

        orderToLe.setOrder(getOrderWithOutOrderTole());
        list.add(orderToLe);
        return list;
    }

    public List<OrdersLeAttributeValue> getOrdersLeAttributeValueList() {
        List<OrdersLeAttributeValue> ordersLeAttributeValueList = new ArrayList<>();
        ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
        return ordersLeAttributeValueList;
    }

    public List<QuoteLeAttributeValue> getQuoteLeAttributeValueList() {
        List<QuoteLeAttributeValue> leAttributeValues = new ArrayList<>();
        leAttributeValues.add(getQuoteLeAttributeValue1());
        return leAttributeValues;
    }

    public UpdateRequest returnUpdateRequestForInvalidAttributeId() {
        UpdateRequest updateRequest = new UpdateRequest();
        ComponentDetail componentDetails = new ComponentDetail();
        AttributeDetail attributeDetail = new AttributeDetail();
        List<AttributeDetail> attributeDetailList = new ArrayList<>();
        List<ComponentDetail> componentDetailsList = new ArrayList<>();
        attributeDetail.setAttributeId(1);
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setName("tata");
        attributeDetail.setValue("tata");
        attributeDetailList.add(attributeDetail);
        componentDetails.setAttributes(attributeDetailList);
        componentDetails.setComponentId(1);
        componentDetails.setComponentMasterId(1);
        componentDetailsList.add(componentDetails);
        updateRequest.setSiteId(1);
        updateRequest.setQuoteId(1);
        updateRequest.setComponentDetails(componentDetailsList);
        updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
        return updateRequest;
    }

    public UpdateRequest returnUpdateRequestWithoutComponentDetails() {
        UpdateRequest updateRequest = new UpdateRequest();
        AttributeDetail attributeDetail = new AttributeDetail();
        List<AttributeDetail> attributeDetailList = new ArrayList<>();
        attributeDetail.setAttributeId(1);
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setName("tata");
        attributeDetail.setValue("tata");
        attributeDetailList.add(attributeDetail);
        updateRequest.setSiteId(1);
        updateRequest.setQuoteId(1);
        updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
        return updateRequest;

    }

    public OrderProductSolution getOrderProductSolutionWithOutIll() {
        OrderProductSolution orderProductSolution = new OrderProductSolution();
        orderProductSolution.setId(1);

        orderProductSolution.setMstProductOffering(getMstOffering());
        return orderProductSolution;
    }

    public OrderIllSite getOrderIllSiteForSiteDetails() {
        OrderIllSite orderIllSite = new OrderIllSite();
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setRequestorDate(new Date());

        orderIllSite.setId(1);
        orderIllSite.setCreatedBy(1);
        orderIllSite.setCreatedTime(new Date());
        orderIllSite.setEffectiveDate(new Date());
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setErfLocSiteaSiteCode("code");
        orderIllSite.setFeasibility((byte) 1);
        orderIllSite.setStatus((byte) 1);
        orderIllSite.setStage("PROVISION_SITES");
        orderIllSite.setOrderProductSolution(getOrderProductSolutionWithOutIll());
        return orderIllSite;
    }

    public OrderToLeProductFamily getorderToLeProductFamiliesWithOutProductSolutions() {
        OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
        orderToLeProductFamily.setId(1);
        orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
        return orderToLeProductFamily;
    }

    public OrderProductSolution getOrderProductSolutionWithOrderToLeProductFamiles() {

        OrderProductSolution orderProductSolution = new OrderProductSolution();
        orderProductSolution.setId(1);

        orderProductSolution.setMstProductOffering(getMstOffering());
        Set set = new HashSet<>();
        set.add(getOrderIllSite());
        orderProductSolution.setOrderIllSites(set);
        orderProductSolution.setOrderToLeProductFamily(getorderToLeProductFamiliesWithOutProductSolutions());
        return orderProductSolution;
    }

    public QuoteDetail getNplQuoteDetailForCreateNew() {
        QuoteDetail quoteDetail = new QuoteDetail();
        quoteDetail.setCustomerId(25);
        quoteDetail.setProductName("NPL");
        quoteDetail.setSite(getNplSite());
        quoteDetail.setQuoteleId(298);
        quoteDetail.setQuoteId(298);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setOfferingName("Standard point-to-point conectivity");
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        solution.toString();
        SolutionDetail solution1 = new SolutionDetail();
        solution1.setOfferingName("Standard point-to-point conectivity");
        solution1.setBandwidth("20");
        solution1.setBandwidthUnit("mbps");
        solution1.toString();
        List<ComponentDetail> components = new ArrayList<>();

        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        ComponentDetail component1 = new ComponentDetail();
        component1.setComponentMasterId(1);
        component1.setName("Private Lines");

        ComponentDetail component2 = new ComponentDetail();
        component2.setComponentMasterId(2);
        component2.setName("Connectivity");

        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetail.setName("Sub Product");
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setValue("Standard NPL");
        attributeDetails.add(attributeDetail);

        AttributeDetail attributeDetail1 = new AttributeDetail();
        attributeDetail1.setName("Sub Product");
        attributeDetail1.setAttributeMasterId(25);
        attributeDetail1.setValue("Standard NPL");
        attributeDetails.add(attributeDetail1);

        component1.setAttributes(attributeDetails);
        component2.setAttributes(attributeDetails);
        component1.setIsActive("Y");
        component2.setIsActive("Y");

        components.add(component2);
        components.add(component1);

        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solution.toString();

        solution1.setComponents(components);
        solution1.setImage("");
        solution1.toString();
        solutionDetails.add(solution);
        solutionDetails.add(solution1);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteDetail getNplQuoteDetailForCreateNew1() {
        QuoteDetail quoteDetail = new QuoteDetail();
        quoteDetail.setCustomerId(25);
        quoteDetail.setProductName("NPL");
        quoteDetail.setSite(getNplSite1());
        quoteDetail.setQuoteleId(298);
        quoteDetail.setQuoteId(298);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setOfferingName("Standard point-to-point conectivity");
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        solution.toString();
        SolutionDetail solution1 = new SolutionDetail();
        solution1.setOfferingName("Standard point-to-point conectivity");
        solution1.setBandwidth("20");
        solution1.setBandwidthUnit("mbps");
        solution1.toString();
        List<ComponentDetail> components = new ArrayList<>();

        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        ComponentDetail component1 = new ComponentDetail();
        component1.setComponentMasterId(1);
        component1.setName("Private Lines");

        ComponentDetail component2 = new ComponentDetail();
        component2.setComponentMasterId(2);
        component2.setName("Connectivity");

        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetail.setName("Sub Product");
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setValue("Standard NPL");
        attributeDetails.add(attributeDetail);

        AttributeDetail attributeDetail1 = new AttributeDetail();
        attributeDetail1.setName("Sub Product");
        attributeDetail1.setAttributeMasterId(25);
        attributeDetail1.setValue("Standard NPL");
        attributeDetails.add(attributeDetail1);

        component1.setAttributes(attributeDetails);
        component2.setAttributes(attributeDetails);
        component1.setIsActive("Y");
        component2.setIsActive("Y");

        components.add(component2);
        components.add(component1);

        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solution.toString();

        solution1.setComponents(components);
        solution1.setImage("");
        solution1.toString();
        solutionDetails.add(solution);
        solutionDetails.add(solution1);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteDetail getNplQuoteDetailForCreateNew3() {
        QuoteDetail quoteDetail = new QuoteDetail();
        quoteDetail.setCustomerId(25);
        quoteDetail.setProductName("NPL");
        quoteDetail.setSite(getNplSite3());
        quoteDetail.setQuoteleId(298);
        quoteDetail.setQuoteId(298);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setOfferingName("Standard Point-to-Point Connectivity");
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        solution.toString();
        SolutionDetail solution1 = new SolutionDetail();
        solution1.setOfferingName("Standard Point-to-Point Connectivity");
        solution1.setBandwidth("20");
        solution1.setBandwidthUnit("mbps");
        solution1.toString();
        List<ComponentDetail> components = new ArrayList<>();

        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        ComponentDetail component1 = new ComponentDetail();
        component1.setComponentMasterId(1);
        component1.setName("Private Lines");

        ComponentDetail component2 = new ComponentDetail();
        component2.setComponentMasterId(2);
        component2.setName("Connectivity");

        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetail.setName("Sub Product");
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setValue("Standard NPL");
        attributeDetails.add(attributeDetail);

        AttributeDetail attributeDetail1 = new AttributeDetail();
        attributeDetail1.setName("Sub Product");
        attributeDetail1.setAttributeMasterId(25);
        attributeDetail1.setValue("Standard NPL");
        attributeDetails.add(attributeDetail1);

        component1.setAttributes(attributeDetails);
        component2.setAttributes(attributeDetails);
        component1.setIsActive("Y");
        component2.setIsActive("Y");

        components.add(component2);
        components.add(component1);

        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solution.toString();

        solution1.setComponents(components);
        solution1.setImage("");
        solution1.toString();
        solutionDetails.add(solution);
        solutionDetails.add(solution1);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteDetail getNplQuoteDetailForCreateNew2() {
        QuoteDetail quoteDetail = new QuoteDetail();
        quoteDetail.setCustomerId(25);
        quoteDetail.setProductName("NPL");
        quoteDetail.setSite(getNplSite2());
        quoteDetail.setQuoteleId(298);
        quoteDetail.setQuoteId(298);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setOfferingName("Buy India Point to Point Connectivity");
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        solution.toString();
        SolutionDetail solution1 = new SolutionDetail();
        solution1.setOfferingName("Buy India Point to Point Connectivity2");
        solution1.setBandwidth("20");
        solution1.setBandwidthUnit("mbps");
        solution1.toString();
        List<ComponentDetail> components = new ArrayList<>();

        ComponentDetail component = new ComponentDetail();
        /* component.toString(); */
        components.add(component);
        ComponentDetail component1 = new ComponentDetail();
        component1.setComponentMasterId(1);
        component1.setName("Private Lines");

        ComponentDetail component2 = new ComponentDetail();
        component2.setComponentMasterId(2);
        component2.setName("Connectivity");

        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /* attributeDetail.toString(); */
        attributeDetail.setName("Sub Product");
        attributeDetail.setAttributeMasterId(1);
        attributeDetail.setValue("Standard NPL");
        attributeDetails.add(attributeDetail);

        AttributeDetail attributeDetail1 = new AttributeDetail();
        attributeDetail1.setName("Sub Product");
        attributeDetail1.setAttributeMasterId(25);
        attributeDetail1.setValue("Standard NPL");
        attributeDetails.add(attributeDetail1);

        component1.setAttributes(attributeDetails);
        component2.setAttributes(attributeDetails);
        component1.setIsActive("Y");
        component2.setIsActive("Y");

        components.add(component2);
        components.add(component1);

        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solution.toString();

        solution1.setComponents(components);
        solution1.setImage("");
        solution1.toString();
        solutionDetails.add(solution);
        solutionDetails.add(solution1);
        quoteDetail.setSolutions(solutionDetails);

        return quoteDetail;
    }

    public QuoteNplLink getQuoteLinkNpl() {
        QuoteNplLink quoteDetail = new QuoteNplLink();
        quoteDetail.setId(1);
        quoteDetail.setLinkCode("AS3GMIGZZFXRLMGP");
        quoteDetail.setProductSolutionId(419);
        quoteDetail.setSiteAId(905);
        quoteDetail.setSiteBId(906);
        quoteDetail.setQuoteId(298);
        quoteDetail.setStatus((byte) 1);
        return quoteDetail;

    }

    public List<MstOmsAttribute> getMstAttributeList() {
        List<MstOmsAttribute> attributesList = new ArrayList<>();
        attributesList.add(getMstAttribute());
        return attributesList;
    }

    public List<MstOmsAttribute> getMstAttributeListWithNullAttr() {
        List<MstOmsAttribute> attributesList = new ArrayList<>();
        attributesList.add(null);
        return attributesList;
    }

    public Set<OrdersLeAttributeValue> setOrdersLeAttributeValue() {
        Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
        setAttri.add(getOrdersLeAttributeValue());
        return setAttri;
    }

    public List<SiteFeasibility> getSiteFeasibilities() {

        List<SiteFeasibility> fblist = new ArrayList<>();
        fblist.add(siteFeasibilities());
        return fblist;
    }

    private SiteFeasibility siteFeasibilities() {

        SiteFeasibility sFb = new SiteFeasibility();
        sFb.setId(1);
        sFb.setFeasibilityCode("Code");
        sFb.setFeasibilityCheck("check");
        sFb.setFeasibilityMode("mode");
        sFb.setIsSelected((byte) 1);
        sFb.setCreatedTime(new Timestamp(0));
        sFb.setType("Type");
        sFb.setProvider("provider");
        sFb.setResponseJson(
                "{\"lm_nrc_ospcapex_onwl\":0.0,\"BW_mbps_2\":2.0,\"Service_ID\":0.0,\"total_cost\":132160.0,\"X0.5km_avg_dist\":9999999.0,\"POP_DIST_KM\":0.5100730350500826,\"X0.5km_prospect_avg_bw\":45.2727272727273,\"Latitude_final\":13.0184111,\"OnnetCity_tag\":1.0,\"lm_arc_bw_prov_ofrf\":0.0,\"Network_F_NF_CC\":\"NA\",\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":40000.0,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":120.0,\"access_check_CC\":\"NA\",\"rank\":10,\"POP_DIST_KM_SERVICE_MOD\":15.0,\"X5km_max_bw\":200.0,\"X5km_prospect_perc_feasible\":0.926775147928994,\"lm_arc_bw_onrf\":0.0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0.0,\"FATG_Building_Type\":\"Commercial\",\"HH_0_5_access_rings_F\":\"NA\",\"hh_flag\":\"NA\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2738.92671182687,\"latitude_final\":13.0184111,\"FATG_Network_Location_Type\":\"Access\\/Customer POP\",\"topology\":\"primary_active\",\"BW_mbps_act\":2.0,\"city_tier\":\"Non_Tier1\",\"lm_nrc_bw_onwl\":0.0,\"X2km_prospect_min_dist\":2.21569108223123,\"X2km_prospect_perc_feasible\":0.94300518134715,\"X0.5km_prospect_num_feasible\":33.0,\"cpe_supply_type\":\"rental\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"Capex\":\"NA\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"Burstable_BW\":0.0,\"connected_cust_tag\":0.0,\"net_pre_feasible_flag\":1.0,\"Network_F_NF_CC_Flag\":\"NA\",\"Orch_LM_Type\":\"Onnet\",\"X5km_prospect_avg_bw\":201.780650887574,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"Feasibility.Response..Created.Date\":17730.0,\"X2km_prospect_min_bw\":2.0,\"X5km_prospect_num_feasible\":1253.0,\"Selected\":false,\"lm_nrc_mast_onrf\":0.0,\"X5km_prospect_count\":1352.0,\"connection_type\":\"Premium\",\"X0.5km_avg_bw\":9999999.0,\"pool_size\":0,\"X2km_cust_count\":2.0,\"Customer_Segment\":\"Enterprise - Strategic\",\"customer_segment\":\"Enterprise - Strategic\",\"FATG_PROW\":\"No\",\"connected_building_tag\":0.0,\"FATG_DIST_KM\":41.33379039696604,\"X0.5km_prospect_max_bw\":500.0,\"X5km_prospect_min_dist\":2.21569108223123,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"scenario_1\":0.0,\"scenario_2\":1.0,\"sno\":3,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":386.0,\"min_hh_fatg\":41.33379039696604,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":1616.32490273161,\"X2km_avg_dist\":1616.32490273161,\"X2km_prospect_avg_bw\":390.502590673575,\"X0.5km_prospect_min_dist\":2.21569108223123,\"cost_permeter\":0.0,\"Identifier\":\"NA\",\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":200.0,\"X2km_prospect_max_bw\":10240.0,\"last_mile_contract_term\":\"1 Year\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":120.0,\"burstable_bw\":0,\"seq_no\":141,\"bw_mbps\":2,\"X0.5km_cust_count\":0.0,\"X0.5km_min_bw\":9999999.0,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"NA\",\"HH_0_5_access_rings_NF\":\"NA\",\"X0.5km_min_dist\":9999999.0,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":48.632402798319305,\"X0.5km_prospect_min_bw\":2.0,\"POP_DIST_KM_SERVICE\":14.152705282191185,\"lm_arc_bw_onwl\":33350.0,\"num_connected_building\":0.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Capex less than 175m\",\"X0.5km_prospect_avg_dist\":243.087859734992,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810.0,\"num_connected_cust\":0.0,\"HH_0_5km\":\"NA\",\"X5km_min_dist\":1424.59360546586,\"feasibility_response_created_date\":\"2018-07-18\",\"X2km_prospect_num_feasible\":364.0,\"additional_ip\":0,\"X2km_avg_bw\":160.0,\"X0.5km_max_bw\":9999999.0,\"X5km_avg_bw\":160.0,\"Status.v2\":\"NA\",\"X5km_prospect_max_bw\":10240.0,\"X2km_prospect_avg_dist\":1361.96557355991,\"cpe_variant\":\"None\",\"Longitude_final\":80.18774819999999,\"X5km_cust_count\":2.0,\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"X0.5km_prospect_perc_feasible\":1.0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":33.0,\"Probabililty_Access_Feasibility\":0.8533333333333334,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\",\"X2km_min_dist\":1424.59360546586,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"NA\"},{\"lm_nrc_ospcapex_onwl\":0.0,\"total_cost\":62375.757575757576,\"otc_tikona\":10000.0,\"Latitude_final\":13.0184111,\"lm_arc_bw_prov_ofrf\":35000.0,\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":0.0,\"offnet_5km_Min_accuracy_num\":50.0,\"interim_BW\":2.0,\"offnet_5km_Min_DistanceKilometers\":1.2178610509953627,\"rank\":5,\"offnet_0_5km_Max_BW_Mbps\":0.0,\"lm_arc_bw_onrf\":0.0,\"prospect_0_5km_Min_BW_Mbps\":0.0,\"lm_nrc_bw_prov_ofrf\":10000.0,\"local_loop_interface\":\"FE\",\"bw_flag_32\":0.0,\"arc_tikona\":35000.0,\"latitude_final\":13.0184111,\"topology\":\"primary_active\",\"min_mast_ht\":0.0,\"lm_nrc_bw_onwl\":0.0,\"cpe_supply_type\":\"rental\",\"offnet_2km_Min_DistanceKilometers\":1.2178610509953627,\"prospect_2km_feasibility_pct\":0.7636363636363637,\"Orch_Connection\":\"Wireless\",\"bw_flag_3\":0.0,\"offnet_2km_cust_Count\":2.0,\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"prospect_2km_Avg_DistanceKilometers\":1.4529000280961837,\"offnet_5km_Max_BW_Mbps\":16.0,\"prospect_2km_Sum_Feasibility_flag\":42.0,\"arc_sify\":45000.0,\"prospect_2km_Avg_BW_Mbps\":2.343709090909091,\"Orch_LM_Type\":\"Offnet\",\"max_mast_ht\":27.0,\"lm_nrc_mast_ofrf\":17375.757575757576,\"Feasibility.Response..Created.Date\":17730.0,\"Selected\":true,\"provider_tot_towers\":0.0,\"lm_nrc_mast_onrf\":0.0,\"connection_type\":\"Premium\",\"offnet_2km_Min_BW_Mbps\":0.512,\"pool_size\":0,\"offnet_0_5km_Min_BW_Mbps\":0.0,\"customer_segment\":\"Enterprise - Strategic\",\"offnet_5km_Avg_BW_Mbps\":1.7612,\"time_taken\":3.1749999999883585,\"prospect_0_5km_Sum_Feasibility_flag\":0.0,\"Local.Loop.Interface\":\"FE\",\"prospect_2km_cust_Count\":55.0,\"Type\":\"OffnetRF\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"prospect_2km_Max_BW_Mbps\":20.0,\"otc_sify\":15000.0,\"Customer.Segment\":\"Enterprise - Strategic\",\"provider_min_dist\":0.7138602916391107,\"offnet_5km_Min_BW_Mbps\":0.128,\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"avg_mast_ht\":9.696969696969697,\"last_mile_contract_term\":\"1 Year\",\"prospect_0_5km_feasibility_pct\":0.0,\"burstable_bw\":0,\"bw_mbps\":2,\"prospect_0_5km_Max_BW_Mbps\":0.0,\"offnet_0_5km_Min_accuracy_num\":0.0,\"product_name\":\"Internet Access Service\",\"closest_provider\":\"tikona_min_dist_km\",\"lm_arc_bw_onwl\":0.0,\"nearest_mast_ht_cost\":56400.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Tikona\",\"lm_nrc_mux_onwl\":0.0,\"offnet_2km_Avg_BW_Mbps\":1.256,\"offnet_2km_Min_accuracy_num\":50.0,\"cust_count\":132.0,\"offnet_0_5km_Avg_BW_Mbps\":0.0,\"feasibility_response_created_date\":\"2018-07-18\",\"prospect_0_5km_Min_DistanceKilometers\":99999.0,\"additional_ip\":0,\"nearest_mast_ht\":18.0,\"cpe_variant\":\"None\",\"prospect_2km_Min_BW_Mbps\":0.064,\"Longitude_final\":80.18774819999999,\"Last.Mile.Contract.Term\":\"1 Year\",\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"offnet_0_5km_Min_DistanceKilometers\":99999.0,\"offnet_0_5km_cust_Count\":0.0,\"prospect_0_5km_Avg_BW_Mbps\":0.0,\"Probabililty_Access_Feasibility\":0.9666666666666667,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\"}");
        return sFb;
    }

    public QuoteNplLink getQuoteNplLink() {
        QuoteNplLink nplLink = new QuoteNplLink();
        nplLink.setChargeableDistance("1");
        nplLink.setCreatedBy(1);
        nplLink.setCreatedDate(new Date());
        nplLink.setId(1);
        nplLink.setLinkCode("sdfsdf");
        nplLink.setProductSolutionId(1);
        nplLink.setQuoteId(1);
        nplLink.setSiteAId(1);
        nplLink.setSiteBId(1);
        nplLink.setStatus((byte) 1);
        nplLink.setWorkflowStatus("sdfsdf");
        return nplLink;
    }

    public List<QuoteNplLink> getQuoteNplLinkList() {
        List<QuoteNplLink> quoteNplLink = new ArrayList<QuoteNplLink>();
        quoteNplLink.add(getQuoteNplLink());
        return quoteNplLink;
    }

    public List<QuoteIllSiteSla> getQuoteIllSiteSlaList() {
        List<QuoteIllSiteSla> list = new ArrayList<>();
        QuoteIllSiteSla sla = new QuoteIllSiteSla();
        sla.setId(1);
        sla.setQuoteIllSite(getQuoteIllSite());
        sla.setSlaEndDate(new Date());
        sla.setSlaMaster(new SlaMaster());
        sla.setSlaStartDate(new Date());
        sla.setSlaValue("sla value");
        list.add(sla);
        return list;
    }

    public Set<QuoteIllSiteSla> getQuoteIllSiteSlaSet() {
        Set<QuoteIllSiteSla> list = new HashSet<>();
        QuoteIllSiteSla sla = new QuoteIllSiteSla();
        sla.setId(1);
        // sla.setQuoteIllSite(getQuoteIllSite());
        sla.setSlaEndDate(new Date());
        sla.setSlaMaster(new SlaMaster());
        sla.setSlaStartDate(new Date());
        sla.setSlaValue("sla value");
        list.add(sla);
        return list;
    }

    public List<SiteFeasibility> getSiteFeasibilityList() {
        List<SiteFeasibility> feasibilities = new ArrayList<>();
        SiteFeasibility feasibility = new SiteFeasibility();
        feasibility.setId(1);
        feasibility.setFeasibilityCheck("Check");
        feasibility.setFeasibilityMode("test");
        feasibilities.add(feasibility);
        return feasibilities;
    }

    public LeProductSla createLeProductSla() {
        LeProductSla leProductSla = new LeProductSla();
        leProductSla.setErfCustomerId(1);
        leProductSla.setErfCustomerLeId(1);
        leProductSla.setId(1);
        leProductSla.setMstProductFamily(getMstProductFamily());
        leProductSla.setSlaTier("tier");
        leProductSla.setSlaValue("sla");
        leProductSla.setSlaMaster(createSlaMaster());
        return leProductSla;
    }

    public List<LeProductSla> createLeProductSlaList() {
        List<LeProductSla> leProductSlaList = new ArrayList<>();
        leProductSlaList.add(createLeProductSla());
        leProductSlaList.add(createLeProductSla());
        return leProductSlaList;
    }

    public Set<QuoteIllSiteSla> createQuoteIllSiteSlaSet() {
        Set<QuoteIllSiteSla> setAttri = new HashSet<>();
        setAttri.add(createQuoteIllSiteSlaWithoutSlaMaster());
        return setAttri;
    }

    public Set<QuoteIllSiteSla> createQuoteIllSiteSlaSetWithSlaMaster() {
        Set<QuoteIllSiteSla> setAttri = new HashSet<>();
        setAttri.add(createQuoteIllSiteSla());
        return setAttri;
    }

    public SlaMaster createSlaMaster() {
        SlaMaster slaMaster = new SlaMaster();
        slaMaster.setId(1);
        slaMaster.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
        slaMaster.setSlaName("sla");
        slaMaster.setSlaDurationInDays(1);
        return slaMaster;
    }

    public QuoteIllSiteSla createQuoteIllSiteSla() {
        QuoteIllSiteSla leProductSla = new QuoteIllSiteSla();
        leProductSla.setId(1);
        leProductSla.setQuoteIllSite(getQuoteIllSite());
        leProductSla.setSlaMaster(createSlaMaster());
        leProductSla.setSlaValue("sla");
        return leProductSla;
    }

    public QuoteIllSiteSla createQuoteIllSiteSlaWithoutSlaMaster() {
        QuoteIllSiteSla leProductSla = new QuoteIllSiteSla();
        leProductSla.setId(1);
        leProductSla.setQuoteIllSite(getQuoteIllSite());
        return leProductSla;
    }

    public List<QuoteIllSiteSla> createQuoteIllSiteSlaList() {
        List<QuoteIllSiteSla> leProductSlaList = new ArrayList<>();
        leProductSlaList.add(createQuoteIllSiteSla());
        leProductSlaList.add(createQuoteIllSiteSla());
        return leProductSlaList;
    }

    public SlaUpdateRequest slaUpdateRequest() {
        SlaUpdateRequest slaUpdateRequest = new SlaUpdateRequest();
        slaUpdateRequest.setProductFamily("ill");
        slaUpdateRequest.setQuoteLeId(1);
        slaUpdateRequest.setSiteId(1);
        return slaUpdateRequest;
    }

    public String createSlaDetailsJson() {
        String jsonString = "";
        return jsonString;
    }

    public SLaDetailsBean createSLaDetailsBean() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Round Trip Delay (ms)");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public String createSLaDetailsBeanList() throws TclCommonException {
        ProductSlaBean productSlaBean = new ProductSlaBean();
        List<SLaDetailsBean> SLaDetailsBeanList = new ArrayList<>();
        SLaDetailsBeanList.add(createSLaDetailsBean());
        productSlaBean.setsLaDetails(SLaDetailsBeanList);
        String json = Utils.convertObjectToJson(productSlaBean).toString();
        return json;
    }

    /**
     * getQuoteFamiles-mock values
     *
     * @return {@link QuoteToLeProductFamily}
     */
    public Set<QuoteToLeProductFamily> getQuoteFamilesWithoutProductSolutions() {
        Set<QuoteToLeProductFamily> mockQoteFamily = new HashSet<>();
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        quoTefamily.toString();
        quoTefamily.setMstProductFamily(getMstProductFamily());
        quoTefamily.setProductSolutions(getProductSolutionwithoutMstOffering());
        mockQoteFamily.add(quoTefamily);

        return mockQoteFamily;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     */
    public Set<ProductSolution> getProductSolutionwithoutMstOffering() {

        Set<ProductSolution> productSolutions = new HashSet<>();
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setQuoteIllSites(getIllsitesList());
        productSolutions.add(productSolution);
        return productSolutions;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public String getAddressDetail() throws TclCommonException {
        AddressDetail addressDetail = new AddressDetail();
        addressDetail.setAddressId(1);
        addressDetail.setAddressLineOne("line1");
        addressDetail.setCity("chennai");
        addressDetail.setCountry("india");
        addressDetail.setLatLong("10,20");
        addressDetail.setLocationId(1);
        String json = Utils.convertObjectToJson(addressDetail);
        return json;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public OpportunityResponseBean createOpportunityResponseBean() {
        OpportunityResponseBean opportunityResponseBean = new OpportunityResponseBean();
        opportunityResponseBean.setCustomOptyId("1");
        opportunityResponseBean.setMessage("optimus");
        opportunityResponseBean.setOpportunity(getOpportunityResponse());
        return opportunityResponseBean;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public OpportunityResponse getOpportunityResponse() {
        OpportunityResponse opportunityResponse = new OpportunityResponse();
        opportunityResponse.setAccountId("1");
        opportunityResponse.setBillingFrequency("years");
        opportunityResponse.setBillingMethod("method");
        opportunityResponse.setCofType("cof");
        opportunityResponse.setPortalTransactionId("1");
        return opportunityResponse;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public ProductsserviceResponse getProductsserviceResponse() {
        ProductsserviceResponse productsserviceResponse = new ProductsserviceResponse();
        productsserviceResponse.setId("1");
        return productsserviceResponse;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public ProductServicesResponseBean getProductServicesResponseBean() {
        ProductServicesResponseBean ProductServicesResponseBean = new ProductServicesResponseBean();
        ProductServicesResponseBean.setMessage("optimus");
        ProductServicesResponseBean.setProductId("1");
        List<ProductsserviceResponse> list = new ArrayList<>();
        list.add(getProductsserviceResponse());
        ProductServicesResponseBean.setProductsservices(list);
        ProductServicesResponseBean.setSfdcid("1");
        ProductServicesResponseBean.setStatus("1");
        return ProductServicesResponseBean;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public StagingResponseBean getStagingResponseBean() {
        StagingResponseBean stagingResponseBean = new StagingResponseBean();
        stagingResponseBean.setCustomOptyId("1");
        stagingResponseBean.setMessage("optimus");
        stagingResponseBean.setOpportunity(getOpportunityResponse());
        stagingResponseBean.setStatus("1");
        return stagingResponseBean;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public List<SiteLocationResponse> getSiteLocationResponseListn() {
        SiteLocationResponse siteLocationResponse = new SiteLocationResponse();
        siteLocationResponse.setLocation("chennai");
        siteLocationResponse.setSfdcSiteName("chennai");
        siteLocationResponse.setSiteId("1");
        siteLocationResponse.setSiteName("chennai");
        List<SiteLocationResponse> list = new ArrayList<>();
        list.add(siteLocationResponse);
        return list;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public SiteResponseBean getSiteResponseBean() {
        SiteResponseBean siteResponseBean = new SiteResponseBean();
        siteResponseBean.setMessage("optimus");
        siteResponseBean.setOpportunityId("1");
        siteResponseBean.setProductService("service");
        siteResponseBean.setStatus("1");
        siteResponseBean.setSiteLocations(getSiteLocationResponseListn());
        return siteResponseBean;
    }

    public OrderSiteRequest getOrderSiteRequest() {
        OrderSiteRequest orderSiteRequest = new OrderSiteRequest();
        orderSiteRequest.setMstOrderSiteStageName("test");
        orderSiteRequest.setMstOrderSiteStatusName("test");
        return orderSiteRequest;
    }

    public MstOrderSiteStatus getMstOrderSiteStatus() {
        MstOrderSiteStatus mstOrderSiteStatus = new MstOrderSiteStatus();
        mstOrderSiteStatus.setId(1);
        mstOrderSiteStatus.setCode("1");
        mstOrderSiteStatus.setIsActive((byte) 1);
        mstOrderSiteStatus.setName("master");
        return mstOrderSiteStatus;
    }

    public MstOrderSiteStage getMstOrderSiteStage() {
        MstOrderSiteStage mstOrderSiteStage = new MstOrderSiteStage();
        mstOrderSiteStage.setId(1);
        mstOrderSiteStage.setName("site");
        return mstOrderSiteStage;

    }

    public List<OrderSiteStatusAudit> getOrderSiteStatusAuditList() {
        List<OrderSiteStatusAudit> orderSiteStatusAuditList = new ArrayList<>();
        OrderSiteStatusAudit orderSiteStatusAudit = new OrderSiteStatusAudit();
        orderSiteStatusAudit.setCreatedBy("Vishesh");
        orderSiteStatusAudit.setCreatedTime(new Timestamp(0));
        orderSiteStatusAudit.setEndTime(null);
        orderSiteStatusAudit.setMstOrderSiteStatus(getMstOrderSiteStatus());
        orderSiteStatusAudit.setOrderSiteStageAudits(orderSiteStageAudits());
        orderSiteStatusAudit.setOrderSiteStageAudits(orderSiteStageAudits());
        orderSiteStatusAuditList.add(orderSiteStatusAudit);
        return orderSiteStatusAuditList;
    }

    public Set<OrderSiteStageAudit> orderSiteStageAudits() {
        Set<OrderSiteStageAudit> set = new HashSet<>();
        OrderSiteStageAudit audit = new OrderSiteStageAudit();
        audit.setCreatedBy("oms");
        audit.setCreatedTime(new Timestamp(0));
        audit.setEndTime(new Timestamp(0));
        audit.setId(1);
        audit.setIsActive((byte) 1);
        audit.setMstOrderSiteStage(getMstOrderSiteStage());
        return set;
    }

    public List<OrderSiteStageAudit> getOrderSiteStageAuditList() {
        List<OrderSiteStageAudit> list = new ArrayList<>();
        OrderSiteStageAudit audit = new OrderSiteStageAudit();
        audit.setEndTime(null);
        list.add(audit);
        return list;
    }

    public RestResponse cretateResponse() {
        RestResponse response = new RestResponse();
        response.setData("{\r\n" + "    \"results\": [\r\n" + "        {\r\n"
                + "            \"Account.RTM_Cust\": \"Direct\",\r\n"
                + "            \"Account_id_with_18_Digit\": \"0012000000BSpusAAD\",\r\n"
                + "            \"Adjusted_CPE_Discount\": 0.25,\r\n" + "            \"Adjustment_Factor\": 0.07,\r\n"
                + "            \"BW_mbps_upd\": \"10\",\r\n"
                + "            \"Bucket_Adjustment_Type\": \"No Change\",\r\n" + "            \"Burstable_BW\": 10,\r\n"
                + "            \"CPE_HW_MP\": \"NA\",\r\n" + "            \"CPE_Hardware_LP_USD\": \"NA\",\r\n"
                + "            \"CPE_Installation_INR\": \"NA\",\r\n"
                + "            \"CPE_Installation_MP\": \"NA\",\r\n" + "            \"CPE_Management_INR\": \"NA\",\r\n"
                + "            \"CPE_Support_INR\": \"NA\",\r\n" + "            \"CPE_Support_MP\": \"NA\",\r\n"
                + "            \"CPE_Variant\": \"None\",\r\n"
                + "            \"CPE_management_type\": \"full_managed\",\r\n" + "            \"CPE_pred\": 0.28,\r\n"
                + "            \"CPE_supply_type\": \"rental\",\r\n" + "            \"Discounted_CPE_ARC\": 0,\r\n"
                + "            \"Discounted_CPE_MRC\": 0,\r\n" + "            \"Discounted_CPE_NRC\": 0,\r\n"
                + "            \"GVPN_ARC_per_BW\": 36048.52,\r\n" + "            \"ILL_ARC\": 2063725.71,\r\n"
                + "            \"ILL_ARC_per_BW\": 13267.29,\r\n" + "            \"ILL_CPE_ARC\": 0,\r\n"
                + "            \"ILL_CPE_MRC\": 0,\r\n" + "            \"ILL_CPE_NRC\": 0,\r\n"
                + "            \"ILL_NRC\": 125000,\r\n" + "            \"ILL_Port_ARC_Adjusted\": 224261.78,\r\n"
                + "            \"ILL_Port_Adjusted_net_Price\": 231761.78,\r\n"
                + "            \"ILL_Port_MRC_Adjusted\": 18688.48,\r\n"
                + "            \"ILL_Port_NRC_Adjusted\": 7500,\r\n" + "            \"Identifier\": \"VALIDATION\",\r\n"
                + "            \"Industry_Cust\": \"IT Enabled Services (ITeS)\",\r\n"
                + "            \"Inv_GVPN_bw\": 3658,\r\n" + "            \"Inv_ILL_bw\": 1097,\r\n"
                + "            \"Inv_NPL_bw\": 2,\r\n" + "            \"Inv_Other_bw\": 270,\r\n"
                + "            \"Inv_Tot_BW\": 5027,\r\n" + "            \"Last_Mile_Cost_ARC\": 10871.19,\r\n"
                + "            \"Last_Mile_Cost_MRC\": 905.93,\r\n" + "            \"Last_Mile_Cost_NRC\": 0,\r\n"
                + "            \"Last_Modified_Date\": \"2018-07-24\",\r\n"
                + "            \"NPL_ARC_per_BW\": 20107.54,\r\n" + "            \"OEM_Discount\": \"NA\",\r\n"
                + "            \"OpportunityID_Prod_Identifier\": \"0012000000BSpusAAD|1\",\r\n"
                + "            \"Orch_Connection\": \"Wireline\",\r\n" + "            \"Orch_LM_Type\": \"Onnet\",\r\n"
                + "            \"Other_ARC_per_BW\": 10242.84,\r\n" + "            \"Recovery_INR\": \"NA\",\r\n"
                + "            \"Segment_Cust\": \"Enterprise\",\r\n"
                + "            \"Sum_CAT_1_2_MACD_FLAG\": 218,\r\n"
                + "            \"Sum_CAT_1_2_New_Opportunity_FLAG\": 41,\r\n"
                + "            \"Sum_CAT_3_MACD_FLAG\": 6,\r\n"
                + "            \"Sum_CAT_3_New_Opportunity_FLAG\": 1,\r\n"
                + "            \"Sum_CAT_4_MACD_FLAG\": 0,\r\n"
                + "            \"Sum_CAT_4_New_Opportunity_FLAG\": 0,\r\n" + "            \"Sum_Cat_1_2_opp\": 259,\r\n"
                + "            \"Sum_GVPN_Flag\": 79,\r\n" + "            \"Sum_IAS_FLAG\": 27,\r\n"
                + "            \"Sum_MACD_Opportunity\": 224,\r\n" + "            \"Sum_NPL_Flag\": 1,\r\n"
                + "            \"Sum_New_ARC_Converted\": 149225488,\r\n"
                + "            \"Sum_New_ARC_Converted_GVPN\": 131865488,\r\n"
                + "            \"Sum_New_ARC_Converted_ILL\": 14554213,\r\n"
                + "            \"Sum_New_ARC_Converted_NPL\": 40215.08,\r\n"
                + "            \"Sum_New_ARC_Converted_Other\": 2765566.25,\r\n"
                + "            \"Sum_New_Opportunity\": 42,\r\n" + "            \"Sum_Offnet_Flag\": 0,\r\n"
                + "            \"Sum_Onnet_Flag\": 0,\r\n" + "            \"Sum_Other_Flag\": 15,\r\n"
                + "            \"Sum_tot_oppy_historic_opp\": 266,\r\n"
                + "            \"Sum_tot_oppy_historic_prod\": 15,\r\n" + "            \"TOT_ARC_per_BW\": 29684.8,\r\n"
                + "            \"Total_CPE_Cost\": 0,\r\n" + "            \"Total_CPE_Price\": 0,\r\n"
                + "            \"account_id_with_18_digit\": \"0012000000BSpusAAD\",\r\n"
                + "            \"additional_IP_ARC\": 0,\r\n" + "            \"additional_IP_MRC\": 0,\r\n"
                + "            \"additional_ip_flag\": \"No\",\r\n"
                + "            \"burst_per_MB_price\": 27811.41,\r\n" + "            \"burstable_bw\": \"10\",\r\n"
                + "            \"bw_mbps\": \"10\",\r\n" + "            \"calc_arc_list_inr\": 2063725.71,\r\n"
                + "            \"connection_type\": \"Standard\",\r\n"
                + "            \"cpe_management_type\": \"full_managed\",\r\n"
                + "            \"cpe_supply_type\": \"rental\",\r\n" + "            \"cpe_variant\": \"None\",\r\n"
                + "            \"createdDate_quote\": 0,\r\n"
                + "            \"customer_segment\": \"Enterprise - Gold\",\r\n" + "            \"datediff\": 0,\r\n"
                + "            \"error_code\": \"NA\",\r\n" + "            \"error_flag\": 0,\r\n"
                + "            \"error_msg\": \"No error\",\r\n" + "            \"f_lm_arc_bw_onrf\": \"0\",\r\n"
                + "            \"f_lm_arc_bw_onwl\": \"100040\",\r\n"
                + "            \"f_lm_arc_bw_prov_ofrf\": \"0\",\r\n" + "            \"f_lm_nrc_bw_onrf\": \"0\",\r\n"
                + "            \"f_lm_nrc_bw_onwl\": \"0\",\r\n" + "            \"f_lm_nrc_bw_prov_ofrf\": \"0\",\r\n"
                + "            \"f_lm_nrc_inbldg_onwl\": \"40000\",\r\n"
                + "            \"f_lm_nrc_mast_ofrf\": \"0.0\",\r\n" + "            \"f_lm_nrc_mast_onrf\": \"0\",\r\n"
                + "            \"f_lm_nrc_mux_onwl\": \"58810\",\r\n"
                + "            \"f_lm_nrc_nerental_onwl\": \"0\",\r\n"
                + "            \"f_lm_nrc_ospcapex_onwl\": \"0\",\r\n"
                + "            \"feasibility_response_created_date\": \"2018-07-24\",\r\n"
                + "            \"hist_flag\": 0,\r\n" + "            \"ip_address_arrangement\": \"None\",\r\n"
                + "            \"ipv4_address_pool_size\": \"8\",\r\n"
                + "            \"ipv6_address_pool_size\": \"0\",\r\n"
                + "            \"last_mile_contract_term\": \"1 Year\",\r\n"
                + "            \"latitude_final\": \"13.018411\",\r\n" + "            \"list_price_mb\": 206372.57,\r\n"
                + "            \"list_price_mb_dummy\": 0,\r\n" + "            \"local_loop_interface\": \"FE\",\r\n"
                + "            \"log_Inv_Tot_BW\": 8.52,\r\n" + "            \"log_Inv_Tot_BW_dummy\": 0,\r\n"
                + "            \"longitude_final\": \"80.18775\",\r\n"
                + "            \"num_products_opp_new.x\": 0,\r\n" + "            \"opportunityTerm\": 12,\r\n"
                + "            \"opportunity_day\": 1,\r\n" + "            \"opportunity_month\": 7,\r\n"
                + "            \"opportunity_term\": \"12\",\r\n" + "            \"overall_BW_mbps_upd\": 10,\r\n"
                + "            \"overall_CPE_node\": \"Node_5\",\r\n"
                + "            \"overall_PortType\": \"Fixed\",\r\n" + "            \"overall_node\": \"node_9\",\r\n"
                + "            \"p_lm_arc_bw_onrf\": \"0\",\r\n" + "            \"p_lm_arc_bw_onwl\": \"100040\",\r\n"
                + "            \"p_lm_arc_bw_prov_ofrf\": \"0\",\r\n" + "            \"p_lm_nrc_bw_onrf\": \"0\",\r\n"
                + "            \"p_lm_nrc_bw_onwl\": \"0\",\r\n" + "            \"p_lm_nrc_bw_prov_ofrf\": \"0\",\r\n"
                + "            \"p_lm_nrc_inbldg_onwl\": \"0\",\r\n"
                + "            \"p_lm_nrc_mast_ofrf\": \"0.0\",\r\n" + "            \"p_lm_nrc_mast_onrf\": \"0\",\r\n"
                + "            \"p_lm_nrc_mux_onwl\": \"0\",\r\n" + "            \"p_lm_nrc_nerental_onwl\": \"0\",\r\n"
                + "            \"p_lm_nrc_ospcapex_onwl\": \"0\",\r\n" + "            \"port_lm_arc\": 0,\r\n"
                + "            \"port_pred_discount\": 0.89,\r\n"
                + "            \"predicted_ILL_Port_ARC\": 224261.78,\r\n"
                + "            \"predicted_ILL_Port_NRC\": 7500,\r\n"
                + "            \"predicted_net_price\": 231761.78,\r\n"
                + "            \"product_name\": \"Internet Access Service\",\r\n"
                + "            \"prospect_name\": \"Intelenet Global Services Private Limited\",\r\n"
                + "            \"quoteType_quote\": \"New Order\",\r\n"
                + "            \"quotetype_quote\": \"New Order\",\r\n"
                + "            \"resp_city\": \"Tiruvallur\",\r\n" + "            \"sales_org\": \"Enterprise\",\r\n"
                + "            \"site_id\": \"1008_primary\",\r\n" + "            \"sum_cat1_2_Opportunity\": 259,\r\n"
                + "            \"sum_cat_3_Opportunity\": 7,\r\n" + "            \"sum_cat_4_Opportunity\": 0,\r\n"
                + "            \"sum_no_of_sites_uni_len\": \"1\",\r\n" + "            \"sum_offnet_flag\": \"0\",\r\n"
                + "            \"sum_onnet_flag\": \"1\",\r\n" + "            \"sum_product_flavours.x\": 1,\r\n"
                + "            \"time_taken\": 1.64,\r\n" + "            \"topology\": \"primary_active\",\r\n"
                + "            \"tot_oppy_current_prod.x\": 1\r\n" + "        }\r\n" + "    ]\r\n" + "}\r\n" + "");
        response.setStatus(Status.SUCCESS);
        return response;
    }


    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public List<CustomerAttributeBean> getCustomerAttributeBean() throws TclCommonException {
        List<CustomerAttributeBean> customerDetailsBeanList = new ArrayList<>();
        CustomerAttributeBean customerDetailsBean = new CustomerAttributeBean();
        customerDetailsBean.setName("ACCOUNT_ID_18");
        customerDetailsBean.setValue("tcl");
        CustomerAttributeBean customerDetailsBean1 = new CustomerAttributeBean();
        customerDetailsBean1.setName("CUSTOMER TYPE");
        customerDetailsBean1.setValue("tcl");
        CustomerAttributeBean customerDetailsBean2 = new CustomerAttributeBean();
        customerDetailsBean2.setName("SALES ORG");
        customerDetailsBean2.setValue("tcl");
        customerDetailsBeanList.add(customerDetailsBean);
        customerDetailsBeanList.add(customerDetailsBean1);
        customerDetailsBeanList.add(customerDetailsBean2);
        return customerDetailsBeanList;
    }

    /**
     * getProductSolution-mock values
     *
     * @return {@link ProductSolution}
     * @throws TclCommonException
     */
    public String getCustomerDetailsBean() throws TclCommonException {
        CustomerDetailsBean customerDetailsBean = new CustomerDetailsBean();
        customerDetailsBean.setCustomerAttributes(getCustomerAttributeBean());
        String json = Utils.convertObjectToJson(customerDetailsBean);
        return json;
    }

    public String getCustomerLedetailsBean() throws TclCommonException {
        CustomerLeDetailsBean bean = new CustomerLeDetailsBean();
        bean.setAccounCuId("5675765757");
        bean.setAccountId("678688");
        bean.setBillingContactId(1);
        bean.setLegalEntityName("vivek");
        bean.setAttributes(getAttr());
        String json = Utils.convertObjectToJson(bean);
        return json;
    }

    public List<Attributes> getAttr() {
        List<Attributes> attributes = new ArrayList<>();

        Attributes attributes2 = new Attributes();
        attributes2.setAttributeName("entityName");
        attributes2.setAttributeValue("Regus");
        attributes.add(attributes2);

        Attributes attributes3 = new Attributes();
        attributes3.setAttributeName("CONTACTNAME");
        attributes3.setAttributeValue("vive");
        attributes.add(attributes3);

        Attributes attributes4 = new Attributes();
        attributes4.setAttributeName("TermInMonths");
        attributes4.setAttributeValue("12");
        attributes.add(attributes4);

        Attributes attributes5 = new Attributes();
        attributes5.setAttributeName("TermInMonths");
        attributes5.setAttributeValue("12");
        attributes.add(attributes5);

        Attributes attributes6 = new Attributes();

        attributes6.setAttributeName("Billing Method");
        attributes6.setAttributeValue("12");
        attributes.add(attributes6);

        Attributes attributes7 = new Attributes();

        attributes7.setAttributeName("TermInMonths");
        attributes7.setAttributeValue("12");
        attributes.add(attributes7);

        Attributes attributes8 = new Attributes();

        attributes8.setAttributeName("Billing Type");
        attributes8.setAttributeValue("12");
        attributes.add(attributes8);

        Attributes attributes9 = new Attributes();

        attributes9.setAttributeName("Customer Contracting Entity");
        attributes9.setAttributeValue("12");
        attributes.add(attributes9);

        Attributes attributes10 = new Attributes();

        attributes10.setAttributeName("RECURRING_CHARGE_TYPE");
        attributes10.setAttributeValue("12");
        attributes.add(attributes10);

        Attributes attributes11 = new Attributes();

        attributes11.setAttributeName("Payment Currency");
        attributes11.setAttributeValue("12");
        attributes.add(attributes11);

        Attributes attributes12 = new Attributes();

        attributes12.setAttributeName("TermInMonths");
        attributes12.setAttributeValue("12");
        attributes.add(attributes12);

        Attributes attributes13 = new Attributes();

        attributes13.setAttributeName("DESIGNATION");
        attributes13.setAttributeValue("12");
        attributes.add(attributes13);

        Attributes attributes14 = new Attributes();

        attributes14.setAttributeName("TermInMonths");
        attributes14.setAttributeValue("12");
        attributes.add(attributes14);

        Attributes attributes15 = new Attributes();

        attributes15.setAttributeName("CONTACTNO");
        attributes15.setAttributeValue("12");
        attributes.add(attributes15);

        Attributes attributes16 = new Attributes();

        attributes16.setAttributeName("CONTACTID");
        attributes16.setAttributeValue("12");
        attributes.add(attributes16);

        Attributes attributes17 = new Attributes();

        attributes17.setAttributeName("CONTACTEMAIL");
        attributes17.setAttributeValue("12");
        attributes.add(attributes17);

        return attributes;

    }

    public OrderNplLink getOrderNplLink() {
        OrderNplLink orderNplLink = new OrderNplLink();
        orderNplLink.setId(1);
        orderNplLink.setLinkCode("AS3GMIGZZFXRLMGP");
        orderNplLink.setProductSolutionId(419);
        orderNplLink.setSiteAId(905);
        orderNplLink.setSiteBId(906);
        orderNplLink.setOrderId(298);
        orderNplLink.setStatus((byte) 1);
        return orderNplLink;

    }

    public OrderIllSiteSla getOrderIllSiteSla() {
        OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
        orderIllSiteSla.setOrderIllSite(getOrderIllSite());
        orderIllSiteSla.setSlaEndDate(new Date());
        orderIllSiteSla.setSlaStartDate(new Date());
        orderIllSiteSla.setSlaValue("Varient");
        orderIllSiteSla.setSlaMaster(createSlaMaster());
        return orderIllSiteSla;

    }

    /*
     * Get Order site fesiblity details
     *
     * @author Anandhi Vijayaraghavan
     *
     * @return OrderSiteFeasibility
     *
     * @throws TclCommonException
     */
    public List<OrderSiteFeasibility> getOrderIllSiteFeasiblity() {
        List<OrderSiteFeasibility> feasiblityList = new ArrayList<>();
        OrderSiteFeasibility feasiblity = new OrderSiteFeasibility();
        feasiblity.setId(1);
        feasiblity.setOrderIllSite(getOrderIllSite());
        feasiblity.setCreatedTime(new Timestamp(0));
        feasiblity.setFeasibilityCheck("Test");
        feasiblity.setFeasibilityMode("Test");
        feasiblity.setIsSelected((byte) 1);
        feasiblity.setProvider("Test");
        feasiblity.setRank(1);
        feasiblity.setResponseJson("{\"test\":\"sample\"}");
        feasiblity.setType("Type");
        feasiblityList.add(feasiblity);
        return feasiblityList;
    }

    /*
     * Get Order site fesiblity details
     *
     * @author Anandhi Vijayaraghavan
     *
     * @return List<PricingDetail>
     *
     * @throws TclCommonException
     */
    public List<PricingEngineResponse> getPricingDetails() {
        List<PricingEngineResponse> pricingList = new ArrayList<>();
        PricingEngineResponse pricing = new PricingEngineResponse();
        pricing.setDateTime(new Timestamp(0));
        pricing.setId(1);
        pricing.setPriceMode("Test");
        pricing.setPricingType("Test");
        pricing.setRequestData("{\"test\":\"sample\"}");
        pricing.setResponseData("{\"test\":\"sample\"}");
        pricing.setSiteCode("TestCode");
        pricingList.add(pricing);
        return pricingList;
    }

    public OrderConfirmationAudit getOrderConfirmationAudit() {
        OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
        orderConfirmationAudit.setName("Test");
        orderConfirmationAudit.setPublicIp("10.86.12.1");
        orderConfirmationAudit.setOrderRefUuid("45");
        orderConfirmationAudit.setCreatedTime(new Date());
        return orderConfirmationAudit;
    }

    public SfdcJob getSfdcJob() {
        SfdcJob sfdcjob = new SfdcJob();
        sfdcjob.setCode(quoteToLe.getQuote().getQuoteCode());
        sfdcjob.setCreatedTime(new Timestamp(new Date().getTime()));
        sfdcjob.setOperation(SFDCConstants.UPDATE_OPTY.toString());
        sfdcjob.setQueueName("sfdcUpdateOpty");
        sfdcjob.setRequestPayload("testRequest");
        sfdcjob.setStatus(SFDCConstants.CREATED.toString());
        sfdcjob.setType(SFDCConstants.OPTY.toString());
        return sfdcjob;
    }

    public ProductAttributeMaster getProductAtrribute() {
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("Service Variant");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(1);

        return productAttributeMaster;
    }

    public com.tcl.dias.common.beans.AddressDetail getAddressDetailcommon() {
        com.tcl.dias.common.beans.AddressDetail locationDetail = new com.tcl.dias.common.beans.AddressDetail();
        locationDetail.setAddressId(1);
        locationDetail.setCity("chennai");
        locationDetail.setCountry("india");
        locationDetail.setLocationId(1);
        return locationDetail;
    }

    public LocationDetail getLocationDetail() {
        LocationDetail locationDetail = new LocationDetail();
        locationDetail.setPopId("1");
        locationDetail.setCustomerId(1);
        locationDetail.setApiAddress(getAddressDetailcommon());
        locationDetail.setAddress(getAddressDetailcommon());
        locationDetail.setErfCusCustomerLeId(1);
        locationDetail.setTier("1");
        locationDetail.setUserAddress(getAddressDetailcommon());
        locationDetail.setLatLong("1");
        locationDetail.setLocationId(1);
        return locationDetail;
    }

    public ProductSlaBean getProductSlaBean() {
        ProductSlaBean productSlaBean = new ProductSlaBean();
        productSlaBean.setTier(1);
        productSlaBean.setTierName("GSC-PSTN");
        List<SLaDetailsBean> sLaDetails = new ArrayList<SLaDetailsBean>();
        sLaDetails.add(createSLaDetailsBean());
        productSlaBean.setsLaDetails(sLaDetails);
        return productSlaBean;
    }

    public String getLocationDetailjson() throws TclCommonException {
        String json = Utils.convertObjectToJson(getLocationDetail());
        return json;
    }

    public String getProductSlaBeanjson() throws TclCommonException {
        String json = Utils.convertObjectToJson(getProductSlaBean());
        return json;
    }

    public Optional<QuoteToLe> returnQuoteToLeForUpdateStatusForContactAttributeInfo() {
        QuoteToLe le = new QuoteToLe();
        le.setId(1);
        le.setCurrencyId(1);
        le.setErfCusCustomerLegalEntityId(1);
        le.setErfCusSpLegalEntityId(1);
        le.setStage("Sample");
        le.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        le.setTpsSfdcOptyId("tpsSfdcOptyId");
        le.setQuote(getQuote1());
        le.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet1());
        return Optional.of(le);
    }

    public Set<QuoteLeAttributeValue> getQuoteLeAttributeValueSet1() {
        Set<QuoteLeAttributeValue> quoteLeAttributeValueSet = new HashSet<>();
        QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
        quoteLeAttributeValue.getMstOmsAttribute().setName("CONTACTID");
        quoteLeAttributeValue.setAttributeValue("1");
        quoteLeAttributeValueSet.add(quoteLeAttributeValue);
        return quoteLeAttributeValueSet;
    }

    public BillingRequest getBillingRequest() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setAccountId("1");
        billingRequest.setAccounCuId("1");
        billingRequest.setBillingContactId(1);
        billingRequest.setLegalEntityName("billing");
        billingRequest.setQuoteLeId(1);
        billingRequest.setCustomerLeId(1);
        billingRequest.setAttributes(billingAttributesBeanList());
        return billingRequest;
    }

    public List<BillingAttributesBean> billingAttributesBeanList() {
        List<BillingAttributesBean> list = new ArrayList<>();
        BillingAttributesBean billingAttributesBean = new BillingAttributesBean();
        billingAttributesBean.setAttributeName("Attribute name");
        billingAttributesBean.setAttributeValue("attribute value");
        billingAttributesBean.setType("attribute type");
        list.add(billingAttributesBean);
        return list;
    }

    public List<SiteFeasibility> getSiteFeasibilities1() {

        List<SiteFeasibility> fblist = new ArrayList<>();
        fblist.add(siteFeasibilities1());

        return fblist;
    }

    private SiteFeasibility siteFeasibilities1() {

        SiteFeasibility sFb = new SiteFeasibility();
        sFb.setId(1);
        sFb.setCreatedTime(new Timestamp(0));
        sFb.setFeasibilityCheck("Test");
        sFb.setFeasibilityMode("Test");
        sFb.setIsSelected((byte) 1);
        sFb.setProvider("Test");
        sFb.setRank(1);
        sFb.setResponseJson("{\"test\":\"sample\"}");
        sFb.setId(1);
        sFb.setType("Onnet");
        sFb.setFeasibilityMode("Onnet");

        return sFb;
    }

    public List<Map<String, Object>> getDashBoardConfiguration() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productName", "optimus");
        map.put("quoteCode", "optimus");
        map.put("createdTime", new Date());
        map.put("quoteId", 1);
        map.put("stage", "optimus");
        map.put("siteCount", BigInteger.valueOf(1));
        list.add(map);
        return list;
    }

    public Map<String, String> craeteMap() {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        return map;

    }

    public OrderSiteFeasibility getSiteFeasiblity() {
        OrderSiteFeasibility feasiblity = new OrderSiteFeasibility();
        feasiblity.setId(1);
        feasiblity.setCreatedTime(new Timestamp(0));
        feasiblity.setFeasibilityCheck("Test");
        feasiblity.setFeasibilityMode("Test");
        feasiblity.setIsSelected((byte) 1);
        feasiblity.setProvider("Test");
        feasiblity.setRank(1);
        feasiblity.setResponseJson("{\"test\":\"sample\"}");
        feasiblity.setType("Type");

        return feasiblity;
    }

    public Set<OrderSiteFeasibility> getSiteFeasiblitySet() {
        Set<OrderSiteFeasibility> orderSiteFeasibilitySet = new HashSet<>();
        orderSiteFeasibilitySet.add(getSiteFeasiblity());
        return orderSiteFeasibilitySet;
    }

    public List<QuotePrice> getQuotePriceList() {
        List<QuotePrice> priceList = new ArrayList<QuotePrice>();

        QuotePrice qp = new QuotePrice();
        qp.setId(1);
        qp.setCatalogArc(Double.parseDouble("10000"));
        qp.setCatalogMrc(Double.parseDouble("5000"));
        qp.setCatalogNrc(Double.parseDouble("8000"));
        qp.setComputedArc(Double.parseDouble("342234"));
        qp.setComputedNrc(Double.parseDouble("345345"));
        qp.setComputedMrc(Double.parseDouble("23222"));
        qp.setEffectiveArc(Double.parseDouble("34324"));
        qp.setEffectiveMrc(Double.parseDouble("3234"));
        qp.setEffectiveNrc(Double.parseDouble("234234"));
        qp.setEffectiveUsagePrice(Double.parseDouble("234234"));
        qp.setMinimumMrc(Double.parseDouble("23434"));
        qp.setMinimumArc(Double.parseDouble("234234"));
        qp.setMinimumNrc(Double.parseDouble("234234"));
        priceList.add(qp);
        return priceList;

    }

    public CurrencyConversion getCurrencyConversionRate() {
        CurrencyConversion currency = new CurrencyConversion();
        currency.setId(1);
        currency.setInputCurrency("USD");
        currency.setOutputCurrency("INR");
        currency.setStatus("YES");
        currency.setConversionRate("68.15");
        return currency;
    }

    public List<SfdcJob> getSfdcJobList() throws TclCommonException {
        List<SfdcJob> sfdcJobsList = new ArrayList<>();
        SfdcJob sfdcJob = new SfdcJob();
        sfdcJob.setRequestPayload(getProductServiceBean());
        sfdcJobsList.add(sfdcJob);
        return sfdcJobsList;
    }

    public String getProductServiceBean() throws TclCommonException {
        return Utils.convertObjectToJson(new ProductServiceBean());
    }

    public List<SfdcJob> getSfdcJobList2() throws TclCommonException {
        List<SfdcJob> sfdcJobsList = new ArrayList<>();
        SfdcJob sfdcJob = new SfdcJob();
        sfdcJob.setRequestPayload(getSiteSolutionOpportunityBean());
        sfdcJobsList.add(sfdcJob);
        return sfdcJobsList;
    }

    public String getSiteSolutionOpportunityBean() throws TclCommonException {
        return Utils.convertObjectToJson(new SiteSolutionOpportunityBean());
    }

    public byte[] getFileAsByteArray() throws IOException {
        List<String> countryList = new ArrayList<>();
        countryList.add("India");
        countryList.add("USA");
        List<String> profileLst = new ArrayList<>();
        profileLst.add("Profile 1");
        profileLst.add("Profile 2");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Header1");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Headrer2");
        header.createCell(1).setCellValue("Headrer3");
        header.createCell(2).setCellValue("Headrer4");
        header.createCell(3).setCellValue("Headrer5");
        header.createCell(4).setCellValue("Headrer6");
        header.createCell(5).setCellValue("Headrer7");
        header.createCell(6).setCellValue("Headrer8");

        Row contentRow = sheet.createRow(1);
        contentRow.createCell(0).setCellValue("1");
        contentRow.createCell(1).setCellValue("India");
        contentRow.createCell(2).setCellValue("Tamilnadu");
        contentRow.createCell(3).setCellValue("Chennai");
        contentRow.createCell(4).setCellValue("600089");
        contentRow.createCell(5).setCellValue("Jeyant tech park, nandambakkam, chennai, 600089");
        contentRow.createCell(6).setCellValue("Single Unmanaged GVPN");

        for (int i = 0; i < 7; i++) {
            CellStyle stylerowHeading = workbook.createCellStyle();
            stylerowHeading.setBorderBottom(BorderStyle.THICK);
            stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            stylerowHeading.setFont(font);
            stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
            stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

            stylerowHeading.setWrapText(true);
            header.getCell(i).setCellStyle(stylerowHeading);

            DataFormat fmt = workbook.createDataFormat();
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(fmt.getFormat("@"));
            sheet.setDefaultColumnStyle(i, textStyle);
        }

        /* country validation begins */
        DataValidation countryDataValidation = null;
        DataValidationConstraint countryConstraint = null;
        DataValidationHelper countryValidationHelper = null;

        countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
        // populate with the list of countries from product catalog
        countryConstraint = countryValidationHelper
                .createExplicitListConstraint(profileLst.stream().toArray(String[]::new));
        countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
        countryDataValidation.setSuppressDropDownArrow(true);
        countryDataValidation.setShowErrorBox(true);
        countryDataValidation.setShowPromptBox(true);
        sheet.addValidationData(countryDataValidation);
        sheet.setColumnWidth(0, 1800);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 7500);
        sheet.setColumnWidth(5, 10000);
        sheet.setColumnWidth(6, 8000);

        /* profile validation begins */
        DataValidation profileDataValidation = null;
        DataValidationConstraint profileConstraint = null;
        DataValidationHelper profileValidationHelper = null;
        profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 6, 6);
        // add the selected profiles
        profileConstraint = profileValidationHelper
                .createExplicitListConstraint(countryList.stream().toArray(String[]::new));
        profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
        profileDataValidation.setSuppressDropDownArrow(true);
        profileDataValidation.setShowErrorBox(true);
        profileDataValidation.setShowPromptBox(true);
        sheet.addValidationData(profileDataValidation);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        return outArray;
    }

    public QuoteBean getQuoteBean1() throws TclCommonException {
        String json = " {\"quoteId\":2018,\"quoteCode\":\"GVPN260918NTRBUB\",\"createdBy\":3,\"createdTime\":1537949035324,\"status\":1,\"version\":1,\"customerId\":2,\"isManualCofSigned\":false,\"legalEntities\":[{\"quoteleId\":2018,\"customerLegalEntityId\":2,\"supplierLegalEntityId\":5,\"finalMrc\":57372.1015625,\"finalNrc\":20000.0,\"finalArc\":688465.0,\"proposedMrc\":57372.1015625,\"proposedNrc\":20000.0,\"proposedArc\":688465.0,\"totalTcv\":708465.0,\"tpsSfdcOptyId\":\"1861538\",\"stage\":\"Order Form\",\"productFamilies\":[{\"solutions\":[{\"productSolutionId\":2482,\"offeringDescription\":\"Single Unmanaged GVPN\",\"offeringName\":\"Single Unmanaged GVPN\",\"status\":1,\"solution\":{\"offeringName\":\"Single Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"10\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"10\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Unmanaged\"}]}]},\"sites\":[{\"siteId\":5390,\"siteCode\":\"HBROMNVKGCRXBBSO\",\"fpStatus\":\"FP\",\"imageUrl\":\"./assets/images/gvpn-profile-images/P-L.png\",\"isFeasible\":1,\"mrc\":34509.55078125,\"nrc\":10000.0,\"arc\":414114.62,\"tcv\":424114.6,\"components\":[{\"componentId\":33816,\"componentMasterId\":12,\"referenceId\":5390,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240715,\"attributeMasterId\":1,\"attributeValues\":\"Fast Ethernet\",\"description\":\"Interface\",\"name\":\"Interface\"},{\"attributeId\":240716,\"attributeMasterId\":2,\"attributeValues\":\"BGP\",\"description\":\"Routing Protocol\",\"name\":\"Routing Protocol\"},{\"attributeId\":240717,\"attributeMasterId\":3,\"attributeValues\":\"10\",\"description\":\"Port Bandwidth\",\"name\":\"Port Bandwidth\"},{\"attributeId\":240718,\"attributeMasterId\":78,\"attributeValues\":\"TCL\",\"description\":\"WAN IP Provided By\",\"name\":\"WAN IP Provided By\"},{\"attributeId\":240719,\"attributeMasterId\":79,\"attributeValues\":\"\",\"description\":\"WAN IP Address\",\"name\":\"WAN IP Address\"},{\"attributeId\":240720,\"attributeMasterId\":4,\"attributeValues\":\"Fixed\",\"description\":\"Service type\",\"name\":\"Service type\"},{\"attributeId\":240721,\"attributeMasterId\":5,\"attributeValues\":\"\",\"description\":\"Burstable Bandwidth\",\"name\":\"Burstable Bandwidth\"},{\"attributeId\":240722,\"attributeMasterId\":6,\"attributeValues\":\"\",\"description\":\"Usage Model\",\"name\":\"Usage Model\"},{\"attributeId\":240723,\"attributeMasterId\":10,\"attributeValues\":\"No\",\"description\":\"Extended LAN Required?\",\"name\":\"Extended LAN Required?\"},{\"attributeId\":240724,\"attributeMasterId\":11,\"attributeValues\":\"No\",\"description\":\"BFD Required\",\"name\":\"BFD Required\"},{\"attributeId\":240725,\"attributeMasterId\":12,\"attributeValues\":\"Loopback\",\"description\":\"BGP Peering on\",\"name\":\"BGP Peering on\"},{\"attributeId\":240726,\"attributeMasterId\":75,\"attributeValues\":\"\",\"description\":\"BGP AS Number\",\"name\":\"BGP AS Number\"},{\"attributeId\":240727,\"attributeMasterId\":76,\"attributeValues\":\"\",\"description\":\"Customer prefixes\",\"name\":\"Customer prefixes\"}],\"price\":{\"id\":6295,\"effectiveMrc\":19527.880859375,\"effectiveNrc\":10000.0,\"effectiveArc\":234334.625,\"quoteId\":2018,\"referenceId\":\"33816\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33817,\"componentMasterId\":2,\"referenceId\":5390,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240728,\"attributeMasterId\":13,\"attributeValues\":\"No\",\"description\":\"Shared Last Mile\",\"name\":\"Shared Last Mile\"},{\"attributeId\":240729,\"attributeMasterId\":14,\"attributeValues\":\"\",\"description\":\"Shared Last Mile Service ID\",\"name\":\"Shared Last Mile Service ID\"},{\"attributeId\":240730,\"attributeMasterId\":15,\"attributeValues\":\"10\",\"description\":\"Local Loop Bandwidth\",\"name\":\"Local Loop Bandwidth\"}],\"price\":{\"id\":6296,\"effectiveMrc\":14981.669921875,\"effectiveNrc\":0.0,\"effectiveArc\":179780.0,\"quoteId\":2018,\"referenceId\":\"33817\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33818,\"componentMasterId\":3,\"referenceId\":5390,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240731,\"attributeMasterId\":16,\"attributeValues\":\"Unmanaged\",\"description\":\"CPE Management Type\",\"name\":\"CPE Management Type\"},{\"attributeId\":240732,\"attributeMasterId\":17,\"attributeValues\":\"false\",\"description\":\"CPE Management\",\"name\":\"CPE Management\"}]},{\"componentId\":33819,\"componentMasterId\":10,\"referenceId\":5390,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240733,\"attributeMasterId\":18,\"attributeValues\":\"Enhanced\",\"description\":\"Service Variant\",\"name\":\"Service Variant\"},{\"attributeId\":240734,\"attributeMasterId\":25,\"attributeValues\":\"No\",\"description\":\"isAuthenticationRequired for protocol\",\"name\":\"isAuthenticationRequired for protocol\"},{\"attributeId\":240735,\"attributeMasterId\":26,\"attributeValues\":\"Default routes\",\"description\":\"Routes Exchanged\",\"name\":\"Routes Exchanged\"},{\"attributeId\":240736,\"attributeMasterId\":53,\"attributeValues\":\"Active\",\"description\":\"Port Mode\",\"name\":\"Port Mode\"},{\"attributeId\":240737,\"attributeMasterId\":27,\"attributeValues\":\"TCL private AS Number\",\"description\":\"AS Number\",\"name\":\"AS Number\"},{\"attributeId\":240738,\"attributeMasterId\":47,\"attributeValues\":\"0%\",\"description\":\"cos 1\",\"name\":\"cos 1\"},{\"attributeId\":240739,\"attributeMasterId\":48,\"attributeValues\":\"0%\",\"description\":\"cos 2\",\"name\":\"cos 2\"},{\"attributeId\":240740,\"attributeMasterId\":49,\"attributeValues\":\"100%\",\"description\":\"cos 3\",\"name\":\"cos 3\"},{\"attributeId\":240741,\"attributeMasterId\":50,\"attributeValues\":\"0%\",\"description\":\"cos 4\",\"name\":\"cos 4\"},{\"attributeId\":240742,\"attributeMasterId\":51,\"attributeValues\":\"0%\",\"description\":\"cos 5\",\"name\":\"cos 5\"},{\"attributeId\":240743,\"attributeMasterId\":52,\"attributeValues\":\"0%\",\"description\":\"cos 6\",\"name\":\"cos 6\"},{\"attributeId\":240744,\"attributeMasterId\":19,\"attributeValues\":\"No\",\"description\":\"Resiliency\",\"name\":\"Resiliency\"},{\"attributeId\":240745,\"attributeMasterId\":20,\"attributeValues\":\"LC\",\"description\":\"Connector Type\",\"name\":\"Connector Type\"},{\"attributeId\":240746,\"attributeMasterId\":21,\"attributeValues\":\"Yes\",\"description\":\"Access Required\",\"name\":\"Access Required\"},{\"attributeId\":240747,\"attributeMasterId\":22,\"attributeValues\":\"Customer provided\",\"description\":\"CPE\",\"name\":\"CPE\"},{\"attributeId\":240748,\"attributeMasterId\":61,\"attributeValues\":\"Full Mesh\",\"description\":\"VPN Topology\",\"name\":\"VPN Topology\"},{\"attributeId\":240749,\"attributeMasterId\":62,\"attributeValues\":\"Mesh\",\"description\":\"Site Type\",\"name\":\"Site Type\"},{\"attributeId\":240750,\"attributeMasterId\":64,\"attributeValues\":\"Unmanaged\",\"description\":\"Access Topology\",\"name\":\"Access Topology\"}]}],\"locationId\":85,\"popLocationId\":1551,\"quoteVersion\":1,\"feasibility\":[{\"feasibilityCheck\":\"system\",\"feasibilityMode\":\"OnnetWL\",\"feasibilityCode\":\"1GKVIMTXLHBGNYQF\",\"rank\":2,\"responseJson\":null,\"provider\":\"TATA COMMUNICATIONS\",\"isSelected\":1,\"type\":\"primary\",\"createdTime\":1537949577000}],\"effectiveDate\":1537949100000,\"quoteSla\":[{\"id\":11741,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11741,\"slaDurationInDays\":null,\"slaName\":\"Service Availability %\"},\"slaValue\":\">= 99%\"},{\"id\":11742,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11742,\"slaDurationInDays\":null,\"slaName\":\"Mean Time To Restore (MTTR) in Hrs\"},\"slaValue\":\"2\"},{\"id\":11743,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11743,\"slaDurationInDays\":null,\"slaName\":\"Time To Restore (TTR) in Hrs\"},\"slaValue\":\"4\"},{\"id\":11744,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11744,\"slaDurationInDays\":null,\"slaName\":\"Jitter Servicer Level Target (ms)\"},\"slaValue\":\"[]\"},{\"id\":11745,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11745,\"slaDurationInDays\":null,\"slaName\":\"Packet Delivery Ratio Service Level Target %\"},\"slaValue\":\"[{\\\"cosKey\\\":\\\"cos 3\\\",\\\"cosValue\\\":\\\">= 99.99%\\\"}]\"}]},{\"siteId\":5391,\"siteCode\":\"NJ54JLOMI4BB5CIH\",\"fpStatus\":\"FP\",\"imageUrl\":\"./assets/images/gvpn-profile-images/P-L.png\",\"isFeasible\":1,\"mrc\":22862.55078125,\"nrc\":10000.0,\"arc\":274350.62,\"tcv\":284350.6,\"components\":[{\"componentId\":33820,\"componentMasterId\":12,\"referenceId\":5391,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240751,\"attributeMasterId\":1,\"attributeValues\":\"Fast Ethernet\",\"description\":\"Interface\",\"name\":\"Interface\"},{\"attributeId\":240752,\"attributeMasterId\":2,\"attributeValues\":\"BGP\",\"description\":\"Routing Protocol\",\"name\":\"Routing Protocol\"},{\"attributeId\":240753,\"attributeMasterId\":3,\"attributeValues\":\"10\",\"description\":\"Port Bandwidth\",\"name\":\"Port Bandwidth\"},{\"attributeId\":240754,\"attributeMasterId\":78,\"attributeValues\":\"TCL\",\"description\":\"WAN IP Provided By\",\"name\":\"WAN IP Provided By\"},{\"attributeId\":240755,\"attributeMasterId\":79,\"attributeValues\":\"\",\"description\":\"WAN IP Address\",\"name\":\"WAN IP Address\"},{\"attributeId\":240756,\"attributeMasterId\":4,\"attributeValues\":\"Fixed\",\"description\":\"Service type\",\"name\":\"Service type\"},{\"attributeId\":240757,\"attributeMasterId\":5,\"attributeValues\":\"\",\"description\":\"Burstable Bandwidth\",\"name\":\"Burstable Bandwidth\"},{\"attributeId\":240758,\"attributeMasterId\":6,\"attributeValues\":\"\",\"description\":\"Usage Model\",\"name\":\"Usage Model\"},{\"attributeId\":240759,\"attributeMasterId\":10,\"attributeValues\":\"No\",\"description\":\"Extended LAN Required?\",\"name\":\"Extended LAN Required?\"},{\"attributeId\":240760,\"attributeMasterId\":11,\"attributeValues\":\"No\",\"description\":\"BFD Required\",\"name\":\"BFD Required\"},{\"attributeId\":240761,\"attributeMasterId\":12,\"attributeValues\":\"Loopback\",\"description\":\"BGP Peering on\",\"name\":\"BGP Peering on\"},{\"attributeId\":240762,\"attributeMasterId\":75,\"attributeValues\":\"\",\"description\":\"BGP AS Number\",\"name\":\"BGP AS Number\"},{\"attributeId\":240763,\"attributeMasterId\":76,\"attributeValues\":\"\",\"description\":\"Customer prefixes\",\"name\":\"Customer prefixes\"}],\"price\":{\"id\":6297,\"effectiveMrc\":19527.880859375,\"effectiveNrc\":10000.0,\"effectiveArc\":234334.625,\"quoteId\":2018,\"referenceId\":\"33820\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33821,\"componentMasterId\":2,\"referenceId\":5391,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240764,\"attributeMasterId\":13,\"attributeValues\":\"No\",\"description\":\"Shared Last Mile\",\"name\":\"Shared Last Mile\"},{\"attributeId\":240765,\"attributeMasterId\":14,\"attributeValues\":\"\",\"description\":\"Shared Last Mile Service ID\",\"name\":\"Shared Last Mile Service ID\"},{\"attributeId\":240766,\"attributeMasterId\":15,\"attributeValues\":\"10\",\"description\":\"Local Loop Bandwidth\",\"name\":\"Local Loop Bandwidth\"}],\"price\":{\"id\":6298,\"effectiveMrc\":3334.669921875,\"effectiveNrc\":0.0,\"effectiveArc\":40016.0,\"quoteId\":2018,\"referenceId\":\"33821\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33822,\"componentMasterId\":3,\"referenceId\":5391,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240767,\"attributeMasterId\":16,\"attributeValues\":\"Unmanaged\",\"description\":\"CPE Management Type\",\"name\":\"CPE Management Type\"},{\"attributeId\":240768,\"attributeMasterId\":17,\"attributeValues\":\"false\",\"description\":\"CPE Management\",\"name\":\"CPE Management\"}]},{\"componentId\":33823,\"componentMasterId\":10,\"referenceId\":5391,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240769,\"attributeMasterId\":18,\"attributeValues\":\"Enhanced\",\"description\":\"Service Variant\",\"name\":\"Service Variant\"},{\"attributeId\":240770,\"attributeMasterId\":25,\"attributeValues\":\"No\",\"description\":\"isAuthenticationRequired for protocol\",\"name\":\"isAuthenticationRequired for protocol\"},{\"attributeId\":240771,\"attributeMasterId\":26,\"attributeValues\":\"Default routes\",\"description\":\"Routes Exchanged\",\"name\":\"Routes Exchanged\"},{\"attributeId\":240772,\"attributeMasterId\":53,\"attributeValues\":\"Active\",\"description\":\"Port Mode\",\"name\":\"Port Mode\"},{\"attributeId\":240773,\"attributeMasterId\":27,\"attributeValues\":\"TCL private AS Number\",\"description\":\"AS Number\",\"name\":\"AS Number\"},{\"attributeId\":240774,\"attributeMasterId\":47,\"attributeValues\":\"0%\",\"description\":\"cos 1\",\"name\":\"cos 1\"},{\"attributeId\":240775,\"attributeMasterId\":48,\"attributeValues\":\"0%\",\"description\":\"cos 2\",\"name\":\"cos 2\"},{\"attributeId\":240776,\"attributeMasterId\":49,\"attributeValues\":\"100%\",\"description\":\"cos 3\",\"name\":\"cos 3\"},{\"attributeId\":240777,\"attributeMasterId\":50,\"attributeValues\":\"0%\",\"description\":\"cos 4\",\"name\":\"cos 4\"},{\"attributeId\":240778,\"attributeMasterId\":51,\"attributeValues\":\"0%\",\"description\":\"cos 5\",\"name\":\"cos 5\"},{\"attributeId\":240779,\"attributeMasterId\":52,\"attributeValues\":\"0%\",\"description\":\"cos 6\",\"name\":\"cos 6\"},{\"attributeId\":240780,\"attributeMasterId\":19,\"attributeValues\":\"No\",\"description\":\"Resiliency\",\"name\":\"Resiliency\"},{\"attributeId\":240781,\"attributeMasterId\":20,\"attributeValues\":\"LC\",\"description\":\"Connector Type\",\"name\":\"Connector Type\"},{\"attributeId\":240782,\"attributeMasterId\":21,\"attributeValues\":\"Yes\",\"description\":\"Access Required\",\"name\":\"Access Required\"},{\"attributeId\":240783,\"attributeMasterId\":22,\"attributeValues\":\"Customer provided\",\"description\":\"CPE\",\"name\":\"CPE\"},{\"attributeId\":240784,\"attributeMasterId\":61,\"attributeValues\":\"Full Mesh\",\"description\":\"VPN Topology\",\"name\":\"VPN Topology\"},{\"attributeId\":240785,\"attributeMasterId\":62,\"attributeValues\":\"Mesh\",\"description\":\"Site Type\",\"name\":\"Site Type\"},{\"attributeId\":240786,\"attributeMasterId\":64,\"attributeValues\":\"Unmanaged\",\"description\":\"Access Topology\",\"name\":\"Access Topology\"}]}],\"locationId\":1654,\"popLocationId\":1546,\"quoteVersion\":1,\"feasibility\":[{\"feasibilityCheck\":\"system\",\"feasibilityMode\":\"OnnetWL\",\"feasibilityCode\":\"4P074TLZBVKGCTSN\",\"rank\":2,\"responseJson\":null,\"provider\":\"TATA COMMUNICATIONS\",\"isSelected\":1,\"type\":\"primary\",\"createdTime\":1537949577000}],\"effectiveDate\":1537949100000,\"quoteSla\":[{\"id\":11746,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11746,\"slaDurationInDays\":null,\"slaName\":\"Service Availability %\"},\"slaValue\":\">= 99%\"},{\"id\":11747,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11747,\"slaDurationInDays\":null,\"slaName\":\"Mean Time To Restore (MTTR) in Hrs\"},\"slaValue\":\"2\"},{\"id\":11748,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11748,\"slaDurationInDays\":null,\"slaName\":\"Time To Restore (TTR) in Hrs\"},\"slaValue\":\"4\"},{\"id\":11749,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11749,\"slaDurationInDays\":null,\"slaName\":\"Jitter Servicer Level Target (ms)\"},\"slaValue\":\"[]\"},{\"id\":11750,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11750,\"slaDurationInDays\":null,\"slaName\":\"Packet Delivery Ratio Service Level Target %\"},\"slaValue\":\"[{\\\"cosKey\\\":\\\"cos 3\\\",\\\"cosValue\\\":\\\">= 99.99%\\\"}]\"}]}]},{\"productSolutionId\":2483,\"offeringDescription\":\"Dual Unmanaged GVPN\",\"offeringName\":\"Dual Unmanaged GVPN\",\"status\":1,\"solution\":{\"offeringName\":\"Dual Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"2\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"2\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"2\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"2\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"Yes\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]}]},\"sites\":[]}],\"productName\":\"GVPN\",\"status\":1,\"termsAndCondition\":\"https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf\"}],\"legalAttributes\":[{\"attributeValue\":\"INR\",\"displayValue\":\"Billing Currency\",\"version\":1,\"mstOmsAttribute\":{\"id\":13,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"INR\",\"isActive\":null,\"name\":\"Billing Currency\"}},{\"attributeValue\":\"4\",\"displayValue\":\"MSA\",\"version\":1,\"mstOmsAttribute\":{\"id\":16,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"1\",\"isActive\":null,\"name\":\"MSA\"}},{\"attributeValue\":\"801-804, Tulsiani Chambers, 8th Floor, Nariman Point   Mumbai Maharashtra India 400098\",\"displayValue\":\"Notice_Address\",\"version\":1,\"mstOmsAttribute\":{\"id\":90,\"category\":null,\"createdBy\":\"optimus_shriram\",\"createdTime\":1534139975000,\"description\":\"Notice_Address\",\"isActive\":null,\"name\":\"Notice_Address\"}},{\"attributeValue\":\"optimus_regus\",\"displayValue\":\"CONTACTID\",\"version\":1,\"mstOmsAttribute\":{\"id\":78,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532942130000,\"description\":\"5\",\"isActive\":null,\"name\":\"CONTACTID\"}},{\"attributeValue\":\"Proposal Sent\",\"displayValue\":\"Proposal Sent\",\"version\":1,\"mstOmsAttribute\":{\"id\":28,\"category\":null,\"createdBy\":\"admin\",\"createdTime\":1531637490000,\"description\":\"SFDC_STAGE\",\"isActive\":null,\"name\":\"SFDC_STAGE\"}},{\"attributeValue\":\"harpreet.singh3@tatacommunications.com\",\"displayValue\":\"SUPPLIER_LE_EMAIL\",\"version\":1,\"mstOmsAttribute\":{\"id\":86,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1533175623000,\"description\":\"SUPPLIER_LE_EMAIL\",\"isActive\":null,\"name\":\"SUPPLIER_LE_EMAIL\"}},{\"attributeValue\":\"212\",\"displayValue\":\"212\",\"version\":1,\"mstOmsAttribute\":{\"id\":83,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532995311000,\"description\":\"BILLING_CONTACT_ID\",\"isActive\":null,\"name\":\"BILLING_CONTACT_ID\"}},{\"attributeValue\":\"true\",\"displayValue\":\"is_feasiblity_check_done\",\"version\":1,\"mstOmsAttribute\":{\"id\":4,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531241153000,\"description\":null,\"isActive\":null,\"name\":\"is_feasiblity_check_done\"}},{\"attributeValue\":\"Billable\",\"displayValue\":\"Billing Type\",\"version\":1,\"mstOmsAttribute\":{\"id\":9,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Site Wise\",\"isActive\":null,\"name\":\"Billing Type\"}},{\"attributeValue\":\"163721\",\"displayValue\":\"163721\",\"version\":1,\"mstOmsAttribute\":{\"id\":27,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531624287000,\"description\":\"CUSTOMER_LE_CUID\",\"isActive\":null,\"name\":\"CUSTOMER_LE_CUID\"}},{\"attributeValue\":\"ARC\",\"displayValue\":\"RECURRING_CHARGE_TYPE\",\"version\":1,\"mstOmsAttribute\":{\"id\":95,\"category\":null,\"createdBy\":\"optimus_regus\",\"createdTime\":1537775134000,\"description\":\"ARC\",\"isActive\":null,\"name\":\"RECURRING_CHARGE_TYPE\"}},{\"attributeValue\":\"Regus\",\"displayValue\":\"CONTACTNAME\",\"version\":1,\"mstOmsAttribute\":{\"id\":76,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532942130000,\"description\":\"root\",\"isActive\":null,\"name\":\"CONTACTNAME\"}},{\"attributeValue\":\"99\",\"displayValue\":\"ERF_LE_STATE_GST_INFO_ID\",\"version\":1,\"mstOmsAttribute\":{\"id\":75,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532933948000,\"description\":\"\",\"isActive\":null,\"name\":\"ERF_LE_STATE_GST_INFO_ID\"}},{\"attributeValue\":\"Account Manager\",\"displayValue\":\"Customer Contact Title / Designation\",\"version\":1,\"mstOmsAttribute\":{\"id\":24,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Project Manager\",\"isActive\":null,\"name\":\"Customer Contact Title / Designation\"}},{\"attributeValue\":\"-\",\"displayValue\":\"Le Contact\",\"version\":1,\"mstOmsAttribute\":{\"id\":87,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1533175623000,\"description\":\"Le Contact\",\"isActive\":null,\"name\":\"Le Contact\"}},{\"attributeValue\":\"single\",\"displayValue\":\"entityName\",\"version\":1,\"mstOmsAttribute\":{\"id\":30,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531909786000,\"description\":\"\",\"isActive\":null,\"name\":\"entityName\"}},{\"attributeValue\":\"INR\",\"displayValue\":\"Payment Currency\",\"version\":1,\"mstOmsAttribute\":{\"id\":14,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"INR\",\"isActive\":null,\"name\":\"Payment Currency\"}},{\"attributeValue\":\"Tata Communications Limited, India\",\"displayValue\":\"Tata Communications Limited, India\",\"version\":1,\"mstOmsAttribute\":{\"id\":72,\"category\":null,\"createdBy\":\"idriveoptimus@gmail.com\",\"createdTime\":1532317828000,\"description\":\"Supplier Contracting Entity\",\"isActive\":null,\"name\":\"Supplier Contracting Entity\"}},{\"displayValue\":\"DESIGNATION\",\"version\":1,\"mstOmsAttribute\":{\"id\":82,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532991407000,\"description\":null,\"isActive\":null,\"name\":\"DESIGNATION\"}},{\"attributeValue\":\"true\",\"displayValue\":\"is_pricing_check_done\",\"version\":1,\"mstOmsAttribute\":{\"id\":5,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531241187000,\"description\":null,\"isActive\":null,\"name\":\"is_pricing_check_done\"}},{\"attributeValue\":\"2441\",\"displayValue\":\"Customer Contracting Entity\",\"version\":1,\"mstOmsAttribute\":{\"id\":23,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"66\",\"isActive\":null,\"name\":\"Customer Contracting Entity\"}},{\"attributeValue\":\"5\",\"displayValue\":\"Service Schedule\",\"version\":1,\"mstOmsAttribute\":{\"id\":20,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"2\",\"isActive\":null,\"name\":\"Service Schedule\"}},{\"attributeValue\":\"manojkumar.rajakumaran@tatacommunications.com\",\"displayValue\":\"CONTACTEMAIL\",\"version\":1,\"mstOmsAttribute\":{\"id\":77,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532942130000,\"description\":\"root\",\"isActive\":null,\"name\":\"CONTACTEMAIL\"}},{\"attributeValue\":\"0000000000\",\"displayValue\":\"CONTACTNO\",\"version\":1,\"mstOmsAttribute\":{\"id\":79,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532942130000,\"description\":null,\"isActive\":null,\"name\":\"CONTACTNO\"}},{\"attributeValue\":\"Paper/Electronic\",\"displayValue\":\"Invoice Method\",\"version\":1,\"mstOmsAttribute\":{\"id\":12,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Electronic\",\"isActive\":null,\"name\":\"Invoice Method\"}},{\"attributeValue\":\"1 Year\",\"displayValue\":\"TermInMonths\",\"version\":1,\"mstOmsAttribute\":{\"id\":7,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531274772000,\"description\":\"TermInMonths\",\"isActive\":null,\"name\":\"TermInMonths\"}},{\"attributeValue\":\"Advance\",\"displayValue\":\"Billing Method\",\"version\":1,\"mstOmsAttribute\":{\"id\":10,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Arrears\",\"isActive\":null,\"name\":\"Billing Method\"}},{\"attributeValue\":\"Harpreet Singh\",\"displayValue\":\"SUPPLIER_LE_OWNER\",\"version\":1,\"mstOmsAttribute\":{\"id\":85,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1533175623000,\"description\":\"SUPPLIER_LE_OWNER\",\"isActive\":null,\"name\":\"SUPPLIER_LE_OWNER\"}},{\"attributeValue\":\"30 days\",\"displayValue\":\"Payment Term (from date of invoice)\",\"version\":1,\"mstOmsAttribute\":{\"id\":81,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1532989659000,\"description\":\"Payment Term (from date of invoice)\",\"isActive\":null,\"name\":\"Payment Term (from date of invoice)\"}},{\"attributeValue\":\"801-804, Tulsiani Chambers, 8th Floor, Nariman Point   Mumbai Maharashtra India 400098\",\"displayValue\":\"Billing Address\",\"version\":1,\"mstOmsAttribute\":{\"id\":18,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Kasturi Buildings 859 & 860 Anna Salai Chennai Tamil Nadu 600002\",\"isActive\":null,\"name\":\"Billing Address\"}},{\"attributeValue\":\"0012000000BSomfAAD\",\"displayValue\":\"0012000000BSomfAAD\",\"version\":1,\"mstOmsAttribute\":{\"id\":29,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531695664000,\"description\":\"CUSTOMER_ACCOUNT_ID\",\"isActive\":null,\"name\":\"CUSTOMER_ACCOUNT_ID\"}},{\"attributeValue\":\"Regus GEM Business Centre Private Limited\",\"displayValue\":\"Regus GEM Business Centre Private Limited\",\"version\":1,\"mstOmsAttribute\":{\"id\":84,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1533028387000,\"description\":\"LEGAL_ENTITY_NAME\",\"isActive\":null,\"name\":\"LEGAL_ENTITY_NAME\"}},{\"attributeValue\":\"false\",\"displayValue\":\"louRequired\",\"version\":1,\"mstOmsAttribute\":{\"id\":1,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1530865259000,\"description\":\"false\",\"isActive\":null,\"name\":\"louRequired\"}},{\"attributeValue\":\"Monthly\",\"displayValue\":\"Billing Frequency\",\"version\":1,\"mstOmsAttribute\":{\"id\":11,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"Quarterly\",\"isActive\":null,\"name\":\"Billing Frequency\"}},{\"attributeValue\":\"2\",\"displayValue\":\"customerLegalEntityId\",\"version\":1,\"mstOmsAttribute\":{\"id\":31,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531909787000,\"description\":\"\",\"isActive\":null,\"name\":\"customerLegalEntityId\"}},{\"attributeValue\":\"-\",\"displayValue\":\"GST_Number\",\"version\":1,\"mstOmsAttribute\":{\"id\":19,\"category\":null,\"createdBy\":\"root\",\"createdTime\":1531553670000,\"description\":\"06AAACD5767E1ZR\",\"isActive\":null,\"name\":\"GST_Number\"}}]}]}";

        return (QuoteBean) Utils.convertJsonToObject(json, QuoteBean.class);
    }

    public QuoteBean getQuoteBean() throws TclCommonException {
        QuoteBean quoteDto = new QuoteBean(getQuote());
        for (QuoteToLeBean quoteLeBean : quoteDto.getLegalEntities()) {
            quoteLeBean.setLegalAttributes(getLegalAttributeBeanList());
            quoteLeBean.setProductFamilies(getQuoteToLeProductFamilyBean());
            quoteLeBean.setFinalArc(Double.parseDouble("450356"));
            quoteLeBean.setFinalMrc(Double.parseDouble("450356"));
            quoteLeBean.setFinalNrc(Double.parseDouble("450356"));
            quoteLeBean.setSupplierLegalEntityId(1);
        }

        return quoteDto;

    }

    public Set<LegalAttributeBean> getLegalAttributeBeanList() {
        Set<LegalAttributeBean> legalAttributeBean = new HashSet<>();
        LegalAttributeBean legalAttributeBean1 = getLegalAttributeBean();
        legalAttributeBean1.setMstOmsAttribute(getMstOmsAttributeBean("LEGAL_ENTITY_NAME"));
        LegalAttributeBean legalAttributeBean2 = getLegalAttributeBean();
        legalAttributeBean2.setMstOmsAttribute(getMstOmsAttributeBean("GST_Number"));
        LegalAttributeBean legalAttributeBean3 = getLegalAttributeBean();
        legalAttributeBean3.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTNAME"));
        LegalAttributeBean legalAttributeBean4 = getLegalAttributeBean();
        legalAttributeBean4.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTNO"));
        LegalAttributeBean legalAttributeBean5 = getLegalAttributeBean();
        legalAttributeBean5.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTEMAIL"));
        LegalAttributeBean legalAttributeBean6 = getLegalAttributeBean();
        legalAttributeBean6.setMstOmsAttribute(getMstOmsAttributeBean("Supplier Contracting Entity"));
        LegalAttributeBean legalAttributeBean7 = getLegalAttributeBean();
        legalAttributeBean7.setMstOmsAttribute(getMstOmsAttributeBean("SUPPLIER_LE_OWNER"));
        LegalAttributeBean legalAttributeBean8 = getLegalAttributeBean();
        legalAttributeBean8.setMstOmsAttribute(getMstOmsAttributeBean("SUPPLIER_LE_EMAIL"));
        LegalAttributeBean legalAttributeBean9 = getLegalAttributeBean();
        legalAttributeBean9.setMstOmsAttribute(getMstOmsAttributeBean("Le Contact"));
        LegalAttributeBean legalAttributeBean10 = getLegalAttributeBean();
        legalAttributeBean10.setMstOmsAttribute(getMstOmsAttributeBean("Billing Method"));
        LegalAttributeBean legalAttributeBean11 = getLegalAttributeBean();
        legalAttributeBean11.setMstOmsAttribute(getMstOmsAttributeBean("Billing Frequency"));
        LegalAttributeBean legalAttributeBean12 = getLegalAttributeBean();
        legalAttributeBean12.setMstOmsAttribute(getMstOmsAttributeBean("Billing Currency"));
        LegalAttributeBean legalAttributeBean13 = getLegalAttributeBean();
        legalAttributeBean13.setMstOmsAttribute(getMstOmsAttributeBean("Payment Currency"));
        LegalAttributeBean legalAttributeBean14 = getLegalAttributeBean();
        legalAttributeBean14.setMstOmsAttribute(getMstOmsAttributeBean("Payment Term (from date of invoice)"));
        LegalAttributeBean legalAttributeBean15 = getLegalAttributeBean();
        legalAttributeBean15.setMstOmsAttribute(getMstOmsAttributeBean("Invoice Method"));
        LegalAttributeBean legalAttributeBean16 = getLegalAttributeBean();
        legalAttributeBean16.setMstOmsAttribute(getMstOmsAttributeBean("TermInMonths"));
        LegalAttributeBean legalAttributeBean19 = getLegalAttributeBean();
        legalAttributeBean19.setMstOmsAttribute(getMstOmsAttributeBean("MSA"));
        LegalAttributeBean legalAttributeBean20 = getLegalAttributeBean();
        legalAttributeBean20.setMstOmsAttribute(getMstOmsAttributeBean("Service Schedule"));
        legalAttributeBean.add(legalAttributeBean1);
        legalAttributeBean.add(legalAttributeBean2);
        legalAttributeBean.add(legalAttributeBean3);
        legalAttributeBean.add(legalAttributeBean4);
        legalAttributeBean.add(legalAttributeBean5);
        legalAttributeBean.add(legalAttributeBean6);
        legalAttributeBean.add(legalAttributeBean7);
        legalAttributeBean.add(legalAttributeBean8);
        legalAttributeBean.add(legalAttributeBean9);
        legalAttributeBean.add(legalAttributeBean10);
        legalAttributeBean.add(legalAttributeBean11);
        legalAttributeBean.add(legalAttributeBean12);
        legalAttributeBean.add(legalAttributeBean13);
        legalAttributeBean.add(legalAttributeBean14);
        legalAttributeBean.add(legalAttributeBean15);
        legalAttributeBean.add(legalAttributeBean16);
        legalAttributeBean.add(legalAttributeBean19);
        legalAttributeBean.add(legalAttributeBean20);
        return legalAttributeBean;
    }

    public MstOmsAttributeBean getMstOmsAttributeBean(String attrName) {
        MstOmsAttributeBean mstOmsAttributeBean = new MstOmsAttributeBean();
        mstOmsAttributeBean.setName(attrName);
        return mstOmsAttributeBean;

    }

    public LegalAttributeBean getLegalAttributeBean() {
        LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
        legalAttributeBean.setId(1);
        legalAttributeBean.setAttributeValue("Value");
        legalAttributeBean.setDisplayValue("displayValue");
        return legalAttributeBean;
    }

    public Set<QuoteToLeProductFamilyBean> getQuoteToLeProductFamilyBean() throws TclCommonException {
        Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamilyBean qFamily = new QuoteToLeProductFamilyBean();
        qFamily.setId(1);
        qFamily.setProductName("GVPN");
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("GVPN");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setSolutions(getProductSolutionBean());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public List<ProductSolutionBean> getProductSolutionBean() throws TclCommonException {

        List<ProductSolutionBean> productSolutions = new ArrayList<>();
        ProductSolutionBean productSolution = new ProductSolutionBean();

        productSolution.setProductSolutionId(1);
        productSolution.setOfferingName("Dual Unmanaged GVPN");

        productSolution.setProductProfileData(
                "{\"offeringName\":\"Dual Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"Yes\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]}]}");
        productSolution.setSites(getQuoteIllSiteBean());
        productSolution.setSolution(getSolutionDetail());
        productSolutions.add(productSolution);
        return productSolutions;
    }

    /**
     * getSolutionDetail
     *
     * @return
     * @throws TclCommonException
     */
    private SolutionDetail getSolutionDetail() throws TclCommonException {

        String solution = "{\"offeringName\":\"Dual Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"Yes\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]}]}";
        SolutionDetail res = (SolutionDetail) Utils.convertJsonToObject(solution, SolutionDetail.class);
        return res;
    }

    /**
     * getQuoteIllSiteBean
     *
     * @return
     * @throws TclCommonException
     */
    public List<QuoteIllSiteBean> getQuoteIllSiteBean() throws TclCommonException {
        List<QuoteIllSiteBean> list = new ArrayList<>();
        QuoteIllSiteBean quoteIllSiteBean = new QuoteIllSiteBean();
        quoteIllSiteBean.setSiteId(1);
        quoteIllSiteBean.setArc(78.0D);
        quoteIllSiteBean.setBandwidth("tes");
        quoteIllSiteBean.setIsFeasible((byte) 1);
        quoteIllSiteBean.setMrc(78.0D);
        quoteIllSiteBean.setNrc(78.0D);

        quoteIllSiteBean.setLocationId(1);
        quoteIllSiteBean.setComponents(getComponentsBean1());
        quoteIllSiteBean.setFeasibility(getFeasibilityDetails());
        quoteIllSiteBean.setQuoteSla(getQuoteSlaDetails());
        list.add(quoteIllSiteBean);
        return list;
    }

    /**
     * getQuoteSlaDetails
     *
     * @return
     * @throws TclCommonException
     */
    private List<QuoteSlaBean> getQuoteSlaDetails() throws TclCommonException {
        String json = "[{\"id\":11741,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11741,\"slaDurationInDays\":null,\"slaName\":\"Service Availability %\"},\"slaValue\":\">= 99%\"},{\"id\":11742,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11742,\"slaDurationInDays\":null,\"slaName\":\"Mean Time To Restore (MTTR) in Hrs\"},\"slaValue\":\"2\"},{\"id\":11743,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11743,\"slaDurationInDays\":null,\"slaName\":\"Time To Restore (TTR) in Hrs\"},\"slaValue\":\"4\"},{\"id\":11744,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11744,\"slaDurationInDays\":null,\"slaName\":\"Jitter Servicer Level Target (ms)\"},\"slaValue\":\"[]\"},{\"id\":11745,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11745,\"slaDurationInDays\":null,\"slaName\":\"Packet Delivery Ratio Service Level Target %\"},\"slaValue\":\"[{\\\"cosKey\\\":\\\"cos 3\\\",\\\"cosValue\\\":\\\">= 99.99%\\\"}]\"}]";
        List<QuoteSlaBean> list = new ArrayList<>();

        QuoteSlaBean[] comp = (QuoteSlaBean[]) Utils.convertJsonToObject(json, QuoteSlaBean[].class);
        for (int i = 0; i <= comp.length - 1; i++) {
            list.add(comp[i]);

        }
        return list;
    }

    /**
     * getFeasibilityDetails
     *
     * @return
     * @throws TclCommonException
     */
    private List<SiteFeasibilityBean> getFeasibilityDetails() throws TclCommonException {
        List<SiteFeasibilityBean> list = new ArrayList<>();

        String json = "[{\"feasibilityCheck\":\"system\",\"feasibilityMode\":\"OnnetWL\",\"feasibilityCode\":\"1GKVIMTXLHBGNYQF\",\"rank\":2,\"responseJson\":null,\"provider\":\"TATA COMMUNICATIONS\",\"isSelected\":1,\"type\":\"primary\",\"createdTime\":1537949577000}]";
        SiteFeasibilityBean[] comp = (SiteFeasibilityBean[]) Utils.convertJsonToObject(json,
                SiteFeasibilityBean[].class);
        for (int i = 0; i <= comp.length - 1; i++) {
            list.add(comp[i]);

        }
        return list;
    }

    /**
     * getComponentsBean
     *
     * @return
     */
    private List<QuoteProductComponentBean> getComponentsBean() {
        List<QuoteProductComponentBean> list = new ArrayList<>();
        List<String> componenList = new ArrayList<>();
        componenList.add("VPN Port");
        componenList.add("CPE");
        componenList.add("CPE Management");
        componenList.add("GVPN Common");
        componenList.add("Last mile");
        componenList.add("Addon");

        componenList.forEach(cmp -> {
            QuoteProductComponentBean bean = new QuoteProductComponentBean();
            bean.setType("primary");
            bean.setName(cmp);
            bean.setReferenceId(1);
            bean.setAttributes(getAttributesList());
            bean.setPrice(getQuotePriceDetails());
            list.add(bean);
        });

        return list;
    }

    public QuoteToLe getQuoteToLeSaveSlaForElse() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilySlaForElse());
        return quoteToLe;
    }

    public QuoteToLe getQuoteToLeSaveSlaForElseIAS() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilySlaForElseIAS());
        return quoteToLe;
    }

    public QuoteToLe getQuoteToLeSaveSla() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilySla());
        return quoteToLe;
    }

    public QuoteToLe getQuoteToLeSaveSlaIAS() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);

        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilySlaIAS());
        return quoteToLe;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilySla() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("GVPN");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolutionSLA());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilySlaIAS() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("IAS");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolutionSLA());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilySlaForElse() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("GVPN");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolutionSLAForElse());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilySlaForElseIAS() {
        Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
        QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
        qFamily.setId(1);
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("IAS");
        mstProductFamily.setStatus((byte) 2);
        qFamily.setMstProductFamily(mstProductFamily);
        qFamily.setProductSolutions(getProductSolutionSLAForElse());
        quoteToLeProductFamilySet.add(qFamily);
        return quoteToLeProductFamilySet;
    }

    public Set<ProductSolution> getProductSolutionSLA() {

        Set<ProductSolution> productSolutions = new HashSet<>();
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setMstProductOffering(getMstOfferingSLA());
        productSolution.setQuoteIllSites(getIllsitesSLA());
        productSolutions.add(productSolution);
        return productSolutions;
    }

    public Set<ProductSolution> getProductSolutionSLAForElse() {

        Set<ProductSolution> productSolutions = new HashSet<>();
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"single unmanaged gvpn\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
        productSolution.setMstProductOffering(getMstOfferingSLA());
        productSolution.setQuoteIllSites(getIllsitesSLAForElse());
        productSolutions.add(productSolution);
        return productSolutions;
    }

    public MstProductOffering getMstOfferingSLA() {
        MstProductOffering mstProductOffering = new MstProductOffering();
        mstProductOffering.setId(1);
        mstProductOffering.setErfProductOfferingId(1);
        mstProductOffering.setProductDescription("viek");
        mstProductOffering.setProductName("single unmanaged gvpn");
        return mstProductOffering;
    }

    public Set<QuoteIllSite> getIllsitesSLA() {
        Set<QuoteIllSite> siteList = new HashSet<>();
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        // site.setQuoteVersion(1);
        site.setStatus((byte) 1);
        // site.setQuoteVersion(1);
        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        site.setArc(13.8);
        site.setProductSolution(getSolution1SAL());
        // site.setErfLocSiteaLocationId(1);
        // site.setErfLocSitebLocationId(1);

        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);

        site.setErfLocSiteaLocationId(2);
        site.setErfLocSiteaSiteCode("CODE234243#$");
        // site.setErfLocSiteaSiteCode("1");
        site.setErfLocSitebLocationId(2);
        site.setErfLocSitebSiteCode("2");
        siteList.add(site);
        return siteList;
    }

    public Set<QuoteIllSite> getIllsitesSLAForElse() {
        Set<QuoteIllSite> siteList = new HashSet<>();
        QuoteIllSite site = new QuoteIllSite();
        site.setId(1);
        site.setIsTaxExempted((byte) 0);
        site.setFeasibility((byte) 0);
        site.setMrc(9.8);
        site.setNrc(19.8);
        // site.setQuoteVersion(1);
        site.setStatus((byte) 1);
        // site.setQuoteVersion(1);
        site.setErfLrSolutionId("YU");
        site.setFeasibility((byte) 1);
        site.setIsTaxExempted((byte) 1);
        site.setArc(13.8);
        site.setProductSolution(getSolution1SAL());
        // site.setErfLocSiteaLocationId(1);
        // site.setErfLocSitebLocationId(1);

        site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
        site.setStatus((byte) 1);

        site.setErfLocSiteaLocationId(2);
        site.setErfLocSiteaSiteCode(null);
        // site.setErfLocSiteaSiteCode("1");
        site.setErfLocSitebLocationId(2);
        site.setErfLocSitebSiteCode("2");
        siteList.add(site);
        return siteList;
    }

    public ProductSolution getSolution1SAL() {
        ProductSolution productSolution = new ProductSolution();
        productSolution.setId(1);
        productSolution.setProductProfileData(
                "{\"offeringName\":\"single unmanaged gvpn\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");

        // productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily1());
        productSolution.setMstProductOffering(getMstOfferingSLA());
        return productSolution;
    }

    public List<QuoteProductComponentsAttributeValue> createQuoteProductAttributeValue1() {
        List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
        list.add(createQuoteProductComponentsAttributeValue());
        return list;
    }

    public ProductAttributeMaster getProductAtrribute1() {
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("GVPN_CITY_TIER_SECONDARY");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(2);

        return productAttributeMaster;
    }

    public ProductAttributeMaster getProductAtrributePrimary() {
        ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
        productAttributeMaster.setName("GVPN_CITY_TIER_PRIMARY");
        productAttributeMaster.setDescription("Cpe related");
        productAttributeMaster.setStatus((byte) 1);
        productAttributeMaster.setId(1);

        return productAttributeMaster;
    }

    public String getProductSlaBeanjsonGVPN() throws TclCommonException {
        String json = Utils.convertObjectToJson(getProductSlaBeanGVPN());
        return json;
    }

    public ProductSlaBean getProductSlaBeanGVPN() {
        ProductSlaBean productSlaBean = new ProductSlaBean();
        productSlaBean.setTier(1);
        productSlaBean.setTierName("GSC-PSTN");
        List<SLaDetailsBean> sLaDetails = new ArrayList<SLaDetailsBean>();
        sLaDetails.add(createSLaDetailsBean());
        sLaDetails.add(createSLaDetailsBean1());
        sLaDetails.add(createSLaDetailsBean2());
        sLaDetails.add(createSLaDetailsBean3());
        sLaDetails.add(createSLaDetailsBean4());
        sLaDetails.add(createSLaDetailsBean5());
        sLaDetails.add(createSLaDetailsBean6());
        productSlaBean.setsLaDetails(sLaDetails);
        return productSlaBean;
    }

    public SLaDetailsBean createSLaDetailsBean6() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Network Uptime");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public SLaDetailsBean createSLaDetailsBean1() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Service Availability %");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public SLaDetailsBean createSLaDetailsBean2() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Mean Time To Restore (MTTR) in Hrs");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public SLaDetailsBean createSLaDetailsBean3() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Jitter Servicer Level Target (ms)");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public SLaDetailsBean createSLaDetailsBean4() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Packet Delivery Ratio Service Level Target %");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public SLaDetailsBean createSLaDetailsBean5() {
        SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
        slaDetailsBean.setName("Time To Restore (TTR) in Hrs");
        slaDetailsBean.setSlaTier("tier1");
        slaDetailsBean.setSlaValue("sla");
        return slaDetailsBean;
    }

    public List<QuoteToLeProductFamily> getQuoteToLeFamilyListGVPN() {
        List<QuoteToLeProductFamily> list = new ArrayList<>();
        list.add(getQuoteToLeFamilyGVPN());
        return list;

    }

    public QuoteToLeProductFamily getQuoteToLeFamilyGVPN() {
        QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
        quoTefamily.setId(1);
        quoTefamily.setId(1);
        /* quoTefamily.toString(); */
        quoTefamily.setProductSolutions(getProductSolution());
        quoTefamily.setMstProductFamily(getMstProductFamilyGVPN());
        quoTefamily.setQuoteToLe(getQuoteToLe());
        return quoTefamily;

    }

    /**
     * getComponentsBean
     *
     * @return
     * @throws TclCommonException
     */
    private List<QuoteProductComponentBean> getComponentsBean1() throws TclCommonException {
        List<QuoteProductComponentBean> list = new ArrayList<>();
        String json = "[{\"componentId\":33816,\"componentMasterId\":12,\"referenceId\":5390,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240715,\"attributeMasterId\":1,\"attributeValues\":\"Fast Ethernet\",\"description\":\"Interface\",\"name\":\"Interface\"},{\"attributeId\":240716,\"attributeMasterId\":2,\"attributeValues\":\"BGP\",\"description\":\"Routing Protocol\",\"name\":\"Routing Protocol\"},{\"attributeId\":240717,\"attributeMasterId\":3,\"attributeValues\":\"10\",\"description\":\"Port Bandwidth\",\"name\":\"Port Bandwidth\"},{\"attributeId\":240718,\"attributeMasterId\":78,\"attributeValues\":\"TCL\",\"description\":\"WAN IP Provided By\",\"name\":\"WAN IP Provided By\"},{\"attributeId\":240719,\"attributeMasterId\":79,\"attributeValues\":\"\",\"description\":\"WAN IP Address\",\"name\":\"WAN IP Address\"},{\"attributeId\":240720,\"attributeMasterId\":4,\"attributeValues\":\"Fixed\",\"description\":\"Service type\",\"name\":\"Service type\"},{\"attributeId\":240721,\"attributeMasterId\":5,\"attributeValues\":\"\",\"description\":\"Burstable Bandwidth\",\"name\":\"Burstable Bandwidth\"},{\"attributeId\":240722,\"attributeMasterId\":6,\"attributeValues\":\"\",\"description\":\"Usage Model\",\"name\":\"Usage Model\"},{\"attributeId\":240723,\"attributeMasterId\":10,\"attributeValues\":\"No\",\"description\":\"Extended LAN Required?\",\"name\":\"Extended LAN Required?\"},{\"attributeId\":240724,\"attributeMasterId\":11,\"attributeValues\":\"No\",\"description\":\"BFD Required\",\"name\":\"BFD Required\"},{\"attributeId\":240725,\"attributeMasterId\":12,\"attributeValues\":\"Loopback\",\"description\":\"BGP Peering on\",\"name\":\"BGP Peering on\"},{\"attributeId\":240726,\"attributeMasterId\":75,\"attributeValues\":\"\",\"description\":\"BGP AS Number\",\"name\":\"BGP AS Number\"},{\"attributeId\":240727,\"attributeMasterId\":76,\"attributeValues\":\"\",\"description\":\"Customer prefixes\",\"name\":\"Customer prefixes\"}],\"price\":{\"id\":6295,\"effectiveMrc\":19527.880859375,\"effectiveNrc\":10000.0,\"effectiveArc\":234334.625,\"quoteId\":2018,\"referenceId\":\"33816\",\"referenceName\":\"COMPONENTS\"}},\r\n"
                + "{\r\n" + "  \"componentId\": 33816,\r\n" + "  \"componentMasterId\": 12,\r\n"
                + "  \"referenceId\": 5390,\r\n" + "  \"name\": \"CPE\",\r\n" + "  \"type\": \"primary\",\r\n"
                + "  \"attributes\": [\r\n" + "   \r\n" + "    {\r\n" + "      \"attributeId\": 240727,\r\n"
                + "      \"attributeMasterId\": 76,\r\n" + "      \"attributeValues\": \"CPE Basic Chassis\",\r\n"
                + "      \"description\": \"CPE Basic Chassis\",\r\n" + "      \"name\": \"CPE Basic Chassis\"\r\n"
                + "    }\r\n" + "  ],\r\n" + "  \"price\": {\r\n" + "    \"id\": 6295,\r\n"
                + "    \"effectiveMrc\": 19527.880859375,\r\n" + "    \"effectiveNrc\": 10000,\r\n"
                + "    \"effectiveArc\": 234334.625,\r\n" + "    \"quoteId\": 2018,\r\n"
                + "    \"referenceId\": \"33816\",\r\n" + "    \"referenceName\": \"COMPONENTS\"\r\n" + "  }\r\n"
                + "},{\"componentId\":33817,\"componentMasterId\":2,\"referenceId\":5390,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240728,\"attributeMasterId\":13,\"attributeValues\":\"No\",\"description\":\"Shared Last Mile\",\"name\":\"Shared Last Mile\"},{\"attributeId\":240729,\"attributeMasterId\":14,\"attributeValues\":\"\",\"description\":\"Shared Last Mile Service ID\",\"name\":\"Shared Last Mile Service ID\"},{\"attributeId\":240730,\"attributeMasterId\":15,\"attributeValues\":\"10\",\"description\":\"Local Loop Bandwidth\",\"name\":\"Local Loop Bandwidth\"}],\"price\":{\"id\":6296,\"effectiveMrc\":14981.669921875,\"effectiveNrc\":0.0,\"effectiveArc\":179780.0,\"quoteId\":2018,\"referenceId\":\"33817\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33818,\"componentMasterId\":3,\"referenceId\":5390,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240731,\"attributeMasterId\":16,\"attributeValues\":\"Unmanaged\",\"description\":\"CPE Management Type\",\"name\":\"CPE Management Type\"},{\"attributeId\":240732,\"attributeMasterId\":17,\"attributeValues\":\"false\",\"description\":\"CPE Management\",\"name\":\"CPE Management\"}]},{\"componentId\":33819,\"componentMasterId\":10,\"referenceId\":5390,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240733,\"attributeMasterId\":18,\"attributeValues\":\"Enhanced\",\"description\":\"Service Variant\",\"name\":\"Service Variant\"},{\"attributeId\":240734,\"attributeMasterId\":25,\"attributeValues\":\"No\",\"description\":\"isAuthenticationRequired for protocol\",\"name\":\"isAuthenticationRequired for protocol\"},{\"attributeId\":240735,\"attributeMasterId\":26,\"attributeValues\":\"Default routes\",\"description\":\"Routes Exchanged\",\"name\":\"Routes Exchanged\"},{\"attributeId\":240736,\"attributeMasterId\":53,\"attributeValues\":\"Active\",\"description\":\"Port Mode\",\"name\":\"Port Mode\"},{\"attributeId\":240737,\"attributeMasterId\":27,\"attributeValues\":\"TCL private AS Number\",\"description\":\"AS Number\",\"name\":\"AS Number\"},{\"attributeId\":240738,\"attributeMasterId\":47,\"attributeValues\":\"0%\",\"description\":\"cos 1\",\"name\":\"cos 1\"},{\"attributeId\":240739,\"attributeMasterId\":48,\"attributeValues\":\"0%\",\"description\":\"cos 2\",\"name\":\"cos 2\"},{\"attributeId\":240740,\"attributeMasterId\":49,\"attributeValues\":\"100%\",\"description\":\"cos 3\",\"name\":\"cos 3\"},{\"attributeId\":240741,\"attributeMasterId\":50,\"attributeValues\":\"0%\",\"description\":\"cos 4\",\"name\":\"cos 4\"},{\"attributeId\":240742,\"attributeMasterId\":51,\"attributeValues\":\"0%\",\"description\":\"cos 5\",\"name\":\"cos 5\"},{\"attributeId\":240743,\"attributeMasterId\":52,\"attributeValues\":\"0%\",\"description\":\"cos 6\",\"name\":\"cos 6\"},{\"attributeId\":240744,\"attributeMasterId\":19,\"attributeValues\":\"No\",\"description\":\"Resiliency\",\"name\":\"Resiliency\"},{\"attributeId\":240745,\"attributeMasterId\":20,\"attributeValues\":\"LC\",\"description\":\"Connector Type\",\"name\":\"Connector Type\"},{\"attributeId\":240746,\"attributeMasterId\":21,\"attributeValues\":\"Yes\",\"description\":\"Access Required\",\"name\":\"Access Required\"},{\"attributeId\":240747,\"attributeMasterId\":22,\"attributeValues\":\"Customer provided\",\"description\":\"CPE\",\"name\":\"CPE\"},{\"attributeId\":240748,\"attributeMasterId\":61,\"attributeValues\":\"Full Mesh\",\"description\":\"VPN Topology\",\"name\":\"VPN Topology\"},{\"attributeId\":240749,\"attributeMasterId\":62,\"attributeValues\":\"Mesh\",\"description\":\"Site Type\",\"name\":\"Site Type\"},{\"attributeId\":240750,\"attributeMasterId\":64,\"attributeValues\":\"Unmanaged\",\"description\":\"Access Topology\",\"name\":\"Access Topology\"}]}]";
        QuoteProductComponentBean[] comp = (QuoteProductComponentBean[]) Utils.convertJsonToObject(json,
                QuoteProductComponentBean[].class);
        for (int i = 0; i <= comp.length - 1; i++) {
            list.add(comp[i]);

        }
        list.addAll(getComponentsBeanSecon());
        return list;
    }

    private List<QuoteProductComponentBean> getComponentsBeanSecon() throws TclCommonException {
        List<QuoteProductComponentBean> list = new ArrayList<>();
        String json = "[\r\n" + "  {\r\n" + "    \"componentId\": 33816,\r\n" + "    \"componentMasterId\": 12,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"VPN Port\",\r\n"
                + "    \"type\": \"secondary\",\r\n" + "    \"attributes\": [\r\n" + "      {\r\n"
                + "        \"attributeId\": 240715,\r\n" + "        \"attributeMasterId\": 1,\r\n"
                + "        \"attributeValues\": \"Fast Ethernet\",\r\n" + "        \"description\": \"Interface\",\r\n"
                + "        \"name\": \"Interface\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240716,\r\n" + "        \"attributeMasterId\": 2,\r\n"
                + "        \"attributeValues\": \"BGP\",\r\n" + "        \"description\": \"Routing Protocol\",\r\n"
                + "        \"name\": \"Routing Protocol\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240717,\r\n" + "        \"attributeMasterId\": 3,\r\n"
                + "        \"attributeValues\": \"10\",\r\n" + "        \"description\": \"Port Bandwidth\",\r\n"
                + "        \"name\": \"Port Bandwidth\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240718,\r\n" + "        \"attributeMasterId\": 78,\r\n"
                + "        \"attributeValues\": \"TCL\",\r\n" + "        \"description\": \"WAN IP Provided By\",\r\n"
                + "        \"name\": \"WAN IP Provided By\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240719,\r\n" + "        \"attributeMasterId\": 79,\r\n"
                + "        \"attributeValues\": \"\",\r\n" + "        \"description\": \"WAN IP Address\",\r\n"
                + "        \"name\": \"WAN IP Address\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240720,\r\n" + "        \"attributeMasterId\": 4,\r\n"
                + "        \"attributeValues\": \"Fixed\",\r\n" + "        \"description\": \"Service type\",\r\n"
                + "        \"name\": \"Service type\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240721,\r\n" + "        \"attributeMasterId\": 5,\r\n"
                + "        \"attributeValues\": \"\",\r\n" + "        \"description\": \"Burstable Bandwidth\",\r\n"
                + "        \"name\": \"Burstable Bandwidth\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240722,\r\n" + "        \"attributeMasterId\": 6,\r\n"
                + "        \"attributeValues\": \"\",\r\n" + "        \"description\": \"Usage Model\",\r\n"
                + "        \"name\": \"Usage Model\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240723,\r\n" + "        \"attributeMasterId\": 10,\r\n"
                + "        \"attributeValues\": \"No\",\r\n"
                + "        \"description\": \"Extended LAN Required?\",\r\n"
                + "        \"name\": \"Extended LAN Required?\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240724,\r\n" + "        \"attributeMasterId\": 11,\r\n"
                + "        \"attributeValues\": \"No\",\r\n" + "        \"description\": \"BFD Required\",\r\n"
                + "        \"name\": \"BFD Required\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240725,\r\n" + "        \"attributeMasterId\": 12,\r\n"
                + "        \"attributeValues\": \"Loopback\",\r\n" + "        \"description\": \"BGP Peering on\",\r\n"
                + "        \"name\": \"BGP Peering on\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240726,\r\n" + "        \"attributeMasterId\": 75,\r\n"
                + "        \"attributeValues\": \"\",\r\n" + "        \"description\": \"BGP AS Number\",\r\n"
                + "        \"name\": \"BGP AS Number\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240727,\r\n" + "        \"attributeMasterId\": 76,\r\n"
                + "        \"attributeValues\": \"\",\r\n" + "        \"description\": \"Customer prefixes\",\r\n"
                + "        \"name\": \"Customer prefixes\"\r\n" + "      }\r\n" + "    ],\r\n" + "    \"price\": {\r\n"
                + "      \"id\": 6295,\r\n" + "      \"effectiveMrc\": 19527.880859375,\r\n"
                + "      \"effectiveNrc\": 10000,\r\n" + "      \"effectiveArc\": 234334.625,\r\n"
                + "      \"quoteId\": 2018,\r\n" + "      \"referenceId\": \"33816\",\r\n"
                + "      \"referenceName\": \"COMPONENTS\"\r\n" + "    }\r\n" + "  },\r\n" + "  {\r\n"
                + "    \"componentId\": 33816,\r\n" + "    \"componentMasterId\": 12,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"CPE\",\r\n" + "    \"type\": \"secondary\",\r\n"
                + "    \"attributes\": [\r\n" + "      {\r\n" + "        \"attributeId\": 240727,\r\n"
                + "        \"attributeMasterId\": 76,\r\n" + "        \"attributeValues\": \"CPE Basic Chassis\",\r\n"
                + "        \"description\": \"CPE Basic Chassis\",\r\n" + "        \"name\": \"CPE Basic Chassis\"\r\n"
                + "      }\r\n" + "    ],\r\n" + "    \"price\": {\r\n" + "      \"id\": 6295,\r\n"
                + "      \"effectiveMrc\": 19527.880859375,\r\n" + "      \"effectiveNrc\": 10000,\r\n"
                + "      \"effectiveArc\": 234334.625,\r\n" + "      \"quoteId\": 2018,\r\n"
                + "      \"referenceId\": \"33816\",\r\n" + "      \"referenceName\": \"COMPONENTS\"\r\n" + "    }\r\n"
                + "  },\r\n" + "  {\r\n" + "    \"componentId\": 33816,\r\n" + "    \"componentMasterId\": 12,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"Addon\",\r\n" + "    \"type\": \"secondary\",\r\n"
                + "    \"attributes\": [\r\n" + "      {\r\n" + "        \"attributeId\": 240727,\r\n"
                + "        \"attributeMasterId\": 76,\r\n" + "        \"attributeValues\": \"CPE Basic Chassis\",\r\n"
                + "        \"description\": \"CPE Basic Chassis\",\r\n" + "        \"name\": \"DNS\"\r\n"
                + "      },\r\n" + "      {\r\n" + "        \"attributeId\": 240727,\r\n"
                + "        \"attributeMasterId\": 76,\r\n" + "        \"attributeValues\": \"CPE Basic Chassis\",\r\n"
                + "        \"description\": \"CPE Basic Chassis\",\r\n" + "        \"name\": \"Additional IPs\"\r\n"
                + "      }\r\n" + "    ],\r\n" + "    \"price\": {\r\n" + "      \"id\": 6295,\r\n"
                + "      \"effectiveMrc\": 19527.880859375,\r\n" + "      \"effectiveNrc\": 10000,\r\n"
                + "      \"effectiveArc\": 234334.625,\r\n" + "      \"quoteId\": 2018,\r\n"
                + "      \"referenceId\": \"33816\",\r\n" + "      \"referenceName\": \"COMPONENTS\"\r\n" + "    }\r\n"
                + "  },\r\n" + "  {\r\n" + "    \"componentId\": 33817,\r\n" + "    \"componentMasterId\": 2,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"Last mile\",\r\n"
                + "    \"type\": \"secondary\",\r\n" + "    \"attributes\": [\r\n" + "      {\r\n"
                + "        \"attributeId\": 240728,\r\n" + "        \"attributeMasterId\": 13,\r\n"
                + "        \"attributeValues\": \"No\",\r\n" + "        \"description\": \"Shared Last Mile\",\r\n"
                + "        \"name\": \"Shared Last Mile\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240729,\r\n" + "        \"attributeMasterId\": 14,\r\n"
                + "        \"attributeValues\": \"\",\r\n"
                + "        \"description\": \"Shared Last Mile Service ID\",\r\n"
                + "        \"name\": \"Shared Last Mile Service ID\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240730,\r\n" + "        \"attributeMasterId\": 15,\r\n"
                + "        \"attributeValues\": \"10\",\r\n" + "        \"description\": \"Local Loop Bandwidth\",\r\n"
                + "        \"name\": \"Local Loop Bandwidth\"\r\n" + "      }\r\n" + "    ],\r\n"
                + "    \"price\": {\r\n" + "      \"id\": 6296,\r\n" + "      \"effectiveMrc\": 14981.669921875,\r\n"
                + "      \"effectiveNrc\": 0,\r\n" + "      \"effectiveArc\": 179780,\r\n"
                + "      \"quoteId\": 2018,\r\n" + "      \"referenceId\": \"33817\",\r\n"
                + "      \"referenceName\": \"COMPONENTS\"\r\n" + "    }\r\n" + "  },\r\n" + "  {\r\n"
                + "    \"componentId\": 33818,\r\n" + "    \"componentMasterId\": 3,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"CPE Management\",\r\n"
                + "    \"type\": \"secondary\",\r\n" + "    \"attributes\": [\r\n" + "      {\r\n"
                + "        \"attributeId\": 240731,\r\n" + "        \"attributeMasterId\": 16,\r\n"
                + "        \"attributeValues\": \"Unmanaged\",\r\n"
                + "        \"description\": \"CPE Management Type\",\r\n"
                + "        \"name\": \"CPE Management Type\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240732,\r\n" + "        \"attributeMasterId\": 17,\r\n"
                + "        \"attributeValues\": \"false\",\r\n" + "        \"description\": \"CPE Management\",\r\n"
                + "        \"name\": \"CPE Management\"\r\n" + "      }\r\n" + "    ]\r\n" + "  },\r\n" + "  {\r\n"
                + "    \"componentId\": 33819,\r\n" + "    \"componentMasterId\": 10,\r\n"
                + "    \"referenceId\": 5390,\r\n" + "    \"name\": \"GVPN Common\",\r\n"
                + "    \"type\": \"secondary\",\r\n" + "    \"attributes\": [\r\n" + "      {\r\n"
                + "        \"attributeId\": 240733,\r\n" + "        \"attributeMasterId\": 18,\r\n"
                + "        \"attributeValues\": \"Enhanced\",\r\n" + "        \"description\": \"Service Variant\",\r\n"
                + "        \"name\": \"Service Variant\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240734,\r\n" + "        \"attributeMasterId\": 25,\r\n"
                + "        \"attributeValues\": \"No\",\r\n"
                + "        \"description\": \"isAuthenticationRequired for protocol\",\r\n"
                + "        \"name\": \"isAuthenticationRequired for protocol\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240735,\r\n" + "        \"attributeMasterId\": 26,\r\n"
                + "        \"attributeValues\": \"Default routes\",\r\n"
                + "        \"description\": \"Routes Exchanged\",\r\n" + "        \"name\": \"Routes Exchanged\"\r\n"
                + "      },\r\n" + "      {\r\n" + "        \"attributeId\": 240736,\r\n"
                + "        \"attributeMasterId\": 53,\r\n" + "        \"attributeValues\": \"Active\",\r\n"
                + "        \"description\": \"Port Mode\",\r\n" + "        \"name\": \"Port Mode\"\r\n" + "      },\r\n"
                + "      {\r\n" + "        \"attributeId\": 240737,\r\n" + "        \"attributeMasterId\": 27,\r\n"
                + "        \"attributeValues\": \"TCL private AS Number\",\r\n"
                + "        \"description\": \"AS Number\",\r\n" + "        \"name\": \"AS Number\"\r\n" + "      },\r\n"
                + "      {\r\n" + "        \"attributeId\": 240738,\r\n" + "        \"attributeMasterId\": 47,\r\n"
                + "        \"attributeValues\": \"0%\",\r\n" + "        \"description\": \"cos 1\",\r\n"
                + "        \"name\": \"cos 1\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240739,\r\n" + "        \"attributeMasterId\": 48,\r\n"
                + "        \"attributeValues\": \"0%\",\r\n" + "        \"description\": \"cos 2\",\r\n"
                + "        \"name\": \"cos 2\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240740,\r\n" + "        \"attributeMasterId\": 49,\r\n"
                + "        \"attributeValues\": \"100%\",\r\n" + "        \"description\": \"cos 3\",\r\n"
                + "        \"name\": \"cos 3\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240741,\r\n" + "        \"attributeMasterId\": 50,\r\n"
                + "        \"attributeValues\": \"0%\",\r\n" + "        \"description\": \"cos 4\",\r\n"
                + "        \"name\": \"cos 4\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240742,\r\n" + "        \"attributeMasterId\": 51,\r\n"
                + "        \"attributeValues\": \"0%\",\r\n" + "        \"description\": \"cos 5\",\r\n"
                + "        \"name\": \"cos 5\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240743,\r\n" + "        \"attributeMasterId\": 52,\r\n"
                + "        \"attributeValues\": \"0%\",\r\n" + "        \"description\": \"cos 6\",\r\n"
                + "        \"name\": \"cos 6\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240744,\r\n" + "        \"attributeMasterId\": 19,\r\n"
                + "        \"attributeValues\": \"No\",\r\n" + "        \"description\": \"Resiliency\",\r\n"
                + "        \"name\": \"Resiliency\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240745,\r\n" + "        \"attributeMasterId\": 20,\r\n"
                + "        \"attributeValues\": \"LC\",\r\n" + "        \"description\": \"Connector Type\",\r\n"
                + "        \"name\": \"Connector Type\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240746,\r\n" + "        \"attributeMasterId\": 21,\r\n"
                + "        \"attributeValues\": \"Yes\",\r\n" + "        \"description\": \"Access Required\",\r\n"
                + "        \"name\": \"Access Required\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240747,\r\n" + "        \"attributeMasterId\": 22,\r\n"
                + "        \"attributeValues\": \"Customer provided\",\r\n" + "        \"description\": \"CPE\",\r\n"
                + "        \"name\": \"CPE\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240748,\r\n" + "        \"attributeMasterId\": 61,\r\n"
                + "        \"attributeValues\": \"Full Mesh\",\r\n" + "        \"description\": \"VPN Topology\",\r\n"
                + "        \"name\": \"VPN Topology\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240749,\r\n" + "        \"attributeMasterId\": 62,\r\n"
                + "        \"attributeValues\": \"Mesh\",\r\n" + "        \"description\": \"Site Type\",\r\n"
                + "        \"name\": \"Site Type\"\r\n" + "      },\r\n" + "      {\r\n"
                + "        \"attributeId\": 240750,\r\n" + "        \"attributeMasterId\": 64,\r\n"
                + "        \"attributeValues\": \"Unmanaged\",\r\n"
                + "        \"description\": \"Access Topology\",\r\n" + "        \"name\": \"Access Topology\"\r\n"
                + "      }\r\n" + "    ]\r\n" + "  }\r\n" + "]";
        QuoteProductComponentBean[] comp = (QuoteProductComponentBean[]) Utils.convertJsonToObject(json,
                QuoteProductComponentBean[].class);
        for (int i = 0; i <= comp.length - 1; i++) {
            list.add(comp[i]);

        }

        return list;
    }

    /**
     * getQuotePriceDetails
     *
     * @return
     */
    private QuotePriceBean getQuotePriceDetails() {
        QuotePriceBean price = new QuotePriceBean();
        return price;
    }

    /**
     * getAttributesList
     *
     * @return
     */
    private List<QuoteProductComponentsAttributeValueBean> getAttributesList() {
        List<QuoteProductComponentsAttributeValueBean> list = new ArrayList<>();
        QuoteProductComponentsAttributeValueBean bean = new QuoteProductComponentsAttributeValueBean();
        bean.setPrice(getQuotePriceDetails());
        list.add(bean);
        return list;
    }

    public CofDetails getCofDetails() {
        CofDetails cofDetails = new CofDetails();
        cofDetails.setOrderUuid("VBHF65B");
        cofDetails.setUriPath("/testpath");
        cofDetails.setSource("automated_cof");
        cofDetails.setCreatedBy("optimus");
        return cofDetails;
    }

    public String getAddressDetailJSON() {
        return "{\"addressLineOne\":\"omkar 1988\",\"city\":\"Mumbai\",\"country\":\"INDIA\",\"locality\":\"Mumbai\",\"pincode\":\"400045\",\"source\":\"manual\",\"state\":\"MAHARASHTRA\",\"latLong\":\"19.0759837,72.8776559\"}";
    }

    public String getSpJSON() throws TclCommonException {
        SPDetails spDetails = new SPDetails();
        spDetails.setAddress("dafvda");
        spDetails.setEntityName("safdafadfda");
        spDetails.setGstnDetails("adffad");

        return Utils.convertObjectToJson(spDetails);
    }

    public QuoteToLeBean getQuoteToLeBean() throws TclCommonException {
        QuoteToLeBean quoteLeBean = new QuoteToLeBean(getQuoteToLe());
        quoteLeBean.setSupplierLegalEntityId(1);
        quoteLeBean.setLegalAttributes(getLegalAttributeBeanList());
        quoteLeBean.setProductFamilies(getQuoteToLeProductFamilyBean());
        quoteLeBean.setFinalArc(Double.parseDouble("450356"));
        quoteLeBean.setFinalMrc(Double.parseDouble("450356"));
        quoteLeBean.setFinalNrc(Double.parseDouble("450356"));
        quoteLeBean.setTotalTcv(Double.parseDouble("450356"));
        return quoteLeBean;
    }

    public String getConstructSupplierInformationsJSON() {
        return "{\"entityName\":\"1456\",\"gstnDetails\":\"27AAACV2808C1ZP\",\"address\":\"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai - 400001\"}";
    }

    /**
     * getOrder with object
     *
     * @return orders
     */
    public Order getOrderGvpn() {
        Order orders = new Order();

        Timestamp timeStamp = new Timestamp(0);

        orders.setCreatedBy(1);
        orders.setCreatedTime(timeStamp);
        orders.setCustomer(getCustomer());
        orders.setEffectiveDate(timeStamp);
        orders.setEndDate(timeStamp);
        orders.setId(1);
        Set<OrderToLe> orderToLes = new HashSet<>();
        orderToLes.add(getOrderToLesAccessTopology());
        orders.setOrderToLes(orderToLes);

        orders.setQuote(getQuote());

        orders.setStage("aa");
        orders.setStartDate(timeStamp);
        orders.setStatus((byte) 1);

        return orders;
    }

    public MstProductFamily getMstProductFamilyGVPN() {
        MstProductFamily mstProductFamily = new MstProductFamily();
        mstProductFamily.setId(1);
        mstProductFamily.setName("GVPN");
        return mstProductFamily;
    }

    public OrderToLe getOrderToLesAccessTopology() {
        OrderToLe orderToLe = new OrderToLe();
        Set set = new HashSet<>();
        set.add(getorderToLeProductFamiliesTopology());

        orderToLe.setId(1);
        orderToLe.setCurrencyId(1);
        orderToLe.setErfCusCustomerLegalEntityId(1);
        orderToLe.setErfCusSpLegalEntityId(1);
        orderToLe.setOrderToLeProductFamilies(set);
        orderToLe.setStage("stage");
        orderToLe.setFinalMrc(1.0);
        orderToLe.setFinalNrc(2.0);
        return orderToLe;
    }

    public OrderToLeProductFamily getorderToLeProductFamiliesTopology() {
        OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
        orderToLeProductFamily.setId(1);
        orderToLeProductFamily.setMstProductFamily(getMstProductFamilyGVPN());
        Set<OrderProductSolution> opsSolution = new HashSet<>();
        opsSolution.add(getOrderProductSolutionGvpn());
        orderToLeProductFamily.setOrderProductSolutions(opsSolution);
        return orderToLeProductFamily;
    }

    public OrderIllSite getOrderGvpnSite() {
        OrderIllSite orderIllSite = new OrderIllSite();
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setRequestorDate(new Date());

        orderIllSite.setId(2);
        orderIllSite.setCreatedBy(1);
        orderIllSite.setCreatedTime(new Date());
        orderIllSite.setEffectiveDate(new Date());
        orderIllSite.setErfLocSiteaLocationId(1);
        orderIllSite.setErfLocSiteaSiteCode("code");
        orderIllSite.setFeasibility((byte) 1);
        orderIllSite.setStatus((byte) 1);
        orderIllSite.setStage("PROVISION_SITES");
        orderIllSite.setSiteCode("TestCode");
        orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
        orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
        orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());

        return orderIllSite;
    }

    public OrderProductSolution getOrderProductSolutionGvpn() {

        OrderProductSolution orderProductSolution = new OrderProductSolution();
        orderProductSolution.setId(1);

        orderProductSolution.setMstProductOffering(getMstOffering());
        Set set = new HashSet<>();
        set.add(getOrderGvpnSite());
        orderProductSolution.setOrderIllSites(set);
        return orderProductSolution;
    }

    public List<QuoteDelegation> getQuoteDelegationList() {
        List<QuoteDelegation> quoteDelegationList = new ArrayList<>();
        quoteDelegationList.add(getQuoteDelegation());
        return quoteDelegationList;
    }

    public List<QuoteIllSite> getIllsitesLists() {
        List<QuoteIllSite> illSites = new ArrayList<>();
        illSites.add(getIllsites1());
        illSites.add(getIllsites2());
        return illSites;
    }

    /**
     * getPageQuote
     *
     * @return
     */
    public Page<Quote> getPageQuote() {
        List<Quote> quotes = getQuoteList();

        Page<Quote> pagedResponse = new PageImpl(quotes);

        return pagedResponse;

    }

    public List<Map<String, Object>> getOrderMapList() {
        List<Map<String, Object>> quoteMapList = new ArrayList<>();
        Map<String, Object> quoteMap = new HashMap<>();
//        quoteMap.put(DashboardConstant.PRODUCT_NAME, "product name");
//        quoteMap.put(DashboardConstant.ORDER_CODE, "order code");
//        quoteMap.put(DashboardConstant.CREATED_TIME, new Date());
//        quoteMap.put(DashboardConstant.ORDER_ID, 1);
//        quoteMap.put(DashboardConstant.QUOTE_STAGE, "string");
//        quoteMap.put(DashboardConstant.SITE_COUNT, BigInteger.valueOf(1L));
//        quoteMap.put(DashboardConstant.CREATED_TIME, new Date());
        quoteMapList.add(quoteMap);
        return quoteMapList;
    }

    public List<Map<String, Object>> getQuoteMapList() {
        List<Map<String, Object>> quoteMapList = new ArrayList<>();
        Map<String, Object> quoteMap = new HashMap<>();
//        quoteMap.put(DashboardConstant.PRODUCT_NAME, "product name");
//        quoteMap.put(DashboardConstant.QUOTE_CODE, "quote code");
//        quoteMap.put(DashboardConstant.CREATED_TIME, new Date());
//        quoteMap.put(DashboardConstant.QUOTE_ID, 1);
//        quoteMap.put(DashboardConstant.ORDER_STAGE, "string");
//        quoteMap.put(DashboardConstant.SITE_COUNT, BigInteger.valueOf(1L));
//        quoteMap.put(DashboardConstant.CREATED_TIME, new Date());
        quoteMapList.add(quoteMap);
        return quoteMapList;
    }

    public List<QuoteIllSite> getListOfQouteIllSitesNotFeasible() {

        List<QuoteIllSite> list = new ArrayList<>();
        list.add(getQuoteIllSiteNotFeasible());
        list.add(getQuoteIllSiteNotFeasible());
        return list;

    }

    public QuoteIllSite getQuoteIllSiteNotFeasible() {
        QuoteIllSite qIs = new QuoteIllSite();
        qIs.setId(1);
        qIs.setStatus((byte) 1);

        qIs.setErfLocSitebLocationId(2);
        qIs.setProductSolution(getSolution());
        qIs.setQuoteIllSiteSlas(getQuoteIllSiteSlaSet());
        qIs.setFeasibility((byte) 0);
        qIs.setSiteCode("code");
        return qIs;

    }

    public List<OrderPrice> getOrderPriceList() {
        List<OrderPrice> set = new ArrayList<>();
        set.add(getOrderPrice());
        return set;
    }

    public GscQuoteDataBean createGscQuoteDataBean() {
        GscQuoteDataBean gscQuoteDataBean = new GscQuoteDataBean();
        Quote quote = getQuote();
        gscQuoteDataBean.setQuoteId(quote.getId());
        gscQuoteDataBean.setCustomerId(quote.getCustomer().getId());
        gscQuoteDataBean.setQuoteLeId(1);
        gscQuoteDataBean.setAccessType("Dummy AccessType");
        gscQuoteDataBean.setProfileName("Dummy Profile Name");
        gscQuoteDataBean.setSolutions(new ArrayList<>());
        gscQuoteDataBean.setLegalEntities(new ArrayList<>());
        gscQuoteDataBean.setProductFamilyName("ITFS");
        return gscQuoteDataBean;
    }

    public QuoteResponse getQuoteResponse() {
        QuoteResponse response = new QuoteResponse();
        response.setQuoteId(1);
        response.setQuoteleId(1);
        return response;
    }

    public SIOrderDataBean getSiOrderDataBean() {
        SIOrderDataBean siOrderDataBean = new SIOrderDataBean();
        siOrderDataBean.setId(11);
        siOrderDataBean.setServiceDetails(siServiceDetailBean());

        return siOrderDataBean;
    }

    public QuoteToLe getQuoteToLeArc() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setFinalArc(222d);
        return quoteToLe;
    }

    public SIServiceDetailDataBean createSiServiceDetailBean() {
        SIServiceDetailDataBean siServiceDetailDataBean = new SIServiceDetailDataBean();
        siServiceDetailDataBean.setArc(200d);
        siServiceDetailDataBean.setTpsServiceId("091GADC623029807467");
        return siServiceDetailDataBean;
    }

    public List<SIServiceDetailDataBean> siServiceDetailBean() {
        List<SIServiceDetailDataBean> siServiceDetailDataBeanList = new ArrayList<>();
        siServiceDetailDataBeanList.add(createSiServiceDetailBean());
        return siServiceDetailDataBeanList;
    }

    public QuoteDetail getQuoteDetailMacd() {
        QuoteDetail quoteDetail = new QuoteDetail();
        /* quoteDetail.toString(); */
        quoteDetail.setCustomerId(1);
        quoteDetail.setProductName("IAS");
        quoteDetail.setSite(getSite2());
        quoteDetail.setQuoteleId(1);
        quoteDetail.setQuoteId(null);
        quoteDetail.setQuoteId(2);
        List<SolutionDetail> solutionDetails = new ArrayList<>();
        SolutionDetail solution = new SolutionDetail();
        solution.setBandwidth("20");
        solution.setBandwidthUnit("mbps");
        /* solution.toString(); */
        List<ComponentDetail> components = new ArrayList<>();
        ComponentDetail component = new ComponentDetail();
        /*
         * component.toString();
         */
        components.add(component);
        List<AttributeDetail> attributeDetails = new ArrayList<>();
        AttributeDetail attributeDetail = new AttributeDetail();
        attributeDetail.setName("Model No");
        attributeDetail.setValue("CISCO");
        /*
         * attributeDetail.toString();
         */
        attributeDetails.add(attributeDetail);
        component.setAttributes(attributeDetails);
        component.setIsActive("Y");
        component.setName("CPE");
        solution.setComponents(components);
        solution.setImage("");
        /* solution.toString(); */
        solution.setOfferingName("Vive");
        solutionDetails.add(solution);
        quoteDetail.setSolutions(solutionDetails);
        return quoteDetail;
    }

    /**
     * getQuoteToLe-mock values
     *
     * @return {@link QuoteToLe}
     */
    public QuoteToLe getQuoteToLe2() {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setId(1);
        quoteToLe.setCurrencyId(1);
        quoteToLe.setFinalMrc(45D);
        quoteToLe.setFinalNrc(67D);
        quoteToLe.setProposedMrc(78D);
        /* quoteToLe.toString(); */
        quoteToLe.setProposedNrc(98D);
        quoteToLe.setQuote(getQuote());
        quoteToLe.setErfCusCustomerLegalEntityId(1);
        quoteToLe.setErfCusSpLegalEntityId(1);
        quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
        quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
        // quoteToLe.setStage("Get Quote");
        quoteToLe.setTpsSfdcOptyId("1927345");
        quoteToLe.setStage("Add Locations");
        quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
        return quoteToLe;
    }

    public CustomerLeVO getCustomerDetails() {
        CustomerLeVO customerLeVO = new CustomerLeVO();
        customerLeVO.setBlacklistStatus("N");
        customerLeVO.setCreditCheckAccountType("1A");
        customerLeVO.setCreditPreapprovedFlag("Y");
        customerLeVO.setPreapprovedBillingMethod("Advance");
        customerLeVO.setPreapprovedMrc(1000.00);
        customerLeVO.setPreapprovedNrc(1000.00);
        customerLeVO.setPreapprovedPaymentTerm("30 days");
        customerLeVO.setTpsSfdcStatusCreditControl("Positive");
        return customerLeVO;

    }

}

