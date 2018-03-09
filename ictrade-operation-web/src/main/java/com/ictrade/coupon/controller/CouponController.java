package com.ictrade.coupon.controller;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;





@Controller
public class CouponController {
	
	//@Autowired
	//private PayClientBuilder payClientBuilder;
	
	@Value("#{appProps['api.pay.serverUrlPrefix']}")
	private String payImplUrlPrefix;
	
	/*优惠券创建列表页面*/
	@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(value="/coupon",method = RequestMethod.GET)
	public String customerList(ModelMap model){
		return "coupon/createdCouponList";
	}
	
	/*@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String customerDetail(@RequestParam(value ="id" , required = true)String id,ModelMap model){
		model.put("id", id);
		return "coupon/coupondetail";
	}*/
	@RequiresPermissions("MENU:VIEW:250")
	/*地推人员选择页面*/
	@RequestMapping(value="/coupon",params="action=groundhandleOut",method = RequestMethod.GET)
	public String groundhandleOut(@RequestParam(value ="id" , required = true)String id,ModelMap model){
		model.put("id", id);
		return "coupon/groundHandleOut";
	}
	@RequiresPermissions("MENU:VIEW:250")
	/*地推人员选择成功页面*/
	@RequestMapping(value="/coupon",params="action=groundhandleSucc",method = RequestMethod.GET)
	public String groundhandleSucc(@RequestParam(value ="id" , required = true)String id,ModelMap model){
		model.put("id", id);
		return "coupon/groundHandleSucc";
	}
	@RequiresPermissions("MENU:VIEW:250")
	/*优惠券创建页面*/
	@RequestMapping(value="/coupon/create",method = RequestMethod.GET)
	public String createCoupon(){
		return "coupon/createCoupon";
	}
	/*优惠券查看页面*/
	@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(value="/coupon/examine",method = RequestMethod.GET)
	public String examineCoupon(){
		return "coupon/editCoupon";
	}
	/*优惠券编辑页面*/
	@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(value="/coupon/edit",method = RequestMethod.GET)
	public String editCoupon(){
		return "coupon/editCoupon";
	}
	@RequiresPermissions("MENU:VIEW:250")
	/*优惠券发放列表页面*/
	@RequestMapping(value="/coupon/couponHandleList",method = RequestMethod.GET)
	public String createdCouponList(){
		return "coupon/couponList";
	}
	/*优惠券统计列表页面*/
	@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(value="/coupon/couponStatisticList",method = RequestMethod.GET)
	public String statisticListCouponList(){
		return "coupon/statisticList";
	}
	@RequiresPermissions("MENU:VIEW:250")
	/*优惠券创建列表页面*/
	@RequestMapping(value="/coupon/detail",method = RequestMethod.GET)
	public String couponDetail(){
		return "coupon/couponDetail";
	}
	@RequiresPermissions("MENU:VIEW:250")
	/*优惠券领取列表页面*/
	@RequestMapping(value="/coupon/getCouponList",method = RequestMethod.GET)
	public String gotCouponDetail(){
		/*return "coupon/couponStatisticsDetail";*/ //新版本基于vue+lemon,还未经过测试，已重构完成
		return "coupon/gotCouponList";  //老版本基于avalon
	}
	
	/*优惠券创建列表页面*/
	@RequiresPermissions("MENU:VIEW:869877804257247232")
	@RequestMapping(value="/PaymentMethodsList",method = RequestMethod.GET)
	public String customerList2(ModelMap model){
		return "paymentMethods/PaymentMethodsList";
	}
	
	/*优惠券创建列表页面*/
	@RequiresPermissions("MENU:VIEW:869877804257247232")
	@RequestMapping(value="/PaymentMethods/edit",method = RequestMethod.GET)
	public String customerEdit2(ModelMap model){
		return "paymentMethods/PaymentMethodsEdit";
	}
	/*增加插入coupon原先数据的是action*/
	@RequestMapping(value="/Coupon/oldDataInsert",method = RequestMethod.GET)
	public void oldDataInsert(){
		   String result = null;
		   CloseableHttpClient httpClient = HttpClients.createDefault();  
		   String url = payImplUrlPrefix+ "/v1/coupons/oldDateInsert";
		   //String url="http://localhost:27085/v1/coupons/oldDateInsert";
	       HttpGet httpPost = new HttpGet(url);
	        //设置请求器的配置
	       HttpResponse response;
			try {
				response = httpClient.execute(httpPost);
				 HttpEntity entity = response.getEntity();
		         result = EntityUtils.toString(entity, "UTF-8");
		         System.out.println(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//String url =payImplUrlPrefix+ "/v1/coupons/oldDateInsert";
		/*String url ="http://localhost:27085/v1/coupons/oldDateInsert";
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getForObject(url, String.class);*/
		//payClientBuilder.couponResource().findPriceByUnitAmount("");
	    //String aString=	payClientBuilder.couponResource().findPriceByUnitAmount("");
	    //System.out.println(aString);
	    
	}
}
