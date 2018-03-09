package com.yikuyi.product.rule.delivery.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.delivery.IDeliveryResource;
import com.yikuyi.product.rule.delivery.manager.DeliveryManager;
import com.yikuyi.rule.delivery.vo.DeliveryInfo;
import com.yikuyi.rule.price.ProductPriceRule;
import com.ykyframework.model.IdGen;

@RestController
@RequestMapping("v1/rules/delivery")
public class DeliveryResource implements IDeliveryResource{
	
	private static final Logger logger = LoggerFactory.getLogger(IDeliveryResource.class);
	
	@Autowired
	private DeliveryManager deliveryManager;

	/**
	 * 新增交期模板
	 * @param info
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public DeliveryInfo addDelivery(@RequestBody DeliveryInfo info) {
		String userId = RequestHelper.getLoginUserId();
		String ruleId = String.valueOf(IdGen.getInstance().nextId());
		return deliveryManager.addDelivery(info, userId,ruleId);
	}

	/**
	 * 查询交期模板列表
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<DeliveryInfo> getDeliveryList(@RequestParam(value="page",required=false,defaultValue="1") int page, 
			@RequestParam(value="size",required=false,defaultValue="20") int size,
			@RequestParam(value="startDate",required=false)String startDate,
			@RequestParam(value="endDate",required=false)String endDate,
			@RequestParam(value="ruleStatus",required=false)String ruleStatus,
			@RequestParam(value="vendorId",required=false)String vendorId,
			@RequestParam(value="ruleName",required=false)String ruleName) {
		RowBounds rowBouds = new RowBounds((page-1)*size, size);
		try {
			ruleName = StringUtils.isNotEmpty(ruleName) ? URLDecoder.decode(ruleName,"UTF-8").replaceAll("\\s", "") : ruleName;
		} catch (UnsupportedEncodingException e) {
			logger.error("DeliveryResource.getDeliveryList decode ruleName UnsupportedEncodingException!");
		}
		return deliveryManager.getDeliveryList(startDate,endDate,ruleStatus,vendorId,ruleName,rowBouds,page,size);
	}

	/**
	 * 根据交期模块Id查询详情
	 * @param id
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public DeliveryInfo getDeliveryDetail(@PathVariable("id") String id) {
		return deliveryManager.getDeliveryDetail(id);
	}

	/**
	 * 修改交期模板
	 * @param id
	 * @param info
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public DeliveryInfo updateDelivery(@PathVariable("id") String id, @RequestBody DeliveryInfo info) {
		String userId = RequestHelper.getLoginUserId();
		String ruleId = String.valueOf(IdGen.getInstance().nextId());
		return deliveryManager.updateDelivery(id, info,userId,ruleId);
	}

	/**
	 * 启用、停用交期模板
	 * @param id
	 * @param stauts
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}/status", method=RequestMethod. PUT)
	public DeliveryInfo updateDeliveryStatus(@PathVariable("id") String id, @RequestParam(value="status") ProductPriceRule.RuleStatus stauts) {
		String userId = RequestHelper.getLoginUserId();
		DeliveryInfo info = deliveryManager.getDeliveryDetail(id);
		return deliveryManager.updateDeliveryStatus(id, stauts,userId,info);
	}

	/**
	 * 删除交期模板
	 * @param id
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public void deleteDelivery(@PathVariable("id") String id) {
		String userId = RequestHelper.getLoginUserId();
		DeliveryInfo info = deliveryManager.getDeliveryDetail(id);
		deliveryManager.deleteDelivery(id,userId,info.getDeliveryRuleName());
	}

}
