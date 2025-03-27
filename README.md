# About this project:

This automation framework, built with TestNG and Selenium, specializes in measuring and validating UI performance metrics, including Core Web Vitals (CLS, FCP, LCP) and key browser performance indicators. The project demonstrates three robust approaches to integrate performance testing into automation suites:

1. JavaScript Executor - Directly querying browser performance APIs to capture real-time metrics
2. Lighthouse Integration - Generating comprehensive performance audits alongside functional tests
3. Chrome DevTools Protocol - Accessing low-level performance data for advanced analysis

Designed for QA engineers transitioning from functional to performance-aware test automation, this solution bridges the gap between UI validation and user experience benchmarking.


## Dependencies

On this project I used TestNG framework that combined with selenium java, this is the detail of dependencies that I used:

- TestNG 7.11.0
- Selenium Java 4.28.1
- Lombok 1.18.36
- Jackson databind 2.18.3
- ExtentReport 5.1.2
- Hamcrest assertion 3.0

## How to run this project

1. Clone this project and build make sure all dependencies downloaded correctly
2. Find the testng.xml under test/resource and run it
3. After run automatically will generate report under target directory

> If you want to learn the basic integration between selenium, lighthouse, javascript executor, and chrome devtools you can open **exampleBasicIntegration** package, you can run it one by one without testng integration

## Result of test:

[//]: # (More detail you can read this [article]&#40;https://www.linkedin.com/pulse/visual-comparison-playwright-java-leveraging-chaining-ravato-wijaya-paf9c&#41;)

## Result of test:

![alt text](https://github.com/AryawanWijaya//webvital-metric-selenium/blob/master/lighthouseReport.png?raw=true)
![alt text](https://github.com/AryawanWijaya//webvital-metric-selenium/blob/master/reportCdp.png?raw=true)
![alt text](https://github.com/AryawanWijaya//webvital-metric-selenium/blob/master/reportWebVitalJs.png?raw=true)
![alt text](https://github.com/AryawanWijaya//webvital-metric-selenium/blob/master/reportLighthouse.png?raw=true)