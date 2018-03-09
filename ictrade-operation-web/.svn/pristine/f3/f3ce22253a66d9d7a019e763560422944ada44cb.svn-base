package com.ictrade.productCategory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/productCategory")
public class productCategoryController {
	
	/**
	 * 分类维护
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:3001")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "category/category";
	}
	
}
