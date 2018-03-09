package com.yikuyi.product.strategy.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.yikuyi.product.common.dao.BaseMongoClient;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.strategy.cache.PackageMailCacheManager;
import com.yikuyi.product.strategy.dao.StrategyProductDraftClient;
import com.yikuyi.product.strategy.reposity.StrategyProductDraftRepository;
import com.yikuyi.product.strategy.reposity.StrategyProductRepository;
import com.yikuyi.product.strategy.reposity.StrategyRepository;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.strategy.model.Strategy;
import com.yikuyi.strategy.model.Strategy.StrategyStatus;
import com.yikuyi.strategy.model.Strategy.StrategyType;
import com.yikuyi.strategy.model.StrategyProduct;
import com.yikuyi.strategy.model.StrategyProductDraft;
import com.yikuyi.strategy.model.StrategyProductDraft.ProductStatus;
import com.yikuyi.strategy.vo.LimitedPurchaseVo;
import com.yikuyi.strategy.vo.StrategyVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;

/**
 * 包邮/限购模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class StrategyManager {
	
	private static final Logger logger = LoggerFactory.getLogger(StrategyManager.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private StrategyRepository strategyRepository;
	
	@Autowired
	private StrategyProductRepository strategyProductRepository;
	
	@Autowired
	private StrategyProductDraftRepository strategyProductDraftRepository;
	
	@Autowired
	private PackageMailCacheManager packageMailCacheManager;
	
	@Autowired
	private StrategyProductDraftClient strategyProductDraftClient;
	
	
	@Autowired
	private ProductRepository productRepository;
	
	
	@Autowired
	private ProductManager productManager;
	
	private static final String OK = "ok";
	
	private static final String ONE_POINT = "1.0";
	
	@Autowired
	private BaseMongoClient baseMongoClient;
	
	/**
	 * 包邮/限购规则列表查询
	 * @param title
	 * @param updateDateStart
	 * @param updateDateEnd
	 * @param creatorName
	 * @param strategyStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<StrategyVo> list(String title, String updateDateStart, String updateDateEnd, String creatorName,
			StrategyStatus strategyStatus,StrategyType strategyType,int page, int pageSize) {
		try {
			//拼装查询条件以及排序
			Query query = this.mergeCondition(title,updateDateStart,updateDateEnd,creatorName,strategyStatus,strategyType);
			int pageNo = 0;
			if(page>0){
				pageNo = page-1;
			} 
			Sort sort = new Sort(Direction.DESC,"createdTimeMillis");
			PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
			Page<StrategyVo> pageInfo = strategyRepository.findStrategyByPage(query.getQueryObject(), pageable);
			List<StrategyVo> volist = pageInfo.getContent();
			if(!CollectionUtils.isEmpty(volist)){
				volist = handleStrategyVoList(volist);
			}
			PageInfo<StrategyVo> pageResult = new PageInfo<>(volist);		
			pageResult.setPageNum(page);
			pageResult.setPageSize(pageSize);
			pageResult.setTotal(pageInfo.getTotalElements());	
			return pageResult;
		} catch (Exception e) {
			logger.error("专属特价列表信息：{}", e);
		}
		return null;
	}

	
	/**
	 * 条件查询
	 * @param title
	 * @param updateDateStart
	 * @param updateDateEnd
	 * @param creatorName
	 * @param strategyStatus
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private Query mergeCondition(String title, String updateDateStart, String updateDateEnd, String creatorName,
			StrategyStatus strategyStatus,StrategyType strategyType) {
		
		Query query = new Query();
		Criteria criteria = new Criteria();
		Optional<Criteria> optCriteria = Optional.ofNullable(criteria);
		if(!optCriteria.isPresent()){
			criteria = new Criteria();
		}
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").is(title);
		}
		try {
		if(StringUtils.isNotBlank(updateDateStart) && StringUtils.isNotBlank(updateDateEnd)){
				String startTime = String.valueOf(sdf.parse(updateDateStart+" 00:00:00").getTime());
				String endTime = String.valueOf(sdf.parse(updateDateEnd +" 23:59:59").getTime());
				criteria.and("updatedTimeMillis").gte(startTime).lt(endTime);
		}
		} catch (Exception e) {
			logger.error("时间查询：{}", e);
		}
		if(StringUtils.isNotBlank(creatorName)){
			criteria.and("creatorName").is(creatorName);
		}
		if(strategyStatus!=null){
			criteria.and("strategyStatus").is(strategyStatus.toString());
		}else{
			criteria.and("strategyStatus").ne(StrategyStatus.DELETED.toString());//过滤条件
		}
		if(strategyType!=null){
			criteria.and("strategyType").is(strategyType.toString());//类型
		}
		query.addCriteria(criteria);
		return query;
	}
	
	/**
	 * 处理数据
	 * @param list
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private List<StrategyVo> handleStrategyVoList(List<StrategyVo> list) {
		if(CollectionUtils.isNotEmpty(list)){
			for (StrategyVo strategyVo : list) {
				getStrategyVo(strategyVo);				
			}
		}
		return list;
	}


	/**
	 * 处理数据
	 * @param strategyVo
	 * @return
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private void getStrategyVo(StrategyVo strategyVo) {
		long startTime = Long.parseLong(strategyVo.getStartDate());
		long endTime = Long.parseLong(strategyVo.getEndDate());
		long currentTime = System.currentTimeMillis();
		if(StrategyStatus.START.toString().equals(strategyVo.getStrategyStatus().toString())){
			//如果是启动状态设置为进行中
			if(currentTime < startTime){
				strategyVo.setStatusShow(StrategyStatus.START.toString());
			}else if(currentTime > startTime  && currentTime < endTime){
				strategyVo.setStatusShow(StrategyStatus.PUBLISHED.toString());
			}else if(currentTime > endTime){
				strategyVo.setStatusShow(StrategyStatus.END.toString());
			}
		}else{
			strategyVo.setStatusShow(strategyVo.getStrategyStatus().toString());
		}
		strategyVo.setStartDate(UtilsHelp.timeStamp2Date(strategyVo.getStartDate()));
		strategyVo.setEndDate(UtilsHelp.timeStamp2Date(strategyVo.getEndDate()));
		strategyVo.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(strategyVo.getCreatedTimeMillis()));
		strategyVo.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(strategyVo.getUpdatedTimeMillis()));
	}


	/**
	 * 查询包邮/限购规则详情
	 * @param id
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public StrategyVo getStrategy(String id) {
		StrategyVo strategyVo = new StrategyVo();
		Strategy strategy = strategyRepository.findOne(id);
		if(strategy!=null){
			BeanUtils.copyProperties(strategy, strategyVo);
			getStrategyVo(strategyVo);
		}
		return strategyVo;
	}


	/**
	 * 删除包邮/限购信息
	 * @param id
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void deleteStrategyById(String id) throws BusinessException{
		LoginUser userInfo = RequestHelper.getLoginUser();
		Strategy strategy = strategyRepository.findOne(id);
		//判断状态,非停用状态下不能删除,软删除
		Strategy strategyNew = new Strategy();
		if(strategy!=null){
			if(strategy.getStrategyStatus()!=null && !StrategyStatus.HOLD.toString().equals(strategy.getStrategyStatus().toString())){
				throw new BusinessException(BusiErrorCode.STRATEGY_STATUS_NOT_HOLD, "非停用状态下，不能删除");
			}
			//1.对strategy表进行软删除
			BeanUtils.copyProperties(strategy, strategyNew);
			strategyNew.setStrategyStatus(StrategyStatus.DELETED);
			strategyNew.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			strategyNew.setLastUpdateUser(userInfo.getId());
			strategyNew.setLastUpdateUserName(userInfo.getUsername());
			strategyRepository.save(strategyNew);
			//2.限购商品信息需要删除product表中的信息
			if(StrategyType.LIMITATIONS.toString().equals(strategy.getStrategyType().toString())){
				List<String> productIds = new ArrayList<>();
				List<StrategyProduct> list = strategyProductRepository.findStrategyProductByStrategyId(id);
				if(CollectionUtils.isNotEmpty(list)){
					for (StrategyProduct strategyProduct : list) {
						productIds.add(strategyProduct.getProductId());
					}
				}
				if(CollectionUtils.isNotEmpty(productIds)){
					List<Product> productList = productRepository.findProductsByIds(productIds);
					//删除product表的限购商品信息(软删除)
					List<ProductVo> productVoList = new ArrayList<>();
					if(CollectionUtils.isNotEmpty(productList)){
						for (Product product : productList) {
							product.setStatus(0);
							Product productNew = productRepository.save(product);
							//2.修改product表中status状态之后，重新发送mq
							ProductVo productVo = new ProductVo();
							BeanUtils.copyProperties(productNew, productVo);
							productVoList.add(productVo);	
							
						}
					}
					//发送mq,此时前台不再显示限购商品的信息
					if(CollectionUtils.isNotEmpty(productVoList)){
						productManager.adviceSyncElasticsearch(productVoList);
					}
				}
			}
			//3.删除正式商品数据
			strategyProductRepository.deleteStrategyProductBystrategyId(id);
		}
	}


	/**
	 * 修改状态
	 * @param id
	 * @param strategyStatus
	 * @throws BusinessException
	 * @throws ParseException
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updateStrategyStatus(String id, StrategyStatus strategyStatus) throws BusinessException,ParseException{
			Strategy strategy = strategyRepository.findOne(id);
			if(strategy!=null){
				//1、判断活动时间，已过期的话活动不能被启用
				long currentTime = System.currentTimeMillis();
				long endDate = Long.parseLong(strategy.getEndDate());
				if(currentTime>endDate && StrategyStatus.START.toString().equals(strategyStatus.toString())){
						throw new BusinessException(BusiErrorCode.ENDDATE_BEFORE_CURRENT, "活动已过期，不能被启用");
				}
				//2.修改strategy表的状态
				Strategy strategyNew = new Strategy();
				BeanUtils.copyProperties(strategy, strategyNew);
				strategyNew.setId(id);
				strategyNew.setStrategyStatus(strategyStatus);
				strategyNew.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
				strategyRepository.save(strategyNew);
				//3.查询关联的商品，去重组成list
				List<String> productIdsList = new ArrayList<>(); 
				Set<String> idsList = new HashSet<>();
				List<StrategyProduct> productList = strategyProductRepository.findProductIdByStrategyId(id);
				List<ProductVo> productVoList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(productList)){
					for (StrategyProduct strategyProduct : productList) {
						if(strategyProduct!=null && strategyProduct.getProductId()!=null && strategyProduct.getOldProductId()!=null){
							//3.1处理product表中信息
							if(StrategyType.LIMITATIONS.toString().equals(strategy.getStrategyType().toString())){
								Product productlimit = productRepository.findOne(strategyProduct.getProductId());
								LimitedPurchaseVo limitedPurchaseVo = new LimitedPurchaseVo();
								if(productlimit!=null){
									if(StrategyStatus.START.toString().equals(strategyStatus.toString())){//启用
										//3.1.1首次启用，停用需要发送mq
										if(strategyProduct.getProductId().equals(strategyProduct.getOldProductId())){
											//1.重新生成一条product信息
											//新的productId
											String newId = String.valueOf(IdGen.getInstance().nextId());
											productlimit.setOriginalId(productlimit.getId());//设置商品的原始id
											productlimit.setId(newId);
											productlimit.setQuickFindKey(newId);
											productlimit.setExpirationTime(Long.parseLong(strategy.getEndDate()));
											
											limitedPurchaseVo.setStrategyId(id);
											limitedPurchaseVo.setLimitedPurchaseFlag("Y");
											limitedPurchaseVo.setStartTime(strategy.getStartDate());
											limitedPurchaseVo.setEndTime(strategy.getEndDate());
											limitedPurchaseVo.setNum(strategyProduct.getLimitNum());
											productlimit.setLimitedPurchaseInfo(limitedPurchaseVo);
											//2.添加到product表中
											Product productNew = productRepository.save(productlimit);
											
											//发送mq,
											ProductVo productVo = new ProductVo();
											BeanUtils.copyProperties(productNew, productVo);
											productVoList.add(productVo);	
											
											//3.替换正式商品表中productId
											strategyProduct.setProductId(newId);
											//4.重新商品表信息
											strategyProductRepository.save(strategyProduct);
										}else{//3.1.2如果不是第一次启动，停用，则要将限购数量，活动时间发送到mq
											productlimit.setStatus(1);//表示停用
											productlimit.setExpirationTime(Long.parseLong(strategy.getEndDate()));
											limitedPurchaseVo.setStrategyId(id);
											limitedPurchaseVo.setLimitedPurchaseFlag("Y");
											limitedPurchaseVo.setStartTime(strategy.getStartDate());
											limitedPurchaseVo.setEndTime(strategy.getEndDate());
											limitedPurchaseVo.setNum(strategyProduct.getLimitNum());
											productlimit.setLimitedPurchaseInfo(limitedPurchaseVo);
											//1、添加到product表中
											Product productNew = productRepository.save(productlimit);
											
											//2、发送mq,
											ProductVo productVo = new ProductVo();
											BeanUtils.copyProperties(productNew, productVo);
											productVoList.add(productVo);	
										}
									}else if(StrategyStatus.HOLD.toString().equals(strategyStatus.toString())){//停用时删除缓存
										productlimit.setStatus(2);//表示停用
										productlimit.setExpirationTime(System.currentTimeMillis());
										limitedPurchaseVo.setLimitedPurchaseFlag("Y");
										productlimit.setLimitedPurchaseInfo(limitedPurchaseVo);
										//1、添加到product表中
										Product productNew = productRepository.save(productlimit);
										
										//2、发送mq,
										ProductVo productVo = new ProductVo();
										BeanUtils.copyProperties(productNew, productVo);
										productVoList.add(productVo);
									}		
								}
							}
							//3.2只有包邮的才走缓存
							if(StrategyType.FREE_DERIVERY.toString().equals(strategy.getStrategyType().toString())){
								//组成productId的集合
								idsList.add(strategyProduct.getProductId());
							}
						}
					}
					productIdsList.addAll(idsList);
				}
				//4.发送mq,将限购信息添加到搜索引擎(启用，停用的时候需要发送mq)
				if(StrategyType.LIMITATIONS.toString().equals(strategy.getStrategyType().toString())){
					if(StrategyStatus.START.toString().equals(strategyStatus.toString()) || 
							StrategyStatus.HOLD.toString().equals(strategyStatus.toString())){//发送mq
						if(CollectionUtils.isNotEmpty(productVoList)){
							//发送mq,将限购信息添加到搜索引擎
							productManager.adviceSyncElasticsearch(productVoList);
						}
					}
				}
				//5.根据商品的id集合，去处理缓存(只有包邮的信息才走缓存)
				if(StrategyType.FREE_DERIVERY.toString().equals(strategy.getStrategyType().toString())){
					if(CollectionUtils.isNotEmpty(productIdsList) && StrategyStatus.START.toString().equals(strategyStatus.toString())){//启用
						packageMailCacheManager.addStrategyCache(productIdsList, strategyNew);
					}else if(CollectionUtils.isNotEmpty(productIdsList) && StrategyStatus.HOLD.toString().equals(strategyStatus.toString())){//停用时删除缓存
						productIdsList.stream().forEach(key -> {
							packageMailCacheManager.deleteStrategyCache(key, strategyNew);
						});
					}
				}
				
			}
		
	}

	
	/**
	 * 添加包邮/限购模块相关信息
	 * @param strategyId
	 * @param strategy
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void addStrategy(String strategyId, Strategy strategy)  throws BusinessException{
			LoginUser userInfo = RequestHelper.getLoginUser();
			//添加包邮规则信息
			strategy.setId(strategyId);
			strategy.setStrategyStatus(StrategyStatus.HOLD);
			strategy.setCreator(userInfo.getId());
			strategy.setCreatorName(userInfo.getUsername());
			strategy.setCreatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			strategy.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			strategy.setLastUpdateUser(userInfo.getId());
			strategy.setLastUpdateUserName(userInfo.getUsername());
			//判断活动起始时间不能大于活动结束时间
			if(strategy.getStartDate()!=null && strategy.getEndDate()!=null){
				long startDate = Long.parseLong(strategy.getStartDate());
				long endDate = Long.parseLong(strategy.getEndDate());
				if(startDate > endDate){//
					throw new BusinessException(BusiErrorCode.ENDDATE_BEFORE_STARTDATE, "活动结束时间不能大于活动起始时间");
				}
			}
			int num = strategyProductDraftRepository.findNumStrategyId(strategyId,ProductStatus.ENABLE.toString());
			if(num == 0)
			{
				throw new BusinessException(BusiErrorCode.SPECIAL_PRODUCT_ENABLE_NULL, "没有有效的数据，不能保存");
			}
			//将商品的信息从草稿表中迁移到正式表中
			Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copyStrategyProductDraft(\""+strategyId+"\")"));
			if(!ONE_POINT.equals(result.get(OK).toString())){
				throw new BusinessException(BusiErrorCode.COPY_PRODUCT_DRAFT, " 商品信息迁移到正式表中报错");
			}
			strategyRepository.insert(strategy);
		}


	/**
	 * 将正式商品表中的数据迁移到草稿表中
	 * @param strategyId
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void copyStrategyProductToDraft(String strategyId) throws BusinessException{
		//在迁移数据之前先清除一下草稿表中的数据
		strategyProductDraftRepository.deleteStrategyProductDraftByStrategyId(strategyId);
		Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copyStrategyProductToDraft(\""+strategyId+"\")"));
		if(!ONE_POINT.equals(result.get(OK).toString())){
			throw new BusinessException(BusiErrorCode.COPY_PRODUCT_TO_DRAFT, " 将商品信息从正式表迁移到草稿表抛异常");
		}				
		
	}

	
	
	
	/**
	 * 编辑包邮/限购模块信息
	 * @param strategy
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updateStrategy(String id,Strategy strategy) throws BusinessException{
		Strategy strategyOld = strategyRepository.findOne(strategy.getId());
		LoginUser userInfo = RequestHelper.getLoginUser();
		if(strategyOld!=null){//编辑产品线内容信息
			strategy.setStrategyStatus(strategyOld.getStrategyStatus());
			strategy.setCreator(strategyOld.getCreator());
			strategy.setCreatorName(strategyOld.getCreatorName());
			strategy.setCreatedTimeMillis(strategyOld.getCreatedTimeMillis());
			strategy.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			strategy.setLastUpdateUser(userInfo.getId());
			strategy.setLastUpdateUserName(userInfo.getUsername());
		}
		//1.如果草稿表中没有有效的数据是不能编辑的
		int num = strategyProductDraftRepository.findNumStrategyId(id,ProductStatus.ENABLE.toString());
		if(num == 0)
		{
			throw new BusinessException(BusiErrorCode.SPECIAL_PRODUCT_ENABLE_NULL, "没有有效的数据，不能保存");
		}
		//2.清除正式表中的数据
		strategyProductRepository.deleteStrategyProductBystrategyId(id);
		//3.处理限购信息添加
		if(StrategyType.LIMITATIONS.toString().equals(strategy.getStrategyType().toString())){
			//查询product信息，并进行复制
			List<StrategyProductDraft> draftList = strategyProductDraftRepository.findStrategyProductDraftByStrategyId(id);
			if(CollectionUtils.isNotEmpty(draftList)){
				List<String> productDraftIds = new ArrayList<>(); //删除的草稿商品的id集合
				List<String> productIds = new ArrayList<>();
				//3.2.1此处表示需要删除的数据
				for (StrategyProductDraft strategyProductDraft : draftList) {
					//将productStatus为DALETED状态的数据彻底删除掉
					if(ProductStatus.DETETED.toString().equals(strategyProductDraft.getProductStatus().toString())){
						productDraftIds.add(strategyProductDraft.getId());
						//如果商品id与原始商品id保持一致，则表示未启动之前的编辑操作，此处不删除product表中的信息
						if(!strategyProductDraft.getProductId().equals(strategyProductDraft.getOldProductId())){
							productIds.add(strategyProductDraft.getProductId());
						}
					}			
				}
				//3.2.2删除的strategy_product_draft表中的信息
				if(CollectionUtils.isNotEmpty(productDraftIds)){
					MongoCollection<Document> specialOfferProductDraftCollection = strategyProductDraftClient.getCollection();
					specialOfferProductDraftCollection.deleteMany(new Document("_id",new Document("$in",productDraftIds)));
					
				}
				List<ProductVo> productVoList = new ArrayList<>();
				//3.2.3查询product表中是否有需要删除的数据，若有则删除调(软删除)
				if(CollectionUtils.isNotEmpty(productIds)){
					List<Product> productList = productRepository.findProductsByIds(productIds);
					for (Product product : productList) {
						//1.对product表进行软删除
						product.setStatus(0);
						Product productNew = productRepository.save(product);
						//2.修改product表中status状态之后，重新发送mq
						ProductVo productVo = new ProductVo();
						BeanUtils.copyProperties(productNew, productVo);
						productVoList.add(productVo);	
					}
					
				}
				//发送mq,此时前台不再显示限购商品的信息
				if(CollectionUtils.isNotEmpty(productVoList)){
					productManager.adviceSyncElasticsearch(productVoList);
				}
				
			}
		}
		//4.从草稿表中重新迁移数据
		Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copyStrategyProductDraft(\""+id+"\")"));
		if(!ONE_POINT.equals(result.get(OK).toString())){//迁移数据成功
			throw new BusinessException(BusiErrorCode.COPY_PRODUCT_DRAFT, " 商品信息迁移到正式表中报错");
		}
		//5.保存包邮主表信息
		strategyRepository.save(strategy);
	}
	
	
	
}