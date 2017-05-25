package com.wanhui.contoller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wanhui.entity.LearnResouce;

/**
 * Thymeleaf模板引擎 引入包  spring-boot-starter-thymeleaf 自带 spring-boot-starter-web,所以不需要再配置了
 * @author hx
 *
 */
@Controller
@RequestMapping("/learn")
public class Chapter2 {
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
		//引入依赖后就在默认的模板路径src/main/resources/templates下编写模板文件即可完成
		ModelAndView mv = new ModelAndView("/index");
		mv.addObject("learnResouces", learnResouces);
		return mv;
	}
}
