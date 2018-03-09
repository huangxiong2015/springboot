package com.yikuyi.product.brand.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.brand.dao.BrandRepository;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandManagerTest{
	private static final Logger logger = LoggerFactory.getLogger(BrandManager.class);

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private BrandManager brandManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	//hash缓存
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ProductBrand> aliasBrandOps;
	
	/**
	 * 测试老版本菜单商品缓存
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testMenuCacheOld(){
		long time1 = System.currentTimeMillis();
		brandManager.cacheBrands();//刷所有缓存
		long time2 = System.currentTimeMillis();
		logger.info("刷老版本菜单缓存耗时："+(time2-time1));
		//检查缓存
		Cache cache = cacheManager.getCache("allBrandsInfoCache");
		ValueWrapper value = cache.get("brandInfoList");
		Assert.assertNotNull(value);
		//检查缓存内容不为空
		List<ProductBrand> brandList = (List<ProductBrand>)value.get();
		Assert.assertTrue(brandList.size()>0);
	}
	
	/**
	 * 测试老版本商品缓存
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testBrandCacheOld(){
		//清掉缓存
		Cache cache = cacheManager.getCache("aliasMap");
		cache.evict("aliasBrandMap");
		//执行获取缓存
		long time1 = System.currentTimeMillis();
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();
		long time2 = System.currentTimeMillis();
		logger.info("无缓存情况下刷老版本商品缓存耗时："+(time2-time1));
		//检查缓存不为空
		Assert.assertNotNull(brandMap);
		Assert.assertFalse(brandMap.isEmpty());
		
		//在已有缓存的情况下重新获取缓存
		time1 = System.currentTimeMillis();
		brandMap = brandManager.getAliasBrandMap();
		time2 = System.currentTimeMillis();
		logger.info("已有缓存情况下刷老版本商品缓存耗时："+(time2-time1));
		//检查缓存不为空
		Assert.assertNotNull(brandMap);
		Assert.assertFalse(brandMap.isEmpty());
	}
	
	/**
	 * 测试新版本缓存刷新
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testInitBrandCache(){
		long time1 = System.currentTimeMillis();
		Map<String, ProductBrand> brandMap = brandManager.initAliasBrand();
		long time2 = System.currentTimeMillis();
		logger.info("刷商品别名缓存耗时："+(time2-time1));
		//检查缓存不为空
		Assert.assertNotNull(brandMap);
		Assert.assertFalse(brandMap.isEmpty());
	}
	
	/**
	 * 测试从缓存获取品牌
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testGetBrand(){
		String key1 = brandManager.getAliasKey("testVendorId", "testAlias1");
		String key2 = brandManager.getAliasKey("testVendorId", "testAlias1");
		//增加一个测试数据
		ProductBrand pb = new ProductBrand();
		pb.setId(0);
		pb.setBrandName("testbrand");
		aliasBrandOps.put(BrandManager.ALIAS_BRAND_NAMESPACE, key1, pb);
		aliasBrandOps.put(BrandManager.ALIAS_BRAND_NAMESPACE, key2, pb);
		//获取单个缓存
		pb = brandManager.getBrandByAliasName("testVendorId", "testAlias1");
		//检查数据
		Assert.assertEquals(new Integer(0), pb.getId());
		Assert.assertEquals("testbrand", pb.getBrandName());
		
		//获取批量
		List<String> keys = new ArrayList<>();
		keys.add(key1);
		keys.add(key2);
		//获取批量缓存
		Map<String, ProductBrand> brandMap = brandManager.getBrandByAliasName(keys);
		//检查数据
		ProductBrand pb1 = brandMap.get(key1);
		Assert.assertEquals(new Integer(0), pb1.getId());
		Assert.assertEquals("testbrand", pb1.getBrandName());
		ProductBrand pb2 = brandMap.get(key2);
		Assert.assertEquals(new Integer(0), pb2.getId());
		Assert.assertEquals("testbrand", pb2.getBrandName());
		
	}
	
	/**
	 * 测试名称排序的查询全部
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testFindAll(){
		//过滤属性非空的查询
		List<ProductBrand> list = brandManager.findAllByCondition();//查询全部记录
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		//不过滤的查询
		list = brandManager.findAll();//查询全部记录
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
	}
	
	/**
	 * 测试通过id查找数据
	 * @since 2017年11月16日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testFindById(){
		//创建个测试数据
		ProductBrand pb = new ProductBrand();
		pb.setId(0);
		pb.setBrandName("testBrand");
		brandRepository.save(pb);
		//查找
		ProductBrand result = brandManager.findById(0);
		//验证
		Assert.assertNotNull(result);
		Assert.assertEquals(new Integer(0),result.getId());
		
		//删除测试数据
		brandRepository.delete(pb);
	}
	
	/**
	 * 测试查询制造商
	 * @since 2017年11月21日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testGetBrandList(){
		//创建个测试数据
		ProductBrand pb = new ProductBrand();
		pb.setId(0);
		pb.setBrandName("testBrand");
		brandRepository.save(pb);
		
		PageInfo<ProductBrand> brands = brandManager.getBrandList("testBrand", null, 1, 10, null, null);
		//验证
		Assert.assertNotNull(brands);
		Assert.assertTrue(brands.getList().size()>1);

		//删除测试数据
		brandRepository.delete(pb);
	}
	
	/**
	 * 测试新增制造商
	 * @since 2017年11月21日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testSaveBrand(){
		//创建个测试数据
		ProductBrand pb = new ProductBrand();
		Integer max = brandManager.getMaxBrandId();
		pb.setBrandName("testBrand");
		pb.setId(max);
		
		//验证
		try {
			brandManager.saveBrand(max, pb);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		ProductBrand result = brandRepository.findById(max);
		//验证
		Assert.assertNotNull(result);
		Assert.assertEquals("testBrand",result.getBrandName());

		//删除测试数据
		brandRepository.delete(pb);
	}
	
	/**
	 * 测试修改品牌
	 * @since 2017年11月21日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testUpdateBrand(){
		//创建个测试数据
		ProductBrand pb = new ProductBrand();
		pb.setId(0);
		pb.setBrandName("testBrand");
		brandRepository.save(pb);
		
		ProductBrand info = new ProductBrand();
		info.setBrandName("testBrand2");
		info.setId(0);
		try {
			brandManager.updateBrand(0, info);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		ProductBrand result = brandRepository.findById(0);
		//验证
		Assert.assertNotNull(result);
		Assert.assertEquals("testBrand2",result.getBrandName());
		
		//删除测试数据
		brandRepository.delete(pb);
	}
	
	/**
	 * 测试删除品牌
	 * @since 2017年11月21日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testDeleteBrand(){
		//创建个测试数据
		ProductBrand pb = new ProductBrand();
		pb.setId(0);
		pb.setBrandName("testBrand");
		brandRepository.save(pb);
		
		brandManager.deleteBrand(0);

		//验证
		ProductBrand result = brandRepository.findById(0);
		Assert.assertNull(result);
	}
	
	@Test
	public void testFindByAilas(){
		List<ProductBrand> result = brandManager.findByAilas("b",null,null);
		Assert.assertNotNull(result);
	}
}
