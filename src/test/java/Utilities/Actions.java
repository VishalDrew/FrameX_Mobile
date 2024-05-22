package Utilities;

import Base.TestSetup;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static Utilities.Utils.sourceExists;

/**
 * Actions class.
 */
public class Actions extends TestSetup {

    public static WebDriver wait;

    public static WebElement element;

    /**
     * Clicks on an element based on the given attribute name and value.
     *
     * @param attributeName the name of the attribute to identify the element (e.g., "ID", "NAME", "XPATH", "CLASSNAME", "CSSSELECTOR", "TAGNAME", "ACCESSIBILITYID")
     * @param attributeValue the value of the attribute to identify the element
     * @throws Exception if an error occurs while finding or clicking the element
     */
    public static void click(String attributeName, String attributeValue) {
        try {
            String AN = attributeName.toUpperCase();
            switch (AN) {
                case "ID" -> driver.findElement(By.id(attributeValue)).click();
                case "NAME" -> driver.findElement(By.name(attributeValue)).click();
                case "XPATH" -> driver.findElement(By.xpath(attributeValue)).click();
                case "CLASSNAME" -> driver.findElement(By.className(attributeValue)).click();
                case "CSSSELECTOR" -> driver.findElement(By.cssSelector(attributeValue)).click();
                case "TAGNAME" -> driver.findElement(By.tagName(attributeValue)).click();
                case "ACCESSIBILITYID" ->  driver.findElement(AppiumBy.accessibilityId(attributeValue)).click();
            }
        }catch (Exception e) {
            log.error("Error occurred while clicking on element: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * Enters text into a web element based on the specified attribute name and value.
     *
     * @param attributeName the name of the attribute used to locate the web element (e.g., "ID", "NAME", "XPATH", etc.)
     * @param attributeValue the value of the attribute used to locate the web element
     * @param inputText the text to be entered into the web element
     * @throws Exception if an error occurs while locating or interacting with the web element
     */
    public static void enter(String attributeName, String attributeValue, String inputText) {

        try {
            String AN = attributeName.toUpperCase();
            switch (AN) {
                case "ID" -> {
                    driver.findElement(By.id(attributeValue)).click();
                    driver.findElement(By.id(attributeValue)).clear();
                    driver.findElement(By.id(attributeValue)).sendKeys(inputText);
                }
                case "NAME" -> {
                    driver.findElement(By.name(attributeValue)).click();
                    driver.findElement(By.name(attributeValue)).clear();
                    driver.findElement(By.name(attributeValue)).sendKeys(inputText);
                }
                case "XPATH" -> {
                    driver.findElement(By.xpath(attributeValue)).click();
                    driver.findElement(By.xpath(attributeValue)).clear();
                    driver.findElement(By.xpath(attributeValue)).sendKeys(inputText);
                }
                case "CLASSNAME" -> {
                    driver.findElement(By.className(attributeValue)).click();
                    driver.findElement(By.className(attributeValue)).clear();
                    driver.findElement(By.className(attributeValue)).sendKeys(inputText);
                }
                case "CSSSELECTOR" -> {
                    driver.findElement(By.cssSelector(attributeValue)).click();
                    driver.findElement(By.cssSelector(attributeValue)).clear();
                    driver.findElement(By.cssSelector(attributeValue)).sendKeys(inputText);
                }
                case "TAGNAME" -> {
                    driver.findElement(By.tagName(attributeValue)).click();
                    driver.findElement(By.tagName(attributeValue)).clear();
                    driver.findElement(By.tagName(attributeValue)).sendKeys(inputText);
                }case "accessibilityId" -> {
                    driver.findElement(AppiumBy.accessibilityId(attributeValue)).click();
                    driver.findElement(AppiumBy.accessibilityId(attributeValue)).clear();
                    driver.findElement(AppiumBy.accessibilityId(attributeValue)).sendKeys(inputText);
                }
                default -> System.out.println("Invalid attribute name specified: " + attributeName + attributeValue);
            }
        }catch (Exception e) {
            log.error("Error occurred while entering text: " + e.getMessage());
            e.getMessage();
        }
    }

    /**
     * Retrieves the text of an element based on the given attribute name and value.
     *
     * @param attributeName the name of the attribute to search for (ID, NAME, XPATH, CLASSNAME, CSSSELECTOR, TAGNAME, ACCESSIBILITYID)
     * @param attributeValue the value of the attribute to search for
     * @return the text of the element found using the specified attribute
     * @throws NoSuchElementException if no element is found with the specified attribute
     */
    public static String gettext(String attributeName, String attributeValue) {
        String AN = attributeName.toUpperCase();
        String text = null;
        try {
            switch (AN) {
                case "ID":
                    text = driver.findElement(By.id(attributeValue)).getText();
                    break;
                case "NAME":
                    text = driver.findElement(By.name(attributeValue)).getText();
                    break;
                case "XPATH":
                    text = driver.findElement(By.xpath(attributeValue)).getText();
                    break;
                case "CLASSNAME":
                    text = driver.findElement(By.className(attributeValue)).getText();
                    break;
                case "CSSSELECTOR":
                    text = driver.findElement(By.cssSelector(attributeValue)).getText();
                    break;
                case "TAGNAME":
                    text = driver.findElement(By.tagName(attributeValue)).getText();
                    break;
                case "ACCESSIBILITYID":
                    text = driver.findElement(AppiumBy.accessibilityId(attributeValue)).getText();
                    break;
                default:
                    log.error("Invalid attribute name specified: " + attributeName);
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving text: " + e.getMessage());
        }
        return text;
    }


    /**
     * Waits for a specific element to be visible on the web page.
     *
     * @param attributeName the attribute name to locate the element (e.g., "ID", "XPATH", "CLASSNAME", "CSSSELECTOR", "ACCESSIBILITYID")
     * @param attributeValue the value of the attribute to locate the element
     * @param seconds the maximum number of seconds to wait for the element to be visible
     * @throws Exception if an error occurs while waiting for the element to be visible
     */
    public static void webdriverWait(String attributeName, String attributeValue, int seconds) {
        try {
            String AN = attributeName.toUpperCase();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            switch (AN) {
                case "ID" -> {
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(attributeValue)));
                }
                case "XPATH" -> {
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(attributeValue)));
                }
                case "CLASSNAME" -> {
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(attributeValue)));
                }
                case "CSSSELECTOR" -> {
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(attributeValue)));
                }
                case "ACCESSIBILITYID"-> {
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId(attributeValue)));
                }
                default -> System.out.println("Invalid attribute name specified: " + attributeName + attributeValue);
            }
        } catch (Exception e) {
            log.error("Error occurred while waiting for element: " + e.getMessage());
            e.getMessage();
        }
    }

    /**
     * Waits for an element to be present on the screen using FluentWait.
     *
     * @param attributeName the attribute name to identify the element (ID, XPATH, CLASSNAME, CSSSELECTOR, ACCESSIBILITYID)
     * @param attributeValue the value of the attribute to locate the element
     * @param seconds the maximum time to wait for the element to be present, in seconds
     * @throws Exception if any error occurs during the wait process
     */
    public static void fluentWait(String attributeName, String attributeValue, int seconds) {
        try {
            String AN = attributeName.toUpperCase();
            FluentWait<AndroidDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(seconds))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(Exception.class);

            switch (AN) {
                case "ID":
                    element = wait.until(driver -> driver.findElement(By.id(attributeValue)));
                    break;
                case "XPATH":
                    element = wait.until(driver -> driver.findElement(By.xpath(attributeValue)));
                    break;
                case "CLASSNAME":
                    element = wait.until(driver -> driver.findElement(By.className(attributeValue)));
                    break;
                case "CSSSELECTOR":
                    element = wait.until(driver -> driver.findElement(By.cssSelector(attributeValue)));
                    break;
                case "ACCESSIBILITYID":
                    element = wait.until(driver -> driver.findElement(AppiumBy.accessibilityId(attributeValue)));
                    break;
                default:
                    System.out.println("Invalid attribute name specified: " + attributeName + attributeValue);
            }
        } catch (Exception e) {
            log.error("Error occurred while waiting for element: " + e.getMessage());
            e.getMessage();
        }
    }


    /**
     * Scrolls the screen either up or down based on the given action.
     *
     * @param action the action to perform, either "up" or "down"
     *
     * @return true if the scroll action is successful, false otherwise
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public static boolean scrollE2E(String action) throws InterruptedException {
        Thread.sleep(1000);
        try {
            if (action.equalsIgnoreCase("up") ) {
                driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(100000)"));
                return true;
            }else{
                driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(100000)"));
                return true;
            }
        }catch (Exception e) {
            log.error("Error occurred while scrolling: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * Scrolls to the specified text.
     *
     * @param text the text to scroll to
     * @throws InterruptedException if the scrolling is interrupted
     */
    public static void scrolltotext(String text) throws InterruptedException {
        String scrollCommand = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + text + "\"));";
        try {
            driver.findElement(AppiumBy.androidUIAutomator(scrollCommand));
        } catch (java.util.NoSuchElementException e) {
            log.error("Error occurred while scrolling to text: " + text + ". Message: " + e.getMessage());
        }
    }

    public static boolean scroll(String action) throws InterruptedException {
        Thread.sleep(1000);
        try {
            if (action.equalsIgnoreCase("up")) {
                driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
                return true;
            } else {
                driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollBackward()"));
                return true;
            }
        } catch (Exception e) {
            log.error("Error occurred while scrolling: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return false;
    }


    /**
     * Captures the screen after each test method execution.
     *
     * @param result the ITestResult object representing the result of the test method execution
     * @throws IOException if an I/O error occurs while capturing or saving the screenshot
     */
    @AfterMethod
    public void captureScreen(ITestResult result) throws IOException {

        if (result.getStatus() == ITestResult.FAILURE) {
            TakesScreenshot ts = driver;
            File source = ts.getScreenshotAs(OutputType.FILE); // capture screenshot file
            File target = new File(properties.get("Screenshotpath") + result.getName() + ".png");
            FileUtils.copyFile(source, target);
        }

    }


    /**
     * Checks if an element with the specified attribute is displayed on the page.
     *
     * @param attributeName the attribute name of the element to be checked (e.g., "ID", "NAME", "XPATH", "CLASSNAME", "CSSSELECTOR", "TAGNAME", "ACCESSIBILITYID")
     * @param attributeValue the value of the attribute of the element to be checked
     * @return true if the element is displayed, false otherwise
     * @throws NoSuchElementException if the element with the specified attribute is not found
     */
    public static boolean isElementDisplayed(String attributeName, String attributeValue) {

        String AN = attributeName.toUpperCase();
        boolean isDisplayed = false;

        try {
            switch (AN) {
                case "ID":
                    isDisplayed = driver.findElement(By.id(attributeValue)).isDisplayed();
                    break;

                case "NAME":
                    isDisplayed = driver.findElement(By.name(attributeValue)).isDisplayed();
                    break;

                case "XPATH":
                    isDisplayed = driver.findElement(By.xpath(attributeValue)).isDisplayed();
                    break;

                case "CLASSNAME":
                    isDisplayed = driver.findElement(By.className(attributeValue)).isDisplayed();
                    break;

                case "CSSSELECTOR":
                    isDisplayed = driver.findElement(By.cssSelector(attributeValue)).isDisplayed();
                    break;

                case "TAGNAME":
                    isDisplayed = driver.findElement(By.tagName(attributeValue)).isDisplayed();
                    break;

                case "ACCESSIBILITYID":
                    isDisplayed = driver.findElement(AppiumBy.accessibilityId(attributeValue)).isDisplayed();
                    break;
            }

        } catch (NoSuchElementException e) {
            log.error("Element with " + attributeName + " '" + attributeValue + "' not found: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while checking if element with " + attributeName + " '" + attributeValue + "' is displayed: " + e.getMessage());
        }

        return isDisplayed;
    }


    /**
     * Waits for a message to be displayed.
     *
     * @param msg the message to wait for
     * @return true if the message is displayed, false otherwise
     */

    public static boolean waitForMessage(String msg) {
        boolean displayed = sourceExists(msg);
        try {
            while (!displayed) {
                log.info("Waiting for message: '" + msg + "'");
                Thread.sleep(1000); // Wait for 1 second before checking again
                displayed = sourceExists(msg);
            }
            log.info("Message '" + msg + "' is displayed");
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for message: '" + msg + "'");
            Thread.currentThread().interrupt(); // Reset interrupted status
        }
        return displayed;
    }

    public static boolean validateTextLimit(String locatorType, String locatorValue, String input, String limit) {
        int limitvalue = Integer.parseInt(limit);
        enter(locatorType, locatorValue, input);
        String enteredText = gettext(locatorType, locatorValue);
        return enteredText.length() <= limitvalue;

    }

        public static boolean validateSpecialCharactersandNumbers(String locatorType, String locatorValue, String input){
            enter(locatorType, locatorValue, input);
            String  enteredText = gettext(locatorType, locatorValue);
            return enteredText.equals(input);
        }

    }
