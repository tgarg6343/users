package com.kkd.userdetailsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kkd.userdetailsservice.model.AadharBean;
import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;

@SpringBootApplication
public class UserDetailsServiceApplication implements CommandLineRunner{

	@Autowired
	private FarmerRepository farmerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(UserDetailsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CustomerBean customerBean=new CustomerBean("kdCustId", "mobileNo", "password", "firstName", null, null, null);
		customerRepository.save(customerBean);
		
		FarmerBean farmerBean=new FarmerBean("kkdFarmId", "mobileNo", "password", "alternateNo", null, new AddressBean(), null, true, new AadharBean());
		farmerRepository.save(farmerBean);	
	}
}
