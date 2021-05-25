package com.tcl.dias.servicefulfillment.entity.entities;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "planned_event")
@NamedQuery(name = "PlannedEvent.findAll", query = "select pe from PlannedEvent pe")
public class PlannedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlannedEvent() {
        // do nothing
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "planned_event_id")
    private String plannedEventId;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "mux_name")
    private String muxName;

    @Column(name = "optimus_status")
    private String optimusStatus;

    @Column(name = "planned_event_status")
    private String plannedEventStatus;

    @Column(name = "planned_event_conflict_status")
    private String plannedEventConflictStatus;

    @Column(name = "pre_fetched")
    private Boolean preFetched;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @Column(name = "error_status")
    private String errorStatus;

    public String getPlannedEventId() {
        return plannedEventId;
    }

    public void setPlannedEventId(String plannedEventId) {
        this.plannedEventId = plannedEventId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMuxName() {
        return muxName;
    }

    public void setMuxName(String muxName) {
        this.muxName = muxName;
    }

    public String getOptimusStatus() {
        return optimusStatus;
    }

    public void setOptimusStatus(String optimusStatus) {
        this.optimusStatus = optimusStatus;
    }

    public String getPlannedEventStatus() {
        return plannedEventStatus;
    }

    public void setPlannedEventStatus(String plannedEventStatus) {
        this.plannedEventStatus = plannedEventStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlannedEventConflictStatus() {
        return plannedEventConflictStatus;
    }

    public void setPlannedEventConflictStatus(String plannedEventConflictStatus) {
        this.plannedEventConflictStatus = plannedEventConflictStatus;
    }

    public Boolean getPreFetched() {
        return preFetched;
    }

    public void setPreFetched(Boolean preFetched) {
        this.preFetched = preFetched;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }
}

