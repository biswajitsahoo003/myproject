package com.tcl.dias.oms.gsc.validation;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

import java.util.Objects;

import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;

import io.vavr.control.Validation;

public class SipAttributeValidator {

	public static Validation<String, OrderProductComponentsAttributeValue> validateDtmfRelaySupport(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("DTMF Relay support"))) {
			return invalid(String.format("DTMF Relay support cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateSupportedSIPPrivacyHeaders(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("Supported SIP Privacy headers"))) {
			return invalid(String.format("Supported SIP Privacy headers cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateSessionKeepAliveTimer(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("Session Keep Alive Timer"))) {
			return invalid(String.format("Session Keep Alive Timer cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validatePrefixAddition(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Prefix addition"))) {
			return invalid(String.format("Prefix addition cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateCustomerPublicIP(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Customer Public IP"))) {
			return invalid(String.format("Customer Public IP cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateTransport(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Transport"))) {
			return invalid(
					String.format("Transport cannot be empty : %s", orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateCodec(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Codec"))) {
			return invalid(String.format("Codec cannot be empty : %s", orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateNoOfConcurrentChannel(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("No Of Concurrent channel"))) {
			return invalid(String.format("No Of Concurrent channel cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateEquipmentAddress(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Equipment address"))) {
			return invalid(String.format("Equipment address cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateRoutingTopology(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Routing Topology"))) {
			return invalid(String.format("Routing Topology cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateDialPlanLogic(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("Dial plan logic (Prefix or CLI)"))) {
			return invalid(String.format("Dial plan logic (Prefix or CLI) cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateCallsPerSecond(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("Calls Per Second (CPS)"))) {
			return invalid(String.format("Calls Per Second (CPS) cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateCertificateAuthoritySupport(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("Certificate Authority Support"))) {
			return invalid(String.format("Certificate Authority Support cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateCustomerPublicIPorFQDN(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(orderProductComponentsAttributeValue.getAttributeValues()
				.equalsIgnoreCase("Customer Public IP or FQDN"))) {
			return invalid(String.format("Customer Public IP or FQDN cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

	public static Validation<String, OrderProductComponentsAttributeValue> validateIPAddressSpace(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		if (Objects.nonNull(
				orderProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("IP Address Space"))) {
			return invalid(String.format("IP Address Space cannot be empty : %s",
					orderProductComponentsAttributeValue.getId()));
		} else {
			return valid(orderProductComponentsAttributeValue);
		}
	}

}
