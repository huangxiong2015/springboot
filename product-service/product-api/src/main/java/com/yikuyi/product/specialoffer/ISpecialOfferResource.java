package com.yikuyi.product.specialoffer;

import com.yikuyi.specialoffer.model.SpecialOffer;
import com.yikuyi.specialoffer.model.SpecialOffer.RuleStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface ISpecialOfferResource {
	
	
	/**
	 * 专属特价详情
	 * @param id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "专属特价详情", notes = "专属特价详情", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public SpecialOffer getSpecialOffer(@ApiParam(value="供应商id", required=false) String id);
	
	/**
	 * 编辑文案内容
	 * @param ruleType
	 * @param createdDateStart
	 * @param createDateEnd
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "编辑文案内容", notes = "编辑文案内容", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String editRuleText(@ApiParam(value="供应商id", required=false) String id,
			@ApiParam(value = "文案内容", required = false) String ruleText);
	
	/**
	 * 编辑状态
	 * @param id
	 * @param ruleText
	 * @return
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "编辑状态", notes = "编辑状态", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String editRuleStatus(@ApiParam(value="供应商id", required=false) String id,
			@ApiParam(value = "状态", required = false) RuleStatus ruleStatus,
			@ApiParam(value = "状态", required = false) String statusName);
}
