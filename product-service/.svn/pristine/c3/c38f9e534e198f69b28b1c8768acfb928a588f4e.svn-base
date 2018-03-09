package com.yikuyi.product.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.v2.category.model.SpuCategory;
import com.yikuyi.v2.leadtime.model.LeadTime;
import com.yikuyi.v2.leadtime.model.LeadTimeCollection;
import com.yikuyi.v2.product.model.SkuPriceLevel;
import com.yikuyi.v2.product.model.SpuAttachment;
import com.yikuyi.v2.product.model.SpuDocument;
import com.yikuyi.v2.product.model.SpuFlags;
import com.yikuyi.v2.product.model.SpuImage;
import com.yikuyi.v2.product.model.SpuParameter;
import com.yikuyi.v2.product.rawdata.ProductPipelineVo;
import com.yikuyi.v2.product.rawdata.ProductPrice;
import com.yikuyi.v2.product.rawdata.Stock;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 识别传入对象，如果是创建sku则要额外将消息转到新版sku中
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
@Service
public class CreateProductTransferListener implements MsgReceiveListener {
	private static final Logger logger = LoggerFactory.getLogger(CreateProductTransferListener.class);

	@Autowired
	private MsgSender msgSender;
	
	/**
	 * 老版本的创建product的mq
	 */
	@Value("${mqProduceConfig.createProductSubOld.topicName}")
	private String createProductTopicName;

	/**
	 * 新版sku流水线mq
	 */
	@Value("${mqProduceConfig.skuPipline.topicName}")
	private String skuPiplineTopicName;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onMessage(Serializable arg) {
		logger.info("SkuPipelineTransferListener 接收到消息开始处理");
		if(arg == null){
			logger.error("SkuPipelineTransferListener 接收到一条值为null的消息，无法知道来源，请检查发送端");
		}
		else if(arg instanceof List){
			if(((List) arg).isEmpty()){
				logger.error("SkuPipelineTransferListener 接收到一条size为0的消息，无法知道来源，请检查发送端");
				return;
			}
			Object obj = ((List) arg).get(0);
			//如果是product对象则转换为sku转入新的消息队列
			if(obj instanceof RawData){
				((RawData)obj).setId(String.valueOf(IdGen.getInstance().nextId()));
				ProductPipelineVo pp = transFerToProductPiplineVo((List) arg);
				msgSender.sendMsg(skuPiplineTopicName, pp, null);//转为新版mq
				//如果是爬虫过来则不再执行原有的逻辑
				if(ProductPipelineVo.SOURCE.CRAWL.getValue().equalsIgnoreCase(((RawData)obj).getSource())){
					logger.info("创建商品走新的逻辑，旧逻辑不再消费");
					return;
				}
			}
		}
		msgSender.sendMsg(createProductTopicName, arg, null);//转为老版的mq
	}
	
	/**
	 * 转换List为pipeline所需vo
	 * @param arg 传入参数
	 * @return
	 * @since 2017年12月27日
	 * @author tongkun@yikuyi.com
	 */
	public ProductPipelineVo transFerToProductPiplineVo(List arg){
		ProductPipelineVo pp = new ProductPipelineVo();
		List<com.yikuyi.v2.product.rawdata.RawData> datas = new ArrayList<>();
		String processId = null;
		for(Object obj:arg){
			RawData oriData = (RawData)obj;
			com.yikuyi.v2.product.rawdata.RawData newData = new com.yikuyi.v2.product.rawdata.RawData();
			
			if(processId==null&&oriData.getProcessId()!=null){
				processId = oriData.getProcessId();
			}
			//基础属性
			newData.setId(oriData.getId());
			newData.setCountryCode(oriData.getCountryCode());
			newData.setDescription(oriData.getDescription());
			newData.setMfr(oriData.getManufacturer());
			newData.setMpn(oriData.getManufacturerPartNumber());
			newData.setMoq(oriData.getMinimumQuantity());
			newData.setSpq(oriData.getSpq());
			newData.setPackagingUnit(oriData.getPackagingUnit());
			newData.setMov(oriData.getMov());
			newData.setPackaging(oriData.getPackaging());
			newData.setPartStatus(oriData.getPartStatus());
			newData.setSkuId(oriData.getSkuId());
			newData.setUnit(oriData.getUnit());
			newData.setVendorDetailsLink(oriData.getVendorDetailsLink());
			newData.setVendorId(oriData.getVendorId());
			newData.setProcessId(oriData.getProcessId());
			newData.setVendorName(oriData.getVendorName());
			newData.setRegion(oriData.getRegion());
			newData.setCurrencyCode(oriData.getCurrencyCode());
			newData.setLineNo(oriData.getLineNo());
			newData.setSheetIndex(oriData.getSheetIndex());
			newData.setFrom(oriData.getFrom());
			newData.setRemark(oriData.getRemark());
			newData.setDateCode(oriData.getDateCode());
			newData.setCostType(oriData.getCostType());
			newData.setExpiryDate(oriData.getExpiryDate());
			//flag集合
			SpuFlags flags = new SpuFlags();
			if(oriData.getRohs()!=null&&("1".equals(oriData.getRohs())||"true".equals(oriData.getRohs()))){
				flags.setRohs(true);
			}
			if(oriData.getRestrictMaterialType()!=null&&oriData.getRestrictMaterialType().indexOf('T')>=0){
				flags.setTax(true);
			}
			if(oriData.getRestrictMaterialType()!=null&&oriData.getRestrictMaterialType().indexOf('F')>=0){
				flags.setForbid(true);
			}
			if(oriData.getRestrictMaterialType()!=null&&oriData.getRestrictMaterialType().indexOf('I')>=0){
				flags.setInspection(true);
			}
			newData.setFlags(flags);
			//文档集合
			List<SpuDocument> docs = new ArrayList<>();
			if(oriData.getDocuments()!=null){
				for(ProductDocument oriDoc:oriData.getDocuments()){
					SpuDocument newDoc = new SpuDocument();
					newDoc.setDescription(oriDoc.getDescription());
					newDoc.setName(oriDoc.getName());
					newDoc.setType(oriDoc.getType());
					newDoc.setUrl(oriDoc.getUrl());
					List<SpuAttachment> atts = new ArrayList<>();
					if(oriDoc.getAttaches()!=null){
						for(ProductAttachment oriAttr:oriDoc.getAttaches()){
							SpuAttachment newAtt = new SpuAttachment();
							newAtt.setName(oriDoc.getName());
							newAtt.setUrl(oriDoc.getUrl());
							atts.add(newAtt);
						}
					}
					newDoc.setAttaches(atts);
					docs.add(newDoc);
				}
			}
			newData.setDocuments(docs);
			//图片集合
			SpuImage image = new SpuImage();
			if(oriData.getImages()!=null){
				for(ProductImage oriImg:oriData.getImages()){
					if("thumbnail".equals(oriImg.getType())){
						image.setThumbnail(oriImg.getUrl());
					}else if("standard".equals(oriImg.getType())){
						image.setStandard(oriImg.getUrl());
					}else if("large".equals(oriImg.getType())){
						image.setLarge(oriImg.getUrl());
					}
				}
			}
			newData.setImage(image);
			//参数集合
			List<SpuParameter> params = new ArrayList<>();
			if(oriData.getParameters()!=null){
				for(ProductParameter oriParam:oriData.getParameters()){
					SpuParameter param = new SpuParameter();
					param.setCode(oriParam.getCode());
					param.setName(oriParam.getName());
					param.setValue(oriParam.getValue());
					params.add(param);
				}
			}
			newData.setParameters(params);
			//价格阶梯
			List<ProductPrice> prices = new ArrayList<>();
			if(oriData.getPrices()!=null){
				for(com.yikuyi.product.model.ProductPrice oriPrice:oriData.getPrices()){
					try{
						ProductPrice price = new ProductPrice();
						price.setCurrencyCode(oriPrice.getCurrencyCode());
						price.setUnitPrice(Double.valueOf(oriPrice.getUnitPrice()));
						List<SkuPriceLevel> levels = new ArrayList<>();
						if(oriPrice.getPriceLevels()!=null){
							for(ProductPriceLevel oriLevel:oriPrice.getPriceLevels()){
								SkuPriceLevel level = new SkuPriceLevel();
								level.setBreakQuantity(oriLevel.getBreakQuantity());
								level.setPrice(Double.valueOf(oriLevel.getPrice()));
								levels.add(level);
							}
						}
						price.setPriceLevels(levels);
						prices.add(price);
					}catch(Exception e){
						logger.error("CreateProductTransferListener 价格不是有效的数字",e);
					}
				}
			}
			newData.setPrices(prices);
			//仓库对象
			List<Stock> stocks = new ArrayList<>();
			if(oriData.getStocks()!=null){
				for(com.yikuyi.product.model.Stock oriStock:oriData.getStocks()){
					Stock stock = new Stock();
					stock.setFacilityId(oriStock.getSourceId());
					stock.setFacilityName(oriStock.getSource());
					stock.setQty(oriStock.getQuantity());
					stocks.add(stock);
				}
			}
			newData.setStocks(stocks);
			//分类
			List<SpuCategory> vendorCategories = new ArrayList<>();
			if(oriData.getVendorCategories()!=null){
				for(ProductCategory oriCate:oriData.getVendorCategories()){
					SpuCategory cate = new SpuCategory();
					cate.setCateName(oriCate.getName());
					cate.setCateLevel(oriCate.getLevel());
					vendorCategories.add(cate);
				}
			}
			newData.setVendorCategories(vendorCategories);
			//交期
			LeadTimeCollection leadTimeCol = new LeadTimeCollection();
			LeadTime cnlt = new LeadTime();
			LeadTime hklt = new LeadTime();
			cnlt.setStockMin(oriData.getMinLeadTimeML()==null?null:Integer.parseInt(oriData.getMinLeadTimeML()));
			cnlt.setStockMax(oriData.getMaxLeadTimeML()==null?null:Integer.parseInt(oriData.getMaxLeadTimeML()));
			cnlt.setFactoryMin(oriData.getMinFactoryLeadTimeML()==null?null:Integer.parseInt(oriData.getMinFactoryLeadTimeML()));
			cnlt.setFactoryMax(oriData.getMaxFactoryLeadTimeML()==null?null:Integer.parseInt(oriData.getMaxFactoryLeadTimeML()));
			hklt.setStockMin(oriData.getMinLeadTimeHK()==null?null:Integer.parseInt(oriData.getMinLeadTimeHK()));
			hklt.setStockMax(oriData.getMaxLeadTimeHK()==null?null:Integer.parseInt(oriData.getMaxLeadTimeHK()));
			hklt.setFactoryMin(oriData.getMinFactoryLeadTimeHK()==null?null:Integer.parseInt(oriData.getMinFactoryLeadTimeHK()));
			hklt.setFactoryMax(oriData.getMaxFactoryLeadTimeHK()==null?null:Integer.parseInt(oriData.getMaxFactoryLeadTimeHK()));
			leadTimeCol.setCn(cnlt);
			leadTimeCol.setHk(hklt);
			newData.setLeadTime(leadTimeCol);
			//加入数据集合
			datas.add(newData);
		}
		pp.setProcessId(processId);
		pp.setDatas(datas);
		pp.setSource(ProductPipelineVo.SOURCE.OLD_LISTENER.getValue());
		return pp;
	}
}
