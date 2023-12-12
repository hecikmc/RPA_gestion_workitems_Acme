package com.acmeSystem3.rpa;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.rpa.driver.Driver;
import com.workfusion.rpa.helpers.RPA;

import static com.acmeSystem3.utils.Constants.*;

public class AcmePlaneRobotRpa{
	private final Driver driver;
    private final SecureEntryDTO credentials;
    
    private final AcmeLoginPage acmeLoginPage;
    private final AcmeWorkItemsPage acmeWorkItemsPage;
    private final AcmeWorkItemDetailsPage acmeWorkItemDetailsPage;
    public final AcmeWorkItemUpdatePage acmeWorkItemUpdatePage;
    
    
    /**
    * Constructor de la clase, requiere las credenciales de acceso a la web en formato SecureEntryDTO
    * y el driver de la ejecucion. 
    * @param driver
    * @param credentials
    */
    public AcmePlaneRobotRpa(Driver driver, SecureEntryDTO credentials)
    {
    	this.driver = Objects.requireNonNull(driver);
        this.credentials = Objects.requireNonNull(credentials);

        setOptions();
        
        acmeLoginPage = PageFactory.initElements(driver, AcmeLoginPage.class);
        acmeWorkItemsPage = PageFactory.initElements(driver, AcmeWorkItemsPage.class);
        acmeWorkItemDetailsPage = PageFactory.initElements(driver, AcmeWorkItemDetailsPage.class);
        acmeWorkItemUpdatePage = PageFactory.initElements(driver, AcmeWorkItemUpdatePage.class);
    }
    
    /**
    * Va a la pagina de login Acme y accede con las credenciales asignadas a la instancia.
    */
    public void login() {
    	try {
    		driver.navigate().to(URL_LOGIN);
        	acmeLoginPage.loginAcme(this.credentials);
        	RPA.sleep(1000);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
    * Accede a una URL en concreto con el navegador actual
    * @param urlSearch String con la URL a la que ir
    */
	public void changeUrl(String urlSearch) {
		driver.navigate().to(urlSearch);
	}

	/**
	 * Devuelve la lista de WebElements que corresponde al total de paginas de casos cargados
	 * en la pagina de WorkItems
	 * @return
	 */
	public List<WebElement> getTotalPages() {
		try {
			changeUrl(URL_WORKITEMS);
			WebElement list = acmeWorkItemsPage.getPages();
			List<WebElement> pages = list.findElements(By.className("page-numbers"));
			return pages;
		}
		catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
	}
	
	/**
	 * Devuelve el parrafo de la web AcmeWorkItemsDetails que contiene el id del cliente
	 * @return
	 */
	public WebElement getCustomerID() {
		try {
			return acmeWorkItemDetailsPage.getCustomer();			
		}
		catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
	}
	
	/**
	 * Devuelve la tabla actual de datos de la pagina AcmeWorkItems
	 * @return
	 */
	public WebElement getCurrentTable() {
		try {
			return (acmeWorkItemsPage.getTable());			
		}
		catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
	}
	
	/**
	 * Configura el navegador web con Firefox. Se establece tiempo de espera para los elementos web al buscarlos antes de arrojar una excepcion
	 * de NoSuchElementException (10segundos). Se establece tiempo de espera para cargar la pagina antes de arrojar excepcion (90segundos).
	 * Se eliminan las cookies del navegador. 
	 */
	private void setOptions() {
		try {
			this.driver.switchDriver(RpaDriver.FIREFOX.getName());
			Driver.Options options = driver.manage();
			options.timeouts()
			.implicitlyWait(10, TimeUnit.SECONDS)
			.pageLoadTimeout(90, TimeUnit.SECONDS);
			
			options.deleteAllCookies();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
    * Cierra el navegador web
    */
    public void close() {
    	driver.close();
    }

}
