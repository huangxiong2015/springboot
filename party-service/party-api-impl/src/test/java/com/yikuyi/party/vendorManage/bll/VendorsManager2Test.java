package com.yikuyi.party.vendorManage.bll;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.basedata.resource.CategoryClient;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckStartOrLose.StartOrLose;
import com.yikuyi.party.vendor.vo.CheckVendorInfoVo;
import com.yikuyi.party.vendor.vo.ContactPersonInfo;
import com.yikuyi.party.vendor.vo.ContactPersonInfo.PersonalTitle;
import com.yikuyi.party.vendor.vo.PartyBankAccount;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineJS;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorQueryVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.product.ProductClientBuilder;
import com.yikuyi.product.resource.BrandClient;
import com.yikuyi.product.resource.SearchClient;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.ApplyClient;
import com.yikuyi.workflow.vo.ApplyExtendsVo;
import com.yikuyi.workflow.vo.ApplyVo;
import com.yikuyi.workflow.vo.ApplyVo.ApplyVoType;
import com.ykyframework.api.persist.IdGen;
import com.ykyframework.exception.BusinessException;

import net.sf.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class VendorsManager2Test {
	
	@Autowired
	private VendorManage2 vendorsManager2;
	

	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	
	@SpyBean
	private ProductClientBuilder productClientBuilder;
	
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	
	public VendorsManager2Test() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
		
	@Test
	public void parseProductsFileTest() {
		try {
			PageInfo<PartyProductLine> parseProductsFile = vendorsManager2.parseProductsFile(null, "//ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/vendor/product/201709/08/28e825fb657046161470748d66e09f23.xlsx", PartyProductLineVo.Type.PROXY);
			List<PartyProductLine> list = parseProductsFile.getList();
			Assert.assertTrue(list.size()>0);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void addVendorTest() throws BusinessException {
		
		Vendor vendor =new Vendor();
		vendor.setDescribe("新建档案");
		
		// 供应商基础信息
		VendorInfoVo vendorInfoVo =new VendorInfoVo();
		// 产品线信息
		List<PartyProductLine> partyProductLineList = new ArrayList<>();
		// 供应商信用信息
		VendorCreditVo vendorCreditVo =new VendorCreditVo();
		// 供应商销售信息
		VendorSaleInfoVo vendorSaleInfoVo = new VendorSaleInfoVo();
		vendorInfoVo.setDescribe("我要新建文档");
		vendorInfoVo.setGroupNameFull("网易有限公司");
		vendorInfoVo.setGroupName("网易"+new Random().nextInt(1000));
		vendorInfoVo.setPartyCode("10086");
		vendorInfoVo.setCategory("1");
		vendorInfoVo.setCategoryName("游戏");
		vendorInfoVo.setIsCore("Y");
		vendorInfoVo.setLogoImageUrl("//aa.img");
		vendorInfoVo.setWebsite("www.xyq163.com");
		vendorInfoVo.setRegion("CHAINA");
		vendorInfoVo.setRegionName("中国");
		vendorInfoVo.setGeneralHeadquarters("中国");
		vendorInfoVo.setGenre("001");
		vendorInfoVo.setGenreName("国有企业");
		vendorInfoVo.setListed("Y");
		vendorInfoVo.setStockCode("888888");
		vendorInfoVo.setLegalPerson("王磊");
		vendorInfoVo.setRegPrice("100000000");
		vendorInfoVo.setRegAddress("杭州");
		vendorInfoVo.setEmployeeNum("10000");
		vendorInfoVo.setDeptId("110");
		vendorInfoVo.setDeptName("业务拓展部");
		vendorInfoVo.setPrincipalId("999");
		vendorInfoVo.setPrincipalName("张三");
		vendorInfoVo.setEnquiryId("888");
		vendorInfoVo.setEnquiryName("李四");	
		vendorInfoVo.setOfferId("999");
		vendorInfoVo.setOfferName("666");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("VENDOR_INFO_LEGALPERSON","张三");
		map.put("VENDOR_INFO_REGPRICE","10000");
		map.put("VENDOR_INFO_REGRADDRESS","北京");
		map.put("VENDOR_INFO_EMPLOYEENUM","100000");
		map.put("VENDOR_INFO_WEBSITE","www.baidu.com");
		map.put("VENDOR_CREDIT_PURCHASEDEAL","ALREADY_SIGN");
		map.put("VENDOR_CREDIT_PURCHASEDEALDATE","2018-8-8");
		map.put("VENDOR_CREDIT_SECRECYPROTOCOL","ALREADY_SIGN");
		map.put("VENDOR_CREDIT_SECRECYPROTOCOLDATE","2018-8-8");
		map.put("VENDOR_SALE_INFOVO_FOCUSFIELDS","电压，电阻");
		map.put("VENDOR_SALE_INFOVO_PRODUCTCATEGORYS","价格优惠");
		map.put("VENDOR_SALE_INFOVO_MAJORCLIENTS","淘宝");
		
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setPartyProductLineId(String.valueOf(IdGen.getInstance().nextId()));
		partyProductLine.setBrandName("3M");
		partyProductLine.setBrandId("1");
		partyProductLine.setCategory1Name("无源器件");
		partyProductLine.setCategory1Id("1");
		partyProductLine.setCategory2Name("电阻");
		partyProductLine.setCategory2Id("2");
		partyProductLine.setCategory3Name("贴片电阻");
		partyProductLine.setCategory3Id("11");
		partyProductLineList.add(partyProductLine);
		
		
		
		
		List<PartyBankAccount> partyBankAccountList =new ArrayList<>();
		PartyBankAccount partyBankAccount2 = new PartyBankAccount();
		partyBankAccount2.setAccountName("中国银行");
		partyBankAccount2.setCurrency(Currency.CNY);
		partyBankAccount2.setBankAccount("123456789");
		partyBankAccount2.setBankName("王磊");
		partyBankAccount2.setTaxNumber("110");
		partyBankAccount2.setAddress("杭州分行");
		partyBankAccount2.setContactNumber("13888888888");
		partyBankAccount2.setIsDefault("Y");
		partyBankAccount2.setSwiftCode(null);
		partyBankAccountList.add(partyBankAccount2);
		
		List<PartyAttachment> creditAttachmentList = new ArrayList<>();
		PartyAttachment partyCreditAttachment = new PartyAttachment();
		partyCreditAttachment.setAttachmentName("账期");
		partyCreditAttachment.setAttachmentUrl("www.baidu.com");
		creditAttachmentList.add(partyCreditAttachment);

		vendorCreditVo.setPartyBankAccount(partyBankAccountList);
		vendorCreditVo.setPurchaseDeal("ALREADY_SIGN");
		vendorCreditVo.setPurchaseDealDate("2018-8-8");
		vendorCreditVo.setSecrecyProtocol("ALREADY_SIGN");
		vendorCreditVo.setSecrecyProtocolDate("2018-8-8");
		vendorCreditVo.setCurrency(Currency.CNY);
		vendorCreditVo.setCreditQuota(10000);
		vendorCreditVo.setRealtimeCreditQuota(0);
		vendorCreditVo.setCreditDeadline("30");
		vendorCreditVo.setCheckDate("30");
		vendorCreditVo.setPaymentTerms("月结");
		vendorCreditVo.setSettlementMethod("现金");
		vendorCreditVo.setPayDate("30");
		vendorCreditVo.setCommon(null);
		vendorCreditVo.setCreditAttachmentList(creditAttachmentList);
		
		List<Facility> facilityList = new ArrayList<>();
		Facility facility = new Facility();
		facility.setFacilityName("亚洲仓");
		facilityList.add(facility);
		
		List<ContactPersonInfo> contactPersonInfoList=new ArrayList<>();
		
		List<String> list = new ArrayList<>();	
		list.add("88888");
		list.add("99999");
		list.add("00000");
				
		ContactPersonInfo contactPersonInfo = new ContactPersonInfo();
		contactPersonInfo.setLastNameLocal("王磊");
		contactPersonInfo.setOccupation("猪席");
		contactPersonInfo.setPersonalTitle(PersonalTitle.ENQUIRY);
		contactPersonInfo.setMail("48484848@qq.com	");
		contactPersonInfo.setFixedtel("6339739");
		contactPersonInfo.setTel("13888888888");
		contactPersonInfo.setAddress("杭州");
		contactPersonInfo.setIsDefault("Y");
		contactPersonInfo.setPartyProductLineIdList(list);
		contactPersonInfoList.add(contactPersonInfo);
		
		vendorSaleInfoVo.setFacilityList(facilityList);
		vendorSaleInfoVo.setFocusFields("电阻,电容");
		vendorSaleInfoVo.setProductCategorys("电阻1,电容1");
		vendorSaleInfoVo.setMajorClients("中国,美国");
		vendorSaleInfoVo.setContactPersonInfoList(contactPersonInfoList);
		vendorSaleInfoVo.setOrderVerify("Y");
		vendorSaleInfoVo.setIsShowName("Y");
		vendorSaleInfoVo.setSupportCurrency("CNY");
		vendorSaleInfoVo.setMinOrderPriceCNY(new BigDecimal(60));
		vendorSaleInfoVo.setMinOrderPriceUSD(new BigDecimal(6));
		List<String> enquiryList = new ArrayList<>();
		enquiryList.add("99888");
		List<String> offerList = new ArrayList<>();
		offerList.add("99888");
		vendorInfoVo.setEnquiryList(enquiryList);
		vendorInfoVo.setOfferList(offerList);
		vendorInfoVo.setGenreName("111");
		vendor.setVendorInfoVo(vendorInfoVo);
		//vendor.setPartyProductLineList(partyProductLineList);
		vendor.setVendorCreditVo(vendorCreditVo);
		/*vendor.setVendorSaleInfoVo(vendorSaleInfoVo);
		vendor.setMap(map);		
		
		
						
			  /*String applyId=String.valueOf(IdGen.getInstance().nextId());*/
			  // 插入PARTY_RELATIONSHIP 供应商与审核的关系
		
		
			  
		Apply apply = new Apply();		
		JSONObject jsonObject = JSONObject.fromObject(vendor);  
		apply.setApplyId("111111111111");
		apply.setApplyContent(jsonObject.toString());
		apply.setApplyUserId("111");
		apply.setProcessId("ORG_SUPPLIER_ARCHIVES_REVIEW");
		apply.setApplyOrgId("111");
		apply.setCompanyName(vendor.getVendorInfoVo().getGroupName());
		apply.setApplyPageUrl("");
		apply.setCallBackUrl("");
		apply.setReason(vendor.getDescribe());

		Map<String, ProductBrand> aliasBrandMap =new HashMap<>();	
		BrandClient brandResource =Mockito.mock(BrandClient.class);
		Mockito.when( productClientBuilder.brandResource()).thenReturn(brandResource);
		Mockito.when(brandResource.getAliasBrandMap(authorizationUtil.getLoginAuthorization())).thenReturn(aliasBrandMap);

		 List<ProductCategoryChild> allCategory = new ArrayList<>();
		com.yikuyi.product.resource.CategoryClient categoryClient =Mockito.mock(com.yikuyi.product.resource.CategoryClient.class);
		Mockito.when(productClientBuilder.categoryResource()).thenReturn((com.yikuyi.product.resource.CategoryClient) categoryClient);
		Mockito.when(categoryClient.getAllCategory(Mockito.any())).thenReturn(allCategory);

		
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn("111111111111");
     
		PageInfo<Apply> page3 = new PageInfo<Apply>();
		ApplyVo applyVo = new ApplyVo();	
		applyVo.setApplyOrgId("1");
		ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
		Mockito.when(applyClient.queryApplyByEntity(ApplyVoType.PROCESS,"ORG_SUPPLIER_ARCHIVES_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(page3);
     
		
	   try{	
		vendorsManager2.addVendor(vendor);	
		Vendor vendor1 =new Vendor();
		vendor1.setPartyId("1");
		VendorInfoVo vo = new VendorInfoVo();
		vo.setGroupName("111");
		vo.setDeptId("1");
		vo.setPrincipalId("1");
		vo.setEnquiryList(Arrays.asList("1"));
		vo.setOfferList(Arrays.asList("1"));
		vendor1.setVendorInfoVo(vo);
		
		VendorCreditVo voCredit = new VendorCreditVo();
		List<PartyBankAccount> listBank = new ArrayList<>();
		PartyBankAccount bank = new PartyBankAccount();
		bank.setAccountName("1");
		listBank.add(bank);
		List<PartyAttachment> listCreit = new ArrayList<>();
		PartyAttachment partyAttachment = new PartyAttachment();
		partyAttachment.setAttachmentName("1");
		listCreit.add(partyAttachment);
		voCredit.setCreditAttachmentList(listCreit);
		voCredit.setPartyBankAccount(listBank);
		vendor1.setVendorCreditVo(voCredit);
		vendorsManager2.addVendor(vendor1);	
	   }catch(Exception e){}
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/vendorlist_sampledata.xml"})
	public void updateVendorSaleInfoVoTest() throws BusinessException {
		
		// 供应商销售信息
		VendorSaleInfoVo vendorSaleInfoVo = new VendorSaleInfoVo();
		
		List<Facility> facilityList = new ArrayList<>();
		Facility facility = new Facility();
		facility.setFacilityName("亚洲仓");
		facility.setOwnerPartyId("899517648864280576");
		facilityList.add(facility);
		
		List<ContactPersonInfo> contactPersonInfoList=new ArrayList<>();
		
		ContactPersonInfo contactPersonInfo = new ContactPersonInfo();
		contactPersonInfo.setPartyId("9999999998888");
		contactPersonInfo.setLastNameLocal("王磊");
		contactPersonInfo.setOccupation("猪席");
		contactPersonInfo.setPersonalTitle(PersonalTitle.ENQUIRY);
		contactPersonInfo.setMail("48484848@qq.com	");
		contactPersonInfo.setFixedtel("6339739");
		contactPersonInfo.setTel("13888888888");
		contactPersonInfo.setAddress("杭州");
		contactPersonInfo.setIsDefault("Y");
		contactPersonInfo.setPartyProductLineIdList(Arrays.asList("1111"));
		contactPersonInfoList.add(contactPersonInfo);
		
		
		
		Map<String, String> map = new HashMap<>();
		map.put("VENDOR_SALE_INFOVO_FOCUSFIELDS", "电阻,电容");
		map.put("VENDOR_SALE_INFOVO_PRODUCTCATEGORYS", "电阻1,电容1");
		map.put("VENDOR_SALE_INFOVO_MAJORCLIENTS", "中国,美国");
		
		vendorSaleInfoVo.setPartyId("9999999901");
		vendorSaleInfoVo.setFacilityList(facilityList);
		vendorSaleInfoVo.setFocusFields("电阻,电容");
		vendorSaleInfoVo.setProductCategorys("电阻1,电容1");
		vendorSaleInfoVo.setMajorClients("中国,美国");
		vendorSaleInfoVo.setContactPersonInfoList(contactPersonInfoList);
		vendorSaleInfoVo.setOrderVerify("Y");
		vendorSaleInfoVo.setIsShowName("Y");
		vendorSaleInfoVo.setSupportCurrency("CNY");
		vendorSaleInfoVo.setSaleAttributeMap(map);
		vendorSaleInfoVo.setMinOrderPriceCNY(new BigDecimal(60));
		vendorSaleInfoVo.setMinOrderPriceUSD(new BigDecimal(6));
		
		vendorSaleInfoVo.setEnquiryList(Arrays.asList("111"));
		vendorSaleInfoVo.setOfferList(Arrays.asList("111"));
		
		vendorSaleInfoVo.setPrincipalId("1");
		
		vendorsManager2.updateVendorSaleInfoVo(vendorSaleInfoVo);	
		
		
		// 供应商销售信息
		VendorSaleInfoVo vendorSaleInfoVo1 = new VendorSaleInfoVo();
		vendorSaleInfoVo1.setPartyId("1");
		vendorSaleInfoVo1.setVendorMovStatus("Y");
		vendorSaleInfoVo1.setSkuMovStatus("Y");
		vendorSaleInfoVo1.setIsShowName("Y");
		vendorSaleInfoVo1.setOrderVerify("Y");
		vendorSaleInfoVo1.setIsSupPrice("y");
		
		vendorsManager2.updateVendorSaleInfoVo(vendorSaleInfoVo1);	
		
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/vendorlist_sampledata.xml"})
	public void updateVendorCreditVoTest() throws BusinessException {
		// 供应商信用信息
	    VendorCreditVo vendorCreditVo =new VendorCreditVo();
	    
	    List<PartyBankAccount> partyBankAccountList =new ArrayList<>();
		PartyBankAccount partyBankAccount2 = new PartyBankAccount();
		partyBankAccount2.setAccountName("王磊");
		partyBankAccount2.setCurrency(Currency.CNY);
		partyBankAccount2.setBankAccount("123456789");
		partyBankAccount2.setBankName("中国银行");
		partyBankAccount2.setTaxNumber("110");
		partyBankAccount2.setAddress("杭州分行");
		partyBankAccount2.setContactNumber("13888888888");
		partyBankAccount2.setIsDefault("Y");
		partyBankAccount2.setSwiftCode(null);
		partyBankAccountList.add(partyBankAccount2);
		
		List<PartyAttachment> creditAttachmentList = new ArrayList<>();
		PartyAttachment partyCreditAttachment = new PartyAttachment();
		partyCreditAttachment.setAttachmentName("账期");
		partyCreditAttachment.setAttachmentUrl("www.baidu.com");
		creditAttachmentList.add(partyCreditAttachment);
		
		Map<String, String> map = new HashMap<>();
		map.put("VENDOR_CREDIT_PURCHASEDEAL", "ALREADY_SIGN");
		map.put("VENDOR_CREDIT_PURCHASEDEALDATE", "2018-8-8");
		map.put("VENDOR_CREDIT_SECRECYPROTOCOL", "ALREADY_SIGN");
		map.put("VENDOR_CREDIT_SECRECYPROTOCOLDATE", "2018-8-8");

		vendorCreditVo.setPartyBankAccount(partyBankAccountList);
		vendorCreditVo.setPartyId("9999999901");	
		vendorCreditVo.setPurchaseDeal("ALREADY_SIGN");
		vendorCreditVo.setPurchaseDealDate("2018-8-8");
		vendorCreditVo.setSecrecyProtocol("ALREADY_SIGN");
		vendorCreditVo.setSecrecyProtocolDate("2018-8-8");
		vendorCreditVo.setCurrency(Currency.CNY);
		vendorCreditVo.setCreditQuota(10000);
		vendorCreditVo.setRealtimeCreditQuota(0);
		vendorCreditVo.setCreditDeadline("30");
		vendorCreditVo.setCheckDate("30");
		vendorCreditVo.setPaymentTerms("月结");
		vendorCreditVo.setSettlementMethod("现金");
		vendorCreditVo.setPayDate("30");
		vendorCreditVo.setCreditAttributeMap(map);
		vendorCreditVo.setCommon(null);
		vendorCreditVo.setCreditAttachmentList(creditAttachmentList);
		
		vendorsManager2.updateVendorCreditVo(vendorCreditVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/vendorlist_sampledata.xml"})
	public void updatePartyProductLineTest() throws BusinessException {
		
		PartyProductLineVo p=new PartyProductLineVo();
			
		List<PartyProductLine> partyProductLineList =new ArrayList<>();
		PartyProductLine partyProductLine1 = new PartyProductLine();
		partyProductLine1.setPartyId("9999999901");
		partyProductLine1.setBrandName("6666");
		PartyProductLine partyProductLine2 = new PartyProductLine();
		partyProductLine2.setBrandName("6666");
		PartyProductLine partyProductLine3 = new PartyProductLine();
		partyProductLine3.setBrandName("7777");
		partyProductLineList.add(partyProductLine1);
		partyProductLineList.add(partyProductLine2);
		partyProductLineList.add(partyProductLine3);
		
		p.setPartyId("9999999901");
		p.setDescribe("产品线变更");
		p.setPartyProductLineList(partyProductLineList);
		
		try {
			Map<String, ProductBrand> aliasBrandMap =new HashMap<>();	
			BrandClient brandResource =Mockito.mock(BrandClient.class);
			Mockito.when( productClientBuilder.brandResource()).thenReturn(brandResource);
			Mockito.when(brandResource.getAliasBrandMap(authorizationUtil.getLoginAuthorization())).thenReturn(aliasBrandMap);

			 List<ProductCategoryChild> allCategory = new ArrayList<>();
			com.yikuyi.product.resource.CategoryClient categoryClient =Mockito.mock(com.yikuyi.product.resource.CategoryClient.class);
			Mockito.when(productClientBuilder.categoryResource()).thenReturn((com.yikuyi.product.resource.CategoryClient) categoryClient);
			Mockito.when(categoryClient.getAllCategory(Mockito.any())).thenReturn(allCategory);

			vendorsManager2.updatePartyProductLine(p);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/vendorlist_sampledata.xml"})
	public void updateVendorInfoVoTest() throws BusinessException {
		// 供应商基础信息
		VendorInfoVo vendorInfoVo =new VendorInfoVo();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("VENDOR_INFO_LEGALPERSON","张三11");
		map.put("VENDOR_INFO_REGPRICE","1000011");
		map.put("VENDOR_INFO_REGRADDRESS","北京11");
		map.put("VENDOR_INFO_EMPLOYEENUM","10000011");
		map.put("VENDOR_INFO_WEBSITE","www.baidu11.com");
		
		vendorInfoVo.setPartyId("9999999901");
		vendorInfoVo.setGroupNameFull("网易有限公司11222");
		vendorInfoVo.setGroupName("网易11222"+new Random().nextInt(1000));
		vendorInfoVo.setPartyCode("YKY2017");
		vendorInfoVo.setCategory("100003");
		vendorInfoVo.setCategoryName("游戏22");
		vendorInfoVo.setIsCore("Y");
		vendorInfoVo.setLogoImageUrl("//aaa.img");
		vendorInfoVo.setWebsite("www.baidu.com");
		vendorInfoVo.setRegion("CHAINA1");
		vendorInfoVo.setRegionName("中国");
		vendorInfoVo.setGeneralHeadquarters("中国1");
		vendorInfoVo.setGenre("008");
		vendorInfoVo.setGenreName("国有企业");
		vendorInfoVo.setListed("N");
		vendorInfoVo.setStockCode("888888112");
		vendorInfoVo.setLegalPerson("王磊");
		vendorInfoVo.setRegPrice("100000000");
		vendorInfoVo.setRegAddress("杭州");
		vendorInfoVo.setEmployeeNum("10000");
		vendorInfoVo.setDeptId("110112211");
		vendorInfoVo.setDeptName("业务拓展部");
		vendorInfoVo.setPrincipalId("99112211");
		vendorInfoVo.setPrincipalName("张三");
		vendorInfoVo.setEnquiryId("888112211");
		vendorInfoVo.setEnquiryName("李四");	
		vendorInfoVo.setVendorInfoAttributeMap(map);
		vendorInfoVo.setDescribe("基础信息更改");
		List<String> enquiryList = new ArrayList<>();
		enquiryList.add("99888");
		List<String> offerList = new ArrayList<>();
		offerList.add("99888");
		vendorInfoVo.setEnquiryList(enquiryList);
		vendorInfoVo.setOfferList(offerList);
		try {
			vendorsManager2.updateVendorInfoVo(vendorInfoVo);
		} catch (Exception e) {
			
		}						
	}
	
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void startOrLoseTest() throws BusinessException {
		
			try {
				PageInfo<Apply> page = new PageInfo<Apply>();
				ApplyVo applyVo = new ApplyVo();	
				applyVo.setApplyOrgId("999");
				ApplyClient applyClient =Mockito.mock(ApplyClient.class);
				Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
				Mockito.when(applyClient.queryApplyByEntity(ApplyVoType.PROCESS,"ORG_SUPPLIER_ENABLED_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(page);
				
				
				PageInfo<Apply> page1 = new PageInfo<Apply>();
				ApplyVo applyVo1 = new ApplyVo();	
				applyVo1.setApplyOrgId("999");
				ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
				Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
				Mockito.when(applyClient1.queryApplyByEntity(ApplyVoType.PROCESS,"ORG_SUPPLIER_INVALID_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(page1);
			
				PageInfo<Apply> page2 = new PageInfo<Apply>();
				ApplyVo applyVo2 = new ApplyVo();	
				applyVo2.setApplyOrgId("999");
				ApplyClient applyClient2 =Mockito.mock(ApplyClient.class);
				Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient2);
				Mockito.when(applyClient2.queryApplyByEntity(ApplyVoType.PROCESS,"ORG_SUPPLIER_ARCHIVES_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(page2);
			
				List<Category> regions = new ArrayList<>();
				CategoryClient categoryClient =Mockito.mock(CategoryClient.class);
				Mockito.when(shipmentClientBuilder.categoryResource()).thenReturn(categoryClient);
				Mockito.when(categoryClient.categoryList("VENDOR_THE_REGION")).thenReturn(regions);
				
				
				Apply apply = new Apply();	
				apply.setApplyOrgId("999");
				ApplyClient applyClient3 =Mockito.mock(ApplyClient.class);
				Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient3);
				Mockito.when(applyClient3.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn("1");
				
				Map<String, Object> map= new HashMap<>();
				Object object="str";
				map.put("productInfo", object);
				SearchClient searchClient =Mockito.mock(SearchClient.class);
				Mockito.when( productClientBuilder.searchResource()).thenReturn(searchClient);
				Mockito.when(searchClient.search(null, "1", null, null, null, 1, 1, null,null)).thenReturn(map);
				
				
				CheckStartOrLose checkStartOrLose = new CheckStartOrLose();
				vendorsManager2.startOrLose("899559114013671424",StartOrLose.LOSE,"我要启动",checkStartOrLose);
				
				CheckStartOrLose checkStartOrLose1 = new CheckStartOrLose();
				vendorsManager2.startOrLose("899552626029363299",StartOrLose.START,"",checkStartOrLose1);
			
			} catch (Exception e) {
			
			}
					
	}
	
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testListVendorManage() {
		List<String> strList = new ArrayList<>();
		strList.add("899559114013671424");
		List<VendorResponVo> list = vendorsManager2.listVendorManage(strList);
		assertEquals(list.get(0).getGroupName(),"深圳市新蕾电子有限公司");
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testFindProductLineByPartyId() {
      vendorsManager2.findProductLineByPartyId("899559114013671424");
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEditProductLineApplySave() {
		List<PartyProductLineJS> partyProductLineList = new ArrayList<>();
		PartyProductLineJS jsVo = new PartyProductLineJS();
		jsVo.setPartyProductLineId("1");
		jsVo.setSelect(PartyProductLineJS.Select.getSelect("NEW"));
		partyProductLineList.add(jsVo);
        vendorsManager2.editProductLineApplySave(partyProductLineList);
        
        
        List<PartyProductLineJS> partyProductLineList1 = new ArrayList<>();
		PartyProductLineJS jsVo1 = new PartyProductLineJS();
		jsVo1.setPartyProductLineId("1");
		jsVo1.setSelect(PartyProductLineJS.Select.getSelect("DEL"));
		partyProductLineList1.add(jsVo1);
        vendorsManager2.editProductLineApplySave(partyProductLineList1);
	}
	
	
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEditVendorInfoApplySave() {
		CheckVendorInfoVo vo = new CheckVendorInfoVo();
		vo.setPartyId("1111111111111");
		vo.setNewPartyCode("111111");
		vo.setGroupName("test");
		vo.setGroupNameFull("test");
		vo.setIsCore("Y");
		vo.setCategory("1");
        vendorsManager2.editVendorInfoApplySave(vo);
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEditLoseApplySave() {
		Apply apply = new Apply();
		apply.setStatus(ApplyStatus.APPROVED);
		apply.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editLoseApplySave(apply);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
        
        Apply apply1 = new Apply();
		apply1.setStatus(ApplyStatus.REJECT);
		apply1.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editLoseApplySave(apply1);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	

	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEditStartApplySave() {
		Apply apply = new Apply();
		apply.setStatus(ApplyStatus.APPROVED);
		apply.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editStartApplySave(apply);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Apply apply1 = new Apply();
		apply1.setStatus(ApplyStatus.REJECT);
		apply1.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editStartApplySave(apply1);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	

	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEditVendorApplySave() {
		Apply apply = new Apply();
		apply.setStatus(ApplyStatus.APPROVED);
		apply.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editVendorApplySave(apply);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        Apply apply1 = new Apply();
		apply1.setStatus(ApplyStatus.REJECT);
		apply1.setApplyOrgId("9999999901");
        try {
			vendorsManager2.editVendorApplySave(apply1);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testEnableOrlose() {
		Party party = new Party();
		party.setId("111");
		party.setPartyCode("2222");
		vendorsManager2.enableOrlose(party);
	}
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testGetOrderVerify() {
		List<String> partyIds = new ArrayList<>();
		partyIds.add("1");
		vendorsManager2.getOrderVerify(partyIds);
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testGetSellManageList() {
		VendorQueryVo vo = new VendorQueryVo();
		List<Category> list = new ArrayList<>();
		CategoryClient categoryClient =Mockito.mock(CategoryClient.class);
		Mockito.when(shipmentClientBuilder.categoryResource()).thenReturn(categoryClient);
		Mockito.when(categoryClient.categoryList("VENDOR_THE_TYPE")).thenReturn(list);
		ApplyExtendsVo applyExtendsVo = new ApplyExtendsVo();
		List<ApplyExtendsVo> applyList = new ArrayList<>();
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.listApplyByVo(applyExtendsVo, authorizationUtil.getLoginAuthorization())).thenReturn(applyList);

		vendorsManager2.getSellManageList(vo,RowBounds.DEFAULT);
		
		
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testGetVendorManageList() {
		VendorQueryVo vo = new VendorQueryVo();
		List<Category> list = new ArrayList<>();
		CategoryClient categoryClient =Mockito.mock(CategoryClient.class);
		Mockito.when(shipmentClientBuilder.categoryResource()).thenReturn(categoryClient);
		Mockito.when(categoryClient.categoryList("VENDOR_THE_TYPE")).thenReturn(list);
		
		ApplyExtendsVo applyExtendsVo = new ApplyExtendsVo();
		List<ApplyExtendsVo> applyList = new ArrayList<>();
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.listApplyByVo(applyExtendsVo, authorizationUtil.getLoginAuthorization())).thenReturn(applyList);

		vendorsManager2.getVendorManageList(vo,RowBounds.DEFAULT);
		
	}
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testDeleteVendorInfo() {
		vendorsManager2.deleteVendorInfo("1");
	}
	
	@Test   
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/vendors/bll/partyProductLine_sampledata.xml"})
	public void testVerifyProductLine() {
		Map<String, ProductBrand> aliasBrandMap =new HashMap<>();	
		BrandClient brandResource =Mockito.mock(BrandClient.class);
		Mockito.when( productClientBuilder.brandResource()).thenReturn(brandResource);
		Mockito.when(brandResource.getAliasBrandMap(authorizationUtil.getLoginAuthorization())).thenReturn(aliasBrandMap);
		List<ProductCategoryChild> allCategory = new ArrayList<>();
		
		com.yikuyi.product.resource.CategoryClient categoryClient =Mockito.mock(com.yikuyi.product.resource.CategoryClient.class);
		Mockito.when(productClientBuilder.categoryResource()).thenReturn((com.yikuyi.product.resource.CategoryClient) categoryClient);
		Mockito.when(categoryClient.getAllCategory(Mockito.any())).thenReturn(allCategory);

		
		Set<PartyProductLine> productList = new LinkedHashSet<>();
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setBrandId("1");
		productList.add(partyProductLine);
		
		try {
			vendorsManager2.verifyProductLine(productList);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	@Test   
	public void testInsertpartyRelationship() {
		Set<PartyProductLine> partyProductLineList = new LinkedHashSet<>();
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setBrandId("1");
		partyProductLineList.add(partyProductLine);
		try {
			vendorsManager2.insertpartyRelationship("1",partyProductLineList);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
}