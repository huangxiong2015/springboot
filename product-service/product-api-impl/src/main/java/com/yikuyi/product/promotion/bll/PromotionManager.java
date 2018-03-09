package com.yikuyi.product.promotion.bll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.framework.springboot.model.LoginUser;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.pay.coupon.CouponActivityRel;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.common.utils.JsonDateValueProcessor;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.goods.manager.PriceQueryManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.promotion.dao.PromoModuleProductDao;
import com.yikuyi.product.promotion.dao.PromoModuleProductDraftDao;
import com.yikuyi.product.promotion.dao.PromotionDao;
import com.yikuyi.product.promotion.dao.PromotionModuleDao;
import com.yikuyi.product.promotion.dao.PromotionModuleDraftDao;
import com.yikuyi.product.promotion.repository.PromotionModuleContentDraftRepository;
import com.yikuyi.product.promotion.repository.PromotionModuleContentRepository;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.promotion.model.PromoModuleProduct;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.CreateType;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.model.PromotionModule.PromoModuleType;
import com.yikuyi.promotion.model.PromotionModuleContent;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft.PromoModuleStatus;
import com.yikuyi.promotion.vo.PromoModuleProductVo;
import com.yikuyi.promotion.vo.PromotionAndModuleDraftVo;
import com.yikuyi.promotion.vo.PromotionAndModuleVo;
import com.yikuyi.promotion.vo.PromotionModuleDraftVo;
import com.yikuyi.promotion.vo.PromotionModuleVo;
import com.yikuyi.promotion.vo.PromotionParamVo;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.promotion.vo.PromotionProductVo.ModuleProductStatus;
import com.yikuyi.promotion.vo.PromotionVo;
import com.yikuyi.rule.price.PriceInfo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
import com.ykyframework.oss.AliyunOSSAccount;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Service
public class PromotionManager {

	private static final Logger logger = LoggerFactory.getLogger(PromotionManager.class);
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private PromotionModuleDraftDao promotionModuleDraftDao;
	
	@Autowired
	private PromotionModuleDao promotionModuleDao;
	
	@Autowired
	private PromoModuleProductDraftDao promoModuleProductDraftDao;
	
	@Autowired
	private PromoModuleProductDao promoModuleProductDao;
	
	@Autowired
	private PromoModuleProductManager promoModuleProductManager;
	
	@Autowired
	private PromotionModuleContentDraftRepository moduleContentDraftRepository;
	
	
	@Autowired
	private PromotionModuleContentRepository moduleContentRepository;
	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private PromotionCacheManager promotionCacheManager;
	
	@Autowired
	private ProductManager productManager;

	
	@Autowired
	private PayClientBuilder payClientBuilder;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	
	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	private AliyunOSSAccount aliyunOSSAccount;
	
	/**
	 * 查询促销活动列表
	 * @param paramVo
	 * @param rowBounds
	 * @return
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<PromotionVo> getPromotionList(PromotionParamVo paramVo, RowBounds rowBounds) {
		PageInfo<PromotionVo> pageInfo = new PageInfo<>(promotionDao.getPromotionVoList(paramVo, rowBounds));
		try {
			if((pageInfo.getList())!=null && CollectionUtils.isNotEmpty(pageInfo.getList())){
				for (PromotionVo promotionVo : pageInfo.getList()) {
					String startDate = sdf.format(promotionVo.getStartDate());// 活动开始时间
					String endDate = sdf.format(promotionVo.getEndDate());// 活动结束时间
					String nowDate = sdf.format(new Date());// 当前日期
					String timeStatusNew = "";  //时间状态
					//无效情况下则无时间状态
					if(PromotionStatus.ENABLE.equals(promotionVo.getPromotionStatus())){
						if(sdf.parse(nowDate).before(sdf.parse(startDate))){  //系统时间小于开始时间
							timeStatusNew = "未开始";
						}else if(sdf.parse(nowDate).after(sdf.parse(endDate))){
							timeStatusNew = "已结束";
						}else{
							timeStatusNew = "进行中";
						}
					}
					promotionVo.setTimeStatus(timeStatusNew);
				}
			}
			pageInfo.setList(pageInfo.getList());
		} catch (Exception e) {
			logger.error("查询活动列表信息:{}", e);
		}
		return pageInfo;
	}
	

	
	/**
	 * 创建活动
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(Promotion promotion) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		String userName = Optional.ofNullable(RequestHelper.getLoginUser()).map(LoginUser::getUsername).orElse(StringUtils.EMPTY);
		promotion.setPromotionId(id);
		promotion.setCreator(userId);
		promotion.setCreatorName(userName);
		promotion.setCreatedDate(new Date());
		promotion.setPromotionStatus(PromotionStatus.DISABLE);
		promotion.setLastUpdateUserName(userName);
		promotion.setLastUpdateDate(new Date());
		promotion.setLastUpdateUser(userId);
		promotionDao.insert(promotion);
	}

	
	/**
	 * 在草稿表中复制促销活动草稿信息
	 * @param promoModuleId
	 * @param userId
	 * @param userName
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String draftCopy(String promotionId, String userId) {
		String falg = "success";
		try {
			
			//复制促销活动信息
			Promotion promotion = promotionDao.getPromotion(promotionId);
			Promotion promotionNew = new Promotion();
			if(promotion == null){
				falg = "PromotionDataIsNull";
				throw new BusinessException(falg, "暂无匹配的促销活动！");
			}
			BeanUtils.copyProperties(promotion ,promotionNew);
			promotionNew.setPromotionId(String.valueOf(IdGen.getInstance().nextId()));
			promotionNew.setPromotionName(promotion.getPromotionName()+"_复制");
			promotionNew.setPromotionStatus(PromotionStatus.DISABLE);//复制活动的时候状态为无效状态
			promotionNew.setStartDate(new Date());  //开始时间顺延一周
			promotionNew.setEndDate(UtilsHelp.getDayWeek(new Date()));  //结束时间顺延一周
			promotionNew.setCreator(userId);
			promotionNew.setLastUpdateUser(userId);
			promotionNew.setCreatedDate(new Date());
			promotionNew.setLastUpdateDate(new Date());
			promotionDao.insert(promotionNew);
			
			//查询促销模块草稿表信息
			PromotionModuleDraft moduleDraft = new PromotionModuleDraft();
			moduleDraft.setPromotionId(promotionId);
			List<PromotionModuleDraft> moduleDraftList = promotionModuleDraftDao.getPromotionModuleDraftList(moduleDraft);
			if(moduleDraftList!=null && CollectionUtils.isNotEmpty(moduleDraftList)){
				for (PromotionModuleDraft promotionModuleDraft : moduleDraftList) {
					PromotionModuleDraft promotionModuleDraftNew = new PromotionModuleDraft();
					BeanUtils.copyProperties(promotionModuleDraft ,promotionModuleDraftNew);
					promotionModuleDraftNew.setPromoModuleId(String.valueOf(IdGen.getInstance().nextId()));
					promotionModuleDraftNew.setPromotionId(promotionNew.getPromotionId());
					promotionModuleDraftNew.setCreator(userId);
					promotionModuleDraftNew.setLastUpdateUser(userId);
					promotionModuleDraftNew.setCreatedDate(new Date());
					promotionModuleDraftNew.setLastUpdateDate(new Date());
					
					//复制促销模块信息到数据库
					promotionModuleDraftDao.insert(promotionModuleDraftNew);
					
					//复制促销商品草稿表信息
					PromoModuleProductDraft  productDraft = new PromoModuleProductDraft();
					productDraft.setPromoModuleId(promotionModuleDraft.getPromoModuleId());
					//查询促销商品草稿表信息
					List<PromoModuleProductDraft> moduleProductDraftList = promoModuleProductDraftDao.getPromoModuleProductDraftList(productDraft);
					if(moduleProductDraftList!=null && CollectionUtils.isNotEmpty(moduleProductDraftList)){
						for (PromoModuleProductDraft promoModuleProductDraft : moduleProductDraftList) {
							PromoModuleProductDraft promoModuleProductDraftNew = new PromoModuleProductDraft();
							BeanUtils.copyProperties(promoModuleProductDraft ,promoModuleProductDraftNew);
							promoModuleProductDraftNew.setPromoModuleProductId(String.valueOf(IdGen.getInstance().nextId()));
							promoModuleProductDraftNew.setPromoModuleId(promotionModuleDraftNew.getPromoModuleId());
							promoModuleProductDraftNew.setPromotionId(promotionNew.getPromotionId());
							promoModuleProductDraftNew.setCreator(userId);
							promoModuleProductDraftNew.setLastUpdateUser(userId);
							promoModuleProductDraftNew.setCreatedDate(new Date());
							promoModuleProductDraftNew.setLastUpdateDate(new Date());
							
							//复制促销商品信息到数据库
							promoModuleProductDraftDao.insert(promoModuleProductDraftNew);
						}
					}
					
					
					//复制mongodb中的数据
					PromotionModuleContentDraft module = moduleContentDraftRepository.findOne(promotionModuleDraft.getPromoModuleId());
					PromotionModuleContentDraft contentDraftNew = new PromotionModuleContentDraft();
					if(module!=null){
						BeanUtils.copyProperties(module ,contentDraftNew);
						contentDraftNew.setPromoModuleId(promotionModuleDraftNew.getPromoModuleId());
						contentDraftNew.setPromotionId(promotionNew.getPromotionId());
						
						//复制该数据到mongodb数据库
						moduleContentDraftRepository.insert(contentDraftNew);
					}
				}
			}
		} catch (Exception e) {
			logger.error("在草稿表中复制促销活动草稿信息:{}", e);
		}
		return falg;
	}
	/**
	 * 编辑活动
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void update(Promotion promotion) {
		String userId = RequestHelper.getLoginUserId();
		promotion.setLastUpdateUser(userId);
		promotion.setLastUpdateUserName(RequestHelper.getLoginUser().getUsername());
		promotion.setLastUpdateDate(new Date());
		promotionDao.update(promotion);
	}
	
	/**
	 * 根据活动ID和状态修改状态
	 * 
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	public synchronized void updatePromotionStatus(String promotionId, PromotionStatus status) throws BusinessException {
		java.util.Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND,0);
		Promotion promotionVo = promotionDao.getPromotion(promotionId);
		if(ObjectUtils.isEmpty(promotionVo)){
			throw new BusinessException("PROMOTION_NOT_FIND","活动不存在!");
		}
		//活动结束时间大于当前时间才能发布
		if(status == PromotionStatus.ENABLE && promotionVo.getEndDate().getTime()<calendar.getTime().getTime()){
			throw new BusinessException("PROMOTION_END_DATE_ERROR","活动结束时间必须大于当前时间!");
		}
		//模版创建的发布时候必须要有模块
		if(status == PromotionStatus.ENABLE && CreateType.TEMPLATE == promotionVo.getCreateType()){
			PromotionModuleDraft queryVo = new PromotionModuleDraft();
			queryVo.setPromotionId(promotionId);
			queryVo.setPromoModuleStatus(PromoModuleStatus.ENABLE);
			List<PromotionModuleDraft> list = promotionModuleDraftDao.getPromotionModuleDraftList(queryVo);
			if(CollectionUtils.isEmpty(list)){
				throw new BusinessException("PROMOTION_NOT_HAVE_MODULE_ERROR","活动不存在装修模块!");
			}
		}
		
		//构建需要更新的Promotion对象
		String userId = RequestHelper.getLoginUserId();
		Promotion updatePromotion = new Promotion();
		updatePromotion.setPromotionId(promotionId);
		updatePromotion.setPromotionStatus(status);
		updatePromotion.setLastUpdateUser(userId);
		updatePromotion.setLastUpdateUserName(RequestHelper.getLoginUser().getUsername());
		updatePromotion.setLastUpdateDate(new Date());
		//修改正式数据
		updatePromotion.setDisplaySidebar(promotionVo.getPreviewDisplaySidebar());
		
		//启用，并且活动开始时间大于当前时间，加入缓存
		if(status == PromotionStatus.ENABLE){//启用
			migrateDate(promotionId, promotionVo , calendar);
		}else if(CreateType.TEMPLATE == promotionVo.getCreateType() && status == PromotionStatus.DISABLE){//停用
			deleteCacheByPromoId(promotionId);
		}
		
		//修改状态
		promotionDao.update(updatePromotion);
		
		//启用时调用优惠券信息
		if(status == PromotionStatus.ENABLE){
			if(!ObjectUtils.isEmpty(promotionVo) && CreateType.TEMPLATE == promotionVo.getCreateType()){
				if("Y".equals(promotionVo.getIsUseCoupon())){//使用优惠券
					//调用sdk优惠券的信息，并保存
					CouponActivityRel couponActivityRel = new CouponActivityRel();
					couponActivityRel.setActivityId(promotionVo.getPromotionId());
					couponActivityRel.setActivityName(promotionVo.getPromotionName());
					if (StringUtils.isNotBlank(promotionVo.getCouponIds())) {
						couponActivityRel.setCouponIds(Arrays.asList(promotionVo.getCouponIds().trim().split(",")));
					}
					couponActivityRel.setStatus("VALID");
					try {
						payClientBuilder.couponResource().addCouponActivity(couponActivityRel, authorizationUtil.getLoginAuthorization());
					} catch (Exception e) {
						logger.error("保存优惠券信息失败：{}, userId ：{},Authorization ：{}",e,userId,authorizationUtil.getMockAuthorization());
					}				
				}
			}
		}
		
	}
	
	/**
	 * 迁移数据，如果符合条件就增加缓存
	 * @param promotionId
	 * @param promotionVo
	 * @param calendar
	 * @throws BusinessException
	 * @since 2017年10月14日
	 * @author jik.shu@yikuyi.com
	 */
	private void migrateDate(String promotionId ,Promotion promotionVo , Calendar calendar) throws BusinessException{
		try {
			/* 迁移MySql数据  START */
			promotionModuleDao.deleteByPromoId(promotionId);
			promotionModuleDao.copyDraftByPromoId(promotionId);
			promoModuleProductDao.deleteByPromoId(promotionId);
			promoModuleProductDao.copyDraftByPromoId(promotionId);
			
			//提前查询需要转移到正式表的数据
			List<PromotionModuleContentDraft> tempModuleContentDrafts = moduleContentDraftRepository.findModuleContentsByPromoId(promotionId);
			ObjectMapper mapper = new ObjectMapper();
			JavaType valueType = TypeFactory.defaultInstance().constructParametricType(List.class, PromotionModuleContent.class);
			List<PromotionModuleContent> tempModuleContents = mapper.readValue(mapper.writeValueAsString(tempModuleContentDrafts), valueType);
			
			//符合条件，新增缓存否则，删除之前的缓存
			if(CreateType.TEMPLATE == promotionVo.getCreateType() && promotionVo.getStartDate().getTime()<=calendar.getTime().getTime()){
				addCacheByPromoId(promotionVo, tempModuleContents);
			}else if(CreateType.TEMPLATE == promotionVo.getCreateType()){
				deleteCacheByPromoId(promotionId);
			}
			
			/* 迁移mongodb数据  START */
			moduleContentRepository.deleteByPromoId(promotionId);
			moduleContentRepository.save(tempModuleContents);
			/* 迁移mongodb数据  end */
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BusinessException("START_PROMOTIN_ERROR","启动活动失败!");
		} 
	}
	
	/**
	 * 增加活动缓存(新增之前需要删除一次)
	 * @param updatePromotion
	 * @param promotionId
	 * @throws BusinessException
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	private void addCacheByPromoId(Promotion promotionVo, List<PromotionModuleContent> tempModuleContents) {
		String promotionId = promotionVo.getPromotionId();
		
		/* 数据迁移前获取要删除的活动数据 */
		List<PromoModuleProduct> delProducts =  promoModuleProductDao.getAllProductByPromoId(promotionId);
		List<PromotionModuleContent> delModuelContents = moduleContentRepository.getBatchModuleByPromoIdAndType(promotionId, PromoModuleType.PRODUCT_LIST.toString());
		List<PromotionModuleContent> addModuleContents = null;
		if(null == tempModuleContents){
			addModuleContents = moduleContentRepository.getBatchModuleByPromoIdAndType(promotionId, PromoModuleType.PRODUCT_LIST.toString());
		}else{
			addModuleContents = tempModuleContents;
		}
		
		//新增商品缓存
		List<PromoModuleProductVo> products = promoModuleProductManager.getAllModuleProduct(promotionId);
		promotionCacheManager.addActivityProductCacheAfterDel(promotionId, delProducts , products);
		//新增规则缓存
		promotionCacheManager.addActivityRuleCacheAfterDel(delModuelContents, promotionVo , addModuleContents);
	}
	
	/**
	 * 通过活动ID删除正式缓存
	 * @param promotionId
	 * @throws BusinessException
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	private void deleteCacheByPromoId(String promotionId) {
		
		List<PromoModuleProduct> products = promoModuleProductDao.getAllProductByPromoId(promotionId);
		promotionCacheManager.deleteActivityProductCache(promotionId, products);
		
		List<PromotionModuleContent> modelContents = moduleContentRepository.getBatchModuleByPromoIdAndType(promotionId, PromoModuleType.PRODUCT_LIST.toString());
		promotionCacheManager.deleteActivityRuleCache(promotionId, modelContents);
	}
	
	/**
	 * 查询活动
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Promotion getPromotion(String promotionId){
		return promotionDao.getPromotion(promotionId);
	}
	
	

	/**
	 * 查询模块商品列表
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<PromotionProductVo> listModuleProduct(String draft,String isCalPrice,PromotionProductVo promoModuleProduct, RowBounds rowBounds) {
		List<PromotionProductVo> productList = null;
		//如果是草稿不做任何处理
		if("Y".equals(draft)){
			if("Y".equals(isCalPrice)){
				promoModuleProduct.setStatus(ModuleProductStatus.ENABLE);
			}
			productList = promoModuleProductDraftDao.listDraftModuleProduct(promoModuleProduct, rowBounds);
		}else{
			promoModuleProduct.setStatus(ModuleProductStatus.ENABLE);
			productList = promoModuleProductDao.listModuleProduct(promoModuleProduct, rowBounds);
		}
		List<PromotionProductVo> spuProductDraftList =	setModuleSpu(productList);
		return new PageInfo<>(priceCalculation(spuProductDraftList,isCalPrice,draft));
	}
	
	private List<PromotionProductVo> setModuleSpu(List<PromotionProductVo> productDraftList){
		if(CollectionUtils.isNotEmpty(productDraftList)){
			for(int i=0;i<productDraftList.size();i++){
				PromotionProductVo	promotionProductVo = productDraftList.get(i);
				Map<String,String> map = new HashMap<>();
				map.put("manufacturerPartNumber", promotionProductVo.getManufacturerPartNumber());//型号
				map.put("manufacturer", promotionProductVo.getManufacturer());//制造商
				promotionProductVo.setSpu(map);
				promotionProductVo.setId(promotionProductVo.getProductId());
				productDraftList.set(i, promotionProductVo);
			}
		}
		
		return productDraftList;
	}


	private void mergePrices(List<PromotionProductVo> productList){
		productList.stream().filter(v->StringUtils.isNotEmpty(v.getPriceBreak1())).forEach(promotionProductVo->{
			List<String> priceBreakList = new ArrayList<>();
			List<String> qtyBreakList = new ArrayList<>();
			//第一个梯度，默认5个梯度
			if(null != promotionProductVo.getPriceBreak1()){
				priceBreakList.add(promotionProductVo.getPriceBreak1());
				qtyBreakList.add(promotionProductVo.getQtyBreak1());
			}
			if(null != promotionProductVo.getPriceBreak2()){
				priceBreakList.add(promotionProductVo.getPriceBreak2());
				qtyBreakList.add(promotionProductVo.getQtyBreak2());
			}
			if(null != promotionProductVo.getPriceBreak3()){
				priceBreakList.add(promotionProductVo.getPriceBreak3());
				qtyBreakList.add(promotionProductVo.getQtyBreak3());
			}
			if(null != promotionProductVo.getPriceBreak4()){
				priceBreakList.add(promotionProductVo.getPriceBreak4());
				qtyBreakList.add(promotionProductVo.getQtyBreak4());
			}
			if(null != promotionProductVo.getPriceBreak5()){
				priceBreakList.add(promotionProductVo.getPriceBreak5());
				qtyBreakList.add(promotionProductVo.getQtyBreak5());
			}
			promotionProductVo.setQtyBreak(qtyBreakList);
			promotionProductVo.setPriceBreak(priceBreakList);
		});
	}
	
	public List<PromotionProductVo> priceCalculation(List<PromotionProductVo> productList, String isCalPrice, String draft) {
		try {
			if ("Y".equals(draft) && !"Y".equals(isCalPrice)) {// 如果是草稿,并且是编辑页面查询，只查询Mysql价格
				mergePrices(productList);
			}else{
				//否则转换mysql多列成梯度
				mergePrices(productList);
				//计算价格
				setResalePricesByPrices(productList, draft);
			}
			//后期优化批量
			setQtyByProductId(productList, draft);
		} catch (Exception e) {
			logger.error("活动列表价格获取失败,{}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return productList;
	}
	
	public void setQtyByProductId(List<PromotionProductVo> productList,String draft){
		for (int i = 0; i < productList.size(); i++) {
			setQtyByProductVos(productList.get(i) , draft);
		}
	}
	
	private void setQtyByProductVos(PromotionProductVo v , String draft){
		try {
			if(StringUtils.isEmpty(v.getProductId())){
				return;
			}
			ProductVo productVo = new ProductVo();
			Product product = productRepository.findOne(v.getProductId());
			BeanUtils.copyProperties(product ,productVo);
			List<ProductVo> priceProducts = new ArrayList<>();
			priceProducts.add(productVo);
			productManager.mergeActivity(priceProducts, draft);
			v.setQty(priceProducts.get(0).getQty());
		} catch (Exception e) {
			logger.error("qty setQtyByProductVos 失败,{}",e.getMessage(),e);
		}
	}
	
	private void setResalePricesByPrices(List<PromotionProductVo> productList,String draft){
		for (int i = 0; i < productList.size(); i++) {
			PromotionProductVo v = productList.get(i);
			if (CollectionUtils.isEmpty(v.getPriceBreak())) {
				setResalePricesFromMongodb(v,draft);
			} else {
				setResalePricesByPrices(v);
			}
		}
	}
	
	private void setResalePricesByPrices(PromotionProductVo v){
		List<ProductPrice> prices = new ArrayList<>();
		ProductPrice productPrice = new ProductPrice();
		productPrice.setCurrencyCode(v.getCurrencyUomId());// 币种
		// 梯度价格
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		for (int j = 0; j < v.getPriceBreak().size(); j++) {
			if (!NumberUtils.isNumber(v.getQtyBreak().get(j)) || !NumberUtils.isNumber(v.getPriceBreak().get(j))) {
				continue;
			}
			ProductPriceLevel priceLevel = new ProductPriceLevel();
			priceLevel.setBreakQuantity(Long.parseLong(v.getQtyBreak().get(j)));
			priceLevel.setPrice(v.getPriceBreak().get(j));
			priceLevels.add(priceLevel);
		}
		productPrice.setPriceLevels(priceLevels);
		prices.add(productPrice);
		v.setOriginalResalePrices(prices);
		v.setPrices(prices);
	}
	
	private void setResalePricesFromMongodb(PromotionProductVo v,String draft){
		try {
			ProductVo productVo = new ProductVo();
			productVo.setId(v.getProductId());
			Product product = productRepository.findOne(v.getProductId());
			if(null == product){
				return;
			}
			BeanUtils.copyProperties(product ,productVo);
			List<ProductVo> priceProducts = new ArrayList<>();
			priceProducts.add(productVo);
			productManager.mergeActivity(priceProducts, draft);
			List<PriceInfo> listPrices = priceQueryManager.queryPriceWithActivity(priceProducts);
			if(CollectionUtils.isNotEmpty(listPrices)){
				//促销价格
				v.setPrices(listPrices.get(0).getResalePrices());
				v.setOriginalResalePrices(listPrices.get(0).getOriginalResalePrices());
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(),e);
		}
	}
	
	public List<String> getProductIdList(List<PromotionProductVo> productList){
		return productList.stream().map(PromotionProductVo ::getProductId).collect(Collectors.toList());
	}
	
	/**
	 * 根据prmotionId查询所有的草稿模块json数据
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String getPromotionModuleDraft2Json(String promotionId) {
		String result = "";
		JSONArray jsonArray = null;
		try {
			Promotion promotion = promotionDao.getPromotion(promotionId);
			PromotionAndModuleDraftVo promotionAndModuleVo = new PromotionAndModuleDraftVo();
			
			PromotionModuleDraft moduleDraft = new PromotionModuleDraft();
			moduleDraft.setPromotionId(promotionId);
			moduleDraft.setOrderSeq(99999999);   //如果为99999999则按序号排序
			List<PromotionModuleDraft> moduleList = promotionModuleDraftDao.getPromotionModuleDraftList(moduleDraft);
			List<PromotionModuleDraftVo> voList = new ArrayList<>();
			if(moduleList!=null && CollectionUtils.isNotEmpty(moduleList)){
				for (PromotionModuleDraft promotionModuleDraft : moduleList) {
					PromotionModuleDraftVo moduleDraftVo = new PromotionModuleDraftVo();
					BeanUtils.copyProperties(promotionModuleDraft ,moduleDraftVo);
					//根据模块Id查询mongodb中对应的数据
					PromotionModuleContentDraft contentModule = moduleContentDraftRepository.findOne(promotionModuleDraft.getPromoModuleId());
					if(contentModule!=null){
						moduleDraftVo.setPromotionContent(contentModule.getPromotionContent());
					}
					if(promotionModuleDraft.getCreatedDate()!=null){
						moduleDraftVo.setCreatedDate(promotionModuleDraft.getCreatedDate());
					}
					if(promotionModuleDraft.getLastUpdateDate()!=null){
						moduleDraftVo.setLastUpdateDate(promotionModuleDraft.getLastUpdateDate());
					}
					voList.add(moduleDraftVo);
				}
			}
			
			if(promotion!=null){
				promotionAndModuleVo.setPromotionId(promotion.getPromotionId());
				promotionAndModuleVo.setPromotionName(promotion.getPromotionName());
				promotionAndModuleVo.setPromotionStatus(promotion.getPromotionStatus().toString());
				promotionAndModuleVo.setCreateType(promotion.getCreateType());
				promotionAndModuleVo.setDisplaySidebar(promotion.getDisplaySidebar());
				promotionAndModuleVo.setPreviewDisplaySidebar(promotion.getPreviewDisplaySidebar());
				promotionAndModuleVo.setPromotionUrl(promotion.getPromotionUrl());
				promotionAndModuleVo.setCouponIds(promotion.getCouponIds());
				promotionAndModuleVo.setIsUseCoupon(promotion.getIsUseCoupon());
				promotionAndModuleVo.setFaceImgUrl(promotion.getFaceImgUrl());
				promotionAndModuleVo.setStartDate(promotion.getStartDate());
				promotionAndModuleVo.setEndDate(promotion.getEndDate());
				promotionAndModuleVo.setList(voList);
			}
			
			//拼装json字符转
			JsonConfig jsonConfig = new JsonConfig();   //JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法  
			jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
			jsonArray = JSONArray.fromObject(promotionAndModuleVo , jsonConfig); 
			result = jsonArray.toString();
			return result;
		} catch (Exception e) {
			logger.error("根据prmotionId查询所有的模块json数据:{}", e);
		}
		return null;
	}


	/**
	 * 根据prmotionId查询所有的模块json数据
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String getPromotionModule2Json(String promotionId) {
		String result = "";
		JSONArray jsonArray = null;
		try {
			Promotion promotion = promotionDao.getPromotion(promotionId);
			PromotionAndModuleVo promotionAndModuleVo = new PromotionAndModuleVo();
			if((PromotionStatus.ENABLE).equals(promotion.getPromotionStatus())){
				PromotionModule module = new PromotionModule();
				module.setPromotionId(promotionId);
				module.setOrderSeq(99999999);   //如果为99999999则按序号排序
				List<PromotionModule> moduleList = promotionModuleDao.getPromotionModuleList(module);
				List<PromotionModuleVo> voList = new ArrayList<>();
				if(moduleList!=null && CollectionUtils.isNotEmpty(moduleList)){
					for (PromotionModule promotionModule : moduleList) {
						PromotionModuleVo moduleVo = new PromotionModuleVo();
						BeanUtils.copyProperties(promotionModule ,moduleVo);
						//根据模块Id查询mongodb中对应的数据
						PromotionModuleContent contentModule = moduleContentRepository.findOne(promotionModule.getPromoModuleId());
						if(contentModule!=null){
							moduleVo.setPromotionContent(contentModule.getPromotionContent());
						}
						if(promotionModule.getCreatedDate()!=null){
							moduleVo.setCreatedDate(promotionModule.getCreatedDate());
						}
						if(promotionModule.getLastUpdateDate()!=null){
							moduleVo.setLastUpdateDate(promotionModule.getLastUpdateDate());
						}
						voList.add(moduleVo);
					}
				}
				
				if(promotion!=null){
					promotionAndModuleVo.setPromotionId(promotion.getPromotionId());
					promotionAndModuleVo.setPromotionName(promotion.getPromotionName());
					promotionAndModuleVo.setPromotionStatus(promotion.getPromotionStatus().toString());
					promotionAndModuleVo.setCreateType(promotion.getCreateType());
					promotionAndModuleVo.setDisplaySidebar(promotion.getDisplaySidebar());
					promotionAndModuleVo.setPreviewDisplaySidebar(promotion.getPreviewDisplaySidebar());
					promotionAndModuleVo.setPromotionUrl(promotion.getPromotionUrl());
					promotionAndModuleVo.setFaceImgUrl(promotion.getFaceImgUrl());
					promotionAndModuleVo.setCouponIds(promotion.getCouponIds());
					promotionAndModuleVo.setIsUseCoupon(promotion.getIsUseCoupon());
					promotionAndModuleVo.setStartDate(promotion.getStartDate());
					promotionAndModuleVo.setEndDate(promotion.getEndDate());
					promotionAndModuleVo.setList(voList);
				}
				
				//拼装json字符转			
				JsonConfig jsonConfig = new JsonConfig();   //JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法  
				jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
				jsonArray = JSONArray.fromObject(promotionAndModuleVo , jsonConfig); 
				result = jsonArray.toString();
				return result;
			}else{
				throw new BusinessException(BusiErrorCode.PROMOTION_STATUS_NOT_ENABLE, "查询的信息不是有效的信息");
			}
		} catch (Exception e) {
			logger.error("根据prmotionId查询所有的模块json数据:{}", e);
		}
		return null;
	}


	/**
	 * 定时器处理缓存中的活动信息
	 * 在活动启动前一天添加缓存信息
	 * 在活动结束后一天清除缓存
	 * @since 2017年10月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void handlePromotionCache(String endDate) {
		try {
			logger.info("每天凌晨定时刷新缓存_start");
			//活动未开始并且系统时间为活动开始时间前一天
			List<Promotion> notStartlist = promotionDao.getPromotionNotStartList();
			if(notStartlist!=null && CollectionUtils.isNotEmpty(notStartlist)){
				for (Promotion promotion : notStartlist) {
					//添加缓存
					addCacheByPromoId(promotion,null);
				 	}
			}	
			//活动未开始并且系统时间为活动开始时间后一天
			List<Promotion> notEndlist = promotionDao.getPromotionNotEndList(endDate);
			if(notEndlist!=null && CollectionUtils.isNotEmpty(notEndlist)){
				for (Promotion promo : notEndlist) {
					//清除缓存
					deleteCacheByPromoId(promo.getPromotionId());
				}
			}
			logger.info("每天凌晨定时刷新缓存_end");
		} catch (Exception e) {
			logger.error("定时器处理缓存中的活动信息:{}", e);
			throw new SystemException(e.getMessage(),e);
		}
	}





	

	
			 
}