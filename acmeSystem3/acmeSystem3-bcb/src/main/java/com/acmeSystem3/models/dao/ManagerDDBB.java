package com.acmeSystem3.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.acmeSystem3.models.Customer;
import com.acmeSystem3.models.WorkItem;
import com.workfusion.bot.service.SecureEntryDTO;


import static com.acmeSystem3.utils.Constants.*;

public class ManagerDDBB {
    private static Connection connection;
    
    /**
     * Constructor que crea la conexion con la bbdd, consulta si ya existe la cola creada, 'workitems_queue', si no existe la crea. 
     * @param login usuario y pwd de acceso al servidor de jdbc
     */
    @Inject
    public ManagerDDBB(SecureEntryDTO login) {
		try {
			connection = DriverManager.getConnection(JDBC_URL, login.getKey(), login.getValue());
			Statement statement = connection.createStatement();
			statement.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.tables WHERE name= 'workitems_queue') " +
	                "BEGIN " +
	                "CREATE TABLE workitems_queue (wiid VARCHAR(20) UNIQUE, customer_id VARCHAR(20), page INTEGER, row INTEGER, report_comments VARCHAR(80), status_queue VARCHAR(30)); " +
	                "END;");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
    }
    
    /**
     * Cierra la conexion con la bbdd
     */
	public void desconexionDDBB() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserta en la tabla los valor de la lista cada workitem: wiid, customer_id, page, row, report_comments, status queue.
	 * @param workItems Lista de objetos workitems a insertar
	 */
	public void insertDatas(List<WorkItem> workItems) {
		PreparedStatement preparedStatement;
		for(WorkItem workItem : workItems) {
				try {
				preparedStatement = connection.prepareStatement("INSERT INTO workitems_queue (wiid, customer_id, page, row, report_comments, status_queue) VALUES (?,?,?,?,?,?)");
				preparedStatement.setString(1, String.valueOf(workItem.getWiid()));
				if (workItem.getCustomer() == null) {
					preparedStatement.setString(2, "");
					preparedStatement.setString(6, "D_1_SE");
				}
				else {
					preparedStatement.setString(2, workItem.getCustomer().getCustomerID());
					preparedStatement.setString(6, "D_1");
				}
				preparedStatement.setInt(3, workItem.getPage());
				preparedStatement.setInt(4, workItem.getRow());
				preparedStatement.setString(5, "");
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Extrae los clientes sin repeticiones de la cola que se encuentren en estado D_1 para ser procesador por el Dispatcher_2
	 * @return Lista de objetos Customer con el id del cliente
	 */
	public List<Customer> extractCustomers() {
		List<Customer> customerList = new ArrayList<Customer>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT customer_id, status_queue FROM workitems_queue WHERE status_queue = ?");
			preparedStatement.setString(1, "D_1");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				 Customer newCustomer = new Customer();
				 newCustomer.setCustomerID(result.getString(1));
				 newCustomer.setStatus_queue(result.getString(2));
				 customerList.add(newCustomer);
			}
			preparedStatement.close();
			return customerList;
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Extrae todos los workitem de la cola que se encuentren en estado D_2 para ser procesados por el Performer. 
	 * @return lista de objetos WorkItem
	 */
	public List<WorkItem> extractWorkItems(){
		List<WorkItem> workItemList = new ArrayList<WorkItem>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT wiid, customer_id, page, row, report_comments, status_queue FROM workitems_queue WHERE status_queue = ?");
			preparedStatement.setString(1, "D_2");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				 WorkItem newWorkItem = new WorkItem();
				 newWorkItem.setWiid(Integer.valueOf(result.getString(1)));
				 Customer newCustomer = new Customer(result.getString(2));
				 newWorkItem.setCustomer(newCustomer);
				 newWorkItem.setPage(result.getInt(3));
				 newWorkItem.setRow(result.getInt(4));
				 newWorkItem.setReport_comment(result.getString(5));
				 newWorkItem.setStatus_queue(result.getString(6));
				 workItemList.add(newWorkItem);
			}
			preparedStatement.close();
			return workItemList;
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Actualiza los datos en cola de los clientes con las cuentas localizadas como activas y un nuevo estado D_2, lo gestiona en todos los items
	 * en estado D_1 y que han sido procesados por el Dispatcher 2. Si algun item se ha quedado sin procesar por el Dispatcher 2 y no tiene las cuentas
	 * de los clientes, en ese caso deja el estado como D_1
	 * @param customerList lista de clientes a actualizar
	 */
	public void updateCustomers(List<Customer> customerList) {
		try {
			for(Customer customer : customerList) {
				PreparedStatement preparedStatement = connection.prepareStatement("UPDATE workitems_queue SET report_comments = ?, status_queue = ? WHERE customer_id = ? AND status_queue = ?");
				preparedStatement.setString(1, customer.getAccounts());
				preparedStatement.setString(2, customer.getStatus_queue());
				preparedStatement.setString(3, customer.getCustomerID());
				preparedStatement.setString(4, "D_1");
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza todos los workItem que han sido procesados por el performer que se encontraban en estado D_2
	 * Actualizando su estado a P si se han volcado los datos con las cuentas activas a la Web Acme.  
	 * @param workItemList
	 */
	public void updateWorkItem(List<WorkItem> workItemList) {
		try {
			for(WorkItem workItem : workItemList) {
				PreparedStatement preparedStatement = connection.prepareStatement("UPDATE workitems_queue SET status_queue = ? WHERE wiid = ? AND status_queue = ?");
				preparedStatement.setString(1, workItem.getStatus_queue());
				preparedStatement.setString(2, String.valueOf(workItem.getWiid()));
				preparedStatement.setString(3, "D_2");
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Actualiza en la cola los datos de los WorkItems que han sido procesados por el Dispatcher 1 y que se encontraban en estado de error (D_1_SE), 
	 * actualiza su estado a D_2 si contiene los datos de los ID de cliente y completa ese campo ID en la cola. 
	 * @param workItemList
	 */
	public void updateWorkItemError(List<WorkItem> workItemList) {
		try {
			for(WorkItem workItem : workItemList) {
				PreparedStatement preparedStatement = connection.prepareStatement("UPDATE workitems_queue SET status_queue = ?, customer_id = ? WHERE wiid = ?");
				preparedStatement.setString(1, workItem.getStatus_queue());
				preparedStatement.setString(2, workItem.getCustomer().getCustomerID());
				preparedStatement.setString(3, String.valueOf(workItem.getWiid()));
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extrae de la cola una lista de WorkItems que se encuentran en estado de error D_1_SE.
	 * @return Lista de workItems en estado D_1_SE
	 */
	public List<WorkItem> extractSystemErrorCases() {
		List <WorkItem> listSystemError = new ArrayList<WorkItem>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT wiid, customer_id, page, row, report_comments, status_queue FROM workitems_queue WHERE status_queue = ?");
			preparedStatement.setString(1, "D_1_SE");
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				 WorkItem newWorkItem = new WorkItem();
				 newWorkItem.setWiid(Integer.valueOf(result.getString(1)));
				 Customer newCustomer = new Customer(result.getString(2));
				 newWorkItem.setCustomer(newCustomer);
				 newWorkItem.setPage(result.getInt(3));
				 newWorkItem.setRow(result.getInt(4));
				 newWorkItem.setReport_comment(result.getString(5));
				 newWorkItem.setStatus_queue(result.getString(6));
				 listSystemError.add(newWorkItem);
			}
			preparedStatement.close();
			return listSystemError;
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Comprueba si ya existe un workitem en la cola. 
	 * @param workItem instancia a comprobar
	 * @return true si existe, false si no existe.
	 */
	public boolean exist(WorkItem workItem) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT wiid FROM workitems_queue WHERE wiid = ?");
			preparedStatement.setString(1, String.valueOf(workItem.getWiid()));
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) 
			    return true;
			else
			    return false;
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}    
}
