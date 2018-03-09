package com.yikuyi.product.material;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.document.vo.DocumentVo;
import com.yikuyi.material.vo.MaterialVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 商品导入/上传入口
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public interface IMaterialResource {

	/**
	 * 文件导入
	 * 
	 * @param fileName
	 * @param oriFileName
	 * @since 2016年12月7日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "商品文件上传通知", notes = "商品文件上传通知", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void fileUploadNotification(MaterialVo materialVo);
	
	/**
	 * future文件导入job
	 * 
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "future文件导入job通知", notes = "ftp文件下载,自动导入系统job通知", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void futureFileImportJob(@ApiParam(value = "vendorId", required = true) String vendorId,@ApiParam(value = "regionId") String regionId,@ApiParam(value ="ftpFileName") String ftpFileName);
	
	
	@ApiOperation(value = "定时清理过期的自动集成的供应商库存数据", notes = "定时清理过期的自动集成的供应商库存数据", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void clearOutOfDateProducts4AutoUpload(@ApiParam(value = "vendorId", required = true) String vendorId,@ApiParam(value = "storeId") String storeId);
	
	
	@ApiOperation(value = "同步索引通知", notes = "同步索引通知")
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void syncElasticsearchProductUpdate(@ApiParam(value = "批量同步搜索引擎数据", required = true) MaterialVo materialVo);
	
	/**
	 * 同步搜索引擎数据(全量)
	 * 
	 * @param documents
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "全量同步索引通知", notes = "全量同步索引通知")
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public List<String> syncElasticsearchProductAll(@ApiParam(value = "要同步的供应商id", required = false) String vendorId);
	
	/**
	 * 停止文档导入
	 * 
	 * @param documents
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "取消文档导入通知", notes = "取消文档导入通知")
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public void cancelImport(@ApiParam(value = "文档ID", required = true) String docId);
	
	/**
	 * 标准物料检测
	 * 
	 * @param fileUrl
	 * @since 2016年12月26日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "标准物料检测", notes = "标准物料检测", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public void materialDetection(HttpServletRequest request, HttpServletResponse res,
			@ApiParam(value = "阿里云文件路径", required = true) String fileUrl);
	
	/**
	 * 错误历史下载
	 * @param id
	 * @since 2017年2月20日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "错误历史下载", notes = "错误历史下载", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public void logHistoryDownload(HttpServletRequest request, HttpServletResponse res,
			@ApiParam(value = "文档ID", required = true) String id);
	
	/**
	 * 上传历史查看
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年1月5日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "上传历史查看", notes = "上传历史查看2", response = DocumentVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<DocumentVo> logHistory(@ApiParam(value = "文档类型", required = true) DocumentType docTypeId,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);
}