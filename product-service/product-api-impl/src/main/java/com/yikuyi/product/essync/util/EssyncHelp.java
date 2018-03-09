package com.yikuyi.product.essync.util;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.vo.ProductEsVo;
import com.yikuyi.product.vo.ProductEsVo.SortLevel;

public class EssyncHelp {

	// 按默认规则,增加排序字段
	public static void addSortLevel(ProductEsVo esVo) {
		// 默认
		Long qty = esVo.getQty();
		if (null != qty && qty != 0) {// 库存不为空,排序为1
			esVo.setSortQtyLevel(SortLevel.ONE.getValue());
		}
		List<ProductPrice> prices = esVo.getPrices();
		if (null != prices && !prices.isEmpty()) {// 价格不为空
			for (int s = 0; s < prices.size(); s++) {
				List<ProductPriceLevel> priceLevelList = prices.get(0).getPriceLevels();
				// 价格梯度不为空,并且单价不为0,排序2
				if (null != priceLevelList && !priceLevelList.isEmpty()) {
					esVo.setSortPriLevel(SortLevel.ONE.getValue());
				}
			}
		}
		// 包含图片,排序3
		ProductStand spu = esVo.getSpu();
		if (null == spu) {
			return;
		}
		List<ProductImage> imagerList = spu.getImages();
		esVo.setSortImaLevel(SortLevel.ONE.getValue());
		if (null == imagerList || imagerList.isEmpty()
				|| (imagerList.size() <= 1 && imagerList.get(0).getType().equalsIgnoreCase("thumbnail"))) {// 如果只有一个图片,并且图片是缩略图,,靠后排
			esVo.setSortImaLevel(SortLevel.TWO.getValue());
		} else {
			for (ProductImage pI : imagerList) {
				if (pI.getType().equalsIgnoreCase("thumbnail")
						&& "//media.digikey.com/photos/nophoto/pna_en.jpg".equals(pI.getUrl())) {// 如果缩略图是dijike图片,排在后面
					esVo.setSortImaLevel(SortLevel.TWO.getValue());
					break;
				}
			}
		}
	}

	public static void analysisDocToElasticsearc(ProductEsVo esVo) {
		esVo.setCreatedDate(null);
		esVo.setLeadTime(null);
		String saleControl = esVo.getSaleControl();
		if ("1".equals(saleControl)) {
			esVo.setStatus(0);
		}
		esVo.getSpu().setParameters(Collections.emptyList());
		esVo.setVendorCategories(Collections.emptyList());
		esVo.setActivityProductVo(null);
		esVo.setLastUpdateDate(new Date());
		esVo.setLastUpdateUser("system");
	}
}
