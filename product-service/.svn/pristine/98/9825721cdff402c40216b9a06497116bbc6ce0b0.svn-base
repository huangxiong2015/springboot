package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.activity.vo.MockActivityDataVo;

/**
 * @author 
 * 活动dao
 */
@Mapper
public interface MockActivityDataDao {
  
	int save(MockActivityDataVo dataVo);
    
    public List<MockActivityDataVo> listGift(RowBounds rowBounds,@Param("mockType")String mockType);
}
