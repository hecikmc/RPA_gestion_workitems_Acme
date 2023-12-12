package com.acmeSystem3.rpa;
import static com.acmeSystem3.utils.Constants.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AcmeWorkItemUpdatePage {
	@FindBy (xpath = BOX_COMMENTS)
	private WebElement boxComments;
	
	@FindBy (xpath = COMBO_STATUS)
	private WebElement combo_status;
	
	@FindBy (xpath = COMBO_COMPLETED)
	private WebElement combo_completed;
	
	@FindBy (xpath = COMBO_REJECTED)
	private WebElement combo_rejected;
	
	@FindBy (xpath = BUTTON_UPDATE)
	private WebElement button_update;
	
	/**
	 * Inserta el atributo comentario en la caja correspondiente
	 * @param workItem
	 */
	public void updateComments(String comment) {
		boxComments.clear();
		boxComments.sendKeys(comment);	
	}
	
	/**
	 * Selecciona el estado del desplegable como Rejected
	 */
	public void updateStatusRejected() {
		combo_status.click();
		combo_rejected.click();
	}
	
	/**
	 * Selecciona el estado del desplegable como Completed
	 */
	public void updateStatusCompleted() {
		combo_status.click();
		combo_completed.click();
	}
	
	/**
	 * Pulsa el boton de actualizar el item
	 */
	public void finishUpdate() {
		button_update.click();
	}

}
