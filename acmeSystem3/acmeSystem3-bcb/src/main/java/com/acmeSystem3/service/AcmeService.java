package com.acmeSystem3.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.acmeSystem3.models.Customer;
import com.acmeSystem3.models.WorkItem;
import com.acmeSystem3.models.dao.ManagerDDBB;
import com.acmeSystem3.rpa.AcmePlaneRobotRpa;

import static com.acmeSystem3.utils.Constants.*;

public class AcmeService {
	private List<WorkItem> workItemList = new ArrayList<>();
	
	
	/**
	* Recoge de la web todos los work items a tratar (cuyo estado sea Open y su descripcion 
	* coincida con Verify Account Position
	* @param webRobot Instancia del planificador de la web, que interactua con los elementos Webs de cada pagina
	* @return lista con todos los workitems localizados (cada workitem tiene: Id, pagina en la que estaba, fila 
	* en la que se encontro
	*/
	public List<WorkItem> getTotalCasesPending(AcmePlaneRobotRpa webRobot, ManagerDDBB managerDDBB){
		try {
			List<WebElement> pages = webRobot.getTotalPages();
			int totalPages = pages.size() - 2;
			for (int i = 2; i <= totalPages + 1; i++) {
				getCasesPending(webRobot.getCurrentTable(), i - 1, webRobot);
				if (i <= totalPages)
					webRobot.changeUrl(URL_WORKITEMS + "?page=" + i);
			}
			removeDuplicates(managerDDBB);
			searchCustomerWork(webRobot);
			return (workItemList);
		}
		catch (Exception e){
			e.printStackTrace();
			return (workItemList);
		}
	}

	/**
	 * Elimina de la lista de WorkItems a tratar los que ya existen en la cola y han sido procesados previamente.
	 * @param managerDDBB gestor de la BBDD que comprueba si ese workItem ya existe en la base de datos
	 */
	private void removeDuplicates(ManagerDDBB managerDDBB) {
		Iterator<WorkItem> iterator = workItemList.iterator();
		while (iterator.hasNext()) {
		    WorkItem workItem = iterator.next();
		    if (managerDDBB.exist(workItem))
		        iterator.remove();
		}
	}

	/**
	 * Extrae cada workItem de una tabla en concreto con el criterio status 'open' y descripcion 
	 * 'verify account position'
	 * @param table Webelement tabla que contiene los datos a buscar
	 * @param page numero de pagina en la que estamos scrapeando la tabla
	 * @param robot Instancia del planificador de la web, que interactua con los elementos Webs de cada pagina
	 */
	private void getCasesPending(WebElement table, int page, AcmePlaneRobotRpa robot) {
		try {
			List<WebElement> columnWIID = table.findElements(By.xpath(".//tr/td[2]"));
			List<WebElement> columnDescription = table.findElements(By.xpath(".//tr/td[3]"));
			List<WebElement> columnStatus = table.findElements(By.xpath(".//tr/td[5]"));
			int i = 0;
			for (WebElement element : columnDescription) {
				if (element.getText().equals("Verify Account Position") && columnStatus.get(i).getText().equals("Open")) {
					WorkItem workItem = new WorkItem(Integer.valueOf(columnWIID.get(i).getText()), i + 2, page);
					workItemList.add(workItem);
				}			
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Busca el id de cliente asociado a cada workitem 
	 * @param robot Instancia del planificador de la web, que interactua con los elementos Webs de cada pagina
	 * @return modifica la lista de workitems y añade el parametro Customer asociado
	 */
	private List<WorkItem> searchCustomerWork(AcmePlaneRobotRpa robot) {
		try {
			for (WorkItem workitem : workItemList) {
				Pattern pattern = Pattern.compile("Client ID: (\\w+)");
				robot.changeUrl(URL_WORKITEMDETAILS + workitem.getWiid());
				WebElement customerId = robot.getCustomerID();
				Matcher matcher = pattern.matcher(customerId.getText());
				if (matcher.find()) {
					Customer newCustomer = new Customer(matcher.group(1));
					workitem.setCustomer(newCustomer);
				}
			}
			return workItemList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return workItemList;
		}

	}

	/**
	 * Busca en la web el ID del cliente asociado a cada workItem de la lista recibida por parametros
	 * @param robot gestor de la capa web Acme
	 * @param workItemList lista de workItems a buscar
	 * @return lista con de workItems incluyendo el atributo Customer asociado a cada item
	 */
	public List<WorkItem> searchCustomerWork(AcmePlaneRobotRpa robot, List<WorkItem> workItemList) {
		try {
			for (WorkItem workitem : workItemList) {
				Pattern pattern = Pattern.compile("Client ID: (\\w+)");
				robot.changeUrl(URL_WORKITEMDETAILS + workitem.getWiid());
				WebElement customerId = robot.getCustomerID();
				Matcher matcher = pattern.matcher(customerId.getText());
				if (matcher.find()) {
					Customer newCustomer = new Customer(matcher.group(1));
					workitem.setCustomer(newCustomer);
					workitem.setStatus_queue("D_1");
				}
			}
			return workItemList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return workItemList;
		}

	}
	
	/**
	 * Vuelca y actualiza los datos de cuentas activas a la web Acme para cada workItem a tratar
	 * Tipifica cada caso como open o reject en función de si tiene cuentas activas o no. 
	 * Cambia su estado a P una vez han sido registrados los datos en la Web. 
	 * @param workItemList Lista de WorkItems a actualizar
	 * @param webRobot manager de la web que interactua con los elementos de la pagina de actualización
	 */
	public void updateWorkItem(List<WorkItem> workItemList, AcmePlaneRobotRpa webRobot) {
		try {
			for (WorkItem workItem: workItemList) {
				builtComment(workItem);
				webRobot.changeUrl(URL_WORKITEMUPDATE + workItem.getWiid());
				webRobot.acmeWorkItemUpdatePage.updateComments(workItem.getReport_comment());
				if (workItem.getStatus())
					webRobot.acmeWorkItemUpdatePage.updateStatusCompleted();
				else
					webRobot.acmeWorkItemUpdatePage.updateStatusRejected();
				webRobot.acmeWorkItemUpdatePage.finishUpdate();
				workItem.setStatus_queue("P");
			}			
		}
		catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Construye en un string el comentario que hay que reportar y volcar en la web Acme, en funcion
	 * de las cuentas activas del cliente. Las cuentas estan escritas originalmente en ese mismo atributo
	 * y contiene unicamente el id de cada cuenta activa separada por '-'
	 * @param workItems Lista de todos los workitems a tratar y añade el campo comentario a cada workitem
	 */
	private void builtComment(WorkItem workItem) {
		if (workItem.getReport_comment().isEmpty()) {
			workItem.setReport_comment("No existen cuentas activas para este cliente");
			workItem.setStatus(false);
		}
		else {
			String[] accounts = workItem.getReport_comment().split("-");
			workItem.setReport_comment("Existe cuenta/s activa/s: ");
			int i = 0;
			while (i < accounts.length) {
				workItem.addAccountReportComment(accounts[i]);
				if (i < accounts.length - 1)
					workItem.addAccountReportComment(", ");
				i++;
			}
			workItem.setStatus(true);
		}
	}
}

