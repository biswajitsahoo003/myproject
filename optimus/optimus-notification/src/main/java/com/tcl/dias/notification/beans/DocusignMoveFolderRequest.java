package com.tcl.dias.notification.beans;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"envelopeIds",
"fromFolderId"
})
public class DocusignMoveFolderRequest {

@JsonProperty("envelopeIds")
private List<String> envelopeIds = null;
@JsonProperty("fromFolderId")
private String fromFolderId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("envelopeIds")
public List<String> getEnvelopeIds() {
return envelopeIds;
}

@JsonProperty("envelopeIds")
public void setEnvelopeIds(List<String> envelopeIds) {
this.envelopeIds = envelopeIds;
}

@JsonProperty("fromFolderId")
public String getFromFolderId() {
return fromFolderId;
}

@JsonProperty("fromFolderId")
public void setFromFolderId(String fromFolderId) {
this.fromFolderId = fromFolderId;
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
