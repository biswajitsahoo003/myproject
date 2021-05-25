package com.tcl.dias.oms.gsc.service.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardFamilyBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.DashboardLegalEntityBean;
import com.tcl.dias.oms.beans.DashboardQuoteBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.util.GscConstants;
//import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;

/**
 * Services to handle all dashboard related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscDashboardService {

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserRepository userRepository;

    @Value("${customer.support.email}")
    String customerSupportEmail;

//    @Autowired
//    PartnerCustomerDetailsService partnerCustomerDetailsService;

    /**
     * Constructs DashboardQuoteBean by OrderToLe
     *
     * @param orderToLe
     * @return {@link DashboardQuoteBean}
     */
    private static DashboardQuoteBean constructDashBoardQuoteBean(OrderToLe orderToLe) {
        DashboardQuoteBean dashboardQuoteBean = new DashboardQuoteBean();
        dashboardQuoteBean.setCreatedDate(orderToLe.getOrder().getCreatedTime());
        dashboardQuoteBean.setOrderCode(orderToLe.getOrder().getOrderCode());
        dashboardQuoteBean.setOrderId(orderToLe.getOrder().getId());
        dashboardQuoteBean.setQuoteCode(orderToLe.getOrder().getQuote().getQuoteCode());
        dashboardQuoteBean.setQuoteId(orderToLe.getOrder().getQuote().getId());
        dashboardQuoteBean.setQuoteStage(orderToLe.getStage());
        return dashboardQuoteBean;
    }

    /**
     * Group customers to a List
     *
     * @param customerMap
     * @param customerDetails
     */
    private static void groupBasedOnCustomer(Map<Integer, List<CustomerDetail>> customerMap,
                                             List<CustomerDetail> customerDetails) {
        customerDetails.forEach(customerDetail -> {
            if (customerMap.get(customerDetail.getCustomerId()) == null) {
                List<CustomerDetail> custDetails = new ArrayList<>();
                custDetails.add(customerDetail);
                customerMap.put(customerDetail.getCustomerId(), custDetails);
            } else {
                customerMap.get(customerDetail.getCustomerId()).add(customerDetail);
            }
        });
    }

    /**
     * Get Dashboard Details of gsc orders by customer.
     *
     * @return {@link DashBoardBean}
     */
    public Try<DashBoardBean> getDashboardDetails() throws TclCommonException {
        return getCustomerDetails().flatMap(this::getDashboardBeanDetails);
    }

    /**
     * Construct DashBoardBean based on customer Map
     *
     * @param customerMap
     * @return {@link DashBoardBean}
     */
    public Try<DashBoardBean> getDashboardBeanDetails(Map<Integer, List<CustomerDetail>> customerMap) {
        OrderCount orderCountMain = new OrderCount();
        DashBoardBean dashBoardBean = new DashBoardBean();
        if (customerMap.size() == 0) {
            return Try.success(dashBoardBean);
        }
        List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();
        customerMap.entrySet().stream().map(customerEntry -> customerMap.get(customerEntry.getKey()))
                .forEach(customerDetail -> {
                    customerDetail.forEach(customer -> {
                        DashboardCustomerbean gscDashboardCustomerbean = new DashboardCustomerbean();
                        gscDashboardCustomerbean.setCustomerId(customer.getCustomerId());
                        if (Objects.nonNull(customer.getCustomerName())) {
                            gscDashboardCustomerbean.setCustomerName(customer.getCustomerName());
                        }
                        List<OrderToLe> orderToLes = orderToLeRepository
                                .findByErfCusCustomerLegalEntityId(customer.getCustomerLeId());
                        if (Objects.nonNull(orderToLes) && !orderToLes.isEmpty()) {
                            OrderCount orderCount = new OrderCount();
                            orderCount.orderCount = 0;
                            Map<String, Object> countMap = constructDashBoardLegalEntityBean(orderToLes,
                                    gscDashboardCustomerbean, orderCount);
                            gscDashboardCustomerbean.setTotalOrderCount((Integer) countMap.get("orderCount"));
                            gscDashboardCustomerbean.setActiveOrderCount((Integer) countMap.get("activeOrderCount"));
                            dashboardCustomerbeans.add(gscDashboardCustomerbean);
                            orderCountMain.totalOrdersCount += (Integer) countMap.get("orderCount");
                            orderCountMain.totalActiveOrdersCount += (Integer) countMap.get("activeOrderCount");
                            orderCountMain.totalActiveSitesCount += (Long) countMap.get("activeSitesCount");
                            orderCountMain.totalSitesCount += (Long) countMap.get("sitesCount");
                        }
                    });
                });

        dashBoardBean.setDashboardCustomerbeans(dashboardCustomerbeans);
        dashBoardBean.setTotalOrders(orderCountMain.totalOrdersCount);
        dashBoardBean.setActiveOrders(orderCountMain.totalActiveOrdersCount);
        dashBoardBean.setTotalSites(orderCountMain.totalSitesCount);
        dashBoardBean.setActiveSites(orderCountMain.totalActiveSitesCount);

        return Try.success(dashBoardBean);
    }

    /**
     * Get customer details by user info
     *
     * @return {@link Map<Integer,List<CustomerDetail>>}
     */
    private Try<Map<Integer, List<CustomerDetail>>> getCustomerDetails() throws TclCommonException {
        Map<Integer, List<CustomerDetail>> customerMap = new HashMap<>();
        List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
        if (Objects.isNull(customerDetails) || customerDetails.isEmpty()) {
            return Try.success(customerMap);
        }
        groupBasedOnCustomer(customerMap, customerDetails);
        return Try.success(customerMap);
    }

    /**
     * Constructs DashboardLegalEntityBean for family Type GSC
     *
     * @param orderToLes
     * @param gscDashboardCustomerbean
     * @param orderCount
     * @return
     */
    private Map<String, Object> constructDashBoardLegalEntityBean(List<OrderToLe> orderToLes,
                                                                  DashboardCustomerbean gscDashboardCustomerbean, OrderCount orderCount) {
        Map<String, Object> countMap = new HashMap<>();
        orderCount.sitesCount = 0L;
        orderCount.activeSitesCount = 0L;
        orderCount.activeOrderCount = 0;
        gscDashboardCustomerbean.setLegalEntityBeans(orderToLes.stream().map(orderToLe -> {
            DashboardLegalEntityBean gscDashboardLegalEntityBean = new DashboardLegalEntityBean();
            if (!orderToLe.getOrderToLeProductFamilies().isEmpty()) {
                OrderToLeProductFamily orderToLeProductFamily = orderToLe.getOrderToLeProductFamilies().stream()
                        .findFirst().get();
                if (orderToLeProductFamily.getMstProductFamily().getName()
                        .equalsIgnoreCase(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase())) {
                    orderCount.orderCount++;
                    if (!(orderToLe.getOrder().getStage()
                            .equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())
                            || (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.getOrderType())
                            && orderToLe.getOrder().getStage()
                            .equalsIgnoreCase(OrderStagingConstants.ORDER_IN_PROGRESS.toString())))) {
                        orderCount.activeOrderCount++;
                    }
                    gscDashboardLegalEntityBean.setLegEntityId(orderToLe.getId());
                    gscDashboardLegalEntityBean.setQuoteBean(constructDashBoardQuoteBean(orderToLe));
                    gscDashboardLegalEntityBean
                            .setFamilyBeans(constructDashBoardFamilyBean(orderToLeProductFamily, orderCount));

                    orderCount.sitesCount += (Long) orderCount.siteCountConfigurationLevel;
                    orderCount.activeSitesCount += (Long) orderCount.activeSiteCountConfigurationLevel;
                }

            }
            return gscDashboardLegalEntityBean;
        }).collect(Collectors.toList()));
        countMap.put("orderCount", orderCount.orderCount);
        countMap.put("activeOrderCount", orderCount.activeOrderCount);
        countMap.put("sitesCount", orderCount.sitesCount);
        countMap.put("activeSitesCount", orderCount.activeSitesCount);
        return countMap;
    }

    /**
     * Construcs DashBoardFamilyBean by OrderToLeProductFamily
     *
     * @param orderToLeProductFamily
     * @param orderCount
     * @return {@link DashBoardFamilyBean}
     */
    private List<DashBoardFamilyBean> constructDashBoardFamilyBean(OrderToLeProductFamily orderToLeProductFamily,
                                                                   OrderCount orderCount) {
        List<DashBoardFamilyBean> dashBoardFamilyBeans = new ArrayList<>();
        DashBoardFamilyBean gscDashBoardFamilyBean = new DashBoardFamilyBean();
        gscDashBoardFamilyBean.setFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
        processOrderGsc(gscDashBoardFamilyBean, orderToLeProductFamily, orderCount);
        dashBoardFamilyBeans.add(gscDashBoardFamilyBean);
        return dashBoardFamilyBeans;
    }

    /**
     * Process OrderGsc based on orderToLeProductFamily
     *
     * @param gscDashBoardFamilyBean
     * @param orderToLeProductFamily
     * @param orderCount
     */
    private void processOrderGsc(DashBoardFamilyBean gscDashBoardFamilyBean,
                                 OrderToLeProductFamily orderToLeProductFamily, OrderCount orderCount) {
        orderCount.siteCountConfigurationLevel = 0L;
        orderCount.activeSiteCountConfigurationLevel = 0L;
        orderToLeProductFamily.getOrderProductSolutions().forEach(solution -> {
            List<OrderGsc> orderGscs = orderGscRepository.findByorderProductSolution(solution);
            if (!orderGscs.isEmpty()) {
                gscDashBoardFamilyBean.setGscOrderBean(orderGscs.stream()
                        .map(orderGsc -> gscOrderService.fromOrderGsc(orderGsc)).collect(Collectors.toList()));
                Long configurationCount = gscDashBoardFamilyBean.getGscOrderBean().stream()
                        .map(gscOrderBean -> gscOrderBean.getConfigurations()).mapToLong(List::size).sum();
                orderCount.siteCountConfigurationLevel += configurationCount;

                orderCount.activeSiteCountConfigurationLevel += gscDashBoardFamilyBean.getGscOrderBean().stream()
                        .map(gscOrderBean -> gscOrderBean.getConfigurations())
                        .map(gscOrderConfigurationBeans -> gscOrderConfigurationBeans.stream().filter(
                                gscOrderConfigurationBean -> Objects.nonNull(gscOrderConfigurationBean.getStatus()))
                                .filter(gscOrderConfigurationBean -> gscOrderConfigurationBean.getStatus()
                                        .equals(GscConstants.ACTIVE_ORDER_CONFIGURATION_STATUS))
                                .collect(Collectors.toList()))
                        .mapToLong(List::size).sum();

                Long provisionalCount = gscDashBoardFamilyBean.getGscOrderBean().stream()
                        .map(gscOrderBean -> gscOrderBean.getConfigurations())
                        .map(gscOrderConfigurationBeans -> gscOrderConfigurationBeans.stream().filter(
                                gscOrderConfigurationBean -> Objects.nonNull(gscOrderConfigurationBean.getStage()))
                                .filter(gscOrderConfigurationBean -> gscOrderConfigurationBean.getStage()
                                        .equals(GscConstants.ACTIVE_ORDER_CONFIGURATION_STAGE))
                                .collect(Collectors.toList()))
                        .mapToLong(List::size).sum();

                gscDashBoardFamilyBean.setProvisionedSiteCount(provisionalCount);
                gscDashBoardFamilyBean.setTotalCount(configurationCount);
            }
        });
    }

    /**
     * Get OrderDetails based on orderId from GscOrderService
     *
     * @param orderId
     * @return {@link GscOrderDataBean}
     */
    public Try<GscOrderDataBean> getOrderDetails(Integer orderId) {
        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        return gscOrderService.getGscOrderById(orderId);
    }

    /**
     * Send Email Notification
     *
     * @return
     */
    public void sendMailNotification() throws TclCommonException {
        try {
            User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
            notificationService.helpTicketNotification(user.getEmailId(), customerSupportEmail,
                    "Domestic Voice Service Order Selection", "Customer wants to proceed on the domestic voice order with only one selection. " +
                            "Please get in touch with Customer to proceed with the order.", CommonConstants.ALL);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }

    /**
     * This class contains the all order and active orders.
     *
     * @author AVALLAPI
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited
     */
    public static class OrderCount {
        Integer orderCount;
        Integer activeOrderCount;
        Long sitesCount;
        Long activeSitesCount;
        Long totalOrdersCount = 0L;
        Long totalActiveOrdersCount = 0L;
        Long totalActiveSitesCount = 0L;
        Long totalSitesCount = 0L;
        Long siteCountConfigurationLevel;
        Long activeSiteCountConfigurationLevel;
    }

}
