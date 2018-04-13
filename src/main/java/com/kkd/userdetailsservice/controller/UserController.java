package com.kkd.userdetailsservice.controller;

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
import com.kkd.userdetailsservice.model.BankDetailsBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.mongodb.MongoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
	@HystrixCommand(fallbackMethod = "updateFarmerfallback")
	@PutMapping("/farmer/update/user/{id}")
	public ResponseEntity<FarmerBean> updateFarmer(@RequestBody FarmerBean farmer, @PathVariable String id) {

		if (farmerRepository.findById(id).isPresent()) {
			FarmerBean savedFarmer = farmerRepository.save(farmer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFarmer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(farmer);
	}

	public ResponseEntity<FarmerBean> updateFarmerfallback(FarmerBean farmer, String id) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(farmer);
	}

	/* update details of a customer using id */
	@HystrixCommand(fallbackMethod = "updateCustomerfallback")
	@PutMapping("/customer/update/user/{id}")
	public ResponseEntity<CustomerBean> updateCustomer(@RequestBody CustomerBean customer, @PathVariable String id) {
		if (customerRepository.findById(id).isPresent()) {
			CustomerBean savedCustomer = customerRepository.save(customer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedCustomer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(customer);
	}

	public ResponseEntity<CustomerBean> updateCustomerfallback(CustomerBean customer, String id) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/* return customer details corresponding to the id provided */
	@HystrixCommand(fallbackMethod = "retreiveCustomerByIdfallback")
	@GetMapping("/customer/{customer_id}")
	public Optional<CustomerBean> retreiveCustomerById(@PathVariable String customer_id) {
		Optional<CustomerBean> findById = customerRepository.findById(customer_id);
		return findById;
	}

	public Optional<CustomerBean> retreiveCustomerByIdfallback(String customer_id) {
		return Optional
				.of(new CustomerBean("kkdCustId", "mobileNo", "password", "firstName", "lastName", null, null, false));
	}

	/* return farmer details corresponding to the id provided */
	@HystrixCommand(fallbackMethod = "retreiveFarmerByIdfallback")
	@GetMapping("/farmer/{farmer_id}")

	public Optional<FarmerBean> retreiveFarmerById(@PathVariable String farmer_id) {
		Optional<FarmerBean> findById = farmerRepository.findById(farmer_id);
		return findById;
	}

	public Optional<FarmerBean> retreiveFarmerByIdfallback(String farmer_id) {
		return Optional.of(new FarmerBean("kkdFarmId", "mobileNo", "password", "alternateNo", null, null, "status",
				null, null, null, null));
	}

	/* return customer details corresponding to the mobile number provided */
	@GetMapping("/customer/mobile/{mobileNo}")
	@HystrixCommand(fallbackMethod = "retrieveCustomerByMobilefallback")
	public Optional<CustomerBean> retrieveCustomerByMobile(@PathVariable String mobileNo) {
		Optional<CustomerBean> findByMobile = customerRepository.findByMobileNo(mobileNo);
		return findByMobile;
	}

	public Optional<CustomerBean> retrieveCustomerByMobilefallback(String mobileNo) {
		return Optional
				.of(new CustomerBean("kkdCustId", "mobileNo", "password", "firstName", "lastName", null, null, false));
	}

	/* return customer details corresponding to the mobile number provided */
	@HystrixCommand(fallbackMethod = "retrieveFarmerByMobilefallback")
	@GetMapping("/farmer/mobile/{mobileNo}")
	public Optional<FarmerBean> retrieveFarmerByMobile(@PathVariable String mobileNo) {
		Optional<FarmerBean> findByMobile = farmerRepository.findByMobileNo(mobileNo);
		return findByMobile;
	}

	public Optional<FarmerBean> retrieveFarmerByMobilefallback(String mobileNo) {
		return Optional.of(new FarmerBean("kkdFarmId", "mobileNo", "password", "alternateNo", null, null, "status",
				null, null, null, null));
	}

	/* customer is provided with functionality to delete his account */
	@HystrixCommand(fallbackMethod = "deleteCustomerfallback")
	@DeleteMapping("/customer/{customer_id}")
	public ResponseEntity<CustomerBean> deleteCustomer(@PathVariable String customer_id) {
		Optional<CustomerBean> findByMobile = customerRepository.findById(customer_id);
		if (findByMobile.isPresent()) {
			customerRepository.delete(findByMobile.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	public ResponseEntity<CustomerBean> deleteCustomerfallback(String customer_id) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/* farmer is provided with functionality to delete his account */
	@HystrixCommand(fallbackMethod = "deleteFarmerfallback")
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

	public ResponseEntity<FarmerBean> deleteFarmerfallback(String farmer_id) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* user is provided with list of farmers having location provided */
	@HystrixCommand(fallbackMethod = "getFarmerBylocationfallback")
	@GetMapping("/farmer/location/{location}")
	public List<FarmerBean> getFarmerBylocation(@PathVariable String location) {
		return farmerRepository.findAllByCities(location);
	}

	public List<FarmerBean> getFarmerBylocationfallback(String location) {
		FarmerBean farmer = new FarmerBean("kkdFarmId", "mobileNo", "password", "alternateNo", null, null, "status",
				null, null, null, null);
		return Arrays.asList(farmer);
	}

	/* farmer can update the addressses */
	@HystrixCommand(fallbackMethod = "addressUpdatefallback")
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

	public ResponseEntity<FarmerBean> addressUpdatefallback(AddressBean address, String mobileNo) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	}

	@HystrixCommand(fallbackMethod = "getAddressesfallback")
	@GetMapping("/user/{customer_id}/addresses")
	public List<AddressBean> getAddresses(@PathVariable String customer_id) {
		Optional<CustomerBean> findByCustomerId = customerRepository.findById(customer_id);
		if (findByCustomerId.isPresent()) {
			return findByCustomerId.get().getAddresses();
		}
		return null;
	}

	public List<AddressBean> getAddressesfallback(String customer_id) {
		AddressBean address = new AddressBean(132039, "addressLine", "city", "district", "state", false);
		return Arrays.asList(address);
	}

	@HystrixCommand(fallbackMethod = "farmerDetailsUpdatedfallback")
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

	public ResponseEntity<FarmerBean> farmerDetailsUpdatedfallback(String id, Map<String, Object> details) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@HystrixCommand(fallbackMethod = "customerDetailsUpdatedfallback")
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

	public ResponseEntity<CustomerBean> customerDetailsUpdatedfallback(String id, Map<String, String> details) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * create farmer account details Bank account details will be in request body
	 * will return farmer with that account and status codes
	 */
	@PostMapping("/farmer/{id}/accounts")
	@HystrixCommand(fallbackMethod = "farmerAccountDetailsAddedFallback")
	public ResponseEntity<FarmerBean> farmerAccountDetailsAdded(@PathVariable String id,
			@RequestBody BankDetailsBean bankDetailsBean) {
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			FarmerBean farmer = findById.get();
			farmer.setBankDetails(bankDetailsBean);
			FarmerBean updatedFarmer = farmerRepository.save(farmer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFarmer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	public ResponseEntity<CustomerBean> farmerAccountDetailsAddedFallback(String id, BankDetailsBean bankDetailsBean) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * update farmer account details Bank account details will be in request body
	 * will return farmer with that account and status codes
	 */
	@PutMapping("/farmer/{id}/accounts")
	@HystrixCommand(fallbackMethod = "farmerAccountDetailsUpdatedFallback")
	public ResponseEntity<FarmerBean> accountDetailsUpdated(@PathVariable String id,
			@RequestBody BankDetailsBean bankDetailsBean) {
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			FarmerBean farmer = findById.get();
			farmer.setBankDetails(bankDetailsBean);
			FarmerBean updatedFarmer = farmerRepository.save(farmer);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFarmer);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	public ResponseEntity<CustomerBean> farmerAccountDetailsUpdatedFallback(String id,
			BankDetailsBean bankDetailsBean) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}