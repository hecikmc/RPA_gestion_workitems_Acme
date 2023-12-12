package com.acmeSystem3.models;

public class WorkItem {
	private int	wiid;
	private int	row;
	private int page;
	private String report_comment;
	private Customer customer;
	private Boolean status;
	private String status_queue;

	public WorkItem(int wiid, int row, int page) {
		this.wiid = wiid;
		this.row = row;
		this.page = page;
	}

	public WorkItem() {	}

	public int getWiid() {
		return wiid;
	}

	public void setWiid(int wiid) {
		this.wiid = wiid;
	}

	public String getReport_comment() {
		return report_comment;
	}

	public void setReport_comment(String report_comment) {
		this.report_comment = report_comment;
	}

	public void addAccountReportComment(String account) {
		this.report_comment = this.report_comment.concat(account);
	}
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getStatus_queue() {
		return status_queue;
	}

	public void setStatus_queue(String status_queue) {
		this.status_queue = status_queue;
	}

	@Override
	public String toString() {
		return "WorkItem [wiid=" + wiid + ", report_comment=" + report_comment + ", row=" + row + ", page=" + page + ", customer="
				+ customer + "]";
	}
}
