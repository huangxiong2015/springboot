package com.ictrade.billSettlement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/billSettlement")
public class BillSettlementController {
	@RequestMapping(method = RequestMethod.GET)
	public String deptList(ModelMap model){
		return "financial/billSettlement";
	}
	@RequestMapping(value="/detail",method = RequestMethod.GET)
	public String add(ModelMap model){
		return "financial/billSettlementDetail";
	}
}
