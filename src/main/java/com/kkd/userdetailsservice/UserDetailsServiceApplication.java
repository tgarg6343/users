package com.kkd.userdetailsservice;

import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kkd.userdetailsservice.model.AadharBean;
import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.BankDetailsBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.mongodb.MongoClient;

import brave.sampler.Sampler;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableRabbit
//@EnableDiscoveryClient
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableSwagger2

public class UserDetailsServiceApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceApplication.class);

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	// name of exchange to be created
	public static final String EXCHANGE_NAME = "appExchange";
	// name of generic queue to be created
	public static final String QUEUE_GENERIC_NAME = "appGenericQueue";
	// name of specific to be created
	public static final String QUEUE_SPECIFIC_NAME = "appSpecificQueue";
	// name of routing to be created
	public static final String ROUTING_KEY = "messages.key";

	public static void main(String[] args) {
		SpringApplication.run(UserDetailsServiceApplication.class, args);
	}

	// for sleuth to generate id
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	// For swagger to document the Service
	@Bean
	public Docket api() throws IOException, XmlPullParserException {
		return new Docket(DocumentationType.SWAGGER_2);
	}

	// creating exchange
	@Bean
	public TopicExchange appExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	// creating generic queue
	@Bean
	public Queue appQueueGeneric() {
		return new Queue(QUEUE_GENERIC_NAME);
	}

	// creating specific queue
	@Bean
	public Queue appQueueSpecific() {
		return new Queue(QUEUE_SPECIFIC_NAME);
	}

	// binding generic queue with exchange with a routing key
	@Bean
	public Binding declareBindingGeneric() {
		return BindingBuilder.bind(appQueueGeneric()).to(appExchange()).with(ROUTING_KEY);
	}

	// binding specific queue with exchange with a routing key
	@Bean
	public Binding declareBindingSpecific() {
		return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY);
	}

	@Override
	public void run(String... args) throws Exception {
		CustomerBean customerBean = new CustomerBean("kdCustId", "mobileNo", "password", "firstName", null, null, null,
				false);
		customerRepository.save(customerBean);

		FarmerBean farmerBean = new FarmerBean("kkd58", "mobileNo", "password", "alternateNo", null, new AddressBean(),
				null, true, new AadharBean(), false, null);
		farmerRepository.save(farmerBean);
	}
}
