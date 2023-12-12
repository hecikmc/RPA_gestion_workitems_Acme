package com.acmeSystem3.models;

public class Customer {
	private String 	customerID;
	private String	accounts;
	private String	status_queue;
	
	public Customer() {
		this.customerID = "";
		this.accounts = "";
	}
	
	public Customer(String customerID) {
		this.customerID = customerID;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String account) {
		this.accounts = this.accounts + account + "-";
	}
	
	public String getStatus_queue() {
		return status_queue;
	}

	public void setStatus_queue(String status_queue) {
		this.status_queue = status_queue;
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", accounts=" + accounts + ", status_queue=" + status_queue + "]";
	}

}
