package com.yikuyi.product.goods.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductVo;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	
	/**
	 * 根据条件查询商品
	 * @param skuId
	 * @param vendorId
	 * @param facilityId
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	@Query("{'quickFindKey':{'$in':?0}}")
	public List<Product> findProductByQuickFindKey(List<String> quickFindKey);
	
	/**
	 * 根据spuId来查询商品
	 * @param spuIds
	 * @return
	 * @since 2017年2月28日
	 * @author tongkun@yikuyi.com
	 */
	@Query("{'spu.spuId':{'$in':?0}}")
	public List<Product> findProductBySpuIds(List<String> spuIds);
	
	@Query("{'vendorId': ?0,'status':1}")	
	public Product findByVendorId(String vendorId);
	
	@Query("{'spu.manufacturerId': ?0}")	
	public List<Product> findByManufacturerId(Integer brandId);
	
	@Query("{'spu._id':{'$in':?0}}")	
	public List<Product> findProductBySkuIds(List<String> skuIds);
	
	/**
	 * 根据条件查询商品
	 * @param skuId
	 * @param vendorId
	 * @param facilityId
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	//@Query("{'spu.manufacturerPartNumber': ?0 ,'spu.manufacturerPartNumber': ?0 , 'spu.manufacturerPartNumber': ?0 , 'spu.manufacturerPartNumber': ?0 }")
	@Query("{'spu.manufacturerPartNumber': ?0}")
	public Product matchingProduct(String partNum);
	
	/**
	 * 分页查询商品
	 * @param obj
	 * @param pageable
	 * @return
	 * @since 2017年2月25日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Query("?0")
	public Page<Product> findByPage(Object obj,Pageable pageable);
	
	/**
	 * 根据  品牌 和 型号  查商品ID，价格阶梯
	 * @params manufacturer
	 * @params manufacturerPartNumber
	 * @author tb.huangqingfeng
	 */
	@Query("{'spu.manufacturer': ?0,'spu.manufacturerPartNumber':?1,'status':1}")	
	public List<ProductVo> findFacturerAndPartNumber(String manufacturer,String manufacturerPartNumber);
	
	/**
	 * 根据批次号查询商品集合
	 */
	@Query("{'processId':?0,'_id':{$gt:?1}}")
	public List<Product> findByProcessId(String processId,String lastId,Pageable pageable);
	
	/**
	 * 分页查询商品，返回list集合
	 * @param obj 动态条件参数
	 * @param pageable 分页信息
	 * @return list集合
	 * @since 2017年6月5日
	 * @author zr.wanghong
	 */
	@Query("?0")
	public List<Product> findListByPage(Object obj,Pageable pageable);
	
	/**
	 * 根据条件动态查询
	 * @param obj 动态条件参数
	 * @return list集合
	 */
	@Query("?0")
	public List<Product> findListByCondition(Object obj);
	
	/**
	 * 根据型号、制造商、供应商ID、仓库ID、状态查询商品数据
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @return
	 * @since 2017年6月13日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("{'spu.manufacturerId':?0,'spu.manufacturerPartNumber':?1,'vendorId':?2,'sourceId':?3,'status':?4}")
	public List<ProductVo> findProductsByMPNandManufacturer(Integer manufacturerId, String manufacturerPartNumber,
			String vendorId, String sourceId,int status);
	/**
	 * 根据型号、制造商、供应商ID、仓库ID、skuId、状态查询商品数据
	 * @param manufacturer
	 * @param manufacturerPartNumber
	 * @param vendorId
	 * @param sourceId
	 * @param skuId
	 * @param status
	 * @return
	 * @since 2017年10月19日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("{'spu.manufacturerId':?0,'spu.manufacturerPartNumber':?1,'vendorId':?2,'sourceId':?3,'skuId':?4,'status':?5}")
	public List<ProductVo> findProductsByCondition(Integer manufacturerId, String manufacturerPartNumber,
			String vendorId, String sourceId,String skuId,int status);
	
	/**
	 * 根据型号、制造商、供应商ID、状态查询商品数据
	 * @param manufacturer
	 * @param manufacturerPartNumber
	 * @param vendorId
	 * @param status
	 * @return
	 * @since 2017年6月16日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("{'spu.manufacturerId':?0,'spu.manufacturerPartNumber':?1,'vendorId':?2,'status':?3}")
	public List<ProductVo> findProductsByCondition(Integer manufacturerId, String manufacturerPartNumber,
			String vendorId,int status);
	
	/**
	 * 根据ids批量查询数据
	 * @param spuIds
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<Product> findProductsByIds(List<String> ids);
	
	/**
	 * 根据id、供应商ID、状态查询商品数据
	 * @param id
	 * @param vendorId
	 * @param status
	 * @return
	 * @since 2017年10月26日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("{'_id':?0,'vendorId':?1,'status':?2}")
	public ProductVo findProductByVendIdAndId(String id,String vendorId,int status);
	
	/**
	 * 根据id、品牌ID、状态查询商品数据
	 * @param id
	 * @param manufacturerId
	 * @param status
	 * @return
	 * @since 2017年10月26日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("{'_id':?0,'spu.manufacturerId':?1,'status':?2}")
	public ProductVo findProductByManufacturerIdAndId(String id,int manufacturerId,int status);
	
	/**
	 * 查询过期数据的id
	 * @param vendorId 供应商id
	 * @param time 过期时间
	 * @return 过期的数据id
	 */
	@Query("{'vendorId':'?0','updatedTimeMillis':{$lte:?1},'priceStatus':{$in:[null]}}")
	public Page<Product> findInvalidProductId(String vendorId,String time,Pageable pageable);
	
	/**
	 * 过期数据的数量
	 * @param vendorId 供应商id
	 * @param time 过期时间
	 * @return 数量
	 */
	@Query(value="{'vendorId':'?0','updatedTimeMillis':{$lte:?1},'priceStatus':{$in:[null]}},{'_id':1}",count=true)
	public Long countInvalidProducts(String vendorId,String time);
}