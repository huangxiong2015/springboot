package com.yikuyi.product.brand.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.brand.manager.BrandManager;
import com.ykyframework.exception.BusinessException;

public class ManufacturerResourceTest extends ProductApplicationTestBase{
	
	//party服务地址
	@Value("${api.party.serverUrlPrefix}")
	private String partyUrl;
	
	@Autowired
	private BrandManager brandManager;
	
	/**
	 * 分页查询所有的制造商
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindAllBrandList(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers";
		ResponseEntity<PageInfo<ProductBrand>> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<ProductBrand>>(){});
		PageInfo<ProductBrand> brand = response.getBody();
		assertNotNull(brand.getList());
	}
	
	/**
	 * 根据制造商名称查询列表
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindBrandListByName(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers?brandName=PANJIT";
		ResponseEntity<PageInfo<ProductBrand>> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<ProductBrand>>(){});
		PageInfo<ProductBrand> brand = response.getBody();
		assertNotNull(brand.getList());
	}
	
	/**
	 * 根据创建人查询列表
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindBrandListByCreator(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers?creator=admin";
		ResponseEntity<PageInfo<ProductBrand>> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<ProductBrand>>(){});
		PageInfo<ProductBrand> brand = response.getBody();
		assertNotNull(brand.getList());
	}
	
	/**
	 * 根据时间段查询列表
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindBrandListByDate(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers?startDate=2017-03-10&endDate=2017-03-30";
		ResponseEntity<PageInfo<ProductBrand>> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<ProductBrand>>(){});
		PageInfo<ProductBrand> brand = response.getBody();
		assertNotNull(brand.getList());
	}
	
	/**
	 * 根据所有条件（制造商名称、创建人、时间段）查询列表
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testFindBrandListByAll(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers?brandName=ffggg&creator=admin&startDate=2017-03-10&endDate=2017-03-30";
		ResponseEntity<PageInfo<ProductBrand>> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<ProductBrand>>(){});
		PageInfo<ProductBrand> brand = response.getBody();
		assertNotNull(brand.getList());
	}
	
	/**
	 * 新增制造商
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testAddBrand(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		ProductBrand brandInfo = new ProductBrand();
		brandInfo.setBrandName("danyuan");
		brandInfo.setLogo("logo");
		brandInfo.setDesc("desc");
		brandInfo.setBrandShort("brandShort");
		List<String> aliasList = new ArrayList<>();
		aliasList.add("danyuan1");
		aliasList.add("danyuan2");
		brandInfo.setBrandAlias(aliasList);
		
		//调用删除先清空
		ProductBrand pb = brandManager.getBrandByAliasName(null, "danyuan");
		if(pb!=null)
			brandManager.deleteBrand(pb.getId());
		
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers";
		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
		ResponseEntity<ProductBrand> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.POST, entity, ProductBrand.class);
		ProductBrand brand = response.getBody();
		assertNotNull(brand);
		brandManager.deleteBrand(brand.getId());
	}
	
	/**
	 * 新增制造商(存在制造商别名无法保存)
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testAddBrandByAlias(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		ProductBrand brandInfo = new ProductBrand();
		brandInfo.setBrandName("danyuan");
		brandInfo.setLogo("logo");
		brandInfo.setDesc("desc");
		brandInfo.setBrandShort("brandShort");
		List<String> aliasList = new ArrayList<>();
		aliasList.add("PANJIT");
		aliasList.add("danyuan2");
		brandInfo.setBrandAlias(aliasList);
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers";
		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
		ResponseEntity<ProductBrand> response = null;
		try{
			response = this.getRestTemplate().exchange(requestUrl, HttpMethod.POST, entity, ProductBrand.class);
		}catch (Exception e) {
			System.out.println("调用新增制造商服务失败！");
		}
		//assertNull(response);
	}
	
	/**
	 * 新增制造商(没有制造商别名)
	 * @since 2017年3月30日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testAddBrandNotAlias(){
		this.mockPartyService();
		//调用本单元测试对应的rest服务，发起真实的请求
		ProductBrand brandInfo = new ProductBrand();
		brandInfo.setBrandName("danyuan11");
		brandInfo.setLogo("logo");
		brandInfo.setDesc("desc");
		brandInfo.setBrandShort("brandShort");
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers";
		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
		ResponseEntity<ProductBrand> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.POST, entity, ProductBrand.class);
		ProductBrand brand = response.getBody();
		assertNotNull(brand);
		brandManager.deleteBrand(brand.getId());
	}
	
	/**
	 * 修改制造商
	 * 
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpdateBrand() throws BusinessException{
		this.mockPartyService();
		//初始化数据
		ProductBrand info = addBrand();
		//调用本单元测试对应的rest服务，发起真实的请求
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers/"+info.getId();
		HttpEntity<ProductBrand> entity = new HttpEntity<>(info);
		ResponseEntity<ProductBrand> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.PUT, entity, ProductBrand.class);
		ProductBrand brand = response.getBody();
		assertNotNull(brand);
		brandManager.deleteBrand(info.getId());
	}
	
	/**
	 * 修改制造商(存在制造商名称无法修改)
	 * 
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpdateBrandByName() throws BusinessException{
		//TODO
//		this.mockPartyService();
//		//初始化数据
//		ProductBrand info = addBrand();
//		//调用本单元测试对应的rest服务，发起真实的请求
//		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers/"+info.getId();
//		ProductBrand brandInfo = new ProductBrand();
//		brandInfo.setBrandName("PANJIT");
//		brandInfo.setLogo("logo");
//		brandInfo.setDesc("desc");
//		brandInfo.setBrandShort("brandShort");
//		List<String> aliasList = new ArrayList<>();
//		aliasList.add("danyuan1");
//		aliasList.add("danyuan2");
//		brandInfo.setBrandAlias(aliasList);
//		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
//		ResponseEntity<ProductBrand> response = null;
//		try{
//			response = this.getRestTemplate().exchange(requestUrl, HttpMethod.PUT, entity, ProductBrand.class);
//		}catch(Exception e) {
//			System.out.println("调用修改制造商服务失败！");
//		}
//		// assertNull(response);
//		brandManager.deleteBrand(info.getId());
	}
	
	/**
	 * 修改制造商(存在制造商别名无法修改)
	 * 
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpdateBrandByAlias() throws BusinessException{
		//TODO
//		this.mockPartyService();
//		//初始化数据
//		ProductBrand info = addBrand();
//		//调用本单元测试对应的rest服务，发起真实的请求
//		ProductBrand brandInfo = new ProductBrand();
//		brandInfo.setBrandName("danyuan");
//		brandInfo.setLogo("logo");
//		brandInfo.setDesc("desc");
//		brandInfo.setBrandShort("brandShort");
//		List<String> aliasList = new ArrayList<>();
//		aliasList.add("PANJIT");
//		aliasList.add("danyuan2");
//		brandInfo.setBrandAlias(aliasList);
//		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers/"+info.getId();
//		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
//		ResponseEntity<ProductBrand> response = null;
//		try{
//			response = this.getRestTemplate().exchange(requestUrl, HttpMethod.PUT, entity, ProductBrand.class);
//		}catch(Exception e) {
//			System.out.println("调用修改制造商服务失败！");
//		}
//		// assertNull(response);
//		brandManager.deleteBrand(info.getId());
	}
	
	/**
	 * 修改制造商(制造商别名为空)
	 * 
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
//	@Test
	public void testUpdateBrandNotAlias() throws BusinessException{
		this.mockPartyService();
		//初始化数据
		ProductBrand info = addBrand();
		//调用本单元测试对应的rest服务，发起真实的请求
		ProductBrand brandInfo = new ProductBrand();
		brandInfo.setBrandName("danyuan");
		brandInfo.setLogo("logo");
		brandInfo.setDesc("desc");
		brandInfo.setBrandShort("brandShort");
		String requestUrl = "http://localhost:" + this.getPort() + "/v1/products/manufacturers/"+info.getId();
		HttpEntity<ProductBrand> entity = new HttpEntity<>(brandInfo);
		ResponseEntity<ProductBrand> response = this.getRestTemplate().exchange(requestUrl, HttpMethod.PUT, entity, ProductBrand.class);
		ProductBrand brand = response.getBody();
		assertNotNull(brand);
		brandManager.deleteBrand(info.getId());
	}
	
	/**
	 * 初始化数据
	 * @return
	 * @throws BusinessException
	 * @since 2017年3月31日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductBrand addBrand() throws BusinessException{
		ProductBrand brandInfo = new ProductBrand();
		brandInfo.setBrandName("danyuantest");
		brandInfo.setLogo("logo");
		brandInfo.setDesc("desc");
		brandInfo.setBrandShort("brandShort");
		List<String> aliasList = new ArrayList<>();
		aliasList.add("danyuantest1");
		aliasList.add("danyuantest2");
		brandInfo.setBrandAlias(aliasList);
		Integer id = 99999910;
		brandManager.deleteBrand(id);
		ProductBrand info = brandManager.saveBrand(id, brandInfo);
		return info;
	}

}
