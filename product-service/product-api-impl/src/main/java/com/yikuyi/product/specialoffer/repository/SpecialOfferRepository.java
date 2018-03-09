package com.yikuyi.product.specialoffer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yikuyi.specialoffer.model.SpecialOffer;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface SpecialOfferRepository extends MongoRepository<SpecialOffer, String> {
	


	
	
}