package com.ary.qa.model.lighthouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricAudit {
  private String id;
  private String title;
  private String description;
  private double score;
  private Double numericValue;
  private String numericUnit;
  private String displayValue;
  private ScoringOptions scoringOptions;


  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ScoringOptions {
    private int p10;
    private int median;
  }

}
