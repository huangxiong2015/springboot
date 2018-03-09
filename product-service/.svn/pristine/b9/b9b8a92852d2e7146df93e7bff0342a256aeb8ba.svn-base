package com.yikuyi.product.category.manager;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.framework.springboot.model.LoginUser;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactory;
import com.ictrade.tools.leadin.LeadInProcesser;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.category.vo.ProductCategoryVo;
import com.yikuyi.common.entity.EhCacheInfo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.product.category.dao.CategoryRepository;
import com.yikuyi.product.category.dao.ProductCategoryParentRepository;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.InvalidDataException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSOperator;

import net.minidev.json.JSONObject;

@Service
public class CategoryManager {
	private static final Logger logger = LoggerFactory.getLogger(CategoryManager.class);
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductCategoryParentRepository categoryParentRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private MongoOperations mongoOptions;

	@Autowired
	private AliyunOSSOperator aliyunOSSOperator;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;

	/**
	 * 导入库存上传文件路径
	 */
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;
	
	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ProductCategoryParent> aliasCategoryOps;

	private static final String ALIAS_CATEGORY_NAMESPACE = "aliasCategoryNamespace";
	
	/**
	 * 更新时间的属性名
	 */
	public static final String UPDATED_TIME_MILLIS_FIELD_NAME = "updatedTimeMillis";
	
	@Value("${mq.topic.ehCacheTopic}")
	private String ehCacheTopic;
	
	@Autowired
	private MsgSender msgSender;
	
	/**
	 * 批量查询类别信息（前台显示）
	 * 只会查出状态为1的分类
	 * @param info
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	public List<ProductCategoryParent> getByCategroyId(List<Long> ids){
		List<ProductCategoryParent> categoryList = null;
		String key = "allCategoryList";
		Cache cache = cacheManager.getCache("allCategoryInfoCache");
		ValueWrapper valueWrapper = null;
		try{
			valueWrapper = cache.get(key);
		}catch(Exception e){
			logger.error("getByCategroyId",e);
		}
		if(valueWrapper == null){
			Sort sort = new Sort(Direction.ASC,"_id");
			categoryList = categoryRepository.findAllCategoryWithParent(new Integer[]{1},sort);
			cache.put(key, categoryList);
		}else{
			categoryList = (List<ProductCategoryParent>)cache.get(key).get();
		} 
		if(ids == null || ids.isEmpty()){
			return categoryList;
		}else{
			return findCategoryById(ids,categoryList);
		}
	}
	
	/**
	 * 根据id查找分类信息
	 * @param ids
	 * @param list
	 * @return
	 * @since 2017年3月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<ProductCategoryParent> findCategoryById(List<Long> ids,List<ProductCategoryParent> list){
		List<ProductCategoryParent> categoryList = new ArrayList<>();
		for(Long id : ids){
			Integer productId = Integer.valueOf(id.toString());
			for(ProductCategoryParent info : list){
				if(productId.equals(info.getId())){
					categoryList.add(info);
				}
			}
		}
		return categoryList;
	}
	
	/**
	 * 查询同义词和类别的map（后台匹配）
	 * 可以查出所有状态的分类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<ProductCategoryAlias,ProductCategoryParent> getAliasCategoryMap(){
		String key = "aliasCategoryMap";
		Cache cache = cacheManager.getCache("aliasMap");
		Map<ProductCategoryAlias,ProductCategoryParent> aliasCategoryMap;
		ValueWrapper valueWrapper = null;
		try{
			valueWrapper = cache.get(key);
		}catch(Exception e){
			logger.error("getAliasCategoryMap -->" , e);
		}
		if(valueWrapper==null){
			aliasCategoryMap = new HashMap<>();
			Sort sort = new Sort(Direction.ASC,"_id");
			List<ProductCategoryParent> categories = categoryRepository.findAllCategoryWithParent(new Integer[]{0,1},sort);
			for(ProductCategoryParent category:categories){
				List<ProductCategoryAlias> alias = category.getCateAlias();
				if(category.getLevel()==null||category.getLevel()<3||alias==null){
					continue;
				}
				for(ProductCategoryAlias alia:alias){
					if(alia.getLevel1()!=null)
						alia.setLevel1(alia.getLevel1().toUpperCase());
					if(alia.getLevel2()!=null)
						alia.setLevel2(alia.getLevel2().toUpperCase());
					aliasCategoryMap.put(alia, category);//建立别名和类别的对应关系
				}
				//放入次小类
				ProductCategoryAlias alia = new ProductCategoryAlias();
				alia.setLevel2(category.getName().toUpperCase());
				aliasCategoryMap.put(alia,category);
				//放入小类和次小类
				alia = new ProductCategoryAlias();
				alia.setLevel1(category.getParent().getName().toUpperCase());
				alia.setLevel2(category.getName().toUpperCase());
				aliasCategoryMap.put(alia,category);
			}
			cache.put(key, aliasCategoryMap);
		}else{
			aliasCategoryMap = (Map<ProductCategoryAlias,ProductCategoryParent>) valueWrapper.get();
		}
		return aliasCategoryMap;
	}
	
	/**
	 * 初始化所有分类数据到redis缓存，key保存的是level1和level2的大写hashcode
	 */
	public void initAliasCategory(){
		Map<String, ProductCategoryParent> aliasCategoryMap = new HashMap<>();
		Sort sort = new Sort(Direction.ASC, "_id");
		List<ProductCategoryParent> categories = categoryRepository.findAllCategoryWithParent(new Integer[] { 0, 1 },sort);
		for (ProductCategoryParent category : categories) {
			List<ProductCategoryAlias> alias = category.getCateAlias();
			if (category.getLevel()<3) {
				continue;
			}
			//全部分类别名转化
			if(alias!=null){
				for (ProductCategoryAlias alia : alias) {
					setCategoryAliasToMap(aliasCategoryMap,alia,alia.getLevel1(),alia.getLevel2(),category);
				}
			}
			//本名末两级转化
			if(category.getParent()!=null)
				setCategoryAliasToMap(aliasCategoryMap,null,category.getParent().getName(),category.getName(),category);
			//最后一级转化
			setCategoryAliasToMap(aliasCategoryMap,null,null,category.getName(),category);
		}
		//清空原来的缓存
		Set<String> keys = aliasCategoryOps.keys(ALIAS_CATEGORY_NAMESPACE);
		if(keys!=null&&keys.size()>0){
			aliasCategoryOps.delete(ALIAS_CATEGORY_NAMESPACE, keys.toArray());
		}
		//重设缓存
		aliasCategoryOps.putAll(ALIAS_CATEGORY_NAMESPACE, aliasCategoryMap);
	}
	
	/**
	 * 根据分类的levle1和levle2查询redis缓存，必须强制大写
	 * @param alias
	 * @return
	 */
	public Map<String,ProductCategoryParent> getCategoryByAliasName(@NotEmpty Collection<String> alias){
		List<ProductCategoryParent> tempList = aliasCategoryOps.multiGet(ALIAS_CATEGORY_NAMESPACE, alias);
		Iterator<String> aliasIterable =  alias.iterator();
		int i = 0;
		Map<String,ProductCategoryParent> resultMap = new HashMap<>();
		while (aliasIterable.hasNext()) {
			//过滤掉没有的数据
			if(tempList.get(i) != null){
				resultMap.put(aliasIterable.next(), tempList.get(i));
			}else{
				aliasIterable.next();//必须调用一次next，否则次数和总数会对应不上
			}
			i++;
		}
		return resultMap;
	}
	
	/**
	 * 将一个别名映射放入Map中
	 * @param aliasCategoryMap 别名映射map
	 * @param alia 别名对象，可为空
	 * @param aliasLevel1 一级别名
	 * @param aliasLevel2 二级别名
	 * @param category 要映射的标准分类
	 * @since 2017年11月7日
	 * @author tongkun@yikuyi.com
	 */
	public void setCategoryAliasToMap(Map<String, ProductCategoryParent> aliasCategoryMap,ProductCategoryAlias alia,String aliasLevel1,String aliasLevel2,ProductCategoryParent category){
		if(alia==null)
			alia = new ProductCategoryAlias();
		alia.setLevel1(StringUtils.isEmpty(aliasLevel1) ? null : aliasLevel1.toUpperCase());
		alia.setLevel2(StringUtils.isEmpty(aliasLevel2) ? null : aliasLevel2.toUpperCase());
		if(aliasLevel2.equals("电阻")){
			System.out.println();
		}
		aliasCategoryMap.put(String.valueOf(alia.hashCode()), category);// 建立别名和类别的对应关系
	}
	
	
	
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return
	 */
	public List<ProductCategory> getChildrenById(Integer parentCateId){	
		List<ProductCategory> categoryList ;
		if(parentCateId == null){
			JSONObject param = new JSONObject();
			param.put("$exists", false);
			categoryList = categoryRepository.find(param);
		}else{
			Integer param = parentCateId;
			categoryList = categoryRepository.find(param);
		}
		return categoryList;
	}
	
	/**
	 * 根据ID查询分类
	 * @param id 类别ID
	 * @return
	 */
	public ProductCategory findById(Integer id){
		ProductCategory productCategory = categoryRepository.findById(id);
		if(StringUtils.isNotEmpty(productCategory.getUpdatedTimeMillis())){
			productCategory.setLastUpdateDate(new Date(Long.valueOf(productCategory.getUpdatedTimeMillis())));
		}
		 return productCategory;
	}
	
	/**
	 * 查询全部分类(前台显示)
	 * 只会查出状态为1的分类
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	public List<ProductCategoryChild> findAll(Integer[] status){
		if(status == null){
			status = new Integer[]{0,1};
			this.clearCategoryCache();
		}
		if(Arrays.asList(status).contains(0)){
			this.clearCategoryCache();
		}
		List<ProductCategoryChild> catChildList = new ArrayList<>();
		Cache cache = cacheManager.getCache("category");
		String key = "categoryInfo";
		ValueWrapper valueWrapper = null;
		try{
			valueWrapper = cache.get(key);
		}catch(Exception e){
			logger.error("findAll -->", e);
		}
		if(valueWrapper==null){
			Sort sort = new Sort(Direction.ASC,"_id");
			List<ProductCategoryParent> catParentList = categoryRepository.findAllCategoryWithParent(status,sort);
			if(catParentList != null && !catParentList.isEmpty()){
				for(ProductCategoryParent parent : catParentList){
					if(parent.getLevel() == 1){
						ProductCategoryChild child = new ProductCategoryChild();
						child.setId(parent.getId());
						child.setName(parent.getName());
						child.setLevel(parent.getLevel());
						ProductCategoryChild childs = handleCategory(catParentList,parent.getId(),child);  
						catChildList.add(childs);
					}
				}
			}
			//有不显示的分类不放入缓存
			if(!Arrays.asList(status).contains(0)){
				cache.put(key, catChildList);
			}
		}else{
			catChildList = (List<ProductCategoryChild>)cache.get(key).get();
		}
		return catChildList;
	}
	
	public List<ProductCategoryParent>  findCategoryByNameAndLevle(Set<String> cateNames,ProductCategoryLevel cateLevel){
		return categoryRepository.findCategoryByNameAndLevle(cateNames, cateLevel.getValue());
	}
	
	/**
	 * 根据名称获取标准分类
	 * <br>同名优先级规则(次小类>小类>大类)
	 * <br>同级别下同名默认第一个
	 * @return
	 */
	public ProductCategory getCateByCateName(String cateName){
		List<ProductCategory> cateList = categoryRepository.findByCateName(cateName);
		if(CollectionUtils.isEmpty(cateList)){
			return null;
		}
		if(cateList.size()==1){
			return cateList.get(0);
		}
		ProductCategory cate = null;
		for(int i=0 ; i<cateList.size() ; i++){
			if(null == cate || cate.getLevel()<cateList.get(i).getLevel()){
				cate = cateList.get(i);	
			}
		}
		return cate;
	}
	/**
	 * 获取catename
	 * @param cateName 默认匹配level2
	 * @return
	 */
	public ProductCategory getProductCategoryByCateName(String cateName){
		ProductCategoryAlias alias = new ProductCategoryAlias();
		alias.setLevel2(cateName.toUpperCase());
		return aliasCategoryOps.get(ALIAS_CATEGORY_NAMESPACE, String.valueOf(alias.hashCode()));
	}
	
	/**
	 * 根据小类，次小类名称获取分类对象
	 * @param cateNameL2 小类名
	 * @param cateNameL3 次小类名
	 * @return 分类对象
	 * @since 2017年11月7日
	 * @author tongkun@yikuyi.com
	 */
	public ProductCategory getCateByCateName(String cateNameL2,String cateNameL3){
		ProductCategoryAlias alia = new ProductCategoryAlias();
		alia.setLevel1(StringUtils.isEmpty(cateNameL2) ? null : cateNameL2.toUpperCase());
		alia.setLevel2(StringUtils.isEmpty(cateNameL3) ? null : cateNameL3.toUpperCase());
		ProductCategory pc = aliasCategoryOps.get(ALIAS_CATEGORY_NAMESPACE, String.valueOf(alia.hashCode()));
		//如果不是本名则返回空
		if(pc!=null&&!pc.getName().equals(cateNameL3))
			pc = null;
		return pc;
	}
	
	/**
	 * 递归处理分类层级信息
	 * @param catParentList
	 * @param parentId
	 * @return
	 * @since 2017年3月4日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductCategoryChild handleCategory(List<ProductCategoryParent> catParentList,int pId,ProductCategoryChild child){
		List<ProductCategoryChild> cateList = new ArrayList<>();
		for (ProductCategoryParent parentInfo : catParentList) {
			ProductCategoryParent parentId = parentInfo.getParent();
			if(parentId != null&&parentId.getId()!=null){
				if(pId == parentId.getId()) {
					ProductCategoryChild cate = new ProductCategoryChild();
					cate.setId(parentInfo.getId());
					cate.setName(parentInfo.getName());
					cate.setLevel(parentInfo.getLevel());
					cate.setStyle(parentInfo.getStyle());
					if (ifChilds(catParentList, parentInfo.getId())) {  
						ProductCategoryChild childs = handleCategory(catParentList, parentInfo.getId(),child); 
						cate.setChildren(childs.getChildren());
					}
					cateList.add(cate);
		        } 
			}
	    } 
		child.setChildren(cateList);
		return child;
	}
	
	private static boolean ifChilds(List<ProductCategoryParent> list,int pId) {  
        boolean flag = false;  
        for (ProductCategoryParent parent : list) {  
            if (parent.getId()==pId) {  
                flag=true;  
                break;  
            }  
        }  
        return flag;  
    }  
	
	public boolean checkNameExists(ProductCategoryVo category){
	
		//次小类校验别名和名称不能重复
		if(category.getLevel() != null && 3 == category.getLevel()){
			String parentName = null;
			if(category.getParent()!=null){
				ProductCategory productCategory = this.findById(category.getParent().getId());
				parentName = productCategory==null?null:productCategory.getName();
			}
			ProductCategory result = getCateByCateName(parentName, category.getName());
			//新增分类别名不存在或存在但是为更新当前数据
			if(result == null || (category.getId() != null && result.getId().equals(category.getId()))){
				return false;
			}
			return true;
		}else{
			//非次小类只校验名称不能重复
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("status").is(1)
			.and("cateName").is(category.getName())
			.and("cateLevel").is(category.getLevel())
			.and("_id").ne(category.getId());
			query.addCriteria(criteria);
			List<ProductCategory> cates = mongoOptions.find(query, ProductCategory.class);
			if(CollectionUtils.isNotEmpty(cates)){
				return true;
			}
			return false;
		}
		
	}
	
	/**
	 * 更新分类
	 * @param category 类别实体
	 * @return 更新后的实体
	 * @throws BusinessException 
	 */
	public ProductCategory update(ProductCategoryVo category,LoginUser userInfo) throws BusinessException{
		if(this.checkNameExists(category)){
			throw new InvalidDataException("类别名称已存在!");
		}
		
		//更新时间
		Update up = Update.update(UPDATED_TIME_MILLIS_FIELD_NAME,Long.toString(new Date().getTime()));
		//操作人
		up.set("operatedUserName", userInfo.getUsername());
		
		//分类图标
		if(StringUtils.isNotEmpty(category.getIcon())){
			up.set("icon", category.getIcon());
		}
		//分类名称
		if(StringUtils.isNotEmpty(category.getName())){
			up.set("cateName", category.getName());
		}
		//扩展 样式
		if(null!=category.getStyle()){
			up.set("style", category.getStyle());
		}
		//状态
		if(null!=category.getStatus()){
			up.set("status", category.getStatus());
		}
		//更新分类
		mongoOptions.updateMulti(query(where("_id").is(category.getId())), up, ProductCategory.class);
		
		//清除分类缓存
		clearCategoryCache();
		
		this.initAliasCategory();
		
		return category;
	}

	public void clearCategoryCache() {
		Cache cache = cacheManager.getCache("category");
		String key = "categoryInfo";
		cache.evict(key);
		
		Cache allCategoryInfoCache = cacheManager.getCache("allCategoryInfoCache");
		key = "allCategoryList";
		allCategoryInfoCache.evict(key);
		
		EhCacheInfo ehCacheInfo = new EhCacheInfo();
		ehCacheInfo.setCacheKey("indexData");
		ehCacheInfo.setCacheValue("indexDataCache");
		msgSender.sendMsg(ehCacheTopic, ehCacheInfo, null);
		
	}
	
	/**
	 * 新增分类
	 */
	public ProductCategoryVo add(ProductCategoryVo category,LoginUser userInfo) throws BusinessException{
		if(this.checkNameExists(category)){
			throw new InvalidDataException("类别名称已存在!");
		}
		category.setOperatedUserName(userInfo.getUsername());
		category.setUpdatedTimeMillis(Long.toString(new Date().getTime()));
		category.setCreatedTimeMillis(Long.toString(new Date().getTime()));
		
		if(null!= category.getParent() && category.getParent().getId() != null){
			ProductCategory productCategory = this.findById(category.getParent().getId());
			ProductCategoryParent parent = new ProductCategoryParent();
			BeanUtils.copyProperties(productCategory ,parent );
			category.setParent(parent);
		}
		
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));  
		query.limit(1);
		List<ProductCategory> list = mongoOptions.find(query, ProductCategory.class);
		if(CollectionUtils.isEmpty(list)){
			throw new SystemException("无法生成分类ID");
		}
		category.setId(list.get(0).getId()+1);
		//别名默认 空集合
		category.setCateAlias(Collections.emptyList());
		if(category.getLevel() != null && 1 == category.getLevel()){
			category.setChildren(Collections.emptyList());
			ProductCategoryChild child = new ProductCategoryChild();
			BeanUtils.copyProperties(category ,child );
			categoryRepository.save(child);
		}else{
			ProductCategoryParent parent = new ProductCategoryParent();
			BeanUtils.copyProperties(category ,parent );
			categoryParentRepository.save(parent);
		}
		
		//清除分类缓存
		clearCategoryCache();
		
		this.initAliasCategory();
		return category;
	}
	
	public List<ProductCategory> getChildrenbyCondition(Integer parentCateId){
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("parent.$id").is(parentCateId);
		query.addCriteria(criteria);
		
		List<ProductCategory> categories =  mongoOptions.find(query, ProductCategory.class);
		//按id排序
	    Collections.sort(categories,(ProductCategory p1, ProductCategory p2) -> p1.getId().compareTo(p2.getId()));
	    categories.stream().forEach(category -> {
	    	if(StringUtils.isNotEmpty(category.getUpdatedTimeMillis())){
	    		category.setLastUpdateDate(new Date(Long.valueOf(category.getUpdatedTimeMillis())));
			}
	    	if(StringUtils.isNotEmpty(category.getCreatedTimeMillis())){
	    		category.setCreatedDate(new Date(Long.valueOf(category.getCreatedTimeMillis())));
			}
	    });
		return categories;
	}
	
	/**
	 * 导出所有分类
	 * 1、查询所有分类
	 * 2、创建输出管道
	 * 3、按格式组装文件内容
	 * 4、输出
	 * @param os 导出用输出流
	 * @since 2017年11月2日
	 * @author tongkun@yikuyi.com
	 */
	public void exportAllCategory(OutputStream os){
		String otherTitle = "其它";
		//1、查询所有分类
		List<ProductCategoryParent> categories = categoryRepository.findAllCategoryWithParent(new Integer[]{1}, new Sort(Direction.ASC,"_id"));
		//2、创建输出管道
		ExportProcesser ep = ExportFactory.getProcesser("xls", os);

		//sheet用
		Set<String> existsVendorName = new HashSet<>();//sheet检测set
		
		//3、按格式组装文件内容
		for(ProductCategoryParent pc:categories){
			int no = 1;//sheet的id，每个分类重置
			//只处理第3级数据
			if(pc.getCateAlias()==null||pc.getLevel().intValue()<3){
				continue;
			}
				
			for(ProductCategoryAlias pca:pc.getCateAlias()){
				//供应商名进行分组，如果没有供应商名则按照title+序号的模式给出
				String vendorName = pca.getVendorName();
				if(vendorName==null){
					vendorName = otherTitle+no;
				}
				//表头
				if(!existsVendorName.contains(vendorName)){
					ep.writeLine(vendorName, new String[]{
							"易库易大类","易库易小类","易库易次小类","一级别名","二级别名"
					});
					existsVendorName.add(vendorName);
					no++;
				}
				String cat1 = "";
				String cat2 = "";
				if(pc.getParent()!=null){
					cat2 = pc.getParent().getName();
					if(pc.getParent().getParent()!=null){
						cat1 = pc.getParent().getParent().getName();
					}
				}
				
				//输出
				ep.writeLine(vendorName, new String[]{
						cat1,
						cat2,
						pc.getName(),
						pca.getLevel1()==null?"":pca.getLevel1(),
						pca.getLevel2()==null?"":pca.getLevel2()
				});
			}
		}
		ep.output();
	}
	
	/**
	 * 导入全部别名
	 * @param fileName
	 * @since 2017年11月3日
	 * @author tongkun@yikuyi.com
	 */
	public List<String> importAllAlias(String fileUrl,String oriName){
		String fileName = fileDownload(fileUrl,oriName);//导入文件上传到本地
		File file = new File(leadMaterialFilePath + File.separator + fileName);
		List<String> result = readAllAlias(file);//执行文件导入
		logger.info("分类更新完成，开始更新缓存");
		initAliasCategory();//批量更新分类别名缓存
		logger.info("分类缓存更新完毕");
		return result;
	}
	
	/**
	 * 缓存文件数据导入
	 * @param fileName 文件名
	 * @return
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	public List<String> readAllAlias(File file){
		List<String> errorLog = new ArrayList<>();
		//获取供应商信息
		Map<String,String> vendorMap = new CaseInsensitiveMap<>(partyClientBuilder.supplierClient().getSupplierNames());
		
		LeadInProcesser process = LeadInFactory.createProcess(file);
		String[] line;//文件读取出来的行
		Map<Integer,List<ProductCategoryAlias>> aliasMap = new HashMap<>();//文件读取出来的,分类id:分类别名集合
		Map<Integer,String> existsSet = new HashMap<>();
		int lineno = 0;//当前行号
		String lastSheetName = null;//前一条数据的sheet名
		String vendorId = null;//供应商id
		boolean sheetError = false;//如果为true表示整个sheet不再读取
		while((line=process.getNext())!=null){
			//sheet没切换则行号增加
			if(process.getSheetName().equals(lastSheetName)){
				//sheet有问题则跳过
				if(sheetError)
					continue;
				//没有问题则行号增加
				else
					lineno++;
			}
			//sheet切换了则重新记行号
			else{
				lineno = 1;
				lastSheetName = process.getSheetName();
				vendorId = vendorMap.get(lastSheetName.toUpperCase().trim());
				if(lastSheetName.indexOf("其它")<0&&vendorId==null){
					errorLog.add("sheet:"+lastSheetName+" 不是正确的供应商名称");
					sheetError = true;
					continue;
				}else{
					sheetError = false;
				}
			}
			//如果是首行或空行则不处理
			if(line.length<4||"易库易大类".equals(line[0])||"YKY大类".equals(line[0])||"".equals(line[0]))
				continue;
			ProductCategory pc = getCateByCateName(line[1],line[2]);
			//如果没有找到这个分类
			if(pc==null){
				errorLog.add("sheet:"+lastSheetName+" 第"+lineno+"行分类："+line[2]+" 不存在");
				continue;
			}
			List<ProductCategoryAlias> aliasList = aliasMap.get(pc.getId());//别名集合
			if(aliasList==null){
				aliasList = new ArrayList<>();
				aliasMap.put(pc.getId(), aliasList);
			}
			//创建别名
			ProductCategoryAlias alias = new ProductCategoryAlias();
			alias.setVendorId(vendorId);
			alias.setVendorName(lastSheetName);
			if(!StringUtils.isEmpty(line[3]))
				alias.setLevel1(line[3].trim());
			if(line.length>4&&!StringUtils.isEmpty(line[4]))
				alias.setLevel2(line[4].trim());
			//判断是否重复
			String hint = existsSet.get(alias.hashCode());
			if(hint!=null){
				errorLog.add("sheet:"+lastSheetName+" 第"+lineno+"行分类："+line[2]+" 与 "+hint+"重复");
				continue;
			}else{
				existsSet.put(alias.hashCode(),"sheet:"+lastSheetName+" 第"+lineno+"行");
				aliasList.add(alias);
			}
		}
		process.close();
		updateCateAliasBatch(aliasMap);//批量更新分类别名
		file.delete();
		return errorLog;
	}
	
	/**
	 * 批量更新分类
	 * @param aliasMap 分类
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	public void updateCateAliasBatch(Map<Integer,List<ProductCategoryAlias>> aliasMap){
		for(Integer id:aliasMap.keySet()){
			mongoOptions.updateFirst(query(where("_id").is(id)), Update.update("cateAlias", aliasMap.get(id)),ProductCategory.class);
		}
	}
	/**
	 * 获取默认分类
	 * @return
	 */
	public List<ProductCategoryParent> getDefaultCategories(){
		List<ProductCategoryParent> categoryParents = categoryRepository.findByIds(Arrays.asList(new Long[]{(long) -1,(long)-2,(long)-3}));
		return categoryParents;
	}
	
	/**
	 * 文件下载到服务器上
	 * @param fileUrl oss文件路径
	 * @param oriName 文件上传前原名
	 * @return 保存在本地的文件名称
	 * @since 2017年11月3日
	 * @author tongkun@yikuyi.com
	 */
	public String fileDownload(String fileUrl,String oriName){
		String fileName = String.valueOf(new Date().getTime());
		if(StringUtils.isNotBlank(oriName)){
			fileName = fileName + oriName.substring(oriName.lastIndexOf('.'));
		}else{
			fileName = fileName + fileUrl.substring(fileUrl.lastIndexOf('.'));
		}
		
		// 应用路径
		int length = 0;
		byte[] bytes = new byte[1024];
		String abPath = leadMaterialFilePath + File.separator + fileName;
		try (FileOutputStream os = new FileOutputStream(abPath);
				InputStream is = this.aliyunOSSOperator.getObject(fileUrl);
			) {
			while ((length = is.read(bytes)) > 0) {
				os.write(bytes, 0, length);
			}
		} catch (Exception e) {
			logger.error("文件下载失败：url是："+fileUrl+" 错误原因是："+e.getMessage(),e);
			throw new SystemException(e.getMessage(), e);
		}
		// 异常情况
		return fileName;
	}
	
	/**
	 * 根据关键字模糊匹配查询分类
	 * @param keyword 关键字
	 * @param fuzzySearchType 匹配类型
	 * @return
	 */
	public List<ProductCategory> fuzzyMatch(String keyword, FuzzySearchType fuzzySearchType){
		if(StringUtils.isEmpty(keyword)){
			return Collections.emptyList();
		}
		//特殊字符
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if(StringUtils.isNotEmpty(keyword)){
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "\\" + key);
	   		    }
			}
   		}
		//拼装名称模糊查询条件
		JSONObject paramJson = new JSONObject();
		if(StringUtils.isNotBlank(keyword)){
			JSONObject regex = new JSONObject();
			if(FuzzySearchType.LEFT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + keyword + "$");
			}else if(FuzzySearchType.RIGHT_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^" + keyword + ".*$");
			}else if(FuzzySearchType.FULL_FUZZY.equals(fuzzySearchType)){
				regex.put("$regex", "^.*" + keyword + ".*$");
			}else if(FuzzySearchType.FULL_MATCH.equals(fuzzySearchType)){
				regex.put("$regex", "^" + keyword + "$");
			}else{
				regex.put("$regex", "^.*" + keyword + ".*$");
			}
			regex.put("$options", "i");
			paramJson.put("cateName", regex);	
		}
		
		List<ProductCategory> categories = categoryRepository.getCategories(paramJson);
		return categories;
	}
}
