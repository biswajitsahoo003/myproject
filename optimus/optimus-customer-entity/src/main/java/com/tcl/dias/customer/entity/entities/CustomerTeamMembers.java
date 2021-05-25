package com.tcl.dias.customer.entity.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * Entity Class
 *
 *
 * @author Suruchi A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer_team_members")
@NamedQuery(name = "CustomerTeamMembers.findAll", query = "SELECT c FROM CustomerTeamMembers c")
public class CustomerTeamMembers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "customer_id")
    private Customer customer;

    @Column(name = "tm_sfdc_code")
    private String tmSfdcCode;

    @Column(name = "tm_sfdc_id")
    private String tmSfdcId;

    @Column(name = "team_role")
    private String teamRole;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "is_team_member")
    private String isTeamMember;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "phone")
    private String phone;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subRegion;

    @Column(name = "created_by")
    private String crreatedBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "source")
    private String source;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTmSfdcCode() {
        return tmSfdcCode;
    }

    public void setTmSfdcCode(String tmSfdcCode) {
        this.tmSfdcCode = tmSfdcCode;
    }

    public String getTmSfdcId() {
        return tmSfdcId;
    }

    public void setTmSfdcId(String tmSfdcId) {
        this.tmSfdcId = tmSfdcId;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsTeamMember() {
        return isTeamMember;
    }

    public void setIsTeamMember(String isTeamMember) {
        this.isTeamMember = isTeamMember;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getCrreatedBy() {
        return crreatedBy;
    }

    public void setCrreatedBy(String crreatedBy) {
        this.crreatedBy = crreatedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
