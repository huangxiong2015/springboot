package com.yikuyi.party.vendorManage.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckStartOrLose.StartOrLose;
import com.yikuyi.party.vendor.vo.CheckVendorInfoVo;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.PartySupplierAlias;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorQueryVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vendorManage.bll.SendMail;
import com.yikuyi.party.vendorManage.bll.VendorManage2;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;

/**
 * 
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class VendorManageResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	@InjectMocks
	private VendorManageResource vendorManageResource;
	
	@Mock
	private VendorManage2 vendorManage;
	
	@Mock
	private SendMail sendMail;
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	   
	public VendorManageResourceTest() {
	}
	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(vendorManageResource).build();
	}
	
	@Test
	public void testAddVendor() throws Exception {
		Vendor vo = new Vendor();
		vo.setPartyId("1");
		when(vendorManage.addVendor(Mockito.any(Vendor.class))).thenReturn(vo);
		Vendor result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/addVendor").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),Vendor.class);
		Assert.assertEquals(result.getPartyId(),"1");
	}
	
	@Test
	public void testEditVendorApplySave() throws Exception {
		Apply apply = new Apply();
		Mockito.doNothing().when(vendorManage).editVendorApplySave(Mockito.any(Apply.class));
        mockMvc.perform(post("/v1/vendorManage/apply/vendor/callback").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());
	}
	@Test
	public void testEditStartApplySave() throws Exception {
		Apply apply = new Apply();
		Mockito.doNothing().when(vendorManage).editStartApplySave(Mockito.any(Apply.class));
        mockMvc.perform(post("/v1/vendorManage/apply/start/callback").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	@Test
	public void testEditLoseApplySave() throws Exception {
		Apply apply = new Apply();
		Mockito.doNothing().when(vendorManage).editLoseApplySave(Mockito.any(Apply.class));
        mockMvc.perform(post("/v1/vendorManage/apply/lose/callback").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	@Test
	public void testEditVendorInfoApplySave() throws Exception {
		Apply apply = new Apply();
		String applyContent = "{'partyId': '1','groupNameFull': 'test', 'groupName': 'test'}";
		apply.setStatus(ApplyStatus.APPROVED);
		apply.setApplyContent(applyContent);
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("9999999901");
		apply.setcRoleId("CEO");
		Mockito.doNothing().when(vendorManage).editVendorInfoApplySave(Mockito.any(CheckVendorInfoVo.class));
        mockMvc.perform(post("/v1/vendorManage/apply/basedata/callback").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	@Test
	public void testEditProductLineApplySave() throws Exception {
		Apply apply = new Apply();
		String applyContent = "{'partyId': '1','brandName': 'test', 'partyProductLineList': [{'brandId':'1'}]}";
		apply.setStatus(ApplyStatus.APPROVED);
		apply.setApplyContent(applyContent);
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("9999999901");
		apply.setcRoleId("CEO");
		Mockito.doNothing().when(vendorManage).editProductLineApplySave(Mockito.anyList());
        mockMvc.perform(post("/v1/vendorManage/apply/productline/callback").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

		
	}
	
	@Test
	public void testUpdateVendorSaleInfoVo() throws Exception {
		VendorSaleInfoVo vo = new VendorSaleInfoVo();
		vo.setCategory("1");
		when(vendorManage.updateVendorSaleInfoVo(Mockito.any(VendorSaleInfoVo.class))).thenReturn(vo);
		VendorSaleInfoVo result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/updateVendorSaleInfoVo").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), VendorSaleInfoVo.class);
		Assert.assertEquals(result.getCategory(),"1");
	
	
	}
	
	@Test
	public void testUpdateVendorCreditVo() throws Exception {
		VendorCreditVo vo = new VendorCreditVo();
		vo.setApplyUser("1");
		vendorManage.updateVendorCreditVo(vo);
		
		when(vendorManage.updateVendorCreditVo(Mockito.any(VendorCreditVo.class))).thenReturn(vo);
		VendorCreditVo result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/updateVendorCreditVo").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), VendorCreditVo.class);
		Assert.assertEquals(result.getApplyUser(),"1");
	}
	
	@Test
	public void testUpdatePartyProductLine() throws Exception {
		PartyProductLineVo vo = new PartyProductLineVo();
		vo.setApplyName("1");
		
		when(vendorManage.updatePartyProductLine(Mockito.any(PartyProductLineVo.class))).thenReturn(vo);
		PartyProductLineVo result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/updatePartyProductLine").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyProductLineVo.class);
		Assert.assertEquals(result.getApplyName(),"1");
	}
	
	@Test
	public void testUpdateVendorInfoVo() throws Exception {
		VendorInfoVo vo = new VendorInfoVo();
		vo.setCategory("1");
		
		when(vendorManage.updateVendorInfoVo(Mockito.any(VendorInfoVo.class))).thenReturn(vo);
		VendorInfoVo result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/updateVendorInfoVo").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), VendorInfoVo.class);
		Assert.assertEquals(result.getCategory(),"1");
	}
	
	@Test
	public void testStartOrLose() throws Exception {
		CheckStartOrLose checkStartOrLose = new CheckStartOrLose();
		checkStartOrLose.setApplyName("test");
	
		when(vendorManage.startOrLose(Mockito.anyString(),Mockito.any(StartOrLose.class),Mockito.anyString(),Mockito.any(CheckStartOrLose.class))).thenReturn(checkStartOrLose);
		CheckStartOrLose result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/startOrLose?partyId=1&startOrLose=START&describe=1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(checkStartOrLose)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), CheckStartOrLose.class);
		Assert.assertEquals(result.getApplyName(),"test");
	}
	
	@Test
	public void testParseProductsFile() throws Exception {
		PageInfo<PartyProductLine> pageInfo = new PageInfo();
		List<PartyProductLine> list = new ArrayList<>();
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setBrandId("1");
		list.add(partyProductLine);
		pageInfo.setList(list);
		when(vendorManage.parseProductsFile(Mockito.anyString(),Mockito.anyString(), PartyProductLineVo.Type.PROXY)).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, PartyProductLine.class);
		
		PageInfo<PartyProductLine> result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/uploadLine?partyId=1&fileUrl=www.baidu.com"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.getList().get(0).getBrandId(),"1");
	}
	

	@Test
	public void testGetVendorManageList() throws Exception {
		PageInfo<VendorResponVo> pageInfo = new PageInfo();
		List<VendorResponVo> list = new ArrayList<>();
		VendorResponVo vendorResponVo = new VendorResponVo();
		vendorResponVo.setCategory("1");;
		list.add(vendorResponVo);
		pageInfo.setList(list);
		when(vendorManage.getVendorManageList(Mockito.any(VendorQueryVo.class),Mockito.any(RowBounds.class))).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, VendorResponVo.class);
		
		PageInfo<VendorResponVo> result = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/getVendorManageList"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.getList().get(0).getCategory(),"1");
	}
	
	@Test
	public void testGetSellManageList() throws Exception {
		PageInfo<VendorResponVo> pageInfo = new PageInfo();
		List<VendorResponVo> list = new ArrayList<>();
		VendorResponVo vendorResponVo = new VendorResponVo();
		vendorResponVo.setCategory("1");;
		list.add(vendorResponVo);
		pageInfo.setList(list);
		when(vendorManage.getSellManageList(Mockito.any(VendorQueryVo.class),Mockito.any(RowBounds.class))).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, VendorResponVo.class);
		
		PageInfo<VendorResponVo> result = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/getSellManageList"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.getList().get(0).getCategory(),"1");
	}
	@Test
	public void testGetOrderVerify() throws Exception {
		List<PartySupplier> list = new ArrayList<>();
		PartySupplier partySupplier = new PartySupplier();
		partySupplier.setCategory("1");;
		list.add(partySupplier);
		when(vendorManage.getOrderVerify(Arrays.asList("1"))).thenReturn(list);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartySupplier.class);
		
		List<PartySupplier> result = mapper.readValue(mockMvc.perform(post("/v1/vendorManage/getOrderVerify").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(Arrays.asList("1"))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.get(0).getCategory(),"1");
	}
	
	@Test
	public void testEnableOrlose() throws Exception {
		Mockito.doNothing().when(vendorManage).enableOrlose(Mockito.any(Party.class));
		mockMvc.perform(put("/v1/vendorManage/enableOrlose/1/"+PartyStatus.PARTY_ENABLED+"/1")).andExpect(status().isOk());

	}
	
/*	@Test
	public void testSendMailTwo() throws Exception {
		Mockito.doNothing().when(sendMail).checkApply(Mockito.anyString(),RoleTypeEnum.CEO,Mockito.any(VendorApplyType.class),Mockito.anyString());
		mockMvc.perform(post("/v1/vendorManage/sendMailTwo/test")).andExpect(status().isOk());

	}*/
	
	@Test
	public void testFindProductLineByPartyId() throws Exception {
	
		PartyProductLineVo mockVo = new PartyProductLineVo();
		mockVo.setApplyName("1");
		when(vendorManage.findProductLineByPartyId("9999999901")).thenReturn(mockVo);
		
		mockVo = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/findProductLineByPartyId/9999999901"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyProductLineVo.class);
		Assert.assertEquals(mockVo.getApplyName(),"1");

	}
	

	@Test
	public void testFindGroupName() throws Exception {
		PartyGroup mockVo = new PartyGroup();
		mockVo.setPartyId("2");
		when(vendorManage.findGroupName(Mockito.anyString(),Mockito.anyString())).thenReturn(mockVo);
		
		mockVo = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/findGroupName?partyId=1&groupName=1"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyGroup.class);
		Assert.assertEquals(mockVo.getPartyId(),"2");
	}
	
	@Test
	public void testListVendorManage() throws Exception {
		
		List<VendorResponVo> list = new ArrayList<>();
		VendorResponVo vendorResponVo = new VendorResponVo();
		vendorResponVo.setCategory("1");
		list.add(vendorResponVo);
		when(vendorManage.listVendorManage(Arrays.asList("1"))).thenReturn(list);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, VendorResponVo.class);
		
		List<VendorResponVo> result = mapper.readValue(mockMvc.perform(put("/v1/vendorManage/listVendorManage").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(Arrays.asList("1"))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.get(0).getCategory(),"1");

	}
	
	@Test
	public void testSaveSupplierAlias() throws Exception {
		List<PartySupplierAlias> partyList = new ArrayList<>();
		PartySupplierAlias partySupplierAlias = new PartySupplierAlias();
		partySupplierAlias.setAliasName("1");
		partyList.add(partySupplierAlias);
		Mockito.doNothing().when(vendorManage).saveSupplierAlias(partyList);
		mockMvc.perform(post("/v1/vendorManage/saveSupplierAlias").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyList)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
	}
	
	
	@Test
	public void isExistAliasNameTest() throws Exception {
		PartySupplierAlias partySupplierAlias = new PartySupplierAlias();
		partySupplierAlias.setAliasName("1");
		when(vendorManage.isExistSupplier(Mockito.anyString())).thenReturn(partySupplierAlias);
		PartySupplierAlias vo = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/isExistAliasName?aliasName=1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString("1")))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartySupplierAlias.class);
		Assert.assertEquals(vo.getAliasName(), "1");
	}
	
	
	@Test
	public void supperAliasNameListTest() throws Exception {
		List<PartySupplierAlias> list = new ArrayList<>();
		PartySupplierAlias partySupplierAlias = new PartySupplierAlias();
		partySupplierAlias.setAliasName("1");
		list.add(partySupplierAlias);
		when(vendorManage.supperAliasNameList(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartySupplierAlias.class);

		List<PartySupplierAlias> result = mapper.readValue(mockMvc.perform(get("/v1/vendorManage/supperAliasNameList?aliasName=1&partyId=1"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(result.get(0).getAliasName(), "1");
	}
	
	@Test
	public void testDeleteSupperAlias() throws Exception {
         mockMvc.perform(delete("/v1/vendorManage/deleteSupperAlias/9999999901").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString("9999999901")).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk());
	}
}
