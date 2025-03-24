package com.ary.qa;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Objects;

public class ExtractFcp {

  @Test
  public void checkFcp() {
    WebDriver driver = new ChromeDriver();
    JavascriptExecutor js = (JavascriptExecutor) driver;
    driver.get("https://www.youtube.com/");
    String script = "return new Promise((resolve) => {\n"
        + "                new PerformanceObserver((entryList) => {\n"
        + "                    const entries = entryList.getEntries();\n"
        + "                    const fcpEntry = entries[0];\n"
        + "                    resolve(fcpEntry);\n"
        + "                }).observe({ type: 'paint', name: 'first-contentful-paint', buffered: true });\n"
        + "            });";

    HashMap<String, Objects>fcpResult = (HashMap<String, Objects>) js.executeScript(script);
    System.out.println(Double.parseDouble(String.valueOf(fcpResult.get("startTime")))/1000+ " s");
    driver.quit();
  }
}
