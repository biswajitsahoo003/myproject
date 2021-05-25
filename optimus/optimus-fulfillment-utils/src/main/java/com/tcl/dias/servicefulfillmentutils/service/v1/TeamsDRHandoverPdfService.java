package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRHandoverNotePdfBean;
import com.tcl.dias.servicefulfillmentutils.helper.UploadAttachmentComponent;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang.StringUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Syed Ali.
 * @createdAt 04/03/2021, Thursday, 15:48
 */
@Service
public class TeamsDRHandoverPdfService {
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    ScServiceAttributeRepository scServiceAttributeRepository;

    @Autowired
    ScServiceSlaRepository scServiceSlaRepository;

    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    UploadAttachmentComponent uploadAttachmentComponent;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    String uploadPath;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ScAttachmentRepository scAttachmentRepository;

    @Autowired
    ScSolutionComponentRepository scSolutionComponentRepository;

    @Autowired
    CommonFulfillmentUtils commonFulfillmentUtils;

    @Autowired
    ScComponentRepository scComponentRepository;

    @Autowired
    GscFlowGroupRepository gscFlowGroupRepository;

    @Autowired
    ScContractInfoRepository scContractInfoRepository;

    @Autowired
    ScOrderAttributeRepository scOrderAttributeRepository;

    @Autowired
    FlowGroupAttributeRepository flowGroupAttributeRepository;

    private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);


    /**
     * Method to process handover note pdf.
     * @param serviceUuid
     * @param serviceId
     * @param scComponentId
     * @param batchId
     * @return
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
    public String processHandoverNotePdf(String serviceUuid, Integer serviceId, Integer scComponentId,Integer batchId) throws TclCommonException {
        String tempDownloadUrl = null;
        try {
            ScServiceDetail scServiceDetail = null;
            Map<String, String> scOrderAttributesmap;
            Set<ScOrderAttribute> scOrderAttributes;
            Map<String, String> scComponentDrSiteAttributes = null;
            Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
            if(scServiceDetailOpt.isPresent()) {
                scServiceDetail =scServiceDetailOpt.get();
            }
            ScOrder scOrder = scServiceDetail.getScOrder();
            scOrderAttributes=scServiceDetail.getScOrder().getScOrderAttributes();
            scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
                    (m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);


            if(Objects.nonNull(scComponentId)){
                Optional<ScComponent> optionalScComponent = scComponentRepository.findById(scComponentId);
                if (optionalScComponent.isPresent()){
                    ScComponent scComponent = optionalScComponent.get();
                    scComponentDrSiteAttributes = commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), scComponent.getComponentName(), scComponent.getSiteType());
                }
            }

            Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

            Map<String,Object> flowGroupAttributes = commonFulfillmentUtils.getFlowGroupAttributes(batchId);

            TeamsDRHandoverNotePdfBean saBean = setPdfBean(scServiceDetail, scOrder,batchId, scOrderAttributesmap,
                    scComponentAttributesmap,scComponentDrSiteAttributes,flowGroupAttributes);

            String html = getHtml(saBean);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PDFGenerator.createPdf(html, bos);
            byte[] outArray = bos.toByteArray();
            String fileName = "sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

            Attachment attachment = new Attachment();
            attachment.setName(fileName);
            attachment.setIsActive("Y");
            attachment.setCategory("Handover-note");
            attachment.setType("Handover-note");
            attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                LOGGER.info("SwiftApiEnabled for Handover Note with service Id {} ",serviceUuid);
                InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                fileName = ".pdf";
                if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
                        && scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
                    LOGGER.info("Getting Stored Object...");
                    StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
                            scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));
                    LOGGER.info("Stored Object for Handover Note is -> {}", storedObject.getName());
                    String[] pathArray = storedObject.getPath().split("/");
                    /*
                     * Map<String, String> cofMap = new HashMap<>(); cofMap.put("FILENAME",
                     * storedObject.getName()); cofMap.put("OBJECT_STORAGE_PATH", pathArray[1]);
                     * String tempUrl =
                     * fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000,
                     * pathArray[1], false); cofMap.put("TEMP_URL", tempUrl);
                     */
                    attachment.setName(storedObject.getName());
                    attachment.setUriPathOrUrl(pathArray[1]);
                    LOGGER.info("PDF service handover note Attachment {} to be saved at location {} for service Id{}",
                            attachment.getName(), pathArray[1],serviceUuid);
                    Attachment savedAttachment = attachmentRepository.save(attachment);
                    ScAttachment scAttachment = new ScAttachment();
                    scAttachment.setAttachment(savedAttachment);
                    scAttachment.setScServiceDetail(scServiceDetail);
                    scAttachment.setIsActive("Y");
                    scAttachment.setSiteType("A");
                    scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
                    scAttachmentRepository.save(scAttachment);
                    LOGGER.info("PDF service handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
                            scAttachment.getId(),serviceId,serviceUuid);

                }
            } else {
                String cofPath = uploadPath;
                File filefolder = new File(cofPath);
                if (!filefolder.exists()) {
                    filefolder.mkdirs();
                }
                String fileFullPath = cofPath + fileName;
                try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
                    bos.writeTo(outputStream);
                }
                attachment.setUriPathOrUrl(fileFullPath);
                Attachment savedAttachment = attachmentRepository.save(attachment);
                LOGGER.info("PDF service handover note Attachment {} to be saved at location {} for service id {}",
                        attachment.getName(),attachment.getUriPathOrUrl(),serviceUuid);
                ScAttachment scAttachment = new ScAttachment();
                scAttachment.setAttachment(savedAttachment);
                scAttachment.setScServiceDetail(scServiceDetail);
                scAttachment.setIsActive("Y");
                scAttachment.setSiteType("A");
                scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
                scAttachmentRepository.save(scAttachment);
                LOGGER.info("PDF service handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
                        scAttachment.getId(),serviceId,serviceUuid);
                /*
                 * if (cofObjectMapper != null) { cofObjectMapper.put("FILE_SYSTEM_PATH",
                 * fileFullPath); }
                 */
            }
        } catch (Exception e) {
            LOGGER.error("Error in Processing Service Acceptance {}",e);
        }
        return tempDownloadUrl;
    }

    /**
     * Method to set data in pdf bean
     * @param scServiceDetail
     * @param scOrder
     * @param scOrderAttributesmap
     * @param scComponentAttributesmap
     * @param scComponentDRAttributesmap
     * @return
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private TeamsDRHandoverNotePdfBean setPdfBean(ScServiceDetail scServiceDetail, ScOrder scOrder, Integer batchId,
                                                  Map<String, String> scOrderAttributesmap,
                                                  Map<String, String> scComponentAttributesmap,
                                                  Map<String, String> scComponentDRAttributesmap,
                                                  Map<String, Object> flowGroupAttributes) throws TclCommonException, IllegalArgumentException, ParseException {

        ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
        TeamsDRHandoverNotePdfBean pdfBean = new TeamsDRHandoverNotePdfBean();

        // Setting common attributes
        pdfBean.setOrderId(scServiceDetail.getScOrderUuid());
        pdfBean.setServiceId(scServiceDetail.getUuid());
        pdfBean.setOrderType(scServiceDetail.getOrderType());
        String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
        pdfBean.setOrderCategory(orderCategory!=null?orderCategory:"");
        pdfBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
        pdfBean.setCustomerName(scOrder.getErfCustCustomerName());

        // to fetch contact number..
        ScOrderAttribute scOrderAttribute = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scOrder.getId(), LeAttributesConstants.CONTACT_NO);
        pdfBean.setCustomerContactNo(Objects.nonNull(scOrderAttribute) ? scOrderAttribute.getAttributeValue() : "");

        pdfBean.setCustomerEmailId(scContractInfo.getCustomerContactEmail());
        pdfBean.setCustomerContactDetails(pdfBean.getCustomerName()+CommonConstants.COMMA+CommonConstants.SPACE+
                pdfBean.getCustomerEmailId()+CommonConstants.COMMA+CommonConstants.SPACE+
                pdfBean.getCustomerContactNo());

        pdfBean.setServiceType("Tata Communication with Microsoft Cloud solution Managed Services - "+scServiceDetail.getErfPrdCatalogFlavourName());
        if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().isPresent())
            pdfBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());

        pdfBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("GST_Number", "") + ";"
                + scOrderAttributesmap.getOrDefault("GST_Address", ""));

        // Setting task related attributes..
        pdfBean.setBillStartDate(String.valueOf(flowGroupAttributes.getOrDefault("msUatCompletionDate", CommonConstants.EMPTY)));
        pdfBean.setDeemedAcceptanceApplicable(String.valueOf(flowGroupAttributes.getOrDefault("deemedAcceptanceApplicable",CommonConstants.EMPTY)));

        // to fetch number of committed users.
//        Optional<GscFlowGroup> optionalGscFlowGroup =  gscFlowGroupRepository.findById(batchId);
//        if(optionalGscFlowGroup.isPresent()){
//            GscFlowGroup flowGroup = optionalGscFlowGroup.get();
//            Optional<ScComponent> scComponentOptional = scComponentRepository.findById(Integer.parseInt(flowGroup.getRefId()));
//            if (scComponentOptional.isPresent()){
//                ScComponent scComponent = scComponentOptional.get();
//                String[] scComponentName = scComponent.getComponentName().split("_");
//                if(scComponentName.length > 0 ){
//                    LOGGER.info("Parent ScCompnent Name  ::  {}", scComponentName[0]);
//                    Optional<ScComponent> optParentScComponent = scComponentRepository
//                            .findByScServiceDetailIdAndComponentName(scServiceDetail.getId(),scComponentName[0]).stream().findAny();
//                    optParentScComponent.ifPresent(component -> scComponentAttributesRepository.findByScComponent(component).stream()
//                            .filter(scComponentAttribute -> "total_users".equals(scComponentAttribute.getAttributeName()))
//                            .forEach(scComponentAttribute ->
//                                    pdfBean.setNumberOfCommittedUsers(scComponentAttribute.getAttributeValue())));
//                }
//            }
//        }

        // To fetch number of committed users..
        pdfBean.setNumberOfCommittedUsers(scComponentAttributesmap.get("Total Committed Users"));

        // To fetch all the number of users provisioned
        Integer numberOfUsersProvisioned = scComponentRepository.findByScServiceDetailIdAndSiteType(scServiceDetail.getId(), "drSite").stream()
                .flatMap(scComponent -> gscFlowGroupRepository.findByRefIdAndRefType(String.valueOf(scComponent.getId()), "COMPONENT").stream())
                .flatMap(gscFlowGroup -> flowGroupAttributeRepository.findByGscFlowGroup(gscFlowGroup).stream())
                .filter(flowGroupAttribute -> "batchUserCount".equals(flowGroupAttribute.getAttributeName()) &&
                        Objects.nonNull(flowGroupAttribute.getAttributeValue()))
                .mapToInt(flowGroupAttribute -> Integer.parseInt(flowGroupAttribute.getAttributeValue())).sum();

        pdfBean.setNumberOfUsersProvisioned(String.valueOf(numberOfUsersProvisioned));

        // to set commisioning date..
        if(scComponentAttributesmap.containsKey("billFreePeriod") && StringUtils.isNotBlank(pdfBean.getBillStartDate())){
            String strBillFreePeriod = scComponentAttributesmap.get("billFreePeriod");
            LOGGER.info("BillFreePeriod :: {}",strBillFreePeriod);
            Integer billFreePeriod = Integer.parseInt(strBillFreePeriod);
            pdfBean.setBillFreePeriod(String.valueOf(billFreePeriod));
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date commisioningDate = dateFormatter.parse(pdfBean.getBillStartDate());
            LOGGER.info("CommisioningDate :: {}",commisioningDate.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(commisioningDate);
            c.add(Calendar.DATE,billFreePeriod);
            LOGGER.info("CommisioningDate after adding billFreePeriod :: {}",c.getTime());
            pdfBean.setCommissioningDate(dateFormatter.format(c.getTime()));
        }

        LOGGER.info("PdfBean after setting all the required data :: {}", Utils.convertObjectToJson(pdfBean));
        return pdfBean;
    }

    /**
     * Method to get html
     * @param teamsDRHandoverNotePdfBean
     * @return
     */
    private String getHtml(TeamsDRHandoverNotePdfBean teamsDRHandoverNotePdfBean) {
        Map<String, Object> variable = objectMapper.convertValue(teamsDRHandoverNotePdfBean, Map.class);
        Context context = new Context();
        context.setVariables(variable);
        return templateEngine.process("teamsdr_service_acceptance_pdf", context);
    }
}
