/*
 * Created: 2015年10月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.api.persist;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.ykyframework.model.Entity;

/**
 * DAO的标准接口类，定义了常用的增删改查方法
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @param <T>
 */
public interface EntityDAO<T extends Entity> {
	/**
	 * 根据主键查找实体的唯一记录。
	 * @param id 主键信息
	 * @return
	 * @author liaoke@yikuyi.com
	 */
	public T getById(Serializable id);
	
	/**
	 * 查询所有实体信息。
	 * @return
	 * @author liaoke@yikuyi.com
	 */
	public List<T> findAll(RowBounds rowBounds);
	
	
	/**
	 * 根据实体部分字段的值，进行组合条件的查询
	 * @param entity
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author liaoke@yikuyi.com
	 */
	public List<T> findByEntity(T entity, RowBounds rowBounds);
	
	/**
	 * 增加一条实体记录。
	 * @return 返回添加成功后的实体信息，主要包含主键信息。
	 * @author liaoke@yikuyi.com
	 */
	public void add(T entity);
	
	/**
	 * 更新一条实体记录。
	 * @param entity
	 * @author liaoke@yikuyi.com
	 */
	public void update(T entity);
	
	/**
	 * 根据主键删除一条实体记录。
	 * @param id 主键信息
	 * @author liaoke@yikuyi.com
	 */
	public void deleteById(Serializable id);

}
