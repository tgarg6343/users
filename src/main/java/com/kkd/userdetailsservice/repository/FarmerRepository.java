package com.kkd.userdetailsservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kkd.userdetailsservice.model.FarmerBean;

public interface FarmerRepository extends MongoRepository<FarmerBean, String> {

}
