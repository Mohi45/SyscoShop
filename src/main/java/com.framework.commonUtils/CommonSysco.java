package com.framework.commonUtils;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import java.time.Instant;
import java.util.List;

import static com.framework.commonUtils.RandomAction.*;
public class CommonSysco {

    private final static Logger logger = Logger.getLogger(CommonSysco.class);
    private static final int TIMEOUT = 10;
    private static final int POLLING = 500;
    public static String fileFormat = "CSV";
    private final WebDriverWait wait;
    protected WebDriver driver;

    public CommonSysco(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, TIMEOUT, POLLING);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, TIMEOUT), this);
    }

    protected WebElement waitForElementToAppear(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitForElementsToAppear(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected WebElement waitForElementToClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForElementToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean waitForElementToDisappear(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected boolean waitForTextToDisappear(By locator, String text) {
        return wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, text)));
    }

    public boolean doLogin(String user, String password) throws InterruptedException {
        try {
            driver.get(Constant.urlSyscoShop);
			Thread.sleep(7000);

			driver.findElement(Locators.loc_userNameDiscovery).click();
			logger.info("User clicks on the Email* ");
			waitForElementToBePresent(Locators.loc_userNameDiscovery).sendKeys(user);
			Thread.sleep(2000);
			String userEmail = driver.findElement(Locators.loc_userNameDiscovery).getAttribute("value");
			System.out.println(user+"   "+userEmail);
			if (!user.equals(userEmail)) {
				waitForElementToBePresent(Locators.loc_userNameDiscovery).sendKeys(user);
				logger.info("Again entering email...");
			}
			Thread.sleep(2000);
			waitForElementToBePresent(Locators.loc_nextDiscovery).click();
			logger.info("User clicks on the nextDiscovery * ");
			Thread.sleep(2000);
			waitForElementToAppear(Locators.loc_password).sendKeys(password);
			logger.info("User enters the password * ");
			Thread.sleep(2000);
			waitForElementToAppear(Locators.loc_password).sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			waitForElementToAppear(Locators.loc_login).click();
			Thread.sleep(10000);
			if (waitForElementToBePresent(Locators.loc_lnkProfile).isDisplayed())
				return true;
        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(5000);
			waitForElementToAppear(Locators.loc_password).sendKeys(Keys.ENTER);
            driver.get("https://shop.sysco.com/app/discover");
            logger.info("Launched the discover url !!");
            Thread.sleep(8000);
            logger.info("Waiting for Profile Page in second try ....");
            if (waitForElementToBePresent(Locators.loc_lnkProfile).isDisplayed()) {
                logger.info("Found Profile Page !!");
                return true;
            }
        }
        return false;
    }

    public void doLogout() {
        try {
            waitForElementToBePresent(Locators.loc_lnkProfile);
            new Actions(driver).clickAndHold(driver.findElement(Locators.loc_lnkProfile)).build().perform();
            Thread.sleep(1000);
            waitForElementToBePresent(Locators.loc_signOut).click();
            Thread.sleep(1000);
            waitForElementToBePresent(Locators.loc_confirmLogout).click();
        } catch (Exception ex) {
            logger.info("failed to logout");
        }
    }

    private void selectList(String listName) throws Exception {
        /*
         * waitForElementToBePresent(Locators.loc_lists); logger.info("list name is "+
         * listName); new Actions(driver).
         * moveToElement(driver.findElement(Locators.loc_lists)). clickAndHold().
         * build(). perform(); try { By loc_listItems =
         * By.xpath("//li[contains(text(),'"+listName+"')]");
         * waitForElementToBePresent(loc_listItems).click(); return; } catch (Exception
         * ex) { logger.info("account not selected by default"); //
         * waitForElementToBePresent(Locators.loc_seeAllLists).click();
         * driver.get("https://shop.sysco.com/app/lists"); }
         */
        driver.get("https://shop.sysco.com/app/lists");
        logger.info("Launched the list page Url !!");
        Thread.sleep(3000);

        List<WebElement> elements = null;

        if (isListDdlPresent()) {
            waitForElementToClickable(Locators.loc_allListDropIcon).click();
            logger.info("User Clicks on the list icon");
            Thread.sleep(1000);
            elements = driver.findElements(Locators.loc_allListValues);
        } else {
            elements = driver.findElements(Locators.loc_btnListNames);
            logger.info("User clicks in the list");
        }

        for (WebElement element : elements) {
            String eleText = element.getText().toLowerCase();
            if (eleText.contains(listName.toLowerCase())) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                Thread.sleep(400);
                try {
                    element.click();
                }catch (Exception ex){
                    System.out.println("Click on list in second try !!");
                    element.click();
                }
                return;
            }
        }
//        throw new Exception("list Name not found in all lists");
    }

    public boolean isListDdlPresent() {
        try {
            waitForElementToAppear(Locators.loc_allListDropIcon);
            return true;
        } catch (Exception e) {
            logger.debug("no list drop down");
            return false;
        }
    }

    public void exportList(String restName) throws InterruptedException {
        waitForElementToClickable(Locators.loc_moreActions).click();
        logger.info("User clicks on the more Actions Button !!");
        waitForElementToClickable(Locators.loc_exportList).click();
        logger.info("User clicks on the export Button !!");
        Thread.sleep(3000);
        waitForElementToClickable(Locators.loc_includePrices).click();
        logger.info("User clicks on the include prices toggel Button !!");
        Thread.sleep(100);
        WebElement text_input = waitForElementToBePresent(Locators.loc_inputFileName);
        text_input.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", text_input);
        Thread.sleep(200);
        text_input.sendKeys(restName.replaceAll("[^A-Za-z]+", "") + "_" + Instant.now().getEpochSecond());
		 Thread.sleep(1000);
        waitForElementToClickable(Locators.loc_btnExport).click();
		Thread.sleep(1000);
        System.out.println("File have been Downloaded !!!");
    }

    private void selectAccount(String accountName) throws InterruptedException {
        if (accountName.contains(".")) {
            int val = new BigDecimal(accountName).intValue();
            accountName = String.valueOf(val);
        } else {
            System.out.println("Account is already Correct format !!");
        }
        Thread.sleep(3000);
		try {
			if (driver.findElement(By.xpath("//body[@id='intercom-container-body']//div[@aria-label='Close']"))
					.isDisplayed()) {
				System.out.println("Found popu for update !!!");
				driver.findElement(By.xpath("//body[@id='intercom-container-body']//div[@aria-label='Close']")).click();
				System.out.println("Closing popu for update !!!");
			}
		} catch (Exception e) {
			System.out.println("No such update popu found on UI !!");
		}
		System.out.println("---------------------------------");
        try {
            waitForElementToClickable(Locators.loc_accountDdlBtn).click();
            logger.info("User clicks on the account List !!");
        }catch (Exception e){
            logger.info("User Trying to click on list in second try...");
            waitForElementToClickable(Locators.loc_accountDdlBtn).click();
            logger.info("User clicks on the account List in second try !!");
        }
        try {
            waitForElementToAppear(Locators.loc_accountSearchBtn).sendKeys(accountName);
            logger.info("User enters the Account Name !!");
            Thread.sleep(100);
            waitForElementToAppear(By.xpath(Locators.loc_accountNum.replace("accountName", accountName))).click();
        } catch (TimeoutException ex) {
            logger.info("Select Account from Second Try ...");
            waitForElementToAppear(Locators.loc_accountSearchBtn1).sendKeys(accountName);
            logger.info("User enters the Account Name Second Try !!");
            Thread.sleep(100);
            waitForElementToAppear(By.xpath(Locators.loc_accountNum.replace("accountName", accountName))).click();
            logger.info("User selected the require account !!");
        } catch (Exception ex) {
            logger.error("failed to select account");
            ex.printStackTrace();
        }
    }

    public boolean stepsToExport(String restName, String accountName, String listName) {
        try {
//            Thread.sleep(3000);
            if (isIframePresent(driver)) {
                dismissPopUp();
            }
            dismissAlert();

            if (accountName != null && !accountName.equalsIgnoreCase("")) {
                selectAccount(accountName);
                Thread.sleep(5000);
                if (!verifyAccount(accountName)) {
                    throw new Exception(
                            String.format("account selection mismatch, expected account {} not selected", accountName));
                }
            }

            if (isIframePresent(driver)) {
                dismissPopUp();
            }
            dismissAlert();

            if (listName != null && !listName.equalsIgnoreCase("")) {
                selectList(listName);
            }

            if (isIframePresent(driver)) {
                dismissPopUp();
            }
            dismissAlert();

            exportList(restName);

            Thread.sleep(3000);
            doLogout();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean verifyAccount(String accountName) {
        return waitForElementToBePresent(By.xpath(Locators.loc_accountName.replace("acNum", accountName)))
                .isDisplayed();
    }
	public void onTestFailure() {
		// TODO Auto-generated method stub
		TakesScreenshot ts = (TakesScreenshot) driver;
		File file = ts.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(file, new File("./ScreenShot_Folder/TestFailure" + timestamp() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("screenshot is taken");

	}


	public static String timestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
	}
}
