package com.ary.qa;

import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.aventstack.extentreports.Status;
import org.hamcrest.Matchers;
import org.openqa.selenium.devtools.v132.performance.Performance;
import org.openqa.selenium.devtools.v132.performance.model.Metric;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

public class CdpPerformanceMetric extends BaseClass {
  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();

  @Parameters({"url"})
  @Test
  public void cdpPerformanceMetric(@org.testng.annotations.Optional("http://www.google.com/")String url) throws FileNotFoundException {
    devTools.send(Performance.enable(Optional.empty()));
    driver.get(url);
    List<Metric> metricList=devTools.send(Performance.getMetrics());
    String templateReport = fileUtil.readHtml(
        System.getProperty("user.dir") + "/src/test/resources/PerformanceTableReportTemplate.html");
    StringBuilder rows = new StringBuilder();
    for (Metric metric : metricList){
      rows.append("<tr><td>").append(metric.getName()).append("</td><td>").append(metric.getValue()).append("</td></tr>");
    }

    templateReport = templateReport.replace("<!--replace_here-->", rows.toString());
    templateReport=templateReport.replace("{title}","Performance CDP");
    String reportName="Performance CDP";
    fileUtil.writeToHtml(reportName, templateReport);
    String htmlReport=fileUtil.readHtml(System.getProperty("user.dir") + "/target/" + reportName + ".html");
    extentTest.log(Status.INFO,htmlReport);

    double timeProcess=0;
    for (Metric metric:metricList){
      if (metric.getName().equalsIgnoreCase("ProcessTime")){
        timeProcess= (double) metric.getValue();
      }
    }
    assertThat("Process time above the target",timeProcess, Matchers.lessThan(10.0));
  }
}
