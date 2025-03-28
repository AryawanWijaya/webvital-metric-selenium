package com.ary.qa;

import com.ary.qa.constant.PerformanceMetric;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.ary.qa.util.PerformanceAudit;
import com.aventstack.extentreports.Status;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;

public class JsPerformanceMetric extends BaseClass {
  PerformanceAudit audit = new PerformanceAudit();
  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();

  @Parameters({"url"})
  @Test
  public void auditYoutubeUsingJs(@Optional("https://www.google.com/")String url) throws FileNotFoundException {
    String templateReport = fileUtil.readHtml(
        System.getProperty("user.dir") + "/src/test/resources/PerformanceTableReportTemplate.html");
    driver.get(url);
    double lcp = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.LCP);
    double fcp = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.FCP);
    double cls = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.CLS);

//    click search textbox
    driver.findElement(By.xpath(".//input[@name='search_query']")).click();
    double fid = audit.getPerformanceMetricByJsScript(jsExecutor, PerformanceMetric.FID);

    StringBuilder rows = new StringBuilder();
    rows.append("<tr><td>FCP</td><td>").append(String.format("%.5f .s", fcp)).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(String.format("%.5f .s", lcp)).append("</td></tr>");
    rows.append("<tr><td>FID</td><td>").append(String.format("%.5f .ms", fid)).append("</td></tr>");
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
    assertThat("FID is bigger than target", cls, Matchers.lessThan(200.0));
    extentTest.pass("FID is lesser than target");
    assertThat("FCP is bigger than target", fcp, Matchers.lessThan(0.0));
    extentTest.pass("FCP is lesser than target");
  }
}
