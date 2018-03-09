package com.yikuyi.product.specialoffer.manager;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.common.dao.BaseMongoClient;
import com.yikuyi.specialoffer.model.SpecialOfferRule;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SpecialOfferCacheManagerTest extends ProductApplicationTestBase {
	
    @Autowired
    private SpecialOfferCacheManager specialOfferCacheManager;

	@Autowired
	private BaseMongoClient baseMongoClient;
    
    @Test
    public void initSpecialOfferProductRuleCacheTest(){
        SpecialOfferRule rule = new SpecialOfferRule();
       // rule.setMfrIds("one,two,three");
       // rule.setSourceIds("上海,北京,深圳");
       //rule.setCatIds("1/2/3,1/2/*,1/*/*,*/*/*");
        specialOfferCacheManager.initSpecialOfferProductRuleCache(rule,"W","e");
    }
    
    /**
     * 初始化供应商Id缓存
     */
    @Test
    public void initSpecialOfferProductIdCacheTest(){
    	specialOfferCacheManager.initSpecialOfferProductIdCache("123", "33");
    }
    
    /**
     * 删除供应商规则缓存
     */
    @Test
    public void deleteSpecialOfferProductIdCacheTest(){
    	specialOfferCacheManager.deleteSpecialOfferProductIdCache("123", "33");
    }
    
	@Test
	public void specialTest(){
		try {
			String ruleId = "2017122000001";
			Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copySpecialProductDraft(\""+ruleId+"\")"));
			Assert.assertEquals(1.0, result.get("ok"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
