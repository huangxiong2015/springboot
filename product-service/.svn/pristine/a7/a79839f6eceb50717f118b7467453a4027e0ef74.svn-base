function() {
    var cateMap = searchAllProductCategory();//获得全部的分类
    var brandMap = searchAllProductBrand();//获得全部的品牌
    var pageMax = 1000;
    var lastId = null;
    var param = {};
    if(lastId){
         param._id=null;
    }
    var count = db.digikey_temp.find(param).count();
    var page = 0;
    while((page*pageMax)<=count){
        var date1 = Date.now();
        lastId = aggregateProductData(pageMax,lastId,cateMap,brandMap);
        var date2 = Date.now();
        page++;
        print((page*pageMax)+'/'+(count)+' processed last id:'+lastId+' time:'+(date2-date1));
    }
}