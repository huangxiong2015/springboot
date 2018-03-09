package com.yikuyi.party.register.api;


import com.yikuyi.party.contact.vo.UserVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author zr.aoxianbing@yikuyi.com
 *
 */
public interface IRegisterResource {
	
	/**
	 * 个人注册保存
	 * @param userVo
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "个人注册保存", notes = "个人注册保存", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public UserVo save(@ApiParam(value = "userVo", required = true)UserVo userVo) throws BusinessException;

	/**
	 * 企业注册保存
	 * @param userVo
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "企业注册保存", notes = "企业注册保存", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public UserVo saveEnt(@ApiParam(value = "userVo", required = true)UserVo userVo)throws BusinessException;
	
	/**
	 * 重新发送注册邮件
	 * @param userVo
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "重新发送注册邮件", notes = "重新发送注册邮件", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String reSend(@ApiParam(value = "userVo", required = true)UserVo userVo);
	/**
	 * 根据账号创建子账号(子账号设置密码)
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据账号创建子账号(子账号设置密码)", notes = "根据账号创建子账号(子账号设置密码)", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String saveAccout(@ApiParam(value = "userVo", required = true)UserVo userVo);
	
	/**
	 * 加入主账号
	 * @param ids
	 * @return
	 * @since 2017年5月4日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "加入主账号", notes = "加入主账号", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = String.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) })
	public String joinMainAccount(@ApiParam(value = "企业id", required = true) String entId,
			@ApiParam(value = "账号", required = true) String account,
			@ApiParam(value = "申请id", required = true)String applyId);
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "生成登陆账号（根据账号）", notes = "生成登陆账号（根据账号）", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void upgrade(@ApiParam(value="用户id",required=true)String partyId, 
			@ApiParam(value="账号",required=true)String account) throws BusinessException;
}
