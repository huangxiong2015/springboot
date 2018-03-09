//package com.yikuyi.party.paymentInvoice.manager;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.TestExecutionListeners;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
//import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
//import com.github.springtestdbunit.annotation.DatabaseOperation;
//import com.github.springtestdbunit.annotation.DatabaseSetup;
//import com.github.springtestdbunit.annotation.ExpectedDatabase;
//import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
//import com.yikuyi.party.paymentInvoice.PartyInvoiceSettings;
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
//	TransactionDbUnitTestExecutionListener.class })
//@Transactional
//@Rollback
//public class PartyInvoiceManagerTest {
//
//	@Autowired
//	private PartyInvoiceManager partyInvoiceManager;
//	
//	/**
//	 * @author zr.guobin
//	 * 新增发票处理
//	 */
////	@Test
////	@Rollback
////	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "partyinvoicebefore-sample.xml" })
////	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "partyinvoiceafter-sample.xml")
////	public void addPartyInvoice() {
////		PartyInvoiceSettings settings = new PartyInvoiceSettings();
////		settings.setInvoiceTypeId(PartyInvoiceSettings.InvoiceTypeId.COMMON);
////		settings.setContactMechId("805944195649896448");
////		settings.setInvoiceHeader("深圳易库易供应链科技有限公司");
////		settings.setTaxCode("405545645459885454");
////		settings.setRegAddress("广东省深圳市南山区科怡路麻雀");
////		settings.setRegPhone("0755-12345678");
////		settings.setBankName("招商银行 科发路支行");
////		partyInvoiceManager.addPartyInvoice(settings);
////	}
//
//	/**
//	 * 查询发票处理
//	 */
//	@Test
//	@Rollback
//	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "partyinvoicebefore-sample.xml" })
//	public void searchPartyInvoice() {
//		PartyInvoiceSettings settings = new PartyInvoiceSettings();
//		settings.setPartyId("易库易公司");
//		settings.setInvoiceTypeId(PartyInvoiceSettings.InvoiceTypeId.COMMON);
//		partyInvoiceManager.searchPartyInvoice(settings);
//	}
//	
//	/**
//	 * 查询发票处理
//	 */
//	@Test
//	@Rollback
//	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "partyinvoicebefore-sample.xml" })
//	public void searchOrderPartyInvoice() {
//		partyInvoiceManager.searchOrderPartyInvoice("2345");
//	}
//}
