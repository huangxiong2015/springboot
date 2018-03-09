package com.yikuyi.party.resource;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface SupplierClient {

	/**
	 * 查询所用供应商名称<br>
	 * key -- 供应商名称<br>
	 * value -- 供应商ID
	 * 
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier/names")
	public Map<String, String> getSupplierNames();

	/**
	 * 根据供应商名称查询供应商信息 <br>
	 * showName是否要供应商必须是显示名称  default = true
	 * 
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier?name={name}&showName={showName}")
	public SupplierVo getSupplierByName(@Param("name") String name, @Param("showName") Boolean showName);

	/**
	 * 查询所用供应商名称<br>
	 * key -- 供应商ID<br>
	 * value -- 供应商显示名称 ? 供应商名称 : 供应商编码
	 * 
	 * @param showName
	 *            <Br>
	 *            null返回所有
	 * @return
	 * @since 2018年1月25日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier/displayName?showName={showName}")
	public Map<String, String> getSupplierDisplayName(@Param("showName") Boolean showName);

	/**
	 * 获取所有自动集成的供应商
	 * 
	 * @param auto
	 *            <Br>
	 *            null返回所有 true返回自动集成，false返回非自动集成
	 * @return
	 * @since 2018年1月25日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier/integrate/ids?auto={auto}")
	public Set<String> getAutoIntegrateSupplier(@Param("auto") Boolean auto);

	/**
	 * 获取所有有效的供应商ID
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier/ids")
	public Set<String> getSupplierIds();

	/**
	 * 获取所有有效的供应商ID
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/supplier/{id}/relationShip/mail")
	public SupplierMailVo getSuplierRelationShipMail(@Param("id") String id);

	/**
	 * 批量查询供应商简单信息<br>
	 * 参数为Collections.emptyList(),返回所有有效供应商
	 * 
	 * @param supplierIds
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("POST /v1/supplier/simple")
	public Map<String, SupplierVo> getSupplierSimple(@RequestBody Collection<String> supplierIds);
}