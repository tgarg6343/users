package com.kkd.userdetailsservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;

@RestController
public class UserController {

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/farmer/user")
	public ResponseEntity<HttpStatus> addFarmer(@RequestBody FarmerBean farmer) {

		if (farmerRepository.findById(farmer.getKkdFarmId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		farmerRepository.save(farmer);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@PostMapping("/customer/user")
	public ResponseEntity<HttpStatus> addCustomer(@RequestBody CustomerBean customer) {

		if (customerRepository.findById(customer.getkkdCustId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		customerRepository.save(customer);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@GetMapping("/farmer/{id}")
	public List<FarmerBean> getFarmer(@PathVariable String id) {
		return farmerRepository.findAll();
	}

	@GetMapping("/customer/{id}")
	public List<CustomerBean> getCustomer(@PathVariable String id) {
		return customerRepository.findAll();
	}

	@PutMapping("/farmer/user")
	public ResponseEntity<HttpStatus> updateFarmer(@RequestBody FarmerBean farmer) {

		if (farmerRepository.findById(farmer.getKkdFarmId()).isPresent()) {
			farmerRepository.save(farmer);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@PutMapping("/customer/user")
	public ResponseEntity<HttpStatus> updateCustomer(@RequestBody CustomerBean customer) {

		if (customerRepository.findById(customer.getkkdCustId()).isPresent()) {
			customerRepository.save(customer);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

}
