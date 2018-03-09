//搜索产品分类
function (vendorCates,cateMap){
    //没有参数则不返回
    if(!vendorCates)
        return [];
    var result = [];
    var cateStr = '';
    var lastCateStr = '';
    for(var i = 0;i < vendorCates.length;i++){
        cateStr = cateStr+vendorCates[i].name;
        if(vendorCates[i].level==2){
            lastCateStr = vendorCates[i].name;
        }
    }
    //查询并返回
    var lastCateVo = cateMap.aMap[cateStr.toUpperCase()];
    if(!lastCateVo){
        lastCateVo = cateMap.aMap[lastCateStr.toUpperCase()];
    }
    var level3Cate = null;
    var level2Cate = null;
    var level1Cate = null;
    if(lastCateVo){
        if(lastCateVo.cateLevel==3)
            level3Cate = lastCateVo;
        if(lastCateVo.cateLevel==2)
            level2Cate = lastCateVo;
        if(lastCateVo.cateLevel==1)
            level1Cate = lastCateVo;
        if(level3Cate)
            level2Cate = cateMap.idMap[level3Cate.parent.$id];
        if(level2Cate)
            level1Cate = cateMap.idMap[level2Cate.parent.$id];
        if(level1Cate)
            result.push({
                _id:level1Cate._id,
                cateName:level1Cate.cateName,
                status:level1Cate.status,
                cateLevel:level1Cate.cateLevel
            });
        if(level2Cate)
            result.push({
                _id:level2Cate._id,
                cateName:level2Cate.cateName,
                status:level2Cate.status,
                cateLevel:level2Cate.cateLevel
            });
        if(level3Cate)
            result.push({
                _id:level3Cate._id,
                cateName:level3Cate.cateName,
                status:level3Cate.status,
                cateLevel:level3Cate.cateLevel
            });
    }
    
    return result;
}