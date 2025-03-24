package com.ary.qa.model.lighthouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LighthouseMain {
  private String lighthouseVersion;
  private String requestedUrl;
  private String finalUrl;
  private String fetchTime;
  private String gatherMode;
  private List<String> runWarnings;
  private String userAgent;
  private Audits audits;
}
