package com.yikuyi.party.supplier;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;

public class SupplierResourceTest extends BaseTest {

	@Test
	public void getSupplierNames() {
		Map<String,String> names = new CaseInsensitiveMap<>(partyClient.supplierClient().getSupplierNames());
		Assert.assertNotNull(names);
	}
	
	@Test
	public void getSupplierByName() {
		SupplierVo tempVo = partyClient.supplierClient().getSupplierByName("富昌", null);
		Assert.assertNotNull(tempVo);
	}
	
	@Test
	public void getSupplierDisplayName() {
		Map<String,String> map = partyClient.supplierClient().getSupplierDisplayName(null);
		Assert.assertNotNull(map);
	}
	
	@Test
	public void getAutoIntegrateSupplier() {
		Set<String> sets = partyClient.supplierClient().getAutoIntegrateSupplier(null);
		Assert.assertNotNull(sets);
	}
	
	@Test
	public void getSupplierIds() {
		Set<String> sets = partyClient.supplierClient().getSupplierIds();
		Assert.assertNotNull(sets);
	}
	
	@Test
	public void getSuplierRelationShipMail() {
		Set<String> sets = partyClient.supplierClient().getSupplierIds();
		SupplierMailVo vo = partyClient.supplierClient().getSuplierRelationShipMail(sets.toArray(new String[]{})[0]);
		Assert.assertNotNull(vo);
	}
	
	@Test
	public void getSupplierSimple() {
		Map<String,SupplierVo> simples = partyClient.supplierClient().getSupplierSimple(Arrays.asList("dijikey"));
		Assert.assertNotNull(simples);
	}
	
}