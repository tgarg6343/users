package com.kkd.userdetailsservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "users")
@JsonInclude(Include.NON_EMPTY)
public class CustomerBean {

	@Id
	private String kkdCustId;
	private String mobileNo;
	private String password;
	private String firstName;
	private String lastName;
	private List<AddressBean> addresses;
	private AddressBean primaryAddress;
	private Boolean isDeleted;

	public CustomerBean() {
		super();
	}

	public CustomerBean(String kkdCustId, String mobileNo, String password, String firstName, String lastName,
			List<AddressBean> addresses, AddressBean primaryAddress, Boolean isDeleted) {
		super();
		this.kkdCustId = kkdCustId;
		this.mobileNo = mobileNo;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addresses = addresses;
		this.primaryAddress = primaryAddress;
		this.isDeleted = isDeleted;
	}

	public String getKkdCustId() {
		return kkdCustId;
	}

	public void setKkdCustId(String kkdCustId) {
		this.kkdCustId = kkdCustId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<AddressBean> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressBean> addresses) {
		this.addresses = addresses;
	}

	public AddressBean getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(AddressBean primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public Boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "CustomerBean [kkdCustId=" + kkdCustId + ", mobileNo=" + mobileNo + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", addresses=" + addresses
				+ ", primaryAddress=" + primaryAddress + ", isDeleted=" + isDeleted + "]";
	}

}
