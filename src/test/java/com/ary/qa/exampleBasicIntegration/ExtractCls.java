package com.ary.qa.exampleBasicIntegration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExtractCls {
  @Test
  public void getCls() {
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.youtube.com/");
    String script = "return new Promise((resolve) => {\n"
        + "                new PerformanceObserver((entryList) => {\n"
        + "                    const entries = entryList.getEntries();\n"
        + "                    resolve(entries);\n"
        + "                }).observe({ type: 'layout-shift', buffered: true });\n"
        + "            });";
    JavascriptExecutor js = (JavascriptExecutor) driver;
    List<Map<String, Objects>> map= (List<Map<String, Objects>>) js.executeScript(script);
    double totalCls=0;
    for (Map a:map){
      totalCls=totalCls+Double.parseDouble(String.valueOf(a.get("value")));
      System.out.println(a.get("value"));
    }
    System.out.println("CLS: "+totalCls);
    driver.quit();
  }
}
