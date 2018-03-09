package com.yikuyi.product.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 询价项目导入模板
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
public class ExcelTemplate {
	/**
	 * excel模板
	 * @author tongkun@yikuyi.com
	 * @version 1.0.0
	 */
	public enum Template{
		EXPORT_SALE_PRODUCT_TEMPLATE("*ManufacturerName（制造商）,*MPN（型号）,SPN（供应商型号）,*STOCK QTY（库存）,*MOQ（最小起订量）,UNIT（单位）,DateCode（批号）,SPQ（标准包装数量）,MOV（最小订单金额）,Packaging（包装）,*Currency（币种）,Description（描述）,*QtyBreak1（数量区间1）,*PriceBreak1（价格区间1）,QtyBreak2（数量区间2）,PriceBreak2（价格区间2）,QtyBreak3（数量区间3）,PriceBreak3（价格区间3）,QtyBreak4（数量区间4）,PriceBreak4（价格区间4）,QtyBreak5（数量区间5）,PriceBreak5（价格区间5）"
				+ ",QtyBreak6（数量区间6）,PriceBreak6（价格区间6）,QtyBreak7（数量区间7）,PriceBreak7（价格区间7）,QtyBreak8（数量区间8）,PriceBreak8（价格区间8）,QtyBreak9（数量区间9）,PriceBreak9（价格区间9）"
				+ ",QtyBreak10（数量区间10）,PriceBreak10（价格区间10）,ROHS（Y/N）,MinLeadTime-ML（内地最小交期/天数）,MaxLeadTime-ML（内地最大交期/天数）,MinLeadTime-HK（香港最小交期/天数）,MaxLeadTime-HK（香港最大交期/天数）,FactoryLeadTime-ML（内地最小工厂交期/周）,FactoryLeadTime-ML（内地最大工厂交期/周）,FactoryLeadTime-HK（香港最小工厂交期/周）,FactoryLeadTime-HK（香港最大工厂交期/周）,上传时间,数据类型,失效日期,系统支持最多导出20W条数据"),
		
		EXPORT_PRODUCT_STAND_TEMPLATE("大类,小类,次小类,品牌,型号,描述,RoHs,状态,限制物料,更新时间");
		
		private static final Map<String, Template> mappings = new HashMap<>(1);
		
		static {
			for (Template template : values()) {
				mappings.put(String.valueOf(template.getValue()), template);
			}
		}
		
		private final String template;

		Template(String value) {
			this.template = value;
		}

		public String getValue() {
			return template;
		}
		
		public static Template fromValue(String value) {
			return mappings.get(value);
		}
	}
	
	
}
