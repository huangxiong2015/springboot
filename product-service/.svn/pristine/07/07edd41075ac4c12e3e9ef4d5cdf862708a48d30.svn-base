//package com.yikuyi.product.material.bll;
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
//import com.yikuyi.material.vo.MaterialVo;
//import com.yikuyi.product.base.ProductApplicationTestBase;
//import com.yikuyi.product.basicmaterial.bll.LeadBasicMaterialExcelReaderTest;
//import com.yikuyi.product.template.bll.ProductTemplateManager;
//import com.yikuyi.template.model.ProductTemplate;
//
//
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
//	TransactionDbUnitTestExecutionListener.class })
//@Transactional
//@Rollback
//public class MaterialManagerTest2 extends ProductApplicationTestBase{
//	
//	@Autowired
//	private MaterialManager materialManager;
//	
//	@Autowired
//	ProductTemplateManager productTemplateManager;
//		
//	
//	/**
//	 * 
//	 * 开始处理job文件下载
//	 * @since 2017年6月27日
//	 * @author zr.zhanghua@yikuyi.com
//	 */
//	
//	@SuppressWarnings("unused") 
//	@Test
//	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/goods/manager/document_sample.xml")
////	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,value = "com/yikuyi/product/goods/manager/document_add.xml")
//	public void processingJobTest(){
//		LeadMaterialExcelReader reader = new LeadMaterialExcelReader();
//		ProductTemplate emTemplate = productTemplateManager.geTemplate("basisTemplateId");
//		String filepath = LeadBasicMaterialExcelReaderTest.class.getClassLoader().getResource("com/yikuyi/product/goods/manager/testFC-1.xlsx").getFile();
//		long time1 = 1400L;
//		MaterialVo pararmMaterialVo = new MaterialVo(); 
//		pararmMaterialVo.setRegionId("xk");
//		String docId = "cnm";
//		materialManager.processingJob(pararmMaterialVo,docId,filepath,time1,emTemplate);
//	}
//	
//}