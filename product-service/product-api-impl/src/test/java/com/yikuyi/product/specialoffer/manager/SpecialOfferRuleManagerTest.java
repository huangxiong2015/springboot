package com.yikuyi.product.specialoffer.manager;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.common.dao.BaseMongoClient;
import com.yikuyi.specialoffer.model.SpecialOfferRule;
import com.yikuyi.specialoffer.model.SpecialOfferRule.MpnType;
import com.yikuyi.specialoffer.model.SpecialOfferRule.RuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SpecialOfferRuleManagerTest extends ProductApplicationTestBase {
	
    @Autowired
    private SpecialOfferRuleManager specialOfferRuleManager;
    
    private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	    response = new MockHttpServletResponse();    
	}
    
    /**
     * 添加专属特价规则信息
     * @throws Exception
     * @since 2018年1月2日
     * @author zr.wuxiansheng@yikuyi.com
     */
    @Test
    public void testAddSpecialOfferRule() throws Exception{
    	//产品线
    	String ruleId = "945843085386973126";
    	SpecialOfferRule specialOfferRule = new SpecialOfferRule();
    	specialOfferRule.setId(ruleId);
    	specialOfferRule.setVendorId("925935277485064192");
    	specialOfferRule.setRuleType(RuleType.RULE);//产品线
    	List<Integer> mfrIds = new ArrayList<>();//制造商
    	mfrIds.add(9);
    	mfrIds.add(31);
    	mfrIds.add(1387);
    	List<String> sourceIds = new ArrayList<>();//仓库
    	sourceIds.add("926014575185231872");
    	sourceIds.add("926014575239757824");
    	List<String> catIds = new ArrayList<>();//次小类
    	catIds.add("1/41/45");
    	catIds.add("1/41/46");
    	catIds.add("1/17/*");
    	
    	specialOfferRuleManager.addSpecialOfferRule(ruleId, specialOfferRule);
    	
    	String ruleIdMpn = "945239081627942458";
    	SpecialOfferRule specialOfferRuleMpn = new SpecialOfferRule();
    	specialOfferRuleMpn.setRuleType(RuleType.MPN);
    	specialOfferRule.setVendorId("digikey");
    	specialOfferRule.setMpnType(MpnType.INPUT);//手动上传
    	specialOfferRuleMpn.setDesc("hahhhahaha");
    	
    }

	
}
