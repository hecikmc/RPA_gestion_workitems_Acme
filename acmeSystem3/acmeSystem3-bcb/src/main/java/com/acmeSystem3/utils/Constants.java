package com.acmeSystem3.utils;

public class Constants{
	//ALIAS SECRETSVAULT
	public static final String ALIAS_ACME = "loginAcces";
	public static final String ALIAS_BBDD = "aliasbd";
	
	//URLS 
    public static final String URL_LOGIN = "https://acme-test.uipath.com/login";
    public static final String URL_WORKITEMS = "https://acme-test.uipath.com/work-items";
    public static final String URL_WORKITEMDETAILS = "https://acme-test.uipath.com/work-items/";
    public static final String URL_WORKITEMUPDATE = "https://acme-test.uipath.com/work-items/update/";
    
    //XPATHs
    	//AcmeLogin WebElements
    public static final String BOX_USER = "//*[@id=\"email\"]";
    public static final String BOX_PWD = "//*[@id=\"password\"]";
    public static final String BUTTON_LOGIN = "/html/body/div/div[2]/div/div/div/form/button";
    
    	//AcmeWorkItems WebElements
    public static final String MENU_WORKITEMS = "/html/body/div/div[2]/div/div[2]/a/button";
    public static final String TABLE_WORKITEMS = "/html/body/div/div[2]/div/table";
    public static final String PAGES_WORKITEMS = "/html/body/div/div[2]/div/nav";
    
    	//AcmeWorkItemsDetails WebElements
    public static final String CUSTOMER_ID = "/html/body/div/div[2]/div/div[2]/div/div/div[1]/p";
    
    	//AcmeWorkItemsUpdate WebElements
    public static final String BOX_COMMENTS = "//*[@id=\"newComment\"]";
    public static final String COMBO_STATUS = "//*[@id=\"loginForm\"]/div[2]/div/div/button";
    public static final String COMBO_COMPLETED = "//*[@id=\"loginForm\"]/div[2]/div/div/div/ul/li[4]/a/span[1]";
    public static final String COMBO_REJECTED = "//*[@id=\"loginForm\"]/div[2]/div/div/div/ul/li[3]/a/span[1]";
    public static final String BUTTON_UPDATE = "//*[@id=\"buttonUpdate\"]";
    
    //BBDD
    public final static String JDBC_URL = "jdbc:sqlserver://localhost:11433;databaseName=wfdb;schema=ds";
    
    //APLICACION DESKTOP (SYSTEM3)
    public final static String PATH_EXE = "C:\\Users\\jesme\\Desktop\\RPA\\PARTE TECNICA\\EJERCICIOS\\FINAL\\Proyecto_Cuentas_Activas_WF\\Acme System 3\\ACME-System3.exe";
    public final static String WINDOWS = "[class=\"WindowsForms10.Window.8.app.0.141b42a_r8_ad1\"][title=\"ACME System3\"]";
    public final static String WINDOWS2 = "[class=\"WindowsForms10.Window.8.app.0.141b42a_r8_ad1\"][title=\"ACME System 3\"]";

    	//UiELEMENTS
    public final static String UI_USER_BOX = "[class=\"WindowsForms10.EDIT.app.0.141b42a_r8_ad1\"][name=\"textBox1\"]";
    public final static String UI_PWD_BOX = "[class=\"WindowsForms10.EDIT.app.0.141b42a_r8_ad1\"][name=\"textBox2\"]";
    public final static String UI_BUTTON_LOGIN = "[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"button1\"]";
    public final static String UI_MENU_NAV = "[class=\"WindowsForms10.Window.8.app.0.141b42a_r8_ad1\"][name=\"menuStrip1\"]";
    public final static String UI_CLIENTS_MENU = ".MenuItem[name=\"Clients\"]";
    public final static String UI_BOX_SEARCH = "[class=\"WindowsForms10.EDIT.app.0.141b42a_r8_ad1\"]";
    public final static String UI_BUTTON_SEARCH = "[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"button1\"]";
    public final static String UI_CHECK_INACTIVE = "[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"checkBox1\"]";
    public final static String UI_CLIENT_ITEM = "[class=\"WindowsForms10.SysListView32.app.0.141b42a_r8_ad1\"]";
    public final static String UI_BUTTON_ACCOUNTS = "[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"button1\"]";
    public final static String UI_LIST_ACCOUNTS = "[class=\"WindowsForms10.SysListView32.app.0.141b42a_r8_ad1\"]";

}    