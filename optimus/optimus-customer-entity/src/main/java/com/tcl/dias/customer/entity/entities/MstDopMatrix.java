package com.tcl.dias.customer.entity.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * Entity Class
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_dop_matrix")
@NamedQuery(name = "MstDopMatrix.findAll", query = "SELECT m FROM MstDopMatrix m")
public class MstDopMatrix implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region")
    private String region;

    @Column(name = "level_one_name")
    private String levelOneName;

    @Column(name = "level_one_email")
    private String levelOneEmail;

    @Column(name = "level_two_name")
    private String levelTwoName;

    @Column(name = "level_two_email")
    private String levelTwoEmail;

    @Column(name = "level_three_name")
    private String levelThreeName;

    @Column(name = "level_three_email")
    private String levelThreeEmail;

    // bi-directional many-to-one association to MstCustomerSegment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_segment")
    private MstCustomerSegment customerSegment;

    @Column(name = "dop_signing_level")
    private String dopSigningLevel;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.DATE)
    private Date createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLevelOneName() {
        return levelOneName;
    }

    public void setLevelOneName(String levelOneName) {
        this.levelOneName = levelOneName;
    }

    public String getLevelOneEmail() {
        return levelOneEmail;
    }

    public void setLevelOneEmail(String levelOneEmail) {
        this.levelOneEmail = levelOneEmail;
    }

    public String getLevelTwoName() {
        return levelTwoName;
    }

    public void setLevelTwoName(String levelTwoName) {
        this.levelTwoName = levelTwoName;
    }

    public String getLevelTwoEmail() {
        return levelTwoEmail;
    }

    public void setLevelTwoEmail(String levelTwoEmail) {
        this.levelTwoEmail = levelTwoEmail;
    }

    public String getLevelThreeName() {
        return levelThreeName;
    }

    public void setLevelThreeName(String levelThreeName) {
        this.levelThreeName = levelThreeName;
    }

    public String getLevelThreeEmail() {
        return levelThreeEmail;
    }

    public void setLevelThreeEmail(String levelThreeEmail) {
        this.levelThreeEmail = levelThreeEmail;
    }

    public MstCustomerSegment getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(MstCustomerSegment customerSegment) {
        this.customerSegment = customerSegment;
    }

    public String getDopSigningLevel() {
        return dopSigningLevel;
    }

    public void setDopSigningLevel(String dopSigningLevel) {
        this.dopSigningLevel = dopSigningLevel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() { return createdTime; }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
