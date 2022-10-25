package com.framework.commonUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RandomAction {
    public static WebDriver driver;

    /*
     * public static File WaitForNewFile(Path folder, String extension, int
     * timeout_sec) throws InterruptedException, IOException { long end_time =
     * System.currentTimeMillis() + timeout_sec * 1000; try (WatchService watcher =
     * FileSystems.getDefault().newWatchService()) { folder.register(watcher,
     * StandardWatchEventKinds.ENTRY_MODIFY); for (WatchKey key; null != (key =
     * watcher.poll(end_time - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
     * key.reset()) { for (WatchEvent<?> event : key.pollEvents()) { File file =
     * folder.resolve(((WatchEvent<Path>) event).context()).toFile(); if
     * (file.toString().toLowerCase().endsWith(extension.toLowerCase())) return
     * file; } } } return null; }
     */
    // enter email id where you need to send email

    public static String getDate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Calendar calobj = Calendar.getInstance();
        System.out.println(df.format(calobj.getTime()));
        String CurrentDate = df.format(calobj.getTime());
        return CurrentDate;
    }

    public static boolean isFramePresent(WebDriver driver) throws InterruptedException {
        //
        // driver.findElement(By.xpath("//html/body/table/tbody/tr[2]/td[1]/div/div[2]/table/tbody/tr[1]/td/input")).click();
        Thread.sleep(3000);
        // List to get & store frame
        List<WebElement> ele = driver.findElements(By.tagName("frame"));
        System.out.println("Number of frames in a page :" + ele.size()); // ele.size
        // -
        // size
        // of
        // frame
        // list

        if (ele.size() == 0) {
            System.out.println("No frames on this page");
            return false; // No frames
        } else {
            System.out.println("Frames present on this page, Below are the details -");

            for (WebElement el : ele) {
                // Returns the Id of a frame
                System.out.println("Frame Id :" + el.getAttribute("id"));
                // Returns the Name of a frame.
                System.out.println("Frame name :" + el.getAttribute("name"));
            }
            return true; // frames present
        }

    }

    public static String setdownloadDir() {
        File files = new File(System.getProperty("user.home") + "\\Downloads\\" + getDate());

        if (!files.exists()) {
            if (files.mkdir()) {
                System.out.println("Multiple directory are created!");
            } else {
                System.out.println("Failed to create multiple directory!.. The directory might exist");
            }
        }
        String filepath = files.getPath();
        return filepath;

    }

    public static WebDriver setDownloadFilePath() {
        // TODO Auto-generated method stub
        String downloadFilepath = setdownloadDir();
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(cap);
        return driver;
    }

    public static File getLatestFilefromDir(String dirPath, String fileType) {
        File getLatestFilefromDir = null;
        File dir = new File(dirPath);
        FileFilter fileFilter = new WildcardFileFilter("*." + fileType);
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {
            /** The newest file comes first **/
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            getLatestFilefromDir = files[0];
        }

        return getLatestFilefromDir;
    }
    public static void DownloadOG(Robot robot, WebDriver driver) {
        try {
            robot = new Robot();
            Thread.sleep(2000); // Thread.sleep throws InterruptedException
            robot.keyPress(KeyEvent.VK_DOWN); // press arrow down key of
            // keyboard to navigate and
            // select Save radio button

            Thread.sleep(2000); // sleep has only been used to showcase each
            // event separately
            robot.keyPress(KeyEvent.VK_TAB);
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_TAB);
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_TAB);
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_ENTER);
            // press enter key of keyboard to perform above selected action
            System.out.println("File is downloaded");

        } catch (AWTException e) {
            ErrScreenshotCapture(driver);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFiles(String path) {
        File dir = new File(path);
        // FileUtils.cleanDirectory(dir);
        for (File file : dir.listFiles())
            if (!file.isDirectory())
                file.delete();
        System.out.println("All files deleted from folder :-" + path);

    }

    public static void deleteFiles(String path, String extension) {
        File dir = new File(path);
        // FileUtils.cleanDirectory(dir);
        for (File file : dir.listFiles())
            if (!file.isDirectory() && file.getName().contains(extension))
                file.delete();
        System.out.println("All files deleted from folder :-" + path);

    }

    public static WebDriver openBrowser(String browser, String path) {
        if (browser.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver",
                    System.getProperty("user.dir") + "\\Downloads\\chromedriver_win32\\ie.exe");
            driver = new InternetExplorerDriver();
        } else if (browser.equalsIgnoreCase("ff")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("gecko")) {
            System.setProperty("webdriver.gecko.driver", path);
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            System.setProperty("webdriver.chrome.driver", path);
            driver = new ChromeDriver(options);
        }
        return driver;
    }
    public static WebDriver launchBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments( "--window-size=1920,1200");
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        WebDriverManager.chromedriver().driverVersion("106.0.5249.119").setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }


    public static boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } // try
        catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    public static boolean isPopUpPresent() {
        try {
           return driver.findElement(Locators.loc_popUpModal).isDisplayed();
        } // try
        catch (Exception Ex) {
            return false;
        }
    }

    public static void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            System.out.println("No such alert found on the page !!");
        }
    }

    public static void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException e) {
            System.out.println("No such alert found on the page !!");
        }
    }

    public static void dismissPopUp() {
        try {
            driver.switchTo().frame("intercom-modal-frame");
            driver.findElement(Locators.loc_dismissPopUpModal).click();
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("error no pop-up or issue closing pop up");
        }
    }

    public static void errorScreenshot(WebDriver driver, String orderID) {
        // Take screenshot and store as a file format
        // WaitForPageToLoad(30);

        try {
            // now copy the screenshot to desired location using copyFile
            // //method
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            FileUtils.copyFile(src,
                    new File("C:\\Users\\Edge\\Desktop\\Reports\\Screenshots\\" + orderID + ".png")); // System.currentTimeMillis()
        } catch (Exception e) {
            System.out.println("Screenshot failed");
            e.printStackTrace();
        }
    }

    public static void ErrScreenshotCapture(WebDriver driver) {
        // Take screenshot and store as a file format
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            // now copy the screenshot to desired location using copyFile
            // //method
            FileUtils.copyFile(src, new File("C:\\Users\\ashsaxen\\Downloads\\errorScreenshotGFS\\error.png"));
        } catch (Exception e) {
            System.out.println("take screenshot failure");
            e.printStackTrace();
        }

    }

    public static boolean isIframePresent(WebDriver driver) throws InterruptedException {
        //
        // driver.findElement(By.xpath("//html/body/table/tbody/tr[2]/td[1]/div/div[2]/table/tbody/tr[1]/td/input")).click();
        Thread.sleep(3000);
        // List to get & store frame
        List<WebElement> ele = driver.findElements(By.tagName("iframe"));
        System.out.println("Number of frames in a page :" + ele.size()); // ele.size
        // -
        // size
        // of
        // frame
        // list

        if (ele.size() == 0) {
            System.out.println("No frames on this page");
            return false; // No frames
        } else {
            System.out.println("Frames present on this page, Below are the details -");

            for (WebElement el : ele) {
                // Returns the Id of a frame
                System.out.println("Frame Id :" + el.getAttribute("id"));
                // Returns the Name of a frame.
                System.out.println("Frame name :" + el.getAttribute("name"));
            }
            return true; // frames present
        }

    }
}
