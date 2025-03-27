package com.ary.qa.exampleBasicIntegration;


import com.ary.qa.model.lighthouse.LighthouseMain;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Unit test for simple App.
 */
public class LightHouseIntegration extends BaseClass {

  FileReaderWriterUtility fileUtil = new FileReaderWriterUtility();

  @Test
  public void testLighthouseWithoutSelenium() throws IOException {
    String url = "https://www.youtube.com/";
    ProcessBuilder builder = new ProcessBuilder("cmd.exe",
        "/c",
        "lighthouse",
        url,
        "--port=9222",
        "--preset=desktop",
        "--output=html",
        "--output-path=target/report-lighthouse.html");
    builder.redirectErrorStream(true);
    Process p = builder.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;
    while (true) {
      line = r.readLine();
      if (line == null) {
        break;
      }
    }
  }

  @Test
  public void testLighthouseWithSelenium() throws IOException, InterruptedException {
    Process chromeProcess = chromeDebug();
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("debuggerAddress", "localhost:9222");
    ChromeDriver driver = new ChromeDriver(options);
    driver.manage().window().maximize();
    String url = "https://www.youtube.com/";
    driver.get(url);
    System.out.println("open youtube on port 9222");

    String lighthouseReportName = "lightHouseReport";
    lighthouseAudit(url, lighthouseReportName, "json");
    generateReport(lighthouseReportName);

    closedProcess(chromeProcess.pid());
  }

  private void closedProcess(long pid) {
    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "taskkill /PID " + pid + " /F /T");
    pb.redirectErrorStream(true);
    try {
      Process process = pb.start();
      process.waitFor();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Process chromeDebug() throws IOException {
    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "chrome-debug --port=9222");
    builder.redirectErrorStream(true);
    return builder.start();
  }

  private static void lighthouseAudit(String URL, String ReportName, String format)
      throws IOException {
    ProcessBuilder builder = new ProcessBuilder("cmd.exe",
        "/c",
        "lighthouse",
        URL,
        "--port=9222",
        "--preset=desktop",
        "--output=" + format,
        "--output-path=target/" + ReportName + "." + format);
    builder.redirectErrorStream(true);
    Process p = builder.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;
    while (true) {
      line = r.readLine();
      if (line == null) {
        break;
      }
    }
  }

  public void generateReport(String reportName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String dir = System.getProperty("user.dir") + "/target/" + reportName + ".json";
    LighthouseMain main = mapper.readValue(new FileReader(dir), LighthouseMain.class);

    String template = fileUtil.readHtml(
        System.getProperty("user.dir") + "/src/test/resources/PerformanceTableReportTemplate.html");

    StringBuilder rows = new StringBuilder();
    String fcp =
        String.format("%.3f %s", main.getAudits().getFcp().getNumericValue() / 1000, " .s");
    String lcp =
        String.format("%.3f %s", main.getAudits().getLcp().getNumericValue() / 1000, " .s");
    String tbt =
        String.format("%.3f %s", main.getAudits().getTbt().getNumericValue() / 1000, " .s");
    String speedIndex =
        String.format("%.3f %s", main.getAudits().getSpeedIndex().getNumericValue() / 1000, " .s");
    String cls = String.format("%.5f", main.getAudits().getCls().getNumericValue());
    rows.append("<tr><td>FCP</td><td>").append(fcp).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(lcp).append("</td></tr>");
    rows.append("<tr><td>CLS</td><td>").append(cls).append("</td></tr>");
    rows.append("<tr><td>TBT</td><td>").append(tbt).append("</td></tr>");
    rows.append("<tr><td>Speed Index</td><td>").append(speedIndex).append("</td></tr>");

    template = template.replace("<!--replace_here-->", rows.toString());
    template=template.replace("{title}","Lighthouse");

    System.out.println(template);
    fileUtil.writeToHtml("lightHouseResult", template);
    Desktop.getDesktop()
        .browse(new File(System.getProperty("user.dir") + "/target/lightHouseResult.html").toURI());
  }
}
