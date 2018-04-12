package com.kkd.userdetailsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kkd.userdetailsservice.model.AadharBean;
import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.mongodb.MongoClient;

@SpringBootApplication
public class UserDetailsServiceApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceApplication.class);

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserDetailsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CustomerBean customerBean = new CustomerBean("kdCustId", "mobileNo", "password", "firstName", null, null, null,
				false);
		customerRepository.save(customerBean);

		FarmerBean farmerBean = new FarmerBean("kkd58", "mobileNo", "password", "alternateNo", null, new AddressBean(),
				null, true, new AadharBean(), false);
		farmerRepository.save(farmerBean);

	}
}
