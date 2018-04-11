package com.kkd.userdetailsservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "users")
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
	@JsonInclude(Include.NON_DEFAULT)
	private boolean autoConfirm;
	private AadharBean aadharData;
	public FarmerBean() {
		super();
	}
	public FarmerBean(String kkdFarmId, String mobileNo, String password, String alternateNo, List<String> cities,
			AddressBean currentAddress, String status, boolean autoConfirm, AadharBean aadharData) {
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
	public boolean isAutoConfirm() {
		return autoConfirm;
	}
	public void setAutoConfirm(boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
	}
	public AadharBean getAadharData() {
		return aadharData;
	}
	public void setAadharData(AadharBean aadharData) {
		this.aadharData = aadharData;
	}
	@Override
	public String toString() {
		return "FarmerBean [kkdFarmId=" + kkdFarmId + ", mobileNo=" + mobileNo + ", password=" + password
				+ ", alternateNo=" + alternateNo + ", cities=" + cities + ", currentAddress=" + currentAddress
				+ ", status=" + status + ", autoConfirm=" + autoConfirm + ", aadharData=" + aadharData + "]";
	}
		
}
