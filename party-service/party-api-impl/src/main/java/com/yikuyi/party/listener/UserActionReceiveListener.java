package com.yikuyi.party.listener;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.framework.springboot.utils.AuthorizationUtil;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.user.RecommendCartsVo;
import com.yikuyi.party.user.UserActionVo;
import com.yikuyi.party.user.UserActionVo.Action;
import com.yikuyi.party.user.UserActionVo.ActionType;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.product.ProductClientBuilder;
import com.yikuyi.promotion.model.Promotion.PromotionType;
import com.yikuyi.promotion.vo.PromotionVo;
import com.yikuyi.transaction.TransactionClient;
import com.yikuyi.transaction.carts.vo.CartsVo;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 用户操作MQ
 * 
 * 用户异步操作（注册送劵，注册登录，登录成功等操作。。。）
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
@Service
public class UserActionReceiveListener implements MsgReceiveListener {

	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(UserActionReceiveListener.class);
	 */

	@Autowired
	private ProductClientBuilder productClientBuilder;

	@Autowired
	private TransactionClient transactionClient;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Autowired
	private PayClientBuilder payClientbuilder;

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.partyUserAction.topicName}")
	private String partyUserActionTopicName;

	private static final String PERSON_REG = "PERSON_REG";
	private static final String COMPANY_REG = "COMPANY_REG";

	@Override
	public void onMessage(Serializable msg) {
		if (msg instanceof RecommendCartsVo) {
			RecommendCartsVo vo = (RecommendCartsVo) msg;
			transactionClient.cartsClient().saveCarts(vo.getCartsVo(), authorizationUtil.mockRealAuthorization(vo.getLoginName(), vo.getUserId()));
		} else if (msg instanceof UserActionVo) {
			UserActionVo userAction = (UserActionVo) msg;
			if (Action.REGISTER.equals(userAction.getAction())) {
				if (ActionType.COUPON_RECOMMEND.equals(userAction.getActionType())) {
					payClientbuilder.couponResource().regOrUpSendCoupon(userAction.isEnterpriseCustomer() ? COMPANY_REG : PERSON_REG, userAction.getUserId(), authorizationUtil.getMockAuthorization());
				} else if (ActionType.PRODUCT_RECOMMEND.equals(userAction.getActionType())) {
					// 避免for混合调用中断，重复执行。拆分为多次MQ发放商品
					getPromotions(userAction).stream().forEach(v -> msgSender.sendMsg(partyUserActionTopicName, v, null));
				}
			}
		}
	}

	/**
	 * 获取活动下面的商品，组成购物车结构
	 * 
	 * @return
	 * @since 2018年2月8日
	 * @author jik.shu@yikuyi.com
	 */
	private Set<RecommendCartsVo> getPromotions(UserActionVo userActionVo) {
		PageInfo<PromotionVo> rs = productClientBuilder.promotionResource().getPromotions(PromotionType.RECOMMEND, "ONGOING", 1, RowBounds.NO_ROW_LIMIT, authorizationUtil.getMockAuthorization());
		if (null == rs || CollectionUtils.isEmpty(rs.getList())) {
			return Collections.emptySet();
		}
		Set<RecommendCartsVo> rstSet = new HashSet<>();
		rs.getList().stream().forEach(v -> {
			JSONArray currencys = v.getPromotionContent().getJSONArray("cartType");
			currencys.forEach(t -> {
				RecommendCartsVo recommendCartsVo = new RecommendCartsVo();
				recommendCartsVo.setUserId(userActionVo.getUserId());
				recommendCartsVo.setLoginName(userActionVo.getLoginName());
				CartsVo cartsVo = new CartsVo();
				cartsVo.setProductIDs(v.getPromotionContent().getString("productId"));
				cartsVo.setCurrency(t.toString());
				cartsVo.setCount(v.getPromotionContent().getInteger("productQuantity"));
				recommendCartsVo.setCartsVo(cartsVo);
				rstSet.add(recommendCartsVo);
			});
		});
		return rstSet;
	}
}