package com.yikuyi.product.rule.mov.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.rule.mov.vo.MovRuleTemplate;

@Repository
public interface MovRuleTemplateRepository extends MongoRepository<MovRuleTemplate, Long>{
	/**
	 * 分页查询mov
	 * @param sort
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Query("{'vendorId':?0,'status':{'$in':?1}}")
	public Page<MovRuleTemplate> findList(String vendorId,List<String> status,Pageable pageable);
	
	/**
	 * 根据IDs查询物料数据
	 * @param ids
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':?0}")
	public MovRuleTemplate findMovById(String _id);
	/**
	 * 分页查询mov
	 * @param sort
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Query("{'vendorId':?0,'ruleType':?1,'status':{'$in':?2}}")
	public List<MovRuleTemplate> findVendorList(String vendorId,String ruleType,List<String> status);

}
