package Utilities;

import Base.TestSetup;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static Listeners.FrameX_Listeners.AllureReportfileName;

public class Rough {

    public static void main(String[] args) {

    }

    public static void generateAllureReport(){
        String allureExecutable = "E:\\allure-2.29.0\\bin\\allure.bat";
        String  allureReportDir = System.getProperty("user.dir") + "\\src\\test\\resources\\Allure Reports";
        String  allureResultsDir = System.getProperty("user.dir") + "\\allure-results";
        String command = String.format("\"%s\" generate --clean --single-file -o \"%s\" \"%s\"", allureExecutable, allureReportDir, allureResultsDir);

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                TestSetup.log.info("Allure report generated successfully.");

                // Rename the index.html file to the desired name
                File oldFile = new File(allureReportDir + File.separator + "index.html");
                File newFile = new File(allureReportDir + File.separator + AllureReportfileName);
                if (oldFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        TestSetup.log.info("Report renamed to " + AllureReportfileName);
                    } else {
                        TestSetup.log.error("Failed to rename report file.");
                    }
                } else {
                    TestSetup.log.error("index.html file not found.");
                }
            } else {
                TestSetup.log.error("Failed to generate Allure report. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
