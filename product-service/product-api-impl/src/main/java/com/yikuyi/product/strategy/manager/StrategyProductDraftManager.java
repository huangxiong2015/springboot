package com.yikuyi.product.strategy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.MongoCollection;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.strategy.dao.StrategyProductDraftClient;
import com.yikuyi.product.strategy.reposity.StrategyProductDraftRepository;
import com.yikuyi.strategy.model.Strategy.StrategyType;
import com.yikuyi.strategy.model.StrategyProductDraft;
import com.yikuyi.strategy.model.StrategyProductDraft.ProductStatus;
import com.ykyframework.model.IdGen;

/**
 * 包邮/限购模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class StrategyProductDraftManager {
	
	@Autowired
	private StrategyProductDraftRepository strategyProductDraftRepository;
	
	@Autowired
	private StrategyProductDraftClient strategyProductDraftClient;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	/**
	 * 正式商品列表
	 * @param strategyId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<StrategyProductDraft> list(String strategyId, int page, int pageSize) {
		Query query  = new Query();
		Criteria criteria = new Criteria();
		criteria.and("strategyId").is(strategyId);
		criteria.and("productStatus").ne(ProductStatus.DETETED.toString());
		int pageNo = 0;
		if(page>0){
			pageNo = page-1;
		}
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		query.addCriteria(criteria);
		Page<StrategyProductDraft> pageInfo = strategyProductDraftRepository.findStrategyProductDraftByPage(query.getQueryObject(), pageable);
		List<StrategyProductDraft> list = pageInfo.getContent();
		if(CollectionUtils.isEmpty(list)){
			PageInfo<StrategyProductDraft> pageResult = new PageInfo<>(Collections.emptyList());
			pageResult.setPageNum(page);
			pageResult.setPageSize(pageSize);
			pageResult.setTotal(pageInfo.getTotalElements());	
			return pageResult;
		}else{
			handleList(list);
		}
		PageInfo<StrategyProductDraft> pageResult = new PageInfo<>(list);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(pageInfo.getTotalElements());	
		return pageResult;
	}
	
	
	
	/**
	 * 处理数据
	 * @param list
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private void handleList(List<StrategyProductDraft> list) {
		for (StrategyProductDraft strategyProductDraft : list) {
			strategyProductDraft.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(strategyProductDraft.getCreatedTimeMillis()));
			strategyProductDraft.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(strategyProductDraft.getUpdatedTimeMillis()));
		}
		
	}




	/**
	 * 删除草稿信息
	 * @param ids
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void deleteBatch(List<String> ids,StrategyType strategyType) {
		//若是包邮信息信息，则将其彻底从mongodb中删除，如果是限购模块信息，则进行软删除
		if(StrategyType.FREE_DERIVERY.toString().equals(strategyType.toString())){
			MongoCollection<Document> specialOfferProductDraftCollection = strategyProductDraftClient.getCollection();
			specialOfferProductDraftCollection.deleteMany(new Document("_id",new Document("$in",ids)));
		}else if(StrategyType.LIMITATIONS.toString().equals(strategyType.toString())){
			if(CollectionUtils.isNotEmpty(ids)){
				for (int i = 0; i < ids.size(); i++) {
					StrategyProductDraft draft = strategyProductDraftRepository.findOne(ids.get(i));
					if(draft!=null){
						draft.setProductStatus(ProductStatus.DETETED);
						strategyProductDraftRepository.save(draft);
					}
				}
			}
		}
	}

	/**
	 * 添加草稿信息
	 * @param productDraft
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void addStrategyProductDraft(StrategyProductDraft productDraft) {
		LoginUser userInfo = RequestHelper.getLoginUser();
		productDraft.setId(String.valueOf(IdGen.getInstance().nextId()));
		productDraft.setOldProductId(productDraft.getProductId());
		productDraft.setCreator(userInfo.getId());
		productDraft.setCreatorName(userInfo.getUsername());
		productDraft.setCreatedTimeMillis(String.valueOf(System.currentTimeMillis()));
		productDraft.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
		productDraft.setLastUpdateUser(userInfo.getId());
		productDraft.setLastUpdateUserName(userInfo.getUsername());
		//添加默认有效
		productDraft.setProductStatus(ProductStatus.ENABLE);
		//处理仓库名称
		if(productDraft.getSourceId()!=null){
			List<String> list = new ArrayList<>();
			list.add(productDraft.getSourceId());
			List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(list);
			if(CollectionUtils.isNotEmpty(facilityList)){
				for (int i = 0; i < facilityList.size(); i++) {
					productDraft.setSourceName(facilityList.get(0).getFacilityName());
				}
			}
		}
		strategyProductDraftRepository.save(productDraft);
	}
	
	
	
	
	

		 
}