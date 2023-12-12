package com.acmeSystem3.rpa;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.workfusion.bot.service.SecureEntryDTO;

import static com.acmeSystem3.utils.Constants.*;

public class AcmeLoginPage {
	
	@FindBy(xpath = BOX_USER)
    private WebElement userBox;
    
	@FindBy(xpath = BOX_PWD)
	private WebElement pwdBox;
	
	@FindBy(xpath = BUTTON_LOGIN)
	private WebElement loginButton;
	
	/**
	 * Interactua con los elementos web que estan involucrados en la web para hacer login
	 * @param login Credenciales de acceso a la web (usuario y contraseña)
	 */
	public void loginAcme(SecureEntryDTO login) {
		userBox.clear();
		pwdBox.clear();
		userBox.sendKeys(login.getKey());
		pwdBox.sendKeys(login.getValue());
		loginButton.click();
	}
}