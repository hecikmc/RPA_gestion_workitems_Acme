package com.acmeSystem3.rpa;

import static com.acmeSystem3.utils.Constants.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AcmeWorkItemDetailsPage {
	
	@FindBy(xpath = CUSTOMER_ID)
	private WebElement customer_id;
	
	/**
	 * Devuelve el parrafo que contiene el customer ID en la pagina del concreta de un workitem
	 * @return
	 */
	public WebElement getCustomer() {
		return customer_id;
	}

}
