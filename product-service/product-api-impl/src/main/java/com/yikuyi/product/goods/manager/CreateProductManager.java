package com.yikuyi.product.goods.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.listener.QuickCreateProductReceiveListener;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.mqservice.sender.MsgSender;
/**
 * 商品
 * 
 * @author zr.wujiajun
 * @2016年12月7日
 */
@Service
public class CreateProductManager {
	private static final Logger logger = LoggerFactory.getLogger(QuickCreateProductReceiveListener.class);

	@Autowired
	private ProductStandManager productStandManager;

	@Autowired
	private ProductManager productManager;
	
	@Value("${mqProduceConfig.syncElasticsearchProduct.topicName}")
	private String syncElasticsearchProductTopicName;
	
	@Autowired
	private MsgSender msgSender;

	public void onMessage(Serializable arg) {
		long s = System.currentTimeMillis();
		logger.debug("CreateProductReceiveListener,arg为：{}", arg);
		String message = null;
		try {
			// 如果传入的是productStand对象则表示更新spu
			if (arg instanceof ProductStand) {
				ProductStand prod = (ProductStand) arg;
				logger.info("更新产品：" + prod.getId() + " 品牌id:" + prod.getManufacturerId());
				productStandManager.updateProductStand(prod);
			}
			// 如果传入的是product对象则表示更新sku
			else if (arg instanceof Product) {
				Product prod = (Product) arg;
				logger.info("更新商品：" + prod.getId() + " spuId:" + (prod.getSpu() == null ? null : prod.getSpu().getId()));
				productManager.updateProductInfo(prod);
			}
			// 如果是传入字符串类型，则表示从爬虫同步而来，用于创建或更新sku和spu数据
			else if (arg instanceof String) {
				doProcess((String) arg);
			}
			// 如果是传入ArrayList类型，则表示从文件上传而来，用于创建或更新sku和spu数据
			else if (arg instanceof ArrayList) {
				ArrayList<?> documentList = (ArrayList<?>) arg;
				if (CollectionUtils.isEmpty(documentList)) {
					message = "传入参数为空";
					return;
				}
				doProcess(documentList);
			} else {
				logger.error("未知的参数：" + arg.getClass());
			}
			message = "done";
		} catch (JsonParseException e) {
			logger.error("JsonParseException", e);
			// json格式错误不抛出异常，避免MQ重试，占用大部分的cpu
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException", e);
			// json格式错误不抛出异常，避免MQ重试，占用大部分的cpu
		} catch (IOException e) {
			logger.error("IOException", e);
			// json格式错误不抛出异常，避免MQ重试，占用大部分的cpu
		} finally {
			logger.info("创建商品完毕：消息:{},耗时:{}", message, System.currentTimeMillis() - s);
		}

	}

	/**
	 * @param dataStr
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @since 2017年4月26日
	 * @author liudian@yikuyi.com
	 */
	private void doProcess(String dataStr) throws IOException {
		if (StringUtils.isEmpty(dataStr)) {
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, RawData.class);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<RawData> datas = mapper.readValue(dataStr.getBytes(), javaType);
		doProcess(datas, MaterialVoType.UPDATE_DATA);
	}

	/**
	 * @param documentList
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @since 2017年4月26日
	 * @author liudian@yikuyi.com
	 */
	private void doProcess(ArrayList<?> documentList) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<RawData> datas = new ArrayList<>();
		List<ProductStand> stands = Lists.newArrayList();
		for (Object object : documentList) {
			if (object instanceof Document) {
				byte[] bts = mapper.writeValueAsBytes(object);
				RawData data = mapper.readValue(bts, RawData.class);
				datas.add(data);
			} else if (object instanceof RawData) {
				datas.add((RawData) object);
			} else if (object instanceof ProductStand) {
				stands.add((ProductStand) object);
			}
		}
		if(CollectionUtils.isNotEmpty(datas)){
			if (StringUtils.isEmpty(datas.get(0).getProcessId())) {
				doProcess(datas, MaterialVoType.UPDATE_DATA);
			} else {
				doProcess(datas, MaterialVoType.FILE_UPLOAD);
			}
		}
		// 更新产品sku
		this.doUpdateSkuProcess(stands);
		return;
	}

	/**
	 * 更新产品sku
	 * 
	 * @param stands
	 * @since 2017年9月4日
	 * @author injor.huang@yikuyi.com
	 */
	public void doUpdateSkuProcess(List<ProductStand> stands) {
		if (CollectionUtils.isEmpty(stands)) {
			return;
		}
		logger.info("更新产品sku,条数为：{}", stands.size());
		productManager.updateProductBySpus(stands);
	}

	/**
	 * @param datas
	 * @since 2017年4月26日
	 * @author liudian@yikuyi.com
	 */
	private void doProcess(List<RawData> datas, MaterialVoType type) {
		try {
			// 只有传入数据才会调用接口
			if (CollectionUtils.isNotEmpty(datas)) {
				//之前爬虫有对CantCreateStand这个字段做开关处理，现在不需要用，默认置为空
				datas.stream().forEach(d -> d.setCantCreateStand(null));
				// 没有传skuId则直接创建spu //传了skuId则创建更新sku
				if (StringUtils.isBlank(datas.get(0).getSkuId())) {
					productStandManager.createProductStand(datas);
				} else {
					productManager.createProduct(datas, type);
				}
			}
		} finally{
			if(MaterialVoType.FILE_UPLOAD.equals(type) && CollectionUtils.isNotEmpty(datas)){
				MaterialVo materialVo = new MaterialVo();
				materialVo.setType(type);
				materialVo.setDocId(datas.get(0).getProcessId());
				materialVo.setSize(datas.size());
				msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
			}
		}
	}
}