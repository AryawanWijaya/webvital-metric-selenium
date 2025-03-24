package com.ary.qa;

import com.ary.qa.constant.PerformanceMetric;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.ary.qa.util.PerformanceAudit;
import com.aventstack.extentreports.Status;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;

public class JsPerformanceMetric extends BaseClass {
  PerformanceAudit audit = new PerformanceAudit();
  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();

  @Test
  public void auditYoutubeUsingJs() throws FileNotFoundException {
    String templateReport = fileUtil.readHtml(
        System.getProperty("user.dir") + "/src/test/resources/LighthouseTemplateTable.html");
    driver.get("https://www.youtube.com/");
    extentTest.pass("Success Open Youtube");
    double lcp = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.LCP);
    double fcp = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.FCP);
    double cls = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.CLS);

    StringBuilder rows = new StringBuilder();
    rows.append("<tr><td>FCP</td><td>").append(String.format("%.5f .s", fcp)).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(String.format("%.5f .s", lcp)).append("</td></tr>");
    rows.append("<tr><td>CLS</td><td>").append(String.format("%.5f", cls)).append("</td></tr>");
    templateReport = templateReport.replace("<!--replace_here-->", rows.toString());
    templateReport=templateReport.replace("{title}","Performance");
    String reportName = "youtubeJsAudit";
    fileUtil.writeToHtml(reportName, templateReport);
    String report =
        fileUtil.readHtml(System.getProperty("user.dir") + "/target/" + reportName + ".html");
    extentTest.log(Status.INFO, report);

    assertThat("LCP is bigger than target", lcp, Matchers.lessThan(2.5));
    extentTest.pass("LCP is lesser than target");
    assertThat("CLS is bigger than target", cls, Matchers.lessThan(0.1));
    extentTest.pass("CLS is lesser than target");
    assertThat("FCP is bigger than target", fcp, Matchers.lessThan(0.0));

  }
}
