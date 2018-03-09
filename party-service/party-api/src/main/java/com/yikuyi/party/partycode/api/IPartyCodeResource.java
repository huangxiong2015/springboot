package com.yikuyi.party.partycode.api;

import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * PARTY_CODE客户编码接口
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public interface IPartyCodeResource {

	/**
	 * 验证YKY客户编码是否已经存在
	 * 
	 * @param partyCode
	 * @return
	 * @throws BusinessException
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "获取认证企业易库易编码", notes = "获取认证企业,可以用来检验是否已经存在", response = String.class)
	public String getPartyCode(@ApiParam(value = "YKY客户编码", required = true) String partyCode);

	/**
	 * 根据编码前缀和企业ID生成对应的客户编码
	 * 
	 * @param vo
	 * @return
	 * @since 2017年5月9日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "根据编码前缀和企业ID生成对应的客户编码", notes = "根据编码前缀(codePrefix)和企业ID(partyId)生成对应的客户编码", response = String.class)
	public String savePartyCode(@ApiParam(value = "partyId", required = true) String partyId, @ApiParam(value = "YKY客户编码", required = true) String partyCode) throws BusinessException;

}