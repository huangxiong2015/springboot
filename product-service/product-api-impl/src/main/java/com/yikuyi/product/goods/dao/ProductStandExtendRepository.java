package com.yikuyi.product.goods.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yikuyi.product.model.ProductStandExtend;

@Repository
public interface ProductStandExtendRepository extends MongoRepository<ProductStandExtend, String> {
	
}