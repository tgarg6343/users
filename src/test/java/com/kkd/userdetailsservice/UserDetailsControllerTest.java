package com.kkd.userdetailsservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserDetailsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FarmerRepository farmerRepository;
	@MockBean
	private CustomerRepository custmerRepository;
	FarmerBean mockFarmer = new FarmerBean("1", "9872", "1abc", "2456", null, null, "fyk", true, null, false);
	String exampleFarmerJson = "{\"kkdFarmId\":\"1\",\"mobileNo\":\"9872\",\"password\":\"1abc\","
			+ "\"alternateNo\":\"2456\",\"cities\":null,\"currentAddress\":null,\"status\":\"fyk\","
			+ "\"autoConfirm\":true,\"aadharData\":null,\"isdeleted\":false}";

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
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/farmer/user").accept(MediaType.APPLICATION_JSON)
				.content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

}
