package com.yikuyi.product.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.vo.SkuDeleteVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.document.bll.SyncElasticsearchProductManager;
import com.yikuyi.product.essync.bll.EssyncManager;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * 文件上传成功,任务发送过程
 * 
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SyncElasticsearchProductReceiveListener implements MsgReceiveListener {

	private static final Logger logger = LoggerFactory.getLogger(SyncElasticsearchProductReceiveListener.class);

	@Autowired
	private SyncElasticsearchProductManager syncElasticsearchProductManager;

	@Autowired
	private EssyncManager essyncManager;

	@Autowired
	private InventorySearchManager inventorySearchManager;

	@Override
	public void onMessage(Serializable arg) {
		long time1 = System.currentTimeMillis();
		if (null == arg) {
			logger.error("SyncElasticsearchProductReceiveListener arg is null");
			return;
		}
		try {
			if (arg instanceof SkuDeleteVo) {
				// 通过查询条件异步通知搜索引擎更新
				SkuDeleteVo deleteVo = (SkuDeleteVo) arg;
				logger.info("异步通知搜索引擎删除的查询条件:{}", JSON.toJSONString(deleteVo));
				inventorySearchManager.delete(deleteVo);
			} else if (arg instanceof MaterialVo) {
				MaterialVo materialVo = (MaterialVo) arg;

				// 文件上传时候的同步数据
				if (MaterialVoType.FILE_UPLOAD == materialVo.getType()) {
					syncElasticsearchProductManager.syncElasticsearchProduct(materialVo);
				} else if (MaterialVoType.UPDATE_DATA == materialVo.getType()) {// 文件更新接口
					syncElasticsearchProductManager.syncElasticsearchProductUpdate(materialVo);
				} else if (MaterialVoType.BASIS_FILE_UPLOAD == materialVo.getType()) {
					materialVo.setMsg(null);
					syncElasticsearchProductManager.syncElasticsearchProduct(materialVo);
				} else if (MaterialVoType.PRODUCT_STAND_NO == materialVo.getType()) {
					//essyncManager.syncElasticsearchNo(materialVo);
				}
				logger.info("搜索引擎同步:Type:{} docId:{} 大小:{} 耗时:{}", materialVo.getType(), materialVo.getDocId(), materialVo.getSize(), System.currentTimeMillis() - time1);
			} else {
				logger.error("es同步mq，消息体不是可以识别的类型:{}", arg);
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
}