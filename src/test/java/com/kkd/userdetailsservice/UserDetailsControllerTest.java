package com.kkd.userdetailsservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.kkd.userdetailsservice.service.MessageSenderService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserDetailsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FarmerRepository farmerRepository;
	@MockBean
	private CustomerRepository customerRepository;
	@MockBean
	MessageSenderService messageSenderService;

	FarmerBean mockFarmer;
	String exampleFarmerJson;
	CustomerBean mockCustomer;
	String exampleCustomerJson;
	List<FarmerBean> farmersList;
	List<CustomerBean> customersList;
	Optional<FarmerBean> farmerBeanOptional;
	Optional<CustomerBean> customerBeanOptional;
	String exampleAccountJson;
	String exampleAddressJson;

	/* initializing the required fiels */
	@Before
	public void setUp() {
		mockFarmer = new FarmerBean("FARM1000", "1234567890", "pass", "0987654321", null, null, "active", true, null,
				null, null);
		exampleFarmerJson = "{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"cities\":null,\"currentAddress\":null,\"status\":\"active\","
				+ "\"autoConfirm\":true,\"aadharData\":null}";
		mockCustomer = new CustomerBean("CUST1000", "1234567890", "pass", "tarun", "garg", null, null, null);
		exampleCustomerJson = "{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"," + "\"addresses\":null,\"primaryAddress\":null}";
		exampleAccountJson = "{\"accountName\":\"OWNERNAME\",\"accountNo\":\"1234567890\",\"ifscCode\":\"IFSC0000000\"}";

		farmersList = new ArrayList<FarmerBean>();
		farmersList.add(new FarmerBean("FARM1000", "1234567890", "pass", "0987654321", null, null, "active", true, null,
				null, null));
		customersList = new ArrayList<>();
		customersList.add(new CustomerBean("CUST1000", "1234567890", "pass", "tarun", "garg", null, null, null));
		farmerBeanOptional = Optional.of(new FarmerBean("FARM1000", "1234567890", "pass", "0987654321", null, null,
				"active", true, null, null, null));
		customerBeanOptional = Optional.of(new CustomerBean("CUST1000", "1234567890", "pass", "tarun", "garg",
				Arrays.asList(new AddressBean(132206, "addressLine", "city", "district", "state", false)),
				new AddressBean(132206, "addressLine", "city", "district", "state", false), null));
		exampleAddressJson = "{\"pincode\":null,\"addressLine\":null,\"city\":null,\"district\":null,\"state\":null,\"primary\":null}";
	}

	@Test
	public void createFarmerTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/farmer/user").accept(MediaType.APPLICATION_JSON)
				.content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void createFarmerNegTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/farmer/user").accept(MediaType.APPLICATION_JSON)
				.content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void createCustomerTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer
		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customer/user").accept(MediaType.APPLICATION_JSON)
				.content(exampleCustomerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void createCustomerNegTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer
		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customer/user").accept(MediaType.APPLICATION_JSON)
				.content(exampleCustomerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void getFarmerTest() throws Exception {
		// farmerRepository.findAll to respond back with farmer
		Mockito.when(farmerRepository.findAll()).thenReturn(farmersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/id/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getFarmerNegTest() throws Exception {
		// farmerRepository.findAll to respond back with farmer
		Mockito.when(farmerRepository.findAll()).thenReturn(farmersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/id/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdFarmId\":\"FARM200\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}]";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getCustomerTest() throws Exception {
		// customerRepository.findAll to respond back with customer
		Mockito.when(customerRepository.findAll()).thenReturn(customersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/id/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getCustomerNegTest() throws Exception {
		// customerRepository.findAll to respond back with customer
		Mockito.when(customerRepository.findAll()).thenReturn(customersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/id/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"," + "\"addresses\":null,\"primaryAddress\":null}]";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
	}

	/*
	 * POSITIVE test case for updating farmer details having farmer object in
	 * request body
	 * 
	 */

	@Test
	public void updateFarmerTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/update/user/FARM1000")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating farmer details having farmer object in
	 * request body
	 */

	@Test
	public void updateFarmerNegTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/update/user/FARM1000")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	/*
	 * Positive test case for updating customer details having customer object in
	 * request body
	 */

	@Test
	public void updateCustomerTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer
		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/update/user/CUST1000")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating customer details having customer object in
	 * request body
	 */
	@Test
	public void updateCustomerNegTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer
		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/update/user/CUST1000")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	/*
	 * Positive test case for retreiving farmer details with farmer id in uri
	 */
	@Test
	public void retreiveFarmerByIdTest() throws Exception {
		// farmerRepository.findById to respond back with farmerBean
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving farmer details with farmer id in uri
	 */
	@Test
	public void retreiveFarmerByIdNegTest() throws Exception {
		// farmerRepository.findById to respond back with farmerBean
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/FARM1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdFarmId\":\"FARM700\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details with customer id in uri
	 */
	@Test
	public void retreiveCustomerByIdTest() throws Exception {
		// customerRepository.findById to respond back with customerBean
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/CUST1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving customer details with customer id in uri
	 */
	@Test
	public void retreiveCustomerByIdNegTest() throws Exception {
		// customerRepository.findById to respond back with customerBean
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/CUST1000")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"}";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving farmer details with farmer mobile in uri
	 */
	@Test
	public void retrieveFarmerByMobileTest() throws Exception {
		// farmerRepository.findByMobileNo to respond back with farmer
		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/mobile/134553")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving farmer details with farmer mobile in uri
	 */
	@Test
	public void retrieveFarmerByMobileNegTest() throws Exception {
		// farmerRepository.findByMobileNo to respond back with farmer
		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/mobile/134553")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details with customer mobile in
	 * uri
	 */
	@Test
	public void retrieveCustomerByMobileTest() throws Exception {
		// customerRepository.findByMobileNo to respond back with customer
		Mockito.when(customerRepository.findByMobileNo(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/mobile/134553")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving customer details with customer mobile in
	 * uri
	 */
	@Test
	public void retrieveCustomerByMobileNegTest() throws Exception {
		// customerRepository.findByMobileNo to respond back with customer
		Mockito.when(customerRepository.findByMobileNo(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/mobile/134553")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"kkdCustId\":\"CUST1000\",\"mobileNo\":\"1234567\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\"}";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details having one of location
	 * matching with the location provided
	 */
	@Test
	public void getFarmerBylocationTest() throws Exception {
		Mockito.when(farmerRepository.findAllByCities(Mockito.anyString())).thenReturn(farmersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/location/delhi")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	/*
	 * Negative test case for retrieving customer details having one of location
	 * matching with the location provided
	 */
	@Test
	public void getFarmerBylocationNegTest() throws Exception {
		Mockito.when(farmerRepository.findAllByCities(Mockito.anyString())).thenReturn(farmersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/location/null")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"kkdFarmId\":\"FARM6700\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"status\":\"active\",\"autoConfirm\":true}]";
		JSONAssert.assertNotEquals(expected, result.getResponse().getContentAsString(), false);
	}

	/* Positive test case for updating farmer address */
	@Test
	public void farmerAddressUpdateTest() throws Exception {
		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/addressUpdate/1234567890")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/* Negative test case for updating farmer address */
	@Test
	public void farmerAddressUpdateNegTest() throws Exception {
		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/addressUpdate/1234567890")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for adding account details for the farmer
	 */
	@Test
	public void farmerAccountDetailsAddedTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/farmer/13245/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Negative test case for adding account details for the farmer
	 */
	@Test
	public void farmerAccountDetailsAddedNegTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/farmer/13245/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for updating account details for the farmer
	 */
	@Test
	public void farmerAccountDetailsUpdatedTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/13245/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating account details for the farmer
	 */
	@Test
	public void farmerAccountDetailsUpdatedNegTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/13245/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for getting list of address of customer
	 */
	@Test
	public void getAddressesCustomerTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/kkd1234/addresses")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for getting list of address of customer
	 */
	@Test
	public void getAddressesCustomerNegTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/hgfjhg/addresses")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	/*
	 * Positive test case for updating customer by any field
	 */
	@Test
	public void updateCustomerByAnyFieldTest() throws Exception {
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/kkd12345")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating customer by any field
	 */
	@Test
	public void updateCustomerByAnyFieldNegTest() throws Exception {
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/kkd12345")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for updating farmer by any field
	 */
	@Test
	public void updateFarmerByAnyFieldTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/kkd12345")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating farmer by any field
	 */
	@Test
	public void updateFarmerByAnyFieldNegTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/kkd12345")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/* cleaning up the objects after every test */
	@After
	public void cleanUp() {
		mockFarmer = null;
		exampleFarmerJson = null;
		mockCustomer = null;
		exampleCustomerJson = null;
		farmersList = null;
		customersList = null;
		farmerBeanOptional = null;
		customerBeanOptional = null;
		exampleAccountJson = null;
	}

}