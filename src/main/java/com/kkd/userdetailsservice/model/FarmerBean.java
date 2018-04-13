package com.kkd.userdetailsservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

@Document(collection = "farmer")
@JsonInclude(Include.NON_NULL)
public class FarmerBean {

	@Id
	private String kkdFarmId;
	private String mobileNo;
	private String password;
	private String alternateNo;
	private List<String> cities;
	private AddressBean currentAddress;
	private String status;
	private Boolean autoConfirm;
	private AadharBean aadharData;
	private Boolean deleted;
	private BankDetailsBean bankDetails;

	public FarmerBean() {
		super();
	}

	public FarmerBean(String kkdFarmId, String mobileNo, String password, String alternateNo, List<String> cities,
			AddressBean currentAddress, String status, Boolean autoConfirm, AadharBean aadharData, Boolean deleted,BankDetailsBean bankDetails) {
		super();
		this.kkdFarmId = kkdFarmId;
		this.mobileNo = mobileNo;
		this.password = password;
		this.alternateNo = alternateNo;
		this.cities = cities;
		this.currentAddress = currentAddress;
		this.status = status;
		this.autoConfirm = autoConfirm;
		this.aadharData = aadharData;
		this.deleted = deleted;
		this.bankDetails =bankDetails;
	}

	public String getKkdFarmId() {
		return kkdFarmId;
	}

	public void setKkdFarmId(String kkdFarmId) {
		this.kkdFarmId = kkdFarmId;
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

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public AddressBean getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(AddressBean currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getAutoConfirm() {
		return autoConfirm;
	}

	public void setAutoConfirm(Boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
	}

	public AadharBean getAadharData() {
		return aadharData;
	}

	public void setAadharData(AadharBean aadharData) {
		this.aadharData = aadharData;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public BankDetailsBean getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(BankDetailsBean bankDetails) {
		this.bankDetails = bankDetails;
	}

	@Override
	public String toString() {
		return "FarmerBean [kkdFarmId=" + kkdFarmId + ", mobileNo=" + mobileNo + ", password=" + password
				+ ", alternateNo=" + alternateNo + ", cities=" + cities + ", currentAddress=" + currentAddress
				+ ", status=" + status + ", autoConfirm=" + autoConfirm + ", aadharData=" + aadharData + ", deleted="
				+ deleted + ", bankDetails=" + bankDetails + "]";
	}

	


}
