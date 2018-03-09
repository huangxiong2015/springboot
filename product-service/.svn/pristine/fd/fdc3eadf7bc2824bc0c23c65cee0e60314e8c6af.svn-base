package com.yikuyi.product.goods.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.product.model.ProductStandAudit;

@Repository
public interface ProductStandAuditRepository extends MongoRepository<ProductStandAudit, String> {
	
	/**
	 * 根据条件查询spu
	 * @param manufacturerPartNumbers
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	@Query("{'spuId':{'$in':?0}}")
	public List<ProductStandAudit> findProductStandBySpuId(List<String> spuIds);
	
	//分页查询物料
	@Query("?0")
	public Page<ProductStandAudit> findProductStandAuditByPage(Object params,Pageable pageable);

	/**
	 * 根据IDs查询物料数据
	 * @param ids
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<ProductStandAudit> findProductStandByIds(List<String> ids);
	
}