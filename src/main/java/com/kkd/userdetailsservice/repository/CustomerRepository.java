package com.kkd.userdetailsservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kkd.userdetailsservice.model.CustomerBean;

public interface CustomerRepository extends MongoRepository<CustomerBean, String>{

}
