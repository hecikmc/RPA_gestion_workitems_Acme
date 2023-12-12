package com.acmeSystem3.service;

import static com.acmeSystem3.utils.Constants.*;

import java.util.List;
import java.util.Objects;

import com.acmeSystem3.models.Customer;
import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.rpa.helpers.RPA;
import com.workfusion.rpa.helpers.UiElement;
import com.workfusion.rpa.helpers.UiElementCollection;

import static com.workfusion.rpa.helpers.RPA.$;

public class AcmeSystem3Service {
    private final SecureEntryDTO credentials;

	public AcmeSystem3Service(SecureEntryDTO credentials) {
        this.credentials = Objects.requireNonNull(credentials);
	}
	
	/**
	 * Ejecuta la aplicacion System3 y realiza el login con las credenciales de la instancia
	 */
    public void login() {
    	try {
    		RPA.open(PATH_EXE);
        	RPA.switchTo().window(WINDOWS);
        	UiElement userBox = $(UI_USER_BOX);
            UiElement passwordBox = $(UI_PWD_BOX);
            userBox.clear();
            userBox.setText(credentials.getKey());
            passwordBox.clear();
            passwordBox.setText(credentials.getValue());
            RPA.pressTab();
            RPA.pressEnter();
            RPA.sleep(5000);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
         
    }
    
    /**
     * Realiza la busqueda de una lista de clientes en la aplicacion System3, navega por la aplicacion
     * hasta la busqueda de clientes por ID y los busca uno a uno, añade el dato de cuentas activas encontradas
     * al atributo String del cliente 'accounts' y separa cada cuenta con guiones '-'
     * @param customerList Lista de clientes a buscar
     * @return La misma Lista de clientes con el atributo accounts relleno con el resultado de la busqueda en la aplicacion
     */
    public List<Customer> searchCustomers(List<Customer> customerList) {
    	try {
    		RPA.switchTo().window(WINDOWS2); 
        	UiElement menu_nav = $(UI_MENU_NAV);
            UiElement clients_menu = menu_nav.find(UI_CLIENTS_MENU);
            clients_menu.click();
            clients_menu.pressTab();
            clients_menu.pressTab();
            clients_menu.pressEnter();
            clients_menu.pressTab();
            clients_menu.pressEnter();
            RPA.sleep(3000);
            UiElement check_inactive = $(UI_CHECK_INACTIVE);
            check_inactive.click();
            for (Customer customer : customerList) {
            	UiElement box_search = $(UI_BOX_SEARCH);
            	UiElement button_search = $(UI_BUTTON_SEARCH);
                box_search.clear();
                box_search.setText(customer.getCustomerID());
                button_search.click();
                UiElement client = $(UI_CLIENT_ITEM);
                client.find(".ListItem").doubleClick();
                UiElement button_accounts = $(UI_BUTTON_ACCOUNTS);
                button_accounts.click();
                getActiveAccounts(customer);
                customer.setStatus_queue("D_2");
            }
            return customerList;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return customerList;
    	}
    	
    }
    
    /**
     * Recorre la tabla resultados de la busqueda de cuentas de un cliente y añade solo las cuentas con estado
     * 'active' al atributo account del cliente, separadas por guiones si encuentra una o mas.
     * @param customer cliente con el atributo accounts relleno con el resultado de cuentas activas encontradas.
     */
    private void getActiveAccounts(Customer customer){
    	try {
    		UiElementCollection accounts = RPA.$$("[CLASS:TextBlock]");
    		for (int i = 0; i < accounts.size(); i += 4) {
        		if (accounts.get(i + 3).getText().equals("Active")) {
        			customer.setAccounts(accounts.get(i).getText());
        		}
        	}
        	UiElement close = $("[CLASS:Button]");
        	close.click();
        	close.click();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
