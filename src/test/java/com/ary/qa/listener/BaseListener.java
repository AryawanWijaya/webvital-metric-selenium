package com.ary.qa.listener;

import com.ary.qa.util.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class BaseListener extends BaseClass implements ITestListener, ISuiteListener {
  Process process;
  @Override
  public void onStart(ISuite suite) {
    System.out.println("1 -  === Run listener before suite - init extent report ===");
    String reportPath = System.getProperty("user.dir") + "/target/report.html";
    ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(reportPath);
    extentReport= new ExtentReports();
    extentReport.attachReporter(extentSparkReporter);
    extentReport.setSystemInfo("OS", System.getProperty("os.name"));
    extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));

  }

  @Override
  public void onFinish(ISuite suite) {
    System.out.println("onFinished");
    extentReport.flush();
    try {
      Desktop.getDesktop()
          .browse(new File(System.getProperty("user.dir") + "/target/report.html").toURI());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onStart(ITestContext testContext) {
    System.out.println("2 -  === Run listener before <test> - create test extent report ===");
    String browserName = testContext.getCurrentXmlTest().getParameter("browserName");
    switch (browserName.toLowerCase()) {
      default:
      case "chrome":
        driver = new ChromeDriver();
        break;
      case "firefox":
        driver = new FirefoxDriver();
        break;
      case "edge":
        driver = new EdgeDriver();
        break;
      case "debug-chrome":
        try {
          process=launchChromeDebug();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:9222");
        driver=new ChromeDriver(options);
        break;
      case "chrome-dev-tools":
        driver = new ChromeDriver();
        ChromeDriver chromeDriver=(ChromeDriver)driver;
        devTools=chromeDriver.getDevTools();
        devTools.createSession();
    }
    driver.manage().window().maximize();
    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    jsExecutor=(JavascriptExecutor) driver;
  }

  @Override
  public void onFinish(ITestContext testContext) {
    driver.quit();
    try {
      if (process.isAlive()) {
        long pid = process.pid();
        ProcessBuilder killBuilder = new ProcessBuilder("cmd", "/c", "taskkill /PID " + pid + " /F /T");
        killBuilder.redirectErrorStream(true);
        Process killProcess = killBuilder.start();
        killProcess.waitFor();
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onTestStart(ITestResult testResult) {
    System.out.println(
        "3 -  === Run listener before each test -  setup detail browser extent report ===");
    extentTest = extentReport.createTest(testResult.getTestContext().getName());
    if (testResult.getTestContext().getCurrentXmlTest().getParameter("author") != null) {
      extentTest.assignAuthor(testResult.getTestContext()
          .getCurrentXmlTest()
          .getParameter("author"));
    } else {
      extentTest.assignAuthor("default-user");
    }
  }

  @Override
  public void onTestSuccess(ITestResult testResult) {
    extentTest.pass("Scenario from method " + testResult.getTestContext().getName() + " passed");
  }

  @Override
  public void onTestFailure(ITestResult testResult) {
    extentTest.fail(testResult.getThrowable());
  }
}
