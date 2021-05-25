
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "sla-profile",
    "circuit-priorities",
    "connection-selection-method",
    "sla-violation-action",
    "evaluate-continuously",
    "recompute-timer",
    "encryption",
    "symmetric-forwarding",
    "turn-redirect",
    "replication",
    "fec",
    "sla-smoothing",
    "sla-dampening",
    "load-balance",
    "gradual-migration",
    "path-reconsider-interval"
})
public class ForwardingProfile_ implements Serializable
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("sla-profile")
    private String slaProfile;
    @JsonProperty("circuit-priorities")
    private CircuitPriorities circuitPriorities;
    @JsonProperty("connection-selection-method")
    private String connectionSelectionMethod;
    @JsonProperty("sla-violation-action")
    private String slaViolationAction;
    @JsonProperty("evaluate-continuously")
    private String evaluateContinuously;
    @JsonProperty("recompute-timer")
    private String recomputeTimer;
    @JsonProperty("encryption")
    private String encryption;
    @JsonProperty("symmetric-forwarding")
    private String symmetricForwarding;
    @JsonProperty("turn-redirect")
    private String turnRedirect;
    @JsonProperty("replication")
    private Replication replication;
    @JsonProperty("fec")
    private Fec fec;
    @JsonProperty("sla-smoothing")
    private SlaSmoothing slaSmoothing;
    @JsonProperty("sla-dampening")
    private SlaDampening slaDampening;
    @JsonProperty("load-balance")
    private String loadBalance;
    @JsonProperty("gradual-migration")
    private String gradualMigration;
    @JsonProperty("path-reconsider-interval")
    private String pathReconsiderInterval;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1510311209190646280L;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("sla-profile")
    public String getSlaProfile() {
        return slaProfile;
    }

    @JsonProperty("sla-profile")
    public void setSlaProfile(String slaProfile) {
        this.slaProfile = slaProfile;
    }

    @JsonProperty("circuit-priorities")
    public CircuitPriorities getCircuitPriorities() {
        return circuitPriorities;
    }

    @JsonProperty("circuit-priorities")
    public void setCircuitPriorities(CircuitPriorities circuitPriorities) {
        this.circuitPriorities = circuitPriorities;
    }

    @JsonProperty("connection-selection-method")
    public String getConnectionSelectionMethod() {
        return connectionSelectionMethod;
    }

    @JsonProperty("connection-selection-method")
    public void setConnectionSelectionMethod(String connectionSelectionMethod) {
        this.connectionSelectionMethod = connectionSelectionMethod;
    }

    @JsonProperty("sla-violation-action")
    public String getSlaViolationAction() {
        return slaViolationAction;
    }

    @JsonProperty("sla-violation-action")
    public void setSlaViolationAction(String slaViolationAction) {
        this.slaViolationAction = slaViolationAction;
    }

    @JsonProperty("evaluate-continuously")
    public String getEvaluateContinuously() {
        return evaluateContinuously;
    }

    @JsonProperty("evaluate-continuously")
    public void setEvaluateContinuously(String evaluateContinuously) {
        this.evaluateContinuously = evaluateContinuously;
    }

    @JsonProperty("recompute-timer")
    public String getRecomputeTimer() {
        return recomputeTimer;
    }

    @JsonProperty("recompute-timer")
    public void setRecomputeTimer(String recomputeTimer) {
        this.recomputeTimer = recomputeTimer;
    }

    @JsonProperty("encryption")
    public String getEncryption() {
        return encryption;
    }

    @JsonProperty("encryption")
    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    @JsonProperty("symmetric-forwarding")
    public String getSymmetricForwarding() {
        return symmetricForwarding;
    }

    @JsonProperty("symmetric-forwarding")
    public void setSymmetricForwarding(String symmetricForwarding) {
        this.symmetricForwarding = symmetricForwarding;
    }

    @JsonProperty("turn-redirect")
    public String getTurnRedirect() {
        return turnRedirect;
    }

    @JsonProperty("turn-redirect")
    public void setTurnRedirect(String turnRedirect) {
        this.turnRedirect = turnRedirect;
    }

    @JsonProperty("replication")
    public Replication getReplication() {
        return replication;
    }

    @JsonProperty("replication")
    public void setReplication(Replication replication) {
        this.replication = replication;
    }

    @JsonProperty("fec")
    public Fec getFec() {
        return fec;
    }

    @JsonProperty("fec")
    public void setFec(Fec fec) {
        this.fec = fec;
    }

    @JsonProperty("sla-smoothing")
    public SlaSmoothing getSlaSmoothing() {
        return slaSmoothing;
    }

    @JsonProperty("sla-smoothing")
    public void setSlaSmoothing(SlaSmoothing slaSmoothing) {
        this.slaSmoothing = slaSmoothing;
    }

    @JsonProperty("sla-dampening")
    public SlaDampening getSlaDampening() {
        return slaDampening;
    }

    @JsonProperty("sla-dampening")
    public void setSlaDampening(SlaDampening slaDampening) {
        this.slaDampening = slaDampening;
    }

    @JsonProperty("load-balance")
    public String getLoadBalance() {
        return loadBalance;
    }

    @JsonProperty("load-balance")
    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    @JsonProperty("gradual-migration")
    public String getGradualMigration() {
        return gradualMigration;
    }

    @JsonProperty("gradual-migration")
    public void setGradualMigration(String gradualMigration) {
        this.gradualMigration = gradualMigration;
    }

    @JsonProperty("path-reconsider-interval")
    public String getPathReconsiderInterval() {
        return pathReconsiderInterval;
    }

    @JsonProperty("path-reconsider-interval")
    public void setPathReconsiderInterval(String pathReconsiderInterval) {
        this.pathReconsiderInterval = pathReconsiderInterval;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
