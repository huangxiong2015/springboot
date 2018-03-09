package com.yikuyi.product.goods.manager;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.goods.dao.ProductClient;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.model.Product;

/**
 * 性能单元测试，用于对各种中间件的性能的比较，以及新用法的尝试
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest{
	
	@Autowired
	private ProductRepository productResp;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Product> aliasCategoryOps;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ProductBrand> aliasBrandOps;
	
	@Autowired
	private MongoOperations mo;
	
	@Autowired
	private BrandManager bm;
	
	@Autowired
	private ProductClient pc;
	


	/**
	 * hash缓存支持模糊搜索
	 * @since 2017年12月7日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testScanHashRedis(){
		Cursor<Entry<String, ProductBrand>> cursor = aliasBrandOps.scan(BrandManager.ALIAS_BRAND_NAMESPACE, ScanOptions.scanOptions().match("*ANJI*").build());
		while(cursor.hasNext()){
			System.out.println(cursor.next().getKey());
		}
	}
	
	/**
	 * 测试id取从Mongo块还是从redis块
	 * @since 2017年12月1日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testRedisSpeed(){
		List<String> list = new ArrayList<>();
		list.add("872316726270754816");
		list.add("872316726547578880");
		list.add("872316726786654208");
		list.add("872316726925066240");
		list.add("872316727118004224");
		list.add("872316727273193472");
		list.add("872316727289970688");
		list.add("872316727327719424");
		list.add("872316731207450624");
		list.add("872316731639463936");
		
		long time1 = System.currentTimeMillis();
		productResp.findProductsByIds(list);
		
//		brandResp.findById(870);
		long time2 = System.currentTimeMillis();
//		brandResp.findById(870);

		list.add("872316731970813952");
		list.add("872316731991785472");
		list.add("872316732067282944");
		list.add("872316732067282945");
		list.add("872316732184723456");
		list.add("872316732218277888");
		list.add("872316732520267776");
		list.add("872316732608348160");
		list.add("872316732679651328");
		list.add("872316732755148800");
		


		list.add("872316733447208960");
		list.add("872316733505929216");
		list.add("872316733807919104");
		list.add("872316733665312768");
		list.add("872316733900193792");
		list.add("872316733925359616");
		list.add("872316733937942528");
		list.add("872316733963108352");
		list.add("872316734055383040");
		list.add("872316733988274176");
		productResp.findProductsByIds(list);
		long time5 = System.currentTimeMillis();
		
		//存测试数据
//		Pageable pageable = new ProductPageable(1);
//		Page<Product> page = productResp.findAll(pageable);
//		do{
//			if(pageable.getPageNumber()>1)
//				page = productResp.findAll(pageable);
//			for(Product p:page.getContent()){
//				aliasCategoryOps.put("testTemp", p.getId(), p);
//			}
//			if(page.getNumber()>1000)
//				break;
//			System.out.println("当前已存数据数："+page.getNumber()*page.getSize());
//		}while((pageable = page.nextPageable())!=null);
		
		
		long time3 = System.currentTimeMillis();
		aliasCategoryOps.multiGet("testTemp", list);
		long time4 = System.currentTimeMillis();
		
		System.out.println("mongodb批量获取数据耗时："+(time2-time1)+" "+(time5-time2)+" redis批量获取数据耗时："+(time4-time3));
	}
	
	/**
	 * 测试一下是否可以使用注入的方式使用原生api
	 * @since 2017年12月7日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testClient(){
		System.out.println(pc.getCollection().count());
	}
	
	
	@Test
	public void testRedisOutOfMemory(){
		List<String> keys = new ArrayList<>();
		keys.add(bm.getAliasKey(null, "PANJIT"));
		keys.add(bm.getAliasKey(null, "强茂"));
		keys.add(bm.getAliasKey(null, "PHC"));
		keys.add(bm.getAliasKey(null, "CONCEPT TECHNOLOGIE (POWER INTEGRATIONS)"));
		keys.add(bm.getAliasKey(null, "电源集成"));
		keys.add(bm.getAliasKey(null, "CONCEPT TECHNOLOGIE / POWER INTEGRATIONS"));
		keys.add(bm.getAliasKey(null, "POWER INTEGRATIONS"));
		keys.add(bm.getAliasKey(null, "PROGRESSIVE"));
		keys.add(bm.getAliasKey(null, "PRONIC"));
		keys.add(bm.getAliasKey(null, "普尼克"));
		keys.add(bm.getAliasKey(null, "PULSELARSEN ANTENNAS"));
		keys.add(bm.getAliasKey(null, "普思"));
		keys.add(bm.getAliasKey(null, "PULSE ELECTRONICS CORPORATION"));
		keys.add(bm.getAliasKey(null, "PULSE ELECTRONICS NETWORK"));
		keys.add(bm.getAliasKey(null, "PULSE ENGINEERING"));
		keys.add(bm.getAliasKey(null, "PULSE ELECTRONICS"));
		keys.add(bm.getAliasKey(null, "PULSE ELECTRONICS POWER"));
		keys.add(bm.getAliasKey(null, "QSPEED SEMICONDUCTOR"));
		keys.add(bm.getAliasKey(null, "AIRPAX SENATA"));
		keys.add(bm.getAliasKey(null, "SINGATRON"));
		keys.add(bm.getAliasKey(null, "3M PELTOR"));
		keys.add(bm.getAliasKey(null, "ESSAILEC"));
		keys.add(bm.getAliasKey(null, "ACCELERATED DESIGNS"));
		keys.add(bm.getAliasKey(null, "ACLSTATICIDE"));
		keys.add(bm.getAliasKey(null, "ADAFRUIT INDUSTRIES"));
		keys.add(bm.getAliasKey(null, "ADESTO TECHNOLOGIES"));
		keys.add(bm.getAliasKey(null, "ADVANCED SENSORS / AMPHENOL"));
		keys.add(bm.getAliasKey(null, "ADVANTECH"));
		keys.add(bm.getAliasKey(null, "AGILENT"));
		keys.add(bm.getAliasKey(null, "SPECSENSORS"));
		for(int i = 0;i < 100;i++){
			new Thread(){

				@Override
				public void run() {
					Map<String,ProductBrand> list = bm.getBrandByAliasName(keys);
					System.out.println(list.size());
					super.run();
				}
				
			}.start();
		}
	}
	
	/**
	 * 测试执行mongo脚本
	 * @since 2017年12月1日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testExecMongojs(){
		try {
			Document result = pc.getDatabase().runCommand(new Document("eval","IdGen(0)"));
			Assert.assertEquals(1.0, result.get("ok"));
			Assert.assertNotNull(result.get("retval"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试原生api查询写入性能
	 * @since 2017年12月1日
	 * @author tongkun@yikuyi.com
	 */
	@SuppressWarnings("unused")
	@Test
	public void testBulkSaveOrBulkUpdate(){
		List<WriteModel<Document>> savelist = new ArrayList<>();
		List<WriteModel<Document>> updatelist = new ArrayList<>();
		List<String> qflist = new ArrayList<>();
		List<String> idlist = new ArrayList<>();
		MongoDatabase database = pc.getDatabase();
		MongoCollection<Document> collections = database.getCollection("product");
		MongoCollection<Document> tempcol = database.getCollection("product_junit_test");
		//测试数据查询1000条数据
		FindIterable<Document> result = collections.find().limit(1000);
		for(Document d:result){
			qflist.add(d.getString("quickFindKey"));
			idlist.add(d.getString("_id"));
			savelist.add(new ReplaceOneModel<>(new Document("_id",d.get("_id")),d,new UpdateOptions().upsert(true)));
			updatelist.add(new UpdateOneModel<>(new Document("_id",d.get("_id")), new Document("$set",new Document("prices",d.get("prices"))
					.append("vendorName", d.get("vendorName")).append("updatedTimeMillis", d.get("updatedTimeMillis")))));
		}
		
		//游标查询
		long time1 = System.currentTimeMillis();
		result = collections.find().limit(1000);
		for(Document d:result){}
		long time2 = System.currentTimeMillis();
		System.out.println("游标查询1000条耗费时间："+(time2-time1));
		
		//quickFindKey查询
		time1 = System.currentTimeMillis();
		result = collections.find(new Document("quickFindKey",new Document("$in",qflist))).hint(new Document("quickFindKey","hashed"));
		for(Document d:result){}
		time2 = System.currentTimeMillis();
		System.out.println("quickFindKey准确查询1000条耗费时间："+(time2-time1));

		//id查询
		time1 = System.currentTimeMillis();
		result = collections.find(new Document("_id",new Document("$in",idlist)));
		for(Document d:result){}
		time2 = System.currentTimeMillis();
		System.out.println("id准确查询1000条耗费时间："+(time2-time1));
		
		//保存到临时表中
		time1 = System.currentTimeMillis();
		tempcol.bulkWrite(savelist);
		time2 = System.currentTimeMillis();
		System.out.println("保存1000条耗费时间："+(time2-time1));
		
		//更新到临时表中
		time1 = System.currentTimeMillis();
		tempcol.bulkWrite(updatelist);
		time2 = System.currentTimeMillis();
		System.out.println("更新1000条耗费时间："+(time2-time1));
	}

	/**
	 * 为以下单元测试临时创建的
	 * @author tongkun@yikuyi.com
	 * @version 1.0.0
	 */
	@org.springframework.data.mongodb.core.mapping.Document(collection = "product_junit_test")
	public class ProductJunitTest extends Product{
		private static final long serialVersionUID = 154235491062162058L;
	}
	
	/**
	 * 测试MongoOperations的批量读写性能
	 * @since 2017年12月1日
	 * @author tongkun@yikuyi.com
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@Test
	public void testBulkSaveOrBulkUpdateByOp(){
		List<Pair<Query, Update>> savelist = new ArrayList<>();
		List<Pair<Query, Update>> updatelist = new ArrayList<>();
		List<String> qflist = new ArrayList<>();
		List<String> idlist = new ArrayList<>();
		//测试数据查询1000条数据
		List<Product> result = mo.find(new Query().limit(1000), Product.class);
		for(Product p:result){
			qflist.add(p.getQuickFindKey());
			idlist.add(p.getId());
			Update saveUp = new Update();
			ObjectMapper om = new ObjectMapper();
			Map map = om.convertValue(p, Map.class);
			for(Object key:map.keySet()){
				if("_id".equals(key))
					continue;
				saveUp.set(key.toString(),map.get(key));
			}
			savelist.add(Pair.of(query(where("_id").is(p.getId())), saveUp));
			updatelist.add(Pair.of(query(where("_id").is(p.getId())), new Update().set("vendorName", p.getVendorName())
					.set("updatedTimeMillis", p.getUpdatedTimeMillis())));
		}
		
		//游标查询
		long time1 = System.currentTimeMillis();
		result = mo.find(new Query().limit(1000),Product.class);
		for(Product p:result){}
		long time2 = System.currentTimeMillis();
		System.out.println("游标查询1000条耗费时间："+(time2-time1));
		
		//quickFindKey查询
		time1 = System.currentTimeMillis();
		result = mo.find(query(where("quickFindKey").in(qflist)), Product.class);
		for(Product p:result){}
		time2 = System.currentTimeMillis();
		System.out.println("quickFindKey准确查询1000条耗费时间："+(time2-time1));

		//id查询
		time1 = System.currentTimeMillis();
		result = mo.find(query(where("_id").in(idlist)), Product.class);
		for(Product p:result){}
		time2 = System.currentTimeMillis();
		System.out.println("id准确查询1000条耗费时间："+(time2-time1));
		
		//保存到临时表中
		time1 = System.currentTimeMillis();
		BulkOperations bo = mo.bulkOps(BulkMode.ORDERED, ProductJunitTest.class);
		bo.upsert(savelist);
		bo.execute();
		time2 = System.currentTimeMillis();
		System.out.println("保存1000条耗费时间："+(time2-time1));
		
		//更新到临时表中
		time1 = System.currentTimeMillis();
		bo = mo.bulkOps(BulkMode.ORDERED, ProductJunitTest.class);
		bo.updateOne(updatelist);
		bo.execute();
		time2 = System.currentTimeMillis();
		System.out.println("更新1000条耗费时间："+(time2-time1));
	}
	
		
}
