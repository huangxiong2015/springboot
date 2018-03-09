package com.yikuyi.product.goods.manager;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.goods.manager.SearchAsyncManager.KeywordMatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class SearchAsyncManagerTest {

	@Autowired
	private SearchAsyncManager searchAsyncManager;

	@SpyBean
	private BrandManager brandManager;

	@SpyBean
	private CategoryManager categoryManager;

	/*
	 * @SpyBean private InfoClientBuilder infoClientBuilder;
	 */

	public SearchAsyncManagerTest() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * 品牌意图识别
	 * 
	 * @throws Exception
	 * @since 2017年11月27日
	 * @author jik.shu@yikuyi.com
	 */
	@Test
	public void testKeywordMatchBrand() throws Exception {
		searchAsyncManager.keywordMatch(null, KeywordMatch.BRAND);
		Mockito.when(brandManager.getAliasBrandMap()).thenReturn(null);
		Assert.assertNull(searchAsyncManager.keywordMatch("3m", KeywordMatch.BRAND).get());
	}

	/**
	 * 品牌意图识别
	 * 
	 * @throws Exception
	 * @since 2017年11月27日
	 * @author jik.shu@yikuyi.com
	 */
	@Test
	public void testKeywordMatchBrand2() {
		Mockito.doThrow(new RuntimeException("Junit Test RuntimeException")).when(brandManager).getAliasBrandMap();
		searchAsyncManager.keywordMatch("3m", KeywordMatch.BRAND);
	}

	/**
	 * 分类意图识别
	 * 
	 * @throws Exception
	 * @since 2017年11月27日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Test
	public void testKeywordMatchCate() throws Exception {
		searchAsyncManager.keywordMatch(null, KeywordMatch.CAT);
		Mockito.when(categoryManager.getCateByCateName("非电阻")).thenReturn(null);
		Assert.assertNull(searchAsyncManager.keywordMatch("非电阻", KeywordMatch.CAT).get());
	}

	/**
	 * 分类意图识别
	 * 
	 * @throws Exception
	 * @since 2017年11月27日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Test
	public void testKeywordMatchCate2() {
		Mockito.doThrow(new RuntimeException("Junit Test RuntimeException")).when(categoryManager).getCateByCateName("电阻");
		searchAsyncManager.keywordMatch("电阻", KeywordMatch.BRAND);
	}

}