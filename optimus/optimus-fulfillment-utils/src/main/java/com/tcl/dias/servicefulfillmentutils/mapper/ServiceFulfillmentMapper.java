package com.tcl.dias.servicefulfillmentutils.mapper;


import java.util.List;
import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillmentutils.beans.ScOrderAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskAdminBean;
import org.springframework.util.CollectionUtils;

public class ServiceFulfillmentMapper {

    private ServiceFulfillmentMapper() {
        //Do Nothing
    }

    public static ScOrderBean mapEntityToOrderBean(ScOrder scOrder) {
        ScOrderBean scOrderBean = new ScOrderBean();
        scOrderBean.setId(scOrder.getId());
        scOrderBean.setCreatedBy(scOrder.getCreatedBy());
        scOrderBean.setCreatedDate(scOrder.getCreatedDate());
        scOrderBean.setCustomerGroupName(scOrder.getCustomerGroupName());
        scOrderBean.setCustomerSegment(scOrder.getCustomerSegment());
        scOrderBean.setDemoFlag(scOrder.getDemoFlag());
        scOrderBean.setErfCustCustomerId(scOrder.getErfCustCustomerId());
        scOrderBean.setErfCustCustomerName(scOrder.getErfCustCustomerName());
        scOrderBean.setErfCustLeId(scOrder.getErfCustLeId());
        scOrderBean.setErfCustLeName(scOrder.getErfCustLeName());
        scOrderBean.setErfCustPartnerId(scOrder.getErfCustPartnerId());
        scOrderBean.setErfCustPartnerName(scOrder.getErfCustPartnerName());
        scOrderBean.setErfCustPartnerLeId(scOrder.getErfCustPartnerLeId());
        scOrderBean.setPartnerCuid(scOrder.getPartnerCuid());
        scOrderBean.setErfCustSpLeId(scOrder.getErfCustSpLeId());
        scOrderBean.setErfCustSpLeName(scOrder.getErfCustSpLeName());
        scOrderBean.setErfUserCustomerUserId(scOrder.getErfUserCustomerUserId());
        scOrderBean.setErfUserInitiatorId(scOrder.getErfUserInitiatorId());
        scOrderBean.setUuid(scOrder.getUuid());
        scOrderBean.setUpdatedDate(scOrder.getUpdatedDate());
        scOrderBean.setUpdatedBy(scOrder.getUpdatedBy());
        scOrderBean.setTpsSfdcCuid(scOrder.getTpsSfdcCuid());
        scOrderBean.setTpsSecsId(scOrder.getTpsSecsId());
        scOrderBean.setTpsSapCrnId(scOrder.getTpsSapCrnId());
        scOrderBean.setTpsCrmSystem(scOrder.getTpsCrmSystem());
        scOrderBean.setTpsCrmOptyId(scOrder.getTpsCrmOptyId());
        scOrderBean.setTpsCrmCofId(scOrder.getTpsCrmCofId());
        scOrderBean.setTpsCrmOptyId(scOrder.getTpsSfdcOptyId());
        scOrderBean.setErfOrderId(scOrder.getErfOrderId());
        scOrderBean.setSfdcAccountId(scOrder.getSfdcAccountId());
        scOrderBean.setParentOpOrderCode(scOrder.getParentOpOrderCode());
        scOrderBean.setSfdcOptyId(scOrder.getTpsSfdcOptyId());
        scOrderBean.setParentId(scOrder.getParentId());
        scOrderBean.setOrderType(scOrder.getOrderType());
        scOrderBean.setOrderStatus(scOrder.getOrderStatus());
        scOrderBean.setOrderStartDate(scOrder.getOrderStartDate());
        scOrderBean.setOrderSource(scOrder.getOrderSource());
        scOrderBean.setOrderEndDate(scOrder.getOrderEndDate());
        scOrderBean.setOrderCategory(scOrder.getOrderCategory());
        scOrderBean.setErfOrderLeId(scOrder.getErfOrderLeId());
        scOrderBean.setOpOrderCode(scOrder.getOpOrderCode());
        scOrderBean.setOpportunityClassification(scOrder.getOpportunityClassification());
        scOrderBean.setIsActive(scOrder.getIsActive());
        scOrderBean.setIsBundleOrder(scOrder.getIsBundleOrder());
        scOrderBean.setIsMultipleLe(scOrder.getIsMultipleLe());
        scOrderBean.setLastMacdDate(scOrder.getLastMacdDate());
        scOrderBean.setMacdCreatedDate(scOrder.getMacdCreatedDate());
        scOrderBean.setScOrderAttributes(null);
        scOrderBean.setscContractInfos(null);
        scOrderBean.setScServiceDetails(null);
        scOrderBean.setScCommercialBean(null);
        return scOrderBean;
    }

     public static ScContractInfoBean mapScContractInfoBean(ScOrder scOrder)
     {
        ScContractInfo scContractInfo=scOrder.getScContractInfos1().stream().findFirst().get();
          return mapContractInfoEntityToBean(scContractInfo);
     }

    public static List<ScOrderAttributesBean> mapScOrderAttributesBeans(ScOrder scOrder, List<ScOrderAttributesBean> scOrderAttributesBeanList ) {
        if (!CollectionUtils.isEmpty(scOrder.getScOrderAttributes())) {

            scOrder.getScOrderAttributes().forEach(attr -> {
                ScOrderAttributesBean scOrderAttributesBean = new ScOrderAttributesBean();
                scOrderAttributesBean.setName(attr.getAttributeName());
                scOrderAttributesBean.setValue(attr.getAttributeValue());
                scOrderAttributesBeanList.add(scOrderAttributesBean);
            });
        }
    return scOrderAttributesBeanList;
    }


    public static ScContractInfoBean mapContractInfoEntityToBean(ScContractInfo scContractInfo) {
        ScContractInfoBean scContractInfoBean = new ScContractInfoBean();
        scContractInfoBean.setAccountManager(scContractInfo.getAccountManager());
        scContractInfoBean.setAccountManagerEmail(scContractInfo.getAccountManagerEmail());
        scContractInfoBean.setArc(scContractInfo.getArc());
        scContractInfoBean.setBillingAddress(scContractInfo.getBillingAddress());
        scContractInfoBean.setBillingFrequency(scContractInfo.getBillingFrequency());
        scContractInfoBean.setBillingMethod(scContractInfo.getBillingMethod());
        scContractInfoBean.setContractEndDate(scContractInfo.getContractEndDate());
        scContractInfoBean.setContractStartDate(scContractInfo.getContractStartDate());
        scContractInfoBean.setCreatedBy(scContractInfo.getCreatedBy());
        scContractInfoBean.setCreatedDate(scContractInfo.getCreatedDate());
        scContractInfoBean.setCustomerContact(scContractInfo.getCustomerContact());
        scContractInfoBean.setCustomerContactEmail(scContractInfo.getCustomerContactEmail());
        scContractInfoBean.setDiscountArc(scContractInfo.getDiscountArc());
        scContractInfoBean.setDiscountMrc(scContractInfo.getDiscountMrc());
        scContractInfoBean.setDiscountNrc(scContractInfo.getDiscountNrc());
        scContractInfoBean.setErfCustCurrencyId(scContractInfo.getErfCustCurrencyId());
        scContractInfoBean.setErfCustLeId(scContractInfo.getErfCustLeId());
        scContractInfoBean.setErfCustLeName(scContractInfo.getErfCustLeName());
        scContractInfoBean.setErfCustSpLeId(scContractInfo.getErfCustSpLeId());
        scContractInfoBean.setErfCustSpLeName(scContractInfo.getErfCustSpLeName());
        scContractInfoBean.setErfLocBillingLocationId(scContractInfo.getErfLocBillingLocationId());
        scContractInfoBean.setIsActive(scContractInfo.getIsActive());
        scContractInfoBean.setLastMacdDate(scContractInfo.getLastMacdDate());
        scContractInfoBean.setMrc(scContractInfo.getMrc());
        scContractInfoBean.setNrc(scContractInfo.getNrc());
        scContractInfoBean.setBillingAddressLine1(scContractInfo.getBillingAddressLine1());
        scContractInfoBean.setBillingAddressLine2(scContractInfo.getBillingAddressLine2());
        scContractInfoBean.setBillingAddressLine3(scContractInfo.getBillingAddressLine3());
        scContractInfoBean.setBillingCity(scContractInfo.getBillingCity());
        scContractInfoBean.setBillingCountry(scContractInfo.getBillingCountry());
        scContractInfoBean.setBillingCity(scContractInfo.getBillingCity());
        scContractInfoBean.setBillingState(scContractInfo.getBillingState());
        scContractInfoBean.setBillingPincode(scContractInfo.getBillingPincode());
        scContractInfoBean.setOrderTermInMonths(scContractInfo.getOrderTermInMonths());
        scContractInfoBean.setPaymentTerm(scContractInfo.getPaymentTerm());
        scContractInfoBean.setTpsSfdcCuid(scContractInfo.getTpsSfdcCuid());
        scContractInfoBean.setUpdatedBy(scContractInfo.getUpdatedBy());
        scContractInfoBean.setUpdatedDate(scContractInfo.getUpdatedDate());
        scContractInfoBean.setBillingContactId(scContractInfo.getBillingContactId());
        scContractInfoBean.setId(scContractInfo.getId());
        return scContractInfoBean;
    }

    public static TaskAdminBean mapTaskBean(Task task)
    {
        TaskAdminBean taskAdminBean=new TaskAdminBean();
        taskAdminBean.setStatus(task.getMstStatus().getCode());
        taskAdminBean.setTaskDefKey(task.getMstTaskDef().getKey());
        taskAdminBean.setAssignee(task.getAssignee());
        taskAdminBean.setCategory(task.getCatagory());
        taskAdminBean.setCity(task.getCity());
        taskAdminBean.setCountry(task.getCountry());
        taskAdminBean.setCustomerName(task.getCustomerName());
        taskAdminBean.setDevicePlatform(task.getDevicePlatform());
        taskAdminBean.setDeviceType(task.getDeviceType());
        taskAdminBean.setDistributionCenterName(task.getDistributionCenterName());
        taskAdminBean.setDowntime(task.getDowntime());
        taskAdminBean.setGscFlowGroupId(task.getGscFlowGroupId());
        taskAdminBean.setIsIpDownTimeRequired(task.getIsIpDownTimeRequired());
        taskAdminBean.setIsJeopardyTask(task.getIsJeopardyTask());
        taskAdminBean.setIsTxDowntimeReqd(task.getIsTxDowntimeReqd());
        taskAdminBean.setLastMileScenario(task.getLastMileScenario());
        taskAdminBean.setLatitude(task.getLatitude());
        taskAdminBean.setLmProvider(task.getLmProvider());
        taskAdminBean.setLmType(task.getLmType());
        taskAdminBean.setLongitude(task.getLongitude());
        taskAdminBean.setOrderCategory(task.getOrderCategory());
        taskAdminBean.setOrderCode(task.getOrderCode());
        taskAdminBean.setOrderSubCategory(task.getOrderSubCategory());
        taskAdminBean.setOrderType(task.getOrderType());
        taskAdminBean.setPriority(task.getPriority());
        taskAdminBean.setProcessId(task.getProcessId());
        taskAdminBean.setQuoteCode(task.getQuoteCode());
        taskAdminBean.setQuoteId(task.getQuoteId());
        taskAdminBean.setScOrderId(task.getScOrderId());
        taskAdminBean.setServiceCode(task.getServiceCode());
        taskAdminBean.setServiceId(task.getServiceId());
        taskAdminBean.setServiceType(task.getServiceType());
        taskAdminBean.setSiteType(task.getSiteType());
        taskAdminBean.setState(task.getState());
        taskAdminBean.setTaskClosureCategory(task.getTaskClosureCategory());
        taskAdminBean.setTaskId(task.getId());
        taskAdminBean.setVendorCode(task.getVendorCode());
        taskAdminBean.setVendorName(task.getVendorName());
        taskAdminBean.setWfCaseInstId(task.getWfCaseInstId());
        taskAdminBean.setWfExecutorId(task.getWfExecutorId());
        taskAdminBean.setWfPlanItemInstId(task.getWfPlanItemInstId());
        taskAdminBean.setWfProcessInstId(task.getWfProcessInstId());
        taskAdminBean.setWfTaskId(task.getWfTaskId());
        return taskAdminBean;
    }

}
