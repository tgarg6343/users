package com.kkd.userdetailsservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.mongodb.MongoClient;

@RestController
public class UserController {

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private static final MongoOperations mongoOps = new MongoTemplate(
			new SimpleMongoDbFactory(new MongoClient(), "test"));

	/* to be deleted later */
	@PostMapping("/farmer/user")
	public ResponseEntity<HttpStatus> addFarmer(@RequestBody FarmerBean farmer) {

		if (farmerRepository.findById(farmer.getKkdFarmId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		farmerRepository.save(farmer);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	/* to be deleted later */
	@PostMapping("/customer/user")
	public ResponseEntity<HttpStatus> addCustomer(@RequestBody CustomerBean customer) {

		if (customerRepository.findById(customer.getKkdCustId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		customerRepository.save(customer);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	/* to be deleted later */

	@GetMapping("/farmer/id/{id}")
	public List<FarmerBean> getFarmer(@PathVariable String id) {
		return farmerRepository.findAll();
	}

	/* to be deleted later */
	@GetMapping("/customer/id/{id}")
	public List<CustomerBean> getCustomer(@PathVariable String id) {
		return customerRepository.findAll();
	}

	/* update details of a farmer using id */
	@PutMapping("/farmer/update/user/{id}")
	public ResponseEntity<FarmerBean> updateFarmer(@RequestBody FarmerBean farmer, @PathVariable String id) {

		if (farmerRepository.findById(id).isPresent()) {
			FarmerBean savedFarmer = farmerRepository.save(farmer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFarmer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(farmer);

	}

	/* update details of a customer using id */
	@PutMapping("/customer/update/user/{id}")
	public ResponseEntity<CustomerBean> updateCustomer(@RequestBody CustomerBean customer, @PathVariable String id) {
		if (customerRepository.findById(id).isPresent()) {
			CustomerBean savedCustomer = customerRepository.save(customer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedCustomer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(customer);
	}

	/* return customer details corresponding to the id provided */
	@GetMapping("/customer/{customer_id}")
	public Optional<CustomerBean> retreiveCustomerById(@PathVariable String customer_id) {
		Optional<CustomerBean> findById = customerRepository.findById(customer_id);
		return findById;
	}

	/* return farmer details corresponding to the id provided */
	@GetMapping("/farmer/{farmer_id}")
	public Optional<FarmerBean> retreiveFarmerById(@PathVariable String farmer_id) {
		Optional<FarmerBean> findById = farmerRepository.findById(farmer_id);
		return findById;
	}

	/* return customer details corresponding to the mobile number provided */
	@GetMapping("/customer/mobile/{mobileNo}")
	public Optional<CustomerBean> retrieveCustomerByMobile(@PathVariable String mobileNo) {
		Optional<CustomerBean> findByMobile = customerRepository.findByMobileNo(mobileNo);
		return findByMobile;
	}

	/* return customer details corresponding to the mobile number provided */
	@GetMapping("/farmer/mobile/{mobileNo}")
	public Optional<FarmerBean> retrieveFarmerByMobile(@PathVariable String mobileNo) {
		Optional<FarmerBean> findByMobile = farmerRepository.findByMobileNo(mobileNo);
		return findByMobile;
	}

	/* customer is provided with functionality to delete his account */
	@DeleteMapping("/customer/{customer_id}")
	public ResponseEntity<CustomerBean> deleteCustomer(@PathVariable String customer_id) {
		Optional<CustomerBean> findByMobile = customerRepository.findById(customer_id);
		if (findByMobile.isPresent()) {
			customerRepository.delete(findByMobile.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* farmer is provided with functionality to delete his account */
	@DeleteMapping("/farmer/{farmer_id}")
	public ResponseEntity<FarmerBean> deleteFarmer(@PathVariable String farmer_id) {
		Optional<FarmerBean> findByMobile = farmerRepository.findById(farmer_id);
		if (findByMobile.isPresent()) {
			System.out.println("farmer present");
			farmerRepository.delete(findByMobile.get());
			System.out.println("farmer deleted");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* user is provided with list of farmers having location provided */
	@GetMapping("/farmer/location/{location}")
	public List<FarmerBean> getFarmerBylocation(@PathVariable String location) {
		return farmerRepository.findAllByCities(location);
	}

	/* farmer can update the addressses */
	@PutMapping("/farmer/addressUpdate/{mobileNo}")
	public ResponseEntity<FarmerBean> addressUpdate(@RequestBody AddressBean address, @PathVariable String mobileNo) {

		Optional<FarmerBean> farmer = farmerRepository.findByMobileNo(mobileNo);
		if (farmer.isPresent()) {
			farmer.get().setCurrentAddress(address);
			farmerRepository.save(farmer.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(farmer.get());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

	}

	@GetMapping("/user/{customer_id}/addresses")
	public List<AddressBean> getAddresses(@PathVariable String customer_id) {
		Optional<CustomerBean> findByCustomerId = customerRepository.findById(customer_id);
		if (findByCustomerId.isPresent()) {
			return findByCustomerId.get().getAddresses();
		}
		return null;
	}

	@PutMapping("farmer/{id}")
	public ResponseEntity<FarmerBean> farmerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, Object> details) {
		System.out.println(details);
		Query query = new Query(Criteria.where("kkdFarmId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		FarmerBean findAndModify = mongoOps.findAndModify(query, update, FarmerBean.class);
		if (findAndModify != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	@PutMapping("customer/{id}")
	public ResponseEntity<CustomerBean> customerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, String> details) {
		System.out.println(details);
		Query query = new Query(Criteria.where("kkdCustId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		System.out.println("here");
		CustomerBean findAndModify = mongoOps.findAndModify(query, update, CustomerBean.class);
		if (findAndModify != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}
}
