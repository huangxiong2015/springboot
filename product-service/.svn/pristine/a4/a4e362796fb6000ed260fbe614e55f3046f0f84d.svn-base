package com.yikuyi.product.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class ProductPageable implements Serializable, Pageable{
	private static final long serialVersionUID = -5714803906580620026L;
    // 当前页面条数  
    public static final Integer PAGESIZE = 100; 

    // 当前页  
    private Integer pagenumber = 1;  
    //排序方式
    private Sort sort;
    
    public ProductPageable(Integer pagenumber){
    	List<Order> orders = new ArrayList<Order>();
    	orders.add(new Order(Direction.DESC, "_id"));
    	sort = new Sort(orders);
    	this.pagenumber = pagenumber;
    }
    
	@Override
	public int getPageNumber() {
		return this.pagenumber;
	}

	@Override
	public int getPageSize() {
		return PAGESIZE;
	}

	@Override
	public int getOffset() {
		return (getPageNumber() - 1) * getPageSize(); 
	}

	@Override
	public Sort getSort() {
		return this.sort;
	}

	@Override
	public Pageable next() {
		return new ProductPageable(this.pagenumber+1);
	}

	@Override
	public Pageable previousOrFirst() {
		if(this.pagenumber==1)
			return this;
		else 
			return new ProductPageable(this.pagenumber-1);
	}

	@Override
	public Pageable first() {
		if(this.pagenumber==1)
			return this;
		else 
			return new ProductPageable(1);
	}

	@Override
	public boolean hasPrevious() {
		if(this.pagenumber==1)
			return false;
		else 
			return true;
	}

}
