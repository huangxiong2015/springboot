package com.yikuyi.product.document;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
/**
 * 分类接口
 * @author zr.wanghong
 */
public interface IDocumentResource {		
	
	@ApiOperation(value = "根据时间删除文件上传的历史记录", notes = "根据时间删除文件上传的历史记录", response = Void.class)
	@RequestMapping(method=RequestMethod.POST)
	public void deleteHistoryLog();
}