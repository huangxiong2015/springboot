package com.yikuyi.product;

import com.yikuyi.product.resource.ActivityClient;
import com.yikuyi.product.resource.ActivityProductClient;
import com.yikuyi.product.resource.BrandClient;
import com.yikuyi.product.resource.CategoryClient;
import com.yikuyi.product.resource.LogisticsFeeClient;
import com.yikuyi.product.resource.MovQueryClient;
import com.yikuyi.product.resource.MovQueryClientV2;
import com.yikuyi.product.resource.PriceQuery2Client;
import com.yikuyi.product.resource.PriceQueryClient;
import com.yikuyi.product.resource.PriceRulesClient;
import com.yikuyi.product.resource.ProductClient;
import com.yikuyi.product.resource.ProductStandClient;
import com.yikuyi.product.resource.PromotionClient;
import com.yikuyi.product.resource.PromotionModuleClient;
import com.yikuyi.product.resource.SearchClient;

public class ProductClientBuilder extends BaseClientBuilder {
	public ProductClientBuilder(String baseUrl) {
		this.setBaseUrl(baseUrl);
	}

	/**
	 * @return
	 */
	public BrandClient brandResource(){
		return super.builder(BrandClient.class);
	}
	
	/**
	 * @return
	 */
	public ProductClient productResource(){
		return super.builder(ProductClient.class);
	}
	
	public ActivityProductClient activityProductResource(){
		return super.builder(ActivityProductClient.class);
	}
	
	public LogisticsFeeClient logisticsFeeResource(){
		return super.builder(LogisticsFeeClient.class);
	}
	
	
	public PriceQueryClient priceQueryResource(){
		return super.builder(PriceQueryClient.class);
	}
	
	public ProductStandClient productStandResource(){
		return super.builder(ProductStandClient.class);
	}
	
	public CategoryClient categoryResource(){
		return super.builder(CategoryClient.class);
	}
		
	public PriceQuery2Client priceQuery2Resource(){
		return super.builder(PriceQuery2Client.class);
	}
	public ActivityClient activityResource(){
		return super.builder(ActivityClient.class);
	}
	public SearchClient searchResource(){
		return super.builder(SearchClient.class);
	}
	
	public PriceRulesClient priceRulesResource(){
		return super.builder(PriceRulesClient.class);
		
	}
	public MovQueryClient movQueryResource(){
		return super.builder(MovQueryClient.class);
		
	}
	public MovQueryClientV2 movQueryResourceV2(){
		return super.builder(MovQueryClientV2.class);
		
	}
	public PromotionModuleClient promotionModuleResource(){
		return super.builder(PromotionModuleClient.class);
		
	}
	
	public PromotionClient promotionResource(){
		return super.builder(PromotionClient.class);
		
	}
}
