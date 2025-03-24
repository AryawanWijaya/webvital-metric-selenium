package com.ary.qa.exampleBasicIntegration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v132.performance.Performance;
import org.openqa.selenium.devtools.v132.performance.model.Metric;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class PerformanceTestUsingCdp {
  @Test
  public void lcpUsingCdp(){
    WebDriver driver = new ChromeDriver();
    ChromeDriver chromeDriver= (ChromeDriver) driver;
    DevTools devTools=chromeDriver.getDevTools();
    devTools.createSession();
    devTools.send(Performance.enable(Optional.empty()));
    driver.get("http://www.youtube.com/");
    List<Metric> metricList=devTools.send(Performance.getMetrics());
    for (Metric metric : metricList){
      System.out.println(metric.getName()+"---"+metric.getValue());
    }
    driver.quit();
  }
}
