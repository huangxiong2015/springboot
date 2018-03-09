package com.yikuyi.product.rule.delivery;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 实时查询交期信息相关的服务
 * @author zr.wenjiao@yikuyi.com
 * @version 2.0.0
 */
@FunctionalInterface
public interface ILeadTimeResourceV2 {
	
	/**
	 * 实时查询交期
	 * @param info
	 * @return
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "实时查询交期", notes = "实时查询交期", response = ProductLeadTimeVo.class,responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在商品信息的交期策略", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<ProductLeadTimeVo> getProductLeadTime(@ApiParam(value = "交期模板信息", required = true) List<ProductLeadTimeVo> info);

}
