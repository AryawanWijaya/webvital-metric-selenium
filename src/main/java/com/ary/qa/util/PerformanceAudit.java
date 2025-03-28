package com.ary.qa.util;

import com.ary.qa.constant.PerformanceMetric;
import org.openqa.selenium.JavascriptExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PerformanceAudit {

  public void lighthouseAudit(String URL, String ReportName, String format, boolean isWeb)
      throws IOException {
    String preset;
    if (isWeb) {
      preset = "desktop";
    } else {
      preset = "mobile";
    }
    ProcessBuilder builder = new ProcessBuilder("cmd.exe",
        "/c",
        "lighthouse",
        URL,
        "--port=9222",
        "--preset=" + preset,
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

  public double getPerformanceMetricByJsScript(JavascriptExecutor js, PerformanceMetric audit) {
    double result = 0;
    String script = "";
    switch (audit) {
      case CLS:
        script = "return new Promise((resolve) => {\n"
            + "                new PerformanceObserver((entryList) => {\n"
            + "                    const entries = entryList.getEntries();\n"
            + "                    resolve(entries);\n"
            + "                }).observe({ type: 'layout-shift', buffered: true });\n"
            + "            });";
        List<Map<String, Objects>> map = (List<Map<String, Objects>>) js.executeScript(script);
        double totalCls = 0;
        for (Map a : map) {
          result = totalCls + Double.parseDouble(String.valueOf(a.get("value")));
        }
        System.out.println("CLS: " + totalCls);
        break;
      case LCP:
        script = "return new Promise((resolve) => {\n"
            + "                new PerformanceObserver((entryList) => {\n"
            + "                    const entries = entryList.getEntries();\n"
            + "                    const lcpEntry = entries[entries.length - 1];\n"
            + "                    resolve(lcpEntry);\n"
            + "                }).observe({ type: 'largest-contentful-paint', buffered: true });\n"
            + "            });";
        HashMap<String, Object> lcpResult = (HashMap<String, Object>) js.executeScript(script);

        //      LCP usualy using renderTime, but if there is no render time will used loadtime
        if (lcpResult.get("renderTime") != null) {
          result = Double.parseDouble(String.valueOf(lcpResult.get("renderTime"))) / 1000;
        } else {
          result = Double.parseDouble(String.valueOf(lcpResult.get("loadTime"))) / 1000;
        }
        System.out.println("LCP: " + result);
        break;
      case FCP:
        script = "return new Promise((resolve) => {\n"
            + "                new PerformanceObserver((entryList) => {\n"
            + "                    const entries = entryList.getEntries();\n"
            + "                    const fcpEntry = entries[0];\n"
            + "                    resolve(fcpEntry);\n"
            + "                }).observe({ type: 'paint', name: 'first-contentful-paint', buffered: true });\n"
            + "            });";

        HashMap<String, Objects> fcpResult = (HashMap<String, Objects>) js.executeScript(script);
        result = Double.parseDouble(String.valueOf(fcpResult.get("startTime"))) / 1000;
        System.out.println("FCP: " + result);
        break;
      case FID:
        script = "return new Promise((resolve) => {\n"
            + "                new PerformanceObserver((entryList) => {\n"
            + "                    const entries = entryList.getEntries();\n"
            + "                    resolve(entries);\n"
            + "                }).observe({ type: 'first-input', buffered: true });\n"
            + "            });";
        List<Map<String, Object>> fid = (List<Map<String, Object>>) js.executeScript(script);
        result=Double.parseDouble(fid.get(0).get("processingStart").toString())
            -Double.parseDouble(fid.get(0).get("startTime").toString());
        System.out.println("FID: " + result);
        break;
    }

    return result;
  }
}
