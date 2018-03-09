package com.yikuyi.party.vendor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineJS;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;

@Mapper
public interface PartyProductLineDao {
	  
	
	/**
	 * 根据partyID获取供应商产品线
	 * @param partyId
	 * @return
	 * @since 2017年8月11日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public PartyProductLine findById(@Param("partyProductLineId")String partyProductLineId);	
	
	/**
	 * 查询产品线集合
	 * @param partyProductLine
	 * @return
	 * @since 2017年10月24日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<PartyProductLine> findByEntity(PartyProductLine partyProductLine);
	
	/**
	 * 供应商管理  插入产品线
	 * @param partyId
	 * @return
	 * @since 2017年8月14日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void insertLineList(@Param("pList")List<PartyProductLine> pList);	
	
	/**
	 * 删除产品线
	 * @param partyId
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void deleteById(String partyId);
	
	/**
	 * 新增产品线
	 * @param partyProductLine
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void insert(PartyProductLineJS partyProductLineJS);
	
	/**
	 * 根据id，删除（专用）
	 * @param partyId
	 * @return
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void deleteByPartyId(@Param("partyId")String partyId);
	

	/**
	 * 查询唯一产品线集合
	 * @param partyProductLine
	 * @return
	 * @since 2017年9月27日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> onlyProductLine(PartyProductLine partyProductLine);

	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @param partyProductLine
	 * @return
	 * @since 2017年10月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PartyProductLineModel> onlyProductCategoryList(PartyProductLineModel partyProductLine);
	

	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> listSupplierByBrandId(@Param("brandId")String brandId);
	
	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> listCategoryByBrandId(@Param("brandId")String brandId);
	
	/**
	 * 分页查询产品线数据
	 * @param partyProductLine
	 * @param rowBouds
	 * @return
	 * @since 2018年1月10日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<PartyProductLine> findByPage(PartyProductLine partyProductLine,RowBounds rowBouds);
	
}