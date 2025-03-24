package com.ary.qa;


import com.ary.qa.model.lighthouse.LighthouseMain;
import com.ary.qa.util.BaseClass;
import com.ary.qa.util.FileReaderWriterUtility;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Unit test for simple App.
 */
public class AppTest extends BaseClass {


  @Test
  public void testYoutube() {

    Runtime rt = Runtime.getRuntime();
    try {
      rt.exec(
          "cmd /c start cmd.exe /K \" lighthouse https://www.youtube.com\"\" --emulated-from-factor=desktop-output --output-path=target/testing.html && exit\" ");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void perforManceTestUsingLighthouse() throws IOException, InterruptedException {
    Process chromeProcess=chromeDebug();
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("debuggerAddress", "localhost:9222");
    ChromeDriver driver = new ChromeDriver(options);
    String url = "https://www.youtube.com/";
    driver.get(url);
    System.out.println("open youtube on port 9222");
    lighthouseAudit(url, "testing", "json");
//    driver.quit();
    String html =readHtml(System.getProperty("user.dir") + "/target/testingReport.html");

    extentTest.log(Status.INFO, html);
    chromeProcess.destroy();
    readReport();
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

  @Test
  public void readReport() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String dir = System.getProperty("user.dir") + "/target/testing.json";
    LighthouseMain main = mapper.readValue(new FileReader(dir), LighthouseMain.class);

    String template=readHtml(System.getProperty("user.dir") + "/src/test/resources/LighthouseTemplateTable.html");
    System.out.println(template);
    StringBuilder rows = new StringBuilder();
    String fcp=String.format("%.3f %s",main.getAudits().getFcp().getNumericValue()/1000," .s");
    String lcp=String.format("%.3f %s",main.getAudits().getLcp().getNumericValue()/1000," .s");
    String cls=String.format("%.5f",main.getAudits().getCls().getNumericValue()/1000);
    rows.append("<tr><td>FCP</td><td>").append(fcp).append("</td></tr>");
    rows.append("<tr><td>LCP</td><td>").append(lcp).append("</td></tr>");
    rows.append("<tr><td>CLS</td><td>").append(cls).append("</td></tr>");

    template=template.replace("<!--replace_here-->", rows.toString());

    writeToHtml("testingReport",template);

  }

  private void writeToHtml(String nameReport,String html){
    String dir =
        System.getProperty("user.dir") + "/target/"+nameReport+".html";
    try {
      BufferedWriter output = new BufferedWriter(new FileWriter(dir));
      output.write(html);
      output.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String readHtml(String location) throws FileNotFoundException {
    StringBuilder sb = new StringBuilder();
    BufferedReader input = new BufferedReader(new FileReader(location));
    String str;
    try {
      while ((str = input.readLine()) != null) {
        sb.append(str);
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }


}
