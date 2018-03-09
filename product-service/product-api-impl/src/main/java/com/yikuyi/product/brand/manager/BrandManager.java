package com.yikuyi.product.brand.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.brand.model.ProductBrandAlias;
import com.yikuyi.product.brand.dao.BrandRepository;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.model.ProductStand;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSAccount;

@Service
public class BrandManager {
	
	private static final Logger logger = LoggerFactory.getLogger(BrandManager.class);
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopicName;
	
	@Autowired
	private MsgSender msgSender;
	
	@Value("${dns-prefetchs}")
	private String dnsPrefetchs;
	
	// 娉ㄥ叆HashOperations瀵硅薄
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ProductBrand> aliasBrandOps;
	
	public static final String ALIAS_BRAND_NAMESPACE = "aliasBrandNamespace";
	
	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	private  AliyunOSSAccount aliyunOSSAccount;
	
	/**
	 * 缓存所有的品牌
	 * @return
	 * @since 2017年5月25日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String cacheBrands(){
		Cache cache = cacheManager.getCache("allBrandsInfoCache");
		String key = "brandInfoList";
		//查询所有的品牌
		Sort sort = new Sort(Direction.ASC,"brandName");
		List<ProductBrand> brandList = brandRepository.findAll(sort);
		//把所有的品牌放入缓存中
		cache.put(key, brandList);
		return "SUCCESS";
	}
	
	/**
	 * 查询同义词和品牌的map
	 * @since 2016年12月
	 * @author tongkun@yikuyi.com
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,ProductBrand> getAliasBrandMap(){
		String key = "aliasBrandMap";
		Cache cache = cacheManager.getCache("aliasMap");
		ValueWrapper valueWrapper = cache.get(key);
		Map<String,ProductBrand> aliasBrandMap;
		if(valueWrapper==null){
			aliasBrandMap = new HashMap<>();
			List<ProductBrand> brands = brandRepository.findAll();
			for(ProductBrand brand:brands){
				List<ProductBrandAlias> alias = brand.getVendorAlias();
				if(alias==null){
					continue;
				}
				for(ProductBrandAlias alia:alias){
					aliasBrandMap.put(getAliasKey(alia.getVendorId(), alia.getName()), brand);//建立别名和类别的对应关系
				}
				aliasBrandMap.put(getAliasKey(null, brand.getBrandName()), brand);//别名不一定包含标准名称，增加标准名称
			}
			cache.put(key, aliasBrandMap);
		}else{
			aliasBrandMap = (Map<String,ProductBrand>) valueWrapper.get();
		}
		return aliasBrandMap;
	}
	
	/**
	 * 清空原来的缓存，并将数据库中所有的品牌重新缓存
	 * @return
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	public Map<String,ProductBrand> initAliasBrand(){
		Map<String,ProductBrand> aliasBrandMap = new HashMap<>();
		List<ProductBrand> brands = brandRepository.findAll();
		for (ProductBrand brand : brands) {
			List<ProductBrandAlias> alias = brand.getVendorAlias();
			if (alias == null) {
				continue;
			}
			for (ProductBrandAlias alia : alias) {
				aliasBrandMap.put(getAliasKey(alia.getVendorId(),alia.getName()), brand);//将别名放入缓存
			}
			aliasBrandMap.put(getAliasKey(null,brand.getBrandName()), brand);//将正式名称放入缓存
		}
		
		//删除原来的缓存
		Set<String> keys = aliasBrandOps.keys(ALIAS_BRAND_NAMESPACE);
		if(keys!=null&&keys.size()>0){
			aliasBrandOps.delete(ALIAS_BRAND_NAMESPACE, keys.toArray());
		}
		//重设缓存
		aliasBrandOps.putAll(ALIAS_BRAND_NAMESPACE, aliasBrandMap);
		return aliasBrandMap;
	}
	
	/**
	 * 用key来获取制造商
	 * @param key 根据的Key
	 * @return
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	public ProductBrand getBrandByKey(String key){
		return aliasBrandOps.get(ALIAS_BRAND_NAMESPACE, key);
	}
	
	/**
	 * 用别名来获取制造商
	 * @param vendorId 专用的供应商id，如果不是专用别名则传空
	 * @param name 制造商名称
	 * @return
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	public ProductBrand getBrandByAliasName(String vendorId,String name){
		return getBrandByKey(getAliasKey(vendorId, name));
	}
	
	/**
	 * 根据缓存key集合批量获取缓存数据
	 * @param keys 别名key集合
	 * @return 获取的品牌Map
	 * @since 2017年10月
	 * @author jik.shu@yikuyi.com
	 */
	public Map<String,ProductBrand> getBrandByAliasName(Collection<String> keys){
		Map<String, ProductBrand> resultMap = new HashMap<>();
		List<ProductBrand> tempList = aliasBrandOps.multiGet(ALIAS_BRAND_NAMESPACE, keys);
		Iterator<String> brandsIterable = keys.iterator();
		int i = 0;
		while (brandsIterable.hasNext()) {
			// 杩囨护鎺夋病鏈夌殑鏁版嵁
			if (tempList.get(i) != null) {
				resultMap.put(brandsIterable.next(), tempList.get(i));
			} else {
				brandsIterable.next();
			}
			i++;
		}
		return resultMap;
	}
	
	/**
	 * 计算key
	 * @param alias
	 * @return
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	public String getAliasKey(String vendorId,String aliasName){
		StringBuffer key = new StringBuffer();
		if(StringUtils.isNotEmpty(vendorId))
			key.append(vendorId.toUpperCase()).append("-");
		key.append(aliasName.toUpperCase());
		return key.toString();
	}
	
	/**
	 * 查询制造商列表，并且为按名称排序的结果
	 * @return
	 * @since 2017年3月28日
	 * @author zr.wanghong
	 */
	public List<ProductBrand> findAllByCondition(){
		Sort sort = new Sort(Direction.ASC,"brandName");
		List<ProductBrand> brandList = brandRepository.findAllByCondition(sort);
		
		//1.过滤属性非空
		brandList = brandList.stream().filter( productbrand -> {
			String brandName = productbrand.getBrandName();
			String logo = productbrand.getLogo();
			String desc = productbrand.getDesc();
			
			if(StringUtils.isNotEmpty(brandName) && StringUtils.isNotEmpty(logo) 
					&& StringUtils.isNotEmpty(desc)){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		//2.根据Collections.sort重载方法来实现按品牌名称的每一个字母排序
	    Collections.sort(brandList,(ProductBrand b1, ProductBrand b2) -> {  
	        	String b1Name = StringUtils.isNotEmpty(b1.getBrandName())?b1.getBrandName().toUpperCase():b1.getBrandName();
	        	String b2Name = StringUtils.isNotEmpty(b2.getBrandName())?b2.getBrandName().toUpperCase():b2.getBrandName();
	            return b1Name.compareTo(b2Name);  
	        });
	    
	    //3.将品牌的logo地址拼接为阿里云的地址
	    brandList.stream().forEach( brand ->{
			handleImageUrl(brand);
		});
		return brandList;
	}
	
	/**
	 * 查询制造商列表，并且为按名称排序的结果
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBrand> findAll(){
		String key = "brandInfoList";
		Cache cache = cacheManager.getCache("allBrandsInfoCache");
		ValueWrapper valueWrapper = cache.get(key);
		List<ProductBrand> brandList = new ArrayList<>();
		if(valueWrapper == null){
			Sort sort = new Sort(Direction.ASC,"brandName");
			brandList = brandRepository.findAll(sort);
			cache.put(key, brandList);
		}else{
			brandList = (List<ProductBrand>)valueWrapper.get();
		}
		for(ProductBrand brand : brandList){
			handleImageUrl(brand);
		}
		return brandList;
	}
	
	/**
	 * 根据制造商名称或别名查询制造商
	 * @param brandName 输入的品牌名称或别名的关键字
	 * @param isSupportAlias 是否支持别名搜索（Y/N）
	 * @param fuzzySearchType 模糊搜索类型 
	 * @return
	 */
	public List<ProductBrand> findByAilas(String brandName,String isSupportAlias, FuzzySearchType fuzzySearchType){
		Assert.notNull(brandName);
		//特殊字符
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if(StringUtils.isNotEmpty(brandName)){
				if (brandName.contains(key)) {
					brandName = brandName.replace(key, "\\" + key);
	   		    }
			}
   		}
		//拼装名称模糊查询条件
		JSONArray params = new JSONArray();
		if(StringUtils.isNotBlank(brandName)){
			JSONObject paramJson = new JSONObject();
			JSONObject regex = new JSONObject();
			if(FuzzySearchType.LEFT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + brandName + "$");
			}else if(FuzzySearchType.RIGHT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^" + brandName + ".*$");
			}else if(FuzzySearchType.FULL_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + brandName + ".*$");
			}else if(FuzzySearchType.FULL_MATCH.equals(fuzzySearchType)){
				regex.put("$regex", "^" + brandName + "$");
			}else{
				regex.put("$regex", "^.*" + brandName + ".*$");
			}
			regex.put("$options", "i");
			paramJson.put("brandName", regex);	
			params.add(paramJson);
		}
		
		//拼装别名模糊查询条件
		if(StringUtils.isNotBlank(brandName) && "Y".equalsIgnoreCase(isSupportAlias)){
			JSONObject paramJson = new JSONObject();
			JSONObject regex = new JSONObject();
			if(FuzzySearchType.LEFT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + brandName + "$");
			}else if(FuzzySearchType.RIGHT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^" + brandName + ".*$");
			}else if(FuzzySearchType.FULL_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + brandName + ".*$");
			}else if(FuzzySearchType.FULL_MATCH.equals(fuzzySearchType)){
				regex.put("$regex", "^" + brandName + "$");
			}else{
				regex.put("$regex", "^.*" + brandName + ".*$");
			}
			regex.put("$options", "i");
			paramJson.put("brandAlias", regex);	
			params.add(paramJson);
		}
		JSONObject orConditon = new JSONObject();
		orConditon.put("$or", params);
		List<ProductBrand> brandList = brandRepository.getBrandList(orConditon);
	
		List<ProductBrand> result = new ArrayList<>();
		result.addAll(brandList);
		distinctBrandList(result);
		
		//2.根据Collections.sort重载方法来实现按品牌名称的每一个字母排序
	    Collections.sort(result,(ProductBrand b1, ProductBrand b2) -> {  
	        	String b1Name = StringUtils.isNotEmpty(b1.getBrandName())?b1.getBrandName().toUpperCase():b1.getBrandName();
	        	String b2Name = StringUtils.isNotEmpty(b2.getBrandName())?b2.getBrandName().toUpperCase():b2.getBrandName();
	            return b1Name.compareTo(b2Name);  
	        });
		return result;
	}
	
	/**
	 * 根据ID查询制造商详情信息
	 * @param id 品牌ID
	 * @return
	 * @since 2017年4月19日
	 * @author zr.wanghong
	 */
	public ProductBrand findById(Integer id){
		ProductBrand brand = brandRepository.findById(id);
		if(brand != null){
			this.handleImageUrl(brand);
		}
		return brand;
	}
	
	/**
	 * 分页查询品牌
	 * @param brandName
	 * @param creator
	 * @param page
	 * @param size
	 * @param startDate
	 * @param endDate
	 * @return
	 * @since 2017年3月20日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public PageInfo<ProductBrand> getBrandList(String brandName,String creator,int page,int size,String startDate,String endDate){
		//分页
		int queryPage = 0;
		if(page > 0){
			queryPage = page-1;
		}
		Sort sort = new Sort(Direction.DESC,"createdDate");
		PageRequest pageable = new PageRequest(queryPage,size,sort);
		//特殊字符
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if(StringUtils.isNotEmpty(brandName)){
				if (brandName.contains(key)) {
					brandName = brandName.replace(key, "\\" + key);
	   		    }
			}
			if(StringUtils.isNotEmpty(creator)){
				if (creator.contains(key)) {
					creator = creator.replace(key, "\\" + key);
				}
			}
   		}
		//拼装查询条件
		JSONObject orConditon = new JSONObject();
		JSONArray params = new JSONArray();
		JSONObject paramJson = new JSONObject();
		JSONObject paramJsonAlias = new JSONObject();
		if(StringUtils.isNotBlank(brandName)){
			//Pattern patternBrandName = Pattern.compile("^.*" + brandName + ".*$", Pattern.CASE_INSENSITIVE);
			JSONObject regex = new JSONObject();
			regex.put("$regex", "^.*" + brandName + ".*$");
			regex.put("$options", "i");
			paramJson.put("brandName", regex);	
			
			
			JSONObject regexAlias = new JSONObject();
			regexAlias.put("$regex", "^.*" + brandName + ".*$");
			regexAlias.put("$options", "i");
			paramJsonAlias.put("brandAlias", regexAlias);	
			
			
		}
		//创建人
		if(StringUtils.isNotBlank(creator)){
			Pattern patternCreator = Pattern.compile("^.*" + creator + ".*$", Pattern.CASE_INSENSITIVE);
			paramJson.put("creator", patternCreator);	
			paramJsonAlias.put("creator", patternCreator);	
		}
		//创建时间-开始时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Document greatAndLessThan = new Document();
		if(StringUtils.isNotBlank(startDate)){				
			try{
				greatAndLessThan.put("$gte", df.parse(startDate));
			}catch(ParseException e){
				logger.error("日期格式不正确", e);
			}				
		}
		//创建时间-结束时间
		if(StringUtils.isNotBlank(endDate)){						
			try {
				Date d = df.parse(endDate);
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(d); 
			    calendar.add(Calendar.DATE,1);
				greatAndLessThan.put("$lte", calendar.getTime());
			} catch (ParseException e) {
				logger.error("日期格式不正确，", e);					
			}
		}		
		//创建时间
		if(!greatAndLessThan.isEmpty()){
			paramJson.put("createdDate", greatAndLessThan);				
			paramJsonAlias.put("createdDate", greatAndLessThan);				
		}
		params.add(paramJson);
		params.add(paramJsonAlias);
		orConditon.put("$or", params);
		
		Page<ProductBrand> brandPage = brandRepository.getBrandList(orConditon,pageable);
		List<ProductBrand> brandList = brandPage.getContent() !=null ? new ArrayList<>(brandPage.getContent()) : new ArrayList<>();
		
		//去重
		distinctBrandList(brandList);
		
		brandList.stream().forEach( brand ->{
			handleImageUrl(brand);
			
		});
		PageInfo<ProductBrand> pageInfo = new PageInfo<>(brandList);
		Long listCount = brandPage.getTotalElements();
		pageInfo.setTotal(listCount);
		pageInfo.setPageSize(size);
		pageInfo.setPageNum(page);
		return pageInfo;
	}

	/**
	 * 去重
	 * @param brandList
	 */
	private void distinctBrandList(List<ProductBrand> brandList) {
		Map<Integer, ProductBrand> map = new HashMap<>();
		brandList.stream().forEach(brand -> {
			map.put(brand.getId(), brand);
		});
		brandList.clear();
		brandList.addAll(map.values());
	}
	
	/**
	 * 拼接图片地址为阿里云地址
	 */
	private void handleImageUrl(ProductBrand brand){
		String[] imageUrl = dnsPrefetchs.split(",");
		String logoUrl = brand.getLogo();
		
		if(StringUtils.isNotEmpty(logoUrl) && logoUrl.startsWith("/manufacturer")){
			int num = 0;
			for(int k = 0;k < logoUrl.length(); k++){
				num += logoUrl.charAt(k);
			}
			num = num % 2;
			if(num == 0){
				logoUrl = imageUrl[0]+logoUrl;
			}else{
				logoUrl = imageUrl[1]+logoUrl;
			}
			brand.setLogo(logoUrl);
		}

	}
	
	/**
	 * 获取制造商Id的最大值
	 * @return
	 * @since 2017年3月24日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public Integer getMaxBrandId(){
		//获取品牌Id最大值
		Sort sort = new Sort(Direction.DESC,"_id");
		PageRequest pageable = new PageRequest(0,1,sort);
		Page<ProductBrand> brandList = brandRepository.getBrandList(new JSONObject(),pageable);
		return brandList.getContent().get(0).getId() + 1;
	}
	
	/**
	 * 新增制造商
	 * @param info
	 * @return
	 * @since 2017年3月20日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Audit(action = "Brand Modifyqqq;;;'#id'qqq;;;'#info.creator'新增", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public ProductBrand saveBrand(@com.framework.springboot.audit.Param("id") Integer id,
			@com.framework.springboot.audit.Param("info") ProductBrand info) throws BusinessException{
		List<ProductBrandAlias> brandAliasList = info.getVendorAlias();
		List<ProductBrandAlias> updateAliasList = new ArrayList<>();
		List<String> updateAliasStringList = new ArrayList<>();
		if(brandAliasList == null || brandAliasList.isEmpty()){
			brandAliasList = new ArrayList<>();
		}
		Set<String> existsAlias = new HashSet<>();//已经保存的制造商
		//判断名称是否重复
		String hashKey = getAliasKey(null, info.getBrandName());
		//不重复的则加入
		ProductBrand pb = getBrandByKey(hashKey);
		if(pb==null||pb.getId().equals(id)){
			existsAlias.add(hashKey);
		}else{
			throw new BusinessException(BusiErrorCode.BRANDNAME_EXIST, "制造商名称已存在，请重新输入");
		}
		//制造商别名去重
		for(ProductBrandAlias alia:brandAliasList){
			//去掉空别名
			if(StringUtils.isEmpty(alia.getName()))
				continue;
			hashKey = getAliasKey(alia.getVendorId(), alia.getName());
			//去掉本页重复
			if(existsAlias.contains(hashKey))
				continue;
			//不重复的则加入
			pb = getBrandByKey(hashKey);
			if(pb==null||pb.getId().equals(id)){
				existsAlias.add(hashKey);
				updateAliasList.add(alia);
				updateAliasStringList.add(alia.getName());
			}else{
				throw new BusinessException(BusiErrorCode.BRANDALIAS_EXIST, "制造商别名已存在，请重新输入");
			}
		}
		
		info.setId(id);
		info.setBrandAlias(updateAliasStringList);
		info.setVendorAlias(updateAliasList);
		brandRepository.save(info);
		//刷新缓存
		cacheBrands();
		initAliasBrand();
		return info;
	}
	
	/**
	 * 修改制造商
	 * @param id
	 * @param info
	 * @return
	 * @since 2017年3月22日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Audit(action = "Brand Modifyqqq;;;'#id'qqq;;;'#info.lastUpdateUser'编辑", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public ProductBrand updateBrand(@com.framework.springboot.audit.Param("id") Integer id,
			@com.framework.springboot.audit.Param("info") ProductBrand info) throws BusinessException{
		List<ProductBrandAlias> brandAliasList = info.getVendorAlias();
		List<ProductBrandAlias> updateAliasList = new ArrayList<>();
		List<String> updateAliasStringList = new ArrayList<>();
		if(brandAliasList == null || brandAliasList.isEmpty()){
			brandAliasList = new ArrayList<>();
		}
		Set<String> existsAlias = new HashSet<>();//已经保存的制造商
		//判断名称是否重复
		String hashKey = getAliasKey(null, info.getBrandName());
		//不重复的则加入
		ProductBrand pb = getBrandByKey(hashKey);
		if(pb==null||pb.getId().equals(id)){
			existsAlias.add(hashKey);
		}else{
			throw new BusinessException(BusiErrorCode.BRANDNAME_EXIST, "制造商名称已存在，请重新输入");
		}
		//制造商别名去重
		for(ProductBrandAlias alia:brandAliasList){
			//去掉空别名
			if(StringUtils.isEmpty(alia.getName()))
				continue;
			hashKey = getAliasKey(alia.getVendorId(), alia.getName());
			//去掉本页重复
			if(existsAlias.contains(hashKey))
				continue;
			//不重复的则加入
			pb = getBrandByKey(hashKey);
			if(pb==null||pb.getId().equals(id)){
				existsAlias.add(hashKey);
				updateAliasList.add(alia);
				updateAliasStringList.add(alia.getName());
			}else{
				throw new BusinessException(BusiErrorCode.BRANDALIAS_EXIST, "制造商别名已存在，请重新输入");
			}
		}
		//执行mongo更新
		mongoOperations.updateFirst(new Query(new Criteria("_id").is(id)),
				new Update().set("brandName", info.getBrandName()).set("logo", info.getLogo()).set("desc", info.getDesc()).set("brandAlias", updateAliasStringList).set("vendorAlias",updateAliasList).set("lastUpdateUser", info.getLastUpdateUser()).set("lastUpdateDate", info.getLastUpdateDate()), 
				ProductBrand.class);
		//更新制造商
		ProductStand standInfo = new ProductStand();
		standInfo.setManufacturerId(id);
		msgSender.sendMsg(createProductTopicName, standInfo, null);
		info.setId(id);
		info.setBrandAlias(updateAliasStringList);
		info.setVendorAlias(updateAliasList);
		Cache cache = cacheManager.getCache("aliasMap");
		String key = "aliasBrandMap";
		cache.evict(key);
		//刷新缓存
		cacheBrands();
		initAliasBrand();
		return info;
	}
	
	/**
	 * 删除制造商
	 * @param id
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void deleteBrand(Integer id){
		brandRepository.deleteById(id);
	}

	/**
	 * 批量验证品牌是否存在
	 * @param brandNameList
	 * @return
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public Map<String,ProductBrand> existBrandNameList(List<String> brandNameList) {
		Map<String,ProductBrand> map = new HashMap<>();
		try {
			if(CollectionUtils.isEmpty(brandNameList)){
					throw new BusinessException(BusiErrorCode.BRANDNAME_LIST_NULL,"品牌信息不存在");
			}
			//将集合中的小写全部转化为大写
			Collection<String> keysList = new ArrayList<>();
			Iterator<String> it = brandNameList.iterator();
			while(it.hasNext()){
				keysList.add(String.valueOf(it.next()).toUpperCase());
			}
			map = getBrandByAliasName(keysList);
			return map;
		} catch (Exception e) {
			logger.error("批量验证品牌是否存在:{}", e);
		}
		return null;

	}
	
	/**
	 * 根据id批量查询品牌
	 * @param ids
	 * @return
	 * @since 2017年12月12日
	 * @author tb.lijing@yikuyi.com
	 */
	public Map<Integer,ProductBrand> findByIds(List<Integer> ids){
		Map<Integer,ProductBrand> map = new HashMap<>();
		List<ProductBrand> result = mongoOperations.find(new Query(new Criteria("_id").in(ids)), ProductBrand.class);
		for(ProductBrand productBrand : result){
			map.put(productBrand.getId(), productBrand);
		}
		return map;
	}
}
