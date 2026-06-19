package org.test.utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports get() {
        if (extent == null) {
            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("test-output/ExtentReport.html");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}