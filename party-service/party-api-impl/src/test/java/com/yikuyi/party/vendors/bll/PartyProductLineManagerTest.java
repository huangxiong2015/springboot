package com.yikuyi.party.vendors.bll;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class PartyProductLineManagerTest {
	
	@Autowired
	private PartyProductLineManager partyProductLineManager;
	
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	

	
	@Value("${api.basedata.serverUrlPrefix}")
	private String serverUrl;
	

	public PartyProductLineManagerTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**
	 * 根据产品线ID 查询产品信息
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void findByIdTest() throws Exception{
		String partyProductLineId = "899552626163580928";
		PartyProductLine partyProductLine = partyProductLineManager.findById(partyProductLineId);
		assertEquals("电线电缆",partyProductLine.getCategory1Name());
		
		String partyProductLineId1 = "";
		partyProductLineManager.findById(partyProductLineId1);
	
	}
	
	/**
	 * 查询产品线集合
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void findByEntityTest() throws Exception{
		PartyProductLine line = new PartyProductLine();
		line.setPartyId("899552626029363200");
		List<PartyProductLine> list = partyProductLineManager.findByEntity(line);
		assertEquals("AKM",list.get(0).getBrandName());
	}
	
	
	/**
	 * 查询唯一产品线集合
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void onlyProductLineTest() throws Exception{
		PartyProductLine productLine = new PartyProductLine();
		productLine.setPartyId("899559114013671424");
		List<PartyProductLineVo> partyProductLineList = partyProductLineManager.onlyProductLine(productLine);
		assertEquals("E-T-A",partyProductLineList.get(0).getBrandName());
	}
	
	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void onlyProductCategoryListTest() throws Exception{
		PartyProductLineModel lineModel = new PartyProductLineModel();
		lineModel.setPartyId("899552626029363200");
		List<PartyProductLineModel> productLineModelList = partyProductLineManager.onlyProductCategoryList(lineModel);
		assertEquals("电线电缆",productLineModelList.get(0).getCategory1Name());
	}
	
	/**
	 * listSupplierByBrandId
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void listSupplierByBrandIdTest() throws Exception{
		String brandId = "387";
		List<PartyProductLineVo> productLineList = partyProductLineManager.listSupplierByBrandId(brandId);
		assertEquals("深圳市新蕾电子有限公司",productLineList.get(0).getGroupName());
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyProductLine_sampledata.xml")
	public void listCategoryByBrandIdTest() throws Exception{
		String brandId = "37";
		List<PartyProductLineVo> voList = partyProductLineManager.listCategoryByBrandId(brandId);
		assertEquals(1,voList.size());
	}

}