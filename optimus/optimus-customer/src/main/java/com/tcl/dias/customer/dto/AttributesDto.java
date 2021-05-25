package com.tcl.dias.customer.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.PartnerLeAttributeValue;

/**
 * 
 * This file contains the Attributes Information
 * 
 *
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class AttributesDto {

	private String attributeName;

	private String attributeValue;

	private String type;

	private List<BillingBean> billing = new ArrayList<BillingBean>();

	private List<AttachmentBean> attachments = new ArrayList<AttachmentBean>();

	private List<AddressBean> address = new ArrayList<AddressBean>();

	private List<ContactBean> contact = new ArrayList<ContactBean>();

	private List<ManagerBean> manager = new ArrayList<ManagerBean>();

	private static final String BILLING = "billing";
	private static final String ATTACHMENT = "attachment";
	private static final String ADDRESS = "address";
	private static final String CONTACT = "contact";
	private static final String MANAGER = "manager";

	public AttributesDto() {
		// do nothing
	}

	public AttributesDto(CustomerLeAttributeValue customerLeAttributeValue) {
		if (!Objects.isNull(customerLeAttributeValue)) {
			this.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
			this.setAttributeValue(customerLeAttributeValue.getAttributeValues());
			this.setType(customerLeAttributeValue.getMstLeAttribute().getType());
		}
	}

	public AttributesDto(Set<CustomerLeAttributeValue> custLeAttributes) {
		custLeAttributes.stream().forEach(customerLeAttributeValue -> {
			if (!Objects.isNull(customerLeAttributeValue) && customerLeAttributeValue.getMstLeAttribute() != null
					&& customerLeAttributeValue.getMstLeAttribute().getType() != null) {

				switch (customerLeAttributeValue.getMstLeAttribute().getType().toLowerCase()) {
				case BILLING:
					BillingBean bean = new BillingBean();
					bean.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					bean.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					billing.add(bean);
					this.setBilling(billing);
					break;
				case ATTACHMENT:
					AttachmentBean attachment = new AttachmentBean();
					attachment.setAttributeName(customerLeAttributeValue.getProductName() + CommonConstants.HYPHEN
							+ customerLeAttributeValue.getMstLeAttribute().getName());
					attachment.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					attachments.add(attachment);
					this.setAttachments(attachments);
					break;
				case ADDRESS:
					AddressBean addr = new AddressBean();
					addr.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					addr.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					address.add(addr);
					this.setAddress(address);
					break;
				case CONTACT:
					ContactBean cont = new ContactBean();
					cont.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					cont.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					contact.add(cont);
					this.setContact(contact);
					break;
				case MANAGER:
					ManagerBean managerBean = new ManagerBean();
					managerBean.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					managerBean.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					manager.add(managerBean);
					this.setManager(manager);
					break;
				}
			}
		});
		if (!billing.isEmpty()) {
			this.setBilling(billing);
		}
		if (!attachments.isEmpty()) {
			this.setAttachments(attachments);
		}
		if (!address.isEmpty()) {
			this.setAddress(address);
		}
		if (!contact.isEmpty()) {
			this.setContact(contact);
		}
		if (!manager.isEmpty()) {
			this.setManager(manager);
		}
	}

	public AttributesDto(Set<PartnerLeAttributeValue> partnerLeAttributes,String partner) {
		partnerLeAttributes.stream().forEach(partnetLeAttributeValue -> {
			if (!Objects.isNull(partnetLeAttributeValue) && partnetLeAttributeValue.getMstLeAttribute() != null
					&& partnetLeAttributeValue.getMstLeAttribute().getType() != null) {

				switch (partnetLeAttributeValue.getMstLeAttribute().getType().toLowerCase()) {
					case BILLING:
						BillingBean bean = new BillingBean();
						bean.setAttributeName(partnetLeAttributeValue.getMstLeAttribute().getName());
						bean.setAttributeValue(partnetLeAttributeValue.getAttributeValues());
						billing.add(bean);
						this.setBilling(billing);
						break;
					case ATTACHMENT:
						AttachmentBean attachment = new AttachmentBean();
						attachment.setAttributeName(partnetLeAttributeValue.getProductName() + CommonConstants.HYPHEN
								+ partnetLeAttributeValue.getMstLeAttribute().getName());
						attachment.setAttributeValue(partnetLeAttributeValue.getAttributeValues());
						attachments.add(attachment);
						this.setAttachments(attachments);
						break;
					case ADDRESS:
						AddressBean addr = new AddressBean();
						addr.setAttributeName(partnetLeAttributeValue.getMstLeAttribute().getName());
						addr.setAttributeValue(partnetLeAttributeValue.getAttributeValues());
						address.add(addr);
						this.setAddress(address);
						break;
					case CONTACT:
						ContactBean cont = new ContactBean();
						cont.setAttributeName(partnetLeAttributeValue.getMstLeAttribute().getName());
						cont.setAttributeValue(partnetLeAttributeValue.getAttributeValues());
						contact.add(cont);
						this.setContact(contact);
						break;
					case MANAGER:
						ManagerBean managerBean = new ManagerBean();
						managerBean.setAttributeName(partnetLeAttributeValue.getMstLeAttribute().getName());
						managerBean.setAttributeValue(partnetLeAttributeValue.getAttributeValues());
						manager.add(managerBean);
						this.setManager(manager);
						break;
				}
			}
		});
		if (!billing.isEmpty()) {
			this.setBilling(billing);
		}
		if (!attachments.isEmpty()) {
			this.setAttachments(attachments);
		}
		if (!address.isEmpty()) {
			this.setAddress(address);
		}
		if (!contact.isEmpty()) {
			this.setContact(contact);
		}
		if (!manager.isEmpty()) {
			this.setManager(manager);
		}
	}

	public AttributesDto(Set<CustomerLeAttributeValue> custLeAttributes, List<AttachmentDto> attachmentDto) {
		custLeAttributes.stream().forEach(customerLeAttributeValue -> {
			if (!Objects.isNull(customerLeAttributeValue)) {
				switch (customerLeAttributeValue.getMstLeAttribute().getType().toLowerCase()) {
				case BILLING:
					BillingBean billingBean = new BillingBean();
					billingBean.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					billingBean.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					this.billing.add(billingBean);
					break;
				case ATTACHMENT:
					AttachmentBean attachment = new AttachmentBean();
					attachment.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					attachment.setAttributeValue(customerLeAttributeValue.getAttributeValues());

					attachmentDto.stream().forEach(eachAttachment -> {
						if (String.valueOf(eachAttachment.getId())
								.equalsIgnoreCase(customerLeAttributeValue.getAttributeValues())) {
							attachment.setAttachmentName(eachAttachment.getName());
							attachment.setAttachmentDisplayName(eachAttachment.getDisplayName());
							attachment.setUriPath(eachAttachment.getUriPath());
						}
					});
					this.attachments.add(attachment);
					break;
				case ADDRESS:
					AddressBean addr = new AddressBean();
					addr.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					addr.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					this.address.add(addr);
					break;
				case CONTACT:
					ContactBean cont = new ContactBean();
					cont.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					cont.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					contact.add(cont);
					this.setContact(contact);
					break;
				case MANAGER:
					ManagerBean managerBean = new ManagerBean();
					managerBean.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
					managerBean.setAttributeValue(customerLeAttributeValue.getAttributeValues());
					manager.add(managerBean);
					this.setManager(manager);
					break;
				}
			}
		});
		if (!billing.isEmpty()) {
			this.setBilling(billing);
		}
		if (!attachments.isEmpty()) {
			this.setAttachments(attachments);
		}
		if (!address.isEmpty()) {
			this.setAddress(address);
		}
		if (!contact.isEmpty()) {
			this.setContact(contact);
		}
		if (!manager.isEmpty()) {
			this.setManager(manager);
		}
	}

	public List<BillingBean> getBilling() {
		return billing;
	}

	public void setBilling(List<BillingBean> billing) {
		this.billing = billing;
	}

	public List<AttachmentBean> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentBean> attachments) {
		this.attachments = attachments;
	}

	public List<AddressBean> getAddress() {
		return address;
	}

	public void setAddress(List<AddressBean> address) {
		this.address = address;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public List<ContactBean> getContact() {
		return contact;
	}

	public void setContact(List<ContactBean> contact) {
		this.contact = contact;
	}

	public List<ManagerBean> getManager() {
		return manager;
	}

	public void setManager(List<ManagerBean> manager) {
		this.manager = manager;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}