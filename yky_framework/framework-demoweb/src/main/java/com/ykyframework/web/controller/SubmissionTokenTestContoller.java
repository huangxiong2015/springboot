/*
 * Created: 2016年4月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ykyframework.wink.annotations.SubmissionToken;

/**重复提交测试Controller
 * @author longyou@yikuyi.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/dupSubmission")
public class SubmissionTokenTestContoller {
	
	@RequestMapping(method=RequestMethod.GET)
	@SubmissionToken(generateToken=true)
	public String gotoDupSubmission(){
		return "dup/submission/main";
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/submit")
	@SubmissionToken(checkToken=true)
	public ModelAndView dupSubmissionCommit(@RequestParam Map reqParams){
		ModelAndView view = new ModelAndView();
		view.setViewName("dup/submission/success");
		view.addAllObjects(reqParams);
		return view;
	}
	

}
