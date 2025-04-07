package com.ary.qa.exampleBasicIntegration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.HashMap;


public class ExtractLcp {
  @Test
  public void lcpUsingJs() {
    WebDriver driver = new ChromeDriver();
    JavascriptExecutor js = (JavascriptExecutor) driver;
    driver.get("http://www.google.com/");
    String script = "return new Promise((resolve) => {\n"
        + "                new PerformanceObserver((entryList) => {\n"
        + "                    const entries = entryList.getEntries();\n"
        + "                    const lcpEntry = entries[entries.length - 1];\n"
        + "                    resolve(lcpEntry);\n"
        + "                }).observe({ type: 'largest-contentful-paint', buffered: true });\n"
        + "            });";
    HashMap<String, Object> lcpResult = (HashMap<String, Object>) js.executeScript(script);
    if (!lcpResult.isEmpty()) {
      System.out.println("LCP Detail");
      System.out.println("StartTime: " + lcpResult.get("startTime"));
      //      LCP usualy using renderTime, but if there is no render time will used loadtime
      System.out.println(
          "RenderTime: " + Double.parseDouble(String.valueOf(lcpResult.get("renderTime"))) / 1000
              + " s");
      System.out.println(
          "LoadTime: " + Double.parseDouble(String.valueOf(lcpResult.get("loadTime"))) / 1000
              + " s");
      System.out.println("Element: " + lcpResult.get("element"));
    } else {
      System.out.println("No LCP Detail");
    }
    driver.close();
  }

}
