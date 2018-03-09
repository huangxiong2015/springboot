package com.yikuyi.party.vendors.bll;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.basedata.resource.CategoryClient;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class VendorManagerTest {
	
	@Autowired
	private VendorManager vendorManager;
	
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	

	
	@Value("${api.basedata.serverUrlPrefix}")
	private String serverUrl;
	

	public VendorManagerTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getVendorInfoTest(){
		
			CategoryClient categoryClient =Mockito.mock(CategoryClient.class);
			Mockito.when(shipmentClientBuilder.categoryResource()).thenReturn(categoryClient);
			Mockito.when(categoryClient.findById("1")).thenReturn(new Category());
			try {
				vendorManager.getVendorInfo("9999999901");
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getVendorSaleInfoVoTest(){
		try {
			vendorManager.getVendorSaleInfoVo("9999999901");
			
			vendorManager.getVendorSaleInfoVo("9999999905");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getVendorCreditTest(){
		vendorManager.getVendorCredit("9999999901");
		
	}
	
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getPartyRelationshipInfoTest(){
		VendorSaleInfoVo vendorSaleInfoVo = new VendorSaleInfoVo();
		
		vendorManager.getPartyRelationshipInfo(vendorSaleInfoVo,"9999999901");
		
	}
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void vendorBatchListTest(){
		vendorManager.vendorBatchList(Arrays.asList("1","2"));
	}
}