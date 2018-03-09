package com.yikuyi.product.goods.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductVo;

/**
 * 
 *@see com.yikuyi.product.goods.manager.MovQueryManagerV2
 */
public class MovQueryManagerV2Test extends ProductApplicationTestBase{

	@Autowired
	private MovQueryManagerV2 movQueryManagerV2;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Test
	public void testQueryByEntities() {
		Sort sort = new Sort(Direction.ASC,"_id");
		PageRequest pageable = new PageRequest(1,10,sort);
		List<Product> products = productRepository.findAll(pageable).getContent();
		if(CollectionUtils.isNotEmpty(products)){
			List<ProductVo> productVos = new ArrayList<>();
			products.forEach(product -> {
				ProductVo productVo = new ProductVo();
				BeanUtils.copyProperties(product, productVo);
				productVos.add(productVo);
			});
			movQueryManagerV2.queryByEntities(productVos);
		}
	}

}
