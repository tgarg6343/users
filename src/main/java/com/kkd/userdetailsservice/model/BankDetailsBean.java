package com.kkd.userdetailsservice.model;

public class BankDetailsBean {

	private String accountName;
	private String accountNo;
	private String ifscCode;

	public BankDetailsBean() {
		super();
	}

	public BankDetailsBean(String accountName, String accountNo, String ifscCode) {
		super();
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.ifscCode = ifscCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Override
	public String toString() {
		return "BankDetailsBean [accountName=" + accountName + ", accountNo=" + accountNo + ", ifscCode=" + ifscCode
				+ "]";
	}

}
