package com.yikuyi.product.category.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import com.framework.springboot.model.LoginUser;
import com.ictrade.tools.leadin.LeadInFactory;
import com.ictrade.tools.leadin.LeadInProcesser;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.category.vo.ProductCategoryVo;
import com.yikuyi.product.category.dao.CategoryRepository;
import com.ykyframework.exception.BusinessException;

/**
 * CategoryManager测试类
 * @author zr.wanghong
 * @see com.yikuyi.product.category.manager.CategoryManager
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryManagerTest{

	@Autowired
	private CategoryManager categoryManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 
	 * 批量查询类别
	 * @since 2016年12月23日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	public void testGetByCategroyId(){
		//1.测试一级分类
		List<ProductCategory> categories = categoryManager.getChildrenById(null);
		List<Long> ids = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(categories)){
			for(int i=0;i<categories.size() && i< 6; i++){
				ids.add(categories.get(i).getId().longValue());				
			}
			
			//测试批量父类查询
			List<ProductCategoryParent> allCateList = categoryManager.getByCategroyId(ids);			
		}		
	}
	
	/**
	 * 根据id查询类别
	 * 
	 * @since 2016年12月23日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	public void testFindById(){
		List<ProductCategory> categories = categoryManager.getChildrenById(null);
		if(CollectionUtils.isNotEmpty(categories)){
			ProductCategory pc = categoryManager.findById(categories.get(0).getId());
			if(pc !=null){				
				Assert.assertNotNull(pc.getName());	
				Assert.assertNotNull(pc.getLevel());			
			}
		}
	}
	
	/**
	 * 测试获取分类别名缓存（旧）
	 * @since 2017年11月21日
	 * @author ????
	 */
	@Test
	public void testGetAliasCategoryMapOld(){
		Map<ProductCategoryAlias, ProductCategoryParent> alias = categoryManager.getAliasCategoryMap();
		ProductCategoryAlias alia = new ProductCategoryAlias();
		alia.setLevel1("未分类");
		alia.setLevel2("未分类");
		ProductCategoryParent pc = alias.get(alia);
		Assert.assertEquals(new Integer(-3), pc.getId());
	}
	
	/**
	 * 测试初始化分类缓存
	 * @since 2017年11月21日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testInitAliasCategory(){
		//创造测试数据
		ProductCategory pc = new ProductCategory();
		pc.setId(0);
		pc.setName("testname");
		pc.setLevel(3);
		pc.setStatus(1);
		categoryRepository.save(pc);
		
		//初始化
		categoryManager.initAliasCategory();
		
		//验证（缓存方法1）
		ProductCategory result = categoryManager.getCateByCateName(null, "testname");
		Assert.assertNotNull(result);
		Assert.assertEquals("testname",result.getName());
		
		//验证（缓存方法2）
		List<String> list = new ArrayList<>();
		ProductCategoryAlias alia = new ProductCategoryAlias();
		alia.setLevel1(null);
		alia.setLevel2("testname".toUpperCase());
		list.add(String.valueOf(alia.hashCode()));
		Map<String, ProductCategoryParent> mapResult = categoryManager.getCategoryByAliasName(list);
		Assert.assertNotNull(mapResult);
		ProductCategoryParent pcp = mapResult.get(String.valueOf(alia.hashCode()));
		Assert.assertEquals("testname",pcp.getName());
		
		//删除测试数据
		categoryRepository.delete(pc);
	}
	
	/**
	 * 测试查询全部分类
	 * 
	 * @since 2017年2月24日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindAll(){
		Cache cache = cacheManager.getCache("category");
		String key = "categoryInfo";
		cache.evict(key);
		List<ProductCategoryChild> result = categoryManager.findAll(new Integer[]{0,1});
		Assert.assertNotNull(result);
	}
	
	/**
	 * 测试根据名称获取标准分类
	 * @since 2017年11月22日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testGetCateByCateName(){
		//创造测试数据
		ProductCategory pc = new ProductCategory();
		pc.setId(0);
		pc.setName("testname");
		pc.setLevel(3);
		pc.setStatus(1);
		categoryRepository.save(pc);
		
		//验证
		ProductCategory result = categoryManager.getCateByCateName("testname");
		Assert.assertNotNull(result);
		Assert.assertEquals("testname",result.getName());
		
		
		//删除测试数据
		categoryRepository.delete(pc);
	}
	
	/**
	 * 测试新增、更新
	 * @since 2017年11月22日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testAddAndUpdate(){
		//创造测试数据
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		ProductCategoryVo pc = new ProductCategoryVo();
		pc.setName("testname1");
		pc.setLevel(3);
		pc.setStatus(1);
		
		//清空测试数据
		ProductCategory tp = categoryManager.getCateByCateName("testname1");
		if(tp!=null)
			categoryRepository.delete(tp);
		
		try {	
			pc = categoryManager.add(pc, loginUser);
			pc.setName("testname2");
			categoryManager.update(pc, loginUser);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		//验证
		ProductCategory result = categoryRepository.findById(pc.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals("testname2",result.getName());
		

		//删除测试数据
		categoryRepository.delete(pc);
	}
	
	/**
	 * 测试导出全部分类
	 * @since 2017年11月2日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testExportAllCategory(){
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		categoryManager.exportAllCategory(out);
		LeadInProcesser pro = LeadInFactory.createProcess(new ByteArrayInputStream(out.toByteArray()),"xls",Charset.forName("UTF-8"));
		String[] line = pro.getNext();
		Assert.assertEquals(5, line.length);
		pro.close();
	}
	
	/**
	 * 测试导入分类别名
	 * @since 2017年11月13日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testImportCategoryAlias(){
		File oriFile = new File(CategoryManagerTest.class.getClassLoader().getResource("com/yikuyi/product/category/test.xlsx").getFile());
		File newFile = new File(oriFile.getParentFile().getAbsolutePath()+"/tempTest.xlsx");
		try(FileInputStream fis = new FileInputStream(oriFile);
				FileOutputStream fos = new FileOutputStream(newFile)){
			byte[] bytes = new byte[1024];
			int length = 0;
			while((length = fis.read(bytes))>0){
				fos.write(bytes, 0, length);
			}
		}catch( Exception e){
			e.printStackTrace();
		}
		List<String> errormsg = categoryManager.readAllAlias(newFile);
		Assert.assertEquals("sheet:digikey 第3行分类：测试次小类 不存在",errormsg.get(0));
	}
	
	
	@Test
	public void testClearCategoryCache() {
		categoryManager.clearCategoryCache();
	}
	
	@Test
	public void testFuzzyMatch(){
		categoryManager.fuzzyMatch("贴片", FuzzySearchType.RIGHT_FUZZY);
	}
}
