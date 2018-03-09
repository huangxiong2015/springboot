
package com.ictrade.supplyChainFinance.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/supplyChainFinance")
public class SupplyChainFinanceController {
	
	/**
	 * 供应链金融列表
	 * @return
	 * @since 20171213
	 * @author zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:304")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "supplyChainFinance/list";
	}
	
	/**
	 * 供应链金融详情
	 * @return
	 * @since 20171213 
	 * @author zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:304")
	@RequestMapping(value="/detail/{id}", method = RequestMethod.GET)
	public String getDetail(@PathVariable String id ,ModelMap model){
		model.put("id", id);
		return "supplyChainFinance/detail";
	}
	
}

