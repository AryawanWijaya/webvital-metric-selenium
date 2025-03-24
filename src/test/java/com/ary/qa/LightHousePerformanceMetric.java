package com.ary.qa;

import com.ary.qa.model.lighthouse.LighthouseMain;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.ary.qa.util.PerformanceAudit;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LightHousePerformanceMetric extends BaseClass {
  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();
  PerformanceAudit performanceAudit = new PerformanceAudit();

  @Test
  public void auditYoutubeUsingLightHouse() throws IOException, InterruptedException {
    String url = "https://www.google.com/";
    driver.get(url);
    String reportName = "PerformanceYoutubeLighthouse";
    driver.getWindowHandles().forEach(window -> {
      System.out.println(window);
    });

    performanceAudit.lighthouseAudit("https://www.youtube.com/", reportName, "json", true);
    mapResponseAndGenerateReport(reportName);

    String report =
        fileUtil.readHtml(System.getProperty("user.dir") + "/target/" + reportName + "Report.html");
    extentTest.log(Status.INFO, report);
  }

  private void mapResponseAndGenerateReport(String jsonReportName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String dir = System.getProperty("user.dir") + "/target/" + jsonReportName + ".json";
    LighthouseMain main = mapper.readValue(new FileReader(dir), LighthouseMain.class);

    String template = fileUtil.readHtml(
        System.getProperty("user.dir") + "/src/test/resources/LighthouseTemplateTable.html");
    StringBuilder rows = new StringBuilder();
    String fcp =
        String.format("%.3f %s", main.getAudits().getFcp().getNumericValue() / 1000, " .s");
    String lcp =
        String.format("%.3f %s", main.getAudits().getLcp().getNumericValue() / 1000, " .s");
    String cls = String.format("%.5f", main.getAudits().getCls().getNumericValue() / 1000);
    rows.append("<tr><td>FCP</td><td>").append(fcp).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(lcp).append("</td></tr>");
    rows.append("<tr><td>CLS</td><td>").append(cls).append("</td></tr>");

    template = template.replace("<!--replace_here-->", rows.toString());
    template = template.replace("{title}", "Lighthouse");
    fileUtil.writeToHtml(jsonReportName + "Report", template);

  }
}
