package com.ary.qa.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

import java.io.IOException;

public class BaseClass {
  public static WebDriver driver;
  public static Wait<WebDriver> webDriverWait;
  public static ExtentReports extentReport;
  public static ExtentTest extentTest;
  public static JavascriptExecutor jsExecutor;

  public static Process launchChromeDebug() throws IOException {
    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "chrome-debug --port=9222");
    builder.redirectErrorStream(true);
    return builder.start();
  }
}
