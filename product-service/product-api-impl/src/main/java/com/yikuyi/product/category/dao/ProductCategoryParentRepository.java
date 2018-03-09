package com.yikuyi.product.category.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yikuyi.category.vo.ProductCategoryParent;

@Repository
public interface ProductCategoryParentRepository extends MongoRepository<ProductCategoryParent, Long>{
}
