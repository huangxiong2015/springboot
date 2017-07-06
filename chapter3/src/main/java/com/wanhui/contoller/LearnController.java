package com.wanhui.contoller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wanhui.entity.LearnResouce;

/**
 * 开发Web应用之JSP篇
 * @author hx
 *
 */
@Controller
@RequestMapping("/learn")
public class LearnController {
	@RequestMapping("")
	public ModelAndView index(){
		List<LearnResouce>  learnResouces = new ArrayList<>();
		//https://wenku.baidu.com/user/mydocs
		LearnResouce bean = new LearnResouce("第一章", "小熊", "http://www.oschina.net/");
		learnResouces.add(bean);
		bean = new LearnResouce("第二章", "小辉", "http://www.weibo.com/?c=spr_sinamkt_buy_hyww_weibo_p137");
		learnResouces.add(bean);
		bean = new LearnResouce("第三章", "小培", "https://www.baidu.com/");
		learnResouces.add(bean);
		ModelAndView mv = new ModelAndView("/index");
		mv.addObject("learnResouces", learnResouces);
		return mv;
	}
}
