package com.tcl.dias.oms.gsc.util;

import static com.tcl.dias.common.constants.CommonConstants.COLON;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceClientConfiguration.TIGER_SERVICE_DATE_FORMAT;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_ACCESS_TYPE_DIRECT_CONNXN;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_ACCESS_TYPE_NEW_SERVICE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_ADD_COUNTRIES;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_CANCEL_NUMBER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_CHANGE_OUTPULSE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_CLID_REALTIME;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_CONNXN_DTG_HEADER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_FIXED;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_OUTPULSE_TYPE_CUSTOM;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.INTERCONNECT_REQUEST_TYPE_NEW;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_EMERGENCY_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_MACD_PRODUCT_REFERENCE_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANGING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.COUNT_CONCURRENT_CHANNELS;
import static com.tcl.dias.oms.gsc.util.GscConstants.COUNT_REQUESTED_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DELETE_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscConstants.DID;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.PSTN;
import static com.tcl.dias.oms.gsc.util.GscConstants.PUBLIC_IP;
import static com.tcl.dias.oms.gsc.util.GscConstants.REQUIRED_PORTING_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE_BYTE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_INACTIVE_BYTE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERMINATION_NUMBER_ISD_CODE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.macd.MACDOrderRequest;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.tiger.TigerServiceConstants;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.AccessNumberItem;
import com.tcl.dias.oms.gsc.tiger.beans.AccessServiceOrderItem;
import com.tcl.dias.oms.gsc.tiger.beans.Address;
import com.tcl.dias.oms.gsc.tiger.beans.BaseOrderManagementBean;
import com.tcl.dias.oms.gsc.tiger.beans.BillingEntity;
import com.tcl.dias.oms.gsc.tiger.beans.BillingProfile;
import com.tcl.dias.oms.gsc.tiger.beans.BusinessEntity;
import com.tcl.dias.oms.gsc.tiger.beans.CallType;
import com.tcl.dias.oms.gsc.tiger.beans.Contact;
import com.tcl.dias.oms.gsc.tiger.beans.ContractingEntity;
import com.tcl.dias.oms.gsc.tiger.beans.DidDetail;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticCallingServiceDetail;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticCallingServiceOrderItem;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectDetail;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderItem;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectTypeDetail;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.Number;
import com.tcl.dias.oms.gsc.tiger.beans.Organisation;
import com.tcl.dias.oms.gsc.tiger.beans.Party;
import com.tcl.dias.oms.gsc.tiger.beans.TerminationDetail;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

@Component
public class GscOrderManagementRequestBuilder {

    public static final Logger LOGGER = LoggerFactory.getLogger(GscAttachmentHelper.class);

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.customer.billing.contact.queue}")
    String billingContactQueueName;

    @Value("${rabbitmq.customer.contact.details.queue}")
    String customerLeContactQueueName;

    @Value("${rabbitmq.customerleattr.product.queue}")
    String customerLeAttrQueueProduct;

    @Value("${rabbitmq.supplier.le.attr.product.queue}")
    String supplierLeAttrQueueProduct;

    @Autowired
    TigerServiceHelper tigerServiceHelper;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Autowired
    GscRepcCountries gscRepcCountries;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    GscOrderDetailService gscOrderDetailService;

    private static final Map<String, String> CODEC_TRANSLATION_TABLE = ImmutableMap.<String, String>builder()
            .put("AMR-NB", "AMR-NB")
            .put("G711u-law", "G.711")
            .put("G.722", "G.722")
            .put("G.722.2", "G.722.2")
            .put("G.729", "G.729")
            .put("G11a-law", "G11a-law")
            .build();

    private Optional<BillingContact> getBillingContact(OrderToLe orderToLe)
            throws TclCommonException, IllegalArgumentException {
        LOGGER.info("MDC Filter token value in before Queue call getBillingContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String response = (String) mqUtils.sendAndReceive(billingContactQueueName,
                String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
        List<BillingContact> billingContacts = GscUtils.fromJson(response, new TypeReference<List<BillingContact>>() {
        });
        return CollectionUtils.isEmpty(billingContacts) ? Optional.empty()
                : Optional.ofNullable(billingContacts.get(0));
    }

    private Optional<CustomerLeContactDetailBean> getCustomerLeContact(OrderToLe orderToLe)
            throws TclCommonException, IllegalArgumentException {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
                String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
        List<CustomerLeContactDetailBean> customerLeContacts = GscUtils.fromJson(response,
                new TypeReference<List<CustomerLeContactDetailBean>>() {
                });
        return CollectionUtils.isEmpty(customerLeContacts) ? Optional.empty()
                : Optional.ofNullable(customerLeContacts.get(0));
    }

    private BillingEntity getBillingEntity(Optional<BillingContact> billingContactOpt,
                                           CustomerLeDetailsBean customerLeDetailsBean, String orgId, Integer orderId) {
        BillingEntity billingEntity = new BillingEntity();
        billingContactOpt.ifPresent(billingContact -> {
            Address billingAddress = new Address();
            billingAddress.setAddressLine1(billingContact.getBillAddr());
            Contact contact = new Contact();
            if (Objects.nonNull(billingContact.getEmailId())) {
                contact.setEmail(billingContact.getEmailId());
            } else {
                customerLeDetailsBean.getAttributes().stream().filter(
                        attributes -> LeAttributesConstants.LE_EMAIL.equalsIgnoreCase(attributes.getAttributeName()))
                        .findFirst().ifPresent(attributes -> contact.setEmail(attributes.getAttributeValue()));
            }
            contact.setMobile(Optional.ofNullable(billingContact.getMobileNumber())
                    .orElse(Optional.ofNullable(billingContact.getPhoneNumber()).orElse("")));
            contact.setName(billingContact.getFname() + " " + billingContact.getLname());
            billingEntity.setAddress(billingAddress);
            billingEntity.setContact(contact);
            BillingProfile billingProfile = tigerServiceHelper.getBillingProfileForOrgId(orderId, orgId);
            billingEntity.setBillingProfileId(billingProfile.getProfileRelNo());
            billingEntity.setBillingEntityName(customerLeDetailsBean.getLegalEntityName());
        });
        return billingEntity;
    }

    private BusinessEntity getTechnicalEntity(Optional<CustomerLeContactDetailBean> customerLeContactOpt) {
        BusinessEntity businessEntity = new BusinessEntity();
        customerLeContactOpt.ifPresent(customerLeContact -> {
            Address address = new Address();
            address.setAddressLine1(customerLeContact.getAddress());
            Contact contact = new Contact();
            contact.setName(customerLeContact.getName());
            contact.setMobile(Optional.ofNullable(customerLeContact.getMobilePhone())
                    .orElse(Optional.ofNullable(customerLeContact.getHomePhone())
                            .orElse(Optional.ofNullable(customerLeContact.getOtherPhone()).orElse(""))));
            contact.setEmail(customerLeContact.getEmailId());
            contact.setFax(customerLeContact.getFax());
            businessEntity.setAddress(address);
            businessEntity.setContact(contact);
        });
        return businessEntity;
    }

    private ContractingEntity getContractingEntity(BusinessEntity technicalEntity, String customerName) {
        ContractingEntity contractingEntity = new ContractingEntity();
        contractingEntity.setAddress(technicalEntity.getAddress());
        contractingEntity.setContact(technicalEntity.getContact());
        contractingEntity.setEntityName(customerName);
        return contractingEntity;
    }

    private Party getTataParty(String orgId) {
        Party party = new Party();
        party.setRequestor(Boolean.FALSE);
        party.setRole("tataEntity");
        party.setOrgId(orgId);
        return party;
    }

    private Party getCustomerParty(String orgNo, Optional<CustomerLeContactDetailBean> customerLeContactOpt) {
        Party party = new Party();
        try {
            party.setOrgId(orgNo);
            party.setRequestor(Boolean.TRUE);
            party.setRole("customerEntity");
            party.setContact(getTechnicalEntity(customerLeContactOpt).getContact());
        } catch (Exception ex) {
            throw new TclCommonRuntimeException("Customer orgId not found for order"+ex.getMessage(),ex);
        }
        return party;
    }

    /**
     * Return max(requestor service date) from all configurations
     *
     * @param orderGscDetails
     * @return
     */
    private String getRequestedCompletionDate(Collection<OrderGscDetail> orderGscDetails) {
        LocalDateTime dateTime = orderGscDetails.stream()
                .map(orderGscDetail -> getProductComponentAttributes(GSC_CONFIG_PRODUCT_COMPONENT_TYPE.toUpperCase(),
                        GSC_CONFIG_PRODUCT_COMPONENT_TYPE, orderGscDetail, ATTR_REQUESTOR_DATE_FOR_SERVICE))
                .map(map -> map.get(ATTR_REQUESTOR_DATE_FOR_SERVICE))
                .map(dateStr -> Objects.nonNull(dateStr)
                        ? LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        return new SimpleDateFormat(TIGER_SERVICE_DATE_FORMAT)
                .format(Date.from(dateTime.toInstant(ZoneOffset.UTC)));
    }

    public Integer getOrderId(BaseOrderManagementBean baseOrderManagementBean) {
        return Ints.tryParse(baseOrderManagementBean.getOrderName().split("-")[1]);
    }

    private <T extends BaseOrderManagementBean> T buildBaseOrderValues(T orderManagementRequest, OrderToLe orderToLe)
            throws TclCommonException {
        Objects.requireNonNull(orderToLe.getErfCusCustomerLegalEntityId(), "Customer LE Id cannot be null");
        Objects.requireNonNull(orderToLe.getErfCusSpLegalEntityId(), "Supplier LE Id cannot be null");
        orderManagementRequest.setCorrID(UUID.randomUUID().toString());
        orderManagementRequest.setDescription(
                String.format("%s - %s", GSC_ORDER_PRODUCT_COMPONENT_TYPE, orderToLe.getOrder().getOrderCode()));
        orderManagementRequest.setState(GscConstants.ORDER_STATE_SUBMIT);
        orderManagementRequest.setRequestedCompletionDate(null);
        orderManagementRequest.setExpectedCompletionDate(null);
        orderManagementRequest.setOrderName(String.format("%s-%s-%s", GSC_ORDER_PRODUCT_COMPONENT_TYPE,
                orderToLe.getOrder().getId(), orderToLe.getOrder().getOrderCode()));

        orderManagementRequest.setOptimusId(orderToLe.getOrder().getOrderCode());
        orderManagementRequest.setSolutionId(orderToLe.getTpsSfdcCopfId());
        orderManagementRequest.setSolutionName(GscConstants.GSC_PRODUCT_NAME.toUpperCase());
        orderManagementRequest.setProductIdentifier(GscConstants.TIGER_OPTIMUS_PRODUCTIDENTIFIER);

        // Get customer legal entity's SFDC - CUID
        CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
        customerLeAttributeRequestBean.setCustomerLeId(orderToLe.getErfCusCustomerLegalEntityId());
        customerLeAttributeRequestBean.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        LOGGER.info("MDC Filter token value in before Queue call buildBaseOrderValues {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String customerLeAttributesResponse = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
                GscUtils.toJson(customerLeAttributeRequestBean));
        CustomerLeDetailsBean customerLeDetailsBean = GscUtils.fromJson(customerLeAttributesResponse,
                CustomerLeDetailsBean.class);
//        Optional<Organisation> customerOrganisationOpt = tigerServiceHelper
//                .getOrganisationByCuId(customerLeDetailsBean.getAccounCuId(), orderToLe.getOrder().getId(), "order");
        LeSapCodeResponse leSapCodeResponse = gscOrderDetailService.getCustomerLeSapCodeDetails(orderToLe.getErfCusCustomerLegalEntityId());
        LeSapCodeBean leSapCodeBean = leSapCodeResponse.getLeSapCodes().stream().findFirst().get();
        LOGGER.info("leSapCodeBean :: {}", leSapCodeBean.toString());
        // SECS Code and Org No both are same
        String orgNo = leSapCodeBean.getCodeValue();

        // Get supplier legal entity's SFDC - CUID
        CustomerLeAttributeRequestBean supplierAttributeRequest = new CustomerLeAttributeRequestBean();
        supplierAttributeRequest.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        supplierAttributeRequest.setCustomerLeId(orderToLe.getErfCusSpLegalEntityId());
        LOGGER.info("MDC Filter token value in before Queue call buildBaseOrderValues {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String supplierLeAttributesResponse = (String) mqUtils.sendAndReceive(supplierLeAttrQueueProduct,
                GscUtils.toJson(supplierAttributeRequest));
        CustomerLeDetailsBean supplierLeDetailsBean = GscUtils.fromJson(supplierLeAttributesResponse,
                CustomerLeDetailsBean.class);
//        Optional<Organisation> supplierOrganisationOpt = tigerServiceHelper
//                .getOrganisationByCuId(supplierLeDetailsBean.getAccounCuId(), orderToLe.getOrder().getId(), "order");
        LeSapCodeResponse leSapCodeResponseSupplier = gscOrderDetailService.getSupplierLeSapCodeDetails(orderToLe.getErfCusSpLegalEntityId());
        LeSapCodeBean leSapCodeBeanSupplier = leSapCodeResponseSupplier.getLeSapCodes().stream().findFirst().get();
        LOGGER.info("leSapCodeBean :: {}", leSapCodeBeanSupplier.toString());
        // SECS Code and Org No both are same
        String supplierSECSCode = leSapCodeBeanSupplier.getCodeValue();
        LOGGER.info("supplierSECSCode :: {}", supplierSECSCode);

        Optional<BillingContact> billingContactOpt = getBillingContact(orderToLe);
        BillingEntity billingEntity = getBillingEntity(billingContactOpt, customerLeDetailsBean, orgNo, orderToLe.getOrder().getId());
        orderManagementRequest.setBillingEntity(billingEntity);
        Optional<CustomerLeContactDetailBean> customerLeContact = getCustomerLeContact(orderToLe);
        BusinessEntity technicalEntity = getTechnicalEntity(customerLeContact);
        orderManagementRequest.setTechnicalEntity(technicalEntity);
        orderManagementRequest.setContractingEntity(
                getContractingEntity(technicalEntity, customerLeDetailsBean.getLegalEntityName()));
        Party customerParty = getCustomerParty(orgNo, customerLeContact);
        List<Party> relatedParties = new ArrayList<>();
        relatedParties.add(customerParty);

        Party supplierParty = getTataParty(supplierSECSCode);
        relatedParties.add(supplierParty);
        // set party ids in increasing sequence
        for (int i = 0; i < relatedParties.size(); i++) {
            relatedParties.get(i).setPartyId(String.valueOf(i + 1));
        }

        orderManagementRequest.setRelatedParties(relatedParties);
        return orderManagementRequest;
    }

    private Number buildNumber(OrderGsc orderGsc, OrderGscDetail orderGscDetail, OrderGscTfn orderGscTfn) {
        Number number = new Number();
        //number.setAccessNumber(orderGscTfn.getTfnNumber());
        number.setPortingRequired(STATUS_ACTIVE_BYTE.equals(orderGscTfn.getIsPorted()));
        CallType callType = new CallType();
        callType.setType(CALL_TYPE_FIXED);
        callType.setClid(CALL_TYPE_CLID_REALTIME);
        callType.setOutpulseType(CALL_TYPE_OUTPULSE_TYPE_CUSTOM);
        callType.setConnectionDtgHeader(CALL_TYPE_CONNXN_DTG_HEADER);

        String referenceOrderId = getProductMasterAttribute(orderGsc, orderGscDetail, GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
        callType.setReferenceOrderId(referenceOrderId);
        if(orderGscTfn.getTfnNumber().contains(COLON)) {
            number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
            callType.setOutpulse(orderGscTfn.getTfnNumber().split(COLON)[0]);
        } else {
            number.setAccessNumber("");
            callType.setOutpulse(orderGscTfn.getTfnNumber());
        }

//        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType()) || CHANGING_OUTPULSE.equalsIgnoreCase(orderCategory)) {
//            String outpulse = getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
//            String outpulseISDCode = getProductMasterAttribute(orderGsc, orderGscDetail, TERMINATION_NUMBER_ISD_CODE);
//            outpulseISDCode = outpulseISDCode.replaceAll("\\+", "");
//            callType.setOutpulse(outpulseISDCode.concat(outpulse));
//        }
        number.setCallType(ImmutableList.of(callType));
        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType())) {
            TerminationDetail terminationDetail = new TerminationDetail();
            String[] dest = orderGscDetail.getDest().split(":");
            String countryName = dest[0];
            GscRepcCountries.GscCountry destinationCountry = gscRepcCountries.forName(countryName);
            Objects.requireNonNull(destinationCountry, String.format("Country with name: %s not found", countryName));
            terminationDetail.setCountryAbbr(destinationCountry.getCode());
            terminationDetail.setTermDetailsId(String.valueOf(1));
            number.setTerminationDetails(ImmutableList.of(terminationDetail));
        }
        return number;
    }

    public String getProductMasterAttribute(OrderGsc orderGsc, OrderGscDetail orderGscDetail, String attributeName) {
        return mstProductComponentRepository
                .findByNameAndStatus(orderGsc.getAccessType(),
                        GscConstants.STATUS_ACTIVE)
                .stream().findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> Tuple.of(orderProductComponent,
                        productAttributeMasterRepository
                                .findByNameAndStatus(attributeName, STATUS_ACTIVE).stream()
                                .findFirst().orElse(new ProductAttributeMaster())))
                .map(componentMasterAttributePair -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(componentMasterAttributePair._1,
                                componentMasterAttributePair._2))
                .flatMap(orderProductComponentsAttributeValues -> orderProductComponentsAttributeValues.stream()
                        .findFirst().map(OrderProductComponentsAttributeValue::getAttributeValues))
                .orElse("");
    }

    private AccessNumberItem buildAccessNumberItem(Order order, OrderGsc orderGsc, OrderGscDetail orderGscDetail) {
        AccessNumberItem accessNumberItem = new AccessNumberItem();
        String[] src = orderGscDetail.getSrc().split(":");
        String countryName = src[0];
        GscRepcCountries.GscCountry originCountry = gscRepcCountries.forName(countryName);
        Objects.requireNonNull(originCountry, String.format("Country with name: %s not found", countryName));
        accessNumberItem.setOriginCountryAbbr(originCountry.getCode());

        List<Number> numbers = new ArrayList<>();
        List<OrderGscTfn> orderGscTfns = new ArrayList<>();
        List<OrderProductComponentsAttributeValue> outpulseAttribute = getAllNumbers(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
        List<OrderProductComponentsAttributeValue> portingAccessNumberAttribute = getAllNumbers(orderGsc, orderGscDetail, REQUIRED_PORTING_NUMBERS);
        // TODO: Check TFN Table and prepare number bank selection
//        List<OrderProductComponentsAttributeValue> numberBankAccessNumberAttribute = getAllNumbers(orderGsc, orderGscDetail, REQUIRED_PORTING_NUMBERS);

        //List<OrderGscTfn> outpulseNumbers = buildOrderGscTfnNumbers1(outpulseAttribute, portingAccessNumberAttribute, STATUS_INACTIVE_BYTE);
        List<OrderGscTfn> outpulseNumbers = buildOrderGscTfnNumbers(outpulseAttribute, STATUS_INACTIVE_BYTE);
        //List<OrderGscTfn> portingNumbers = buildOrderGscTfnNumbers(portingAccessNumberAttribute, STATUS_ACTIVE_BYTE);

        if (!CollectionUtils.isEmpty(portingAccessNumberAttribute)) {
            for (int i = 0; i < portingAccessNumberAttribute.size(); i++) {
                OrderGscTfn orderGscTfn = outpulseNumbers.get(i);
                orderGscTfn.setTfnNumber(orderGscTfn.getTfnNumber() +COLON+ portingAccessNumberAttribute.get(i).getAttributeValues());
                orderGscTfn.setIsPorted(STATUS_ACTIVE_BYTE);
            }
        }

        if (!CollectionUtils.isEmpty(outpulseNumbers)) {
            Optional.ofNullable(outpulseNumbers).ifPresent(orderGscTfns::addAll);
        }

//        if (!CollectionUtils.isEmpty(portingNumbers)) {
//            Optional.ofNullable(portingNumbers).ifPresent(orderGscTfns::addAll);
//        }

        String orderCategory = getOrderCategory(order);
        if(CHANGING_OUTPULSE.equalsIgnoreCase(orderCategory)) {
            orderGscTfns = getTollFreeNumberForChangeOutpulse(orderGscDetail, orderGscTfns);
        } else if(DELETE_NUMBER.equalsIgnoreCase(orderCategory)) {
            orderGscTfns = getTollFreeNumberOnlyForDeleteNumber(orderGscDetail, orderGscTfns);
        }

        if (CollectionUtils.isEmpty(orderGscTfns)) {
            orderGscTfns.add(new OrderGscTfn());
        }
//        List<OrderGscTfn> orderGscTfns = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
        numbers = orderGscTfns.stream().map(orderGscTfn -> buildNumber(orderGsc, orderGscDetail, orderGscTfn)).collect(Collectors.toList());

        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setNumberDetailsId(String.valueOf(i + 1));
        }
        accessNumberItem.setNumbers(numbers);
        return accessNumberItem;
    }

    private String getOrderCategory(Order order) {
        return order.getOrderToLes().stream().findFirst().get().getOrderCategory();
    }

    private List<OrderGscTfn> getTollFreeNumberOnlyForDeleteNumber(OrderGscDetail orderGscDetail, List<OrderGscTfn> orderGscTfns) {
        //if(DELETE_NUMBER.equalsIgnoreCase(orderCategory)) {
        List<OrderGscTfn> removeNumbers = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
        if(!CollectionUtils.isEmpty(removeNumbers)) {
            //Optional.ofNullable(removeNumbers).ifPresent(orderGscTfns::addAll);
            orderGscTfns.addAll(removeNumbers.stream().map(orderGscTfn -> {
                orderGscTfn.setTfnNumber("" + COLON + orderGscTfn.getTfnNumber());
                return orderGscTfn;
            }).collect(Collectors.toList()));
        }
//        }
        return orderGscTfns;
    }

    private List<OrderGscTfn> getTollFreeNumberForChangeOutpulse(OrderGscDetail orderGscDetail, List<OrderGscTfn> orderGscTfns) {
        List<OrderGscTfn> accessNumbers = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
        if(!CollectionUtils.isEmpty(accessNumbers)) {
            //Optional.ofNullable(accessNumbers).ifPresent(orderGscTfns::addAll);
            accessNumbers.stream().forEach(accessNumber -> {
                orderGscTfns.stream().forEach(outpulseNumber -> {
                    outpulseNumber.setTfnNumber(outpulseNumber.getTfnNumber() +COLON+ accessNumber.getTfnNumber());
                });
            });
        }
        return orderGscTfns;
    }

    private AccessNumberItem buildAccessNumberItemForCity(OrderGsc orderGsc, OrderGscDetail orderGscDetail, OrderGscTfn orderGscTfn) {
        AccessNumberItem accessNumberItem = new AccessNumberItem();
        String[] srcCity = orderGscTfn.getCountryCode().split(":");
        if (1 == srcCity.length) {
            return accessNumberItem;
        }
        String[] src = orderGscDetail.getSrc().split(":");
        String countryName = src[0];
        GscRepcCountries.GscCountry originCountry = gscRepcCountries.forName(countryName);
        Objects.requireNonNull(originCountry, String.format("Country with name: %s not found", countryName));
        accessNumberItem.setOriginCountryAbbr(originCountry.getCode());
        accessNumberItem.setOriginCityAbbr(orderGscTfn.getCountryCode().split(":")[1]);
        accessNumberItem.setSupplierID("");
        List<Number> numbers = Arrays.asList(buildNumber(orderGsc, orderGscDetail, orderGscTfn));
        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setNumberDetailsId(String.valueOf(i + 1));
        }
        accessNumberItem.setNumbers(numbers);
        return accessNumberItem;
    }


    private List<OrderGscTfn> buildOrderGscTfnNumbers(List<OrderProductComponentsAttributeValue> attributeValues, Byte isPorting) {
        return attributeValues.stream().map(attributeValue -> {
            if (!StringUtils.isEmpty(attributeValue.getAttributeValues())) {
                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setTfnNumber(attributeValue.getAttributeValues());
                orderGscTfn.setIsPorted(isPorting);
                return orderGscTfn;
            }
            return null;
        }).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<OrderProductComponentsAttributeValue> getAllNumbers(OrderGsc orderGsc, OrderGscDetail orderGscDetail, String attribute) {
        return mstProductComponentRepository.findByNameAndStatus(orderGsc.getAccessType()
                , GscConstants.STATUS_ACTIVE).stream().findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
                                productAttributeMasterRepository
                                        .findByNameAndStatus(attribute, STATUS_ACTIVE).stream().findFirst().get())).get();
    }


    private Map<String, String> getProductComponentAttributes(String componentName, String referenceType,
                                                              OrderGscDetail orderGscDetail, String... attributeNames) {
        List<ProductAttributeMaster> attributeMasters = productAttributeMasterRepository
                .findByNameIn(Arrays.asList(attributeNames));
        return mstProductComponentRepository.findByNameAndStatus(componentName, GscConstants.STATUS_ACTIVE).stream()
                .findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                referenceType)
                        .stream().findFirst())
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findAllByOrderProductComponentAndProductAttributeMasterIn(orderProductComponent,
                                attributeMasters))
                .map(orderProductComponentsAttributeValues -> orderProductComponentsAttributeValues.stream()
                        .collect(Collectors.toMap(attribute -> attribute.getProductAttributeMaster().getName(),
                                OrderProductComponentsAttributeValue::getAttributeValues)))
                .orElse(ImmutableMap.of());

    }

    private String getRequestType(OrderToLe orderToLe) {
        if (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.getOrderType())) {
            switch (orderToLe.getOrderCategory()) {
                case MACDOrderRequest.REQUEST_TYPE_NUMBER_ADD:
                    return ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
                case MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE:
                    return ACCESS_SERVICE_REQUEST_TYPE_CANCEL_NUMBER;
                case MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY:
                    return ACCESS_SERVICE_REQUEST_TYPE_ADD_COUNTRIES;
                case MACDOrderRequest.REQUEST_TYPE_CHANGE_OUTPULSE:
                    return ACCESS_SERVICE_REQUEST_TYPE_CHANGE_OUTPULSE;
                case MACDOrderRequest.REQUEST_TYPE_INTERCONNET_ATTRIBUTE:
                case MACDOrderRequest.REQUEST_TYPE_AGGREGATE_SIP_TRUNK:
                    return ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
                default:
                    throw new RuntimeException(
                            String.format("Unsupported MACD request type: %s", orderToLe.getOrderCategory()));
            }
        } else {
            return ACCESS_SERVICE_ACCESS_TYPE_NEW_SERVICE;
        }
    }

    private AccessServiceOrderItem buildAccessServiceOrderItem(Order order, OrderToLe orderToLe, OrderGsc orderGsc) {
        AccessServiceOrderItem orderItem = new AccessServiceOrderItem();
        orderItem = setServiceName(orderItem, orderGsc);
        orderItem.setRequestType(getRequestType(orderToLe));
        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType())) {
            orderItem.setAccessType(PSTN);
        } else {
            orderItem.setAccessType(ACCESS_SERVICE_ACCESS_TYPE_DIRECT_CONNXN);
            orderItem.setDirectConnectionType(orderGsc.getAccessType());
        }
        orderItem.setWithICR("false");
        orderItem.setWithIVR("false");
        orderItem.setPreferredCurrency(orderToLe.getCurrencyCode());
        List<AccessNumberItem> accessNumberItems = orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                .map(orderGscDetail -> buildAccessNumberItem(order, orderGsc, orderGscDetail))
                .collect(Collectors.toList());

        if (LNS.equalsIgnoreCase(orderGsc.getProductName()) || ACANS.equalsIgnoreCase(orderGsc.getProductName())) {
            List<AccessNumberItem> accessNumberItemsForCity = orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                    .map(orderGscDetail -> orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
                            .map(orderGscTfn -> buildAccessNumberItemForCity(orderGsc, orderGscDetail, orderGscTfn)))
                    .flatMap(accessNumberItem -> accessNumberItem)
                    .filter(accessNumberItem -> Objects.nonNull(accessNumberItem.getNumbers()))
                    .collect(Collectors.toList());
            accessNumberItemsForCity.stream().forEach(accessNumberItems::add);

        }
        for (int i = 0; i < accessNumberItems.size(); i++) {
            accessNumberItems.get(i).setNumberItemId(String.valueOf(i + 1));
        }
        orderItem.setAccessNumberItems(accessNumberItems);
        return orderItem;
    }

    private AccessServiceOrderItem setServiceName(AccessServiceOrderItem orderItem, OrderGsc orderGsc) {
        if (ACDTFS.equalsIgnoreCase(orderGsc.getProductName())) {
            orderItem.setServiceName("Audio Conferencing DTF Service");
        } else if (ACANS.equalsIgnoreCase(orderGsc.getProductName())) {
            orderItem.setServiceName("Audio Conferencing Access Number Service");
        } else {
            orderItem.setServiceName(orderGsc.getProductName());
        }
        return orderItem;
    }

    private <T extends InternationalOrderManagementRequest> T buildAccessServiceOrderItems(T orderManagementRequest,
                                                                                           Order order, OrderToLe orderToLe, List<OrderGsc> orderGscs) {
        List<AccessServiceOrderItem> accessServiceOrderItems = orderGscs.stream()
                .map(orderGsc -> buildAccessServiceOrderItem(order, orderToLe, orderGsc)).collect(Collectors.toList());
        orderManagementRequest.setAccessServiceOrderItems(accessServiceOrderItems);
        for (int i = 0; i < accessServiceOrderItems.size(); i++) {
            accessServiceOrderItems.get(i).setOrderItemId(String.valueOf(i + 1));
        }
        List<OrderGscDetail> orderGscDetails = orderGscs.stream().map(OrderGsc::getOrderGscDetails).flatMap(Set::stream)
                .collect(Collectors.toList());
        orderManagementRequest.setRequestedCompletionDate(getRequestedCompletionDate(orderGscDetails));
        orderManagementRequest.setSFDCOPTYID(orderToLe.getTpsSfdcCopfId());
        return orderManagementRequest;
    }

    private DomesticCallingServiceDetail buildDomesticCallingServiceDetail(OrderGscDetail orderGscDetail) {
        DomesticCallingServiceDetail domesticCallingServiceDetail = new DomesticCallingServiceDetail();
        Map<String, String> configurationAttributes = getProductComponentAttributes(DOMESTIC_VOICE,
                GSC_ORDER_PRODUCT_COMPONENT_TYPE, orderGscDetail, COUNT_CONCURRENT_CHANNELS,
                ATTR_PORTING_SERVICE_NEEDED, ATTR_EMERGENCY_ADDRESS,
                COUNT_REQUESTED_NUMBERS);
        domesticCallingServiceDetail.setNoOfConcurrentChannels(
                configurationAttributes.getOrDefault(GscConstants.COUNT_CONCURRENT_CHANNELS, "0"));
        String serviceAddress = configurationAttributes.get(GscAttributeConstants.ATTR_EMERGENCY_ADDRESS);
        if (Objects.nonNull(serviceAddress)) {
            Address serviceAddressObj = new Address();
            serviceAddressObj.setAddressLine1(serviceAddress);
            serviceAddressObj.setCountry(orderGscDetail.getSrc().toUpperCase());
            domesticCallingServiceDetail.setServiceAddress(serviceAddressObj);
        }
        domesticCallingServiceDetail
                .setNoOfEmeregencyNos(configurationAttributes.getOrDefault(COUNT_REQUESTED_NUMBERS, "0"));

        List<DidDetail> didDetails = new ArrayList<>();
        didDetails.addAll(buildDidDetails(orderGscDetail, getTerminationAndPortingNumbers(REQUIRED_PORTING_NUMBERS), true));
        didDetails.addAll(buildDidDetails(orderGscDetail, getTerminationAndPortingNumbers(TERMINATION_NUMBER_WORKING_OUTPULSE), false));

        for (int i = 0; i < didDetails.size(); i++) {
            didDetails.get(i).setDidDetailsId(1);
        }
        domesticCallingServiceDetail.setDidDetails(didDetails);
        return domesticCallingServiceDetail;
    }

    private ProductAttributeMaster getTerminationAndPortingNumbers(String attributeName) {
        return productAttributeMasterRepository.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream().findFirst().get();
    }

    private List<DidDetail> buildDidDetails(OrderGscDetail orderGscDetail, ProductAttributeMaster attributeMaster, Boolean isPorting) {
        String componentName = orderGscDetailRepository.findById(orderGscDetail.getId()).get().getOrderGsc().getAccessType();

        List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = mstProductComponentRepository.findByNameAndStatus(componentName, GscConstants.STATUS_ACTIVE).stream()
                .findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
                                attributeMaster)).get();
        return orderProductComponentsAttributeValues.stream().map(orderProductComponentsAttributeValue -> {
            DidDetail didDetail = new DidDetail();
            didDetail.setDidNumber(orderProductComponentsAttributeValue.getAttributeValues());
            didDetail.setPortingRequired(isPorting);
            didDetail.setConnectionDtgHeader(TigerServiceConstants.DID_CONNECTION_DTG_HEADER);
            return didDetail;

        }).collect(Collectors.toList());
    }

    private DomesticCallingServiceOrderItem buildDomesticServiceOrderItem(
            DomesticOrderManagementRequest orderManagementRequest, Order order, OrderToLe orderToLe,
            OrderGsc orderGsc) {
        DomesticCallingServiceOrderItem orderItem = new DomesticCallingServiceOrderItem();
        orderItem.setInternationalCalling(Boolean.FALSE);
        orderItem.setRequestType(getRequestType(orderToLe));
        orderItem.setDirectConnectionType(orderGsc.getAccessType());
        orderItem.setServiceName(DID);
        List<DomesticCallingServiceDetail> domesticCallingServiceDetails = orderGscDetailRepository
                .findByorderGsc(orderGsc).stream().map(this::buildDomesticCallingServiceDetail)
                .collect(Collectors.toList());
        for (int i = 0; i < domesticCallingServiceDetails.size(); i++) {
            domesticCallingServiceDetails.get(i).setDomesticCallingServiceDetailId(i + 1);
        }
        orderItem.setDomesticCallingServiceDetails(domesticCallingServiceDetails);
        orderManagementRequest.setRequestedCompletionDate(getRequestedCompletionDate(orderGsc.getOrderGscDetails()));
        return orderItem;
    }

    private DomesticOrderManagementRequest buildDomesticServiceOrderItems(
            DomesticOrderManagementRequest orderManagementRequest, Order order, OrderToLe orderToLe,
            List<OrderGsc> orderGscs) {
        List<DomesticCallingServiceOrderItem> callingServiceOrderItems = orderGscs.stream()
                .map(orderGsc -> buildDomesticServiceOrderItem(orderManagementRequest, order, orderToLe, orderGsc))
                .collect(Collectors.toList());
        for (int i = 0; i < callingServiceOrderItems.size(); i++) {
            callingServiceOrderItems.get(i).setOrderItemId(i + 1);
        }
        orderManagementRequest.setDomesticCallingServiceOrderItem(callingServiceOrderItems);
        orderManagementRequest.setSFDCOPTYID(orderToLe.getTpsSfdcCopfId());
        return orderManagementRequest;
    }

    /**
     * Build Domestic Order Management Request for Tiger
     *
     * @param order
     * @return {@link DomesticOrderManagementRequest}
     */
    public DomesticOrderManagementRequest buildDomesticOrderManagementRequest(Order order) {
        OrderToLe orderToLe = gscOrderService.getOrderToLeByOrder(order);
        List<OrderGsc> orderGscs = Optional
                .<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
                .orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
                .map(orderProductSolutionRepository::findByOrderToLeProductFamily).flatMap(List::stream)
                .map(orderProductSolution -> orderGscRepository
                        .findByorderProductSolutionAndStatus(orderProductSolution, GscConstants.STATUS_ACTIVE))
                .flatMap(List::stream)
                .filter(orderGsc -> GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName()))
                .collect(Collectors.toList());
        DomesticOrderManagementRequest orderManagementRequest = new DomesticOrderManagementRequest();
        if (CollectionUtils.isEmpty(orderGscs)) {
            return orderManagementRequest;
        } else {
            return Try.success(orderManagementRequest).mapTry(request -> buildBaseOrderValues(request, orderToLe))
                    .map(request -> buildDomesticServiceOrderItems(request, order, orderToLe, orderGscs))
                    .getOrElseThrow(e -> new TCLException(String.format(
                            "Error occurred while building domestic Order management request for order id: %s",
                            order.getId()), e));
        }
    }

    /**
     * Build International Order Request for Tiger
     *
     * @param order
     * @return {@link InternationalOrderManagementRequest}
     */
    public InternationalOrderManagementRequest buildInternationalOrderRequest(Order order) {
        OrderToLe orderToLe = gscOrderService.getOrderToLeByOrder(order);
        List<OrderGsc> orderGscs = Optional
                .<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
                .orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
                .map(orderProductSolutionRepository::findByOrderToLeProductFamily).flatMap(List::stream)
                .map(orderProductSolution -> orderGscRepository
                        .findByorderProductSolutionAndStatus(orderProductSolution, GscConstants.STATUS_ACTIVE))
                .flatMap(List::stream)
                .filter(orderGsc -> !GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())
                        && !GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orderGscs)) {
            return new InternationalOrderManagementRequest();
        } else {
            return Try.success(new InternationalOrderManagementRequest())
                    .mapTry(request -> buildBaseOrderValues(request, orderToLe))
                    .map(request -> buildAccessServiceOrderItems(request, order, orderToLe, orderGscs))
                    .getOrElseThrow(e -> new TCLException(
                            String.format("Error occurred while building Order management request for order id: %s",
                                    order.getId()),
                            e));
        }
    }

    private InterConnectTypeDetail buildInterConnectTypeDetail(OrderGsc orderGsc, OrderGscDetail orderGscDetail, MstProductComponent accessTypeComponent) {
        List<OrderProductComponent> accessTypeOrderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), accessTypeComponent,
                        GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        OrderProductComponent accessTypeOrderProductComponent = accessTypeOrderProductComponents
                .stream().findFirst().orElseThrow(() -> new RuntimeException(
                        String.format("No SIP attributes product component found for configuration id: %s, access type: %s", orderGscDetail.getId(), accessTypeComponent.getName())));
        Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes = accessTypeOrderProductComponent
                .getOrderProductComponentsAttributeValues();
        List<String> attributesList = Arrays.asList(GscConstants.CUSTOMER_PUBLIC_IP, GscConstants.CODEC,
                GscConstants.NO_OF_CONCURRENT_CHANNEL, GscConstants.EQUIPMENT_ADDRESS);
        List<ProductAttributeMaster> attributeMasters = productAttributeMasterRepository.findByNameIn(attributesList);
        List<OrderProductComponentsAttributeValue> attributeValues = orderProductComponentsAttributeValueRepository.findAllByOrderProductComponentAndProductAttributeMasterIn(
                accessTypeOrderProductComponent, attributeMasters);
        Map<String, String> attributeMap = Optional.ofNullable(attributeValues)
                .orElse(ImmutableList.of())
                .stream()
                .collect(Collectors.toMap(value -> value.getProductAttributeMaster().getName(),
                        value -> value.getAttributeValues()));
        InterConnectTypeDetail typeDetail = new InterConnectTypeDetail();
        String codec = CODEC_TRANSLATION_TABLE.get(attributeMap.get(GscConstants.CODEC));
        Objects.requireNonNull(codec, String.format("Codec SIP attribute cannot be null for configuration id: %s", orderGscDetail.getId()));
        typeDetail.setCodec(codec);
        typeDetail.setNoOfConcurrentChannels(Integer.valueOf(attributeMap.getOrDefault(GscConstants.NO_OF_CONCURRENT_CHANNEL, "0")));
        Address siteAddress = new Address();
        siteAddress.setAddressLine1(attributeMap.getOrDefault(GscConstants.EQUIPMENT_ADDRESS, ""));
        siteAddress.setCity("");
        siteAddress.setCountry("");
        siteAddress.setState("");
        siteAddress.setZipCode("");
        if (PUBLIC_IP.equalsIgnoreCase(orderGsc.getAccessType())) {
            typeDetail.setPublicIPAddresses(attributeMap.get(GscConstants.CUSTOMER_PUBLIC_IP));
        }
        typeDetail.setServiceAddress(siteAddress);
        return typeDetail;
    }

    private InterConnectOrderItem buildInterConnectOrderItem(OrderGsc orderGsc, String serviceName) {
        InterConnectOrderItem orderItem = new InterConnectOrderItem();
        orderItem.setConnectionType(orderGsc.getAccessType());
        orderItem.setServiceName(serviceName);
        orderItem.setRequestType(INTERCONNECT_REQUEST_TYPE_NEW);
        MstProductComponent accessTypeComponent = mstProductComponentRepository
                .findByName(orderGsc.getAccessType());
        List<InterConnectTypeDetail> interConnectTypeDetails = orderGsc.getOrderGscDetails().stream()
                .map(orderGscDetail -> buildInterConnectTypeDetail(orderGsc, orderGscDetail, accessTypeComponent))
                .collect(Collectors.toList());
        for (int i = 0; i < interConnectTypeDetails.size(); i++) {
            interConnectTypeDetails.get(i).setInterConnectionDetailId(i + 1);
        }
        orderItem.setInterConnectTypeDetails(interConnectTypeDetails);
        return orderItem;
    }

    private <T extends InterConnectOrderManagementRequest> T buildInterConnectOrderItems(T orderManagementRequest,
                                                                                         Order order, OrderToLe orderToLe, List<OrderGsc> orderGscs, String serviceName) {
        orderManagementRequest.setSFDCID(orderToLe.getTpsSfdcCopfId());
        List<InterConnectDetail> interConnectDetails = tigerServiceHelper.getAccessServiceInterConnectDetails(order.getId(),
                orderManagementRequest.getRelatedParties().stream()
                        .filter(Party::getRequestor)
                        .findFirst()
                        .map(Party::getOrgId)
                        .orElseThrow(() -> new RuntimeException("No requestor party found")));
        Set<Tuple2<String, String>> interConnectTypes = interConnectDetails.stream()
                .map(interConnectDetail -> Tuple.of(interConnectDetail.getServiceType().toUpperCase(),
                        interConnectDetail.getInterconnectType().toUpperCase()))
                .collect(Collectors.toSet());
        List<InterConnectOrderItem> interConnectOrderItems = orderGscs.stream()
                .filter(orderGsc -> !interConnectTypes.contains(Tuple.of(orderGsc.getProductName().toUpperCase(),
                        orderGsc.getAccessType().toUpperCase())))
                .map(orderGsc -> buildInterConnectOrderItem(orderGsc, serviceName))
                .collect(Collectors.toList());

        // Collect and save existing interconnect id against to each OrderGsc
        if (CollectionUtils.isEmpty(interConnectOrderItems)) {
            getOrderGscInterConnect(interConnectDetails, orderGscs);
        }

        for (int i = 0; i < interConnectOrderItems.size(); i++) {
            interConnectOrderItems.get(i).setOrderItemId(i + 1);
        }
        orderManagementRequest.setInterconnectOrderItems(interConnectOrderItems);
        if (!CollectionUtils.isEmpty(interConnectOrderItems)) {
            List<OrderGscDetail> orderGscDetails = orderGscs.stream().map(OrderGsc::getOrderGscDetails)
                    .flatMap(Set::stream)
                    .collect(Collectors.toList());
            orderManagementRequest.setRequestedCompletionDate(getRequestedCompletionDate(orderGscDetails));
        }
        return orderManagementRequest;
    }

    /**
     * Get Interconnect ID for each Order Gsc
     *
     * @param orderGscInterConnects
     * @param orderGscs
     */
    private void getOrderGscInterConnect(List<InterConnectDetail> orderGscInterConnects, List<OrderGsc> orderGscs) {
        Map<OrderGsc, String> mapInterconnect = new HashMap<>();
        orderGscs.stream().forEach(orderGsc -> {
            orderGscInterConnects.stream().filter(orderGscInterConnect -> orderGscInterConnect.getServiceType().equalsIgnoreCase(orderGsc.getProductName()))
                    .map(orderGscInterConnect -> mapInterconnect.put(orderGsc, orderGscInterConnect.getInterconnectID()));
        });
        setOrderGscInterconnect(mapInterconnect);
    }

    /**
     * Create OrderGsc level attributes
     *
     * @param orderGscInterConnects
     */
    private void setOrderGscInterconnect(Map<OrderGsc, String> orderGscInterConnects) {
        orderGscInterConnects.forEach((orderGsc, interConnectId) -> {
            final Map<String, String> orderAttributes = ImmutableMap.of(GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID,
                    interConnectId);
            gscOrderService.saveOrderGscAttributes(orderGsc, orderAttributes);
        });
    }

    public InterConnectOrderManagementRequest buildInterConnectOrderRequest(Order order, OrderToLe orderToLe, List<OrderGsc> orderGscs, String serviceName) {
        return Try.success(new InterConnectOrderManagementRequest())
                .mapTry(request -> buildBaseOrderValues(request, orderToLe))
                .map(request -> buildInterConnectOrderItems(request, order, orderToLe, orderGscs, serviceName))
                .getOrElseThrow(e -> new TCLException(
                        String.format("Error occurred while building Interconnect Order management request for order id: %s",
                                order.getId()),
                        e));
    }

}
