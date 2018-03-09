package com.yikuyi.product.material;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.template.model.ProductTemplate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 基础物料导入/上传入口
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public interface IBasicMaterialResource {

	/**
	 * 文件导入通知
	 * @param materialVo
	 * @since 2017年2月22日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "基础商品文件上传通知", notes = "基础商品文件上传通知", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void fileUploadNotification(MaterialVo materialVo);
	
	/**
	 * 解析导入文件
	 * @param materialVo
	 * @since 2017年2月22日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "阿里云文件解析通知", notes = "阿里云文件解析通知", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void parseImportFile(MaterialVo materialVo);
	
	@ApiOperation(value = "根据供应商ID查询上传模板", notes = "作者：王洪<br/>根据供应商ID查询上传模板", response = ProductTemplate.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public ProductTemplate findTemplatebyId(@ApiParam("供应商ID") String vendorId,@ApiParam("仓库ID") String sourceId);
}