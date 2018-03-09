package com.yikuyi.product.specialoffer;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.specialoffer.model.SpecialOfferRule;
import com.yikuyi.specialoffer.model.SpecialOfferRule.RuleType;
import com.yikuyi.specialoffer.vo.SpecialOfferRuleVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 专属特价规则
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface ISpecialOfferRuleResource {
	
	
	/**
	 * 专属特价列表信息
	 * @param vendorId
	 * @param ruleType
	 * @param createdDateStart
	 * @param createDateEnd
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "专属特价列表信息", notes = "专属特价列表信息", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<SpecialOfferRuleVo> list(@ApiParam(value="供应商id", required=false) String vendorId,
			@ApiParam(value="规则类型", required=false) RuleType ruleType,
			@ApiParam(value = "开始创建时间", required = false) String createdDateStart,
			@ApiParam(value = "结束创建时间", required = false) String createDateEnd,			
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);
	
	/**
	 * 查询规则详情
	 * @param id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询规则详情", notes = "查询规则详情", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public SpecialOfferRuleVo getSpecialOfferRuleVo(@ApiParam(value="规则id", required=true) String id);
	
	/**
	 * 删除专属特价规则
	 * @param ruleType
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "删除专属特价规则", notes = "删除专属特价规则", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void deleteRuleById(@ApiParam(value="规则id", required=true) String id,
			@ApiParam(value="供应商id", required=true) String vendorId) throws BusinessException;
	
	/**
	 * 添加专属特价规则相关信息
	 * @param ruleId
	 * @param specialOfferRule
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "添加专属特价规则相关信息", notes = "添加专属特价相关规则", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void addSpecialOfferRule(@ApiParam(value="规则id", required=true) String ruleId,
			@RequestBody(required=true) SpecialOfferRule specialOfferRule) throws BusinessException;
	
	
	/**
	 * 生成规则id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "生成规则id", notes = "生成规则id", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String getRuleId();
	
	/**
	 * 编辑专属特价规则
	 * @param specialOfferRule
	 * @return
	 * @since 2017年12月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "编辑专属特价规则", notes = "编辑专属特价规则", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updateSpecialOfferRule(@RequestBody(required=true) SpecialOfferRule specialOfferRule) throws BusinessException;
	
	
	/**
	 * 将正式商品表中的数据迁移到草稿表中
	 * @param ruleId
	 * @since 2017年12月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "将正式商品表中的数据迁移到草稿表中", notes = "将正式商品表中的数据迁移到草稿表中", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void copyProductToDraft(@ApiParam(value="规则id", required=true) String ruleId) throws BusinessException;
	
}
