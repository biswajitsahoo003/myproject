package com.tcl.dias.oms.gsc.service.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscDocumentsByProductAndCountryRequest;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.GscAttachments;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.repository.GscAttachmentsRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tcl.dias.oms.gsc.util.GscConstants.DOCUMENT_STATUS_PENDING;

/*
 *
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
@Service
public class GscOrderDetailService2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscOrderDetailService2.class);

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    GscIsoCountries gscIsoCountries;

    @Value("${rabbitmq.country.specific.documents.queue}")
    String countrySpecificDocumentsQueueName;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    GscOrderDetailService gscOrderDetailService;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    GscAttachmentsRepository gscAttachmentRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Value("${rabbitmq.le.sap.queue}")
    String secsCodeQueue;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    GscOrderService gscOrderService;

    /**
     * Method to fetch config related docs.
     *
     * @param configurationId
     * @return
     * @throws TclCommonException
     */
    @Transactional
    public List<GscAttachmentBean> getDocumentsForConfigurationId(Integer configurationId) throws TclCommonException {
        OrderGscDetail orderGscDetail = fetchOrderGscDetailById(configurationId);
        return getDocumentsForConfiguration(orderGscDetail);
    }

    /**
     * Method to fetch ordergscdetail by id.
     *
     * @param orderGscDetailId
     * @return
     * @throws TclCommonException
     */
    private OrderGscDetail fetchOrderGscDetailById(Integer orderGscDetailId) throws TclCommonException {
        LOGGER.info("OrderGscDetail ID :: {}", orderGscDetailId);
        Optional<OrderGscDetail> optionalOrderGscDetail = orderGscDetailRepository.findById(orderGscDetailId);
        if (!optionalOrderGscDetail.isPresent()) {
            throw new TclCommonException(ExceptionConstants.GSC_ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        return optionalOrderGscDetail.get();
    }

    /**
     * Method to get documents for configuration
     *
     * @param orderGscDetail
     * @return
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private List<GscAttachmentBean> getDocumentsForConfiguration(OrderGscDetail orderGscDetail)
            throws TclCommonException, IllegalArgumentException {
        List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(
                GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, orderGscDetail.getId(), GscConstants.OTHERS);
        if (CollectionUtils.isEmpty(omsAttachments)) {
            OrderToLe orderToLe = orderGscDetail.getOrderGsc().getOrderToLe();
            List<GscCountrySpecificDocumentBean> countrySpecificDocuments = getDocumentsForConfigurationFromProductCatalog(
                    orderGscDetail);
            List<GscAttachments> gscAttachments = countrySpecificDocuments.stream()
                    .map(documentBean -> toGscAttachment(documentBean, orderGscDetail, orderToLe))
                    .collect(Collectors.toList());
            omsAttachments = omsAttachmentRepository.saveAll(
                    gscAttachments.stream().map(GscAttachments::getOmsAttachment).collect(Collectors.toList()));
            gscAttachmentRepository.saveAll(gscAttachments);
        }
        List<GscAttachmentBean> gscAttachmentBeans = gscAttachmentRepository.findAllByOmsAttachmentIn(omsAttachments)
                .stream().map(GscAttachmentBean::fromGscAttachment).collect(Collectors.toList());

        gscAttachmentBeans.forEach(gscAttachment -> {
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                gscAttachment.setSwiftEnabled(true);
            } else {
                gscAttachment.setSwiftEnabled(false);
            }
            OmsAttachment attachment = omsAttachmentRepository
                    .findByReferenceNameAndAttachmentType(gscAttachment.getDocumentUid(), GscConstants.OTHERS);
            if (Objects.nonNull(attachment)) {
                gscAttachment.setTemplateAttachmentId(attachment.getErfCusAttachmentId());
            }
        });
        return gscAttachmentBeans;
    }

    /**
     * Method to get documents from PC.
     *
     * @param orderGscDetail
     * @return
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private List<GscCountrySpecificDocumentBean> getDocumentsForConfigurationFromProductCatalog(
            OrderGscDetail orderGscDetail) throws TclCommonException, IllegalArgumentException {
        GscIsoCountries.GscCountry gscCountry = gscIsoCountries.forName(orderGscDetail.getSrc());
        if (Objects.isNull(gscCountry)) {
            throw new TclCommonException("Unable to get document list from product catalogue service");
        }
        String iso3CountryCode = gscCountry.getCode();
        String productName = orderGscDetail.getOrderGsc().getProductName();
        GscDocumentsByProductAndCountryRequest request = new GscDocumentsByProductAndCountryRequest();
        request.setIso3CountryCode(iso3CountryCode);
        request.setProductName(productName);
        LOGGER.info("MDC Filter token value in before Queue call getDocumentsForConfigurationFromProductCatalog {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String mqResponse = (String) mqUtils.sendAndReceive(countrySpecificDocumentsQueueName,
                GscUtils.toJson(request));
        if (!Strings.isNullOrEmpty(mqResponse)) {
            return GscUtils.fromJson(mqResponse, new TypeReference<List<GscCountrySpecificDocumentBean>>() {
            });
        } else {
            throw new TclCommonException("Error occurred while fetching document list from product catalogue service");
        }
    }

    /**
     * Method to convert document bean to GscAttachment.
     *
     * @param documentBean
     * @param orderGscDetail
     * @param orderToLe
     * @return
     */
    private GscAttachments toGscAttachment(GscCountrySpecificDocumentBean documentBean, OrderGscDetail orderGscDetail,
                                           OrderToLe orderToLe) {
        GscAttachments gscAttachment = new GscAttachments();
        gscAttachment.setDocumentName(documentBean.getDocumentName());
        gscAttachment.setDocumentCategory(documentBean.getCategory());
        gscAttachment.setDocumentType(documentBean.getType());
        gscAttachment.setStatus(DOCUMENT_STATUS_PENDING);
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            gscAttachment.setDocumentUId(documentBean.getuID() + "_OBJECT");
        } else {
            gscAttachment.setDocumentUId(documentBean.getuID());
        }
        gscAttachment.setTemplateName(documentBean.getTemplate());
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setAttachmentType(GscConstants.OTHERS);
        omsAttachment.setReferenceId(orderGscDetail.getId());
        omsAttachment.setOrderToLe(orderToLe);
        omsAttachment.setReferenceName(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
        gscAttachment.setOmsAttachment(omsAttachment);
        return gscAttachment;
    }
}
