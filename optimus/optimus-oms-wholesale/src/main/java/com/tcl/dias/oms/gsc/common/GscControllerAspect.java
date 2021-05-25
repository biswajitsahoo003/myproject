package com.tcl.dias.oms.gsc.common;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Common Aspect controller for Gsc Order and Quote controllers
 *
 * @author VISHESH AWASTHI
 */
//@Aspect
//@Component
//@Order(1)
public class GscControllerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(GscControllerAspect.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderToLeRepository orderToLeRepository;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    @Autowired
    UserInfoUtils userInfoUtils;
//    @Autowired
//    PartnerCustomerDetailsService partnerCustomerDetailsService;
    @Autowired
    DocusignAuditRepository docusignAuditRepository;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
        // Point cut definition
    }

    @Pointcut("execution(* com.tcl.dias.oms.gsc.controller.v1.GscOrderController.*(..))")
    protected void allGscOrderMethod() {
        // Point cut definition
    }

    @Pointcut("execution(* com.tcl.dias.oms.gsc.controller.v1.GscQuoteController.*(..))")
    protected void allGscQuoteMethod() {
        // Point cut definition
    }

    @Pointcut("execution(*  com.tcl.dias.oms.gsc.controller.v1.GscDashboardController.*(..))")
    protected void allGscDashboardMethod() {
        // Point cut definition
    }

    @Around("controller() && allGscDashboardMethod()")
    public Object validateQuoteAndOrderAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String httpMethod = request.getMethod();
        Object[] args = joinPoint.getArgs();
        Integer orderId = null;
        Integer orderLeId = null;
        Integer quoteId = null;
        Integer quoteLeId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof PathVariable))
                    continue;
                PathVariable pathParam = (PathVariable) annotation;
                if ("orderId".equals(pathParam.value())) {
                    orderId = (Integer) args[argIndex];
                }

                if ("orderToLeId".equals(pathParam.value())) {
                    orderLeId = (Integer) args[argIndex];
                }
                if ("quoteId".equals(pathParam.value())) {
                    quoteId = (Integer) args[argIndex];
                }

                if ("quoteLeId".equals(pathParam.value())) {
                    quoteLeId = (Integer) args[argIndex];
                }
            }
        }
        List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
        Set<Integer> customersSet = new HashSet<>();
        Set<Integer> customerLeIds = new HashSet<>();
        getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
        if (orderId != null) {
            Optional<com.tcl.dias.oms.entity.entities.Order> orderEntity = orderRepository.findById(orderId);
            if (orderEntity.isPresent()) {
                validateOrderId(orderLeId, customersSet, customerLeIds, orderEntity.get(), httpMethod);
            } else {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
            }
        }
        if (quoteId != null) {
            Optional<com.tcl.dias.oms.entity.entities.Quote> quoteEntity = quoteRepository.findById(quoteId);
            if (quoteEntity.isPresent()) {
                validateQuoteId(quoteLeId, customersSet, customerLeIds, quoteEntity.get(), httpMethod);
            } else {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
            }
        }
        return joinPoint.proceed();
    }

    @Around("controller() && allGscOrderMethod()")
    public Object validateOrderAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String httpMethod = request.getMethod();
        Object[] args = joinPoint.getArgs();
        Integer orderId = null;
        Integer orderLeId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof PathVariable))
                    continue;
                PathVariable pathParam = (PathVariable) annotation;
                if ("orderId".equals(pathParam.value())) {
                    orderId = (Integer) args[argIndex];
                }

                if ("orderToLeId".equals(pathParam.value())) {
                    orderLeId = (Integer) args[argIndex];
                }
            }
        }
        List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
        Set<Integer> customersSet = new HashSet<>();
        Set<Integer> customerLeIds = new HashSet<>();
        getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
        if (orderId != null) {
            Optional<com.tcl.dias.oms.entity.entities.Order> orderEntity = orderRepository.findById(orderId);
            if (orderEntity.isPresent()) {
                validateOrderId(orderLeId, customersSet, customerLeIds, orderEntity.get(), httpMethod);
            } else {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
            }
        }
        return joinPoint.proceed();
    }

    @Around("controller() && allGscQuoteMethod()")
    public Object validateQuoteAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String httpMethod = request.getMethod();
        Object[] args = joinPoint.getArgs();
        Integer quoteId = null;
        Integer quoteLeId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof PathVariable))
                    continue;
                PathVariable pathParam = (PathVariable) annotation;
                if ("quoteId".equals(pathParam.value())) {
                    quoteId = (Integer) args[argIndex];
                }

                if ("quoteLeId".equals(pathParam.value())) {
                    quoteLeId = (Integer) args[argIndex];
                }
            }
        }
        List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
        Set<Integer> customersSet = new HashSet<>();
        Set<Integer> customerLeIds = new HashSet<>();
        getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
        if (quoteId != null) {
            Optional<com.tcl.dias.oms.entity.entities.Quote> quoteEntity = quoteRepository.findById(quoteId);
            if (quoteEntity.isPresent()) {
                validateQuoteId(quoteLeId, customersSet, customerLeIds, quoteEntity.get(), httpMethod);
            } else {
                LOGGER.info("Quote is not found");
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
            }
        }
        return joinPoint.proceed();
    }

    /**
     * validateOrderId
     *
     * @param orderLeId
     * @param customersSet
     * @param customerLeIds
     * @param method
     * @throws TclCommonException
     */
    private void validateOrderId(Integer orderLeId, Set<Integer> customersSet, Set<Integer> customerLeIds,
                                 com.tcl.dias.oms.entity.entities.Order order, String method) throws TclCommonException {
        Integer customerId = order.getCustomer().getId();
        boolean isValidated = false;
        if (customersSet.contains(customerId) || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
            isValidated = true;
        }
        if (isValidated && orderLeId != null) {
            validateOrderLeId(orderLeId, customerLeIds, method);
        }
        if (!isValidated) {
            LOGGER.info("Unauthorized access for order {}", order.getId());
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
        }
    }

    private void validateQuoteId(Integer quoteLeId, Set<Integer> customersSet, Set<Integer> customerLeIds,
                                 com.tcl.dias.oms.entity.entities.Quote quote, String method) throws TclCommonException {
        Integer customerId = quote.getCustomer().getId();
        boolean isValidated = false;
        if (customersSet.contains(customerId) || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
            isValidated = true;
        }
        if (isValidated && quoteLeId != null) {
            validateQuoteLeId(quoteLeId, customerLeIds, quote, method);
            validateDocusignSentOrNot(quote, method);
        }

        if (!isValidated) {
            LOGGER.info("Unauthorized access for quote {}", quote.getId());
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
        }
    }

    /**
     * validateOrderLeId
     *
     * @param orderLeId
     * @param customerLeIds
     * @throws TclCommonException
     */
    private void validateOrderLeId(Integer orderLeId, Set<Integer> customerLeIds, String method)
            throws TclCommonException {
        boolean isValidated;
        Optional<OrderToLe> orderLeEntity = orderToLeRepository.findById(orderLeId);
        if (orderLeEntity.isPresent()) {
            if (customerLeIds.contains(orderLeEntity.get().getErfCusCustomerLegalEntityId())
                    || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                isValidated = true;
                if (orderLeEntity.get().getStage().equals(OrderStagingConstants.ORDER_COMPLETED.toString())
                        && !method.equals(RequestMethod.GET.toString())) {
                    isValidated = false;
                }
            } else {
                isValidated = false;
            }
        } else {
            isValidated = false;
        }
        if (!isValidated) {
            LOGGER.info("Unauthorized access for orderLeId {}", orderLeId);
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
        }
    }

    private void validateQuoteLeId(Integer quoteLeId, Set<Integer> customerLeIds, Quote quote, String method)
            throws TclCommonException {
        boolean isValidated;
        Optional<QuoteToLe> quoteLeEntity = quoteToLeRepository.findById(quoteLeId);
        if (quoteLeEntity.isPresent()) {
            Integer cuLeId = quoteLeEntity.get().getErfCusCustomerLegalEntityId();
            if (cuLeId == null || customerLeIds.contains(quoteLeEntity.get().getErfCusCustomerLegalEntityId())
                    || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                isValidated = true;
                if (!method.equals(RequestMethod.GET.toString())) {
                    com.tcl.dias.oms.entity.entities.Order orders = orderRepository.findByQuoteAndStatus(quote,
                            CommonConstants.BACTIVE);
                    if (orders != null) {
                        isValidated = false;
                    }
                }
            } else {
                isValidated = false;
            }
        } else {
            isValidated = false;
        }
        if (!isValidated) {
            LOGGER.info("Unauthorized access for quoteLeId {}", quoteLeId);
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
        }
    }

    /**
     * getMapperCustomerDetails
     *
     * @param customerDetails
     * @param customersSet
     * @param customerLeIds
     */
    private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
                                          Set<Integer> customerLeIds) {
        for (CustomerDetail customerDetail : customerDetails) {
            customersSet.add(customerDetail.getCustomerId());
            customerLeIds.add(customerDetail.getCustomerLeId());
        }
    }

    private void validateDocusignSentOrNot(Quote quote, String method) throws TclCommonException {
        boolean isValidated = true;
        if (quote.getQuoteCode() != null && !method.equals(RequestMethod.GET.toString())) {
            DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
            if (docusignAudit != null) {
                isValidated = false;
            }
        }
        if (!isValidated) {
            LOGGER.info("Unauthorized access for quoteCode because docusign had already been sent {}",
                    quote.getQuoteCode());
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
        }
    }
}
