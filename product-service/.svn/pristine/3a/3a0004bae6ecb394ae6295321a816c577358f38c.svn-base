package com.yikuyi.product.brand.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.brand.model.ProductBrand;

@Repository
public interface BrandRepository extends MongoRepository<ProductBrand, Long>{
	
	
	/**
	 * 根据ID查询品牌
	 * @param id
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wanghong
	 */
	public ProductBrand findById(Integer id);
	
	@Query("{'logo':{ $exists: true },'desc':{ $exists: true },'brandName':{ $exists: true }}")
	public List<ProductBrand> findAllByCondition(Sort sort);
	
	/**
	 * 分页查询品牌
	 * @param sort
	 * @return
	 * @since 2017年3月20日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Query("?0")
	public Page<ProductBrand> getBrandList(Object obj,Pageable pageable);
	
	/**
	 * 分页查询品牌
	 * @param sort
	 * @return
	 * @since 2017年3月20日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Query("?0")
	public List<ProductBrand> getBrandList(Object obj);
	
	/**
	 * 查询制造商别名是否存在
	 * @param brandAlias
	 * @param obj
	 * @return
	 * @since 2017年3月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Query("{'brandAlias':{'$in':?0},'_id':{'$ne':?1}}")
	public List<ProductBrand> findBrandAliasIsExist(List<String> brandAlias,Integer id);
	
	/**
	 * 查询制造商名称是否存在
	 * @param brandName
	 * @param id
	 * @return
	 * @since 2017年3月27日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Query("{'brandName':?0,'_id':{'$ne':?1}}")
	public List<ProductBrand> findBrandNameIsExist(String brandName,Integer id);
	
	/**
	 * 删除制造商
	 * @param id
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void deleteById(Integer id);
	
	/**
	 * 根据IDs查询制造商信息
	 * @param ids
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<ProductBrand> findBrandByIds(List<Integer> ids);

}
