package com.ary.qa;

import com.ary.qa.listener.BaseListener;
import com.ary.qa.model.lighthouse.LighthouseMain;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.ary.qa.util.PerformanceAudit;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;

@Listeners({BaseListener.class})
public class LightHousePerformanceMetric extends BaseClass {
  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();
  PerformanceAudit performanceAudit = new PerformanceAudit();

  @Parameters({"url"})
  @Test
  public void auditYoutubeUsingLightHouse(@Optional("https://www.google.com/")String url) throws IOException, InterruptedException {
    driver.get(url);
    String reportName = "PerformanceYoutubeLighthouse";

    performanceAudit.lighthouseAudit(url, reportName, "json", true);
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
        System.getProperty("user.dir") + "/src/test/resources/PerformanceTableReportTemplate.html");
    StringBuilder rows = new StringBuilder();
    String fcp =
        String.format("%.3f %s", main.getAudits().getFcp().getNumericValue() / 1000, " .s");
    String lcp =
        String.format("%.3f %s", main.getAudits().getLcp().getNumericValue() / 1000, " .s");
    String cls = String.format("%.5f", main.getAudits().getCls().getNumericValue());
    String tbt =
        String.format("%.3f %s", main.getAudits().getTbt().getNumericValue() / 1000, " .s");
    String speedIndex =
        String.format("%.3f %s", main.getAudits().getSpeedIndex().getNumericValue() / 1000, " .s");
    rows.append("<tr><td>FCP</td><td>").append(fcp).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(lcp).append("</td></tr>");
    rows.append("<tr><td>CLS</td><td>").append(cls).append("</td></tr>");
    rows.append("<tr><td>TBT</td><td>").append(tbt).append("</td></tr>");
    rows.append("<tr><td>Speed Index</td><td>").append(speedIndex).append("</td></tr>");

    template = template.replace("<!--replace_here-->", rows.toString());
    template = template.replace("{title}", "Lighthouse");
    fileUtil.writeToHtml(jsonReportName + "Report", template);
  }
}
