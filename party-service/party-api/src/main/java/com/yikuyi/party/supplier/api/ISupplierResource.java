package com.yikuyi.party.supplier.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestParam;

import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 定义供应商相关接口
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public interface ISupplierResource {

	/**
	 * 查询所有供应商名称和主键映射 key--供应商名称 value--供应商ID
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "查询所有供应商名称和主键映射", notes = "查询所有供应商名称和主键映射", response = Map.class)
	public Map<String, String> getSupplierName();

	/**
	 * 根据供应商名称查询供应商信息 <br>
	 * showName是否要供应商必须是显示名称
	 * 
	 * @param name
	 * @param showName
	 * @return
	 * @since 2018年2月1日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "根据供应商名称查询供应商信息<br>showName是否要供应商必须是显示名称", notes = "根据供应商名称查询供应商信息<br>showName是否要供应商必须是显示名称", response = SupplierVo.class)
	public SupplierVo getSupplierByName(@RequestParam(value = "name", required = true) String name, @ApiParam(value = "showName", required = false, defaultValue = "true") boolean showName);

	/**
	 * 查询所有供应商显示名称(后台设置显示，返回供应商简称否则返回编码) key -- 供应商ID value -- 供应商显示名称
	 * 
	 * @param showName
	 *            通过showName限定返回数据类型
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "查询所有供应商显示名称(后台设置显示，返回供应商简称否则返回编码)", notes = "查询所有供应商显示名称(后台设置显示，返回供应商简称否则返回编码)", response = Map.class)
	public Map<String, String> getSupplierDisplayName(@ApiParam(value = "showName", required = false) Boolean showName);

	/**
	 * 查询所有库存的供应商ID
	 * 
	 * @param auto
	 *            通过auto限定是否自动集成数据
	 * @return
	 * @since 2018年1月25日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "查询所有自动集成库存的供应商ID", notes = "查询所有自动集成库存的供应商ID", response = Map.class)
	public Set<String> getAutoIntegrateSupplier(@ApiParam(value = "auto", required = false) Boolean auto);

	/**
	 * 查询所有有效供应商ID
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "查询所有有效供应商ID", notes = "查询所有有效供应商ID", response = Map.class)
	public Set<String> getSupplierIds();

	/**
	 * 获取供应商负责人邮箱<br>
	 * 负责人 询价人
	 * 
	 * @param id
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "获取供应商联系人邮箱", notes = "获取供应商联系人邮箱", response = Void.class)
	public SupplierMailVo getSuplierRelationShipMail(@ApiParam(value = "id", required = true) String id);

	/**
	 * 获取供应商基本信息<br>
	 * 
	 * @param ids
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "获取供应商简单信息", notes = "获取供应商简单信息", response = Void.class)
	public Map<String, SupplierVo> getSupplierSimple(@ApiParam(value = "ids", required = true) Collection<String> ids);

	/**
	 * 刷新供应商缓存
	 * 
	 * @since 2018年1月30日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "刷新供应商缓存", notes = "刷新供应商缓存", response = Void.class)
	public void refreshSupplierCache(@ApiParam(value = "ids", required = false) List<String> ids);
}
