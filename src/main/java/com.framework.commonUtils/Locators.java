package com.framework.commonUtils;

import org.openqa.selenium.By;

public class Locators {
    public static By loc_userNameDiscovery = By.xpath("//input[@data-id='txt_login_email']");
    public static By loc_nextDiscovery = By.xpath("//button[@data-id='btn_next']");
    public static By loc_userName = By.id("okta-signin-username");
    public static By loc_password = By.id("okta-signin-password");
    public static By loc_login = By.id("okta-signin-submit");

    public static By loc_lists = By.xpath("//div[normalize-space()='Lists']"); // https://shop.sysco.com/app/lists
//    public static String  listItems = "//div[contains(@data-dd-action-name,'%s')]";

    public static By loc_seeAllLists = By.xpath("//div[contains(@data-dd-action-name,'See All Lists')]");
    public static By loc_allListDropIcon = By.xpath("//div[@data-id='list_dropdown_wrapper']");
    public static By loc_allListValues = By.xpath("//span[contains(@data-id,'listDropdownItem-')]");


    public static By loc_moreActions = By.xpath("//button[@data-id='more-actions-btn']");
    public static By loc_exportList = By.xpath("//li[@data-id='export-list-btn']");
    public static By loc_includePrices = By.xpath("//input[@data-id='export-list-modal-price-toggle']/following-sibling::div");
    public static By loc_inputFileName = By.xpath("//input[@data-id='export-list-modal-file-name-input']");
    public static By loc_btnExport = By.xpath("//button[@data-id='export-list-modal-export-btn']");
    public static By loc_btnCancelExport = By.xpath("//button[data-id='export-list-modal-cancel-btn']");

    public static By loc_lnkProfile = By.xpath("//div[@data-id='profile-button-container']");
    public static By loc_signOut = By.xpath("//li[normalize-space()='Log Out']");
    public static By loc_confirmLogout = By.xpath("//button[@id='cart-alert-yes-button']");

    // select account - search
    public static By loc_accountDdlBtn = By.xpath("//div[@data-id='topPanel-dropdown-button-globalCustomerSelection']/button");
    public static String loc_accountName = "//div[@data-id='topPanel-dropdown-button-globalCustomerSelection']/label/div[contains(text(),'acNum')]";
    public static By loc_accountSearchBtn = By.xpath("//input[contains(@placeholder,'Search for Customer or Site')]");
    public static By loc_accountNumDash = By.xpath("//div[@data-id='topPanel-dropdown-button-globalCustomerSelection']//div[contains(.,'acNum')]");
    public static By loc_firstAccount = By.xpath("//div[@data-id='globalCustomerSelectFlyout-customerList-label-customerInfoCard'][1]");
    static String loc_accountNum = "//div[@data-id='globalCustomerSelectFlyout-customerList-label-customerInfoCard']//p[normalize-space()='#accountName']";
    static By loc_accountSearchBtn1 = By.xpath("//input[@data-id='txt_account_search_customer']");
    //ddl all acNums
    public static By loc_allAcNums = By.xpath("//p[@data-id='globalCustomerSelectFlyout-dropdown-label-customerId']");
    //list buttons
    public static By loc_btnListNames = By.xpath("//button[@data-id='my-lists-toggle-button']/following-sibling::*//button");
    // pop up modal
    public static By loc_popUpModal = By.xpath("//div[@class='modal-content modal-lg']");
//    public static By loc_dismissPopUpModal = By.xpath("//button[@aria-label='Close']//div[@class='icon icon-primary icon-md']");
    public static By loc_dismissPopUpModal = By.xpath("//span[starts-with(@class,'intercom-post-close')]");
}
