package com.yikuyi.party.vendors.bll;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
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
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.info.resource.NewsClient;
import com.yikuyi.news.model.News;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.vo.FacilityVo;
import com.yikuyi.party.vo.VendorVo;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class VendorsManagerTest {
	
	@Autowired
	private VendorsManager vendorsManager;
	
	@Autowired
	private VendorManager vendorManager;
	
	
	
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@SpyBean
	private InfoClientBuilder infoClientBuilder;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String serverUrl;
	

	public VendorsManagerTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void deleteTest() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("1001");
		list.add("1002");
		
		//Mockito.when(shipmentClientBuilder.facilityResource()).thenReturn(Mockito.mock(FacilityClient.class));
		//Mockito.when(shipmentClientBuilder.facilityResource().delFacilityById("9999999901", authorizationUtil.getLoginAuthorization())).thenReturn(true);
		
		vendorsManager.delete(list, "9999999901");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void saveTest() throws UnsupportedEncodingException{
		VendorVo vo = new VendorVo();
		vo.setIsShowName("Y");
		vo.setVendorId("123456789");
		vo.setLogoUrl("www.baidu.com");
		vo.setVendorName("腾讯QQ");
		FacilityVo facilityVo = new FacilityVo();
		facilityVo.setName("腾讯QQ");
		List<FacilityVo> vos = new ArrayList<>();
		vos.add(facilityVo);
		vo.setItem(vos);

		
		Facility facility=new Facility();
        facility.setFacilityName(facilityVo.getName());
        facility.setOwnerPartyId(vo.getVendorId());
		//Mockito.when(shipmentClientBuilder.facilityResource()).thenReturn(Mockito.mock(FacilityClient.class));
		//Mockito.when(shipmentClientBuilder.facilityResource().addFacility(facility, authorizationUtil.getLoginAuthorization())).thenReturn(facility);
		
		vendorsManager.save(vo, "123456789");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void getUserTest(){
		
		vendorsManager.getUser("770876471316054016");
	}
	
	@Test
	public void getFacilityByPartyIdTest(){
		
		//List<Facility> facilityList = new ArrayList<>();
		//Mockito.when(shipmentClientBuilder.facilityResource()).thenReturn(Mockito.mock(FacilityClient.class));
		//Mockito.when(shipmentClientBuilder.facilityResource().getFacilityList("770876471316054016")).thenReturn(facilityList);
		vendorsManager.getFacilityByPartyId("770876471316054016");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void saveSupplierShareTest(){
		UserVo userVo = new UserVo();
		userVo.setId("10001,10002");
		userVo.setName("张无忌，张三丰");
		vendorsManager.saveSupplierShare("", userVo);
		vendorsManager.saveSupplierShare("9999999901", userVo);
		
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void findSupplierShareTest(){
		vendorsManager.findSupplierShare("9999999902");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyrelactionship_sampledata.xml")
	public void getVendorListTest(){
		vendorsManager.getVendorList("99900",null,null,1, 10);
	}
	

	@Test
	public void getVendorSaleInfoVoTest() throws BusinessException{
		vendorManager.getVendorSaleInfoVo("900629983003672576");
	}
	
	@Test
	public void getVendorCreditTest(){
		vendorManager.getVendorCredit("900629983003672576");
	}
	
	
	
	
	@Test
	public void getVendorPartyIds(){
		vendorsManager.getVendorPartyIds("900629983003672576");
	}
	
	
	@Test
	public void updateLeadTimeTest(){
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName("11");
		partyGroup.setPartyId("11");
		vendorsManager.updateLeadTime(partyGroup);
	}
	
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getPartySupplierListTest(){
		 vendorsManager.getPartySupplierList("test","test",1,1);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void supplierLeadTimejobTest(){
		 vendorsManager.supplierLeadTimejob();
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void deptPartyIdsTest(){
		 vendorsManager.deptPartyIds("99900");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getParentRelationInfoTest(){
		 vendorsManager.getParentRelationInfo("999",PartyRelationshipTypeEnum.AFFILIATED_REL);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void delFacilityByIdTest(){
		 vendorsManager.delFacilityById("999");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void saveFacilityTest(){
		Facility vo = new Facility();
		vo.setOwnerPartyId("1");
		vo.setId("1");
		 vendorsManager.saveFacility(vo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void updateTest(){
		VendorVo vo = new VendorVo();
		vo.setVendorId("1");
		vo.setCode("1");
		vo.setIsShowName("1");
		vo.setSupPrice("1");
		vo.setVendorName("111");
		 vendorsManager.update(vo,"1");
	}
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getVendorDetailTest(){
		 vendorsManager.getVendorDetail("9999999901");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getPartyByPartyIdTest(){
		 vendorsManager.getPartyByPartyId("11","111");
		 vendorsManager.getPartyByPartyId("12","222");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getNewsByVnedorIdTest(){
		List<String> list = new ArrayList<>();
		List<News> newsList = new ArrayList<>();
		News news = new News();
		news.setNewsIds(list);
		news.setStatus("PUBLISHED");
		news.setCategoryTypeId("VENDOR");
		Mockito.when(infoClientBuilder.builderNewsClient()).thenReturn(Mockito.mock(NewsClient.class));
		Mockito.when(infoClientBuilder.builderNewsClient().newsBatch(news)).thenReturn(newsList);
				
		
		 vendorsManager.getNewsByVnedorId(list);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getVendorNameListByIdsTest(){
		 vendorsManager.getVendorNameListByIds(Arrays.asList("1"));
	}
	
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getPartyListTest(){
		PartyGroupVo vo = new PartyGroupVo();
		 vendorsManager.getPartyList(vo,RowBounds.DEFAULT);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "partyrelactionship_sampledata.xml")
	public void getSellPartyIdsTest(){
		 vendorsManager.getSellPartyIds("88888");
	}
	

}