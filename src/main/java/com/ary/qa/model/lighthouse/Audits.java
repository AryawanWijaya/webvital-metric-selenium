package com.ary.qa.model.lighthouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audits {
  @JsonProperty("first-contentful-paint")
  public MetricAudit fcp;

  @JsonProperty("largest-contentful-paint")
  public MetricAudit lcp;

  @JsonProperty("total-blocking-time")
  public MetricAudit tbt;

  @JsonProperty("speed-index")
  public MetricAudit speedIndex;

  @JsonProperty("cumulative-layout-shift")
  public MetricAudit cls;
}
