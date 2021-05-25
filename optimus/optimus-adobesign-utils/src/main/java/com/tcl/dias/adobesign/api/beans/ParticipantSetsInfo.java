
package com.tcl.dias.adobesign.api.beans;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "memberInfos",
    "order",
    "role",
    "id",
    "label",
    "name",
    "privateMessage",
    "visiblePages"
})
public class ParticipantSetsInfo {

    @JsonProperty("memberInfos")
    private List<MemberInfo> memberInfos = null;
    @JsonProperty("order")
    private Integer order;
    @JsonProperty("role")
    private String role;
    @JsonProperty("id")
    private String id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("name")
    private String name;
    @JsonProperty("privateMessage")
    private String privateMessage;
    @JsonProperty("visiblePages")
    private List<String> visiblePages = null;

    @JsonProperty("memberInfos")
    public List<MemberInfo> getMemberInfos() {
        return memberInfos;
    }

    @JsonProperty("memberInfos")
    public void setMemberInfos(List<MemberInfo> memberInfos) {
        this.memberInfos = memberInfos;
    }

    @JsonProperty("order")
    public Integer getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(Integer order) {
        this.order = order;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("privateMessage")
    public String getPrivateMessage() {
        return privateMessage;
    }

    @JsonProperty("privateMessage")
    public void setPrivateMessage(String privateMessage) {
        this.privateMessage = privateMessage;
    }

    @JsonProperty("visiblePages")
    public List<String> getVisiblePages() {
        return visiblePages;
    }

    @JsonProperty("visiblePages")
    public void setVisiblePages(List<String> visiblePages) {
        this.visiblePages = visiblePages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("memberInfos", memberInfos).append("order", order).append("role", role).append("id", id).append("label", label).append("name", name).append("privateMessage", privateMessage).append("visiblePages", visiblePages).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(role).append(visiblePages).append(name).append(id).append(label).append(privateMessage).append(memberInfos).append(order).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ParticipantSetsInfo) == false) {
            return false;
        }
        ParticipantSetsInfo rhs = ((ParticipantSetsInfo) other);
        return new EqualsBuilder().append(role, rhs.role).append(visiblePages, rhs.visiblePages).append(name, rhs.name).append(id, rhs.id).append(label, rhs.label).append(privateMessage, rhs.privateMessage).append(memberInfos, rhs.memberInfos).append(order, rhs.order).isEquals();
    }

}
