package com.acmeSystem3.rpa;

import static com.acmeSystem3.utils.Constants.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AcmeWorkItemsPage {
	@FindBy(xpath = MENU_WORKITEMS)
	private WebElement menu_workitems;
	
	@FindBy(xpath = TABLE_WORKITEMS)
    private WebElement table;
	
	@FindBy(xpath = PAGES_WORKITEMS)
    private WebElement pages;
	
	
	public void clickWorkItems() {
		menu_workitems.click();
	}
	
	/**
	 * Devuelve la tabla de datos de la pagina donde estan todos los WorkItem
	 * @return
	 */
	public WebElement getTable() {
		return table;
	}

	/**
	 * Devuelve la lista de paginas con datos de workItems
	 * @return
	 */
	public WebElement getPages() {
		return pages;
	}

}
