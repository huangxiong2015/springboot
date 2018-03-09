package com.yikuyi.product.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.basicmaterial.bll.BasicMaterialManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.goods.manager.ProductStandManager;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductStandRequest;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * 文件上传成功,任务发送过程
 * 
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
@Service
public class ParseImportFileReceiveListener implements MsgReceiveListener {

	private static final Logger logger = LoggerFactory.getLogger(ParseImportFileReceiveListener.class);

	@Autowired
	private BasicMaterialManager basicMaterialManager;

	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private ProductStandManager productStandManager;
	@Autowired
	private ProductManager productManager;

	@Override
	public void onMessage(Serializable arg) {
		try{
		if (null == arg) {
			logger.error("ParseImportFileReceiveListener arg is null");
			return;
		}
		logger.info("ParseImportFileReceiveListener start");
		
		if( arg instanceof ProductRequest){
			//销售中产品下载
			ProductRequest productRequest = (ProductRequest)arg;
			productManager.doExport(productRequest);
			
		}else if(arg instanceof ProductStandRequest){
			//物料下载
			ProductStandRequest  productStandRequest = (ProductStandRequest)arg;
			productStandManager.doExport(productStandRequest);
		}else if(arg instanceof MaterialVo){
			MaterialVo vo = (MaterialVo) arg;
			if (MaterialVoType.BASIS_FILE_UPLOAD.equals(vo.getType())) {
				basicMaterialManager.basicMaterialParseImportFile(vo);
			} else if (MaterialVoType.FILE_UPLOAD_JOB.equals(vo.getType())) {
				materialManager.processingJob(vo);
			} else {
				materialManager.importsParseImportFile(vo);
			}
			logger.info("ParseImportFileReceiveListener end");
		}
		}catch(Exception e){
			logger.error("ParseImportFileReceiveListener异常");
		}
		
	}
}