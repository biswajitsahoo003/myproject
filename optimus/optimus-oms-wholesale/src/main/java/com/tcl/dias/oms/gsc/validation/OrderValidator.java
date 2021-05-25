package com.tcl.dias.oms.gsc.validation;

import com.google.common.base.Strings;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import io.vavr.control.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

@Component
public class OrderValidator {

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    public static Validation<String, Order> validateOrderCode(Order order) {
        if (Strings.isNullOrEmpty(order.getOrderCode())) {
            return invalid("Order code cannot be empty");
        } else {
            return valid(order);
        }
    }

    public static Validation<String, Order> validateTermInMonths(Order order) {
        if (Objects.isNull(order.getTermInMonths())) {
            return invalid("Term in months cannot be empty or zero");
        } else {
            return valid(order);
        }
    }

    public static Validation<String, OrderToLe> validateOrderToLeCustLeId(OrderToLe orderToLe) {
        if (Objects.nonNull(orderToLe.getErfCusCustomerLegalEntityId())) {
            return invalid(
                    String.format("Customer legal entity id cannot be empty for ordertole: %s", orderToLe.getId()));
        } else {
            return valid(orderToLe);
        }
    }

    public static Validation<String, OrderToLe> validateOrderToLeSpLeId(OrderToLe orderToLe) {
        if (Objects.nonNull(orderToLe.getErfCusSpLegalEntityId())) {
            return invalid(
                    String.format("Supplier legal entity id cannot be empty for ordertole: %s", orderToLe.getId()));
        } else {
            return valid(orderToLe);
        }
    }

    public static Validation<String, OrderGsc> validateMrc(OrderGsc orderGsc) {
        if (Objects.isNull(orderGsc.getMrc())) {
            return invalid(String.format("MRC cannot be null for ordergsc: %s", orderGsc.getId()));
        } else {
            return valid(orderGsc);
        }
    }

    public static Validation<String, OrderGsc> validateArc(OrderGsc orderGsc) {
        if (Objects.isNull(orderGsc.getArc())) {
            return invalid(String.format("ARC cannot be null for ordergsc: %s", orderGsc.getId()));
        } else {
            return valid(orderGsc);
        }
    }

    public static Validation<String, OrderGsc> validateNrc(OrderGsc orderGsc) {
        if (Objects.isNull(orderGsc.getNrc())) {
            return invalid(String.format("NRC cannot be null for ordergsc: %s", orderGsc.getId()));
        } else {
            return valid(orderGsc);
        }
    }

    public static Validation<String, OrderGsc> validateSla(OrderGsc orderGsc) {
        if (CollectionUtils.isEmpty(orderGsc.getOrderGscSlas())) {
            return invalid(String.format("Atleast one SLA shoud be present for orderGsc: %s", orderGsc.getId()));
        } else {
            return valid(orderGsc);
        }
    }

    public List<String> validateOrderProductComponentAttributeValue(
            OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
        return DataValidator.validate(orderProductComponentsAttributeValue,
                SipAttributeValidator::validateDtmfRelaySupport,
                SipAttributeValidator::validateSupportedSIPPrivacyHeaders,
                SipAttributeValidator::validateSessionKeepAliveTimer, SipAttributeValidator::validatePrefixAddition,
                SipAttributeValidator::validateCustomerPublicIP, SipAttributeValidator::validateTransport,
                SipAttributeValidator::validateCodec, SipAttributeValidator::validateNoOfConcurrentChannel,
                SipAttributeValidator::validateEquipmentAddress, SipAttributeValidator::validateRoutingTopology,
                SipAttributeValidator::validateDialPlanLogic, SipAttributeValidator::validateCallsPerSecond,
                SipAttributeValidator::validateCertificateAuthoritySupport,
                SipAttributeValidator::validateCustomerPublicIPorFQDN, SipAttributeValidator::validateIPAddressSpace);
    }

    public List<String> validateOrderProductComponent(OrderProductComponent orderProductComponent) {
        List<String> errors = new ArrayList<>();

        List<String> orderProductComponentErrors = DataValidator.validate(orderProductComponent, null);
        errors.addAll(orderProductComponentErrors);

        List<String> productComponentAttributeErrors = orderProductComponentsAttributeValueRepository
                .findByOrderProductComponent(orderProductComponent).stream()
                .map(this::validateOrderProductComponentAttributeValue).flatMap(List::stream)
                .collect(Collectors.toList());
        errors.addAll(productComponentAttributeErrors);
        return errors;
    }

    public List<String> validateOrderGscDetail(OrderGscDetail orderGscDetail) {
        List<String> errors = new ArrayList<>();

        List<String> gscDetailErrors = DataValidator.validate(orderGscDetail, null);
        errors.addAll(gscDetailErrors);

        List<String> productComponentErrors = orderProductComponentRepository.findByReferenceId(orderGscDetail.getId())
                .stream().map(this::validateOrderProductComponent).flatMap(List::stream).collect(Collectors.toList());
        errors.addAll(productComponentErrors);
        return errors;
    }

    public List<String> validateOrderGsc(OrderGsc orderGsc) {
        List<String> errors = new ArrayList<>();

        List<String> gscErrors = DataValidator.validate(orderGsc, OrderValidator::validateArc,
                OrderValidator::validateMrc, OrderValidator::validateNrc, OrderValidator::validateSla);
        errors.addAll(gscErrors);

        List<String> gscDetailErrors = orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                .map(this::validateOrderGscDetail).flatMap(List::stream).collect(Collectors.toList());
        errors.addAll(gscDetailErrors);
        return errors;
    }

    public List<String> validateOrderProductSolution(OrderProductSolution solution) {
        List<String> errors = new ArrayList<>();
        List<String> gscErrors = orderGscRepository.findByorderProductSolution(solution).stream()
                .map(this::validateOrderGsc).flatMap(List::stream).collect(Collectors.toList());
        errors.addAll(gscErrors);
        return errors;
    }

    public List<String> validateOrderToLeProductFamily(OrderToLeProductFamily productFamily) {
        List<String> errors = new ArrayList<>();
        List<String> solutionErrors = productFamily.getOrderProductSolutions().stream()
                .map(this::validateOrderProductSolution).flatMap(List::stream).collect(Collectors.toList());
        errors.addAll(solutionErrors);
        return errors;
    }

    public List<String> validateOrderToLe(OrderToLe orderToLe) {
        List<String> errors = new ArrayList<>();
        List<String> orderToLeErrors = DataValidator.validate(orderToLe, OrderValidator::validateOrderToLeCustLeId,
                OrderValidator::validateOrderToLeSpLeId);
        errors.addAll(orderToLeErrors);
        List<String> productFamilyErrors = orderToLe.getOrderToLeProductFamilies().stream()
                .map(this::validateOrderToLeProductFamily).flatMap(List::stream).collect(Collectors.toList());
        errors.addAll(productFamilyErrors);
        return errors;
    }

    public List<String> validateOrder(Order order) {
        List<String> validationErrors = new ArrayList<>();
        List<String> orderErrors = DataValidator.validate(order, OrderValidator::validateOrderCode,
                OrderValidator::validateTermInMonths);
        validationErrors.addAll(orderErrors);
        List<String> orderToLeErrors = order.getOrderToLes().stream().map(this::validateOrderToLe).flatMap(List::stream)
                .collect(Collectors.toList());
        validationErrors.addAll(orderToLeErrors);
        return validationErrors;
    }

}
