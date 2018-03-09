package com.yikuyi.party.vendors.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.party.vo.SupplierVo;
import com.yikuyi.party.vo.VendorVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义供应商的相关接口
 * 
 * @author guowenyao
 *
 */
public interface IVendorResource {

	/**
	 * 查询供应商列表
	 * 
	 * @author 张伟
	 * @param partyGroup
	 * @return
	 */
	@ApiOperation(value = "获取供应商列表", notes = "获取供应商列表", response = PartyVo.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PageInfo<PartyVo> getPartyList(@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "10") int size);

	/**
	 * 根据供应商id获取单条供应商详情
	 * 
	 * @author 张伟
	 * @param partyId
	 * @return
	 */
	@ApiOperation(value = "根据供应商id获取单条供应商详情", notes = "根据供应商id获取单条供应商详情", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public Party getPartyByPartyId(@ApiParam(value = "供应商id", required = true) String partyId);
	
	
	/**
	 * 根据供应商id获取单条供应商详情
	 * 
	 * @author 张伟
	 * @param partyId
	 * @return
	 */
	@ApiOperation(value = "根据供应商ids获取单条供应商详情", notes = "根据供应商ids获取单条供应商详情", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<PartyVo> getVendorNameListByIds(@ApiParam(value = "供应商ids", required = true) List<String> partyIds);
	
	/**
	 * 查询供应商详情
	 * 
	 * @author 张伟
	 * @param partyId
	 * @return
	 */
	@ApiOperation(value = "根据供应商id获取单条供应商详情", notes = "根据供应商id获取单条供应商详情", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public Party getVendorDetail(@ApiParam(value = "供应商id", required = true) String partyId);

	/**
	 * 保存供应商信息
	 * 
	 * @author 张伟
	 * @param Party
	 * @return
	 */
	@ApiOperation(value = "保存供应商信息", notes = "保存供应商", response = MsgResultVo.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void save(@ApiParam(value = "供应商信息的json结构", required = true) VendorVo vendorVo) throws BusinessException;

	/**
	 * 4.编辑供应商
	 * 
	 * @param id,供应商json
	 * @return 供应商
	 */
	@ApiOperation(value = "修改供应商", notes = "修改供应商", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void update(@ApiParam(value = "根据id修改供应商信息", required = true) VendorVo vendorVo)throws BusinessException;

	/**
	 * 5.删除供应商
	 * 
	 * @param id
	 * @return 供应商
	 */
	@ApiOperation(value = "删除供应商", notes = "删除供应商", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void delete(@ApiParam(value = "根据id修改供应商信息", required = true) List<String> ids);
	
	/**
	 * 保存分享关系
	 * @param id
	 * @param userVo
	 * @return
	 * @since 2017年8月25日
	 */
	@ApiOperation(value = "保存分享关系", notes = "保存分享关系", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public String saveSupplierShare(@ApiParam(value = "供应商ID", required = true) String id,@ApiParam(value = "用户vo，获取公司名字，id（id多个用,分开）", required = true)UserVo userVo);
	
	/**
	 * 查询供应商分享关系
	 * @param id
	 * @return
	 * @since 2017年8月25日
	 */
	@ApiOperation(value = "查询供应商分享关系", notes = "查询供应商分享关系", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<PartyRelationship> findSupplierShare(@ApiParam(value = "供应商ID", required = true) String id);
	

	/**
	 * 获取供应商列表
	 * @param id
	 * @param groupName  供应商名称
	 * @param partyCode  供应商编码
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年7月18日
	 */
	@ApiOperation(value = "查询供应商列表", notes = "查询供应商列表", response = SupplierVo.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PageInfo<SupplierVo> getVendorList(@ApiParam(value = "当前登录用户ID", required = true) String id,
			@ApiParam(value = "供应商名称", required = false) String vendorName,@ApiParam(value = "供应商编码", required = false) String vendorCode,
			@ApiParam(value = "page", required = true) int page,@ApiParam(value = "size", required = true) int size);
	
	
	/*************************************供应商信息查询相关*******************************************/
	/**
	 * 查询【供应商管理】基本信息
	 * @param partyId
	 * @return
	 * @since 2017年8月10日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "查询【供应商管理】基本信息 ", notes = "供应商基本信息 ", response = VendorInfoVo.class)
	public  VendorInfoVo vendorInfo(@ApiParam(value = "供应商partyId", required = true) String partyId) throws BusinessException;
	
	/**
	 * 查询【供应商管理】产品线
	 * @param partyId
	 * @return
	 * @since 2017年8月10日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "查询【供应商管理】产品线 ", notes = "产品线信息列表 ", response = PartyProductLine.class,responseContainer = "List")
	public  List<PartyProductLine> productLine(@ApiParam(value = "供应商partyId", required = true) String partyId,@ApiParam(value = "代理线类型", required = false) Type type);
	
	
	/**
	 * 查询【供应商管理】信用
	 * @param partyId
	 * @return
	 * @since 2017年8月10日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "查询【供应商管理】信用 ", notes = "信用 ", response = VendorCreditVo.class)
	public  VendorCreditVo vendorCredit(@ApiParam(value = "供应商partyId", required = true) String partyId);
	
	/**
	 * 查询【供应商管理】信用
	 * @param partyId
	 * @return
	 * @since 2017年8月10日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "查询【供应商管理】销售基本信息 ", notes = "销售基本信息 ", response = VendorSaleInfoVo.class)
	public  VendorSaleInfoVo vendorSaleInfo(@ApiParam(value = "供应商partyId", required = true) String partyId) throws BusinessException;
	
	/**
	 * 查询分销商的父子关系
	 * @param entId
	 * @param corporationId
	 * @param partyType
	 * @return
	 * @since 2017年8月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询分销商的父子关系", notes = "查询分销商的父子关系", response = PartyRelationship.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyRelationship getParentRelationInfo(@ApiParam(value = "sonId", required = true) String sonId,@ApiParam(value = "关系类型", required = true) PartyRelationshipTypeEnum relationshipType);
	
	
	/**
	 * 查询【分销商/供应商】产品线品牌去掉重复的数据
	 * @param partyId
	 * @return
	 * @since 2017年9月27日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询【供应商管理】产品线去掉重复的数据", notes = "产品线品牌信息列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyProductLineVo> onlyProductLine(@ApiParam(value="分销商/供应商partyId",required=true)String partyId,@ApiParam(value = "代理线类型", required = false) Type type);
	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据制造商id,查询所有分销商过滤重复", notes = "产品线信息列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyProductLineVo> listSupplierByBrandId(@ApiParam(value="制造商Id",required=true)String brandId);
	/**
	 * 根据制造商id,查询所有分类过滤重复
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据制造商id,查询所有分类过滤重复", notes = "产品线信息列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyProductLineVo> listCategoryByBrandId(@ApiParam(value="制造商Id",required=true)String brandId);

	
	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询【分销商/供应商】产品线类别去掉重复的数据", notes = "产品线类别信息列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyProductLineModel> onlyProductLineCategoryList(@ApiParam(value="分销商/供应商partyId",required=true) String partyId);
	
	

	/**
	 * 查询供应商列表
	 * 
	 * @author zr.helinmei@yikuyi.com
	 * @param partyGroup
	 * @return
	 */
	@ApiOperation(value = "查询供应商列表，交期准确率数据", notes = "查询供应商列表，交期准确率数据", response = PartyVo.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PageInfo<SupplierVo> getPartySupplierList(@ApiParam(value = "供应商名称", required = false) String vendorName,@ApiParam(value = "排序", required = false) String orderSq,@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "10") int size);
	
	/**
	 * 新增/编辑/删除交期正确率
	 * 
	 * @param partyGroup
	 * @return 
	 */
	@ApiOperation(value = "新增/编辑/删除交期正确率", notes = "新增/编辑/删除交期正确率", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void updateLeadTime(@ApiParam(value = "供应商", required = true) PartyGroup partyGroup);

	/**
	 * 定时波动交期准确率
	 * 
	 * @param 
	 * @return 
	 */
	@ApiOperation(value = "定时波动交期准确率", notes = "定时波动交期准确率", response = Party.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void supplierLeadTimejob()throws BusinessException;

	/**
	 * 根据供应商id集合批量查询供应商信息
	 * @param 
	 * @since 2017年12月13日
	 * @author zr.helinmeig@yikuyi.com
	 */
	@ApiOperation(value = "根据供应商id集合批量查询供应商信息", notes = "根据供应商id集合批量查询供应商信息", response = List.class, responseContainer="List")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public  List<VendorInfoVo> vendorBatchList(@ApiParam(value = "ids", required = true) List<String> ids);
	
	/**
	 * 查询mov设置产品线品牌信息
	 * @param partyId
	 * @return
	 * @since 2018年1月12日
	 * @author tb.lijing@yikuyi.com
	 */
/*	@ApiOperation(value = "查询【供应商管理】产品线去掉重复的数据", notes = "产品线品牌信息列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<?> findProductLineBrand(@ApiParam(value="分销商/供应商partyId",required=true)String partyId);*/

}