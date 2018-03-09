package com.yikuyi.product.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品价格梯度
 * @author zr.wujiajun
 *
 */
public class ProductPriceLevel extends AbstractEntity{
	
	private static final long serialVersionUID = -6244999888335139745L;
	@ApiModelProperty(value="封装,为空表示散装")
	private String packaging;
	@ApiModelProperty(value="价格")
	private String price;
	@ApiModelProperty(value="数量")
	private Long breakQuantity;
	public String getPackaging() {
		return packaging;
	}
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Long getBreakQuantity() {
		return breakQuantity;
	}
	public void setBreakQuantity(Long breakQuantity) {
		this.breakQuantity = breakQuantity;
	}	
	/*
   @Override  
   public int compareTo(ProductPriceLevel productPriceLevel) {  
       int i = Integer.parseInt(this.getBreakQuantity()) - Integer.parseInt(productPriceLevel.getBreakQuantity());//按照数量
       if(i == 0){  
         return (int) ( Double.parseDouble(this.price)-Double.parseDouble(productPriceLevel.getPrice()));//按照价格排序
       }  
      return i;  
   }  */

}
