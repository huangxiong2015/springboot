package com.yikuyi.party.party.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyAttribute;

@Mapper
public interface PartyAttributeDao {
	// 所属行业
	int insertIndustryCategory(Party party);

	// 所属行业其它属性拓展属性
	int insertOtherAttrs(Party party);

	// 插入资质信息属性
	int insertCerAttrs(Party party);

	// 企业授权委托书
	int insertLoa(Party party);

	// 公司类型
	int insertCorporationCategory(Party party);

	// 是否显示名称 Y(显示),N(不显示) // 是否支持价格策略,Y支持，N不支持
	int insertIsShowName(Party party);

	// 更新是否显示供应商名称
	int updateIsShowName(Party party);

	// 更新是否支持价格策略
	int updateIsSupPrice(Party party);

	// 用户地址币种类型CNY(国内) USD(香港)
	int insertUsedCurruncy(Party party);

	/**
	 * 获取属性信息通过id
	 * 
	 * @param id
	 * @return
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyAttribute> getPartAttribute(String id);

	/**
	 * 修改企业信息属性
	 * 
	 * @param id
	 * @return
	 * @since 2016年2月7日
	 * @author zr.helinmei@yikuyi.com
	 */
	int updateEnterpriseAttribute(Party party);

	/**
	 * 获取属性信息通过id和key
	 * 
	 * @param id
	 * @return
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PartyAttribute getAttrByKey(@Param("id") String id, @Param("attrName") String attrName);

	// 根据id和key值删除数据
	void deleteByIdOrKey(Party party);

	/**
	 * 查询是否符号条件的partyCode
	 * 
	 * @param name公司名称
	 *            attrValue组织机构的值
	 * @return List<EnterpriseVo>
	 * @since 2017年5月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<EnterpriseVo> findPartyCodeList(@Param("name") String name, @Param("attrValue") String attrValue,
			@Param("attrName") String attrName);

	/**
	 * 看属性是否存在如果存在就修改不存在就新增
	 * 
	 * @param id,
	 *            attrName属性key, attrValue组织机构的值
	 * @return void
	 * @since 2017年6月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void save(@Param("id") String id, @Param("attrName") String attrName, @Param("attrValue") String attrValue);
	
	/**
	 * 供应商管理插入
	 * 
	 *  VENDOR_INFO_LEGALPERSON="VENDOR_INFO_LEGALPERSON"; //公司法人                            
		VENDOR_INFO_REGPRICE="VENDOR_INFO_REGPRICE"; //注册资金                                  
		VENDOR_INFO_REGRADDRESS="VENDOR_INFO_REGRADDRESS"; //注册地址                            
		VENDOR_INFO_EMPLOYEENUM="VENDOR_INFO_EMPLOYEENUM"; //员工人数                            
		VENDOR_INFO_WEBSITE="VENDOR_INFO_WEBSITE"; //供应商官网                                   
		VENDOR_CREDIT_PURCHASEDEAL="VENDOR_CREDIT_PURCHASEDEAL"; //采购协议                      
		VENDOR_CREDIT_PURCHASEDEALDATE="VENDOR_CREDIT_PURCHASEDEALDATE"; //采购协议有效期           
		VENDOR_CREDIT_SECRECYPROTOCOL="VENDOR_CREDIT_SECRECYPROTOCOL"; //保密协议                
		VENDOR_CREDIT_SECRECYPROTOCOLDATE="VENDOR_CREDIT_SECRECYPROTOCOLDATE"; //保密协议有效期     
		VENDOR_SALE_INFOVO_FOCUSFIELDS="VENDOR_SALE_INFOVO_FOCUSFIELDS"; //关注领域              
		VENDOR_SALE_INFOVO_PRODUCTCATEGORYS="VENDOR_SALE_INFOVO_PRODUCTCATEGORYS"; //优势产品类别  
		VENDOR_SALE_INFOVO_MAJORCLIENTS="VENDOR_SALE_INFOVO_MAJORCLIENTS"; //主要客户            
	 * @param id,
	 *           
	 * @return void
	 * @since 2017年8月14日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void insertVendorField(@Param("arrayList") List<PartyAttribute> arrayList);
	
	/**
	 * 使用mysql replace关键字
	 * 更加唯一索引批量插入或者更新数据
	 * 返回影响行数受存在数据影响，不一定等于传人总数
	 * @param partyAttributeList
	 * @since 2017年8月17日
	 * @author jik.shu@yikuyi.com
	 */
	public Integer batchReplace(@Param("partyAttributeList") List<PartyAttribute> partyAttributeList);
	
	/**
	 * 根据属性的Key值批量查询所有的属性
	 * @param partyId
	 * @param attributeKeys(可以为null,为null的时候查询所有)
	 * @return
	 * @since 2017年8月17日
	 * @author jik.shu@yikuyi.com
	 */
	public List<PartyAttribute> getPartyAttributesByKeys(@Param("partyId") String partyId,@Param("attributeKeys") List<String> attributeKeys);

	// 属性字段修改
	int updateField(PartyAttribute partyAttribute);
	
	/**
	 * 根据属性的Key值批量删除所有的属性
	 * @param partyId
	 * @param 
	 * @return
	 * @since 2017年8月21日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	int delByPartyIdAndName(@Param("partyId") String partyId,@Param("attributeKeys") List<String> attributeKeys);
	
	
	 /**
	  * 查询附件数组
	  * @param partyCreditId
	  * @return
	  * @since 2017年8月14日
	  * @author zr.chenxuemin@yikuyi.com
	  */
	 public List<PartyAttachment>  findAttrchmentList(@Param("partyId") String partyId,@Param("attachmentType")String attachmentType);
	 
	
	
}