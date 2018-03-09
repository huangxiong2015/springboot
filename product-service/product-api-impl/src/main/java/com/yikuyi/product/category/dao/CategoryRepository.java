package com.yikuyi.product.category.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.vo.ProductCategoryParent;

@Repository
public interface CategoryRepository extends MongoRepository<ProductCategory, Long>{
	/**
	 * 动态查询分类
	 * @param param
	 * @return
	 */
	@Query("{'parent.$id':?0}")
	public List<ProductCategory> find(Object param);
	
	/**
	 * 根据名称获取分类
	 * @param param
	 * @return
	 */
	@Query("{'cateName':?0}")
	public List<ProductCategory> findByCateName(String cateName);
	
	/**
	 * 根据名称获取分类（带父类）
	 * @param param
	 * @return
	 */
	@Query("{'cateName':?0}")
	public List<ProductCategoryParent> findByCateNameWithParent(String cateName);
	
	/**
	 * 根据ID查询分类
	 * @param id
	 * @return
	 */
	public ProductCategory findById(Integer id);
	
	/**
	 * 查询全部分类
	 * @return
	 */
	@Query("{'status':{$in:?0}}")
	public List<ProductCategoryParent> findAllCategoryWithParent(Integer[] status,Sort sort);
	
	/**
	 * 查询全部分类
	 * @return
	 */
	@Query("{'cateName':{$in:?0},'cateLevel':?1,'status':1}")
	public List<ProductCategoryParent> findCategoryByNameAndLevle(Set<String> cateNames ,Integer  cateLevel);
	
	/**
	 * 根据ID查询分类
	 * @return
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<ProductCategoryParent> findByIds(List<Long> ids);
	
	/**
	 * 根据条件动态查询
	 * @param obj
	 * @return
	 */
	@Query("?0")
	public List<ProductCategory> getCategories(Object obj);
}
