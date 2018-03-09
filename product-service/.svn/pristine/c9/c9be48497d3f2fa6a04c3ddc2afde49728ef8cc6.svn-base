package com.yikuyi.product.common.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * mapreduce的执行类
 * @author tongkun@yikuyi.com
 */
@Service
public class MapReduceUtil{
	
	private MapReduceUtil(){
		super();
	}
	
	/**
	 * 执行一个mapreduce，并执行回调方法
	 * @param mo 数据库连接
	 * @param condition
	 * @param collection
	 * @param groupBy
	 * @param callback
	 * @since 2018年1月25日
	 * @author tongkun@yikuyi.com
	 */
	public static void doMapReduce(MongoOperations mo,Criteria condition,String collection,GroupBy groupBy,MapReduceCallback callback){
		GroupByResults<Document> result = mo.group(condition,collection, groupBy, Document.class);
		callback.callback(result);
	}

}
