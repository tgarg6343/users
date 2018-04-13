package com.kkd.userdetailsservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.kkd.userdetailsservice.service.MessageSenderService;
import com.mongodb.MongoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class UserController {

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	MessageSenderService messageSenderToRabbit;

	/*
	 * CREATING AN INSTANCE OF LOGGERFACTORY
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
		logger.info("{}", "finding farmer with :farmer id : " + id);
		if (farmerRepository.findById(id).isPresent()) {
			logger.info("{}", "saving the farmer with id : " + id);
			this.sendMsg("saving the farmer with id : " + id);
			FarmerBean savedFarmer = farmerRepository.save(farmer);
			logger.info("{}", "farmer saved with :farmer id : " + id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFarmer);
		}
		logger.info("{}", "farmer is not successfully saved with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(farmer);
	}

	/* fallback method for updating farmer details by provided */
	public ResponseEntity<FarmerBean> updateFarmerfallback(FarmerBean farmer, String id) {
		logger.info("{}", "fallback method called for updating farmer with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(farmer);
	}

	/*
	 * update details of a customer using id containing customer details in Request
	 * Body
	 */
	@HystrixCommand(fallbackMethod = "updateCustomerfallback")
	@PutMapping("/customer/update/user/{id}")
	public ResponseEntity<CustomerBean> updateCustomer(@RequestBody CustomerBean customer, @PathVariable String id) {
		logger.info("{}", "finding customer with :customer id : " + id);
		if (customerRepository.findById(id).isPresent()) {
			logger.info("{}", "saving the customer with id : " + id);
			CustomerBean savedCustomer = customerRepository.save(customer);
			this.sendMsg("updating the customer with id : " + id);
			logger.info("{}", "customer updated with :customer id : " + id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedCustomer);
		}
		logger.info("{}", "customer is not successfully saved with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(customer);
	}

	/* fallback method updating customer details by provided id */
	public ResponseEntity<CustomerBean> updateCustomerfallback(CustomerBean customer, String id) {
		logger.info("{}", "farmer is not successfully saved with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/* return customer details corresponding to the id provided */
	@HystrixCommand(fallbackMethod = "retreiveCustomerByIdfallback")
	@GetMapping("/customer/{customer_id}")
	public ResponseEntity<Optional<CustomerBean>> retreiveCustomerById(@PathVariable String customer_id) {
		logger.info("{}", "retreiving customer details with id : " + customer_id);
		Optional<CustomerBean> findById = customerRepository.findById(customer_id);
		this.sendMsg("obtaining the customer with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.OK).body(findById);
	}

	/* fallback method retreiving customer details by provided id */
	public ResponseEntity<Optional<CustomerBean>> retreiveCustomerByIdfallback(String customer_id) {
		logger.info("{}", "fallback method executing for retreiving customer details with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional
				.of(new CustomerBean("kkdCustId", "mobileNo", "password", "firstName", "lastName", null, null, false)));
	}

	/* return farmer details corresponding to the id provided */
	@HystrixCommand(fallbackMethod = "retreiveFarmerByIdfallback")
	@GetMapping("/farmer/{farmer_id}")
	public ResponseEntity<Optional<FarmerBean>> retreiveFarmerById(@PathVariable String farmer_id) {
		logger.info("{}", "retreiving farmer details with id : " + farmer_id);
		Optional<FarmerBean> findById = farmerRepository.findById(farmer_id);
		this.sendMsg("obtaining the farmer with id : " + farmer_id);
		return ResponseEntity.status(HttpStatus.OK).body(findById);
	}

	/* fallback method retreiving farmer details by provided id */
	public ResponseEntity<Optional<FarmerBean>> retreiveFarmerByIdfallback(String farmer_id) {
		logger.info("{}", "fallback method executing for retreiving farmer details with id : " + farmer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.of(new FarmerBean("kkdFarmId", "mobileNo",
				"password", "alternateNo", null, null, "status", null, null, null, null)));
	}

	/* return customer details corresponding to the mobile number provided */
	@GetMapping("/customer/mobile/{mobileNo}")
	@HystrixCommand(fallbackMethod = "retrieveCustomerByMobilefallback")
	public ResponseEntity<Optional<CustomerBean>> retrieveCustomerByMobile(@PathVariable String mobileNo) {
		logger.info("{}", "retreiving customer details with mobile number : " + mobileNo);
		Optional<CustomerBean> findByMobile = customerRepository.findByMobileNo(mobileNo);
		this.sendMsg("obtaining the customer with mobile number : " + mobileNo);
		return ResponseEntity.status(HttpStatus.OK).body(findByMobile);
	}

	/*
	 * fallback method for return customer details corresponding to the mobile
	 * number provided
	 */
	public ResponseEntity<Optional<CustomerBean>> retrieveCustomerByMobilefallback(String mobileNo) {
		logger.info("{}", "fallback method executing for retreiving customer details with mobile numeber  :"
				+ " mobile : " + mobileNo);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional
				.of(new CustomerBean("kkdCustId", "mobileNo", "password", "firstName", "lastName", null, null, false)));
	}

	/* return farmer details corresponding to the mobile number provided */
	@HystrixCommand(fallbackMethod = "retrieveFarmerByMobilefallback")
	@GetMapping("/farmer/mobile/{mobileNo}")
	public ResponseEntity<Optional<FarmerBean>> retrieveFarmerByMobile(@PathVariable String mobileNo) {
		logger.info("{}", "retreiving farmer details with mobile number : " + mobileNo);
		Optional<FarmerBean> findByMobile = farmerRepository.findByMobileNo(mobileNo);
		this.sendMsg("obtaining the farmer with mobile number : " + mobileNo);
		return ResponseEntity.status(HttpStatus.OK).body(findByMobile);
	}

	/* fallback method for retreiving farmer details by mobile number */
	public ResponseEntity<Optional<FarmerBean>> retrieveFarmerByMobilefallback(String mobileNo) {
		logger.info("{}", "fallback method executing for retreiving farmer details with mobile numeber  :"
				+ " mobile : " + mobileNo);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.of(new FarmerBean("kkdFarmId", "mobileNo",
				"password", "alternateNo", null, null, "status", null, null, null, null)));
	}

	/* customer is provided with functionality to delete his account */
	@HystrixCommand(fallbackMethod = "deleteCustomerfallback")
	@DeleteMapping("/customer/{customer_id}")
	public ResponseEntity<CustomerBean> deleteCustomer(@PathVariable String customer_id) {
		logger.info("{}", "retreiving customer details with id : " + customer_id);
		Optional<CustomerBean> findByMobile = customerRepository.findById(customer_id);
		if (findByMobile.isPresent()) {
			logger.info("{}", "deleting customer details with id : " + customer_id);
			this.sendMsg("deleting the customer with id : " + customer_id);
			customerRepository.delete(findByMobile.get());
			logger.info("{}", "customer deleted with id : " + customer_id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
		}
		logger.info("{}", "conflict in deleting customer with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* fallback method for deleting a customer profile */
	public ResponseEntity<CustomerBean> deleteCustomerfallback(String customer_id) {
		logger.info("{}", "fallback method executing for deleting customer details with id  :" + customer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/* farmer is provided with functionality to delete his account */
	@HystrixCommand(fallbackMethod = "deleteFarmerfallback")
	@DeleteMapping("/farmer/{farmer_id}")
	public ResponseEntity<FarmerBean> deleteFarmer(@PathVariable String farmer_id) {
		logger.info("{}", "retreiving farmer details with id : " + farmer_id);
		Optional<FarmerBean> findByMobile = farmerRepository.findById(farmer_id);
		if (findByMobile.isPresent()) {
			logger.info("{}", "deleting farmer details with id : " + farmer_id);
			this.sendMsg("deleting the farmer with id : " + farmer_id);
			farmerRepository.delete(findByMobile.get());
			logger.info("{}", "farmer deleted with id : " + farmer_id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
		}
		logger.info("{}", "conflict in deleting farmer with id : " + farmer_id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* fallback method for deleting a farmer profile */
	public ResponseEntity<FarmerBean> deleteFarmerfallback(String farmer_id) {
		logger.info("{}", "fallback method executing for deleting farmer details with id  :" + farmer_id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* user is provided with list of farmers having location provided */
	@HystrixCommand(fallbackMethod = "getFarmerBylocationfallback")
	@GetMapping("/farmer/location/{location}")
	public List<FarmerBean> getFarmerBylocation(@PathVariable String location) {
		logger.info("{}", "getting list of all farmers with location :" + location);
		this.sendMsg("getting the list of all farmers with location : " + location);
		return farmerRepository.findAllByCities(location);
	}

	/* fallback method for getting list of farmers by specific location */
	public List<FarmerBean> getFarmerBylocationfallback(String location) {
		logger.info("{}", "fallback method executing for getting list of all farmers with location :" + location);
		FarmerBean farmer = new FarmerBean("kkdFarmId", "mobileNo", "password", "alternateNo", null, null, "status",
				null, null, null, null);
		return Arrays.asList(farmer);
	}

	/* farmer can update the addressses by providing mobile number */
	@HystrixCommand(fallbackMethod = "addressUpdatefallback")
	@PutMapping("/farmer/addressUpdate/{mobileNo}")
	public ResponseEntity<FarmerBean> addressUpdate(@RequestBody AddressBean address, @PathVariable String mobileNo) {
		logger.info("{}", "retreiving farmer details with mobile Number : " + mobileNo);
		Optional<FarmerBean> farmer = farmerRepository.findByMobileNo(mobileNo);
		if (farmer.isPresent()) {
			farmer.get().setCurrentAddress(address);
			logger.info("{}", "saving farmer details with mobile Number : " + mobileNo + " and address : " + address);
			this.sendMsg("saving the addresses of farmers with mobile number: " + mobileNo);
			farmerRepository.save(farmer.get());
			logger.info("{}", "farmer details saved with mobile Number : " + mobileNo + " and address : " + address);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(farmer.get());
		}
		logger.info("{}",
				"conflict in saving farmer details with mobile Number : " + mobileNo + " and address : " + address);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

	}

	/* fallback method for updating the addressses by providing mobile number */
	public ResponseEntity<FarmerBean> addressUpdatefallback(AddressBean address, String mobileNo) {
		logger.info("{}", "fallback method executing for saving farmer details with mobile Number : " + mobileNo
				+ " and address : " + address);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	}

	/* method for retreiving address for specific customer with given id provided */
	@HystrixCommand(fallbackMethod = "getAddressesfallback")
	@GetMapping("/user/{customer_id}/addresses")
	public ResponseEntity<List<AddressBean>> getAddresses(@PathVariable String customer_id) {
		logger.info("{}", "retreiving customer details with id : " + customer_id);
		Optional<CustomerBean> findByCustomerId = customerRepository.findById(customer_id);
		if (findByCustomerId.isPresent()) {
			logger.info("{}", "sending list of address for customer with id : " + customer_id);
			// this.sendMsg("obtaining the addresses of customer with id: " + customer_id);
			return ResponseEntity.status(HttpStatus.OK).body(findByCustomerId.get().getAddresses());
		}
		logger.info("{}", "conflict in sending list of address for customer with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	/*
	 * fallback method for retreiving address for specific customer with given id
	 * provided
	 */
	public ResponseEntity<List<AddressBean>> getAddressesfallback(String customer_id) {
		logger.info("{}", "fallback method for retreiving customer details with id : " + customer_id);
		AddressBean address = new AddressBean(132039, "addressLine", "city", "district", "state", false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Arrays.asList(address));
	}

	/*
	 * farmer details are updated using map technique request body contains Map i.e
	 * set of keys and values
	 */
	@HystrixCommand(fallbackMethod = "farmerDetailsUpdatedfallback")
	@PutMapping("farmer/{id}")
	public ResponseEntity<FarmerBean> farmerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, Object> details) {
		Query query = new Query(Criteria.where("kkdFarmId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		logger.info("{}", "retreiving farmer details with id : " + id);
		if (farmerRepository.findById(id).isPresent()) {
			logger.info("{}", "modifying farmer details with id : " + id);
			this.sendMsg("modifying all details of farmer with id: " + id);
			FarmerBean findAndModify = mongoOps.findAndModify(query, update, FarmerBean.class);
			logger.info("{}", "farmer details modified with id : " + id);
			if (findAndModify != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
			}
		}
		logger.info("{}", "conflict in updating farmer details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/*
	 * fallback method for updating farmer details with Request body conatining map
	 */
	public ResponseEntity<FarmerBean> farmerDetailsUpdatedfallback(String id, Map<String, Object> details) {
		logger.info("{}", "fallback method for updating farmer details with id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * customer details are updated using map technique request body contains Map
	 * i.e set of keys and values
	 */
	@HystrixCommand(fallbackMethod = "customerDetailsUpdatedfallback")
	@PutMapping("customer/{id}")
	public ResponseEntity<CustomerBean> customerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, String> details) {
		Query query = new Query(Criteria.where("kkdCustId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		logger.info("{}", "retreiving custpmer details with id : " + id);
		if (customerRepository.findById(id).isPresent()) {
			logger.info("{}", "modifying customer details with id : " + id);
			this.sendMsg("modifying all details of customer with id: " + id);
			CustomerBean findAndModify = mongoOps.findAndModify(query, update, CustomerBean.class);
			logger.info("{}", "customer details modified with id : " + id);
			if (findAndModify != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
			}
		}
		logger.info("{}", "conflict in updating customer details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/*
	 * fallback method for updating customer details with Request body conatining
	 * map
	 */
	public ResponseEntity<CustomerBean> customerDetailsUpdatedfallback(String id, Map<String, String> details) {
		logger.info("{}", "fallback method for updating customer details with id : " + id);
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
		logger.info("{}", "retreiving farmer details with id : " + id);
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			FarmerBean farmer = findById.get();
			farmer.setBankDetails(bankDetailsBean);
			logger.info("{}", "adding farmer bank account details with id : " + id);
			this.sendMsg("saving the bank details of farmer with id: " + id);
			FarmerBean updatedFarmer = farmerRepository.save(farmer);
			logger.info("{}", "farmer bank account details added with id : " + id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFarmer);
		}
		logger.info("{}", "conflict in saving farmer bank account details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* fallback method for creating farmer account details */
	public ResponseEntity<CustomerBean> farmerAccountDetailsAddedFallback(String id, BankDetailsBean bankDetailsBean) {
		logger.info("{}", "fallback occured in saving farmer bank account details with id : " + id);
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
		logger.info("{}", "retreiving farmer details with id : " + id);
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			FarmerBean farmer = findById.get();
			farmer.setBankDetails(bankDetailsBean);
			logger.info("{}", "updating farmer bank account details with id : " + id);
			FarmerBean updatedFarmer = farmerRepository.save(farmer);
			logger.info("{}", "farmer bank account details updated with id : " + id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFarmer);
		}
		logger.info("{}", "conflict in saving farmer bank account details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/* fallback method for updating farmer account details */
	public ResponseEntity<CustomerBean> farmerAccountDetailsUpdatedFallback(String id,
			BankDetailsBean bankDetailsBean) {
		logger.info("{}", "fallback occured in saving farmer bank account details with id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	public String sendMsg(String message) {
		logger.info("Sending message...");
		messageSenderToRabbit.produceMsg(message);
		return "Done";
	}
}