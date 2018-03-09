package com.yikuyi.product.goods.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.product.model.ProductStand;

@Repository
public interface ProductStandRepository extends MongoRepository<ProductStand, String> {
	
	/**
	 * 根据条件查询spu
	 * @param manufacturerPartNumbers
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	@Query("{'spuId':{'$in':?0}}")
	public List<ProductStand> findProductStandBySpuId(List<String> spuIds);
	
	/**
	 * 根据spuId查询spu
	 * @param spuId
	 * @return
	 */
	@Query("{'spuId':?0}")
	public List<ProductStand> findProductStandBySpuId(String spuId);
	
	@Query("{'manufacturerPartNumber':?0}")
	public List<ProductStand> findProductStandByNo(String manufacturerPartNumber);
	
	/**
	 * 根据IDs查询物料数据
	 * @param ids
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<ProductStand> findProductStandByIds(List<String> ids);

	/**
	 * 根据品牌id查询物料
	 * @param manufacturerId
	 * @return
	 * @since 2017年4月7日
	 * @author tongkun@yikuyi.com
	 */
	@Query("{'manufacturerId':?0}")
	public List<ProductStand> findProductStandByManufacturerId(Integer manufacturerId);
	
	//分页查询物料
	@Query("?0")
	public Page<ProductStand> listProductStandByPage(Object params,Pageable pageable);
	
	//根据条件查询
	@Query("?0")
	public List<ProductStand> listProductStand(Object params,Pageable pageable);

	/**
	 * 批量验证型号是否存在
	 * @param manufacturerPartNumberList
	 * @return
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'manufacturerPartNumber':{'$in':?0}}")
	public List<ProductStand> existManufacturerPartNumberList(List<String> manufacturerPartNumberList);
	
}