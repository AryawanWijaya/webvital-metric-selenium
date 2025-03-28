package com.ary.qa.exampleBasicIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ExtractFid {

  @Test
  public void testExtractFid() {
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.youtube.com/");
    String script = "return new Promise((resolve) => {\n"
        + "                new PerformanceObserver((entryList) => {\n"
        + "                    const entries = entryList.getEntries();\n"
        + "                    resolve(entries);\n"
        + "                }).observe({ type: 'first-input', buffered: true });\n"
        + "            });";

    JavascriptExecutor js = (JavascriptExecutor) driver;
    driver.findElement(By.xpath(".//input[@name='search_query']")).click();
    List<Map<String, Object>> fid = (List<Map<String, Object>>) js.executeScript(script);

    double resultFid = Double.parseDouble(fid.get(0).get("processingStart").toString())
        -Double.parseDouble(fid.get(0).get("startTime").toString());
    System.out.println("FID: "+resultFid);
  }
}
