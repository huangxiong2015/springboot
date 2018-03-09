package com.yikuyi.product.activity.bll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yikuyi.activity.vo.MockActivityDataVo;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.pay.coupon.CouponActivityRel;
import com.yikuyi.pay.coupon.CouponStatus;
import com.yikuyi.product.activity.dao.MockActivityDataDao;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class RegisterActivityMockManager {
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterActivityMockManager.class);
	
	@Autowired
	private MockActivityDataDao mockActivityDataDao;
	
	@Autowired
	private PayClientBuilder payClientBuilder;
	
	private String[] mobilePrifixSeeds = new String[]{"135","131","158","159","187","130","182","157","139","138"};
	private String[] entPrifixSeeds = new String[]{"唐","秦","邯","邢","保","张","承","沧","廊","衡","大","阳","晋","朔","运","忻","临","吕","包","乌","赤","通","鄂","呼","兴","锡","巴","阿","沈","鞍","抚","本","丹","锦","营","辽","盘","铁","朝","葫","长","吉","四","白","松","延","齐","鸡","鹤","双","伊","佳","七","牡","黑","绥","无","徐","常","苏"};
	private String[] endPrifixSeeds = new String[]{"科技","公司","公司","公司"};
	private String[] middleEntSeeds = new String[]{"***","****","*****"};
	private double runRate = 0.9d;//工作日大概有90%的机遇每个小时有代金券发放
	private double entRate = 0.8d; //企业的机遇为80%;其他为个人
	private double entRegRate = 0.7d; //注册企业升级的机遇为50%
	private double personRegRate = 0.8d; //个人注册升级为企业的机遇为50%
	
	/**
	 * mock 参加活动的手机号码
	 * @return
	 */
	public String mockMobileData(){
		return getRandomFromArray(mobilePrifixSeeds)+"****"+mockMobileLast4Data();
	} 
	
	/**
	 * mock手机后四位
	 * @param min
	 * @param max
	 * @return
	 */
	private  String mockMobileLast4Data() {
	    return String.valueOf(getRandom(0,9))+
	    		String.valueOf(getRandom(0,9))+
	    		String.valueOf(getRandom(0,9))+
	    		String.valueOf(getRandom(0,9));
	}

	/**
	 * mock 参加活动的企业名称
	 * @return
	 */
	public String mockEntData(){
		return getRandomFromArray(entPrifixSeeds) + 
				getRandomFromArray(middleEntSeeds) + 
				getRandomFromArray(endPrifixSeeds);
	}
	
	/**
	 * mock 企业活动类型
	 * @return
	 */
	public String mockActivityTypeData(){
		return StringUtils.EMPTY;
	}
	
	/**
	 * 从数组里面随机获取一个
	 * @param arrayData
	 * @return
	 */
	public String getRandomFromArray(String[] arrayData){
		return arrayData[getRandom(0,arrayData.length-1)];
	}
	
	/**
	 * 获取某个范围的随机整数
	 * @param min
	 * @param max
	 * @return
	 */
	private  int getRandom(int min, int max){
	    Random random = new Random();
	    return random.nextInt(max) % (max - min + 1) + min;
	}
	
	/**
	 * 以某个概率抽一次奖，是否得奖。
	 * @param rate
	 * @return
	 */
	private boolean getResultByPercent(double rate){
		return rate >= new Random().nextDouble();
	}
	
	private boolean judgeWeekEndDay(Calendar calendar){
		//Calendar calendar = Calendar.getInstance();    
        int week = calendar.get(Calendar.DAY_OF_WEEK);//星期天是一个星期的开始也就是1	
		return week==1 || week==7;
	}
	
	/**
	 * 获取生成时间
	 * @return String
	 */
	public String  getGiftTime()throws ParseException{
		String giftTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		giftTime = sdf.format(new Date(new Date().getTime() - (getRandom(0,59) * 60 * 1000L) - (getRandom(0,59) * 1000L)));
	    return giftTime;
	}
	
	/**
	 * 获取生成几率
	 * @return double
	 */
	public double  getActuralRate(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean isWeekEndDay = judgeWeekEndDay(sdf.getCalendar());
		double acturalRate =runRate;
		if(isWeekEndDay){
			acturalRate = runRate*0.5d; //周末发送代金券的机遇减半
		}
		if(hour>=9&&hour<=12){
			acturalRate = runRate*0.9d;
		}else if(hour>=12&&hour<=14){
			acturalRate = runRate*0.7d;
		}else if(hour>=14&&hour<=18){
			acturalRate = runRate*0.9d;
		}else if(hour>=18&&hour<=22){
			acturalRate = runRate*0.4d;
		}else if(hour>=22&&hour<=24){
			acturalRate = runRate*0.3d;
		}else if(hour>=1&&hour<=5){
			acturalRate = runRate*0.2d;
		}else if(hour>=5&&hour<=9){
			acturalRate = runRate*0.2d;
		}
		return acturalRate;
	}
	
	public MockActivityDataVo getRandomData(String activity) throws ParseException{
		String name;
		String giftType;
		String gift;
		//生成时间
		String	giftTime = getGiftTime();
		//生成几率
		double acturalRate = getActuralRate();
		if(!getResultByPercent(acturalRate)){
			return null;
		}//10%的机遇没有发放。
	    
		if(getResultByPercent(entRate)){//企业
			name = mockEntData();
			if(getResultByPercent(entRegRate)){//企业注册
				giftType = "企业注册";
				gift= "50";
			}else{//企业升级
				giftType = "企业升级";
				gift= "20";
			}
		}else{//个人
			name = mockMobileData();
			if(getResultByPercent(personRegRate)){//个人注册
				giftType = "个人注册";
				gift= "20";
			}else{//个人升级
				giftType = "个人升级";
				gift= "50";
			}
		}
		
		
		MockActivityDataVo mockData = new MockActivityDataVo();
		mockData.setPartyName(name);
		//如果是松下活动则改变类型
        if(!"REGISTER".equals(activity)){
       	  giftType = "参加松下电阻8折特惠活动";
          gift = "5";
       	  mockData.setMockType(activity);
	    }else{
	    	mockData.setMockType("REGISTER");
	    }
		mockData.setActivityType(giftType);
		mockData.setGift(gift);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Date newDate = dateFormat.parse(giftTime);
		mockData.setCreateDate(newDate);
		
		String id = String.valueOf(IdGen.getInstance().nextId());
		mockData.setPartyId(id);
		return mockData;
	}
	
	/**
	 * 创建mock数据
	 * @throws ParseException
	 */
	public void generateMockData() throws ParseException{
		try{
			//1.产生注册的mock数据
			MockActivityDataVo mockRegister = getRandomData("REGISTER");
			if(null != mockRegister){
				mockActivityDataDao.save(mockRegister);
				logger.info("创建mock数据注册{}:",JSONObject.toJSON(mockRegister).toString());
			}
			//2.产生**活动的mock数据
			MockActivityDataVo mockActive = getRandomData("892219419546091520");
			if(null != mockActive){
				mockActivityDataDao.save(mockActive);
				logger.info("创建mock数据活动{}:",JSONObject.toJSON(mockActive).toString());
			}
		}catch(Exception e){
			logger.error("创建mock数据异常:{}", e);
		}
	}
	
	/**
	 * 查询代金券
	 * @return
	 * @since 2017年8月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<MockActivityDataVo> listGift(String activity,RowBounds rowBounds){
		//1. 插入真实数据
		if("REGISTER".equals(activity)){
			  PageInfo<CouponStatus> couponPage = payClientBuilder.couponResource().getGiftList(1, 20);
			  if(null != couponPage && CollectionUtils.isNotEmpty(couponPage.getList())){
				 List<CouponStatus> list = couponPage.getList();
				 for(CouponStatus couponStatus:list){
					 if("PERSON_REG".equals(couponStatus.getBusinessType()) || "COMPANY_REG".equals(couponStatus.getBusinessType()) || "UP_COMPANY".equals(couponStatus.getBusinessType())){
					
					 MockActivityDataVo realData = new MockActivityDataVo();
					 realData.setPartyId(couponStatus.getPartyId());
					 realData.setActivityType(couponStatus.getName());
					 realData.setPartyName(couponStatus.getPartyName());
					 realData.setCreateDate(new Date());
					 realData.setGift(couponStatus.getUnitAmount()+"");
					 realData.setMockType("REGISTER");
					 mockActivityDataDao.save(realData);
					 }
				 }
			 } 
		}else{
			//其他
			List<CouponActivityRel> couponPage = payClientBuilder.couponActivityResource().queryActivityCouponList(activity);
			  if(CollectionUtils.isNotEmpty(couponPage)){
				 List<CouponActivityRel> list = couponPage;
				 for(CouponActivityRel couponActivityRel:list){
					 MockActivityDataVo realData = new MockActivityDataVo();
					 realData.setPartyId(couponActivityRel.getPartyId());
					 realData.setActivityType("参加松下电阻8折特惠活动");
					 realData.setPartyName(couponActivityRel.getPartyName());
					 realData.setCreateDate(new Date());
					 realData.setGift(couponActivityRel.getUnitAmount()+"");
					 realData.setMockType(activity);
					 mockActivityDataDao.save(realData);
				 }
			 } 
		}
		//2.查询数据返回 
		return new PageInfo<>(mockActivityDataDao.listGift(rowBounds,activity));
	}
}
