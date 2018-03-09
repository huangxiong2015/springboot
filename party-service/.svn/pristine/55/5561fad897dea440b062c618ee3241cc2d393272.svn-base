package com.yikuyi.party.supplier.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.party.supplier.api.ISupplierResource;
import com.yikuyi.party.supplier.bll.SupplierManager;

/**
 * 定义供应商相关实现
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/supplier")
public class SupplierResource implements ISupplierResource {

	@Autowired
	private SupplierManager supplierManager;

	@Override
	@RequestMapping(value = "/names", method = RequestMethod.GET)
	public Map<String, String> getSupplierName() {
		return supplierManager.getSupplierName();
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public SupplierVo getSupplierByName(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "showName", required = false, defaultValue = "true") boolean showName) {
		return supplierManager.getSupplierByName(name, showName);
	}
	
	@Override
	@RequestMapping(value = "/displayName", method = RequestMethod.GET)
	public Map<String, String> getSupplierDisplayName(@RequestParam(value = "showName", required = false) Boolean showName) {
		return supplierManager.getSupplierDisplayName(showName);
	}

	@Override
	@RequestMapping(value = "/simple", method = RequestMethod.POST)
	public Map<String, SupplierVo> getSupplierSimple(@RequestBody Collection<String> supplierIds) {
		return supplierManager.getSupplierSimple(supplierIds);
	}

	@Override
	@RequestMapping(value = "/integrate/ids", method = RequestMethod.GET)
	public Set<String> getAutoIntegrateSupplier(@RequestParam(value = "auto", required = false) Boolean auto) {
		return supplierManager.getAutoIntegrateSupplier(auto);
	}

	@Override
	@RequestMapping(value = "/ids", method = RequestMethod.GET)
	public Set<String> getSupplierIds() {
		return supplierManager.getSupplierIds();
	}

	@Override
	@RequestMapping(value = "/{id}/relationShip/mail", method = RequestMethod.GET)
	public SupplierMailVo getSuplierRelationShipMail(@PathVariable String id) {
		return supplierManager.getSuplierrelationShipMail(id);
	}

	@Override
	@RequestMapping(value = "/cache/simple", method = RequestMethod.PUT)
	public void refreshSupplierCache(@RequestBody List<String> supplierIds) {
		supplierManager.refreshSupplierCache(supplierIds);
	}
}